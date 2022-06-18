package br.com.acenetwork.craftlandia.executor;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Directional;

import br.com.acenetwork.commons.manager.CommonsConfig;
import br.com.acenetwork.commons.manager.CommonsConfig.Type;
import br.com.acenetwork.commons.player.CommonPlayer;
import br.com.acenetwork.commons.player.craft.CraftCommonPlayer;
import br.com.acenetwork.craftlandia.ItemTag;
import br.com.acenetwork.craftlandia.Main;
import br.com.acenetwork.craftlandia.Util;
import br.com.acenetwork.craftlandia.inventory.CurrencySelector;
import br.com.acenetwork.craftlandia.inventory.EnchantSelector;
import br.com.acenetwork.craftlandia.inventory.ItemSelector;
import br.com.acenetwork.craftlandia.inventory.PriceSelector;
import br.com.acenetwork.craftlandia.inventory.RaritySelector;
import br.com.acenetwork.craftlandia.manager.Currency;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

public class Shop implements TabExecutor, Listener
{
	public Shop()
	{
		Bukkit.getPluginManager().registerEvents(this, Main.getPlugin());
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
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
		
		String commandLine = "shop ";
		
		for(int i = 0; i < args.length; i++)
		{
			commandLine += args[i] + (i + 1 < args.length ? " " : "");
		}
		
		if(args.length == 0)
		{
			new ItemSelector(cp);
		}
		else if(args.length == 2)
		{
			new RaritySelector(cp, commandLine);
		}
		else if(args.length == 3)
		{
			new EnchantSelector(cp, commandLine);
		}
		else if(args.length == 4)
		{
			new CurrencySelector(cp, commandLine);
		}
		else if(args.length == 5 || args.length == 6)
		{
			try
			{
				Currency currency = Currency.valueOf(args[4]);
				
				if(currency == Currency.SHARDS)
				{
					TextComponent[] extra = new TextComponent[1];
					
					extra[0] = new TextComponent("(" + bundle.getString("shards") + ")");
					extra[0].setColor(ChatColor.YELLOW);
					
					String currencyName = ChatColor.YELLOW + "(" + bundle.getString("shards") + ")";
					String key = args.length == 5 ? "inv.price-selector.sell-price-type" : "inv.price-selector.buy-price-type"; 
					new PriceSelector(cp, key, commandLine, currencyName, extra);
				}
				else if(currency == Currency.$BTA)
				{
					TextComponent[] extra = new TextComponent[1];
					
					extra[0] = new TextComponent("($BTA)");
					extra[0].setColor(ChatColor.LIGHT_PURPLE);
					
					String currencyName = ChatColor.LIGHT_PURPLE + "($BTA)";
					String key = args.length == 5 ? "inv.price-selector.sell-price-type" : "inv.price-selector.buy-price-type";
					new PriceSelector(cp, key, commandLine, currencyName, extra);
				}
			}
			catch(IllegalArgumentException ex)
			{
				p.sendMessage("Currency not found.");
			}
		}
		else if(args.length == 7)
		{
			Material type;
			
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
			}
			catch(IllegalArgumentException e)
			{
				p.sendMessage("Rarity not found.");
				return true;
			}
			
			short data;
			
			try
			{
				data = Short.valueOf(args[1]);
			}
			catch(NumberFormatException e)
			{
				p.sendMessage("Invalid number format");
				return true;
			}
			
			ItemTag rarity;
			
			try
			{
				rarity = ItemTag.valueOf(args[2].toUpperCase());
			}
			catch(IllegalArgumentException e)
			{
				p.sendMessage("Rarity not found.");
				return true;
			}
			
			int enchantId;
			
			try
			{
				enchantId = Integer.valueOf(args[3]);
			}
			catch(NumberFormatException e)
			{
				p.sendMessage("Invalid number format");
				return true;
			}
			
			Currency currency;
			
			try
			{
				currency = Currency.valueOf(args[4].toUpperCase());
			}
			catch(IllegalArgumentException e)
			{
				p.sendMessage("Currency not found");
				return true;
			}
			
			int sell;
			
			try
			{
				sell = Integer.valueOf(args[5]);
			}
			catch(NumberFormatException e)
			{
				p.sendMessage("Invalid number format");
				return true;
			}
			
			int buy;
			
			try
			{
				buy = Integer.valueOf(args[6]);
			}
			catch(NumberFormatException e)
			{
				p.sendMessage("Invalid number format");
				return true;
			}
			
			ItemStack sign = new ItemStack(Material.SIGN);
			ItemMeta meta = sign.getItemMeta();
			
			String line1 = "" + type.getId();
			
			if(data != 0)
			{
				line1 += ":" + data;
			}
			
			if(enchantId != 0)
			{
				line1 += "#" + data;
			}
			
			meta.setDisplayName(rarity.getColor() + line1);
			meta.setLore(Arrays.asList
			(
					ChatColor.WHITE + String.valueOf(type.getMaxStackSize()),
					currency.getColor() + currency.name(),
					"" + ChatColor.DARK_GREEN + sell + " " + ChatColor.DARK_RED + buy
			));
			
			sign.setItemMeta(meta);
			
			p.getInventory().addItem(sign);
		}
		return false;
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void asad(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		Block clickedBlock = e.getClickedBlock();
		ItemStack itemInHand = e.getItem();
		
		if(clickedBlock == null || itemInHand == null)
		{
			return;
		}
		
		Block relative = clickedBlock.getRelative(e.getBlockFace());
		
		if(clickedBlock.getType() != Material.CHEST || relative.getType() != Material.AIR)
		{
			return;
		}
		
		BlockFace face = e.getBlockFace();
		
		if(e.getAction() != Action.RIGHT_CLICK_BLOCK || 
				!(face == BlockFace.EAST || face == BlockFace.WEST || face == BlockFace.NORTH || face == BlockFace.SOUTH))
		{
			return;
		}
		
		String[] line = new String[] {"", "", "", ""};
		if(itemInHand.getType() == Material.SIGN)
		{
			if(itemInHand.hasItemMeta())
			{
				if(itemInHand.getItemMeta().hasDisplayName())
				{
					line[0] = itemInHand.getItemMeta().getDisplayName();
					e.setCancelled(true);
				}
				
				if(itemInHand.getItemMeta().hasLore())
				{
					List<String> lore = itemInHand.getItemMeta().getLore();
					
					for(int i = 0; i + 1 < line.length && i < lore.size(); i++)
					{
						line[i + 1] = lore.get(i).replace(ChatColor.WHITE.toString(), ChatColor.BLACK.toString());
					}
					
					e.setCancelled(true);
				}
			}
		}
		
		if(e.isCancelled())
		{
			relative.setType(Material.WALL_SIGN);
			
			if(relative.getState() instanceof Sign)
			{
				Sign sign = (Sign) relative.getState();
				
				for(int i = 0; i < line.length; i++)
				{
					sign.setLine(i, line[i]);
				}
				
				org.bukkit.material.Sign signData = (org.bukkit.material.Sign) sign.getData();
				signData.setFacingDirection(face);
				sign.setRawData(signData.getData());
				sign.update();
				
				Util.writeSign(relative, p.getUniqueId());
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void asada(PlayerInteractEvent e)
	{
		Block b = e.getClickedBlock();
		
		if(b == null || b.getType() != Material.WALL_SIGN)
		{
			return;
		}
		
		Sign sign = (Sign) b.getState();
		ItemTag rarity = null;
		
		for(ItemTag rarities : ItemTag.values())
		{
			if(sign.getLine(0).startsWith(rarities.getColor()))
			{
				rarity = rarities;
				break;
			}
		}
		
		if(rarity == null)
		{
			return;
		}
		
		String iteminfo = sign.getLine(0).substring(rarity.getColor().length());
		
		ItemStack item;
		int amount;
		Currency currency;
		int buy, sell;
		
		try
		{
			item = ItemInfo.getItemInfo(iteminfo);
			
			String line1 = sign.getLine(1);
			String line2 = sign.getLine(2);
			String line3 = sign.getLine(3);
			
			for(ChatColor color : ChatColor.values())
			{			
				line1 = line1.replace(color.toString(), "");
				line2 = line2.replace(color.toString(), "");
				line3 = line3.replace(color.toString(), "");
			}
			
			amount = Integer.valueOf(sign.getLine(1));
			currency = Currency.valueOf(line2.toUpperCase());
			
			String[] split = line3.split(" ");
			
			if(split.length != 2)
			{
				throw new Exception();
			}
			
			sell = Integer.valueOf(split[0]);
			buy = Integer.valueOf(split[1]);
		}
		catch(Exception ex)
		{
			Bukkit.broadcastMessage("illegal");
			return;
		}
		
		UUID owner = Util.readSign(b);
		Player p = e.getPlayer();
		
		if(owner == null || p.getUniqueId().equals(owner))
		{
			return;
		}
		
		if(!(b.getState().getData() instanceof org.bukkit.material.Sign))
		{
			return;
		}
		
		org.bukkit.material.Sign dataSign = (org.bukkit.material.Sign) b.getState().getData();
		
		Block chest = b.getRelative(dataSign.getFacing().getOppositeFace());
		
		if(!(chest.getState() instanceof Chest))
		{
			return;
		}
		
		Chest c = (Chest) chest.getState();
		
		ItemStack[] chestContents = new ItemStack[c.getInventory().getSize()];
		
		for(int i = 0; i < c.getInventory().getSize(); i++)
		{
			ItemStack itemStack = c.getInventory().getItem(i);
			
			if(itemStack == null)
			{
				continue;
			}
			
			chestContents[i] = itemStack.clone();
		}
		
		ItemStack[] playerContents = new ItemStack[p.getInventory().getSize()];
		
		for(int i = 0; i < p.getInventory().getSize(); i++)
		{
			ItemStack itemStack = p.getInventory().getItem(i);
			
			if(itemStack == null)
			{
				continue;
			}
			
			playerContents[i] = itemStack.clone();
		}
		
		item.setAmount(amount);
		Util.setRarity(item, rarity);
		Action action = e.getAction();
		
		Inventory removeInv, addInv;
		
		int value;
		
		if(action == Action.LEFT_CLICK_BLOCK)
		{
			removeInv = p.getInventory();
			addInv = c.getInventory();
			value = sell;
		}
		else if(action == Action.RIGHT_CLICK_BLOCK)
		{
			removeInv = c.getInventory();
			addInv = p.getInventory();
			value = -buy;
		}
		else
		{
			return;
		}
			
		int amountSold = amount;
		
		for(int i = 0; i < removeInv.getSize(); i++)
		{
			if(amountSold <= 0)
			{
				break;
			}
			
			ItemStack itemStack = removeInv.getItem(i);
			
			if(itemStack == null)
			{
				continue;
			}
			
			if(itemStack.isSimilar(item))
			{
				int oldAmount = itemStack.getAmount();
				itemStack.setAmount(Math.max(0, oldAmount - amountSold));
				
				removeInv.setItem(i, itemStack);
				
				int extraAmount = oldAmount - itemStack.getAmount();
				amountSold -= extraAmount;
			}
		}
		
		if(amountSold > 0)
		{
			c.getInventory().setContents(chestContents);
			p.getInventory().setContents(playerContents);
			p.updateInventory();
			return;
		}
		
		amountSold = amount;
		
		for(int i = 0; i < addInv.getSize(); i++)
		{
			if(amountSold <= 0)
			{
				break;
			}
			
			ItemStack itemStack = addInv.getItem(i);
			
			if(itemStack == null)
			{
				itemStack = item;
				itemStack.setAmount(0);
			}
			
			if(itemStack.isSimilar(item))
			{
				int oldAmount = itemStack.getAmount();
				itemStack.setAmount(Math.min(itemStack.getMaxStackSize(), oldAmount + amountSold));
				addInv.setItem(i, itemStack);
				
				int extraAmount = itemStack.getAmount() - oldAmount;
				amountSold -= extraAmount;
			}
		}
		
		if(amountSold > 0)
		{
			c.getInventory().setContents(chestContents);
			p.getInventory().setContents(playerContents);
			p.updateInventory();
			return;
		}
		
		File ownerFile = CommonsConfig.getFile(Type.PLAYER, true, owner); 
		YamlConfiguration ownerConfig = YamlConfiguration.loadConfiguration(ownerFile);
		
		File playerFile = CommonsConfig.getFile(Type.PLAYER, true, p.getUniqueId()); 
		YamlConfiguration playerConfig = YamlConfiguration.loadConfiguration(playerFile);
		
		double playerBalance = playerConfig.getDouble(currency.getYamlKey());
		double ownerBalance = ownerConfig.getDouble(currency.getYamlKey());
		
		playerBalance += value;
		ownerBalance -= value;
		
		if(playerBalance <= 0 || ownerBalance <= 0)
		{
			c.getInventory().setContents(chestContents);
			p.getInventory().setContents(playerContents);
			p.updateInventory();
			return;
		}
		
		playerConfig.set(currency.getYamlKey(), playerBalance);
		ownerConfig.set(currency.getYamlKey(), ownerBalance);
		
		CommonPlayer cp = CraftCommonPlayer.get(p);
		ResourceBundle bundle = ResourceBundle.getBundle("message", cp.getLocale());
		
		try
		{
			playerConfig.save(playerFile);
			ownerConfig.save(ownerFile);
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
			p.sendMessage(ChatColor.RED + bundle.getString("commons.unexpected-error"));
		}
	}
}