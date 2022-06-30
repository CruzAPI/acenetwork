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
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
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
import com.google.common.io.Files;

import br.com.acenetwork.commons.Common;
import br.com.acenetwork.commons.CommonsUtil;
import br.com.acenetwork.commons.manager.CommonsConfig;
import br.com.acenetwork.commons.manager.CommonsConfig.Type;
import br.com.acenetwork.commons.manager.Message;
import br.com.acenetwork.commons.manager.ObjectField;
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
	
	@SuppressWarnings("unchecked")
	public Permission() throws RuntimeException
	{
		File file = CommonsConfig.getFile(Type.PERMISSIONS, false);
		
		if(file.exists())
		{
			try(FileInputStream fileIn = new FileInputStream(file);
					ByteArrayInputStream stream = new ByteArrayInputStream(ByteStreams.toByteArray(fileIn));
					ObjectInputStream in = new ObjectInputStream(stream))
			{
				groupPermission = (Map<String, Map<String, Long>>) in.readObject();
				groupUser = (Map<String, Map<UUID, Long>>) in.readObject();
				userPermission = (Map<UUID, Map<String, Long>>) in.readObject();
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
			userPermission = new HashMap<>();
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
			if(args.length == 4 && (args[0].equalsIgnoreCase("group") || args[0].equalsIgnoreCase("g")) 
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
				
				Map<String, Long> MAP = groupPermission.get(group);
				MAP.put(permission, 0L);
				
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
				
				if(!groupPermission.containsKey(group))
				{
					sender.sendMessage(ChatColor.RED + bundle.getString("commons.cmd.permission.group-not-found"));
					return true;
				}
				
				Map<String, Long> map = groupPermission.get(group);
				
				final ObjectField<Integer> of = new ObjectField<>();
				
				if(permission.endsWith("*"))
				{
					map.keySet().stream().filter(x -> x.startsWith(permission.substring(0, permission.length() - 1)))
							.forEach(x -> 
							{
								if(map.remove(x) != null)
								{
									of.object++;
								}
							});
				}
				else if(map.remove(permission) != null)
				{
					of.object++;
				}

				if(of.object > 0)
				{
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
			else if(args.length == 5 && (args[0].equalsIgnoreCase("group") || args[0].equalsIgnoreCase("g")) &&
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
				
				map.put(op.getUniqueId(), 0L);
				
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
			else if(args.length == 4 && (args[0].equalsIgnoreCase("user") || args[0].equalsIgnoreCase("u")) &&
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
				
				if(!userPermission.containsKey(op.getUniqueId()))
				{
					userPermission.put(op.getUniqueId(), new HashMap<>());
				}
				
				Map<String, Long> map = userPermission.get(op.getUniqueId());
				
				map.put(permission, 0L);
				
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
				
				final ObjectField<Integer> of = new ObjectField<>();
				
				if(userPermission.containsKey(op.getUniqueId()))
				{
					Map<String, Long> map = userPermission.get(op.getUniqueId());
					
					if(permission.endsWith("*"))
					{
						map.keySet().stream().filter(x -> x.startsWith(permission.substring(0, permission.length() - 1)))
								.forEach(x -> 
								{
									if(map.remove(x) != null)
									{
										of.object++;
									}
								});
					}
					else if(map.remove(permission) != null)
					{
						of.object++;
					}
				}

				if(of.object > 0)
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
							return value != null && value < System.currentTimeMillis();
						}).collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue()));
				
				String commandLine = "/" + cmd.getName();
				
				for(int i = 0; i < args.length && i < 3; i++)
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
							return value != null && value < System.currentTimeMillis();
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
							return value != null && value < System.currentTimeMillis();
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
							return value != null && value < System.currentTimeMillis();
						}).collect(Collectors.toMap(Entry::getKey, x -> x.getValue().get(op.getUniqueId())));
				
				String commandLine = "/" + cmd.getName();
				
				for(int i = 0; i < args.length && i < 4; i++)
				{
					commandLine += " " + args[i];
				}
				
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
		Player p = cp.getPlayer();
		
		if(map.isEmpty())
		{
			p.sendMessage("map is empty");
			return;
		}
		
		final byte pageSize = 18;
		
		Map<String, Long> treeMap = new TreeMap<>(map);
		List<Entry<String, Long>> list = new ArrayList<>(treeMap.entrySet());
		
		if(list.size() > (page - 1) * pageSize && page > 0)
		{
			final int maxPage = list.size() / pageSize + (list.size() % pageSize == 0 ? 0 : 1);
			p.sendMessage("List [" + page + "/" + maxPage + "]");
			
			for(int i = (page - 1) * pageSize; i < page * pageSize && i < list.size(); i++)
			{
				Entry<String, Long> entry = list.get(i);
				
				String key = entry.getKey();
				long value = entry.getValue();
				
				p.sendMessage(key + " " + value);
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
			p.sendMessage("page not found");
		}
	}
	
	public void save()
	{
		long time = System.currentTimeMillis();

		File file = CommonsConfig.getFile(Type.PERMISSIONS, true);
		
		try(FileOutputStream fileOut = new FileOutputStream(file);
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				ObjectOutputStream out = new ObjectOutputStream(stream);)
		{
			out.writeObject(groupPermission);
			out.writeObject(groupUser);
			out.writeObject(userPermission);
			fileOut.write(stream.toByteArray());
			
			time = System.currentTimeMillis() - time;
			
			Bukkit.broadcastMessage(ChatColor.GREEN + "Time elapsed: " + time + "ms");
			
			DecimalFormat df = new DecimalFormat();
			df.setGroupingSize(3);
			df.setGroupingUsed(true);
			
			Bukkit.broadcastMessage("Bytes: " + df.format(stream.toByteArray().length));

		}
		catch(IOException e1)
		{
			e1.printStackTrace();
		}
	}
}
