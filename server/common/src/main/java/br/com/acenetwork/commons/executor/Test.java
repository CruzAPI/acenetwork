package br.com.acenetwork.commons.executor;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.Repairable;

import br.com.acenetwork.commons.Common;
import br.com.acenetwork.commons.manager.CommonsConfig;
import br.com.acenetwork.commons.manager.CommonsConfig.Type;
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
		
		
		
		File file = CommonsConfig.getFile(Type.CHEST_VIP, true, p.getUniqueId());
		
		if(args.length == 1)
		{
			try
			{
				int vip = Integer.valueOf(args[0]);
				
				Bukkit.broadcastMessage(file.exists() + " exists");
				Bukkit.broadcastMessage(file.length() + " length");
				
				try(RandomAccessFile access = new RandomAccessFile(file, "rw"))
				{
					long pos;
					
					while((pos = access.getFilePointer()) < access.length() && vip > 0)
					{
						byte b = (byte) Math.max(0, access.readByte());
						byte amountAvailable = (byte) (64 - b);
						
						Bukkit.broadcastMessage("byte " + pos + " " + b);
						
						if(amountAvailable <= 0)
						{
							continue;
						}
						
						if(vip > amountAvailable)
						{
							vip -= amountAvailable;
							access.seek(pos);
							access.write((byte) 64);
							Bukkit.broadcastMessage("newByte " + pos + " 64");
						}
						else
						{
							b += vip;
							vip = 0;
							access.seek(pos);
							access.write(b);
							Bukkit.broadcastMessage("newByte " + pos + " " + b);
						}
					}
				}
				catch(IOException ex)
				{
					throw ex;
				}
				
				Bukkit.broadcastMessage("exitCode vip " + vip);
			}
			catch(Exception ex)
			{
				Bukkit.broadcastMessage("exitCode -1 !!");
			}
		}
		else
		{
			try(RandomAccessFile access = new RandomAccessFile(file, "r"))
			{
				List<Byte> list = new ArrayList<>();
				
				while(access.getFilePointer() < access.length())
				{
					list.add(access.readByte());
				}
				
				Bukkit.broadcastMessage(ChatColor.RED + list.toString());
			}
			catch(IOException e)
			{
				Bukkit.broadcastMessage("ERRO");
				e.printStackTrace();
			}
		}
		
//		ItemStack item = p.getItemInHand();
//		
//		
//		ItemMeta meta = item.getItemMeta();
//		
//		if(meta instanceof Repairable)
//		{
//			p.sendMessage("repairCost = " + ((Repairable) meta).getRepairCost());
//		}
//		
//		p.sendMessage((item.getItemMeta() instanceof Repairable) + "");
//		Bukkit.broadcastMessage(p.getName() + " " + p.getUniqueId() + " v" + p.getUniqueId().version());
		
//		if(args.length == 1)
//		{
//			p.getWorld().setDifficulty(Difficulty.getByValue(Integer.valueOf(args[0])));
//		}
//		
//			WorldCreator wc = new WorldCreator(args[0]);
//			
////			wc.generator(Common.getPlugin().new VoidGenerator());
//			wc.createWorld();
////			wc.generator("2;0;1;");
//		}
//		else
//		{
//			World w = Bukkit.getWorld("farmkkk");
//			
//			if(w == null)
//			{
//				Bukkit.broadcastMessage("w == null");
//				return true;
//			}
//			
//			p.teleport(new Location(w, 0.5D, 66.0D, 0.5D, 0.0F, 0.0F));
//		}
//		Player p = (Player) sender;
//		
//		ResourceBundle bundle = ResourceBundle.getBundle("message", CraftCommonPlayer.get(p).getLocale());
//		
//		TextComponent extra3 = new TextComponent("✕");
//		TextComponent hover = new TextComponent("Click to mute");
//		
//		extra3.setColor(ChatColor.RED);
//		extra3.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[] {hover}));
//		extra3.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/me test"));
//		
//		TextComponent extra1 = new TextComponent("➟ ");
//		extra1.setColor(ChatColor.GRAY);
//		
//		TextComponent extra2 = new TextComponent(" ");
//		extra2.setColor(ChatColor.DARK_GRAY);
//		extra2.addExtra("[");
//		extra2.addExtra(extra3);
//		extra2.addExtra("]");
//		
//		TextComponent text = new TextComponent();
//		text.setColor(ChatColor.YELLOW);
//		
//		TextComponent[] array = new TextComponent[1];
//		
//		hover = new TextComponent(bundle.getString("raid.cmd.pricechart.click-to-see"));
//		hover.setColor(ChatColor.GOLD);
//		
//		array[0] = new TextComponent("/pricechart");
//		array[0].setColor(ChatColor.GOLD);
//		array[0].setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/pricechart"));
//		array[0].setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[] {hover}));
//		
////		text.addExtra(extra1);
////		text.addExtra("Teste teste teste");
////		text.addExtra(extra2);
//		
//		extra1.addExtra(text);
//		extra1.addExtra(extra2);
//		p.spigot().sendMessage(Message.getTextComponent(bundle.getString("raid.broadcast.7"), array));
		return false;
	}
}