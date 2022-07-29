package br.com.acenetwork.craftlandia.warp;

import java.util.Optional;
import java.util.ResourceBundle;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockMultiPlaceEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.acenetwork.commons.CommonsUtil;
import br.com.acenetwork.commons.player.CommonPlayer;
import br.com.acenetwork.craftlandia.Main;
import br.com.acenetwork.craftlandia.Util;
import br.com.acenetwork.craftlandia.event.NPCLoadEvent;
import br.com.acenetwork.craftlandia.manager.BlockData;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.md_5.bungee.api.ChatColor;

public class Newbie extends Warp
{
	private final Location spawnLocation;
	private final Location portalLocation;
	private boolean pvp;
	
	public Newbie(World w)
	{
		super(w);
		
		spawnLocation = new Location(w, 0.5D, 69.0D, 0.5D, 0.0F, 0.0F);
		portalLocation = new Location(w, 13.5D, 88.0D, -7.5D, 0.0F, 0.0F);
		
		pvp = isDay() && !w.hasStorm();
		
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				if(w.hasStorm())
				{
					pvp = true;
					return;
				}
				
				if(pvp != (pvp = !isDay()))
				{
					for(CommonPlayer cp : getCommonPlayers())
					{
						Player p = cp.getPlayer();
						ResourceBundle bundle = ResourceBundle.getBundle("message", cp.getLocale());
						
						if(pvp)
						{
							p.sendMessage(ChatColor.RED + bundle.getString("newbie.dusk")
							+ " " + ChatColor.DARK_RED + ChatColor.BOLD + "PVP ON!");
						}
						else
						{
							p.sendMessage(ChatColor.GREEN + bundle.getString("newbie.dawn")
							+ " " + ChatColor.DARK_GREEN + ChatColor.BOLD + "PVP OFF!");
						}
					}
				}
			}
		}.runTaskTimer(Main.getInstance(), 1L, 1L);
	}
	
	private boolean isDay()
	{
		return w.getTime() < 12000L;
	}
	
	@Override
	public boolean isSpawnProtection(Location l)
	{
		return Math.abs(l.getBlockX()) < 256 && Math.abs(l.getBlockZ()) < 256;
	}
	
	@Override
	public Location getSpawnLocation()
	{
		return spawnLocation;
	}
	
	@Override
	public boolean canSetHome()
	{
		return true;
	}
	
	@Override
	public int blocksAwayFromSpawnToSetHome()
	{
		return 1024;
	}
	
	@EventHandler
	public void a(NPCLoadEvent e)
	{
		NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, "" 
				+ ChatColor.YELLOW + ChatColor.BOLD + "SKIP PARKOUR");
		npc.spawn(new Location(w, -1.5D, 69.0D, 2.5D, -135.0F, 0.0F));
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void entityDamage(EntityDamageEvent e)
	{
		Entity entity = e.getEntity();
		
		if(entity.getWorld() != w)
		{
			return;
		}
		
		if(!(entity instanceof Player))
		{
			return;
		}
		
		if(CommonsUtil.isPVP(e))
		{
			e.setCancelled(!pvp);
			return;
		}
		
		e.setCancelled(false);
	}
	
	@EventHandler
	public void aasd(WeatherChangeEvent e)
	{
		if(e.getWorld() != w)
		{
			return;
		}
		
		boolean changed = false;
		
		if(w.hasStorm())
		{
			if(isDay())
			{
				changed = pvp != (pvp = false);
			}
		}
		else
		{
			changed = pvp != (pvp = true);
		}
		
		if(!changed)
		{
			return;
		}
		
		for(CommonPlayer cp : getCommonPlayers())
		{
			Player p = cp.getPlayer();
			ResourceBundle bundle = ResourceBundle.getBundle("message", cp.getLocale());
			
			if(pvp)
			{
				p.sendMessage(ChatColor.RED + bundle.getString("newbie.started-raining")
						+ " " + ChatColor.DARK_RED + ChatColor.BOLD + "PVP ON!");
			}
			else
			{
				p.sendMessage(ChatColor.GREEN + bundle.getString("newbie.stoped-raining")
						+ " " + ChatColor.DARK_GREEN + ChatColor.BOLD + "PVP OFF!");
			}
		}
	}
	
	@Override
	public boolean isSafeZone(Location l)
	{
		return Math.abs(l.getBlockX()) <= 65 && Math.abs(l.getBlockZ()) <= 65;
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void c(BlockPlaceEvent e)
	{
		if(e.getBlock().getWorld() != w)
		{
			return;
		}
		
		super.c(e);
		
		Player p = e.getPlayer();
		
		if(p.getGameMode() == GameMode.CREATIVE)
		{
			return;
		}
		
		Material type = e.getItemInHand().getType();
		
		if(!(type.name().contains("DOOR") || type.name().contains("FENCE") || type.name().contains("STAIRS")
				|| type.name().contains("RAIL") || type.name().contains("PLATE") || type.name().contains("BRICK")
				|| type.name().contains("PISTON") || type.name().contains("SANDSTONE") || type.name().contains("BUTTON"))
				|| type.name().contains("SLAB") || type.name().contains("STEP"))
		{
			switch(type)
			{
			case CHEST:
			case FURNACE:
			case DISPENSER:
			case SIGN:
			case ANVIL:
			case BANNER:
			case BEACON:
			case BED:
			case BOOKSHELF:
			case CAKE:
			case CARPET:
			case CAULDRON_ITEM:
			case CLAY:
			case COAL_BLOCK:
			case COBBLE_WALL:
			case REDSTONE_COMPARATOR:
			case WORKBENCH:
			case DAYLIGHT_DETECTOR:
			case DIAMOND_BLOCK:
			case DROPPER:
			case EMERALD_BLOCK:
			case ENCHANTMENT_TABLE:
			case ENDER_PORTAL_FRAME:
			case ENDER_CHEST:
			case GLASS:
			case THIN_GLASS:
			case STAINED_GLASS_PANE:
			case GLOWSTONE:
			case GOLD_BLOCK:
			case HARD_CLAY:
			case STAINED_CLAY:
			case HAY_BLOCK:
			case HOPPER:
			case ICE:
			case IRON_BLOCK:
			case JUKEBOX:
			case LADDER:
			case LAPIS_BLOCK:
			case LEVER:
			case JACK_O_LANTERN:
			case LOG:
			case LOG_2:
			case NOTE_BLOCK:
			case OBSIDIAN:
			case PACKED_ICE:
			case PRISMARINE:
			case QUARTZ_BLOCK:
			case RED_SANDSTONE:
			case REDSTONE_BLOCK:
			case REDSTONE_LAMP_OFF:
			case SEA_LANTERN:
			case SKULL_ITEM:
			case SLIME_BLOCK:
			case SNOW:
			case SNOW_BLOCK:
			case SPONGE:
			case STONE:
			case TRAPPED_CHEST:
			case TRIPWIRE_HOOK:
			case WOOL:
			case WOOD:
				break;
			default:
				return;
			}
		}
		
		BlockData data = Optional.ofNullable(Util.readBlock(e.getBlock())).orElse(new BlockData());
		data.setPlayer(p.getUniqueId());
		
		if(e instanceof BlockMultiPlaceEvent)
		{
			BlockMultiPlaceEvent ee = (BlockMultiPlaceEvent) e;
			ee.getReplacedBlockStates().forEach(x -> Util.writeBlock(x.getBlock(), data));
		}
		else
		{
			Util.writeBlock(e.getBlock(), data);
		}
	}
	
	@Override
	public Location getPortalLocation()
	{
		return portalLocation;
	}
}