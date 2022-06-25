package br.com.acenetwork.craftlandia.inventory;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitTask;

import br.com.acenetwork.commons.Common;
import br.com.acenetwork.commons.CommonsUtil;
import br.com.acenetwork.commons.inventory.GUI;
import br.com.acenetwork.commons.player.CommonPlayer;
import br.com.acenetwork.craftlandia.Main;
import br.com.acenetwork.craftlandia.executor.Jackpot;
import net.md_5.bungee.api.ChatColor;

public class JackpotPercentage extends GUI
{
	public JackpotPercentage(CommonPlayer cp)
	{
		super(cp, () ->
		{
			return Bukkit.createInventory(cp.getPlayer(), 9 * 3, "              JACKPOT %");
		});
		
		int size = Jackpot.PRIZE_LIST.size();
		
		for(Entry<ItemStack, Integer> entry : Jackpot.MAP.entrySet())
		{
			ItemStack key = entry.getKey().clone();
			int value = entry.getValue();
			
			ItemMeta meta = key.getItemMeta();
			
			DecimalFormat df = new DecimalFormat("#.####");
			
			meta.setDisplayName("" + ChatColor.GREEN + value + "/" + size + " (" + df.format(((double) value / size) * 100.0D) + "%)");
			key.setItemMeta(meta);
			inv.addItem(key);
		}
	}
	
	@EventHandler
	public void asdasdfas(InventoryClickEvent e)
	{
		Player p = cp.getPlayer();
		
		if(e.getWhoClicked() != p)
		{
			return;
		}
		
		e.setCancelled(true);
	}
}