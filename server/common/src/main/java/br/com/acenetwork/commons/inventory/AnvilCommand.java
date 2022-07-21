package br.com.acenetwork.commons.inventory;
import java.text.MessageFormat;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import br.com.acenetwork.commons.Common;
import br.com.acenetwork.commons.manager.FakeAnvil;
import br.com.acenetwork.commons.player.CommonPlayer;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.PacketPlayOutOpenWindow;

public class AnvilCommand extends GUI
{
	private final String command;
	private final boolean clickablePlaceHolder;
	private int task;
	
	public AnvilCommand(CommonPlayer cp, String placeholder, String command)
	{	
		this(cp, placeholder, command, false);
	}
	
	public AnvilCommand(CommonPlayer cp, String placeholder, String command, boolean clickablePlaceHolder)
	{
		super(cp, getInventory(cp.getPlayer()), false);
		
		this.command = command;
		this.clickablePlaceHolder = clickablePlaceHolder;
		
		if(placeholder != null)
		{
			ItemStack item = new ItemStack(Material.PAPER);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(placeholder);
			item.setItemMeta(meta);
			
			inv.setItem(0, item);
		}
		
		task = Bukkit.getScheduler().scheduleSyncRepeatingTask(Common.getPlugin(), () ->
		{
			ItemStack item = inv.getItem(2);
			
			if(item == null)
			{
				return;
			}
			
			ItemMeta meta = item.getItemMeta();
			
			if(meta != null && meta.hasDisplayName())
			{
				meta.setDisplayName(meta.getDisplayName().replace('&', ChatColor.COLOR_CHAR));
				item.setItemMeta(meta);
			}
		}, 0L, 1L);
	}
	
	public void run(String displayName)
	{
		p.closeInventory();
		p.chat(MessageFormat.format(command, displayName));
	}
	
	@EventHandler
	public void ab(InventoryCloseEvent e)
	{
		if(e.getPlayer() != p)
		{
			return;
		}
		Bukkit.getScheduler().cancelTask(task);
		task = 0;
	}
	@EventHandler
	public void clearInventoryOnClose(InventoryCloseEvent e)
	{
		if(e.getPlayer() != p)
		{
			return;
		}
		
		inv.clear();
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent e)
	{
		Player p = cp.getPlayer();
		
		if(e.getWhoClicked() != p)
		{
			return;
		}
		
		e.setCancelled(true);
		
		ItemStack current = e.getCurrentItem();
		
		if(current == null)
		{
			return;
		}
		
		
		String displayName = current.hasItemMeta() ? current.getItemMeta().getDisplayName() : null;
		
		ClickType click = e.getClick();
		int rawSlot = e.getRawSlot();
		
		if(rawSlot == 2)
		{
			final int level = p.getLevel();
			Bukkit.broadcastMessage("p.getLevel() = " + level);
			
			Bukkit.getScheduler().runTask(Common.getPlugin(), () ->
			{
				p.setLevel(p.getLevel());
			});
		}
		if(displayName != null && (rawSlot == 0 && clickablePlaceHolder || rawSlot == 2) && click == ClickType.LEFT)
		{
			run(displayName);
		}
	}
	
	private static Inventory getInventory(Player p)
	{
		EntityPlayer entityPlayer = ((CraftPlayer) p).getHandle();
		FakeAnvil fakeAnvil = new FakeAnvil(entityPlayer);
		int containerId = entityPlayer.nextContainerCounter();
		
		entityPlayer.playerConnection.sendPacket(new PacketPlayOutOpenWindow
				(containerId, "minecraft:anvil", new ChatComponentText("Repairing"), 0));
		
		entityPlayer.activeContainer = fakeAnvil;
		entityPlayer.activeContainer.windowId = containerId;
		entityPlayer.activeContainer.addSlotListener(entityPlayer);
		
		return fakeAnvil.getBukkitView().getTopInventory();
	}
}