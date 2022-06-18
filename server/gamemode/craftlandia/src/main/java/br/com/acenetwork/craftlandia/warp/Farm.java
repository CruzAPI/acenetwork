package br.com.acenetwork.craftlandia.warp;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.SpawnerSpawnEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import br.com.acenetwork.craftlandia.Main;

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
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void a(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		
		if(p.getWorld() != w)
		{
			return;
		}
		
		ItemStack item = e.getItem();
		
		if(item != null && item.getType() == Material.BOAT)
		{
			e.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void a(PlayerInteractEntityEvent e)
	{
		Entity entity = e.getRightClicked();
		
		if(entity.getWorld() != w)
		{
			return;
		}
		
		if(entity instanceof ItemFrame)
		{
			e.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void a(EntityDamageEvent e)
	{
		Entity entity = e.getEntity();
		
		if(entity.getWorld() != w)
		{
			return;
		}
		
		if(entity instanceof ItemFrame)
		{
			e.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void a(HangingBreakEvent e)
	{
		if(e.getEntity().getWorld() != w)
		{
			return;
		}
		
		e.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void a(HangingPlaceEvent e)
	{
		if(e.getEntity().getWorld() != w)
		{
			return;
		}
		
		e.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void b(PlayerBucketFillEvent e)
	{
		Block b = e.getBlockClicked();
		
		if(b.getWorld() != w)
		{
			return;
		}
		
		e.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void b(PlayerBucketEmptyEvent e)
	{
		Block b = e.getBlockClicked();
		
		if(b.getWorld() != w)
		{
			return;
		}
		
		e.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void b(BlockBreakEvent e)
	{
		if(e.getBlock().getWorld() != w)
		{
			return;
		}
		
		e.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void b(BlockPlaceEvent e)
	{
		if(e.getBlock().getWorld() != w)
		{
			return;
		}
		
		e.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void b(CreatureSpawnEvent e)
	{
		if(e.getLocation().getWorld() != w)
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
		entity.setMetadata("task", new FixedMetadataValue(Main.getPlugin(), 
				Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), () ->
		{
			if(entity.isDead() || !entity.hasMetadata("spawner") || !entity.hasMetadata("spawner"))
			{
				if(entity.hasMetadata("task"))
				{
					Bukkit.getScheduler().cancelTask(entity.getMetadata("task").get(0).asInt());
					entity.removeMetadata("task", Main.getPlugin());
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
		if(e.getWorld() != w)
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
		
		if(r.nextInt(6) != 0)
		{
			e.setCancelled(true);
			return;
		}
		
		e.getEntity().setMetadata("spawner", new FixedMetadataValue(Main.getInstance(), e.getSpawner().getLocation()));
		e.getEntity().setMetadata("spawn", new FixedMetadataValue(Main.getInstance(), e.getLocation()));
	}
	
	@EventHandler
	public void b(EntityChangeBlockEvent e)
	{
		if(e.getBlock().getWorld() != w)
		{
			return;
		}
		
		if(e.getEntity().getType() == EntityType.ENDERMAN)
		{
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void b(EntityExplodeEvent e)
	{
		if(e.getLocation().getWorld() != w)
		{
			return;
		}
		
		e.blockList().clear();
	}
	
	@EventHandler
	public void b(BlockExplodeEvent e)
	{
		if(e.getBlock().getWorld() != w)
		{
			return;
		}
		
		e.blockList().clear();
	}
}