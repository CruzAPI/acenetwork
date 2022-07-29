package br.com.acenetwork.commons.executor;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.acenetwork.commons.Common;
import br.com.acenetwork.commons.CommonsUtil;
import br.com.acenetwork.commons.event.PlayerSuccessLoginEvent;
import br.com.acenetwork.commons.manager.CommonPlayerData;
import br.com.acenetwork.commons.manager.Message;
import br.com.acenetwork.commons.player.CommonPlayer;
import br.com.acenetwork.commons.player.craft.CraftCommonPlayer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

public class Login implements TabExecutor, Listener
{
	private final Map<UUID, Integer> taskMap = new HashMap<>();
	private final Map<UUID, Integer> attemptMap = new HashMap<>();
	
	public Login()
	{
		Bukkit.getPluginManager().registerEvents(this, Common.getInstance());
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args)
	{
		return new ArrayList<>();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String aliases, String[] args)
	{
		ResourceBundle bundle = ResourceBundle.getBundle("message");
		
		if(!(sender instanceof Player))
		{
			TextComponent text = new TextComponent(bundle.getString("commons.cmds.cant-perform-command"));
			text.setColor(ChatColor.RED);
			CommonsUtil.sendMessage(sender, text);
			return true;
		}

		Player p = (Player) sender;
		CommonPlayer cp = CraftCommonPlayer.get(p);
		CommonPlayerData data = cp.getCommonPlayerData();
		
		bundle = ResourceBundle.getBundle("message", cp.getLocale());

		if(args.length == 1)
		{
			if(cp.isLogged())
			{
				return true;
			}
			
			if(data.getPassword() == null)
			{
				return true;
			}
			
			String password = args[0];
			
			try
			{
				MessageDigest m = MessageDigest.getInstance("MD5");
				
				if(Arrays.equals(data.getPassword(), m.digest(password.getBytes())))
				{
					cp.setLogged(true);
					p.sendMessage(ChatColor.GREEN + bundle.getString("cmd.login.successfull"));
					return true;
				}
				
				int attempt = Optional.ofNullable(attemptMap.get(p.getUniqueId())).orElse(0) + 1;
				
				if(attempt < 3)
				{
					attemptMap.put(p.getUniqueId(), attempt);
					p.sendMessage(ChatColor.RED + bundle.getString("cmd.login.incorrect-password"));
					return true;
				}
				
				p.kickPlayer("");
			}
			catch(NoSuchAlgorithmException e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			TextComponent[] extra = new TextComponent[1];
			
			String nounPassword = bundle.getString("noun.password");
			
			extra[0] = new TextComponent("/" + aliases + " <" + nounPassword + ">");
			
			TextComponent text = Message.getTextComponent(bundle.getString("commons.cmds.wrong-syntax-try"), extra);
			text.setColor(ChatColor.RED);
			CommonsUtil.sendMessage(sender, text);
		}

		return true;
	}
	
	private void cancelTask(UUID uuid)
	{
		attemptMap.remove(uuid);
		Integer id = taskMap.remove(uuid);
		
		if(id != null)
		{
			Bukkit.getScheduler().cancelTask(id);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void on(PlayerDropItemEvent e)
	{
		Player p = e.getPlayer();
		CommonPlayer cp = CraftCommonPlayer.get(p);
		
		if(!cp.isLogged())
		{
			e.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void on(AsyncPlayerChatEvent e)
	{
		Player p = e.getPlayer();
		CommonPlayer cp = CraftCommonPlayer.get(p);
		
		if(!cp.isLogged())
		{
			e.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void on(PlayerCommandPreprocessEvent e)
	{
		Player p = e.getPlayer();
		CommonPlayer cp = CraftCommonPlayer.get(p);
		
		if(cp.isLogged())
		{
			return;
		}
		
		String msg = e.getMessage().substring(1);
		String[] split = msg.split(" ");
		
		PluginCommand pluginCommand = Common.getInstance().getCommand(split[0]);
		
		if(pluginCommand == null ||
				!(pluginCommand.getExecutor() instanceof Login) && !(pluginCommand.getExecutor() instanceof Register))
		{
			e.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void on(PlayerPickupItemEvent e)
	{
		Player p = e.getPlayer();
		CommonPlayer cp = CraftCommonPlayer.get(p);
		
		if(!cp.isLogged())
		{
			e.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void on(InventoryClickEvent e)
	{
		Player p = (Player) e.getWhoClicked();
		CommonPlayer cp = CraftCommonPlayer.get(p);
		
		if(!cp.isLogged())
		{
			e.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void on(InventoryDragEvent e)
	{
		Player p = (Player) e.getWhoClicked();
		CommonPlayer cp = CraftCommonPlayer.get(p);
		
		if(!cp.isLogged())
		{
			e.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void on(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		CommonPlayer cp = CraftCommonPlayer.get(p);
		
		if(!cp.isLogged())
		{
			e.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void on(PlayerInteractEntityEvent e)
	{
		Player p = e.getPlayer();
		CommonPlayer cp = CraftCommonPlayer.get(p);
		
		if(!cp.isLogged())
		{
			e.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void on(EntityDamageEvent e)
	{
		if(!(e.getEntity() instanceof Player))
		{
			return;
		}
		
		Player p = (Player) e.getEntity();
		CommonPlayer cp = CraftCommonPlayer.get(p);
		
		if(!cp.isLogged())
		{
			e.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void on(EntityDamageByEntityEvent e)
	{
		if(!(e.getDamager() instanceof Player))
		{
			return;
		}
		
		Player p = (Player) e.getDamager();
		CommonPlayer cp = CraftCommonPlayer.get(p);
		
		if(!cp.isLogged())
		{
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void on(PlayerQuitEvent e)
	{
		cancelTask(e.getPlayer().getUniqueId());
	}
	
	@EventHandler
	public void on(PlayerSuccessLoginEvent e)
	{
		cancelTask(e.getPlayer().getUniqueId());
	}
	
	@EventHandler
	public void on(PlayerJoinEvent e)
	{
		Player p = e.getPlayer();
		CommonPlayer cp = CraftCommonPlayer.get(p);
		
		Location l = p.getLocation();
		
		if(!cp.isLogged())
		{
			taskMap.put(p.getUniqueId(), new BukkitRunnable()
			{
				private int secondsToKick = 30 / 5;
				@Override
				public void run()
				{					
					if(cp.isLogged())
					{
						cancel();
						return;
					}
					
					if(secondsToKick <= 0)
					{
						p.kickPlayer("");
						return;
					}
					
					secondsToKick--;
					
					p.teleport(l);
					
					ResourceBundle bundle = ResourceBundle.getBundle("message", cp.getLocale());
					
					String nounPassword = bundle.getString("noun.password");
					
					TextComponent[] extra = new TextComponent[1];
					String key;
					
					if(cp.getCommonPlayerData().getPassword() == null)
					{
						key = "cmd.register.warn";
						extra[0] = new TextComponent("/register <" + nounPassword + "> [" + nounPassword + "]");
					}
					else
					{
						key = "cmd.login.warn";
						extra[0] = new TextComponent("/login <" + nounPassword + ">");
					}
					
					TextComponent text = Message.getTextComponent(bundle.getString(key), extra);
					text.setColor(ChatColor.RED);
					p.spigot().sendMessage(text);
				}
			}.runTaskTimer(Common.getInstance(), 0L, 100L).getTaskId());
		}
	}
}