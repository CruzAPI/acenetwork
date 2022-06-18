package br.com.acenetwork.craftlandia.executor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import br.com.acenetwork.commons.executor.Balance;
import br.com.acenetwork.commons.executor.BroadcastCMD;
import br.com.acenetwork.commons.manager.CommonsConfig;
import br.com.acenetwork.commons.manager.Message;
import br.com.acenetwork.commons.player.CommonPlayer;
import br.com.acenetwork.commons.player.craft.CraftCommonPlayer;
import br.com.acenetwork.craftlandia.manager.Config;
import br.com.acenetwork.craftlandia.manager.CryptoInfo;
import br.com.acenetwork.craftlandia.manager.Config.Type;
import br.com.acenetwork.craftlandia.manager.PRICE;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Temp implements TabExecutor
{
	private static final double HIGHEST = 200.0D;
	private static final double HIGH = 100.0D;
	private static final double NORMAL = 50.0D;
	private static final double LOW = 25.0D;
	private static final double LOWEST = 10.0D;
	private static final double MEME = 5.0D;
	
	public Temp()
	{
		// 200 < 1%  liquidez altíssima
		// 100 < 2% liquidez alta
		// 50 < 4% liquidez média
		// 25 < 7,5% liquidez baixa
		// 10 < 17,5% liquidez baixíssima
		// 5 < 30%  liquidez meme
		
		new PRICE(Material.STONE.getId(), (short) 0, 1.0D, 64, LOW);
		new PRICE(Material.STONE.getId(), (short) 1, 1.0D, 64, LOW);
		new PRICE(Material.STONE.getId(), (short) 2, 1.0D, 64, LOW);
		new PRICE(Material.STONE.getId(), (short) 3, 1.0D, 64, LOW);
		new PRICE(Material.STONE.getId(), (short) 4, 1.0D, 64, LOW);
		new PRICE(Material.STONE.getId(), (short) 5, 1.0D, 64, LOW);
		new PRICE(Material.STONE.getId(), (short) 6, 1.0D, 64, LOW);
		
//		for(Material type : Material.values())
//		{
//			switch(type)
//			{
//			case STONE:
//			case GRASS:
//			case DIRT:
//			case COBBLESTONE:
//				break;
//			case WOOD:
////				new PRICE(type, (short) 0, 1.0D, 64, LOWEST);
//				break;
//			case SAND:
//			case GRAVEL:
//			case COAL_ORE:
//			case IRON_ORE:
//			case GOLD_ORE:
//			case LAPIS_ORE:
//			case REDSTONE_ORE:
//			case DIAMOND_ORE:
//			case EMERALD_ORE:
//			case QUARTZ_ORE:
//			case LOG:
//			case LOG_2:
//			case SPONGE:
//			case GLASS:
//			case SANDSTONE:
//			case WOOL:
//			case BRICK:
//			case BOOKSHELF:
//			case MOSSY_COBBLESTONE:
//			case OBSIDIAN:
//			case ICE:
//			case SNOW_BLOCK:
//			case CLAY:
//			case PUMPKIN:
//			case NETHERRACK:
//			case SOUL_SAND:
//			case GLOWSTONE:
//			case JACK_O_LANTERN:
//			case STAINED_GLASS:
//			case SMOOTH_BRICK:
//			case MELON_BLOCK:
//			case MYCEL:
//			case NETHER_BRICK:
//			case ENDER_STONE:
//			case QUARTZ_BLOCK:
//			case STAINED_CLAY:
//			case PRISMARINE:
//			case SEA_LANTERN:
//			case HAY_BLOCK:
//			case HARD_CLAY:
//			case PACKED_ICE:
//			case RED_SANDSTONE:
//			case TNT:
//			case REDSTONE_LAMP_OFF:
//			
//			
//			
//			case SAPLING:
//			case LEAVES:
//			case LEAVES_2:
//			case WEB:
//			case LONG_GRASS:
//			case DEAD_BUSH:
//			case YELLOW_FLOWER:
//			case RED_ROSE:
//			case BROWN_MUSHROOM:
//			case RED_MUSHROOM:
//			case CACTUS:
//			case VINE:
//			case WATER_LILY:
//			case DOUBLE_PLANT:
//			case SKULL_ITEM:
//			
//				
//			
//			
//				
//			case APPLE:
//			case MUSHROOM_SOUP:
//			case BREAD:
//			case PORK:
//			case GRILLED_PORK:
//			case GOLDEN_APPLE:
//			case RAW_FISH:
//			case COOKED_FISH:
//			case CAKE:
//			case COOKIE:
//			case MELON:
//			case RAW_BEEF:
//			case COOKED_BEEF:
//			case RAW_CHICKEN:
//			case COOKED_CHICKEN:
//			case ROTTEN_FLESH:
//			case SPIDER_EYE:
//			case CARROT_ITEM:
//			case POTATO_ITEM:
//			case BAKED_POTATO:
//			case POISONOUS_POTATO:
//			case PUMPKIN_PIE:
//			case RABBIT:
//			case COOKED_RABBIT:
//			case RABBIT_STEW:
//			case MUTTON:
//			case COOKED_MUTTON:
//			
//				
//			case GHAST_TEAR:
//			case FERMENTED_SPIDER_EYE:
//			case BLAZE_POWDER:
//			case MAGMA_CREAM:
//			case SPECKLED_MELON:
//			case GOLDEN_CARROT:
//			case RABBIT_FOOT:
//			
//			case COAL:
//			case REDSTONE:
//			case DIAMOND:
//			case IRON_INGOT:
//			case GOLD_INGOT:
//			case COMPASS:
//			case WATCH:
//			case NAME_TAG:
//			case SADDLE:
//			case ARROW:
//			case STICK:
//			case BOWL:
//			case STRING:
//			case FEATHER:
//			case SULPHUR:
//			case SEEDS:
//			case WHEAT:
//			case FLINT:
//			case LEATHER:
//			case CLAY_BRICK:
//			case CLAY_BALL:
//			case SUGAR_CANE:
//			case EGG:
//			case GLOWSTONE_DUST:
//			case INK_SACK:
//			case SUGAR:
//			case PUMPKIN_SEEDS:
//			case MELON_SEEDS:
//			case BLAZE_ROD:
//			case NETHER_WARTS:
//			case EMERALD:
//			case NETHER_STAR:
//			case NETHER_BRICK_ITEM:
//			case QUARTZ:
//			case PRISMARINE_SHARD:
//			case PRISMARINE_CRYSTALS:
//			case RABBIT_HIDE:
//			case SNOW_BALL:
//			case PAPER:
//			case BOOK:
//			case SLIME_BALL:
//			case BONE:
//			case ENDER_PEARL:
//			case EYE_OF_ENDER:
//			case FIREBALL:
//			case FIREWORK_CHARGE:
//			case IRON_BARDING:
//			case GOLD_BARDING:
//			case DIAMOND_BARDING:
//			case GOLD_RECORD:
//			case GREEN_RECORD:
//			case RECORD_3:
//			case RECORD_4:
//			case RECORD_5:
//			case RECORD_6:
//			case RECORD_7:
//			case RECORD_8:
//			case RECORD_9:
//			case RECORD_10:	
//			case RECORD_11:
//			case RECORD_12:
//			default:
//				break;
//			}
//		}
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String aliases, String[] args)
	{
		return new ArrayList<>();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String aliases, String[] args)
	{
		if(sender instanceof Player)
		{
			sender.sendMessage("Unknown command. Type \"/help\" for help.");
			return true;
		}
		
		File file = Config.getFile(Type.PRICE, true);
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		
		for(PRICE price : PRICE.LIST)
		{
//			Bukkit.broadcastMessage(price.toString());
//			config.set(price.ide + ".market-cap", price.marketCap);
//			config.set(price.type + ".circulating-supply", price.circulatingSupply);
		}
		
		try
		{
			config.save(file);
			Bukkit.broadcastMessage("top");
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
		return true;
	}
}