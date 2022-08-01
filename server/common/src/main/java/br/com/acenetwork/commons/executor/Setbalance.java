package br.com.acenetwork.commons.executor;

import java.io.File;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import br.com.acenetwork.commons.CommonsUtil;
import br.com.acenetwork.commons.manager.CommonsConfig;
import br.com.acenetwork.commons.manager.Message;
import br.com.acenetwork.commons.manager.CommonPlayerData;
import br.com.acenetwork.commons.player.craft.CraftCommonPlayer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

public class Setbalance implements TabExecutor
{
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String aliases, String[] args)
	{
		List<String> list = new ArrayList<>();
		
		Player p = null;
		
		if(sender instanceof Player)
		{
			p = (Player) sender;
		}
		
		if(args.length == 1)
		{
			for(Player all : Bukkit.getOnlinePlayers())
			{
				if(all.getName().toLowerCase().startsWith(args[0].toLowerCase()) && 
						(p == null || p.canSee(all)))
				{
					list.add(all.getName());
				}
			}
		}

		return list;
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
			
			if(!CommonsUtil.hasPermission(p, "cmd.setbalance"))
			{
				p.sendMessage(ChatColor.RED + bundle.getString("commons.cmds.permission"));
				return true;
			}
		}
		
		final OfflinePlayer op;
		boolean add = args.length > 0 && (args[args.length - 1].startsWith("+") || args[args.length - 1].startsWith("-"));
		double value;
		
		try
		{
			if(args.length == 1 && p != null)
			{
				op = p;
				value = Double.valueOf(args[0]);
			}
			else if(args.length == 2)
			{
				op = CommonsUtil.getOfflinePlayerIfCached(args[0]);
				value = Double.valueOf(args[1]);
			}
			else
			{
				TextComponent[] extra = new TextComponent[1];
				
				extra[0] = new TextComponent("/" + aliases);
				
				String player = bundle.getString("commons.words.player");
				String nounBalance = bundle.getString("noun.balance");
				
				if(p == null)
				{
					extra[0].addExtra(" <" + player + "> <" + nounBalance + ">");
				}
				else
				{
					extra[0].addExtra(" [" + player + "] <" + nounBalance + ">");
				}
				
				TextComponent text = Message.getTextComponent(bundle.getString("commons.cmds.wrong-syntax-try"), extra);
				text.setColor(ChatColor.RED);
				CommonsUtil.sendMessage(sender, text);
				return true;
			}
		}
		catch(NumberFormatException e)
		{
			sender.sendMessage(ChatColor.RED + bundle.getString("commons.cmds.invalid-number-format"));
			return true;
		}

		if(op == null)
		{
			sender.sendMessage(ChatColor.RED + bundle.getString("commons.cmds.user-not-found"));
			return true;
		}
		
		CommonPlayerData pd = CommonPlayerData.load(op.getUniqueId());
		
		TextComponent[] extra = new TextComponent[2];
		
		DecimalFormat df = new DecimalFormat("#.##", new DecimalFormatSymbols(bundle.getLocale()));
		df.setGroupingSize(3);
		df.setGroupingUsed(true);
		
		String displayName = op.isOnline() ? op.getPlayer().getDisplayName() : ChatColor.GRAY + op.getName();
		
		double oldBalance = pd.getBalance();
		
		if(add)
		{
			pd.setBalance(Math.max(0.0D, pd.getBalance() + value));
			
			extra[0] = new TextComponent(df.format(value));
			extra[0].setColor(value < 0.0D ? ChatColor.DARK_RED : ChatColor.DARK_GREEN);
			
			extra[1] = new TextComponent(displayName);
			
			TextComponent text = Message.getTextComponent(bundle.getString("cmd.setbalance.add"), extra);			
			text.setColor(ChatColor.GREEN);
			sender.sendMessage(text.toLegacyText());
			
		}
		else
		{
			pd.setBalance(value);
			
			extra[0] = new TextComponent(displayName);
			
			extra[1] = new TextComponent(df.format(value));
			extra[1].setColor(ChatColor.YELLOW);
			
			TextComponent text = Message.getTextComponent(bundle.getString("cmd.setbalance.set"), extra);			
			text.setColor(ChatColor.GREEN);
			sender.sendMessage(text.toLegacyText());
		}
		
		double newBalance = pd.getBalance();
		value = newBalance - oldBalance;
		
		if(op.isOnline())
		{
			op.getPlayer().sendMessage((value < 0.0D ? ChatColor.DARK_RED : ChatColor.DARK_GREEN) + "(" + (value >= 0.0D ? "+" : "") + df.format(value) + " SHARDS)");
		}
		
		return false;
	}
}