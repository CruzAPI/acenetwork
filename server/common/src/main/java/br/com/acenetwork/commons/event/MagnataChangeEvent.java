package br.com.acenetwork.commons.event;

import org.bukkit.OfflinePlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MagnataChangeEvent extends Event
{
	private static final HandlerList HANDLER = new HandlerList();
	
	private final OfflinePlayer oldMagnata;
	private final OfflinePlayer newMagnata;
	
	public MagnataChangeEvent(OfflinePlayer oldMagnata, OfflinePlayer newMagnata)
	{
		this.oldMagnata = oldMagnata;
		this.newMagnata = newMagnata;
	}
	
	public OfflinePlayer getOldMagnata()
	{
		return oldMagnata;
	}
	
	public OfflinePlayer getNewMagnata()
	{
		return newMagnata;
	}
	
	public static HandlerList getHandlerList()
	{
		return HANDLER;
	}
	
	@Override
	public HandlerList getHandlers()
	{
		return HANDLER;
	}
}