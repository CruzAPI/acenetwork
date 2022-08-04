package br.com.acenetwork.craftlandia.manager;

import java.io.Serializable;

import org.bukkit.Location;

public class LandEntityData implements Serializable
{
	private static final long serialVersionUID = 1361053351313371972L;
	
	private final int landId;
	private transient Land land;
	private transient Location lastLocation;
	
	public LandEntityData(int landId, Location location)
	{
		this.landId = landId;
		this.land = Land.getById(landId);
		this.lastLocation = location;
	}
	
	public Land getLand()
	{
		return land == null ? land = Land.getById(landId) : land;
	}
	
	public int getLandId()
	{
		return landId;
	}
	
	public Location getLastLocation()
	{
		return lastLocation;
	}
	
	public void setLastLocation(Location lastLocation)
	{
		this.lastLocation = lastLocation;
	}
	
	@Override
	public String toString()
	{
		return "LandEntityData [landId=" + landId + ", lastLocation=" + lastLocation + "]";
	}
}
