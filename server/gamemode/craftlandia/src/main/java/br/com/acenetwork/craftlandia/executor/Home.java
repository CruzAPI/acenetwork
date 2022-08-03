package br.com.acenetwork.craftlandia.executor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import com.google.common.io.ByteStreams;

import br.com.acenetwork.commons.manager.Message;
import br.com.acenetwork.commons.player.CommonPlayer;
import br.com.acenetwork.commons.player.craft.CraftCommonPlayer;
import br.com.acenetwork.craftlandia.manager.ChannelCommand;
import br.com.acenetwork.craftlandia.manager.Config;
import br.com.acenetwork.craftlandia.manager.Config.Type;
import br.com.acenetwork.craftlandia.manager.HomeObj;
import br.com.acenetwork.craftlandia.player.SurvivalPlayer;
import br.com.acenetwork.craftlandia.warp.Warp;
import br.com.acenetwork.craftlandia.warp.Warp.Result;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class Home implements TabExecutor, ChannelCommand
{
	public final Map<UUID, Map<String, HomeObj>> map;
	
	private static Home instance;
	
	@SuppressWarnings("unchecked")
	public Home()
	{
		instance = this;
		
		File file = Config.getFile(Type.HOMES, false);
		
		if(file.exists() && file.length() > 0L)
		{
			try(FileInputStream fileIn = new FileInputStream(file);
					ByteArrayInputStream streamIn = new ByteArrayInputStream(ByteStreams.toByteArray(fileIn));
					ObjectInputStream in = new ObjectInputStream(streamIn))
			{
				map = (Map<UUID, Map<String, HomeObj>>) in.readObject();
			}
			catch(IOException | ClassNotFoundException ex)
			{
				throw new RuntimeException(ex);
			}
		}
		else
		{
			map = new HashMap<>();
		}
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, HomeObj> load(UUID uuid)
	{
		Map<String, HomeObj> homeMap;
		
		if((homeMap = map.get(uuid)) != null)
		{
			return homeMap;
		}
		
		File file = Config.getFile(Type.HOME, false, uuid);
		
		if(!file.exists() || file.length() == 0L)
		{
			map.put(uuid, homeMap = new HashMap<>());
			return homeMap;
		}
		
		try(FileInputStream fileIn = new FileInputStream(file);
				ByteArrayInputStream stream = new ByteArrayInputStream(ByteStreams.toByteArray(fileIn));
				ObjectInputStream in = new ObjectInputStream(stream))
		{
			map.put(uuid, homeMap = (Map<String, HomeObj>) in.readObject());
			return homeMap;
		}
		catch(ClassNotFoundException | IOException ex)
		{
			throw new RuntimeException(ex);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void save()
	{
		File file;
		
		file = Config.getFile(Type.HOMES, true);
		
		Map<UUID, Map<String, HomeObj>> tempMap;
		
		if(file.length() > 0L)
		{
			try(FileInputStream fileIn = new FileInputStream(file);
					ByteArrayInputStream streamIn = new ByteArrayInputStream(ByteStreams.toByteArray(fileIn));
					ObjectInputStream in = new ObjectInputStream(streamIn))
			{
				tempMap = (Map<UUID, Map<String, HomeObj>>) in.readObject();
			}
			catch(IOException | ClassNotFoundException ex)
			{
				ex.printStackTrace();
				return;
			}
		}
		else
		{
			tempMap = new HashMap<>();
		}
		
		for(Entry<UUID, Map<String, HomeObj>> entry : map.entrySet())
		{
			if(entry.getValue().isEmpty())
			{
				tempMap.remove(entry.getKey());
			}
			else
			{
				tempMap.put(entry.getKey(), entry.getValue());
			}
		}
		
		try(FileOutputStream fileOut = new FileOutputStream(file);
				ByteArrayOutputStream streamOut = new ByteArrayOutputStream();
				ObjectOutputStream out = new ObjectOutputStream(streamOut))
		{
			out.writeObject(tempMap);
			fileOut.write(streamOut.toByteArray());
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
		
		Iterator<Entry<UUID, Map<String, HomeObj>>> iterator = map.entrySet().iterator();
		
		while(iterator.hasNext())
		{
			Entry<UUID, Map<String, HomeObj>> entry = iterator.next();
			
			UUID uuid = entry.getKey();
			Map<String, HomeObj> value = entry.getValue();
			
			file = Config.getFile(Type.HOME, !value.isEmpty(), uuid);
			
			if(value.isEmpty())
			{
				file.delete();
				continue;
			}
			
			try(FileOutputStream fileOut = new FileOutputStream(file);
					ByteArrayOutputStream streamOut = new ByteArrayOutputStream();
					ObjectOutputStream out = new ObjectOutputStream(streamOut))
			{
				out.writeObject(value);
				fileOut.write(streamOut.toByteArray());
				
				if(!Bukkit.getOfflinePlayer(uuid).isOnline())
				{
					iterator.remove();
				}
			}
			catch(IOException ex)
			{
				ex.printStackTrace();
			}
		}
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args)
	{
		List<String> list = new ArrayList<>();
		
		if(!(sender instanceof Player))
		{
			return list;
		}
		
		if(args.length > 2)
		{
			return list;
		}
		
		Player p = (Player) sender;
		
		Map<String, HomeObj> homeMap = load(p.getUniqueId());
		
		if(args.length == 2)
		{
			CommonPlayer cp = CraftCommonPlayer.get(p);
			
			if(!cp.hasPermission("cmd.home.public"))
			{
				return list;
			}
			
			HomeObj homeObj = homeMap.get(args[0].toLowerCase());
			
			if(homeObj == null)
			{
				return list;
			}
			
			String subCommand = homeObj.isPublic() ? "private" : "public";
			
			if(subCommand.startsWith(args[1].toLowerCase()))
			{
				list.add(subCommand);
			}
		}
		
		if(args.length != 1)
		{
			return list;
		}
		
		for(String key : homeMap.keySet())
		{
			if(key.toLowerCase().startsWith(args[0].toLowerCase()))
			{
				list.add(key.toLowerCase());
			}
		}
		
		Collections.sort(list, new Comparator<String>()
		{
			@Override
			public int compare(String o1, String o2)
			{
				return o1.compareTo(o2);
			}
		});
		
		return list;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String aliases, String[] args)
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
		
		if(args.length == 0)
		{
			TextComponent[] extra = new TextComponent[1];
			
			extra[0] = new TextComponent("");
			
			Map<String, HomeObj> homeMap = load(p.getUniqueId());
			
			final String comma = ", ";
			
			if(homeMap.isEmpty())
			{
				sender.sendMessage(ChatColor.RED + bundle.getString("raid.cmd.home.home-list-empty"));
				return true;
			}
			
			Iterator<Entry<String, HomeObj>> iterator = homeMap.entrySet().iterator();
			
			while(iterator.hasNext())
			{
				Entry<String, HomeObj> entry = iterator.next();
				String key = entry.getKey();
				HomeObj value = entry.getValue();
				
				TextComponent text = new TextComponent(key);
				
				if(value.getWorld() == p.getWorld() || cp.hasPermission("portals.bypass"))
				{
					text.setColor(ChatColor.YELLOW);
					text.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/home " + key));
					text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, 
							new ComponentBuilder(bundle.getString("cmd.home.click-to-teleport"))
							.color(ChatColor.YELLOW).create()));
				}
				else
				{
					text.setColor(ChatColor.RED);
					text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, 
							new ComponentBuilder(bundle.getString("cmd.home.different-world"))
							.color(ChatColor.RED).create()));
				}
				
				extra[0].addExtra(text);
				
				text = new TextComponent("■");
				
				if(value.isPublic())
				{
					text.setColor(ChatColor.DARK_GREEN);
					text.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/home " + key + " private /home"));
					text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, 
							new ComponentBuilder(StringUtils.capitalize(bundle.getString("cmd.home.adjective.public")))
							.color(ChatColor.DARK_GREEN).create()));
				}
				else
				{
					text.setColor(ChatColor.DARK_RED);
					text.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/home " + key + " public /home"));
					text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, 
							new ComponentBuilder(StringUtils.capitalize(bundle.getString("cmd.home.adjective.private")))
							.color(ChatColor.DARK_RED).create()));
				}
				
				extra[0].addExtra(text);
				
				if(iterator.hasNext())
				{
					extra[0].addExtra(comma);
				}
			}
			
			TextComponent text = Message.getTextComponent(bundle.getString("raid.cmd.home.your-homes"), extra);
			text.setColor(ChatColor.GREEN);
			p.spigot().sendMessage(text);
		}
		else if(args.length == 1)
		{
			String key = args[0].toLowerCase();
			HomeObj homeObj = load(p.getUniqueId()).get(key);
			Location destiny = homeObj == null ? null : homeObj.getLocation();
			
			if(destiny == null)
			{
				TextComponent[] extra = new TextComponent[1];
				
				extra[0] = new TextComponent(key);
				
				TextComponent text = Message.getTextComponent(bundle.getString("raid.cmds.home-not-found"), extra);
				text.setColor(ChatColor.RED);
				p.spigot().sendMessage(text);
				
				return true;
			}
			
			if(destiny.getWorld() != p.getWorld() && !cp.hasPermission("portals.bypass"))
			{
				TextComponent[] extra = new TextComponent[1];
				
				extra[0] = new TextComponent("/" + aliases);
				
				TextComponent text = Message.getTextComponent(bundle.getString("cmd.home.between-dimensions"), extra);
				text.setColor(ChatColor.RED);
				p.spigot().sendMessage(text);
				
				return true;
			}
			
			if(cp instanceof SurvivalPlayer)
			{
				SurvivalPlayer sp = (SurvivalPlayer) cp;
				Warp warp = Warp.MAP.get(p.getWorld().getUID());
				
				if(warp.canTeleportAwaySpawn(sp) == Result.INVINCIBILITY)
				{
					TextComponent text = new TextComponent(bundle.getString("raid.cmds.cant-teleport-with-spawn-protection"));
					text.setColor(ChatColor.RED);
					p.spigot().sendMessage(text);
					return true;
				}
				
				sp.channel(this, warp.getChannelingTicks(sp), destiny, key);
			}
			else
			{
				run(cp, destiny, key);
			}
		}
		else if((args.length == 2 || args.length == 3 && args[2].equals("/home")) 
				&& (args[1].equalsIgnoreCase("public") || args[1].equalsIgnoreCase("private")))
		{
			if(!cp.hasPermission("cmd.home.public"))
			{
				p.sendMessage(ChatColor.RED + bundle.getString("commons.cmds.permission"));
				return true;
			}
			
			String key = args[0].toLowerCase();
			HomeObj homeObj = load(p.getUniqueId()).get(key);
			Location destiny = homeObj == null ? null : homeObj.getLocation();
			
			if(destiny == null)
			{
				TextComponent[] extra = new TextComponent[1];
				
				extra[0] = new TextComponent(key);
				
				TextComponent text = Message.getTextComponent(bundle.getString("raid.cmds.home-not-found"), extra);
				text.setColor(ChatColor.RED);
				p.spigot().sendMessage(text);
				
				return true;
			}
			
			boolean setPublic = args[1].equalsIgnoreCase("public");
			
			if(homeObj.isPublic() == setPublic)
			{
				TextComponent[] extra = new TextComponent[1];
				
				if(homeObj.isPublic())
				{
					extra[0] = new TextComponent(bundle.getString("cmd.home.adjective.public"));
				}
				else
				{
					extra[0] = new TextComponent(bundle.getString("cmd.home.adjective.private"));
				}
				
				TextComponent text = Message.getTextComponent(bundle.getString("cmd.home.home-already-public"), extra);
				text.setColor(ChatColor.RED);
				p.spigot().sendMessage(text);
				
				return true;
			}
			
			homeObj.setPublic(setPublic);
			
			TextComponent[] extra = new TextComponent[2];
			
			extra[0] = new TextComponent(key);
			extra[0].setColor(ChatColor.YELLOW);
			
			if(homeObj.isPublic())
			{
				extra[1] = new TextComponent(bundle.getString("cmd.home.adjective.public"));
				extra[1].setColor(ChatColor.DARK_GREEN);
			}
			else
			{
				extra[1] = new TextComponent(bundle.getString("cmd.home.adjective.private"));
				extra[1].setColor(ChatColor.DARK_RED);
			}
			
			TextComponent text = Message.getTextComponent(bundle.getString("cmd.home.home-is-public-now"), extra);
			text.setColor(ChatColor.GREEN);
			p.spigot().sendMessage(text);
			
			if(args.length == 3)
			{
				Bukkit.dispatchCommand(p, "home");
			}
		}
		else
		{
			TextComponent[] extra = new TextComponent[1];
			
			String nounHome = bundle.getString("commons.words.home");
			
			extra[0] = new TextComponent("\n/" + aliases + " [" + nounHome + "]" 
					+ "\n/" + aliases + " <" + nounHome + "> public"
					+ "\n/" + aliases + " <" + nounHome + "> private");
			
			TextComponent text = Message.getTextComponent(bundle.getString("commons.cmds.wrong-syntax-try"), extra);
			text.setColor(ChatColor.RED);
			p.spigot().sendMessage(text);
		}
		
		return false;
	}
	
	@Override
	public void run(CommonPlayer cp, Location destiny, Object... args)
	{
		ChannelCommand.super.run(cp, destiny);
		
		ResourceBundle bundle = ResourceBundle.getBundle("message", cp.getLocale());
		Player p = cp.getPlayer();
		
		TextComponent[] extra = new TextComponent[1];
		
		extra[0] = new TextComponent(args[0].toString());
		extra[0].setColor(ChatColor.YELLOW);
		
		TextComponent text = Message.getTextComponent(bundle.getString("raid.cmd.home.teleported-to-home"), extra);
		text.setColor(ChatColor.GREEN);
		p.spigot().sendMessage(text);
	}
	
	public static Home getInstance()
	{
		return instance;
	}
}
