package br.com.acenetwork.craftlandia.warp;

import java.util.Optional;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockMultiPlaceEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import br.com.acenetwork.craftlandia.Util;
import br.com.acenetwork.craftlandia.manager.BlockData;

public class Newbie extends Warp
{
	private final Location spawnLocation;
	
	public Newbie(World w)
	{
		super(w);
		
		spawnLocation = new Location(w, 0.5D, 69.0D, 0.5D, 0.0F, 0.0F);
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
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void c(BlockPlaceEvent e)
	{
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
}