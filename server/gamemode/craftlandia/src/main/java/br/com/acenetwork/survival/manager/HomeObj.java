package br.com.acenetwork.craftlandia.manager;

import java.io.Serializable;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class HomeObj implements Serializable
{
	private static final long serialVersionUID = 4062212327249555557L;
	
	private long mostSigBits;
	private long leastSigBits;
	private double x;
	private double y;
	private double z;
	private float yaw;
	private float pitch;
	private boolean isPublic;
	
	public HomeObj(Location l, boolean isPublic)
	{
		mostSigBits = l.getWorld().getUID().getMostSignificantBits();
		leastSigBits = l.getWorld().getUID().getLeastSignificantBits();
		x = l.getX();
		y = l.getY();
		z = l.getZ();
		yaw = l.getYaw();
		pitch = l.getPitch();
		this.isPublic = isPublic;
	}
	
	public HomeObj(Location l)
	{
		this(l, false);
	}

	public void setLocation(Location l)
	{
		mostSigBits = l.getWorld().getUID().getMostSignificantBits();
		leastSigBits = l.getWorld().getUID().getLeastSignificantBits();
		x = l.getBlockX();
		y = l.getBlockY();
		z = l.getBlockZ();
		yaw = l.getYaw();
		pitch = l.getPitch();
	}
	
	public void setPublic(boolean isPublic)
	{
		this.isPublic = isPublic;
	}
	
	public boolean isPublic()
	{
		return isPublic;
	}
	
	public World getWorld()
	{
		return Bukkit.getWorld(new UUID(mostSigBits, leastSigBits));
	}
	
	public Location getLocation()
	{
		return new Location(getWorld(), x, y, z, yaw, pitch);
	}
}