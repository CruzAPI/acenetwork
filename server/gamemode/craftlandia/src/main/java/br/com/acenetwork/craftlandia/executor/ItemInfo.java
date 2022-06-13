package br.com.acenetwork.craftlandia.executor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.google.common.io.Files;

import br.com.acenetwork.commons.manager.CommonsConfig;
import br.com.acenetwork.commons.manager.CommonsConfig.Type;

public class ItemInfo implements TabExecutor
{
	private static final Map<String, Map<Integer, Map<Enchantment, Integer>>> MAP = new HashMap<>();
	
	public ItemInfo()
	{
		File file = CommonsConfig.getFile(Type.ITEM_INFO_FOLDER, true);
		
		for(File files : file.listFiles())
		{
			String[] args = Files.getNameWithoutExtension(files.getName()).split("#");
			YamlConfiguration config = YamlConfiguration.loadConfiguration(files);
			
			Map<Integer, Map<Enchantment, Integer>> map = MAP.containsKey(args[0]) ? MAP.get(args[0]) : new HashMap<>();
			Map<Enchantment, Integer> enchantMap = new HashMap<>();
			
			for(String keys : config.getKeys(false))
			{
				enchantMap.put(Enchantment.getByName(keys), config.getInt(keys));
			}
			
			map.put(Integer.valueOf(args[1]), enchantMap);
			
			MAP.put(args[0], map);
		}
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args)
	{
		return new ArrayList<>();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		if(args.length == 0 && sender instanceof Player)
		{
			Player p = (Player) sender;
			
			ItemStack item = p.getItemInHand();
			
			int id = item.getTypeId();
			short data = item.getDurability();
			String customId = "" + id + ":" + data;
			
			int enchantId = 0;
			
			if(item.hasItemMeta() && item.getItemMeta().hasEnchants())
			{
				if(MAP.containsKey(customId))
				{
					for(Entry<Integer, Map<Enchantment, Integer>> entry : MAP.get(customId).entrySet())
					{
						if(item.getEnchantments().equals(entry.getValue()))
						{
							enchantId = entry.getKey();
							break;
						}
					}
				}
				
				if(enchantId == 0 && MAP.containsKey(customId))
				{
					Optional<Integer> optional = MAP.get(customId).keySet().stream().max(Comparator.naturalOrder());
					
					if(optional.isPresent())
					{
						enchantId = optional.get() + 1;
					}
				}
				
				enchantId = enchantId == 0 ? 1 : enchantId;
				
				Map<Integer, Map<Enchantment, Integer>> map = MAP.containsKey(customId) ? MAP.get(customId) : new HashMap<>();
				map.put(enchantId, item.getEnchantments());
				
				File file = CommonsConfig.getFile(Type.ITEM_INFO, true, customId + "#" + enchantId);
				YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
				
				for(Entry<Enchantment, Integer> entry : item.getEnchantments().entrySet())
				{
					config.set(entry.getKey().getName(), entry.getValue());
				}
				
				try
				{
					config.save(file);
				}
				catch(IOException e)
				{
					e.printStackTrace();
					p.sendMessage("unexpected error");
					return true;
				}
			}
			
			customId = "" + id;
			
			if(data > 0)
			{
				customId += ":" + data;
			}
			
			if(enchantId != 0)
			{
				customId += "#" + enchantId;
			}
			
			p.sendMessage(customId);
			
			for(Entry<Enchantment, Integer> entry : item.getEnchantments().entrySet())
			{
				p.sendMessage(entry.getKey() + " " + entry.getValue());
			}
		}
		else if(args.length == 1)
		{
			String idString = "0";
			String dataString = "0";
			String enchantString = "0";
			
			int j = 0;
			
			int size = 0;
			
			for(int i = 0; i < args[0].length(); i++)
			{
				char c = args[0].charAt(i);
				
				if(String.valueOf(c).matches("\\d")
						&& (j == 0 && size <= 3 || j == 1 && size <= 5 || j == 2 && size <= 3))
				{
					if(j == 0)
					{
						idString += c;
					}
					else if(j == 1)
					{
						dataString += c;
					}
					else
					{
						enchantString += c;
					}
					
					size++;
				}
				else if(c == ':' && j == 0 && i != 0 && i + 1 < args[0].length() && String.valueOf(args[0].charAt(i + 1)).matches("\\d"))
				{
					j = 1;
					size = 0;
				}
				else if(c == '#' && (j == 0 || j == 1) && i + 1 < args[0].length() && String.valueOf(args[0].charAt(i + 1)).matches("\\d"))
				{
					j = 2;
					size = 0;
				}
				else
				{
					sender.sendMessage("erro");
					return true;
				}
			}
			
			int id = Integer.valueOf(idString);
			short data = Short.valueOf(dataString);
			int enchant = Integer.valueOf(enchantString);
			
			if(enchant == 0)
			{
				sender.sendMessage("Material: " + Material.getMaterial(id));
				
				if(data != 0)
				{
					sender.sendMessage("Data: " + data);
				}
			}
			else
			{
				String idData = id + ":" + data;
				
				if(MAP.containsKey(idData))
				{
					Map<Integer, Map<Enchantment, Integer>> map = MAP.get(idData);
					
					if(map.containsKey(enchant))
					{
						sender.sendMessage(map.get(enchant).toString());
						return true;
					}
				}
				
				sender.sendMessage("enchant not found");
			}
		}
		else
		{
			
		}
		
		return false;
	}
}