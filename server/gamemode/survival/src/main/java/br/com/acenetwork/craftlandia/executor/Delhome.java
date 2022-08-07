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
import br.com.acenetwork.craftlandia.manager.HomeObj;
import br.com.acenetwork.craftlandia.manager.Config.Type;
import br.com.acenetwork.craftlandia.player.SurvivalPlayer;
import br.com.acenetwork.craftlandia.warp.Warp;
import br.com.acenetwork.craftlandia.warp.Warp.Result;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class Delhome implements TabExecutor
{
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String aliases, String[] args)
	{
		List<String> list = new ArrayList<>();
		
		if(!(sender instanceof Player))
		{
			return list;
		}
		
		if(args.length != 1)
		{
			return list;
		}
		
		Player p = (Player) sender;
		
		for(String key : Home.getInstance().load(p.getUniqueId()).keySet())
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
		
		if(args.length == 1)
		{
			String key = args[0].toLowerCase();
			Map<String, HomeObj> homeMap = Home.getInstance().load(p.getUniqueId());
			
			if(homeMap.remove(key) == null)
			{
				TextComponent[] extra = new TextComponent[1];
				
				extra[0] = new TextComponent(key);
				
				TextComponent text = Message.getTextComponent(bundle.getString("raid.cmds.home-not-found"), extra);
				text.setColor(ChatColor.RED);
				p.spigot().sendMessage(text);
				
				return true;
			}
			
			TextComponent[] extra = new TextComponent[1];
			
			extra[0] = new TextComponent(args[0].toString());
			extra[0].setColor(ChatColor.YELLOW);
			
			TextComponent text = Message.getTextComponent(bundle.getString("raid.cmd.delhome.home-deleted"), extra);
			text.setColor(ChatColor.GREEN);
			p.spigot().sendMessage(text);
		}
		else
		{
			TextComponent[] extra = new TextComponent[1];
			
			extra[0] = new TextComponent("/" + aliases + " <" + bundle.getString("commons.words.home") + ">");
			
			TextComponent text = Message.getTextComponent(bundle.getString("commons.cmds.wrong-syntax-try"), extra);
			text.setColor(ChatColor.RED);
			p.spigot().sendMessage(text);
		}
		
		return false;
	}
}
