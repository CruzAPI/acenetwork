package br.com.acenetwork.craftlandia.inventory;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.comphenix.protocol.ProtocolLibrary;

import br.com.acenetwork.commons.inventory.GUI;
import br.com.acenetwork.commons.player.CommonPlayer;
import br.com.acenetwork.craftlandia.executor.Jackpot;
import net.md_5.bungee.api.ChatColor;

public class JackpotPercentage extends GUI
{
	public JackpotPercentage(CommonPlayer cp, Map<Byte, Integer> map)
	{
		super(cp, () ->
		{
			return Bukkit.createInventory(cp.getPlayer(), 9 * 3, "              JACKPOT %");
		});
		
		int size = 0;
		
		for(int i : map.values())
		{
			size += i;
		}
		
		int i = 0;
		double totalAvg = 0;
		
		DecimalFormat df = new DecimalFormat("#.###");
		
		Player p = cp.getPlayer();
		ResourceBundle bundle = ResourceBundle.getBundle("message", cp.getLocale());
		double bet = 1000.0D;
		int version = ProtocolLibrary.getProtocolManager().getProtocolVersion(p);
		
		for(Entry<Byte, Integer> entry : map.entrySet())
		{
			ItemStack key = Jackpot.Item.getById(entry.getKey()).getItemSupplier().get(bundle, bet, version).clone();
			int value = entry.getValue();
			
			double shards = Jackpot.getValueInShardsTheoretically(bet, key);
			
			ItemMeta meta = key.getItemMeta();
			
			double div = ((double) value / size);
			double avg = div * shards;
			totalAvg += avg;
			
			List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<>();
			
			lore.add("" + ChatColor.GRAY + value + "/" + size + " (" + df.format(div * 100.0D) + "%)");
			lore.add("(" + df.format(avg) + " shards)");
			
			meta.setLore(lore);
			
			key.setItemMeta(meta);
			inv.setItem(i++, key);
		}
		
		ItemStack info = new ItemStack(Material.PAPER);
		ItemMeta meta = info.getItemMeta();
		meta.setDisplayName(ChatColor.WHITE + "Total AVG without jackpot = " + df.format(totalAvg));
		
		double avgJackpot = (bet - totalAvg) * size;
		
		double div = ((double) 1 / size);
		double avg = div * avgJackpot * 1.0D;
		
		meta.setLore(Arrays.asList(
				ChatColor.GRAY + "Total AVG w/jackpot 100% = " + df.format(totalAvg + avg),
				ChatColor.GRAY + "AVG 100% jackpot = " + df.format(avgJackpot)));
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