package br.com.acenetwork.commons;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Locale;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import br.com.acenetwork.commons.event.PlayerModeEvent;
import br.com.acenetwork.commons.event.SocketEvent;
import br.com.acenetwork.commons.executor.AdminCMD;
import br.com.acenetwork.commons.executor.BTA;
import br.com.acenetwork.commons.executor.Balance;
import br.com.acenetwork.commons.executor.Baltop;
import br.com.acenetwork.commons.executor.BanCMD;
import br.com.acenetwork.commons.executor.BroadcastCMD;
import br.com.acenetwork.commons.executor.Build;
import br.com.acenetwork.commons.executor.ChatClean;
import br.com.acenetwork.commons.executor.Deposit;
import br.com.acenetwork.commons.executor.Give;
import br.com.acenetwork.commons.executor.Invis;
import br.com.acenetwork.commons.executor.Invsee;
import br.com.acenetwork.commons.executor.Login;
import br.com.acenetwork.commons.executor.Mutebroadcast;
import br.com.acenetwork.commons.executor.Pardon;
import br.com.acenetwork.commons.executor.Permission;
import br.com.acenetwork.commons.executor.Ping;
import br.com.acenetwork.commons.executor.Register;
import br.com.acenetwork.commons.executor.Setbalance;
import br.com.acenetwork.commons.executor.Setip;
import br.com.acenetwork.commons.executor.Specs;
import br.com.acenetwork.commons.executor.Stop;
import br.com.acenetwork.commons.executor.TagCMD;
import br.com.acenetwork.commons.executor.Test;
import br.com.acenetwork.commons.executor.Tp;
import br.com.acenetwork.commons.executor.VipChest;
import br.com.acenetwork.commons.executor.Wallet;
import br.com.acenetwork.commons.executor.WatchCMD;
import br.com.acenetwork.commons.executor.Withdraw;
import br.com.acenetwork.commons.listener.CustomListener;
import br.com.acenetwork.commons.listener.EntitySpawn;
import br.com.acenetwork.commons.listener.InventoryClose;
import br.com.acenetwork.commons.listener.PlayerDeath;
import br.com.acenetwork.commons.listener.PlayerJoin;
import br.com.acenetwork.commons.listener.PlayerLogin;
import br.com.acenetwork.commons.listener.PlayerQuit;
import br.com.acenetwork.commons.listener.SocketListener;
import br.com.acenetwork.commons.listener.SoupListener;
import br.com.acenetwork.commons.listener.WorldSave;
import br.com.acenetwork.commons.manager.CommonPlayerData;
import br.com.acenetwork.commons.player.CommonPlayer;
import br.com.acenetwork.commons.player.craft.CraftCommonPlayer;

public class Common extends JavaPlugin implements Listener
{
	private static Common instance;
	private static boolean restarting;
	
	public void onEnable()
	{
		instance = this;
		
		Locale.setDefault(new Locale("en", "US"));
		
		Bukkit.getConsoleSender().sendMessage("§aServer is in production!");
		
		Bukkit.getConsoleSender().sendMessage("§aListening socket port on " + getSocketPort());
		
		Bukkit.getMessenger().registerIncomingPluginChannel(this, "commons:commons", new PluginMessage());
		Bukkit.getMessenger().registerOutgoingPluginChannel(this, "commons:commons");
		
		Bukkit.getPluginManager().registerEvents(this, this);
		Bukkit.getPluginManager().registerEvents(new CustomListener(), this);
		Bukkit.getPluginManager().registerEvents(new EntitySpawn(), this);
		Bukkit.getPluginManager().registerEvents(new InventoryClose(), this);
//		Bukkit.getPluginManager().registerEvents(new PlayerChat(), this);
		Bukkit.getPluginManager().registerEvents(new PlayerDeath(), this);
		Bukkit.getPluginManager().registerEvents(new PlayerJoin(), this);
		Bukkit.getPluginManager().registerEvents(new PlayerLogin(), this);
		Bukkit.getPluginManager().registerEvents(new PlayerQuit(), this);
		Bukkit.getPluginManager().registerEvents(new SocketListener(), this);
		Bukkit.getPluginManager().registerEvents(new WorldSave(), this);
		Bukkit.getPluginManager().registerEvents(new SoupListener(), this);
		
		registerCommand(new AdminCMD(), "admin");
		registerCommand(new Balance(), "balance", "bal", "points", "coins");
		registerCommand(new Setbalance(), "setbalance", "setbal");
		registerCommand(new Baltop(), "baltop");
		registerCommand(new BanCMD(), "ban");
		registerCommand(new BroadcastCMD(), "broadcast", "bc", "shout");
		registerCommand(new Build(), "build");
		registerCommand(new BTA(), "bta");
		registerCommand(new ChatClean(), "chatclean", "cc");
		registerCommand(new Deposit(), "deposit");
		registerCommand(new Give(), "give");
//		registerCommand(new ChatCMD(), "chat");
//		registerCommand(new Ignore(), "ignore");
//		registerCommand(new MuteCMD(), "mute");
//		registerCommand(new Reply(), "reply", "r");
//		registerCommand(new Tell(), "tell", "msg", "t", "w", "whisper");
//		registerCommand(new Unmute(), "unmute");
		registerCommand(new Invis(), "invis", "v", "vanish");
		registerCommand(new Invis(), "vis");
		registerCommand(new Invsee(), "invsee");
		registerCommand(new Mutebroadcast(), "mutebroadcast");
		registerCommand(new Pardon(), "pardon");
		registerCommand(new Permission(), "permission", "pex", "perm");
		registerCommand(new Ping(), "ping");
		registerCommand(new Specs(), "specs");
		registerCommand(new Setip(), "setip");
		registerCommand(new Stop(), "stop");
		registerCommand(new TagCMD(), "tag");
		registerCommand(new Test(), "test");
		registerCommand(new Tp(), "teleport", "tp");
		registerCommand(new VipChest(), "vipchest");
		registerCommand(new WatchCMD(), "watch");
		registerCommand(new Wallet(), "wallet");
		registerCommand(new Withdraw(), "withdraw");
		registerCommand(new Login(), "login");
		registerCommand(new Register(), "register");
		
		getCommand("admin").setPermission("cmd.admin");
		
		CommonPlayerData.load();
		
		for(Player all : Bukkit.getOnlinePlayers())
		{
			try
			{
				Bukkit.getPluginManager().callEvent(new PlayerModeEvent(all));
				
				CommonPlayer cp = CraftCommonPlayer.get(all);
				
				if(cp.hasPermission("cmd.admin"))
				{
					all.chat("/admin");
				}
				else if(cp.hasPermission("cmd.watch"))
				{
					all.chat("/watch");
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		
		new Thread(() ->
		{
			while(true)
			{
				try(ServerSocket ss = new ServerSocket(getSocketPort()); Socket s = ss.accept(); 
						DataInputStream in = new DataInputStream(s.getInputStream());)
				{
					String line = "";
					int read;
					
					while((read = in.read()) != -1)
					{
						line += (char) read;
					}
					
					Bukkit.getPluginManager().callEvent(new SocketEvent(line.split(" ")));
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	@Override
	public void onDisable()
	{
		
	}
	
	public static int getSocketPort()
	{
		return Bukkit.getPort() + 5000;
	}

	protected void registerCommand(TabExecutor executor, String name, String... aliases)
	{
		try
		{
			final Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
			bukkitCommandMap.setAccessible(true);
			
			CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());
			
			Class<?> clazz = PluginCommand.class;
			
			Constructor<?> constructor = clazz.getDeclaredConstructor(String.class, Plugin.class);
			constructor.setAccessible(true);
			
			PluginCommand command = (PluginCommand) constructor.newInstance(name, this);
			
			command.setAliases(Arrays.asList(aliases));
			command.register(commandMap);
			command.setExecutor(executor);
			command.setTabCompleter(executor);
			commandMap.register(command.getName(), command);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	@EventHandler
	public void a(PlayerKickEvent e)
	{
		if(e.getReason().equalsIgnoreCase("disconnect.spam"))
		{
			e.setCancelled(true);
		}
	}
	
	public static Common getInstance()
	{
		return instance;
	}
	
	public File getConfigFolder()
	{
		return new File(System.getProperty("user.home") + "/." + new File(System.getProperty("user.dir")).getParentFile().getName());
	}

	public static void setRestarting(boolean value)
	{
		restarting = value;
	}
	
	public static boolean isRestarting()
	{
		return restarting;
	}
}