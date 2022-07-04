package br.com.acenetwork.craftlandia.executor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.function.Supplier;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import br.com.acenetwork.commons.CommonsUtil;
import br.com.acenetwork.commons.event.PlayerInvincibilityChangeEvent;
import br.com.acenetwork.commons.event.PlayerModeChangeEvent;
import br.com.acenetwork.commons.manager.Message;
import br.com.acenetwork.commons.player.CommonPlayer;
import br.com.acenetwork.commons.player.craft.CraftCommonPlayer;
import br.com.acenetwork.craftlandia.Main;
import br.com.acenetwork.craftlandia.player.SurvivalPlayer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

public class Playtime implements TabExecutor, Listener
{
	public static final Map<UUID, Time> MAP = new HashMap<>();
	private static final String WBTA_DISPLAY_NAME = ChatColor.DARK_GREEN + "Wrapped $BTA";
	private static final int SEMI_LENGTH = CommonsUtil.getRandomItemUUID().length() + WBTA_DISPLAY_NAME.length();
	private final 
	public static final Supplier<ItemStack> WRAPPED_BTA_SUPPLIER = () ->
	{
		ItemStack wbta = new ItemStack(Material.EMERALD);
		ItemMeta meta = wbta.getItemMeta();
		meta.setDisplayName(CommonsUtil.getRandomItemUUID() + WBTA_DISPLAY_NAME + CommonsUtil.hideNumberData(System.currentTimeMillis()));
		meta.addEnchant(Enchantment.DURABILITY, 1, true);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		wbta.setItemMeta(meta);
		
		return null;
	};
	
	public Playtime()
	{
		Bukkit.getPluginManager().registerEvents(this, Main.getPlugin());
	}
	
	private class Time
	{
		private int taskId;
		private long seconds = 30L;
//		private long seconds = 2L * 60L * 60L;
		
		public Time()
		{
			this.taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), () ->
			{
				if(--seconds <= 0L)
				{
					Bukkit.getScheduler().cancelTask(taskId);
				}
			}, 20L, 20L);
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
		ResourceBundle bundle;
		
		if(!(sender instanceof Player))
		{
			bundle = ResourceBundle.getBundle("message");
			sender.sendMessage(ChatColor.RED + bundle.getString("commons.cmds.cant-perform-command"));
			return true;
		}
		
		Player p = (Player) sender;
		CommonPlayer cp = CraftCommonPlayer.get(p);
		
		bundle = ResourceBundle.getBundle("message", cp.getLocale());
		
		if(args.length == 0)
		{
			TextComponent[] extra;
			TextComponent text;
			
			if(!isValidWorld(p.getWorld()))
			{
				extra = new TextComponent[1];
				extra[0] = new TextComponent("Raid");
				
				text = Message.getTextComponent(bundle.getString("cmd.playtime.invalid-world"), extra);
				text.setColor(ChatColor.RED);
				p.spigot().sendMessage(text);
				return true;
			}
			
			if(!(cp instanceof SurvivalPlayer))
			{
				p.sendMessage(ChatColor.RED + bundle.getString("cmd.playtime.player-mode"));
				return true;
			}
			
			if(cp.hasInvincibility())
			{
				p.sendMessage(ChatColor.RED + bundle.getString("cmd.playtime.has-invincibility"));
				return true;
			}
			
			if(!MAP.containsKey(p.getUniqueId()))
			{
				put(p.getUniqueId());
			}
			
			long time = MAP.get(p.getUniqueId()).seconds;
			
			long seconds = time % 60;
			long minutes = time / (60L) % 60;
			long hours = time / (60L * 60L) % 24;
			
			String h = bundle.getString("commons.words.hour").substring(0, 1);
			String m = bundle.getString("commons.words.minute").substring(0, 1);
			String s = bundle.getString("commons.words.second").substring(0, 1);
			
			String msg = "";
			
			if(hours != 0L)
			{
				msg = hours + h + " " + minutes + m + " " + seconds + s;
			}
			else if(minutes != 0L)
			{
				msg = minutes + m + " " + seconds + s;
			}
			else
			{
				msg = seconds + s;
			}
			
			extra = new TextComponent[2];
			
			extra[0] = new TextComponent("$BTA");
			extra[0].setColor(ChatColor.DARK_PURPLE);
			
			extra[1] = new TextComponent(msg);
			extra[1].setColor(ChatColor.GRAY);
			
			text = Message.getTextComponent(bundle.getString("cmd.playtime"), extra);
			text.setColor(ChatColor.LIGHT_PURPLE);
			p.spigot().sendMessage(text);
		}
		else
		{
			TextComponent[] extra = new TextComponent[1];
			
			extra[0] = new TextComponent("/" + aliases);
			
			TextComponent text = Message.getTextComponent(bundle.getString("commons.cmds.wrong-syntax-try"), extra);
			text.setColor(ChatColor.RED);
			p.spigot().sendMessage(text);
		}

		return false;
	}
	
	@EventHandler
	public void a(PlayerInvincibilityChangeEvent e)
	{
		CommonPlayer cp = e.getCommonPlayer();
		Player p = cp.getPlayer();
		
		if(!isValidWorld(p.getWorld()))
		{
			return;
		}
		
		if(cp.hasInvincibility())
		{
			remove(p.getUniqueId());
		}
		else
		{
			put(p.getUniqueId());
		}
	}
	
	private void put(UUID uuid)
	{
		Time time = MAP.put(uuid, new Time());
		
		if(time != null)
		{
			Bukkit.getScheduler().cancelTask(time.taskId);
		}
	}
	
	private void remove(UUID uuid)
	{
		Time time = MAP.remove(uuid);
		
		if(time != null)
		{
			Bukkit.getScheduler().cancelTask(time.taskId);
		}
	}
	
	@EventHandler
	public void a(PlayerModeChangeEvent e)
	{
		CommonPlayer cp = e.getCommonPlayer();
		Player p = cp.getPlayer();
		
		if(cp instanceof SurvivalPlayer)
		{
			put(p.getUniqueId());
		}
		else
		{
			remove(p.getUniqueId());
		}
	}
	
	@EventHandler
	public void a(PlayerChangedWorldEvent e)
	{
		Player p = e.getPlayer();
		CommonPlayer cp = CraftCommonPlayer.get(p);
		
		if(!(cp instanceof SurvivalPlayer))
		{
			remove(p.getUniqueId());
			return;
		}
		
		boolean validFrom = isValidWorld(e.getFrom());
		boolean validTo = isValidWorld(p.getWorld());
		
		if(validFrom && validTo)
		{
			return;
		}
		
		if(validTo)
		{
			put(p.getUniqueId());
		}
		else
		{
			remove(p.getUniqueId());
		}
	}
	
	@EventHandler
	public void a(PlayerQuitEvent e)
	{
		Player p = e.getPlayer();
		remove(p.getUniqueId());
	}
	
	@EventHandler
	public void a(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		ItemStack item = e.getItem();
		
		CommonsUtil.compareUUID(item, WBTA_MAINUUID);
	}
	
	private boolean isValidWorld(World w)
	{
		return w.getName().contains("factions");
	}
}
