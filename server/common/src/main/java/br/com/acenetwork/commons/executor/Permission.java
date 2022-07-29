package br.com.acenetwork.commons.executor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
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
import org.bukkit.event.Listener;

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

public class Permission implements TabExecutor
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
			groupPermission = new LinkedHashMap<>();
			groupUser = new HashMap<>();
		}
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
				
				OfflinePlayer op = user.equals("*") ? Bukkit.getOfflinePlayer(new UUID(0L, 0L)) : CommonsUtil.getOfflinePlayerIfCached(user);
				
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
				
				extra[0] = new TextComponent(Optional.ofNullable(op.getName()).orElse(user));
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

				OfflinePlayer op = user.equals("*") ? Bukkit.getOfflinePlayer(new UUID(0L, 0L)) : CommonsUtil.getOfflinePlayerIfCached(user);

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
					
					extra[0] = new TextComponent(Optional.ofNullable(op.getName()).orElse(user));
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
				
				Map<String, Long> map = loadUser(op.getUniqueId());
				
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
				
				Map<String, Long> map = loadUser(op.getUniqueId());
				
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
			else if(args.length == 3 &&
					(args[0].equalsIgnoreCase("group") || args[0].equalsIgnoreCase("g")) && 
					 args[2].equalsIgnoreCase("top"))
			{
				String group = args[1].toLowerCase();
				
				Map<String, Long> value = groupPermission.remove(group);
				
				Map<String, Map<String, Long>> temp = new LinkedHashMap<>();
				temp.put(group, value);
				temp.putAll(groupPermission);
				groupPermission.clear();
				groupPermission.putAll(temp);
				
				Bukkit.dispatchCommand(sender, "pex group list");
			}
			else if((args.length == 2 || args.length == 3) &&
					(args[0].equalsIgnoreCase("group") || args[0].equalsIgnoreCase("g")) && 
					 args[1].equalsIgnoreCase("list"))
			{
				Iterator<String> iterator = groupPermission.keySet().iterator();
				
				if(!iterator.hasNext())
				{
					TextComponent text = new TextComponent("Groups list is empty!");
					text.setColor(ChatColor.RED);
					sender.sendMessage(text.toLegacyText());
					return true;
				}
				
				TextComponent text = new TextComponent("Groups: ");
				
				text.setColor(ChatColor.GREEN);
				
				while(iterator.hasNext())
				{
					String key = iterator.next();
					TextComponent extra = new TextComponent(key);
					extra.setColor(ChatColor.YELLOW);
					extra.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/pex group " + key + " top"));
					text.addExtra(extra);
					
					if(iterator.hasNext())
					{
						text.addExtra(", ");
					}
				}
				
				CommonsUtil.sendMessage(sender, text);
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
				}).collect(Collectors.toMap(x -> Optional.ofNullable(Bukkit.getOfflinePlayer(x.getKey()).getName()).orElse("*"), x -> x.getValue()));	
				
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
				
				Map<String, Long> map = getUserGroupList(op);
				
				String commandLine = "/" + cmd.getName();
				
				for(int i = 0; i < args.length && i < 4; i++)
				{
					commandLine += " " + args[i];
				}
				
				TextComponent[] extra = new TextComponent[2];
				
				String displayName = op.isOnline() ? op.getPlayer().getDisplayName() : ChatColor.YELLOW + op.getName();
				
				extra[0] = new TextComponent(displayName);
				extra[1] = new TextComponent("???");
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
	
	private Long getBestValue(Long l1, Long l2)
	{
		if(l1 != null && l2 != null)
		{
			if(l1 == 0L || l2 == 0L)
			{
				return 0L;
			}
			
			return Math.max(l1, l2);
		}
		
		return l1 == null ? l2 : l1;
	}
	
	public Map<String, Long> getUserGroupList(OfflinePlayer op)
	{
		Map<String, Long> map = new HashMap<>();
		
		for(Entry<String, Map<UUID, Long>> entry : groupUser.entrySet())
		{
			String key = entry.getKey();
			Map<UUID, Long> value = entry.getValue();
			
			Long all = value.get(new UUID(0L, 0L));
			Long user = value.get(op.getUniqueId());
			Long bestValue = getBestValue(all, user);
			
			if(bestValue != null)
			{
				map.put(key, bestValue);
			}
		}
		
		return map;
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
	
	public Map<String, Long> loadUser(UUID uuid) throws RuntimeException
	{
		if(userPermission.containsKey(uuid))
		{
			return userPermission.get(uuid);
		}
		
		File file = CommonsConfig.getFile(Type.USER_DAT, false, uuid);
		Map<String, Long> map;
		
		if(!file.exists() || file.length() == 0L)
		{
			userPermission.put(uuid, map = new HashMap<>());
			return map;
		}
		
		try(FileInputStream fileIn = new FileInputStream(file);
				ByteArrayInputStream stream = new ByteArrayInputStream(ByteStreams.toByteArray(fileIn));
				ObjectInputStream in = new ObjectInputStream(stream))
		{
			userPermission.put(uuid, map = (Map<String, Long>) in.readObject());
			return map;
		}
		catch(ClassNotFoundException | IOException ex)
		{
			throw new RuntimeException(ex);
		}
	}
	
	public void save()
	{
		File file;
		
		Iterator<Entry<UUID, Map<String, Long>>> iterator = userPermission.entrySet().iterator();
		
		while(iterator.hasNext())
		{
			Entry<UUID, Map<String, Long>> entry = iterator.next();
			UUID key = entry.getKey();
			Map<String, Long> value = entry.getValue();
			
			file = CommonsConfig.getFile(Type.USER_DAT, !value.isEmpty(), key);
			
			if(file.exists())
			{
				try(FileOutputStream fileOut = new FileOutputStream(file);
						ByteArrayOutputStream outStream = new ByteArrayOutputStream();
						ObjectOutputStream out = new ObjectOutputStream(outStream);)
				{
					if(value.isEmpty())
					{
						file.delete();
					}
					else
					{
						out.writeObject(value);
						fileOut.write(outStream.toByteArray());
					}
					
					if(!Bukkit.getOfflinePlayer(key).isOnline())
					{
						iterator.remove();
					}
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
			}
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
	
	public Map<UUID, Map<String, Long>> getUserPermission()
	{
		return userPermission;
	}
	
	public Map<String, Map<UUID, Long>> getGroupUser()
	{
		return groupUser;
	}
	
	public Map<String, Map<String, Long>> getGroupPermission()
	{
		return groupPermission;
	}
	
	public static Permission getInstance()
	{
		return instance;
	}
}
