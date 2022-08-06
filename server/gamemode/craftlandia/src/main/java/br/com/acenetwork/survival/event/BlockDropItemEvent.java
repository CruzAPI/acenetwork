package br.com.acenetwork.survival.event;

import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockEvent;

public class BlockDropItemEvent extends BlockEvent
{
	private static final HandlerList HANDLER = new HandlerList();
	
	private final BlockState blockState;
	private final Player player;
	private final List<Item> items;
	
	public BlockDropItemEvent(Block block, BlockState blockState, Player player, List<Item> items)
	{
		super(block);
		
		this.blockState = blockState;
		this.player = player;
		this.items = items;
	}
	
	public BlockState getBlockState()
	{
		return blockState;
	}
	
	public Player getPlayer()
	{
		return player;
	}
	
	public List<Item> getItems()
	{
		return items;
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