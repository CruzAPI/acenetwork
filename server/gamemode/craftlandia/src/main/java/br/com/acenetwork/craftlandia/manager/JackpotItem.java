package br.com.acenetwork.craftlandia.manager;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Arrays;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import br.com.acenetwork.commons.CommonsUtil;
import br.com.acenetwork.commons.manager.BundleSupplier;
import br.com.acenetwork.craftlandia.executor.Jackpot;
import br.com.acenetwork.craftlandia.listener.RandomItem;
import net.md_5.bungee.api.ChatColor;

public enum JackpotItem
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
				meta.setDisplayName(Jackpot.JACKPOT_UUID + ChatColor.AQUA + ChatColor.BOLD + bundle.getString("noun.jackpot").toUpperCase());
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
			meta.setDisplayName(Jackpot.NONE_UUID + ChatColor.RED + ChatColor.BOLD + bundle.getString("inv.jackpot.none.item"));
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
			double prize = bet * Jackpot.getMultiplier(item);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(Jackpot.SHARDS_UUID + ChatColor.GOLD + ChatColor.BOLD + df.format(prize) + " " + bundle.getString("shards").toUpperCase());
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
			double prize = bet * Jackpot.getMultiplier(item);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(Jackpot.SHARDS_UUID + ChatColor.GOLD + ChatColor.BOLD + df.format(prize) + " " + bundle.getString("shards").toUpperCase());
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
			double prize = bet * Jackpot.getMultiplier(item);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(Jackpot.SHARDS_UUID + ChatColor.GOLD + ChatColor.BOLD + df.format(prize) + " " + bundle.getString("shards").toUpperCase());
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
			double prize = bet * Jackpot.getMultiplier(item);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(Jackpot.SHARDS_UUID + ChatColor.GOLD + ChatColor.BOLD + df.format(prize) + " " + bundle.getString("shards").toUpperCase());
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
			double prize = bet * Jackpot.getMultiplier(item);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(Jackpot.SHARDS_UUID + ChatColor.GOLD + ChatColor.BOLD + df.format(prize) + " " + bundle.getString("shards").toUpperCase());
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
			double prize = bet * Jackpot.getMultiplier(item);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(Jackpot.SHARDS_UUID + ChatColor.GOLD + ChatColor.BOLD + df.format(prize) + " " + bundle.getString("shards").toUpperCase());
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
			double prize = bet * Jackpot.getMultiplier(item);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(Jackpot.SHARDS_UUID + ChatColor.GOLD + ChatColor.BOLD + df.format(prize) + " " + bundle.getString("shards").toUpperCase());
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
			double prize = bet * Jackpot.getMultiplier(item);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(Jackpot.SHARDS_UUID + ChatColor.GOLD + ChatColor.BOLD + df.format(prize) + " " + bundle.getString("shards").toUpperCase());
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
			double prize = bet * Jackpot.getMultiplier(item);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(Jackpot.SHARDS_UUID + ChatColor.GOLD + ChatColor.BOLD + df.format(prize) + " " + bundle.getString("shards").toUpperCase());
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
			double prize = bet * Jackpot.getMultiplier(item);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(Jackpot.SHARDS_UUID + ChatColor.GOLD + ChatColor.BOLD + df.format(prize) + " " + bundle.getString("shards").toUpperCase());
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
			double prize = bet * Jackpot.getMultiplier(item);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(Jackpot.SHARDS_UUID + ChatColor.GOLD + ChatColor.BOLD + df.format(prize) + " " + bundle.getString("shards").toUpperCase());
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
			double prize = bet * Jackpot.getMultiplier(item);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(Jackpot.SHARDS_UUID + ChatColor.GOLD + ChatColor.BOLD + df.format(prize) + " " + bundle.getString("shards").toUpperCase());
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
			double prize = bet * Jackpot.getMultiplier(item);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(Jackpot.SHARDS_UUID + ChatColor.GOLD + ChatColor.BOLD + df.format(prize) + " " + bundle.getString("shards").toUpperCase());
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
			double prize = bet * Jackpot.getMultiplier(item);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(Jackpot.SHARDS_UUID + ChatColor.GOLD + ChatColor.BOLD + df.format(prize) + " " + bundle.getString("shards").toUpperCase());
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
			double prize = bet * Jackpot.getMultiplier(item);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(Jackpot.SHARDS_UUID + ChatColor.GOLD + ChatColor.BOLD + df.format(prize) + " " + bundle.getString("shards").toUpperCase());
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
			double prize = bet * Jackpot.getMultiplier(item);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(Jackpot.$BTA_UUID + ChatColor.DARK_PURPLE + ChatColor.BOLD + df.format(prize) + " $BTA");
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
			double prize = bet * Jackpot.getMultiplier(item);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(Jackpot.$BTA_UUID + ChatColor.DARK_PURPLE + ChatColor.BOLD + df.format(prize) + " $BTA");
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
			double prize = bet * Jackpot.getMultiplier(item);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(Jackpot.$BTA_UUID + ChatColor.DARK_PURPLE + ChatColor.BOLD + df.format(prize) + " $BTA");
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
				CommonsUtil.setCustomSkull((SkullMeta) meta, RandomItem.SKIN_VALUE);
			}
			else
			{
				item = new ItemStack(Material.CHEST);
				meta = item.getItemMeta();
			}
			
			meta.setDisplayName(Jackpot.RANDOM_ITEM_UUID + ChatColor.WHITE + ChatColor.BOLD + bundle.getString("inv.jackpot.random-item.item"));
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
			
			meta.setDisplayName(Jackpot.VIP_UUID + ChatColor.GREEN + ChatColor.BOLD + bundle.getString("abbreviation.vip"));
			item.setItemMeta(meta);
			return item;
		}
	}),
	
	;
	
	private final byte id;
	private final BundleSupplier<ItemStack> itemSupplier;
	
	private JackpotItem(byte id, BundleSupplier<ItemStack> itemSupplier)
	{
		this.id = id;
		this.itemSupplier = itemSupplier;
	}
	
	public byte getId()
	{
		return id;
	}
	
	public BundleSupplier<ItemStack> getItemSupplier()
	{
		return itemSupplier;
	}

	public static JackpotItem getById(Byte id)
	{
		for(JackpotItem value : values())
		{
			if(value.id == id)
			{
				return value;
			}
		}
		
		return null;
	}
}