package br.com.acenetwork.craftlandia;

import org.bukkit.World;

import net.md_5.bungee.api.ChatColor;

public enum ItemTag 
{
	COMMON("" + ChatColor.GREEN + ChatColor.BOLD, (byte) 1), 
	RARE("" + ChatColor.DARK_PURPLE + ChatColor.BOLD, (byte) 2), 
	LEGENDARY("" + ChatColor.GOLD + ChatColor.BOLD, (byte) 3), 
	;
	
	private final String tag;
	private final byte data;
	private final String color;
	
	ItemTag(String color, byte data)
	{
		tag = color + name();
		this.color = color;
		this.data = data;
	}
	
	@Override
	public String toString()
	{
		return tag;
	}

	public byte getData()
	{
		return data;
	}
	
	public static ItemTag getByData(short data)
	{
		for(ItemTag itemTag : values())
		{
			if(itemTag.data == data)
			{
				return itemTag;
			}
		}
		
		return null;
	}
	
	public String getColor()
	{
		return color;
	}
	
	public static ItemTag getByDataOrWorld(byte data, World world)
	{
		ItemTag rarity = getByData(data);
		rarity = rarity == null ? Util.getRarity(world) : rarity;
		return rarity;
	}
}
