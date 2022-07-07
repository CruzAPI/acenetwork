package br.com.acenetwork.craftlandia.listener;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ResourceBundle;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.WorldSaveEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import com.google.common.io.ByteStreams;

import br.com.acenetwork.commons.CommonsUtil;
import br.com.acenetwork.commons.manager.BundleSupplier;
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
		
		if(CommonsUtil.compareUUID(item, uuid))
		{
			p.sendMessage("is a random ITEM");
		}
	}
}