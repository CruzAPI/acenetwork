package br.com.acenetwork.craftlandia;

import net.md_5.bungee.api.ChatColor;

public enum ItemTag 
{
	COMMON("" + ChatColor.GREEN + ChatColor.BOLD, (byte) 1), 
	RARE("" + ChatColor.DARK_PURPLE + ChatColor.BOLD, (byte) 2), 
	LEGENDARY("" + ChatColor.GOLD + ChatColor.BOLD, (byte) 3), 
	;
	
	private final String tag;
	private final byte data;
	
	ItemTag(String color, byte data)
	{
		tag = color + name();
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
}
