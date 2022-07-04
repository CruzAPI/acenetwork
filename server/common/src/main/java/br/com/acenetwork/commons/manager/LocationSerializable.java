package br.com.acenetwork.commons.manager;

import java.io.Serializable;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class LocationSerializable implements Serializable
{
	private static final long serialVersionUID = -8219124607473025384L;
	
	private String w;
	private int x;
	private int y;
	private int z;
	
	public LocationSerializable(Location l)
	{
		w = l.getWorld().getName();
		x = l.getBlockX();
		y = l.getBlockY();
		z = l.getBlockZ();
	}
	
	public Location getLocation()
	{
		return new Location(Bukkit.getWorld(w), x, y, z);
	}
}