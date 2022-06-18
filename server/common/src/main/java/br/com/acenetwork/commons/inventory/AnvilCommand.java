package br.com.acenetwork.commons.inventory;
import java.text.MessageFormat;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import br.com.acenetwork.commons.manager.FakeAnvil;
import br.com.acenetwork.commons.player.CommonPlayer;
import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.PacketPlayOutOpenWindow;

public class AnvilCommand extends GUI
{
	private final String command;
	private final boolean clickablePlaceHolder;
	
	public AnvilCommand(CommonPlayer cp, String placeholder, String command)
	{	
		this(cp, placeholder, command, false);
	}
	
	public AnvilCommand(CommonPlayer cp, String placeholder, String command, boolean clickablePlaceHolder)
	{
		super(cp, getInventory(cp.getPlayer()), false);
		
		this.command = command;
		this.clickablePlaceHolder = clickablePlaceHolder;
		
		ItemStack item = new ItemStack(Material.PAPER);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(placeholder);
		item.setItemMeta(meta);

		inv.setItem(0, item);
	}

	@EventHandler
	public void onInventoryClose(InventoryCloseEvent e)
	{
		Player p = cp.getPlayer();
	
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
		 
		if(displayName != null && (rawSlot == 0 && clickablePlaceHolder || rawSlot == 2) && click == ClickType.LEFT)
		{
			p.closeInventory();
			p.chat(MessageFormat.format(command, displayName));
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