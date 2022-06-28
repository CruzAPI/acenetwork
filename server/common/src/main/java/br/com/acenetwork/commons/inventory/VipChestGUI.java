package br.com.acenetwork.commons.inventory;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.function.Supplier;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.world.WorldSaveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import br.com.acenetwork.commons.Common;
import br.com.acenetwork.commons.CommonsUtil;
import br.com.acenetwork.commons.event.SocketEvent;
import br.com.acenetwork.commons.manager.CommonsConfig;
import br.com.acenetwork.commons.manager.CommonsConfig.Type;
import br.com.acenetwork.commons.player.CommonPlayer;
import br.com.acenetwork.commons.player.craft.CraftCommonPlayer;
import net.md_5.bungee.api.ChatColor;

public class VipChestGUI extends GUI
{
	private static final Supplier<ItemStack> VIP = () ->
	{
		ItemStack item = new ItemStack(Material.WOOL, 1, (short) 5);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("" + ChatColor.GREEN + ChatColor.BOLD + "VIP");
		item.setItemMeta(meta);
		return item;
	};
	
	public VipChestGUI(CommonPlayer cp, Inventory inv)
	{
		super(cp, () ->
		{
			return inv;
		});
	}
	
	public static ItemStack getVipItem(int amount)
	{
		ItemStack item = VIP.get().clone();
		item.setAmount(amount);
		return item;
	}
	
	public static ItemStack getVipItem()
	{
		return VIP.get().clone();
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onInventoryClick(InventoryClickEvent e)
	{
		Player p = cp.getPlayer();
		
		if(e.getWhoClicked() != p)
		{
			return;
		}
		
		if(!getVipItem().isSimilar(e.getCurrentItem()))
		{
			e.setCancelled(true);
			return;
		}
	}
}