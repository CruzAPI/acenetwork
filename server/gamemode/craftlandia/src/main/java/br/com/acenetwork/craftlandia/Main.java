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
import java.util.Optional;
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
import org.bukkit.block.ContainerBlock;
import org.bukkit.block.Furnace;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.MushroomCow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Slime;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Wither;
import org.bukkit.entity.WitherSkull;
import org.bukkit.entity.minecart.StorageMinecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.SlimeSplitEvent;
import org.bukkit.event.entity.SpawnerSpawnEvent;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.event.world.WorldSaveEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.FurnaceInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Attachable;
import org.bukkit.material.Bed;
import org.bukkit.material.Directional;
import org.bukkit.material.Door;
import org.bukkit.material.Rails;
import org.bukkit.material.Stairs;
import org.bukkit.material.Step;
import org.bukkit.material.TrapDoor;
import org.bukkit.material.WoodenStep;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.acenetwork.commons.Common;
import br.com.acenetwork.commons.manager.CommonsConfig;
import br.com.acenetwork.commons.manager.CommonsConfig.Type;
import br.com.acenetwork.commons.player.CommonPlayer;
import br.com.acenetwork.commons.player.craft.CraftCommonPlayer;
import br.com.acenetwork.craftlandia.event.FallingBlockObstructEvent;
import br.com.acenetwork.craftlandia.executor.Delhome;
import br.com.acenetwork.craftlandia.executor.Give;
import br.com.acenetwork.craftlandia.executor.Home;
import br.com.acenetwork.craftlandia.executor.ItemInfo;
import br.com.acenetwork.craftlandia.executor.Jackpot;
import br.com.acenetwork.craftlandia.executor.LandCMD;
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
import br.com.acenetwork.craftlandia.inventory.CustomAnvil;
import br.com.acenetwork.craftlandia.listener.FallingBlockChecker;
import br.com.acenetwork.craftlandia.listener.PlayerMode;
import br.com.acenetwork.craftlandia.listener.RandomItem;
import br.com.acenetwork.craftlandia.listener.TestListener;
import br.com.acenetwork.craftlandia.manager.BlockData;
import br.com.acenetwork.craftlandia.manager.BreakReason;
import br.com.acenetwork.craftlandia.manager.LandData;
import br.com.acenetwork.craftlandia.warp.Factions;
import br.com.acenetwork.craftlandia.warp.Farm;
import br.com.acenetwork.craftlandia.warp.Newbie;
import br.com.acenetwork.craftlandia.warp.Portals;
import br.com.acenetwork.craftlandia.warp.WarpJackpot;
import br.com.acenetwork.craftlandia.warp.WarpLand;
import br.com.acenetwork.craftlandia.warp.WarpTutorial;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.event.CitizensPreReloadEvent;
import net.citizensnpcs.api.event.CitizensReloadEvent;
import net.citizensnpcs.api.npc.NPCRegistry;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;

public class Main extends Common implements Listener
{
	private static Main instance;
	
	@Override
	public void onEnable()
	{
		instance = this;
		
//		CitizensAPI.getNPCRegistry().deregisterAll();
		
		registerCommand(new Give(), "give");
		
		super.onEnable();
		
		WorldCreator wc;
		
		wc = new WorldCreator("factions");
		wc.environment(Environment.NORMAL);
		wc.generateStructures(true);
		new Factions(wc.createWorld());
		
		wc = new WorldCreator("newbie");
		wc.environment(Environment.NORMAL);
		wc.generateStructures(true);
		new Newbie(wc.createWorld());
		
		wc = new WorldCreator("testtt");
		wc.environment(Environment.NORMAL);
		wc.generateStructures(true);
		wc.createWorld();
		
		wc = new WorldCreator("farm");
		wc.environment(Environment.THE_END);
		new Farm(wc.createWorld());
		
		wc = new WorldCreator("portals");
		wc.environment(Environment.THE_END);
		new Portals(wc.createWorld());
		
		wc = new WorldCreator("tutorial");
		wc.environment(Environment.THE_END);
		new WarpTutorial(wc.createWorld());
		
		wc = new WorldCreator("jackpot");
		wc.environment(Environment.NORMAL);
		new WarpJackpot(wc.createWorld());
		
		wc = new WorldCreator("oldworld");
		new WarpLand(wc.createWorld());
		
		Bukkit.getWorlds().stream().forEach(x -> x.setWeatherDuration(0));
		
		getServer().getPluginManager().registerEvents(this, this);
		getServer().getPluginManager().registerEvents(new PlayerMode(), this);
		getServer().getPluginManager().registerEvents(new RandomItem(), this);
		getServer().getPluginManager().registerEvents(new FallingBlockChecker(), this);
		getServer().getPluginManager().registerEvents(new TestListener(), this);
		
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
		
		registerCommand(new LandCMD(), "land");
		
		registerCommand(new Home(), "home");
		registerCommand(new Sethome(), "sethome");
		registerCommand(new Delhome(), "delhome");
		registerCommand(new Visit(), "visit");
	}
	
	@Override
	public void onDisable()
	{
		super.onDisable();
	}
	
	@EventHandler(priority =  EventPriority.LOWEST)
	public void a(PluginEnableEvent e)
	{
		if(e.getPlugin().getName().equals("Citizens"))
		{
			new BukkitRunnable()
			{
				@Override
				public void run()
				{
					CitizensAPI.getNPCRegistry().deregisterAll();
					Bukkit.getConsoleSender().sendMessage("deregistering all!");
				}
			}.runTaskLater(this, 100L);
		}
	}
	@EventHandler
	public void a(WorldSaveEvent e)
	{
		if(!e.getWorld().getName().equals("world"))
		{
			return;
		}
		
		LandData.save();
		Home.getInstance().save();
		Portal.getInstance().save();
		Playtime.getInstance().save();
		Price.getInstance().save();
		Jackpot.getInstance().save();
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void a(PlayerShearEntityEvent e)
	{
		Entity entity = e.getEntity();
		Rarity entityRarity = Util.getRarity(entity);
		Rarity rarity = Optional.ofNullable(entityRarity).orElse(Util.getRarity(entity.getWorld()));
		
		ItemStack item;
		
		entity.getWorld().playSound(entity.getLocation(), Sound.SHEEP_SHEAR, 1.0F, 1.0F);
		
		if(entity instanceof MushroomCow)
		{
			entity.remove();
			
			entity.getWorld().spawnEntity(entity.getLocation(), EntityType.COW);
			
			item = new ItemStack(Material.RED_MUSHROOM, 5);
			Util.setCommodity(item, rarity);
			entity.getWorld().dropItemNaturally(entity.getLocation(), item);
			
			e.setCancelled(true);
		}
		else if(entity instanceof Sheep)
		{
			((Sheep) entity).setSheared(true);
			
			Random r = new Random();
			
			item = new ItemStack(Material.WOOL, 1 + r.nextInt(3), ((Sheep) entity).getColor().getData());
			Util.setCommodity(item, rarity);
			entity.getWorld().dropItemNaturally(entity.getLocation(), item);
			
			e.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void sapling(StructureGrowEvent e)
	{
		Block b = e.getLocation().getBlock();
		BlockData data = Util.readBlock(b);
		
		if(data == null)
		{
			return;
		}
		
		e.getBlocks().forEach(x -> Util.writeBlock(x.getBlock(), data));
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void doublePlantBoneMeal(PlayerInteractEvent e)
	{
		Block clicked = e.getClickedBlock();
		
		if(clicked == null || clicked.getType() != Material.DOUBLE_PLANT)
		{
			return;
		}
		
		ItemStack item = e.getItem();
		
		if(item == null || item.getType() != Material.INK_SACK || item.getDurability() != 15 || item.getAmount() <= 0)
		{
			return;
		}
		
		Block base = clicked.getData() == 10 ? clicked.getRelative(BlockFace.DOWN) : clicked;
		
		if(base.getData() == 10 || base.getData() == 2 || base.getData() == 3)
		{
			return;
		}
		
		if(e.getPlayer().getGameMode() != GameMode.CREATIVE)
		{
			item.setAmount(item.getAmount() - 1);
			
			if(item.getAmount() <= 0)
			{
				e.getPlayer().setItemInHand(null);
			}
		}
		
		BlockData data = Util.readBlock(base);
		Rarity blockRarity = Optional.ofNullable(data == null ? null : data.getRarity()).orElse(Util.getRarity(base.getWorld()));
		Rarity itemRarity = Util.getRarity(item);
		Rarity worstRarity = Util.getWorstRarity(blockRarity, itemRarity);
		
		ItemStack drop = new ItemStack(Material.DOUBLE_PLANT, 1, base.getData());
		Util.setCommodity(drop, worstRarity);
		base.getWorld().dropItemNaturally(base.getLocation().add(0.5D, 0.5D, 0.5D), drop);
		
		((CraftPlayer) e.getPlayer()).getHandle().playerConnection.sendPacket(new PacketPlayOutWorldParticles(
				EnumParticle.VILLAGER_HAPPY, true, clicked.getX() + 0.5F, clicked.getY() + 0.5F, clicked.getZ() + 0.5F, 0.25F, 0.25F, 0.25F, 0.0F, 20, 174));
		
		e.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void breakNaturallyOnBlockPhysics(BlockPhysicsEvent e)
	{
		Block b = e.getBlock();
		Block down = b.getRelative(BlockFace.DOWN);
		Block up = b.getRelative(BlockFace.UP);
		Block attached;
		
		switch0:switch(b.getType())
		{
		case SUGAR_CANE_BLOCK:
			if(down.getType() == Material.SUGAR_CANE_BLOCK)
			{
				break;
			}
			
			if(down.getType() == Material.SAND || down.getType() == Material.DIRT || down.getType() == Material.GRASS)
			{
				BlockFace[] directions = new BlockFace[] {NORTH, SOUTH, EAST, WEST};
				
				for(BlockFace face : directions)
				{
					if(down.getRelative(face).getType() == Material.WATER || down.getRelative(face).getType() == Material.STATIONARY_WATER)
					{
						break switch0;
					}
				}
			}
			
			e.setCancelled(true);
			BlockUtil.breakNaturally(b, BreakReason.PHYSIC);
			break;
		case CACTUS:
			boolean toBreak = true;
			
			if(down.getType() == Material.CACTUS || down.getType() == Material.SAND)
			{
				toBreak = false;
				
				BlockFace[] directions = new BlockFace[] {NORTH, SOUTH, EAST, WEST};
				
				for(BlockFace face : directions)
				{
					Material type = b.getRelative(face).getType();
					
					if(type.isSolid() || type == Material.WEB)
					{
						toBreak = true;
						break;
					}
				}
			}
			
			if(!toBreak)
			{
				break;
			}
			
			e.setCancelled(true);
			BlockUtil.breakNaturally(b, BreakReason.PHYSIC);
			break;
		case RED_MUSHROOM:
		case BROWN_MUSHROOM:
			if(!down.getType().isOccluding() && !down.getType().name().contains("LEAVES")
					|| (!(down.getType() == Material.MYCEL || down.getType() == Material.DIRT && b.getData() == 2) && b.getLightLevel() > 12))
			{
				e.setCancelled(true);
				BlockUtil.breakNaturally(b, BreakReason.PHYSIC);
			}
			break;
		case SAPLING:
		case YELLOW_FLOWER:
		case RED_ROSE:
		case LONG_GRASS:
			if(down.getType() != Material.GRASS && down.getType() != Material.DIRT)
			{
				e.setCancelled(true);
				BlockUtil.breakNaturally(b, BreakReason.PHYSIC);
			}
			break;
		case DOUBLE_PLANT:
			boolean isBase = b.getData() != 10;
			
			if(isBase)
			{
				if(down.getType() != Material.GRASS && down.getType() != Material.DIRT
						|| up.getType() != b.getType() || up.getData() != 10)
				{
					e.setCancelled(true);
					BlockUtil.breakNaturally(b, BreakReason.PHYSIC);
				}
			}
			else
			{
				if(down.getType() != b.getType() || down.getData() == 10)
				{
					e.setCancelled(true);
					BlockUtil.breakNaturally(b, BreakReason.PHYSIC);
				}
			}
			
			break;
		case CARPET:
			if(down.getType() == Material.AIR)
			{
				e.setCancelled(true);
				BlockUtil.breakNaturally(b, BreakReason.PHYSIC);
			}
			break;
		case WATER_LILY:
			if(down.getType() != Material.STATIONARY_WATER || down.getData() != 0)
			{
				e.setCancelled(true);
				BlockUtil.breakNaturally(b, BreakReason.PHYSIC);
			}
			break;
		case WALL_BANNER:
		case WALL_SIGN:
			Attachable attachable = (Attachable) b.getState().getData();
			attached = b.getRelative(attachable.getAttachedFace());
			
			if(!attached.getType().isSolid())
			{
				e.setCancelled(true);
				BlockUtil.breakNaturally(b, BreakReason.PHYSIC);
			}
			break;
		case TRAP_DOOR:
		case IRON_TRAPDOOR:
			TrapDoor trapDoor = (TrapDoor) b.getState().getData();
			attached = b.getRelative(trapDoor.getAttachedFace());
			
			boolean isInverted;
			
			if(attached.getState().getData() instanceof Step)
			{
				isInverted = ((Step) attached.getState().getData()).isInverted();
			}
			else if(attached.getState().getData() instanceof WoodenStep)
			{
				isInverted = ((WoodenStep) attached.getState().getData()).isInverted();
			}
			else if(attached.getType() == Material.STONE_SLAB2)
			{
				isInverted = attached.getData() == 0 ? false : true;
			}
			else if(!attached.getType().isOccluding() && !(attached.getState().getData() instanceof Stairs))
			{
				e.setCancelled(true);
				BlockUtil.breakNaturally(b, BreakReason.PHYSIC);
				break;
			}
			else
			{
				break;
			}
			
			if(isInverted != trapDoor.isInverted())
			{
				e.setCancelled(true);
				BlockUtil.breakNaturally(b, BreakReason.PHYSIC);
			}
			break;
		case TORCH:
		case REDSTONE_TORCH_OFF:
		case REDSTONE_TORCH_ON:
		case LADDER:
		case TRIPWIRE:
		case STONE_BUTTON:
		case WOOD_BUTTON:
		case LEVER:
			Directional directional = (Directional) b.getState().getData();
			Block relative = b.getRelative(directional.getFacing().getOppositeFace());
			
			if(!relative.getType().isSolid() || !relative.getType().isOccluding())
			{
				e.setCancelled(true);
				BlockUtil.breakNaturally(b, BreakReason.PHYSIC);
			}
			break;
		case SIGN_POST:
		case STANDING_BANNER:
			if(!down.getType().isSolid())
			{
				e.setCancelled(true);
				BlockUtil.breakNaturally(b, BreakReason.PHYSIC);
			}
			break;
		case FLOWER_POT:
		case WOOD_PLATE:
		case STONE_PLATE:
		case IRON_PLATE:
		case GOLD_PLATE:
		case REDSTONE_WIRE:
		case DIODE_BLOCK_ON:
		case DIODE_BLOCK_OFF:
		case REDSTONE_COMPARATOR_OFF:
		case REDSTONE_COMPARATOR_ON:
			if(!down.getType().isOccluding() || !down.getType().isSolid())
			{
				e.setCancelled(true);
				BlockUtil.breakNaturally(b, BreakReason.PHYSIC);
			}
			break;
		case RAILS:
		case ACTIVATOR_RAIL:
		case DETECTOR_RAIL:
		case POWERED_RAIL:
			Rails rails = (Rails) b.getState().getData();
			
			if(!down.getType().isOccluding()
					|| (rails.isOnSlope() && !b.getRelative(rails.getDirection()).getType().isOccluding()))
			{
				e.setCancelled(true);
				BlockUtil.breakNaturally(b, BreakReason.PHYSIC);
			}
			break;
		case ACACIA_DOOR:
		case BIRCH_DOOR:
		case DARK_OAK_DOOR:
		case IRON_DOOR_BLOCK:
		case JUNGLE_DOOR:
		case SPRUCE_DOOR:
		case WOODEN_DOOR:
			Door door = (Door) b.getState().getData();
			
			if(door.isTopHalf())
			{
				if((down.getType() != b.getType() || ((Door) down.getState().getData()).isTopHalf()))
				{
					e.setCancelled(true);
					BlockUtil.breakNaturally(b, BreakReason.PHYSIC);
				}
			}
			else
			{
				if(!down.getType().isOccluding() || up.getType() != b.getType() || !((Door) up.getState().getData()).isTopHalf())
				{
					e.setCancelled(true);
					BlockUtil.breakNaturally(b, BreakReason.PHYSIC);
				}
			}
			break;
		case BED_BLOCK:
			Bed bed = (Bed) b.getState().getData();
			relative = b.getRelative(bed.isHeadOfBed() ? bed.getFacing().getOppositeFace() : bed.getFacing());
			
			if(relative.getType() != Material.BED_BLOCK)
			{
				e.setCancelled(true);
				BlockUtil.breakNaturally(b, BreakReason.PHYSIC);
			}
			break;
		default:
			break;
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void setRarityOnVillagerTrade(InventoryClickEvent e)
	{
		if(e.getInventory().getType() != InventoryType.MERCHANT)
		{
			return;
		}
		
		if(e.getAction() == InventoryAction.NOTHING)
		{
			return;
		}
		
		MerchantInventory inv = (MerchantInventory) e.getInventory();
		
		ItemStack result = inv.getItem(2);
		
		if(result == null)
		{
			return;
		}
		
		Entity entity = (Entity) inv.getHolder();
		ItemStack left = inv.getItem(0);
		ItemStack right = inv.getItem(1);
		
		
		List<Rarity> list = new ArrayList<>();
		
		Rarity entityRarity = Optional.ofNullable(Rarity.valueOfToString(entity.getCustomName())).orElse(Util.getRarity(entity.getWorld()));
		Rarity leftRarity = left == null ? null : Optional.ofNullable(Util.getRarity(left)).orElse(Rarity.COMMON);
		Rarity rightRarity = right == null ? null : Optional.ofNullable(Util.getRarity(right)).orElse(Rarity.COMMON);
		
		list.add(entityRarity);
		list.add(leftRarity);
		list.add(rightRarity);
		list.removeIf(x -> x == null);
		Rarity worstRarity = Util.getWorstRarity(list.stream().toArray(x -> new Rarity[x]));
		
		Util.setCommodity(result, worstRarity);
		inv.setItem(2, result);
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
	public void a(BlockPistonExtendEvent e)
	{
		List<BlockData> list = new ArrayList<>();
		
		for(int i = 0; i < e.getBlocks().size(); i++)
		{
			Block b = e.getBlocks().get(i);
			
			BlockData data = Util.readBlock(b);
			
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
	

//	@EventHandler
//	public void a(BlockPhysicsEvent e)
//	{
//		Block b = e.getBlock();
//		World w = b.getWorld();
//		
//		if(b.getType() == Material.SUGAR_CANE_BLOCK)
//		{
//			e.setCancelled(true);
//			
//			Block down = b.getRelative(BlockFace.DOWN);
//			
//			if(down.getType() == Material.SUGAR_CANE_BLOCK)
//			{
//				return;
//			}
//			
//			if(down.getType() == Material.SAND || down.getType() == Material.DIRT || down.getType() == Material.GRASS)
//			{
//				BlockFace[] directions = new BlockFace[] {NORTH, SOUTH, EAST, WEST};
//				
//				for(BlockFace face : directions)
//				{
//					if(down.getRelative(face).getType() == Material.WATER || down.getRelative(face).getType() == Material.STATIONARY_WATER)
//					{
//						return;
//					}
//				}
//			}
//			
//			b.setType(Material.AIR, true);
//			
//			ItemStack item = new ItemStack(Material.SUGAR_CANE, 1);
//			ItemMeta meta = item.getItemMeta();
//			meta.setLore(Util.getLore(b));
//			item.setItemMeta(meta);
//			
//			Util.writeBlock(b, null);
//			
//			w.dropItemNaturally(b.getLocation(), item);
//		}
//		else if(b.getType() == Material.CACTUS)
//		{
//			e.setCancelled(true);
//		
//			Block down = b.getRelative(BlockFace.DOWN);
//			
//			boolean toBreak = true;
//			
//			if(down.getType() == Material.CACTUS || down.getType() == Material.SAND)
//			{
//				toBreak = false;
//				
//				BlockFace[] directions = new BlockFace[] {NORTH, SOUTH, EAST, WEST};
//				
//				for(BlockFace face : directions)
//				{
//					if(b.getRelative(face).getType().isSolid())
//					{
//						toBreak = true;
//						break;
//					}
//				}
//			}
//			
//			if(!toBreak)
//			{
//				return;
//			}
//			
//			b.setType(Material.AIR, true);
//			
//			ItemStack item = new ItemStack(Material.CACTUS, 1);
//			ItemMeta meta = item.getItemMeta();
//			meta.setLore(Util.getLore(b));
//			item.setItemMeta(meta);
//			
//			Util.writeBlock(b, null);
//			
//			w.dropItemNaturally(b.getLocation(), item);
//		}
//	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
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
		
		BlockData data = Util.readBlock(source);
		
		Util.writeBlock(newState.getBlock(), data);
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void a(PlayerBucketFillEvent e)
	{
		Player p = e.getPlayer();
		Block b = e.getBlockClicked();
		
		if(b == null)
		{
			return;
		}
		
		ItemStack itemInHand = p.getItemInHand();
		ItemStack item = e.getItemStack();
		
		BlockData data = Util.readBlock(b);
		
		Rarity blockRarity = Optional.ofNullable(data == null ? null : data.getRarity()).orElse(Util.getRarity(b.getWorld()));
		Rarity itemRarity = Optional.ofNullable(Util.getRarity(itemInHand)).orElse(Rarity.COMMON);
		Rarity worstRarity = Util.getWorstRarity(blockRarity, itemRarity);
		
		Util.writeBlock(b, null);
		Util.setCommodity(item, worstRarity);
		
		e.setItemStack(item);
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void a(PlayerBucketEmptyEvent e)
	{
		Player p = e.getPlayer();
		Block b = e.getBlockClicked();
		Block relative = b.getRelative(e.getBlockFace());
		
		ItemStack itemInHand = p.getItemInHand();
		ItemStack item = e.getItemStack();
		
		BlockData data = new BlockData();
		
		Rarity itemRarity = Util.getRarity(itemInHand);
		
		data.setRarity(itemRarity);
		data.setProperties(Util.getProperties(itemInHand));
		
		Util.writeBlock(relative, data);
		
		Util.setCommodity(item, itemRarity);
		
		e.setItemStack(item);
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
	public void setRarityChestLoot(InventoryMoveItemEvent e)
	{
		Inventory destination = e.getDestination();
		
		if(destination.getHolder() instanceof ContainerBlock)
		{
			ContainerBlock container = (ContainerBlock) destination.getHolder();
			
			BlockData data = Util.readBlock(((BlockState) container).getBlock());
			
			if(data != null)
			{
				return;
			}
			
			setChestLootCommodity(container);
		}
	}
	
	private void setChestLootCommodity(ContainerBlock container)
	{
		Block b = ((BlockState) container).getBlock();
		Rarity rarity = Util.getRarity(b.getWorld());
		BlockData data = new BlockData();
		data.setRarity(rarity);
		Util.writeBlock(b, data);
		
		for(ItemStack item : container.getInventory())
		{
			if(item != null && Util.getRarity(item) == null)
			{
				Util.setCommodity(item, rarity);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void setRarityChestLoot(PlayerInteractEvent e)
	{
		Block b = e.getClickedBlock();
		
		if(b == null || !(b.getState() instanceof ContainerBlock))
		{
			return;
		}
		
		BlockData data = Util.readBlock(b);
		
		if(data != null)
		{
			return;
		}
		
		setChestLootCommodity((ContainerBlock) b.getState());
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
		BlockData data = Util.readBlock(e.getSpawner().getBlock());
		entity.setCustomName(((data == null ? null : data.getRarity()) == null
				? Util.getRarity(entity.getWorld()) : data.getRarity()).toString());
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
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void a(WeatherChangeEvent e)
	{
		e.setCancelled(false);
//		e.setCancelled(e.getWorld().getWeatherDuration() <= 0);
	}
	
	@EventHandler
	public void a(BlockPistonRetractEvent e)
	{
		List<BlockData> list = new ArrayList<>();
		
		for(int i = 0; i < e.getBlocks().size(); i++)
		{
			Block b = e.getBlocks().get(i);
			
			BlockData data = Util.readBlock(b);
			
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
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void a(EntityChangeBlockEvent e)
	{
		Block b = e.getBlock();
		Entity entity = e.getEntity();
		
		if(entity.getType() != EntityType.FALLING_BLOCK)
		{
			return;
		}
		
		FallingBlock fb = (FallingBlock) entity;
		fb.setDropItem(false);
		
		if(e.getTo() == Material.AIR)
		{
			BlockData data = Util.readBlock(b);
			entity.setMetadata("data", new FixedMetadataValue(this, data));
			
			Util.writeBlock(b, null);
		}
		else
		{
			if(entity.hasMetadata("data"))
			{
				BlockData data = (BlockData) entity.getMetadata("data").get(0).value();
				Util.writeBlock(b, data);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void a(FallingBlockObstructEvent e)
	{
		FallingBlock fb = e.getFallingBlock();
		
		if(fb.getDropItem())
		{
			return;
		}
		
		if(fb.hasMetadata("data"))
		{
			BlockData data = (BlockData) fb.getMetadata("data").get(0).value();
			
			ItemStack item = new ItemStack(fb.getBlockId(), 1, fb.getBlockData());
			ItemMeta meta = item.getItemMeta();
			meta.setLore(Util.getLore(data, fb.getWorld()));
			item.setItemMeta(meta);
			
			fb.getWorld().dropItem(fb.getLocation(), item);
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void ab(EntityExplodeEvent e)
	{
		int power;
		
		Entity entity = e.getEntity();
		
		if(entity instanceof Creeper)
		{
			power = ((Creeper) entity).isPowered() ? 6 : 3;
		}
		else if(entity instanceof TNTPrimed)
		{
			power = 4;
		}
		else if(entity instanceof EnderCrystal)
		{
			power = 6;
		}
		else if(entity instanceof Wither)
		{
			power = 8;
		}
		else
		{
			power = 1;
		}
		
		Random r = new Random();
		
		for(Block b : e.blockList())
		{
			if(r.nextInt(power) == 0)
			{
				BlockUtil.breakNaturally(b, BreakReason.EXPLOSION);
			}
			else
			{
				b.setType(Material.AIR);
			}
		}
		
		e.blockList().clear();
	}
	
	private void updateFurnace(Furnace furnace)
	{
		FurnaceInventory inv = furnace.getInventory();
		
		if(furnace.hasMetadata("task") || furnace.getBurnTime() <= 0)
		{
			return;
		}
		
		furnace.setMetadata("task", new FixedMetadataValue(this, new BukkitRunnable()
		{
			@Override
			public void run()
			{
				if(furnace.getBurnTime() > 0)
				{
					ItemStack smelting = inv.getSmelting();
					ItemStack result = inv.getResult();
					
					if(smelting == null || result == null)
					{
						return;
					}
					
					if(Optional.ofNullable(Util.getRarity(smelting)).orElse(Rarity.COMMON) != Util.getRarity(result))
					{
						furnace.setCookTime((short) 0);
						furnace.update();
					}
				}
				else
				{
					cancel();
					furnace.removeMetadata("task", Main.this);
					furnace.update();
				}
			}
		}.runTaskTimer(this, 0L, 1L).getTaskId()));
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void monitor(FurnaceBurnEvent e)
	{
		updateFurnace((Furnace) e.getBlock().getState());
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void highest(FurnaceBurnEvent e)
	{
		Furnace f = (Furnace) e.getBlock().getState();
		FurnaceInventory inv = f.getInventory();
		
		ItemStack smelting = inv.getSmelting();
		ItemStack result = inv.getResult();
		
		if(Optional.ofNullable(Util.getRarity(smelting)).orElse(Rarity.COMMON) != Util.getRarity(result))
		{
			e.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void ab(InventoryOpenEvent e)
	{
		if(e.getInventory().getType() != InventoryType.FURNACE)
		{
			return;
		}
		
		updateFurnace((Furnace) e.getInventory().getHolder());
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void ab(FurnaceSmeltEvent e)
	{
		Rarity rarity = Optional.ofNullable(Util.getRarity(e.getSource())).orElse(Rarity.COMMON);
		ItemStack result = e.getResult();
		
		Util.setCommodity(result, rarity);
		e.setResult(result);
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void ab(PlayerFishEvent e)
	{
		Player p = e.getPlayer();
		
		Rarity rarity = Util.getWorstRarity(Optional.ofNullable(Util.getRarity(p.getItemInHand())).orElse(Rarity.COMMON), 
				Util.getRarity(p.getWorld()));
		
		Item item = (Item) e.getCaught();
		ItemStack itemStack = item.getItemStack();
		Util.setCommodity(itemStack, rarity);
		item.setItemStack(itemStack);
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void asdad(ChunkLoadEvent e)
	{
		if(!e.isNewChunk())
		{
			return;
		}
		
		Bukkit.getScheduler().runTask(this, () ->
		{
			Rarity rarity = Util.getRarity(e.getWorld());
			
			for(Entity entity : e.getChunk().getEntities())
			{
				if(entity.getType() == EntityType.MINECART_CHEST)
				{
					for(ItemStack item : ((StorageMinecart) entity).getInventory())
					{
						if(item != null && Util.getRarity(item) == null)
						{
							Util.setCommodity(item, rarity);
						}
					}
				}
			}
		});
	}
	
//	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
//	public void setRarityStorageMinecart(PlayerInteractEntityEvent e)
//	{
//		Entity rightClicked = e.getRightClicked();
//		
//		if(rightClicked instanceof Player || !(rightClicked instanceof StorageMinecart))
//		{
//			return;
//		}
//		
//		Rarity rarity = Util.getRarity(rightClicked);
//		
//		if(rarity != null)
//		{
//			return;
//		}
//		
//		rarity = Util.getRarity(rightClicked.getWorld());
//		rightClicked.setCustomName(getName());
//		
//	for(ItemStack item : ((StorageMinecart) rightClicked).getInventory())
//	{
//		if(item != null && Util.getRarity(item) == null)
//		{
//			Util.setCommodity(item, rarity);
//		}
//	}
//	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void ab(BlockFromToEvent e)
	{
		if(e.getBlock().getType().name().contains("LAVA") || e.getBlock().getType() == Material.DRAGON_EGG)
		{
			return;
		}
		
		Material type = e.getToBlock().getType();
		
		if(type != Material.AIR && type != Material.WATER && type != Material.STATIONARY_WATER)
		{
			BlockUtil.breakNaturally(e.getToBlock(), BreakReason.LIQUID);
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void ab(BlockPistonExtendEvent e)
	{
		for(Block b : e.getBlocks())
		{
			if(b.getPistonMoveReaction() == PistonMoveReaction.BREAK)
			{
				BlockUtil.breakNaturally(b, BreakReason.LEAVES_DECAY);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void a(LeavesDecayEvent e)
	{
		BlockUtil.breakNaturally(e.getBlock(), BreakReason.LEAVES_DECAY);
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
		
		BlockUtil.breakNaturally(b, tool);
		
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
		
		if(tool.getType().getMaxDurability() == 0)
		{
			return;
		}
		
		if(r.nextDouble() <= (1.0D / (tool.getEnchantmentLevel(Enchantment.DURABILITY) + 1.0D)))
		{
			tool.setDurability((short) (tool.getDurability() + damage));
			
			if(tool.getDurability() > tool.getType().getMaxDurability())
			{
				p.playSound(p.getLocation(), Sound.ITEM_BREAK, 1.0F, 1.0F);
				p.setItemInHand(null);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void abc(PlayerInteractEvent e)
	{
		Block clickedBlock = e.getClickedBlock();
		
		if(clickedBlock != null && clickedBlock.getType() == Material.ANVIL && e.getAction().name().contains("RIGHT"))
		{
			BlockData data = Util.readBlock(clickedBlock);
			Rarity rarity = Optional.ofNullable(data == null ? null : data.getRarity()).orElse(Rarity.COMMON);
			Player p = e.getPlayer();
			CommonPlayer cp = CraftCommonPlayer.get(p);
			
			e.setCancelled(true);
			
			new CustomAnvil(cp, rarity, clickedBlock);
		}
	}
	
	@EventHandler
	public void a(PlayerInteractEvent e)
	{
		Block b = e.getClickedBlock();
		
		if(b == null)
		{
			return;
		}
		
		BlockData data = Util.readBlock(b);
		
		if(e.getPlayer().isSneaking())
		{
			Bukkit.broadcastMessage(data + "");
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void mushroom(PlayerInteractEntityEvent e)
	{
		Entity clicked = e.getRightClicked();
		
		Player p = e.getPlayer();
		ItemStack inHand = p.getItemInHand();
		
		if(inHand.getType() != Material.BOWL && inHand.getType() != Material.BUCKET)
		{
			return;
		}
		
		ItemStack result = new ItemStack(inHand.getType() == Material.BOWL ? Material.MUSHROOM_SOUP : Material.MILK_BUCKET);
		
		if(!(clicked instanceof MushroomCow) && (!(clicked instanceof Cow) || inHand.getType() != Material.BUCKET))
		{
			return;
		}
		
		if(inHand.getType() != Material.BOWL || inHand.getAmount() <= 0 || inHand.getAmount() != 1 && p.getInventory().firstEmpty() == -1)
		{
			return;
		}
		
		Rarity entityRarity = Optional.ofNullable(Util.getRarity(clicked)).orElse(Util.getRarity(clicked.getWorld()));
		Rarity itemRarity = Optional.ofNullable(Util.getRarity(inHand)).orElse(Rarity.COMMON);
		Rarity worst = Util.getWorstRarity(entityRarity, itemRarity);
		
		Util.setCommodity(result, worst);
		
		if(inHand.getAmount() == 1)
		{
			p.setItemInHand(result);
		}
		else if(p.getInventory().addItem(result).isEmpty())
		{
			inHand.setAmount(inHand.getAmount() - 1);
			p.setItemInHand(inHand);
		}
		
		e.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void a(BlockSpreadEvent e)
	{
		Block source = e.getSource();
		Block b = e.getBlock();
		
		Util.writeBlock(b, Util.readBlock(source));
	}
	
	@EventHandler
	public void a(BlockBurnEvent e)
	{
		e.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void a(BlockPlaceEvent e)
	{
		Block b = e.getBlock();
		
//		if(b.getType() == Material.LEAVES_2)
//		{
//			b.setData((byte) (e.getItemInHand().getData().getData() + 4));
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