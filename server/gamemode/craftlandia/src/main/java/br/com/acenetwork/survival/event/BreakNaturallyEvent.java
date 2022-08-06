package br.com.acenetwork.craftlandia.event;

import org.bukkit.block.Block;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockEvent;

public class BreakNaturallyEvent extends BlockEvent
{
	private static final HandlerList HANDLER = new HandlerList();
	
	public BreakNaturallyEvent(Block block)
	{
		super(block);
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