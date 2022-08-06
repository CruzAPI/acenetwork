package br.com.acenetwork.survival.inventory;

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

import br.com.acenetwork.commons.CommonsUtil;
import br.com.acenetwork.commons.inventory.GUI;
import br.com.acenetwork.commons.player.CommonPlayer;
import br.com.acenetwork.survival.executor.Jackpot;
import br.com.acenetwork.survival.manager.JackpotType;
import net.md_5.bungee.api.ChatColor;

public class JackpotPercentage extends GUI
{
	private ItemStack next;
	private ItemStack previous;
	
	private final JackpotType type;
	
	public JackpotPercentage(CommonPlayer cp, JackpotType type)
	{
		super(cp, () ->
		{
			return Bukkit.createInventory(cp.getPlayer(), 9 * 5, "      " + ChatColor.BOLD + "  " + type.getColor() + ChatColor.BOLD + "   JACKPOT %");
		});
		
		this.type = type;
		
		int size = 0;
		
		ResourceBundle bundle = ResourceBundle.getBundle("message", cp.getLocale());
		
		Map<ItemStack, Integer> map = type.getMapSupplier().get(bundle, cp.getVersion());
		
		for(int i : map.values())
		{
			size += i;
		}
		
		int i = 0;
		double totalAvg = 0;
		
		DecimalFormat df = new DecimalFormat("#.###");
		df.setGroupingSize(3);
		df.setGroupingUsed(true);
		
		boolean analytics = cp.hasPermission("jackpot.analytics");
		
		for(Entry<ItemStack, Integer> entry : map.entrySet())
		{
			ItemStack key = entry.getKey();
			int value = entry.getValue();
			
			double shards = Jackpot.getValueInShardsTheoretically(type.getBet(), key);
			
			ItemMeta meta = key.getItemMeta();
			
			double div = ((double) value / size);
			double avg = div * shards;
			totalAvg += avg;
			
			List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<>();
			
			lore.add("" + ChatColor.GRAY + value + "/" + size + " (" + df.format(div * 100.0D) + "%)");
			
			if(analytics)
			{
				lore.add("(" + df.format(avg) + " shards)");
			}
			
			meta.setLore(lore);
			
			key.setItemMeta(meta);
			inv.setItem(i++, key);
		}
		
		ItemStack info = new ItemStack(Material.PAPER);
		ItemMeta meta = info.getItemMeta();
		meta.setDisplayName(ChatColor.WHITE + "Total AVG without jackpot = " + df.format(totalAvg));
		
		int value = map.entrySet().stream().filter(x -> CommonsUtil.containsUUID(x.getKey(), Jackpot.JACKPOT_UUID) 
				|| CommonsUtil.containsUUID(x.getKey(), Jackpot.NONE_UUID))
				.findFirst().get().getValue().intValue();
		
		double avgJackpot = (type.getBet() - totalAvg) * ((double) size / value);
		
		double div = ((double) value / size);
		double avg = div * avgJackpot * Jackpot.PERCENT;
		
		meta.setLore(Arrays.asList(
				ChatColor.GRAY + "Total AVG w/jackpot " + df.format(Jackpot.PERCENT * 100.0D) + "% = " + df.format(totalAvg + avg),
				ChatColor.GRAY + "100% jackpot prize ≃ " + df.format(avgJackpot),
				ChatColor.GRAY + df.format(Jackpot.PERCENT * 100.0D) + "% jackpot prize ≃ " + df.format(avgJackpot * Jackpot.PERCENT)));
		info.setItemMeta(meta);
		
		if(analytics)
		{
			inv.setItem(inv.getSize() - 2, info);
		}
		
		JackpotType nextType = type.getNext();
		JackpotType previousType = type.getPrevious();
		
		if(nextType != null)
		{
			next = new ItemStack(nextType.getMaterial());
			meta = next.getItemMeta();
			meta.setDisplayName("" + nextType.getColor() + ChatColor.BOLD + nextType.name() + " %");
			next.setItemMeta(meta);
			inv.setItem(inv.getSize() - 1, next);
		}
		
		if(previousType != null)
		{
			previous = new ItemStack(previousType.getMaterial());
			meta = previous.getItemMeta();
			meta.setDisplayName("" + previousType.getColor() + ChatColor.BOLD + previousType.name() + " %");
			previous.setItemMeta(meta);
			inv.setItem(inv.getSize() - 9, previous);
		}
	}
	
	@EventHandler
	public void asdasdfas(InventoryClickEvent e)
	{
		if(e.getWhoClicked() != p)
		{
			return;
		}
		
		ItemStack current = e.getCurrentItem();
		
		if(previous != null && previous.equals(current))
		{
			Bukkit.dispatchCommand(p, "jackpot " + type.getPrevious().name());
		}
		else if(next != null && next.equals(current))
		{
			Bukkit.dispatchCommand(p, "jackpot " + type.getNext().name());
		}
		
		e.setCancelled(true);
	}
}