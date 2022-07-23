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
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.google.common.io.ByteStreams;

import br.com.acenetwork.commons.CommonsUtil;
import br.com.acenetwork.commons.manager.Message;
import br.com.acenetwork.commons.player.CommonPlayer;
import br.com.acenetwork.commons.player.craft.CraftCommonPlayer;
import br.com.acenetwork.craftlandia.inventory.JackpotGUI;
import br.com.acenetwork.craftlandia.inventory.JackpotPercentage;
import br.com.acenetwork.craftlandia.manager.Config;
import br.com.acenetwork.craftlandia.manager.Config.Type;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

public class Jackpot implements TabExecutor
{
	public static final String JACKPOT_UUID = CommonsUtil.getRandomItemUUID();
	public static final String RANDOM_ITEM_UUID = CommonsUtil.getRandomItemUUID();
	public static final String SHARDS_UUID = CommonsUtil.getRandomItemUUID();
	public static final String VIP_UUID = CommonsUtil.getRandomItemUUID();
	public static final String $BTA_UUID = CommonsUtil.getRandomItemUUID();
	public static final String NONE_UUID = CommonsUtil.getRandomItemUUID();
	
	public static final double PERCENT = 0.25D; 
	private double jackpot;
	
	private static Jackpot instance;
	
	public static final int $BTA_TO_SHARDS = 200;
	
	public static final Map<Byte, Integer> COAL_MAP = new LinkedHashMap<>();
	
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
				setJackpot(in.readDouble());
			}
			catch(IOException e)
			{
				throw new RuntimeException(e);
			}
		}
		else
		{
			setJackpot(0.0D);
		}
		
		int size = 0;
		final int maxSize = 30000;
		
		COAL_MAP.put(JACKPOT.getId(), -size + (size += 100000));
		COAL_MAP.put(VIP.getId(), -size + (size += 30));
		COAL_MAP.put(RANDOM_ITEM.getId(), -size + (size += 3000));
		COAL_MAP.put(NUGGET_1.getId(), -size + (size += 1800));
		COAL_MAP.put(NUGGET_2.getId(), -size + (size += 1800));
		COAL_MAP.put(NUGGET_3.getId(), -size + (size += 1800));
		COAL_MAP.put(NUGGET_4.getId(), -size + (size += 1800));
		COAL_MAP.put(NUGGET_5.getId(), -size + (size += 1800));
		COAL_MAP.put(INGOT_1.getId(), -size + (size += 1500));
		COAL_MAP.put(INGOT_2.getId(), -size + (size += 1200));
		COAL_MAP.put(INGOT_3.getId(), -size + (size += 900));
		COAL_MAP.put(INGOT_4.getId(), -size + (size += 600));
		COAL_MAP.put(INGOT_5.getId(), -size + (size += 300));
		COAL_MAP.put(BLOCK_1.getId(), -size + (size += 225));
		COAL_MAP.put(BLOCK_2.getId(), -size + (size += 30));
		COAL_MAP.put(BLOCK_3.getId(), -size + (size += 20));
		COAL_MAP.put(BLOCK_4.getId(), -size + (size += 15));
		COAL_MAP.put(BLOCK_5.getId(), -size + (size += 10));
		COAL_MAP.put($BTA_1.getId(), -size + (size += 900));
		COAL_MAP.put($BTA_3.getId(), -size + (size += 400));
		COAL_MAP.put($BTA_5.getId(), -size + (size += 200));
		COAL_MAP.put(NONE.getId(), -size + (size += Math.max(0, maxSize - size)));
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
		
		bundle = ResourceBundle.getBundle("message", cp.getLocale());
		
		if(args.length == 0)
		{
			if(cp.isJackpoting())
			{
				return true;
			}
			
			final double bet = 1000.0D;
			
			double newBalance = cp.getBalance() - bet;
			
			if(newBalance < 0.0D)
			{
				p.sendMessage(ChatColor.RED + bundle.getString("commons.cmds.insufficient-balance"));
				return true;
			}
			
			cp.setBalance(newBalance);
			setJackpot(getJackpot() + bet);
			
			cp.setJackpoting(true);
			new JackpotGUI(cp, COAL_MAP);
		}
		else if(args.length == 1 && args[0].equalsIgnoreCase("%"))
		{
			new JackpotPercentage(cp, COAL_MAP);
		}
		else
		{
			TextComponent[] extra = new TextComponent[1];
			
			extra[0] = new TextComponent("\n/" + aliases + "\n" + "/" + aliases + " %");
			
			TextComponent text = Message.getTextComponent(bundle.getString("commons.cmds.wrong-syntax-try"), extra);
			text.setColor(ChatColor.RED);
			p.spigot().sendMessage(text);
		}

		return false;
	}
	
	public double getJackpot()	
	{
		return jackpot;
	}
	
	public void setJackpot(double jackpot)
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
		if(CommonsUtil.compareUUID(item, Jackpot.JACKPOT_UUID))
		{
			return Math.max(0.0D, instance.getJackpot());
		}
		
		if(CommonsUtil.compareUUID(item, Jackpot.SHARDS_UUID))
		{
			return bet * getMultiplier(item);
		}
		
		if(CommonsUtil.compareUUID(item, Jackpot.$BTA_UUID))
		{
			return bet * getMultiplier(item) * $BTA_TO_SHARDS;
		}
		
		if(CommonsUtil.compareUUID(item, Jackpot.VIP_UUID))
		{
			return 10000;
		}
		
		return 0;
	}
	
	public static double getValueInShardsTheoretically(double bet, ItemStack item)
	{
		if(CommonsUtil.compareUUID(item, JACKPOT_UUID))
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
			multiplier = 0.001D;
			break;
		default:
			return 0.0D;
		}
		
		return multiplier * item.getAmount();
	}
}

