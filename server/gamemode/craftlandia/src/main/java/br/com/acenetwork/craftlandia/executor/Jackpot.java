package br.com.acenetwork.craftlandia.executor;

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
import java.util.List;
import java.util.ResourceBundle;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import com.google.common.io.ByteStreams;

import br.com.acenetwork.commons.CommonsUtil;
import br.com.acenetwork.commons.manager.InsufficientBalanceException;
import br.com.acenetwork.commons.player.CommonPlayer;
import br.com.acenetwork.commons.player.craft.CraftCommonPlayer;
import br.com.acenetwork.craftlandia.Main;
import br.com.acenetwork.craftlandia.inventory.JackpotGUI;
import br.com.acenetwork.craftlandia.inventory.JackpotPercentage;
import br.com.acenetwork.craftlandia.item.VipItem;
import br.com.acenetwork.craftlandia.manager.Config;
import br.com.acenetwork.craftlandia.manager.Config.Type;
import br.com.acenetwork.craftlandia.manager.ItemSpecial;
import br.com.acenetwork.craftlandia.manager.JackpotType;
import net.citizensnpcs.api.npc.NPC;
import net.md_5.bungee.api.ChatColor;

public class Jackpot implements TabExecutor, Listener
{	
	public static final String JACKPOT_UUID = CommonsUtil.getRandomItemUUID();
	public static final String RANDOM_ITEM_UUID = CommonsUtil.getRandomItemUUID();
	public static final String SHARDS_UUID = CommonsUtil.getRandomItemUUID();
	public static final String VIP_UUID = CommonsUtil.getRandomItemUUID();
	public static final String $BTA_UUID = CommonsUtil.getRandomItemUUID();
	public static final String NONE_UUID = CommonsUtil.getRandomItemUUID();
	
	public static final double PERCENT = 0.2D; 
	private double jackpot;
	
	private static Jackpot instance;
	
	public static final int $BTA_TO_SHARDS = 200;
	
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
		
		JackpotType type = JackpotType.COAL;
		
		if(args.length == 1)
		{
			try
			{
				type = JackpotType.valueOf(args[0].toUpperCase());
			}
			catch(IllegalArgumentException e)
			{
				
			}
		}
		
		new JackpotPercentage(cp, type);
		
		return false;
	}
	
	public boolean run(CommonPlayer cp, NPC machine, JackpotType type)
	{
		ResourceBundle bundle = ResourceBundle.getBundle("message", cp.getLocale());
		Player p = cp.getPlayer();
		boolean inUse = machine.data().get("inUse");
		
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
			df.setGroupingSize(3);
			df.setGroupingUsed(true);
			
			cp.setBalance(cp.getBalance() - type.getBet());
			p.sendMessage(ChatColor.DARK_RED + "(-" + df.format(type.getBet()) + " SHARDS)");
		}
		catch(InsufficientBalanceException e)
		{
			p.sendMessage(ChatColor.RED + bundle.getString("commons.cmds.insufficient-balance"));
			return false;
		}
		
		setJackpotTotal(getJackpotTotal() + type.getBet());
		
		cp.setJackpoting(true);
		machine.data().set("inUse", true);
		new JackpotGUI(cp, machine, type);
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
		
		if(CommonsUtil.containsUUID(item, ItemSpecial.getInstance(VipItem.class).getUUID()))
		{
			return 10000.0D * item.getAmount();
		}
		
		return 0.0D;
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
}

