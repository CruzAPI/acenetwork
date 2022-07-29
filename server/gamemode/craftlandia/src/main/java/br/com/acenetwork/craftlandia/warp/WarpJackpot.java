package br.com.acenetwork.craftlandia.warp;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;

import br.com.acenetwork.craftlandia.event.NPCLoadEvent;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.md_5.bungee.api.ChatColor;

public class WarpJackpot extends Warp
{
	private final Location spawnLocation;
	
	public WarpJackpot(World w)
	{
		super(w);
		
		spawnLocation = new Location(w, 0.5D, 66.0D, 0.5D, 0.0F, 0.0F);
	}
	
	@EventHandler
	public void a(NPCLoadEvent e)
	{
		NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, "" + ChatColor.BOLD + "CLICK TO PLAY");
		npc.spawn(new Location(w, 0.5D, 83.0D, 34.0D, 180.0F, 0.0F));
	}
	
	@Override
	public Location getSpawnLocation()
	{
		return spawnLocation;
	}
}