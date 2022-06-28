package br.com.acenetwork.commons.listener;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;

import br.com.acenetwork.commons.event.SocketEvent;
import br.com.acenetwork.commons.executor.Stop;
import br.com.acenetwork.commons.inventory.VipChestGUI;
import br.com.acenetwork.commons.manager.CommonsConfig;
import br.com.acenetwork.commons.manager.CommonsConfig.Type;
import br.com.acenetwork.commons.player.CommonPlayer;
import br.com.acenetwork.commons.player.craft.CraftCommonPlayer;
import net.md_5.bungee.api.ChatColor;

public class SocketListener implements Listener
{
	@EventHandler
	public void a(SocketEvent e)
	{
		String[] args = e.getArgs();
		
		String cmd = args[0];
		
		if(cmd.equals("stop"))
		{
			Stop.stop();
		}
	}
	
	@EventHandler
	public void aasdasd(SocketEvent e)
	{
		String[] args = e.getArgs();
		
		String cmd = args[0];
		
		if(cmd.equalsIgnoreCase("vip"))
		{
			Player p = Bukkit.getPlayer(UUID.fromString(args[2]));
			CommonPlayer cp = CraftCommonPlayer.get(p);
			
			if(cp == null)
			{
				return;
			}
			
			int taskId = Integer.valueOf(args[1]);
			
			if(!Bukkit.getScheduler().isQueued(taskId))
			{
				return;
			}
			
			Bukkit.getScheduler().cancelTask(taskId);
			
			int exitCode = Integer.valueOf(args[3]);
			
			ResourceBundle bundle = ResourceBundle.getBundle("message", cp.getLocale());
			
			if(exitCode == -1)
			{
				p.sendMessage(ChatColor.RED + bundle.getString("commons.unexpected-error"));
				return;
			}
			
			File file = CommonsConfig.getFile(Type.CHEST_VIP, true, p.getUniqueId());
			
			Inventory inv = cp.getVipChest();
			inv = inv == null ? Bukkit.createInventory(p, 3 * 9, "VIP Chest") : inv;
			
			p.openInventory(inv);
			
			try(RandomAccessFile access = new RandomAccessFile(file, "r"))
			{
				for(int i = 0; i < inv.getSize() && access.getFilePointer() < access.length(); i++)
				{
					byte b = access.readByte();
					inv.setItem(i, b == 0 ? null : VipChestGUI.getVipItem(b));
				}
				
				cp.setVipChest(inv);
			}
			catch(IOException ex)
			{
				ex.printStackTrace();
				p.sendMessage(ChatColor.RED + bundle.getString("commons.unexpected-error"));
				return;
			}
		}
	}
}