package br.com.acenetwork.craftlandia.executor;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
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
import br.com.acenetwork.craftlandia.Property;
import br.com.acenetwork.craftlandia.Rarity;
import br.com.acenetwork.craftlandia.Util;
import br.com.acenetwork.craftlandia.manager.BlockData;
import br.com.acenetwork.craftlandia.manager.BlockSerializable;
import br.com.acenetwork.craftlandia.manager.Config;
import br.com.acenetwork.craftlandia.manager.Land;
import br.com.acenetwork.craftlandia.manager.LandData;
import br.com.acenetwork.craftlandia.manager.NameAlreadyInUseException;
import br.com.acenetwork.craftlandia.manager.SchematicSerializable;
import br.com.acenetwork.craftlandia.manager.Config.Type;
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
	private static final long RESET_COOLDOWN = 30L * 24L * 60L * 60L * 1000L;
	
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
		
//		if(args.length == 1 && args[0].equalsIgnoreCase("schem"))
//		{
//			for(Land land : Land.SET)
//			{
//				World w = land.getWorld();
//				int maxHeight = w.getMaxHeight();
//				
//				BlockSerializable[][][] blocks = new BlockSerializable[land.getSize()][maxHeight][land.getSize()];
//				
//				for(int x = 0; x < blocks.length; x++)
//				{
//					for(int y = 0; y < blocks[x].length; y++)
//					{
//						for(int z = 0; z < blocks[x][y].length; z++)
//						{
//							Block b = w.getBlockAt(land.getMinX() + x, y, land.getMinZ() + z);
//							
//							blocks[x][y][z] = new BlockSerializable(b.getTypeId(), b.getData());
//						}
//					}
//				}
//				
//				SchematicSerializable ss = new SchematicSerializable(0, 0, 0, blocks);
//				
//				File file = Config.getFile(Type.LAND_SCHEM, true, land.getId());
//				
//				try(FileOutputStream fileOut = new FileOutputStream(file);
//						ByteArrayOutputStream streamOut = new ByteArrayOutputStream();
//						ObjectOutputStream out = new ObjectOutputStream(streamOut))
//				{
//					out.writeObject(ss);
//					fileOut.write(streamOut.toByteArray());
//				}
//				catch(IOException e)
//				{
//					e.printStackTrace();
//				}
//			}
//			
//			return true;
//		}
//		
//		if(args.length == 3 && args[0].equalsIgnoreCase("test"))
//		{
//			Land land = Land.getById(Integer.valueOf(args[1]));
//			OfflinePlayer op = CommonsUtil.getOfflinePlayerIfCached(args[2]);
//			land.setOwner(op == null ? null : op.getUniqueId());
//			return true;
//		}
		
		if((args.length == 1 && (args[0].equalsIgnoreCase("identify") || args[0].equalsIgnoreCase("id") || args[0].equalsIgnoreCase("info")))
				|| args.length == 2 && (args[1].equalsIgnoreCase("identify") || args[1].equalsIgnoreCase("id") || args[1].equalsIgnoreCase("info")))
		{
			Location l  = p.getLocation();
			
			Land land = null;
			
			if(args.length == 2)
			{
				land = Land.getLandByUserInput(args[0]);
			}
			else
			{
				for(Land lands : Land.SET)
				{
					if(lands.isLand(l))
					{
						land = lands;
						break;
					}
				}
			}
			
			if(land == null)
			{
				p.sendMessage(ChatColor.RED + bundle.getString("cmd.land.land-not-found"));
				return true;
			}
			
			p.sendMessage("");
			p.sendMessage(ChatColor.GREEN + StringUtils.capitalize(bundle.getString("noun.land")) + ": " + ChatColor.YELLOW + land.getBeautyId());
			
			p.sendMessage(ChatColor.GREEN + StringUtils.capitalize(bundle.getString("noun.size")) + ": " + ChatColor.YELLOW
					+ bundle.getString("size." + land.getType().name().toLowerCase()).toUpperCase() + ChatColor.GRAY 
					+ " (" + land.getSize() + "x" + land.getSize() + ")");
			
			p.sendMessage(ChatColor.GREEN + StringUtils.capitalize(bundle.getString("noun.biome")) + ": " + Land.getColor(land.getBiome()) 
			+ WordUtils.capitalize(land.getBiome().name().replace('_', ' ').toLowerCase()));
			
			if(land.getName() != null || land.hasOwner())
			{
				p.sendMessage("");
			}
			
			if(land.getName() != null)
			{
				p.sendMessage(ChatColor.GREEN + StringUtils.capitalize(bundle.getString("noun.name")) + ": " + ChatColor.YELLOW + land.getName());
			}
			
			if(land.hasOwner())
			{
				OfflinePlayer op = Bukkit.getOfflinePlayer(land.getOwner());
				String displayName = op.isOnline() ? op.getPlayer().getDisplayName() : ChatColor.GRAY + op.getName();
				p.sendMessage(ChatColor.GREEN + StringUtils.capitalize(bundle.getString("noun.owner")) + ": " + displayName);
			}
			
			p.sendMessage("");
			
			return true;
		}
		else if(args.length == 2 && args[1].equalsIgnoreCase("setspawnlocation"))
		{
			Land land = Land.getLandByUserInput(args[0]);
			
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
			
			Location l = p.getLocation();
			
			if(!land.isLand(l))
			{
				TextComponent[] extra = new TextComponent[1];
				extra[0] = new TextComponent(land.getBeautyName());
				TextComponent text = Message.getTextComponent(bundle.getString("cmd.land.you-need-to-be-in-land"), extra);
				text.setColor(ChatColor.RED);
				p.spigot().sendMessage(text);
				return true;
			}
			
			TextComponent[] extra = new TextComponent[1];
			extra[0] = new TextComponent(land.getBeautyName());
			extra[0].setColor(ChatColor.YELLOW);
			TextComponent text = Message.getTextComponent(bundle.getString("cmd.land.spawn-location-reset"), extra);
			text.setColor(ChatColor.GREEN);
			p.spigot().sendMessage(text);
			land.setLocation(l);
		}
		else if(args.length == 2 && args[1].equalsIgnoreCase("trust"))
		{
			Land land = Land.getLandByUserInput(args[0]);
			
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
			
			Set<UUID> treeSet = new TreeSet<>(new Comparator<UUID>()
			{
				@Override
				public int compare(UUID arg0, UUID arg1)
				{
					return Bukkit.getOfflinePlayer(arg0).getName().compareTo(Bukkit.getOfflinePlayer(arg1).getName());
				}
			});
			
			treeSet.addAll(land.getTrustedPlayers());
			Iterator<UUID> iterator = treeSet.iterator();
			
			TextComponent[] extra;
			extra = new TextComponent[1];
			extra[0] = new TextComponent(land.getBeautyName());
			
			TextComponent text;
			
			if(!iterator.hasNext())
			{
				text = Message.getTextComponent(bundle.getString("cmd.land.trust-list-empty"), extra);
				text.setColor(ChatColor.RED);
				p.spigot().sendMessage(text);
				return true;
			}
			
			extra = new TextComponent[2];
			extra[0] = new TextComponent(land.getBeautyName());
			extra[0].setColor(ChatColor.YELLOW);
			
			extra[1] = new TextComponent("");
			
			while(iterator.hasNext())
			{
				OfflinePlayer op = Bukkit.getOfflinePlayer(iterator.next());
				
				extra[1].addExtra(op.isOnline() ? op.getPlayer().getDisplayName() : ChatColor.GRAY + op.getName());
				extra[1].addExtra(" ");
				TextComponent untrust = new TextComponent("x"); //✕
				untrust.setColor(ChatColor.RED);
				untrust.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, 
						"/land " + land.getBeautyId() + " untrust " + op.getName() + " /land"));
				untrust.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(
						StringUtils.capitalize(bundle.getString("verb.untrust"))).color(ChatColor.RED).create()));
				extra[1].addExtra(untrust);
				
				if(iterator.hasNext())
				{
					extra[1].addExtra(", ");
				}
			}
			
			text = Message.getTextComponent(bundle.getString("cmd.land.trust-list"), extra);
			text.setColor(ChatColor.GREEN);
			p.spigot().sendMessage(text);
		}
		else if((args.length == 3 || args.length == 4 && args[3].equalsIgnoreCase("/land")) && (args[1].equalsIgnoreCase("trust") || args[1].equalsIgnoreCase("untrust")))
		{
			Land land = Land.getLandByUserInput(args[0]);
			
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
			
			OfflinePlayer op = CommonsUtil.getOfflinePlayerIfCached(args[2]);
			
			if(op == null)
			{
				p.sendMessage(ChatColor.RED + bundle.getString("commons.cmds.user-not-found"));
				return true;
			}
			
			boolean trust = args[1].equalsIgnoreCase("trust");
			
			TextComponent[] extra;
			TextComponent text;
			
			extra = new TextComponent[2];
			extra[0] = new TextComponent(op.isOnline() ? op.getPlayer().getDisplayName() : ChatColor.GRAY + op.getName());
			extra[1] = new TextComponent(land.getBeautyName());
			
			if(trust)
			{
				if(land.getTrustedPlayers().size() >= land.getTrustedPlayerLimit())
				{
					extra = new TextComponent[1];
					extra[0] = new TextComponent(land.getBeautyName());
					text = Message.getTextComponent(bundle.getString("cmd.land.trust-list-is-full"), extra);
					text.setColor(ChatColor.RED);
					p.spigot().sendMessage(text);
					return true;
				}
				
				if(land.getTrustedPlayers().add(op.getUniqueId()))
				{
					extra[1].setColor(ChatColor.YELLOW);
					text = Message.getTextComponent(bundle.getString("cmd.land.user-trusted"), extra);
					text.setColor(ChatColor.GREEN);
					p.spigot().sendMessage(text);
				}
				else
				{
					text = Message.getTextComponent(bundle.getString("cmd.land.user-already-trusted"), extra);
					text.setColor(ChatColor.RED);
					p.spigot().sendMessage(text);
				}
			}
			else
			{				
				if(land.getTrustedPlayers().remove(op.getUniqueId()))
				{
					extra[1].setColor(ChatColor.YELLOW);
					text = Message.getTextComponent(bundle.getString("cmd.land.user-untrusted"), extra);
					text.setColor(ChatColor.GREEN);
					p.spigot().sendMessage(text);
				}
				else
				{
					text = Message.getTextComponent(bundle.getString("cmd.land.user-already-untrusted"), extra);
					text.setColor(ChatColor.RED);
					p.spigot().sendMessage(text);
				}
			}
			
			if(args.length == 4)
			{
				Bukkit.dispatchCommand(p, "land " + land.getBeautyId() + " trust");
			}
		}
		else if(args.length == 3 && args[0].equalsIgnoreCase("reset") && args[1].equalsIgnoreCase("confirm"))
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
			df.setGroupingSize(3);
			df.setGroupingUsed(true);
			
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
			Bukkit.getConsoleSender().sendMessage(text.toLegacyText() + " " + ChatColor.DARK_RED + "(-" + df.format(RESET_COST_$BTA) + " $BTA)");
			
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
				
				try
				{
					order.land.setName(null);
				}
				catch(NameAlreadyInUseException e)
				{
					
				}
				
				text = Message.getTextComponent(bundle.getString("cmd.land.log-land-reset"), extra);
				text.setColor(ChatColor.GREEN);
				Bukkit.getConsoleSender().sendMessage(text.toLegacyText());
				
				extra = new TextComponent[1];
				
				extra[0] = new TextComponent(order.land.getBeautyName());
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
			Land land = Land.getLandByUserInput(args[0]);
			
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
			
			int taskId = Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () ->
			{
				MAP.remove(p.getUniqueId());
			}, 20L * 10L);
			
			ResetOrder order = new ResetOrder(land, taskId);
			put(p.getUniqueId(), order);
			
			TextComponent[] extra;
			
			extra = new TextComponent[1];
			
			extra[0] = new TextComponent(land.getBeautyName());
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
		else if(args.length == 3 && args[1].equalsIgnoreCase("rename"))
		{
			Land land = Land.getLandByUserInput(args[0]);
			
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
			
			String rename = args[2];
			
			if(!rename.matches("\\w*"))
			{
				p.sendMessage(ChatColor.RED + bundle.getString("cmds.invalid-characters"));
				return true;
			}
			
			
			if(!rename.matches(".{3,16}"))
			{
				TextComponent[] extra = new TextComponent[2];
				
				extra[0] = new TextComponent("3");
				extra[1] = new TextComponent("16");
				
				TextComponent text = Message.getTextComponent(bundle.getString("cmd.land.name-length"), extra);
				text.setColor(ChatColor.RED);
				p.spigot().sendMessage(text);
				return true;
			}
			
			if(!rename.matches(".*[a-zA-Z]+.*"))
			{
				p.sendMessage(ChatColor.RED + bundle.getString("cmd.land.at-least-one-letter"));
				return true;
			}
			
			try
			{
				land.setName(rename);
				
				TextComponent[] extra = new TextComponent[2];
				
				extra[0] = new TextComponent(land.getBeautyId());
				extra[0].setColor(ChatColor.YELLOW);
				
				extra[1] = new TextComponent(rename);
				extra[1].setColor(ChatColor.YELLOW);
				
				TextComponent text = Message.getTextComponent(bundle.getString("cmd.land.land-renamed"), extra);
				text.setColor(ChatColor.GREEN);
				p.spigot().sendMessage(text);
			}
			catch(NameAlreadyInUseException e)
			{
				p.sendMessage(ChatColor.RED + bundle.getString("cmd.land.name-already-in-use"));
			}
		}
		else if(args.length == 0)
		{
			TextComponent[] publicLands = new TextComponent[1];
			
			publicLands[0] = new TextComponent("");
			
			TextComponent[] array = new TextComponent[1];
			
			array[0] = new TextComponent("");
			
			Iterator<Land> iterator = Land.SET.iterator();
			
			int publicCount = 0;
			int count = 0;
			
			while(iterator.hasNext())
			{
				Land land = iterator.next();
				
				if(land.isPublic() && land.hasOwner())
				{
					if(publicCount++ > 0)
					{
						publicLands[0].addExtra(", ");
					}
					
					TextComponent text = new TextComponent(land.getBeautyName());
					
					if(land.getWorld() == p.getWorld() || cp.hasPermission("portals.bypass"))
					{
						text.setColor(ChatColor.YELLOW);
						text.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/land " + land.getBeautyId()));
						text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, 
								new ComponentBuilder(bundle.getString("hover.click-to-teleport"))
								.color(ChatColor.YELLOW).create()));
					}
					else
					{
						text.setColor(ChatColor.RED);
						text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, 
								new ComponentBuilder(bundle.getString("cmd.home.different-world"))
								.color(ChatColor.RED).create()));
					}
					
					publicLands[0].addExtra(text);
				}
				
				if(!land.isTrusted(p.getUniqueId()))
				{
					continue;
				}
				
				if(count > 0)
				{
					array[0].addExtra(", ");
				}
				
				TextComponent text = new TextComponent(land.getBeautyName());
				
				if(land.getWorld() == p.getWorld() || cp.hasPermission("portals.bypass"))
				{
					text.setColor(ChatColor.YELLOW);
					text.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/land " + land.getBeautyId()));
					text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, 
							new ComponentBuilder(bundle.getString("hover.click-to-teleport"))
							.color(ChatColor.YELLOW).create()));
				}
				else
				{
					text.setColor(ChatColor.RED);
					text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, 
							new ComponentBuilder(bundle.getString("cmd.home.different-world"))
							.color(ChatColor.RED).create()));
				}
				
				array[0].addExtra(text);
				
				text = new TextComponent("■");
				
				if(land.isPublic())
				{
					text.setColor(ChatColor.DARK_GREEN);
					text.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/land " + land.getBeautyId() + " private /land"));
					text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, 
							new ComponentBuilder(StringUtils.capitalize(bundle.getString("cmd.land.adjective.public")))
							.color(ChatColor.DARK_GREEN).create()));
				}
				else
				{
					text.setColor(ChatColor.DARK_RED);
					text.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/land " + land.getBeautyId() + " public /land"));
					text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, 
							new ComponentBuilder(StringUtils.capitalize(bundle.getString("cmd.land.adjective.private")))
							.color(ChatColor.DARK_RED).create()));
				}
				
				array[0].addExtra(text);
				
				count++;
			}
			
			if(publicCount > 0)
			{
				TextComponent text = Message.getTextComponent(bundle.getString("cmd.land.public-lands"), publicLands);
				text.setColor(ChatColor.GREEN);
				p.spigot().sendMessage(text);
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
			Land land = Land.getLandByUserInput(args[0]);
			
			if(land == null)
			{
				p.sendMessage(ChatColor.RED + bundle.getString("cmd.land.land-not-found"));
				return true;
			}
			
			if(!land.isPublic() && !land.isTrusted(p) && !cp.hasPermission("land.bypass"))
			{
				p.sendMessage(ChatColor.RED + bundle.getString("commons.cmds.permission"));
				return true;
			}
			
			final World w = land.getWorld();
			
			if(p.getWorld() != w && !cp.hasPermission("portals.bypass"))
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
			
			if(y >= w.getMaxHeight())
			{
				y = 63;
				Block b = w.getBlockAt(x, y - 1, z);
				b.setType(Material.GLASS);
				
				BlockData data = new BlockData();
				
				data.setRarity(Rarity.COMMON);
				data.setProperties(new HashSet<>());
				data.getProperties().add(Property.SOLD);
				
				Util.writeBlock(b, data);
			}
			
			p.teleport(new Location(w, x + 0.5D, y, z + 0.5D, -135.0F, 0.0F));
		}
		else if((args.length == 2 || args.length == 3 && args[2].equals("/land")) 
				&& (args[1].equalsIgnoreCase("public") || args[1].equalsIgnoreCase("private")))
		{
			Land land = Land.getLandByUserInput(args[0]);
			
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
			
			boolean setPublic = args[1].equalsIgnoreCase("public");
			
			if(land.isPublic() == setPublic)
			{
				TextComponent[] extra = new TextComponent[1];
				
				if(land.isPublic())
				{
					extra[0] = new TextComponent(bundle.getString("cmd.land.adjective.public"));
				}
				else
				{
					extra[0] = new TextComponent(bundle.getString("cmd.land.adjective.private"));
				}
				
				TextComponent text = Message.getTextComponent(bundle.getString("cmd.land.land-already-public"), extra);
				text.setColor(ChatColor.RED);
				p.spigot().sendMessage(text);
				
				return true;
			}
			
			if(!land.setPublic(setPublic))
			{
				return true;
			}
			
			TextComponent[] extra = new TextComponent[2];
			
			extra[0] = new TextComponent(land.getBeautyName());
			extra[0].setColor(ChatColor.YELLOW);
			
			if(land.isPublic())
			{
				extra[1] = new TextComponent(bundle.getString("cmd.land.adjective.public"));
				extra[1].setColor(ChatColor.DARK_GREEN);
			}
			else
			{
				extra[1] = new TextComponent(bundle.getString("cmd.land.adjective.private"));
				extra[1].setColor(ChatColor.DARK_RED);
			}
			
			TextComponent text = Message.getTextComponent(bundle.getString("cmd.land.land-is-public-now"), extra);
			text.setColor(ChatColor.GREEN);
			p.spigot().sendMessage(text);
			
			if(args.length == 3)
			{
				Bukkit.dispatchCommand(p, "land");
			}
		}
		else
		{
			TextComponent[] extra = new TextComponent[1];
			
			String nounHome = bundle.getString("commons.words.home");
			
			extra[0] = new TextComponent("/" + aliases + " [" + nounHome + "]");
			
			TextComponent text = Message.getTextComponent(bundle.getString("commons.cmds.wrong-syntax-try"), extra);
			text.setColor(ChatColor.RED);
			p.spigot().sendMessage(text);
		}
		return true;
	}
	
	private TextComponent getCooldownText(long time, ResourceBundle bundle)
	{
		time += 1000L;
		long seconds = time / 1000L % 60L;
		long minutes = time / (60L * 1000L) % 60L;
		long hours = time / (60L * 60L * 1000L) % 24L;
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
				
				Bukkit.getScheduler().runTask(Main.getInstance(), () ->
				{
					if(op.isOnline())
					{
						Bukkit.dispatchCommand(op.getPlayer(), "land");
					}
				});
			}
		}
	}
}