package br.com.acenetwork.craftlandia.event;

import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;

import br.com.acenetwork.commons.manager.IdData;

public class SellItemEvent extends ItemEvent 
{
	private static final HandlerList HANDLER = new HandlerList();
	
	public SellItemEvent(CommandSender sender, IdData idData, int amount, double oldMarketCap, double newMarketCap, double oldCirculatingSupply, double newCirculatingSupply)
	{
		super(sender, idData, amount, oldMarketCap, newMarketCap, oldCirculatingSupply, newCirculatingSupply);
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