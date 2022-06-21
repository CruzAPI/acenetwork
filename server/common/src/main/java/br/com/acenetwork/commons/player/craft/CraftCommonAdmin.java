package br.com.acenetwork.commons.player.craft;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.ContainerBlock;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.Inventory;

import br.com.acenetwork.commons.player.CommonAdmin;

public class CraftCommonAdmin extends CraftCommonPlayer implements CommonAdmin
{
	private boolean build;
	
	public CraftCommonAdmin(Player p)
	{
		super(p);
	}
	
	@Override
	public void reset()
	{
		super.reset();
		setInvis(true);
		
		p.setGameMode(GameMode.CREATIVE);
		p.setAllowFlight(true);
	}

	@Override
	public void setBuild(boolean value)
	{
		build = value;
	}

	@Override
	public boolean canBuild()
	{
		return build;
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void a(HangingPlaceEvent e)
	{
		if(e.getPlayer() != p)
		{
			return;
		}
		
		e.setCancelled(!build);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void a(HangingBreakByEntityEvent e)
	{
		if(e.getRemover() != p)
		{
			return;
		}
		
		e.setCancelled(!build);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void on(PlayerGameModeChangeEvent e)
	{
		if(e.getPlayer() != p)
		{
			return;
		}
		
		if(e.getNewGameMode() != GameMode.CREATIVE)
		{
			e.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityTarget(EntityTargetEvent e)
	{
		if(e.getTarget() != p)
		{
			return;
		}
		
		e.setCancelled(!build);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityDamageByEntity(EntityDamageByEntityEvent e)
	{
		if(e.getDamager() != p)
		{
			return;
		}
		
		e.setCancelled(!build);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockBreak(BlockBreakEvent e)
	{		
		if(e.getPlayer() != p)
		{
			return;
		}

		e.setCancelled(!build);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockPlace(BlockPlaceEvent e)
	{		
		if(e.getPlayer() != p)
		{
			return;
		}

		e.setCancelled(!build);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityPickupItem(PlayerPickupItemEvent e)
	{		
		if(e.getPlayer() != p)
		{
			return;
		}

		e.setCancelled(!build);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerDropItem(PlayerDropItemEvent e)
	{		
		if(e.getPlayer() != p)
		{
			return;
		}

		e.setCancelled(!build);
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onInventoryClick(InventoryClickEvent e)
	{		
		if(e.getWhoClicked() != p)
		{
			return;
		}

		e.setCancelled(!build);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void a(PlayerInteractEntityEvent e)
	{
		if(e.getPlayer() != p)
		{
			return;
		}
		
		Entity entity = e.getRightClicked();
		
		if(entity instanceof ItemFrame)
		{
			e.setCancelled(!build);
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerInteract(PlayerInteractEvent e)
	{		
		if(e.getPlayer() != p)
		{
			return;
		}

		Block b = e.getClickedBlock();
		
		e.setCancelled(!build);
		
		if(b != null && b.getState() instanceof ContainerBlock)
		{
			ContainerBlock chest = (ContainerBlock) b.getState();
			
			if(e.isCancelled())
			{
				Inventory inv;
				
				if(chest.getInventory().getType() == InventoryType.CHEST)
				{
					inv = Bukkit.createInventory(p, chest.getInventory().getSize());
				}
				else
				{
					inv = Bukkit.createInventory(p, chest.getInventory().getType());
				}
				
				inv.setContents(chest.getInventory().getContents());
				p.openInventory(inv);
			}
		}
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerInteractEntity(PlayerInteractEntityEvent e)
	{	
		if(e.getPlayer() != p)
		{
			return;
		}
				
		Entity entity = e.getRightClicked();
		
		if(entity instanceof Player && p.getItemInHand().getType() == Material.AIR)
		{
			Player t = (Player) entity;

			p.openInventory(t.getInventory());
		}
	}
}