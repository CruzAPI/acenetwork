package br.com.acenetwork.craftlandia.event;

import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class TNTSpawnEvent extends Event
{
	private static final HandlerList HANDLER = new HandlerList();
	
	private final TNTPrimed tnt;
	
	public TNTSpawnEvent(TNTPrimed tnt)
	{
		this.tnt = tnt;
	}
	
	public TNTPrimed getTNTPrimed()
	{
		return tnt;
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