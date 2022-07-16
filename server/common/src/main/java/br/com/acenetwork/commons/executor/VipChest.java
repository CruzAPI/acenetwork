package br.com.acenetwork.commons.executor;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import br.com.acenetwork.commons.Common;
import br.com.acenetwork.commons.CommonsUtil;
import br.com.acenetwork.commons.manager.CommonsConfig;
import br.com.acenetwork.commons.manager.CommonsConfig.Type;
import br.com.acenetwork.commons.manager.Message;
import br.com.acenetwork.commons.player.CommonPlayer;
import br.com.acenetwork.commons.player.craft.CraftCommonPlayer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

public class VipChest implements TabExecutor, Listener
{
	public static final Set<UUID> ACTIVATED_VIPS = new HashSet<>();
	private static String vipHiddenUUID;
	
	public VipChest()
	{
		File file = CommonsConfig.getFile(Type.ACTIVATED_VIPS, true);
		
		try(RandomAccessFile access = new RandomAccessFile(file, "r"))
		{
			while(access.getFilePointer() < access.length())
			{
				String builder = "";
				
				for(int i = 0; i < 36; i++)
				{
					builder += access.readChar();
				}
				
				ACTIVATED_VIPS.add(UUID.fromString(builder));
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
		file = CommonsConfig.getFile(Type.CONFIG, true);
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		
		vipHiddenUUID = config.contains("vip-hidden-uuid") 
				? config.getString("vip-hidden-uuid") 
				: CommonsUtil.getRandomItemUUID();
		
		config.set("vip-hidden-uuid", vipHiddenUUID);
		
		try
		{
			config.save(file);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
		Bukkit.getPluginManager().registerEvents(this, Common.getPlugin());
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
		
		bundle = ResourceBundle.getBundle("message", cp.getLocale());

		if(args.length == 0)
		{		
			try
			{
				cp.readVipChest();
			}
			catch(IOException e)
			{
				e.printStackTrace();
				p.sendMessage(ChatColor.RED + bundle.getString("commons.unexpected-error"));
			}
		}
		else
		{
			TextComponent[] extra = new TextComponent[1];
			
			extra[0] = new TextComponent("/" + aliases);
			
			TextComponent text = Message.getTextComponent(bundle.getString("commons.cmds.wrong-syntax-try"), extra);
			text.setColor(ChatColor.RED);
			CommonsUtil.sendMessage(sender, text);
		}

		return true;
	}
	
	public static String getVipFirstHiddenUUID()
	{
		if(vipHiddenUUID == null)
		{
			throw new NullPointerException();
		}
		
		return vipHiddenUUID;
	}
}