package br.com.acenetwork.commons.event;

import java.util.List;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

public class CustomEntityDeathEvent extends Event
{
	private static final HandlerList HANDLER = new HandlerList();
	private final EntityDeathEvent e;
	private boolean keepInventory;
	private boolean keepExp;
	
	public CustomEntityDeathEvent(EntityDeathEvent e)
	{
		this.e = e;
	}
	
	public boolean getKeepInventory()
	{
		return keepInventory;
	}

	public void setKeepInventory(boolean keepInventory)
	{
		this.keepInventory = keepInventory;
	}

	public boolean getKeepExp()
	{
		return keepExp;
	}

	public void setKeepExp(boolean keepExp)
	{
		this.keepExp = keepExp;
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

	public EntityDeathEvent getEntityDeathEventEvent()
	{
		return e;
	}
}