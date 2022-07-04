package br.com.acenetwork.commons.manager;

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
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import com.google.common.io.ByteStreams;

import br.com.acenetwork.commons.manager.CommonsConfig.Type;

public class PlayerData implements Listener, Serializable, Cloneable
{
	private static final long serialVersionUID = 5240858812052753505L;

	public static final Map<UUID, PlayerData> MAP = new HashMap<>();
	
	private double balance;
	private double bta;
	private boolean invincibility;
	private transient double diskBalance;
	private transient double diskBTA;
	private transient boolean diskInvincibility;
	
	public PlayerData()
	{
		
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException
	{
		return super.clone();
	}
	
	public PlayerData(PlayerData pd)
	{
		this(pd.diskBalance , pd.diskBTA, pd.diskInvincibility);
	}
	
	public PlayerData(double balance, double bta, boolean invincibility)
	{
		this.balance = balance;
		this.bta = bta;
		this.invincibility = invincibility;
		this.diskBalance = balance;
		this.diskBTA = bta;
		this.diskInvincibility = invincibility;
	}
	
	public boolean hasInvincibility()
	{
		return invincibility;
	}
	
	public void setInvincibility(boolean invincibility)
	{
		this.invincibility = invincibility;
	}	
	
	public double getDiskBalance()
	{
		return diskBalance;
	}
	
	public void setDiskBTA(double diskBTA)
	{
		this.diskBTA = diskBTA;
	}
	
	public double getBalance()
	{
		return balance;
	}
	
	public void setBalance(double balance)
	{
		this.balance = balance;
	}
	
	public double getDiskBTA()
	{
		return diskBTA;
	}
	
	public double getBTA()
	{
		return bta;
	}
	
	public void setBTA(double bta)
	{
		if(bta < 0.0D)
		{
			throw new RuntimeException("can't set negative value");
		}
		
		this.bta = bta;
	}
	
	public static void save()
	{
		save(MAP);
	}
	
	@SuppressWarnings("unchecked")
	public static void save(Map<UUID, PlayerData> toSave)
	{
		File file = CommonsConfig.getFile(Type.PLAYERS_DATA, true);
		
		Map<UUID, PlayerData> diskMap;
		
		if(file.length() > 0L)
		{
			try(FileInputStream fileIn = new FileInputStream(file);
					ByteArrayInputStream streamIn = new ByteArrayInputStream(ByteStreams.toByteArray(fileIn));
					ObjectInputStream in = new ObjectInputStream(streamIn);)
			{
				diskMap = (Map<UUID, PlayerData>) in.readObject();
			}
			catch(ClassNotFoundException | IOException ex)
			{
				throw new RuntimeException(ex);
			}
		}
		else
		{
			diskMap = new HashMap<>();
		}
		
		try(FileOutputStream fileOut = new FileOutputStream(file);
				ByteArrayOutputStream streamOut = new ByteArrayOutputStream();
				ObjectOutputStream out = new ObjectOutputStream(streamOut))
		{
			diskMap.putAll(toSave);
			out.writeObject(diskMap);
		}
		catch(IOException ex)
		{
			throw new RuntimeException(ex);
		}
		
		Iterator<Entry<UUID, PlayerData>> iterator = toSave.entrySet().iterator();
		
		while(iterator.hasNext())
		{
			Entry<UUID, PlayerData> entry = iterator.next();
			
			UUID uuid = entry.getKey();
			PlayerData pd = entry.getValue();
			file = CommonsConfig.getFile(Type.PLAYER_DATA, true, uuid);
			
			try(FileOutputStream fileOut = new FileOutputStream(file);
					ByteArrayOutputStream streamOut = new ByteArrayOutputStream();
					ObjectOutputStream out = new ObjectOutputStream(streamOut))
			{
				out.writeObject(pd);
				pd.setDiskBTA(pd.getBTA());
				fileOut.write(streamOut.toByteArray());
				
				if(!Bukkit.getOfflinePlayer(uuid).isOnline())
				{
					iterator.remove();
				}
			}
			catch(IOException ex)
			{
				throw new RuntimeException(ex);
			}
		}
	}
	
	public static PlayerData load(UUID uuid) throws RuntimeException
	{
		if(MAP.containsKey(uuid))
		{
			return MAP.get(uuid);
		}
		
		File file = CommonsConfig.getFile(Type.PLAYER_DATA, false, uuid);
		PlayerData pd = null;
		
		try
		{
			if(!file.exists() || file.length() == 0L)
			{
				MAP.put(uuid, pd = new PlayerData());
				return pd;
			}
			
			try(FileInputStream fileIn = new FileInputStream(file);
					ByteArrayInputStream streamIn = new ByteArrayInputStream(ByteStreams.toByteArray(fileIn));
					ObjectInputStream in = new ObjectInputStream(streamIn))
			{
				MAP.put(uuid, pd = (PlayerData) in.readObject());
				return pd;
			}
			catch(ClassNotFoundException | IOException e)
			{
				throw new RuntimeException(e);
			}
		}
		finally
		{
			if(pd != null)
			{
				pd.diskBalance = pd.balance;
				pd.diskBTA = pd.bta;
			}
		}
	}
}