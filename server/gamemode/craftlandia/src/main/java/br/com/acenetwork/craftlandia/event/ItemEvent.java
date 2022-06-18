package br.com.acenetwork.craftlandia.event;

import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;

public abstract class ItemEvent extends Event
{
	final CommandSender sender;
	final String key;
	final int amount;
	final double oldMarketCap;
	final double newMarketCap;
	final double oldCirculatingSupply;
	final double newCirculatingSupply;
	
	public ItemEvent(CommandSender sender, String key, int amount, double oldMarketCap, double newMarketCap, double oldCirculatingSupply, double newCirculatingSupply)
	{
		this.sender = sender;
		this.key = key;
		this.amount = amount;
		this.oldMarketCap = oldMarketCap;
		this.newMarketCap = newMarketCap;
		this.oldCirculatingSupply = oldCirculatingSupply;
		this.newCirculatingSupply = newCirculatingSupply;
	}
	
	public CommandSender getSender()
	{
		return sender;
	}
	
	public String getKey()
	{
		return key;
	}
	
	public int getAmount()
	{
		return amount;
	}
	
	public double getOldLiquidity()
	{
		return oldMarketCap;
	}
	
	public double getNewLiquidity()
	{
		return newMarketCap;
	}
	
	public double getOldMarketCap()
	{
		return oldCirculatingSupply;
	}
	
	public double getNewMarketCap()
	{
		return newCirculatingSupply;
	}
}