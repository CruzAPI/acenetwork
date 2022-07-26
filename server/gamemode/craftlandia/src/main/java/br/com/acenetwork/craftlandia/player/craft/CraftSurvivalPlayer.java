package br.com.acenetwork.craftlandia.player.craft;

import java.util.HashSet;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.Inventory;

import br.com.acenetwork.commons.CommonsUtil;
import br.com.acenetwork.commons.manager.Message;
import br.com.acenetwork.commons.player.craft.CraftCommonPlayer;
import br.com.acenetwork.craftlandia.Main;
import br.com.acenetwork.craftlandia.Util;
import br.com.acenetwork.craftlandia.listener.PlayerData;
import br.com.acenetwork.craftlandia.manager.BlockData;
import br.com.acenetwork.craftlandia.manager.ChannelCommand;
import br.com.acenetwork.craftlandia.player.SurvivalPlayer;
import br.com.acenetwork.craftlandia.warp.Warp;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

public class CraftSurvivalPlayer extends CraftCommonPlayer implements SurvivalPlayer
{
	private int channelTaskId;
	private long channelTaskTicks;
	private long channelTaskTotalTicks;
	private PlayerData playerData;

	public CraftSurvivalPlayer(Player p)
	{
		super(p);
		playerData = PlayerData.load(p.getUniqueId());
	}
	
	@Override
	public void channel(final ChannelCommand channel, long ticks, Location destiny, Object... args)
	{
		if(channelTaskId != 0)
		{
			return;
		}
		
		long seconds = ticks / 20L;
		
		if(ticks > 0L)
		{
			ResourceBundle bundle = ResourceBundle.getBundle("message", getLocale());
			
			TextComponent[] extra = new TextComponent[1];
			ChatColor textColor = ChatColor.GREEN;
			
			extra[0] = new TextComponent(seconds + " ");
			extra[0].setColor(ChatColor.YELLOW);
			extra[0].addExtra(textColor + bundle.getString("commons.words.seconds"));
			
			TextComponent text = Message.getTextComponent(bundle.getString("raid.cmds.channeling"), extra);
			text.setColor(textColor);
			p.spigot().sendMessage(text);
			
			channelTaskTicks = ticks;
			channelTaskTotalTicks = channelTaskTicks;
			
			channelTaskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), new Runnable()
			{
				final int tiles = 40;
				Location location = p.getLocation().clone();
				
				@Override
				public void run()
				{
					float f = (float) channelTaskTicks / (float) channelTaskTotalTicks;
					int redTiles = (int) (f * tiles);
					
					TextComponent extra = new TextComponent(StringUtils.repeat('▍', redTiles));
					extra.setColor(ChatColor.RED);
					
					TextComponent text = new TextComponent(StringUtils.repeat('▍', tiles - redTiles));
					text.setColor(ChatColor.GREEN);
					text.addExtra(extra);
					
					sendActionBarMessage(text.toLegacyText());
					
					if(p.isSneaking() && cancelChannel(false))
					{
						return;
					}
					
					if(p.getWorld() != location.getWorld() || p.getLocation().getX() != location.getX() 
							|| p.getLocation().getY() != location.getY() || p.getLocation().getZ() != location.getZ())
					{
						if(cancelChannel(false))
						{
							return;
						}
						
						location = p.getLocation().clone();
					}
					
					if(channelTaskTicks <= 0 && channelTaskId != 0)
					{
						Bukkit.getScheduler().cancelTask(channelTaskId);
						channelTaskId = 0;
						
						sendActionBarMessage("");
						channel.run(CraftSurvivalPlayer.this, destiny, args);
						return;
					}
					
					channelTaskTicks--;
				}
			}, 0L, 1L);
		}
		else
		{
			channel.run(CraftSurvivalPlayer.this, destiny, args);
		}
	}
	
	@Override
	public void reset()
	{
		setInvis(false);
		p.setGameMode(GameMode.SURVIVAL);
	}
	
	@Override
	public boolean cancelChannel(boolean force)
	{
		if(channelTaskId == 0 || (!force && channelTaskTotalTicks - channelTaskTicks <= 35L))
		{
			return false;
		}
		
		Bukkit.getScheduler().cancelTask(channelTaskId);
		channelTaskId = 0;
		
		ResourceBundle bundle = ResourceBundle.getBundle("message", getLocale());
		
		sendActionBarMessage("");
		
		TextComponent text = new TextComponent(bundle.getString("raid.cmds.teleport-cancelled"));
		text.setColor(ChatColor.RED);
		p.spigot().sendMessage(text);
		
		return true;
	}
	
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void aa(PlayerInteractEvent e)
	{
		if(e.getPlayer() != p)
		{
			return;
		}
		
		cancelChannel(false);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void aaa(PlayerCommandPreprocessEvent e)
	{
		if(e.getPlayer() != p)
		{
			return;
		}
		
		if(channelTaskId != 0)
		{
			e.setCancelled(true);
			
			ResourceBundle bundle = ResourceBundle.getBundle("message", getLocale());
			
			TextComponent text = new TextComponent(bundle.getString("raid.event.player-command-preprocess.cant-perform-cmd-while-channeling"));
			text.setColor(ChatColor.RED);
			p.spigot().sendMessage(text);
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void aa(EntityDamageEvent e)
	{
		if(e.getEntity() != p)
		{
			return;
		}
		
		cancelChannel(false);
	}
	
	@EventHandler
	public void a(PlayerDeathEvent e)
	{
		if(e.getEntity() != p)
		{
			return;
		}
		
		cancelChannel(true);
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void a(InventoryOpenEvent e)
	{
		if(e.getPlayer() != p)
		{
			return;
		}
		
		Inventory inv = e.getInventory();
		
		if(inv.getType() == InventoryType.CRAFTING)
		{
			return;
		}
		
		cancelChannel(true);
	}
	
	@EventHandler
	public void a(PlayerQuitEvent e)
	{
		if(e.getPlayer() != p)
		{
			return;
		}
		
		cancelChannel(true);
	}
	
	@Override
	public PlayerData getPlayerData()
	{
		return playerData;
	}
	
	@Override
	public void setPlayerData(PlayerData playerData)
	{
		this.playerData = playerData;
	}
	
	@EventHandler
	public void a(PlayerRespawnEvent e)
	{
		if(e.getPlayer() != p)
		{
			return;
		}
		
		int[] coords = playerData.getBedHomes().get(p.getWorld().getUID());
		
		Warp warp = Warp.getByWorld(p.getWorld());
		Block b = coords == null || coords.length != 3 ? null : p.getWorld().getBlockAt(coords[0], coords[1], coords[2]);
		
		if(p.getBedSpawnLocation() == null)
		{
			p.setBedSpawnLocation(warp.getSpawnLocation(), true);
		}
		
		if(CommonsUtil.isBedObstructed(b))
		{
			p.sendMessage("BED OBSTRUCTED OR NOT FOUND!");
			e.setRespawnLocation(warp.getSpawnLocation());
			setInvincibility(true);
			setPVPInvincibility(true);
			return;
		}
		
		e.setRespawnLocation(new Location(b.getWorld(), b.getX() + 0.5D, b.getY() + 0.5625, b.getZ() + 0.5D));
	}
	
	@EventHandler(priority =  EventPriority.MONITOR, ignoreCancelled = true)
	public void a(PlayerBedEnterEvent e)
	{
		if(e.getPlayer() != p)
		{
			return;
		}
		
		Block bed = e.getBed();
		World w = bed.getWorld();
		
		if(w.getEnvironment() != Environment.NORMAL)
		{
			return;
		}
		
		Bukkit.broadcastMessage(bed.getX() + " " + bed.getY() + " " + bed.getZ());
		
		BlockData data = Optional.ofNullable(Util.readBlock(bed)).orElse(new BlockData());
		Set<UUID> bedPlayers = Optional.ofNullable(data.getBed()).orElse(data.setBed(new HashSet<>()));
		bedPlayers.add(p.getUniqueId());
		Util.writeBlock(bed, data);
		playerData.getBedHomes().put(w.getUID(), new int[] {bed.getX(), bed.getY(), bed.getZ()});
	}
}