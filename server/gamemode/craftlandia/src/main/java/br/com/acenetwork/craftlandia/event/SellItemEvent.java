package br.com.acenetwork.craftlandia.event;

import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;

public class SellItemEvent extends ItemEvent 
{
	private static final HandlerList HANDLER = new HandlerList();
	
	public SellItemEvent(CommandSender sender, String key, int amount, double oldMarketCap, double newMarketCap, double oldCirculatingSupply, double newCirculatingSupply)
	{
		super(sender, key, amount, oldMarketCap, newMarketCap, oldCirculatingSupply, newCirculatingSupply);
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