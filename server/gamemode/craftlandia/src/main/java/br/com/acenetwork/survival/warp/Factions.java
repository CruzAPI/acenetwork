package br.com.acenetwork.craftlandia.warp;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;

import br.com.acenetwork.commons.player.CommonPlayer;
import br.com.acenetwork.commons.player.craft.CraftCommonPlayer;
import br.com.acenetwork.craftlandia.Main;
import br.com.acenetwork.craftlandia.event.NPCLoadEvent;
import br.com.acenetwork.craftlandia.player.SurvivalPlayer;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.md_5.bungee.api.ChatColor;

public class Factions extends Warp
{
	private final Location spawnLocation;
	private final Location portalLocation;
	
	public Factions(World w)
	{
		super(w);
		
		spawnLocation = new Location(w, 0.5D, 69.0D, 0.5D, 0.0F, 0.0F);
		portalLocation = new Location(w, -13.5D, 88.0D, 12.5D, 180.0F, 0.0F);
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), () ->
		{
			for(CommonPlayer cp : getCommonPlayers())
			{
				Player p = cp.getPlayer();
				
				if(!isSafeZone(p.getLocation()))
				{
					cp.setInvincibility(false);
					cp.setPVPInvincibility(false);
				}
			}
		}, 20L, 20L);
	}
	
	@EventHandler
	public void a(NPCLoadEvent e)
	{
		NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, "" 
				+ ChatColor.YELLOW + ChatColor.BOLD + "SKIP PARKOUR");
		npc.spawn(new Location(w, -1.5D, 69.0D, 2.5D, -135.0F, 0.0F));
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void a(ProjectileLaunchEvent e)
	{
		if(!(e.getEntity().getShooter() instanceof Player))
		{
			return;
		}
		
		Player p = (Player) e.getEntity().getShooter();
		
		if(p.getWorld() != w)
		{
			return;
		}
		
		CommonPlayer cp = CraftCommonPlayer.get(p);
		
		cp.setInvincibility(false);
		cp.setPVPInvincibility(false);
	}
	
	
	@EventHandler(priority = EventPriority.HIGH)
	public void asd(PlayerChangedWorldEvent e)
	{
		Player p = e.getPlayer();
		
		if(p.getWorld() != w)
		{
			return;
		}
		
		World from = e.getFrom();
		
		if(from.getName().equals("factions_nether") || from.getName().equals("factions_the_end"))
		{
			return;
		}
		
		if(isSafeZone(p.getLocation()))
		{
			CommonPlayer cp = CraftCommonPlayer.get(p);
			
			cp.setInvincibility(true);
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void a(EntityDamageByEntityEvent e)
	{
		if(e.getDamager().getWorld() != w)
		{
			return;
		}
		
		if(!(e.getDamager() instanceof Player))
		{
			return;
		}
		
		Player p = (Player) e.getDamager();
		CommonPlayer cp = CraftCommonPlayer.get(p);
		
		cp.setInvincibility(false);
		cp.setPVPInvincibility(false);
	}
	
	@Override
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onEntityDamage(EntityDamageEvent e)
	{
		Entity entity = e.getEntity();
		
		if(entity.getWorld() != w)
		{
			return;
		}
		
		if(entity instanceof Player)
		{
			Player p = (Player) entity;
			CommonPlayer cp = CraftCommonPlayer.get(p);
			
			if(cp.hasInvincibility())
			{
				e.setCancelled(true);
			}
		}
	}
	
	@Override
	public boolean isSafeZone(Location l)
	{
		return Math.abs(l.getBlockX()) < 70 && Math.abs(l.getBlockZ()) < 70;
	}
	
	@Override
	public boolean isSpawnProtection(Location l)
	{
		return Math.abs(l.getBlockX()) <= 100 && Math.abs(l.getBlockZ()) <= 100;
	}
	
	@Override
	public long getChannelingTicks(SurvivalPlayer sp)
	{
		Player p = sp.getPlayer();
		
		if(p.getWorld() != w)
		{
			return MAP.get(p.getWorld().getUID()).getChannelingTicks(sp);
		}
		
		if(sp.hasInvincibility())
		{
			return 0L;
		}
		
		for(Player all : w.getPlayers())
		{
			if(all == this || !(all instanceof SurvivalPlayer))
			{
				continue;
			}
			
			if(p.getWorld() == all.getWorld() && p.getLocation().distance(all.getLocation()) < 100.0D)
			{
				return 8L * 5L * 20L;
			}
		}
		
		return 8L * 20L;
	}
	
	@Override
	public Result canTeleportAwaySpawn(SurvivalPlayer sp)
	{
		Player p = sp.getPlayer();
		
		if(p.getWorld() != w)
		{
			return MAP.get(p.getWorld().getUID()).canTeleportAwaySpawn(sp);
		}
		
		if(sp.hasInvincibility() || sp.hasPVPInvincibility())
		{
			return Result.INVINCIBILITY;
		}
		
		return Result.ALLOW;
	}
	
	@Override
	public Location getPortalLocation()
	{
		return portalLocation;
	}
	
	@Override
	public Location getSpawnLocation()
	{
		return spawnLocation;
	}
	
	@Override
	public boolean canSetHome()
	{
		return true;
	}
	
	@Override
	public int blocksAwayFromSpawnToSetHome()
	{
		return 512;
	}
	
	@Override
	public String getColoredName()
	{
		return ChatColor.DARK_PURPLE + "Raid";
	}
	
	@Override
	public boolean hasPVP()
	{
		return true;
	}
}