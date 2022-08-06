package br.com.acenetwork.survival.warp;

import org.bukkit.Location;
import org.bukkit.World;

import net.md_5.bungee.api.ChatColor;

public class Portals extends Warp
{
	private final Location spawnLocation;
	
	public Portals(World w)
	{
		super(w);
		
		spawnLocation = new Location(w, 0.5D, 103.0D, -17.5D, 0.0F, 0.0F);
	}
	
	@Override
	public Location getSpawnLocation()
	{
		return spawnLocation;
	}
	
	@Override
	public String getColoredName()
	{
		return ChatColor.LIGHT_PURPLE + "Portal Center";
	}
}