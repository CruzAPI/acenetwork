package br.com.acenetwork.survival.executor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.Repairable;

import br.com.acenetwork.commons.CommonsUtil;
import br.com.acenetwork.commons.constants.Currency;
import br.com.acenetwork.commons.manager.Message;
import br.com.acenetwork.commons.manager.CommonPlayerData;
import br.com.acenetwork.commons.player.CommonPlayer;
import br.com.acenetwork.commons.player.craft.CraftCommonPlayer;
import br.com.acenetwork.survival.Main;
import br.com.acenetwork.survival.Property;
import br.com.acenetwork.survival.Rarity;
import br.com.acenetwork.survival.Util;
import br.com.acenetwork.survival.inventory.CurrencySelector;
import br.com.acenetwork.survival.inventory.EnchantSelector;
import br.com.acenetwork.survival.inventory.ItemSelector;
import br.com.acenetwork.survival.inventory.PriceSelector;
import br.com.acenetwork.survival.inventory.RaritySelector;
import br.com.acenetwork.survival.manager.BlockData;
import br.com.acenetwork.survival.manager.InvalidCommandArgumentException;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

public class Shop implements TabExecutor, Listener
{
	public Shop()
	{
		Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args)
	{
		return new ArrayList<>();
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
		
		if(!cp.hasPermission("cmd.shop"))
		{
			p.sendMessage(ChatColor.RED + bundle.getString("commons.cmds.permission"));
			return true;
		}
		
		String commandLine = "shop ";
		
		for(int i = 0; i < args.length; i++)
		{
			commandLine += args[i] + (i + 1 < args.length ? " " : "");
		}
		
		try
		{
			if(args.length == 0)
			{
				new ItemSelector(cp);
			}
			else if(args.length == 2)
			{
				new PriceSelector(cp, "inv.amount-selector", commandLine, bundle.getString("commons.words.amount"));
			}
			else if(args.length == 3)
			{
				new RaritySelector(cp, commandLine);
			}
			else if(args.length == 4)
			{
				new EnchantSelector(cp, commandLine);
			}
			else if(args.length == 5)
			{
				new CurrencySelector(cp, commandLine);
			}
			else if(args.length == 6 || args.length == 7)
			{
				try
				{
					Currency currency = Currency.valueOf(args[5]);
					
					if(currency == Currency.SHARDS)
					{
						TextComponent[] extra = new TextComponent[1];
						
						extra[0] = new TextComponent("(" + bundle.getString("shards") + ")");
						extra[0].setColor(ChatColor.GOLD);
						
						String currencyName = ChatColor.YELLOW + "(" + bundle.getString("shards") + ")";
						String key = args.length == 7 ? "inv.price-selector.sell-price-type" : "inv.price-selector.buy-price-type"; 
						new PriceSelector(cp, key, commandLine, currencyName, extra);
					}
					else if(currency == Currency.$BTA)
					{
						TextComponent[] extra = new TextComponent[1];
						
						extra[0] = new TextComponent("($BTA)");
						extra[0].setColor(ChatColor.DARK_PURPLE);
						
						String currencyName = ChatColor.LIGHT_PURPLE + "($BTA)";
						String key = args.length == 7 ? "inv.price-selector.sell-price-type" : "inv.price-selector.buy-price-type";
						new PriceSelector(cp, key, commandLine, currencyName, extra);
					}
				}
				catch(IllegalArgumentException ex)
				{
					TextComponent text = new TextComponent(bundle.getString("commons.cmds.currency-not-found"));
					text.setColor(ChatColor.RED);
					throw new InvalidCommandArgumentException(5, text);
				}
			}
			else if(args.length == 8)
			{
				Material type;
				String special = null;
				
				try
				{
					if(args[0].equalsIgnoreCase("vip") || args[0].equalsIgnoreCase("random_item"))
					{
						type = Material.SKULL_ITEM;
						special = args[0];
					}
					else
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
				}
				catch(IllegalArgumentException e)
				{
					TextComponent text = new TextComponent(bundle.getString("commons.cmds.id-not-found"));
					text.setColor(ChatColor.RED);
					throw new InvalidCommandArgumentException(0, text);
				}
				
				short data;
				
				try
				{
					data = Short.valueOf(args[1]);
				}
				catch(NumberFormatException e)
				{
					TextComponent text = new TextComponent(bundle.getString("commons.cmds.invalid-number-format"));
					text.setColor(ChatColor.RED);
					throw new InvalidCommandArgumentException(1, text);
				}
				
				int amount;
				
				try
				{
					amount = Integer.valueOf(args[2]);
				}
				catch(NumberFormatException e)
				{
					TextComponent text = new TextComponent(bundle.getString("commons.cmds.invalid-number-format"));
					text.setColor(ChatColor.RED);
					throw new InvalidCommandArgumentException(2, text);
				}
				
				Rarity rarity;
				
				try
				{
					rarity = Rarity.valueOf(args[3].toUpperCase());
				}
				catch(IllegalArgumentException e)
				{
					TextComponent text = new TextComponent(bundle.getString("commons.cmds.rarity-not-found"));
					text.setColor(ChatColor.RED);
					throw new InvalidCommandArgumentException(3, text);
				}
				
				int enchantId;
				
				try
				{
					enchantId = Integer.valueOf(args[4]);
				}
				catch(NumberFormatException e)
				{
					TextComponent text = new TextComponent(bundle.getString("commons.cmds.invalid-number-format"));
					text.setColor(ChatColor.RED);
					throw new InvalidCommandArgumentException(4, text);
				}
				
				Currency currency;
				
				try
				{
					currency = Currency.valueOf(args[5].toUpperCase());
				}
				catch(IllegalArgumentException e)
				{
					TextComponent text = new TextComponent(bundle.getString("commons.cmds.currency-not-found"));
					text.setColor(ChatColor.RED);
					throw new InvalidCommandArgumentException(5, text);
				}
				
				int sell;
				
				try
				{
					sell = Integer.valueOf(args[6]);
				}
				catch(NumberFormatException e)
				{
					TextComponent text = new TextComponent(bundle.getString("commons.cmds.invalid-number-format"));
					text.setColor(ChatColor.RED);
					throw new InvalidCommandArgumentException(6, text);
				}
				
				int buy;
				
				try
				{
					buy = Integer.valueOf(args[7]);
				}
				catch(NumberFormatException e)
				{
					TextComponent text = new TextComponent(bundle.getString("commons.cmds.invalid-number-format"));
					text.setColor(ChatColor.RED);
					throw new InvalidCommandArgumentException(7, text);
				}
				
				ItemStack sign = new ItemStack(Material.SIGN);
				ItemMeta meta = sign.getItemMeta();
				
				String line1;
				
				if(special != null)
				{
					line1 = special.toUpperCase().replace('_', ' ');
				}
				else
				{
					line1 = "" + type.getId();
					
					if(data != 0)
					{
						line1 += ":" + data;
					}
					
					if(enchantId != 0)
					{
						line1 += "#" + enchantId;
					}
				}
				
				meta.setDisplayName(rarity.getColor() + line1);
				meta.setLore(Arrays.asList
				(
						ChatColor.WHITE + String.valueOf(amount),
						currency.getColor() + currency.name(),
						"" + ChatColor.DARK_GREEN + sell + " " + ChatColor.DARK_RED + buy
				));
				
				sign.setItemMeta(meta);
				
				p.getInventory().addItem(sign);
			}
		}
		catch(InvalidCommandArgumentException e)
		{
			p.closeInventory();
			
			TextComponent[] array = new TextComponent[1];
			array[0] = new TextComponent("/" + label);
			array[0].setColor(ChatColor.GRAY);
			
			for(int i = 0; i < args.length; i++)
			{
				array[0].addExtra(" ");
				
				if(i == e.getArgPos())
				{
					TextComponent extra1 = new TextComponent(args[i]);
					extra1.setColor(ChatColor.RED);
					extra1.setUnderlined(true);
					
					array[0].addExtra(extra1);
				}
				else
				{
					array[0].addExtra(args[i]);
				}
			}
			
			p.sendMessage(ChatColor.RED + bundle.getString("cmds.invalid-argument-here"));
			p.spigot().sendMessage(array[0]);
			p.sendMessage("");
			p.spigot().sendMessage(e.getText());
		}
	
		return false;
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void asad(PlayerInteractEvent e)
	{
		Block clickedBlock = e.getClickedBlock();
		ItemStack itemInHand = e.getItem();
		
		if(clickedBlock == null || itemInHand == null)
		{
			return;
		}
		
		Block relative = clickedBlock.getRelative(e.getBlockFace());
		
		if(clickedBlock.getType() != Material.CHEST || relative.getType() != Material.AIR || !e.getPlayer().isSneaking())
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
			BlockState state = relative.getState();
			BlockPlaceEvent blockPlaceEvent = new BlockPlaceEvent(relative, state, clickedBlock, itemInHand, e.getPlayer(), true);
			relative.setType(Material.WALL_SIGN);
			Bukkit.getPluginManager().callEvent(blockPlaceEvent);
			
			if(blockPlaceEvent.isCancelled())
			{
				relative.setTypeIdAndData(state.getTypeId(), state.getRawData(), false);
				return;
			}
			
			if(e.getPlayer().getGameMode() != GameMode.CREATIVE)
			{
				itemInHand.setAmount(itemInHand.getAmount() - 1);
				
				if(itemInHand.getAmount() <= 0)
				{
					e.getPlayer().setItemInHand(null);
				}
			}
			
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
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void asada(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		CommonPlayer cp = CraftCommonPlayer.get(p);
		ResourceBundle bundle = ResourceBundle.getBundle("message", cp.getLocale());
		
		try
		{
			Block b = e.getClickedBlock();
			
			if(b == null || b.getType() != Material.WALL_SIGN)
			{
				return;
			}
			
			Sign sign = (Sign) b.getState();
			Rarity rarity = null;
			
			for(Rarity rarities : Rarity.values())
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
			
			ResourceBundle minecraftBundle = ResourceBundle.getBundle("minecraft", cp.getLocale());
			
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
				return;
			}
			
			BlockData data = Util.readBlock(b);
			UUID owner;
			
			if(data == null || (owner = data.getPlayer()) == null)
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
			Util.setItemTag(item, rarity);
			Util.setItemTag(item, Property.SOLD);
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
			
			if(value == 0 || amount == 0)
			{
				TextComponent[] extra = new TextComponent[1];
				extra[0] = new TextComponent("[" + bundle.getString("chest-shop.ace-shop") + "]");
				extra[0].setColor(ChatColor.AQUA);
				
				if(action == Action.LEFT_CLICK_BLOCK)
				{
					TextComponent text = Message.getTextComponent(bundle.getString("chest-shop.not-accepting-sales"), extra);
					text.setColor(ChatColor.RED);
					p.spigot().sendMessage(text);
					return;
				}
				
				TextComponent text = Message.getTextComponent(bundle.getString("chest-shop.not-accepting-purchase"), extra);
				text.setColor(ChatColor.RED);
				p.spigot().sendMessage(text);
				return;
			}
			
			if(p.getUniqueId().equals(owner))
			{
				TextComponent[] extra = new TextComponent[1];
				extra[0] = new TextComponent("[" + bundle.getString("chest-shop.ace-shop") + "]");
				extra[0].setColor(ChatColor.AQUA);
				
				if(value > 0)
				{
					TextComponent text = Message.getTextComponent(bundle.getString("chest-shop.can-not-sell-to-yourself"), extra);
					text.setColor(ChatColor.RED);
					p.spigot().sendMessage(text);
					return;
				}
				
				TextComponent text = Message.getTextComponent(bundle.getString("chest-shop.can-not-buy-from-yourself"), extra);
				text.setColor(ChatColor.RED);
				p.spigot().sendMessage(text);
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
				
				ItemStack cloneItemStack = itemStack.clone();
				boolean isSold = Util.getProperties(cloneItemStack).contains(Property.SOLD);
				Util.setItemTag(cloneItemStack, Property.SOLD);
				
				if(Util.isShoppable(cloneItemStack, item, isSold))
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
				
				TextComponent[] extra = new TextComponent[1];
				extra[0] = new TextComponent("[" + bundle.getString("chest-shop.ace-shop") + "]");
				extra[0].setColor(ChatColor.AQUA);
				
				if(value > 0)
				{
					TextComponent text = Message.getTextComponent(bundle.getString("chest-shop.you-do-not-have-enought-item"), extra);
					text.setColor(ChatColor.RED);
					p.spigot().sendMessage(text);
					return;
				}
				
				TextComponent text = Message.getTextComponent(bundle.getString("chest-shop.chest-empty"), extra);
				text.setColor(ChatColor.RED);
				p.spigot().sendMessage(text);
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
				
				boolean isSold = Util.getProperties(itemStack).contains(Property.SOLD);
				
				if(Util.isShoppable(itemStack, item, isSold))
				{
					int oldAmount = itemStack.getAmount();
					itemStack.setAmount(Math.min(itemStack.getMaxStackSize(), oldAmount + amountSold));
					
					ItemMeta meta = itemStack.getItemMeta();
					
					if(meta instanceof Repairable)
					{
						Repairable repairable = (Repairable) meta;
						repairable.setRepairCost(0);
						itemStack.setItemMeta(meta);
					}
					
					Util.setItemTag(itemStack, Property.SOLD);
					
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
				
				TextComponent[] extra = new TextComponent[1];
				extra[0] = new TextComponent("[" + bundle.getString("chest-shop.ace-shop") + "]");
				extra[0].setColor(ChatColor.AQUA);
				
				if(value > 0)
				{
					TextComponent text = Message.getTextComponent(bundle.getString("chest-shop.chest-full"), extra);
					text.setColor(ChatColor.RED);
					p.spigot().sendMessage(text);
					return;
				}
				
				TextComponent text = Message.getTextComponent(bundle.getString("chest-shop.inventory-full"), extra);
				text.setColor(ChatColor.RED);
				p.spigot().sendMessage(text);
				return;
			}
			
			CommonPlayerData ownerPD = CommonPlayerData.load(owner);
			double playerBalance;
			double ownerBalance;
			
			if(currency == Currency.SHARDS)
			{
				playerBalance = cp.getBalance();
				ownerBalance = ownerPD.getBalance();
			}
			else if(currency == Currency.$BTA)
			{
				playerBalance = cp.getBTA();
				ownerBalance = ownerPD.getBTA();
			}
			else
			{
				throw new RuntimeException();
			}
			
			playerBalance += value;
			ownerBalance -= value;
			
			OfflinePlayer op = Bukkit.getOfflinePlayer(owner);

			if(playerBalance <= 0 || ownerBalance <= 0)
			{
				c.getInventory().setContents(chestContents);
				p.getInventory().setContents(playerContents);
				p.updateInventory();
				
				if(playerBalance <= 0)
				{
					TextComponent[] extra = new TextComponent[1];
					
					extra[0] = new TextComponent("[" + bundle.getString("chest-shop.ace-shop") + "]");
					extra[0].setColor(ChatColor.AQUA);
					
					TextComponent text = Message.getTextComponent(bundle.getString("chest-shop.you-cant-afford-it"), extra);
					text.setColor(ChatColor.RED);
					p.spigot().sendMessage(text);
					return;
				}
				
				TextComponent[] extra = new TextComponent[2];
				
				extra[0] = new TextComponent("[" + bundle.getString("chest-shop.ace-shop") + "]");
				extra[0].setColor(ChatColor.AQUA);
				
				extra[1] = new TextComponent(op.isOnline() ? op.getPlayer().getDisplayName() : ChatColor.GRAY + op.getName());
				
				TextComponent text = Message.getTextComponent(bundle.getString("chest-shop.other-cant-afford-it"), extra);
				text.setColor(ChatColor.RED);
				p.spigot().sendMessage(text);
				return;
			}
			
			
			if(currency == Currency.SHARDS)
			{
				cp.setBalance(playerBalance);
				ownerPD.setBalance(ownerBalance);
			}
			else if(currency == Currency.$BTA)
			{
				cp.setBTA(playerBalance);
				ownerPD.setBTA(ownerBalance);
			}
			else
			{
				throw new RuntimeException();
			}
			
			Player t = Bukkit.getPlayer(owner);
			CommonPlayer ct = CraftCommonPlayer.get(t);
			
			if(ct != null)
			{
				ResourceBundle targetBundle = ResourceBundle.getBundle("message", ct.getLocale());
				ResourceBundle targetMinecraftBundle = ResourceBundle.getBundle("minecraft", ct.getLocale());
				
				TextComponent[] extra = new TextComponent[3];
				
				extra[0] = new TextComponent("[" + targetBundle.getString("chest-shop.ace-shop") + "]");
				extra[0].setColor(ChatColor.AQUA);
				
				final TextComponent text;
				
				extra[1] = new TextComponent(p.getDisplayName());
				
				extra[2] = new TextComponent(CommonsUtil.getTranslation(item, targetMinecraftBundle) + " x" + amount);
				extra[2].setColor(ChatColor.DARK_AQUA);
				
				TextComponent text2;
				
				if(value > 0)
				{
					text2 = new TextComponent("(-" + value + " " + currency.name() + ")");
					text2.setColor(ChatColor.DARK_RED);
					
					text = Message.getTextComponent(targetBundle.getString("chest-shop.other-sold"), extra);
				}
				else
				{
					text2 = new TextComponent("(+" + Math.abs(value) + " " + currency.name() + ")");
					text2.setColor(ChatColor.DARK_GREEN);
					
					text = Message.getTextComponent(targetBundle.getString("chest-shop.other-bought"), extra);
				}
				
				text.setColor(ChatColor.AQUA);
				
				t.spigot().sendMessage(text);
				t.spigot().sendMessage(text2);
			}
			
			TextComponent[] extra = new TextComponent[3];
			
			extra[0] = new TextComponent("");
			extra[0] = new TextComponent("[" + bundle.getString("chest-shop.ace-shop") + "]");
			extra[0].setColor(ChatColor.AQUA);
			
			final TextComponent text;
			
			extra[1] = new TextComponent(CommonsUtil.getTranslation(item, minecraftBundle) + " x" + amount);
			extra[1].setColor(ChatColor.DARK_AQUA);
			
			extra[2] = new TextComponent(op.isOnline() ? op.getPlayer().getDisplayName() : ChatColor.GRAY + op.getName());
			
			TextComponent text2;
			
			if(value > 0)
			{
				text2 = new TextComponent("(+" + value + " " + currency.name() + ")");
				text2.setColor(ChatColor.DARK_GREEN);
				
				text = Message.getTextComponent(bundle.getString("chest-shop.you-sold"), extra);
			}
			else
			{
				text2 = new TextComponent("(-" + Math.abs(value) + " " + currency.name() + ")");
				text2.setColor(ChatColor.DARK_RED);
				
				text = Message.getTextComponent(bundle.getString("chest-shop.you-bought"), extra);
			}
			
			text.setColor(ChatColor.AQUA);
			p.spigot().sendMessage(text);
			p.spigot().sendMessage(text2);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			p.sendMessage(ChatColor.RED + bundle.getString("commons.unexpected-error"));
		}
	}
}