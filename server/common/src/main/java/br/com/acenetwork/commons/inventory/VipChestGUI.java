package br.com.acenetwork.commons.inventory;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.acenetwork.commons.Common;
import br.com.acenetwork.commons.CommonsUtil;
import br.com.acenetwork.commons.executor.VipChest;
import br.com.acenetwork.commons.manager.CommonPlayerData;
import br.com.acenetwork.commons.player.CommonPlayer;
import net.md_5.bungee.api.ChatColor;

public class VipChestGUI extends GUI
{
	private final VipChest instance = VipChest.getInstance();
	
	public static boolean isItemStackVIP(ItemStack item)
	{
		return CommonsUtil.containsUUID(item, VipChest.getInstance().getUUID());
	}
	
	public static boolean isValidItemStackVIP(ItemStack item)
	{
		if(!isItemStackVIP(item))
		{
			return false;
		}
		
		List<UUID> list = CommonsUtil.getHiddenUUIDs(item);
		
		if(list.size() != 2)
		{
			return false;
		}
		
		return VipChest.getInstance().getValidVips().contains(list.get(1));
	}
	
	public VipChestGUI(CommonPlayer cp)
	{
		super(cp, () ->
		{
			return Bukkit.createInventory(cp.getPlayer(), 3 * 9);
		});
		
		UUID[] uuids = Optional.ofNullable(cp.getCommonPlayerData().getVipInvContents()).orElse(new UUID[inv.getSize()]);
		
		for(int i = 0; i < inv.getSize() && i < uuids.length; i++)
		{
			if(uuids[i] == null)
			{
				inv.setItem(i, null);
				continue;
			}
			
			inv.setItem(i, instance.getVipSupplier().get(null, uuids[i]));
		}
		
		int slot;
		
		CommonPlayerData data = cp.getCommonPlayerData();
		
		while((slot = inv.firstEmpty()) != -1 && data.getVip() > 0)
		{
			data.setVip(data.getVip() - 1);
			inv.setItem(slot, instance.getVipSupplier().get(null));
		}
		
		update();
	}
	
	public void update()
	{
		UUID[] uuids = new UUID[inv.getSize()];
		
		for(int i = 0; i < inv.getSize(); i++)
		{
			ItemStack item = inv.getItem(i);
			List<UUID> list = CommonsUtil.getHiddenUUIDs(item);
			
			if(list.size() != 2 || !list.get(0).equals(instance.getUUID()) || !instance.getValidVips().contains(list.get(1))) //invalid vip item
			{
				uuids[i] = null;
				continue;
			}
			
			uuids[i] = list.get(1);
		}
		
		cp.getCommonPlayerData().setVipInvContents(uuids);
	}
	
	public static ItemStack getVipItem()
	{
		return VipChest.getInstance().getVipSupplier().get(null);
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void monitor(InventoryDragEvent e)
	{
		Bukkit.getScheduler().runTask(Common.getPlugin(), () ->
		{
			update();
		});
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void monitor(InventoryClickEvent e)
	{
		if(e.getAction() == InventoryAction.NOTHING)
		{
			return;
		}
		
		Bukkit.getScheduler().runTask(Common.getPlugin(), () ->
		{
			update();
		});
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onInventoryClick(InventoryDragEvent e)
	{
		if(e.getWhoClicked() != p)
		{
			return;
		}
		
		if(e.getNewItems().entrySet().stream().filter(x -> x.getKey() < inv.getSize() 
				&& x.getValue().getType() != Material.AIR && !isItemStackVIP(x.getValue())).count() > 0L)
		{
			e.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onInventoryClick(InventoryClickEvent e)
	{
		if(e.getWhoClicked() != p)
		{
			return;
		}
		
		ItemStack current = e.getCurrentItem();
		ItemStack cursor = e.getCursor();
		
		Inventory clicked = e.getClickedInventory();
		InventoryAction action = e.getAction();
		
		if(clicked == null)
		{
			return;
		}
		
		if(inv.equals(clicked))
		{
			switch(action)
			{
			case PLACE_ALL:
			case PLACE_ONE:
			case PLACE_SOME:
			case SWAP_WITH_CURSOR:
				if(!isItemStackVIP(cursor))
				{
					e.setCancelled(true);
					return;
				}
				return;
			case HOTBAR_SWAP:
				ItemStack hotbar = p.getInventory().getItem(e.getHotbarButton());
				
				if(hotbar.getType() != Material.AIR && !isItemStackVIP(hotbar))
				{
					e.setCancelled(true);
					return;
				}
				return;
			default:
				return;
			}
		}
		
		switch(action)
		{
		case MOVE_TO_OTHER_INVENTORY:
			if(!isItemStackVIP(current))
			{
				e.setCancelled(true);
				return;
			}
		default:
			break;
		}
		
		e.getHotbarButton();
		
		if(!isItemStackVIP(current))
		{
//			Bukkit.broadcastMessage("not a vip item, canceling InventoryClickEvent");
//			e.setCancelled(true);
			return;
		}
	}
}