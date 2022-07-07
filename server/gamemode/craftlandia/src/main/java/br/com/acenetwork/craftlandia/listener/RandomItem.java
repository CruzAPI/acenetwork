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
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.WorldSaveEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.google.common.io.ByteStreams;

import br.com.acenetwork.commons.manager.BundleSupplier;
import br.com.acenetwork.craftlandia.manager.Config;
import br.com.acenetwork.craftlandia.manager.Config.Type;
import net.md_5.bungee.api.ChatColor;

public class RandomItem implements Listener
{
	private final UUID uuid;
	
	
	
	private final BundleSupplier<ItemStack> itemSupplier = new BundleSupplier<ItemStack>()
	{
		@Override
		public ItemStack get(ResourceBundle bundle, Object... args)
		{
			int version = args.length > 1 ? (int) args[1] : 47;
			ItemStack item;
			ItemMeta meta;
			
			if(version > 5)
			{
				item = new ItemStack(Material.SKULL, 1, (short) 3);
				meta = item.getItemMeta();
			}
			else
			{
				item = new ItemStack(Material.CAKE_BLOCK);
				meta = item.getItemMeta();
				meta.addEnchant(Enchantment.DURABILITY, 1, true);
				meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			}
			meta.setDisplayName(CommonsUtil. + ChatColor.WHITE + ChatColor.BOLD + "inv.jackpot.random-item.item");
			item.setItemMeta(meta);
			
			return null;
		}
	};
	
	public RandomItem()
	{
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
	}
	
	@EventHandler
	public void a(PlayerInteractEvent e)
	{
		//TODO:
	}
}