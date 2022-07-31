package br.com.acenetwork.commons;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import br.com.acenetwork.commons.executor.Permission;
import br.com.acenetwork.commons.manager.IdData;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

public class CommonsUtil
{
	public static UUID getUUID(File file)
	{
		UUID uuid;
		
		if(file.exists() && file.length() > 0L)
		{
			try(FileInputStream fileIn = new FileInputStream(file);
					ByteArrayInputStream streamIn = new ByteArrayInputStream(ByteStreams.toByteArray(fileIn));
					DataInputStream in = new DataInputStream(streamIn))
			{
				uuid = new UUID(in.readLong(), in.readLong());
			}
			catch(IOException ex)
			{
				throw new RuntimeException(ex);
			}
		}
		else
		{
			uuid = UUID.randomUUID();
			
			try(FileOutputStream fileOut = new FileOutputStream(file);
					ByteArrayOutputStream streamOut = new ByteArrayOutputStream();
					DataOutputStream out = new DataOutputStream(streamOut))
			{
				out.writeLong(uuid.getMostSignificantBits());
				out.writeLong(uuid.getLeastSignificantBits());
				fileOut.write(streamOut.toByteArray());
			}
			catch(IOException ex)
			{
				throw new RuntimeException(ex);
			}
		}
		
		return uuid;
	}
	
	public static boolean addEnchant(ItemStack item, Enchantment ench, int level, boolean ignoreLevelRestriction)
	{
		ItemMeta meta = item.getItemMeta();
		boolean changed;
		
		if(item.getItemMeta() instanceof EnchantmentStorageMeta)
		{
			EnchantmentStorageMeta storage = ((EnchantmentStorageMeta) meta);
			changed = storage.addStoredEnchant(ench, level, ignoreLevelRestriction);
		}
		else
		{
			changed = meta.addEnchant(ench, level, ignoreLevelRestriction);
		}
		
		item.setItemMeta(meta);
		return changed;
	}
	
	public static boolean removeEnchant(ItemStack item, Enchantment ench)
	{
		ItemMeta meta = item.getItemMeta();
		
		boolean changed;
		
		if(meta instanceof EnchantmentStorageMeta)
		{
			EnchantmentStorageMeta storage = ((EnchantmentStorageMeta) meta);
			changed = storage.removeStoredEnchant(ench);
		}
		else
		{
			changed = meta.removeEnchant(ench);
		}
		
		item.setItemMeta(meta);
		return changed;
	}
	
	public static Map<Enchantment, Integer> getEnchants(ItemStack item)
	{
		if(!item.hasItemMeta())
		{
			return new HashMap<>();
		}
		
		if(item.getItemMeta() instanceof EnchantmentStorageMeta)
		{
			return ((EnchantmentStorageMeta) item.getItemMeta()).getStoredEnchants();
		}
		
		return item.getEnchantments();
	}
	
	public static int getEnchantmentMultiplierFromItem(Enchantment enchantment)
	{
		if(enchantment.equals(Enchantment.PROTECTION_FIRE))
		{
			return 2;
		}
		
		if(enchantment.equals(Enchantment.PROTECTION_FALL))
		{
			return 2;
		}
		
		if(enchantment.equals(Enchantment.PROTECTION_EXPLOSIONS))
		{
			return 4;
		}
		
		if(enchantment.equals(Enchantment.THORNS))
		{
			return 8;
		}
		
		if(enchantment.equals(Enchantment.OXYGEN))
		{
			return 4;
		}
		
		if(enchantment.equals(Enchantment.DEPTH_STRIDER))
		{
			return 4;
		}
		
		if(enchantment.equals(Enchantment.WATER_WORKER))
		{
			return 4;
		}
		
		if(enchantment.equals(Enchantment.DAMAGE_UNDEAD))
		{
			return 2;
		}
		
		if(enchantment.equals(Enchantment.DAMAGE_ARTHROPODS))
		{
			return 2;
		}
		
		if(enchantment.equals(Enchantment.KNOCKBACK))
		{
			return 2;
		}
		
		if(enchantment.equals(Enchantment.FIRE_ASPECT))
		{
			return 4;
		}
		
		if(enchantment.equals(Enchantment.LOOT_BONUS_MOBS))
		{
			return 2;
		}
		
		if(enchantment.equals(Enchantment.SILK_TOUCH))
		{
			return 8;
		}
		
		if(enchantment.equals(Enchantment.DURABILITY))
		{
			return 2;
		}
		
		if(enchantment.equals(Enchantment.LOOT_BONUS_BLOCKS))
		{
			return 4;
		}
		
		if(enchantment.equals(Enchantment.ARROW_KNOCKBACK))
		{
			return 4;
		}
		
		if(enchantment.equals(Enchantment.ARROW_FIRE))
		{
			return 4;
		}
		
		if(enchantment.equals(Enchantment.ARROW_INFINITE))
		{
			return 8;
		}
		
		if(enchantment.equals(Enchantment.LUCK))
		{
			return 4;
		}
		
		if(enchantment.equals(Enchantment.LURE))
		{
			return 4;
		}
		
		return 1;
	}
	
	public static int getEnchantmentMultiplierFromBook(Enchantment enchantment)
	{
		if(enchantment.equals(Enchantment.PROTECTION_EXPLOSIONS))
		{
			return 2;
		}
		
		if(enchantment.equals(Enchantment.THORNS))
		{
			return 4;
		}
		
		if(enchantment.equals(Enchantment.OXYGEN))
		{
			return 2;
		}
		
		if(enchantment.equals(Enchantment.DEPTH_STRIDER))
		{
			return 2;
		}
		
		if(enchantment.equals(Enchantment.WATER_WORKER))
		{
			return 2;
		}
		
		if(enchantment.equals(Enchantment.FIRE_ASPECT))
		{
			return 2;
		}
		
		if(enchantment.equals(Enchantment.LOOT_BONUS_MOBS))
		{
			return 2;
		}
		
		if(enchantment.equals(Enchantment.SILK_TOUCH))
		{
			return 4;
		}
		
		if(enchantment.equals(Enchantment.LOOT_BONUS_BLOCKS))
		{
			return 2;
		}
		
		if(enchantment.equals(Enchantment.ARROW_KNOCKBACK))
		{
			return 2;
		}
		
		if(enchantment.equals(Enchantment.ARROW_FIRE))
		{
			return 2;
		}
		
		if(enchantment.equals(Enchantment.ARROW_INFINITE))
		{
			return 4;
		}
		
		if(enchantment.equals(Enchantment.LUCK))
		{
			return 2;
		}
		
		if(enchantment.equals(Enchantment.LURE))
		{
			return 2;
		}
		
		return 1;
	}
	
	public static int getHighestBlockYAt(World w, int x, int z)
	{
		for(int y = w.getMaxHeight(); y >= 0; y--)
		{
			Block b =  w.getBlockAt(x, y, z);
			
			if(b.getType() != Material.AIR)
			{
				return y;
			}
		}
		
		return w.getMaxHeight();
	}
	
	public static void setCustomSkull(SkullMeta skull, String url)
	{
		if(url.isEmpty())
		{
			return;
		}
		
		try
		{
			GameProfile profile = new GameProfile(UUID.randomUUID(), null);
			
			profile.getProperties().put("textures", new Property("textures", url));
			
			Field field = skull.getClass().getDeclaredField("profile");
			
			field.setAccessible(true);
			field.set(skull, profile);
		}
		catch(IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e)
		{
			e.printStackTrace();
		}
	}
	
	public static void shuffle(int[] array, Random r)
	{
		for(int i = 0; i < array.length; i++)
		{
			int j = r.nextInt(array.length - i) + i;
			int temp = array[j];
			array[j] = array[i];
			array[i] = temp;
		}
	}
	
	public static void shuffle(byte[] array, Random r)
	{
		for(int i = 0; i < array.length; i++)
		{
			int j = r.nextInt(array.length - i) + i;
			byte temp = array[j];
			array[j] = array[i];
			array[i] = temp;
		}
	}
	
	public static void sendMessage(CommandSender sender, TextComponent text)
	{
		if(sender instanceof Player)
		{
			((Player) sender).spigot().sendMessage(text);
//			PacketPlayOutChat packet = new PacketPlayOutChat(ChatSerializer.a(text.toLegacyText()));
//			((CraftPlayer) sender).getHandle().playerConnection.sendPacket(packet);
		}
		else
		{
			sender.sendMessage(text.toLegacyText());
		}
	}
	
	public static Locale getLocaleFromMinecraft(String locale)
	{
		String[] split = locale.split("_");
		
		if(split.length == 1)
		{
			return new Locale(split[0]);
		}
		else
		{
			return new Locale(split[0], split[1]);
		}
	}
	
	public static String getTranslation(int id, short data, ResourceBundle bundle)
	{
		return getTranslation(Material.getMaterial(id), data, bundle);
	}
	
	public static String getTranslation(IdData key, ResourceBundle bundle)
	{
		return getTranslation(Material.getMaterial(key.getId()), key.getData(), bundle);
	}
	
	public static String getTranslation(String key, ResourceBundle bundle)
	{
		String[] split = key.split(":");
		
		int id = Integer.valueOf(split[0]);
		short data = Short.valueOf(split[1]);
		
		return getTranslation(Material.getMaterial(id), data, bundle);
	}
	
	public static String getTranslation(ItemStack item, ResourceBundle bundle)
	{
		return getTranslation(item.getType(), item.getDurability(), bundle);
	}
	
	public static String getTranslation(Material type, short data, ResourceBundle bundle)
	{
		switch(type)
		{
		case STONE:
			switch(data)
			{
			case 0:
				return bundle.getString("tile.stone.stone.name");
			case 1:
				return bundle.getString("tile.stone.granite.name");
			case 2:
				return bundle.getString("tile.stone.graniteSmooth.name");
			case 3:
				return bundle.getString("tile.stone.diorite.name");
			case 4:
				return bundle.getString("tile.stone.dioriteSmooth.name");
			case 5:
				return bundle.getString("tile.stone.andesite.name");
			case 6:
				return bundle.getString("tile.stone.andesiteSmooth.name");
			default:
				return null;
			}
		case GRASS:
			return bundle.getString("tile.grass.name");
		case DIRT:
			switch(data)
			{
			case 0:
				return bundle.getString("tile.dirt.name");
			case 1:
				return bundle.getString("tile.dirt.coarse.name");
			case 2:
				return bundle.getString("tile.dirt.podzol.name");
			default:
				return null;
			}
		case COBBLESTONE:
			return bundle.getString("tile.stonebrick.name");
		case WOOD:
			switch(data)
			{
			case 0:
				return bundle.getString("tile.wood.oak.name");
			case 1:
				return bundle.getString("tile.wood.spruce.name");
			case 2:
				return bundle.getString("tile.wood.birch.name");
			case 3:
				return bundle.getString("tile.wood.jungle.name");
			case 4:
				return bundle.getString("tile.wood.acacia.name");
			case 5:
				return bundle.getString("tile.wood.big_oak.name");
			default:
				return null;
			}
		case BEDROCK:
			return bundle.getString("tile.bedrock.name");
		case SAND:
			switch(data)
			{
			case 0:
				return bundle.getString("tile.sand.name");
			case 1:
				return bundle.getString("tile.sand.red.name");
			default:
				return null;
			}
		case GRAVEL:
			return bundle.getString("tile.gravel.name");
		case GOLD_ORE:
			return bundle.getString("tile.oreGold.name");
		case IRON_ORE:
			return bundle.getString("tile.oreIron.name");
		case COAL_ORE:
			return bundle.getString("tile.oreCoal.name");
		case LOG:
			switch(data)
			{
			case 0:
				return bundle.getString("tile.log.oak.name");
			case 1:
				return bundle.getString("tile.log.spruce.name");
			case 2:
				return bundle.getString("tile.log.birch.name");
			case 3:
				return bundle.getString("tile.log.jungle.name");
			default:
				return null;
			}
		case SPONGE:
			switch(data)
			{
			case 0:
				return bundle.getString("tile.sponge.dry.name");
			case 1:
				return bundle.getString("tile.sponge.wet.name");
			default:
				return null;
			}
		case GLASS:
			return bundle.getString("tile.glass.name");
		case LAPIS_ORE:
			return bundle.getString("tile.oreLapis.name");
		case LAPIS_BLOCK:
			return bundle.getString("tile.blockLapis.name");
		case SANDSTONE:
			switch(data)
			{
			case 0:
				return bundle.getString("tile.sandStone.default.name");
				case 1:
					return bundle.getString("tile.sandStone.chiseled.name");
				case 2:
					return bundle.getString("tile.sandStone.smooth.name");
				default:
					return null;
			}
		case WOOL:
			switch(data)
			{
			case 0:
				return bundle.getString("tile.cloth.white.name");
			case 1:
				return bundle.getString("tile.cloth.orange.name");
			case 2:
				return bundle.getString("tile.cloth.magenta.name");
			case 3:
				return bundle.getString("tile.cloth.lightBlue.name");
			case 4:
				return bundle.getString("tile.cloth.yellow.name");
			case 5:
				return bundle.getString("tile.cloth.lime.name");
			case 6:
				return bundle.getString("tile.cloth.pink.name");
			case 7:
				return bundle.getString("tile.cloth.gray.name");
			case 8:
				return bundle.getString("tile.cloth.silver.name");
			case 9:
				return bundle.getString("tile.cloth.cyan.name");
			case 10:
				return bundle.getString("tile.cloth.purple.name");
			case 11:
				return bundle.getString("tile.cloth.blue.name");
			case 12:
				return bundle.getString("tile.cloth.brown.name");
			case 13:
				return bundle.getString("tile.cloth.green.name");
			case 14:
				return bundle.getString("tile.cloth.red.name");
			case 15:
				return bundle.getString("tile.cloth.black.name");
			default:
				return null;
			}
		case GOLD_BLOCK:
			return bundle.getString("tile.blockGold.name");
		case IRON_BLOCK:
			return bundle.getString("tile.blockIron.name");
		case STEP:
			switch(data)
			{
			case 0:
				return bundle.getString("tile.stoneSlab.stone.name");
			case 1:
				return bundle.getString("tile.stoneSlab.sand.name");
			case 3:
				return bundle.getString("tile.stoneSlab.cobble.name");
			case 4:
				return bundle.getString("tile.stoneSlab.brick.name");
			case 5:
				return bundle.getString("tile.stoneSlab.smoothStoneBrick.name");
			case 6:
				return bundle.getString("tile.stoneSlab.netherBrick.name");
			case 7:
				return bundle.getString("tile.stoneSlab.quartz.name");
			default:
				return null;
			}
		case BRICK:
			return bundle.getString("tile.brick.name");
		case BOOKSHELF:
			return bundle.getString("tile.bookshelf.name");
		case MOSSY_COBBLESTONE:
			return bundle.getString("tile.stoneMoss.name");
		case OBSIDIAN:
			return bundle.getString("tile.obsidian.name");
		case MOB_SPAWNER:
			return bundle.getString("tile.mobSpawner.name");
		case WOOD_STAIRS:
			return bundle.getString("tile.stairsWood.name");
		case DIAMOND_ORE:
			return bundle.getString("tile.oreDiamond.name");
		case DIAMOND_BLOCK:
			return bundle.getString("tile.blockDiamond.name");
		case COBBLESTONE_STAIRS:
			return bundle.getString("tile.stairsStone.name");
		case REDSTONE_ORE:
			return bundle.getString("tile.oreRedstone.name");
		case ICE:
			return bundle.getString("tile.ice.name");
		case SNOW_BLOCK:
			return bundle.getString("tile.snow.name");
		case CLAY:
			return bundle.getString("tile.clay.name");
		case PUMPKIN:
			return bundle.getString("tile.pumpkin.name");
		case NETHERRACK:
			return bundle.getString("tile.hellrock.name");
		case SOUL_SAND:
			return bundle.getString("tile.hellsand.name");
		case GLOWSTONE:
			return bundle.getString("tile.lightgem.name");
		case JACK_O_LANTERN:
			return bundle.getString("tile.litpumpkin.name");
		case STAINED_GLASS:
			switch(data)
			{
			case 0:
				return bundle.getString("tile.stainedGlass.white.name");
			case 1:
				return bundle.getString("tile.stainedGlass.orange.name");
			case 2:
				return bundle.getString("tile.stainedGlass.magenta.name");
			case 3:
				return bundle.getString("tile.stainedGlass.lightBlue.name");
			case 4:
				return bundle.getString("tile.stainedGlass.yellow.name");
			case 5:
				return bundle.getString("tile.stainedGlass.lime.name");
			case 6:
				return bundle.getString("tile.stainedGlass.pink.name");
			case 7:
				return bundle.getString("tile.stainedGlass.gray.name");
			case 8:
				return bundle.getString("tile.stainedGlass.silver.name");
			case 9:
				return bundle.getString("tile.stainedGlass.cyan.name");
			case 10:
				return bundle.getString("tile.stainedGlass.purple.name");
			case 11:
				return bundle.getString("tile.stainedGlass.blue.name");
			case 12:
				return bundle.getString("tile.stainedGlass.brown.name");
			case 13:
				return bundle.getString("tile.stainedGlass.green.name");
			case 14:
				return bundle.getString("tile.stainedGlass.red.name");
			case 15:
				return bundle.getString("tile.stainedGlass.black.name");
			default:
				return null;
			}
		case SMOOTH_BRICK:
			switch(data)
			{
			case 0:
				return bundle.getString("tile.stonebricksmooth.default.name");
			case 1:
				return bundle.getString("tile.stonebricksmooth.mossy.name");
			case 2:
				return bundle.getString("tile.stonebricksmooth.cracked.name");
			case 3:
				return bundle.getString("tile.stonebricksmooth.chiseled.name");
			default:
				return null;
			}
		case MELON_BLOCK:
			return bundle.getString("tile.melon.name");
		case BRICK_STAIRS:
			return bundle.getString("tile.stairsBrick.name");
		case SMOOTH_STAIRS:
			return bundle.getString("tile.stairsStoneBrickSmooth.name");
		case MYCEL:
			return bundle.getString("tile.mycel.name");
		case NETHER_BRICK:
			return bundle.getString("tile.netherBrick.name");
		case NETHER_BRICK_STAIRS:
			return bundle.getString("tile.stairsNetherBrick.name");
		case ENDER_STONE:
			return bundle.getString("tile.whiteStone.name");
		case WOOD_STEP:
			switch(data)
			{
			case 0:
				return bundle.getString("tile.woodSlab.oak.name");
			case 1:
				return bundle.getString("tile.woodSlab.spruce.name");
			case 2:
				return bundle.getString("tile.woodSlab.birch.name");
			case 3:
				return bundle.getString("tile.woodSlab.jungle.name");
			case 4:
				return bundle.getString("tile.woodSlab.acacia.name");
			case 5:
				return bundle.getString("tile.woodSlab.big_oak.name");
			default:
				return null;
			}
		case SANDSTONE_STAIRS:
			return bundle.getString("tile.stairsSandStone.name");
		case EMERALD_ORE:
			return bundle.getString("tile.oreEmerald.name");
		case EMERALD_BLOCK:
			return bundle.getString("tile.blockEmerald.name");
		case SPRUCE_WOOD_STAIRS:
			return bundle.getString("tile.stairsWoodSpruce.name");
		case BIRCH_WOOD_STAIRS:
			return bundle.getString("tile.stairsWoodBirch.name");
		case JUNGLE_WOOD_STAIRS:
			return bundle.getString("tile.stairsWoodJungle.name");
		case COBBLE_WALL:
			switch(data)
			{
			case 0:
				return bundle.getString("tile.cobbleWall.normal.name");
			case 1:
				return bundle.getString("tile.cobbleWall.mossy.name");
			default:
				return null;
			}
		case QUARTZ_ORE:
			return bundle.getString("tile.netherquartz.name");
		case QUARTZ_BLOCK:
			switch(data)
			{
			case 0:
				return bundle.getString("tile.quartzBlock.default.name");
			case 1:
				return bundle.getString("tile.quartzBlock.chiseled.name");
			case 2:
				return bundle.getString("tile.quartzBlock.lines.name");
			default:
				return null;
			}
		case QUARTZ_STAIRS:
			return bundle.getString("tile.stairsQuartz.name");
		case STAINED_CLAY:
			switch(data)
			{
			case 0:
				return bundle.getString("tile.clayHardenedStained.white.name");
			case 1:
				return bundle.getString("tile.clayHardenedStained.orange.name");
			case 2:
				return bundle.getString("tile.clayHardenedStained.magenta.name");
			case 3:
				return bundle.getString("tile.clayHardenedStained.lightBlue.name");
			case 4:
				return bundle.getString("tile.clayHardenedStained.yellow.name");
			case 5:
				return bundle.getString("tile.clayHardenedStained.lime.name");
			case 6:
				return bundle.getString("tile.clayHardenedStained.pink.name");
			case 7:
				return bundle.getString("tile.clayHardenedStained.gray.name");
			case 8:
				return bundle.getString("tile.clayHardenedStained.silver.name");
			case 9:
				return bundle.getString("tile.clayHardenedStained.cyan.name");
			case 10:
				return bundle.getString("tile.clayHardenedStained.purple.name");
			case 11:
				return bundle.getString("tile.clayHardenedStained.blue.name");
			case 12:
				return bundle.getString("tile.clayHardenedStained.brown.name");
			case 13:
				return bundle.getString("tile.clayHardenedStained.green.name");
			case 14:
				return bundle.getString("tile.clayHardenedStained.red.name");
			case 15:
				return bundle.getString("tile.clayHardenedStained.black.name");
			default:
				return null;
			}
		case LOG_2:
			switch(data)
			{
			case 0:
				return bundle.getString("tile.log.acacia.name");
			case 1:
				return bundle.getString("tile.log.big_oak.name");
			default:
				return null;
			}
		case ACACIA_STAIRS:
			return bundle.getString("tile.stairsWoodAcacia.name");
		case DARK_OAK_STAIRS:
			return bundle.getString("tile.stairsWoodDarkOak.name");
		case PRISMARINE:
			switch(data)
			{
			case 0:
				return bundle.getString("tile.prismarine.rough.name");
			case 1:
				return bundle.getString("tile.prismarine.bricks.name");
			case 2:
				return bundle.getString("tile.prismarine.dark.name");
			default:
				return null;
			}
		case SEA_LANTERN:
			return bundle.getString("tile.seaLantern.name");
		case HAY_BLOCK:
			return bundle.getString("tile.hayBlock.name");
		case HARD_CLAY:
			return bundle.getString("tile.clayHardened.name");
		case COAL_BLOCK:
			return bundle.getString("tile.blockCoal.name");
		case PACKED_ICE:
			return bundle.getString("tile.icePacked.name");
		case RED_SANDSTONE:
			switch(data)
			{
			case 0:
				return bundle.getString("tile.redSandStone.default.name");
			case 1:
				return bundle.getString("tile.redSandStone.chiseled.name");
			case 2:
				return bundle.getString("tile.redSandStone.smooth.name");
			default:
				return null;
			}
		case RED_SANDSTONE_STAIRS:
			return bundle.getString("tile.stairsRedSandStone.name");
		case STONE_SLAB2:
			return bundle.getString("tile.stoneSlab2.red_sandstone.name");
		
		case SAPLING:
			switch(data)
			{
			case 0:
				return bundle.getString("tile.sapling.oak.name");
			case 1:
				return bundle.getString("tile.sapling.spruce.name");
			case 2:
				return bundle.getString("tile.sapling.birch.name");
			case 3:
				return bundle.getString("tile.sapling.jungle.name");
			case 4:
				return bundle.getString("tile.sapling.acacia.name");
			case 5:
				return bundle.getString("tile.sapling.big_oak.name");
			default:
				return null;
			}
		case LEAVES:
			switch(data)
			{
			case 0:
				return bundle.getString("tile.leaves.oak.name");
			case 1:
				return bundle.getString("tile.leaves.spruce.name");
			case 2:
				return bundle.getString("tile.leaves.birch.name");
			case 3:
				return bundle.getString("tile.leaves.jungle.name");
			default:
				return null;
			}
		case WEB:
			return bundle.getString("tile.web.name");
		case LONG_GRASS:
			switch(data)
			{
			case 0:
				return bundle.getString("tile.tallgrass.shrub.name");
			case 1:
				return bundle.getString("tile.tallgrass.grass.name");
			case 2:
				return bundle.getString("tile.tallgrass.fern.name");
			default:
				return null;
			}
		case DEAD_BUSH:
			return bundle.getString("tile.deadbush.name");
		case YELLOW_FLOWER:
			return bundle.getString("tile.flower1.dandelion.name");
		case RED_ROSE:
			switch(data)
			{
			case 0:
				return bundle.getString("tile.flower2.poppy.name");
			case 1:
				return bundle.getString("tile.flower2.blueOrchid.name");
			case 2:
				return bundle.getString("tile.flower2.allium.name");
			case 3:
				return bundle.getString("tile.flower2.houstonia.name");
			case 4:
				return bundle.getString("tile.flower2.tulipRed.name");
			case 5:
				return bundle.getString("tile.flower2.tulipOrange.name");
			case 6:
				return bundle.getString("tile.flower2.tulipWhite.name");
			case 7:
				return bundle.getString("tile.flower2.tulipPink.name");
			case 8:
				return bundle.getString("tile.flower2.oxeyeDaisy.name");
			default:
				return null;
			}
		case BROWN_MUSHROOM:
			return bundle.getString("tile.mushroom.name");
		case RED_MUSHROOM:
			return bundle.getString("tile.mushroom.name");
		case TORCH:
			return bundle.getString("tile.torch.name");
		case CHEST:
			return bundle.getString("tile.chest.name");
		case WORKBENCH:
			return bundle.getString("tile.workbench.name");
		case FURNACE:
			return bundle.getString("tile.furnace.name");
		case LADDER:
			return bundle.getString("tile.ladder.name");
		case SNOW:
			return bundle.getString("tile.snow.name");
		case CACTUS:
			return bundle.getString("tile.cactus.name");
		case JUKEBOX:
			return bundle.getString("tile.jukebox.name");
		case FENCE:
			return bundle.getString("tile.fence.name");
		case MONSTER_EGGS:
			switch(data)
			{
			case 0:
				return bundle.getString("tile.monsterStoneEgg.name");
			case 1:
				return bundle.getString("tile.monsterStoneEgg.cobble.name");
			case 2:
				return bundle.getString("tile.monsterStoneEgg.brick.name");
			case 3:
				return bundle.getString("tile.monsterStoneEgg.mossybrick.name");
			case 4:
				return bundle.getString("tile.monsterStoneEgg.mossybrick.name");
			case 5:
				return bundle.getString("tile.monsterStoneEgg.chiseledbrick.name");
			default:
				return null;
			}
		case IRON_FENCE:
			return bundle.getString("tile.fenceIron.name");
		case THIN_GLASS:
			return bundle.getString("tile.thinGlass.name");
		case VINE:
			return bundle.getString("tile.vine.name");
		case WATER_LILY:
			return bundle.getString("tile.waterlily.name");
		case NETHER_FENCE:
			return bundle.getString("tile.netherFence.name");
		case ENCHANTMENT_TABLE:
			return bundle.getString("tile.enchantmentTable.name");
		case ENDER_PORTAL_FRAME:
			return bundle.getString("tile.endPortalFrame.name");
		case ENDER_CHEST:
			return bundle.getString("tile.enderChest.name");
		case ANVIL:
			switch(data)
			{
			case 0:
				return bundle.getString("tile.anvil.name");
			case 1:
				return bundle.getString("tile.anvil.slightlyDamaged.name");
			case 2:
				return bundle.getString("tile.anvil.veryDamaged.name");
			default:
				return null;
			}
		case TRAPPED_CHEST:
			return bundle.getString("tile.chestTrap.name");
		case STAINED_GLASS_PANE:
			switch(data)
			{
			case 0:
				return bundle.getString("tile.stainedGlass.white.name");
			case 1:
				return bundle.getString("tile.stainedGlass.orange.name");
			case 2:
				return bundle.getString("tile.stainedGlass.magenta.name");
			case 3:
				return bundle.getString("tile.stainedGlass.lightBlue.name");
			case 4:
				return bundle.getString("tile.stainedGlass.yellow.name");
			case 5:
				return bundle.getString("tile.stainedGlass.lime.name");
			case 6:
				return bundle.getString("tile.stainedGlass.pink.name");
			case 7:
				return bundle.getString("tile.stainedGlass.gray.name");
			case 8:
				return bundle.getString("tile.stainedGlass.silver.name");
			case 9:
				return bundle.getString("tile.stainedGlass.cyan.name");
			case 10:
				return bundle.getString("tile.stainedGlass.purple.name");
			case 11:
				return bundle.getString("tile.stainedGlass.blue.name");		
			case 12:
				return bundle.getString("tile.stainedGlass.brown.name");
			case 13:
				return bundle.getString("tile.stainedGlass.green.name");
			case 14:
				return bundle.getString("tile.stainedGlass.red.name");
			case 15:
				return bundle.getString("tile.stainedGlass.black.name");
			default:
				return null;
			}
		case LEAVES_2:
			switch(data)
			{
			case 0:
				return bundle.getString("tile.leaves.acacia.name");
			case 1:
				return bundle.getString("tile.leaves.big_oak.name");
			default:
				return null;
			}
		case SLIME_BLOCK:
			return bundle.getString("tile.slime.name");
		case CARPET:
			switch(data)
			{
			case 0:
				return bundle.getString("tile.woolCarpet.white.name");
			case 1:
				return bundle.getString("tile.woolCarpet.orange.name");
			case 2:
				return bundle.getString("tile.woolCarpet.magenta.name");
			case 3:
				return bundle.getString("tile.woolCarpet.lightBlue.name");
			case 4:
				return bundle.getString("tile.woolCarpet.yellow.name");
			case 5:
				return bundle.getString("tile.woolCarpet.lime.name");
			case 6:
				return bundle.getString("tile.woolCarpet.pink.name");
			case 7:
				return bundle.getString("tile.woolCarpet.gray.name");
			case 8:
				return bundle.getString("tile.woolCarpet.silver.name");
			case 9:
				return bundle.getString("tile.woolCarpet.cyan.name");
			case 10:
				return bundle.getString("tile.woolCarpet.purple.name");
			case 11:
				return bundle.getString("tile.woolCarpet.blue.name");		
			case 12:
				return bundle.getString("tile.woolCarpet.brown.name");
			case 13:
				return bundle.getString("tile.woolCarpet.green.name");
			case 14:
				return bundle.getString("tile.woolCarpet.red.name");
			case 15:
				return bundle.getString("tile.woolCarpet.black.name");
			default:
				return null;
			}
		case DOUBLE_PLANT:
			switch(data)
			{
			case 0:
				return bundle.getString("tile.doublePlant.sunflower.name");
			case 1:
				return bundle.getString("tile.doublePlant.syringa.name");
			case 2:
				return bundle.getString("tile.doublePlant.grass.name");
			case 3:
				return bundle.getString("tile.doublePlant.fern.name");
			case 4:
				return bundle.getString("tile.doublePlant.rose.name");
			case 5:
				return bundle.getString("tile.doublePlant.paeonia.name");
			default:
				return null;
			}
		case SPRUCE_FENCE:
			return bundle.getString("tile.spruceFence.name");
		case BIRCH_FENCE:
			return bundle.getString("tile.birchFence.name");
		case JUNGLE_FENCE:
			return bundle.getString("tile.jungleFence.name");
		case DARK_OAK_FENCE:
			return bundle.getString("tile.darkOakFence.name");
		case ACACIA_FENCE:
			return bundle.getString("tile.acaciaFence.name");
		case PAINTING:
			return bundle.getString("item.painting.name");
		case SIGN:
			return bundle.getString("tile.sign.name");
		case BED:
			return bundle.getString("tile.bed.name");
		case ITEM_FRAME:
			return bundle.getString("item.frame.name");
		case FLOWER_POT_ITEM:
			return bundle.getString("item.flowerPot.name");
		case SKULL_ITEM:
			switch(data)
			{
			case 0:
				return bundle.getString("item.skull.skeleton.name");
			case 1:
				return bundle.getString("item.skull.wither.name");
			case 2:
				return bundle.getString("item.skull.zombie.name");
			case 3:
				return bundle.getString("item.skull.char.name");
			case 4:
				return bundle.getString("item.skull.creeper.name");
			default:
				return null;
			}
		case ARMOR_STAND:
			return bundle.getString("item.armorStand.name");
		case BANNER:
			switch(data)
			{
			case 0:
				return bundle.getString("tile.banner.white.name");
			case 1:
				return bundle.getString("tile.banner.orange.name");
			case 2:
				return bundle.getString("tile.banner.magenta.name");
			case 3:
				return bundle.getString("tile.banner.lightBlue.name");
			case 4:
				return bundle.getString("tile.banner.yellow.name");
			case 5:
				return bundle.getString("tile.banner.lime.name");
			case 6:
				return bundle.getString("tile.banner.pink.name");
			case 7:
				return bundle.getString("tile.banner.gray.name");
			case 8:
				return bundle.getString("tile.banner.silver.name");
			case 9:
				return bundle.getString("tile.banner.cyan.name");
			case 10:
				return bundle.getString("tile.banner.purple.name");
			case 11:
				return bundle.getString("tile.banner.blue.name");		
			case 12:
				return bundle.getString("tile.banner.brown.name");
			case 13:
				return bundle.getString("tile.banner.green.name");
			case 14:
				return bundle.getString("tile.banner.red.name");
			case 15:
				return bundle.getString("tile.banner.black.name");
			default:
				return null;
			}
		case DISPENSER:
            return bundle.getString("tile.dispenser.name");
        case NOTE_BLOCK:
            return bundle.getString("tile.musicBlock.name");
        case PISTON_STICKY_BASE:
            return bundle.getString("tile.pistonStickyBase.name");
        case PISTON_BASE:
            return bundle.getString("tile.pistonBase.name");        
        case TNT:
            return bundle.getString("tile.tnt.name");
        case LEVER:
            return bundle.getString("tile.lever.name");
        case STONE_PLATE:
            return bundle.getString("tile.pressurePlateStone.name");
        case WOOD_PLATE:
            return bundle.getString("tile.pressurePlateWood.name");    
        case REDSTONE_TORCH_ON:
            return bundle.getString("tile.notGate.name");
        case STONE_BUTTON:
            return bundle.getString("tile.button.name");
        case TRAP_DOOR:
            return bundle.getString("tile.trapdoor.name");
        case FENCE_GATE:
            return bundle.getString("tile.fenceGate.name");    
        case REDSTONE_LAMP_OFF:
            return bundle.getString("tile.redstoneLight.name");
        case TRIPWIRE_HOOK:
            return bundle.getString("tile.tripWireSource.name");
        case WOOD_BUTTON:
            return bundle.getString("tile.button.name");
        case GOLD_PLATE:
            return bundle.getString("tile.weightedPlate_light.name");
        case IRON_PLATE:
            return bundle.getString("tile.weightedPlate_heavy.name");
        case DAYLIGHT_DETECTOR:
            return bundle.getString("tile.daylightDetector.name");
        case REDSTONE_BLOCK:
            return bundle.getString("tile.blockRedstone.name");
        case HOPPER:
            return bundle.getString("tile.hopper.name");
        case DROPPER:
            return bundle.getString("tile.dropper.name");
        case IRON_TRAPDOOR:
            return bundle.getString("tile.ironTrapdoor.name");
        case SPRUCE_FENCE_GATE:
            return bundle.getString("tile.spruceFenceGate.name");
        case BIRCH_FENCE_GATE:
            return bundle.getString("tile.birchFenceGate.name");
        case ACACIA_FENCE_GATE:
            return bundle.getString("tile.acaciaFenceGate.name");
        case JUNGLE_FENCE_GATE:
            return bundle.getString("tile.jungleFenceGate.name");
        case DARK_OAK_FENCE_GATE:
            return bundle.getString("tile.darkOakFenceGate.name");
        case WOOD_DOOR:
            return bundle.getString("item.doorOak.name");
        case IRON_DOOR:
            return bundle.getString("item.doorIron.name");
        case REDSTONE:
            return bundle.getString("item.redstone.name");
        case REDSTONE_COMPARATOR:
            return bundle.getString("item.comparator.name");
        case DIODE:
            return bundle.getString("item.diode.name");
        case SPRUCE_DOOR_ITEM:
            return bundle.getString("item.doorSpruce.name");
        case BIRCH_DOOR_ITEM:
            return bundle.getString("item.doorBirch.name");
        case ACACIA_DOOR_ITEM:
            return bundle.getString("item.doorAcacia.name");
        case JUNGLE_DOOR_ITEM:
            return bundle.getString("item.doorJungle.name");
        case DARK_OAK_DOOR_ITEM:
            return bundle.getString("item.doorDarkOak.name");
        case BEACON:
            return bundle.getString("tile.beacon.name");
        case BUCKET:
            return bundle.getString("item.bucket.name");
        case WATER_BUCKET:
            return bundle.getString("item.bucketWater.name");
        case LAVA_BUCKET:
            return bundle.getString("item.bucketLava.name");
        case SNOW_BALL:
            return bundle.getString("item.snowball.name");
        case MILK_BUCKET:
            return bundle.getString("item.milk.name");
        case PAPER:
            return bundle.getString("item.paper.name");
        case BOOK:
            return bundle.getString("item.book.name");
        case SLIME_BALL:
            return bundle.getString("item.slimeball.name");
        case BONE:
            return bundle.getString("item.bone.name");
        case ENDER_PEARL:
            return bundle.getString("item.enderPearl.name");
        case EYE_OF_ENDER:
            return bundle.getString("item.eyeOfEnder.name");
        case MONSTER_EGG:
            String spawn = bundle.getString("item.monsterPlacer.name") + " ";
            switch(data)
            {
            case 50:
                return spawn + bundle.getString("entity.Creeper.name");
            case 51:
                return spawn + bundle.getString("entity.Skeleton.name");
            case 52:
                return spawn + bundle.getString("entity.Spider.name");
            case 54:
                return spawn + bundle.getString("entity.Zombie.name");
            case 55:
                return spawn + bundle.getString("entity.Slime.name");
            case 56:
                return spawn + bundle.getString("entity.Ghast.name");
            case 57:
                return spawn + bundle.getString("entity.PigZombie.name");
            case 58:
                return spawn + bundle.getString("entity.Enderman.name");
            case 59:
                return spawn + bundle.getString("entity.CaveSpider.name");
            case 60:
                return spawn + bundle.getString("entity.Silverfish.name");
            case 61:
                return spawn + bundle.getString("entity.Blaze.name");
            case 62:
                return spawn + bundle.getString("entity.LavaSlime.name");
            case 65:
                return spawn + bundle.getString("entity.Bat.name");
            case 66:
                return spawn + bundle.getString("entity.Witch.name");
            case 67:
                return spawn + bundle.getString("entity.Endermite.name");
            case 68:
                return spawn + bundle.getString("entity.Guardian.name");
            case 90:
                return spawn + bundle.getString("entity.Pig.name");
            case 91:
                return spawn + bundle.getString("entity.Sheep.name");
            case 92:
                return spawn + bundle.getString("entity.Cow.name");
            case 93:
                return spawn + bundle.getString("entity.Chicken.name");
            case 94:
                return spawn + bundle.getString("entity.Squid.name");
            case 95:
                return spawn + bundle.getString("entity.Wolf.name");
            case 96:
                return spawn + bundle.getString("entity.MushroomCow.name");
            case 98:
                return spawn + bundle.getString("entity.Ozelot.name");
            case 100:
                return spawn + bundle.getString("entity.EntityHorse.name");
            case 101:
                return spawn + bundle.getString("entity.Rabbit.name");
            case 120:
                return spawn + bundle.getString("entity.Villager.name");
            default:
                return null;
            }
        case EXP_BOTTLE:
            return bundle.getString("item.expBottle.name");
        case FIREBALL:
            return bundle.getString("item.fireball.name");
        case BOOK_AND_QUILL:
            return bundle.getString("item.writingBook.name");
        case EMPTY_MAP:
            return bundle.getString("item.emptyMap.name");
        case FIREWORK_CHARGE:
            return bundle.getString("item.fireworksCharge.name");      
        case IRON_BARDING:
            return bundle.getString("item.horsearmormetal.name");
        case GOLD_BARDING:
            return bundle.getString("item.horsearmorgold.name");   
        case DIAMOND_BARDING:
            return bundle.getString("item.horsearmordiamond.name");
        case GREEN_RECORD:
        case GOLD_RECORD:
        case RECORD_3:
        case RECORD_4:
        case RECORD_5:
        case RECORD_6:                                           
        case RECORD_7:
        case RECORD_8:
        case RECORD_9:
        case RECORD_10:
        case RECORD_11:
        case RECORD_12:        
            return bundle.getString("item.record.name");
        case APPLE:
            return bundle.getString("item.apple.name");
        case MUSHROOM_SOUP:
            return bundle.getString("item.mushroomStew.name");
        case BREAD:
            return bundle.getString("item.bread.name");
        case PORK:
            return bundle.getString("item.porkchopRaw.name");
        case GRILLED_PORK:
            return bundle.getString("item.porkchopCooked.name");
        case GOLDEN_APPLE:
            switch(data)
            {
            case 1:
                return bundle.getString("item.appleGold.name");
            case 0:
                return bundle.getString("item.appleGold.name");
            default:
                return null;
            }
        case RAW_FISH:
            switch(data)
            {
            case 3:
                return bundle.getString("item.fish.pufferfish.raw.name");
            case 2:
                return bundle.getString("item.fish.clownfish.raw.name");
            case 1:
                return bundle.getString("item.fish.salmon.raw.name");
            case 0:
                return bundle.getString("item.fish.cod.raw.name");
            default:
                return null;
            }
        case COOKED_FISH:
            switch(data)
            {
            case 1:
                return bundle.getString("item.fish.salmon.cooked.name");
            case 0:
                return bundle.getString("item.fish.cod.cooked.name");
            default:
                return null;
            }        
        case CAKE:
            return bundle.getString("item.cake.name");
        case COOKIE:
            return bundle.getString("item.cookie.name");
        case MELON:
            return bundle.getString("item.melon.name");
        case RAW_BEEF:
            return bundle.getString("item.beefRaw.name");            
        case COOKED_BEEF:
            return bundle.getString("item.beefCooked.name");
        case RAW_CHICKEN:
            return bundle.getString("item.chickenRaw.name");        
        case COOKED_CHICKEN:
            return bundle.getString("item.chickenCooked.name");
        case ROTTEN_FLESH:
            return bundle.getString("item.rottenFlesh.name");         
        case SPIDER_EYE:
            return bundle.getString("item.spiderEye.name");         
        case CARROT_ITEM:
            return bundle.getString("item.carrots.name");
        case POTATO_ITEM:
            return bundle.getString("item.potato.name");
        case BAKED_POTATO:
            return bundle.getString("item.potatoBaked.name");
        case POISONOUS_POTATO:
            return bundle.getString("item.potatoPoisonous.name");
        case PUMPKIN_PIE:
            return bundle.getString("item.pumpkinPie.name");
        case RABBIT:
            return bundle.getString("item.rabbitRaw.name");     
        case COOKED_RABBIT:
            return bundle.getString("item.rabbitCooked.name");
        case RABBIT_STEW:
            return bundle.getString("item.rabbitStew.name");
        case MUTTON:
            return bundle.getString("item.muttonRaw.name");
        case COOKED_MUTTON:     
            return bundle.getString("item.muttonCooked.name");

        case IRON_SPADE:
            return bundle.getString("item.shovelIron.name");
        case STONE_SPADE:
            return bundle.getString("item.shovelStone.name");
        case WOOD_SPADE:
            return bundle.getString("item.shovelWood.name");
        case DIAMOND_SPADE:
            return bundle.getString("item.shovelDiamond.name");
        case GOLD_SPADE:
            return bundle.getString("item.shovelGold.name");

        case IRON_PICKAXE:
            return bundle.getString("item.pickaxeIron.name");
        case STONE_PICKAXE:        
            return bundle.getString("item.pickaxeStone.name");
        case WOOD_PICKAXE:
            return bundle.getString("item.pickaxeWood.name");
        case DIAMOND_PICKAXE:
            return bundle.getString("item.pickaxeDiamond.name");
        case GOLD_PICKAXE:
            return bundle.getString("item.pickaxeGold.name");

        case IRON_AXE:
            return bundle.getString("item.hatchetIron.name");
        case DIAMOND_AXE:
            return bundle.getString("item.hatchetDiamond.name");
        case STONE_AXE:
            return bundle.getString("item.hatchetStone.name");
        case WOOD_AXE:
            return bundle.getString("item.hatchetWood.name");
        case GOLD_AXE:
            return bundle.getString("item.hatchetGold.name");
            
        case STONE_SWORD:
            return bundle.getString("item.swordStone.name");
        case WOOD_SWORD:
            return bundle.getString("item.swordWood.name");
        case DIAMOND_SWORD:
            return bundle.getString("item.swordDiamond.name");
        case IRON_SWORD:
            return bundle.getString("item.swordIron.name");
        case GOLD_SWORD:
            return bundle.getString("item.swordGold.name");

        case GOLD_HOE:
            return bundle.getString("item.hoeGold.name");
        case IRON_HOE:
            return bundle.getString("item.hoeIron.name");
        case STONE_HOE:
            return bundle.getString("item.hoeStone.name");
        case DIAMOND_HOE:
            return bundle.getString("item.hoeDiamond.name");
        case WOOD_HOE:
            return bundle.getString("item.hoeWood.name");

        case FLINT_AND_STEEL:
            return bundle.getString("item.flintAndSteel.name");  
        case COMPASS:
            return bundle.getString("item.compass.name");     
        case FISHING_ROD:
            return bundle.getString("item.fishingRod.name");     
        case WATCH:
            return bundle.getString("item.clock.name");
        case SHEARS:
            return bundle.getString("item.shears.name"); 
        case LEASH:
            return bundle.getString("item.leash.name");  
        case NAME_TAG:
            return bundle.getString("item.nameTag.name");
        case BOW:
            return bundle.getString("item.bow.name");    
        case ARROW:
            return bundle.getString("item.arrow.name");    

        case LEATHER_HELMET:
            return bundle.getString("item.helmetCloth.name");    
        case GOLD_HELMET:
            return bundle.getString("item.helmetGold.name");    
        case DIAMOND_HELMET:
            return bundle.getString("item.helmetDiamond.name");    
        case IRON_HELMET:
            return bundle.getString("item.helmetIron.name");    
        case CHAINMAIL_HELMET:
            return bundle.getString("item.helmetChain.name");    

        case LEATHER_CHESTPLATE:
            return bundle.getString("item.chestplateCloth.name");    
        case GOLD_CHESTPLATE:
            return bundle.getString("item.chestplateGold.name");    
        case DIAMOND_CHESTPLATE:
            return bundle.getString("item.chestplateDiamond.name");    
        case IRON_CHESTPLATE:
            return bundle.getString("item.chestplateIron.name");    
        case CHAINMAIL_CHESTPLATE:
            return bundle.getString("item.chestplateChain.name");                
        
        case LEATHER_LEGGINGS:
            return bundle.getString("item.leggingsCloth.name");    
        case GOLD_LEGGINGS:
            return bundle.getString("item.leggingsGold.name");    
        case DIAMOND_LEGGINGS:
            return bundle.getString("item.leggingsDiamond.name");    
        case IRON_LEGGINGS:
            return bundle.getString("item.leggingsIron.name");    
        case CHAINMAIL_LEGGINGS:
            return bundle.getString("item.leggingsChain.name");    

        case LEATHER_BOOTS:
            return bundle.getString("item.bootsCloth.name");    
        case GOLD_BOOTS:
            return bundle.getString("item.bootsGold.name");    
        case DIAMOND_BOOTS:
            return bundle.getString("item.bootsDiamond.name");    
        case IRON_BOOTS:
            return bundle.getString("item.bootsIron.name");    
        case CHAINMAIL_BOOTS:
            return bundle.getString("item.bootsChain.name");            
        
        case GHAST_TEAR:
            return bundle.getString("item.ghastTear.name");        

        case POTION:
            switch(data)
            {
            case 8193:
            case 8225:
            case 8257:
                return bundle.getString("potion.regeneration.postfix");
            case 16385:
            case 16417:
            case 16449:
                return bundle.getString("potion.regeneration.postfix");
            case 8194:
            case 8226:
            case 8258:
                return bundle.getString("potion.moveSpeed.postfix");
            case 16386:
            case 16418:
            case 16450:
                return bundle.getString("potion.moveSpeed.postfix");
            case 8227:
            case 8259:
                return bundle.getString("potion.fireResistance.postfix");        
            case 16387:
            case 16419:
                return bundle.getString("potion.fireResistance.postfix");
            case 8196:
            case 8228:
            case 8260:
                return bundle.getString("potion.poison.postfix");        
            case 16388:
            case 16420:
            case 16452:
                return bundle.getString("potion.poison.postfix");
            case 8229:
            case 8261:
                return bundle.getString("potion.heal.postfix");        
            case 16421:
            case 16453:
                return bundle.getString("potion.heal.postfix");
            case 8230:
            case 8262:
                return bundle.getString("potion.nightVision.postfix");        
            case 16422:
            case 16454:
                return bundle.getString("potion.nightVision.postfix");
            case 8232:
            case 8264:
                return bundle.getString("potion.weakness.postfix");        
            case 16424:
            case 16456:
                return bundle.getString("potion.weakness.postfix");
            case 8201: 
            case 8233:
            case 8265:
                return bundle.getString("potion.damageBoost.postfix");        
            case 16393:
            case 16425:
            case 16457:
                return bundle.getString("potion.damageBoost.postfix");
            case 8234:
            case 8266:
                return bundle.getString("potion.moveSlowdown.postfix");                    
            case 16426:
            case 16458:
                return bundle.getString("potion.moveSlowdown.postfix");
            case 8235:
            case 8267:
                return bundle.getString("potion.jump.postfix");                    
            case 16427:
            case 16459:
                return bundle.getString("potion.jump.postfix");           
            case 8236:
            case 8268:
                return bundle.getString("potion.harm.postfix");                    
            case 16428:
            case 16460:
                return bundle.getString("potion.harm.postfix");
            case 8237:
            case 8269:
                return bundle.getString("potion.waterBreathing.postfix");                    
            case 16429:
            case 16461:
                return bundle.getString("potion.waterBreathing.postfix");   
            case 8238:
            case 8270:
                return bundle.getString("potion.invisibility.postfix");                    
            case 16430:
            case 16462:
                return bundle.getString("potion.invisibility.postfix");               
            default:
                return null;
            }
        case GLASS_BOTTLE:
            return bundle.getString("item.glassBottle.name");   
        case BLAZE_POWDER:   
            return bundle.getString("item.blazePowder.name");
        case FERMENTED_SPIDER_EYE:
            return bundle.getString("item.fermentedSpiderEye.name");  
        case MAGMA_CREAM:
            return bundle.getString("item.magmaCream.name");
        case BREWING_STAND_ITEM:
            return bundle.getString("item.brewingStand.name");
        case CAULDRON_ITEM:             
            return bundle.getString("item.cauldron.name");
        case SPECKLED_MELON:
            return bundle.getString("item.speckledMelon.name");
        case GOLDEN_CARROT:
            return bundle.getString("item.carrotGolden.name");
        case RABBIT_FOOT:
            return bundle.getString("item.rabbitFoot.name");
        
        case COAL:
            switch(data)
            {
            case 0:
                return bundle.getString("item.coal.name");
            case 1:
                return bundle.getString("item.charcoal.name");
            }
        case DIAMOND:
            return bundle.getString("item.diamond.name");
        case IRON_INGOT:
            return bundle.getString("item.ingotIron.name");
        case GOLD_INGOT:
            return bundle.getString("item.ingotGold.name");
        case STICK:
            return bundle.getString("item.stick.name");
        case BOWL:
            return bundle.getString("item.bowl.name");
        case STRING:
            return bundle.getString("item.string.name");
        case FEATHER:
            return bundle.getString("item.feather.name");
        case SULPHUR:
            return bundle.getString("item.sulphur.name");
        case SEEDS:
            return bundle.getString("item.seeds.name");
        case WHEAT:
            return bundle.getString("item.wheat.name");
        case FLINT:
            return bundle.getString("item.flint.name");
        case LEATHER:
            return bundle.getString("item.leather.name");
        case CLAY_BRICK:
            return bundle.getString("item.brick.name");
        case CLAY_BALL:
            return bundle.getString("item.clay.name");
        case SUGAR_CANE:
            return bundle.getString("item.reeds.name");
        case EGG:
            return bundle.getString("item.egg.name");
        case GLOWSTONE_DUST:
            return bundle.getString("item.yellowDust.name");
        case INK_SACK:
            switch(data)
            {
            case 0:
                return bundle.getString("item.dyePowder.black.name");
            case 1:
                return bundle.getString("item.dyePowder.red.name");
            case 2:
                return bundle.getString("item.dyePowder.green.name");
            case 3:
                return bundle.getString("item.dyePowder.brown.name");
            case 4:
                return bundle.getString("item.dyePowder.blue.name");
            case 5:
                return bundle.getString("item.dyePowder.purple.name");
            case 6:
                return bundle.getString("item.dyePowder.cyan.name");
            case 7:
                return bundle.getString("item.dyePowder.silver.name");
            case 8:
                return bundle.getString("item.dyePowder.gray.name");
            case 9:
                return bundle.getString("item.dyePowder.pink.name");
            case 10:
                return bundle.getString("item.dyePowder.lime.name");
            case 11:
                return bundle.getString("item.dyePowder.yellow.name");
            case 12:
                return bundle.getString("item.dyePowder.lightBlue.name");
            case 13:
                return bundle.getString("item.dyePowder.magenta.name");
            case 14:
                return bundle.getString("item.dyePowder.orange.name");
            case 15:
                return bundle.getString("item.dyePowder.white.name");
            default:
                return null;
            } 
        case SUGAR:
            return bundle.getString("item.sugar.name");     
        case PUMPKIN_SEEDS:
            return bundle.getString("item.seeds_pumpkin.name");     
        case MELON_SEEDS:
            return bundle.getString("item.seeds_melon.name"); 
        case BLAZE_ROD:
            return bundle.getString("item.blazeRod.name");   
        case GOLD_NUGGET:
            return bundle.getString("item.goldNugget.name");
        case NETHER_STALK:
            return bundle.getString("item.netherStalkSeeds.name");
        case EMERALD:
            return bundle.getString("item.emerald.name");
        case NETHER_STAR:
            return bundle.getString("item.netherStar.name");
        case NETHER_BRICK_ITEM:
            return bundle.getString("item.netherbrick.name");
        case QUARTZ:
            return bundle.getString("item.netherquartz.name");
        case PRISMARINE_CRYSTALS:
            return bundle.getString("item.prismarineCrystals.name");
        case PRISMARINE_SHARD:
            return bundle.getString("item.prismarineShard.name");
        case RABBIT_HIDE:
            return bundle.getString("item.rabbitHide.name");
        case ENCHANTED_BOOK:
        	return bundle.getString("item.enchantedBook.name");
        default:
			return null;
		}
	}
	
	public static boolean hasPermission(OfflinePlayer op, String perm)
	{
		Permission pex = Permission.getInstance();
		
		for(Entry<String, Long> entry : pex.loadUser(op.getUniqueId()).entrySet())
		{
			String key = entry.getKey();
			long value = entry.getValue();
			
			if((value == 0 || value - System.currentTimeMillis() > 0L) 
					&& ((key.endsWith("*") && perm.startsWith(key.substring(0, key.length() - 1)))
					|| perm.equals(key)))
			{
				return true;
			}
		}
		
		Map<String, Map<String, Long>> gp = pex.getGroupPermission();
		
		for(String group : pex.getUserGroupList(op).keySet())
		{
			if(!gp.containsKey(group))
			{
				continue;
			}
			
			for(Entry<String, Long> entry : gp.get(group).entrySet())
			{
				String key = entry.getKey();
				long value = entry.getValue();
				
				if((value == 0 || value - System.currentTimeMillis() > 0L) 
						&& ((key.endsWith("*") && perm.startsWith(key.substring(0, key.length() - 1)))
						|| perm.equals(key)))
				{
					return true;
				}
			}
		}
		
		return false;
	}
	
	public static boolean permissionSyntaxIsValid(String perm)
	{
		if(!perm.matches("[a-z.*]{0,30}") || perm.startsWith(".") || perm.endsWith("."))
		{
			return false;
		}

		if(perm.contains("*") && !perm.equals("*") && !perm.endsWith(".*"))
		{
			return false;
		}

		for(int i = 0; i + 1 < perm.length(); i++)
		{
			if((perm.charAt(i) == '.' && perm.charAt(i + 1) == '.') || perm.charAt(i) == '*')
			{
				return false;
			}
		}

		return true;
	}

	public static void bungeeKickPlayer(String playerName, String kickMessage)
	{
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		
		out.writeUTF("kick");
		out.writeUTF(playerName);
		out.writeUTF(kickMessage);
		
		Bukkit.getServer().sendPluginMessage(Common.getInstance(), "commons:commons", out.toByteArray());
	}
	
	public static boolean compareDisplayName(ItemStack i1, ItemStack i2)
	{
		return  i1 != null && i1.hasItemMeta() && i1.getItemMeta().hasDisplayName() && 
				i2 != null && i2.hasItemMeta() && i2.getItemMeta().hasDisplayName() && 
				i1.getItemMeta().getDisplayName().equals(i2.getItemMeta().getDisplayName());
	}
	
	public static void bungeeSendPlayer(String playerName, String serverInfo)
	{
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		
		out.writeUTF("sendplayer");
		out.writeUTF(playerName);
		out.writeUTF(serverInfo);
		
		Bukkit.getServer().sendPluginMessage(Common.getInstance(), "commons:commons", out.toByteArray());
	}
	
	public static boolean groupSyntaxIsValid(String group)
	{
		return group.matches("[a-z]{0,16}");
	}
	
	public static String[] split(MessageFormat mf)
	{
		Object[] argss = new Object[mf.getFormatsByArgumentIndex().length];
		
		String regex = "?+-+?";
		
		for(int i = 0; i < argss.length; i++)
		{
			argss[i] = regex;
		}
		
		return mf.format(argss).split(regex);
	}

	public static OfflinePlayer getOfflinePlayerIfCached(String name)
	{
		return Arrays.stream(Bukkit.getOfflinePlayers()).filter(x -> x.getName().equalsIgnoreCase(name)).findAny().orElse(null);
	}
	
	public static String getTranslationLevel(int level)
	{
		switch(level)
		{
		case 1:
			return "enchantment.level.1";
		case 2:
			return "enchantment.level.2";
		case 3:
			return "enchantment.level.3";
		case 4:
			return "enchantment.level.4";
		case 5:
			return "enchantment.level.5";
		case 6:
			return "enchantment.level.6";
		case 7:
			return "enchantment.level.7";
		case 8:
			return "enchantment.level.8";
		case 9:
			return "enchantment.level.9";
		case 10:
			return "enchantment.level.10";
		default:
			return level + "";
		}
	}
	
	public static String getTranslation(Enchantment enchantment)
	{
		if(enchantment.equals(Enchantment.DAMAGE_ALL))
		{
			return "enchantment.damage.all";
		}
		else if(enchantment.equals(Enchantment.DAMAGE_UNDEAD))
		{
			return "enchantment.damage.undead";
		}
		else if(enchantment.equals(Enchantment.DAMAGE_ARTHROPODS))
		{
			return "enchantment.damage.arthropods";
		}
		else if(enchantment.equals(Enchantment.KNOCKBACK))
		{
			return "enchantment.knockback";
		}
		else if(enchantment.equals(Enchantment.FIRE_ASPECT))
		{
			return "enchantment.fire";
		}
		else if(enchantment.equals(Enchantment.PROTECTION_ENVIRONMENTAL))
		{
			return "enchantment.protect.all";
		}
		else if(enchantment.equals(Enchantment.PROTECTION_FIRE))
		{
			return "enchantment.protect.fire";
		}
		else if(enchantment.equals(Enchantment.PROTECTION_FALL))
		{
			return "enchantment.protect.fall";
		}
		else if(enchantment.equals(Enchantment.PROTECTION_EXPLOSIONS))
		{
			return "enchantment.protect.explosion";
		}
		else if(enchantment.equals(Enchantment.PROTECTION_PROJECTILE))
		{
			return "enchantment.protect.projectile";
		}
		else if(enchantment.equals(Enchantment.OXYGEN))
		{
			return "enchantment.oxygen";
		}
		else if(enchantment.equals(Enchantment.WATER_WORKER))
		{
			return "enchantment.waterWorker";
		}
		else if(enchantment.equals(Enchantment.DEPTH_STRIDER))
		{
			return "enchantment.waterWalker";
		}
		else if(enchantment.equals(Enchantment.DIG_SPEED))
		{
			return "enchantment.digging";
		}
		else if(enchantment.equals(Enchantment.SILK_TOUCH))
		{
			return "enchantment.untouching";
		}
		else if(enchantment.equals(Enchantment.DURABILITY))
		{
			return "enchantment.durability";
		}
		else if(enchantment.equals(Enchantment.LOOT_BONUS_MOBS))
		{
			return "enchantment.lootBonus";
		}
		else if(enchantment.equals(Enchantment.LOOT_BONUS_BLOCKS))
		{
			return "enchantment.lootBonusDigger";
		}
		else if(enchantment.equals(Enchantment.LUCK))
		{
			return "enchantment.lootBonusFishing";
		}
		else if(enchantment.equals(Enchantment.LURE))
		{
			return "enchantment.fishingSpeed";
		}
		else if(enchantment.equals(Enchantment.ARROW_DAMAGE))
		{
			return "enchantment.arrowDamage";
		}
		else if(enchantment.equals(Enchantment.ARROW_FIRE))
		{
			return "enchantment.arrowFire";
		}
		else if(enchantment.equals(Enchantment.ARROW_KNOCKBACK))
		{
			return "enchantment.arrowKnockback";
		}
		else if(enchantment.equals(Enchantment.ARROW_INFINITE))
		{
			return "enchantment.arrowInfinite";
		}
		else if(enchantment.equals(Enchantment.THORNS))
		{
			return "enchantment.thorns";
		}
		
		return null;
	}
	
	public static <T> void shuffleList(Random r, List<T> list)
	{
		List<T> newList = new ArrayList<>();
		
		while(!list.isEmpty())
		{
			newList.add(list.remove(r.nextInt(list.size())));
		}
		
		list.addAll(newList);
	}
	
	public static List<UUID> getHiddenUUIDs(ItemStack item)
	{
		List<UUID> list = new ArrayList<>();
		
		if(item == null)
		{
			return list;
		}
		
		ItemMeta meta = item.getItemMeta();
		
		if(meta == null || !meta.hasDisplayName())
		{
			return list;
		}
		
		String displayName = meta.getDisplayName();
		
		for(int i = 0; ; i += 66)
		{
			try
			{
				list.add(convertHiddenUUID(displayName.substring(i, i + 64)));
			}
			catch(Exception e)
			{
				break;
			}
		}
		
		return list;
	}
	
	public static boolean compareUUID(ItemStack i1, ItemStack i2)
	{
		if(!i1.hasItemMeta() || !i2.hasItemMeta())
		{
			return false;
		}
		
		if(!i1.getItemMeta().hasDisplayName() || !i2.getItemMeta().hasDisplayName())
		{
			return false;
		}
		
		if(i1.getItemMeta().getDisplayName().length() < 64 || i2.getItemMeta().getDisplayName().length() < 64)
		{
			return false;
		}
		
		String hiddenData = i1.getItemMeta().getDisplayName().substring(
				i1.getItemMeta().getDisplayName().length() - 64, i1.getItemMeta().getDisplayName().length());
		
		return i2.getItemMeta().getDisplayName().endsWith(hiddenData);
	}
	
	public static void setItemCopyOf(ItemStack i1, ItemStack i2)
	{
		i1.setType(i2.getType());
		i1.setAmount(i2.getAmount());
		i1.setDurability(i2.getDurability());
		i1.setData(i2.getData());
		i1.setItemMeta(i2.getItemMeta());
	}
	
	public static boolean containsUUID(ItemStack i, UUID uuid)
	{
		return uuid == null ? false : containsUUID(i, hideUUID(uuid));
	}
	
	public static boolean containsUUID(ItemStack i, String hiddenData)
	{
		if(i == null || hiddenData == null)
		{
			return false;
		}
		
		if(!i.hasItemMeta())
		{
			return false;
		}
		
		if(!i.getItemMeta().hasDisplayName())
		{
			return false;
		}
		
		return i.getItemMeta().getDisplayName().contains(hiddenData);
	}
	
	public static UUID convertHiddenUUID(String hiddenUUID) throws Exception
	{
		hiddenUUID = hiddenUUID.replace("" + ChatColor.COLOR_CHAR, "");
		return UUID.fromString(hiddenUUID.substring(0, 8) + "-" 
				+ hiddenUUID.substring(8, 12) + "-" 
				+ hiddenUUID.substring(12, 16) + "-" 
				+ hiddenUUID.substring(16, 20) + "-" 
				+ hiddenUUID.substring(20, 32));
	}
	
	public static String getHiddenLastUUID(ItemStack item) throws Exception
	{
		String displayName = item.getItemMeta().getDisplayName();
		String hiddenLastUUID = displayName.substring(displayName.length() - 64, displayName.length());
		return hiddenLastUUID;
	}
	
	public static String getRandomItemUUID()
	{
		return hideUUID(UUID.randomUUID());
	}
	
	public static String hideUUID(UUID uuid)
	{
		String hiddenData = "";
		
		for(char c : uuid.toString().replace("-", "").toCharArray())
		{
			hiddenData += ChatColor.COLOR_CHAR + "" + c;
		}
		
		return hiddenData;
	}

	public static String hideNumberData(long number)
	{
		String hiddenData = "";
		
		for(char c : String.valueOf(number).toCharArray())
		{
			hiddenData += ChatColor.COLOR_CHAR + "" + c;
		}
		
		return hiddenData;
	}
	
	public static boolean isDispensable(Material type)
	{
		return isDispensable(type, (short) 0);
	}
	
	public static boolean isInteractable(Material type)
	{
		return isInteractable(type, (short) 0);
	}
	
	public static boolean isInteractable(Material type, short data)
	{
		if(type.name().contains("DOOR"))
		{
			return true;
		}
		
		switch(type)
		{
		case CHEST:
		case WORKBENCH:
		case FURNACE:
		case BURNING_FURNACE:
		case JUKEBOX:
		case ENCHANTMENT_TABLE:
		case ENDER_CHEST:
		case ANVIL:
		case TRAPPED_CHEST:
		case BED_BLOCK:
		case FLOWER_POT:
		case DISPENSER:
		case NOTE_BLOCK:
		case LEVER:
		case GOLD_PLATE:
		case IRON_PLATE:
		case STONE_PLATE:
		case WOOD_PLATE:
		case STONE_BUTTON:
		case WOOD_BUTTON:
		case DAYLIGHT_DETECTOR:
		case DAYLIGHT_DETECTOR_INVERTED:
		case TRAP_DOOR:
		case IRON_TRAPDOOR:
		case IRON_DOOR_BLOCK:
		case ACACIA_DOOR:
		case BIRCH_DOOR:
		case DARK_OAK_DOOR:
		case IRON_DOOR:
		case JUNGLE_DOOR:
		case WOOD_DOOR:
		case SPRUCE_DOOR:
		case WOODEN_DOOR:
		case ACACIA_FENCE_GATE:
		case BIRCH_FENCE_GATE:
		case DARK_OAK_FENCE_GATE:
		case FENCE_GATE:
		case JUNGLE_FENCE_GATE:
		case SPRUCE_FENCE_GATE:
		case ACACIA_FENCE:
		case FENCE:
		case BIRCH_FENCE:
		case DARK_OAK_FENCE:
		case JUNGLE_FENCE:
		case NETHER_FENCE:
		case SPRUCE_FENCE:
		default:
			return false;
		}
	}
	
	public static boolean isDispensable(Material type, short data)
	{
		switch(type)
		{
		case WATER_BUCKET:
		case LAVA_BUCKET:
		case BUCKET:
		case ARMOR_STAND:
		case BOAT:
		case MINECART:
		case POWERED_MINECART:
		case STORAGE_MINECART:
		case COMMAND_MINECART:
		case EXPLOSIVE_MINECART:
		case HOPPER_MINECART:
		case FLINT_AND_STEEL:
		case TNT:
		case SHEARS:
		case GLASS_BOTTLE:
		case POTION:
			return true;
		case INK_SACK:
			if(data != 15)
			{
				return false;
			}
			
			return true;
		default:
			return false;
		}
	}

	public static boolean isPVP(EntityDamageEvent e)
	{
		if(!(e instanceof EntityDamageByEntityEvent))
		{
			return false;
		}
		
		EntityDamageByEntityEvent ee = (EntityDamageByEntityEvent) e;
		
		if(ee.getDamager() instanceof Player)
		{
			return true;
		}
		
		if(ee.getDamager() instanceof Projectile && ((Projectile) ee.getDamager()).getShooter() instanceof Player)
		{
			return true;
		}
		
		return false;
	}
	
	public static boolean isBedObstructed(Block b)
	{
		if(b == null || b.getType() != Material.BED_BLOCK)
		{
			return true;
		}
		
		return b.getRelative(BlockFace.UP).getType() != Material.AIR;
	}
	
	public static Item dropItem(Player p, ItemStack itemStack)
	{
		Item item = p.getWorld().dropItem(p.getLocation().add(0.0D, 1.25D, 0.0D), itemStack);
		item.setVelocity(p.getLocation().getDirection().multiply(0.35D));
		return item;
	}
}