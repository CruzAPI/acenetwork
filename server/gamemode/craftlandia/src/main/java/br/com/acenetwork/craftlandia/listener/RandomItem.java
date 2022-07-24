package br.com.acenetwork.craftlandia.listener;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

import com.google.common.io.ByteStreams;

import br.com.acenetwork.commons.CommonsUtil;
import br.com.acenetwork.commons.manager.BundleSupplier;
import br.com.acenetwork.commons.manager.Message;
import br.com.acenetwork.craftlandia.Rarity;
import br.com.acenetwork.craftlandia.Util;
import br.com.acenetwork.craftlandia.executor.Jackpot;
import br.com.acenetwork.craftlandia.inventory.SpecialItems;
import br.com.acenetwork.craftlandia.manager.Config;
import br.com.acenetwork.craftlandia.manager.Config.Type;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

public class RandomItem implements Listener
{
	private final UUID uuid;
	
	private static RandomItem instance;
	
	private final BundleSupplier<ItemStack> itemSupplier;
	public static final String SKIN_VALUE = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjViOTVkYTEyODE2NDJkYWE1ZDAyMmFkYmQzZTdjYjY5ZGMwOTQyYzgxY2Q2M2JlOWMzODU3ZDIyMmUxYzhkOSJ9fX0=";
	
	public RandomItem()
	{
		instance = this;
		File file = Config.getFile(Type.RANDOM_ITEM_UUID, true);
		
		if(file.length() > 0L)
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
		
		itemSupplier = new BundleSupplier<ItemStack>()
		{
			@Override
			public ItemStack get(ResourceBundle bundle, Object... args)
			{
				int version = args.length > 1 ? (int) args[1] : 47;
				ItemStack item;
				ItemMeta meta;
				
				if(version > 5)
				{
					item = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
					meta = item.getItemMeta();
					CommonsUtil.setCustomSkull((SkullMeta) meta, SKIN_VALUE);
				}
				else
				{
					item = new ItemStack(Material.CAKE_BLOCK);
					meta = item.getItemMeta();
					meta.addEnchant(Enchantment.DURABILITY, 1, true);
					meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
				}
				
				meta.setDisplayName(CommonsUtil.hideUUID(uuid) + ChatColor.WHITE + ChatColor.BOLD + "RANDOM ITEM");
				item.setItemMeta(meta);
				
				return item;
			}
		};
	}
	
	public BundleSupplier<ItemStack> getItemSupplier()
	{
		return itemSupplier;
	}
	
	public static RandomItem getInstance()
	{
		return instance;
	}
	
	@EventHandler
	public void a(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		
		ItemStack item = e.getItem();
		
		if(item == null)
		{
			return;
		}
		
		Random r = new Random(System.currentTimeMillis());
		
		if(CommonsUtil.compareUUID(item, uuid))
		{
			int nextInt = r.nextInt(20);
			int n = 0;
			
			Rarity rarity;
			
			if(nextInt < (n += 1))
			{
				rarity = Rarity.LEGENDARY;
			}
			else if(nextInt < (n += 4))
			{
				rarity = Rarity.RARE;
			}
			else
			{
				rarity = Rarity.COMMON;
			}
			
			double nextDouble = r.nextInt(100) + r.nextDouble();
			double d = 0.0D;
			
			ItemStack nextItem = new ItemStack(Material.STONE);
			ItemMeta meta;
			
			boolean setCommodity = true;
			
			if(nextDouble < (d += 0.2D))
			{
				ItemStack[] array = new ItemStack[] 
				{
					new ItemStack(Material.MOB_SPAWNER),
					new ItemStack(Material.GOLDEN_APPLE, 1 + r.nextInt(64), (short) 1),
					new ItemStack(Material.DIAMOND_BLOCK, 1 + r.nextInt(64)),
				};
				
				nextItem = array[r.nextInt(array.length)];
			}
			else if(nextDouble < (d += 3.0D))
			{
				short[] array = new short[] {50, 51, 52, 54, 55, 56, 57, 58, 59, 60, 61, 62, 65,
						66, 67, 68, 90, 91, 92, 93, 94, 95, 96, 98, 100, 101, 120};
				
				nextItem = new ItemStack(Material.MONSTER_EGG, 1, array[r.nextInt(array.length)]);
			}
			else if(nextDouble < (d += 3.0D))
			{
				ItemStack[] array = new ItemStack[] 
				{
					new ItemStack(Material.COAL, 1 + r.nextInt(16)),
					new ItemStack(Material.IRON_INGOT, 1 + r.nextInt(16)),
					new ItemStack(Material.GOLD_INGOT, 1 + r.nextInt(16)),
					new ItemStack(Material.REDSTONE, 1 + r.nextInt(16)),
					new ItemStack(Material.INK_SACK, 1 + r.nextInt(64), (short) 4),
					new ItemStack(Material.DIAMOND, 1 + r.nextInt(4)),
					new ItemStack(Material.EMERALD, 1 + r.nextInt(4)),
				};
				
				nextItem = array[r.nextInt(array.length)];
			}
			else if(nextDouble < (d += 6.0D))
			{
				nextItem = SpecialItems.getInstance().getContainmentPickaxeSupplier().get(null, r.nextInt(25) + r.nextDouble());
				setCommodity = false;
			}
			else if(nextDouble < (d += 8.0D))
			{
				ItemStack[] array = new ItemStack[] 
				{
					new ItemStack(Material.STICK, 1 + r.nextInt(64)),
					new ItemStack(Material.WOOD, 1 + r.nextInt(16), (short) r.nextInt(4)),
					new ItemStack(Material.COBBLESTONE, 1 + r.nextInt(16)),
					new ItemStack(Material.BOWL, 1 + r.nextInt(64)),
					new ItemStack(Material.STRING, 1 + r.nextInt(16)),
					new ItemStack(Material.LEATHER, 1 + r.nextInt(4)),
					new ItemStack(Material.PAPER, 1 + r.nextInt(12)),
					new ItemStack(Material.BOOK, 1 + r.nextInt(3)),
					new ItemStack(Material.EGG, 1 + r.nextInt(16)),
					new ItemStack(Material.BONE, 1 + r.nextInt(16)),
					new ItemStack(Material.QUARTZ, 1 + r.nextInt(16)),
					new ItemStack(Material.GLOWSTONE_DUST, 1 + r.nextInt(8)),
					new ItemStack(Material.FEATHER, 1 + r.nextInt(16)),
					new ItemStack(Material.BLAZE_ROD, 1 + r.nextInt(2)),
					new ItemStack(Material.BLAZE_POWDER, 1 + r.nextInt(4)),
					new ItemStack(Material.ENDER_PEARL, 1 + r.nextInt(8)),
					new ItemStack(Material.EYE_OF_ENDER, 1 + r.nextInt(4)),
					new ItemStack(Material.GOLD_NUGGET, 1 + r.nextInt(64)),
					new ItemStack(Material.SULPHUR, 1 + r.nextInt(16)),
					new ItemStack(Material.FLINT, 1 + r.nextInt(16)),
					new ItemStack(Material.MAGMA_CREAM, 1 + r.nextInt(4)),
					new ItemStack(Material.SLIME_BALL, 1 + r.nextInt(16)),
					new ItemStack(Material.SPECKLED_MELON, 1 + r.nextInt(4)),
					new ItemStack(Material.GOLDEN_CARROT, 1 + r.nextInt(16)),
					new ItemStack(Material.FERMENTED_SPIDER_EYE, 1 + r.nextInt(4)),
					new ItemStack(Material.RABBIT_FOOT, 1 + r.nextInt(4)),
					new ItemStack(Material.RABBIT_HIDE, 1 + r.nextInt(4)),
					new ItemStack(Material.GHAST_TEAR, 1 + r.nextInt(4)),
					new ItemStack(Material.FIREBALL, 1 + r.nextInt(2)),
					new ItemStack(Material.EXP_BOTTLE, 1 + r.nextInt(64)),
				};
						
						nextItem = array[r.nextInt(array.length)];
			}
			else if(nextDouble < (d += 10.0D))
			{
				ItemStack[] array = new ItemStack[] 
				{
					new ItemStack(373, 1, (short) 8193),
					new ItemStack(373, 1, (short) 8225),
					new ItemStack(373, 1, (short) 8257),
					new ItemStack(373, 1, (short) 16385),
					new ItemStack(373, 1, (short) 16417),
					new ItemStack(373, 1, (short) 16449),
					new ItemStack(373, 1, (short) 8194),
					new ItemStack(373, 1, (short) 8226),
					new ItemStack(373, 1, (short) 8258),
					new ItemStack(373, 1, (short) 16386),
					new ItemStack(373, 1, (short) 16418),
					new ItemStack(373, 1, (short) 16450),
					new ItemStack(373, 1, (short) 8227),
					new ItemStack(373, 1, (short) 8259),
					new ItemStack(373, 1, (short) 16419),
					new ItemStack(373, 1, (short) 16451),
					new ItemStack(373, 1, (short) 8196),
					new ItemStack(373, 1, (short) 8228),
					new ItemStack(373, 1, (short) 8260),
					new ItemStack(373, 1, (short) 16388),
					new ItemStack(373, 1, (short) 16420),
					new ItemStack(373, 1, (short) 16452),
					new ItemStack(373, 1, (short) 8261),
					new ItemStack(373, 1, (short) 8229),
					new ItemStack(373, 1, (short) 16453),
					new ItemStack(373, 1, (short) 16421),
					new ItemStack(373, 1, (short) 8230),
					new ItemStack(373, 1, (short) 8262),
					new ItemStack(373, 1, (short) 16422),
					new ItemStack(373, 1, (short) 16454),
					new ItemStack(373, 1, (short) 8232),
					new ItemStack(373, 1, (short) 8264),
					new ItemStack(373, 1, (short) 16424),
					new ItemStack(373, 1, (short) 16456),
					new ItemStack(373, 1, (short) 8201),
					new ItemStack(373, 1, (short) 8233),
					new ItemStack(373, 1, (short) 8265),
					new ItemStack(373, 1, (short) 16393),
					new ItemStack(373, 1, (short) 16425),
					new ItemStack(373, 1, (short) 16457),
					new ItemStack(373, 1, (short) 8234),
					new ItemStack(373, 1, (short) 8266),
					new ItemStack(373, 1, (short) 16426),
					new ItemStack(373, 1, (short) 16458),
					new ItemStack(373, 1, (short) 8267),
					new ItemStack(373, 1, (short) 8235),
					new ItemStack(373, 1, (short) 16459),
					new ItemStack(373, 1, (short) 16427),
					new ItemStack(373, 1, (short) 8268),
					new ItemStack(373, 1, (short) 8236),
					new ItemStack(373, 1, (short) 16460),
					new ItemStack(373, 1, (short) 16428),
					new ItemStack(373, 1, (short) 8237),
					new ItemStack(373, 1, (short) 8269),
					new ItemStack(373, 1, (short) 16429),
					new ItemStack(373, 1, (short) 16461),
					new ItemStack(373, 1, (short) 8238),
					new ItemStack(373, 1, (short) 8270),
					new ItemStack(373, 1, (short) 16430),
					new ItemStack(373, 1, (short) 16462),
				};
				
				nextItem = array[r.nextInt(array.length)];
			}
			else if(nextDouble < (d += 10.0D))
			{
				ItemStack[] array = new ItemStack[] 
				{
					new ItemStack(Material.LEATHER_HELMET),
					new ItemStack(Material.LEATHER_CHESTPLATE),
					new ItemStack(Material.LEATHER_LEGGINGS),
					new ItemStack(Material.LEATHER_BOOTS),
					new ItemStack(Material.CHAINMAIL_HELMET),
					new ItemStack(Material.CHAINMAIL_CHESTPLATE),
					new ItemStack(Material.CHAINMAIL_LEGGINGS),
					new ItemStack(Material.CHAINMAIL_BOOTS),
					new ItemStack(Material.GOLD_HELMET),
					new ItemStack(Material.GOLD_CHESTPLATE),
					new ItemStack(Material.GOLD_LEGGINGS),
					new ItemStack(Material.GOLD_BOOTS),
					new ItemStack(Material.IRON_HELMET),
					new ItemStack(Material.IRON_CHESTPLATE),
					new ItemStack(Material.IRON_LEGGINGS),
					new ItemStack(Material.IRON_BOOTS),
					new ItemStack(Material.DIAMOND_HELMET),
					new ItemStack(Material.DIAMOND_CHESTPLATE),
					new ItemStack(Material.DIAMOND_LEGGINGS),
					new ItemStack(Material.DIAMOND_BOOTS),
					new ItemStack(Material.WOOD_SWORD),
					new ItemStack(Material.STONE_SWORD),
					new ItemStack(Material.IRON_SWORD),
					new ItemStack(Material.GOLD_SWORD),
					new ItemStack(Material.DIAMOND_SWORD),
					new ItemStack(Material.WOOD_PICKAXE),
					new ItemStack(Material.STONE_PICKAXE),
					new ItemStack(Material.IRON_PICKAXE),
					new ItemStack(Material.GOLD_PICKAXE),
					new ItemStack(Material.DIAMOND_PICKAXE),
					new ItemStack(Material.WOOD_AXE),
					new ItemStack(Material.STONE_AXE),
					new ItemStack(Material.IRON_AXE),
					new ItemStack(Material.GOLD_AXE),
					new ItemStack(Material.DIAMOND_AXE),
					new ItemStack(Material.WOOD_SPADE),
					new ItemStack(Material.STONE_SPADE),
					new ItemStack(Material.IRON_SPADE),
					new ItemStack(Material.GOLD_SPADE),
					new ItemStack(Material.DIAMOND_SPADE),
					new ItemStack(Material.STONE_HOE),
					new ItemStack(Material.STONE_HOE),
					new ItemStack(Material.IRON_HOE),
					new ItemStack(Material.GOLD_HOE),
					new ItemStack(Material.DIAMOND_HOE),
					new ItemStack(Material.FISHING_ROD),
					new ItemStack(Material.SHEARS),
					new ItemStack(Material.FLINT_AND_STEEL),
				};
				
				nextItem = array[r.nextInt(array.length)];
			}
			else if(nextDouble < (d += 10.0D))
			{
				nextItem = new ItemStack(Material.ENCHANTED_BOOK);
				
				EnchantmentStorageMeta storage = (EnchantmentStorageMeta) nextItem.getItemMeta();
				
				nextInt = r.nextInt(50);
				n = 0;
				
				int enchantNumber;
				
				if(nextInt < (n += 1))
				{
					enchantNumber = 4;
				}
				else if(nextInt < (n += 4))
				{
					enchantNumber = 3;
				}
				else if(nextInt < (n += 15))
				{
					enchantNumber = 2;
				}
				else
				{
					enchantNumber = 1;
				}
				
				Enchantment[] enchantments = Enchantment.values();
				
				for(int i = 0; i < enchantNumber; i++)
				{
					Enchantment ench = enchantments[r.nextInt(enchantments.length)];
					
					if(storage.hasConflictingStoredEnchant(ench))
					{
						continue;
					}
					
					storage.addStoredEnchant(ench, 1 + r.nextInt(Util.getMaxLevel(ench, rarity)), true);
				}
				
				nextItem.setItemMeta(storage);
			}
			else
			{
				ItemStack[] array = new ItemStack[] 
				{
					new ItemStack(Material.ROTTEN_FLESH, 1 + r.nextInt(16)),
					new ItemStack(Material.RAW_FISH, 1 + r.nextInt(3), (short) 3),
					new ItemStack(Material.POISONOUS_POTATO, 1 + r.nextInt(3)),
					new ItemStack(Material.SPIDER_EYE, 1 + r.nextInt(3)),
					new ItemStack(Material.COOKIE, 1 + r.nextInt(64)),
					new ItemStack(Material.BREAD, 1 + r.nextInt(16)),
					new ItemStack(Material.CAKE),
					new ItemStack(Material.GOLDEN_APPLE, 1 + r.nextInt(3)),
					new ItemStack(Material.APPLE, 1 + r.nextInt(16)),
					new ItemStack(Material.PUMPKIN_PIE, 1 + r.nextInt(16)),
					new ItemStack(Material.BAKED_POTATO, 1 + r.nextInt(16)),
					new ItemStack(Material.COOKED_BEEF, 1 + r.nextInt(16)),
					new ItemStack(Material.COOKED_MUTTON, 1 + r.nextInt(16)),
					new ItemStack(Material.COOKED_RABBIT, 1 + r.nextInt(16)),
					new ItemStack(Material.GRILLED_PORK, 1 + r.nextInt(16)),
					new ItemStack(Material.COOKED_FISH, 1 + r.nextInt(16)),
					new ItemStack(Material.COOKED_FISH, 1 + r.nextInt(16), (short) 1),
					new ItemStack(Material.MUSHROOM_SOUP, 1 + r.nextInt(32)),
				};
				
				nextItem = array[r.nextInt(array.length)];
			}
			
			if(setCommodity)
			{
				Util.setCommodity(nextItem, rarity);
			}
			
			p.setItemInHand(nextItem);
			ResourceBundle bundle = ResourceBundle.getBundle("message", CommonsUtil.getLocaleFromMinecraft(p.spigot().getLocale()));
			ResourceBundle minecraftBundle = ResourceBundle.getBundle("minecraft", CommonsUtil.getLocaleFromMinecraft(p.spigot().getLocale()));
			String tag = ChatColor.AQUA + "[" + bundle.getString("noun.jackpot").toUpperCase()  + "]";
			
			TextComponent[] extra = new TextComponent[1];
			
			String displayName = nextItem.getItemMeta().hasDisplayName() 
					? nextItem.getItemMeta().getDisplayName() 
					: CommonsUtil.getTranslation(nextItem, minecraftBundle);
			
			extra[0] = new TextComponent(rarity.getColor() + displayName + " x" + nextItem.getAmount());
			
			TextComponent text = new TextComponent("");
			text.setColor(ChatColor.GREEN);
			text.addExtra(tag);
			TextComponent text1 = Message.getTextComponent(bundle.getString("cmd.jackpot.won"), extra);
			text1.setColor(text.getColor());
			text.addExtra(" " + text1.toLegacyText());
			p.spigot().sendMessage(text);
		}
	}
}