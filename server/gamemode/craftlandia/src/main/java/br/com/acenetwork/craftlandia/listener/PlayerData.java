package br.com.acenetwork.craftlandia.listener;

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

import com.google.common.io.ByteStreams;

import br.com.acenetwork.craftlandia.manager.Config;
import br.com.acenetwork.craftlandia.manager.Config.Type;

public class PlayerData implements Serializable
{
	private static final long serialVersionUID = -1352599961563097422L;
	
	private static final Map<UUID, PlayerData> MAP = new HashMap<>();
	
	private Map<UUID, int[]> bedHomes;
	
	public PlayerData()
	{
		bedHomes = new HashMap<>();
	}
	
	public Map<UUID, int[]> getBedHomes()
	{
		return bedHomes;
	}
	
	public static void save()
	{
		Iterator<Entry<UUID, PlayerData>> iterator = MAP.entrySet().iterator();
		File file;
		
		while(iterator.hasNext())
		{
			Entry<UUID, PlayerData> entry = iterator.next();
			
			UUID uuid = entry.getKey();
			PlayerData pd = entry.getValue();
			file = Config.getFile(Type.PLAYER_DATA, true, uuid);
			
			try(FileOutputStream fileOut = new FileOutputStream(file);
					ByteArrayOutputStream streamOut = new ByteArrayOutputStream();
					ObjectOutputStream out = new ObjectOutputStream(streamOut))
			{
				out.writeObject(pd);
				fileOut.write(streamOut.toByteArray());
				
				if(!Bukkit.getOfflinePlayer(uuid).isOnline())
				{
					iterator.remove();
				}
			}
			catch(IOException ex)
			{
				ex.printStackTrace();
			}
		}
	}
	
	public static PlayerData load(UUID uuid) throws RuntimeException
	{
		if(MAP.containsKey(uuid))
		{
			return MAP.get(uuid);
		}
		
		File file = Config.getFile(Type.PLAYER_DATA, false, uuid);
		PlayerData pd = null;
		
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