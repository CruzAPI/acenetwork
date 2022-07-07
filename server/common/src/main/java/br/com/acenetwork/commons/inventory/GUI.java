package br.com.acenetwork.commons.inventory;

import java.util.ResourceBundle;
import java.util.function.Supplier;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import br.com.acenetwork.commons.player.CommonPlayer;

public abstract class GUI implements Listener
{
	protected final CommonPlayer cp;
	protected final Inventory inv;
		
	public GUI(CommonPlayer cp, Inventory inv)
	{
		this(cp, inv, true);
	}
	
	public GUI(CommonPlayer cp, Inventory inv, boolean openInventory)
	{
		this.cp = cp;
		this.inv = inv;
		
		if(openInventory)
		{
			cp.getPlayer().openInventory(inv);
		}
		
		cp.setGUI(this);
	}
	
	protected class Item extends ItemStack
	{
		public Item(ItemStack itemStack)
		{
			super(itemStack);
		}
		
		public Item(Supplier<ItemStack> supplier)
		{
			super(supplier.get());
		}
	}
	
	public GUI(CommonPlayer cp, String key, InventoryType type)
	{
		ResourceBundle bundle = ResourceBundle.getBundle("message", cp.getLocale());
		
		this.cp = cp;
		this.inv = Bukkit.createInventory(cp.getPlayer(), type, bundle.getString(key));
		
		cp.getPlayer().openInventory(inv);
		cp.setGUI(this);
	}
	
	public GUI(CommonPlayer cp, Supplier<Inventory> supplier)
	{
		this(cp, supplier, true);
	}
	
	public GUI(CommonPlayer cp, Supplier<Inventory> supplier, boolean openInventory)
	{
		this.cp = cp;
		this.inv = supplier.get();
		
		if(openInventory)
		{
			cp.getPlayer().openInventory(inv);
		}
		
		cp.setGUI(this);
	}
	
	public GUI(CommonPlayer cp, String key, int size)
	{
		ResourceBundle bundle = ResourceBundle.getBundle("message", cp.getLocale());
		
		this.cp = cp;
		this.inv = Bukkit.createInventory(cp.getPlayer(), size, bundle.getString(key));
		
		cp.getPlayer().openInventory(inv);
		cp.setGUI(this);
	}
	
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent e)
	{
		if(e.getPlayer() != cp.getPlayer())
		{
			return;
		}
		
		cp.setGUI(null);
	}
}