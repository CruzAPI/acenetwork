package br.com.acenetwork.craftlandia.warp;

import java.util.HashSet;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.logging.log4j.core.net.Priority;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockMultiPlaceEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.EntityBlockFormEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityCreatePortalEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.SheepRegrowWoolEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.PortalCreateEvent;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Dispenser;

import br.com.acenetwork.commons.CommonsUtil;
import br.com.acenetwork.commons.event.CustomStructureGrowEvent;
import br.com.acenetwork.commons.player.CommonPlayer;
import br.com.acenetwork.commons.player.craft.CraftCommonPlayer;
import br.com.acenetwork.craftlandia.Main;
import net.md_5.bungee.api.ChatColor;

public abstract class Warp implements Listener
{
	public static final Set<Warp> SET = new HashSet<>();
	
	protected final World w;
	protected final String worldName;
	
	public Warp(World w)
	{
		this.w = w;
		this.worldName = w.getName();
		Bukkit.getPluginManager().registerEvents(this, Main.getPlugin());
		SET.add(this);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void a(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		
		if(p.getWorld() != w)
		{
			return;
		}
		
		ItemStack item = e.getItem();
		Block b = e.getClickedBlock();
		
		if(b != null && CommonsUtil.isInteractable(b.getType()))
		{
			
		}
		else if(item != null && item.getType() == Material.BOAT && b != null)
		{
			e.setCancelled(isSpawnProtection(b.getLocation()));
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void a(BlockGrowEvent e)
	{
		Block b = e.getBlock();
		
		if(b.getWorld() != w)
		{
			return;
		}
		
		BlockState bs = e.getNewState();
		e.setCancelled(isSpawnProtection(b.getLocation()) || isSpawnProtection(bs.getLocation()));
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void a(BlockSpreadEvent e)
	{
		Block b = e.getBlock();
		
		if(b.getWorld() != w)
		{
			return;
		}
		
		e.setCancelled(isSpawnProtection(b.getLocation()));
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void a(BlockFadeEvent e)
	{
		Block b = e.getBlock();
		
		if(b.getWorld() != w)
		{
			return;
		}
		
		e.setCancelled(isSpawnProtection(b.getLocation()));
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void a(SheepRegrowWoolEvent e)
	{
		
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void a(PlayerInteractEntityEvent e)
	{
		Entity entity = e.getRightClicked();
		
		if(entity.getWorld() != w)
		{
			return;
		}
		
		if(entity instanceof ItemFrame)
		{
			e.setCancelled(isSpawnProtection(entity.getLocation()));
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void a(EntityDamageEvent e)
	{
		Entity entity = e.getEntity();
		
		if(entity.getWorld() != w)
		{
			return;
		}
		
		if(entity instanceof ItemFrame)
		{
			e.setCancelled(isSpawnProtection(entity.getLocation()));
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void a(HangingBreakEvent e)
	{
		Entity entity = e.getEntity();
		
		if(entity.getWorld() != w)
		{
			return;
		}
		
		e.setCancelled(isSpawnProtection(entity.getLocation()));
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void a(HangingPlaceEvent e)
	{
		Entity entity = e.getEntity();
		
		if(entity.getWorld() != w)
		{
			return;
		}
		
		e.setCancelled(isSpawnProtection(entity.getLocation()));
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void b(PlayerBucketFillEvent e)
	{
		Block b = e.getBlockClicked();
		
		if(b.getWorld() != w)
		{
			return;
		}
		
		e.setCancelled(isSpawnProtection(b.getLocation()));
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void b(PlayerBucketEmptyEvent e)
	{
		Block b = e.getBlockClicked();
		
		if(b.getWorld() != w)
		{
			return;
		}
		
		e.setCancelled(isSpawnProtection(b.getLocation()));
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void b(BlockBreakEvent e)
	{
		Block b = e.getBlock();
		
		if(b.getWorld() != w)
		{
			return;
		}
		
		e.setCancelled(isSpawnProtection(b.getLocation()));
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void b(BlockPlaceEvent e)
	{
		Block b = e.getBlock();
		
		if(b.getWorld() != w)
		{
			return;
		}
		
		e.setCancelled(isSpawnProtection(b.getLocation()));
		
		if(!e.isCancelled() && e.getBlock().getType() == Material.SPONGE)
		{
			Bukkit.broadcastMessage("sponge...");
			e.setCancelled(true);
			Bukkit.getScheduler().runTask(Main.getPlugin(), () ->
			{
				b.setType(Material.SPONGE, false);
			});
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void on(CreatureSpawnEvent e)
	{
		Location l = e.getLocation();
		
		if(l.getWorld() != w)
		{
			return;
		}
		
		e.setCancelled(isSpawnProtection(l));
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void b(EntityChangeBlockEvent e)
	{
		if(e.getBlock().getWorld() != w)
		{
			return;
		}
		
		if(e.getEntity().getType() == EntityType.ENDERMAN)
		{
			e.setCancelled(isSpawnProtection(e.getBlock().getLocation()));
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void b(EntityExplodeEvent e)
	{
		if(e.getLocation().getWorld() != w)
		{
			return;
		}
		
		Iterator<Block> iterator = e.blockList().iterator();
		
		while(iterator.hasNext())
		{
			if(isSpawnProtection(iterator.next().getLocation()))
			{
				iterator.remove();
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void b(BlockExplodeEvent e)
	{
		if(e.getBlock().getWorld() != w)
		{
			return;
		}
		
		Iterator<Block> iterator = e.blockList().iterator();
		
		while(iterator.hasNext())
		{
			if(isSpawnProtection(iterator.next().getLocation()))
			{
				iterator.remove();
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void a(LeavesDecayEvent e)
	{
		Block b = e.getBlock();
		
		if(b.getWorld() != w)
		{
			return;
		}
		
		e.setCancelled(isSpawnProtection(b.getLocation()));
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void a(CustomStructureGrowEvent ce)
	{
		StructureGrowEvent e = ce.getStructureGrowEvent();
		
		if(e.getWorld() != w)
		{
			return;
		}
		
		e.getBlocks().removeAll(e.getBlocks().stream().filter(x -> isSpawnProtection(x.getLocation())).collect(Collectors.toList()));
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void a(EntityBlockFormEvent e)
	{
		Block b = e.getBlock();
		
		if(b.getWorld() != w)
		{
			return;
		}
		
		e.setCancelled(isSpawnProtection(b.getLocation()));
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void a(BlockFormEvent e)
	{
		Block b = e.getBlock();
		
		if(b.getWorld() != w)
		{
			return;
		}
		
		e.setCancelled(isSpawnProtection(b.getLocation()));
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void a(BlockDispenseEvent e)
	{
		Block b = e.getBlock();
		
		if(b.getWorld() != w)
		{
			return;
		}
		
		Dispenser dispenser = (Dispenser) b.getState().getData();
		Block relative = b.getRelative(dispenser.getFacing());
		e.setCancelled(CommonsUtil.isDispensable(e.getItem().getType()) && isSpawnProtection(relative.getLocation()));
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void a(EntityCreatePortalEvent e)
	{
		for(BlockState blocks : e.getBlocks())
		{
			if(blocks.getWorld() != w)
			{
				continue;
			}
			
			if(isSpawnProtection(blocks.getLocation()))
			{
				if(e.getEntity() instanceof Player)
				{
					Player p = (Player) e.getEntity();
					ResourceBundle bundle = ResourceBundle.getBundle("message", CommonsUtil.getLocaleFromMinecraft(p.spigot().getLocale()));
					p.sendMessage(ChatColor.RED + bundle.getString("raid.event.portal-create.cant-create-portal-spawn"));
				}
				
				e.setCancelled(true);
				return;
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void a(BlockPistonExtendEvent e)
	{
		Block b = e.getBlock();
		
		if(b.getWorld() != w)
		{
			return;
		}
		
		if(isSpawnProtection(b.getLocation()))
		{
			e.setCancelled(true);
			return;
		}
		
		Block relative = b.getRelative(e.getDirection());
		
		if(isSpawnProtection(relative.getLocation()))
		{
			e.setCancelled(true);
			return;
		}

		
		for(Block blocks : e.getBlocks())
		{
			blocks = blocks.getRelative(e.getDirection());
			
			if(isSpawnProtection(blocks.getLocation()))
			{
				e.setCancelled(true);
				return;
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void a(BlockPistonRetractEvent e)
	{
		Block b = e.getBlock();
		
		if(b.getWorld() != w)
		{
			return;
		}
		
		if(isSpawnProtection(b.getLocation()))
		{
			e.setCancelled(true);
			return;
		}
		
		for(Block blocks : e.getBlocks())
		{
			if(isSpawnProtection(blocks.getLocation()))
			{
				e.setCancelled(true);
				return;
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void a(EntityChangeBlockEvent e)
	{
		Block b = e.getBlock();
		
		if(b.getWorld() != w)
		{
			return;
		}
		
		e.setCancelled(isSpawnProtection(b.getLocation()));
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void a(BlockFromToEvent e)
	{
		Block b = e.getToBlock();
		
		if(b.getWorld() != w)
		{
			return;
		}
		
		e.setCancelled(isSpawnProtection(b.getLocation()));
		
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void a(BlockMultiPlaceEvent e)
	{
		Block b = e.getBlock();
		
		if(b.getWorld() != w)
		{
			return;
		}
		
		for(BlockState bs : e.getReplacedBlockStates())
		{
			if(isSpawnProtection(bs.getLocation()))
			{
				e.setCancelled(true);
				return;
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void a(BlockPhysicsEvent e)
	{
		Block b = e.getBlock();
		
		if(b.getWorld() != w)
		{
			return;
		}
		
		e.setCancelled(isSpawnProtection(b.getLocation()));
	}
	
	public Set<CommonPlayer> getCommonPlayers()
	{
		return w.getPlayers().stream().map(x -> CraftCommonPlayer.get(x)).collect(Collectors.toSet());
	}
	
	public boolean isSpawnProtection(Location l)
	{
		return true;
	}
}