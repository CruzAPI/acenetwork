package br.com.acenetwork.craftlandia.executor;

import static br.com.acenetwork.craftlandia.manager.JackpotItem.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

import com.google.common.io.ByteStreams;

import br.com.acenetwork.commons.CommonsUtil;
import br.com.acenetwork.commons.manager.InsufficientBalanceException;
import br.com.acenetwork.commons.player.CommonPlayer;
import br.com.acenetwork.commons.player.craft.CraftCommonPlayer;
import br.com.acenetwork.craftlandia.Main;
import br.com.acenetwork.craftlandia.inventory.JackpotGUI;
import br.com.acenetwork.craftlandia.inventory.JackpotPercentage;
import br.com.acenetwork.craftlandia.manager.Config;
import br.com.acenetwork.craftlandia.manager.Config.Type;
import net.citizensnpcs.api.CitizensAPI;
import net.md_5.bungee.api.ChatColor;

public class Jackpot implements TabExecutor, Listener
{
	public static final String JACKPOT_UUID = CommonsUtil.getRandomItemUUID();
	public static final String RANDOM_ITEM_UUID = CommonsUtil.getRandomItemUUID();
	public static final String SHARDS_UUID = CommonsUtil.getRandomItemUUID();
	public static final String VIP_UUID = CommonsUtil.getRandomItemUUID();
	public static final String $BTA_UUID = CommonsUtil.getRandomItemUUID();
	public static final String NONE_UUID = CommonsUtil.getRandomItemUUID();
	public static final double COAL_BET = 1000.0D;
	
	public static final double PERCENT = 0.25D; 
	private double jackpot;
	
	private static Jackpot instance;
	
	private boolean inUse;
	
	public static final int $BTA_TO_SHARDS = 200;
	
	public static final Map<Byte, Integer> COAL_MAP = new LinkedHashMap<>();
	public static final double BTA_BET_MULTIPLIER = 0.001D;
	
	public Jackpot()
	{
		instance = this;
		
		File file = Config.getFile(Type.JACKPOT, false);
		
		if(file.exists() && file.length() > 0L)
		{
			try(FileInputStream fileIn = new FileInputStream(file);
					ByteArrayInputStream streamIn = new ByteArrayInputStream(ByteStreams.toByteArray(fileIn));
					DataInputStream in = new DataInputStream(streamIn))
			{
				setJackpotTotal(in.readDouble());
			}
			catch(IOException e)
			{
				throw new RuntimeException(e);
			}
		}
		else
		{
			setJackpotTotal(0.0D);
		}
		
		int size = 0;
		final int maxSize = 32000;
		
		COAL_MAP.put(JACKPOT.getId(), -size + (size += 1));
		COAL_MAP.put(VIP.getId(), -size + (size += 32));
		COAL_MAP.put(RANDOM_ITEM.getId(), -size + (size += 6400));
		COAL_MAP.put(NUGGET_1.getId(), -size + (size += 1536));
		COAL_MAP.put(NUGGET_2.getId(), -size + (size += 1792));
		COAL_MAP.put(NUGGET_3.getId(), -size + (size += 2048));
		COAL_MAP.put(NUGGET_4.getId(), -size + (size += 2304));
		COAL_MAP.put(NUGGET_5.getId(), -size + (size += 2560));
		COAL_MAP.put(INGOT_1.getId(), -size + (size += 1280));
		COAL_MAP.put(INGOT_2.getId(), -size + (size += 1024));
		COAL_MAP.put(INGOT_3.getId(), -size + (size += 768));
		COAL_MAP.put(INGOT_4.getId(), -size + (size += 512));
		COAL_MAP.put(INGOT_5.getId(), -size + (size += 256));
		COAL_MAP.put(BLOCK_1.getId(), -size + (size += 64));
		COAL_MAP.put(BLOCK_2.getId(), -size + (size += 32));
		COAL_MAP.put(BLOCK_3.getId(), -size + (size += 16));
		COAL_MAP.put(BLOCK_4.getId(), -size + (size += 8));
		COAL_MAP.put(BLOCK_5.getId(), -size + (size += 4));
		COAL_MAP.put($BTA_1.getId(), -size + (size += 3200));
		COAL_MAP.put($BTA_3.getId(), -size + (size += 1600));
		COAL_MAP.put($BTA_5.getId(), -size + (size += 800));
		COAL_MAP.put(NONE.getId(), -size + (size += Math.max(0, maxSize - size)));
		
		Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String aliases, String[] args)
	{
		return new ArrayList<>();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String aliases, String[] args)
	{
		ResourceBundle bundle = ResourceBundle.getBundle("message");
		
		if(!(sender instanceof Player))
		{
			sender.sendMessage(ChatColor.RED + bundle.getString("commons.cmds.cant-perform-command"));
			return true;
		}
		
		Player p = (Player) sender;
		CommonPlayer cp = CraftCommonPlayer.get(p);
		new JackpotPercentage(cp, COAL_BET, COAL_MAP);
		
		return false;
	}
	
	@EventHandler
	public void a(PlayerInteractEntityEvent e)
	{
		Player p = e.getPlayer();
		
		Entity clicked = e.getRightClicked();
		
		if(CitizensAPI.getNPCRegistry().isNPC(clicked) && clicked.getName().contains("CLICK TO PLAY"))
		{
			run(CraftCommonPlayer.get(p));
		}
	}
	
	private boolean run(CommonPlayer cp)
	{
		ResourceBundle bundle = ResourceBundle.getBundle("message", cp.getLocale());
		Player p = cp.getPlayer();
		
		if(inUse)
		{
			p.sendMessage(ChatColor.AQUA + "[" + bundle.getString("noun.jackpot").toUpperCase() + "] " 
					+ ChatColor.RED + bundle.getString("cmd.jackpot.occupied-machine"));
			return false;
		}
		
		if(cp.isJackpoting())
		{
			return false;
		}
		
		try
		{
			DecimalFormat df = new DecimalFormat("#.##", new DecimalFormatSymbols(cp.getLocale()));
			
			cp.setBalance(cp.getBalance() - COAL_BET);
			p.sendMessage(ChatColor.DARK_RED + "(-" + df.format(COAL_BET) + " SHARDS)");
		}
		catch(InsufficientBalanceException e)
		{
			p.sendMessage(ChatColor.RED + bundle.getString("commons.cmds.insufficient-balance"));
			return false;
		}
		
		setJackpotTotal(getJackpotTotal() + COAL_BET);
		
		cp.setJackpoting(true);
		inUse = true;
		new JackpotGUI(cp, COAL_BET, COAL_MAP);
		return true;
	}
	
	public double getJackpotTotal()	
	{
		return jackpot;
	}
	
	public double getJackpotPrize()	
	{
		return jackpot * PERCENT;
	}
	
	public void setJackpotTotal(double jackpot)
	{
		this.jackpot = jackpot;
	}
	
	public void save()
	{
		File file = Config.getFile(Type.JACKPOT, true);
		
		try(FileOutputStream fileOut = new FileOutputStream(file);
				ByteArrayOutputStream streamOut = new ByteArrayOutputStream();
				DataOutputStream out = new DataOutputStream(streamOut))
		{
			out.writeDouble(jackpot);
			fileOut.write(streamOut.toByteArray());
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
			return;
		}
	}
	
	public static Jackpot getInstance()
	{
		return instance;
	}
	
	public static double getValueInShards(double bet, ItemStack item)
	{
		if(CommonsUtil.containsUUID(item, Jackpot.JACKPOT_UUID))
		{
			return Math.max(0.0D, instance.getJackpotPrize());
		}
		
		if(CommonsUtil.containsUUID(item, Jackpot.SHARDS_UUID))
		{
			return bet * getMultiplier(item);
		}
		
		if(CommonsUtil.containsUUID(item, Jackpot.$BTA_UUID))
		{
			return bet * getMultiplier(item) * $BTA_TO_SHARDS;
		}
		
		if(CommonsUtil.containsUUID(item, Jackpot.VIP_UUID))
		{
			return 10000;
		}
		
		return 0;
	}
	
	public static double getValueInShardsTheoretically(double bet, ItemStack item)
	{
		if(CommonsUtil.containsUUID(item, JACKPOT_UUID))
		{
			return 0;
		}
		
		return getValueInShards(bet, item);
	}
	
	public static double getMultiplier(ItemStack item)
	{
		double multiplier;
		
		switch(item.getType())
		{
		case GOLD_BLOCK:
			multiplier = 10.0D;
			break;
		case GOLD_INGOT:
			multiplier = 1.0D;
			break;
		case GOLD_NUGGET:
			multiplier = 0.1D;
			break;
		case NETHER_STAR:
			multiplier = BTA_BET_MULTIPLIER;
			break;
		default:
			return 0.0D;
		}
		
		return multiplier * item.getAmount();
	}
	
	public void setInUse(boolean inUse)
	{
		this.inUse = inUse;
	}
	
	public boolean isInUse()
	{
		return inUse;
	}
}

