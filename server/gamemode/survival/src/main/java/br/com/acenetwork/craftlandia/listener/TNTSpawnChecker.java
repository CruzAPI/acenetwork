package br.com.acenetwork.craftlandia.listener;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.Listener;

import br.com.acenetwork.craftlandia.Main;
import br.com.acenetwork.craftlandia.event.TNTSpawnEvent;

public class TNTSpawnChecker implements Listener
{
	private static final Set<TNTPrimed> SET = new HashSet<>();
	
	public TNTSpawnChecker()
	{
		Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), () ->
		{
			for(World w : Bukkit.getWorlds())
			{
				for(TNTPrimed tnts : w.getEntitiesByClass(TNTPrimed.class))
				{
					if(SET.add(tnts))
					{
						Bukkit.getPluginManager().callEvent(new TNTSpawnEvent(tnts));
					}
				}
			}
			
			Iterator<TNTPrimed> iterator = SET.iterator();
			
			while(iterator.hasNext())
			{
				if(iterator.next().isDead())
				{
					iterator.remove();
				}
			}
		}, 0L, 1L);
	}
}
