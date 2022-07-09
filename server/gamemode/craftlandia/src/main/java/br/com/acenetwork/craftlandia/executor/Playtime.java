package br.com.acenetwork.craftlandia.executor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.function.Supplier;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.ContainerBlock;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.google.common.io.ByteStreams;

import br.com.acenetwork.commons.CommonsUtil;
import br.com.acenetwork.commons.event.PlayerInvincibilityChangeEvent;
import br.com.acenetwork.commons.event.PlayerModeChangeEvent;
import br.com.acenetwork.commons.manager.Message;
import br.com.acenetwork.commons.player.CommonPlayer;
import br.com.acenetwork.commons.player.craft.CraftCommonPlayer;
import br.com.acenetwork.craftlandia.Main;
import br.com.acenetwork.craftlandia.manager.Config;
import br.com.acenetwork.craftlandia.manager.Config.Type;
import br.com.acenetwork.craftlandia.manager.WBTA;
import br.com.acenetwork.craftlandia.player.SurvivalPlayer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

public class Playtime implements TabExecutor, Listener
{
	public static final Map<UUID, Time> MAP = new HashMap<>();
	private static final String WBTA_DISPLAY_NAME = ChatColor.DARK_GREEN + "Wrapped $BTA";
	private final UUID wbtaUUID;
	
	private static final long BTA_PER_SECONDS = 2L * 60L * 60L * 20L;
	private static final long TICKS = 60L * 20L;
//	private static final long TICKS = 7L * 24L * 60L * 60L * 20L;
	
	public final Supplier<ItemStack> wbtaSupplier;
	public final ItemStack burnedWBTA;
	
	private final Map<UUID, WBTA> itemMap;
	
	public static Playtime instance;
	private static final int TOTAL_TILES = Math.max(1, (int) (TICKS / (24L * 60L * 60L * 20L)));
	
	public Playtime()
	{
		instance = this;
		File file = Config.getFile(Type.WRAPPED_BTA_UUID, true);
		
		if(file.length() == 16L)
		{
			try(FileInputStream fileIn = new FileInputStream(file);
					ByteArrayInputStream streamIn = new ByteArrayInputStream(ByteStreams.toByteArray(fileIn));
					DataInputStream in = new DataInputStream(streamIn))
			{
				wbtaUUID = new UUID(in.readLong(), in.readLong());
			}
			catch(IOException ex)
			{
				throw new RuntimeException(ex);
			}
		}
		else
		{
			wbtaUUID = UUID.randomUUID();
			
			try(FileOutputStream fileOut = new FileOutputStream(file);
					ByteArrayOutputStream streamOut = new ByteArrayOutputStream();
					DataOutputStream out = new DataOutputStream(streamOut))
			{
				out.writeLong(wbtaUUID.getMostSignificantBits());
				out.writeLong(wbtaUUID.getLeastSignificantBits());
				fileOut.write(streamOut.toByteArray());
			}
			catch(IOException ex)
			{
				throw new RuntimeException(ex);
			}
		}
		
		
		file = Config.getFile(Type.WRAPPED_BTA_MAP, false);
		
		if(file.exists() && file.length() > 0L)
		{
			try(FileInputStream fileIn = new FileInputStream(file);
					ByteArrayInputStream streamIn = new ByteArrayInputStream(ByteStreams.toByteArray(fileIn));
					ObjectInputStream in = new ObjectInputStream(streamIn))
			{
				itemMap = (Map<UUID, WBTA>) in.readObject();
				
				Iterator<Entry<UUID, WBTA>> iterator = itemMap.entrySet().iterator();
				
				while(iterator.hasNext())
				{
					Entry<UUID, WBTA> entry = iterator.next();
					
					if(entry.getValue().getTimeRemaining() < 0L)
					{
						iterator.remove();
					}
				}
			}
			catch(ClassNotFoundException | IOException ex)
			{
				throw new RuntimeException(ex);
			}
		}
		else
		{
			itemMap = new HashMap<>();
		}
		
		ItemMeta meta;
		
		burnedWBTA = new ItemStack(Material.COAL, 1);
		meta = burnedWBTA.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "Burned $BTA");
		meta.addEnchant(Enchantment.DURABILITY, 1, true);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		burnedWBTA.setItemMeta(meta);
		
		wbtaSupplier = () ->
		{
			ItemMeta meta1;
			
			ItemStack wbta = new ItemStack(Material.EMERALD);
			meta1 = wbta.getItemMeta();
			meta1.setDisplayName(CommonsUtil.hideUUID(wbtaUUID) + ChatColor.RESET + CommonsUtil.getRandomItemUUID() + WBTA_DISPLAY_NAME + CommonsUtil.hideNumberData(TICKS));
			meta1.addEnchant(Enchantment.DURABILITY, 1, true);
			meta1.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			wbta.setItemMeta(meta1);
			
			return wbta;
		};
		
		
		
		long delay = 20L;
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), () ->
		{
			for(Entry<UUID, WBTA> entry : itemMap.entrySet())
			{
				WBTA wbta = entry.getValue();
				
				if(wbta.getTimeRemaining() < 0L)
				{
					continue;
				}
				
				InventoryHolder invHolder = wbta.getInvHolder();
				
				if(invHolder instanceof Player)
				{
					wbta.setTimeRemaining(Math.max(0L, wbta.getTimeRemaining() - delay * 2L));
				}
				else if(invHolder != null)
				{
					wbta.setTimeRemaining(Math.max(0L, wbta.getTimeRemaining() - delay));
				}
				else
				{
					continue;
				}
				
				Inventory inv = invHolder.getInventory();
				UUID uuid = entry.getKey();
				
				for(int i = 0; i < inv.getSize(); i++)
				{
					ItemStack item = inv.getItem(i);
					
					if(!CommonsUtil.compareUUID(item, uuid))
					{
						continue;
					}
					
					List<String> lore = new ArrayList<String>();
					
					long time = wbta.getTimeRemaining();
					
					int redTiles = (int) ((time % (24L * 60L * 60L * 20L) == 0 ? 0 : 1) 
							+ time / (24L * 60L * 60L * 20L));
					int greenTiles = TOTAL_TILES - redTiles;
					
					lore.add(ChatColor.GREEN + StringUtils.repeat('▍', Math.max(0, greenTiles)) 
							+ ChatColor.RED + StringUtils.repeat('▍', Math.max(0, redTiles)));
					
					if(time > 0L)
					{
						long seconds = time / 20L % 60;
						long minutes = time / (60L * 20L) % 60;
						long hours = time / (60L * 60L * 20L) % 24;
						long days = time / (24L * 60L * 60L * 20L);
						
						String msg = "";
						
						if(days != 0L)
						{
							msg = days + "d " + hours + "h " + minutes + "m " + seconds + "s";
						}
						else if(hours != 0L)
						{
							msg = hours + "h " + minutes + "m " + seconds + "s";
						}
						else if(minutes != 0L)
						{
							msg = minutes + "m " + seconds + "s";
						}
						else
						{
							msg = seconds + "s";
						}
						
						lore.add("" + ChatColor.GRAY + ChatColor.ITALIC + msg);
					}
					
					ItemMeta meta1 = item.getItemMeta();
					meta1.setLore(lore);
					item.setItemMeta(meta1);
				}
			}
		}, delay, delay);
		
		Bukkit.getPluginManager().registerEvents(this, Main.getPlugin());
	}
	
	private class Time
	{
		private int taskId;
		private long seconds = 2L;
//		private long seconds = BTA_PER_SECONDS;
		
		public Time(Player p)
		{
			this.taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), () ->
			{
				if(--seconds <= 0L)
				{
					seconds = BTA_PER_SECONDS;
					ItemStack wbta = wbtaSupplier.get();
					itemMap.put(CommonsUtil.getHiddenUUIDs(wbta).get(1), new WBTA(p, TICKS));
					p.getInventory().addItem(wbta);
				}
			}, 20L, 20L);
		}		
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args)
	{
		return new ArrayList<>();
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String aliases, String[] args)
	{
		ResourceBundle bundle;
		
		if(!(sender instanceof Player))
		{
			bundle = ResourceBundle.getBundle("message");
			sender.sendMessage(ChatColor.RED + bundle.getString("commons.cmds.cant-perform-command"));
			return true;
		}
		
		Player p = (Player) sender;
		CommonPlayer cp = CraftCommonPlayer.get(p);
		
		bundle = ResourceBundle.getBundle("message", cp.getLocale());
		
		if(args.length == 0)
		{
			TextComponent[] extra;
			TextComponent text;
			
			if(!isValidWorld(p.getWorld()))
			{
				extra = new TextComponent[1];
				extra[0] = new TextComponent("Raid");
				
				text = Message.getTextComponent(bundle.getString("cmd.playtime.invalid-world"), extra);
				text.setColor(ChatColor.RED);
				p.spigot().sendMessage(text);
				return true;
			}
			
			if(!(cp instanceof SurvivalPlayer))
			{
				p.sendMessage(ChatColor.RED + bundle.getString("cmd.playtime.player-mode"));
				return true;
			}
			
			if(cp.hasInvincibility())
			{
				p.sendMessage(ChatColor.RED + bundle.getString("cmd.playtime.has-invincibility"));
				return true;
			}
			
			if(!MAP.containsKey(p.getUniqueId()))
			{
				put(p);
			}
			
			long time = MAP.get(p.getUniqueId()).seconds;
			
			long seconds = time % 60;
			long minutes = time / (60L) % 60;
			long hours = time / (60L * 60L) % 24;
			
			String h = bundle.getString("commons.words.hour").substring(0, 1);
			String m = bundle.getString("commons.words.minute").substring(0, 1);
			String s = bundle.getString("commons.words.second").substring(0, 1);
			
			String msg = "";
			
			if(hours != 0L)
			{
				msg = hours + h + " " + minutes + m + " " + seconds + s;
			}
			else if(minutes != 0L)
			{
				msg = minutes + m + " " + seconds + s;
			}
			else
			{
				msg = seconds + s;
			}
			
			extra = new TextComponent[2];
			
			extra[0] = new TextComponent("$BTA");
			extra[0].setColor(ChatColor.DARK_PURPLE);
			
			extra[1] = new TextComponent(msg);
			extra[1].setColor(ChatColor.GRAY);
			
			text = Message.getTextComponent(bundle.getString("cmd.playtime"), extra);
			text.setColor(ChatColor.LIGHT_PURPLE);
			p.spigot().sendMessage(text);
		}
		else
		{
			TextComponent[] extra = new TextComponent[1];
			
			extra[0] = new TextComponent("/" + aliases);
			
			TextComponent text = Message.getTextComponent(bundle.getString("commons.cmds.wrong-syntax-try"), extra);
			text.setColor(ChatColor.RED);
			p.spigot().sendMessage(text);
		}

		return false;
	}
	
	public void save()
	{
		File file = Config.getFile(Type.WRAPPED_BTA_MAP, true);
		
		final Map<UUID, WBTA> map;
		
		if(file.length() > 0L)
		{
			try(FileInputStream fileIn = new FileInputStream(file);
					ByteArrayInputStream streamIn = new ByteArrayInputStream(ByteStreams.toByteArray(fileIn));
					ObjectInputStream in = new ObjectInputStream(streamIn))
			{
				map = (Map<UUID, WBTA>) in.readObject();
			}
			catch(ClassNotFoundException | IOException ex)
			{
				ex.printStackTrace();
				return;
			}
		}
		else
		{
			map = new HashMap<>();
		}
		
		try(FileOutputStream fileOut = new FileOutputStream(file);
				ByteArrayOutputStream streamOut = new ByteArrayOutputStream();
				ObjectOutputStream out = new ObjectOutputStream(streamOut))
		{
			map.putAll(itemMap);
			out.writeObject(map);
			fileOut.write(streamOut.toByteArray());
		}
		catch(IOException e)
		{
			e.printStackTrace();
			return;
		}
	}
	
	@EventHandler
	public void a(PlayerInvincibilityChangeEvent e)
	{
		CommonPlayer cp = e.getCommonPlayer();
		Player p = cp.getPlayer();
		
		if(!isValidWorld(p.getWorld()))
		{
			return;
		}
		
		if(cp.hasInvincibility())
		{
			remove(p.getUniqueId());
		}
		else
		{
			put(p);
		}
	}
	
	private void put(Player p)
	{
		Time time = MAP.put(p.getUniqueId(), new Time(p));
		
		if(time != null)
		{
			Bukkit.getScheduler().cancelTask(time.taskId);
		}
	}
	
	private void remove(UUID uuid)
	{
		Time time = MAP.remove(uuid);
		
		if(time != null)
		{
			Bukkit.getScheduler().cancelTask(time.taskId);
		}
	}
	
	@EventHandler
	public void a(PlayerModeChangeEvent e)
	{
		CommonPlayer cp = e.getCommonPlayer();
		Player p = cp.getPlayer();
		
		if(cp instanceof SurvivalPlayer && isValidWorld(p.getWorld()))
		{
			put(p);
		}
		else
		{
			remove(p.getUniqueId());
		}
	}
	
	@EventHandler
	public void a(PlayerChangedWorldEvent e)
	{
		Player p = e.getPlayer();
		CommonPlayer cp = CraftCommonPlayer.get(p);
		
		if(!(cp instanceof SurvivalPlayer))
		{
			remove(p.getUniqueId());
			return;
		}
		
		boolean validFrom = isValidWorld(e.getFrom());
		boolean validTo = isValidWorld(p.getWorld());
		
		if(validFrom && validTo)
		{
			return;
		}
		
		if(validTo)
		{
			put(p);
		}
		else
		{
			Inventory inv = p.getInventory();
			
			for(int i = 0; i < inv.getSize(); i++)
			{
				ItemStack item = inv.getItem(i);
				
				if(CommonsUtil.compareUUID(item, wbtaUUID))
				{
					CommonsUtil.setItemCopyOf(item, burnedWBTA);
				}
			}
			
			remove(p.getUniqueId());
		}
	}
	
	@EventHandler
	public void a(PlayerQuitEvent e)
	{
		Player p = e.getPlayer();
		remove(p.getUniqueId());
	}
	
	@EventHandler
	public void a(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		CommonPlayer cp = CraftCommonPlayer.get(p);
		ItemStack item = e.getItem();
		
		if(CommonsUtil.compareUUID(item, wbtaUUID))
		{
			UUID uuid = CommonsUtil.getHiddenUUIDs(item).get(1);
			WBTA wbta = itemMap.get(uuid);
			
			if(wbta != null && wbta.getTimeRemaining() >= 0L)
			{
				if(wbta.getTimeRemaining() == 0L)
				{
					wbta.setTimeRemaining(-1L);
					cp.setBTA(cp.getBTA() + 1.0D);
					p.setItemInHand(null);
					p.sendMessage(ChatColor.DARK_GREEN + "(+1 $BTA)");
				}
				else
				{
					ResourceBundle bundle = ResourceBundle.getBundle("message", cp.getLocale());
					
					long time = wbta.getTimeRemaining();
					long seconds = time / 20L % 60;
					long minutes = time / (60L * 20L) % 60;
					long hours = time / (60L * 60L * 20L) % 24;
					long days = time / (24L * 60L * 60L * 20L);
					
					String d = bundle.getString("commons.words.day").substring(0, 1);
					String h = bundle.getString("commons.words.hour").substring(0, 1);
					String m = bundle.getString("commons.words.minute").substring(0, 1);
					String s = bundle.getString("commons.words.second").substring(0, 1);
					
					String msg = "";
					
					if(days != 0L)
					{
						msg = days + d + " " + hours + h + " " + minutes + m + " " + seconds + s;
					}
					else if(hours != 0L)
					{
						msg = hours + h + " " + minutes + m + " " + seconds + s;
					}
					else if(minutes != 0L)
					{
						msg = minutes + m + " " + seconds + s;
					}
					else
					{
						msg = seconds + s;
					}
					
					TextComponent[] extra = new TextComponent[2];
					
					extra[0] = new TextComponent("$BTA");
					
					extra[1] = new TextComponent(msg);
					
					TextComponent text = Message.getTextComponent(bundle.getString("cmd.playtime.will-be-ready"), extra);
					text.setColor(ChatColor.RED);
					p.spigot().sendMessage(text);
				}
			}
			else
			{
				p.sendMessage("fake bta");
			}
		}
	}
	
	@EventHandler
	public void a(InventoryClickEvent e)
	{
		Player p = (Player) e.getWhoClicked();
		Inventory inv = e.getInventory();
		Inventory clickedInv = e.getClickedInventory();
		Inventory otherInv = inv.equals(clickedInv) ? p.getInventory() : inv;
		
		ItemStack current = e.getCurrentItem();
		ItemStack cursor = e.getCursor();
		ItemStack hotbar = e.getHotbarButton() != -1 ? p.getInventory().getItem(e.getHotbarButton()) : null;
		
		InventoryAction action = e.getAction();
		
		if(CommonsUtil.compareUUID(current, wbtaUUID))
		{
			UUID uuid = CommonsUtil.getHiddenUUIDs(current).get(1);
			
			if(!itemMap.containsKey(uuid))
			{
				return;
			}
			
			WBTA wbta = itemMap.get(uuid);
			
			switch(action)
			{
			case MOVE_TO_OTHER_INVENTORY:
				wbta.setInvHolder(otherInv.getHolder());
				break;
			case HOTBAR_SWAP:
			case HOTBAR_MOVE_AND_READD:
				wbta.setInvHolder(p);
				break;
			case PICKUP_ALL:
			case PICKUP_HALF:
			case PICKUP_ONE:
			case PICKUP_SOME:
			case SWAP_WITH_CURSOR:
			case DROP_ALL_SLOT:
			case DROP_ONE_SLOT:
			case COLLECT_TO_CURSOR:
			default:
				wbta.setInvHolder(null);
				break;
			}
		}
		
		if(CommonsUtil.compareUUID(cursor, wbtaUUID))
		{
			UUID uuid = CommonsUtil.getHiddenUUIDs(cursor).get(1);
					
			if(!itemMap.containsKey(uuid))
			{
				return;
			}
			
			WBTA wbta = itemMap.get(uuid);
			
			switch(action)
			{
			case PLACE_ALL:
			case PLACE_ONE:
			case PLACE_SOME:
			case SWAP_WITH_CURSOR:
				wbta.setInvHolder(clickedInv == null ? null : clickedInv.getHolder());
				break;
			default:
				break;
			}
		}
		
		if(CommonsUtil.compareUUID(hotbar, wbtaUUID))
		{
			UUID uuid = CommonsUtil.getHiddenUUIDs(hotbar).get(1);
			
			if(!itemMap.containsKey(uuid))
			{
				return;
			}
			
			WBTA wbta = itemMap.get(uuid);

			
			switch(action)
			{
			case HOTBAR_SWAP:
				wbta.setInvHolder(clickedInv.getHolder());
				break;
			default:
				break;
			}
		}
	}
	
	@EventHandler
	public void a(PlayerDropItemEvent e)
	{
		ItemStack itemStack = e.getItemDrop().getItemStack();
		
		if(!CommonsUtil.compareUUID(itemStack, wbtaUUID))
		{
			return;
		}
		
		UUID uuid = CommonsUtil.getHiddenUUIDs(itemStack).get(1);
		
		if(!itemMap.containsKey(uuid))
		{
			return;
		}
		
		WBTA wbta = itemMap.get(uuid);		
		wbta.setInvHolder(null);
	}
	
	@EventHandler
	public void a(PlayerPickupItemEvent e)
	{
		ItemStack itemStack = e.getItem().getItemStack();
		
		if(!CommonsUtil.compareUUID(itemStack, wbtaUUID))
		{
			return;
		}
		
		UUID uuid = CommonsUtil.getHiddenUUIDs(itemStack).get(1);
		
		if(!itemMap.containsKey(uuid))
		{
			return;
		}
		
		WBTA wbta = itemMap.get(uuid);		
		wbta.setInvHolder(e.getPlayer());
	}
	
	@EventHandler
	public void a(InventoryMoveItemEvent e)
	{
		ItemStack item = e.getItem();
		
		if(!CommonsUtil.compareUUID(item, wbtaUUID))
		{
			return;
		}
		
		UUID uuid = CommonsUtil.getHiddenUUIDs(item).get(1);
		
		if(!itemMap.containsKey(uuid))
		{
			return;
		}
		
		WBTA wbta = itemMap.get(uuid);		
		wbta.setInvHolder(e.getDestination().getHolder());
	}
	
	@EventHandler
	public void a(BlockBreakEvent e)
	{
		Block b = e.getBlock();
		
		if(b.getState() instanceof ContainerBlock)
		{
			ContainerBlock cb = (ContainerBlock) b.getState();
			
			for(Entry<UUID, WBTA> entry : itemMap.entrySet())
			{
				WBTA wbta = entry.getValue();
				
				if(wbta.getInvHolder().equals(cb.getInventory().getHolder()))
				{
					wbta.setInvHolder(null);
				}
			}
		}
	}
	
	
	@EventHandler
	public void a(InventoryPickupItemEvent e)
	{
		ItemStack itemStack = e.getItem().getItemStack();
		
		if(!CommonsUtil.compareUUID(itemStack, wbtaUUID))
		{
			return;
		}
		
		UUID uuid = CommonsUtil.getHiddenUUIDs(itemStack).get(1);
		
		if(!itemMap.containsKey(uuid))
		{
			return;
		}
		
		WBTA wbta = itemMap.get(uuid);		
		wbta.setInvHolder(e.getInventory().getHolder());
	}
	
	public Map<UUID, WBTA> getItemMap()
	{
		return itemMap;
	}
	
	public static Playtime getInstance()
	{
		return instance;
	}
	
	private boolean isValidWorld(World w)
	{
		return w.getName().contains("factions");
	}
}
