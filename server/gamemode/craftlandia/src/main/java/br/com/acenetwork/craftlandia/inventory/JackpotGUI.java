package br.com.acenetwork.craftlandia.inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import br.com.acenetwork.commons.CommonsUtil;
import br.com.acenetwork.commons.inventory.GUI;
import br.com.acenetwork.commons.player.CommonPlayer;
import br.com.acenetwork.craftlandia.Main;
import br.com.acenetwork.craftlandia.executor.Jackpot;
import net.md_5.bungee.api.ChatColor;

public class JackpotGUI extends GUI
{
	private ItemStack middleGlass;
	private ItemStack glass1;
	private ItemStack glass2;
	
	private final List<ItemStack> list;
	
	private int tickSpeed = 2;
	private long tick;
	private int pos;
	private int nextTick;
	private int times;
	private int targetTimes;
	
	private boolean invert;
	private int task;
	private int taskB;
	
	public JackpotGUI(CommonPlayer cp, List<ItemStack> list)
	{
		super(cp, () ->
		{
			return Bukkit.createInventory(cp.getPlayer(), 9 * 3, "              " + ChatColor.BLACK + ChatColor.BOLD + "JACKPOT");
		});
		
		this.list = new ArrayList<>(list);
		
		Player p = cp.getPlayer();
		
		Random r = new Random();
		
		ItemMeta meta;
		
		middleGlass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);
		meta = middleGlass.getItemMeta();
		meta.setDisplayName(" ");
		middleGlass.setItemMeta(meta);
		
		glass1 = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 3);
		meta = glass1.getItemMeta();
		meta.setDisplayName(" ");
		glass1.setItemMeta(meta);
		
		glass2 = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 5);
		meta = glass2.getItemMeta();
		meta.setDisplayName(" ");
		glass2.setItemMeta(meta);
		
		inv.setItem(4, middleGlass);
		inv.setItem(22, middleGlass);
		
		targetTimes = 50 + r.nextInt(50);
		
		task = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), () ->
		{
			if(!inv.equals(p.getOpenInventory().getTopInventory()))
			{
				p.openInventory(inv);
			}
			
			if(tick >= nextTick)
			{
//				if(tickSpeed >= 1)
				if(tickSpeed > 15)
				{
					int next = r.nextInt(6);
					
					if(next == 0)
					{
						refresh(true);
						Bukkit.getScheduler().cancelTask(task);
						return;
					}
				}
				
				refresh(false);
				pos = pos + 1 < this.list.size() ? pos + 1 : 0;
				
				if(times >= targetTimes)
				{
					times = 0;
					targetTimes /= 4;
					int random = r.nextInt(Math.max(targetTimes, 1));
					targetTimes += random;
					
					
					tickSpeed = tickSpeed == 1 ? 2 : tickSpeed + 2;
				}
				else
				{
					times++;
				}
				
				nextTick += tickSpeed;
			}
			
			tick++;
		}, 0L, 1L);
	}
	
	private void refresh(boolean finish)
	{
		Player p = cp.getPlayer();
		
		ItemStack glass1 = invert ? this.glass1.clone() : this.glass2.clone();
		ItemStack glass2 = invert ? this.glass2.clone() : this.glass1.clone();
		
		invert = !invert;
		
		for(int i = 0; i < inv.getSize(); i++)
		{
			if(i == 9)
			{
				i = 17;
				continue;
			}
			
			if(i == 4 || i == 22)
			{
				continue;
			}
			
			inv.setItem(i, i % 2 == 0 ? glass1 : glass2);
		}
		
		for(int i = 9; i < 9 + 9; i++)
		{
			inv.setItem(i, list.get(pos + i >= list.size() ? (pos + i) - list.size() : pos + i));
		}
		
		if(finish)
		{
			cp.setJackpoting(false);
			ItemStack item = inv.getItem(13);
			
			if(CommonsUtil.compareUUID(Jackpot.JACKPOT_ITEM, item))
			{
				int prize = Jackpot.getJackpot();
				
				if(prize <= 0)
				{
					p.playSound(p.getLocation(), Sound.NOTE_BASS_GUITAR, 5.0F, 1.0F);
					return;
				}
				
				Bukkit.broadcastMessage("prize = " + prize);
				Jackpot.setJackpot(0);
				Bukkit.broadcastMessage("Jackpot = " + Jackpot.getJackpot());
				
				taskB = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), new Runnable()
				{
					int times = 0;
					
					@Override
					public void run()
					{
						if(times >= 20)
						{
							Bukkit.getScheduler().cancelTask(taskB);
							taskB = 0;
							return;
						}
						
						p.playSound(p.getLocation(), Sound.NOTE_PLING, 1.0F, 1.5F);
						p.playSound(p.getLocation(), Sound.NOTE_PLING, 1.0F, 3.0F);
						times++;
					}
				}, 0L, 4L);
			}
			else if(CommonsUtil.compareUUID(Jackpot.NONE_ITEM, item))
			{
				p.playSound(p.getLocation(), Sound.NOTE_BASS_GUITAR, 5.0F, 1.0F);
			}
			else if(CommonsUtil.compareUUID(item, Jackpot.SHARDS_UUID))
			{
				p.playSound(p.getLocation(), Sound.NOTE_PLING, 1.0F, 1.5F);
				
				double multiplier;
				
				switch(item.getType())
				{
				case GOLD_NUGGET:
					multiplier = 0.1D;
					break;
				case GOLD_INGOT:
					multiplier = 1.0D;
					break;
				case GOLD_BLOCK:
					multiplier = 10.0D;
					break;
				default:
					return;
				}
				
				int prize = (int) (1000 * item.getAmount() * multiplier);
				
				Bukkit.broadcastMessage("prize = " + prize);
				
				Jackpot.setJackpot(Jackpot.getJackpot() - prize); 
				
				Bukkit.broadcastMessage("Jackpot = " + Jackpot.getJackpot());
			}
			else
			{
				p.playSound(p.getLocation(), Sound.NOTE_PLING, 1.0F, 1.5F);
			}
		}
		else
		{
			p.playSound(p.getLocation(), Sound.NOTE_PLING, 1.0F, 0.75F);
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
	}
}