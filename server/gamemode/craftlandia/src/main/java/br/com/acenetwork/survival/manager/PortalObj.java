package br.com.acenetwork.craftlandia.manager;

import java.io.Serializable;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import br.com.acenetwork.craftlandia.executor.Portal;

public class PortalObj implements Serializable
{
	private static final long serialVersionUID = -4811192992776828584L;
	
	private byte id;
	private byte linkedId;
	private long mostSigBits;
	private long leastSigBits;
	private double x;
	private double y;
	private double z;
	private float yaw;
	
	public PortalObj(byte id, World w, double x, double y, double z, float yaw)
	{
		this.id = id;
		this.linkedId = id;
		this.mostSigBits = w.getUID().getMostSignificantBits();
		this.leastSigBits = w.getUID().getLeastSignificantBits();
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = yaw;
	}
	
	public byte getId()
	{
		return id;
	}
	
	public PortalObj getLinkedPortal()
	{
		return linkedId == id ? null : Portal.getInstance().getPortal(linkedId);
	}
	
	public void setLinkedPortal(PortalObj portal)
	{
		setLinkedPortal(portal.id);
	}
	
	public void setLinkedPortal(byte id)
	{
		this.linkedId = id;
	}
	
	public World getWorld()
	{
		return Bukkit.getWorld(new UUID(mostSigBits, leastSigBits));
	}
	
	public double getX()
	{
		return x;
	}
	
	public double getY()
	{
		return y;
	}
	
	public double getZ()
	{
		return z;
	}
	
	public Location getLocation()
	{
		return new Location(getWorld(), x, y, z, yaw, 0.0F);
	}
}
