package br.com.acenetwork.craftlandia.listener;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;

import br.com.acenetwork.craftlandia.Main;
import br.com.acenetwork.craftlandia.event.FallingBlockObstructEvent;

public class FallingBlockChecker implements Listener
{
	private static final Set<FallingBlock> SET = new HashSet<>();
	
	public FallingBlockChecker()
	{
		Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), () ->
		{
			Iterator<FallingBlock> iterator = SET.iterator();
			
			while(iterator.hasNext())
			{
				FallingBlock fb = iterator.next();
				
				if(fb.isDead())
				{
					iterator.remove();
					Bukkit.getPluginManager().callEvent(new FallingBlockObstructEvent(fb));
				}
			}
		}, 0L, 1L);
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void a(EntityChangeBlockEvent e)
	{
		if(!(e.getEntity() instanceof FallingBlock))
		{
			return;
		}
		
		if(e.getTo() == Material.AIR)
		{
			SET.add((FallingBlock) e.getEntity());
		}
		else
		{
			SET.remove(e.getEntity());
		}
	}
}
