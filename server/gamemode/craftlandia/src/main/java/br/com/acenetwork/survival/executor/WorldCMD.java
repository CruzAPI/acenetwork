package br.com.acenetwork.survival.executor;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import br.com.acenetwork.commons.manager.Message;
import br.com.acenetwork.commons.player.CommonPlayer;
import br.com.acenetwork.commons.player.craft.CraftCommonPlayer;
import br.com.acenetwork.survival.warp.Warp;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

public class WorldCMD implements TabExecutor
{
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args)
	{
		return new ArrayList<>();
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
			
			Warp warp = Warp.getByWorld(p.getWorld());
			String pvpStatus = warp.hasPVP() 
					? "" + ChatColor.DARK_RED + ChatColor.BOLD + "PVP ON"
					: "" + ChatColor.DARK_GREEN + ChatColor.BOLD + "PVP OFF";
			extra[0] = new TextComponent(warp.getColoredName() + ChatColor.GRAY + ChatColor.BOLD + " - " + pvpStatus);
			
			TextComponent text = Message.getTextComponent(bundle.getString("cmd.world"), extra);
			text.setColor(ChatColor.GRAY);
			p.spigot().sendMessage(text);
		}
		else
		{
			TextComponent[] extra = new TextComponent[1];
			
			extra[0] = new TextComponent("/" + aliases);
			
			TextComponent text = Message.getTextComponent(bundle.getString("commons.cmds.wrong-syntax-try"), extra);
			text.setColor(ChatColor.RED);
			p.spigot().sendMessage(text);
		}
		
		return false;
	}
}
