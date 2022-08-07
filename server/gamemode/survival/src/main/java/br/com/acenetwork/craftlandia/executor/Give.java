package br.com.acenetwork.craftlandia.executor;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import br.com.acenetwork.commons.CommonsUtil;
import br.com.acenetwork.commons.manager.Message;
import br.com.acenetwork.commons.player.CommonPlayer;
import br.com.acenetwork.commons.player.craft.CraftCommonPlayer;
import br.com.acenetwork.craftlandia.Rarity;
import br.com.acenetwork.craftlandia.Util;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

public class Give implements TabExecutor
{
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String aliases, String[] args)
	{
		List<String> list = new ArrayList<>();
		
		if(sender instanceof Player)
		{
			Player p = (Player) sender;
			CommonPlayer cp = CraftCommonPlayer.get(p);
			
			if(!cp.hasPermission("cmd.give"))
			{
				return list;
			}
		}
		
		if(args.length == 1)
		{
			for(Player all : Bukkit.getOnlinePlayers())
			{
				if(all.getName().toLowerCase().startsWith(args[0].toLowerCase()))
				{
					list.add(all.getName());
				}
			}
			
			return list;
		}
		
		if(args.length == 2)
		{
			for(Material type : Material.values())
			{
				if(type.name().toUpperCase().startsWith(args[1].toUpperCase()))
				{
					list.add(type.name());
				}
			}
			
			return list;
		}
		
		return list;
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
			
			if(!cp.hasPermission("cmd.give"))
			{
				p.sendMessage(ChatColor.RED + bundle.getString("commons.cmds.permission"));
				return true;
			}
		}
		
		if(args.length > 1 && args.length < 6)
		{
			Player t = Bukkit.getPlayer(args[0]);
			
			if(t == null)
			{
				sender.sendMessage(ChatColor.RED + bundle.getString("commons.cmds.player-not-found"));
				return true;
			}
			
			try
			{
				Material type;
				
				try
				{
					type = Material.getMaterial(Integer.valueOf(args[1]));
				}
				catch(NumberFormatException e)
				{
					type = Material.valueOf(args[1].toUpperCase());
				}
				
				if(type == null)
				{
					throw new IllegalArgumentException();
				}
				
				int amount = args.length > 2 ? Integer.valueOf(args[2]) : 1;
				short data = args.length > 3 ? Short.valueOf(args[3]) : 0;	
				byte commodityData = args.length > 4 ? Byte.valueOf(args[4]) : 0;	
				
				if(amount < 0)
				{
					amount = 1;
				}
				
				final int finalAmount = amount;
				
				ItemStack item = new ItemStack(type, 1, data);
				
				Util.setCommodity(item, Rarity.getByData(commodityData));
				
				while(amount > 0)
				{
					int itemAmount = Math.min(type.getMaxStackSize(), amount);
					amount -= itemAmount;
					item.setAmount(itemAmount);
					t.getInventory().addItem(item);
				}
				
				TextComponent[] extra = new TextComponent[3];
				
				extra[0] = new TextComponent(type.name());
				extra[0].setColor(ChatColor.YELLOW);
				
				extra[1] = new TextComponent(finalAmount + "");
				extra[1].setColor(ChatColor.YELLOW);
				
				extra[2] = new TextComponent(t.getName());
				extra[2].setColor(ChatColor.YELLOW);
				
				TextComponent text = Message.getTextComponent(bundle.getString("commons.cmd.give.given"), extra);
				text.setColor(ChatColor.GREEN);
				CommonsUtil.sendMessage(sender, text);
			}
			catch(NumberFormatException e)
			{
				sender.sendMessage(ChatColor.RED + bundle.getString("commons.cmds.invalid-number-format"));
			}
			catch(IllegalArgumentException e)
			{
				sender.sendMessage(ChatColor.RED + bundle.getString("commons.cmds.id-not-found"));
			}
		}
		else
		{
			TextComponent[] extra = new TextComponent[1];
			
			extra[0] = new TextComponent("/" + aliases);
			extra[0].addExtra(" <" + bundle.getString("commons.words.player") + ">");
			extra[0].addExtra(" <" + bundle.getString("commons.words.item") + ">");
			extra[0].addExtra(" [" + bundle.getString("commons.words.amount") + "]");
			extra[0].addExtra(" [" + bundle.getString("commons.words.data") + "]");
			
			TextComponent text = Message.getTextComponent(bundle.getString("commons.cmds.wrong-syntax-try"), extra);
			text.setColor(ChatColor.RED);
			CommonsUtil.sendMessage(sender, text);
		}
		
		return false;
	}
}
