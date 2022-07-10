package br.com.acenetwork.craftlandia;

import static org.bukkit.block.BlockFace.EAST;
import static org.bukkit.block.BlockFace.NORTH;
import static org.bukkit.block.BlockFace.SOUTH;
import static org.bukkit.block.BlockFace.WEST;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.entity.Wither;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.SlimeSplitEvent;
import org.bukkit.event.entity.SpawnerSpawnEvent;
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
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.event.world.WorldSaveEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

import br.com.acenetwork.commons.Common;
import br.com.acenetwork.commons.manager.CommonsConfig;
import br.com.acenetwork.commons.manager.CommonsConfig.Type;
import br.com.acenetwork.craftlandia.executor.Delhome;
import br.com.acenetwork.craftlandia.executor.Give;
import br.com.acenetwork.craftlandia.executor.Home;
import br.com.acenetwork.craftlandia.executor.ItemInfo;
import br.com.acenetwork.craftlandia.executor.Jackpot;
import br.com.acenetwork.craftlandia.executor.Playtime;
import br.com.acenetwork.craftlandia.executor.Portal;
import br.com.acenetwork.craftlandia.executor.Price;
import br.com.acenetwork.craftlandia.executor.Sell;
import br.com.acenetwork.craftlandia.executor.Sellall;
import br.com.acenetwork.craftlandia.executor.Sethome;
import br.com.acenetwork.craftlandia.executor.Shop;
import br.com.acenetwork.craftlandia.executor.ShopSearch;
import br.com.acenetwork.craftlandia.executor.Spawn;
import br.com.acenetwork.craftlandia.executor.Temp;
import br.com.acenetwork.craftlandia.executor.Visit;
import br.com.acenetwork.craftlandia.listener.PlayerMode;
import br.com.acenetwork.craftlandia.listener.RandomItem;
import br.com.acenetwork.craftlandia.warp.Factions;
import br.com.acenetwork.craftlandia.warp.Farm;
import br.com.acenetwork.craftlandia.warp.Newbie;
import br.com.acenetwork.craftlandia.warp.Portals;
import br.com.acenetwork.craftlandia.warp.WarpJackpot;
import br.com.acenetwork.craftlandia.warp.WarpLand;
import net.md_5.bungee.api.ChatColor;

public class Main extends Common implements Listener
{
	private static Main instance;
	
	@Override
	public void onEnable()
	{
		instance = this;
		
		registerCommand(new Give(), "give");
		
		super.onEnable();
		
		
		getServer().getPluginManager().registerEvents(this, this);
		getServer().getPluginManager().registerEvents(new PlayerMode(), this);
		getServer().getPluginManager().registerEvents(new RandomItem(), this);
		
		WorldCreator wc;
		
		wc = new WorldCreator("factions");
		wc.environment(Environment.NORMAL);
		wc.generateStructures(true);
		new Factions(wc.createWorld());
		
		wc = new WorldCreator("newbie");
		wc.environment(Environment.NORMAL);
		wc.generateStructures(true);
		new Newbie(wc.createWorld());
		
		wc = new WorldCreator("farm");
		wc.environment(Environment.THE_END);
		new Farm(wc.createWorld());
		
		wc = new WorldCreator("portals");
		wc.environment(Environment.THE_END);
		new Portals(wc.createWorld());
		
		wc = new WorldCreator("jackpot");
		wc.environment(Environment.NORMAL);
		new WarpJackpot(wc.createWorld());
		
		wc = new WorldCreator("oldworld");
		new WarpLand(wc.createWorld());
		
		registerCommand(new Temp(), "temp");
		
		registerCommand(new ItemInfo(), "iteminfo");
		registerCommand(new Jackpot(), "jackpot");
		registerCommand(new Playtime(), "playtime");
		registerCommand(new Portal(), "portal");
		registerCommand(new Price(), "price");
		registerCommand(new Spawn(), "spawn");
		registerCommand(new Sell(), "sell");
		registerCommand(new Sellall(), "sellall");
		registerCommand(new Shop(), "shop");
		registerCommand(new ShopSearch(), "shopsearch");
		
		registerCommand(new Home(), "home");
		registerCommand(new Sethome(), "sethome");
		registerCommand(new Delhome(), "delhome");
		registerCommand(new Visit(), "visit");
		
		for(World w : Bukkit.getWorlds())
		{
			File file;
			
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
	
	@Override
	public void onDisable()
	{
		super.onDisable();
	}
	
	@EventHandler
	public void a(WorldSaveEvent e)
	{
		if(!e.getWorld().getName().equals("world"))
		{
			return;
		}
		
		Home.getInstance().save();
		Portal.getInstance().save();
		Playtime.getInstance().save();
		Price.getInstance().save();
		Jackpot.getInstance().save();
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
				Util.writeBlock(b, null);
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
			
			Util.writeBlock(b, null);
			
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
			
			Util.writeBlock(b, null);
			
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
		
		byte data = Util.readBlock(b)[0];
		Util.writeBlock(b, null);
		
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
		
		if(entity instanceof Player)
		{
			return;
		}
		
		Rarity rarity = Rarity.valueOfToString(entity.getCustomName());
		Rarity rarity1 = rarity == null ? Util.getRarity(entity.getWorld()) : rarity;
		
		e.getDrops().forEach(x -> Util.setCommodity(x, rarity1));
	}
	
	@EventHandler
	public void a(SlimeSplitEvent e)
	{
		Slime slime = e.getEntity();
		
		if(slime.getCustomName() != null)
		{
			e.setCancelled(true);
			
			World w = slime.getWorld();
			
			Random r = new Random();
			
			double slimeX = slime.getLocation().getX();
			double slimeY = slime.getLocation().getY();
			double slimeZ = slime.getLocation().getZ();
			
			int i = 0;
			int count = 2 + r.nextInt(3);
			
			loop:for(double x = -0.25D; x <= 0.25D; x += 0.5D)
			{
				for(double z = -0.25D; z <= 0.25D; z += 0.5D)
				{
					if(i++ >= count)
					{
						break loop;
					}
					
					Slime child = (Slime) w.spawnEntity(new Location(w, slimeX + x, slimeY + 0.5D, slimeZ + z, 0.0F, r.nextInt(360) + r.nextFloat()), slime.getType());
					child.setSize(Math.max(1, slime.getSize() / 2));
					child.setCustomName(slime.getCustomName());
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void ab(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		
		ItemStack item = e.getItem();
		Block b = e.getClickedBlock();
		
		if(b == null || item == null || item.getType() != Material.MONSTER_EGG)
		{
			return;
		}
		
		Rarity rarity = Util.getRarity(item);
		
		if(rarity == null)
		{
			return;
		}
		
		e.setCancelled(true);
		
		
		Block relative = b.getRelative(e.getBlockFace());
		
		LivingEntity le = relative.getWorld().spawnCreature(relative.getLocation(), EntityType.fromId(item.getDurability()));
		
		if(!le.isValid())
		{
			le.remove();
			return;
		}
		
		le.setCustomName(rarity.toString());
		
		if(p.getGameMode() == GameMode.CREATIVE)
		{
			return;
		}
		
		item.setAmount(item.getAmount() - 1);
		
		if(item.getAmount() <= 0)
		{	
			p.setItemInHand(null);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void ab(PlayerInteractEntityEvent e)
	{
		Entity entity = e.getRightClicked();
		
		if(entity.getCustomName() == null)
		{
			return;
		}
		
		for(Rarity values : Rarity.values())
		{
			if(values.toString().equals(entity.getCustomName()))
			{
				e.setCancelled(true);
				return;
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void a(SpawnerSpawnEvent e)
	{
		Entity entity = e.getEntity();
		entity.setCustomName(Rarity.getByDataOrWorld(Util.readBlock(e.getSpawner().getBlock())[0], entity.getWorld()).toString());
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
				Util.writeBlock(b, null);
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
			
			Util.writeBlock(b, null);
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
		
		byte[] bytes = Util.readBlock(b);
		
		Bukkit.broadcastMessage("" + bytes[0] + " " + bytes[1]);
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
		
//		if(b.getType() == Material.BEDROCK)
//		{
//			e.setCancelled(true);
//			
//			World w = b.getWorld();
//			
//			w.spawnEntity(b.getLocation().add(0.5D, 1.0D, 0.5D), EntityType.ENDER_CRYSTAL);
//		}
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