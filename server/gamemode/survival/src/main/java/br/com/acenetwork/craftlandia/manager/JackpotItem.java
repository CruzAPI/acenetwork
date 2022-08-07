package br.com.acenetwork.craftlandia.manager;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import br.com.acenetwork.commons.manager.BundleSupplier;
import br.com.acenetwork.craftlandia.executor.Jackpot;
import br.com.acenetwork.craftlandia.item.CommonRandomItem;
import br.com.acenetwork.craftlandia.item.ContainmentPickaxe;
import br.com.acenetwork.craftlandia.item.LegendaryRandomItem;
import br.com.acenetwork.craftlandia.item.NormalRandomItem;
import br.com.acenetwork.craftlandia.item.RareRandomItem;
import br.com.acenetwork.craftlandia.item.VipItem;
import net.md_5.bungee.api.ChatColor;

public enum JackpotItem
{
	JACKPOT(new BundleSupplier<ItemStack>()
	{
		@Override
		public ItemStack get(ResourceBundle bundle, Object... args)
		{
			double prize = Jackpot.getInstance().getJackpotPrize();
			
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
	
	NONE(new BundleSupplier<ItemStack>()
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
	
	NUGGET(new BundleSupplier<ItemStack>()
	{
		@Override
		public ItemStack get(ResourceBundle bundle, Object... args)
		{
			double bet = (double) args[0];
			int amount = (int) args[1];
			
			DecimalFormat df = new DecimalFormat("#.##", new DecimalFormatSymbols(bundle.getLocale()));
			df.setGroupingSize(3);
			df.setGroupingUsed(true);
			
			ItemStack item = new ItemStack(Material.GOLD_NUGGET, amount);
			double prize = bet * Jackpot.getMultiplier(item);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(Jackpot.SHARDS_UUID + ChatColor.GOLD + ChatColor.BOLD + df.format(prize) + " " + bundle.getString("shards").toUpperCase());
			item.setItemMeta(meta);
			return item;
		}
	}),
	
	INGOT(new BundleSupplier<ItemStack>()
	{
		@Override
		public ItemStack get(ResourceBundle bundle, Object... args)
		{
			double bet = (double) args[0];
			int amount = (int) args[1];
			
			DecimalFormat df = new DecimalFormat("#.##", new DecimalFormatSymbols(bundle.getLocale()));
			df.setGroupingSize(3);
			df.setGroupingUsed(true);
			
			ItemStack item = new ItemStack(Material.GOLD_INGOT, amount);
			double prize = bet * Jackpot.getMultiplier(item);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(Jackpot.SHARDS_UUID + ChatColor.GOLD + ChatColor.BOLD + df.format(prize) + " " + bundle.getString("shards").toUpperCase());
			item.setItemMeta(meta);
			return item;
		}
	}),
	
	BLOCK(new BundleSupplier<ItemStack>()
	{
		@Override
		public ItemStack get(ResourceBundle bundle, Object... args)
		{
			double bet = (double) args[0];
			int amount = (int) args[1];
			
			DecimalFormat df = new DecimalFormat("#.##", new DecimalFormatSymbols(bundle.getLocale()));
			df.setGroupingSize(3);
			df.setGroupingUsed(true);
			
			ItemStack item = new ItemStack(Material.GOLD_BLOCK, amount);
			double prize = bet * Jackpot.getMultiplier(item);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(Jackpot.SHARDS_UUID + ChatColor.GOLD + ChatColor.BOLD + df.format(prize) + " " + bundle.getString("shards").toUpperCase());
			item.setItemMeta(meta);
			return item;
		}
	}),
	
	$BTA(new BundleSupplier<ItemStack>()
	{
		@Override
		public ItemStack get(ResourceBundle bundle, Object... args)
		{
			double bet = (double) args[0];
			int amount = (int) args[1];
			
			DecimalFormat df = new DecimalFormat("#.##", new DecimalFormatSymbols(bundle.getLocale()));
			df.setGroupingSize(3);
			df.setGroupingUsed(true);
			
			ItemStack item = new ItemStack(Material.NETHER_STAR, amount);
			double prize = bet * Jackpot.getMultiplier(item);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(Jackpot.$BTA_UUID + ChatColor.DARK_PURPLE + ChatColor.BOLD + df.format(prize) + " $BTA");
			item.setItemMeta(meta);
			return item;
		}
	}),
	
	VIP(new BundleSupplier<ItemStack>()
	{
		@Override
		public ItemStack get(ResourceBundle bundle, Object... args)
		{
			int amount = (int) args[1];
			int version = (int) args[2];
			ItemStack item = ItemSpecial.getInstance(VipItem.class).getItemStack(bundle, UUID.randomUUID(), version);
			item.setAmount(amount);
			return item;
		}
	}),
	
	RANDOM_ITEM(new BundleSupplier<ItemStack>()
	{
		@Override
		public ItemStack get(ResourceBundle bundle, Object... args)
		{
			int amount = (int) args[1];
			int version = (int) args[2];
			ItemStack item = ItemSpecial.getInstance(NormalRandomItem.class).getItemStack(bundle, UUID.randomUUID(), version);
			item.setAmount(amount);
			return item;
		}
	}),
	
	COMMON_RANDOM_ITEM(new BundleSupplier<ItemStack>()
	{
		@Override
		public ItemStack get(ResourceBundle bundle, Object... args)
		{
			int amount = (int) args[1];
			int version = (int) args[2];
			ItemStack item = ItemSpecial.getInstance(CommonRandomItem.class).getItemStack(bundle, UUID.randomUUID(), version);
			item.setAmount(amount);
			return item;
		}
	}),
	
	RARE_RANDOM_ITEM(new BundleSupplier<ItemStack>()
	{
		@Override
		public ItemStack get(ResourceBundle bundle, Object... args)
		{
			int amount = (int) args[1];
			int version = (int) args[2];
			ItemStack item = ItemSpecial.getInstance(RareRandomItem.class).getItemStack(bundle, UUID.randomUUID(), version);
			item.setAmount(amount);
			return item;
		}
	}),
	
	LEGENDARY_RANDOM_ITEM(new BundleSupplier<ItemStack>()
	{
		@Override
		public ItemStack get(ResourceBundle bundle, Object... args)
		{
			int amount = (int) args[1];
			int version = (int) args[2];
			ItemStack item = ItemSpecial.getInstance(LegendaryRandomItem.class).getItemStack(bundle, UUID.randomUUID(), version);
			item.setAmount(amount);
			return item;
		}
	}),
	
	CONTAINMENT_PICKAXE(new BundleSupplier<ItemStack>()
	{
		@Override
		public ItemStack get(ResourceBundle bundle, Object... args)
		{
			float chance = (float) args[1];
			ItemStack item = ItemSpecial.getInstance(ContainmentPickaxe.class).getItemStack(bundle, chance);
			return item;
		}
	}),
	
	;
	
	private final BundleSupplier<ItemStack> itemSupplier;
	
	private JackpotItem(BundleSupplier<ItemStack> itemSupplier)
	{
		this.itemSupplier = itemSupplier;
	}
	
	public BundleSupplier<ItemStack> getItemSupplier()
	{
		return itemSupplier;
	}
}