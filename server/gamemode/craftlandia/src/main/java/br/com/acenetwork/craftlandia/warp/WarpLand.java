package br.com.acenetwork.craftlandia.warp;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.event.world.WorldSaveEvent;

import com.google.common.io.ByteStreams;

import br.com.acenetwork.commons.player.CommonPlayer;
import br.com.acenetwork.craftlandia.Main;
import br.com.acenetwork.craftlandia.event.NPCLoadEvent;
import br.com.acenetwork.craftlandia.manager.Config;
import br.com.acenetwork.craftlandia.manager.Config.Type;
import br.com.acenetwork.craftlandia.player.SurvivalPlayer;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.md_5.bungee.api.ChatColor;
import br.com.acenetwork.craftlandia.manager.Land;
import br.com.acenetwork.craftlandia.manager.LandEntityData;

public class WarpLand extends Warp
{
	public final Map<UUID, LandEntityData> map;
	private final Location spawnLocation;
	private final Location portalLocation;
	
	public WarpLand(World w)
	{
		super(w);
		
		File file = Config.getFile(Type.LAND_ENTITY_DATA, false);
		
		if(file.exists() && file.length() > 0L)
		{
			try(FileInputStream fileIn = new FileInputStream(file);
					ByteArrayInputStream streamIn = new ByteArrayInputStream(ByteStreams.toByteArray(fileIn));
					ObjectInputStream in = new ObjectInputStream(streamIn))
			{
				map = (Map<UUID, LandEntityData>) in.readObject();
			}
			catch(IOException | ClassNotFoundException e)
			{
				throw new RuntimeException(e);
			}
		}
		else
		{
			map = new HashMap<>();
		}
		
		spawnLocation = new Location(w, 0.5D, 69.0D, 0.5D, 0.0F, 0.0F);
		portalLocation = new Location(w, 13.5D, 88.0D, -7.5D, 0.0F, 0.0F);

		Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), () ->
		{
			loop:for(CommonPlayer cp : getCommonPlayers())
			{
				if(!(cp instanceof SurvivalPlayer))
				{
					continue;
				}
				
				Player p = cp.getPlayer();
				
				for(Land land : Land.SET)
				{
					if(land.isLand(p.getLocation()))
					{
						p.spigot().setCollidesWithEntities(land.isTrusted(p));
						continue loop;
					}
				}
				
				p.spigot().setCollidesWithEntities(false);
			}
		}, 1L, 1L);
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), () ->
		{
			Map<UUID, Entity> tempMap = new HashMap<>();
			
			for(Entity entity : w.getEntities())
			{
				tempMap.put(entity.getUniqueId(), entity);
			}
			
			Iterator<Entry<UUID, LandEntityData>> iterator = map.entrySet().iterator();
			
			while(iterator.hasNext())
			{
				Entry<UUID, LandEntityData> entry = iterator.next();
				LandEntityData value = entry.getValue();
				Entity entity = tempMap.get(entry.getKey());
				
				if(entity == null)
				{
//					iterator.remove();
//					Bukkit.broadcastMessage("iterator.remove()");
					continue;
				}
				
				Location l = entity.getLocation();
				Location lastLocation = value.getLastLocation();
				
				if(value.getLand().isLand(l))
				{
					value.setLastLocation(l);
				}
				else if(lastLocation != null)
				{
					entity.teleport(lastLocation);
				}
			}
		}, 10L, 10L);
	}
	
	@EventHandler
	public void a(NPCLoadEvent e)
	{
		NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, "" 
				+ ChatColor.YELLOW + ChatColor.BOLD + "SKIP PARKOUR");
		npc.spawn(new Location(w, -1.5D, 69.0D, 2.5D, -135.0F, 0.0F));
	}
	
	@Override
	public boolean isSpawnProtection(Location l)
	{
		return Math.abs(l.getBlockX()) < 190 && Math.abs(l.getBlockZ()) < 190;
	}
	
	@Override
	public Location getSpawnLocation()
	{
		return spawnLocation;
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void a(EntitySpawnEvent e)
	{
		Location l = e.getLocation();
		
		if(l.getWorld() != w)
		{
			return;
		}
		
		Entity entity = e.getEntity();
		
		if(!entity.hasMetadata("land"))
		{
			return;
		}
		
		int landId = entity.getMetadata("land").get(0).asInt();
		
		LandEntityData landEntityData = new LandEntityData(landId, l);
		map.put(entity.getUniqueId(), landEntityData);
	}
	
	@EventHandler
	public void a(EntityDeathEvent e)
	{
		map.remove(e.getEntity().getUniqueId());
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void a(EntityDamageEvent e)
	{
		Entity entity = e.getEntity();
		
		if(entity.getWorld() != w)
		{
			return;
		}
		
		if(entity instanceof Player)
		{
			return;
		}
		
		LandEntityData entityData = map.get(entity.getUniqueId());
		
		if(entityData == null || !entityData.getLand().hasOwner())
		{
			e.setCancelled(true);
			return;
		}
		
		if(e instanceof EntityDamageByEntityEvent)
		{
			EntityDamageByEntityEvent ee = (EntityDamageByEntityEvent) e;
			
			Player damager;
			
			if(ee.getDamager() instanceof Player)
			{
				damager = (Player) ee.getDamager();
			}
			else if(ee.getDamager() instanceof Projectile && ((Projectile) ee.getDamager()).getShooter() instanceof Player)
			{
				damager = (Player) ((Projectile) ee.getDamager()).getShooter();
			}
			else
			{
				e.setCancelled(false);
				return;
			}
			
			if(!entityData.getLand().isTrusted(damager))
			{
				e.setCancelled(true);
				return;
			}
			
			return;
		}
		
		e.setCancelled(false);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void a(EntityTargetEvent e)
	{
		Entity entity = e.getEntity();
		
		if(entity.getWorld() != w)
		{
			return;
		}
		
		LandEntityData entityData = map.get(entity.getUniqueId());
		
		if(entityData == null)
		{
			e.setCancelled(true);
			return;
		}
		
		if((e.getTarget() instanceof Player))
		{
			e.setCancelled(!entityData.getLand().isTrusted(e.getTarget().getUniqueId()));
		}
	}
	
	@EventHandler
	public void ab(WorldSaveEvent e)
	{
		if(e.getWorld() != w)
		{
			return;
		}
		
		File file = Config.getFile(Type.LAND_ENTITY_DATA, true);
		
		try(FileOutputStream fileOut = new FileOutputStream(file);
				ByteArrayOutputStream streamOut = new ByteArrayOutputStream();
				ObjectOutputStream out = new ObjectOutputStream(streamOut))
		{
			out.writeObject(map);
			fileOut.write(streamOut.toByteArray());
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
	}
	
	@Override
	public boolean isSafeZone(Location l)
	{
		return Math.abs(l.getBlockX()) <= 65 && Math.abs(l.getBlockX()) <= 65;
	}
	
	@Override
	public Location getPortalLocation()
	{
		return portalLocation;
	}
	
	@Override
	public String getColoredName()
	{
		return ChatColor.GOLD + "Lands";
	}
}