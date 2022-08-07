package br.com.acenetwork.craftlandia.manager;

import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockMultiPlaceEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.material.Dispenser;
import org.bukkit.metadata.FixedMetadataValue;

import br.com.acenetwork.commons.CommonsUtil;
import br.com.acenetwork.commons.event.CustomEntityDeathEvent;
import br.com.acenetwork.commons.event.CustomStructureGrowEvent;
import br.com.acenetwork.commons.event.SocketEvent;
import br.com.acenetwork.commons.listener.EntitySpawn;
import br.com.acenetwork.craftlandia.Main;
import br.com.acenetwork.craftlandia.warp.Warp;
import br.com.acenetwork.craftlandia.warp.WarpLand;
import net.md_5.bungee.api.ChatColor;

public class Land implements Listener
{
	public static final Set<Land> SET = new TreeSet<Land>(new Comparator<Land>()
	{
		@Override
		public int compare(Land arg0, Land arg1)
		{
			return arg0.getBeautyName().compareTo(arg1.getBeautyName());
		}
	});
	
	private final UUID worldUID;
	private final int x, z;
	private final int id;
	private final Type type;
	private final Biome biome;
	
	private LandData landData;
	
	private int task;
	
	public enum Direction
	{
		EAST(1, 0),
		NORTH(0, -1), 
		WEST(-1, 0), 
		SOUTH(0, 1),
		;
		
		private final int x;
		private final int z;
		
		Direction(int x, int z)
		{
			this.x = x;
			this.z = z;
		}
	}
	
	private static final int PATH_WIDTH = 5;
	
	public static void loadLands(UUID worldUID)
	{
		int x = -65, z = 65;
		int k = 0;
		
		int id = 0;
		
		for(int j = 0; j < Land.Type.values().length; j++)
		{
			Land.Type type = Land.Type.values()[j];
			final int size = type.getSize();
			
			z += PATH_WIDTH * Direction.SOUTH.z + size * Direction.SOUTH.z;
			
			k = (1 + k) * 2 + 1;
			
			for(int i = 0; i < Direction.values().length; i++)
			{
				Direction direction = Direction.values()[i];
				
				for(int l = 0; l < k; l++)
				{
					if(l == 0 && i == 0)
					{
						new Land(id++, worldUID, x, z, type);
						continue;
					}
					
					x += PATH_WIDTH * direction.x + size * direction.x; 
					z += PATH_WIDTH * direction.z + size * direction.z;
					
					new Land(id++, worldUID, x, z, type);
				}
			}
		}
	}
	
	public enum Type
	{
		LARGE(63),
		MEDIUM(29), 
		SMALL(12), 
		;
		
		private final int size;
		
		Type(int size)
		{
			this.size = size;
		}
		
		public int getSize()
		{
			return size;
		}
	}
	
	public boolean isLand(Block b)
	{
		return b != null && isLand(b.getLocation());
	}
	
	public boolean isLand(Location l)
	{
		return isLand(l.getWorld(), l.getBlockX(), l.getBlockZ());
	}
	
	public boolean isLand(World w, int x, int z)
	{
		return Warp.MAP.get(w.getUID()) instanceof WarpLand 
				&& x >= this.x && x < this.x + type.size && z <= this.z && z > this.z - type.size;
	}
	
	private Land(int id, UUID worldUID, int x, int z, Type type)
	{
		this.id = id;
		this.worldUID = worldUID;
		this.x = x;
		this.z = z;
		this.type = type;
		this.biome = Bukkit.getWorld(worldUID).getBiome(x, z);
		
		this.landData = LandData.load(id);
		
		SET.add(this);
		
		Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
	}
	
	public static Land getById(int id)
	{
		return SET.stream().filter(x -> x.id == id).findAny().orElse(null);
	}
	
	public Set<UUID> getTrustedPlayers()
	{
		return landData.getTrustedPlayers();
	}
	
	public LandData getLandData()
	{
		return landData;
	}
	
	public void setOwner(UUID uuid)
	{
		landData.setOwner(uuid);
	}
	
	public UUID getOwner()
	{
		return landData.getOwner();
	}
	
	public boolean isTrusted(UUID uuid)
	{
		return landData.isTrusted(uuid);
	}
	
	public boolean isTrusted(Player p)
	{
		return isTrusted(p.getUniqueId());
	}
	
	public long getResetCooldown()
	{
		return landData.getResetCooldown();
	}
	
	public void setResetCooldown(long cooldown)
	{
		landData.setResetCooldown(cooldown);
	}
	
	public boolean isPublic()
	{
		return landData.getOwner() == null || landData.isPublic();
	}
	
	public boolean setPublic(boolean isPublic)
	{
		if(task != 0 || isPublic() == isPublic)
		{
			return false;
		}
		
		final int minX = getMinX() - 1;
		final int maxX = minX + getSize() + 1;
		final int minZ = getMinZ() - 1;
		final int maxZ = minZ + getSize() + 1;
		final int size = getSize() + 1;
		
		World w = getWorld();
		
		Material type = isPublic ? Material.AIR : Material.STAINED_GLASS_PANE;
		byte data;
		Block b = w.getBlockAt(getX(), 63, getZ());
	
		switch(b.getBiome())
		{
		case ICE_PLAINS_SPIKES:
			data = 0;
			break;
		case DESERT:
			data = 4;
			break;
		case HELL:
			data = 14;
			break;
		case SKY:
			data = 10;
			break;
		case JUNGLE:
			data = 5;
			break;
		case SWAMPLAND:
			data = 13;
			break;
		case MESA:
			data = 1;
			break;
		case MUSHROOM_ISLAND:
			data = 2;
			break;
		default:
			type = Material.GLASS;
			data = 0;
			break;
		}
		
		int id = type.getId();
		
		int maxHeight = w.getMaxHeight();
		
		task = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), new Runnable()
		{
			int y = 63;
			
			@Override
			public void run()
			{
				if(y >= maxHeight)
				{
					Bukkit.getScheduler().cancelTask(task);
					task = 0;
					
					Warp warp = Warp.MAP.get(w.getUID());
					
					for(Player all : w.getPlayers())
					{
						if(isTrusted(all))
						{
							continue;
						}
						
						if(isLand(all.getLocation()))
						{
							all.teleport(warp.getSpawnLocation());
						}
					}
					
					return;
				}
				
				for(int x = minX; x <= maxX; x++)
				{
					for(int z = minZ; z <= maxZ;)
					{
						Block b = w.getBlockAt(x, y, z);
						b.setTypeIdAndData(id, data, false);
						w.playSound(b.getLocation(), Sound.DIG_STONE, 1.0F, 1.0F);
						
						if(x == minX || x == maxX)
						{
							z++;
						}
						else
						{
							z += size;
						}
					}
				}
				
				y++;
			}
		}, 2L, 2L);
		
		landData.setPublic(isPublic);
		return true;
	}
	
	public void setName(String name) throws NameAlreadyInUseException
	{
		landData.setName(name);
	}
	
	public String getName()
	{
		return landData.getName();
	}
	
	public Location getLocation()
	{
		return landData.getLocation();
	}
	
	public void setLocation(Location l)
	{
		landData.setLocation(l);
	}
	
	@EventHandler
	public void aa(SocketEvent e)
	{
		String[] args = e.getArgs();
		String cmd = args[0];
		
		if(cmd.equals("landuuid"))
		{
			int id = Integer.valueOf(args[1]);
			
			if(this.id != id)
			{
				return;
			}
			
			try
			{
				setOwner(UUID.fromString(args[2]));
			}
			catch(IllegalArgumentException ex)
			{
				setOwner(null);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void a(BlockGrowEvent e)
	{
		Block b = e.getBlock();
		BlockState block = e.getNewState();
		
		if(isLand(b) && isLand(block.getLocation()) && getOwner() != null)
		{
			e.setCancelled(false);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void a(BlockSpreadEvent e)
	{
		Block b = e.getSource();
		BlockState block = e.getNewState();
		
		if(isLand(b) && isLand(block.getLocation()) && getOwner() != null)
		{
			e.setCancelled(false);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
	public void on(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		Block b = e.getClickedBlock();
		
		if(b != null && isLand(b))
		{
			if(isTrusted(p))
			{
				e.setCancelled(false);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void a(HangingPlaceEvent e)
	{
		Player p = e.getPlayer();
		Entity entity = e.getEntity();
		
		if(isLand(entity.getLocation()) && isTrusted(p))
		{
			e.setCancelled(false);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void a(HangingBreakByEntityEvent e)
	{
		Entity entity = e.getEntity();
		
		if(isLand(entity.getLocation()))
		{
			Entity remover = e.getRemover();
			Projectile projectile = null;
			Player p = null;
			
			if(remover instanceof Projectile)
			{
				projectile = (Projectile) remover;
				
				if(projectile.getShooter() instanceof Player)
				{
					p = (Player) projectile.getShooter();
				}
			}
			else if(remover instanceof Player)
			{
				p = (Player) remover;
			}
			
			if(p != null)
			{
				if(isTrusted(p))
				{
					e.setCancelled(false);
				}
			}
			else if(projectile != null)
			{
				if(projectile.getShooter() instanceof Block)
				{
					if(isLand(((Block) projectile.getShooter())))
					{
						e.setCancelled(false);
					}
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void a(PlayerBucketFillEvent e)
	{
		Block b = e.getBlockClicked();
		
		if(isLand(b))
		{
			Player p = e.getPlayer();
			e.setCancelled(!isTrusted(p));
		}
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void a(PlayerBucketEmptyEvent e)
	{
		Block b = e.getBlockClicked();
		
		if(isLand(b))
		{
			Player p = e.getPlayer();
			e.setCancelled(!isTrusted(p));
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
	public void on(BlockBreakEvent e)
	{
		Block b = e.getBlock();
		Player p = e.getPlayer();
		
		if(isLand(b))
		{
			e.setCancelled(!isTrusted(p));
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
	public void on(BlockPlaceEvent e)
	{
		Block b = e.getBlock();
		Player p = e.getPlayer();
		
		if(isLand(b))
		{
			e.setCancelled(!isTrusted(p));
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void a(BlockFromToEvent e)
	{
		Block b = e.getToBlock();
		
		if(isLand(b) && getOwner() != null)
		{
			e.setCancelled(false);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void b(CustomEntityDeathEvent ce)
	{
		EntityDeathEvent e = ce.getEntityDeathEventEvent();
		
		if(!isLand(e.getEntity().getLocation()) || !hasOwner())
		{
			return;
		}
		
		ce.setKeepExp(false);
		ce.setKeepInventory(false);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void a(BlockPhysicsEvent e)
	{
		Block b = e.getBlock();
		
		if(isLand(b) && getOwner() != null)
		{
			e.setCancelled(false);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void a(BlockPistonExtendEvent e)
	{
		Block b = e.getBlock();
		
		if(isLand(b) && getOwner() != null)
		{
			e.setCancelled(false);
			
			for(Block blocks : e.getBlocks())
			{
				blocks = blocks.getRelative(e.getDirection());
				
				if(!isLand(blocks))
				{
					e.setCancelled(true);
					return;
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void a(BlockPistonRetractEvent e)
	{
		Block b = e.getBlock();
		
		if(isLand(b))
		{
			e.setCancelled(false);
			
			for(Block blocks : e.getBlocks())
			{
				if(!isLand(blocks))
				{
					e.setCancelled(true);
					return;
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void a(BlockMultiPlaceEvent e)
	{
		Block b = e.getBlock();
		
		if(isLand(b))
		{
			Player p = e.getPlayer();
			
			if(isTrusted(p))
			{
				e.setCancelled(false);
				
				for(BlockState bs : e.getReplacedBlockStates())
				{
					if(!isLand(bs.getLocation()))
					{
						e.setCancelled(true);
						return;
					}
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void a(CustomStructureGrowEvent ce)
	{
		StructureGrowEvent e = ce.getStructureGrowEvent();
		
		Location l = e.getLocation();
		
		if(isLand(l))
		{
			e.getBlocks().clear();
			
			for(BlockState blocks : ce.getOriginalBlocks())
			{
				if(isLand(blocks.getLocation()))
				{
					e.getBlocks().add(blocks);
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void asd(BlockDispenseEvent e)
	{
		Block b = e.getBlock();
		
		if(isLand(b))
		{
			Dispenser dispenser = (Dispenser) b.getState().getData();
			
			if(!CommonsUtil.isDispensable(e.getItem().getType()) || isLand(b.getRelative(dispenser.getFacing())))
			{
				e.setCancelled(false);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void ab(EntityChangeBlockEvent e)
	{
		Block b = e.getBlock();
		
		if(isLand(b) && hasOwner())
		{
			e.setCancelled(false);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void a(CreatureSpawnEvent e)
	{
		if(isLand(e.getLocation()))
		{
			if(hasOwner())
			{
				e.setCancelled(false);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void a(EntitySpawnEvent e)
	{
		if(!isLand(e.getLocation()))
		{
			return;
		}
		
		e.getEntity().setMetadata("land", new FixedMetadataValue(Main.getInstance(), id));
	}
	
	public World getWorld()
	{
		return Bukkit.getWorld(worldUID);
	}
	
	public UUID getWorldUID()
	{
		return worldUID;
	}
	
	public int getX()
	{
		return x;
	}
	
	public int getZ()
	{
		return z;
	}
	
	public int getSize()
	{
		return type.size;
	}
	
	public boolean isOwner(UUID uuid)
	{
		return uuid.equals(getOwner());
	}
	
	public String getBeautyId()
	{
		return new DecimalFormat("000").format(id + 1);
	}
	
	@Override
	public String toString()
	{
		return "Land [id=" + id + "]";
	}
	
	public int getId()
	{
		return id;
	}
	
	public int getMinX()
	{
		return x;
	}
	
	public int getMinZ()
	{
		return z - getSize() + 1;
	}
	
	public String getBeautyName()
	{
		return getName() == null ? getBeautyId() : getName();
	}
	
	public static Land getLandByUserInput(String input)
	{
		try
		{
			return getById(Integer.valueOf(input) - 1);
		}
		catch(NumberFormatException e)
		{
			return SET.stream().filter(x -> input.equals(x.getName())).findAny().orElse(null);
		}
	}
	
	public static ChatColor getColor(Biome biome)
	{
		switch(biome)
		{
		case DESERT:
			return ChatColor.YELLOW;
		case MESA:
			return ChatColor.GOLD;
		case HELL:
			return ChatColor.RED;
		case SKY:
			return ChatColor.DARK_PURPLE;
		case MUSHROOM_ISLAND:
			return ChatColor.LIGHT_PURPLE;
		case ICE_PLAINS_SPIKES:
			return ChatColor.WHITE;
		case JUNGLE:
			return ChatColor.GREEN;
		case SWAMPLAND:
			return ChatColor.DARK_GREEN;
		default:
			return ChatColor.GRAY;
		}
	}
	
	public Biome getBiome()
	{
		return biome;
	}
	
	public Type getType()
	{
		return type;
	}
	
	public int getTrustedPlayerLimit()
	{
		return getSize() / 4;
	}
	
	public boolean hasOwner()
	{
		return getOwner() != null;
	}
}