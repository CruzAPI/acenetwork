package br.com.acenetwork.craftlandia.warp;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

public class Portals extends Warp
{
	private final Location spawnLocation;
	
	public Portals(World w)
	{
		super(w);
		
		spawnLocation = new Location(w, 0.5D, 103.0D, -17.5D, 0.0F, 0.0F);
	}
	
	@EventHandler
	public void a(EntityDamageEvent e)
	{
		if(e.getEntity().getWorld() != w)
		{
			return;
		}
		
		if(e.getEntity() instanceof Player && e.getCause() == DamageCause.VOID)
		{
			e.getEntity().teleport(getSpawnLocation(), TeleportCause.PLUGIN);
		}
	}
	
	@Override
	public Location getSpawnLocation()
	{
		return spawnLocation;
	}
}