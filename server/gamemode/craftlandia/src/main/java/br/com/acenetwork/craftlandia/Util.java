package br.com.acenetwork.craftlandia;

import static br.com.acenetwork.craftlandia.Rarity.COMMON;
import static br.com.acenetwork.craftlandia.Rarity.LEGENDARY;
import static br.com.acenetwork.craftlandia.Rarity.RARE;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.Repairable;
import org.bukkit.metadata.FixedMetadataValue;

import com.google.common.io.ByteStreams;

import br.com.acenetwork.commons.manager.CommonsConfig;
import br.com.acenetwork.commons.manager.CommonsConfig.Type;
import br.com.acenetwork.craftlandia.manager.BlockData;
import br.com.acenetwork.craftlandia.manager.BreakReason;
import br.com.acenetwork.craftlandia.manager.ChunkLocation;
import br.com.acenetwork.craftlandia.manager.Config;
import br.com.acenetwork.craftlandia.warp.Warp;

public class Util
{
	public static Rarity getRarity(World w)
	{
		switch(w.getName())
		{
		case "world":
		case "oldworld":
			return LEGENDARY;
		case "factions":
		case "factions_nether":
		case "factions_the_end":
			return RARE;
		default:
			return COMMON;
		}
	}
	
	public static byte[] toByteArray(short x)
	{
	    return new byte[] {(byte) (x >> 8), (byte) x};
	}
	
	public static short toShort(byte[] a)
	{
		return (short) (a[0] << 8 | a[1] & 0xFF);
	}
	
	public static Rarity getRarity(Entity entity)
	{
		if(entity.getCustomName() == null)
		{
			return null;
		}
		
		return Rarity.valueOfToString(entity.getCustomName());	
	}
	
	public static Rarity getRarity(ItemStack item)
	{
		if(item == null)
		{
			return null;
		}
		
		ItemMeta meta = item.getItemMeta();
		
		if(meta == null)
		{
			return null;
		}
		
		if(!item.getItemMeta().hasLore())
		{
			return null;
		}
		
		for(String line : item.getItemMeta().getLore())
		{
			for(Rarity itemTag : Rarity.values())
			{
				if(line.equals(itemTag.toString()))
				{
					return itemTag;
				}
			}
		}
		
		return null;
	}
	
	public static List<String> getLore(Block b)
	{
		return getLore(Util.readBlock(b), b.getWorld());
	}
	public static List<String> getLore(BlockData data, World world)
	{
		Rarity rarity = data == null || data.getRarity() == null ? getRarity(world) : data.getRarity();
		
		List<String> lore = new ArrayList<>();
		
		lore.add(rarity.toString());
		
		if(data != null && data.getProperties() != null)
		{
			for(Property property : data.getProperties())
			{
				lore.add(property.toString());
			}
		}
		
		return lore;
	}
	
	public static BlockData readBlock(Block b)
	{
		return Warp.MAP.get(b.getWorld().getUID()).readBlock(b);
	}
	
	public static BlockData writeBlock(Block b, BlockData data)
	{
		return Warp.MAP.get(b.getWorld().getUID()).writeBlock(b, data);
	}
	
	public static byte[] copyArrays(byte[]... arrays)
	{
		int length = 0;
		
		for(byte[] array : arrays)
		{
			length += array.length;
		}
		
		byte[] copy = new byte[length];
		
		for(int i = 0, destPos = 0; i < arrays.length && destPos < copy.length; destPos += arrays[i++].length)
		{
			System.arraycopy(arrays[i], 0, copy, destPos, arrays[i].length);
		}
		
		return copy;
	}
	
	public static int getArrayLength()
	{
		return 1 + Property.getByteArrayLength();
	}
	
	public static byte[] emptyArray()
	{
		byte[] emptyArray = new byte[getArrayLength()];
		
		for(int i = 0; i < emptyArray.length; i++)
		{
			emptyArray[i] = Byte.MIN_VALUE;
		}
		
		return emptyArray;
	}
	
	public static short chunkCoordsToShort(Block b)
	{
		Chunk c = b.getChunk();
		
		byte x = (byte) Math.abs(c.getX() * 16 - b.getX());
		byte y = (byte) (b.getY() - 128);
		byte z = (byte) Math.abs(c.getZ() * 16 - b.getZ());
		
		return coordsToShort(new byte[] {x, y, z});
	}
	
	private static byte[] getChunkBlockCoords(short s)
	{
		byte[] b = toByteArray(s);
		
		byte y = b[0];
		byte x = (byte) (b[1] >> 4 & 0xF);
		byte z = (byte) (b[1] & 0xF);
		
		return new byte[] {x, y, z};
	}
	
	private static short coordsToShort(byte[] coords)
	{
		return (short) (coords[1] << 8 | coords[0] << 4 & 0xF0 | coords[2] & 0x0F);
	}
	
	public static byte[] getByteArray(ItemStack item)
	{
		ItemMeta meta = item.getItemMeta();
		
		if(!meta.hasLore())
		{
			return emptyArray();
		}
		
		List<String> lore = meta.getLore();
		
		ListIterator<String> iterator = lore.listIterator(lore.size());
		
		byte[] bytes = emptyArray();
		
		while(iterator.hasPrevious())
		{
			Rarity rarity = Rarity.valueOfToString(iterator.previous());
			
			if(rarity != null)
			{
				bytes[0] = rarity.getData();
				break;
			}
		}
		
		byte[] propertyArray = Property.getByteArray(getProperties(item));
		
		for(int i = 0; i + 1 < bytes.length && i < propertyArray.length; i++)
		{
			bytes[i + 1] = propertyArray[i];
		}
		
		return bytes;
	}
	
	public static void setCommodity(ItemStack item, Rarity rarity)
	{
		ItemMeta meta = item.getItemMeta();
		
		if(meta == null)
		{
			return;
		}
		
		List<String> lore = new ArrayList<>();
		
		if(meta.hasLore())
		{
			lore = meta.getLore();
		}
		
		for(Rarity values : Rarity.values())
		{
			lore.remove(values.toString());
		}
		
		List<String> newLore = new ArrayList<>();
		
		if(rarity != null)
		{
			newLore.add(rarity.toString());
		}
		
		newLore.addAll(lore);
		
		meta.setLore(newLore);
		item.setItemMeta(meta);
	}
	
	public static void setItemTag(ItemStack itemStack, ItemTag itemTag)
	{
		ItemMeta meta = itemStack.getItemMeta();
		
		List<String> lore = new ArrayList<>();
		
		if(meta.hasLore())
		{
			lore = meta.getLore();
		}
		
		if(lore.contains(itemTag.toString()))
		{
			return;
		}
		
		lore.add(itemTag.toString());
		meta.setLore(lore);
		itemStack.setItemMeta(meta);
	}

	public static void addRarity(ItemStack itemStack, Rarity rarity)
	{
		if(rarity == null)
		{
			return;
		}
		
		ItemMeta meta = itemStack.getItemMeta();
		
		List<String> lore = new ArrayList<>();
		
		if(meta.hasLore())
		{
			lore = meta.getLore();
		}
		
		lore.add(rarity.toString());
		
		meta.setLore(lore);
		itemStack.setItemMeta(meta);
	}
	
	public static void removeLastRarity(ItemStack itemStack)
	{
		ItemMeta meta = itemStack.getItemMeta();
		
		List<String> lore = new ArrayList<>();
		
		if(meta.hasLore())
		{
			lore = meta.getLore();
		}
		
		ListIterator<String> iterator = lore.listIterator(lore.size());
		
		while(iterator.hasPrevious())
		{
			String line = iterator.previous();
			
			try
			{
				Rarity.valueOf(line);
				iterator.remove();
				break;
			}
			catch(IllegalArgumentException e)
			{
				continue;
			}
		}
		
		meta.setLore(lore);
		itemStack.setItemMeta(meta);
	}

	public static Rarity getLastRarity(ItemStack item)
	{
		ItemMeta meta = item.getItemMeta();
		
		List<String> lore = new ArrayList<>();
		
		if(meta.hasLore())
		{
			lore = meta.getLore();
		}
		
		ListIterator<String> iterator = lore.listIterator(lore.size());
		
		while(iterator.hasPrevious())
		{
			String line = iterator.previous();
			
			try
			{
				return Rarity.valueOf(line);
			}
			catch(IllegalArgumentException e)
			{
				continue;
			}
		}
		
		return null;
	}
	
	public static Object getPrivateField(String fieldName, Class<?> clazz, Object object)
	{
		Field field;
		Object o = null;
		
		try
		{
			field = clazz.getDeclaredField(fieldName);
			
			field.setAccessible(true);
			
			o = field.get(object);
		}
		catch(NoSuchFieldException e)
		{
			e.printStackTrace();
		}
		catch(IllegalAccessException e)
		{
			e.printStackTrace();
		}
		
		return o;
	}
	
	public static boolean isShoppable(ItemStack shop, ItemStack toCompare, boolean isSold)
	{
		if(shop.getType() != toCompare.getType())
		{
			Bukkit.broadcastMessage("A");
			return false;
		}
		
		if(shop.getDurability() != toCompare.getDurability())
		{
			Bukkit.broadcastMessage("B");
			return false;
		}
		
		ItemMeta shopMeta = shop.getItemMeta();
		ItemMeta toCompareMeta = toCompare.getItemMeta();
		
		if(shopMeta == null || !shopMeta.hasLore() || toCompareMeta == null || !toCompareMeta.hasLore())
		{
			Bukkit.broadcastMessage("C1");
			return false;
		}
		
		Bukkit.broadcastMessage(shopMeta.getLore().toString());
		Bukkit.broadcastMessage("-----");
		Bukkit.broadcastMessage(toCompareMeta.getLore().toString());
		
		if(!shopMeta.getLore().equals(toCompareMeta.getLore()))
		{
			Bukkit.broadcastMessage("C2");
			return false;
		}
		
		if(!shop.getEnchantments().equals(toCompare.getEnchantments()))
		{
			Bukkit.broadcastMessage("D");
			return false;
		}
		
		if(shopMeta instanceof Repairable)
		{
			Repairable repairable = (Repairable) shopMeta;
			
			if(isSold && repairable.getRepairCost() != 0)
			{
				Bukkit.broadcastMessage("E");
				return false;
			}
			
			if(shop.getDurability() != 0)
			{
				Bukkit.broadcastMessage("F");
				return false;
			}
		}
		
		Bukkit.broadcastMessage("G");
		return true;
	}
	
	private static boolean containsItemTag(ItemStack item, ItemTag tag)
	{
		ItemMeta meta = item.getItemMeta();
		
		List<String> lore = new ArrayList<>();
		
		if(meta.hasLore())
		{
			lore = meta.getLore();
		}
		
		for(String line : lore)
		{
			if(ItemTag.getByTag(line) == tag)
			{
				return true;
			}
		}
		
		return false;
	}
	
	public static Set<Property> getProperties(ItemStack item)
	{
		ItemMeta meta = item.getItemMeta();
		List<String> lore = meta != null && meta.hasLore() ? meta.getLore() : new ArrayList<>();
		return Arrays.stream(Property.values()).filter(x -> lore.contains(x.toString())).collect(Collectors.toSet());
	}
	
	public static Rarity getWorstRarity(Rarity... rarities)
	{
		if(rarities.length == 0)
		{
			return COMMON;
		}
		
		Rarity worst = LEGENDARY;
		
		for(Rarity rarity : rarities)
		{
			if(rarity == null || rarity == COMMON)
			{
				return COMMON;
			}
			
			if(rarity.getData() < worst.getData())
			{
				worst = rarity;
			}
		}
		
		return worst;
	}
	
	public static int getMaxLevel(Enchantment enchantment, Rarity rarity)
	{
		switch(rarity)
		{
		case RARE:
		case LEGENDARY:
			if(enchantment.equals(Enchantment.DAMAGE_ALL) 
					|| enchantment.equals(Enchantment.DAMAGE_ARTHROPODS) 
					|| enchantment.equals(Enchantment.DAMAGE_UNDEAD)
					|| enchantment.equals(Enchantment.ARROW_DAMAGE)
					|| enchantment.equals(Enchantment.PROTECTION_ENVIRONMENTAL)
					|| enchantment.equals(Enchantment.PROTECTION_EXPLOSIONS)
					|| enchantment.equals(Enchantment.PROTECTION_PROJECTILE)
					|| enchantment.equals(Enchantment.PROTECTION_FIRE)
					|| enchantment.equals(Enchantment.PROTECTION_FALL)
					|| enchantment.equals(Enchantment.DIG_SPEED))
			{
				return rarity == LEGENDARY ? 10 : 7;
			}
			
			if(enchantment.equals(Enchantment.LOOT_BONUS_MOBS)
					|| enchantment.equals(Enchantment.OXYGEN)
					|| enchantment.equals(Enchantment.ARROW_DAMAGE)
					|| enchantment.equals(Enchantment.THORNS)
					|| enchantment.equals(Enchantment.DURABILITY)
					|| enchantment.equals(Enchantment.LOOT_BONUS_BLOCKS)
					|| enchantment.equals(Enchantment.LURE)
					|| enchantment.equals(Enchantment.LUCK))
			{
				return rarity == LEGENDARY ? 5 : 4;
			}
			
			default:
				return enchantment.getMaxLevel();
		}
	}
}