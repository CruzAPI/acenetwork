package br.com.acenetwork.craftlandia.warp;

import org.bukkit.Location;
import org.bukkit.World;

public class Newbie extends Warp
{
	private final Location spawnLocation;
	
	public Newbie(World w)
	{
		super(w);
		
		spawnLocation = new Location(w, 0.5D, 69.0D, 0.5D, 0.0F, 0.0F);
	}
	
	@Override
	public boolean isSpawnProtection(Location l)
	{
		return Math.abs(l.getBlockX()) < 256 && Math.abs(l.getBlockZ()) < 256;
	}
	
	@Override
	public Location getSpawnLocation()
	{
		return spawnLocation;
	}
	
	@Override
	public boolean canSetHome()
	{
		return true;
	}
	
	@Override
	public int blocksAwayFromSpawnToSetHome()
	{
		return 1024;
	}
}