package br.com.acenetwork.survival.executor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import br.com.acenetwork.commons.manager.Message;
import br.com.acenetwork.commons.player.CommonPlayer;
import br.com.acenetwork.commons.player.craft.CraftCommonPlayer;
import br.com.acenetwork.survival.manager.HomeObj;
import br.com.acenetwork.survival.warp.Warp;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

public class Sethome implements TabExecutor
{
	private static final int MAX_HOME = 3;
	private static final int VIP_MAX_HOME = 50;
	
	@Override
	public List<String> onTabComplete(CommandSender arg0, Command arg1, String arg2, String[] arg3)
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
		
		if(args.length == 1)
		{
			Warp warp = Warp.MAP.get(p.getWorld().getUID());
			
			if(!warp.canSetHome())
			{
				p.sendMessage(ChatColor.RED + bundle.getString("cmd.sethome.can-not-set-home-in-this-world"));
				return true;
			}
			
			
			String key = args[0].toLowerCase();
			
			int blocksAwayFromSpawn = warp.blocksAwayFromSpawnToSetHome();
			
			if(Math.abs(p.getLocation().getBlockX()) < blocksAwayFromSpawn
					 && Math.abs(p.getLocation().getBlockZ()) < blocksAwayFromSpawn)
			{
				TextComponent[] extra = new TextComponent[1];
				
				extra[0] = new TextComponent(blocksAwayFromSpawn + "");
				
				TextComponent text = Message.getTextComponent(bundle.getString("raid.cmd.sethome.need-away-from-spawn"), extra);
				text.setColor(ChatColor.RED);
				sender.sendMessage(text.toLegacyText());
				
				return true;
			}
			
			Map<String, HomeObj> homeMap = Home.getInstance().load(p.getUniqueId());
			
			if(homeMap.containsKey(key))
			{
				TextComponent[] extra = new TextComponent[1];
				
				extra[0] = new TextComponent(key);
				
				TextComponent text = Message.getTextComponent(bundle.getString("raid.cmd.sethome.home-already-set"), extra);
				text.setColor(ChatColor.RED);
				p.spigot().sendMessage(text);
				
				return true;
			}
			
			if(!key.matches("[a-z0-9]{0,16}"))
			{
				TextComponent text = new TextComponent(bundle.getString("commons.cmds.invalid-input"));
				text.setColor(ChatColor.RED);
				p.spigot().sendMessage(text);
				
				return true;
			}
			
			int maxHome = cp.hasPermission("vip.sethome") ? VIP_MAX_HOME : MAX_HOME;
			
			if(homeMap.size() >= maxHome)
			{
				TextComponent text = new TextComponent(bundle.getString("raid.cmd.sethome.limit-reached"));
				text.setColor(ChatColor.RED);
				p.spigot().sendMessage(text);
				
				return true;
			}
			
			homeMap.put(key, new HomeObj(p.getLocation()));
			
			TextComponent[] extra = new TextComponent[1];
			
			extra[0] = new TextComponent(args[0].toString());
			extra[0].setColor(ChatColor.YELLOW);
			
			TextComponent text = Message.getTextComponent(bundle.getString("raid.cmd.sethome.home-set"), extra);
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