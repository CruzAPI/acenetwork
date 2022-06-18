package br.com.acenetwork.craftlandia.inventory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import br.com.acenetwork.commons.CommonsUtil;
import br.com.acenetwork.commons.inventory.GUI;
import br.com.acenetwork.commons.player.CommonPlayer;
import br.com.acenetwork.craftlandia.executor.ItemInfo;
import net.md_5.bungee.api.ChatColor;

public class EnchantSelector extends GUI
{
	private ItemStack enchantedItem;
	
	private final String commandLine;
	private final List<Enchantment> enchantmentList = new ArrayList<>();
	
	public EnchantSelector(CommonPlayer cp, String commandLine)
	{
		super(cp, () ->
		{
			ResourceBundle bundle = ResourceBundle.getBundle("message", cp.getLocale());
			return Bukkit.createInventory(cp.getPlayer(), 9 * 6, bundle.getString("inv.enchant-selector"));
		});
		
		this.commandLine = commandLine;
		
		String[] args = commandLine.split(" ");
		
		int id = Integer.valueOf(args[1]);
		short data = Short.valueOf(args[2]);
		
		enchantedItem = new ItemStack(id, 1, data);
		
		inv.setItem(4, enchantedItem);
		
		Enchantment[] enchantments = Enchantment.values();
		
		ResourceBundle minecraftBundle = ResourceBundle.getBundle("minecraft", cp.getLocale());
		
		for(int i = 0; i < enchantments.length; i++)
		{
			Enchantment ench = enchantments[i];
			
			if(!ench.canEnchantItem(enchantedItem))
			{
				continue;
			}
			
			int index = enchantmentList.size();
			
			enchantmentList.add(ench);
			
			ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
			ItemMeta meta = book.getItemMeta();
			meta.setDisplayName(ChatColor.AQUA + minecraftBundle.getString(CommonsUtil.getTranslation(ench)));
			book.setItemMeta(meta);
			
			inv.setItem(18 + index, book);
		}
		
		if(enchantmentList.isEmpty())
		{
			confirm();
		}
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
		
		if(enchantedItem.equals(current))
		{
			confirm();
			return;
		}
		
		int rawSlot = e.getRawSlot();
		int i = rawSlot - 18;
		
		if(i < 0 || i > enchantmentList.size())
		{
			return;
		}
		
		Enchantment enchantment = enchantmentList.get(i);
		int level = current.getItemMeta().hasEnchant(enchantment) ? current.getItemMeta().getEnchantLevel(enchantment) : 0;
		int maxLevel = enchantment.getMaxLevel();
		
		for(Enchantment enchants : enchantedItem.getItemMeta().getEnchants().keySet())
		{
			if(!enchants.equals(enchantment) && enchants.conflictsWith(enchantment))
			{
				p.playSound(p.getLocation(), Sound.NOTE_BASS, 1.0F, 1.0F);
				return;
			}
		}
		
		if(click == ClickType.LEFT)
		{
			level = level >= maxLevel ? 0 : level + 1;
		}
		else if(click == ClickType.SHIFT_LEFT)
		{
			level = maxLevel;
		}
		else if(click == ClickType.RIGHT)
		{
			level = level <= 0 ? maxLevel : level - 1;
		}
		else if(click == ClickType.SHIFT_RIGHT)
		{
			level = 0;
		}
		
		ItemMeta meta;
		
		if(level == 0)
		{
			meta = enchantedItem.getItemMeta();
			meta.removeEnchant(enchantment);
			enchantedItem.setItemMeta(meta);
			
			meta = current.getItemMeta();
			meta.removeEnchant(enchantment);
			current.setItemMeta(meta);
		}
		else
		{
			meta = enchantedItem.getItemMeta();
			meta.addEnchant(enchantment, level, true);
			enchantedItem.setItemMeta(meta);
			
			meta = current.getItemMeta();
			meta.addEnchant(enchantment, level, true);
			current.setItemMeta(meta);
		}
		
		inv.setItem(4, enchantedItem);
	}

	private void confirm()
	{
		Player p = cp.getPlayer();
		
		ResourceBundle bundle = ResourceBundle.getBundle("message", cp.getLocale());
		
		p.closeInventory();
		
		try
		{
			int data = ItemInfo.getItemInfoData(enchantedItem);
			Bukkit.dispatchCommand(p, commandLine + " " + data);
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
			p.sendMessage(ChatColor.RED + bundle.getString("commons.unexpected-error"));
		}
	}
}