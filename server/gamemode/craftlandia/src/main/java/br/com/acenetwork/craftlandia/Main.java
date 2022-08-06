package br.com.acenetwork.craftlandia;

import static org.bukkit.block.BlockFace.EAST;
import static org.bukkit.block.BlockFace.NORTH;
import static org.bukkit.block.BlockFace.SOUTH;
import static org.bukkit.block.BlockFace.WEST;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Optional;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.ContainerBlock;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.block.Furnace;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.MushroomCow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Slime;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.entity.Wither;
import org.bukkit.entity.minecart.StorageMinecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
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
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.event.world.WorldSaveEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.FurnaceInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantInventory;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Attachable;
import org.bukkit.material.Bed;
import org.bukkit.material.Directional;
import org.bukkit.material.Door;
import org.bukkit.material.Rails;
import org.bukkit.material.Sign;
import org.bukkit.material.Stairs;
import org.bukkit.material.Step;
import org.bukkit.material.TrapDoor;
import org.bukkit.material.WoodenStep;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;

import br.com.acenetwork.commons.Common;
import br.com.acenetwork.commons.CommonsUtil;
import br.com.acenetwork.commons.constants.Tag;
import br.com.acenetwork.commons.event.MagnataChangeEvent;
import br.com.acenetwork.commons.event.PlayerSuccessLoginEvent;
import br.com.acenetwork.commons.executor.Permission;
import br.com.acenetwork.commons.executor.VipChest;
import br.com.acenetwork.commons.manager.Message;
import br.com.acenetwork.commons.player.CommonPlayer;
import br.com.acenetwork.commons.player.craft.CraftCommonPlayer;
import br.com.acenetwork.craftlandia.event.BreakNaturallyEvent;
import br.com.acenetwork.craftlandia.event.FallingBlockObstructEvent;
import br.com.acenetwork.craftlandia.event.NPCLoadEvent;
import br.com.acenetwork.craftlandia.executor.Delhome;
import br.com.acenetwork.craftlandia.executor.Give;
import br.com.acenetwork.craftlandia.executor.Home;
import br.com.acenetwork.craftlandia.executor.Jackpot;
import br.com.acenetwork.craftlandia.executor.LandCMD;
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
import br.com.acenetwork.craftlandia.executor.WorldCMD;
import br.com.acenetwork.craftlandia.inventory.CustomAnvil;
import br.com.acenetwork.craftlandia.item.CommonRandomItem;
import br.com.acenetwork.craftlandia.item.ContainmentPickaxe;
import br.com.acenetwork.craftlandia.listener.FallingBlockChecker;
import br.com.acenetwork.craftlandia.listener.PlayerMode;
import br.com.acenetwork.craftlandia.listener.TestListener;
import br.com.acenetwork.craftlandia.manager.BlockData;
import br.com.acenetwork.craftlandia.manager.BreakReason;
import br.com.acenetwork.craftlandia.manager.ItemSpecial;
import br.com.acenetwork.craftlandia.manager.LandData;
import br.com.acenetwork.craftlandia.manager.PlayerData;
import br.com.acenetwork.craftlandia.warp.Factions;
import br.com.acenetwork.craftlandia.warp.FactionsNether;
import br.com.acenetwork.craftlandia.warp.FactionsTheEnd;
import br.com.acenetwork.craftlandia.warp.Farm;
import br.com.acenetwork.craftlandia.warp.Newbie;
import br.com.acenetwork.craftlandia.warp.NewbieNether;
import br.com.acenetwork.craftlandia.warp.NewbieTheEnd;
import br.com.acenetwork.craftlandia.warp.Portals;
import br.com.acenetwork.craftlandia.warp.Warp;
import br.com.acenetwork.craftlandia.warp.WarpJackpot;
import br.com.acenetwork.craftlandia.warp.WarpLand;
import br.com.acenetwork.craftlandia.warp.WarpTutorial;
import net.citizensnpcs.api.CitizensAPI;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagList;
import net.minecraft.server.v1_8_R3.NBTTagString;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import net.minecraft.server.v1_8_R3.StatisticList;

public class Main extends Common
{
	private static Main instance;
	
	@Override
	public void onEnable()
	{
		instance = this;
		
		try
		{
			WorldCreator wc;
			
			wc = new WorldCreator("world");
			wc.environment(Environment.NORMAL);
			wc.generateStructures(true);
			new Factions(wc.createWorld());
			
			wc = new WorldCreator("world_nether");
			wc.environment(Environment.NETHER);
			wc.generateStructures(true);
			new FactionsNether(wc.createWorld());
			
			wc = new WorldCreator("world_the_end");
			wc.environment(Environment.THE_END);
			wc.generateStructures(true);
			new FactionsTheEnd(wc.createWorld());
			
			wc = new WorldCreator("newbie");
			wc.environment(Environment.NORMAL);
			wc.generateStructures(true);
			new Newbie(wc.createWorld());
			
			wc = new WorldCreator("newbie_nether");
			wc.environment(Environment.NETHER);
			wc.generateStructures(true);
			new NewbieNether(wc.createWorld());
			
			wc = new WorldCreator("newbie_the_end");
			wc.environment(Environment.THE_END);
			wc.generateStructures(true);
			new NewbieTheEnd(wc.createWorld());
			
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
			
			wc = new WorldCreator("land");
			new WarpLand(wc.createWorld());
			
			registerCommand(new Give(), "give");
			
			super.onEnable();
			
			getServer().getPluginManager().registerEvents(new PlayerMode(), this);
			getServer().getPluginManager().registerEvents(new FallingBlockChecker(), this);
			getServer().getPluginManager().registerEvents(new TestListener(), this);
			
			ItemSpecial.load();
			
			registerCommand(new Temp(), "temp");
			
//			registerCommand(new ItemInfo(), "iteminfo");
			registerCommand(new Jackpot(), "jackpot");
//			registerCommand(new Playtime(), "playtime");
			registerCommand(new Portal(), "portal");
			registerCommand(new Price(), "price");
			registerCommand(new Spawn(), "spawn");
			registerCommand(new Sell(), "sell");
			registerCommand(new Sellall(), "sellall");
			registerCommand(new Shop(), "shop");
			registerCommand(new ShopSearch(), "shopsearch");
			registerCommand(new WorldCMD(), "world", "wrld");
			
			registerCommand(new LandCMD(), "land");
			
			registerCommand(new Home(), "home");
			registerCommand(new Sethome(), "sethome");
			registerCommand(new Delhome(), "delhome");
			registerCommand(new Visit(), "visit");
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			Bukkit.shutdown();
		}
	}
	
	@Override
	public void onDisable()
	{
		super.onDisable();
	}
	
	public static void open(Player p, net.minecraft.server.v1_8_R3.ItemStack book, boolean addStats)
	{
		EntityHuman player = ((CraftHumanEntity) p).getHandle();
		ItemStack hand = p.getItemInHand();
		
		try
		{
			p.setItemInHand(CraftItemStack.asBukkitCopy(book));
			player.openBook(book);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			p.setItemInHand(hand);
		}
		
		if(addStats)
		{
			player.b(StatisticList.USE_ITEM_COUNT[387]);
		}
    }
	
	public static net.minecraft.server.v1_8_R3.ItemStack createBook(String author, String name, List<String> list)
	{
		net.minecraft.server.v1_8_R3.ItemStack book = new net.minecraft.server.v1_8_R3.ItemStack(net.minecraft.server.v1_8_R3.Item.getById(387));
		NBTTagCompound tag = new NBTTagCompound();
		tag.setString("author", author);
		tag.setString("title", name);
		NBTTagList pages = new NBTTagList();
		
		for(String page : list)
		{
			pages.add(new NBTTagString(page));
		}
		
		tag.set("pages", pages);
		book.setTag(tag);
		return book;
	}
	
	@EventHandler
	public void a(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		
		Block b = e.getClickedBlock();
		
		if(b != null && b.getState() instanceof org.bukkit.block.Sign && !p.isSneaking())
		{
			org.bukkit.block.Sign sign = (org.bukkit.block.Sign) b.getState();
			
			String regex = "(https?:\\/\\/(?:www\\.|(?!www))[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\\.[^\\s]{2,}|www\\.[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\\.[^\\s]{2,}|https?:\\/\\/(?:www\\.|(?!www))[a-zA-Z0-9]+\\.[^\\s]{2,}|www\\.[a-zA-Z0-9]+\\.[^\\s]{2,})";
			Pattern pattern = Pattern.compile(regex);
			
			List<String> links = new ArrayList<>();
			
			for(String line : sign.getLines())
			{
				Matcher matcher = pattern.matcher(line);
				
				while(matcher.find())
				{
					String group = matcher.group();
					
					if(!group.startsWith("http://") && !group.startsWith("https://"))
					{
						group = "http://" + group;
					}
					
					TextComponent text = new TextComponent(group);
					text.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, group));
					text.setColor(ChatColor.DARK_BLUE);
					text.setUnderlined(true);
					links.add(text.toLegacyText());
				}
			}
			
			if(!links.isEmpty())
			{
				open(p, createBook(p.getName(), "test", links), true);
			}
		}
	}
	
	@EventHandler
	public void b(SignChangeEvent e)
	{
		String[] lines = e.getLines();
		
		String regex = "(https?:\\/\\/(?:www\\.|(?!www))[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\\.[^\\s]{2,}|www\\.[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\\.[^\\s]{2,}|https?:\\/\\/(?:www\\.|(?!www))[a-zA-Z0-9]+\\.[^\\s]{2,}|www\\.[a-zA-Z0-9]+\\.[^\\s]{2,})";
		Pattern pattern = Pattern.compile(regex);
		String colorCode = "" + ChatColor.DARK_BLUE + ChatColor.UNDERLINE;
		String resetCode = "" + ChatColor.RESET;
		
		for(int i = 0; i < lines.length; i++)
		{
			String oldLine = lines[i];
			Matcher matcher = pattern.matcher(oldLine);
			
			String newLine = "";
			
			int end = 0;
			
			while(matcher.find())
			{
				int start = matcher.start();
				newLine += oldLine.substring(end, start) + colorCode;
				end = matcher.end();
				newLine += oldLine.substring(start, end) + resetCode;
			}
			
			newLine += oldLine.substring(end, oldLine.length());
			
			e.setLine(i, newLine);
		}
	}
	
	@EventHandler
	public void a(PlayerQuitEvent e)
	{
		Player p = e.getPlayer();
		CommonPlayer cp = CraftCommonPlayer.get(p);
		
		if(!cp.isLogged())
		{
			return;
		}
		
		PlayerData data = PlayerData.load(p.getUniqueId());
		
		data.setLastLocation(p.getLocation());
	}
	
	@EventHandler
	public void a(PlayerSuccessLoginEvent e)
	{
		Player p = e.getPlayer();
		PlayerData data = PlayerData.load(p.getUniqueId());
		
		Location last = data.getLastLocation();
		
		if(last == null)
		{
			WarpTutorial tutorial = Warp.getInstance(WarpTutorial.class);
			p.teleport(tutorial.getSpawnLocation());
		}
		else
		{
			p.teleport(last);
		}
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void a(PlayerJoinEvent e)
	{
		Player p = e.getPlayer();
		CommonPlayer cp = CraftCommonPlayer.get(p);
		
		if(cp.isLogged())
		{
			return;
		}
		
		Warp warp = Warp.getByWorld(p.getWorld());
		
		Location l = Optional.ofNullable(warp.getSpawnLocation()).orElse(new Location(p.getWorld(), 0.5D, 69.0D, 0.5D, 0.0F, 0.0F));
		p.teleport(l);
	}
	
	@EventHandler
	public void a(BreakNaturallyEvent e)
	{
		Block b = e.getBlock();
		
		if(b.getType() != Material.BED_BLOCK)
		{
			return;
		}
		
		BlockData data = Util.readBlock(b);
		
		if(data == null || data.getBed() == null)
		{
			return;
		}
		
		for(UUID uuid : data.getBed())
		{
			PlayerData pd = PlayerData.load(uuid);
			
			pd.getBedHomes().remove(b.getWorld().getUID(), new int[] {b.getX(), b.getY(), b.getZ()});
		}
		
		data.setBed(null);
	}
	
	@EventHandler(priority =  EventPriority.LOWEST)
	public void a(PluginEnableEvent e)
	{
		if(e.getPlugin().getName().equals("Citizens"))
		{
			new BukkitRunnable()
			{
				private int ticks = 0;
				
				@Override
				public void run()
				{
					if(ticks > 60 * 20)
					{
						Bukkit.getPluginManager().callEvent(new NPCLoadEvent());
						cancel();
						return;
					}
					
					if(CitizensAPI.getNPCRegistry().iterator().hasNext())
					{
						CitizensAPI.getNPCRegistry().deregisterAll();
						Bukkit.getPluginManager().callEvent(new NPCLoadEvent());
						cancel();
						return;
					}
					
					ticks++;
				}
			}.runTaskTimer(this, 0L, 1L);
		}
	}
	
	@EventHandler
	public void a(MagnataChangeEvent e)
	{
		OfflinePlayer oldMagnata = e.getOldMagnata();
		OfflinePlayer newMagnata = e.getNewMagnata();
		
		if(oldMagnata != null)
		{
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "perm user " + oldMagnata.getName() + " remove " + Tag.MAGNATA.getPermission());
			
			if(oldMagnata.isOnline())
			{
				Player p = oldMagnata.getPlayer();
				CommonPlayer cp = CraftCommonPlayer.get(p);
				
				if(cp.getTag() == Tag.MAGNATA)
				{
					cp.setTag(cp.getBestTag());
				}
			}
		}
		
		if(newMagnata != null)
		{
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "perm user " + newMagnata.getName() + " add " + Tag.MAGNATA.getPermission());
			
			if(newMagnata.isOnline())
			{
				Player p = newMagnata.getPlayer();
				CommonPlayer cp = CraftCommonPlayer.get(p);
				
				cp.setTag(Tag.MAGNATA);
			}
		}
		
		String displayName = newMagnata.isOnline() ? newMagnata.getPlayer().getDisplayName() : ChatColor.GRAY + newMagnata.getName();
		
		ResourceBundle bundle;
		
		TextComponent[] extra = new TextComponent[2];
		extra[0] = new TextComponent(displayName);
		
		for(CommonPlayer cp : CraftCommonPlayer.SET)
		{
			Player p = cp.getPlayer();
			bundle = ResourceBundle.getBundle("message", cp.getLocale());
			
			extra[1] = new TextComponent(StringUtils.capitalize(bundle.getString("noun.magnata")));
			extra[1].setColor(ChatColor.DARK_GREEN);
			
			TextComponent text = Message.getTextComponent(bundle.getString("broadcast.magnata-change"), extra);
			text.setColor(ChatColor.GREEN);
			
			p.sendMessage("");
			p.spigot().sendMessage(text);;
			p.sendMessage("");
		}
		
		bundle = ResourceBundle.getBundle("message");
		extra[1] = new TextComponent(StringUtils.capitalize(bundle.getString("noun.magnata")));
		extra[1].setColor(ChatColor.DARK_GREEN);
		
		TextComponent text = Message.getTextComponent(bundle.getString("broadcast.magnata-change"), extra);
		text.setColor(ChatColor.GREEN);
		Bukkit.getConsoleSender().sendMessage(text.toLegacyText());
	}
	
	@EventHandler
	public void a(WorldSaveEvent e)
	{
		if(!e.getWorld().getName().equals("world"))
		{
			return;
		}
		
		for(Player p : Bukkit.getOnlinePlayers())
		{
			PlayerData data = PlayerData.load(p.getUniqueId());
			
			data.setLastLocation(p.getLocation());
		}
		
		PlayerData.save();
		LandData.save();
		Permission.getInstance().save();
		VipChest.getInstance().save();
		Home.getInstance().save();
		Portal.getInstance().save();
//		Playtime.getInstance().save();
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
	
	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void breakNaturallyOnBlockPhysics(BlockPhysicsEvent e)
	{
		Block b = e.getBlock();
		Block down = b.getRelative(BlockFace.DOWN);
		Block up = b.getRelative(BlockFace.UP);
		Block attached;
		
		switch0:switch(b.getType())
		{
		case DIRT:
		case SAND:
		case GRASS:
			if(up.getType() == Material.SUGAR_CANE_BLOCK)
			{
				Bukkit.getPluginManager().callEvent(new BlockPhysicsEvent(up, b.getTypeId()));
			}
			break;
		case NETHER_WARTS:
			if(down.getType() == Material.SOUL_SAND)
			{
				break;
			}
			
			e.setCancelled(true);
			BlockUtil.breakNaturally(b, BreakReason.PHYSIC);
			break;
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
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void loseCommodity(PlayerChangedWorldEvent e)
	{
		if(!e.getPlayer().getWorld().getName().contains("newbie"))
		{
			return;
		}
		
		Player p = e.getPlayer();
		
		for(int i = 0; i < p.getInventory().getSize() + 4; i++)
		{
			ItemStack item = p.getInventory().getItem(i);
			
			if(Util.getRarity(item) == Rarity.RARE)
			{
				loseCommodity(item);
			}
		}
	}
	
	private void loseCommodity(ItemStack item)
	{
		Util.setCommodity(item, Rarity.COMMON);
		
		for(Entry<Enchantment, Integer> entry : CommonsUtil.getEnchants(item).entrySet())
		{
			int maxLevel = Util.getMaxLevel(entry.getKey(), Rarity.COMMON);
			
			if(entry.getValue() > maxLevel) 
			{
				CommonsUtil.removeEnchant(item, entry.getKey());
				CommonsUtil.addEnchant(item, entry.getKey(), maxLevel, true);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void a(BlockGrowEvent e)
	{
		Block b = e.getBlock();
		
		BlockState newState = e.getNewState();
		
		Block source;
		
		switch(newState.getType())
		{
		case CACTUS:
			e.setCancelled(true);
			
			Bukkit.getScheduler().runTask(this, () ->
			{
				b.setType(Material.CACTUS, false);
				BlockPhysicsEvent event = new BlockPhysicsEvent(b, 0);
						
				Bukkit.getPluginManager().callEvent(new BlockPhysicsEvent(b, 0));
				
				if(event.getBlock().getType() == Material.AIR)
				{
					for(Player p : b.getWorld().getPlayers())
					{
						((CraftPlayer) p).getHandle().playerConnection.sendPacket(new PacketPlayOutWorldParticles(
								EnumParticle.BLOCK_CRACK, true, b.getX() + 0.5F, b.getY() + 0.5F, b.getZ() + 0.5F, 0.25F, 0.25F, 0.25F, 0.0F, 40, 81));
					}
				}
			});
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
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void b(PlayerBucketEmptyEvent e)
	{
		if(e.getBucket() != Material.WATER_BUCKET)
		{
			return;
		}
		
		Block b = e.getBlockClicked();
		Block relative = b.getRelative(e.getBlockFace());
		
		if(relative.getBiome() != Biome.HELL)
		{
			return;
		}
		
		e.setCancelled(true);
		Player p = e.getPlayer();
		
		p.getItemInHand().setType(Material.BUCKET);
		
		((CraftPlayer) e.getPlayer()).getHandle().playerConnection.sendPacket(new PacketPlayOutWorldParticles(
				EnumParticle.SMOKE_NORMAL, true, relative.getX() + 0.5F, relative.getY() + 0.5F, relative.getZ() + 0.5F, 0.25F, 0.25F, 0.25F, 0.0F, 20, 174));
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
	public void changeSpawner(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		
		ItemStack item = e.getItem();
		Block b = e.getClickedBlock();
		
		if(b == null || item == null || item.getType() != Material.MONSTER_EGG || b.getType() != Material.MOB_SPAWNER)
		{
			return;
		}
		
		e.setCancelled(true);
		
		EntityType entityType = EntityType.fromId(item.getDurability());
		CreatureSpawner spawner = (CreatureSpawner) b.getState();
		
		if(spawner.getSpawnedType() == entityType)
		{
			return;
		}
		
		spawner.setSpawnedType(entityType);
		
		if(spawner.update())
		{
			Rarity itemRarity = Optional.ofNullable(Util.getRarity(item)).orElse(Rarity.COMMON);
			
			if(p.getGameMode() != GameMode.CREATIVE)
			{
				item.setAmount(item.getAmount() - 1);
				
				if(item.getAmount() <= 0)
				{
					p.setItemInHand(null);
				}
			}
			
			BlockData data = Optional.ofNullable(Util.readBlock(b)).orElse(new BlockData());
			Rarity blockRarity = Optional.ofNullable(data.getRarity()).orElse(Util.getRarity(b.getWorld()));
			
			Rarity worst = Util.getWorstRarity(itemRarity, blockRarity);
			
			data.setRarity(worst);
			Util.writeBlock(b, data);
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void ab(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		
		ItemStack item = e.getItem();
		Block b = e.getClickedBlock();
		
		if(b == null || item == null || item.getType() != Material.MONSTER_EGG || b.getType() == Material.MOB_SPAWNER)
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
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void cancelEnderChest(PlayerInteractEvent e)
	{
		Block clicked = e.getClickedBlock();
		
		if(clicked == null || clicked.getType() != Material.ENDER_CHEST)
		{
			return;
		}
		
		BlockData data = Util.readBlock(clicked);
		
		Rarity rarity = data == null ? null : data.getRarity();
		
		if(rarity == Rarity.LEGENDARY)
		{
			return;
		}
		
		e.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void cancelEnderChest(PrepareItemCraftEvent e)
	{
		CraftingInventory inv = e.getInventory();
		
		if(inv.getResult().getType() != Material.ENDER_CHEST)
		{
			return;
		}
		
		List<Rarity> rarities = new ArrayList<>();
		
		for(ItemStack content : inv.getMatrix())
		{
			if(content == null || content.getType() == Material.AIR)
			{
				continue;
			}
			
			rarities.add(Optional.ofNullable(Util.getRarity(content)).orElse(Rarity.COMMON));
		}
		
		Rarity worst = Util.getWorstRarity(rarities.toArray(new Rarity[rarities.size()]));
		
		if(worst != Rarity.LEGENDARY)
		{
			e.getInventory().setResult(null);
		}
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
		
		if(result != null && result.getType() != Material.AIR && Optional.ofNullable(Util.getRarity(smelting)).orElse(Rarity.COMMON) != Util.getRarity(result))
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
			e.setCancelled(true);
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
		Block b = e.getBlock();
		
		if(p.getGameMode() == GameMode.CREATIVE)
		{
			Util.writeBlock(b, null);
			return;
		}
		
		e.setCancelled(true);
		
		ItemStack tool = p.getItemInHand();
		
		BlockUtil.breakNaturally(b, tool);
		
		if(ItemSpecial.getInstance(ContainmentPickaxe.class).isInstanceOf(tool))
		{
			p.playSound(p.getLocation(), Sound.ITEM_BREAK, 1.0F, 1.0F);
			p.setItemInHand(null);
			return;
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
	
	public boolean activateVIP(Player p, int slot)
	{
		ItemStack item = p.getInventory().getItem(slot);
		List<UUID> list = CommonsUtil.getHiddenUUIDs(item);
		
		VipChest vipChest = VipChest.getInstance();
		
		if(list.size() != 2 || !list.get(0).equals(vipChest.getUUID()))
		{
			return false;
		}
		
		int amount = item.getAmount();
		
		if(amount <= 0)
		{
			return false;
		}
		
		boolean isValid = vipChest.getValidVips().remove(list.get(1));
		item.setAmount(--amount);
		
		if(amount <= 0 || !isValid)
		{
			p.getInventory().setItem(slot, null);
		}
		
		if(!isValid)
		{
			return false;
		}
		
		List<CommandSender> senders = new ArrayList<>(Bukkit.getOnlinePlayers());
		senders.add(Bukkit.getConsoleSender());
		
		TextComponent[] extra = new TextComponent[1];
		
		extra[0] = new TextComponent(p.getDisplayName());
		
		for(CommandSender sender : senders)
		{
			ResourceBundle bundle = ResourceBundle.getBundle("message");
			
			TextComponent text = Message.getTextComponent(bundle.getString("broadcast.vip-activated"), extra);
			text.setColor(ChatColor.GOLD);
			
			sender.sendMessage("");
			sender.sendMessage(text.toLegacyText());
			sender.sendMessage("");
		}
		
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "pex group vip user add " + p.getName() +  " +" + (30L * 24L * 60L * 60L));
		
		CommonPlayer cp = CraftCommonPlayer.get(p);
		cp.setTag(Tag.VIP);
		
		double shards = 10000.0D;
		cp.setBalance(cp.getBalance() + shards);
		
		ResourceBundle bundle = ResourceBundle.getBundle("message", cp.getLocale());
		DecimalFormat df = new DecimalFormat("#.##", new DecimalFormatSymbols(bundle.getLocale()));
		
		p.sendMessage(ChatColor.DARK_GREEN + "(+" + df.format(shards) + " SHARDS)");
		
		p.setFallDistance(-256.0F);
		
		PotionEffect resistance = null;
		
		for(PotionEffect effects : p.getActivePotionEffects())
		{
			if(effects.getType() == PotionEffectType.DAMAGE_RESISTANCE)
			{
				resistance = effects;
				break;
			}
		}
		
		p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 1, 5));
		p.getWorld().createExplosion(p.getLocation(), 6.0F);
		p.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
		
		if(resistance != null)
		{
			p.addPotionEffect(resistance);
		}
		
		Firework f = (Firework) p.getWorld().spawnEntity(p.getLocation(), EntityType.FIREWORK);
		FireworkMeta fireworkMeta = f.getFireworkMeta();
		
		fireworkMeta.addEffect(FireworkEffect.builder().with(FireworkEffect.Type.BALL).withColor(Color.GRAY).build());
		fireworkMeta.addEffect(FireworkEffect.builder().with(FireworkEffect.Type.BALL_LARGE).withColor(Color.AQUA).build());
		fireworkMeta.setPower(1);
		
		List<ItemStack> commonItems = new ArrayList<ItemStack>();
		
		ItemStack helmet = new ItemStack(Material.DIAMOND_HELMET);
		ItemStack chestplate = new ItemStack(Material.DIAMOND_CHESTPLATE);
		ItemStack leggings = new ItemStack(Material.DIAMOND_LEGGINGS);
		ItemStack boots = new ItemStack(Material.DIAMOND_BOOTS);
		
		commonItems.add(helmet);
		commonItems.add(chestplate);
		commonItems.add(leggings);
		commonItems.add(boots);
		
		commonItems.forEach(x -> 
		{
			x.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
			x.addEnchantment(Enchantment.DURABILITY, 3);
		});
		
		ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
		
		sword.addEnchantment(Enchantment.DAMAGE_ALL, 2);
		sword.addEnchantment(Enchantment.DURABILITY, 3);
		
		commonItems.add(sword);
		
		ItemStack[] tools = new ItemStack[]
		{
			new ItemStack(Material.DIAMOND_PICKAXE),	
			new ItemStack(Material.DIAMOND_AXE),	
			new ItemStack(Material.DIAMOND_SPADE),	
		};
		
		for(ItemStack tool : tools)
		{
			tool.addEnchantment(Enchantment.DIG_SPEED, 2);
			tool.addEnchantment(Enchantment.DURABILITY, 3);
			
			commonItems.add(tool);
		}
		
		commonItems.add(new ItemStack(Material.GOLDEN_APPLE, 2, (short) 1));
		commonItems.add(new ItemStack(Material.GRILLED_PORK, 32));
		
		Random r = new Random();
		
		List<ItemStack> rareItems = new ArrayList<ItemStack>();
		
		if(r.nextInt(10) < 4)
		{
			rareItems.add(new ItemStack(Material.MOB_SPAWNER));
			
			short[] array = new short[] {50, 51, 52, 54, 55, 56, 57, 58, 59, 60, 61, 62, 65,
					66, 67, 68, 90, 91, 92, 93, 94, 95, 96, 98, 100, 101, 120};
			
			rareItems.add(new ItemStack(Material.MONSTER_EGG, 1, array[r.nextInt(array.length)]));
		}
		
		commonItems.forEach(x -> Util.setCommodity(x, Rarity.COMMON));
		rareItems.forEach(x -> Util.setCommodity(x, Rarity.RARE));
		
		if(p.getInventory().getHelmet() == null)
		{
			p.getInventory().setHelmet(helmet);
			commonItems.remove(helmet);
		}
		
		if(p.getInventory().getChestplate() == null)
		{
			p.getInventory().setChestplate(chestplate);
			commonItems.remove(chestplate);
		}
		
		if(p.getInventory().getLeggings() == null)
		{
			p.getInventory().setLeggings(leggings);
			commonItems.remove(leggings);
		}
		
		if(p.getInventory().getBoots() == null)
		{
			p.getInventory().setBoots(boots);
			commonItems.remove(boots);
		}
		
		List<ItemStack> items = new ArrayList<>();
		
		items.addAll(commonItems);
		items.addAll(rareItems);
		
		for(ItemStack itemStack : commonItems)
		{
			for(ItemStack drops : p.getInventory().addItem(itemStack).values())
			{
				CommonsUtil.dropItem(p, drops);
				continue;
			}
		}
		
		f.setFireworkMeta(fireworkMeta);
		
		return true;
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void asdasa(PlayerJoinEvent e)
	{
		Player p = e.getPlayer();
		
		if(!p.hasPlayedBefore())
		{
			p.teleport(Warp.getInstance(WarpTutorial.class).getSpawnLocation());
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void a(PlayerTeleportEvent e)
	{
		if(e.getCause() == TeleportCause.COMMAND)
		{
			return;
		}
		
		if(e.getFrom().getWorld().getName().contains("world") ||
				!e.getTo().getWorld().getName().contains("world"))
		{
			return;
		}
		
		Player p = e.getPlayer();
		CommonPlayer cp = CraftCommonPlayer.get(p);
		
		if(p.getUniqueId().version() == 3 && !cp.hasPermission("raid.access"))
		{
			e.setCancelled(true);
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