package br.com.acenetwork.craftlandia.inventory;

import java.util.Iterator;
import java.util.ResourceBundle;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import br.com.acenetwork.commons.CommonsUtil;
import br.com.acenetwork.commons.inventory.AnvilCommand;
import br.com.acenetwork.commons.inventory.Scroller;
import br.com.acenetwork.commons.player.CommonPlayer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

public class ItemSelector extends Scroller
{
	private ItemStack itemSearch;
	private ItemStack clearSearch;
	private String search;
	
	public ItemSelector(CommonPlayer cp)
	{
		this(cp, null);
	}
	
	private class TranslatedItem extends Item
	{
		public TranslatedItem(ItemStack itemStack, ResourceBundle bundle)
		{
			super(() ->
			{
				ItemMeta meta = itemStack.getItemMeta();
				meta.setDisplayName(ChatColor.WHITE + bundle.getString(CommonsUtil.getTranslation(itemStack)));
				itemStack.setItemMeta(meta);
				
				return itemStack;
			});
		}
		
	}
	
	@SuppressWarnings("deprecation")
	public ItemSelector(CommonPlayer cp, String search)
	{
		super(cp, "inv.item-selector");
		
		this.search = search;
		
		ResourceBundle minecraftBundle = ResourceBundle.getBundle("minecraft", cp.getLocale());
		ResourceBundle bundle = ResourceBundle.getBundle("message", cp.getLocale());
		
		itemList.add(new TranslatedItem(new ItemStack(1, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(1, 1, (short) 1), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(1, 1, (short) 2), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(1, 1, (short) 3), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(1, 1, (short) 4), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(1, 1, (short) 5), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(1, 1, (short) 6), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(2, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(3, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(3, 1, (short) 1), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(3, 1, (short) 2), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(4, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(5, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(5, 1, (short) 1), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(5, 1, (short) 2), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(5, 1, (short) 3), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(5, 1, (short) 4), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(5, 1, (short) 5), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(7, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(12, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(12, 1, (short) 1), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(13, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(14, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(15, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(16, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(17, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(17, 1, (short) 1), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(17, 1, (short) 2), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(17, 1, (short) 3), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(19, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(19, 1, (short) 1), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(20, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(21, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(22, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(24, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(24, 1, (short) 1), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(24, 1, (short) 2), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(35, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(35, 1, (short) 1), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(35, 1, (short) 2), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(35, 1, (short) 3), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(35, 1, (short) 4), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(35, 1, (short) 5), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(35, 1, (short) 6), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(35, 1, (short) 7), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(35, 1, (short) 8), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(35, 1, (short) 9), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(35, 1, (short) 10), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(35, 1, (short) 11), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(35, 1, (short) 12), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(35, 1, (short) 13), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(35, 1, (short) 14), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(35, 1, (short) 15), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(41, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(42, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(44, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(44, 1, (short) 1), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(44, 1, (short) 3), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(44, 1, (short) 4), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(44, 1, (short) 5), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(44, 1, (short) 6), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(44, 1, (short) 7), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(45, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(47, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(48, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(49, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(53, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(56, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(57, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(67, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(73, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(79, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(80, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(82, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(86, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(87, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(88, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(89, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(91, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(95, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(95, 1, (short) 1), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(95, 1, (short) 2), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(95, 1, (short) 3), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(95, 1, (short) 4), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(95, 1, (short) 5), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(95, 1, (short) 6), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(95, 1, (short) 7), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(95, 1, (short) 8), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(95, 1, (short) 9), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(95, 1, (short) 10), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(95, 1, (short) 11), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(95, 1, (short) 12), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(95, 1, (short) 13), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(95, 1, (short) 14), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(95, 1, (short) 15), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(98, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(98, 1, (short) 1), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(98, 1, (short) 2), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(98, 1, (short) 3), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(103, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(108, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(109, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(110, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(112, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(114, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(121, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(126, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(126, 1, (short) 1), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(126, 1, (short) 2), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(126, 1, (short) 3), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(126, 1, (short) 4), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(126, 1, (short) 5), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(128, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(129, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(133, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(134, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(135, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(136, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(139, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(139, 1, (short) 1), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(153, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(155, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(155, 1, (short) 1), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(155, 1, (short) 2), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(156, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(159, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(159, 1, (short) 1), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(159, 1, (short) 2), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(159, 1, (short) 3), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(159, 1, (short) 4), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(159, 1, (short) 5), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(159, 1, (short) 6), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(159, 1, (short) 7), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(159, 1, (short) 8), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(159, 1, (short) 9), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(159, 1, (short) 10), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(159, 1, (short) 11), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(159, 1, (short) 12), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(159, 1, (short) 13), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(159, 1, (short) 14), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(159, 1, (short) 15), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(162, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(162, 1, (short) 1), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(163, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(164, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(168, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(168, 1, (short) 1), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(168, 1, (short) 2), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(169, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(170, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(172, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(173, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(174, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(179, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(179, 1, (short) 1), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(179, 1, (short) 2), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(180, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(182, 1, (short) 0), minecraftBundle));
		
		Iterator<Item> iterator = itemList.iterator();
		
		ItemMeta meta;
		
		if(search != null)
		{
			while(iterator.hasNext())
			{
				Item item = iterator.next();
				
				if(!item.getItemMeta().getDisplayName().substring(2).toLowerCase().contains(search.toLowerCase()))
				{
					iterator.remove();
				}
			}
			
			clearSearch = new ItemStack(Material.INK_SACK, 1, (short) 1);
			meta = clearSearch.getItemMeta();
			meta.setDisplayName(ChatColor.RED + bundle.getString("inv.item-selector.clear-search"));
			clearSearch.setItemMeta(meta);
			
			inv.setItem(2, clearSearch);
		}
		
		itemSearch = new ItemStack(Material.NAME_TAG);
		meta = itemSearch.getItemMeta();
		meta.setDisplayName(ChatColor.WHITE + bundle.getString("inv.item-selector.search-item"));
		itemSearch.setItemMeta(meta);
		
		inv.setItem(3, itemSearch);
		
		refresh();
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
		
		if(CommonsUtil.compareDisplayName(current, itemSearch) && click == ClickType.LEFT)
		{
			ResourceBundle bundle = ResourceBundle.getBundle("message", cp.getLocale());
			
			new AnvilCommand(cp, search == null ? bundle.getString("inv.item-selector.search-item") : search, "/shopsearch {0}", search != null);
		}
		else if(CommonsUtil.compareDisplayName(current, clearSearch) && click == ClickType.LEFT)
		{
			Bukkit.dispatchCommand(p, "shop");
		}
		else
		{
			for(ItemStack item : itemList)
			{
				if(item.equals(current))
				{
					p.closeInventory();
					Bukkit.dispatchCommand(p, "shop " + item.getTypeId() + " " + item.getDurability());
					return;
				}
			}
				
		}
	}
}