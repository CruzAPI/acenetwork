package br.com.acenetwork.craftlandia;

import net.md_5.bungee.api.ChatColor;

public enum ItemTag 
{
	COMMON("" + ChatColor.GREEN + ChatColor.BOLD), 
	RARE("" + ChatColor.DARK_PURPLE + ChatColor.BOLD), 
	LEGENDARY("" + ChatColor.GOLD + ChatColor.BOLD), 
	;
	
	private final String tag;
	
	ItemTag(String color)
	{
		tag = color + name();
	}
	
	@Override
	public String toString()
	{
		return tag;
	}
}
