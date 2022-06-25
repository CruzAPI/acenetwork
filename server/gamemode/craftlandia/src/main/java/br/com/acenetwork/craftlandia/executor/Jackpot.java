package br.com.acenetwork.craftlandia.executor;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import javax.accessibility.AccessibleKeyBinding;

import java.util.ResourceBundle;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import br.com.acenetwork.commons.CommonsUtil;
import br.com.acenetwork.commons.manager.CommonsConfig;
import br.com.acenetwork.commons.manager.Message;
import br.com.acenetwork.commons.player.CommonPlayer;
import br.com.acenetwork.commons.player.craft.CraftCommonPlayer;
import br.com.acenetwork.craftlandia.event.SellItemEvent;
import br.com.acenetwork.craftlandia.inventory.JackpotGUI;
import br.com.acenetwork.craftlandia.inventory.JackpotPercentage;
import br.com.acenetwork.craftlandia.manager.AmountPrice;
import br.com.acenetwork.craftlandia.manager.Config;
import br.com.acenetwork.craftlandia.manager.Config.Type;
import br.com.acenetwork.craftlandia.manager.CryptoInfo;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.TranslatableComponent;

public class Jackpot implements TabExecutor
{
	public static final List<ItemStack> PRIZE_LIST = new ArrayList<>();
	public static final ItemStack JACKPOT_ITEM = new ItemStack(Material.BEACON);
	private static final String JACKPOT_UUID = CommonsUtil.getRandomItemUUID();
	public static final ItemStack NONE_ITEM = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14);
	public static final String SHARDS_UUID = CommonsUtil.getRandomItemUUID();
	
	private static int jackpot;
	
	public static final Map<ItemStack, Integer> MAP = new LinkedHashMap<>();
	
	public Jackpot()
	{
		File file = Config.getFile(Type.JACKPOT, true);
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		setJackpot(config.getInt("jackpot"));
		
		ItemMeta meta;
		
		PRIZE_LIST.addAll(Collections.nCopies(1, JACKPOT_ITEM));
		MAP.put(JACKPOT_ITEM, 1);
		
		ItemStack randomItem = new ItemStack(Material.COBBLESTONE);
		
		meta = randomItem.getItemMeta();
		meta.setDisplayName(ChatColor.WHITE + "Random Item");
		randomItem.setItemMeta(meta);
		
		PRIZE_LIST.addAll(Collections.nCopies(3000, randomItem.clone()));
		MAP.put(randomItem.clone(), 3000);
		
		ItemStack vip = new ItemStack(Material.WOOL, 1, (short) 5);
		
		meta = vip.getItemMeta();
		meta.setDisplayName("" + ChatColor.GREEN + ChatColor.BOLD + "VIP");
		vip.setItemMeta(meta);
		
		PRIZE_LIST.addAll(Collections.nCopies(30, vip.clone()));
		MAP.put(vip.clone(), 30);
		
		ItemStack nugget = new ItemStack(Material.GOLD_NUGGET);
		
		nugget.setAmount(1);
		meta = nugget.getItemMeta();
		meta.setDisplayName("" + ChatColor.GOLD + ChatColor.BOLD + "100 SHARDS" + SHARDS_UUID);
		nugget.setItemMeta(meta);
		
		PRIZE_LIST.addAll(Collections.nCopies(900, nugget.clone()));
		MAP.put(nugget.clone(), 900);

		nugget.setAmount(2);
		meta = nugget.getItemMeta();
		meta.setDisplayName("" + ChatColor.GOLD + ChatColor.BOLD + "200 SHARDS" + SHARDS_UUID);
		nugget.setItemMeta(meta);
		
		PRIZE_LIST.addAll(Collections.nCopies(1350, nugget.clone()));
		MAP.put(nugget.clone(), 1350);
		
		nugget.setAmount(3);
		meta = nugget.getItemMeta();
		meta.setDisplayName("" + ChatColor.GOLD + ChatColor.BOLD + "300 SHARDS" + SHARDS_UUID);
		nugget.setItemMeta(meta);
		
		PRIZE_LIST.addAll(Collections.nCopies(1800, nugget.clone()));
		MAP.put(nugget.clone(), 1800);
		
		nugget.setAmount(4);
		meta = nugget.getItemMeta();
		meta.setDisplayName("" + ChatColor.GOLD + ChatColor.BOLD + "400 SHARDS" + SHARDS_UUID);
		nugget.setItemMeta(meta);
		
		PRIZE_LIST.addAll(Collections.nCopies(2250, nugget.clone()));
		MAP.put(nugget.clone(), 2250);
		
		nugget.setAmount(5);
		meta = nugget.getItemMeta();
		meta.setDisplayName("" + ChatColor.GOLD + ChatColor.BOLD + "500 SHARDS" + SHARDS_UUID);
		nugget.setItemMeta(meta);
		
		PRIZE_LIST.addAll(Collections.nCopies(2700, nugget.clone()));
		MAP.put(nugget.clone(), 2700);
		
		
		
		ItemStack ingot = new ItemStack(Material.GOLD_INGOT);
		
		ingot.setAmount(1);
		meta = ingot.getItemMeta();
		meta.setDisplayName("" + ChatColor.GOLD + ChatColor.BOLD + "1,000 SHARDS" + SHARDS_UUID);
		ingot.setItemMeta(meta);
		
		PRIZE_LIST.addAll(Collections.nCopies(1800, ingot.clone()));
		MAP.put(ingot.clone(), 1800);
		
		ingot.setAmount(2);
		meta = ingot.getItemMeta();
		meta.setDisplayName("" + ChatColor.GOLD + ChatColor.BOLD + "2,000 SHARDS" + SHARDS_UUID);
		ingot.setItemMeta(meta);
		
		PRIZE_LIST.addAll(Collections.nCopies(1500, ingot.clone()));
		MAP.put(ingot.clone(), 1500);
		
		ingot.setAmount(3);
		meta = ingot.getItemMeta();
		meta.setDisplayName("" + ChatColor.GOLD + ChatColor.BOLD + "3,000 SHARDS" + SHARDS_UUID);
		ingot.setItemMeta(meta);
		
		PRIZE_LIST.addAll(Collections.nCopies(1200, ingot.clone()));
		MAP.put(ingot.clone(), 1200);
		
		ingot.setAmount(4);
		meta = ingot.getItemMeta();
		meta.setDisplayName("" + ChatColor.GOLD + ChatColor.BOLD + "4,000 SHARDS" + SHARDS_UUID);
		ingot.setItemMeta(meta);
		
		PRIZE_LIST.addAll(Collections.nCopies(900, ingot.clone()));
		MAP.put(ingot.clone(), 900);
		
		ingot.setAmount(5);
		meta = ingot.getItemMeta();
		meta.setDisplayName("" + ChatColor.GOLD + ChatColor.BOLD + "5,000 SHARDS" + SHARDS_UUID);
		ingot.setItemMeta(meta);
		
		PRIZE_LIST.addAll(Collections.nCopies(600, ingot.clone()));
		MAP.put(ingot.clone(), 600);
		
		
		
		ItemStack block = new ItemStack(Material.GOLD_BLOCK);
		
		block.setAmount(1);
		meta = block.getItemMeta();
		meta.setDisplayName("" + ChatColor.GOLD + ChatColor.BOLD + "10,000 SHARDS" + SHARDS_UUID);
		block.setItemMeta(meta);
		
		PRIZE_LIST.addAll(Collections.nCopies(120, block.clone()));
		MAP.put(block.clone(), 120);
		
		block.setAmount(2);
		meta = block.getItemMeta();
		meta.setDisplayName("" + ChatColor.GOLD + ChatColor.BOLD + "20,000 SHARDS" + SHARDS_UUID);
		block.setItemMeta(meta);
		
		PRIZE_LIST.addAll(Collections.nCopies(90, block.clone()));
		MAP.put(block.clone(), 90);
		
		block.setAmount(3);
		meta = block.getItemMeta();
		meta.setDisplayName("" + ChatColor.GOLD + ChatColor.BOLD + "30,000 SHARDS" + SHARDS_UUID);
		block.setItemMeta(meta);
		
		PRIZE_LIST.addAll(Collections.nCopies(45, block.clone()));
		MAP.put(block.clone(), 45);
		
		block.setAmount(4);
		meta = block.getItemMeta();
		meta.setDisplayName("" + ChatColor.GOLD + ChatColor.BOLD + "40,000 SHARDS" + SHARDS_UUID);
		block.setItemMeta(meta);
		
		PRIZE_LIST.addAll(Collections.nCopies(30, block.clone()));
		MAP.put(block.clone(), 30);
		
		block.setAmount(5);
		meta = block.getItemMeta();
		meta.setDisplayName("" + ChatColor.GOLD + ChatColor.BOLD + "50,000 SHARDS" + SHARDS_UUID);
		block.setItemMeta(meta);
		
		PRIZE_LIST.addAll(Collections.nCopies(15, block.clone()));
		MAP.put(block.clone(), 15);
		
		Bukkit.getConsoleSender().sendMessage("PRIZE_LIST " + PRIZE_LIST.size());
		
		meta = NONE_ITEM.getItemMeta();
		meta.setDisplayName("" + ChatColor.RED + "None" + CommonsUtil.getRandomItemUUID());
		NONE_ITEM.setItemMeta(meta);
		
		int nCopies = Math.max(0, 30000 - PRIZE_LIST.size());
		PRIZE_LIST.addAll(Collections.nCopies(nCopies, NONE_ITEM));
		MAP.put(NONE_ITEM, nCopies);
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
			
			Collections.shuffle(PRIZE_LIST);
			
			setJackpot(getJackpot() + 1000);
			
			Bukkit.broadcastMessage("jackpot = " + jackpot);
			
			cp.setJackpoting(true);
			new JackpotGUI(cp, PRIZE_LIST);
		}
		else
		{
			new JackpotPercentage(cp);
//			TextComponent[] extra = new TextComponent[1];
//			
//			extra[0] = new TextComponent("/" + aliases);
//			
//			TextComponent text = Message.getTextComponent(bundle.getString("commons.cmds.wrong-syntax-try"), extra);
//			text.setColor(ChatColor.RED);
//			p.spigot().sendMessage(text);
		}

		return false;
	}
	
	public static int getJackpot()	
	{
		return jackpot;
	}
	
	public static void setJackpot(int jackpot)
	{
		Jackpot.jackpot = jackpot;
		File file = Config.getFile(Type.JACKPOT, true);
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		
		DecimalFormat df = new DecimalFormat();
		df.setGroupingSize(3);
		df.setGroupingUsed(true);
		
		ItemMeta meta;
		
		if(jackpot > 0)
		{
			JACKPOT_ITEM.setType(Material.BEACON);
			meta = JACKPOT_ITEM.getItemMeta();
			meta.setDisplayName("" + ChatColor.AQUA + ChatColor.BOLD + "JACKPOT" + JACKPOT_UUID);
			meta.setLore(Arrays.asList(ChatColor.GRAY + "Prize: " + ChatColor.WHITE + df.format(jackpot) + " SHARDS"));
			JACKPOT_ITEM.setItemMeta(meta);
		}
		else
		{
			CommonsUtil.setItemCopyOf(JACKPOT_ITEM, NONE_ITEM);
		}
		
		config.set("jackpot", jackpot);
		
		try
		{
			config.save(file);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}

