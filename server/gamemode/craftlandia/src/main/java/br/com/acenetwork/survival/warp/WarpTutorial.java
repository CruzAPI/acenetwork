package br.com.acenetwork.craftlandia.warp;

import org.bukkit.Location;
import org.bukkit.World;

import net.md_5.bungee.api.ChatColor;

public class WarpTutorial extends Warp
{
	private final Location spawnLocation;
	
	public WarpTutorial(World w)
	{
		super(w);
		
		spawnLocation = new Location(w, 0.5D, 102.0D, 0.5D, 0.0F, 0.0F);
	}
	
	@Override
	public Location getSpawnLocation()
	{
		return spawnLocation;
	}
	
	@Override
	public boolean isSpawnProtection(Location l)
	{
		return true;
	}
	
	@Override
	public String getColoredName()
	{
		return ChatColor.GREEN + "Tutorial";
	}
}