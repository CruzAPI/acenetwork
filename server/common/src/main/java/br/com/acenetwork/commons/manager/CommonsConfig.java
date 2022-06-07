package br.com.acenetwork.commons.manager;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.YamlConfiguration;

import br.com.acenetwork.commons.Common;

public class CommonsConfig
{
	public enum Type
	{
		WHITELISTED_IP,
		BALANCE_FOLDER, BALANCE_RAID_PLAYER, BALANCE_RAID_FOLDER, 
		CLANS_JSON, MESSAGE, GROUP, USER, 
		PLAYER, 
		BANNED_PLAYERS, BANNED_IPS, MUTED_PLAYERS, DATABASE;
	}

	public static File getFile(Type type, boolean createNewFile, Object... args)
	{
		File file;
		YamlConfiguration config = null;

		switch(type)
		{
		case WHITELISTED_IP:
			file = new File(Common.getPlugin().getConfigFolder(), "whitelisted_ips.yml");
			break;
		case DATABASE:
			file = new File(Common.getPlugin().getConfigFolder(), "database.yml");
			break;
		case BALANCE_FOLDER:
			file = new File(Common.getPlugin().getConfigFolder() + "/balance");
			break;
		case BALANCE_RAID_FOLDER:
			file = new File(Common.getPlugin().getConfigFolder() + "/balance/raid");
			break;
		case BALANCE_RAID_PLAYER:
			file = new File(Common.getPlugin().getConfigFolder() + "/balance/raid", args[0] + ".yml");
			config = YamlConfiguration.loadConfiguration(file);
			config.set("max-balance", 3000.0D);
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
		case GROUP:
			file = new File(Common.getPlugin().getConfigFolder() + "/permissions/groups", args[0] + ".yml");
			break;
		case USER:
			file = new File(Common.getPlugin().getConfigFolder() + "/permissions/users", args[0] + ".yml");
			break;
		case PLAYER:
			file = new File(Common.getPlugin().getConfigFolder() + "/players", args[0] + ".yml");
			break;
		default:
			return null;
		}
		
		if(createNewFile && !file.exists())
		{
			file.toPath().getParent().toFile().mkdirs();
			
			try
			{
				file.createNewFile();
				
				if(config == null)
				{
					config = YamlConfiguration.loadConfiguration(file);
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