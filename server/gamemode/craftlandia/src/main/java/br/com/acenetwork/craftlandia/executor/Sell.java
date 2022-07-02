package br.com.acenetwork.craftlandia.executor;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.accessibility.AccessibleKeyBinding;

import java.util.ResourceBundle;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import br.com.acenetwork.commons.CommonsUtil;
import br.com.acenetwork.commons.manager.CommonsConfig;
import br.com.acenetwork.commons.manager.IdData;
import br.com.acenetwork.commons.manager.Message;
import br.com.acenetwork.commons.player.CommonPlayer;
import br.com.acenetwork.commons.player.craft.CraftCommonPlayer;
import br.com.acenetwork.craftlandia.event.SellItemEvent;
import br.com.acenetwork.craftlandia.manager.AmountPrice;
import br.com.acenetwork.craftlandia.manager.Config;
import br.com.acenetwork.craftlandia.manager.Config.Type;
import br.com.acenetwork.craftlandia.manager.CryptoInfo;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.TranslatableComponent;

public class Sell implements TabExecutor
{
	public enum SellType
	{
		HAND, ALL;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String aliases, String[] args)
	{
		return new ArrayList<>();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String aliases, String[] args)
	{
		ResourceBundle bundle = ResourceBundle.getBundle("message");
		
		if(!(sender instanceof Player))
		{
			sender.sendMessage(ChatColor.RED + bundle.getString("commons.cmds.cant-perform-command"));
			return true;
		}
		
		Player p = (Player) sender;
		CommonPlayer cp = CraftCommonPlayer.get(p);
		
		bundle = ResourceBundle.getBundle("message", cp.getLocale());
		
		if(args.length == 0)
		{
			sell(cp, SellType.HAND);
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
	
	public static void sell(CommonPlayer cp, SellType sellType)
	{
		ResourceBundle bundle = ResourceBundle.getBundle("message", cp.getLocale());
		ResourceBundle minecraftBundle = ResourceBundle.getBundle("minecraft", cp.getLocale());
		
		Player p = cp.getPlayer();

		double balance = cp.getBalance();
		
		Map<IdData, AmountPrice> map = new HashMap<>();
		
		List<ItemStack> itemsToSell = new ArrayList<>();
		
		if(sellType == SellType.HAND)
		{
			itemsToSell.add(p.getItemInHand());
		}
		else
		{
			for(ItemStack content : p.getInventory())
			{
				itemsToSell.add(content);
			}
		}
		
		double total = 0.0D;
		
		List<SellItemEvent> events = new ArrayList<>();
		
		File file = Config.getFile(Type.PRICE, false);
		
		try(RandomAccessFile access = new RandomAccessFile(file, "rw"))
		{
			for(int i = 0; i < itemsToSell.size(); i++)
			{
				ItemStack item = itemsToSell.get(i);
				
				if(item == null || item.getType() == Material.AIR)
				{
					if(sellType == SellType.HAND)
					{
						TextComponent text = new TextComponent(bundle.getString("raid.cmd.sell.need-hoolding-item"));
						text.setColor(ChatColor.RED);
						p.spigot().sendMessage(text);
						return;
					}
					
					continue;
				}
				
				final int id = item.getTypeId();
				final short data = item.getData().getData();
				
				IdData idData = new IdData(id, data);
				Map<IdData, CryptoInfo> priceMap = Price.getPriceMap();
				
				if(!priceMap.containsKey(idData))
				{
					if(sellType == SellType.HAND)
					{
						p.sendMessage(ChatColor.RED + bundle.getString("raid.cmd.sell.item-not-for-sale"));
						return;
					}
					
					continue;
				}
				
				CryptoInfo cryptoInfo = priceMap.get(idData);
				
				final double oldMarketCap = cryptoInfo.getMarketCap();
				double marketCap = oldMarketCap;
				final double oldCirculatingSupply = cryptoInfo.getCirculatingSupply();
				double circulatingSupply = oldCirculatingSupply;
				
				final int amountToSell = item.getAmount();
				
				if(sellType == SellType.HAND)
				{
					p.setItemInHand(null);
				}
				else
				{
					p.getInventory().setItem(i, null);
				}
				
				double oldPrice = marketCap / circulatingSupply;
				double newPrice = (marketCap - oldPrice * amountToSell) / (circulatingSupply + amountToSell);
				
				final double price = (oldPrice + newPrice) / 2.0D;
				
				double shards = price * amountToSell;
				
				marketCap -= shards;
				circulatingSupply += amountToSell;
				
				cryptoInfo.setMarketCap(marketCap);
				cryptoInfo.setCirculatingSupply(circulatingSupply);
				
				cp.setBalance(balance += shards);
				
				total += shards;
				
				AmountPrice ap = map.containsKey(idData) ? map.get(idData) : new AmountPrice();
				
				ap.amount += amountToSell;
				ap.price += shards;
				
				map.put(idData, ap);
				
				events.add(new SellItemEvent(p, idData, amountToSell, oldMarketCap, marketCap, oldCirculatingSupply, circulatingSupply));
			}
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
		
		events.forEach(x -> Bukkit.getPluginManager().callEvent(x));
		
		if(map.isEmpty())
		{
			TextComponent text = new TextComponent(bundle.getString("raid.cmd.sellall.no-items-to-sell"));
			text.setColor(ChatColor.RED);
			p.spigot().sendMessage(text);
		}
		else
		{
			DecimalFormat df = new DecimalFormat("#.##", new DecimalFormatSymbols(bundle.getLocale()));
			
			df.setGroupingSize(3);
			df.setGroupingUsed(true);
			
			for(Entry<IdData, AmountPrice> entry : map.entrySet())
			{
				IdData idData = entry.getKey();
				AmountPrice ap = entry.getValue();
				
				TextComponent[] extra = new TextComponent[2];
				
				extra[0] = new TextComponent(ap.amount + " ");
				extra[0].addExtra(CommonsUtil.getTranslation(idData, minecraftBundle));
				extra[0].setColor(ChatColor.YELLOW);
				
				extra[1] = new TextComponent(df.format(ap.price));
				extra[1].setColor(ChatColor.YELLOW);
				
				TextComponent text = Message.getTextComponent(bundle.getString("raid.cmd.sell.item-sold"), extra);
				text.setColor(ChatColor.GREEN);
				p.spigot().sendMessage(text);
			}
			
			if(sellType == SellType.ALL)
			{
				TextComponent[] extra = new TextComponent[1];
				
				extra[0] = new TextComponent(df.format(total));
				extra[0].setColor(ChatColor.YELLOW);
				
				TextComponent text = Message.getTextComponent(bundle.getString("raid.cmd.sellall.totalizing"), extra);
				text.setColor(ChatColor.GREEN);
				p.spigot().sendMessage(text);
			}
		}
	}
}

