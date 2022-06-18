package br.com.acenetwork.craftlandia.manager;

import net.md_5.bungee.api.ChatColor;

public enum Currency
{
	$BTA("bta", "" + ChatColor.DARK_PURPLE + ChatColor.BOLD), SHARDS("balance", "" + ChatColor.DARK_BLUE + ChatColor.BOLD)
	;
	
	private final String color;
	private final String key;
	
	private Currency(String key, String color)
	{
		this.key = key;
		this.color = color;
	}
	
	public String getColor()
	{
		return color;
	}

	public String getYamlKey()
	{
		return key;
	}
}