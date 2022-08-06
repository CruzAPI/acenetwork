package br.com.acenetwork.craftlandia.inventory;

import java.util.ResourceBundle;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import br.com.acenetwork.commons.inventory.Scroller;
import br.com.acenetwork.commons.player.CommonPlayer;
import br.com.acenetwork.craftlandia.Rarity;
import br.com.acenetwork.craftlandia.item.CommonRandomItem;
import br.com.acenetwork.craftlandia.item.LegendaryRandomItem;
import br.com.acenetwork.craftlandia.item.NormalRandomItem;
import br.com.acenetwork.craftlandia.item.RareRandomItem;
import br.com.acenetwork.craftlandia.item.VipItem;
import br.com.acenetwork.craftlandia.manager.ItemSpecial;
import net.md_5.bungee.api.ChatColor;

public class SpecialItemSelector extends Scroller
{
	private ItemStack normalItems;
	
	private class ItemKey extends Item
	{
		private final String key;
		
		public ItemKey(ItemStack itemStack, String key)
		{
			super(itemStack);
			this.key = key;
		}
	}
	
	public SpecialItemSelector(CommonPlayer cp)
	{
		super(cp, "inv.item-selector");
		
		itemList.add(new ItemKey(ItemSpecial.getInstance(VipItem.class).getItemStack(null, UUID.randomUUID()), "vip"));
		itemList.add(new ItemKey(ItemSpecial.getInstance(NormalRandomItem.class).getItemStack(null, UUID.randomUUID()), "random_item"));
		itemList.add(new ItemKey(ItemSpecial.getInstance(CommonRandomItem.class).getItemStack(null, UUID.randomUUID()), "common_random_item"));
		itemList.add(new ItemKey(ItemSpecial.getInstance(RareRandomItem.class).getItemStack(null, UUID.randomUUID()), "rare_random_item"));
		itemList.add(new ItemKey(ItemSpecial.getInstance(LegendaryRandomItem.class).getItemStack(null, UUID.randomUUID()), "legendary_random_item"));
		
		ResourceBundle bundle = ResourceBundle.getBundle("message", cp.getLocale());
		
		ItemMeta meta;
		
		normalItems = new ItemStack(Material.GRASS);
		meta = normalItems.getItemMeta();
		meta.setDisplayName(ChatColor.WHITE + bundle.getString("inv.item-selector.normal-items"));
		normalItems.setItemMeta(meta);		
		inv.setItem(1, normalItems);
		
		refresh();
	}
	
	@EventHandler
	public void a(InventoryClickEvent e)
	{
		if(e.getWhoClicked() != p)
		{
			return;
		}
		
		e.setCancelled(true);
		
		ClickType click = e.getClick();
		ItemStack current = e.getCurrentItem();
		
		if(normalItems.equals(current) && click == ClickType.LEFT)
		{
			p.closeInventory();
			Bukkit.dispatchCommand(p, "shop");
		}
		else if(click == ClickType.LEFT)
		{
			for(Item item : itemList)
			{
				if(item.equals(current))
				{
					p.closeInventory();
					Bukkit.dispatchCommand(p, "shop " + ((ItemKey) item).key + " 0 1 " + Rarity.COMMON.name() + " 0");
					return;
				}
			}
		}
	}
}