package br.com.acenetwork.commons.manager;

import java.io.Serializable;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class BlockLoc implements Serializable
{
	private static final long serialVersionUID = -8890707570298373950L;
	
	private final long mostSigBits;
	private final long leastSigBits;
	private final int x;
	private final int y;
	private final int z;
	
	public BlockLoc(Location l)
	{
		mostSigBits = l.getWorld().getUID().getMostSignificantBits();
		leastSigBits = l.getWorld().getUID().getLeastSignificantBits();
		x = l.getBlockX();
		y = l.getBlockY();
		z = l.getBlockZ();
	}
	
	public int getX()
	{
		return x;
	}
	
	public int getY()
	{
		return y;
	}
	
	public int getZ()
	{
		return z;
	}
	
	public World getWorld()
	{
		return Bukkit.getWorld(new UUID(mostSigBits, leastSigBits));
	}
}
