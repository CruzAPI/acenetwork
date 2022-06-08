package br.com.acenetwork.commons.executor;

import java.util.List;
import java.util.ResourceBundle;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import br.com.acenetwork.commons.manager.Message;
import br.com.acenetwork.commons.player.craft.CraftCommonPlayer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;


public class Test implements TabExecutor
{
	@Override
	public List<String> onTabComplete(CommandSender arg0, Command arg1, String arg2, String[] arg3)
	{
		return null;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args)
	{
		Player p = (Player) sender;
		
		ResourceBundle bundle = ResourceBundle.getBundle("message", CraftCommonPlayer.get(p).getLocale());
		
		TextComponent extra3 = new TextComponent("✕");
		TextComponent hover = new TextComponent("Click to mute");
		
		extra3.setColor(ChatColor.RED);
		extra3.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[] {hover}));
		extra3.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/me test"));
		
		TextComponent extra1 = new TextComponent("➟ ");
		extra1.setColor(ChatColor.GRAY);
		
		TextComponent extra2 = new TextComponent(" ");
		extra2.setColor(ChatColor.DARK_GRAY);
		extra2.addExtra("[");
		extra2.addExtra(extra3);
		extra2.addExtra("]");
		
		TextComponent text = new TextComponent();
		text.setColor(ChatColor.YELLOW);
		
		TextComponent[] array = new TextComponent[1];
		
		hover = new TextComponent(bundle.getString("raid.cmd.pricechart.click-to-see"));
		hover.setColor(ChatColor.GOLD);
		
		array[0] = new TextComponent("/pricechart");
		array[0].setColor(ChatColor.GOLD);
		array[0].setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/pricechart"));
		array[0].setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[] {hover}));
		
//		text.addExtra(extra1);
//		text.addExtra("Teste teste teste");
//		text.addExtra(extra2);
		
		extra1.addExtra(text);
		extra1.addExtra(extra2);
		p.spigot().sendMessage(Message.getTextComponent(bundle.getString("raid.broadcast.7"), array));
		return false;
	}
}