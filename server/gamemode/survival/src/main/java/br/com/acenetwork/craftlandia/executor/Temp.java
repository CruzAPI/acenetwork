package br.com.acenetwork.craftlandia.executor;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import br.com.acenetwork.commons.manager.IdData;
import br.com.acenetwork.commons.player.CommonPlayer;
import br.com.acenetwork.commons.player.craft.CraftCommonPlayer;
import br.com.acenetwork.craftlandia.Main;
import br.com.acenetwork.craftlandia.item.CommonRandomItem;
import br.com.acenetwork.craftlandia.item.LegendaryRandomItem;
import br.com.acenetwork.craftlandia.item.NormalRandomItem;
import br.com.acenetwork.craftlandia.item.RareRandomItem;
import br.com.acenetwork.craftlandia.item.VipItem;
import br.com.acenetwork.craftlandia.manager.ItemSpecial;
import br.com.acenetwork.craftlandia.manager.LandEntityData;
import br.com.acenetwork.craftlandia.manager.PRICE;
import br.com.acenetwork.craftlandia.warp.Warp;
import br.com.acenetwork.craftlandia.warp.WarpLand;

public class Temp implements TabExecutor, Listener
{
	private static final double HIGHEST = 200.0D * 20.0D;
	private static final double HIGH = 100.0D * 20.0D;
	private static final double $1KK = 50.0D * 20.0D;
	private static final double LOW = 25.0D * 20.0D;
	private static final double LOWEST = 10.0D * 20.0D;
	private static final double MEME = 5.0D * 20.0D;
	
	public Temp()
	{
		// 200 < 1%  liquidez altíssima
		// 100 < 2% liquidez alta
		// 50 < 4% liquidez média
		// 25 < 7,5% liquidez baixa
		// 10 < 17,5% liquidez baixíssima
		// 5 < 30%  liquidez meme
		
		new PRICE(Material.STONE,				(short) 0, 0.02D,	$1KK);
		new PRICE(Material.DIRT,				(short) 0, 0.01D,	$1KK);
		new PRICE(Material.SAND,				(short) 0, 0.05D,	$1KK);
		new PRICE(Material.SAND,				(short) 1, 0.10D,	$1KK);
		new PRICE(Material.GRAVEL,				(short) 0, 0.20D,	$1KK);
		new PRICE(Material.LOG,					(short) 0, 1.00D,	$1KK);
		new PRICE(Material.LOG,					(short) 1, 1.00D,	$1KK);
		new PRICE(Material.LOG,					(short) 2, 1.00D,	$1KK);
		new PRICE(Material.LOG,					(short) 3, 1.00D,	$1KK);
		new PRICE(Material.LOG_2,				(short) 0, 1.00D,	$1KK);
		new PRICE(Material.LOG_2,				(short) 1, 1.00D,	$1KK);
		new PRICE(Material.SPONGE,				(short) 0, 500.00D, $1KK);
		new PRICE(Material.WOOL,				(short) 0, 1.00D,	$1KK);
		new PRICE(Material.OBSIDIAN,			(short) 0, 1.00D,	$1KK);
		new PRICE(Material.ICE,					(short) 0, 0.20D,	$1KK);
		new PRICE(Material.SNOW_BLOCK,			(short) 0, 0.20D,	$1KK);
		new PRICE(Material.CLAY,				(short) 0, 0.25D,	$1KK);
		new PRICE(Material.PUMPKIN,				(short) 0, 0.50D,	$1KK);
		new PRICE(Material.NETHERRACK,			(short) 0, 0.10D,	$1KK);
		new PRICE(Material.SOUL_SAND,			(short) 0, 1.00D,	$1KK);
		new PRICE(Material.GLOWSTONE,			(short) 0, 1.00D,	$1KK);
		new PRICE(Material.MELON_BLOCK,			(short) 0, 1.00D,	$1KK);
		new PRICE(Material.ENDER_STONE,			(short) 0, 1.00D,	$1KK);
		
		new PRICE(Material.COAL_ORE,			(short) 0, 2.50D,	$1KK);
		new PRICE(Material.IRON_ORE,			(short) 0, 5.00D,	$1KK);
		new PRICE(Material.GOLD_ORE,			(short) 0, 25.00D,	$1KK);
		new PRICE(Material.REDSTONE_ORE,		(short) 0, 10.00D,	$1KK);
		new PRICE(Material.LAPIS_ORE,			(short) 0, 10.00D,	$1KK);
		new PRICE(Material.DIAMOND_ORE,			(short) 0, 100.00D,	$1KK);
		new PRICE(Material.EMERALD_ORE,			(short) 0, 500.00D,	$1KK);
		new PRICE(Material.QUARTZ_ORE,			(short) 0, 5.00D,	$1KK);
		new PRICE(Material.COAL,				(short) 0, 0.25D,	$1KK);
		new PRICE(Material.IRON_INGOT,			(short) 0, 0.50D,	$1KK);
		new PRICE(Material.GOLD_INGOT,			(short) 0, 1.00D,	$1KK);
		new PRICE(Material.REDSTONE,			(short) 0, 1.00D,	$1KK);
		new PRICE(Material.INK_SACK,			(short) 4, 1.00D,	$1KK); //LAPIS_LAZULI
		new PRICE(Material.DIAMOND,				(short) 0, 10.00D,	$1KK);
		new PRICE(Material.EMERALD,				(short) 0, 1.00D,	$1KK);
		new PRICE(Material.QUARTZ,				(short) 0, 2.00D,	$1KK);
		
		new PRICE(Material.LEAVES,				(short) 0, 0.10D,	$1KK);
		new PRICE(Material.LEAVES,				(short) 1, 0.10D,	$1KK);
		new PRICE(Material.LEAVES,				(short) 2, 0.10D,	$1KK);
		new PRICE(Material.LEAVES,				(short) 3, 0.10D,	$1KK);
		new PRICE(Material.LEAVES_2,			(short) 0, 0.10D,	$1KK);
		new PRICE(Material.LEAVES_2,			(short) 1, 0.10D,	$1KK);
		
		new PRICE(Material.LONG_GRASS,			(short) 1, 0.10D,	$1KK);
		new PRICE(Material.LONG_GRASS,			(short) 2, 0.10D,	$1KK);
		new PRICE(Material.DEAD_BUSH,			(short) 0, 0.10D,	$1KK);
		
		new PRICE(Material.RED_MUSHROOM,		(short) 0, 1.00D,	$1KK);
		new PRICE(Material.BROWN_MUSHROOM,		(short) 0, 1.00D,	$1KK);
		
		new PRICE(Material.CACTUS,				(short) 0, 1.00D,	$1KK);
		
		new PRICE(Material.YELLOW_FLOWER,		(short) 0, 1.00D,	$1KK);
		new PRICE(Material.RED_ROSE,			(short) 0, 1.00D,	$1KK);
		
		new PRICE(Material.RED_ROSE,			(short) 1, 1.00D,	$1KK);
		new PRICE(Material.RED_ROSE,			(short) 2, 1.00D,	$1KK);
		new PRICE(Material.RED_ROSE,			(short) 3, 1.00D,	$1KK);
		new PRICE(Material.RED_ROSE,			(short) 4, 1.00D,	$1KK);
		new PRICE(Material.RED_ROSE,			(short) 5, 1.00D,	$1KK);
		new PRICE(Material.RED_ROSE,			(short) 6, 1.00D,	$1KK);
		new PRICE(Material.RED_ROSE,			(short) 7, 1.00D,	$1KK);
		new PRICE(Material.RED_ROSE,			(short) 8, 1.00D,	$1KK);
		
		new PRICE(Material.DOUBLE_PLANT,		(short) 0, 0.50D,	$1KK);
		new PRICE(Material.DOUBLE_PLANT,		(short) 1, 0.50D,	$1KK);
		new PRICE(Material.DOUBLE_PLANT,		(short) 2, 0.50D,	$1KK);
		new PRICE(Material.DOUBLE_PLANT,		(short) 3, 0.50D,	$1KK);
		new PRICE(Material.DOUBLE_PLANT,		(short) 4, 0.50D,	$1KK);
		new PRICE(Material.DOUBLE_PLANT,		(short) 5, 0.50D,	$1KK);
		
		new PRICE(Material.VINE,				(short) 0, 0.10D,	$1KK);
		new PRICE(Material.WATER_LILY,			(short) 0, 1.00D,	$1KK);
		
		new PRICE(Material.SKULL_ITEM,			(short) 0, 1000D,	$1KK);
		new PRICE(Material.SKULL_ITEM,			(short) 1, 100.D,	$1KK); //WITHER
		new PRICE(Material.SKULL_ITEM,			(short) 2, 1000D,	$1KK);
//		new PRICE(Material.SKULL_ITEM,			(short) 3, 1000D,	$1KK); //PLAYER
		new PRICE(Material.SKULL_ITEM,			(short) 4, 1000D,	$1KK);
		
		new PRICE(Material.SADDLE,				(short) 0, 50.0D,	$1KK);
		new PRICE(Material.IRON_BARDING,		(short) 0, 250D,	$1KK);
		new PRICE(Material.GOLD_BARDING,		(short) 0, 500D,	$1KK);
		new PRICE(Material.DIAMOND_BARDING,		(short) 0, 1000D,	$1KK);
		new PRICE(Material.NAME_TAG,			(short) 0, 50.0D,	$1KK);
		new PRICE(Material.RECORD_12,			(short) 0, 1.00D,	$1KK);
		new PRICE(Material.RECORD_11,			(short) 0, 1.00D,	$1KK);
		new PRICE(Material.RECORD_10,			(short) 0, 1.00D,	$1KK);
		new PRICE(Material.RECORD_9,			(short) 0, 1.00D,	$1KK);
		new PRICE(Material.RECORD_8,			(short) 0, 1.00D,	$1KK);
		new PRICE(Material.RECORD_7,			(short) 0, 1.00D,	$1KK);
		new PRICE(Material.RECORD_6,			(short) 0, 1.00D,	$1KK);
		new PRICE(Material.RECORD_5,			(short) 0, 1.00D,	$1KK);
		new PRICE(Material.RECORD_4,			(short) 0, 1.00D,	$1KK);
		new PRICE(Material.RECORD_3,			(short) 0, 1.00D,	$1KK);
		new PRICE(Material.GREEN_RECORD,		(short) 0, 1.00D,	$1KK);
		new PRICE(Material.GOLD_RECORD,			(short) 0, 1.00D,	$1KK);
		
		new PRICE(Material.BONE,				(short) 0, 1.00D,	$1KK);
		new PRICE(Material.SLIME_BALL,			(short) 0, 2.00D,	$1KK);
		new PRICE(Material.ENDER_PEARL,			(short) 0, 3.00D,	$1KK);
		
		
		new PRICE(Material.PORK,				(short) 0, 0.50D,	$1KK);
		new PRICE(Material.RAW_FISH,			(short) 0, 0.50D,	$1KK);
		new PRICE(Material.RAW_FISH,			(short) 1, 0.50D,	$1KK);
		new PRICE(Material.RAW_FISH,			(short) 2, 0.50D,	$1KK);
		new PRICE(Material.RAW_FISH,			(short) 3, 0.50D,	$1KK);
		new PRICE(Material.RAW_BEEF,			(short) 0, 0.50D,	$1KK);
		new PRICE(Material.RAW_CHICKEN,			(short) 0, 0.50D,	$1KK);
		new PRICE(Material.ROTTEN_FLESH,		(short) 0, 1.00D,	$1KK);
		new PRICE(Material.SPIDER_EYE,			(short) 0, 1.00D,	$1KK);
		new PRICE(Material.CARROT_ITEM,			(short) 0, 1.00D,	$1KK);
		new PRICE(Material.POTATO_ITEM,			(short) 0, 1.00D,	$1KK);
		new PRICE(Material.RABBIT,				(short) 0, 0.50D,	$1KK);
		new PRICE(Material.MUTTON,				(short) 0, 0.50D,	$1KK);
		
		
		
		
		new PRICE(Material.STRING,				(short) 0, 1.00D,	$1KK);
		new PRICE(Material.FEATHER,				(short) 0, 1.00D,	$1KK);
		new PRICE(Material.SULPHUR,				(short) 0, 2.50D,	$1KK);
		new PRICE(Material.WHEAT,				(short) 0, 1.00D,	$1KK);
		new PRICE(Material.LEATHER,				(short) 0, 2.00D,	$1KK);
		new PRICE(Material.SUGAR_CANE,			(short) 0, 1.00D,	$1KK);
		new PRICE(Material.EGG,					(short) 0, 0.25D,	$1KK);
		new PRICE(Material.INK_SACK,			(short) 0, 1.00D,	$1KK);
		new PRICE(Material.INK_SACK,			(short) 3, 1.00D,	$1KK); //COCOA_BEAN
		new PRICE(Material.BLAZE_ROD,			(short) 0, 3.00D,	$1KK);
		new PRICE(Material.GHAST_TEAR,			(short) 0, 5.00D,	$1KK);
		new PRICE(Material.NETHER_STALK,		(short) 0, 1.00D,	$1KK);
		new PRICE(Material.NETHER_STAR,			(short) 0, 5000D,	$1KK);
		new PRICE(Material.PRISMARINE_SHARD,	(short) 0, 2.50D,	$1KK);
		new PRICE(Material.PRISMARINE_CRYSTALS,	(short) 0, 2.50D,	$1KK);
		new PRICE(Material.RABBIT_FOOT,			(short) 0, 1.00D,	$1KK);
		new PRICE(Material.DRAGON_EGG,			(short) 0, 10000D,	$1KK);

		
//		new PRICE(1, (short) 0, 0.3D, 64, LOW);
//		new PRICE(1, (short) 1, 0.3D, 64, LOW);
//		new PRICE(1, (short) 2, 0.3D, 64, LOW);
//		new PRICE(1, (short) 3, 0.3D, 64, LOW);
//		new PRICE(1, (short) 4, 0.3D, 64, LOW);
//		new PRICE(1, (short) 5, 0.3D, 64, LOW);
//		new PRICE(1, (short) 6, 0.3D, 64, LOW);
//		new PRICE(2, (short)0, 0.25D, 640, LOW);
//		new PRICE(3, (short)0, 0.01D, 640, NORMAL);
//		new PRICE(3, (short)1, 0.01D, 640, NORMAL);
//		new PRICE(3, (short)2, 0.01D, 640, NORMAL);
//		new PRICE(4, (short)0, 0.03D, 640, NORMAL);
//		new PRICE(5, (short)0, 0.1D, 640, HIGHEST);
//		new PRICE(5, (short)1, 0.1D, 640, HIGHEST);
//		new PRICE(5, (short)2, 0.1D, 640, HIGHEST);
//		new PRICE(5, (short)3, 0.1D, 640, HIGHEST);
//		new PRICE(5, (short)4, 0.1D, 640, HIGHEST);
//		new PRICE(5, (short)5, 0.1D, 640, HIGHEST);
//		new PRICE(6, (short)0, 0.1D, 640, LOW);
//		new PRICE(6, (short)1, 0.1D, 640, LOW);
//		new PRICE(6, (short)2, 0.1D, 640, LOW);
//		new PRICE(6, (short)3, 0.1D, 640, LOW);
//		new PRICE(6, (short)4, 0.1D, 640, LOW);
//		new PRICE(6, (short)5, 0.1D, 640, LOW);
//		new PRICE(12, (short)0, 0.04D, 320, NORMAL);
//		new PRICE(12, (short)1, 0.04D, 320, NORMAL);
//		new PRICE(13, (short)0, 0.04D, 320, NORMAL);
//		new PRICE(14, (short)0, 1.5D, 128, LOW);
//		new PRICE(15, (short)0, 1.0D, 128, LOW);
//		new PRICE(16, (short)0, 1.5D, 128, LOW);
//		new PRICE(17, (short)0, 0.4D, 128, NORMAL);
//		new PRICE(17, (short)1, 0.4D, 128, NORMAL);
//		new PRICE(17, (short)2, 0.4D, 128, NORMAL);
//		new PRICE(17, (short)3, 0.4D, 128, NORMAL);
//		new PRICE(18, (short)0, 0.03D, 128, NORMAL);
//		new PRICE(18, (short)1, 0.03D, 128, NORMAL);
//		new PRICE(18, (short)2, 0.03D, 128, NORMAL);
//		new PRICE(18, (short)3, 0.03D, 128, NORMAL);
//		new PRICE(162, (short)0, 0.4D, 128, NORMAL);
//		new PRICE(162, (short)1, 0.4D, 128, NORMAL);
//		new PRICE(19, (short)0, 50.0D, 4, LOWEST);
//		new PRICE(20, (short)0, 0.15D, 192, NORMAL);
//		new PRICE(21, (short)0, 1.2D, 128, LOW);
//		new PRICE(22, (short)0, 0.5D, 128, NORMAL);
//		new PRICE(24, (short)0, 0.16D, 128, MEME);
//		new PRICE(24, (short)0, 0.16D, 128, MEME);
//		new PRICE(24, (short)0, 0.16D, 128, MEME);
//		new PRICE(35, (short)0, 0.25D, 320, NORMAL);
//		new PRICE(35, (short)1, 0.25D, 320, NORMAL);
//		new PRICE(35, (short)2, 0.25D, 320, NORMAL);
//		new PRICE(35, (short)3, 0.25D, 320, NORMAL);
//		new PRICE(35, (short)4, 0.25D, 320, NORMAL);
//		new PRICE(35, (short)5, 0.25D, 320, NORMAL);
//		new PRICE(35, (short)6, 0.25D, 320, NORMAL);
//		new PRICE(35, (short)7, 0.25D, 320, NORMAL);
//		new PRICE(35, (short)8, 0.25D, 320, NORMAL);
//		new PRICE(35, (short)9, 0.25D, 320, NORMAL);
//		new PRICE(35, (short)10, 0.25D, 320, NORMAL);
//		new PRICE(35, (short)11, 0.25D, 320, NORMAL);
//		new PRICE(35, (short)12, 0.25D, 320, NORMAL);
//		new PRICE(35, (short)13, 0.25D, 320, NORMAL);
//		new PRICE(35, (short)14, 0.25D, 320, NORMAL);
//		new PRICE(35, (short)15, 0.25D, 320, NORMAL);
//		new PRICE(45, (short)0, 0.5D, 320, NORMAL);
//		new PRICE(47, (short)0, 0.6D, 128, HIGHEST);
//		new PRICE(48, (short)0, 2.0D, 256, LOWEST);
//		new PRICE(49, (short)0, 1.2D, 256, LOWEST);
//		new PRICE(56, (short)0, 3.0D, 256, LOWEST);
//		new PRICE(73, (short)0, 0.7D, 256, LOW);
//		new PRICE(79, (short)0, 0.2D, 256, NORMAL);
//		new PRICE(80, (short)0, 0.15D, 256, NORMAL);
//		new PRICE(86, (short)0, 0.05D, 640, HIGH);
//		new PRICE(87, (short)0, 0.03D, 640, HIGHEST);
//		new PRICE(88, (short)0, 0.05D, 640, HIGHEST);
//		new PRICE(348, (short)0, 0.25D, 640, HIGHEST);
//		new PRICE(112, (short)0, 0.15D, 640, HIGHEST);
//		new PRICE(121, (short)0, 1.0D, 640, NORMAL);
//		new PRICE(129, (short)0, 5.0D, 640, NORMAL);
//		new PRICE(133, (short)0, 1.2D, 640, NORMAL);
//		new PRICE(153, (short)0, 0.2D, 640, NORMAL);
//		new PRICE(168, (short)0, 1.0D, 640, NORMAL);
//		new PRICE(168, (short)1, 1.0D, 640, NORMAL);
//		new PRICE(168, (short)2, 1.0D, 640, NORMAL);
//		new PRICE(174, (short)0, 1.0D, 640, LOW);
//		new PRICE(37, (short)0, 0.04D, 128, HIGHEST);
//		new PRICE(38, (short)0, 0.04D, 128, HIGHEST);
//		new PRICE(38, (short)1, 0.04D, 128, HIGHEST);
//		new PRICE(38, (short)2, 0.04D, 128, HIGHEST);
//		new PRICE(38, (short)3, 0.04D, 128, HIGHEST);
//		new PRICE(38, (short)4, 0.04D, 128, HIGHEST);
//		new PRICE(38, (short)5, 0.04D, 128, HIGHEST);
//		new PRICE(38, (short)6, 0.04D, 128, HIGHEST);
//		new PRICE(38, (short)7, 0.04D, 128, HIGHEST);
//		new PRICE(38, (short)8, 0.04D, 128, HIGHEST);
//		new PRICE(39, (short)0, 0.3D, 128, NORMAL);
//		new PRICE(40, (short)0, 0.3D, 128, NORMAL);
//		new PRICE(81, (short)0, 0.5D, 640, NORMAL);
//		new PRICE(175, (short)0, 0.04D, 640, NORMAL);
//		new PRICE(175, (short)1, 0.04D, 640, NORMAL);
//		new PRICE(175, (short)2, 0.04D, 640, NORMAL);
//		new PRICE(175, (short)3, 0.04D, 640, NORMAL);
//		new PRICE(175, (short)4, 0.04D, 640, NORMAL);
//		new PRICE(175, (short)5, 0.04D, 640, NORMAL);
//		new PRICE(46, (short)0, 0.5D, 128, LOWEST);
//		new PRICE(138, (short)0, 0.5D, 640, NORMAL);
//		new PRICE(332, (short)0, 0.02D, 320, NORMAL);
//		new PRICE(340, (short)0, 0.1D, 640, NORMAL);
//		new PRICE(339, (short)0, 0.05D, 640, NORMAL);
//		new PRICE(341, (short)0, 0.8D, 640, NORMAL);
//		new PRICE(352, (short)0, 0.4D, 640, NORMAL);
//		new PRICE(368, (short)0, 0.4D, 640, NORMAL);
//		new PRICE(381, (short)0, 1.25D, 640, NORMAL);
//		new PRICE(385, (short)0, 2.0D, 128, LOW);
//		new PRICE(402, (short)0, 1.2D, 64, MEME);
//		new PRICE(260, (short)0, 0.5D, 640, NORMAL);
//		new PRICE(282, (short)0, 1.3D, 128, NORMAL);
//		new PRICE(319, (short)0, 0.1D, 128, NORMAL);
//		new PRICE(322, (short)0, 1.5D, 128, MEME);
//		new PRICE(322, (short)1, 5.0D, 128, MEME);
//		new PRICE(349, (short)0, 0.2D, 320, NORMAL);
//		new PRICE(349, (short)1, 0.2D, 320, NORMAL);
//		new PRICE(349, (short)2, 0.2D, 320, NORMAL);
//		new PRICE(349, (short)3, 0.2D, 320, NORMAL);
//		new PRICE(349, (short)3, 0.2D, 320, NORMAL);
//		new PRICE(263, (short)0, 0.7D, 640, HIGH);
//		new PRICE(263, (short)1, 0.35D, 640, HIGH);
//		new PRICE(264, (short)0, 5.0D, 640, HIGH);
//		new PRICE(265, (short)0, 1.3D, 640, HIGH);
//		new PRICE(266, (short)0, 2.0D, 640, HIGH);
//		new PRICE(287, (short)0, 0.3D, 640, HIGH);
//		new PRICE(30, (short)0, 5.0D, 320, NORMAL);
//		new PRICE(288, (short)0, 0.2D, 640, HIGH);
//		new PRICE(289, (short)0, 0.2D, 640, HIGH);
//		new PRICE(295, (short)0, 0.1D, 640, HIGH);
//		new PRICE(296, (short)0, 0.5D, 640, HIGHEST);
//		new PRICE(318, (short)0, 0.5D, 640, HIGH);
//		new PRICE(334, (short)0, 0.4D, 640, HIGH);
//		new PRICE(336, (short)0, 0.3D, 640, HIGH);
//		new PRICE(337, (short)0, 0.15D, 320, LOW);
//		new PRICE(338, (short)0, 0.3D, 640, HIGHEST);
//		new PRICE(353, (short)0, 0.3D, 640, LOWEST);
//		new PRICE(344, (short)0, 0.15D, 640, NORMAL);
//		new PRICE(351, (short)0, 0.15D, 640, NORMAL);
//		new PRICE(351, (short)1, 0.15D, 640, NORMAL);
//		new PRICE(351, (short)2, 0.15D, 640, NORMAL);
//		new PRICE(351, (short)3, 0.15D, 640, NORMAL);
//		new PRICE(351, (short)4, 0.4D, 640, HIGH);
//		new PRICE(331, (short)0, 0.4D, 640, HIGH);
//		new PRICE(351, (short)5, 0.15D, 640, NORMAL);
//		new PRICE(351, (short)6, 0.15D, 640, NORMAL);
//		new PRICE(351, (short)7, 0.15D, 640, NORMAL);
//		new PRICE(351, (short)8, 0.15D, 640, NORMAL);
//		new PRICE(351, (short)9, 0.15D, 640, NORMAL);
//		new PRICE(351, (short)10, 0.15D, 640, NORMAL);
//		new PRICE(351, (short)11, 0.15D, 640, NORMAL);
//		new PRICE(351, (short)12, 0.15D, 640, NORMAL);
//		new PRICE(351, (short)13, 0.15D, 640, NORMAL);
//		new PRICE(351, (short)14, 0.15D, 640, NORMAL);
//		new PRICE(351, (short)15, 0.15D, 640, NORMAL);
//		new PRICE(351, (short)15, 0.15D, 640, NORMAL);
//		new PRICE(361, (short)0, 0.2D, 640, NORMAL);
//		new PRICE(362, (short)0, 0.2D, 640, NORMAL);
//		new PRICE(369, (short)0, 1.0D, 640, NORMAL);
//		new PRICE(372, (short)0, 0.7D, 640, NORMAL);
//		new PRICE(388, (short)0, 7.0D, 640, NORMAL);
//		new PRICE(399, (short)0, 20.0D, 640, NORMAL);
//		new PRICE(405, (short)0, 0.4D, 640, NORMAL);
//		new PRICE(406, (short)0, 0.8D, 640, NORMAL);
//		new PRICE(409, (short)0, 2.8D, 640, NORMAL);
//		new PRICE(410, (short)0, 2.8D, 640, NORMAL);
//		new PRICE(415, (short)0, 0.6D, 640, NORMAL);
//		new PRICE(391, (short)0, 0.2D, 640, HIGHEST);
//		new PRICE(392, (short)0, 0.2D, 640, HIGHEST);
//		new PRICE(394, (short)0, 0.2D, 640, NORMAL);
//		new PRICE(363, (short)0, 0.25D, 640, HIGHEST);
//		new PRICE(365, (short)0, 0.25D, 640, HIGHEST);
		
		Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String aliases, String[] args)
	{
		return new ArrayList<>();
	}

	@EventHandler
	public void a(PlayerInteractEntityEvent e)
	{
		Player p = e.getPlayer();
		
		CommonPlayer cp = CraftCommonPlayer.get(p);
		
		if(!cp.hasPermission("cmd.temp") || temp != p)
		{
			return;
		}
		
		WarpLand warpLand = Warp.getInstance(WarpLand.class);
		
		LandEntityData data = warpLand.map.get(e.getRightClicked().getUniqueId());
		p.sendMessage("" + data);
	}
	
	Player temp;
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String aliases, String[] args)
	{
		if(sender instanceof Player)
		{
			Player p = (Player) sender;
			CommonPlayer cp = CraftCommonPlayer.get(p);
			
			if(!cp.hasPermission("cmd.temp"))
			{
				return true;
			}
			
			temp = temp == null ? p : null;
			
//			
//			ItemStack item = p.getItemInHand();
//			
//			ResourceBundle bundle = ResourceBundle.getBundle("message", cp.getLocale());
//			
//			Jackpot.getInstance().setJackpotTotal(1000000.0d);
			if(args.length == 1 && args[0].equalsIgnoreCase("vip"))
			{
				p.setItemInHand(ItemSpecial.getInstance(VipItem.class).getItemStack(null));
			}
			else if(args.length == 1)
			{
				try
				{
					Difficulty diff = Difficulty.getByValue(Integer.valueOf(args[0]));
					
					p.getWorld().setDifficulty(diff);
					p.sendMessage(p.getWorld().getName() + " difficulty set to " + diff.name());
				}
				catch(Exception e)
				{
					Player t = Bukkit.getPlayer(args[0]);
					
					if(t != null)
					{
						t.spigot().respawn();
						p.sendMessage(t.getName() + " respawned!");
					}
				}
			}
			else if(args.length == 2 && args[0].equalsIgnoreCase("random"))
			{
				switch(args[1])
				{
				case "1":
					p.setItemInHand(ItemSpecial.getInstance(CommonRandomItem.class).getItemStack(null));
					break;
				case "2":
					p.setItemInHand(ItemSpecial.getInstance(RareRandomItem.class).getItemStack(null));
					break;
				case "3":
					p.setItemInHand(ItemSpecial.getInstance(LegendaryRandomItem.class).getItemStack(null));
					break;
				default:
					p.setItemInHand(ItemSpecial.getInstance(NormalRandomItem.class).getItemStack(null));
					break;
				}
			}
//			p.sendMessage(aliases);
			
//			p.sendMessage(CommonsUtil.getEnchants(item).toString());
			
//			p.sendMessage(item.getEnchantmentLevel(Enchantment.getById(Integer.valueOf(args[0])))	 + "");
			
			
//			if(args.length == 2)
//			{
//				ItemStack item = p.getItemInHand();
//				
//				item.addUnsafeEnchantment(Enchantment.getById(Integer.valueOf(args[0])), Integer.valueOf(args[1]));
//				
//				p.setItemInHand(item);
//			}
//			p.sendMessage(((Repairable) p.getItemInHand().getItemMeta()).getRepairCost() + " repairCost");
//			p.sendMessage(((WarpLand) Warp.MAP.get(p.getWorld().getUID())).map.keySet().size() + "");
//			p.sendMessage(Playtime.getInstance().getItemMap().toString());
			
//			p.sendMessage("version = " + ProtocolLibrary.getProtocolManager().getProtocolVersion(p));
//			sender.sendMessage("" + p.getWorld().getName() + " chunks in memory = " + Warp.MAP.get(p.getWorld().getUID()).blockData.size());
//			sender.sendMessage("Unknown command. Type \"/help\" for help.");
			return true;
		}
		
		return true;
	}
}