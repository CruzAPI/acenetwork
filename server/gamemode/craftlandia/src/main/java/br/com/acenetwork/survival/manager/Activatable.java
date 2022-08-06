package br.com.acenetwork.survival.manager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.WorldSaveEvent;
import org.bukkit.inventory.ItemStack;

import com.google.common.io.ByteStreams;

import br.com.acenetwork.commons.CommonsUtil;
import br.com.acenetwork.survival.manager.Config.Type;
import net.md_5.bungee.api.ChatColor;

public abstract class Activatable extends ItemSpecial
{
	protected final Set<UUID> validSet;
	
	public Activatable(String fileName)
	{
		super(fileName);
		
		File file = Config.getFile(Type.SPECIAL_ITEM_SET, false, fileName + "_set");
		
		if(file.exists() && file.length() > 0L)
		{
			try(FileInputStream fileIn = new FileInputStream(file);
					ByteArrayInputStream streamIn = new ByteArrayInputStream(ByteStreams.toByteArray(fileIn));
					ObjectInputStream in = new ObjectInputStream(streamIn))
			{
				validSet = (Set<UUID>) in.readObject();
			}
			catch(IOException | ClassNotFoundException ex)
			{
				throw new RuntimeException(ex);
			}
		}
		else
		{
			validSet = new HashSet<>();
		}
	}
	
	@EventHandler
	public void a(WorldSaveEvent e)
	{
		if(!e.getWorld().getName().equals("world"))
		{
			return;
		}
		
		File file = Config.getFile(Type.SPECIAL_ITEM_SET, false, fileName + "_set");
		
		try(FileOutputStream fileOut = new FileOutputStream(file);
				ByteArrayOutputStream streamOut = new ByteArrayOutputStream();
				ObjectOutputStream out = new ObjectOutputStream(streamOut))
		{
			out.writeObject(validSet);
			fileOut.write(streamOut.toByteArray());
		}
		catch(IOException ex)
		{
			throw new RuntimeException(ex);
		}
	}
	
	public Set<UUID> getValidSet()
	{
		return validSet;
	}
	
	protected byte consume(Player p, int slot)
	{
		ItemStack item = p.getInventory().getItem(slot);
		
		if(item == null)
		{
			return -2;
		}
		
		List<UUID> list = CommonsUtil.getHiddenUUIDs(item);
		
		if(list.size() < 2)
		{
			return -2;
		}
		
		if(!uuid.equals(list.get(0)))
		{
			return -2;
		}
		
		p.getInventory().setItem(slot, null);
		
		if(!validSet.remove(list.get(1)))
		{
			Bukkit.getConsoleSender().sendMessage("" + ChatColor.RED + ChatColor.BOLD + p.getName() + " activated an invalid item! Probably a dup?");
			return -1;
		}
		
		return 0;
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void a(BlockPlaceEvent e)
	{
		if(isInstanceOf(e.getItemInHand()))
		{
			e.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void a(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		
		switch(consume(p, p.getInventory().getHeldItemSlot()))
		{
		case 0:
			run(p);
		case -1:
			e.setCancelled(true);
			break;
		}
	}
	
	public abstract void run(Player p);
}