package br.com.acenetwork.craftlandia.inventory;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
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
		int i = 0;
		double totalAvg = 0;
		
		DecimalFormat df = new DecimalFormat("#.###");
		
		int bet = 1000;
		
		for(Entry<ItemStack, Integer> entry : Jackpot.MAP.entrySet())
		{
			ItemStack key = entry.getKey().clone();
			int value = entry.getValue();
			
			double shards = Jackpot.getValueInShardsTheoretically(bet, key);
			
			ItemMeta meta = key.getItemMeta();
			
			double div = ((double) value / size);
			double avg = div * shards;
			totalAvg += avg;
			
			meta.setDisplayName("" + ChatColor.GREEN + value + "/" + size + " (" + df.format(div * 100.0D) + "%) "
					+ ChatColor.GRAY + "(" + df.format(avg) + " shards)");
			key.setItemMeta(meta);
			inv.setItem(i++, key);
		}
		
		ItemStack info = new ItemStack(Material.PAPER);
		ItemMeta meta = info.getItemMeta();
		meta.setDisplayName(ChatColor.WHITE + "Total Average = " + df.format(totalAvg));
		
		double avgJackpot = (bet - totalAvg) * (30000.0D);

		meta.setLore(Arrays.asList(ChatColor.GRAY + "Average jackpot prize = " + df.format(avgJackpot)));
		info.setItemMeta(meta);
		
		inv.setItem(inv.getSize() - 1, info);
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