package br.com.acenetwork.survival.event;

import org.bukkit.entity.FallingBlock;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class FallingBlockObstructEvent extends Event
{
	private static final HandlerList HANDLER = new HandlerList();
	
	private final FallingBlock fb;
	
	public FallingBlockObstructEvent(FallingBlock fb)
	{
		this.fb = fb;
	}
	
	public FallingBlock getFallingBlock()
	{
		return fb;
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