package br.com.acenetwork.craftlandia.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class NPCLoadEvent extends Event
{
	private static final HandlerList HANDLER = new HandlerList();
	
	public NPCLoadEvent()
	{
		
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