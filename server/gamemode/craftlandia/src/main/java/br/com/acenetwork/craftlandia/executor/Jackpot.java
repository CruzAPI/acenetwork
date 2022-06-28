package br.com.acenetwork.craftlandia.executor;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import br.com.acenetwork.commons.CommonsUtil;
import br.com.acenetwork.commons.player.CommonPlayer;
import br.com.acenetwork.commons.player.craft.CraftCommonPlayer;
import br.com.acenetwork.craftlandia.inventory.JackpotGUI;
import br.com.acenetwork.craftlandia.inventory.JackpotPercentage;
import br.com.acenetwork.craftlandia.manager.Config;
import br.com.acenetwork.craftlandia.manager.Config.Type;
import net.md_5.bungee.api.ChatColor;

public class Jackpot implements TabExecutor
{
	public static final List<ItemStack> PRIZE_LIST = new ArrayList<>();
	public static final ItemStack JACKPOT_ITEM = new ItemStack(Material.BEACON);
	private static final String JACKPOT_UUID = CommonsUtil.getRandomItemUUID();
	public static final ItemStack NONE_ITEM = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14);
	public static final String SHARDS_UUID = CommonsUtil.getRandomItemUUID();
	public static final String VIP_UUID = CommonsUtil.getRandomItemUUID();
	public static final String $BTA_UUID = CommonsUtil.getRandomItemUUID();
	
	private static int jackpot;
	private static final int $BTA_TO_SHARDS = 200;
	
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
		meta.setDisplayName("" + ChatColor.GREEN + ChatColor.BOLD + "VIP" + VIP_UUID);
		vip.setItemMeta(meta);
		
		PRIZE_LIST.addAll(Collections.nCopies(30, vip.clone()));
		MAP.put(vip.clone(), 30);
		
		ItemStack nugget = new ItemStack(Material.GOLD_NUGGET);
		
		nugget.setAmount(1);
		meta = nugget.getItemMeta();
		meta.setDisplayName("" + ChatColor.GOLD + ChatColor.BOLD + "100 SHARDS" + SHARDS_UUID);
		nugget.setItemMeta(meta);
		
		PRIZE_LIST.addAll(Collections.nCopies(1800, nugget.clone()));
		MAP.put(nugget.clone(), 1800);

		nugget.setAmount(2);
		meta = nugget.getItemMeta();
		meta.setDisplayName("" + ChatColor.GOLD + ChatColor.BOLD + "200 SHARDS" + SHARDS_UUID);
		nugget.setItemMeta(meta);
		
		PRIZE_LIST.addAll(Collections.nCopies(1800, nugget.clone()));
		MAP.put(nugget.clone(), 1800);
		
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
		
		PRIZE_LIST.addAll(Collections.nCopies(1800, nugget.clone()));
		MAP.put(nugget.clone(), 1800);
		
		nugget.setAmount(5);
		meta = nugget.getItemMeta();
		meta.setDisplayName("" + ChatColor.GOLD + ChatColor.BOLD + "500 SHARDS" + SHARDS_UUID);
		nugget.setItemMeta(meta);
		
		PRIZE_LIST.addAll(Collections.nCopies(1800, nugget.clone()));
		MAP.put(nugget.clone(), 1800);
		
		
		
		ItemStack ingot = new ItemStack(Material.GOLD_INGOT);
		
		ingot.setAmount(1);
		meta = ingot.getItemMeta();
		meta.setDisplayName("" + ChatColor.GOLD + ChatColor.BOLD + "1,000 SHARDS" + SHARDS_UUID);
		ingot.setItemMeta(meta);
		
		PRIZE_LIST.addAll(Collections.nCopies(1500, ingot.clone()));
		MAP.put(ingot.clone(), 1500);
		
		ingot.setAmount(2);
		meta = ingot.getItemMeta();
		meta.setDisplayName("" + ChatColor.GOLD + ChatColor.BOLD + "2,000 SHARDS" + SHARDS_UUID);
		ingot.setItemMeta(meta);
		
		PRIZE_LIST.addAll(Collections.nCopies(1200, ingot.clone()));
		MAP.put(ingot.clone(), 1200);
		
		ingot.setAmount(3);
		meta = ingot.getItemMeta();
		meta.setDisplayName("" + ChatColor.GOLD + ChatColor.BOLD + "3,000 SHARDS" + SHARDS_UUID);
		ingot.setItemMeta(meta);
		
		PRIZE_LIST.addAll(Collections.nCopies(900, ingot.clone()));
		MAP.put(ingot.clone(), 900);
		
		ingot.setAmount(4);
		meta = ingot.getItemMeta();
		meta.setDisplayName("" + ChatColor.GOLD + ChatColor.BOLD + "4,000 SHARDS" + SHARDS_UUID);
		ingot.setItemMeta(meta);
		
		PRIZE_LIST.addAll(Collections.nCopies(600, ingot.clone()));
		MAP.put(ingot.clone(), 600);
		
		ingot.setAmount(5);
		meta = ingot.getItemMeta();
		meta.setDisplayName("" + ChatColor.GOLD + ChatColor.BOLD + "5,000 SHARDS" + SHARDS_UUID);
		ingot.setItemMeta(meta);
		
		PRIZE_LIST.addAll(Collections.nCopies(300, ingot.clone()));
		MAP.put(ingot.clone(), 300);
		
		
		
		ItemStack block = new ItemStack(Material.GOLD_BLOCK);
		
		block.setAmount(1);
		meta = block.getItemMeta();
		meta.setDisplayName("" + ChatColor.GOLD + ChatColor.BOLD + "10,000 SHARDS" + SHARDS_UUID);
		block.setItemMeta(meta);
		
		PRIZE_LIST.addAll(Collections.nCopies(225, block.clone()));
		MAP.put(block.clone(), 225);
		
		block.setAmount(2);
		meta = block.getItemMeta();
		meta.setDisplayName("" + ChatColor.GOLD + ChatColor.BOLD + "20,000 SHARDS" + SHARDS_UUID);
		block.setItemMeta(meta);
		
		PRIZE_LIST.addAll(Collections.nCopies(30, block.clone()));
		MAP.put(block.clone(), 30);
		
		block.setAmount(3);
		meta = block.getItemMeta();
		meta.setDisplayName("" + ChatColor.GOLD + ChatColor.BOLD + "30,000 SHARDS" + SHARDS_UUID);
		block.setItemMeta(meta);
		
		PRIZE_LIST.addAll(Collections.nCopies(20, block.clone()));
		MAP.put(block.clone(), 20);
		
		block.setAmount(4);
		meta = block.getItemMeta();
		meta.setDisplayName("" + ChatColor.GOLD + ChatColor.BOLD + "40,000 SHARDS" + SHARDS_UUID);
		block.setItemMeta(meta);
		
		PRIZE_LIST.addAll(Collections.nCopies(15, block.clone()));
		MAP.put(block.clone(), 15);
		
		block.setAmount(5);
		meta = block.getItemMeta();
		meta.setDisplayName("" + ChatColor.GOLD + ChatColor.BOLD + "50,000 SHARDS" + SHARDS_UUID);
		block.setItemMeta(meta);
		
		PRIZE_LIST.addAll(Collections.nCopies(10, block.clone()));
		MAP.put(block.clone(), 10);
		
		ItemStack bta = new ItemStack(Material.NETHER_STAR);
		
		bta.setAmount(1);
		meta = bta.getItemMeta();
		meta.setDisplayName("" + ChatColor.DARK_PURPLE + ChatColor.BOLD + "1 $BTA" + $BTA_UUID);
		bta.setItemMeta(meta);
		
		PRIZE_LIST.addAll(Collections.nCopies(900, bta.clone()));
		MAP.put(bta.clone(), 900);
		
		bta.setAmount(3);
		meta = bta.getItemMeta();
		meta.setDisplayName("" + ChatColor.DARK_PURPLE + ChatColor.BOLD + "3 $BTA" + $BTA_UUID);
		bta.setItemMeta(meta);
		
		PRIZE_LIST.addAll(Collections.nCopies(450, bta.clone()));
		MAP.put(bta.clone(), 450);
		
		bta.setAmount(5);
		meta = bta.getItemMeta();
		meta.setDisplayName("" + ChatColor.DARK_PURPLE + ChatColor.BOLD + "5 $BTA" + $BTA_UUID);
		bta.setItemMeta(meta);
		
		PRIZE_LIST.addAll(Collections.nCopies(150, bta.clone()));
		MAP.put(bta.clone(), 150);
		
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
	
	public static int getValueInShards(int bet, ItemStack item)
	{
		if(CommonsUtil.compareUUID(item, Jackpot.JACKPOT_UUID))
		{
			return Math.max(0, jackpot);
		}
		
		if(CommonsUtil.compareUUID(item, Jackpot.SHARDS_UUID))
		{
			double multiplier;
			
			switch(item.getType())
			{
			case GOLD_NUGGET:
				multiplier = 0.1D;
				break;
			case GOLD_INGOT:
				multiplier = 1.0D;
				break;
			case GOLD_BLOCK:
				multiplier = 10.0D;
				break;
			default:
				return 0;
			}
			
			return (int) (bet * item.getAmount() * multiplier);
		}
		
		if(CommonsUtil.compareUUID(item, Jackpot.$BTA_UUID))
		{
			double multiplier = 0.001D;
			
			return (int) (bet * item.getAmount() * multiplier * $BTA_TO_SHARDS);
		}
		
		if(CommonsUtil.compareUUID(item, Jackpot.VIP_UUID))
		{
			return 10000;
		}
		
		return 0;
	}
	
	public static int getValueInShardsTheoretically(int bet, ItemStack item)
	{
		if(CommonsUtil.compareUUID(item, JACKPOT_UUID))
		{
			return 0;
		}
		
		return getValueInShards(bet, item);
	}
}

