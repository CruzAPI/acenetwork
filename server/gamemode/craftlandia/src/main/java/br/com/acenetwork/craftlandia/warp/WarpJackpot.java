package br.com.acenetwork.craftlandia.warp;

import org.bukkit.Location;
import org.bukkit.World;

public class WarpJackpot extends Warp
{
	private final Location spawnLocation;
	
	public WarpJackpot(World w)
	{
		super(w);
		
		spawnLocation = new Location(w, 0.0D, 69.0D, 0.0D, 0.0F, 0.0F);
	}
}