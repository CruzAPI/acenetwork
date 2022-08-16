package br.com.acenetwork.craftlandia.executor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.google.common.io.ByteStreams;

import br.com.acenetwork.commons.CommonsUtil;
import br.com.acenetwork.commons.executor.Balance;
import br.com.acenetwork.commons.manager.IdData;
import br.com.acenetwork.commons.manager.Message;
import br.com.acenetwork.commons.player.craft.CraftCommonPlayer;
import br.com.acenetwork.craftlandia.manager.Config;
import br.com.acenetwork.craftlandia.manager.CryptoInfo;
import br.com.acenetwork.craftlandia.manager.PRICE;
import br.com.acenetwork.craftlandia.manager.Config.Type;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

public class Price implements TabExecutor
{
	private Map<IdData, CryptoInfo> map = new HashMap<>();
	private static Price instance;
	private final Map<IdData, Map<IdData, Double>> elementMap = new HashMap<>();
	
	@SuppressWarnings("unchecked")
	public Price()
	{
		instance = this;
		
		File file = Config.getFile(Type.PRICE, false);
		
		if(!file.exists() || file.length() == 0L)
		{
			for(PRICE price : PRICE.LIST)
			{
				map.put(new IdData(price.id, price.data), new CryptoInfo(price.marketCap, price.circulatingSupply));
			}
			
			PRICE.LIST = null;
		}
		else
		{
			try(FileInputStream fileIn = new FileInputStream(file);
					ByteArrayInputStream streamIn = new ByteArrayInputStream(ByteStreams.toByteArray(fileIn));
					ObjectInputStream in = new ObjectInputStream(streamIn))
			{
				map = (Map<IdData, CryptoInfo>) in.readObject();
			}
			catch(ClassNotFoundException | IOException e)
			{
				throw new RuntimeException(e);
			}
		}
		
		//primitives
		
//		new IdData(Material.STONE, (short) 0);
//		new IdData(Material.DIRT, (short) 0);
//		new IdData(Material.SAND, (short) 0);
//		new IdData(Material.SAND, (short) 1);
//		new IdData(Material.GRAVEL, (short) 0);
//		new IdData(Material.LOG, (short) 0);
//		new IdData(Material.LOG, (short) 1);
//		new IdData(Material.LOG, (short) 2);
//		new IdData(Material.LOG, (short) 3);
//		new IdData(Material.LOG_2, (short) 0);
//		new IdData(Material.LOG_2, (short) 1);
//		new IdData(Material.SPONGE, (short) 0);
//		new IdData(Material.WOOL, (short) 0);
//		new IdData(Material.OBSIDIAN, (short) 0);
//		new IdData(Material.ICE, (short) 0);
//		new IdData(Material.SNOW_BLOCK, (short) 0);
//		new IdData(Material.CLAY, (short) 0);
//		new IdData(Material.PUMPKIN, (short) 0);
//		new IdData(Material.NETHERRACK, (short) 0);
//		new IdData(Material.SOUL_SAND, (short) 0);
//		new IdData(Material.GLOWSTONE, (short) 0);
//		new IdData(Material.MELON_BLOCK, (short) 0);
//		new IdData(Material.ENDER_STONE, (short) 0);
//		
//		new IdData(Material.COAL_ORE, (short) 0);
//		new IdData(Material.IRON_ORE, (short) 0);
//		new IdData(Material.GOLD_ORE, (short) 0);
//		new IdData(Material.REDSTONE_ORE, (short) 0);
//		new IdData(Material.LAPIS_ORE, (short) 0);
//		new IdData(Material.DIAMOND_ORE, (short) 0);
//		new IdData(Material.EMERALD_ORE, (short) 0);
//		new IdData(Material.QUARTZ_ORE, (short) 0);
//		
//		new IdData(Material.LEAVES, (short) 0);
//		new IdData(Material.LEAVES, (short) 1);
//		new IdData(Material.LEAVES, (short) 2);
//		new IdData(Material.LEAVES, (short) 3);
//		new IdData(Material.LEAVES_2, (short) 0);
//		new IdData(Material.LEAVES_2, (short) 1);
//		
//		new IdData(Material.LONG_GRASS, (short) 1);
//		new IdData(Material.LONG_GRASS, (short) 2);
//		new IdData(Material.DEAD_BUSH, (short) 0);
//		
//		new IdData(Material.RED_MUSHROOM, (short) 0);
//		new IdData(Material.BROWN_MUSHROOM, (short) 0);
//		
//		new IdData(Material.CACTUS, (short) 0);
//		
//		new IdData(Material.YELLOW_FLOWER, (short) 0);
//		new IdData(Material.RED_ROSE, (short) 0);
//		
//		new IdData(Material.RED_ROSE, (short) 1);
//		new IdData(Material.RED_ROSE, (short) 2);
//		new IdData(Material.RED_ROSE, (short) 3);
//		new IdData(Material.RED_ROSE, (short) 4);
//		new IdData(Material.RED_ROSE, (short) 5);
//		new IdData(Material.RED_ROSE, (short) 6);
//		new IdData(Material.RED_ROSE, (short) 7);
//		new IdData(Material.RED_ROSE, (short) 8);
//		
//		new IdData(Material.DOUBLE_PLANT, (short) 0);
//		new IdData(Material.DOUBLE_PLANT, (short) 1);
//		new IdData(Material.DOUBLE_PLANT, (short) 2);
//		new IdData(Material.DOUBLE_PLANT, (short) 3);
//		new IdData(Material.DOUBLE_PLANT, (short) 4);
//		new IdData(Material.DOUBLE_PLANT, (short) 5);
//		
//		new IdData(Material.VINE, (short) 0);
//		new IdData(Material.WATER_LILY, (short) 0);
//		
//		new IdData(Material.SKULL_ITEM, (short) 0);
//		new IdData(Material.SKULL_ITEM, (short) 1);
//		new IdData(Material.SKULL_ITEM, (short) 2);
//		new IdData(Material.SKULL_ITEM, (short) 3);
//		new IdData(Material.SKULL_ITEM, (short) 4);
//		
//		new IdData(Material.SADDLE, (short) 0);
//		new IdData(Material.IRON_BARDING, (short) 0);
//		new IdData(Material.GOLD_BARDING, (short) 0);
//		new IdData(Material.DIAMOND_BARDING, (short) 0);
//		new IdData(Material.NAME_TAG, (short) 0);
//		new IdData(Material.RECORD_12, (short) 0);
//		new IdData(Material.RECORD_11, (short) 0);
//		new IdData(Material.RECORD_10, (short) 0);
//		new IdData(Material.RECORD_9, (short) 0);
//		new IdData(Material.RECORD_8, (short) 0);
//		new IdData(Material.RECORD_7, (short) 0);
//		new IdData(Material.RECORD_6, (short) 0);
//		new IdData(Material.RECORD_5, (short) 0);
//		new IdData(Material.RECORD_4, (short) 0);
//		new IdData(Material.RECORD_3, (short) 0);
//		new IdData(Material.GREEN_RECORD, (short) 0);
//		new IdData(Material.GOLD_RECORD, (short) 0);
//		
//		new IdData(Material.SLIME_BALL, (short) 0);
//		new IdData(Material.BONE, (short) 0);
//		new IdData(Material.ENDER_PEARL, (short) 0);
//		
//		
//		new IdData(Material.PORK, (short) 0);
//		new IdData(Material.RAW_FISH, (short) 0);
//		new IdData(Material.RAW_FISH, (short) 1);
//		new IdData(Material.RAW_FISH, (short) 2);
//		new IdData(Material.RAW_FISH, (short) 3);
//		new IdData(Material.RAW_BEEF, (short) 0);
//		new IdData(Material.RAW_CHICKEN, (short) 0);
//		new IdData(Material.ROTTEN_FLESH, (short) 0);
//		new IdData(Material.SPIDER_EYE, (short) 0);
//		new IdData(Material.CARROT_ITEM, (short) 0);
//		new IdData(Material.POTATO_ITEM, (short) 0);
//		new IdData(Material.RABBIT, (short) 0);
//		new IdData(Material.MUTTON, (short) 0);
//		
//		
//		new IdData(Material.COAL, (short) 0);
//		new IdData(Material.IRON_INGOT, (short) 0);
//		new IdData(Material.GOLD_INGOT, (short) 0);
//		new IdData(Material.REDSTONE, (short) 0);
//		new IdData(Material.INK_SACK, (short) 4); //LAPIZ_LAZULI
//		new IdData(Material.DIAMOND, (short) 0);
//		new IdData(Material.EMERALD, (short) 0);
//		new IdData(Material.QUARTZ, (short) 0);
//		
//		new IdData(Material.STRING, (short) 0);
//		new IdData(Material.FEATHER, (short) 0);
//		new IdData(Material.SULPHUR, (short) 0);
//		new IdData(Material.SEEDS, (short) 0);
//		new IdData(Material.WHEAT, (short) 0);
//		new IdData(Material.LEATHER, (short) 0);
//		new IdData(Material.SUGAR, (short) 0);
//		new IdData(Material.EGG, (short) 0);
//		new IdData(Material.INK_SACK, (short) 0);
//		new IdData(Material.INK_SACK, (short) 3); //COCOA_BEAN
//		new IdData(Material.BLAZE_ROD, (short) 0);
//		new IdData(Material.GHAST_TEAR, (short) 0);
//		new IdData(Material.NETHER_STALK, (short) 0);
//		new IdData(Material.NETHER_STAR, (short) 0);
//		new IdData(Material.PRISMARINE_SHARD, (short) 0);
//		new IdData(Material.PRISMARINE_CRYSTALS, (short) 0);
//		new IdData(Material.RABBIT_HIDE, (short) 0);
//		new IdData(Material.RABBIT_FOOT, (short) 0);
		
		//elements
		
		Map<IdData, Double> tempMap;
		
		elementMap.put(new IdData(Material.INK_SACK, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.STONE, (short) 0), 4.0D);
		
		
		
		elementMap.put(new IdData(Material.STONE, (short) 1), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.STONE, (short) 0), 4.0D);
		
		elementMap.put(new IdData(Material.STONE, (short) 2), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.STONE, (short) 0), 4.0D);
		
		elementMap.put(new IdData(Material.STONE, (short) 3), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.STONE, (short) 0), 4.0D);
		
		elementMap.put(new IdData(Material.STONE, (short) 4), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.STONE, (short) 0), 4.0D);
		
		elementMap.put(new IdData(Material.STONE, (short) 5), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.STONE, (short) 0), 4.0D);
		
		elementMap.put(new IdData(Material.STONE, (short) 6), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.STONE, (short) 0), 4.0D);
		
		elementMap.put(new IdData(Material.GRASS, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.DIRT, (short) 0), 4.0D);
		
		elementMap.put(new IdData(Material.DIRT, (short) 1), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.DIRT, (short) 0), 0.5D);
		tempMap.put(new IdData(Material.GRAVEL, (short) 0), 0.5D);
		
		elementMap.put(new IdData(Material.DIRT, (short) 2), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.DIRT, (short) 0), 16.0D);
		
		elementMap.put(new IdData(Material.COBBLESTONE, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.STONE, (short) 0), 0.5D);
		
		elementMap.put(new IdData(Material.WOOD, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.LOG, (short) 0), 0.25D);
		
		elementMap.put(new IdData(Material.WOOD, (short) 1), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.LOG, (short) 1), 0.25D);
		
		elementMap.put(new IdData(Material.WOOD, (short) 2), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.LOG, (short) 2), 0.25D);
		
		elementMap.put(new IdData(Material.WOOD, (short) 3), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.LOG, (short) 3), 0.25D);
		
		elementMap.put(new IdData(Material.WOOD, (short) 4), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.LOG_2, (short) 0), 0.25D);
		
		elementMap.put(new IdData(Material.WOOD, (short) 5), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.LOG_2, (short) 0), 0.25D);
		
		elementMap.put(new IdData(Material.SPONGE, (short) 1), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.SPONGE, (short) 0), 1.0D);
		
		elementMap.put(new IdData(Material.GLASS, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.SAND, (short) 0), 1.0D);
		tempMap.put(new IdData(Material.SAND, (short) 1), 1.0D);
		
		elementMap.put(new IdData(Material.LAPIS_BLOCK, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.INK_SACK, (short) 4), 9.0D);
		
		elementMap.put(new IdData(Material.SANDSTONE, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.SAND, (short) 0), 4.0D);
		
		elementMap.put(new IdData(Material.SANDSTONE, (short) 1), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.SAND, (short) 0), 4.0D);
		
		elementMap.put(new IdData(Material.SANDSTONE, (short) 2), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.SAND, (short) 0), 4.0D);

		elementMap.put(new IdData(Material.WOOL, (short) 1), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.WOOL, (short) 0), 1.0D);
		tempMap.put(new IdData(Material.INK_SACK, (short) 14), 1.0D);

		elementMap.put(new IdData(Material.WOOL, (short) 2), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.WOOL, (short) 0), 1.0D);
		tempMap.put(new IdData(Material.INK_SACK, (short) 13), 1.0D);

		elementMap.put(new IdData(Material.WOOL, (short) 3), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.WOOL, (short) 0), 1.0D);
		tempMap.put(new IdData(Material.INK_SACK, (short) 12), 1.0D);

		elementMap.put(new IdData(Material.WOOL, (short) 4), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.WOOL, (short) 0), 1.0D);
		tempMap.put(new IdData(Material.INK_SACK, (short) 11), 1.0D);

		elementMap.put(new IdData(Material.WOOL, (short) 5), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.WOOL, (short) 0), 1.0D);
		tempMap.put(new IdData(Material.INK_SACK, (short) 10), 1.0D);

		elementMap.put(new IdData(Material.WOOL, (short) 6), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.WOOL, (short) 0), 1.0D);
		tempMap.put(new IdData(Material.INK_SACK, (short) 9), 1.0D);

		elementMap.put(new IdData(Material.WOOL, (short) 7), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.WOOL, (short) 0), 1.0D);
		tempMap.put(new IdData(Material.INK_SACK, (short) 8), 1.0D);

		elementMap.put(new IdData(Material.WOOL, (short) 8), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.WOOL, (short) 0), 1.0D);
		tempMap.put(new IdData(Material.INK_SACK, (short) 7), 1.0D);

		elementMap.put(new IdData(Material.WOOL, (short) 9), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.WOOL, (short) 0), 1.0D);
		tempMap.put(new IdData(Material.INK_SACK, (short) 6), 1.0D);

		elementMap.put(new IdData(Material.WOOL, (short) 10), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.WOOL, (short) 0), 1.0D);
		tempMap.put(new IdData(Material.INK_SACK, (short) 5), 1.0D);

		elementMap.put(new IdData(Material.WOOL, (short) 11), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.WOOL, (short) 0), 1.0D);
		tempMap.put(new IdData(Material.INK_SACK, (short) 4), 1.0D);

		elementMap.put(new IdData(Material.WOOL, (short) 12), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.WOOL, (short) 0), 1.0D);
		tempMap.put(new IdData(Material.INK_SACK, (short) 3), 1.0D);

		elementMap.put(new IdData(Material.WOOL, (short) 13), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.WOOL, (short) 0), 1.0D);
		tempMap.put(new IdData(Material.INK_SACK, (short) 2), 1.0D);

		elementMap.put(new IdData(Material.WOOL, (short) 14), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.WOOL, (short) 0), 1.0D);
		tempMap.put(new IdData(Material.INK_SACK, (short) 1), 1.0D);

		elementMap.put(new IdData(Material.WOOL, (short) 15), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.WOOL, (short) 0), 1.0D);
		tempMap.put(new IdData(Material.INK_SACK, (short) 0), 1.0D);
		
		elementMap.put(new IdData(Material.GOLD_BLOCK, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.GOLD_INGOT, (short) 0), 9.0D);
		
		elementMap.put(new IdData(Material.IRON_BLOCK, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.IRON_INGOT, (short) 0), 9.0D);
		
		elementMap.put(new IdData(Material.STEP, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.STONE, (short) 0), 0.5D);
		
		elementMap.put(new IdData(Material.STEP, (short) 1), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.SAND, (short) 0), 0.5D);
		
		elementMap.put(new IdData(Material.STEP, (short) 3), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.COBBLESTONE, (short) 0), 0.5D);
		
		elementMap.put(new IdData(Material.STEP, (short) 4), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.BRICK, (short) 0), 0.5D);
		
		elementMap.put(new IdData(Material.STEP, (short) 5), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.SMOOTH_BRICK, (short) 0), 0.5D);
		
		elementMap.put(new IdData(Material.STEP, (short) 6), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.NETHER_BRICK, (short) 0), 0.5D);
		
		elementMap.put(new IdData(Material.STEP, (short) 7), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.QUARTZ_BLOCK, (short) 0), 0.5D);
		
		elementMap.put(new IdData(Material.BRICK, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.CLAY_BRICK, (short) 0), 4.0D);
		
		elementMap.put(new IdData(Material.BOOKSHELF, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.WOOD, (short) 0), 6.0D);
		tempMap.put(new IdData(Material.BOOK, (short) 0), 3.0D);
		
		elementMap.put(new IdData(Material.MOSSY_COBBLESTONE, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.COBBLESTONE, (short) 0), 1.0D);
		tempMap.put(new IdData(Material.VINE, (short) 0), 1.0D);
		
		elementMap.put(new IdData(Material.OBSIDIAN, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.LAVA, (short) 0), 2.0D);
		
		elementMap.put(new IdData(Material.WOOD_STAIRS, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.WOOD, (short) 0), 1.5D);
		
		elementMap.put(new IdData(Material.SPRUCE_WOOD_STAIRS, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.WOOD, (short) 1), 1.5D);
		
		elementMap.put(new IdData(Material.BIRCH_WOOD_STAIRS, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.WOOD, (short) 2), 1.5D);
		
		elementMap.put(new IdData(Material.JUNGLE_WOOD_STAIRS, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.WOOD, (short) 3), 1.5D);
		
		elementMap.put(new IdData(Material.ACACIA_STAIRS, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.WOOD, (short) 4), 1.5D);
		
		elementMap.put(new IdData(Material.DARK_OAK_STAIRS, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.WOOD, (short) 5), 1.5D);
		
		elementMap.put(new IdData(Material.WOOD_STEP, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.WOOD, (short) 0), 0.5D);
		
		elementMap.put(new IdData(Material.WOOD_STEP, (short) 1), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.WOOD, (short) 1), 0.5D);
		
		elementMap.put(new IdData(Material.WOOD_STEP, (short) 2), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.WOOD, (short) 2), 0.5D);
		
		elementMap.put(new IdData(Material.WOOD_STEP, (short) 3), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.WOOD, (short) 3), 0.5D);
		
		elementMap.put(new IdData(Material.WOOD_STEP, (short) 4), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.WOOD, (short) 4), 0.5D);
		
		elementMap.put(new IdData(Material.WOOD_STEP, (short) 5), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.WOOD, (short) 5), 0.5D);
		
		elementMap.put(new IdData(Material.DIAMOND_BLOCK, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.DIAMOND, (short) 0), 9.0D);
		
		elementMap.put(new IdData(Material.COBBLESTONE_STAIRS, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.COBBLESTONE, (short) 0), 1.5D);
		
		elementMap.put(new IdData(Material.JACK_O_LANTERN, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.PUMPKIN, (short) 0), 1.0D);
		tempMap.put(new IdData(Material.TORCH, (short) 0), 1.0D);
		
		
		elementMap.put(new IdData(Material.STAINED_GLASS, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.GLASS, (short) 0), 1.0D);
		tempMap.put(new IdData(Material.INK_SACK, (short) 15), 1.0D / 8.0D);
		
		elementMap.put(new IdData(Material.STAINED_GLASS, (short) 1), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.GLASS, (short) 0), 1.0D);
		tempMap.put(new IdData(Material.INK_SACK, (short) 14), 1.0D / 8.0D);
		
		elementMap.put(new IdData(Material.STAINED_GLASS, (short) 2), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.GLASS, (short) 0), 1.0D);
		tempMap.put(new IdData(Material.INK_SACK, (short) 13), 1.0D / 8.0D);
		
		elementMap.put(new IdData(Material.STAINED_GLASS, (short) 3), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.GLASS, (short) 0), 1.0D);
		tempMap.put(new IdData(Material.INK_SACK, (short) 12), 1.0D / 8.0D);
		
		elementMap.put(new IdData(Material.STAINED_GLASS, (short) 4), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.GLASS, (short) 0), 1.0D);
		tempMap.put(new IdData(Material.INK_SACK, (short) 11), 1.0D / 8.0D);
		
		elementMap.put(new IdData(Material.STAINED_GLASS, (short) 5), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.GLASS, (short) 0), 1.0D);
		tempMap.put(new IdData(Material.INK_SACK, (short) 10), 1.0D / 8.0D);
		
		elementMap.put(new IdData(Material.STAINED_GLASS, (short) 6), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.GLASS, (short) 0), 1.0D);
		tempMap.put(new IdData(Material.INK_SACK, (short) 9), 1.0D / 8.0D);
		
		elementMap.put(new IdData(Material.STAINED_GLASS, (short) 7), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.GLASS, (short) 0), 1.0D);
		tempMap.put(new IdData(Material.INK_SACK, (short) 8), 1.0D / 8.0D);
		
		elementMap.put(new IdData(Material.STAINED_GLASS, (short) 8), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.GLASS, (short) 0), 1.0D);
		tempMap.put(new IdData(Material.INK_SACK, (short) 7), 1.0D / 8.0D);
		
		elementMap.put(new IdData(Material.STAINED_GLASS, (short) 9), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.GLASS, (short) 0), 1.0D);
		tempMap.put(new IdData(Material.INK_SACK, (short) 6), 1.0D / 8.0D);
		
		elementMap.put(new IdData(Material.STAINED_GLASS, (short) 10), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.GLASS, (short) 0), 1.0D);
		tempMap.put(new IdData(Material.INK_SACK, (short) 5), 1.0D / 8.0D);
		
		elementMap.put(new IdData(Material.STAINED_GLASS, (short) 11), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.GLASS, (short) 0), 1.0D);
		tempMap.put(new IdData(Material.INK_SACK, (short) 4), 1.0D / 8.0D);
		
		elementMap.put(new IdData(Material.STAINED_GLASS, (short) 12), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.GLASS, (short) 0), 1.0D);
		tempMap.put(new IdData(Material.INK_SACK, (short) 3), 1.0D / 8.0D);
		
		elementMap.put(new IdData(Material.STAINED_GLASS, (short) 13), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.GLASS, (short) 0), 1.0D);
		tempMap.put(new IdData(Material.INK_SACK, (short) 2), 1.0D / 8.0D);
		
		elementMap.put(new IdData(Material.STAINED_GLASS, (short) 14), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.GLASS, (short) 0), 1.0D);
		tempMap.put(new IdData(Material.INK_SACK, (short) 1), 1.0D / 8.0D);
		
		elementMap.put(new IdData(Material.STAINED_GLASS, (short) 15), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.GLASS, (short) 0), 1.0D);
		tempMap.put(new IdData(Material.INK_SACK, (short) 0), 1.0D / 8.0D);
		
		elementMap.put(new IdData(Material.SMOOTH_BRICK, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.STONE, (short) 0), 1.0D);
		
		elementMap.put(new IdData(Material.SMOOTH_BRICK, (short) 1), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.STONE, (short) 0), 1.0D);
		tempMap.put(new IdData(Material.VINE, (short) 0), 1.0D);
		
		elementMap.put(new IdData(Material.SMOOTH_BRICK, (short) 2), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.STONE, (short) 0), 1.0D);
		
		elementMap.put(new IdData(Material.SMOOTH_BRICK, (short) 3), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.STONE, (short) 0), 1.0D);
		
		elementMap.put(new IdData(Material.BRICK_STAIRS, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.BRICK, (short) 0), 1.5D);
		
		elementMap.put(new IdData(Material.SMOOTH_STAIRS, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.SMOOTH_BRICK, (short) 0), 1.5D);
		
		elementMap.put(new IdData(Material.MYCEL, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.DIRT, (short) 0), 64.0D);
		
		elementMap.put(new IdData(Material.NETHER_BRICK, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.NETHER_BRICK_ITEM, (short) 0), 4.0D);
		
		elementMap.put(new IdData(Material.NETHER_BRICK_STAIRS, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.NETHER_BRICK, (short) 0), 1.5D);
		
		elementMap.put(new IdData(Material.SANDSTONE_STAIRS, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.SANDSTONE, (short) 0), 1.5D);
		
		elementMap.put(new IdData(Material.EMERALD_BLOCK, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.EMERALD, (short) 0), 9.0D);
		
		elementMap.put(new IdData(Material.COBBLE_WALL, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.COBBLESTONE, (short) 0), 1.0D);
		
		elementMap.put(new IdData(Material.COBBLE_WALL, (short) 1), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.MOSSY_COBBLESTONE, (short) 0), 1.0D);
		
		elementMap.put(new IdData(Material.QUARTZ_BLOCK, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.QUARTZ, (short) 0), 4.0D);
		
		elementMap.put(new IdData(Material.QUARTZ_BLOCK, (short) 1), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.QUARTZ_BLOCK, (short) 0), 1.0D);
		
		elementMap.put(new IdData(Material.QUARTZ_BLOCK, (short) 2), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.QUARTZ_BLOCK, (short) 0), 1.0D);
		
		elementMap.put(new IdData(Material.QUARTZ_STAIRS, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.QUARTZ_BLOCK, (short) 0), 1.5D);
		
		elementMap.put(new IdData(Material.HARD_CLAY, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.CLAY, (short) 0), 2.0D);
		
		elementMap.put(new IdData(Material.STAINED_CLAY, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.HARD_CLAY, (short) 0), 1.0D);
		tempMap.put(new IdData(Material.INK_SACK, (short) 15), 1.0D / 8.0D);
		
		elementMap.put(new IdData(Material.STAINED_CLAY, (short) 1), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.HARD_CLAY, (short) 0), 1.0D);
		tempMap.put(new IdData(Material.INK_SACK, (short) 14), 1.0D / 8.0D);
		
		elementMap.put(new IdData(Material.STAINED_CLAY, (short) 2), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.HARD_CLAY, (short) 0), 1.0D);
		tempMap.put(new IdData(Material.INK_SACK, (short) 13), 1.0D / 8.0D);
		
		elementMap.put(new IdData(Material.STAINED_CLAY, (short) 3), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.HARD_CLAY, (short) 0), 1.0D);
		tempMap.put(new IdData(Material.INK_SACK, (short) 12), 1.0D / 8.0D);
		
		elementMap.put(new IdData(Material.STAINED_CLAY, (short) 4), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.HARD_CLAY, (short) 0), 1.0D);
		tempMap.put(new IdData(Material.INK_SACK, (short) 11), 1.0D / 8.0D);
		
		elementMap.put(new IdData(Material.STAINED_CLAY, (short) 5), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.HARD_CLAY, (short) 0), 1.0D);
		tempMap.put(new IdData(Material.INK_SACK, (short) 10), 1.0D / 8.0D);
		
		elementMap.put(new IdData(Material.STAINED_CLAY, (short) 6), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.HARD_CLAY, (short) 0), 1.0D);
		tempMap.put(new IdData(Material.INK_SACK, (short) 9), 1.0D / 8.0D);
		
		elementMap.put(new IdData(Material.STAINED_CLAY, (short) 7), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.HARD_CLAY, (short) 0), 1.0D);
		tempMap.put(new IdData(Material.INK_SACK, (short) 8), 1.0D / 8.0D);
		
		elementMap.put(new IdData(Material.STAINED_CLAY, (short) 8), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.HARD_CLAY, (short) 0), 1.0D);
		tempMap.put(new IdData(Material.INK_SACK, (short) 7), 1.0D / 8.0D);
		
		elementMap.put(new IdData(Material.STAINED_CLAY, (short) 9), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.HARD_CLAY, (short) 0), 1.0D);
		tempMap.put(new IdData(Material.INK_SACK, (short) 6), 1.0D / 8.0D);
		
		elementMap.put(new IdData(Material.STAINED_CLAY, (short) 10), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.HARD_CLAY, (short) 0), 1.0D);
		tempMap.put(new IdData(Material.INK_SACK, (short) 5), 1.0D / 8.0D);
		
		elementMap.put(new IdData(Material.STAINED_CLAY, (short) 11), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.HARD_CLAY, (short) 0), 1.0D);
		tempMap.put(new IdData(Material.INK_SACK, (short) 4), 1.0D / 8.0D);
		
		elementMap.put(new IdData(Material.STAINED_CLAY, (short) 12), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.HARD_CLAY, (short) 0), 1.0D);
		tempMap.put(new IdData(Material.INK_SACK, (short) 3), 1.0D / 8.0D);
		
		elementMap.put(new IdData(Material.STAINED_CLAY, (short) 13), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.HARD_CLAY, (short) 0), 1.0D);
		tempMap.put(new IdData(Material.INK_SACK, (short) 2), 1.0D / 8.0D);
		
		elementMap.put(new IdData(Material.STAINED_CLAY, (short) 14), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.HARD_CLAY, (short) 0), 1.0D);
		tempMap.put(new IdData(Material.INK_SACK, (short) 1), 1.0D / 8.0D);
		
		elementMap.put(new IdData(Material.STAINED_CLAY, (short) 15), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.HARD_CLAY, (short) 0), 1.0D);
		tempMap.put(new IdData(Material.INK_SACK, (short) 0), 1.0D / 8.0D);
		
		elementMap.put(new IdData(Material.PRISMARINE, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.PRISMARINE_SHARD, (short) 0), 4.0D);
		
		elementMap.put(new IdData(Material.PRISMARINE, (short) 1), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.PRISMARINE_SHARD, (short) 0), 9.0D);
		
		elementMap.put(new IdData(Material.PRISMARINE, (short) 2), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.PRISMARINE_SHARD, (short) 0), 8.0D);
		tempMap.put(new IdData(Material.INK_SACK, (short) 0), 1.0D);
		
		elementMap.put(new IdData(Material.SEA_LANTERN, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.PRISMARINE_SHARD, (short) 0), 4.0D);
		tempMap.put(new IdData(Material.PRISMARINE_CRYSTALS, (short) 0), 5.0D);
		
		elementMap.put(new IdData(Material.HAY_BLOCK, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.WHEAT, (short) 0), 9.0D);
		
		elementMap.put(new IdData(Material.COAL_BLOCK, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.COAL, (short) 0), 9.0D);
		
		elementMap.put(new IdData(Material.PACKED_ICE, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.ICE, (short) 0), 9.0D);
		
		elementMap.put(new IdData(Material.RED_SANDSTONE, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.SAND, (short) 1), 4.0D);
		
		elementMap.put(new IdData(Material.RED_SANDSTONE, (short) 1), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.RED_SANDSTONE, (short) 0), 1.0D);
		
		elementMap.put(new IdData(Material.RED_SANDSTONE, (short) 2), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.RED_SANDSTONE, (short) 0), 1.0D);
		
		elementMap.put(new IdData(Material.RED_SANDSTONE_STAIRS, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.RED_SANDSTONE, (short) 0), 1.5D);
		
		elementMap.put(new IdData(Material.STONE_SLAB2, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.RED_SANDSTONE, (short) 0), 0.5D);
		
		elementMap.put(new IdData(Material.SAPLING, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.LEAVES, (short) 0), 10.0D);
		
		elementMap.put(new IdData(Material.SAPLING, (short) 1), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.LEAVES, (short) 1), 10.0D);
		
		elementMap.put(new IdData(Material.SAPLING, (short) 2), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.LEAVES, (short) 2), 10.0D);
		
		elementMap.put(new IdData(Material.SAPLING, (short) 3), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.LEAVES, (short) 3), 10.0D);
		
		elementMap.put(new IdData(Material.SAPLING, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.LEAVES_2, (short) 0), 10.0D);
		
		elementMap.put(new IdData(Material.SAPLING, (short) 1), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.LEAVES_2, (short) 1), 10.0D);
		
		elementMap.put(new IdData(Material.WEB, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.STRING, (short) 0), 10.0D);
		
		elementMap.put(new IdData(Material.TORCH, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.STICK, (short) 0), 1.0D / 4.0D);
		tempMap.put(new IdData(Material.COAL, (short) 0), 0.5D / 4.0D);
		tempMap.put(new IdData(Material.COAL, (short) 1), 0.5D / 4.0D);
		
		elementMap.put(new IdData(Material.CHEST, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.WOOD, (short) 0), 8.0D);
		
		elementMap.put(new IdData(Material.WORKBENCH, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.WOOD, (short) 0), 4.0D);
		
		elementMap.put(new IdData(Material.FURNACE, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.COBBLESTONE, (short) 0), 8.0D);
		
		elementMap.put(new IdData(Material.LADDER, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.STICK, (short) 0), 7.0D / 3.0D);
		
		elementMap.put(new IdData(Material.SNOW, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.SNOW_BLOCK, (short) 0), 3.0D / 6.0D);
		
		elementMap.put(new IdData(Material.JUKEBOX, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.WOOD, (short) 0), 8.0D);
		tempMap.put(new IdData(Material.DIAMOND, (short) 0), 1.0D);
		
		elementMap.put(new IdData(Material.FENCE, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.WOOD, (short) 0), 4.0D / 3.0D);
		tempMap.put(new IdData(Material.STICK, (short) 0), 2.0D / 3.0D);
		
		elementMap.put(new IdData(Material.SPRUCE_FENCE, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.WOOD, (short) 1), 4.0D / 3.0D);
		tempMap.put(new IdData(Material.STICK, (short) 0), 2.0D / 3.0D);
		
		elementMap.put(new IdData(Material.BIRCH_FENCE, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.WOOD, (short) 2), 4.0D / 3.0D);
		tempMap.put(new IdData(Material.STICK, (short) 0), 2.0D / 3.0D);
		
		elementMap.put(new IdData(Material.JUNGLE_FENCE, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.WOOD, (short) 3), 4.0D / 3.0D);
		tempMap.put(new IdData(Material.STICK, (short) 0), 2.0D / 3.0D);
		
		elementMap.put(new IdData(Material.ACACIA_FENCE, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.WOOD, (short) 4), 4.0D / 3.0D);
		tempMap.put(new IdData(Material.STICK, (short) 0), 2.0D / 3.0D);
		
		elementMap.put(new IdData(Material.DARK_OAK_FENCE, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.WOOD, (short) 5), 4.0D / 3.0D);
		tempMap.put(new IdData(Material.STICK, (short) 0), 2.0D / 3.0D);
		
		elementMap.put(new IdData(Material.THIN_GLASS, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.GLASS, (short) 0), 6.0D / 16.0D);
		
		elementMap.put(new IdData(Material.IRON_FENCE, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.IRON_INGOT, (short) 0), 6.0D / 16.0D);
		
		elementMap.put(new IdData(Material.NETHER_FENCE, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.NETHER_BRICK, (short) 0), 6.0D / 6.0D);
		
		elementMap.put(new IdData(Material.ENCHANTMENT_TABLE, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.OBSIDIAN, (short) 0), 4.0D);
		tempMap.put(new IdData(Material.DIAMOND, (short) 0), 2.0D);
		tempMap.put(new IdData(Material.BOOK, (short) 0), 1.0D);
		
		elementMap.put(new IdData(Material.ENDER_CHEST, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.OBSIDIAN, (short) 0), 8.0D);
		tempMap.put(new IdData(Material.EYE_OF_ENDER, (short) 0), 1.0D);
		
		elementMap.put(new IdData(Material.ANVIL, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.IRON_BLOCK, (short) 0), 3.0D);
		tempMap.put(new IdData(Material.IRON_INGOT, (short) 0), 4.0D);
		
		elementMap.put(new IdData(Material.ANVIL, (short) 1), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.ANVIL, (short) 0), 2.0D / 3.0D);
		
		elementMap.put(new IdData(Material.ANVIL, (short) 2), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.ANVIL, (short) 0), 1.0D / 3.0D);
		
		elementMap.put(new IdData(Material.TRAPPED_CHEST, (short) 1), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.CHEST, (short) 0), 1.0D);
		tempMap.put(new IdData(Material.TRIPWIRE_HOOK, (short) 0), 1.0D);
		
		elementMap.put(new IdData(Material.STAINED_GLASS_PANE, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.THIN_GLASS, (short) 0), 8.0D / 8.0D);
		tempMap.put(new IdData(Material.INK_SACK, (short) 15), 1.0D / 8.0D);
		
		elementMap.put(new IdData(Material.STAINED_GLASS_PANE, (short) 1), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.THIN_GLASS, (short) 0), 8.0D / 8.0D);
		tempMap.put(new IdData(Material.INK_SACK, (short) 14), 1.0D / 8.0D);
		
		elementMap.put(new IdData(Material.STAINED_GLASS_PANE, (short) 2), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.THIN_GLASS, (short) 0), 8.0D / 8.0D);
		tempMap.put(new IdData(Material.INK_SACK, (short) 13), 1.0D / 8.0D);
		
		elementMap.put(new IdData(Material.STAINED_GLASS_PANE, (short) 3), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.THIN_GLASS, (short) 0), 8.0D / 8.0D);
		tempMap.put(new IdData(Material.INK_SACK, (short) 12), 1.0D / 8.0D);
		
		elementMap.put(new IdData(Material.STAINED_GLASS_PANE, (short) 4), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.THIN_GLASS, (short) 0), 8.0D / 8.0D);
		tempMap.put(new IdData(Material.INK_SACK, (short) 11), 1.0D / 8.0D);
		
		elementMap.put(new IdData(Material.STAINED_GLASS_PANE, (short) 5), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.THIN_GLASS, (short) 0), 8.0D / 8.0D);
		tempMap.put(new IdData(Material.INK_SACK, (short) 10), 1.0D / 8.0D);
		
		elementMap.put(new IdData(Material.STAINED_GLASS_PANE, (short) 6), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.THIN_GLASS, (short) 0), 8.0D / 8.0D);
		tempMap.put(new IdData(Material.INK_SACK, (short) 9), 1.0D / 8.0D);
		
		elementMap.put(new IdData(Material.STAINED_GLASS_PANE, (short) 7), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.THIN_GLASS, (short) 0), 8.0D / 8.0D);
		tempMap.put(new IdData(Material.INK_SACK, (short) 8), 1.0D / 8.0D);
		
		elementMap.put(new IdData(Material.STAINED_GLASS_PANE, (short) 8), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.THIN_GLASS, (short) 0), 8.0D / 8.0D);
		tempMap.put(new IdData(Material.INK_SACK, (short) 7), 1.0D / 8.0D);
		
		elementMap.put(new IdData(Material.STAINED_GLASS_PANE, (short) 9), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.THIN_GLASS, (short) 0), 8.0D / 8.0D);
		tempMap.put(new IdData(Material.INK_SACK, (short) 6), 1.0D / 8.0D);
		
		elementMap.put(new IdData(Material.STAINED_GLASS_PANE, (short) 10), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.THIN_GLASS, (short) 0), 8.0D / 8.0D);
		tempMap.put(new IdData(Material.INK_SACK, (short) 5), 1.0D / 8.0D);
		
		elementMap.put(new IdData(Material.STAINED_GLASS_PANE, (short) 11), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.THIN_GLASS, (short) 0), 8.0D / 8.0D);
		tempMap.put(new IdData(Material.INK_SACK, (short) 4), 1.0D / 8.0D);
		
		elementMap.put(new IdData(Material.STAINED_GLASS_PANE, (short) 12), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.THIN_GLASS, (short) 0), 8.0D / 8.0D);
		tempMap.put(new IdData(Material.INK_SACK, (short) 3), 1.0D / 8.0D);
		
		elementMap.put(new IdData(Material.STAINED_GLASS_PANE, (short) 13), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.THIN_GLASS, (short) 0), 8.0D / 8.0D);
		tempMap.put(new IdData(Material.INK_SACK, (short) 2), 1.0D / 8.0D);
		
		elementMap.put(new IdData(Material.STAINED_GLASS_PANE, (short) 14), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.THIN_GLASS, (short) 0), 8.0D / 8.0D);
		tempMap.put(new IdData(Material.INK_SACK, (short) 1), 1.0D / 8.0D);
		
		elementMap.put(new IdData(Material.STAINED_GLASS_PANE, (short) 15), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.THIN_GLASS, (short) 0), 8.0D / 8.0D);
		tempMap.put(new IdData(Material.INK_SACK, (short) 0), 1.0D / 8.0D);
		
		elementMap.put(new IdData(Material.SLIME_BLOCK, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.SLIME_BALL, (short) 0), 9.0D / 1.0D);
		
		elementMap.put(new IdData(Material.SLIME_BLOCK, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.SLIME_BALL, (short) 0), 9.0D / 1.0D);
		
		elementMap.put(new IdData(Material.CARPET, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.WOOL, (short) 0), 2.0D / 3.0D);
		
		elementMap.put(new IdData(Material.CARPET, (short) 1), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.WOOL, (short) 1), 2.0D / 3.0D);
		
		elementMap.put(new IdData(Material.CARPET, (short) 2), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.WOOL, (short) 2), 2.0D / 3.0D);
		
		elementMap.put(new IdData(Material.CARPET, (short) 3), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.WOOL, (short) 3), 2.0D / 3.0D);
		
		elementMap.put(new IdData(Material.CARPET, (short) 4), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.WOOL, (short) 4), 2.0D / 3.0D);
		
		elementMap.put(new IdData(Material.CARPET, (short) 5), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.WOOL, (short) 5), 2.0D / 3.0D);
		
		elementMap.put(new IdData(Material.CARPET, (short) 6), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.WOOL, (short) 6), 2.0D / 3.0D);
		
		elementMap.put(new IdData(Material.CARPET, (short) 7), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.WOOL, (short) 7), 2.0D / 3.0D);
		
		elementMap.put(new IdData(Material.CARPET, (short) 8), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.WOOL, (short) 8), 2.0D / 3.0D);
		
		elementMap.put(new IdData(Material.CARPET, (short) 9), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.WOOL, (short) 9), 2.0D / 3.0D);
		
		elementMap.put(new IdData(Material.CARPET, (short) 10), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.WOOL, (short) 10), 2.0D / 3.0D);
		
		elementMap.put(new IdData(Material.CARPET, (short) 11), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.WOOL, (short) 11), 2.0D / 3.0D);
		
		elementMap.put(new IdData(Material.CARPET, (short) 12), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.WOOL, (short) 12), 2.0D / 3.0D);
		
		elementMap.put(new IdData(Material.CARPET, (short) 13), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.WOOL, (short) 13), 2.0D / 3.0D);
		
		elementMap.put(new IdData(Material.CARPET, (short) 14), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.WOOL, (short) 14), 2.0D / 3.0D);
		
		elementMap.put(new IdData(Material.CARPET, (short) 15), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.WOOL, (short) 15), 2.0D / 3.0D);
		
		elementMap.put(new IdData(Material.PAINTING, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.STICK, (short) 0), 8.0D / 1.0D);
		tempMap.put(new IdData(Material.WOOL, (short) 0), 1.0D / 1.0D);
		
		elementMap.put(new IdData(Material.SIGN, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.WOOD, (short) 0), 6.0D / 3.0D);
		tempMap.put(new IdData(Material.STICK, (short) 0), 1.0D / 3.0D);
		
		elementMap.put(new IdData(Material.BED, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.WOOD, (short) 0), 3.0D / 1.0D);
		tempMap.put(new IdData(Material.WOOL, (short) 0), 3.0D / 1.0D);
		
		elementMap.put(new IdData(Material.ITEM_FRAME, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.STICK, (short) 0), 8.0D / 1.0D);
		tempMap.put(new IdData(Material.LEATHER, (short) 0), 1.0D / 1.0D);
		
		elementMap.put(new IdData(Material.FLOWER_POT, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.CLAY_BRICK, (short) 0), 3.0D / 1.0D);
		
		elementMap.put(new IdData(Material.DISPENSER, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.COBBLE_WALL, (short) 0), 7.0D / 1.0D);
		tempMap.put(new IdData(Material.BOW, (short) 0), 1.0D / 1.0D);
		tempMap.put(new IdData(Material.REDSTONE, (short) 0), 1.0D / 1.0D);
		
		elementMap.put(new IdData(Material.NOTE_BLOCK, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.WOOD, (short) 0), 8.0D / 1.0D);
		tempMap.put(new IdData(Material.REDSTONE, (short) 0), 1.0D / 1.0D);
		
		elementMap.put(new IdData(Material.PISTON_STICKY_BASE, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.PISTON_BASE, (short) 0), 1.0D / 1.0D);
		tempMap.put(new IdData(Material.SLIME_BALL, (short) 0), 1.0D / 1.0D);
		
		elementMap.put(new IdData(Material.PISTON_BASE, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.WOOD, (short) 0), 3.0D / 1.0D);
		tempMap.put(new IdData(Material.COBBLESTONE, (short) 0), 4.0D / 1.0D);
		tempMap.put(new IdData(Material.REDSTONE, (short) 0), 1.0D / 1.0D);
		tempMap.put(new IdData(Material.IRON_INGOT, (short) 0), 1.0D / 1.0D);
		
		elementMap.put(new IdData(Material.TNT, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.SAND, (short) 0), 4.0D / 1.0D);
		tempMap.put(new IdData(Material.SULPHUR, (short) 0), 5.0D / 1.0D);
		
		elementMap.put(new IdData(Material.LEVER, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.STICK, (short) 0), 1.0D / 1.0D);
		tempMap.put(new IdData(Material.COBBLESTONE, (short) 0), 1.0D / 1.0D);
		
		elementMap.put(new IdData(Material.STONE_PLATE, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.STONE, (short) 0), 2.0D / 1.0D);
		
		elementMap.put(new IdData(Material.WOOD_PLATE, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.WOOD, (short) 0), 2.0D / 1.0D);
		
		elementMap.put(new IdData(Material.IRON_PLATE, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.IRON_INGOT, (short) 0), 2.0D / 1.0D);
		
		elementMap.put(new IdData(Material.GOLD_PLATE, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.GOLD_INGOT, (short) 0), 2.0D / 1.0D);
		
		elementMap.put(new IdData(Material.REDSTONE_TORCH_ON, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.STICK, (short) 0), 1.0D / 1.0D);
		tempMap.put(new IdData(Material.REDSTONE, (short) 0), 1.0D / 1.0D);
		
		elementMap.put(new IdData(Material.WOOD_BUTTON, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.WOOD, (short) 0), 1.0D / 1.0D);
		
		elementMap.put(new IdData(Material.STONE_BUTTON, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.STONE, (short) 0), 1.0D / 1.0D);
		
		elementMap.put(new IdData(Material.TRAP_DOOR, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.WOOD, (short) 0), 6.0D / 2.0D);
		
		elementMap.put(new IdData(Material.GOLD_PLATE, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.GOLD_INGOT, (short) 0), 2.0D / 1.0D);
		
		elementMap.put(new IdData(Material.FENCE_GATE, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.STICK, (short) 0), 4.0D / 1.0D);
		tempMap.put(new IdData(Material.WOOD, (short) 0), 2.0D / 1.0D);
		
		elementMap.put(new IdData(Material.SPRUCE_FENCE_GATE, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.STICK, (short) 0), 4.0D / 1.0D);
		tempMap.put(new IdData(Material.WOOD, (short) 1), 2.0D / 1.0D);
		
		elementMap.put(new IdData(Material.BIRCH_FENCE_GATE, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.STICK, (short) 0), 4.0D / 1.0D);
		tempMap.put(new IdData(Material.WOOD, (short) 2), 2.0D / 1.0D);
		
		elementMap.put(new IdData(Material.JUNGLE_FENCE_GATE, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.STICK, (short) 0), 4.0D / 1.0D);
		tempMap.put(new IdData(Material.WOOD, (short) 3), 2.0D / 1.0D);
		
		elementMap.put(new IdData(Material.ACACIA_FENCE_GATE, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.STICK, (short) 0), 4.0D / 1.0D);
		tempMap.put(new IdData(Material.WOOD, (short) 4), 2.0D / 1.0D);
		
		elementMap.put(new IdData(Material.SPRUCE_FENCE_GATE, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.STICK, (short) 0), 4.0D / 1.0D);
		tempMap.put(new IdData(Material.WOOD, (short) 5), 2.0D / 1.0D);
		
		elementMap.put(new IdData(Material.REDSTONE_LAMP_OFF, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.REDSTONE, (short) 0), 4.0D / 1.0D);
		tempMap.put(new IdData(Material.GLOWSTONE, (short) 0), 1.0D / 1.0D);
		
		elementMap.put(new IdData(Material.TRIPWIRE_HOOK, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.STICK, (short) 0), 1.0D / 2.0D);
		tempMap.put(new IdData(Material.WOOD, (short) 0), 1.0D / 2.0D);
		tempMap.put(new IdData(Material.IRON_INGOT, (short) 0), 1.0D / 2.0D);
		
		elementMap.put(new IdData(Material.DAYLIGHT_DETECTOR, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.GLASS, (short) 0), 3.0D / 1.0D);
		tempMap.put(new IdData(Material.QUARTZ, (short) 0), 3.0D / 1.0D);
		tempMap.put(new IdData(Material.WOOD_STEP, (short) 0), 3.0D / 1.0D);
		
		elementMap.put(new IdData(Material.REDSTONE_BLOCK, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.REDSTONE, (short) 0), 9.0D / 1.0D);
		
		elementMap.put(new IdData(Material.HOPPER, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.CHEST, (short) 0), 1.0D / 1.0D);
		tempMap.put(new IdData(Material.IRON_INGOT, (short) 0), 5.0D / 1.0D);
		
		elementMap.put(new IdData(Material.DROPPER, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.COBBLESTONE, (short) 0), 7.0D / 1.0D);
		tempMap.put(new IdData(Material.REDSTONE, (short) 0), 1.0D / 1.0D);
		
		elementMap.put(new IdData(Material.IRON_TRAPDOOR, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.IRON_INGOT, (short) 0), 4.0D / 1.0D);
		
		elementMap.put(new IdData(Material.TRIPWIRE_HOOK, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.STICK, (short) 0), 1.0D / 2.0D);
		tempMap.put(new IdData(Material.WOOD, (short) 0), 1.0D / 2.0D);
		tempMap.put(new IdData(Material.IRON_INGOT, (short) 0), 1.0D / 2.0D);
		
		elementMap.put(new IdData(Material.DIODE, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.REDSTONE_TORCH_ON, (short) 0), 2.0D / 1.0D);
		tempMap.put(new IdData(Material.REDSTONE, (short) 0), 1.0D / 1.0D);
		tempMap.put(new IdData(Material.STONE, (short) 0), 3.0D / 1.0D);
		
		elementMap.put(new IdData(Material.REDSTONE_COMPARATOR, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.REDSTONE_TORCH_ON, (short) 0), 3.0D / 1.0D);
		tempMap.put(new IdData(Material.QUARTZ, (short) 0), 1.0D / 1.0D);
		tempMap.put(new IdData(Material.STONE, (short) 0), 3.0D / 1.0D);
		
		elementMap.put(new IdData(Material.IRON_DOOR, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.IRON_INGOT, (short) 0), 6.0D / 3.0D);
		
		elementMap.put(new IdData(Material.WOOD_DOOR, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.WOOD, (short) 0), 6.0D / 3.0D);
		
		elementMap.put(new IdData(Material.SPRUCE_DOOR_ITEM, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.WOOD, (short) 1), 6.0D / 3.0D);
		
		elementMap.put(new IdData(Material.BIRCH_DOOR_ITEM, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.WOOD, (short) 2), 6.0D / 3.0D);
		
		elementMap.put(new IdData(Material.JUNGLE_DOOR_ITEM, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.WOOD, (short) 3), 6.0D / 3.0D);
		
		elementMap.put(new IdData(Material.ACACIA_DOOR_ITEM, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.WOOD, (short) 4), 6.0D / 3.0D);
		
		elementMap.put(new IdData(Material.DARK_OAK_DOOR_ITEM, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.WOOD, (short) 5), 6.0D / 3.0D);
		
		elementMap.put(new IdData(Material.RAILS, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.IRON_INGOT, (short) 0), 6.0D / 16.0D);
		tempMap.put(new IdData(Material.STICK, (short) 0), 1.0D / 16.0D);
		
		elementMap.put(new IdData(Material.POWERED_RAIL, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.GOLD_INGOT, (short) 0), 6.0D / 6.0D);
		tempMap.put(new IdData(Material.STICK, (short) 0), 1.0D / 6.0D);
		tempMap.put(new IdData(Material.REDSTONE, (short) 0), 1.0D / 6.0D);
		
		elementMap.put(new IdData(Material.DETECTOR_RAIL, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.IRON_INGOT, (short) 0), 6.0D / 6.0D);
		tempMap.put(new IdData(Material.STONE_PLATE, (short) 0), 1.0D / 6.0D);
		tempMap.put(new IdData(Material.REDSTONE, (short) 0), 1.0D / 6.0D);
		
		elementMap.put(new IdData(Material.ACTIVATOR_RAIL, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.IRON_INGOT, (short) 0), 6.0D / 6.0D);
		tempMap.put(new IdData(Material.STICK, (short) 0), 2.0D / 6.0D);
		tempMap.put(new IdData(Material.REDSTONE_TORCH_ON, (short) 0), 1.0D / 6.0D);
		
		elementMap.put(new IdData(Material.MINECART, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.IRON_INGOT, (short) 0), 5.0D / 1.0D);
		
		elementMap.put(new IdData(Material.EXPLOSIVE_MINECART, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.TNT, (short) 0), 1.0D / 1.0D);
		tempMap.put(new IdData(Material.MINECART, (short) 0), 1.0D / 1.0D);
		
		elementMap.put(new IdData(Material.HOPPER_MINECART, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.HOPPER, (short) 0), 1.0D / 1.0D);
		tempMap.put(new IdData(Material.MINECART, (short) 0), 1.0D / 1.0D);
		
		elementMap.put(new IdData(Material.STORAGE_MINECART, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.CHEST, (short) 0), 1.0D / 1.0D);
		tempMap.put(new IdData(Material.MINECART, (short) 0), 1.0D / 1.0D);
		
		elementMap.put(new IdData(Material.POWERED_MINECART, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.FURNACE, (short) 0), 1.0D / 1.0D);
		tempMap.put(new IdData(Material.MINECART, (short) 0), 1.0D / 1.0D);
		
		elementMap.put(new IdData(Material.EXPLOSIVE_MINECART, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.TNT, (short) 0), 1.0D / 1.0D);
		tempMap.put(new IdData(Material.MINECART, (short) 0), 1.0D / 1.0D);
		
		elementMap.put(new IdData(Material.BOAT, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.WOOD, (short) 0), 5.0D / 1.0D);
		
		elementMap.put(new IdData(Material.CARROT_STICK, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.STICK, (short) 0), 3.0D / 1.0D);
		tempMap.put(new IdData(Material.STRING, (short) 0), 2.0D / 1.0D);
		tempMap.put(new IdData(Material.CARROT, (short) 0), 1.0D / 1.0D);
		
		elementMap.put(new IdData(Material.BEACON, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.GLASS, (short) 0), 5.0D / 1.0D);
		tempMap.put(new IdData(Material.NETHER_STAR, (short) 0), 1.0D / 1.0D);
		tempMap.put(new IdData(Material.OBSIDIAN, (short) 0), 3.0D / 1.0D);
		
		elementMap.put(new IdData(Material.BUCKET, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.IRON_INGOT, (short) 0), 3.0D / 1.0D);
		
		elementMap.put(new IdData(Material.WATER_BUCKET, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.BUCKET, (short) 0), 1.0D / 1.0D);
		tempMap.put(new IdData(Material.WATER, (short) 0), 1.0D / 1.0D);
		
		elementMap.put(new IdData(Material.LAVA_BUCKET, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.BUCKET, (short) 0), 1.0D / 1.0D);
		tempMap.put(new IdData(Material.LAVA, (short) 0), 1.0D / 1.0D);
		
		elementMap.put(new IdData(Material.MILK_BUCKET, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.BUCKET, (short) 0), 1.0D / 1.0D);
		tempMap.put(new IdData(Material.STATIONARY_WATER, (short) 0), 1.0D / 1.0D);
		
		elementMap.put(new IdData(Material.SNOW_BALL, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.SNOW_BLOCK, (short) 0), 1.0D / 4.0D);
		
		elementMap.put(new IdData(Material.PAPER, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.SUGAR_CANE, (short) 0), 3.0D / 3.0D);
		
		elementMap.put(new IdData(Material.BOOK, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.PAPER, (short) 0), 3.0D / 1.0D);
		tempMap.put(new IdData(Material.LEATHER, (short) 0), 1.0D / 1.0D);
		
		elementMap.put(new IdData(Material.EYE_OF_ENDER, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.ENDER_PEARL, (short) 0), 1.0D / 1.0D);
		tempMap.put(new IdData(Material.BLAZE_POWDER, (short) 0), 1.0D / 1.0D);
		
		elementMap.put(new IdData(Material.FIREBALL, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.BLAZE_POWDER, (short) 0), 1.0D / 3.0D);
		tempMap.put(new IdData(Material.COAL, (short) 0), 1.0D / 3.0D);
		tempMap.put(new IdData(Material.SULPHUR, (short) 0), 1.0D / 3.0D);
		
		elementMap.put(new IdData(Material.BOOK_AND_QUILL, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.BOOK, (short) 0), 1.0D / 1.0D);
		tempMap.put(new IdData(Material.FEATHER, (short) 0), 1.0D / 1.0D);
		tempMap.put(new IdData(Material.INK_SACK, (short) 0), 1.0D / 1.0D);
		
		elementMap.put(new IdData(Material.EMPTY_MAP, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.COMPASS, (short) 0), 1.0D / 1.0D);
		tempMap.put(new IdData(Material.PAPER, (short) 0), 8.0D / 1.0D);
		
		elementMap.put(new IdData(Material.APPLE, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.LEAVES, (short) 0), 20.0D / 1.0D);
		
		elementMap.put(new IdData(Material.BREAD, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.WHEAT, (short) 0), 3.0D / 1.0D);
		
		elementMap.put(new IdData(Material.GRILLED_PORK, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.PORK, (short) 0), 2.0D / 1.0D);
		
		elementMap.put(new IdData(Material.GOLDEN_APPLE, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.APPLE, (short) 0), 1.0D / 1.0D);
		tempMap.put(new IdData(Material.GOLD_INGOT, (short) 0), 8.0D / 1.0D);
		
		elementMap.put(new IdData(Material.GOLDEN_APPLE, (short) 1), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.APPLE, (short) 0), 1.0D / 1.0D);
		tempMap.put(new IdData(Material.GOLD_BLOCK, (short) 0), 8.0D / 1.0D);
		
		elementMap.put(new IdData(Material.COOKED_FISH, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.RAW_FISH, (short) 0), 2.0D / 1.0D);
		
		elementMap.put(new IdData(Material.COOKED_FISH, (short) 1), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.RAW_FISH, (short) 1), 2.0D / 1.0D);
		
		elementMap.put(new IdData(Material.CAKE, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.STATIONARY_WATER, (short) 0), 3.0D / 1.0D);
		tempMap.put(new IdData(Material.SUGAR, (short) 0), 2.0D / 1.0D);
		tempMap.put(new IdData(Material.EGG, (short) 0), 1.0D / 1.0D);
		tempMap.put(new IdData(Material.WHEAT, (short) 0), 3.0D / 1.0D);
		
		elementMap.put(new IdData(Material.COOKIE, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.WHEAT, (short) 0), 2.0D / 8.0D);
		tempMap.put(new IdData(Material.INK_SACK, (short) 3), 1.0D / 8.0D);
		
		elementMap.put(new IdData(Material.COOKED_FISH, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.RAW_FISH, (short) 0), 2.0D / 1.0D);
		
		elementMap.put(new IdData(Material.MELON, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.MELON_BLOCK, (short) 0), 1.0D / 9.0D);
		
		elementMap.put(new IdData(Material.COOKED_BEEF, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.RAW_BEEF, (short) 0), 2.0D / 1.0D);
		
		elementMap.put(new IdData(Material.COOKED_CHICKEN, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.RAW_CHICKEN, (short) 0), 2.0D / 1.0D);
		
		elementMap.put(new IdData(Material.COOKED_MUTTON, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.MUTTON, (short) 0), 2.0D / 1.0D);
		
		elementMap.put(new IdData(Material.COOKED_RABBIT, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.RABBIT, (short) 0), 2.0D / 1.0D);
		
		elementMap.put(new IdData(Material.BAKED_POTATO, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.POTATO_ITEM, (short) 0), 2.0D / 1.0D);
		
		elementMap.put(new IdData(Material.POISONOUS_POTATO, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.POTATO_ITEM, (short) 0), 50.0D / 1.0D);
		
		elementMap.put(new IdData(Material.PUMPKIN_PIE, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.PUMPKIN, (short) 0), 1.0D / 1.0D);
		tempMap.put(new IdData(Material.EGG, (short) 0), 1.0D / 1.0D);
		tempMap.put(new IdData(Material.SUGAR, (short) 0), 1.0D / 1.0D);
		
		elementMap.put(new IdData(Material.RABBIT_STEW, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.COOKED_RABBIT, (short) 0), 1.0D / 1.0D);
		tempMap.put(new IdData(Material.BROWN_MUSHROOM, (short) 0), 1.0D / 1.0D);
		tempMap.put(new IdData(Material.CARROT_ITEM, (short) 0), 1.0D / 1.0D);
		tempMap.put(new IdData(Material.BAKED_POTATO, (short) 0), 1.0D / 1.0D);
		tempMap.put(new IdData(Material.BOWL, (short) 0), 1.0D / 1.0D);
		
		elementMap.put(new IdData(Material.COMPASS, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.IRON_INGOT, (short) 0), 4.0D / 1.0D);
		tempMap.put(new IdData(Material.REDSTONE, (short) 0), 1.0D / 1.0D);
		
		elementMap.put(new IdData(Material.WATCH, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.GOLD_INGOT, (short) 0), 4.0D / 1.0D);
		tempMap.put(new IdData(Material.REDSTONE, (short) 0), 1.0D / 1.0D);
		
		elementMap.put(new IdData(Material.LEASH, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.STRING, (short) 0), 4.0D / 2.0D);
		tempMap.put(new IdData(Material.SLIME_BALL, (short) 0), 1.0D / 2.0D);
		
		elementMap.put(new IdData(Material.ARROW, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.STICK, (short) 0), 1.0D / 4.0D);
		tempMap.put(new IdData(Material.FEATHER, (short) 0), 1.0D / 4.0D);
		tempMap.put(new IdData(Material.FLINT, (short) 0), 1.0D / 4.0D);
		
		elementMap.put(new IdData(Material.GLASS_BOTTLE, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.GLASS, (short) 0), 3.0D / 3.0D);
		
		elementMap.put(new IdData(Material.FERMENTED_SPIDER_EYE, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.SPIDER_EYE, (short) 0), 1.0D / 1.0D);
		tempMap.put(new IdData(Material.SUGAR, (short) 0), 1.0D / 1.0D);
		tempMap.put(new IdData(Material.BROWN_MUSHROOM, (short) 0), 1.0D / 1.0D);
		
		elementMap.put(new IdData(Material.BLAZE_POWDER, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.BLAZE_ROD, (short) 0), 1.0D / 2.0D);
		
		elementMap.put(new IdData(Material.MAGMA_CREAM, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.SLIME_BALL, (short) 0), 1.0D / 1.0D);
		tempMap.put(new IdData(Material.BLAZE_POWDER, (short) 0), 1.0D / 1.0D);
		
		elementMap.put(new IdData(Material.BREWING_STAND_ITEM, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.BLAZE_ROD, (short) 0), 1.0D / 1.0D);
		tempMap.put(new IdData(Material.COBBLESTONE, (short) 0), 3.0D / 1.0D);
		
		elementMap.put(new IdData(Material.CAULDRON_ITEM, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.IRON_INGOT, (short) 0), 7.0D / 1.0D);
		
		elementMap.put(new IdData(Material.GOLDEN_CARROT, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.GOLD_NUGGET, (short) 0), 8.0D / 1.0D);
		tempMap.put(new IdData(Material.CARROT_ITEM, (short) 0), 1.0D / 1.0D);
		
		elementMap.put(new IdData(Material.SPECKLED_MELON, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.GOLD_NUGGET, (short) 0), 8.0D / 1.0D);
		tempMap.put(new IdData(Material.MELON, (short) 0), 1.0D / 1.0D);
		
		elementMap.put(new IdData(Material.COAL, (short) 1), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.LOG, (short) 0), 2.0D / 1.0D);
		
		elementMap.put(new IdData(Material.STICK, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.WOOD, (short) 0), 2.0D / 4.0D);
		
		elementMap.put(new IdData(Material.BOWL, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.WOOD, (short) 0), 3.0D / 4.0D);
		
		elementMap.put(new IdData(Material.MELON_SEEDS, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.MELON, (short) 0), 1.0D / 1.0D);
		
		elementMap.put(new IdData(Material.PUMPKIN_SEEDS, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.PUMPKIN, (short) 0), 1.0D / 4.0D);
		
		elementMap.put(new IdData(Material.SEEDS, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.WHEAT, (short) 0), 1.0D / 4.0D);
		
		elementMap.put(new IdData(Material.FLINT, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.GRASS, (short) 0), 10.0D / 1.0D);
		
		elementMap.put(new IdData(Material.CLAY_BRICK, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.CLAY_BALL, (short) 0), 2.0D / 1.0D);
		
		elementMap.put(new IdData(Material.CLAY_BALL, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.CLAY, (short) 0), 1.0D / 4.0D);
		
		elementMap.put(new IdData(Material.GLOWSTONE_DUST, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.GLOWSTONE, (short) 0), 1.0D / 4.0D);
		
		elementMap.put(new IdData(Material.SUGAR, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.SUGAR_CANE, (short) 0), 1.0D / 1.0D);
		
		elementMap.put(new IdData(Material.GOLD_NUGGET, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.GOLD_INGOT, (short) 0), 1.0D / 9.0D);
		
		elementMap.put(new IdData(Material.CLAY_BRICK, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.CLAY_BALL, (short) 0), 2.0D / 1.0D);
		
		elementMap.put(new IdData(Material.NETHER_BRICK_ITEM, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.NETHERRACK, (short) 0), 2.0D / 1.0D);
		
		elementMap.put(new IdData(Material.RABBIT_HIDE, (short) 0), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.LEATHER, (short) 0), 1.0D / 4.0D);
		
		elementMap.put(new IdData(Material.INK_SACK, (short) 1), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.RED_ROSE, (short) 0), 1.0D / 1.0D);
		
		elementMap.put(new IdData(Material.INK_SACK, (short) 2), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.CACTUS, (short) 0), 2.0D / 1.0D);
		
		elementMap.put(new IdData(Material.INK_SACK, (short) 5), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.INK_SACK, (short) 1), 1.0D / 2.0D);
		tempMap.put(new IdData(Material.INK_SACK, (short) 4), 1.0D / 2.0D);
		
		elementMap.put(new IdData(Material.INK_SACK, (short) 6), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.INK_SACK, (short) 2), 1.0D / 2.0D);
		tempMap.put(new IdData(Material.INK_SACK, (short) 4), 1.0D / 2.0D);
		
		elementMap.put(new IdData(Material.INK_SACK, (short) 7), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.INK_SACK, (short) 15), 1.0D / 2.0D);
		tempMap.put(new IdData(Material.INK_SACK, (short) 8), 1.0D / 2.0D);
		
		elementMap.put(new IdData(Material.INK_SACK, (short) 8), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.INK_SACK, (short) 0), 1.0D / 2.0D);
		tempMap.put(new IdData(Material.INK_SACK, (short) 15), 1.0D / 2.0D);
		
		elementMap.put(new IdData(Material.INK_SACK, (short) 9), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.INK_SACK, (short) 1), 1.0D / 2.0D);
		tempMap.put(new IdData(Material.INK_SACK, (short) 15), 1.0D / 2.0D);
		
		elementMap.put(new IdData(Material.INK_SACK, (short) 10), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.INK_SACK, (short) 2), 1.0D / 2.0D);
		tempMap.put(new IdData(Material.INK_SACK, (short) 15), 1.0D / 2.0D);
		
		elementMap.put(new IdData(Material.INK_SACK, (short) 11), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.YELLOW_FLOWER, (short) 1), 1.0D / 1.0D);
		
		elementMap.put(new IdData(Material.INK_SACK, (short) 12), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.INK_SACK, (short) 4), 1.0D / 2.0D);
		tempMap.put(new IdData(Material.INK_SACK, (short) 15), 1.0D / 2.0D);
		
		elementMap.put(new IdData(Material.INK_SACK, (short) 13), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.INK_SACK, (short) 9), 1.0D / 2.0D);
		tempMap.put(new IdData(Material.INK_SACK, (short) 5), 1.0D / 2.0D);
		
		elementMap.put(new IdData(Material.INK_SACK, (short) 14), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.INK_SACK, (short) 1), 1.0D / 2.0D);
		tempMap.put(new IdData(Material.INK_SACK, (short) 11), 1.0D / 2.0D);
		
		elementMap.put(new IdData(Material.INK_SACK, (short) 15), tempMap = new HashMap<>());
		tempMap.put(new IdData(Material.BONE, (short) 1), 1.0D / 3.0D);
	}
	
	public void save()
	{
		File file = Config.getFile(Type.PRICE, true);
		
		try(FileOutputStream fileOut = new FileOutputStream(file);
				ByteArrayOutputStream streamOut = new ByteArrayOutputStream();
				ObjectOutputStream out = new ObjectOutputStream(streamOut))
		{
			out.writeObject(map);
			fileOut.write(streamOut.toByteArray());
		}
		catch(IOException e)
		{
			e.printStackTrace();
			return;
		}
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String aliases, String[] args)
	{
		List<String> list = new ArrayList<>();
		
		if(args.length == 1)
		{
			for(IdData key : map.keySet())
			{
				Material type = Material.getMaterial(key.getId());
				
				if(type != null && !list.contains(type.name()) && type.name().toUpperCase().startsWith(args[0].toUpperCase()))
				{
					list.add(type.name());
				}
			}
		}

		return list;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String aliases, String[] args)
	{
		ResourceBundle bundle = ResourceBundle.getBundle("message");
		ResourceBundle minecraftBundle = ResourceBundle.getBundle("minecraft");
		
		Player p = null;
		
		if(sender instanceof Player)
		{
			p = (Player) sender;
			bundle = ResourceBundle.getBundle("message", CraftCommonPlayer.get(p).getLocale());
			minecraftBundle = ResourceBundle.getBundle("minecraft", CraftCommonPlayer.get(p).getLocale());
		}
		
		Material type;
		short data;
		
		if(args.length == 0 && p != null)
		{
			ItemStack item = p.getItemInHand();
			type = item.getType();
			data = item.getDurability();
			
			if(type == Material.AIR)
			{
				sender.sendMessage(ChatColor.RED + bundle.getString("raid.cmd.sell.need-hoolding-item"));
				return true;
			}
		}
		else if(args.length > 0 && args.length < 3)
		{
			try
			{
				try
				{
					type = Material.getMaterial(Integer.valueOf(args[0]));
				}
				catch(NumberFormatException e)
				{
					type = Material.valueOf(args[0].toUpperCase());
				}
				
				if(type == null)
				{
					throw new IllegalArgumentException();
				}
				
				data = args.length == 2 ? Short.valueOf(args[1]) : 0;	
			}
			catch(NumberFormatException e)
			{
				sender.sendMessage(ChatColor.RED + bundle.getString("commons.cmds.invalid-number-format"));
				return true;
			}
			catch(IllegalArgumentException e)
			{
				sender.sendMessage(ChatColor.RED + bundle.getString("commons.cmds.id-not-found"));
				return true;
			}
		}
		else
		{
			TextComponent[] extra = new TextComponent[1];
			
			extra[0] = new TextComponent("/" + aliases);
			
			if(sender instanceof Player)
			{
				extra[0].addExtra(" [" + bundle.getString("commons.words.item").toLowerCase() + "]");
			}
			else
			{
				extra[0].addExtra(" <" + bundle.getString("commons.words.item").toLowerCase() + ">");
			}
			
			extra[0].addExtra(" [" + bundle.getString("commons.words.data").toLowerCase() + "]");
			
			TextComponent text = Message.getTextComponent(bundle.getString("commons.cmds.wrong-syntax-try"), extra);
			text.setColor(ChatColor.RED);
			sender.sendMessage(text.toLegacyText());
			return true;
		}

		IdData key = new IdData(type.getId(), data);
		
		
		Map<IdData, Double> tempMap = new HashMap<>();
		
		if(elementMap.containsKey(key))
		{
			tempMap = elementMap.get(key); //Derivative
		}
		else
		{
			tempMap.put(key, 1.0D); //Original
		}
		
		if(tempMap.isEmpty() || tempMap.keySet().stream().filter(x -> map.containsKey(x)).count() != tempMap.size())
		{
			sender.sendMessage(ChatColor.RED + bundle.getString("raid.cmd.sell.item-not-for-sale"));
			return true;
		}
		
		double price = 0.0D;
		
		for(Entry<IdData, Double> entry : tempMap.entrySet())
		{
			price += entry.getValue() * map.get(entry.getKey()).getPrice();
		}
		
		DecimalFormat df =  new DecimalFormat("#.##", new DecimalFormatSymbols(bundle.getLocale()));
		
		TextComponent text = new TextComponent(CommonsUtil.getTranslation(key, minecraftBundle));
		text.addExtra(": ");
		text.setColor(ChatColor.GREEN);
		
		TextComponent extra = new TextComponent("" + df.format(price));
		extra.setColor(ChatColor.YELLOW);
		text.addExtra(extra);
		
		CommonsUtil.sendMessage(sender, text);

		return false;
	}
	
	public Map<IdData, CryptoInfo> getPriceMap()
	{
		return map;
	}
	
	public Map<IdData, Map<IdData, Double>> getElementMap()
	{
		return elementMap;
	}
	
	public static Price getInstance()
	{
		return instance;
	}
	
	public Map<IdData, Double> getOriginalPrices(Map<IdData, Double> tempMap, IdData idData)
	{
		if(map.containsKey(idData))
		{
			tempMap.put(idData, 1.0D);
			return tempMap;
		}
		else if(!elementMap.containsKey(idData))
		{
			return null;
		}
		
		for(Entry<IdData, Double> entry : tempMap.entrySet())
		{
			
		}
		
		elementMap.get(idData);
		getOriginalPrices(tempMap, idData);
		
		return tempMap;
	}
}