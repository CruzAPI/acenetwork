package br.com.acenetwork.commons.executor;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.google.common.io.ByteStreams;

import br.com.acenetwork.commons.Common;
import br.com.acenetwork.commons.CommonsUtil;
import br.com.acenetwork.commons.constants.Currency;
import br.com.acenetwork.commons.event.SocketEvent;
import br.com.acenetwork.commons.manager.CommonsConfig;
import br.com.acenetwork.commons.manager.CommonsConfig.Type;
import br.com.acenetwork.commons.manager.Message;
import br.com.acenetwork.commons.manager.PlayerData;
import br.com.acenetwork.commons.player.CommonPlayer;
import br.com.acenetwork.commons.player.craft.CraftCommonPlayer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

public class Deposit implements TabExecutor, Listener
{
	public Deposit()
	{
		Bukkit.getPluginManager().registerEvents(this, Common.getPlugin());
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
		
		bundle = ResourceBundle.getBundle("message", cp.getLocale());

		if(args.length == 1)
		{
			try
			{
				if(cp.isRequesting())
				{
					return true;
				}
				
				int amount = Integer.valueOf(args[0]);
				
				if(amount <= 0)
				{
					throw new NumberFormatException();
				}
				
				Runtime.getRuntime().exec(String.format("node %s/reset/deposit %s %s %s %s", System.getProperty("user.home"),
						Common.getSocketPort(), 
						cp.requestDatabase(), 
						p.getUniqueId(), 
						amount));
			}
			catch(NumberFormatException e)
			{
				p.sendMessage(ChatColor.RED + bundle.getString("commons.cmds.invalid-number-format"));
			}
			catch(IOException e)
			{
				e.printStackTrace();
				p.sendMessage(ChatColor.RED + bundle.getString("commons.unexpected-error"));
			}
		}
		else
		{
			TextComponent[] extra = new TextComponent[1];
			
			extra[0] = new TextComponent("/" + aliases + " <" + bundle.getString("commons.words.amount") + ">");
			
			TextComponent text = Message.getTextComponent(bundle.getString("commons.cmds.wrong-syntax-try"), extra);
			text.setColor(ChatColor.RED);
			CommonsUtil.sendMessage(sender, text);
		}

		return true;
	}
	
	@EventHandler
	public void onSocketEvent(SocketEvent e)
	{
		String[] args = e.getArgs();
		
		String cmd = args[0];
		
		if(cmd.equalsIgnoreCase("deposit"))
		{
			int taskId = Integer.valueOf(args[1]);
			UUID uuid = UUID.fromString(args[2]);
			OfflinePlayer op = Bukkit.getOfflinePlayer(uuid);
			Player p = op.getPlayer();
			CommonPlayer cp = CraftCommonPlayer.get(p);
			double amount = Double.valueOf(args[3]);
			boolean isQueued = Bukkit.getScheduler().isQueued(taskId);
			ResourceBundle bundle = cp == null ? null : ResourceBundle.getBundle("message", cp.getLocale());
			
			if(isQueued)
			{
				Bukkit.getScheduler().cancelTask(taskId);
			}
			
			if(amount <= 0)
			{
				if(bundle != null)
				{
					p.sendMessage(ChatColor.RED + bundle.getString("commons.cmds.insufficient-balance"));
				}
				
				return;
			}
			
			new Thread(() ->
			{
				try
				{
					PlayerData pdMemory = PlayerData.load(p.getUniqueId());
					PlayerData pdDisk = new PlayerData(pdMemory);
					pdDisk.setBTA(pdDisk.getBTA() + amount);
					
					Map<UUID, PlayerData> map = new HashMap<>();
					map.put(p.getUniqueId(), pdDisk);
					PlayerData.save(map);
					
					pdMemory.setBTA(pdMemory.getBTA() + amount);
					
					TextComponent[] extra;
					TextComponent text;
					DecimalFormat df = new DecimalFormat("#.##");
					
					extra = new TextComponent[2];
					extra[0] = new TextComponent(op.getName());
					extra[1] = new TextComponent(df.format(amount) + " $BTA");
					
					ResourceBundle consoleBundle = ResourceBundle.getBundle("message");
					text = Message.getTextComponent(consoleBundle.getString("commons.cmd.deposit.log"), extra);
					Bukkit.getConsoleSender().sendMessage(text.toLegacyText());
					
					if(bundle != null)
					{
						df.setDecimalFormatSymbols(new DecimalFormatSymbols(bundle.getLocale()));
						
						extra = new TextComponent[1];
						
						extra[0] = new TextComponent(df.format(amount) + " $BTA");
						extra[0].setColor(ChatColor.DARK_PURPLE);
						
						text = Message.getTextComponent(bundle.getString("commons.cmd.deposit"), extra);
						text.setColor(ChatColor.LIGHT_PURPLE);
						
						p.spigot().sendMessage(text);
					}
				}
				catch(RuntimeException ex)
				{
					if(isQueued && bundle != null)
					{
						p.sendMessage(ChatColor.RED + bundle.getString("commons.unexpected-error"));
					}
					
					throw ex;
				}
			}).start();
		}
	}
}