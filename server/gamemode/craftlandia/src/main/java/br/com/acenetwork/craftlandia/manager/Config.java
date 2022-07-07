package br.com.acenetwork.craftlandia.manager;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.YamlConfiguration;

import br.com.acenetwork.craftlandia.Main;

public class Config
{
	public enum Type
	{
		REGION,
		WRAPPED_BTA_UUID,
		JACKPOT,
		BOT, 
		COMBATLOG, 
		PRICE, 
		PLAYER_INFO,
		;
	}
	
	public static File getFile(Type type, boolean createNewFile, Object... args)
	{
		File file;
		YamlConfiguration config = null;

		switch(type)
		{
		case REGION:
			file = new File(Main.getInstance().getDataFolder() + "/region/" + args[0], args[1] + ".dat");
			break;
		case WRAPPED_BTA_UUID:
			file = new File(Main.getInstance().getDataFolder() + "/wrapped_bta", "uuid.dat");
			break;
		case JACKPOT:
			file = new File(Main.getInstance().getDataFolder(), "jackpot.dat");
			break;
		case PLAYER_INFO:
			file = new File(Main.getInstance().getDataFolder() + "/player_info", args[0] + ".yml");
			break;
		case BOT:
			file = new File(Main.getInstance().getDataFolder(), "bot.yml");
			break;
		case COMBATLOG:
			file = new File(Main.getInstance().getDataFolder() + "/combatlog", args[0] + ".yml");
			break;
		case PRICE:
			file = new File(Main.getInstance().getDataFolder(), "price.dat");
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
				
				switch(type)
				{
				case BOT:
					config.set("min-delay", 3L * 60L * 20L);
					config.set("max-delay", 10L * 60L * 20L);
					config.set("min-percent", 10);
					config.set("max-percent", 50);
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
