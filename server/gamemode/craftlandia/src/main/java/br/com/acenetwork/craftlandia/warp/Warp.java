package br.com.acenetwork.craftlandia.warp;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockMultiPlaceEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.EntityBlockFormEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityCreatePortalEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.SheepRegrowWoolEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.event.world.WorldSaveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Dispenser;

import com.google.common.io.ByteStreams;

import br.com.acenetwork.commons.CommonsUtil;
import br.com.acenetwork.commons.event.CustomStructureGrowEvent;
import br.com.acenetwork.commons.player.CommonPlayer;
import br.com.acenetwork.commons.player.craft.CraftCommonPlayer;
import br.com.acenetwork.craftlandia.Main;
import br.com.acenetwork.craftlandia.Rarity;
import br.com.acenetwork.craftlandia.Util;
import br.com.acenetwork.craftlandia.manager.BlockData;
import br.com.acenetwork.craftlandia.manager.ChunkLocation;
import br.com.acenetwork.craftlandia.manager.Config;
import br.com.acenetwork.craftlandia.player.SurvivalPlayer;
import net.md_5.bungee.api.ChatColor;

public abstract class Warp implements Listener
{
	public static final Map<UUID, Warp> MAP = new HashMap<>();
	public final Map<ChunkLocation, Map<Short, BlockData>> blockData = new HashMap<>();
	
	protected final World w;
	protected final String worldName;
	
	public enum Result
	{
		ALLOW, INVINCIBILITY;
	}
	
	public Warp(World w)
	{
		this.w = w;
		this.worldName = w.getName();
		Bukkit.getPluginManager().registerEvents(this, Main.getPlugin());
		MAP.put(w.getUID(), this);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void a(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		
		if(p.getWorld() != w)
		{
			return;
		}
		
		ItemStack item = e.getItem();
		Block b = e.getClickedBlock();
		
		if(b != null && CommonsUtil.isInteractable(b.getType()))
		{
			
		}
		else if(item != null && item.getType() == Material.BOAT && b != null)
		{
			e.setCancelled(isSpawnProtection(b.getLocation()));
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void a(BlockGrowEvent e)
	{
		Block b = e.getBlock();
		
		if(b.getWorld() != w)
		{
			return;
		}
		
		BlockState bs = e.getNewState();
		e.setCancelled(isSpawnProtection(b.getLocation()) || isSpawnProtection(bs.getLocation()));
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void a(BlockSpreadEvent e)
	{
		Block b = e.getBlock();
		
		if(b.getWorld() != w)
		{
			return;
		}
		
		e.setCancelled(isSpawnProtection(b.getLocation()));
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void a(BlockFadeEvent e)
	{
		Block b = e.getBlock();
		
		if(b.getWorld() != w)
		{
			return;
		}
		
		e.setCancelled(isSpawnProtection(b.getLocation()));
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void a(SheepRegrowWoolEvent e)
	{
		
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void a(PlayerInteractEntityEvent e)
	{
		Entity entity = e.getRightClicked();
		
		if(entity.getWorld() != w)
		{
			return;
		}
		
		if(entity instanceof ItemFrame)
		{
			e.setCancelled(isSpawnProtection(entity.getLocation()));
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void a(EntityDamageEvent e)
	{
		Entity entity = e.getEntity();
		
		if(entity.getWorld() != w)
		{
			return;
		}
		
		if(entity instanceof ItemFrame)
		{
			e.setCancelled(isSpawnProtection(entity.getLocation()));
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void a(HangingBreakEvent e)
	{
		Entity entity = e.getEntity();
		
		if(entity.getWorld() != w)
		{
			return;
		}
		
		e.setCancelled(isSpawnProtection(entity.getLocation()));
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void a(HangingPlaceEvent e)
	{
		Entity entity = e.getEntity();
		
		if(entity.getWorld() != w)
		{
			return;
		}
		
		e.setCancelled(isSpawnProtection(entity.getLocation()));
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void b(PlayerBucketFillEvent e)
	{
		Block b = e.getBlockClicked();
		
		if(b.getWorld() != w)
		{
			return;
		}
		
		e.setCancelled(isSpawnProtection(b.getLocation()));
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void b(PlayerBucketEmptyEvent e)
	{
		Block b = e.getBlockClicked();
		
		if(b.getWorld() != w)
		{
			return;
		}
		
		e.setCancelled(isSpawnProtection(b.getLocation()));
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void b(BlockBreakEvent e)
	{
		Block b = e.getBlock();
		
		if(b.getWorld() != w)
		{
			return;
		}
		
		e.setCancelled(isSpawnProtection(b.getLocation()));
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void b(BlockPlaceEvent e)
	{
		Block b = e.getBlock();
		
		if(b.getWorld() != w)
		{
			return;
		}
		
		e.setCancelled(isSpawnProtection(b.getLocation()));
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void c(BlockPlaceEvent e)
	{
		Block b = e.getBlock();
		
		BlockData bd = new BlockData();
		
		bd.setRarity(Optional.ofNullable(Util.getRarity(e.getItemInHand())).orElse(Rarity.COMMON));
		bd.setProperties(Util.getProperties(e.getItemInHand()));
		
		writeBlock(b, bd);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void on(CreatureSpawnEvent e)
	{
		Location l = e.getLocation();
		
		if(l.getWorld() != w)
		{
			return;
		}
		
		e.setCancelled(isSpawnProtection(l));
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void b(EntityExplodeEvent e)
	{
		if(e.getLocation().getWorld() != w)
		{
			return;
		}
		
		Iterator<Block> iterator = e.blockList().iterator();
		
		while(iterator.hasNext())
		{
			if(isSpawnProtection(iterator.next().getLocation()))
			{
				iterator.remove();
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void b(BlockExplodeEvent e)
	{
		if(e.getBlock().getWorld() != w)
		{
			return;
		}
		
		Iterator<Block> iterator = e.blockList().iterator();
		
		while(iterator.hasNext())
		{
			if(isSpawnProtection(iterator.next().getLocation()))
			{
				iterator.remove();
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void a(LeavesDecayEvent e)
	{
		Block b = e.getBlock();
		
		if(b.getWorld() != w)
		{
			return;
		}
		
		e.setCancelled(isSpawnProtection(b.getLocation()));
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void a(CustomStructureGrowEvent ce)
	{
		StructureGrowEvent e = ce.getStructureGrowEvent();
		
		if(e.getWorld() != w)
		{
			return;
		}
		
		e.getBlocks().removeAll(e.getBlocks().stream().filter(x -> isSpawnProtection(x.getLocation())).collect(Collectors.toList()));
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void a(EntityBlockFormEvent e)
	{
		Block b = e.getBlock();
		
		if(b.getWorld() != w)
		{
			return;
		}
		
		e.setCancelled(isSpawnProtection(b.getLocation()));
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void a(BlockFormEvent e)
	{
		Block b = e.getBlock();
		
		if(b.getWorld() != w)
		{
			return;
		}
		
		e.setCancelled(isSpawnProtection(b.getLocation()));
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void a(BlockDispenseEvent e)
	{
		Block b = e.getBlock();
		
		if(b.getWorld() != w)
		{
			return;
		}
		
		Dispenser dispenser = (Dispenser) b.getState().getData();
		Block relative = b.getRelative(dispenser.getFacing());
		e.setCancelled(CommonsUtil.isDispensable(e.getItem().getType()) && isSpawnProtection(relative.getLocation()));
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void a(EntityCreatePortalEvent e)
	{
		for(BlockState blocks : e.getBlocks())
		{
			if(blocks.getWorld() != w)
			{
				continue;
			}
			
			if(isSpawnProtection(blocks.getLocation()))
			{
				if(e.getEntity() instanceof Player)
				{
					Player p = (Player) e.getEntity();
					ResourceBundle bundle = ResourceBundle.getBundle("message", CommonsUtil.getLocaleFromMinecraft(p.spigot().getLocale()));
					p.sendMessage(ChatColor.RED + bundle.getString("raid.event.portal-create.cant-create-portal-spawn"));
				}
				
				e.setCancelled(true);
				return;
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void a(BlockPistonExtendEvent e)
	{
		Block b = e.getBlock();
		
		if(b.getWorld() != w)
		{
			return;
		}
		
		if(isSpawnProtection(b.getLocation()))
		{
			e.setCancelled(true);
			return;
		}
		
		Block relative = b.getRelative(e.getDirection());
		
		if(isSpawnProtection(relative.getLocation()))
		{
			e.setCancelled(true);
			return;
		}

		
		for(Block blocks : e.getBlocks())
		{
			blocks = blocks.getRelative(e.getDirection());
			
			if(isSpawnProtection(blocks.getLocation()))
			{
				e.setCancelled(true);
				return;
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void a(BlockPistonRetractEvent e)
	{
		Block b = e.getBlock();
		
		if(b.getWorld() != w)
		{
			return;
		}
		
		if(isSpawnProtection(b.getLocation()))
		{
			e.setCancelled(true);
			return;
		}
		
		for(Block blocks : e.getBlocks())
		{
			if(isSpawnProtection(blocks.getLocation()))
			{
				e.setCancelled(true);
				return;
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void a(EntityChangeBlockEvent e)
	{
		Block b = e.getBlock();
		
		if(b.getWorld() != w)
		{
			return;
		}
		
		e.setCancelled(isSpawnProtection(b.getLocation()));
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void a(BlockFromToEvent e)
	{
		Block b = e.getToBlock();
		
		if(b.getWorld() != w)
		{
			return;
		}
		
		e.setCancelled(isSpawnProtection(b.getLocation()));
		
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void a(BlockMultiPlaceEvent e)
	{
		Block b = e.getBlock();
		
		if(b.getWorld() != w)
		{
			return;
		}
		
		for(BlockState bs : e.getReplacedBlockStates())
		{
			if(isSpawnProtection(bs.getLocation()))
			{
				e.setCancelled(true);
				return;
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void sda(PlayerChangedWorldEvent e)
	{
		if(e.getPlayer().getWorld() != w)
		{
			return;
		}
		
		e.getPlayer().spigot().setCollidesWithEntities(true);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void a(PlayerDropItemEvent e)
	{
		if(e.getPlayer().getWorld() != w)
		{
			return;
		}
		
		e.setCancelled(!e.getPlayer().spigot().getCollidesWithEntities());
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void a(BlockPhysicsEvent e)
	{
		Block b = e.getBlock();
		
		if(b.getWorld() != w)
		{
			return;
		}
		
		e.setCancelled(isSpawnProtection(b.getLocation()));
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void a(BlockIgniteEvent e)
	{
		Block b = e.getBlock();
		
		if(b.getWorld() != w)
		{
			return;
		}
		
		e.setCancelled(isSpawnProtection(b.getLocation()));
	}
	
	@EventHandler
	public void a(WorldSaveEvent e)
	{
		if(e.getWorld() != w)
		{
			return;
		}
		
		saveChunks();
	}
	
	public BlockData readBlock(Block b)
	{
		Chunk c = b.getChunk();
		Map<Short, BlockData> map = blockData.get(loadChunk(c.getX(), c.getZ()));
		short coords = Util.chunkCoordsToShort(b);
		
		if(!map.containsKey(coords))
		{
			return null;
		}
		
		return map.get(coords);
	}
	
	public void writeBlock(Block b, BlockData bd)
	{
		if(b.getWorld() != w)
		{
			return;
		}
		
		Chunk c = b.getChunk();
		Map<Short, BlockData> map = blockData.get(loadChunk(c.getX(), c.getZ()));
		short coords = Util.chunkCoordsToShort(b);
		
		if(bd == null)
		{
			map.remove(coords);
		}
		else
		{
			map.put(coords, bd);
		}
	}
	
	private void saveChunks()
	{
		Iterator<Entry<ChunkLocation, Map<Short, BlockData>>> iterator = blockData.entrySet().iterator();
		
		while(iterator.hasNext())
		{
			Entry<ChunkLocation, Map<Short, BlockData>> entry = iterator.next();
			ChunkLocation cl = entry.getKey();
			Map<Short, BlockData> value = entry.getValue();
			
			File file = Config.getFile(Config.Type.REGION, true, w.getName(), cl.getX() + "." + cl.getZ());
			
			try(FileOutputStream fileOut = new FileOutputStream(file);
					ByteArrayOutputStream streamOut = new ByteArrayOutputStream();
					ObjectOutputStream out = new ObjectOutputStream(streamOut))
			{
				Map<Short, byte[]> temp = new HashMap<>();
				
				for(Entry<Short, BlockData> entry1 : value.entrySet())
				{
					temp.put(entry1.getKey(), entry1.getValue().toByteArray());
				}
				
				out.writeObject(temp);
				fileOut.write(streamOut.toByteArray());
				
				if(!w.isChunkInUse(cl.getX(), cl.getZ()))
				{
					iterator.remove();
				}
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
		}
	}
	
	private ChunkLocation loadChunk(int x, int z)
	{
		ChunkLocation cl = new ChunkLocation(x, z);
		
		if(blockData.containsKey(cl))
		{
			return cl;
		}
		
		File file = Config.getFile(Config.Type.REGION, false, w.getName(), x + "." + z);
		
		if(!file.exists() || file.length() == 0L)
		{
			blockData.put(cl, new HashMap<>());
			return cl;
		}
		
		try(FileInputStream fileIn = new FileInputStream(file);
				ByteArrayInputStream streamIn = new ByteArrayInputStream(ByteStreams.toByteArray(fileIn));
				ObjectInputStream in = new ObjectInputStream(streamIn))
		{
			Map<Short, BlockData> temp = new HashMap<>();
			
			for(Entry<Short, byte[]> entry : ((Map<Short, byte[]>) in.readObject()).entrySet())
			{
				temp.put(entry.getKey(), new BlockData(entry.getValue()));
			}
			
			blockData.put(cl, temp);
		}
		catch(ClassNotFoundException | IOException ex)
		{
			blockData.put(cl, new HashMap<>());
			ex.printStackTrace();
		}
		
		return cl;
	}
	
	public Set<CommonPlayer> getCommonPlayers()
	{
		return w.getPlayers().stream().map(x -> CraftCommonPlayer.get(x)).collect(Collectors.toSet());
	}
	
	public boolean isSpawnProtection(Location l)
	{
		return true;
	}
	
	public long getChannelingTicks(SurvivalPlayer p)
	{
		return 0L;
	}
	
	public Result canTeleportAwaySpawn(SurvivalPlayer sp)
	{
		return Result.ALLOW;
	}
	
	public boolean canSetHome()
	{
		return false;
	}
	
	public int blocksAwayFromSpawnToSetHome()
	{
		return 0;
	}
	
	public Location getSpawnLocation()
	{
		return null;
	}
}