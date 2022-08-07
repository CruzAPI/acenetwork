package br.com.acenetwork.survival;

import org.bukkit.World;

import net.md_5.bungee.api.ChatColor;

public enum Rarity implements ItemTag
{
	COMMON("" + ChatColor.GREEN + ChatColor.BOLD, (byte) 1, 1), 
	RARE("" + ChatColor.DARK_PURPLE + ChatColor.BOLD, (byte) 2, 10), 
	LEGENDARY("" + ChatColor.GOLD + ChatColor.BOLD, (byte) 3, 100), 
	;
	
	private final String tag;
	private final byte data;
	private final String color;
	private final int multiplierAdminShop;
	
	Rarity(String color, byte data, int multiplierAdminShop)
	{
		tag = color + name();
		this.color = color;
		this.data = data;
		this.multiplierAdminShop = multiplierAdminShop;
		SET.add(this);
	}
	
	public int getMultiplierAdminShop()
	{
		return multiplierAdminShop;
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
