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
import org.bukkit.inventory.Inventory;

import com.google.common.io.ByteStreams;

import br.com.acenetwork.commons.executor.VipChest;
import br.com.acenetwork.commons.manager.CommonsConfig.Type;

public class CommonPlayerData implements Listener, Serializable, Cloneable
{
	private static final long serialVersionUID = -1358069840285451923L;

	public static final Map<UUID, CommonPlayerData> MAP = new HashMap<>();
	
	private double balance;
	private double bta;
	private boolean invincibility;
	private int vip;
	private UUID[] vipInvContents;
	private transient double diskBalance;
	private transient double diskBTA;
	private transient boolean diskInvincibility;
	private transient int diskVip;
	private transient UUID[] diskVipInvContents;
	
	public CommonPlayerData()
	{
		
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException
	{
		return super.clone();
	}
	
	public CommonPlayerData(CommonPlayerData pd)
	{
		this(pd.diskBalance , pd.diskBTA, pd.diskInvincibility, pd.diskVip, pd.vipInvContents);
	}
	
	public CommonPlayerData(double balance, double bta, boolean invincibility, int vip, UUID[] vipInvContents)
	{
		this.balance = balance;
		this.bta = bta;
		this.invincibility = invincibility;
		this.vip = vip;
		this.vipInvContents = vipInvContents;
		this.diskBalance = balance;
		this.diskBTA = bta;
		this.diskInvincibility = invincibility;
		this.diskVip = vip;
		this.diskVipInvContents = vipInvContents;
	}
	
	public void setVipInvContents(UUID[] vipInvContents)
	{
		this.vipInvContents = vipInvContents;
	}
	
	public boolean hasInvincibility()
	{
		return invincibility;
	}
	
	public boolean setInvincibility(boolean invincibility)
	{
		return this.invincibility != (this.invincibility = invincibility);
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
		if(balance < 0.0D)
		{
			throw new InsufficientBalanceException();
		}
		
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
			throw new InsufficientBalanceException();
		}
		
		this.bta = bta;
	}
	
	public int getVip()
	{
		return vip;
	}
	
	public void setVip(int vip)
	{
		if(vip < 0)
		{
			throw new InsufficientBalanceException();
		}
		
		this.vip = vip;
	}
	
	public UUID[] getVipInvContents()
	{
		return vipInvContents;
	}
	
	public static void save()
	{
		save(MAP);
	}
	
	@SuppressWarnings("unchecked")
	public static void save(Map<UUID, CommonPlayerData> toSave)
	{
		File file = CommonsConfig.getFile(Type.PLAYERS_DATA, true);
		
		Map<UUID, CommonPlayerData> diskMap;
		
		if(file.length() > 0L)
		{
			try(FileInputStream fileIn = new FileInputStream(file);
					ByteArrayInputStream streamIn = new ByteArrayInputStream(ByteStreams.toByteArray(fileIn));
					ObjectInputStream in = new ObjectInputStream(streamIn);)
			{
				diskMap = (Map<UUID, CommonPlayerData>) in.readObject();
			}
			catch(ClassNotFoundException | IOException ex)
			{
				ex.printStackTrace();
				return;
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
			ex.printStackTrace();
			return;
		}
		
		Iterator<Entry<UUID, CommonPlayerData>> iterator = toSave.entrySet().iterator();
		
		while(iterator.hasNext())
		{
			Entry<UUID, CommonPlayerData> entry = iterator.next();
			
			UUID uuid = entry.getKey();
			CommonPlayerData pd = entry.getValue();
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
				ex.printStackTrace();
			}
		}
	}
	
	public static CommonPlayerData load(UUID uuid) throws RuntimeException
	{
		if(MAP.containsKey(uuid))
		{
			return MAP.get(uuid);
		}
		
		File file = CommonsConfig.getFile(Type.PLAYER_DATA, false, uuid);
		CommonPlayerData pd = null;
		
		try
		{
			if(!file.exists() || file.length() == 0L)
			{
				MAP.put(uuid, pd = new CommonPlayerData());
				return pd;
			}
			
			try(FileInputStream fileIn = new FileInputStream(file);
					ByteArrayInputStream streamIn = new ByteArrayInputStream(ByteStreams.toByteArray(fileIn));
					ObjectInputStream in = new ObjectInputStream(streamIn))
			{
				MAP.put(uuid, pd = (CommonPlayerData) in.readObject());
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