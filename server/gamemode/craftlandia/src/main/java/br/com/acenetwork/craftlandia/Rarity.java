package br.com.acenetwork.craftlandia;

import org.bukkit.World;

import net.md_5.bungee.api.ChatColor;

public enum Rarity implements ItemTag
{
	COMMON("" + ChatColor.GREEN + ChatColor.BOLD, (byte) 1), 
	RARE("" + ChatColor.DARK_PURPLE + ChatColor.BOLD, (byte) 2), 
	LEGENDARY("" + ChatColor.GOLD + ChatColor.BOLD, (byte) 3), 
	;
	
	private final String tag;
	private final byte data;
	private final String color;
	
	Rarity(String color, byte data)
	{
		tag = color + name();
		this.color = color;
		this.data = data;
		SET.add(this);
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
	
	public static Rarity valueOfToString(String toString)
	{
		for(Rarity rarity : Rarity.values())
		{
			if(rarity.toString().equals(toString))
			{
				return rarity;
			}
		}
		
		return null;
	}
	
	public static Rarity getByData(byte data)
	{
		for(Rarity itemTag : values())
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
	
	public static Rarity getByDataOrWorld(byte data, World world)
	{
		Rarity rarity = getByData(data);
		return rarity == null ? Util.getRarity(world) : rarity;
	}
}
