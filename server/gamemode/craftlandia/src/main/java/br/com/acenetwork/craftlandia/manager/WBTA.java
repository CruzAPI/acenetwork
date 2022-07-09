package br.com.acenetwork.craftlandia.manager;

import java.io.Serializable;

import org.bukkit.inventory.InventoryHolder;

public class WBTA implements Serializable
{
	private static final long serialVersionUID = -3294945477311039171L;
	private transient InventoryHolder invHolder;
	private Serializable holder;
	private long timeRemaining;
	
	public WBTA(InventoryHolder invHolder, long timeRemaining)
	{
		this.invHolder = invHolder;
		this.timeRemaining = timeRemaining;
	}
	
	public WBTA(long timeRemaining)
	{
		this.timeRemaining = timeRemaining;
	}
	
	public Serializable getHolder()
	{
		return holder;
	}
	
	public void setHolder(Serializable holder)
	{
		this.holder = holder;
	}
	
	public long getTimeRemaining()
	{
		return timeRemaining;
	}
	
	public void setTimeRemaining(long timeRemaining)
	{
		this.timeRemaining = timeRemaining;
	}
	
	public InventoryHolder getInvHolder()
	{
		return invHolder;
	}
	
	public void setInvHolder(InventoryHolder invHolder)
	{
		this.invHolder = invHolder;
	}
	
	@Override
	public String toString()
	{
		return invHolder == null ? null : invHolder.toString();
	}
}
