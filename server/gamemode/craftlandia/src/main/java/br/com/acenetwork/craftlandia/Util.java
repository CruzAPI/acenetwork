package br.com.acenetwork.craftlandia;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import br.com.acenetwork.commons.manager.CommonsConfig;
import br.com.acenetwork.commons.manager.CommonsConfig.Type;

public class Util
{
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
		
		for(ItemTag itemTag : ItemTag.values())
		{
			if(item.getItemMeta().getLore().contains(itemTag.toString()))
			{
				return itemTag;
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
}