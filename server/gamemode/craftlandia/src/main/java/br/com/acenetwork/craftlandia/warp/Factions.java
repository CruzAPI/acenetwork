package br.com.acenetwork.craftlandia.warp;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;

import br.com.acenetwork.commons.player.CommonPlayer;
import br.com.acenetwork.commons.player.craft.CraftCommonPlayer;
import br.com.acenetwork.craftlandia.Main;

public class Factions extends Warp
{
	private final Location spawnLocation;
	
	public Factions(World w)
	{
		super(w);
		
		spawnLocation = new Location(w, 0.0D, 69.0D, 0.0D, 0.0F, 0.0F);
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), () ->
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
	
	
	@EventHandler
	public void asd(PlayerChangedWorldEvent e)
	{
		Player p = e.getPlayer();
		
		if(p.getWorld() != w)
		{
			return;
		}
		
		World from = e.getFrom();
		Bukkit.broadcastMessage("world changed to factions");
		
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
	
	private boolean isSafeZone(Location l)
	{
		return Math.abs(l.getBlockX()) < 70 && Math.abs(l.getBlockZ()) < 70;
	}
	
	@Override
	public boolean isSpawnProtection(Location l)
	{
		return Math.abs(l.getBlockX()) <= 100 && Math.abs(l.getBlockZ()) <= 100;
	}
}