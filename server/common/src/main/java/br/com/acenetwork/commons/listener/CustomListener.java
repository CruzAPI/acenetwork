package br.com.acenetwork.commons.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.world.StructureGrowEvent;

import br.com.acenetwork.commons.event.CustomBlockExplodeEvent;
import br.com.acenetwork.commons.event.CustomEntityDeathEvent;
import br.com.acenetwork.commons.event.CustomEntityExplodeEvent;
import br.com.acenetwork.commons.event.CustomStructureGrowEvent;

public class CustomListener implements Listener
{
	@EventHandler(priority = EventPriority.LOWEST)
	public void on(StructureGrowEvent e)
	{
		Bukkit.getPluginManager().callEvent(new CustomStructureGrowEvent(e));
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void on(BlockExplodeEvent e)
	{
		Bukkit.getPluginManager().callEvent(new CustomBlockExplodeEvent(e));
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void on(EntityExplodeEvent e)
	{
		Bukkit.getPluginManager().callEvent(new CustomEntityExplodeEvent(e));
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void on(EntityDeathEvent e)
	{
		CustomEntityDeathEvent ce = new CustomEntityDeathEvent(e);
		Bukkit.getPluginManager().callEvent(ce);
		
		if(e instanceof PlayerDeathEvent)
		{
			PlayerDeathEvent pe = (PlayerDeathEvent) e;
			
			pe.setKeepInventory(ce.getKeepInventory());
			pe.setKeepLevel(ce.getKeepExp());
		}
		else
		{
			if(ce.getKeepInventory())
			{
				e.getDrops().clear();
			}
		}
		
		if(ce.getKeepExp())
		{
			e.setDroppedExp(0);
		}
	}
}
