package br.com.acenetwork.craftlandia.manager;

import static br.com.acenetwork.craftlandia.manager.JackpotItem.*;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import br.com.acenetwork.commons.manager.BundleSupplier;
import net.md_5.bungee.api.ChatColor;

public enum JackpotType
{
	COAL(1000.0D, Material.COAL_BLOCK, (short) 15, (short) 3, (short) 5, 20, ChatColor.BLACK, new BundleSupplier<Map<ItemStack, Integer>>()
	{
		@Override
		public Map<ItemStack, Integer> get(ResourceBundle bundle, Object... args)
		{
			double bet = COAL.getBet();
			int version = args.length > 1 ? (int) args[1] : 47;
			
			int size = 0;
			final int maxSize = 64000;
			
			Map<ItemStack, Integer> map = new LinkedHashMap<>();
			
			map.put(JACKPOT.getItemSupplier().get(bundle, bet, 1, version), -size + (size += 1));
			map.put(VIP.getItemSupplier().get(bundle, bet, 1, version), -size + (size += 16));
			map.put(RANDOM_ITEM.getItemSupplier().get(bundle, bet, 1, version), -size + (size += 6400));
			map.put(COMMON_RANDOM_ITEM.getItemSupplier().get(bundle, bet, 1, version), -size + (size += 640 * 15));
			map.put(RARE_RANDOM_ITEM.getItemSupplier().get(bundle, bet, 1, version), -size + (size += 640 * 4));
			map.put(LEGENDARY_RANDOM_ITEM.getItemSupplier().get(bundle, bet, 1, version), -size + (size += 640 * 1));
			map.put(CONTAINMENT_PICKAXE.getItemSupplier().get(bundle, bet, 2.0F, version), -size + (size += 640 * 2));
			map.put(CONTAINMENT_PICKAXE.getItemSupplier().get(bundle, bet, 4.0F, version), -size + (size += 512 * 2));
			map.put(CONTAINMENT_PICKAXE.getItemSupplier().get(bundle, bet, 6.0F, version), -size + (size += 384 * 2));
			map.put(CONTAINMENT_PICKAXE.getItemSupplier().get(bundle, bet, 8.0F, version), -size + (size += 256 * 2));
			map.put(CONTAINMENT_PICKAXE.getItemSupplier().get(bundle, bet, 10.0F, version), -size + (size += 128 * 2));
			map.put(NUGGET.getItemSupplier().get(bundle, bet, 1, version), -size + (size += 1536));
			map.put(NUGGET.getItemSupplier().get(bundle, bet, 2, version), -size + (size += 1792));
			map.put(NUGGET.getItemSupplier().get(bundle, bet, 3, version), -size + (size += 2048));
			map.put(NUGGET.getItemSupplier().get(bundle, bet, 4, version), -size + (size += 2304));
			map.put(NUGGET.getItemSupplier().get(bundle, bet, 5, version), -size + (size += 2560));
			map.put(INGOT.getItemSupplier().get(bundle, bet, 1, version), -size + (size += 1280));
			map.put(INGOT.getItemSupplier().get(bundle, bet, 2, version), -size + (size += 1024));
			map.put(INGOT.getItemSupplier().get(bundle, bet, 3, version), -size + (size += 768));
			map.put(INGOT.getItemSupplier().get(bundle, bet, 4, version), -size + (size += 512));
			map.put(INGOT.getItemSupplier().get(bundle, bet, 5, version), -size + (size += 256));
			map.put(BLOCK.getItemSupplier().get(bundle, bet, 1, version), -size + (size += 64));
			map.put(BLOCK.getItemSupplier().get(bundle, bet, 2, version), -size + (size += 32));
			map.put(BLOCK.getItemSupplier().get(bundle, bet, 3, version), -size + (size += 16));
			map.put(BLOCK.getItemSupplier().get(bundle, bet, 4, version), -size + (size += 8));
			map.put(BLOCK.getItemSupplier().get(bundle, bet, 5, version), -size + (size += 4));
			map.put($BTA.getItemSupplier().get(bundle, bet, 1, version), -size + (size += 4096));
			map.put($BTA.getItemSupplier().get(bundle, bet, 2, version), -size + (size += 1024));
			map.put($BTA.getItemSupplier().get(bundle, bet, 4, version), -size + (size += 512));
			map.put($BTA.getItemSupplier().get(bundle, bet, 8, version), -size + (size += 256));
			map.put($BTA.getItemSupplier().get(bundle, bet, 16, version), -size + (size += 128));
			map.put($BTA.getItemSupplier().get(bundle, bet, 32, version), -size + (size += 64));
			map.put($BTA.getItemSupplier().get(bundle, bet, 64, version), -size + (size += 32));
			map.put(NONE.getItemSupplier().get(bundle, bet, 1, version), -size + (size += Math.max(0, maxSize - size)));
			
			return map;
		}
	}),
	
	REDSTONE(10000.0D, Material.REDSTONE_BLOCK, (short) 14, (short) 3, (short) 5, 30, ChatColor.DARK_RED, new BundleSupplier<Map<ItemStack, Integer>>()
	{
		@Override
		public Map<ItemStack, Integer> get(ResourceBundle bundle, Object... args)
		{
			double bet = REDSTONE.getBet();
			int version = args.length > 0 ? (int) args[0] : 47;
			
			int size = 0;
			final int maxSize = 64000;
			
			Map<ItemStack, Integer> map = new LinkedHashMap<>();
			
			map.put(JACKPOT.getItemSupplier().get(bundle, bet, 1, version), -size + (size += 10 * 2));
			map.put(VIP.getItemSupplier().get(bundle, bet, 1, version), -size + (size += 16 * 20));
			map.put(RANDOM_ITEM.getItemSupplier().get(bundle, bet, 10, version), -size + (size += 640 * 10));
			map.put(COMMON_RANDOM_ITEM.getItemSupplier().get(bundle, bet, 10, version), -size + (size += 640 * 15));
			map.put(RARE_RANDOM_ITEM.getItemSupplier().get(bundle, bet, 10, version), -size + (size += 640 * 4));
			map.put(LEGENDARY_RANDOM_ITEM.getItemSupplier().get(bundle, bet, 1, version), -size + (size += 640 * 10));
			map.put(CONTAINMENT_PICKAXE.getItemSupplier().get(bundle, bet, 20.0F, version), -size + (size += 640 * 4));
			map.put(CONTAINMENT_PICKAXE.getItemSupplier().get(bundle, bet, 40.0F, version), -size + (size += 512 * 4));
			map.put(CONTAINMENT_PICKAXE.getItemSupplier().get(bundle, bet, 60.0F, version), -size + (size += 384 * 4));
			map.put(CONTAINMENT_PICKAXE.getItemSupplier().get(bundle, bet, 80.0F, version), -size + (size += 256 * 4));
			map.put(CONTAINMENT_PICKAXE.getItemSupplier().get(bundle, bet, 100.0F, version), -size + (size += 128 * 4));
			map.put(NUGGET.getItemSupplier().get(bundle, bet, 1, version), -size + (size += 1536));
			map.put(NUGGET.getItemSupplier().get(bundle, bet, 2, version), -size + (size += 1792));
			map.put(NUGGET.getItemSupplier().get(bundle, bet, 3, version), -size + (size += 2048));
			map.put(NUGGET.getItemSupplier().get(bundle, bet, 4, version), -size + (size += 2304));
			map.put(NUGGET.getItemSupplier().get(bundle, bet, 5, version), -size + (size += 2560));
			map.put(INGOT.getItemSupplier().get(bundle, bet, 1, version), -size + (size += 1280));
			map.put(INGOT.getItemSupplier().get(bundle, bet, 2, version), -size + (size += 1024));
			map.put(INGOT.getItemSupplier().get(bundle, bet, 3, version), -size + (size += 768));
			map.put(INGOT.getItemSupplier().get(bundle, bet, 4, version), -size + (size += 512));
			map.put(INGOT.getItemSupplier().get(bundle, bet, 5, version), -size + (size += 256));
			map.put(BLOCK.getItemSupplier().get(bundle, bet, 1, version), -size + (size += 64));
			map.put(BLOCK.getItemSupplier().get(bundle, bet, 2, version), -size + (size += 32));
			map.put(BLOCK.getItemSupplier().get(bundle, bet, 3, version), -size + (size += 16));
			map.put(BLOCK.getItemSupplier().get(bundle, bet, 4, version), -size + (size += 8));
			map.put(BLOCK.getItemSupplier().get(bundle, bet, 5, version), -size + (size += 4));
			map.put($BTA.getItemSupplier().get(bundle, bet, 1, version), -size + (size += 4096));
			map.put($BTA.getItemSupplier().get(bundle, bet, 2, version), -size + (size += 1024));
			map.put($BTA.getItemSupplier().get(bundle, bet, 4, version), -size + (size += 512));
			map.put($BTA.getItemSupplier().get(bundle, bet, 8, version), -size + (size += 256));
			map.put($BTA.getItemSupplier().get(bundle, bet, 16, version), -size + (size += 128));
			map.put($BTA.getItemSupplier().get(bundle, bet, 32, version), -size + (size += 64));
			map.put($BTA.getItemSupplier().get(bundle, bet, 64, version), -size + (size += 32));
			map.put(NONE.getItemSupplier().get(bundle, bet, 1, version), -size + (size += Math.max(0, maxSize - size)));
			
			return map;
		}
	}),
	
	DIAMOND(100000.0D, Material.DIAMOND_BLOCK, (short) 3, (short) 14, (short) 15, 40, ChatColor.AQUA, new BundleSupplier<Map<ItemStack, Integer>>()
	{
		@Override
		public Map<ItemStack, Integer> get(ResourceBundle bundle, Object... args)
		{
			double bet = DIAMOND.getBet();
			int version = args.length > 0 ? (int) args[0] : 47;
			
			int size = 0;
			final int maxSize = 64000;
			
			Map<ItemStack, Integer> map = new LinkedHashMap<>();
			
			map.put(JACKPOT.getItemSupplier().get(bundle, bet, 1, version), -size + (size += 100 * 4));
			map.put(VIP.getItemSupplier().get(bundle, bet, 1, version), -size + (size += 1600 * 4));
			map.put(LEGENDARY_RANDOM_ITEM.getItemSupplier().get(bundle, bet, 10, version), -size + (size += 640 * 20));
			map.put(RANDOM_ITEM.getItemSupplier().get(bundle, bet, 36, version), -size + (size += 640 * 10));
			map.put(CONTAINMENT_PICKAXE.getItemSupplier().get(bundle, bet, 100.0F, version), -size + (size += 1280 * 8));
			map.put(NUGGET.getItemSupplier().get(bundle, bet, 1, version), -size + (size += 1536));
			map.put(NUGGET.getItemSupplier().get(bundle, bet, 2, version), -size + (size += 1792));
			map.put(NUGGET.getItemSupplier().get(bundle, bet, 3, version), -size + (size += 2048));
			map.put(NUGGET.getItemSupplier().get(bundle, bet, 4, version), -size + (size += 2304));
			map.put(NUGGET.getItemSupplier().get(bundle, bet, 5, version), -size + (size += 2560));
			map.put(INGOT.getItemSupplier().get(bundle, bet, 1, version), -size + (size += 1280));
			map.put(INGOT.getItemSupplier().get(bundle, bet, 2, version), -size + (size += 1024));
			map.put(INGOT.getItemSupplier().get(bundle, bet, 3, version), -size + (size += 768));
			map.put(INGOT.getItemSupplier().get(bundle, bet, 4, version), -size + (size += 512));
			map.put(INGOT.getItemSupplier().get(bundle, bet, 5, version), -size + (size += 256));
			map.put(BLOCK.getItemSupplier().get(bundle, bet, 1, version), -size + (size += 64));
			map.put(BLOCK.getItemSupplier().get(bundle, bet, 2, version), -size + (size += 32));
			map.put(BLOCK.getItemSupplier().get(bundle, bet, 3, version), -size + (size += 16));
			map.put(BLOCK.getItemSupplier().get(bundle, bet, 4, version), -size + (size += 8));
			map.put(BLOCK.getItemSupplier().get(bundle, bet, 5, version), -size + (size += 4));
			map.put($BTA.getItemSupplier().get(bundle, bet, 1, version), -size + (size += 4096));
			map.put($BTA.getItemSupplier().get(bundle, bet, 2, version), -size + (size += 1024));
			map.put($BTA.getItemSupplier().get(bundle, bet, 4, version), -size + (size += 512));
			map.put($BTA.getItemSupplier().get(bundle, bet, 8, version), -size + (size += 256));
			map.put($BTA.getItemSupplier().get(bundle, bet, 16, version), -size + (size += 128));
			map.put($BTA.getItemSupplier().get(bundle, bet, 32, version), -size + (size += 64));
			map.put($BTA.getItemSupplier().get(bundle, bet, 64, version), -size + (size += 32));
			map.put(NONE.getItemSupplier().get(bundle, bet, 1, version), -size + (size += Math.max(0, maxSize - size)));
			
			return map;
		}
	}),
	
	EMERALD(1000000.0D, Material.EMERALD_BLOCK, (short) 5, (short) 14, (short) 15, 50, ChatColor.GREEN, new BundleSupplier<Map<ItemStack, Integer>>()
	{
		@Override
		public Map<ItemStack, Integer> get(ResourceBundle bundle, Object... args)
		{
			double bet = EMERALD.getBet();
			int version = args.length > 0 ? (int) args[0] : 47;
			
			int size = 0;
			final int maxSize = 64000;
			
			Map<ItemStack, Integer> map = new LinkedHashMap<>();
			
			map.put(JACKPOT.getItemSupplier().get(bundle, bet, 1, version), -size + (size += 1000 * 8));
			map.put(VIP.getItemSupplier().get(bundle, bet, 10, version), -size + (size += 1600 * 8));
			map.put(LEGENDARY_RANDOM_ITEM.getItemSupplier().get(bundle, bet, 36, version), -size + (size += 640 * 30));
			map.put(NUGGET.getItemSupplier().get(bundle, bet, 1, version), -size + (size += 1536));
			map.put(NUGGET.getItemSupplier().get(bundle, bet, 2, version), -size + (size += 1792));
			map.put(NUGGET.getItemSupplier().get(bundle, bet, 3, version), -size + (size += 2048));
			map.put(NUGGET.getItemSupplier().get(bundle, bet, 4, version), -size + (size += 2304));
			map.put(NUGGET.getItemSupplier().get(bundle, bet, 5, version), -size + (size += 2560));
			map.put(INGOT.getItemSupplier().get(bundle, bet, 1, version), -size + (size += 1280));
			map.put(INGOT.getItemSupplier().get(bundle, bet, 2, version), -size + (size += 1024));
			map.put(INGOT.getItemSupplier().get(bundle, bet, 3, version), -size + (size += 768));
			map.put(INGOT.getItemSupplier().get(bundle, bet, 4, version), -size + (size += 512));
			map.put(INGOT.getItemSupplier().get(bundle, bet, 5, version), -size + (size += 256));
			map.put(BLOCK.getItemSupplier().get(bundle, bet, 1, version), -size + (size += 64));
			map.put(BLOCK.getItemSupplier().get(bundle, bet, 2, version), -size + (size += 32));
			map.put(BLOCK.getItemSupplier().get(bundle, bet, 3, version), -size + (size += 16));
			map.put(BLOCK.getItemSupplier().get(bundle, bet, 4, version), -size + (size += 8));
			map.put(BLOCK.getItemSupplier().get(bundle, bet, 5, version), -size + (size += 4));
			map.put($BTA.getItemSupplier().get(bundle, bet, 1, version), -size + (size += 4096));
			map.put($BTA.getItemSupplier().get(bundle, bet, 2, version), -size + (size += 1024));
			map.put($BTA.getItemSupplier().get(bundle, bet, 4, version), -size + (size += 512));
			map.put($BTA.getItemSupplier().get(bundle, bet, 8, version), -size + (size += 256));
			map.put($BTA.getItemSupplier().get(bundle, bet, 16, version), -size + (size += 128));
			map.put($BTA.getItemSupplier().get(bundle, bet, 32, version), -size + (size += 64));
			map.put($BTA.getItemSupplier().get(bundle, bet, 64, version), -size + (size += 32));
			map.put(NONE.getItemSupplier().get(bundle, bet, 1, version), -size + (size += Math.max(0, maxSize - size)));
			
			return map;
		}
	}),
	
	
	;
	
	private final double bet;
	private final ChatColor color;
	private final BundleSupplier<Map<ItemStack, Integer>> mapSupplier;
	private final short middleGlass;
	private final short glass1;
	private final short glass2;
	private final int time;
	private final Material material;
	
	JackpotType(double bet, Material material, short middleGlass, short glass1, short glass2, int time, ChatColor color, BundleSupplier<Map<ItemStack, Integer>> mapSupplier)
	{
		this.bet = bet;
		this.material = material;
		this.middleGlass = middleGlass;
		this.glass1 = glass1;
		this.glass2 = glass2;
		this.time = time;
		this.color = color;
		this.mapSupplier = mapSupplier;
	}
	
	public static JackpotType getByMaterial(Material material)
	{
		for(JackpotType value : values())
		{
			if(value.material == material)
			{
				return value;
			}
		}
		
		return null;
	}
	
	public JackpotType getNext()
	{
		JackpotType[] values = values();
		
		for(int i = 0; i + 1 < values.length; i++)
		{
			if(values[i] == this)
			{
				return values[i + 1];
			}
		}
		
		return null;
	}
	
	public JackpotType getPrevious()
	{
		JackpotType[] values = values();
		
		for(int i = values.length - 1; i - 1 >= 0; i--)
		{
			if(values[i] == this)
			{
				return values[i - 1];
			}
		}
		
		return null;
	}
	
	public Material getMaterial()
	{
		return material;
	}
	
	public ChatColor getColor()
	{
		return color;
	}
	
	public double getBet()
	{
		return bet;
	}
	
	public short getGlass1()
	{
		return glass1;
	}
	
	public short getGlass2()
	{
		return glass2;
	}
	
	public short getMiddleGlass()
	{
		return middleGlass;
	}
	
	public int getTime()
	{
		return time;
	}
	
	public BundleSupplier<Map<ItemStack, Integer>> getMapSupplier()
	{
		return mapSupplier;
	}
}