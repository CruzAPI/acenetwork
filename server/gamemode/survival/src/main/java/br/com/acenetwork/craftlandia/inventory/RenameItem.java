package br.com.acenetwork.craftlandia.inventory;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.acenetwork.commons.inventory.AnvilCommand;
import br.com.acenetwork.commons.player.CommonPlayer;
import br.com.acenetwork.craftlandia.Main;
import br.com.acenetwork.craftlandia.Rarity;

public class RenameItem extends AnvilCommand
{
	private final Rarity rarity;
	private final Block b;
	private ItemStack[] contents;
	private int task;
	
	public RenameItem(CommonPlayer cp, Rarity rarity, Block b, ItemStack[] contents)
	{
		super(cp, null, null);
		this.rarity = rarity;
		this.contents = contents;
		this.b = b;
		
		task = new BukkitRunnable()
		{
			@Override
			public void run()
			{
				if(b.getType() != Material.ANVIL 
						|| p.getWorld() != b.getWorld()
						|| p.getLocation().distance(b.getLocation()) > 5.0D)
				{
					this.cancel();
					p.closeInventory();
					return;
				}
			}
		}.runTaskTimer(Main.getInstance(), 0L, 1L).getTaskId();
		
		inv.setItem(0, contents[0]);
		
		p.updateInventory();
	}
	
	@Override
	@EventHandler
	public void clearInventoryOnClose(InventoryCloseEvent e)
	{
		
	}
	
	@EventHandler
	public void cancelTask(InventoryCloseEvent e)
	{
		if(e.getPlayer() != p)
		{
			return;
		}
		
		if(contents != null)
		{
			for(ItemStack item : contents)
			{
				if(item != null)
				{
					p.getWorld().dropItem(p.getLocation().add(0.0D, 1.25D, 0.0D), item).setVelocity(p.getLocation().getDirection().multiply(0.35D));
				}
			}
			
			contents = null;
		}
		
		inv.clear();
		Bukkit.getScheduler().cancelTask(task);
		task = 0;
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void cancelTask(PlayerTeleportEvent e)
	{
		if(e.getPlayer() != p)
		{
			return;
		}
		
		Bukkit.getScheduler().cancelTask(task);
		task = 0;
	}
	
	@Override
	public void run(String displayName)
	{
		inv.clear();
		ItemStack[] clone = Arrays.stream(contents).map(x -> x == null ? null : x.clone()).toArray(x -> new ItemStack[x]);
		contents = null;
		new CustomAnvil(cp, rarity, b, displayName, clone);
	}
}