package br.com.acenetwork.survival.executor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import br.com.acenetwork.commons.CommonsUtil;
import br.com.acenetwork.commons.manager.Message;
import br.com.acenetwork.commons.player.CommonPlayer;
import br.com.acenetwork.commons.player.craft.CraftCommonPlayer;
import br.com.acenetwork.survival.manager.ChannelCommand;
import br.com.acenetwork.survival.manager.HomeObj;
import br.com.acenetwork.survival.player.SurvivalPlayer;
import br.com.acenetwork.survival.warp.Warp;
import br.com.acenetwork.survival.warp.Warp.Result;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class Visit implements TabExecutor, ChannelCommand
{
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args)
	{
		List<String> list = new ArrayList<>();
		
		if(!(sender instanceof Player))
		{
			return list;
		}
		
		Player p = (Player) sender;
		
		if(args.length == 1)
		{
			for(Player all : Bukkit.getOnlinePlayers())
			{
				if(all == p || !p.canSee(all))
				{
					continue;
				}
				
				if(all.getName().startsWith(args[0].toLowerCase()))
				{
					list.add(all.getName());
				}
			}
			
			return list;
		}
		
		if(args.length == 2)
		{
			OfflinePlayer op = CommonsUtil.getOfflinePlayerIfCached(args[0]);
			
			if(op == null || p == op)
			{
				return list;
			}
			
			Map<String, HomeObj> homeMap = Home.getInstance().load(op.getUniqueId());
			
			for(Entry<String, HomeObj> entry : homeMap.entrySet())
			{
				String key = entry.getKey();
				HomeObj value = entry.getValue();
				
				if(value.isPublic() && key.toLowerCase().startsWith(args[1].toLowerCase()))
				{
					list.add(key.toLowerCase());
				}
			}
			
			return list;
		}
		
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
		
		if(args.length == 1)
		{
			OfflinePlayer op = CommonsUtil.getOfflinePlayerIfCached(args[0]);
			
			if(op == null)
			{
				p.sendMessage(ChatColor.RED + bundle.getString("commons.cmds.user-not-found"));
				return true;
			}
			
			if(op == p)
			{
				p.sendMessage(ChatColor.RED + bundle.getString("cmd.visit.can-not-visit-yourself"));
				return true;
			}
			
			TextComponent[] extra = new TextComponent[2];
			String displayName = op.isOnline() ? op.getPlayer().getDisplayName() : ChatColor.GRAY + op.getName();
			
			extra[0] = new TextComponent(displayName); 
			extra[1] = new TextComponent("");
			
			Map<String, HomeObj> homeMap = Home.getInstance().load(op.getUniqueId());
			
			Map<String, HomeObj> publicHomeMap = homeMap.entrySet().stream().filter(x -> x.getValue().isPublic())
					.collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue()));
			
			Iterator<Entry<String, HomeObj>> iterator = publicHomeMap.entrySet().iterator();
			
			if(!iterator.hasNext())
			{
				sender.sendMessage(ChatColor.RED + bundle.getString("cmd.visit.this-user-do-not-have-public-homes"));
				return true;
			}
			
			while(iterator.hasNext())
			{
				Entry<String, HomeObj> entry = iterator.next();
				String key = entry.getKey();
				HomeObj value = entry.getValue();
				
				TextComponent text = new TextComponent(key);
				
				if(value.getWorld() == p.getWorld() || cp.hasPermission("portals.bypass"))
				{
					text.setColor(ChatColor.YELLOW);
					text.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/visit " + op.getName() + " " + key));
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
				
				extra[1].addExtra(text);
				
				if(iterator.hasNext())
				{
					extra[1].addExtra(", ");
				}
			}
			
			TextComponent text = Message.getTextComponent(bundle.getString("cmd.visit.public-homes"), extra);
			text.setColor(ChatColor.GREEN);
			p.spigot().sendMessage(text);
		}
		else if(args.length == 2)
		{
			OfflinePlayer op = CommonsUtil.getOfflinePlayerIfCached(args[0]);
			
			if(op == null)
			{
				p.sendMessage(ChatColor.RED + bundle.getString("commons.cmds.user-not-found"));
				return true;
			}
			
			if(op == p)
			{
				p.sendMessage(ChatColor.RED + bundle.getString("cmd.visit.can-not-visit-yourself"));
				return true;
			}
			
			String key = args[1].toLowerCase();
			HomeObj homeObj = Home.getInstance().load(op.getUniqueId()).get(key);
			
			if(homeObj == null || !homeObj.isPublic())
			{
				TextComponent[] extra = new TextComponent[1];
				
				extra[0] = new TextComponent(key);
				
				TextComponent text = Message.getTextComponent(bundle.getString("cmd.visit.home-not-found-or-not-public"), extra);
				text.setColor(ChatColor.RED);
				p.spigot().sendMessage(text);
				
				return true;
			}
			
			Location destiny = homeObj.getLocation();
			
			if(destiny.getWorld() != p.getWorld() && !cp.hasPermission("portals.bypass"))
			{
				TextComponent[] extra = new TextComponent[1];
				
				extra[0] = new TextComponent("/" + aliases);
				
				TextComponent text = Message.getTextComponent(bundle.getString("cmd.home.between-dimensions"), extra);
				text.setColor(ChatColor.RED);
				p.spigot().sendMessage(text);
				
				return true;
			}
			
			String displayName = op.isOnline() ? op.getPlayer().getDisplayName() : ChatColor.GRAY + op.getName();
			
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
				
				sp.channel(this, warp.getChannelingTicks(sp), destiny, displayName, key);
			}
			else
			{
				run(cp, destiny, displayName, key);
			}
		}
		else
		{
			TextComponent[] extra = new TextComponent[1];
			
			String nounHome = bundle.getString("commons.words.home");
			String nounPlayer = bundle.getString("commons.words.player");
			
			extra[0] = new TextComponent("/" + aliases + " <" + nounPlayer + "> [" + nounHome + "]");
			
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
		
		TextComponent[] extra = new TextComponent[2];
		
		extra[0] = new TextComponent(args[0].toString());
		extra[0].setColor(ChatColor.YELLOW);
		
		extra[1] = new TextComponent(args[1].toString());
		extra[1].setColor(ChatColor.YELLOW);
		
		TextComponent text = Message.getTextComponent(bundle.getString("cmd.visit"), extra);
		text.setColor(ChatColor.GREEN);
		p.spigot().sendMessage(text);
	}
}
