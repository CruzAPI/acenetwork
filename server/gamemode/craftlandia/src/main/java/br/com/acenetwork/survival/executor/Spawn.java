package br.com.acenetwork.survival.executor;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import br.com.acenetwork.commons.manager.Message;
import br.com.acenetwork.commons.player.CommonPlayer;
import br.com.acenetwork.commons.player.craft.CraftCommonPlayer;
import br.com.acenetwork.survival.Main;
import br.com.acenetwork.survival.manager.ChannelCommand;
import br.com.acenetwork.survival.player.SurvivalPlayer;
import br.com.acenetwork.survival.warp.Warp;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

public class Spawn implements TabExecutor, ChannelCommand
{
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
		
		if(args.length == 0)
		{
			Warp warp = Warp.MAP.get(p.getWorld().getUID());
			
			Location spawnLocation = warp.getSpawnLocation();
			
			if(spawnLocation == null)
			{
				p.sendMessage(ChatColor.RED + bundle.getString("cmd.spawn.do-not-have"));
				return true;
			}
			
			if(cp instanceof SurvivalPlayer)
			{
				SurvivalPlayer sp = (SurvivalPlayer) cp;
				long ticks = warp.getChannelingTicks(sp);
				
				sp.channel(this, ticks, spawnLocation);
			}
			else
			{
				run(cp, spawnLocation);
			}
		}
		else
		{
			TextComponent[] extra = new TextComponent[1];
			
			extra[0] = new TextComponent("/" + aliases);
			
			TextComponent text = Message.getTextComponent(bundle.getString("commons.cmds.wrong-syntax-try"), extra);
			text.setColor(ChatColor.RED);
			sender.sendMessage(text.toLegacyText());
		}
		
		return false;
	}
	
	@Override
	public void run(CommonPlayer cp, Location destiny, Object... args)
	{
		ChannelCommand.super.run(cp, destiny);
		cp.setInvincibility(true);
	}
}