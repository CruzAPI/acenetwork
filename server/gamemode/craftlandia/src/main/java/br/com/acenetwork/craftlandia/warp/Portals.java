package br.com.acenetwork.craftlandia.warp;

import org.bukkit.Location;
import org.bukkit.World;

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
}