package br.com.acenetwork.craftlandia;

import static br.com.acenetwork.craftlandia.ItemTag.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOError;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
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
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
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
import org.bukkit.metadata.FixedMetadataValue;

import br.com.acenetwork.commons.Common;
import br.com.acenetwork.commons.manager.CommonsConfig;
import br.com.acenetwork.commons.manager.CommonsConfig.Type;
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
		
		wc = new WorldCreator("jackpot");
		wc.environment(Environment.NORMAL);
		wc.createWorld();
		
		wc = new WorldCreator("oldworld");
		wc.createWorld();
		
		for(World w : Bukkit.getWorlds())
		{
			File file = CommonsConfig.getFile(Type.BLOCK_DATA, false, w.getName());
			
			if(!file.exists())
			{
				continue;
			}
			
			try(RandomAccessFile access = new RandomAccessFile(file, "r"))
			{
				while(access.getFilePointer() < access.length())
				{
					int x = access.readInt();
					int y = access.readInt();
					int z = access.readInt();
					
					access.readByte();
					
					w.getBlockAt(x, y, z).setMetadata("pos", new FixedMetadataValue(this, access.getFilePointer() - 13L));
				}
			}
			catch(IOException ex)
			{
				ex.printStackTrace();
			}
		}
		
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
	public void b(BlockPlaceEvent e)
	{
		ItemTag rarity = Util.getRarity(e.getItemInHand());
		
		if(rarity == null)
		{
			return;
		}
		
		byte data = rarity.getData();
		Block b = e.getBlock();
		
		Util.writeBlock(b, data);
	}
	
	
	@EventHandler
	public void a(BlockFromToEvent e)
	{
		e.setCancelled(true);
	}
	
	@EventHandler
	public void a(BlockPistonExtendEvent e)
	{
		List<Byte> list = new ArrayList<>();
		
		for(int i = 0; i < e.getBlocks().size(); i++)
		{
			Block b = e.getBlocks().get(i);
			
			byte data = Util.readBlock(b);
			
			list.add(data);
			
			if(i == 0)
			{
				Util.writeBlock(b, (byte) 0);
			}
		}
		
		for(int i = 0; i < e.getBlocks().size(); i++)
		{
			Util.writeBlock(e.getBlocks().get(i).getRelative(e.getDirection()), list.get(i));
		}
	}
	
	@EventHandler
	public void a(BlockPistonRetractEvent e)
	{
		List<Byte> list = new ArrayList<>();
		
		for(int i = 0; i < e.getBlocks().size(); i++)
		{
			Block b = e.getBlocks().get(i);
			
			byte data = Util.readBlock(b);
			
			list.add(data);
			
			if(i == 0)
			{
				Util.writeBlock(b, (byte) 0);
			}
		}
		
		for(int i = 0; i < e.getBlocks().size(); i++)
		{
			Bukkit.broadcastMessage(e.getBlocks().get(i).getRelative(e.getDirection().getOppositeFace()) + "");
			Util.writeBlock(e.getBlocks().get(i).getRelative(e.getDirection()), list.get(i));
		}
	}
	
	@EventHandler
	public void a(EntityChangeBlockEvent e)
	{
		Block b = e.getBlock();
		Entity entity = e.getEntity();
		
		if(entity.getType() != EntityType.FALLING_BLOCK)
		{
			return;
		}
		
		if(e.getTo() == Material.AIR)
		{
			byte data = Util.readBlock(b);
			entity.setMetadata("rarity", new FixedMetadataValue(this, data));
			
			Util.writeBlock(b, (byte) 0);
		}
		else
		{
			if(entity.hasMetadata("rarity"))
			{
				byte data = entity.getMetadata("rarity").get(0).asByte();
				Util.writeBlock(b, data);
			}
		}
	}
	
	@EventHandler
	public void a(BlockBreakEvent e)
	{
		Player p = e.getPlayer();
		
		if(p.getGameMode() == GameMode.CREATIVE)
		{
			return;
		}
		
		Block b = e.getBlock();
		
		ItemTag rarity = ItemTag.getByData(Util.readBlock(b));
		
		if(rarity == null)
		{
			switch(b.getWorld().getName())
			{
			case "world":
				rarity = LEGENDARY;
				break;
			case "factions":
			case "factions_nether":
			case "factions_the_end":
				rarity = RARE;
				break;
			default:
				rarity = COMMON;
				break;
			}
		}
		
		
		
		e.setCancelled(true);
		
		
		for(ItemStack item : b.getDrops(p.getItemInHand()))
		{
			ItemMeta meta;
			meta = item.getItemMeta();
			meta.setLore(Arrays.asList(rarity.toString()));
			item.setItemMeta(meta);
			
			Bukkit.getScheduler().runTask(this, () ->
			{
				b.getWorld().dropItemNaturally(b.getLocation(), item);
			});
		}
		
		b.setType(Material.AIR);
	}
	
	@EventHandler
	public void a(PlayerInteractEvent e)
	{
		Block b = e.getClickedBlock();
		
		if(b == null)
		{
			return;
		}
		
		World w = b.getWorld();
		File file = CommonsConfig.getFile(Type.BLOCK_DATA, true, w.getName());
		
		Action action = e.getAction();
		
		if(action == Action.LEFT_CLICK_BLOCK)
		{
			
		}
		else if(action == Action.RIGHT_CLICK_BLOCK && b.hasMetadata("pos"))
		{
			
		}
	}
	
//	@EventHandler
//	public void a(PlayerPickupItemEvent e)
//	{
//		Item item = e.getItem();
//		ItemStack itemStack = item.getItemStack();
//		
//		ItemTag itemTag;
//		
//		
//		
//		ItemMeta meta;
//		
//		meta = itemStack.getItemMeta();
//		
//		meta.setLore(Arrays.asList(
//		(
//				itemTag.toString()
//		)));
//		
//		itemStack.setItemMeta(meta);
//	}
	
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