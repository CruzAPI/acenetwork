package br.com.acenetwork.craftlandia.warp;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import br.com.acenetwork.commons.player.CommonPlayer;
import br.com.acenetwork.commons.player.craft.CraftCommonPlayer;
import br.com.acenetwork.craftlandia.event.NPCLoadEvent;
import br.com.acenetwork.craftlandia.executor.Jackpot;
import br.com.acenetwork.craftlandia.manager.JackpotType;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;
import net.md_5.bungee.api.ChatColor;

public class WarpJackpot extends Warp
{
	private final Location spawnLocation;
	private final Set<NPC> npcSet = new HashSet<>();
	
	public WarpJackpot(World w)
	{
		super(w);
		
		spawnLocation = new Location(w, 0.5D, 66.0D, 0.5D, 0.0F, 0.0F);
	}
	
	@EventHandler
	public void a(NPCLoadEvent e)
	{
		NPC npc;
		
		npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, "" + ChatColor.BOLD + "CLICK TO PLAY");
		npc.data().set("type", JackpotType.COAL);
		npc.data().set("inUse", false);
		npc.spawn(new Location(w, 0.5D, 83.0D, 34.0D, 180.0F, 0.0F));
		npcSet.add(npc);
		
		npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, "" + ChatColor.BOLD + "CLICK TO PLAY");
		npc.data().set("type", JackpotType.REDSTONE);
		npc.data().set("inUse", false);
		npc.spawn(new Location(w, 34.0D, 83.0D, 0.5D, 90.0F, 0.0F));
		npcSet.add(npc);
		
		npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, "" + ChatColor.BOLD + "CLICK TO PLAY");
		npc.data().set("type", JackpotType.DIAMOND);
		npc.data().set("inUse", false);
		npc.spawn(new Location(w, -33.0D, 83.0D, 0.5D, -90.0F, 0.0F));
		npcSet.add(npc);
		
		npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, "" + ChatColor.BOLD + "CLICK TO PLAY");
		npc.data().set("type", JackpotType.EMERALD);
		npc.data().set("inUse", false);
		npc.spawn(new Location(w, 0.5D, 83.0D, -33.0D, 0.0F, 0.0F));
		npcSet.add(npc);
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void asd(PlayerInteractEntityEvent e)
	{
		Entity clicked = e.getRightClicked();
		NPCRegistry npcRegistry = CitizensAPI.getNPCRegistry();
		
		if(!npcRegistry.isNPC(clicked))
		{
			return;
		}
		
		NPC npc = npcRegistry.getNPC(clicked);
		
		if(npcSet.contains(npc))
		{
			Player p = e.getPlayer();
			CommonPlayer cp = CraftCommonPlayer.get(p);
			
			Jackpot.getInstance().run(cp, npc, npc.data().get("type"));
		}
	}
	
	@Override
	public Location getSpawnLocation()
	{
		return spawnLocation;
	}
	
	@Override
	public String getColoredName()
	{
		return ChatColor.AQUA + "Jackpot";
	}
}