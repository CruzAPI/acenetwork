package br.com.acenetwork.craftlandia;

import static br.com.acenetwork.craftlandia.Rarity.COMMON;
import static br.com.acenetwork.craftlandia.Rarity.LEGENDARY;
import static br.com.acenetwork.craftlandia.Rarity.RARE;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Damageable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.Repairable;
import org.bukkit.metadata.FixedMetadataValue;

import br.com.acenetwork.commons.manager.CommonsConfig;
import br.com.acenetwork.commons.manager.CommonsConfig.Type;

public class Util
{
	public static Rarity getRarity(World w)
	{
		switch(w.getName())
		{
		case "world":
			return LEGENDARY;
		case "factions":
		case "factions_nether":
		case "factions_the_end":
			return RARE;
		default:
			return COMMON;
		}
	}
	
	public static Rarity getRarity(ItemStack item)
	{
		if(!item.hasItemMeta())
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
	
	public static byte readBlockRarity(Block b)
	{
		if(!b.hasMetadata("pos"))
		{
			return 0;
		}
		
		File file = CommonsConfig.getFile(Type.BLOCK_DATA, true, b.getWorld().getName());
		
		try(RandomAccessFile access = new RandomAccessFile(file, "r"))
		{
			access.seek(b.getMetadata("pos").get(0).asLong() + 12L);
			return access.readByte();
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
			return 0;
		}
	}
	
	public static byte readBlockProperties(Block b)
	{
		if(!b.hasMetadata("pos"))
		{
			return 0;
		}
		
		File file = CommonsConfig.getFile(Type.BLOCK_DATA, true, b.getWorld().getName());
		
		try(RandomAccessFile access = new RandomAccessFile(file, "r"))
		{
			access.seek(b.getMetadata("pos").get(0).asLong() + 13L);
			return access.readByte();
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
			return 0;
		}
	}
	
	public static UUID readSign(Block b)
	{
		if(!b.hasMetadata("signPos"))
		{
			return null;
		}
		
		File file = CommonsConfig.getFile(Type.SIGN_DATA, true, b.getWorld().getName());
		
		try(RandomAccessFile access = new RandomAccessFile(file, "r"))
		{
			access.seek(b.getMetadata("signPos").get(0).asLong() + 12L);
			
			String string = "";
			
			for(int i = 0; i < 36; i++)
			{
				string += access.readChar();
			}
			
			return UUID.fromString(string);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			return null;
		}
	}
	
	public static void writeBlock(Block b, byte data)
	{
		File file = CommonsConfig.getFile(Type.BLOCK_DATA, true, b.getWorld().getName());
		
		try(RandomAccessFile access = new RandomAccessFile(file, "rw"))
		{
			long pos;
			
			if(b.hasMetadata("pos"))
			{
				pos = b.getMetadata("pos").get(0).asLong();
			}
			else
			{
				pos = access.length();
				b.setMetadata("pos", new FixedMetadataValue(Main.getInstance(), pos));
			}
			
			access.seek(pos);
			access.writeInt(b.getX());
			access.writeInt(b.getY());
			access.writeInt(b.getZ());
			access.writeByte(data);
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
	}
	
	public static void writeSign(Block b, UUID uuid)
	{
		File file = CommonsConfig.getFile(Type.SIGN_DATA, true, b.getWorld().getName());
		
		try(RandomAccessFile access = new RandomAccessFile(file, "rw"))
		{
			long pos;
			
			if(b.hasMetadata("signPos"))
			{
				pos = b.getMetadata("signPos").get(0).asLong();
			}
			else
			{
				pos = access.length();
				b.setMetadata("signPos", new FixedMetadataValue(Main.getInstance(), pos));
			}
			
			access.seek(pos);
			access.writeInt(b.getX());
			access.writeInt(b.getY());
			access.writeInt(b.getZ());
			access.writeChars(uuid.toString());
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
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
	
	public static boolean isShoppable(ItemStack shop, ItemStack toCompare)
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
			
			if(Util.containsItemTag(shop, Property.SOLD) && repairable.getRepairCost() != 0)
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
}