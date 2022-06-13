package br.com.acenetwork.craftlandia;

import static br.com.acenetwork.craftlandia.ItemTag.*;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkPopulateEvent;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import br.com.acenetwork.commons.Common;
import br.com.acenetwork.craftlandia.executor.ItemInfo;
import br.com.acenetwork.craftlandia.listener.PlayerMode;
import net.md_5.bungee.api.ChatColor;

public class Main extends Common implements Listener
{
	private static Main instance;
	
	@Override
	public void onEnable()
	{
		instance = this;
		
		super.onEnable();
		
		registerCommand(new ItemInfo(), "iteminfo");
		
		getServer().getPluginManager().registerEvents(this, this);
		getServer().getPluginManager().registerEvents(new PlayerMode(), this);
		
		WorldCreator wc;
		
		wc = new WorldCreator("factions");
		wc.environment(Environment.NORMAL);
		wc.generateStructures(true);
		wc.createWorld();
		
		wc = new WorldCreator("farm");
		wc.environment(Environment.THE_END);
		wc.createWorld();
		
		wc = new WorldCreator("portals");
		wc.environment(Environment.THE_END);
		wc.createWorld();
		
		wc = new WorldCreator("oldworld");
		wc.createWorld();
		
//		WorldCreator wc = new WorldCreator("farmkkkk");
//		
//		wc.environment(Environment.THE_END);
//		wc.generateStructures(false);
//		wc.generator(Common.getPlugin().new VoidGenerator());
//		wc.createWorld();
//		
//		wc = new WorldCreator("world");
//		
////		wc.environment(Environment.THE_END);
//		wc.generateStructures(false);
//		wc.generator(Common.getPlugin().new VoidGenerator());
//		wc.createWorld();

		
//		Bukkit.getWorld("world").gene
	}
	
	@EventHandler
	public void a(SignChangeEvent e)
	{
		String[] lines = e.getLines();
		
		for(int i = 0; i < lines.length; i++)
		{
			lines[i] = lines[i].replace('&', ChatColor.COLOR_CHAR);
			e.setLine(i, lines[i]);
		}
	}
	
	@EventHandler
	public void a(PlayerInteractEvent e)
	{
		Block b = e.getClickedBlock();
		
//		if(b != null && b.getState() instanceof Sign)
//		{
//			Sign sign = (Sign) b.getState();
//			sign.setLine(0, ChatColor.DARK_PURPLE + "DIAMOND#3");
//			sign.setLine(1, ChatColor.LIGHT_PURPLE + "64");
//			sign.setLine(2, "" + ChatColor.DARK_PURPLE + ChatColor.BOLD + "B 1000 S 1000");
//			sign.setLine(3, ChatColor.GOLD + "B 100 S 100");
//			sign.update();
//		}
	}
	
	@EventHandler
	public void a(PlayerPickupItemEvent e)
	{
		Item item = e.getItem();
		ItemStack itemStack = item.getItemStack();
		
		ItemTag itemTag;
		
		switch(item.getWorld().getName())
		{
		case "world":
			itemTag = LEGENDARY;
			break;
		case "oldworld":
			itemTag = RARE;
			break;
		default:
			itemTag = COMMON;
			break;
		}
		
		ItemMeta meta;
		
		meta = itemStack.getItemMeta();
		
		meta.setLore(Arrays.asList(
		(
				itemTag.toString()
		)));
		
		itemStack.setItemMeta(meta);
	}
	
	@EventHandler
	public void a(PrepareItemCraftEvent e)
	{
//		e.get
//		e.get
//		e.
	}
	
//	@EventHandler
//	public void a(ChunkPopulateEvent e)
//	{
//		if(e.getChunk().getX() == 0 && e.getChunk().getZ() == 0)
//		{
//			Bukkit.broadcastMessage("0 0");
//		}
//		
//		if(e.getWorld().getName().equals("farm"))
//		{
////			new Thread(() ->
////			{
//				for(int x = 0; x < 16; x++)
//				{
//					for(int z = 0; z < 16; z++)
//					{
//						for(int y = 0; y < 4; y++)
//						{
//							e.getChunk().getBlock(x, y, z).setType(Material.AIR);
//						}
//					}
//				}
////			}).start();
//		}
//	}
//	
	@EventHandler
	public void a(BlockSpreadEvent e)
	{
		e.setCancelled(true);
	}
	
	@EventHandler
	public void a(BlockBurnEvent e)
	{
		e.setCancelled(true);
	}
	
	@EventHandler
	public void a(BlockPlaceEvent e)
	{
		Block b = e.getBlock();
		
		if(b.getType() == Material.BEDROCK)
		{
			e.setCancelled(true);
			
			World w = b.getWorld();
			
			w.spawnEntity(b.getLocation().add(0.5D, 1.0D, 0.5D), EntityType.ENDER_CRYSTAL);
		}
	}
	
	@EventHandler
	public void a(InventoryOpenEvent e)
	{
		Inventory inv = e.getInventory();
		
		if(inv.getType() == InventoryType.ENCHANTING)
		{
			inv.setItem(1, new ItemStack(Material.INK_SACK, 3, (short) 4));
		}
	}
	
	@EventHandler
	public void a(InventoryCloseEvent e)
	{
		Inventory inv = e.getInventory();
		
		if(inv.getType() == InventoryType.ENCHANTING)
		{
			inv.setItem(1, null);
		}
	}
	
	@EventHandler
	public void a(InventoryDragEvent e)
	{
		if(e.getInventory().getType() == InventoryType.ENCHANTING)
		{
			if(e.getRawSlots().contains(1))
			{
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void a(InventoryClickEvent e)
	{
		InventoryAction action = e.getAction();
		int rawSlot = e.getRawSlot();
		ItemStack current = e.getCurrentItem();
		ItemStack cursor = e.getCursor();
		
		if(e.getInventory().getType() == InventoryType.ENCHANTING)
		{
			if(rawSlot == 1)
			{
				e.setCancelled(true);
			}
			else if(current != null && current.getType() == Material.INK_SACK && current.getDurability() == (short) 4
					&& action == InventoryAction.MOVE_TO_OTHER_INVENTORY)
			{
				e.setCancelled(true);
			}
			else if(cursor != null && cursor.getType() == Material.INK_SACK && cursor.getDurability() == (short) 4
					&& action == InventoryAction.COLLECT_TO_CURSOR)
			{
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void a(EnchantItemEvent e)
	{
		Player p = e.getEnchanter();
		Inventory inv = e.getInventory();
		
		Bukkit.getScheduler().runTask(this, () ->
		{
			if(p.getGameMode() != GameMode.CREATIVE)
			{
				p.setLevel(p.getLevel() - (e.getExpLevelCost() - (1 + e.whichButton())));
				inv.setItem(1, new ItemStack(Material.INK_SACK, 3, (short) 4));
			}
		});
	}

	public static Main getInstance()
	{
		return instance;
	}
}