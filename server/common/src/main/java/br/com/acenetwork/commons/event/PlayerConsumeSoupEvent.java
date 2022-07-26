package br.com.acenetwork.commons.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerConsumeSoupEvent extends PlayerEvent implements Cancellable
{
	private static final HandlerList HANDLER = new HandlerList();
	private boolean isCancelled;
	private int heal;
	private final ItemStack soup;
	
	public PlayerConsumeSoupEvent(Player p, ItemStack soup, int heal)
	{
		super(p);
		this.soup = soup;
		this.heal = heal;
	}
	
	public ItemStack getSoup()
	{
		return soup;
	}
	
	public static HandlerList getHandlerList()
	{
		return HANDLER;
	}
	
	public int getHeal()
	{
		return heal;
	}
	
	public void setHeal(int heal)
	{
		this.heal = heal;
	}
	
	@Override
	public HandlerList getHandlers()
	{
		return HANDLER;
	}
	
	@Override
	public boolean isCancelled()
	{
		return isCancelled;
	}

	@Override
	public void setCancelled(boolean arg0)
	{
		isCancelled = arg0;
	}
}