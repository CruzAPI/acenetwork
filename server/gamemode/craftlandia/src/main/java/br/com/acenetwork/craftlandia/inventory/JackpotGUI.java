package br.com.acenetwork.craftlandia.inventory;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.comphenix.protocol.ProtocolLibrary;

import br.com.acenetwork.commons.CommonsUtil;
import br.com.acenetwork.commons.inventory.GUI;
import br.com.acenetwork.commons.inventory.VipChestGUI;
import br.com.acenetwork.commons.manager.Message;
import br.com.acenetwork.commons.player.CommonPlayer;
import br.com.acenetwork.commons.player.craft.CraftCommonPlayer;
import br.com.acenetwork.craftlandia.Main;
import br.com.acenetwork.craftlandia.executor.Jackpot;
import br.com.acenetwork.craftlandia.listener.RandomItem;
import br.com.acenetwork.craftlandia.manager.JackpotItem;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

public class JackpotGUI extends GUI
{
	private ItemStack middleGlass;
	private ItemStack glass1;
	private ItemStack glass2;
	
	private final Random r;
	private final byte[] array;
	
	private int tickSpeed = 2;
	private long tick;
	private int pos;
	private int nextTick;
	private int times;
	private int targetTimes;
	
	private boolean invert;
	private int task;
	private int taskB;
	private final ResourceBundle bundle;
	
	private final int version;
	private final Map<Byte, ItemStack> items = new HashMap<>();
	private boolean finished;
	private final Jackpot jackpot;
	
	public JackpotGUI(CommonPlayer cp, Map<Byte, Integer> map)
	{
		super(cp, () ->
		{
			return Bukkit.createInventory(cp.getPlayer(), 9 * 3, "              " + ChatColor.BLACK + ChatColor.BOLD + "JACKPOT");
		});
		
		this.jackpot = Jackpot.getInstance();
		this.bundle = ResourceBundle.getBundle("message", cp.getLocale());
		
		r = new Random();
		int size = 0;
		
		for(int i : map.values())
		{
			size += i;
		}
		
		array = new byte[size];
		
		int j = 0;
		
		for(Entry<Byte, Integer> entry : map.entrySet())
		{
			for(int i = 0; i < entry.getValue() && j < array.length; i++, j++)
			{
				array[j] = entry.getKey();
			}
		}
		
		double bet = 1000.0D;
		
		Player p = cp.getPlayer();
		version = ProtocolLibrary.getProtocolManager().getProtocolVersion(p);

		for(JackpotItem item : JackpotItem.values())
		{
			items.put(item.getId(), item.getItemSupplier().get(bundle, bet, version));
		}
		
		CommonsUtil.shuffle(array, r);
		
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
			if(!inv.equals(p.getOpenInventory().getTopInventory()) && !finished)
			{
				p.openInventory(inv);
			}
			
			if(tick >= nextTick)
			{
//				if(tickSpeed >= 1) //skip animation
				if(tickSpeed > 15)
				{
					int next = r.nextInt(Math.max(1, 90 / tickSpeed));
					
					if(next == 0)
					{
						Bukkit.getScheduler().cancelTask(task);
						finished = true;
						jackpot.setInUse(false);
						refresh(true);
						return;
					}
				}
				
				refresh(false);
				pos = pos + 1 < array.length ? pos + 1 : 0;
				
				if(times >= targetTimes)
				{
					times = 0;
					targetTimes /= 4;
					int random = r.nextInt(Math.max(targetTimes, 1));
					targetTimes += random;
					
					
					tickSpeed = tickSpeed == 1 ? 2 : tickSpeed + 2 + 2 * tickSpeed / 20;
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
			byte b = array[(pos + i >= array.length ? (pos + i) - array.length : pos + i)];
			inv.setItem(i, getItemStack(b));
		}
		
		double bet = 1000.0D;
		
		if(finish)
		{
			cp.setJackpoting(false);
			ItemStack item = inv.getItem(13);
			
			Jackpot jackpot = Jackpot.getInstance();
			double jackpotPrize = jackpot.getJackpotPrize();
			
			TextComponent tag = new TextComponent("[" + bundle.getString("noun.jackpot").toUpperCase() + "]");
			tag.setColor(ChatColor.AQUA);
			
			if(CommonsUtil.containsUUID(item, Jackpot.JACKPOT_UUID) && jackpotPrize > 0.0D)
			{
				cp.setBalance(cp.getBalance() + jackpotPrize);
				jackpot.setJackpotTotal(0.0D);
				
				Set<CommandSender> senderList = new HashSet<>(Bukkit.getOnlinePlayers());
				senderList.add(Bukkit.getConsoleSender());
				
				for(CommandSender sender : senderList)
				{
					ResourceBundle bundle;
					
					if(sender instanceof Player)
					{
						Player all = (Player) sender;
						CommonPlayer cpall = CraftCommonPlayer.get(all);
						bundle = ResourceBundle.getBundle("message", cpall.getLocale());
						
						all.playSound(all.getLocation(), Sound.AMBIENCE_THUNDER, 1.0F, 2.5F);
					}
					else
					{
						bundle = ResourceBundle.getBundle("message");
					}
					
					DecimalFormat df = new DecimalFormat("#.##", new DecimalFormatSymbols(bundle.getLocale()));
					df.setGroupingSize(3);
					df.setGroupingUsed(true);
					
					TextComponent[] extra = new TextComponent[3];
					
					tag = new TextComponent("[" + bundle.getString("noun.jackpot").toUpperCase() + "]");
					tag.setColor(ChatColor.AQUA);
					
					extra[0] = tag;
					
					extra[1] = new TextComponent(p.getDisplayName());
					
					extra[2] = new TextComponent(bundle.getString("noun.jackpot").toUpperCase());
					extra[2].setColor(ChatColor.AQUA);
					extra[2].setBold(true);
					
					TextComponent text1 = Message.getTextComponent(bundle.getString("cmd.jackpot.won-jackpot"), extra);
					text1.setColor(ChatColor.GREEN);
					
					
					TextComponent text2 = new TextComponent("");
					text2.setColor(ChatColor.GRAY);
					
					text2.addExtra(tag);
					text2.addExtra(" " + StringUtils.capitalize(bundle.getString("noun.prize")) + ": ");
					text2.addExtra("" + ChatColor.WHITE + ChatColor.BOLD + df.format(jackpotPrize) + " "
							+ bundle.getString("noun.shards").toUpperCase());
					
					sender.sendMessage("");
					sender.sendMessage(text1.toLegacyText());
					sender.sendMessage(text2.toLegacyText());
					sender.sendMessage("");
				}
				
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
						
						p.getWorld().playSound(p.getLocation(), Sound.NOTE_PLING, 1.0F, 1.5F);
						p.getWorld().playSound(p.getLocation(), Sound.NOTE_PLING, 1.0F, 3.0F);
						times++;
					}
				}, 0L, 4L);
				
				return;
			}
			else if(CommonsUtil.containsUUID(item, Jackpot.$BTA_UUID))
			{
				double bta = bet * 0.001D * item.getAmount();
				
				jackpot.setJackpotTotal(jackpot.getJackpotTotal() - Jackpot.$BTA_TO_SHARDS * bta);
				cp.setBTA(cp.getBTA() + bta);
			}
			else if(CommonsUtil.containsUUID(item, Jackpot.VIP_UUID))
			{
				jackpot.setJackpotTotal(jackpot.getJackpotTotal() - 10.000D);
				p.getInventory().addItem(VipChestGUI.getVipItem()).values()
						.forEach(x -> p.getWorld().dropItemNaturally(p.getLocation(), x));
			}
			else if(CommonsUtil.containsUUID(item, Jackpot.SHARDS_UUID))
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
				
				double prize = bet * item.getAmount() * multiplier;
				
				jackpot.setJackpotTotal(jackpot.getJackpotTotal() - prize); 
				cp.setBalance(cp.getBalance() + prize);
			}
			else if(CommonsUtil.containsUUID(item, Jackpot.RANDOM_ITEM_UUID))
			{
				p.getInventory().addItem(RandomItem.getInstance().getItemSupplier().get(bundle, version)).values()
						.forEach(x -> p.getWorld().dropItemNaturally(p.getLocation(), x));
			}
			else
			{
				p.sendMessage(tag.toLegacyText() + " " + ChatColor.RED + bundle.getString("cmd.jackpot.none"));
				p.playSound(p.getLocation(), Sound.NOTE_BASS_GUITAR, 5.0F, 1.0F);
				return;
			}
			
			TextComponent[] extra = new TextComponent[1];
			
			extra[0] = new TextComponent(item.getItemMeta().getDisplayName());
			
			TextComponent text = Message.getTextComponent(bundle.getString("cmd.jackpot.won"), extra);
			text.setColor(ChatColor.GREEN);
			
			p.sendMessage(tag.toLegacyText() + " " + text.toLegacyText());
			p.playSound(p.getLocation(), Sound.NOTE_PLING, 1.0F, 1.5F);
		}
		else
		{
			p.playSound(p.getLocation(), Sound.NOTE_PLING, 1.0F, 0.75F);
		}
	}
	
	private ItemStack getItemStack(byte b)
	{
		ItemStack item = items.get(b);
		
		if(item != null)
		{
			if(JackpotItem.JACKPOT.getId() == b)
			{
				return JackpotItem.JACKPOT.getItemSupplier().get(bundle);
			}
			
			return item;
		}
		
		return null;
	}
	
	@EventHandler
	@Override
	public void onInventoryClose(InventoryCloseEvent e)
	{
		Player p = cp.getPlayer();
		
		if(e.getPlayer() != p)
		{
			return;
		}
		
		if(finished)
		{
			cp.setGUI(null);
			return;
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