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
		GROUPS_FOLDER,
		USERS_FOLDER,
		BLOCK_DATA,
		SIGN_DATA,
		ITEM_INFO_FOLDER,
		ITEM_INFO,
		WHITELISTED_IP,
		CLANS_JSON, MESSAGE, GROUP, USER, 
		PLAYER, 
		BANNED_PLAYERS, BANNED_IPS, MUTED_PLAYERS, DATABASE, CHEST_VIP, 
		PERMISSIONS
		;
	}

	public static File getFile(Type type, boolean createNewFile, Object... args)
	{
		File file;
		YamlConfiguration config = null;

		switch(type)
		{
		case CHEST_VIP:
			file = new File(Common.getPlugin().getConfigFolder() + "/chest_vip", args[0] + ".txt");
			break;
		case SIGN_DATA:
			file = new File(Common.getPlugin().getConfigFolder() + "/sign_data", args[0] + ".txt");
			break;
		case BLOCK_DATA:
			file = new File(Common.getPlugin().getConfigFolder() + "/block_data", args[0] + ".txt");
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
		case GROUP:
			file = new File(Common.getPlugin().getConfigFolder() + "/permissions/groups", args[0] + ".yml");
			break;
		case GROUPS_FOLDER:
			file = new File(Common.getPlugin().getConfigFolder() + "/permissions/groups");
			break;
		case USER:
			file = new File(Common.getPlugin().getConfigFolder() + "/permissions/users", args[0] + ".yml");
			break;
		case PERMISSIONS:
			file = new File(Common.getPlugin().getConfigFolder(), "permissions.dat");
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
				
				if(!file.getName().endsWith(".txt"))
				{
					switch(type)
					{
					case CHEST_VIP:
//						try(RandomAccessFile access = new RandomAccessFile(file, "rw"))
//						{
//							for(int i = 0; i < 9 * 3; i++)
//							{
//								access.writeByte(0);
//							}
//							
//							break;
//						}
//						catch(IOException ex)
//						{
//							throw ex;
//						}
					default:
						break;
					}
					
					return file;
				}
				
				if(!file.getName().endsWith(".yml"))
				{
					return file;
				}
				
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