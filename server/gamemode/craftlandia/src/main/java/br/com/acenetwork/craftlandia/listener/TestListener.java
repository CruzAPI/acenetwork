package br.com.acenetwork.craftlandia.listener;

import java.util.Optional;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import br.com.acenetwork.commons.event.PlayerConsumeSoupEvent;
import br.com.acenetwork.commons.inventory.VipChestGUI;
import br.com.acenetwork.craftlandia.Main;
import br.com.acenetwork.craftlandia.Rarity;
import br.com.acenetwork.craftlandia.Util;

public class TestListener implements Listener
{
	@EventHandler
	public void a(PlayerConsumeSoupEvent e)
	{
		Rarity rarity = Optional.ofNullable(Util.getRarity(e.getSoup())).orElse(Rarity.COMMON);
		
		int heal;
		
		switch(rarity)
		{
		case LEGENDARY:
			heal = 10;
			break;
		case RARE:
			heal = 7;
			break;
		default:
			heal = 4;
			break;
		}
		
		e.setHeal(heal);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void vipActivation(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		
		ItemStack item = e.getItem();
		
		if(!VipChestGUI.isItemStackVIP(item))
		{
			return;
		}
		
		e.setCancelled(true);
		
		if(!e.getAction().name().contains("RIGHT"))
		{
			return;
		}
		
		Main.getInstance().activateVIP(p, p.getInventory().getHeldItemSlot());
	}
}
