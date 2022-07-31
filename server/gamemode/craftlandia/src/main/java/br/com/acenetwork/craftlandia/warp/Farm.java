package br.com.acenetwork.craftlandia.warp;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.SpawnerSpawnEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.metadata.FixedMetadataValue;

import br.com.acenetwork.craftlandia.Main;
import net.md_5.bungee.api.ChatColor;

public class Farm extends Warp
{
	public Farm(World w)
	{
		super(w);
		
		for(Chunk chunk : w.getLoadedChunks())
		{
			for(Entity entity : chunk.getEntities())
			{
				if(!(entity instanceof Player) && entity instanceof LivingEntity)
				{
					entity.remove();
				}
			}
		}
	
	}
	
	@Override
	public boolean isSafeZone(Location l)
	{
		return false;
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void on(CreatureSpawnEvent e)
	{
		Location l = e.getLocation();
		
		if(l.getWorld() != w)
		{
			return;
		}
		
		LivingEntity entity = e.getEntity();
		
		if(e.getSpawnReason() != SpawnReason.SPAWNER)
		{
			e.setCancelled(true);
		}
		else
		{
			e.setCancelled(false);
			startTask(entity);
		}
	}
	
	private void startTask(LivingEntity entity)
	{
		entity.setMetadata("task", new FixedMetadataValue(Main.getInstance(), 
				Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), () ->
		{
			if(entity.isDead() || !entity.hasMetadata("spawner") || !entity.hasMetadata("spawner"))
			{
				if(entity.hasMetadata("task"))
				{
					Bukkit.getScheduler().cancelTask(entity.getMetadata("task").get(0).asInt());
					entity.removeMetadata("task", Main.getInstance());
				}
				
				return;
			}
			
			Location spawner = (Location) entity.getMetadata("spawner").get(0).value();
			Location spawn = (Location) entity.getMetadata("spawn").get(0).value();
			
			if(entity.getLocation().getWorld() != spawner.getWorld() || entity.getLocation().distance(spawner) > 8.0D)
			{
				entity.teleport(spawn);
			}
		}, 100L, 100L)));
	}
	
	@EventHandler
	public void a(ChunkUnloadEvent e)
	{
		if(!e.getWorld().getName().equals(worldName))
		{
			return;
		}
		
		for(Entity entities : e.getChunk().getEntities())
		{
			if(entities.hasMetadata("task"))
			{
				Bukkit.getScheduler().cancelTask(entities.getMetadata("task").get(0).asInt());
			}
		}
	}
	
	@EventHandler
	public void a(ChunkLoadEvent e)
	{
		if(e.getWorld() != w)
		{
			return;
		}
		
		for(Entity entities : e.getChunk().getEntities())
		{
			if(entities.hasMetadata("task"))
			{
				int id = entities.getMetadata("task").get(0).asInt();
				
				if(Bukkit.getScheduler().isCurrentlyRunning(id) || Bukkit.getScheduler().isQueued(id))
				{
					continue;
				}
				
				if(entities instanceof LivingEntity)
				{
					startTask((LivingEntity) entities);
				}
			}
			else if(!(entities instanceof Player))
			{
				entities.remove();
			}
		}
	}
	
	@EventHandler
	public void b(SpawnerSpawnEvent e)
	{
		if(e.getLocation().getWorld() != w)
		{
			return;
		}
		
		Random r = new Random();
		
//		if(r.nextInt(6) != 0)
//		{
//			e.setCancelled(true);
//			return;
//		}
		
		e.getEntity().setMetadata("spawner", new FixedMetadataValue(Main.getInstance(), e.getSpawner().getLocation()));
		e.getEntity().setMetadata("spawn", new FixedMetadataValue(Main.getInstance(), e.getLocation()));
	}
	
	@Override
	public Location getRespawnLocation()
	{
		return Warp.getInstance(Portals.class).getRespawnLocation();
	}
	
	@Override
	public String getColoredName()
	{
		return ChatColor.DARK_GRAY + "Farm " + ChatColor.GREEN + "(Common)";
	}
	
	@Override
	public boolean hasPVP()
	{
		return true;
	}
}
