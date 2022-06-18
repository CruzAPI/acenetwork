package br.com.acenetwork.craftlandia;

import static br.com.acenetwork.craftlandia.ItemTag.*;
import static org.bukkit.block.BlockFace.*;

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
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.SpawnerSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkPopulateEvent;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.io.BukkitObjectInputStream;

import br.com.acenetwork.commons.Common;
import br.com.acenetwork.commons.manager.CommonsConfig;
import br.com.acenetwork.commons.manager.CommonsConfig.Type;
import br.com.acenetwork.craftlandia.executor.ItemInfo;
import br.com.acenetwork.craftlandia.executor.Price;
import br.com.acenetwork.craftlandia.executor.Sell;
import br.com.acenetwork.craftlandia.executor.Sellall;
import br.com.acenetwork.craftlandia.executor.Shop;
import br.com.acenetwork.craftlandia.executor.ShopSearch;
import br.com.acenetwork.craftlandia.executor.Temp;
import br.com.acenetwork.craftlandia.listener.PlayerMode;
import br.com.acenetwork.craftlandia.warp.Farm;
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
		
		registerCommand(new Temp(), "temp");
		
		registerCommand(new Price(), "price");
		registerCommand(new Sell(), "sell");
		registerCommand(new Sellall(), "sellall");
		registerCommand(new Shop(), "shop");
		registerCommand(new ShopSearch(), "shopsearch");
		
		WorldCreator wc;
		
		wc = new WorldCreator("factions");
		wc.environment(Environment.NORMAL);
		wc.generateStructures(true);
		wc.createWorld();
		
		wc = new WorldCreator("farm");
		wc.environment(Environment.THE_END);
		new Farm(wc.createWorld());
		
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
			
			file = CommonsConfig.getFile(Type.SIGN_DATA, false, w.getName());
			
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
					
					w.getBlockAt(x, y, z).setMetadata("signPos", new FixedMetadataValue(this, access.getFilePointer() - 12L));
					
					access.skipBytes(72);
//					access.seek(access.getFilePointer() + 72L);
				}
			}
			catch(IOException ex)
			{
				ex.printStackTrace();
			}
		}
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
	public void a(BlockPhysicsEvent e)
	{
		Block b = e.getBlock();
		World w = b.getWorld();
		
		if(b.getType() == Material.SUGAR_CANE_BLOCK)
		{
			e.setCancelled(true);
			
			Block down = b.getRelative(BlockFace.DOWN);
			
			if(down.getType() == Material.SUGAR_CANE_BLOCK)
			{
				return;
			}
			
			if(down.getType() == Material.SAND || down.getType() == Material.GRAVEL)
			{
				BlockFace[] directions = new BlockFace[] {NORTH, SOUTH, EAST, WEST};
				
				for(BlockFace face : directions)
				{
					if(down.getRelative(face).getType() == Material.WATER || down.getRelative(face).getType() == Material.STATIONARY_WATER)
					{
						return;
					}
				}
			}
			
			byte data = Util.readBlock(b);
			ItemTag rarity = ItemTag.getByData(data);
			rarity = rarity == null ? Util.getRarity(w) : rarity;
			
			b.setType(Material.AIR, true);
			
			Util.writeBlock(b, (byte) 0);
			
			ItemStack item = new ItemStack(Material.SUGAR_CANE, 1);
			ItemMeta meta = item.getItemMeta();
			meta.setLore(Arrays.asList(rarity.toString()));
			item.setItemMeta(meta);
			
			w.dropItemNaturally(b.getLocation(), item);
		}
	}
	
	@EventHandler
	public void a(BlockGrowEvent e)
	{
		Block b = e.getBlock();
		
		BlockState newState = e.getNewState();
		
		Block source;
		
		switch(newState.getType())
		{
		case CACTUS:
		case SUGAR_CANE:
			source = newState.getBlock().getRelative(BlockFace.DOWN);
			break;
		default:
			source = b;
			break;
		}
		
		byte data = Util.readBlock(source);
		
		Util.writeBlock(newState.getBlock(), data);
	}
	
	@EventHandler
	public void a(PlayerBucketFillEvent e)
	{
		Player p = e.getPlayer();
		Block b = e.getBlockClicked();
		
		ItemStack itemInHand = p.getItemInHand();
		ItemStack item = e.getItemStack();
		
		byte data = Util.readBlock(b);
		Util.writeBlock(b, (byte) 0);
		
		ItemTag rarity = ItemTag.getByDataOrWorld(data, b.getWorld());
		
		Util.addRarity(item, Util.getRarity(itemInHand));
		Util.addRarity(item, rarity);
		
		e.setItemStack(item);
	}
	
	@EventHandler
	public void a(PlayerBucketEmptyEvent e)
	{
		Player p = e.getPlayer();
		Block b = e.getBlockClicked();
		
		ItemStack itemInHand = p.getItemInHand();
		ItemStack item = e.getItemStack();
		
		ItemTag rarity = Util.getLastRarity(itemInHand);
		byte data = rarity == null ? 0 : rarity.getData();
		Util.writeBlock(b, data);
		
		Util.addRarity(item, Util.getRarity(itemInHand));
		
		e.setItemStack(item);
	}
	
	@EventHandler
	public void a(PlayerInteractEntityEvent e)
	{
		Entity entity = e.getRightClicked();
		
		Player p = e.getPlayer();
		
		p.sendMessage(entity.hasMetadata("task") + "");
	}
	
	@EventHandler
	public void a(EntityDeathEvent e)
	{
		Entity entity = e.getEntity();
		
		final byte data = entity.hasMetadata("rarity") 
				? ItemTag.getByDataOrWorld(entity.getMetadata("rarity").get(0).asByte(), entity.getWorld()).getData() 
				: Util.getRarity(entity.getWorld()).getData();
		
		e.getDrops().forEach(x -> Util.setRarity(x, ItemTag.getByData(data)));
	}
	
	@EventHandler
	public void a(CreatureSpawnEvent e)
	{
		if(e.getSpawnReason() == SpawnReason.SPAWNER)
		{
			return;
		}
		
		Entity entity = e.getEntity();
		
		byte data = Util.getRarity(entity.getWorld()).getData();
		entity.setMetadata("rarity", new FixedMetadataValue(this, data));
	}
	
	@EventHandler
	public void a(SpawnerSpawnEvent e)
	{
		Entity entity = e.getEntity();
		byte data = ItemTag.getByDataOrWorld(Util.readBlock(e.getSpawner().getBlock()), entity.getWorld()).getData();
		entity.setMetadata("rarity", new FixedMetadataValue(this, data));
	}
	
	@EventHandler
	public void a(PrepareItemCraftEvent e)
	{
		byte data = 3;
		CraftingInventory inv = e.getInventory();
		
		for(ItemStack content : inv.getMatrix())
		{
			if(content == null || content.getType() == Material.AIR)
			{
				continue;
			}
			
			ItemTag rarity = Util.getRarity(content);
			
			if(rarity == null)
			{
				data = 1;
				break;
			}
			
			if(rarity.getData() < data)
			{
				data = rarity.getData();
			}
		}
		
		ItemTag rarity = ItemTag.getByData(data);
		
		ItemStack result = inv.getResult();
		Util.setRarity(result, rarity);
		inv.setResult(result);
	}
	
	@EventHandler
	public void a(WeatherChangeEvent e)
	{
		e.setCancelled(true);
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
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void a(BlockBreakEvent e)
	{
		Player p = e.getPlayer();
		
		if(p.getGameMode() == GameMode.CREATIVE)
		{
			return;
		}
		
		Block b = e.getBlock();
		
		ItemTag rarity = ItemTag.getByDataOrWorld(Util.readBlock(b), b.getWorld());
				
		e.setCancelled(true);
		
		ItemStack tool = p.getItemInHand();
		List<ItemStack> drops = new ArrayList<>(b.getDrops(tool));
		
		Bukkit.broadcastMessage(b.getType() + "");
		
		if(b.getType() == Material.MOB_SPAWNER && tool.getType() == Material.GOLD_PICKAXE)
		{
			Bukkit.broadcastMessage("aaadd");
			drops.add(new ItemStack(Material.MOB_SPAWNER, 1, (short) b.getData()));
		}
		
		for(ItemStack item : drops)
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