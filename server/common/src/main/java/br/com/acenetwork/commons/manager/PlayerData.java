package br.com.acenetwork.commons.manager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import com.google.common.io.ByteStreams;

import br.com.acenetwork.commons.manager.CommonsConfig.Type;

public class PlayerData implements Listener
{
	public static final Map<UUID, PlayerData> MAP = new HashMap<>();
	
	private double balance;
	private double bta;
	
	public double getBalance()
	{
		return balance;
	}
	
	public void setBalance(double balance)
	{
		this.balance = balance;
	}
	
	public double getBTA()
	{
		return bta;
	}
	
	public void setBTA(double bta)
	{
		this.bta = bta;
	}
	
	@SuppressWarnings("unchecked")
	public static void save()
	{
		File file = CommonsConfig.getFile(Type.PLAYERS_DATA, true);
		
		Map<UUID, PlayerData> map;
		
		if(file.length() > 0L)
		{
			try(FileInputStream fileIn = new FileInputStream(file);
					ByteArrayInputStream streamIn = new ByteArrayInputStream(ByteStreams.toByteArray(fileIn));
					ObjectInputStream in = new ObjectInputStream(streamIn);)
			{
				map = (Map<UUID, PlayerData>) in.readObject();
			}
			catch(ClassNotFoundException | IOException ex)
			{
				throw new RuntimeException(ex);
			}
		}
		else
		{
			map = new HashMap<>();
		}
		
		try(FileOutputStream fileOut = new FileOutputStream(file);
				ByteArrayOutputStream streamOut = new ByteArrayOutputStream();
				ObjectOutputStream out = new ObjectOutputStream(streamOut))
		{
			map.putAll(MAP);
			out.writeObject(map);
		}
		catch(IOException ex)
		{
			throw new RuntimeException(ex);
		}
		
		Iterator<Entry<UUID, PlayerData>> iterator = MAP.entrySet().iterator();
		
		while(iterator.hasNext())
		{
			Entry<UUID, PlayerData> entry = iterator.next();
			
			UUID uuid = entry.getKey();
			file = CommonsConfig.getFile(Type.PLAYER_DATA, true, uuid);
			
			try(FileOutputStream fileOut = new FileOutputStream(file);
					ByteArrayOutputStream streamOut = new ByteArrayOutputStream();
					ObjectOutputStream out = new ObjectOutputStream(streamOut))
			{
				out.writeObject(entry.getValue());
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
	
	public static PlayerData load(UUID uuid)
	{
		if(MAP.containsKey(uuid))
		{
			return MAP.get(uuid);
		}
		
		File file = CommonsConfig.getFile(Type.PLAYER_DATA, false, uuid);
		PlayerData pd;
		
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
}