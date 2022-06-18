package br.com.acenetwork.craftlandia;

import static br.com.acenetwork.craftlandia.ItemTag.COMMON;
import static br.com.acenetwork.craftlandia.ItemTag.LEGENDARY;
import static br.com.acenetwork.craftlandia.ItemTag.RARE;
import static org.mockito.Mockito.verifyNoMoreInteractions;

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
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

import br.com.acenetwork.commons.manager.CommonsConfig;
import br.com.acenetwork.commons.manager.CommonsConfig.Type;

public class Util
{
	public static ItemTag getRarity(World w)
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
	
	public static ItemTag getRarity(ItemStack item)
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
			for(ItemTag itemTag : ItemTag.values())
			{
				if(line.equals(itemTag.toString()))
				{
					return itemTag;
				}
			}
		}
		
		return null;
	}
	
	public static byte readBlock(Block b)
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
	
	public static void setRarity(ItemStack itemStack, ItemTag rarity)
	{
		ItemMeta meta = itemStack.getItemMeta();
		
		List<String> lore = new ArrayList<>();
		
		boolean set = false;
		
		if(meta.hasLore())
		{
			loop:for(String line : meta.getLore())
			{
				for(ItemTag rarities : ItemTag.values())
				{
					if(line.equals(rarities.toString()))
					{
						if(!set)
						{
							lore.add(rarity.toString());
						}
						
						set = true;
						continue loop;
					}
				}
				
				lore.add(line);
			}
		}
		
		if(!set)
		{
			lore.add(rarity.toString());
		}
		
		meta.setLore(lore);
		itemStack.setItemMeta(meta);
	}

	public static void addRarity(ItemStack itemStack, ItemTag rarity)
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
				ItemTag.valueOf(line);
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

	public static ItemTag getLastRarity(ItemStack item)
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
				return ItemTag.valueOf(line);
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
}