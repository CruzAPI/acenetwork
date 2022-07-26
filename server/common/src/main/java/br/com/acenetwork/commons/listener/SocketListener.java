package br.com.acenetwork.commons.listener;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import br.com.acenetwork.commons.CommonsUtil;
import br.com.acenetwork.commons.event.SocketEvent;
import br.com.acenetwork.commons.executor.Stop;
import br.com.acenetwork.commons.inventory.VipChestGUI;
import br.com.acenetwork.commons.manager.CommonPlayerData;
import br.com.acenetwork.commons.manager.CommonsConfig;
import br.com.acenetwork.commons.manager.Message;
import br.com.acenetwork.commons.manager.CommonsConfig.Type;
import br.com.acenetwork.commons.player.CommonPlayer;
import br.com.acenetwork.commons.player.craft.CraftCommonPlayer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

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
			Player tempP;
			
			try
			{
				tempP = Bukkit.getPlayer(UUID.fromString(args[2]));
			}
			catch(IllegalArgumentException ex)
			{
				tempP = CommonsUtil.getOfflinePlayerIfCached(args[2]).getPlayer();
			}
			
			Player p = tempP;
			
			CommonPlayer cp = CraftCommonPlayer.get(p);
			
			if(cp == null)
			{
				return;
			}
			
			final int taskId = Integer.valueOf(args[1]);
						
			final int amount = Math.max(0, Integer.valueOf(args[3]));
			
			ResourceBundle bundle = ResourceBundle.getBundle("message", cp.getLocale());
			
			final boolean isQueued = Bukkit.getScheduler().isQueued(taskId);
			Bukkit.getScheduler().cancelTask(taskId);
			
			if(!isQueued)
			{
				return;
			}
			
			new Thread(() ->
			{
				try
				{
					CommonPlayerData pdMemory = CommonPlayerData.load(p.getUniqueId());
					CommonPlayerData pdDisk = new CommonPlayerData(pdMemory);
					pdDisk.setVip(pdDisk.getVip() + amount);
					
					Map<UUID, CommonPlayerData> map = new HashMap<>();
					map.put(p.getUniqueId(), pdDisk);
					CommonPlayerData.save(map);
					
					pdMemory.setVip(pdMemory.getVip() + amount);
				}
				catch(RuntimeException ex)
				{
					if(isQueued && bundle != null)
					{
						p.sendMessage(ChatColor.RED + bundle.getString("commons.unexpected-error"));
					}
					
					throw ex;
				}
			}).start();
		}
	}
}