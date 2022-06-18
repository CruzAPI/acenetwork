package br.com.acenetwork.craftlandia.executor;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import br.com.acenetwork.commons.CommonsUtil;
import br.com.acenetwork.commons.executor.Balance;
import br.com.acenetwork.commons.manager.Message;
import br.com.acenetwork.commons.player.craft.CraftCommonPlayer;
import br.com.acenetwork.craftlandia.manager.Config;
import br.com.acenetwork.craftlandia.manager.Config.Type;
import br.com.acenetwork.craftlandia.manager.CryptoInfo;
import br.com.acenetwork.craftlandia.manager.PRICE;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.TranslatableComponent;

public class Price implements TabExecutor
{
	public static final Map<String, CryptoInfo> MAP = new HashMap<>();
	
	public Price()
	{
		File file = Config.getFile(Type.PRICE, false);
		
		if(!file.exists())
		{
			file.toPath().getParent().toFile().mkdirs();
			
			try
			{
				if(file.createNewFile())
				{
					try(RandomAccessFile access = new RandomAccessFile(file, "rw"))
					{
						for(PRICE price : PRICE.LIST)
						{
							access.writeInt(price.id);
							access.writeShort(price.data);
							access.writeDouble(price.marketCap);
							access.writeDouble(price.circulatingSupply);
						}
					}
					catch(IOException ex)
					{
						throw ex;
					}
				}
			}
			catch(IOException ex)
			{
				ex.printStackTrace();
				return;
			}
		}
		
		try(RandomAccessFile access = new RandomAccessFile(file, "r"))
		{
			while(access.getFilePointer() < access.length())
			{
				final long pos = access.getFilePointer();
				final int id = access.readInt();
				final short data = access.readShort();
				final double marketCap = access.readDouble();
				final double circulatingSupply = access.readDouble();
				
				MAP.put(id + ":" + data, new CryptoInfo(marketCap, circulatingSupply, pos));
			}
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
			return;
		}
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String aliases, String[] args)
	{
		List<String> list = new ArrayList<>();
		
		if(args.length == 1)
		{
			Set<Material> typeSet = new HashSet<>();
			
			for(String key : Price.MAP.keySet())
			{
				Material type = Material.getMaterial(Integer.valueOf(key.split(":")[0]));
				
				if(type != null)
				{
					typeSet.add(type);
				}
			}
			
			for(Material type : typeSet)
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
		Player p = null;
		
		if(sender instanceof Player)
		{
			p = (Player) sender;
			bundle = ResourceBundle.getBundle("message", CraftCommonPlayer.get(p).getLocale());
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

		String key = type.getId() + ":" + data;

		if(!Price.MAP.containsKey(key))
		{
			sender.sendMessage(ChatColor.RED + bundle.getString("raid.cmd.sell.item-not-for-sale"));
			return true;
		}
		
		CryptoInfo cryptoInfo = Price.MAP.get(key);
		double marketCap = cryptoInfo.getMarketCap();
		double circulatingSupply = cryptoInfo.getCirculatingSupply();
		
		String price = Balance.getDecimalFormat().format(marketCap / circulatingSupply);
		
		String translationKey = CommonsUtil.getTranslation(key);
		
		TranslatableComponent tc = new TranslatableComponent(translationKey);
		
		TextComponent text = new TextComponent("");
		text.addExtra(tc);
		text.addExtra(": ");
		text.setColor(ChatColor.GREEN);
		
		TextComponent extra = new TextComponent("" + price);
		extra.setColor(ChatColor.YELLOW);
		text.addExtra(extra);
		
		CommonsUtil.sendMessage(sender, text);

		return false;
	}
}