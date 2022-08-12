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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.google.common.io.ByteStreams;

import br.com.acenetwork.commons.CommonsUtil;
import br.com.acenetwork.commons.executor.Balance;
import br.com.acenetwork.commons.manager.IdData;
import br.com.acenetwork.commons.manager.Message;
import br.com.acenetwork.commons.player.craft.CraftCommonPlayer;
import br.com.acenetwork.craftlandia.manager.Config;
import br.com.acenetwork.craftlandia.manager.CryptoInfo;
import br.com.acenetwork.craftlandia.manager.PRICE;
import br.com.acenetwork.craftlandia.manager.Config.Type;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

public class Setprice implements TabExecutor
{
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String aliases, String[] args)
	{
		List<String> list = new ArrayList<>();
		
		if(sender instanceof Player && !CommonsUtil.hasPermission((Player) sender, "cmd.setprice"))
		{
			return list;
		}
		
		if(args.length == 1)
		{
			for(Material type : Material.values())
			{
				if(type.name().toUpperCase().startsWith(args[0].toUpperCase()))
				{
					list.add(type.name());
				}
			}
		}

		return list;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String aliases, String[] args)
	{
		ResourceBundle bundle = ResourceBundle.getBundle("message");
		ResourceBundle minecraftBundle = ResourceBundle.getBundle("minecraft");
		
		Player p = null;
		
		if(sender instanceof Player)
		{
			p = (Player) sender;
			
			bundle = ResourceBundle.getBundle("message", CraftCommonPlayer.get(p).getLocale());
			minecraftBundle = ResourceBundle.getBundle("minecraft", CraftCommonPlayer.get(p).getLocale());
			
			if(!CommonsUtil.hasPermission(p, "cmd.setprice"))
			{
				p.sendMessage(ChatColor.RED + bundle.getString("commons.cmds.permission"));
				return true;
			}
		}
		
		Map<IdData, CryptoInfo> map = Price.getInstance().getPriceMap();
		
		if(args.length == 4)
		{
			try
			{
				IdData idData = new IdData(Integer.valueOf(args[0]), Short.valueOf(args[1]));
				CryptoInfo cryptoInfo = new CryptoInfo(Double.valueOf(args[2]), Double.valueOf(args[3]));
				map.put(idData, cryptoInfo);
				p.sendMessage("setprice");
				p.sendMessage(idData + "");
				p.sendMessage(cryptoInfo + "");
			}
			catch(NumberFormatException e)
			{
				p.sendMessage(e.getMessage());
			}
		}
		else
		{
//			TextComponent[] extra = new TextComponent[1];
//			
//			extra[0] = new TextComponent("/" + aliases);
//			
//			if(sender instanceof Player)
//			{
//				extra[0].addExtra(" [" + bundle.getString("commons.words.item").toLowerCase() + "]");
//			}
//			else
//			{
//				extra[0].addExtra(" <" + bundle.getString("commons.words.item").toLowerCase() + ">");
//			}
//			
//			extra[0].addExtra(" [" + bundle.getString("commons.words.data").toLowerCase() + "]");
//			
//			TextComponent text = Message.getTextComponent(bundle.getString("commons.cmds.wrong-syntax-try"), extra);
//			text.setColor(ChatColor.RED);
//			sender.sendMessage(text.toLegacyText());
			return true;
		}
		
		return true;
	}
}