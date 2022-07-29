package br.com.acenetwork.commons.manager;

import java.io.Serializable;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class LocationSerializable implements Serializable
{
	private static final long serialVersionUID = 7821870627566438837L;
	
	private long mostSigBits;
	private long leastSigBits;
	private double x;
	private double y;
	private double z;
	private float yaw;
	private float pitch;
	
	public LocationSerializable(Location l)
	{
		mostSigBits = l.getWorld().getUID().getMostSignificantBits();
		leastSigBits = l.getWorld().getUID().getLeastSignificantBits();
		x = l.getBlockX();
		y = l.getBlockY();
		z = l.getBlockZ();
		yaw = l.getYaw();
		pitch = l.getPitch();
	}
	
	public Location getLocation()
	{
		return new Location(Bukkit.getWorld(new UUID(mostSigBits, leastSigBits)), x, y, z, yaw, pitch);
	}
}