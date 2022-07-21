package br.com.acenetwork.craftlandia.inventory;

import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import br.com.acenetwork.commons.inventory.AnvilCommand;
import br.com.acenetwork.commons.player.CommonPlayer;
import br.com.acenetwork.craftlandia.Rarity;

public class RenameItem extends AnvilCommand
{
	private final Rarity rarity;
	
	public RenameItem(CommonPlayer cp, Rarity rarity, ItemStack[] contents)
	{
		super(cp, null, null);
		this.rarity = rarity;
		
		for(int i = 0; i < contents.length && i < inv.getSize(); i++)
		{
			inv.setItem(i, contents[i]);
		}
		
		p.updateInventory();
	}
	
	@Override
	@EventHandler
	public void clearInventoryOnClose(InventoryCloseEvent e)
	{
		
	}
	
	@EventHandler
	public void asdasd(InventoryClickEvent e)
	{
		if(e.getWhoClicked() != p)
		{
			return;
		}
		
		if(e.getRawSlot() == 0)
		{
			ItemStack[] clonedContents = getClonedContents();
			inv.clear();
			new CustomAnvil(cp, rarity, null, clonedContents);
		}
	}
	
	private ItemStack[] getClonedContents()
	{
		ItemStack[] contents = getContents();
		ItemStack[] clonedContents = new ItemStack[contents.length];
		
		for(int i = 0; i < contents.length; i++)
		{
			clonedContents[i] = contents[i] == null ? null : contents[i].clone();
		}
		
		return clonedContents;
	}
	
	@Override
	public void run(String displayName)
	{
		ItemStack[] clonedContents = getClonedContents();
		inv.clear();
		new CustomAnvil(cp, rarity, displayName, clonedContents);
	}
	
	public ItemStack[] getContents()
	{
		ItemStack[] contents = new ItemStack[2];
		
		contents[0] = inv.getItem(0);
		contents[1] = inv.getItem(1);
		
		return contents;
	}
}