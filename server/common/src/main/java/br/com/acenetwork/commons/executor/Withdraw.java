package br.com.acenetwork.commons.executor;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import br.com.acenetwork.commons.Common;
import br.com.acenetwork.commons.CommonsUtil;
import br.com.acenetwork.commons.manager.Message;
import br.com.acenetwork.commons.manager.PlayerData;
import br.com.acenetwork.commons.player.CommonPlayer;
import br.com.acenetwork.commons.player.craft.CraftCommonPlayer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class Withdraw implements TabExecutor
{
	private static final double TAX = 10.0D;
	private static final Map<UUID, Order> MAP = new HashMap<>();
	
	private class Order
	{
		private final int taskId;
		private final double amount;
		
		public Order(int taskId, double amount)
		{
			this.taskId = taskId;
			this.amount = amount;
		}
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) 
	{
		return new ArrayList<>();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String aliases, String[] args)
	{
		final ResourceBundle bundle;
		
		if(!(sender instanceof Player))
		{
			bundle = ResourceBundle.getBundle("message");
			TextComponent text = new TextComponent(bundle.getString("commons.cmds.cant-perform-command"));
			text.setColor(ChatColor.RED);
			CommonsUtil.sendMessage(sender, text);
			return true;
		}

		Player p = (Player) sender;
		CommonPlayer cp = CraftCommonPlayer.get(p);
		
		bundle = ResourceBundle.getBundle("message", cp.getLocale());
		
		final double withdrawableBTA = cp.getWithdrawableBTA();
		
		TextComponent[] extra;
		TextComponent text;
		
		DecimalFormat df = new DecimalFormat("#.##", new DecimalFormatSymbols(bundle.getLocale()));
		try
		{
			if(args.length == 0)
			{
				if(withdrawableBTA <= 0.0D)
				{
					extra = new TextComponent[1];
					extra[0] = new TextComponent("$BTA");
					
					text = Message.getTextComponent(bundle.getString("commons.cmd.withdraw.not-available"), extra);
					text.setColor(ChatColor.RED);
					p.spigot().sendMessage(text);
					return true;
				}
				
				extra = new TextComponent[1];
				
				extra[0] = new TextComponent(df.format(withdrawableBTA) + " $BTA");
				extra[0].setColor(ChatColor.DARK_PURPLE);
				
				text = Message.getTextComponent(bundle.getString("commons.cmd.withdraw.available"), extra);
				text.setColor(ChatColor.LIGHT_PURPLE);
				
				TextComponent text1 = text;
				
				extra = new TextComponent[1];
				
				extra[0] = new TextComponent(df.format(TAX) + " $BTA");
				extra[0].setColor(ChatColor.DARK_PURPLE);
				
				text = Message.getTextComponent(bundle.getString("commons.cmd.withdraw.tax-fee"), extra);
				text.setColor(ChatColor.LIGHT_PURPLE);
				
				TextComponent text2 = text;
				
				extra = new TextComponent[2];
				
				extra[0] = new TextComponent("$BTA");
				extra[0].setColor(ChatColor.DARK_PURPLE);
				
				extra[1] = new TextComponent("/" + aliases + " <" + bundle.getString("commons.words.amount") + ">");
				extra[1].setColor(ChatColor.GRAY);
				extra[1].setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/" + aliases + " " + (int) withdrawableBTA));
				
				text = Message.getTextComponent(bundle.getString("commons.cmd.withdraw.try"), extra);
				text.setColor(ChatColor.LIGHT_PURPLE);
				
				p.sendMessage("");
				p.spigot().sendMessage(text1);
				p.spigot().sendMessage(text2);
				p.sendMessage("");
				p.spigot().sendMessage(text);
				p.sendMessage("");
			}
			else if(args.length == 1 && args[0].equalsIgnoreCase("confirm"))
			{
				Order order = MAP.remove(p.getUniqueId());
				
				if(order == null)
				{
					p.sendMessage(ChatColor.RED + bundle.getString("commons.cmd.withdraw.order-not-found-or-expired"));
					return true;
				}
				
				if(order.amount <= 0)
				{
					throw new NumberFormatException();
				}
				
				if(order.amount > withdrawableBTA)
				{
					p.sendMessage(ChatColor.RED + bundle.getString("commons.cmds.insufficient-balance"));
					return true;
				}
				
				if(order.amount <= TAX)
				{
					extra = new TextComponent[1];
					
					extra[0] = new TextComponent(df.format(TAX) + " $BTA");
					
					text = Message.getTextComponent(bundle.getString("commons.cmd.withdraw.tax-fee"), extra);
					text.setColor(ChatColor.RED);
					
					p.sendMessage("");
					p.sendMessage(ChatColor.RED + bundle.getString("commons.cmd.withdraw.greater-than-fee"));
					p.spigot().sendMessage(text);
					p.sendMessage("");
					return true;
				}
				
				new Thread(() -> 
				{
					PlayerData cloneMemoryPD = null;
					
					try
					{
						PlayerData memoryPD = cp.getPlayerData();
						cloneMemoryPD = (PlayerData) memoryPD.clone();
						PlayerData diskPD = new PlayerData(memoryPD);
						diskPD.setBTA(diskPD.getBTA() - order.amount);
						
						memoryPD.setBTA(memoryPD.getBTA() - order.amount);
						memoryPD.setDiskBTA(diskPD.getBTA());
						
						Map<UUID, PlayerData> map = new HashMap<>();
						map.put(p.getUniqueId(), diskPD);
						PlayerData.save(map);
						
						TextComponent[] extra1;
						TextComponent text1;
						
						extra1 = new TextComponent[2];
						extra1[0] = new TextComponent(p.getName());
						extra1[1] = new TextComponent(df.format(order.amount) + " $BTA");
						
						ResourceBundle consoleBundle = ResourceBundle.getBundle("message");
						text1 = Message.getTextComponent(consoleBundle.getString("commons.cmd.withdraw.log"), extra1);
						Bukkit.getConsoleSender().sendMessage(text1.toLegacyText());
						
						try
						{
							Runtime.getRuntime().exec(String.format("node %s/reset/withdraw %s %s %s %s", System.getProperty("user.home"),
									Common.getSocketPort(), 
									cp.requestDatabase(), 
									p.getUniqueId(), 
									order.amount));
							
							df.setDecimalFormatSymbols(new DecimalFormatSymbols(bundle.getLocale()));
							
							extra1 = new TextComponent[1];
							
							extra1[0] = new TextComponent(df.format(order.amount) + " $BTA");
							extra1[0].setColor(ChatColor.DARK_PURPLE);
							
							text1 = Message.getTextComponent(bundle.getString("commons.cmd.withdraw"), extra1);
							text1.setColor(ChatColor.LIGHT_PURPLE);
							
							p.spigot().sendMessage(text1);
						}
						catch(IOException ex)
						{
							ex.printStackTrace();
							p.sendMessage(ChatColor.RED + bundle.getString("commons.unexpected-error"));
						}
					}
					catch(Exception e)
					{
						if(cloneMemoryPD != null)
						{
							cp.setPlayerData(cloneMemoryPD);
						}
						
						p.sendMessage(ChatColor.RED + bundle.getString("commons.unexpected-error"));
						throw new RuntimeException(e);
					}
				}).start();
			}
			else if(args.length == 1)
			{
				double amount = Double.valueOf(args[0].replace(',', '.'));
				
				if(amount <= 0)
				{
					throw new NumberFormatException();
				}
				
				if(amount > withdrawableBTA)
				{
					p.sendMessage(ChatColor.RED + bundle.getString("commons.cmds.insufficient-balance"));
					return true;
				}
				
				if(amount <= TAX)
				{
					extra = new TextComponent[1];
					
					extra[0] = new TextComponent(df.format(TAX) + " $BTA");
					
					text = Message.getTextComponent(bundle.getString("commons.cmd.withdraw.tax-fee"), extra);
					text.setColor(ChatColor.RED);
					
					p.sendMessage("");
					p.sendMessage(ChatColor.RED + bundle.getString("commons.cmd.withdraw.greater-than-fee"));
					p.spigot().sendMessage(text);
					p.sendMessage("");
					return true;
				}
				
				int taskId = Bukkit.getScheduler().scheduleSyncDelayedTask(Common.getPlugin(), () ->
				{
					MAP.remove(p.getUniqueId());
				}, 20L * 10L);
				
				Order order = new Order(taskId, amount);
				Order oldOrder = MAP.put(p.getUniqueId(), order);
				
				if(oldOrder != null)
				{
					Bukkit.getScheduler().cancelTask(oldOrder.taskId);
				}
				
				extra = new TextComponent[1];
				
				extra[0] = new TextComponent(df.format(amount - TAX) + " $BTA");
				extra[0].setColor(ChatColor.WHITE);
				
				TextComponent text1 = Message.getTextComponent(bundle.getString("commons.cmd.withdraw.you-will-get"), extra);
				text1.setColor(ChatColor.GRAY);
				
				
				extra[0] = new TextComponent(df.format(TAX) + " $BTA");
				extra[0].setColor(ChatColor.WHITE);
				
				TextComponent text2 = Message.getTextComponent(bundle.getString("commons.cmd.withdraw.tax-fee"), extra);
				text2.setColor(ChatColor.GRAY);
				
				
				extra[0] = new TextComponent(df.format(amount) + " $BTA");
				extra[0].setColor(ChatColor.WHITE);
				
				TextComponent text3 = Message.getTextComponent(bundle.getString("commons.cmd.withdraw.withdrawal-amount"), extra);
				text3.setColor(ChatColor.GRAY);
				
				
				extra[0] = new TextComponent("");
				extra[0].addExtra(StringUtils.capitalize(bundle.getString("commons.words.within")));
				extra[0].addExtra(" 48 ");
				extra[0].addExtra(bundle.getString("commons.words.hours"));
				extra[0].setColor(ChatColor.WHITE);
				
				TextComponent text4 = Message.getTextComponent(bundle.getString("commons.cmd.withdraw.funds-will-arrive"), extra);
				text4.setColor(ChatColor.GRAY);
				
				TextComponent text5 = new TextComponent("                       [");
				text5.setColor(ChatColor.DARK_GRAY);
				
				TextComponent confirm = new TextComponent(bundle.getString("verb.confirm").toUpperCase());
				confirm.setColor(ChatColor.DARK_GREEN);
				confirm.setBold(true);
				confirm.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" + aliases + " confirm"));
				confirm.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, 
						new ComponentBuilder(StringUtils.capitalize(bundle.getString("commons.click-to-confirm")))
						.color(ChatColor.GREEN).create()));
				
				text5.addExtra(confirm);
				text5.addExtra("]");
				
				p.sendMessage(ChatColor.GRAY + "======= " + ChatColor.WHITE + ChatColor.BOLD 
						+ bundle.getString("commons.cmd.withdraw.withdrawal-confirmation") + ChatColor.GRAY + " =======");
				p.sendMessage("");
				p.spigot().sendMessage(text1);
				p.sendMessage("");
				p.spigot().sendMessage(text2);
				p.spigot().sendMessage(text3);
				p.spigot().sendMessage(text4);
				p.sendMessage("");
				p.spigot().sendMessage(text5);
				p.sendMessage(ChatColor.GRAY + "=========================================");
			}
			else
			{
				extra = new TextComponent[1];
				
				extra[0] = new TextComponent("/" + aliases + " [" + bundle.getString("commons.words.amount") + "]");
				
				text = Message.getTextComponent(bundle.getString("commons.cmds.wrong-syntax-try"), extra);
				text.setColor(ChatColor.RED);
				CommonsUtil.sendMessage(sender, text);
			}
		}
		catch(NumberFormatException e)
		{
			p.sendMessage(ChatColor.RED + bundle.getString("commons.cmds.invalid-number-format"));
		}
		return true;
	}
}