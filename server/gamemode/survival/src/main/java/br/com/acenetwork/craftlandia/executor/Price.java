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

public class Price implements TabExecutor
{
	private Map<IdData, CryptoInfo> MAP = new HashMap<>();
	private static Price instance;
	
	@SuppressWarnings("unchecked")
	public Price()
	{
		instance = this;
		
		File file = Config.getFile(Type.PRICE, false);
		
		if(!file.exists() || file.length() == 0L)
		{
			for(PRICE price : PRICE.LIST)
			{
				MAP.put(new IdData(price.id, price.data), new CryptoInfo(price.marketCap, price.circulatingSupply));
			}
			
			PRICE.LIST = null;
		}
		else
		{
			try(FileInputStream fileIn = new FileInputStream(file);
					ByteArrayInputStream streamIn = new ByteArrayInputStream(ByteStreams.toByteArray(fileIn));
					ObjectInputStream in = new ObjectInputStream(streamIn))
			{
				MAP = (Map<IdData, CryptoInfo>) in.readObject();
			}
			catch(ClassNotFoundException | IOException e)
			{
				throw new RuntimeException(e);
			}
		}
	}
	
	public void save()
	{
		File file = Config.getFile(Type.PRICE, true);
		
		try(FileOutputStream fileOut = new FileOutputStream(file);
				ByteArrayOutputStream streamOut = new ByteArrayOutputStream();
				ObjectOutputStream out = new ObjectOutputStream(streamOut))
		{
			out.writeObject(MAP);
			fileOut.write(streamOut.toByteArray());
		}
		catch(IOException e)
		{
			e.printStackTrace();
			return;
		}
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String aliases, String[] args)
	{
		List<String> list = new ArrayList<>();
		
		if(args.length == 1)
		{
			for(IdData key : MAP.keySet())
			{
				Material type = Material.getMaterial(key.getId());
				
				if(type != null && !list.contains(type.name()) && type.name().toUpperCase().startsWith(args[0].toUpperCase()))
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
		}
		
		Material type;
		short data;
		
		if(args.length == 0 && p != null)
		{
			ItemStack item = p.getItemInHand();
			type = item.getType();
			data = item.getDurability();
			
			if(type == Material.AIR)
			{
				sender.sendMessage(ChatColor.RED + bundle.getString("raid.cmd.sell.need-hoolding-item"));
				return true;
			}
		}
		else if(args.length > 0 && args.length < 3)
		{
			try
			{
				try
				{
					type = Material.getMaterial(Integer.valueOf(args[0]));
				}
				catch(NumberFormatException e)
				{
					type = Material.valueOf(args[0].toUpperCase());
				}
				
				if(type == null)
				{
					throw new IllegalArgumentException();
				}
				
				data = args.length == 2 ? Short.valueOf(args[1]) : 0;	
			}
			catch(NumberFormatException e)
			{
				sender.sendMessage(ChatColor.RED + bundle.getString("commons.cmds.invalid-number-format"));
				return true;
			}
			catch(IllegalArgumentException e)
			{
				sender.sendMessage(ChatColor.RED + bundle.getString("commons.cmds.id-not-found"));
				return true;
			}
		}
		else
		{
			TextComponent[] extra = new TextComponent[1];
			
			extra[0] = new TextComponent("/" + aliases);
			
			if(sender instanceof Player)
			{
				extra[0].addExtra(" [" + bundle.getString("commons.words.item").toLowerCase() + "]");
			}
			else
			{
				extra[0].addExtra(" <" + bundle.getString("commons.words.item").toLowerCase() + ">");
			}
			
			extra[0].addExtra(" [" + bundle.getString("commons.words.data").toLowerCase() + "]");
			
			TextComponent text = Message.getTextComponent(bundle.getString("commons.cmds.wrong-syntax-try"), extra);
			text.setColor(ChatColor.RED);
			sender.sendMessage(text.toLegacyText());
			return true;
		}

		IdData key = new IdData(type.getId(), data);

		if(!MAP.containsKey(key))
		{
			sender.sendMessage(ChatColor.RED + bundle.getString("raid.cmd.sell.item-not-for-sale"));
			return true;
		}
		
		CryptoInfo cryptoInfo = MAP.get(key);
		double marketCap = cryptoInfo.getMarketCap();
		double circulatingSupply = cryptoInfo.getCirculatingSupply();
		
		String price = Balance.getDecimalFormat().format(marketCap / circulatingSupply);
		
		TextComponent text = new TextComponent(CommonsUtil.getTranslation(key, minecraftBundle));
		text.addExtra(": ");
		text.setColor(ChatColor.GREEN);
		
		TextComponent extra = new TextComponent("" + price);
		extra.setColor(ChatColor.YELLOW);
		text.addExtra(extra);
		
		CommonsUtil.sendMessage(sender, text);

		return false;
	}
	
	public static Map<IdData, CryptoInfo> getPriceMap()
	{
		return instance.MAP;
	}
	
	public static Price getInstance()
	{
		return instance;
	}
}