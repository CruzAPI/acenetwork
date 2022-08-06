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
import br.com.acenetwork.craftlandia.manager.InvalidCommandArgumentException;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

public class AmountSelector extends GUI
{
	private final String commandLine;
	private ItemStack item;
	private ItemStack confirm;
	
	@SuppressWarnings("deprecation")
	public AmountSelector(CommonPlayer cp, String commandLine) throws InvalidCommandArgumentException
	{
		super(cp, () ->
		{
			ResourceBundle bundle = ResourceBundle.getBundle("message", cp.getLocale());
			return Bukkit.createInventory(cp.getPlayer(), InventoryType.WORKBENCH, bundle.getString("inv.amount-selector"));
		});
		
		this.commandLine = commandLine;
		
		String[] args = commandLine.split(" ");
		
		ResourceBundle bundle = ResourceBundle.getBundle("message", cp.getLocale());
		
		Material type;
		
		try
		{
			try
			{
				type = Material.getMaterial(Integer.valueOf(args[1]));
			}
			catch(NumberFormatException e)
			{
				type = Material.valueOf(args[1].toUpperCase());
			}
		}
		catch(IllegalArgumentException e)
		{
			TextComponent text = new TextComponent(bundle.getString("commons.cmds.id-not-found"));
			text.setColor(ChatColor.RED);
			throw new InvalidCommandArgumentException(0, text);
		}
		
		short data;
		
		try
		{
			data = Short.valueOf(args[2]);
		}
		catch(NumberFormatException e)
		{
			TextComponent text = new TextComponent(bundle.getString("commons.cmds.invalid-number-format"));
			text.setColor(ChatColor.RED);
			throw new InvalidCommandArgumentException(1, text);
		}
		
		item = new ItemStack(type, 1, data);
		
		inv.setItem(5, item);
		inv.setItem(0, item);
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
		
		Bukkit.broadcastMessage(e.getRawSlot() + "");
		
		if(e.getRawSlot() == 0)
		{
			p.closeInventory();
			Bukkit.dispatchCommand(p, commandLine + " " + current.getAmount());
		}
		else if(item.isSimilar(current))
		{
			final int maxStackSize = current.getType().getMaxStackSize();
			int amount = current.getAmount();
			
			if(click == ClickType.LEFT)
			{
				amount = amount >= maxStackSize ? 1 : amount + 1;
			}
			else if(click == ClickType.SHIFT_LEFT)
			{
				amount = maxStackSize;
			}
			else if(click == ClickType.RIGHT)
			{
				amount = amount <= 1 ? maxStackSize : amount - 1;
			}
			else if(click == ClickType.SHIFT_RIGHT)
			{
				amount = 1;
			}
			
			current.setAmount(amount);
			inv.setItem(0, current);
		}
	}
}