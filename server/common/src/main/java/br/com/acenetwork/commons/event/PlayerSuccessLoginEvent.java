package br.com.acenetwork.commons.event;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class PlayerSuccessLoginEvent extends PlayerEvent
{
	private static final HandlerList HANDLER = new HandlerList();

	public PlayerSuccessLoginEvent(Player p)
	{
		super(p);
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