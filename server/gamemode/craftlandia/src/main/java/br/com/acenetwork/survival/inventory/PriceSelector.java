package br.com.acenetwork.survival.inventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import br.com.acenetwork.commons.CommonsUtil;
import br.com.acenetwork.commons.inventory.GUI;
import br.com.acenetwork.commons.manager.Message;
import br.com.acenetwork.commons.player.CommonPlayer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

public class PriceSelector extends GUI
{
	private final String command;
	private final String currency;
	
	private ItemStack itemCancel;
	private ItemStack itemConfirm;
	
	public PriceSelector(CommonPlayer cp, String command)
	{
		this(cp, "inv.price-selector", command, ChatColor.YELLOW + "shards");
	}
	
	public PriceSelector(CommonPlayer cp, String key, String command, String currency, TextComponent... extra)
	{
		super(cp, () ->
		{
			ResourceBundle bundle = ResourceBundle.getBundle("message", cp.getLocale());
			TextComponent text = Message.getTextComponent(bundle.getString(key), extra);
			text.setColor(ChatColor.RESET);
			return Bukkit.createInventory(cp.getPlayer(), 9, text.toLegacyText());
		});
		
		this.command = command;
		this.currency = currency;
		
		ResourceBundle bundle = ResourceBundle.getBundle("message", cp.getLocale());
		
		ItemMeta meta;
		
		itemCancel = new ItemStack(Material.STAINED_CLAY, 1, (short) 14);
		meta = itemCancel.getItemMeta();
		meta.setDisplayName("" + ChatColor.RED + ChatColor.BOLD + bundle.getString("verb.cancel").toUpperCase());
		itemCancel.setItemMeta(meta);
		
		for(int i = 1; i < 8; i++)
		{
			ItemStack item = new ItemStack(Material.INK_SACK, 1, (short) 8);
			meta = item.getItemMeta();
			meta.setDisplayName("" + ChatColor.GRAY + 0);
			item.setItemMeta(meta);
			inv.setItem(i, item);
		}
		
		itemConfirm = new ItemStack(Material.STAINED_CLAY, 1, (short) 5);
		meta = itemConfirm.getItemMeta();
		meta.setDisplayName("" + ChatColor.GREEN + ChatColor.BOLD + bundle.getString("verb.confirm").toUpperCase());
		itemConfirm.setItemMeta(meta);
		
		inv.setItem(0, itemCancel);
		
		refresh(bundle);
	}
	
	public int getPrice()
	{
		int price = 0;
		
		for(int i = 1; i < 8; i++)
		{
			ItemStack item = inv.getItem(i);
			price += item.getDurability() == 8 ? 0 : Math.min(9, item.getAmount()) * Integer.valueOf("1" + StringUtils.repeat('0', 7 - i));
		}
		
		return price;
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
		
		if(CommonsUtil.compareDisplayName(current, itemCancel) && click == ClickType.LEFT)
		{
			p.closeInventory();
			
			String backCommand = "";
			String[] split = command.split(" ");
			
			for(int i = 0; i < split.length; i++)
			{
				backCommand += i + 1 < split.length ? split[i] + " " : "";
			}
			
			Bukkit.dispatchCommand(p, backCommand);
		}
		else if(CommonsUtil.compareDisplayName(current, itemConfirm) && click == ClickType.LEFT)
		{
			p.closeInventory();
			Bukkit.dispatchCommand(p, command + " " + getPrice());
		}
		else if(current != null && current.getType() == Material.INK_SACK)
		{
			int value = current.getDurability() == 8 ? 0 : current.getAmount();
			
			if(click == ClickType.SHIFT_LEFT)
			{
				value = 9;
			}
			else if(click == ClickType.SHIFT_RIGHT)
			{
				value = 0;
			}
			else if(click == ClickType.LEFT)
			{
				value = value >= 9 ? 0 : value + 1;
			}
			else if(click == ClickType.RIGHT)
			{
				value = value <= 0 ? 9 : value - 1;
			}
			
			current.setDurability(value == 0 ? (short) 8 : (short) 10);
			current.setAmount(value == 0 ? 1 : value);
			
			ItemMeta meta = current.getItemMeta();
			meta.setDisplayName("" + (value == 0 ? ChatColor.GRAY : ChatColor.GREEN) + value);
			current.setItemMeta(meta);
			
			ResourceBundle bundle = ResourceBundle.getBundle("message", cp.getLocale());

			refresh(bundle);
		}
	}
	
	private void refresh(ResourceBundle bundle)
	{
		int price = getPrice();
		
		ItemMeta meta;
		
		for(int i = 1; i < 8; i++)
		{
			ItemStack item = inv.getItem(i);
			meta = item.getItemMeta();
			meta.setDisplayName("" + (item.getDurability() == 10 ? ChatColor.GREEN : ChatColor.GRAY ) + getPrice());
			item.setItemMeta(meta);
//			inv.setItem(i, item);
		}
		
		meta = itemConfirm.getItemMeta();
		
		if(price == 0)
		{
			meta.setLore(new ArrayList<>());
		}
		else
		{
			meta.setLore(Arrays.asList
			(
					"" + ChatColor.WHITE + price + " " + currency 
			));
		}
		
		itemConfirm.setItemMeta(meta);
		inv.setItem(8, itemConfirm);
	}
}