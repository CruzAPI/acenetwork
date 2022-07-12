package br.com.acenetwork.craftlandia.executor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.google.common.io.ByteStreams;

import br.com.acenetwork.commons.Common;
import br.com.acenetwork.commons.CommonsUtil;
import br.com.acenetwork.commons.event.SocketEvent;
import br.com.acenetwork.commons.manager.CommonsConfig;
import br.com.acenetwork.commons.manager.InsufficientBalanceException;
import br.com.acenetwork.commons.manager.Message;
import br.com.acenetwork.commons.player.CommonPlayer;
import br.com.acenetwork.commons.player.craft.CraftCommonPlayer;
import br.com.acenetwork.craftlandia.Main;
import br.com.acenetwork.craftlandia.Util;
import br.com.acenetwork.craftlandia.manager.Config;
import br.com.acenetwork.craftlandia.manager.Config.Type;
import br.com.acenetwork.craftlandia.manager.Land;
import br.com.acenetwork.craftlandia.manager.LandData;
import br.com.acenetwork.craftlandia.manager.SchematicSerializable;
import br.com.acenetwork.craftlandia.warp.Warp;
import br.com.acenetwork.craftlandia.warp.WarpLand;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class LandCMD implements TabExecutor, Listener
{
	private static final double RESET_COST_$BTA = 100.0D;
	private static final long RESET_COOLDOWN = 1L * 30L * 1000L;
	
	private static final Map<UUID, ResetOrder> MAP = new HashMap<>();
	
	private class ResetOrder
	{
		private final UUID uuid;
		private final int taskId;
		private final Land land;
		
		public ResetOrder(Land land, int taskId)
		{
			this.uuid = UUID.randomUUID();
			this.land = land;
			this.taskId = taskId;
		}
	}
	
	public LandCMD()
	{
		LandData.loadAll();
		Land.loadLands(Warp.MAP.entrySet().stream().filter(x -> (x.getValue() instanceof WarpLand)).findAny().orElse(null).getKey());
		Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String aliases, String[] args)
	{
		return new ArrayList<>();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String aliases, String[] args)
	{
		final ResourceBundle bundle;
		
		if(!(sender instanceof Player))
		{
			bundle = ResourceBundle.getBundle("message");
			sender.sendMessage(ChatColor.RED + bundle.getString("commons.cmds.cant-perform-command"));
			return true;
		}
		
		Player p = (Player) sender;
		CommonPlayer cp = CraftCommonPlayer.get(p);
		
		bundle = ResourceBundle.getBundle("message", cp.getLocale());
		
		if(args.length == 1 && args[0].equalsIgnoreCase("schem"))
		{
			for(Land land : Land.SET)
			{
				World w = land.getWorld();
				int maxHeight = w.getMaxHeight();
				
				BlockSerializable[][][] blocks = new BlockSerializable[land.getSize()][maxHeight][land.getSize()];
				
				for(int x = 0; x < blocks.length; x++)
				{
					for(int y = 0; y < blocks[x].length; y++)
					{
						for(int z = 0; z < blocks[x][y].length; z++)
						{
							Block b = w.getBlockAt(land.getMinX() + x, y, land.getMinZ() + z);
							
							blocks[x][y][z] = new BlockSerializable(b.getTypeId(), b.getData());
						}
					}
				}
				
				SchematicSerializable ss = new SchematicSerializable(0, 0, 0, blocks);
				
				File file = Config.getFile(Type.LAND_SCHEM, true, land.getId());
				
				try(FileOutputStream fileOut = new FileOutputStream(file);
						ByteArrayOutputStream streamOut = new ByteArrayOutputStream();
						ObjectOutputStream out = new ObjectOutputStream(streamOut))
				{
					out.writeObject(ss);
					fileOut.write(streamOut.toByteArray());
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
			}
			
			return true;
		}
		
		if(args.length == 3 && args[0].equalsIgnoreCase("reset") && args[1].equalsIgnoreCase("confirm"))
		{
			UUID uuid;
			
			try
			{
				uuid = UUID.fromString(args[2]);
			}
			catch(IllegalArgumentException e)
			{
				uuid = null;
			}
			
			ResetOrder order;
			
			if(uuid == null || (order = MAP.get(p.getUniqueId())) == null || !uuid.equals(order.uuid))
			{
				p.sendMessage(ChatColor.RED + bundle.getString("cmd.land.reset-order-not-found-or-expired"));
				return true;
			}
			
			MAP.remove(p.getUniqueId());
			
			long time = order.land.getResetCooldown() - System.currentTimeMillis();
			
			if(time > 0L)
			{
				TextComponent text = getCooldownText(time, bundle);
				p.sendMessage(text.toLegacyText());
				return true;
			}
			
			DecimalFormat df = new DecimalFormat("#.##", new DecimalFormatSymbols(bundle.getLocale()));
			
			try
			{
				cp.setBTA(cp.getBTA() - RESET_COST_$BTA);
				p.sendMessage(ChatColor.DARK_RED + "(-" + df.format(RESET_COST_$BTA) + " $BTA)");
			}
			catch(InsufficientBalanceException e)
			{
				p.sendMessage(ChatColor.RED + bundle.getString("commons.cmds.insufficient-balance"));
				return true;
			}
			
			df.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.getDefault()));
			
			TextComponent[] extra = new TextComponent[2];
			
			extra[0] = new TextComponent(p.getDisplayName());
			extra[1] = new TextComponent(order.land.getBeautyId());
			extra[1].setColor(ChatColor.YELLOW);
			TextComponent text = Message.getTextComponent(bundle.getString("cmd.land.log-land-reseting"), extra);
			text.setColor(ChatColor.GREEN);
			Bukkit.getConsoleSender().sendMessage(text.toLegacyText() + " " + ChatColor.DARK_RED + "(" + df.format(RESET_COST_$BTA) + " $BTA)");
			
			order.land.setResetCooldown(System.currentTimeMillis() + RESET_COOLDOWN);
			
			File file = Config.getFile(Type.LAND_SCHEM, false, order.land.getId());
			
			try(FileInputStream fileIn = new FileInputStream(file);
					ByteArrayInputStream streamIn = new ByteArrayInputStream(ByteStreams.toByteArray(fileIn));
					ObjectInputStream in = new ObjectInputStream(streamIn))
			{
				SchematicSerializable ss = (SchematicSerializable) in.readObject();
				
				BlockSerializable[][][] blocks = ss.getBlocks();
				World w = order.land.getWorld();
				
				for(int x = 0; x < blocks.length; x++)
				{
					for(int y = 0; y < blocks[x].length; y++)
					{
						for(int z = 0; z < blocks[x][y].length; z++)
						{
							Block b = w.getBlockAt(order.land.getMinX() + ss.getRelativeX() + x, 
									ss.getRelativeY() + y, 
									order.land.getMinZ() + ss.getRelativeZ() + z);
							
							BlockSerializable bs = blocks[x][y][z];
							b.setTypeIdAndData(bs.getId(), bs.getData(), false);
						}
					}
				}
				
				text = Message.getTextComponent(bundle.getString("cmd.land.log-land-reseting"), extra);
				text.setColor(ChatColor.GREEN);
				Bukkit.getConsoleSender().sendMessage(text.toLegacyText());
				
				extra = new TextComponent[1];
				
				extra[0] = new TextComponent(order.land.getBeautyId());
				extra[0].setColor(ChatColor.YELLOW);
				
				text = Message.getTextComponent(bundle.getString("cmd.land.reset-successfully"), extra);
				text.setColor(ChatColor.GREEN);
				p.spigot().sendMessage(text);
			}
			catch(IOException | ClassNotFoundException ex)
			{
				ex.printStackTrace();
				p.sendMessage(ChatColor.RED + bundle.getString("commons.unexpected-error"));
			}
		}
		else if(args.length == 2 && args[1].equalsIgnoreCase("reset"))
		{
			Land land = Land.getById(Integer.valueOf(args[0]) - 1);
			
			if(land == null)
			{
				p.sendMessage(ChatColor.RED + bundle.getString("cmd.land.land-not-found"));
				return true;
			}
			
			if(!land.isOwner(p.getUniqueId()))
			{
				p.sendMessage(ChatColor.RED + bundle.getString("commons.cmds.permission"));
				return true;
			}
			
			long time = land.getResetCooldown() - System.currentTimeMillis();
			
			if(time > 0L)
			{
				TextComponent text = getCooldownText(time, bundle);
				p.sendMessage(text.toLegacyText());
				return true;
			}
			
			if(cp.getBTA() - RESET_COST_$BTA < 0.0D)
			{
				p.sendMessage(ChatColor.RED + bundle.getString("commons.cmds.insufficient-balance"));
				return true;
			}
			
			int taskId = Bukkit.getScheduler().scheduleSyncDelayedTask(Common.getPlugin(), () ->
			{
				MAP.remove(p.getUniqueId());
			}, 20L * 10L);
			
			ResetOrder order = new ResetOrder(land, taskId);
			put(p.getUniqueId(), order);
			
			TextComponent[] extra;
			
			extra = new TextComponent[1];
			
			extra[0] = new TextComponent(land.getBeautyId());
			extra[0].setColor(ChatColor.WHITE);
			
			TextComponent text1 = Message.getTextComponent(bundle.getString("cmd.land.reset-land"), extra);
			text1.setColor(ChatColor.GRAY);
			
			TextComponent append = new TextComponent(" (" + bundle.getString("phrase.factory-reset") +")");
			append.setColor(ChatColor.DARK_GRAY);
			
			text1.addExtra(append);
			
			
			TextComponent text2 = new TextComponent(bundle.getString("noun.warn").toUpperCase() + ": ");
			text2.setColor(ChatColor.RED);
			text2.setBold(true);
			
			append = new TextComponent(bundle.getString("cmd.land.reset-warn"));
			append.setColor(ChatColor.GRAY);
			append.setBold(false);
			text2.addExtra(append);
			
			
			TextComponent text3 = new TextComponent(StringUtils.capitalize(bundle.getString("noun.cost")) + ": ");
			text3.setColor(ChatColor.GRAY);
			
			DecimalFormat df = new DecimalFormat("#.##", new DecimalFormatSymbols(bundle.getLocale()));
			
			append = new TextComponent("" + df.format(RESET_COST_$BTA) + " $BTA");
			append.setColor(ChatColor.WHITE);
			text3.addExtra(append);
			
			TextComponent text5 = new TextComponent("                       [");
			text5.setColor(ChatColor.DARK_GRAY);
			
			TextComponent confirm = new TextComponent(bundle.getString("verb.confirm").toUpperCase());
			confirm.setColor(ChatColor.DARK_GREEN);
			confirm.setBold(true);
			confirm.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" + aliases + " reset confirm " + order.uuid));
			confirm.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, 
					new ComponentBuilder(StringUtils.capitalize(bundle.getString("commons.click-to-confirm")))
					.color(ChatColor.GREEN).create()));
			
			text5.addExtra(confirm);
			text5.addExtra("]");
			
			p.sendMessage(ChatColor.GRAY + "============ " + ChatColor.WHITE + ChatColor.BOLD 
					+ bundle.getString("cmd.land.reset-confirmation") + ChatColor.GRAY + " ============");
			p.sendMessage("");
			p.spigot().sendMessage(text1);
			p.sendMessage("");
			p.spigot().sendMessage(text2);
			p.sendMessage("");
			p.spigot().sendMessage(text3);
			p.sendMessage("");
			p.spigot().sendMessage(text5);
			p.sendMessage(ChatColor.GRAY + "===========================================");
			
			return true;
		}
		
		if(args.length == 3 && args[0].equalsIgnoreCase("test"))
		{
			Land land = Land.getById(Integer.valueOf(args[1]));
			OfflinePlayer op = CommonsUtil.getOfflinePlayerIfCached(args[2]);
			land.setOwner(op == null ? null : op.getUniqueId());
			return true;
		}
		
		if(args.length == 0)
		{
			TextComponent[] array = new TextComponent[1];
			
			array[0] = new TextComponent("");
			
			Iterator<Land> iterator = Land.SET.iterator();
			
			int count = 0;
			
			while(iterator.hasNext())
			{
				Land land = iterator.next();
				
				if(!land.isOwner(p.getUniqueId()))
				{
					continue;
				}
				
				if(count > 0)
				{
					array[0].addExtra(", ");
				}
				
				TextComponent extra = new TextComponent(land.getBeautyId());
				extra.setColor(ChatColor.YELLOW);
				extra.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/land " + land.getBeautyId()));
				extra.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(
						bundle.getString("hover.click-to-teleport")).create()));
				array[0].addExtra(extra);
				
				count++;
			}
			
			if(count == 0)
			{
				File linksFile = CommonsConfig.getFile(CommonsConfig.Type.LINKS, true);
				YamlConfiguration linksConfig = YamlConfiguration.loadConfiguration(linksFile);
				
				TextComponent[] extra = new TextComponent[1];
				
				String url = linksConfig.getString("lands");
				
				extra[0] = new TextComponent(url);
				extra[0].setColor(ChatColor.GRAY);
				extra[0].setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url));
				
				TextComponent text = Message.getTextComponent(bundle.getString("cmd.land.you-do-not-have-lands"), extra);
				text.setColor(ChatColor.RED);
				p.spigot().sendMessage(text);
				return true;
			}
			
			TextComponent text = Message.getTextComponent(bundle.getString("cmd.land.your-lands"), array);
			text.setColor(ChatColor.GREEN);
			p.spigot().sendMessage(text);
		}
		else if(args.length == 1 && args[0].equalsIgnoreCase("sync"))
		{
			try
			{
				p.sendMessage(ChatColor.GREEN + bundle.getString("cmd.land.sync-lands"));
				Runtime.getRuntime().exec(String.format("node %s/reset/uuidland %s %s %s", System.getProperty("user.home"), 
						Common.getSocketPort(), cp.requestDatabase(), p.getUniqueId()));
			}
			catch(IOException e)
			{
				e.printStackTrace();
				sender.sendMessage(ChatColor.RED + bundle.getString("commons.unexpected-error"));
			}
		}
		else if(args.length == 1)
		{
			try
			{
				int id = Integer.valueOf(args[0]) - 1;
				
				Land land = Land.getById(id);
				
				if(land == null)
				{
					p.sendMessage(ChatColor.RED + bundle.getString("cmd.land.land-not-found"));
					return true;
				}
				
				if(!land.isTrusted(p)) // && !cp.hasPermission("land.bypass"))
				{
					p.sendMessage(ChatColor.RED + bundle.getString("commons.cmds.permission"));
					return true;
				}
				
				final World w = land.getWorld();
				
				if(p.getWorld() != w)
				{
					p.sendMessage(ChatColor.RED + bundle.getString("cmds.can-not-teleport-between-dimensions"));
					return true;
				}
				
				Location homeLocation = land.getLocation();
				
				if(homeLocation != null)
				{
					p.teleport(homeLocation);
					return true;
				}
				
				final int x = land.getX();
				final int z = land.getZ();
				
				int y = 1 + CommonsUtil.getHighestBlockYAt(w, x, z);
				
				if(y == w.getMaxHeight())
				{
					y = 64;
					Block b = w.getBlockAt(x, y - 1, z);
					b.setType(Material.GLASS);
					Util.writeBlock(b, new byte[] {1, -128});
				}
				
				p.teleport(new Location(w, x + 0.5D, y, z + 0.5D, -135.0F, 0.0F));
			}
			catch(NumberFormatException e)
			{
				p.sendMessage(ChatColor.RED + bundle.getString("commons.cmds.invalid-number-format"));
			}
		}
		
		return true;
	}
	
	private TextComponent getCooldownText(long time, ResourceBundle bundle)
	{
		long seconds = time / 1000L % 60;
		long minutes = time / (60L * 1000L) % 60;
		long hours = time / (60L * 60L * 1000L) % 24;
		long days = time / (24L * 60L * 60L * 1000L);
		
		String d = bundle.getString("commons.words.day").substring(0, 1);
		String h = bundle.getString("commons.words.hour").substring(0, 1);
		String m = bundle.getString("commons.words.minute").substring(0, 1);
		String s = bundle.getString("commons.words.second").substring(0, 1);
		
		String msg = "";
		
		if(days != 0L)
		{
			msg = days + d + " " + hours + h + " " + minutes + m + " " + seconds + s;
		}
		else if(hours != 0L)
		{
			msg = hours + h + " " + minutes + m + " " + seconds + s;
		}
		else if(minutes != 0L)
		{
			msg = minutes + m + " " + seconds + s;
		}
		else
		{
			msg = seconds + s;
		}
		
		TextComponent[] extra = new TextComponent[1];
		
		extra[0] = new TextComponent(msg);
		
		TextComponent text = Message.getTextComponent(bundle.getString("cmd.land.cooldown"), extra);
		text.setColor(ChatColor.RED);
		return text;
	}

	private void put(UUID uuid, ResetOrder order)
	{
		ResetOrder old = MAP.put(uuid, order);
		
		if(old != null)
		{
			Bukkit.getScheduler().cancelTask(old.taskId);
		}
	}
	
	@EventHandler
	public void oi(SocketEvent e)
	{
		String[] args = e.getArgs();
		String cmd = args[0];
		
		if(cmd.equals("uuidland"))
		{
			int taskId = Integer.valueOf(args[1]);
			OfflinePlayer op = Bukkit.getOfflinePlayer(UUID.fromString(args[2]));
			
			if(op == null)
			{
				return;
			}
			
			for(int i = 3; i < args.length; i++)
			{
				int id = Integer.valueOf(args[i]);
				
				Land land = Land.getById(id);
				
				if(land != null)
				{
					land.setOwner(op.getUniqueId());
				}
			}
			
			if(Bukkit.getScheduler().isQueued(taskId))
			{
				Bukkit.getScheduler().cancelTask(taskId);
				
				if(op.isOnline())
				{
					Bukkit.dispatchCommand(op.getPlayer(), "land");
				}
			}
		}
	}
}