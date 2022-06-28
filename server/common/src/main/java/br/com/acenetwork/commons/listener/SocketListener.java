package br.com.acenetwork.commons.listener;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

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
			
			final int taskId = Integer.valueOf(args[1]);
			final boolean isQueued = Bukkit.getScheduler().isQueued(taskId);
			
			Bukkit.getScheduler().cancelTask(taskId);
			
			final int amount = Math.max(0, Integer.valueOf(args[3]));
			
			ResourceBundle bundle = ResourceBundle.getBundle("message", cp.getLocale());
			
			File file = CommonsConfig.getFile(Type.CHEST_VIP, true, p.getUniqueId());
			YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
			
			Inventory inv = Bukkit.createInventory(p, 3 * 9, "VIP Chest");
			
			if(isQueued)
			{
				new VipChestGUI(cp, inv);
			}
			
			try
			{
				List<ItemStack> list = config.contains("inventory") ? (List<ItemStack>) config.getList("inventory") : new ArrayList<>();
				int vip = config.getInt("vip") + amount;
				
				for(int i = 0; i < list.size() && i < inv.getSize(); i++)
				{
					inv.setItem(i, list.get(i));
				}
				
				int firstEmpty;
				
				while((firstEmpty = inv.firstEmpty()) != -1 && vip > 0)
				{
					inv.setItem(firstEmpty, VipChestGUI.getVipItem());
					vip--;
				}
				
				cp.setVipChest(inv);
				
				config.set("inventory", inv.getContents());
				config.set("vip", vip);
				config.save(file);
			}
			catch(IOException ex)
			{
				ex.printStackTrace();
				
				if(isQueued)
				{
					p.sendMessage(ChatColor.RED + bundle.getString("commons.unexpected-error"));
				}
				return;
			}
		}
	}
}