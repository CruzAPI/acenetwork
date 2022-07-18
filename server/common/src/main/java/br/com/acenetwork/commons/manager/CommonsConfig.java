package br.com.acenetwork.commons.manager;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import org.bukkit.configuration.file.YamlConfiguration;

import br.com.acenetwork.commons.Common;
import br.com.acenetwork.commons.inventory.VipChestGUI;

public class CommonsConfig
{
	public enum Type
	{
		LINKS,
		PLAYER_DATA,
		PLAYERS_DATA,
		PLAYER_PERMISSION,
		GROUPS_DAT,
		USERS_DAT,
		USER_DAT,
		USERS_FOLDER,
		BLOCK_DATA,
		ITEM_INFO_FOLDER,
		ITEM_INFO,
		WHITELISTED_IP,
		CLANS_JSON, MESSAGE, 
		PLAYER, 
		BANNED_PLAYERS, BANNED_IPS, MUTED_PLAYERS, DATABASE, CHEST_VIP, ACTIVATED_VIPS, CONFIG,
		;
	}

	public static File getFile(Type type, boolean createNewFile, Object... args)
	{
		File file;
		YamlConfiguration config = null;

		switch(type)
		{
		case LINKS:
			file = new File(Common.getPlugin().getDataFolder(), "links.yml");
			break;
		case PLAYER_DATA:
			file = new File(Common.getPlugin().getConfigFolder() + "/player_data/users/" + args[0], "user.dat");
			break;
		case PLAYERS_DATA:
			file = new File(Common.getPlugin().getConfigFolder() + "/player_data/", "users.dat");
			break;
		case PLAYER_PERMISSION:
			file = new File(Common.getPlugin().getConfigFolder() + "/player_data/" + args[0], "permisison.dat");
		case CONFIG:
			file = new File(Common.getPlugin().getConfigFolder(), "config.yml");
			break;
		case CHEST_VIP:
			file = new File(Common.getPlugin().getConfigFolder() + "/chest_vip", args[0] + ".yml");
			break;
		case ACTIVATED_VIPS:
			file = new File(Common.getPlugin().getConfigFolder(), "activated_vips.txt");
			break;
		case ITEM_INFO:
			file = new File(Common.getPlugin().getConfigFolder() + "/item_info", args[0] + ".yml");
			break;
		case ITEM_INFO_FOLDER:
			file = new File(Common.getPlugin().getConfigFolder() + "/item_info");
			break;
		case WHITELISTED_IP:
			file = new File(Common.getPlugin().getConfigFolder(), "whitelisted_ips.yml");
			break;
		case DATABASE:
			file = new File(Common.getPlugin().getConfigFolder(), "database.yml");
			break;
		case MESSAGE:
			file = new File(Common.getPlugin().getConfigFolder() + "/messages", args[0] + ".yml");
			break;
		case MUTED_PLAYERS:
			file = new File(Common.getPlugin().getConfigFolder() + "/muted-players", args[0] + ".yml");
			break;
		case BANNED_IPS:
			file = new File(Common.getPlugin().getConfigFolder() + "/banned-ips", args[0] + ".yml");
			break;
		case BANNED_PLAYERS:
			file = new File(Common.getPlugin().getConfigFolder() + "/banned-players", args[0] + ".yml");
			break;
		case GROUPS_DAT:
			file = new File(Common.getPlugin().getConfigFolder() + "/permissions", "groups.dat");
			break;
		case USERS_DAT:
			file = new File(Common.getPlugin().getConfigFolder() + "/permissions", "users.dat");
			break;
		case USER_DAT:
			file = new File(Common.getPlugin().getConfigFolder() + "/permissions/users", args[0] + ".dat");
			break;
		case USERS_FOLDER:
			file = new File(Common.getPlugin().getConfigFolder() + "/permissions/users");
			break;
		case PLAYER:
			file = new File(Common.getPlugin().getConfigFolder() + "/players", args[0] + ".yml");
			break;
		default:
			return null;
		}
		
		if(createNewFile && !file.exists())
		{
			if(type.name().contains("FOLDER"))
			{
				file.mkdirs();
				return file;
			}
			
			file.toPath().getParent().toFile().mkdirs();
			
			try
			{
				file.createNewFile();
				
				if(!file.getName().endsWith(".yml"))
				{
					return file;
				}
				
				if(config == null)
				{
					config = YamlConfiguration.loadConfiguration(file);
				}
				
				switch(type)
				{
				case LINKS:
					config.set("website", "https://www.acetokennetwork.com.br/");
					config.set("lands", "https://www.lands.com.br/");
					config.set("whitepaper", "https://ace-network.gitbook.io/ace-network-whitepaper-versao-em-portugues/introducao/visao-geral");
					config.set("tracker", "https://www.brawl.com/wiki/tracking-raid/");
					config.set("discord", "https://discord.gg/EYV538gQt7");
					break;
				default:
					break;
				}
				
				config.save(file);
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}

		return file;
	}	
}