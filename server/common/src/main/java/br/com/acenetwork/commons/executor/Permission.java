package br.com.acenetwork.commons.executor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.TreeMap;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldSaveEvent;

import com.google.common.io.ByteStreams;

import br.com.acenetwork.commons.Common;
import br.com.acenetwork.commons.CommonsUtil;
import br.com.acenetwork.commons.manager.CommonsConfig;
import br.com.acenetwork.commons.manager.CommonsConfig.Type;
import br.com.acenetwork.commons.manager.Message;
import br.com.acenetwork.commons.player.CommonPlayer;
import br.com.acenetwork.commons.player.craft.CraftCommonPlayer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class Permission implements TabExecutor, Listener
{
	private final Map<String, Map<String, Long>> groupPermission;
	private final Map<String, Map<UUID, Long>> groupUser;
	public final Map<UUID, Map<String, Long>> userPermission;
	
	public static final String TAG = "" + ChatColor.DARK_GRAY + ChatColor.BOLD + "("
			+ ChatColor.DARK_RED + ChatColor.BOLD + "PEX"
			+ ChatColor.DARK_GRAY + ChatColor.BOLD + ")" + ChatColor.RESET;
	
	private static Permission instance;
	
	@SuppressWarnings("unchecked")
	public Permission() throws RuntimeException
	{
		instance = this;
		
		File groupsFile = CommonsConfig.getFile(Type.GROUPS_DAT, false);
		File usersFile = CommonsConfig.getFile(Type.USERS_DAT, false);
		
		userPermission = new HashMap<>();
		
		if(usersFile.exists())
		{
			try(FileInputStream fileIn = new FileInputStream(usersFile);
					ByteArrayInputStream stream = new ByteArrayInputStream(ByteStreams.toByteArray(fileIn));
					ObjectInputStream in = new ObjectInputStream(stream))
			{
				Map<UUID, Map<String, Long>> map = (Map<UUID, Map<String, Long>>) in.readObject();
				
				Bukkit.getOnlinePlayers().stream().forEach(x -> 
				{
					Map<String, Long> map1 = map.containsKey(x.getUniqueId()) ? map.get(x.getUniqueId()) : new HashMap<>();
					userPermission.put(x.getUniqueId(), map1);
				});
			}
			catch(ClassNotFoundException | IOException ex)
			{
				throw new RuntimeException(ex);
			}
		}
		
		if(groupsFile.exists())
		{
			try(FileInputStream fileIn = new FileInputStream(groupsFile);
					ByteArrayInputStream stream = new ByteArrayInputStream(ByteStreams.toByteArray(fileIn));
					ObjectInputStream in = new ObjectInputStream(stream))
			{
				groupPermission = (Map<String, Map<String, Long>>) in.readObject();
				groupUser = (Map<String, Map<UUID, Long>>) in.readObject();
			}
			catch(ClassNotFoundException | IOException ex)
			{
				throw new RuntimeException(ex);
			}
		}
		else
		{
			groupPermission = new HashMap<>();
			groupUser = new HashMap<>();
		}
		
		Bukkit.getPluginManager().registerEvents(this, Common.getPlugin());
	}

	@Override
	public List<String> onTabComplete(CommandSender arg0, Command arg1, String arg2, String[] arg3)
	{
		return null;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String aliases, String[] args)
	{
		CommonPlayer cp = null;
		ResourceBundle bundle = ResourceBundle.getBundle("message");
		
		if(sender instanceof Player)
		{
			Player p = (Player) sender;
			cp = CraftCommonPlayer.get(p);
			bundle = ResourceBundle.getBundle("message", cp.getLocale());
			
			if(!cp.hasPermission("cmd.permission"))
			{
				p.sendMessage(ChatColor.RED + bundle.getString("commons.cmds.permission"));
				return true;
			}
		}
		
		try
		{
			if((args.length == 4 || args.length == 5) && (args[0].equalsIgnoreCase("group") || args[0].equalsIgnoreCase("g")) 
					&& args[2].equalsIgnoreCase("add"))
			{
				final String group = args[1].toLowerCase();
				final String permission = args[3].toLowerCase();

				if(!CommonsUtil.groupSyntaxIsValid(group))
				{
					sender.sendMessage(ChatColor.RED + bundle.getString("commons.cmd.permission.invalid-group-syntax"));
					return true;	
				}

				if(!CommonsUtil.permissionSyntaxIsValid(permission))
				{
					sender.sendMessage(ChatColor.RED + bundle.getString("commons.cmd.permission.invalid-permission-syntax"));
					return true;
				}
				
				if(!groupPermission.containsKey(group))
				{
					groupPermission.put(group, new HashMap<>());
				}
				
				Map<String, Long> map = groupPermission.get(group);
				
				Long value = map.get(permission);
				
				long currentTime = value == null || value != 0 && value < System.currentTimeMillis() 
						? System.currentTimeMillis()
						: value;
				
				long seconds = args.length == 5 
						? args[4].startsWith("+") ? Integer.valueOf(args[4].substring(1)) : Integer.valueOf(args[4]) 
						: 0L;
				
				long newTime = args.length == 5 
						? args[4].startsWith("+") 
						? currentTime == 0L ? 0L : currentTime + seconds * 1000L
						: System.currentTimeMillis() + seconds * 1000L
						: 0L;
				
				map.put(permission, newTime);
				
				TextComponent[] extra = new TextComponent[2];
				
				extra[0] = new TextComponent(permission);
				extra[0].setColor(ChatColor.YELLOW);
				
				extra[1] = new TextComponent(group);
				extra[1].setColor(ChatColor.YELLOW);

				TextComponent text = Message.getTextComponent(bundle.getString("commons.cmd.permission.permission-added-to-group"), extra);
				text.setColor(ChatColor.GREEN);
				CommonsUtil.sendMessage(sender, text);
			}
			else if(args.length == 4 && (args[0].equalsIgnoreCase("group") || args[0].equalsIgnoreCase("g")) 
				&& args[2].equalsIgnoreCase("remove"))
			{
				final String group = args[1].toLowerCase();
				final String permission = args[3].toLowerCase();
				
				Map<String, Long> map = groupPermission.get(group);

				if(map != null 
						&& (permission.endsWith("*") 
						? map.keySet().removeAll(map.keySet().stream()
						.filter(x -> x.startsWith(permission.substring(0, permission.length() - 1)))
						.collect(Collectors.toSet()))
						: map.remove(permission) != null))
				{
					if(map.isEmpty())
					{
						groupPermission.remove(group);
					}
					
					TextComponent[] extra = new TextComponent[2];
					
					extra[0] = new TextComponent(permission);
					extra[0].setColor(ChatColor.YELLOW);
					
					extra[1] = new TextComponent(group);
					extra[1].setColor(ChatColor.YELLOW);
					
					TextComponent text = Message.getTextComponent(bundle.getString("commons.cmd.permission.permission-removed-from-group"), extra);
					text.setColor(ChatColor.GREEN);
					CommonsUtil.sendMessage(sender, text);
				}
				else
				{
					TextComponent text = new TextComponent(bundle.getString("commons.cmd.permission.permission-not-found"));
					text.setColor(ChatColor.RED);
					CommonsUtil.sendMessage(sender, text);
				}
			}
			else if((args.length == 5 || args.length == 6) && (args[0].equalsIgnoreCase("group") || args[0].equalsIgnoreCase("g")) &&
				(args[2].equalsIgnoreCase("user") || args[2].equalsIgnoreCase("u")) &&
				 args[3].equalsIgnoreCase("add"))
			{
				final String group = args[1].toLowerCase();
				final String user = args[4];
				
				OfflinePlayer op = CommonsUtil.getOfflinePlayerIfCached(user);
				
				if(op == null)
				{
					sender.sendMessage(ChatColor.RED + bundle.getString("commons.cmds.user-not-found"));
					return true;	
				}

				if(!CommonsUtil.groupSyntaxIsValid(group))
				{
					sender.sendMessage(ChatColor.RED + bundle.getString("commons.cmd.permission.invalid-group-syntax"));
					return true;	
				}
				
				if(!groupUser.containsKey(group))
				{
					groupUser.put(group, new HashMap<>());
				}
				
				Map<UUID, Long> map = groupUser.get(group);
				
				Long value = map.get(op.getUniqueId());
				
				long currentTime = value == null || value != 0 && value < System.currentTimeMillis() 
						? System.currentTimeMillis()
						: value;
				
				long seconds = args.length == 6 
						? args[5].startsWith("+") ? Integer.valueOf(args[5].substring(1)) : Integer.valueOf(args[5]) 
						: 0L;
				
				long newTime = args.length == 6 
						? args[5].startsWith("+") 
						? currentTime == 0L ? 0L : currentTime + seconds * 1000L
						: System.currentTimeMillis() + seconds * 1000L
						: 0L;
				
				map.put(op.getUniqueId(), newTime);
				
				TextComponent[] extra = new TextComponent[2];
				
				extra[0] = new TextComponent(op.getName());
				extra[0].setColor(ChatColor.YELLOW);
				
				extra[1] = new TextComponent(group);
				extra[1].setColor(ChatColor.YELLOW);

				TextComponent text = Message.getTextComponent(bundle.getString("commons.cmd.permission.user-added-to-group"), extra);
				text.setColor(ChatColor.GREEN);
				CommonsUtil.sendMessage(sender, text);
			}
			else if(args.length == 5 && (args[0].equalsIgnoreCase("group") || args[0].equalsIgnoreCase("g")) &&
				(args[2].equalsIgnoreCase("user") || args[2].equalsIgnoreCase("u")) &&
				 args[3].equalsIgnoreCase("remove"))
			{
				final String group = args[1].toLowerCase();
				final String user = args[4];

				OfflinePlayer op = CommonsUtil.getOfflinePlayerIfCached(user);

				if(op == null)
				{
					sender.sendMessage(ChatColor.RED + bundle.getString("commons.cmds.user-not-found"));
					return true;	
				}
				
				if(!groupUser.containsKey(group))
				{
					sender.sendMessage(ChatColor.RED + bundle.getString("commons.cmd.permission.group-not-found"));
					return true;
				}
				
				Map<UUID, Long> map = groupUser.get(group);
				
				if(map.remove(op.getUniqueId()) != null)
				{
					TextComponent[] extra = new TextComponent[2];
					
					extra[0] = new TextComponent(op.getName());
					extra[0].setColor(ChatColor.YELLOW);
					
					extra[1] = new TextComponent(group);
					extra[1].setColor(ChatColor.YELLOW);

					TextComponent text = Message.getTextComponent(bundle.getString("commons.cmd.permission.user-removed-from-group"), extra);
					text.setColor(ChatColor.GREEN);
					CommonsUtil.sendMessage(sender, text);
				}
				else
				{
					sender.sendMessage(ChatColor.RED + bundle.getString("commons.cmd.permission.user-not-found-in-group"));
				}
			}
			else if((args.length == 4 || args.length == 5) && (args[0].equalsIgnoreCase("user") || args[0].equalsIgnoreCase("u")) &&
					args[2].equalsIgnoreCase("add"))
			{
				final String user = args[1];
				final String permission = args[3].toLowerCase();
				OfflinePlayer op = CommonsUtil.getOfflinePlayerIfCached(user);
				
				if(op == null)
				{
					sender.sendMessage(ChatColor.RED + bundle.getString("commons.cmds.user-not-found"));
					return true;	
				}

				if(!CommonsUtil.permissionSyntaxIsValid(permission))
				{
					sender.sendMessage(ChatColor.RED + bundle.getString("commons.cmd.permission.invalid-permission-syntax"));
					return true;
				}
				
				loadUser(op.getUniqueId());
				
				Map<String, Long> map = userPermission.get(op.getUniqueId());
				
				Long value = map.get(permission);
				
				long currentTime = value == null || value != 0 && value < System.currentTimeMillis() 
						? System.currentTimeMillis()
						: value;
				
				long seconds = args.length == 5 
						? args[4].startsWith("+") ? Integer.valueOf(args[4].substring(1)) : Integer.valueOf(args[4]) 
						: 0L;
				
				long newTime = args.length == 5 
						? args[4].startsWith("+") 
						? currentTime == 0L ? 0L : currentTime + seconds * 1000L
						: System.currentTimeMillis() + seconds * 1000L
						: 0L;
				
				map.put(permission, newTime);
				
				TextComponent[] extra = new TextComponent[2];
				
				extra[0] = new TextComponent(permission);
				extra[0].setColor(ChatColor.YELLOW);
				
				extra[1] = new TextComponent(op.getName());
				extra[1].setColor(ChatColor.YELLOW);

				TextComponent text = Message.getTextComponent(bundle.getString("commons.cmd.permission.permission-added-to-user"), extra);
				text.setColor(ChatColor.GREEN);
				CommonsUtil.sendMessage(sender, text);
			}
			else if(args.length == 4 && (args[0].equalsIgnoreCase("user") || args[0].equalsIgnoreCase("u")) &&
				args[2].equalsIgnoreCase("remove"))
			{
				final String user = args[1];
				final String permission = args[3].toLowerCase();

				OfflinePlayer op = CommonsUtil.getOfflinePlayerIfCached(user);
				
				if(op == null)
				{
					sender.sendMessage(ChatColor.RED + bundle.getString("commons.cmds.user-not-found"));
					return true;
				}
				
				loadUser(op.getUniqueId());
				
				Map<String, Long> map = userPermission.get(op.getUniqueId());
				
				if(!map.isEmpty()
						&& (permission.endsWith("*") 
						? map.keySet().removeAll(map.keySet().stream()
						.filter(x -> x.startsWith(permission.substring(0, permission.length() - 1)))
						.collect(Collectors.toSet()))
						: map.remove(permission) != null))
				{
					TextComponent[] extra = new TextComponent[2];
					
					extra[0] = new TextComponent(permission);
					extra[0].setColor(ChatColor.YELLOW);
					
					extra[1] = new TextComponent(op.getName());
					extra[1].setColor(ChatColor.YELLOW);

					TextComponent text = Message.getTextComponent(bundle.getString("commons.cmd.permission.permission-removed-from-user"), extra);
					text.setColor(ChatColor.GREEN);
					CommonsUtil.sendMessage(sender, text);
				}
				else
				{
					sender.sendMessage(ChatColor.RED + bundle.getString("commons.cmd.permission.permission-not-found"));
				}
			}
			else if((args.length == 3 || args.length == 4) &&
					(args[0].equalsIgnoreCase("group") || args[0].equalsIgnoreCase("g")) && 
					 args[2].equalsIgnoreCase("list"))
			{
				final int page = args.length == 4 ? Integer.valueOf(args[3]) : 1;
				final String group = args[1].toLowerCase();
				
				Map<String, Long> map = groupPermission.containsKey(group)
						? groupPermission.get(group)
						: new HashMap<>();
				
				map = map.entrySet().stream().filter(x -> 
						{
							Long value = x.getValue();
							return value != null && (value == 0L || value >= System.currentTimeMillis());
						}).collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue()));
				
				String commandLine = "/" + cmd.getName();
				
				for(int i = 0; i < args.length && i < 3; i++)
				{
					commandLine += " " + args[i];
				}
				
				printList(cp, map, commandLine, page);
			}
			else if((args.length == 2 || args.length == 3) &&
					(args[0].equalsIgnoreCase("group") || args[0].equalsIgnoreCase("g")) && 
					 args[1].equalsIgnoreCase("list"))
			{
				final int page = args.length == 3 ? Integer.valueOf(args[2]) : 1;
				
				Map<String, Long> map = new HashMap<>();
				
				groupUser.keySet().stream().forEach(x -> map.put(x, 0L));
				groupPermission.keySet().stream().forEach(x -> map.put(x, 0L));
				
				String commandLine = "/" + cmd.getName();
				
				for(int i = 0; i < args.length && i < 2; i++)
				{
					commandLine += " " + args[i];
				}
				
				printList(cp, map, commandLine, page);
			}
			else if((args.length == 4 || args.length == 5) && 
					(args[0].equalsIgnoreCase("group") || args[0].equalsIgnoreCase("g")) &&
					(args[2].equalsIgnoreCase("user") || args[2].equalsIgnoreCase("u")) &&
					 args[3].equalsIgnoreCase("list"))
			{
				final int page = args.length == 5 ? Integer.valueOf(args[4]) : 1;
				final String group = args[1].toLowerCase();
				
				String commandLine = "/" + cmd.getName();
				
				Map<UUID, Long> map = groupUser.containsKey(group) 
						? groupUser.get(group) 
						: new HashMap<>();
				
				Map<String, Long> convertedMap = map.entrySet().stream().filter(x -> 
						{
							Long value = x.getValue();
							return value != null && (value == 0L || value >= System.currentTimeMillis());
						}).collect(Collectors.toMap(x -> Bukkit.getOfflinePlayer(x.getKey()).getName(), x -> x.getValue()));	
				
				for(int i = 0; i < args.length && i < 4; i++)
				{
					commandLine += " " + args[i];
				}
				
				printList(cp, convertedMap, commandLine, page);
			}
			else if((args.length == 3 || args.length == 4) &&
					(args[0].equalsIgnoreCase("user") || args[0].equalsIgnoreCase("u")) && 
					 args[2].equalsIgnoreCase("list"))
			{
				final int page = args.length == 4 ? Integer.valueOf(args[3]) : 1;
				final String user = args[1];
				
				final OfflinePlayer op = CommonsUtil.getOfflinePlayerIfCached(user);
				
				if(op == null)
				{
					sender.sendMessage(ChatColor.RED + bundle.getString("commons.cmds.user-not-found"));
					return true;	
				}
				
				Map<String, Long> map = userPermission.containsKey(op.getUniqueId()) 
						? userPermission.get(op.getUniqueId()) 
						: new HashMap<>();
				
				map = map.entrySet().stream().filter(x -> 
						{
							Long value = x.getValue();
							return value != null && (value == 0L || value >= System.currentTimeMillis());
						}).collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue()));
				
				String commandLine = "/" + cmd.getName();
				
				for(int i = 0; i < args.length && i < 3; i++)
				{
					commandLine += " " + args[i];
				}
				
				printList(cp, map, commandLine, page);
			}
			else if((args.length == 4 || args.length == 5) && 
					(args[0].equalsIgnoreCase("user") || args[0].equalsIgnoreCase("u")) &&
					(args[2].equalsIgnoreCase("group") || args[2].equalsIgnoreCase("g")) &&
					 args[3].equalsIgnoreCase("list"))
			{
				final int page = args.length == 5 ? Integer.valueOf(args[4]) : 1;
				final String user = args[1];

				final OfflinePlayer op = CommonsUtil.getOfflinePlayerIfCached(user);
				
				if(op == null)
				{
					sender.sendMessage(ChatColor.RED + bundle.getString("commons.cmds.user-not-found"));
					return true;
				}
				
				Map<String, Long> map = groupUser.entrySet().stream()
						.filter(x -> 
						{
							Map<UUID, Long> userMap = x.getValue();
							Long value = userMap.get(op.getUniqueId());
							return value != null && (value == 0L || value >= System.currentTimeMillis());
						}).collect(Collectors.toMap(Entry::getKey, x -> x.getValue().get(op.getUniqueId())));
				
				String commandLine = "/" + cmd.getName();
				
				for(int i = 0; i < args.length && i < 4; i++)
				{
					commandLine += " " + args[i];
				}
				
				TextComponent[] extra = new TextComponent[2];
				
				String displayName = op.isOnline() ? op.getPlayer().getDisplayName() : ChatColor.YELLOW + op.getName();
				
				extra[0] = new TextComponent(displayName);
//				extra[1] = new TextComponent(group);
				extra[1].setColor(ChatColor.YELLOW);
				
				TextComponent text = Message.getTextComponent(bundle.getString("commons.cmd.permission.user-group-list"), extra);
				text.setColor(ChatColor.GREEN);
				
				String title = TAG + " " + text.toLegacyText();
				String empty = ChatColor.RED + bundle.getString("commons.cmd.permission.group-list-empty");
				printList(cp, map, commandLine, page);
			}
			else
			{
				TextComponent[] extra = new TextComponent[1];
				
				extra[0] = new TextComponent("/" + aliases + " help");
				
				TextComponent text = Message.getTextComponent(bundle.getString("commons.cmds.wrong-syntax-try"), extra);
				text.setColor(ChatColor.RED);
				CommonsUtil.sendMessage(sender, text);
			}
		}
		catch(NumberFormatException e)
		{
			sender.sendMessage(ChatColor.RED + bundle.getString("commons.cmds.invalid-number-format"));
		}

		return false;
	}
	
	@EventHandler
	public void a(WorldSaveEvent e)
	{
		if(!e.getWorld().getName().equals("world"))
		{
			return;
		}
		
		save();
	}
	
	private void printList(CommonPlayer cp, Map<String, Long> map, String commandLine, int page)
	{
		printList(cp, "List ...", "Empty list", map, commandLine, page);
	}
	private void printList(CommonPlayer cp, String title, String empty, Map<String, Long> map, String commandLine, int page)
	{
		Player p = cp.getPlayer();
		
		if(map.isEmpty())
		{
			p.sendMessage(empty);
			return;
		}
		
		final byte pageSize = 18;
		
		Map<String, Long> treeMap = new TreeMap<>(map);
		List<Entry<String, Long>> list = new ArrayList<>(treeMap.entrySet());
		
		if(list.size() > (page - 1) * pageSize && page > 0)
		{
			final int maxPage = list.size() / pageSize + (list.size() % pageSize == 0 ? 0 : 1);
//			p.sendMessage("(PEX) List [" + page + "/" + maxPage + "]");
			p.sendMessage(title);
			
			for(int i = (page - 1) * pageSize; i < page * pageSize && i < list.size(); i++)
			{
				Entry<String, Long> entry = list.get(i);
				
				String key = entry.getKey();
				long time = entry.getValue();
				
				TextComponent text = new TextComponent(" - ");
				text.setColor(ChatColor.DARK_GRAY);
				
				TextComponent extra0 = new TextComponent(key);
				TextComponent extra1 = new TextComponent(" ");
				
				if(time == 0L)
				{
					extra0.setColor(ChatColor.GREEN);
				}
				else if((time -= System.currentTimeMillis()) < 0L)
				{
					extra0.setColor(ChatColor.RED);
				}
				else
				{
					extra0.setColor(ChatColor.GREEN);
					
					extra1.setItalic(true);
					extra1.setColor(ChatColor.GRAY);
					
					long seconds = time / 1000L % 60;
					long minutes = time / (60L * 1000L) % 60;
					long hours = time / (60L * 60L * 1000L) % 24;
					long days = time / (24L * 60L * 60L * 1000L);
					
					String timestamp = days != 0 ? days + "d " + hours + "h " + minutes + "m " + seconds + "s"
							: hours != 0 ? hours + "h " + minutes + "m " + seconds + "s"
									: minutes != 0 ? minutes + "m " + seconds + "s"
											: seconds + "s";
					
					extra1.addExtra(timestamp);
				}
				
				text.addExtra(extra0);
				text.addExtra(extra1);
				
				p.spigot().sendMessage(text);
			}
			
			TextComponent text = new TextComponent("");
			text.setColor(ChatColor.DARK_GRAY);
			
			if(page <= 1)
			{
				text.addExtra(" -");
			}
			else
			{
				TextComponent extra = new TextComponent("<<");
				extra.setColor(ChatColor.WHITE);
				extra.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, commandLine + " " + (page - 1)));
				
				text.addExtra(extra);
			}
			
			text.addExtra(" | ");
			
			if(maxPage > page)
			{
				TextComponent extra = new TextComponent(">>");
				extra.setColor(ChatColor.WHITE);
				extra.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, commandLine + " " + (page + 1)));
				
				text.addExtra(extra);
			}
			else
			{
				text.addExtra("-");
			}
			
			p.spigot().sendMessage(text);
		}
		else
		{
			p.sendMessage(ChatColor.RED + "commons.cmds.page-not-found");
		}
	}
	
	public void loadUser(UUID uuid) throws RuntimeException
	{
		Bukkit.broadcastMessage("Loading user... (synchronized)");
		
		if(userPermission.containsKey(uuid))
		{
			Bukkit.broadcastMessage("Already loaded!");
			return;
		}

		
		File file = CommonsConfig.getFile(Type.USER_DAT, false, uuid);
		
		long time = System.currentTimeMillis();
		
		if(!file.exists())
		{
			Bukkit.broadcastMessage("File not exists! Loading empty map.");
			userPermission.put(uuid, new HashMap<>());
			return;
		}
		
		try(FileInputStream fileIn = new FileInputStream(file);
				ByteArrayInputStream stream = new ByteArrayInputStream(ByteStreams.toByteArray(fileIn));
				ObjectInputStream in = new ObjectInputStream(stream))
		{
			@SuppressWarnings("unchecked")
			Map<String, Long> map = (Map<String, Long>) in.readObject();
			userPermission.put(uuid, map);
			Bukkit.broadcastMessage("User loaded!");
			Bukkit.broadcastMessage("TIME ELAPSED: " + (System.currentTimeMillis() - time) + "ms");
		}
		catch(ClassNotFoundException | IOException ex)
		{
			Bukkit.broadcastMessage("ERROR!");
			throw new RuntimeException(ex);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void save()
	{
		File file;
		
		file = CommonsConfig.getFile(Type.USERS_DAT, true);
		
		try(FileOutputStream fileOut = new FileOutputStream(file);
				ByteArrayOutputStream outStream = new ByteArrayOutputStream();
				ObjectOutputStream out = new ObjectOutputStream(outStream);)
		{
			Map<UUID, Map<String, Long>> map = new HashMap<>();
			
			if(file.length() > 0L)
			{
				try(FileInputStream fileIn = new FileInputStream(file);
						ByteArrayInputStream inStream = new ByteArrayInputStream(ByteStreams.toByteArray(fileIn));
						ObjectInputStream in = new ObjectInputStream(inStream))
				{
					map = (Map<UUID, Map<String, Long>>) in.readObject();
				}
				catch(IOException ex)
				{
					throw ex;
				}
			}
			
			Map<UUID, Map<String, Long>> userPermissionClone = new HashMap<>(userPermission);
			
			Iterator<Entry<UUID, Map<String, Long>>> iterator1 = userPermission.entrySet().iterator();
			
			while(iterator1.hasNext())
			{
				Entry<UUID, Map<String, Long>> entry = iterator1.next();
				
				if(!Bukkit.getOfflinePlayer(entry.getKey()).isOnline())
				{
					iterator1.remove();
				}
			}
			
			Iterator<Entry<UUID, Map<String, Long>>> iterator = userPermissionClone.entrySet().iterator();
			
			while(iterator.hasNext())
			{
				Entry<UUID, Map<String, Long>> entry = iterator.next();
				boolean isEmpty = entry.getValue().isEmpty();
				File file1 = CommonsConfig.getFile(Type.USER_DAT, !isEmpty, entry.getKey());
				
				try(FileOutputStream fileOut1 = new FileOutputStream(file1);
						ByteArrayOutputStream outStream1 = new ByteArrayOutputStream();
						ObjectOutputStream out1 = new ObjectOutputStream(outStream1))
				{
					if(isEmpty)
					{
						file1.delete();
						iterator.remove();
						map.remove(entry.getKey());
					}
					else
					{
						out1.writeObject(entry.getValue());
						fileOut1.write(outStream1.toByteArray());
					}
				}
				catch(IOException e)
				{
					throw e;
				}
			}
			
			map.putAll(userPermissionClone);
			out.writeObject(map);
			fileOut.write(outStream.toByteArray());
		}
		catch(ClassNotFoundException | IOException e1)
		{
			e1.printStackTrace();
		}
		
		file = CommonsConfig.getFile(Type.GROUPS_DAT, true);
		
		try(FileOutputStream fileOut = new FileOutputStream(file);
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				ObjectOutputStream out = new ObjectOutputStream(stream);)
		{
			out.writeObject(groupPermission);
			out.writeObject(groupUser);
			fileOut.write(stream.toByteArray());
		}
		catch(IOException e1)
		{
			e1.printStackTrace();
		}
	}
	
	public static Permission getInstance()
	{
		return instance;
	}
}
