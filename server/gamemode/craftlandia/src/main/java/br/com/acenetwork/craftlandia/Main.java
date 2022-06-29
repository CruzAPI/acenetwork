package br.com.acenetwork.craftlandia;

import static br.com.acenetwork.craftlandia.Rarity.*;
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
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.entity.Wither;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.EntityBlockFormEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.entity.SheepRegrowWoolEvent;
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
import org.bukkit.event.world.StructureGrowEvent;
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
		
		wc = new WorldCreator("newbie");
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
				int skipBytes = Util.getArrayLength();
				
				while(access.getFilePointer() < access.length())
				{
					int x = access.readInt();
					int y = access.readInt();
					int z = access.readInt();
					
					access.skipBytes(skipBytes);
					
					w.getBlockAt(x, y, z).setMetadata("pos", new FixedMetadataValue(this, access.getFilePointer() - 12L - skipBytes));
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
	public void a(EntityExplodeEvent e)
	{
		Entity entity = e.getEntity();
		
		if(entity instanceof Creeper)
		{
			Creeper creeper = (Creeper) entity;
			Bukkit.broadcastMessage("POWER: " + (creeper.isPowered() ? 6 : 3));
		}
		else if(entity instanceof TNTPrimed)
		{
			TNTPrimed tntPrimed = (TNTPrimed) entity;
			Bukkit.broadcastMessage("POWER: 4 (yield = " + tntPrimed.getYield() + ")");
		}
		else if(entity instanceof EnderCrystal)
		{
			Bukkit.broadcastMessage("POWER: 6");
		}
		else if(entity instanceof Wither)
		{
			
		}
		else if(entity instanceof WitherSkull)
		{
			
		}
		
		Bukkit.broadcastMessage(e.getEntityType() + " type");
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
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void b(BlockPlaceEvent e)
	{
		Block b = e.getBlock();
		
		Util.writeBlock(b, Util.getByteArray(e.getItemInHand()));
	}
	
	
	@EventHandler
	public void a(BlockFromToEvent e)
	{
		e.setCancelled(false);
	}
	
	@EventHandler
	public void a(BlockPistonExtendEvent e)
	{
		List<byte[]> list = new ArrayList<>();
		
		for(int i = 0; i < e.getBlocks().size(); i++)
		{
			Block b = e.getBlocks().get(i);
			
			byte[] data = Util.readBlock(b);
			
			list.add(data);
			
			if(i == 0)
			{
				Util.writeBlock(b, Util.emptyArray());
			}
		}
		
		for(int i = 0; i < e.getBlocks().size(); i++)
		{
			Util.writeBlock(e.getBlocks().get(i).getRelative(e.getDirection()), list.get(i));
		}
	}
	
	@EventHandler
	public void temp(LeavesDecayEvent e)
	{
		e.setCancelled(true);
	}
	
	@EventHandler
	public void temp(BlockBurnEvent e)
	{
		e.setCancelled(true);
	}
	
	@EventHandler
	public void temp(BlockIgniteEvent e)
	{
		e.setCancelled(true);
	}
	
	@EventHandler
	public void temp(EntityChangeBlockEvent e)
	{
		e.setCancelled(true);
	}
	
	@EventHandler
	public void temp(EntityBlockFormEvent e)
	{
		e.setCancelled(true);
	}
	
	@EventHandler
	public void temp(StructureGrowEvent e)
	{
		e.setCancelled(true);
	}
	
	@EventHandler
	public void temasd(EntityBlockFormEvent e)
	{
		e.setCancelled(true);
	}
	
	@EventHandler
	public void temp(BlockSpreadEvent e)
	{
		e.setCancelled(true);
	}
	
	@EventHandler
	public void temp(BlockGrowEvent e)
	{
		e.setCancelled(true);
	}
	
	@EventHandler
	public void temp(BlockFormEvent e)
	{
		e.setCancelled(true);
	}
	
	@EventHandler
	public void a(BlockPhysicsEvent e)
	{
		if(true)
		{
			e.setCancelled(true);
			return;
		}
		e.setCancelled(true);
		
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
			
			if(down.getType() == Material.SAND || down.getType() == Material.DIRT || down.getType() == Material.GRASS)
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
			
			b.setType(Material.AIR, true);
			
			ItemStack item = new ItemStack(Material.SUGAR_CANE, 1);
			ItemMeta meta = item.getItemMeta();
			meta.setLore(Util.getLore(b));
			item.setItemMeta(meta);
			
			Util.writeBlock(b, Util.emptyArray());
			
			w.dropItemNaturally(b.getLocation(), item);
		}
		else if(b.getType() == Material.CACTUS)
		{
			e.setCancelled(true);
		
			Block down = b.getRelative(BlockFace.DOWN);
			
			boolean toBreak = true;
			
			if(down.getType() == Material.CACTUS || down.getType() == Material.SAND)
			{
				toBreak = false;
				
				BlockFace[] directions = new BlockFace[] {NORTH, SOUTH, EAST, WEST};
				
				for(BlockFace face : directions)
				{
					if(b.getRelative(face).getType().isSolid())
					{
						toBreak = true;
						break;
					}
				}
			}
			
			if(!toBreak)
			{
				return;
			}
			
			b.setType(Material.AIR, true);
			
			ItemStack item = new ItemStack(Material.CACTUS, 1);
			ItemMeta meta = item.getItemMeta();
			meta.setLore(Util.getLore(b));
			item.setItemMeta(meta);
			
			Util.writeBlock(b, Util.emptyArray());
			
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
		case SUGAR_CANE_BLOCK:
			source = newState.getBlock().getRelative(BlockFace.DOWN);
			break;
		default:
			source = b;
			break;
		}
		
		byte[] data = Util.readBlock(source);
		
		Util.writeBlock(newState.getBlock(), data);
	}
	
	@EventHandler
	public void a(PlayerBucketFillEvent e)
	{
		Player p = e.getPlayer();
		Block b = e.getBlockClicked();
		
		ItemStack itemInHand = p.getItemInHand();
		ItemStack item = e.getItemStack();
		
		byte data = Util.readBlockRarity(b);
		Util.writeBlock(b, Util.emptyArray());
		
		Rarity rarity = Rarity.getByDataOrWorld(data, b.getWorld());
		
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
		
		Util.writeBlock(b, Util.getByteArray(itemInHand));
		
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
				? Rarity.getByDataOrWorld(entity.getMetadata("rarity").get(0).asByte(), entity.getWorld()).getData() 
				: Util.getRarity(entity.getWorld()).getData();
		
		e.getDrops().forEach(x -> Util.setItemTag(x, Rarity.getByData(data)));
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
		byte data = Rarity.getByDataOrWorld(Util.readBlockRarity(e.getSpawner().getBlock()), entity.getWorld()).getData();
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
			
			Rarity rarity = Util.getRarity(content);
			
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
		
		Rarity rarity = Rarity.getByData(data);
		
		ItemStack result = inv.getResult();
		Util.setItemTag(result, rarity);
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
		List<byte[]> list = new ArrayList<>();
		
		for(int i = 0; i < e.getBlocks().size(); i++)
		{
			Block b = e.getBlocks().get(i);
			
			byte[] data = Util.readBlock(b);
			
			list.add(data);
			
			if(i == 0)
			{
				Util.writeBlock(b, Util.emptyArray());
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
			byte[] data = Util.readBlock(b);
			entity.setMetadata("data", new FixedMetadataValue(this, data));
			
			Util.writeBlock(b, Util.emptyArray());
		}
		else
		{
			if(entity.hasMetadata("data"))
			{
				byte[] data = (byte[]) entity.getMetadata("data").get(0).value();
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
			meta.setLore(Util.getLore(b));
			item.setItemMeta(meta);
			
			Bukkit.getScheduler().runTask(this, () ->
			{
				b.getWorld().dropItemNaturally(b.getLocation(), item);
			});
		}
		
		short damage;
		
		loop:switch(tool.getType())
		{
		case SHEARS:
			switch(b.getType())
			{
			case TRIPWIRE:
			case LEAVES:
			case LEAVES_2:
			case WEB:
			case DEAD_BUSH:
			case LONG_GRASS:
			case VINE:
			case WOOL:
				damage = 1;
				break loop;
			default:
				damage = 0;
				break loop;
			}
		case DIAMOND_SWORD:
		case GOLD_SWORD:
		case IRON_SWORD:
		case STONE_SWORD:
		case WOOD_SWORD:
			damage = 2;
			break;
		case DIAMOND_SPADE:
		case GOLD_SPADE:
		case IRON_SPADE:
		case STONE_SPADE:
		case WOOD_SPADE:
		case DIAMOND_PICKAXE:
		case GOLD_PICKAXE:
		case IRON_PICKAXE:
		case STONE_PICKAXE:
		case WOOD_PICKAXE:
		case DIAMOND_AXE:
		case GOLD_AXE:
		case IRON_AXE:
		case STONE_AXE:
		case WOOD_AXE:
			damage = 1;
			break;
		default:
			damage = 0;
			break;
		}
		
		Random r = new Random();
		
		if(r.nextDouble() <= (1.0D / (tool.getEnchantmentLevel(Enchantment.DURABILITY) + 1.0D)))
		{
			tool.setDurability((short) (tool.getDurability() + damage));
			
			if(tool.getDurability() > tool.getType().getMaxDurability())
			{
				p.playSound(p.getLocation(), Sound.ITEM_BREAK, 1.0F, 1.0F);
				p.setItemInHand(null);
			}
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