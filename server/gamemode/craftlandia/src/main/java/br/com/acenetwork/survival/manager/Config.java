package br.com.acenetwork.survival.manager;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.YamlConfiguration;

import br.com.acenetwork.survival.Main;

public class Config
{
	public enum Type
	{
		HOMES,
		HOME,
		RANDOM_ITEM_UUID,
		REGION,
		WRAPPED_BTA_UUID,
		JACKPOT,
		BOT, 
		COMBATLOG, 
		PRICE, 
		PLAYER_INFO, WRAPPED_BTA_MAP, PORTALS, LANDS, LINKS, LAND_SCHEM, LAND_ENTITY_DATA, 
		CONTAINMENT_PICKAXE_UUID, RANDOM_ITEM_SET, PLAYER_DATA,
		SPECIAL_ITEM_UUID,
		SPECIAL_ITEM_SET,
		;
	}
	
	public static File getFile(Type type, boolean createNewFile, Object... args)
	{
		File file;
		YamlConfiguration config = null;

		switch(type)
		{
		case PLAYER_DATA:
			file = new File(Main.getInstance().getDataFolder() + "/player_data", args[0] + ".dat");
			break;
		case LAND_ENTITY_DATA:
			file = new File(Main.getInstance().getDataFolder() + "/lands", "entities.dat");
			break;
		case LANDS:
			file = new File(Main.getInstance().getDataFolder() + "/lands", "map.dat");
			break;
		case LAND_SCHEM:
			file = new File(Main.getInstance().getDataFolder() + "/lands/" + args[0], "schem.dat");
			break;
		case HOMES:
			file = new File(Main.getInstance().getDataFolder() + "/home", "map.dat");
			break;
		case HOME:
			file = new File(Main.getInstance().getDataFolder() + "/home/map", args[0] + ".dat");
			break;
		case REGION:
			file = new File(Main.getInstance().getDataFolder() + "/region/" + args[0], args[1] + ".dat");
			break;
		case PORTALS:
			file = new File(Main.getInstance().getDataFolder(), "portals.dat");
			break;
		case RANDOM_ITEM_UUID:
			file = new File(Main.getInstance().getDataFolder() + "/special_items", "random_item_uuid.dat");
			break;
		case RANDOM_ITEM_SET:
			file = new File(Main.getInstance().getDataFolder() + "/special_items", "random_item_set.dat");
			break;
		case SPECIAL_ITEM_UUID:
			file = new File(Main.getInstance().getDataFolder() + "/special_items", args[0] + ".dat");
			break;
		case SPECIAL_ITEM_SET:
			file = new File(Main.getInstance().getDataFolder() + "/special_items", args[0] + ".dat");
			break;
		case CONTAINMENT_PICKAXE_UUID:
			file = new File(Main.getInstance().getDataFolder() + "/special_items", "containment_pickaxe_uuid.dat");
			break;
		case WRAPPED_BTA_UUID:
			file = new File(Main.getInstance().getDataFolder() + "/special_items", "wrapped_bta.dat");
			break;
		case WRAPPED_BTA_MAP:
			file = new File(Main.getInstance().getDataFolder() + "/special_items", "wrapped_bta_map.dat");
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
