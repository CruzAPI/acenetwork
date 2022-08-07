package br.com.acenetwork.survival.warp;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;

import br.com.acenetwork.survival.event.NPCLoadEvent;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.md_5.bungee.api.ChatColor;

public class NewbieNether extends Warp
{
	private final Location spawnLocation;
	private final Location portalLocation;
	
	public NewbieNether(World w)
	{
		super(w);
		
		spawnLocation = new Location(w, 0.5D, 69.0D, 0.5D, 0.0F, 0.0F);
		portalLocation = new Location(w, -13.5D, 88.0D, 12.5D, 180.0F, 0.0F);
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
		return Math.abs(l.getBlockX()) < 128 && Math.abs(l.getBlockZ()) < 128;
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
	public boolean isSafeZone(Location l)
	{
		return Math.abs(l.getBlockX()) <= 69 && Math.abs(l.getBlockZ()) <= 69;
	}
	
	@Override
	public Location getPortalLocation()
	{
		return portalLocation;
	}
	
	@Override
	public String getColoredName()
	{
		return ChatColor.GREEN + "Newbie " + ChatColor.RED + "(Nether)";
	}
	
	@Override
	public boolean hasPVP()
	{
		return true;
	}
}