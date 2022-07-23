package br.com.acenetwork.craftlandia.listener;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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
import br.com.acenetwork.craftlandia.Rarity;
import br.com.acenetwork.craftlandia.Util;
import br.com.acenetwork.craftlandia.manager.Config;
import br.com.acenetwork.craftlandia.manager.Config.Type;
import net.md_5.bungee.api.ChatColor;

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
			
			ItemStack nextItem;
			ItemMeta meta;
			
			if(nextDouble < (d += 0.2D))
			{
				nextItem = new ItemStack(Material.MOB_SPAWNER);
			}
			else if(nextDouble < (d += 3.0D))
			{
				short[] array = new short[] {50, 51, 52, 54, 55, 56, 57, 58, 59, 60, 61, 62, 65,
						66, 67, 68, 90, 91, 92, 93, 94, 95, 96, 98, 100, 101, 120};
				
				nextItem = new ItemStack(Material.MONSTER_EGG, 1, array[r.nextInt(array.length)]);
			}
			else if(nextDouble < (d += 5.0D))
			{
				nextItem = new ItemStack(Material.GOLD_PICKAXE);
				meta = nextItem.getItemMeta();
				meta.addEnchant(Enchantment.LUCK, 1, true);
				meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
				nextItem.setItemMeta(meta);
			}
			else if(nextDouble < (d += 40.0D))
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
				};
				
				nextItem = array[r.nextInt(array.length)];
			}
			else if(nextDouble < (d += 10.0D))
			{
				PotionType[] potionTypes = PotionType.values();
				
				Potion potion = new Potion(potionTypes[r.nextInt(potionTypes.length)]);
				
				potion.setSplash(r.nextBoolean());
				
				try
				{
					potion.setHasExtendedDuration(r.nextBoolean());
				}
				catch(Exception ex)
				{
					Bukkit.broadcastMessage(ex.getClass().toString());
				}
				
				try
				{
					potion.setLevel(1 + r.nextInt(2));
				}
				catch(Exception ex)
				{
					Bukkit.broadcastMessage(ex.getClass().toString());
				}
				
				nextItem = potion.toItemStack(1);
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
				nextItem = new ItemStack(Material.STONE);
			}
			
			Util.setCommodity(nextItem, rarity);
			
			p.setItemInHand(nextItem);
			
			p.sendMessage("you won " + rarity.toString() + ChatColor.RESET + nextItem.getAmount() + " " + nextItem.getType());
		}
	}
}