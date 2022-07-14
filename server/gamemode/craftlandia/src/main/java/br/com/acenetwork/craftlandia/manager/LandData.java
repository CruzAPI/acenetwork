package br.com.acenetwork.craftlandia.manager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import com.google.common.io.ByteStreams;

import br.com.acenetwork.commons.manager.LocationSerializable;
import br.com.acenetwork.craftlandia.manager.Config.Type;

public class LandData implements Serializable
{
	private static final long serialVersionUID = -1004416978143252480L;

	public static Map<Integer, LandData> map = new HashMap<>();
	
	private UUID ownerUUID;
	private Set<UUID> trustedPlayers;
	private boolean isPublic;
	private String name;
	private long resetCooldown;
	
	private LocationSerializable locationSerial;
	
	public LandData()
	{
		
	}
	
	public void setLocation(Location l)
	{
		locationSerial = l == null ? null : new LocationSerializable(l);
	}
	
	public void setOwner(UUID ownerUUID)
	{
		if(this.ownerUUID != (this.ownerUUID = ownerUUID))
		{
			Bukkit.broadcastMessage("!=");
			trustedPlayers = new HashSet<>();
		}
	}
	
	public UUID getOwner()
	{
		return ownerUUID;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name) throws NameAlreadyInUseException
	{
		if(name != null)
		{
			if(map.values().stream().filter(x -> name.equals(x.name)).findAny().isPresent())
			{
				throw new NameAlreadyInUseException();
			}
		}
		
		this.name = name;
	}
	
	public boolean isPublic()
	{
		return isPublic;
	}
	
	public void setPublic(boolean isPublic)
	{
		this.isPublic = isPublic;
	}
	
	public long getResetCooldown()
	{
		return resetCooldown;
	}
	
	public void setResetCooldown(long resetCooldown)
	{
		this.resetCooldown = resetCooldown;
	}
	
	public Set<UUID> getTrustedPlayers()
	{
		return trustedPlayers == null ? trustedPlayers = new HashSet<>() : trustedPlayers;
	}
	
	public boolean isTrusted(UUID uuid)
	{
		return uuid.equals(ownerUUID) || getTrustedPlayers().contains(uuid);
	}
	
	public Location getLocation()
	{
		return locationSerial == null ? null : locationSerial.getLocation();
	}
	
	public static void save()
	{
		File file = Config.getFile(Type.LANDS, true);
		
		try(FileOutputStream fileOut = new FileOutputStream(file);
				ByteArrayOutputStream streamOut = new ByteArrayOutputStream();
				ObjectOutputStream out = new ObjectOutputStream(streamOut))
		{
			out.writeObject(map);
			fileOut.write(streamOut.toByteArray());
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public static void loadAll()
	{
		File file = Config.getFile(Type.LANDS, false);
		
		if(file.exists() && file.length() > 0L)
		{
			try(FileInputStream fileIn = new FileInputStream(file);
					ByteArrayInputStream streamIn = new ByteArrayInputStream(ByteStreams.toByteArray(fileIn));
					ObjectInputStream in = new ObjectInputStream(streamIn))
			{
				map = (Map<Integer, LandData>) in.readObject();
			}
			catch(IOException | ClassNotFoundException ex)
			{
				throw new RuntimeException(ex);
			}
		}
		else
		{
			map = new HashMap<>();
		}
	}
	
	public static LandData load(int id)
	{
		LandData landData = map.get(id);
		
		if(landData != null)
		{
			return landData;
		}
		
		map.put(id, landData = new LandData());
		return landData;
	}
}