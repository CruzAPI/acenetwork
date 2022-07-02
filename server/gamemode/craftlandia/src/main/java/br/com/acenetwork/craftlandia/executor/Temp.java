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
import br.com.acenetwork.craftlandia.Rarity;
import br.com.acenetwork.craftlandia.Util;
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
		
		new PRICE(1, (short) 0, 0.3D, 64, LOW);
		new PRICE(1, (short) 1, 0.3D, 64, LOW);
		new PRICE(1, (short) 2, 0.3D, 64, LOW);
		new PRICE(1, (short) 3, 0.3D, 64, LOW);
		new PRICE(1, (short) 4, 0.3D, 64, LOW);
		new PRICE(1, (short) 5, 0.3D, 64, LOW);
		new PRICE(1, (short) 6, 0.3D, 64, LOW);
		new PRICE(2, (short)0, 0.25D, 640, LOW);
		new PRICE(3, (short)0, 0.01D, 640, NORMAL);
		new PRICE(3, (short)1, 0.01D, 640, NORMAL);
		new PRICE(3, (short)2, 0.01D, 640, NORMAL);
		new PRICE(4, (short)0, 0.03D, 640, NORMAL);
		new PRICE(5, (short)0, 0.1D, 640, HIGHEST);
		new PRICE(5, (short)1, 0.1D, 640, HIGHEST);
		new PRICE(5, (short)2, 0.1D, 640, HIGHEST);
		new PRICE(5, (short)3, 0.1D, 640, HIGHEST);
		new PRICE(5, (short)4, 0.1D, 640, HIGHEST);
		new PRICE(5, (short)5, 0.1D, 640, HIGHEST);
		new PRICE(6, (short)0, 0.1D, 640, LOW);
		new PRICE(6, (short)1, 0.1D, 640, LOW);
		new PRICE(6, (short)2, 0.1D, 640, LOW);
		new PRICE(6, (short)3, 0.1D, 640, LOW);
		new PRICE(6, (short)4, 0.1D, 640, LOW);
		new PRICE(6, (short)5, 0.1D, 640, LOW);
		new PRICE(12, (short)0, 0.04D, 320, NORMAL);
		new PRICE(12, (short)1, 0.04D, 320, NORMAL);
		new PRICE(13, (short)0, 0.04D, 320, NORMAL);
		new PRICE(14, (short)0, 1.5D, 128, LOW);
		new PRICE(15, (short)0, 1.0D, 128, LOW);
		new PRICE(16, (short)0, 1.5D, 128, LOW);
		new PRICE(17, (short)0, 0.4D, 128, NORMAL);
		new PRICE(17, (short)1, 0.4D, 128, NORMAL);
		new PRICE(17, (short)2, 0.4D, 128, NORMAL);
		new PRICE(17, (short)3, 0.4D, 128, NORMAL);
		new PRICE(18, (short)0, 0.03D, 128, NORMAL);
		new PRICE(18, (short)1, 0.03D, 128, NORMAL);
		new PRICE(18, (short)2, 0.03D, 128, NORMAL);
		new PRICE(18, (short)3, 0.03D, 128, NORMAL);
		new PRICE(162, (short)0, 0.4D, 128, NORMAL);
		new PRICE(162, (short)1, 0.4D, 128, NORMAL);
		new PRICE(19, (short)0, 50.0D, 4, LOWEST);
		new PRICE(20, (short)0, 0.15D, 192, NORMAL);
		new PRICE(21, (short)0, 1.2D, 128, LOW);
		new PRICE(22, (short)0, 0.5D, 128, NORMAL);
		new PRICE(24, (short)0, 0.16D, 128, MEME);
		new PRICE(24, (short)0, 0.16D, 128, MEME);
		new PRICE(24, (short)0, 0.16D, 128, MEME);
		new PRICE(35, (short)0, 0.25D, 320, NORMAL);
		new PRICE(35, (short)1, 0.25D, 320, NORMAL);
		new PRICE(35, (short)2, 0.25D, 320, NORMAL);
		new PRICE(35, (short)3, 0.25D, 320, NORMAL);
		new PRICE(35, (short)4, 0.25D, 320, NORMAL);
		new PRICE(35, (short)5, 0.25D, 320, NORMAL);
		new PRICE(35, (short)6, 0.25D, 320, NORMAL);
		new PRICE(35, (short)7, 0.25D, 320, NORMAL);
		new PRICE(35, (short)8, 0.25D, 320, NORMAL);
		new PRICE(35, (short)9, 0.25D, 320, NORMAL);
		new PRICE(35, (short)10, 0.25D, 320, NORMAL);
		new PRICE(35, (short)11, 0.25D, 320, NORMAL);
		new PRICE(35, (short)12, 0.25D, 320, NORMAL);
		new PRICE(35, (short)13, 0.25D, 320, NORMAL);
		new PRICE(35, (short)14, 0.25D, 320, NORMAL);
		new PRICE(35, (short)15, 0.25D, 320, NORMAL);
		new PRICE(45, (short)0, 0.5D, 320, NORMAL);
		new PRICE(47, (short)0, 0.6D, 128, HIGHEST);
		new PRICE(48, (short)0, 2.0D, 256, LOWEST);
		new PRICE(49, (short)0, 1.2D, 256, LOWEST);
		new PRICE(56, (short)0, 3.0D, 256, LOWEST);
		new PRICE(73, (short)0, 0.7D, 256, LOW);
		new PRICE(79, (short)0, 0.2D, 256, NORMAL);
		new PRICE(80, (short)0, 0.15D, 256, NORMAL);
		new PRICE(86, (short)0, 0.05D, 640, HIGH);
		new PRICE(87, (short)0, 0.03D, 640, HIGHEST);
		new PRICE(88, (short)0, 0.05D, 640, HIGHEST);
		new PRICE(348, (short)0, 0.25D, 640, HIGHEST);
		new PRICE(112, (short)0, 0.15D, 640, HIGHEST);
		new PRICE(121, (short)0, 1.0D, 640, NORMAL);
		new PRICE(129, (short)0, 5.0D, 640, NORMAL);
		new PRICE(133, (short)0, 1.2D, 640, NORMAL);
		new PRICE(153, (short)0, 0.2D, 640, NORMAL);
		new PRICE(168, (short)0, 1.0D, 640, NORMAL);
		new PRICE(168, (short)1, 1.0D, 640, NORMAL);
		new PRICE(168, (short)2, 1.0D, 640, NORMAL);
		new PRICE(174, (short)0, 1.0D, 640, LOW);
		new PRICE(37, (short)0, 0.04D, 128, HIGHEST);
		new PRICE(38, (short)0, 0.04D, 128, HIGHEST);
		new PRICE(38, (short)1, 0.04D, 128, HIGHEST);
		new PRICE(38, (short)2, 0.04D, 128, HIGHEST);
		new PRICE(38, (short)3, 0.04D, 128, HIGHEST);
		new PRICE(38, (short)4, 0.04D, 128, HIGHEST);
		new PRICE(38, (short)5, 0.04D, 128, HIGHEST);
		new PRICE(38, (short)6, 0.04D, 128, HIGHEST);
		new PRICE(38, (short)7, 0.04D, 128, HIGHEST);
		new PRICE(38, (short)8, 0.04D, 128, HIGHEST);
		new PRICE(39, (short)0, 0.3D, 128, NORMAL);
		new PRICE(40, (short)0, 0.3D, 128, NORMAL);
		new PRICE(81, (short)0, 0.5D, 640, NORMAL);
		new PRICE(175, (short)0, 0.04D, 640, NORMAL);
		new PRICE(175, (short)1, 0.04D, 640, NORMAL);
		new PRICE(175, (short)2, 0.04D, 640, NORMAL);
		new PRICE(175, (short)3, 0.04D, 640, NORMAL);
		new PRICE(175, (short)4, 0.04D, 640, NORMAL);
		new PRICE(175, (short)5, 0.04D, 640, NORMAL);
		new PRICE(46, (short)0, 0.5D, 128, LOWEST);
		new PRICE(138, (short)0, 0.5D, 640, NORMAL);
		new PRICE(332, (short)0, 0.02D, 320, NORMAL);
		new PRICE(340, (short)0, 0.1D, 640, NORMAL);
		new PRICE(339, (short)0, 0.05D, 640, NORMAL);
		new PRICE(341, (short)0, 0.8D, 640, NORMAL);
		new PRICE(352, (short)0, 0.4D, 640, NORMAL);
		new PRICE(368, (short)0, 0.4D, 640, NORMAL);
		new PRICE(381, (short)0, 1.25D, 640, NORMAL);
		new PRICE(385, (short)0, 2.0D, 128, LOW);
		new PRICE(402, (short)0, 1.2D, 64, MEME);
		new PRICE(260, (short)0, 0.5D, 640, NORMAL);
		new PRICE(282, (short)0, 1.3D, 128, NORMAL);
		new PRICE(319, (short)0, 0.1D, 128, NORMAL);
		new PRICE(322, (short)0, 1.5D, 128, MEME);
		new PRICE(322, (short)1, 5.0D, 128, MEME);
		new PRICE(349, (short)0, 0.2D, 320, NORMAL);
		new PRICE(349, (short)1, 0.2D, 320, NORMAL);
		new PRICE(349, (short)2, 0.2D, 320, NORMAL);
		new PRICE(349, (short)3, 0.2D, 320, NORMAL);
		new PRICE(349, (short)3, 0.2D, 320, NORMAL);
		new PRICE(263, (short)0, 0.7D, 640, HIGH);
		new PRICE(263, (short)1, 0.35D, 640, HIGH);
		new PRICE(264, (short)0, 5.0D, 640, HIGH);
		new PRICE(265, (short)0, 1.3D, 640, HIGH);
		new PRICE(266, (short)0, 2.0D, 640, HIGH);
		new PRICE(287, (short)0, 0.3D, 640, HIGH);
		new PRICE(30, (short)0, 5.0D, 320, NORMAL);
		new PRICE(288, (short)0, 0.2D, 640, HIGH);
		new PRICE(289, (short)0, 0.2D, 640, HIGH);
		new PRICE(295, (short)0, 0.1D, 640, HIGH);
		new PRICE(296, (short)0, 0.5D, 640, HIGHEST);
		new PRICE(318, (short)0, 0.5D, 640, HIGH);
		new PRICE(334, (short)0, 0.4D, 640, HIGH);
		new PRICE(336, (short)0, 0.3D, 640, HIGH);
		new PRICE(337, (short)0, 0.15D, 320, LOW);
		new PRICE(338, (short)0, 0.3D, 640, HIGHEST);
		new PRICE(353, (short)0, 0.3D, 640, LOWEST);
		new PRICE(344, (short)0, 0.15D, 640, NORMAL);
		new PRICE(351, (short)0, 0.15D, 640, NORMAL);
		new PRICE(351, (short)1, 0.15D, 640, NORMAL);
		new PRICE(351, (short)2, 0.15D, 640, NORMAL);
		new PRICE(351, (short)3, 0.15D, 640, NORMAL);
		new PRICE(351, (short)4, 0.4D, 640, HIGH);
		new PRICE(331, (short)0, 0.4D, 640, HIGH);
		new PRICE(351, (short)5, 0.15D, 640, NORMAL);
		new PRICE(351, (short)6, 0.15D, 640, NORMAL);
		new PRICE(351, (short)7, 0.15D, 640, NORMAL);
		new PRICE(351, (short)8, 0.15D, 640, NORMAL);
		new PRICE(351, (short)9, 0.15D, 640, NORMAL);
		new PRICE(351, (short)10, 0.15D, 640, NORMAL);
		new PRICE(351, (short)11, 0.15D, 640, NORMAL);
		new PRICE(351, (short)12, 0.15D, 640, NORMAL);
		new PRICE(351, (short)13, 0.15D, 640, NORMAL);
		new PRICE(351, (short)14, 0.15D, 640, NORMAL);
		new PRICE(351, (short)15, 0.15D, 640, NORMAL);
		new PRICE(351, (short)15, 0.15D, 640, NORMAL);
		new PRICE(361, (short)0, 0.2D, 640, NORMAL);
		new PRICE(362, (short)0, 0.2D, 640, NORMAL);
		new PRICE(369, (short)0, 1.0D, 640, NORMAL);
		new PRICE(372, (short)0, 0.7D, 640, NORMAL);
		new PRICE(388, (short)0, 7.0D, 640, NORMAL);
		new PRICE(399, (short)0, 20.0D, 640, NORMAL);
		new PRICE(405, (short)0, 0.4D, 640, NORMAL);
		new PRICE(406, (short)0, 0.8D, 640, NORMAL);
		new PRICE(409, (short)0, 2.8D, 640, NORMAL);
		new PRICE(410, (short)0, 2.8D, 640, NORMAL);
		new PRICE(415, (short)0, 0.6D, 640, NORMAL);
		new PRICE(391, (short)0, 0.2D, 640, HIGHEST);
		new PRICE(392, (short)0, 0.2D, 640, HIGHEST);
		new PRICE(394, (short)0, 0.2D, 640, NORMAL);
		new PRICE(363, (short)0, 0.25D, 640, HIGHEST);
		new PRICE(365, (short)0, 0.25D, 640, HIGHEST);
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
			Player p = (Player) sender;
			p.sendMessage(Util.isShoppable(p.getItemInHand(), p.getItemInHand()) + "");
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