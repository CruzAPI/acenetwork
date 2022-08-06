package br.com.acenetwork.craftlandia.inventory;

import java.util.ResourceBundle;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import br.com.acenetwork.commons.CommonsUtil;
import br.com.acenetwork.commons.inventory.GUI;
import br.com.acenetwork.commons.player.CommonPlayer;
import net.md_5.bungee.api.ChatColor;

public class CurrencySelector extends GUI
{
	private final String commandLine;
	
	private ItemStack itemShards;
	private ItemStack itemBTA;
	
	public CurrencySelector(CommonPlayer cp, String commandLine)
	{
		super(cp, () ->
		{
			ResourceBundle bundle = ResourceBundle.getBundle("message", cp.getLocale());
			return Bukkit.createInventory(cp.getPlayer(), InventoryType.HOPPER, bundle.getString("inv.currency-selector"));
		});
		
		this.commandLine = commandLine;
		
		ResourceBundle bundle = ResourceBundle.getBundle("message", cp.getLocale());
		
		ItemMeta meta;
		
		itemShards = new ItemStack(Material.DOUBLE_PLANT, 1);
		meta = itemShards.getItemMeta();
		meta.setDisplayName("" + ChatColor.GOLD + bundle.getString("shards"));
		itemShards.setItemMeta(meta);
		
		itemBTA = new ItemStack(Material.NETHER_STAR, 1);
		meta = itemBTA.getItemMeta();
		meta.setDisplayName("" + ChatColor.DARK_PURPLE + "$BTA");
		itemBTA.setItemMeta(meta);

		inv.setItem(1, itemShards);
		inv.setItem(3, itemBTA);
	}
	
	@EventHandler
	public void asdasdf(InventoryClickEvent e)
	{
		Player p = cp.getPlayer();
		
		if(e.getWhoClicked() != p)
		{
			return;
		}
		
		e.setCancelled(true);
		
		ClickType click = e.getClick();
		ItemStack current = e.getCurrentItem();
		
		if(CommonsUtil.compareDisplayName(current, itemShards) && click == ClickType.LEFT)
		{
			p.closeInventory();
			Bukkit.dispatchCommand(p, commandLine + " SHARDS");
		}
		else if(CommonsUtil.compareDisplayName(current, itemBTA) && click == ClickType.LEFT)
		{
			p.closeInventory();
			Bukkit.dispatchCommand(p, commandLine + " $BTA");
		}
	}
}