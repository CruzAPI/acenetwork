package br.com.acenetwork.commons.executor;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import br.com.acenetwork.commons.CommonsUtil;
import br.com.acenetwork.commons.manager.CommonPlayerData;
import br.com.acenetwork.commons.manager.Message;
import br.com.acenetwork.commons.player.craft.CraftCommonPlayer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

public class Baltop implements TabExecutor
{
	public static final int SIZE = 8;
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String aliases, String[] args)
	{
		return new ArrayList<>();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String aliases, String[] args)
	{
		Player p = null;
		ResourceBundle bundle = ResourceBundle.getBundle("message");
		
		if(sender instanceof Player)
		{
			p = (Player) sender;
			bundle = ResourceBundle.getBundle("message", CraftCommonPlayer.get(p).getLocale());
		}
		
		if(args.length == 0)
		{
			Iterator<Entry<UUID, CommonPlayerData>> balTop = CommonPlayerData.getBalTopMap().entrySet().iterator();
			
			DecimalFormat df = new DecimalFormat("#.##", new DecimalFormatSymbols(bundle.getLocale()));
			
			sender.sendMessage(ChatColor.DARK_GRAY + "============" + ChatColor.DARK_GREEN + ChatColor.BOLD + " BALTOP " + ChatColor.DARK_GRAY + "============");
			
			for(int i = 0; balTop.hasNext() && i < SIZE; i++)
			{
				Entry<UUID, CommonPlayerData> entry = balTop.next();
				UUID uuid = entry.getKey();
				CommonPlayerData data = entry.getValue();
				OfflinePlayer op = Bukkit.getOfflinePlayer(uuid);
				String displayName = op.isOnline() ? op.getPlayer().getDisplayName() : ChatColor.GRAY + op.getName();
				
				sender.sendMessage((i + 1) + "º " + displayName + " " + ChatColor.YELLOW + df.format(data.getBalance()) + " SHARDS");
			}
			
			sender.sendMessage(ChatColor.DARK_GRAY + "=================================");
		}
		else
		{
			TextComponent[] extra = new TextComponent[1];
			
			extra[0] = new TextComponent("/" + aliases);
			
			TextComponent text = Message.getTextComponent(bundle.getString("commons.cmds.wrong-syntax-try"), extra);
			text.setColor(ChatColor.RED);
			CommonsUtil.sendMessage(sender, text);
		}
		
		return false;
	}
	
	public static DecimalFormat getDecimalFormat()
	{
		return new DecimalFormat("0.##");
	}
}