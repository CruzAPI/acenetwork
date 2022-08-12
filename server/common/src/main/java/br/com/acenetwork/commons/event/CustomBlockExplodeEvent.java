package br.com.acenetwork.commons.event;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockExplodeEvent;

public class CustomBlockExplodeEvent extends Event
{
	private static final HandlerList HANDLER = new HandlerList();
	private final List<Block> blocks;
	private final BlockExplodeEvent e;
	
	public CustomBlockExplodeEvent(BlockExplodeEvent e)
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

	public BlockExplodeEvent getEvent()
	{
		return e;
	}
}