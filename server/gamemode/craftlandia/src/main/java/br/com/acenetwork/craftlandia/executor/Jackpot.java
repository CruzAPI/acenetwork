package br.com.acenetwork.craftlandia.executor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import com.google.common.io.ByteStreams;

import br.com.acenetwork.commons.CommonsUtil;
import br.com.acenetwork.commons.manager.BundleSupplier;
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
	
	private double jackpot;
	
	private static Jackpot instance;
	
	public static final int $BTA_TO_SHARDS = 200;
	
	public static final Map<Byte, Integer> COAL_MAP = new LinkedHashMap<>();

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
	
	public enum Item
	{
		
		JACKPOT((byte) -1, new BundleSupplier<ItemStack>()
		{
			@Override
			public ItemStack get(ResourceBundle bundle, Object... args)
			{
				double prize = Jackpot.getInstance().getJackpot();
				
				if(prize > 0.0D)
				{
					DecimalFormat df = new DecimalFormat("#.##", new DecimalFormatSymbols(bundle.getLocale()));
					df.setGroupingSize(3);
					df.setGroupingUsed(true);
					
					ItemStack item = new ItemStack(Material.BEACON);
					ItemMeta meta = item.getItemMeta();
					meta.setDisplayName(JACKPOT_UUID + ChatColor.AQUA + ChatColor.BOLD + bundle.getString("noun.jackpot").toUpperCase());
					meta.setLore(Arrays.asList(
							ChatColor.GRAY + StringUtils.capitalize(bundle.getString("noun.prize")) + ": " 
									+ ChatColor.WHITE + df.format(prize)));
					item.setItemMeta(meta);
					return item;
				}
				
				return NONE.getItemSupplier().get(bundle);
			}
		}),
		
		NONE((byte) 0, new BundleSupplier<ItemStack>()
		{
			@Override
			public ItemStack get(ResourceBundle bundle, Object... args)
			{
				ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14);
				ItemMeta meta = item.getItemMeta();
				meta.setDisplayName(NONE_UUID + ChatColor.RED + ChatColor.BOLD + bundle.getString("inv.jackpot.none.item"));
				item.setItemMeta(meta);
				return item;
			}
		}),
		
		NUGGET_1((byte) 1, new BundleSupplier<ItemStack>()
		{
			@Override
			public ItemStack get(ResourceBundle bundle, Object... args)
			{
				double bet = (double) args[0];
				DecimalFormat df = new DecimalFormat("#.##", new DecimalFormatSymbols(bundle.getLocale()));
				df.setGroupingSize(3);
				df.setGroupingUsed(true);
				
				ItemStack item = new ItemStack(Material.GOLD_NUGGET, 1);
				double prize = bet * getMultiplier(item);
				ItemMeta meta = item.getItemMeta();
				meta.setDisplayName(SHARDS_UUID + ChatColor.GOLD + ChatColor.BOLD + df.format(prize) + " " + bundle.getString("shards").toUpperCase());
				item.setItemMeta(meta);
				return item;
			}
		}),
		
		NUGGET_2((byte) 2, new BundleSupplier<ItemStack>()
		{
			@Override
			public ItemStack get(ResourceBundle bundle, Object... args)
			{
				double bet = (double) args[0];
				
				DecimalFormat df = new DecimalFormat("#.##", new DecimalFormatSymbols(bundle.getLocale()));
				df.setGroupingSize(3);
				df.setGroupingUsed(true);
				
				ItemStack item = new ItemStack(Material.GOLD_NUGGET, 2);
				double prize = bet * getMultiplier(item);
				ItemMeta meta = item.getItemMeta();
				meta.setDisplayName(SHARDS_UUID + ChatColor.GOLD + ChatColor.BOLD + df.format(prize) + " " + bundle.getString("shards").toUpperCase());
				item.setItemMeta(meta);
				return item;
			}
		}),
		
		NUGGET_3((byte) 3, new BundleSupplier<ItemStack>()
		{
			@Override
			public ItemStack get(ResourceBundle bundle, Object... args)
			{
				double bet = (double) args[0];
				
				DecimalFormat df = new DecimalFormat("#.##", new DecimalFormatSymbols(bundle.getLocale()));
				df.setGroupingSize(3);
				df.setGroupingUsed(true);
				
				ItemStack item = new ItemStack(Material.GOLD_NUGGET, 3);
				double prize = bet * getMultiplier(item);
				ItemMeta meta = item.getItemMeta();
				meta.setDisplayName(SHARDS_UUID + ChatColor.GOLD + ChatColor.BOLD + df.format(prize) + " " + bundle.getString("shards").toUpperCase());
				item.setItemMeta(meta);
				return item;
			}
		}),
		
		
		NUGGET_4((byte) 4, new BundleSupplier<ItemStack>()
		{
			@Override
			public ItemStack get(ResourceBundle bundle, Object... args)
			{
				double bet = (double) args[0];
				
				DecimalFormat df = new DecimalFormat("#.##", new DecimalFormatSymbols(bundle.getLocale()));
				df.setGroupingSize(3);
				df.setGroupingUsed(true);
				
				ItemStack item = new ItemStack(Material.GOLD_NUGGET, 4);
				double prize = bet * getMultiplier(item);
				ItemMeta meta = item.getItemMeta();
				meta.setDisplayName(SHARDS_UUID + ChatColor.GOLD + ChatColor.BOLD + df.format(prize) + " " + bundle.getString("shards").toUpperCase());
				item.setItemMeta(meta);
				return item;
			}
		}),
		
		NUGGET_5((byte) 5, new BundleSupplier<ItemStack>()
		{
			@Override
			public ItemStack get(ResourceBundle bundle, Object... args)
			{
				double bet = (double) args[0];
				
				DecimalFormat df = new DecimalFormat("#.##", new DecimalFormatSymbols(bundle.getLocale()));
				df.setGroupingSize(3);
				df.setGroupingUsed(true);
				
				ItemStack item = new ItemStack(Material.GOLD_NUGGET, 5);
				double prize = bet * getMultiplier(item);
				ItemMeta meta = item.getItemMeta();
				meta.setDisplayName(SHARDS_UUID + ChatColor.GOLD + ChatColor.BOLD + df.format(prize) + " " + bundle.getString("shards").toUpperCase());
				item.setItemMeta(meta);
				return item;
			}
		}),
		
		INGOT_1((byte) 6, new BundleSupplier<ItemStack>()
		{
			@Override
			public ItemStack get(ResourceBundle bundle, Object... args)
			{
				double bet = (double) args[0];
				
				DecimalFormat df = new DecimalFormat("#.##", new DecimalFormatSymbols(bundle.getLocale()));
				df.setGroupingSize(3);
				df.setGroupingUsed(true);
				
				ItemStack item = new ItemStack(Material.GOLD_INGOT, 1);
				double prize = bet * getMultiplier(item);
				ItemMeta meta = item.getItemMeta();
				meta.setDisplayName(SHARDS_UUID + ChatColor.GOLD + ChatColor.BOLD + df.format(prize) + " " + bundle.getString("shards").toUpperCase());
				item.setItemMeta(meta);
				return item;
			}
		}),
		
		INGOT_2((byte) 7, new BundleSupplier<ItemStack>()
		{
			@Override
			public ItemStack get(ResourceBundle bundle, Object... args)
			{
				double bet = (double) args[0];
				
				DecimalFormat df = new DecimalFormat("#.##", new DecimalFormatSymbols(bundle.getLocale()));
				df.setGroupingSize(3);
				df.setGroupingUsed(true);
				
				ItemStack item = new ItemStack(Material.GOLD_INGOT, 2);
				double prize = bet * getMultiplier(item);
				ItemMeta meta = item.getItemMeta();
				meta.setDisplayName(SHARDS_UUID + ChatColor.GOLD + ChatColor.BOLD + df.format(prize) + " " + bundle.getString("shards").toUpperCase());
				item.setItemMeta(meta);
				return item;
			}
		}),
		
		INGOT_3((byte) 8, new BundleSupplier<ItemStack>()
		{
			@Override
			public ItemStack get(ResourceBundle bundle, Object... args)
			{
				double bet = (double) args[0];
				
				DecimalFormat df = new DecimalFormat("#.##", new DecimalFormatSymbols(bundle.getLocale()));
				df.setGroupingSize(3);
				df.setGroupingUsed(true);
				
				ItemStack item = new ItemStack(Material.GOLD_INGOT, 3);
				double prize = bet * getMultiplier(item);
				ItemMeta meta = item.getItemMeta();
				meta.setDisplayName(SHARDS_UUID + ChatColor.GOLD + ChatColor.BOLD + df.format(prize) + " " + bundle.getString("shards").toUpperCase());
				item.setItemMeta(meta);
				return item;
			}
		}),
		
		INGOT_4((byte) 9, new BundleSupplier<ItemStack>()
		{
			@Override
			public ItemStack get(ResourceBundle bundle, Object... args)
			{
				double bet = (double) args[0];
				
				DecimalFormat df = new DecimalFormat("#.##", new DecimalFormatSymbols(bundle.getLocale()));
				df.setGroupingSize(3);
				df.setGroupingUsed(true);
				
				ItemStack item = new ItemStack(Material.GOLD_INGOT, 4);
				double prize = bet * getMultiplier(item);
				ItemMeta meta = item.getItemMeta();
				meta.setDisplayName(SHARDS_UUID + ChatColor.GOLD + ChatColor.BOLD + df.format(prize) + " " + bundle.getString("shards").toUpperCase());
				item.setItemMeta(meta);
				return item;
			}
		}),
		
		INGOT_5((byte) 10, new BundleSupplier<ItemStack>()
		{
			@Override
			public ItemStack get(ResourceBundle bundle, Object... args)
			{
				double bet = (double) args[0];
				
				DecimalFormat df = new DecimalFormat("#.##", new DecimalFormatSymbols(bundle.getLocale()));
				df.setGroupingSize(3);
				df.setGroupingUsed(true);
				
				ItemStack item = new ItemStack(Material.GOLD_INGOT, 5);
				double prize = bet * getMultiplier(item);
				ItemMeta meta = item.getItemMeta();
				meta.setDisplayName(SHARDS_UUID + ChatColor.GOLD + ChatColor.BOLD + df.format(prize) + " " + bundle.getString("shards").toUpperCase());
				item.setItemMeta(meta);
				return item;
			}
		}),
		
		BLOCK_1((byte) 11, new BundleSupplier<ItemStack>()
		{
			@Override
			public ItemStack get(ResourceBundle bundle, Object... args)
			{
				double bet = (double) args[0];
				
				DecimalFormat df = new DecimalFormat("#.##", new DecimalFormatSymbols(bundle.getLocale()));
				df.setGroupingSize(3);
				df.setGroupingUsed(true);
				
				ItemStack item = new ItemStack(Material.GOLD_BLOCK, 1);
				double prize = bet * getMultiplier(item);
				ItemMeta meta = item.getItemMeta();
				meta.setDisplayName(SHARDS_UUID + ChatColor.GOLD + ChatColor.BOLD + df.format(prize) + " " + bundle.getString("shards").toUpperCase());
				item.setItemMeta(meta);
				return item;
			}
		}),
		
		BLOCK_2((byte) 12, new BundleSupplier<ItemStack>()
		{
			@Override
			public ItemStack get(ResourceBundle bundle, Object... args)
			{
				double bet = (double) args[0];
				
				DecimalFormat df = new DecimalFormat("#.##", new DecimalFormatSymbols(bundle.getLocale()));
				df.setGroupingSize(3);
				df.setGroupingUsed(true);
				
				ItemStack item = new ItemStack(Material.GOLD_BLOCK, 2);
				double prize = bet * getMultiplier(item);
				ItemMeta meta = item.getItemMeta();
				meta.setDisplayName(SHARDS_UUID + ChatColor.GOLD + ChatColor.BOLD + df.format(prize) + " " + bundle.getString("shards").toUpperCase());
				item.setItemMeta(meta);
				return item;
			}
		}),
		
		BLOCK_3((byte) 13, new BundleSupplier<ItemStack>()
		{
			@Override
			public ItemStack get(ResourceBundle bundle, Object... args)
			{
				double bet = (double) args[0];
				
				DecimalFormat df = new DecimalFormat("#.##", new DecimalFormatSymbols(bundle.getLocale()));
				df.setGroupingSize(3);
				df.setGroupingUsed(true);
				
				ItemStack item = new ItemStack(Material.GOLD_BLOCK, 3);
				double prize = bet * getMultiplier(item);
				ItemMeta meta = item.getItemMeta();
				meta.setDisplayName(SHARDS_UUID + ChatColor.GOLD + ChatColor.BOLD + df.format(prize) + " " + bundle.getString("shards").toUpperCase());
				item.setItemMeta(meta);
				return item;
			}
		}),
		
		BLOCK_4((byte) 14, new BundleSupplier<ItemStack>()
		{
			@Override
			public ItemStack get(ResourceBundle bundle, Object... args)
			{
				double bet = (double) args[0];
				
				DecimalFormat df = new DecimalFormat("#.##", new DecimalFormatSymbols(bundle.getLocale()));
				df.setGroupingSize(3);
				df.setGroupingUsed(true);
				
				ItemStack item = new ItemStack(Material.GOLD_BLOCK, 4);
				double prize = bet * getMultiplier(item);
				ItemMeta meta = item.getItemMeta();
				meta.setDisplayName(SHARDS_UUID + ChatColor.GOLD + ChatColor.BOLD + df.format(prize) + " " + bundle.getString("shards").toUpperCase());
				item.setItemMeta(meta);
				return item;
			}
		}),
		
		BLOCK_5((byte) 15, new BundleSupplier<ItemStack>()
		{
			@Override
			public ItemStack get(ResourceBundle bundle, Object... args)
			{
				double bet = (double) args[0];
				
				DecimalFormat df = new DecimalFormat("#.##", new DecimalFormatSymbols(bundle.getLocale()));
				df.setGroupingSize(3);
				df.setGroupingUsed(true);
				
				ItemStack item = new ItemStack(Material.GOLD_BLOCK, 5);
				double prize = bet * getMultiplier(item);
				ItemMeta meta = item.getItemMeta();
				meta.setDisplayName(SHARDS_UUID + ChatColor.GOLD + ChatColor.BOLD + df.format(prize) + " " + bundle.getString("shards").toUpperCase());
				item.setItemMeta(meta);
				return item;
			}
		}),
		
		$BTA_1((byte) 16, new BundleSupplier<ItemStack>()
		{
			@Override
			public ItemStack get(ResourceBundle bundle, Object... args)
			{
				double bet = (double) args[0];
				
				DecimalFormat df = new DecimalFormat("#.##", new DecimalFormatSymbols(bundle.getLocale()));
				df.setGroupingSize(3);
				df.setGroupingUsed(true);
				
				ItemStack item = new ItemStack(Material.NETHER_STAR, 1);
				double prize = bet * getMultiplier(item);
				ItemMeta meta = item.getItemMeta();
				meta.setDisplayName($BTA_UUID + ChatColor.DARK_PURPLE + ChatColor.BOLD + df.format(prize) + " $BTA");
				item.setItemMeta(meta);
				return item;
			}
		}),
		
		$BTA_3((byte) 17, new BundleSupplier<ItemStack>()
		{
			@Override
			public ItemStack get(ResourceBundle bundle, Object... args)
			{
				double bet = (double) args[0];
				
				DecimalFormat df = new DecimalFormat("#.##", new DecimalFormatSymbols(bundle.getLocale()));
				df.setGroupingSize(3);
				df.setGroupingUsed(true);
				
				ItemStack item = new ItemStack(Material.NETHER_STAR, 3);
				double prize = bet * getMultiplier(item);
				ItemMeta meta = item.getItemMeta();
				meta.setDisplayName($BTA_UUID + ChatColor.DARK_PURPLE + ChatColor.BOLD + df.format(prize) + " $BTA");
				item.setItemMeta(meta);
				return item;
			}
		}),
		
		$BTA_5((byte) 18, new BundleSupplier<ItemStack>()
		{
			@Override
			public ItemStack get(ResourceBundle bundle, Object... args)
			{
				double bet = (double) args[0];
				
				DecimalFormat df = new DecimalFormat("#.##", new DecimalFormatSymbols(bundle.getLocale()));
				df.setGroupingSize(3);
				df.setGroupingUsed(true);
				
				ItemStack item = new ItemStack(Material.NETHER_STAR, 5);
				double prize = bet * getMultiplier(item);
				ItemMeta meta = item.getItemMeta();
				meta.setDisplayName($BTA_UUID + ChatColor.DARK_PURPLE + ChatColor.BOLD + df.format(prize) + " $BTA");
				item.setItemMeta(meta);
				return item;
			}
		}),
		
		RANDOM_ITEM((byte) 19, new BundleSupplier<ItemStack>()
		{
			@Override
			public ItemStack get(ResourceBundle bundle, Object... args)
			{
				int version = (int) args[1];
				
				ItemStack item;
				ItemMeta meta;
				
				if(version > 5)
				{
					item = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
					meta = item.getItemMeta();
					CommonsUtil.setCustomSkull((SkullMeta) meta, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjViOTVkYTEyODE2NDJkYWE1ZDAyMmFkYmQzZTdjYjY5ZGMwOTQyYzgxY2Q2M2JlOWMzODU3ZDIyMmUxYzhkOSJ9fX0=");
				}
				else
				{
					item = new ItemStack(Material.CHEST);
					meta = item.getItemMeta();
				}
				
				meta.setDisplayName(RANDOM_ITEM_UUID + ChatColor.WHITE + ChatColor.BOLD + bundle.getString("inv.jackpot.random-item.item"));
				item.setItemMeta(meta);
				return item;
			}
		}),
		
		VIP((byte) 20, new BundleSupplier<ItemStack>()
		{
			@Override
			public ItemStack get(ResourceBundle bundle, Object... args)
			{
				int version = (int) args[1];
				
				ItemStack item;
				ItemMeta meta;
				
				if(version > 5)
				{
					item = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
					meta = item.getItemMeta();
					CommonsUtil.setCustomSkull((SkullMeta) meta, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDVjNmRjMmJiZjUxYzM2Y2ZjNzcxNDU4NWE2YTU2ODNlZjJiMTRkNDdkOGZmNzE0NjU0YTg5M2Y1ZGE2MjIifX19");
				}
				else
				{
					item = new ItemStack(Material.WOOL, 1, (short) 5);
					meta = item.getItemMeta();
				}
				
				meta.setDisplayName(VIP_UUID + ChatColor.GREEN + ChatColor.BOLD + bundle.getString("abbreviation.vip"));
				item.setItemMeta(meta);
				return item;
			}
		}),
		
		;
		
		private final byte id;
		private final BundleSupplier<ItemStack> itemSupplier;
		
		private Item(byte id, BundleSupplier<ItemStack> itemSupplier)
		{
			this.id = id;
			this.itemSupplier = itemSupplier;
		}
		
		private Item(byte id)
		{
			this(id, null);
		}
		
		public byte getId()
		{
			return id;
		}
		
		public BundleSupplier<ItemStack> getItemSupplier()
		{
			return itemSupplier;
		}

		public static Item getById(Byte id)
		{
			for(Item value : values())
			{
				if(value.id == id)
				{
					return value;
				}
			}
			
			return null;
		}
	}
	

	
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
		
		COAL_MAP.put(Item.JACKPOT.getId(), -size + (size += 1));
		COAL_MAP.put(Item.VIP.getId(), -size + (size += 30));
		COAL_MAP.put(Item.RANDOM_ITEM.getId(), -size + (size += 3000));
		COAL_MAP.put(Item.NUGGET_1.getId(), -size + (size += 1800));
		COAL_MAP.put(Item.NUGGET_2.getId(), -size + (size += 1800));
		COAL_MAP.put(Item.NUGGET_3.getId(), -size + (size += 1800));
		COAL_MAP.put(Item.NUGGET_4.getId(), -size + (size += 1800));
		COAL_MAP.put(Item.NUGGET_5.getId(), -size + (size += 1800));
		COAL_MAP.put(Item.INGOT_1.getId(), -size + (size += 1500));
		COAL_MAP.put(Item.INGOT_2.getId(), -size + (size += 1200));
		COAL_MAP.put(Item.INGOT_3.getId(), -size + (size += 900));
		COAL_MAP.put(Item.INGOT_4.getId(), -size + (size += 600));
		COAL_MAP.put(Item.INGOT_5.getId(), -size + (size += 300));
		COAL_MAP.put(Item.BLOCK_1.getId(), -size + (size += 225));
		COAL_MAP.put(Item.BLOCK_2.getId(), -size + (size += 30));
		COAL_MAP.put(Item.BLOCK_3.getId(), -size + (size += 20));
		COAL_MAP.put(Item.BLOCK_4.getId(), -size + (size += 15));
		COAL_MAP.put(Item.BLOCK_5.getId(), -size + (size += 10));
		COAL_MAP.put(Item.$BTA_1.getId(), -size + (size += 900));
		COAL_MAP.put(Item.$BTA_3.getId(), -size + (size += 400));
		COAL_MAP.put(Item.$BTA_5.getId(), -size + (size += 200));
		COAL_MAP.put(Item.NONE.getId(), -size + (size += Math.max(0, maxSize - size)));
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
			throw new RuntimeException(ex);
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
}

