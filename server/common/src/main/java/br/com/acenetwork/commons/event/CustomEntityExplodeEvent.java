package br.com.acenetwork.commons.event;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityExplodeEvent;

public class CustomEntityExplodeEvent extends Event
{
	private static final HandlerList HANDLER = new HandlerList();
	private final List<Block> blocks;
	private final EntityExplodeEvent e;
	
	public CustomEntityExplodeEvent(EntityExplodeEvent e)
	{
		this.e = e;
		blocks = new ArrayList<>(e.blockList());
	}
	
	public List<Block> getOriginalBlocks()
	{
		return new ArrayList<>(blocks);
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

	public EntityExplodeEvent getEvent()
	{
		return e;
	}
}