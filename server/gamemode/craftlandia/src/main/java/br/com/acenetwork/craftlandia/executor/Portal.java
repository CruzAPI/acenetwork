package br.com.acenetwork.craftlandia.executor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerPortalEvent;

import com.google.common.io.ByteStreams;

import br.com.acenetwork.commons.manager.BlockLoc;
import br.com.acenetwork.commons.manager.Message;
import br.com.acenetwork.commons.player.CommonPlayer;
import br.com.acenetwork.commons.player.craft.CraftCommonPlayer;
import br.com.acenetwork.craftlandia.Main;
import br.com.acenetwork.craftlandia.manager.Config;
import br.com.acenetwork.craftlandia.manager.Config.Type;
import br.com.acenetwork.craftlandia.manager.PortalObj;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

public class Portal implements TabExecutor, Listener
{
	private static Portal instance;
	
	private final Set<PortalObj> set;
	private final Map<Block, PortalObj> map = new HashMap<>();
	private final Map<UUID, PortalObj> portalSelection = new HashMap<>();
	
	public byte getIdAvailable()
	{
		Set<Byte> set = map.values().stream().map(x -> x.getId()).collect(Collectors.toSet());
		
		for(byte b = Byte.MIN_VALUE; b < Byte.MAX_VALUE; b++)
		{
			if(!set.contains(b))
			{
				return b;
			}
		}
		
		return Byte.MAX_VALUE;
	}
	
	public PortalObj getPortal(byte id)
	{
		for(PortalObj portal : set)
		{
			if(portal.getId() == id)
			{
				return portal;
			}
		}
		
		return null;
	}
	
	public Portal()
	{
		instance = this;
		
		File file = Config.getFile(Type.PORTALS, false);
		
		if(file.exists() && file.length() > 0L)
		{
			try(FileInputStream fileIn = new FileInputStream(file);
					ByteArrayInputStream streamIn = new ByteArrayInputStream(ByteStreams.toByteArray(fileIn));
					ObjectInputStream in = new ObjectInputStream(streamIn))
			{
				set = (Set<PortalObj>) in.readObject();
				Map<BlockLoc, Byte> tempMap = (Map<BlockLoc, Byte>) in.readObject();
				
				tempMap.entrySet().forEach(x ->
				{
					byte id = x.getValue();
					PortalObj portal = getPortal(id);
					
					if(portal != null)
					{
						map.put(x.getKey().getBlock(), portal);
					}
				});
			}
			catch(ClassNotFoundException | IOException e)
			{
				throw new RuntimeException(e);
			}
		}
		else
		{
			set = new HashSet<>();
		}
		
		Bukkit.getPluginManager().registerEvents(this, Main.getPlugin());
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args)
	{
		return new ArrayList<>();
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String aliases, String[] args)
	{
		ResourceBundle bundle;
		
		if(!(sender instanceof Player))
		{
			bundle = ResourceBundle.getBundle("message");
			sender.sendMessage(ChatColor.RED + bundle.getString("commons.cmds.cant-perform-command"));
			return true;
		}
		
		Player p = (Player) sender;
		CommonPlayer cp = CraftCommonPlayer.get(p);
		
		bundle = ResourceBundle.getBundle("message", cp.getLocale());
		
		if(args.length == 1 && args[0].equalsIgnoreCase("select"))
		{
			Set<Material> ignoreSet = new HashSet<>();
			
			ignoreSet.add(Material.AIR);
			
			Block b = p.getTargetBlock(ignoreSet, 10);
			
			if(b.getType() != Material.PORTAL)
			{
				p.sendMessage("NOT A PORTAL");
				return true;
			}
			
			if(!map.containsKey(b))
			{
				p.sendMessage("USE /portal create");
				return true;
			}
			
			portalSelection.put(p.getUniqueId(), map.get(b));
			p.sendMessage("PORTAL SELECTED!!!");
		}
		else if(args.length == 1 && args[0].equalsIgnoreCase("create"))
		{
			Set<Material> ignoreSet = new HashSet<>();
			ignoreSet.add(Material.AIR);
			Block b = p.getTargetBlock(ignoreSet, 10);
			
			if(b.getType() != Material.PORTAL)
			{
				p.sendMessage("NOT A PORTAL");
				return true;
			}
			
			float yaw = Math.round(p.getLocation().getYaw() / 90.0F) * 90.0F - 180.0F;
			
			PortalObj portal = new PortalObj(getIdAvailable(), b.getWorld(), b.getX() + 0.5D, b.getY(), b.getZ() + 0.5D, yaw);
			
			Set<Block> set = new HashSet<>();
			find(b, Material.PORTAL, set);
			
			set.forEach(x -> map.put(x, portal));
			this.set.remove(portal);
			this.set.add(portal);
			portalSelection.put(p.getUniqueId(), portal);
			p.sendMessage("PORTAL CREATED AND SELECTED!!! + yaw = " + yaw);
		}
		else if(args.length == 1 && args[0].equalsIgnoreCase("link"))
		{
			PortalObj selected = portalSelection.get(p.getUniqueId());
			
			if(selected == null)
			{
				p.sendMessage("NO PORTAL SELECTED!");
				p.sendMessage("USE /portal select");
				return true;
			}
			
			Set<Material> ignoreSet = new HashSet<>();
			ignoreSet.add(Material.AIR);
			Block b = p.getTargetBlock(ignoreSet, 10);
			
			if(b.getType() != Material.PORTAL)
			{
				p.sendMessage("NOT A PORTAL");
				return true;
			}
			
			PortalObj target = map.get(b);
			
			if(target == null)
			{
				p.sendMessage("THIS PORTAL ISN'T CREATED.");
				p.sendMessage("USE /portal create");
				return true;
			}
			
			target.setLinkedPortal(selected);
			selected.setLinkedPortal(target);
			p.sendMessage("PORTALS LINKED!!!");
			
		}
		else
		{
			TextComponent[] extra = new TextComponent[1];
			
			extra[0] = new TextComponent("/" + aliases);
			
			TextComponent text = Message.getTextComponent(bundle.getString("commons.cmds.wrong-syntax-try"), extra);
			text.setColor(ChatColor.RED);
			p.spigot().sendMessage(text);
		}
		
		return false;
	}
	
	public void save()
	{
		File file = Config.getFile(Type.PORTALS, true);
		
		try(FileOutputStream fileOut = new FileOutputStream(file);
				ByteArrayOutputStream streamOut = new ByteArrayOutputStream();
				ObjectOutputStream out = new ObjectOutputStream(streamOut))
		{
			out.writeObject(set);
			
			Map<BlockLoc, Byte> tempMap = new HashMap<>();

			map.entrySet().forEach(x ->
			{
				tempMap.put(new BlockLoc(x.getKey().getLocation()), x.getValue().getId());
			});
			
			out.writeObject(tempMap);
			fileOut.write(streamOut.toByteArray());
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	private Set<Block> find(Block block, Material type, Set<Block> set)
	{
		int r = 1;
		
		for(int x = -r; x <= r; x++)
		{
			for(int y = -r; y <= r; y++)
			{
				for(int z = -r; z <= r; z++)
				{
					Block b = block.getWorld().getBlockAt(x + block.getX(), y + block.getY(), z + block.getZ());

					if(b.getType() == type && !set.contains(b))
					{
						set.add(b);
						find(b, type, set);
					}
				}
			}
		}

		return set;
	}
	
	@EventHandler
	public void a(BlockBreakEvent e)
	{
		PortalObj portal = map.get(e.getBlock());
		
		if(portal == null)
		{
			return;
		}
		
		set.remove(portal);
		Iterator<Entry<Block, PortalObj>> iterator = map.entrySet().iterator();
		
		while(iterator.hasNext())
		{
			if(iterator.next().getValue() == portal)
			{
				iterator.remove();
			}
		}
		
		e.getPlayer().sendMessage("PORTAL DELETED!");
	}
	
	@EventHandler
	public void a(PlayerPortalEvent e)
	{
		PortalObj portal = map.get(e.getFrom().getBlock());
		
		if(portal == null)
		{
			return;
		}
		
		e.setCancelled(true);
		
		Player p = e.getPlayer();
		
		PortalObj target = portal.getLinkedPortal();
		
		if(target == null)
		{
			p.sendMessage("target == null");
			return;
		}
		
		p.teleport(target.getLocation());
	}
	
	public static Portal getInstance()
	{
		return instance;
	}
}
