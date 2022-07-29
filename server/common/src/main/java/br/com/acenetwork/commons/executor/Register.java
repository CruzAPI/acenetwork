package br.com.acenetwork.commons.executor;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import br.com.acenetwork.commons.Common;
import br.com.acenetwork.commons.CommonsUtil;
import br.com.acenetwork.commons.manager.CommonPlayerData;
import br.com.acenetwork.commons.manager.Message;
import br.com.acenetwork.commons.player.CommonPlayer;
import br.com.acenetwork.commons.player.craft.CraftCommonPlayer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

public class Register implements TabExecutor, Listener
{
	public Register()
	{
		Bukkit.getPluginManager().registerEvents(this, Common.getInstance());
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args)
	{
		return new ArrayList<>();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String aliases, String[] args)
	{
		ResourceBundle bundle = ResourceBundle.getBundle("message");
		
		if(!(sender instanceof Player))
		{
			TextComponent text = new TextComponent(bundle.getString("commons.cmds.cant-perform-command"));
			text.setColor(ChatColor.RED);
			CommonsUtil.sendMessage(sender, text);
			return true;
		}
		
		Player p = (Player) sender;
		CommonPlayer cp = CraftCommonPlayer.get(p);
		CommonPlayerData data = cp.getCommonPlayerData();
		
		bundle = ResourceBundle.getBundle("message", cp.getLocale());

		if(args.length == 1 || args.length == 2)
		{
			if(cp.isLogged())
			{
				return true;
			}
			
			if(data.getPassword() != null)
			{
				return true;
			}
			
			String password = args[0];
			
			if(!password.matches("[\\p{Punct}\\w]*"))
			{
				p.sendMessage(ChatColor.RED + bundle.getString("cmd.register.only-letters-numbers-and-common-punctuation"));
				return true;
			}
			
			if(password.length() < 4)
			{
				TextComponent[] extra = new TextComponent[1];
				extra[0] = new TextComponent("4");
				
				TextComponent text = Message.getTextComponent(bundle.getString("cmd.register.at-least-x-chars"), extra);
				text.setColor(ChatColor.RED);
				p.spigot().sendMessage(text);
				return true;
			}
			
			if(args.length == 2 && !args[0].equals(args[1]))
			{
				p.sendMessage(ChatColor.RED + bundle.getString("cmd.register.password-did-not-match"));
				return true;
			}
			
			try
			{
				MessageDigest m = MessageDigest.getInstance("MD5");
				data.setPassword(m.digest(password.getBytes()));
				cp.setLogged(true);
			}
			catch(NoSuchAlgorithmException e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			TextComponent[] extra = new TextComponent[1];
			
			String nounPassword = bundle.getString("noun.password");
			
			extra[0] = new TextComponent("/" + aliases + " <" + nounPassword + "> [" + nounPassword + "]");
			
			TextComponent text = Message.getTextComponent(bundle.getString("commons.cmds.wrong-syntax-try"), extra);
			text.setColor(ChatColor.RED);
			CommonsUtil.sendMessage(sender, text);
		}

		return true;
	}
}