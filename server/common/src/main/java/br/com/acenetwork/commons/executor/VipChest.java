package br.com.acenetwork.commons.executor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;

import org.apache.logging.log4j.core.net.Facility;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import com.google.common.io.ByteStreams;

import br.com.acenetwork.commons.Common;
import br.com.acenetwork.commons.CommonsUtil;
import br.com.acenetwork.commons.inventory.VipChestGUI;
import br.com.acenetwork.commons.manager.BundleSupplier;
import br.com.acenetwork.commons.manager.CommonsConfig;
import br.com.acenetwork.commons.manager.CommonsConfig.Type;
import br.com.acenetwork.commons.manager.Message;
import br.com.acenetwork.commons.player.CommonPlayer;
import br.com.acenetwork.commons.player.craft.CraftCommonPlayer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

public class VipChest implements TabExecutor, Listener
{
	public static final String SKIN_VALUE = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDVjNmRjMmJiZjUxYzM2Y2ZjNzcxNDU4NWE2YTU2ODNlZjJiMTRkNDdkOGZmNzE0NjU0YTg5M2Y1ZGE2MjIifX19";
	private final Set<UUID> validVips;
	private final UUID uuid;
	private final BundleSupplier<ItemStack> vipSupplier;
	private static VipChest instance;
	
	public VipChest()
	{
		instance = this;
		uuid = CommonsUtil.getUUID(CommonsConfig.getFile(Type.ITEM_VIP_UUID, true));
		File file = CommonsConfig.getFile(Type.ACTIVATED_VIPS, false);
		
		if(file.exists() && file.length() > 0L)
		{
			try(FileInputStream fileIn = new FileInputStream(file);
					ByteArrayInputStream streamIn = new ByteArrayInputStream(ByteStreams.toByteArray(fileIn));
					ObjectInputStream in = new ObjectInputStream(streamIn))
			{
				validVips = (Set<UUID>) in.readObject();
			}
			catch(IOException | ClassNotFoundException e)
			{
				throw new RuntimeException(e);
			}
		}
		else
		{
			validVips = new HashSet<>();
		}
		
		vipSupplier = new BundleSupplier<ItemStack>()
		{
			@Override
			public ItemStack get(ResourceBundle bundle, Object... args)
			{
				UUID uuid = args.length == 1 ? (UUID) args[0] : UUID.randomUUID();
				
				if(args.length == 0)
				{
					validVips.add(uuid);
				}
				
				ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
				ItemMeta meta = item.getItemMeta();
				meta.setDisplayName(CommonsUtil.hideUUID(VipChest.getInstance().getUUID()) + ChatColor.RESET
						+ CommonsUtil.hideUUID(uuid) + ChatColor.GREEN + ChatColor.BOLD + "VIP");
				CommonsUtil.setCustomSkull((SkullMeta) meta, SKIN_VALUE);
				item.setItemMeta(meta);
				return item;
			}
		};
		
		Bukkit.getPluginManager().registerEvents(this, Common.getInstance());
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) 
	{
		return new ArrayList<>();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String aliases, String[] args)
	{
		ResourceBundle bundle = ResourceBundle.getBundle("message");
		
		if(!(sender instanceof Player))
		{
			TextComponent text = new TextComponent(bundle.getString("commons.cmds.cant-perform-command"));
			text.setColor(ChatColor.RED);
			CommonsUtil.sendMessage(sender, text);
			return true;
		}

		Player p = (Player) sender;
		CommonPlayer cp = CraftCommonPlayer.get(p);
		
		bundle = ResourceBundle.getBundle("message", cp.getLocale());

		if(args.length == 0)
		{
			new VipChestGUI(cp);
		}
		else if(args.length == 1 && args[0].equalsIgnoreCase("sync"))
		{
			try
			{
				if(cp.isRequesting())
				{
					return false;
				}
				
				Runtime.getRuntime().exec(String.format("node %s/reset/vip %s %s %s", System.getProperty("user.home"),
						Common.getSocketPort(), 
						cp.requestDatabase(), 
						p.getUniqueId().version() == 4 ? p.getUniqueId() : p.getName()));
			}
			catch(IOException e)
			{
				e.printStackTrace();
				p.sendMessage(ChatColor.RED + bundle.getString("commons.unexpected-error"));
			}
		}
		else
		{
			TextComponent[] extra = new TextComponent[1];
			
			extra[0] = new TextComponent("/" + aliases);
			
			TextComponent text = Message.getTextComponent(bundle.getString("commons.cmds.wrong-syntax-try"), extra);
			text.setColor(ChatColor.RED);
			CommonsUtil.sendMessage(sender, text);
		}

		return true;
	}
	
	public void save()
	{
		File file = CommonsConfig.getFile(Type.ACTIVATED_VIPS, true);
		
		try(FileOutputStream fileOut = new FileOutputStream(file);
				ByteArrayOutputStream streamOut = new ByteArrayOutputStream();
				ObjectOutputStream out = new ObjectOutputStream(streamOut))
		{
			out.writeObject(validVips);
			fileOut.write(streamOut.toByteArray());
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public BundleSupplier<ItemStack> getVipSupplier()
	{
		return vipSupplier;
	}
	
	public static VipChest getInstance()
	{
		return instance;
	}
	
	public Set<UUID> getValidVips()
	{
		return validVips;
	}
	
	public UUID getUUID()
	{
		return uuid;
	}
}