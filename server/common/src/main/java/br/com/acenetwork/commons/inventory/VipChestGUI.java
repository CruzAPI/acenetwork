package br.com.acenetwork.commons.inventory;
import java.util.UUID;
import java.util.function.Supplier;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import br.com.acenetwork.commons.CommonsUtil;
import br.com.acenetwork.commons.executor.VipChest;
import br.com.acenetwork.commons.player.CommonPlayer;
import net.md_5.bungee.api.ChatColor;

public class VipChestGUI extends GUI
{
	private static final Supplier<ItemStack> VIP = () ->
	{
		ItemStack item = new ItemStack(Material.WOOL, 1, (short) 5);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(VipChest.getVipFirstHiddenUUID() + ChatColor.GREEN + ChatColor.BOLD + "VIP" + CommonsUtil.getRandomItemUUID());
		item.setItemMeta(meta);
		return item;
	};
	
	public static boolean isItemStackVIP(ItemStack item)
	{
		if(item == null || !item.hasItemMeta() || !item.getItemMeta().hasDisplayName())
		{
			return false;
		}
		
		return item.getItemMeta().getDisplayName().startsWith(VipChest.getVipFirstHiddenUUID());
	}
	
	public static boolean isValidItemStackVIP(ItemStack item)
	{
		if(!isItemStackVIP(item))
		{
			return false;
		}
		
		
		String hiddenLastUUID;
		UUID uuid;
		
		try
		{
			hiddenLastUUID = CommonsUtil.getHiddenLastUUID(item);
			uuid = CommonsUtil.convertHiddenUUID(hiddenLastUUID);
		}
		catch(Exception e)
		{
			return false;
		}
		
		return !VipChest.ACTIVATED_VIPS.contains(uuid);
	}
	
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
//		
//		ItemStack current = e.getCurrentItem();
//		ItemStack cursor = e.getCursor();
		
		if(!isItemStackVIP(e.getCurrentItem()))
		{
//			Bukkit.broadcastMessage("not a vip item, canceling InventoryClickEvent");
//			e.setCancelled(true);
			return;
		}
	}
}