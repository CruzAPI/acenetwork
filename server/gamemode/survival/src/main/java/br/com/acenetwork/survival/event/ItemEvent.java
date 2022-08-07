package br.com.acenetwork.survival.event;

import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;

import br.com.acenetwork.commons.manager.IdData;
import br.com.acenetwork.survival.manager.IdDataRarity;

public abstract class ItemEvent extends Event
{
	final CommandSender sender;
	final IdDataRarity idData;
	final int amount;
	final double oldMarketCap;
	final double newMarketCap;
	final double oldCirculatingSupply;
	final double newCirculatingSupply;
	
	public ItemEvent(CommandSender sender, IdDataRarity idData, int amount, double oldMarketCap, double newMarketCap, double oldCirculatingSupply, double newCirculatingSupply)
	{
		this.sender = sender;
		this.idData = idData;
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
	
	public IdDataRarity getIdData()
	{
		return idData;
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