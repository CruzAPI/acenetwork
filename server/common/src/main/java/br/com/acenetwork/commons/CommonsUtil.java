package br.com.acenetwork.commons;

import java.io.File;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Locale;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import br.com.acenetwork.commons.manager.CommonsConfig;
import br.com.acenetwork.commons.manager.CommonsConfig.Type;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.TranslatableComponent;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;

public class CommonsUtil
{
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
	
	public static String getTranslation(String key)
	{
		String[] split = key.split(":");
		
		int id = Integer.valueOf(split[0]);
		short data = Short.valueOf(split[1]);
		
		return getTranslation(Material.getMaterial(id), data);
	}
	
	public static String getTranslation(ItemStack item)
	{
		return getTranslation(item.getType(), item.getDurability());
	}
	
	public static String getTranslation(Material type, short data)
	{
		switch(type)
		{
		case STONE:
			switch(data)
			{
			case 0:
				return "tile.stone.stone.name";
			case 1:
				return "tile.stone.granite.name";
			case 2:
				return "tile.stone.graniteSmooth.name";
			case 3:
				return "tile.stone.diorite.name";
			case 4:
				return "tile.stone.dioriteSmooth.name";
			case 5:
				return "tile.stone.andesite.name";
			case 6:
				return "tile.stone.andesiteSmooth.name";
			default:
				return null;
			}
		case GRASS:
			return "tile.grass.name";
		case DIRT:
			switch(data)
			{
			case 0:
				return "tile.dirt.name";
			case 1:
				return "tile.dirt.coarse.name";
			case 2:
				return "tile.dirt.podzol.name";
			default:
				return null;
			}
		case COBBLESTONE:
			return "tile.stonebrick.name";
		case WOOD:
			switch(data)
			{
			case 0:
				return "tile.wood.oak.name";
			case 1:
				return "tile.wood.spruce.name";
			case 2:
				return "tile.wood.birch.name";
			case 3:
				return "tile.wood.jungle.name";
			case 4:
				return "tile.wood.acacia.name";
			case 5:
				return "tile.wood.big_oak.name";
			default:
				return null;
			}
		case BEDROCK:
			return "tile.bedrock.name";
		case SAND:
			switch(data)
			{
			case 0:
				return "tile.sand.name";
			case 1:
				return "tile.sand.red.name";
			default:
				return null;
			}
		case GRAVEL:
			return "tile.gravel.name";
		case GOLD_ORE:
			return "tile.oreGold.name";
		case IRON_ORE:
			return "tile.oreIron.name";
		case COAL_ORE:
			return "tile.oreCoal.name";
		case LOG:
			switch(data)
			{
			case 0:
				return "tile.log.oak.name";
			case 1:
				return "tile.log.spruce.name";
			case 2:
				return "tile.log.birch.name";
			case 3:
				return "tile.log.jungle.name";
			default:
				return null;
			}
		case SPONGE:
			switch(data)
			{
			case 0:
				return "tile.sponge.dry.name";
			case 1:
				return "tile.sponge.wet.name";
			default:
				return null;
			}
		case GLASS:
			return "tile.glass.name";
		case LAPIS_ORE:
			return "tile.oreLapis.name";
		case LAPIS_BLOCK:
			return "tile.blockLapis.name";
		case SANDSTONE:
			switch(data)
			{
			case 0:
				return "tile.sandStone.default.name";
				case 1:
					return "tile.sandStone.chiseled.name";
				case 2:
					return "tile.sandStone.smooth.name";
				default:
					return null;
			}
		case WOOL:
			switch(data)
			{
			case 0:
				return "tile.cloth.white.name";
			case 1:
				return "tile.cloth.orange.name";
			case 2:
				return "tile.cloth.magenta.name";
			case 3:
				return "tile.cloth.lightBlue.name";
			case 4:
				return "tile.cloth.yellow.name";
			case 5:
				return "tile.cloth.lime.name";
			case 6:
				return "tile.cloth.pink.name";
			case 7:
				return "tile.cloth.gray.name";
			case 8:
				return "tile.cloth.silver.name";
			case 9:
				return "tile.cloth.cyan.name";
			case 10:
				return "tile.cloth.purple.name";
			case 11:
				return "tile.cloth.blue.name";
			case 12:
				return "tile.cloth.brown.name";
			case 13:
				return "tile.cloth.green.name";
			case 14:
				return "tile.cloth.red.name";
			case 15:
				return "tile.cloth.black.name";
			default:
				return null;
			}
		case GOLD_BLOCK:
			return "tile.blockGold.name";
		case IRON_BLOCK:
			return "tile.blockIron.name";
		case STEP:
			switch(data)
			{
			case 0:
				return "tile.stoneSlab.stone.name";
			case 1:
				return "tile.stoneSlab.sand.name";
			case 3:
				return "tile.stoneSlab.cobble.name";
			case 4:
				return "tile.stoneSlab.brick.name";
			case 5:
				return "tile.stoneSlab.smoothStoneBrick.name";
			case 6:
				return "tile.stoneSlab.netherBrick.name";
			case 7:
				return "tile.stoneSlab.quartz.name";
			default:
				return null;
			}
		case BRICK:
			return "tile.brick.name";
		case BOOKSHELF:
			return "tile.bookshelf.name";
		case MOSSY_COBBLESTONE:
			return "tile.stoneMoss.name";
		case OBSIDIAN:
			return "tile.obsidian.name";
		case MOB_SPAWNER:
			return "tile.mobSpawner.name";
		case WOOD_STAIRS:
			return "tile.stairsWood.name";
		case DIAMOND_ORE:
			return "tile.oreDiamond.name";
		case DIAMOND_BLOCK:
			return "tile.blockDiamond.name";
		case COBBLESTONE_STAIRS:
			return "tile.stairsStone.name";
		case REDSTONE_ORE:
			return "tile.oreRedstone.name";
		case ICE:
			return "tile.ice.name";
		case SNOW_BLOCK:
			return "tile.snow.name";
		case CLAY:
			return "tile.clay.name";
		case PUMPKIN:
			return "tile.pumpkin.name";
		case NETHERRACK:
			return "tile.hellrock.name";
		case SOUL_SAND:
			return "tile.hellsand.name";
		case GLOWSTONE:
			return "tile.lightgem.name";
		case JACK_O_LANTERN:
			return "tile.litpumpkin.name";
		case STAINED_GLASS:
			switch(data)
			{
			case 0:
				return "tile.stainedGlass.white.name";
			case 1:
				return "tile.stainedGlass.orange.name";
			case 2:
				return "tile.stainedGlass.magenta.name";
			case 3:
				return "tile.stainedGlass.lightBlue.name";
			case 4:
				return "tile.stainedGlass.yellow.name";
			case 5:
				return "tile.stainedGlass.lime.name";
			case 6:
				return "tile.stainedGlass.pink.name";
			case 7:
				return "tile.stainedGlass.gray.name";
			case 8:
				return "tile.stainedGlass.silver.name";
			case 9:
				return "tile.stainedGlass.cyan.name";
			case 10:
				return "tile.stainedGlass.purple.name";
			case 11:
				return "tile.stainedGlass.blue.name";
			case 12:
				return "tile.stainedGlass.brown.name";
			case 13:
				return "tile.stainedGlass.green.name";
			case 14:
				return "tile.stainedGlass.red.name";
			case 15:
				return "tile.stainedGlass.black.name";
			default:
				return null;
			}
		case SMOOTH_BRICK:
			switch(data)
			{
			case 0:
				return "tile.stonebricksmooth.default.name";
			case 1:
				return "tile.stonebricksmooth.mossy.name";
			case 2:
				return "tile.stonebricksmooth.cracked.name";
			case 3:
				return "tile.stonebricksmooth.chiseled.name";
			default:
				return null;
			}
		case MELON_BLOCK:
			return "tile.melon.name";
		case BRICK_STAIRS:
			return "tile.stairsBrick.name";
		case SMOOTH_STAIRS:
			return "tile.stairsStoneBrickSmooth.name";
		case MYCEL:
			return "tile.mycel.name";
		case NETHER_BRICK:
			return "tile.netherBrick.name";
		case NETHER_BRICK_STAIRS:
			return "tile.stairsNetherBrick.name";
		case ENDER_STONE:
			return "tile.whiteStone.name";
		case WOOD_STEP:
			switch(data)
			{
			case 0:
				return "tile.woodSlab.oak.name";
			case 1:
				return "tile.woodSlab.spruce.name";
			case 2:
				return "tile.woodSlab.birch.name";
			case 3:
				return "tile.woodSlab.jungle.name";
			case 4:
				return "tile.woodSlab.acacia.name";
			case 5:
				return "tile.woodSlab.big_oak.name";
			default:
				return null;
			}
		case SANDSTONE_STAIRS:
			return "tile.stairsSandStone.name";
		case EMERALD_ORE:
			return "tile.oreEmerald.name";
		case EMERALD_BLOCK:
			return "tile.blockEmerald.name";
		case SPRUCE_WOOD_STAIRS:
			return "tile.stairsWoodSpruce.name";
		case BIRCH_WOOD_STAIRS:
			return "tile.stairsWoodBirch.name";
		case JUNGLE_WOOD_STAIRS:
			return "tile.stairsWoodJungle.name";
		case COBBLE_WALL:
			switch(data)
			{
			case 0:
				return "tile.cobbleWall.normal.name";
			case 1:
				return "tile.cobbleWall.mossy.name";
			default:
				return null;
			}
		case QUARTZ_ORE:
			return "tile.netherquartz.name";
		case QUARTZ_BLOCK:
			switch(data)
			{
			case 0:
				return "tile.quartzBlock.default.name";
			case 1:
				return "tile.quartzBlock.chiseled.name";
			case 2:
				return "tile.quartzBlock.lines.name";
			default:
				return null;
			}
		case QUARTZ_STAIRS:
			return "tile.stairsQuartz.name";
		case STAINED_CLAY:
			switch(data)
			{
			case 0:
				return "tile.clayHardenedStained.white.name";
			case 1:
				return "tile.clayHardenedStained.orange.name";
			case 2:
				return "tile.clayHardenedStained.magenta.name";
			case 3:
				return "tile.clayHardenedStained.lightBlue.name";
			case 4:
				return "tile.clayHardenedStained.yellow.name";
			case 5:
				return "tile.clayHardenedStained.lime.name";
			case 6:
				return "tile.clayHardenedStained.pink.name";
			case 7:
				return "tile.clayHardenedStained.gray.name";
			case 8:
				return "tile.clayHardenedStained.silver.name";
			case 9:
				return "tile.clayHardenedStained.cyan.name";
			case 10:
				return "tile.clayHardenedStained.purple.name";
			case 11:
				return "tile.clayHardenedStained.blue.name";
			case 12:
				return "tile.clayHardenedStained.brown.name";
			case 13:
				return "tile.clayHardenedStained.green.name";
			case 14:
				return "tile.clayHardenedStained.red.name";
			case 15:
				return "tile.clayHardenedStained.black.name";
			default:
				return null;
			}
		case LOG_2:
			switch(data)
			{
			case 0:
				return "tile.log.acacia.name";
			case 1:
				return "tile.log.big_oak.name";
			default:
				return null;
			}
		case ACACIA_STAIRS:
			return "tile.stairsWoodAcacia.name";
		case DARK_OAK_STAIRS:
			return "tile.stairsWoodDarkOak.name";
		case PRISMARINE:
			switch(data)
			{
			case 0:
				return "tile.prismarine.rough.name";
			case 1:
				return "tile.prismarine.bricks.name";
			case 2:
				return "tile.prismarine.dark.name";
			default:
				return null;
			}
		case SEA_LANTERN:
			return "tile.seaLantern.name";
		case HAY_BLOCK:
			return "tile.hayBlock.name";
		case HARD_CLAY:
			return "tile.clayHardened.name";
		case COAL_BLOCK:
			return "tile.blockCoal.name";
		case PACKED_ICE:
			return "tile.icePacked.name";
		case RED_SANDSTONE:
			switch(data)
			{
			case 0:
				return "tile.redSandStone.default.name";
			case 1:
				return "tile.redSandStone.chiseled.name";
			case 2:
				return "tile.redSandStone.smooth.name";
			default:
				return null;
			}
		case RED_SANDSTONE_STAIRS:
			return "tile.stairsRedSandStone.name";
		case STONE_SLAB2:
			return "tile.stoneSlab2.red_sandstone.name";
		
		case SAPLING:
			switch(data)
			{
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
			default:
				return null;
			}
		case LEAVES:
			switch(data)
			{
			case 0:
			case 1:
			case 2:
			case 3:
			default:
				return null;
			}
		case WEB:
		case LONG_GRASS:
			switch(data)
			{
			case 1:
			case 2:
			default:
				return null;
			}
		case DEAD_BUSH:
		case YELLOW_FLOWER:
		case RED_ROSE:
			switch(data)
			{
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
			case 6:
			case 7:
			case 8:
			default:
				return null;
			}
		case BROWN_MUSHROOM:
		case RED_MUSHROOM:
		case TORCH:
		case CHEST:
		case WORKBENCH:
		case FURNACE:
		case LADDER:
		case SNOW:
		case CACTUS:
		case JUKEBOX:
		case FENCE:
		case MONSTER_EGGS:
			switch(data)
			{
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
			default:
				return null;
			}
		case IRON_FENCE:
		case THIN_GLASS:
		case VINE:
		case WATER_LILY:
		case NETHER_FENCE:
		case ENCHANTMENT_TABLE:
		case ENDER_PORTAL:
		case ENDER_CHEST:
		case ANVIL:
			switch(data)
			{
			case 0:
			case 1:
			case 2:
			default:
				return null;
			}
		case TRAPPED_CHEST:
		case STAINED_GLASS_PANE:
			switch(data)
			{
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
			case 6:
			case 7:
			case 8:
			case 9:
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
			default:
				return null;
			}
		case LEAVES_2:
			switch(data)
			{
			case 0:
			case 1:
			default:
				return null;
			}
		case SLIME_BLOCK:
		case CARPET:
			switch(data)
			{
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
			case 6:
			case 7:
			case 8:
			case 9:
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
			default:
				return null;
			}
		case DOUBLE_PLANT:
			switch(data)
			{
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
			default:
				return null;
			}
		case SPRUCE_FENCE:
		case BIRCH_FENCE:
		case JUNGLE_FENCE:
		case DARK_OAK_FENCE:
		case ACACIA_FENCE:
		case PAINTING:
		case SIGN:
		case BED:
		case ITEM_FRAME:
		case FLOWER_POT_ITEM:
		case SKULL_ITEM:
			switch(data)
			{
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
			default:
				return null;
			}
		case ARMOR_STAND:
		case BANNER:
			switch(data)
			{
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
			case 6:
			case 7:
			case 8:
			case 9:
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
			default:
				return null;
			}
		default:
			return null;
		}
	}
	{
//		Material.STONE_SLAB2
	}
	public static boolean hasPermission(UUID uuid, String perm)
	{
		perm = perm.replace('.', ':');

		File userFile = CommonsConfig.getFile(Type.USER, true, uuid);
		YamlConfiguration userConfig = YamlConfiguration.loadConfiguration(userFile);

		ConfigurationSection userPermissions = userConfig.getConfigurationSection("permission");

		if(userPermissions != null)
		{
			for(String key : userPermissions.getKeys(false))
			{
				long value = userConfig.getLong("permission." + key);
				boolean valid = value == 0 || value > System.currentTimeMillis();

				if(valid && (key.endsWith("*") && perm.startsWith(key.substring(0, key.length() - 1)) || 
					perm.equals(key)))
				{
					return true;
				}
			}
		}
		
		ConfigurationSection userGroups = userConfig.getConfigurationSection("group");
		
		if(userGroups != null)
		{
			for(String key : userGroups.getKeys(false))
			{
				long value = userConfig.getLong("group." + key);
				boolean valid = value == 0 || value > System.currentTimeMillis();

				if(valid)
				{
					File groupFile = CommonsConfig.getFile(Type.GROUP, true, key);
					YamlConfiguration groupConfig = YamlConfiguration.loadConfiguration(groupFile);

					ConfigurationSection groupPermissions = groupConfig.getConfigurationSection("permission");

					if(groupPermissions != null)
					{
						for(String key1 : groupPermissions.getKeys(false))
						{
							value = groupConfig.getLong("permisison." + key1);
							valid = value == 0 || value > System.currentTimeMillis();
							
							if(valid && (key1.endsWith("*") && perm.startsWith(key1.substring(0, key1.length() - 1)) || 
								perm.equals(key1)))
							{
								return true;
							}
						}
					}
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
		
		Bukkit.getServer().sendPluginMessage(Common.getPlugin(), "commons:commons", out.toByteArray());
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
		
		Bukkit.getServer().sendPluginMessage(Common.getPlugin(), "commons:commons", out.toByteArray());
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
}