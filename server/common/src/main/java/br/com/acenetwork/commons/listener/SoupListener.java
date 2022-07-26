package br.com.acenetwork.commons.listener;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import br.com.acenetwork.commons.event.PlayerConsumeSoupEvent;

public class SoupListener implements Listener
{
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerInteract(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		ItemStack item = e.getItem();
		
		if(item == null)
		{
			return;
		}
		
		boolean rightClick = e.getAction().name().startsWith("RIGHT");
		
		e.setCancelled(false);
		
		if(item.getType() == Material.MUSHROOM_SOUP && rightClick)
		{
			Damageable d = (Damageable) p;
			
			PlayerConsumeSoupEvent event = new PlayerConsumeSoupEvent(p, item, 7);
			Bukkit.getPluginManager().callEvent(event);
			
			if(event.isCancelled())
			{
				return;
			}
			
			if(d.getHealth() < d.getMaxHealth())
			{
				p.setHealth(Math.min(d.getHealth() + event.getHeal(), d.getMaxHealth()));
				p.getItemInHand().setType(Material.BOWL);
			}
			else if(p.getFoodLevel() < 20)
			{
				p.setFoodLevel(Math.min(p.getFoodLevel() + event.getHeal(), 20));
				p.getItemInHand().setType(Material.BOWL);
			}
		}
	}
}