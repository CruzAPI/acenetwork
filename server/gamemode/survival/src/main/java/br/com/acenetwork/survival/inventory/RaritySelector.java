package br.com.acenetwork.survival.inventory;

import java.util.Iterator;
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
import br.com.acenetwork.commons.inventory.AnvilCommand;
import br.com.acenetwork.commons.inventory.GUI;
import br.com.acenetwork.commons.inventory.Scroller;
import br.com.acenetwork.commons.player.CommonPlayer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

public class RaritySelector extends GUI
{
	private final String command;
	
	private ItemStack itemCommon;
	private ItemStack itemRare;
	private ItemStack itemLegendary;
	
	public RaritySelector(CommonPlayer cp, String command)
	{
		super(cp, "inv.rarity-selector", InventoryType.HOPPER);
		
		this.command = command;
		
		ResourceBundle bundle = ResourceBundle.getBundle("message", cp.getLocale());
		
		ItemMeta meta;
		
		itemCommon = new ItemStack(Material.STAINED_CLAY, 1, (short) 5);
		meta = itemCommon.getItemMeta();
		meta.setDisplayName("" + ChatColor.GREEN + ChatColor.BOLD + bundle.getString("common"));
		itemCommon.setItemMeta(meta);
		
		itemRare = new ItemStack(Material.STAINED_CLAY, 1, (short) 11);
		meta = itemRare.getItemMeta();
		meta.setDisplayName("" + ChatColor.DARK_PURPLE + ChatColor.BOLD + bundle.getString("rare"));
		itemRare.setItemMeta(meta);

		itemLegendary = new ItemStack(Material.STAINED_CLAY, 1, (short) 4);
		meta = itemLegendary.getItemMeta();
		meta.setDisplayName("" + ChatColor.GOLD + ChatColor.BOLD + bundle.getString("legendary"));
		itemLegendary.setItemMeta(meta);
		
		inv.setItem(1, itemCommon);
		inv.setItem(2, itemRare);
		inv.setItem(3, itemLegendary);
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
		
		if(CommonsUtil.compareDisplayName(current, itemCommon) && click == ClickType.LEFT)
		{
			p.closeInventory();
			Bukkit.dispatchCommand(p, command + " COMMON");
		}
		else if(CommonsUtil.compareDisplayName(current, itemRare) && click == ClickType.LEFT)
		{
			p.closeInventory();
			Bukkit.dispatchCommand(p, command + " RARE");
		}
		else if(CommonsUtil.compareDisplayName(current, itemLegendary) && click == ClickType.LEFT)
		{
			p.closeInventory();
			Bukkit.dispatchCommand(p, command + " LEGENDARY");
		}
	}
}