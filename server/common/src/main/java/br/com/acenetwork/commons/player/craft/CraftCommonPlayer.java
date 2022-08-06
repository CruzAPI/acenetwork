package br.com.acenetwork.commons.player.craft;

import java.io.File;
import java.util.HashSet;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.Vector;

import br.com.acenetwork.commons.Common;
import br.com.acenetwork.commons.CommonsHotbar;
import br.com.acenetwork.commons.CommonsScoreboard;
import br.com.acenetwork.commons.CommonsUtil;
import br.com.acenetwork.commons.constants.Tag;
import br.com.acenetwork.commons.event.PlayerInvincibilityChangeEvent;
import br.com.acenetwork.commons.event.PlayerModeChangeEvent;
import br.com.acenetwork.commons.event.PlayerSuccessLoginEvent;
import br.com.acenetwork.commons.inventory.GUI;
import br.com.acenetwork.commons.manager.CommonPlayerData;
import br.com.acenetwork.commons.manager.CommonsConfig;
import br.com.acenetwork.commons.manager.CommonsConfig.Type;
import br.com.acenetwork.commons.player.CommonPlayer;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;

public abstract class CraftCommonPlayer implements CommonPlayer
{
	public static final Set<CommonPlayer> SET = new HashSet<>();
	
	protected final Player p;
	
	private GUI gui;
	private CommonsScoreboard commonsScoreboard;
	private CommonsHotbar commonsHotbar;
	private Tag tag;
	private long combat;
	private long playerCombat;
	private Player lastPlayerDamage;
	private boolean specs;
	private boolean invis;
	private boolean ignoreInvisAndSpecs;
	private String walletAddress;
	public int taskRequest;
	private boolean isJackpoting;
	private CommonPlayerData playerData;
	private boolean pvpInvincibility;
	private boolean logged;
	
	public CraftCommonPlayer(Player p)
	{
		this.p = p;
		
		CommonPlayer previous = get(p);
		
		SET.add(this);
		
		if(previous != null)	
		{
			playerData = previous.getCommonPlayerData();
			logged = previous.isLogged();
			specs = previous.canSpecs();
			tag = previous.getTag();
			commonsScoreboard = previous.getCommonsScoreboard();
			pvpInvincibility = previous.hasPVPInvincibility();
			previous.delete();
		}
		else
		{
			logged = p.getUniqueId().version() == 4;
			playerData = CommonPlayerData.load(p.getUniqueId());
		}
		
		reset();
		
		Bukkit.getPluginManager().callEvent(new PlayerModeChangeEvent(this));
		Bukkit.getPluginManager().registerEvents(this, Common.getInstance());
	}
	
	public static <T extends CommonPlayer> Set<T> getAll(Class<T> type)
	{
		return SET.stream().filter(x -> type.isInstance(x)).map(x -> type.cast(x)).collect(Collectors.toSet());
	}
	
	public static CommonPlayer get(Player p)
	{
		return SET.stream().filter(x -> x.getPlayer() == p).findAny().orElse(null);
	}
	
	@Override
	public boolean delete()
	{
		reset();
		HandlerList.unregisterAll(this);
		return SET.remove(this);
	}
	
	@Override
	public int requestDatabase()
	{
		return requestDatabase(20L * 5L);
	}
	
	@Override
	public int requestDatabase(long timeout)
	{
		ResourceBundle bundle = ResourceBundle.getBundle("message", getLocale());
		
//		p.sendMessage(ChatColor.GREEN + bundle.getString("commons.cmds.checking-database"));
		taskRequest = Bukkit.getScheduler().scheduleSyncDelayedTask(Common.getInstance(), () ->
		{
			p.sendMessage(ChatColor.RED + bundle.getString("commons.cmds.request-timed-out"));
		}, timeout);
		return taskRequest;
	}
	
	@Override
	public boolean isRequesting()
	{
		return Bukkit.getScheduler().isQueued(taskRequest);
	}
	
	@Override
	public void setSpecs(boolean value)
	{
		if(ignoreInvisAndSpecs)
		{
			return;
		}
		
		if(value)
		{
			for(CommonPlayer cp : SET)
			{
				p.showPlayer(cp.getPlayer());
			}
		}
		else
		{
			for(CommonPlayer cp : SET)
			{
				if(cp.isInvis())
				{
					p.hidePlayer(cp.getPlayer());
				}
				else
				{
					p.showPlayer(cp.getPlayer());
				}
			}
		}
		
		specs = value;
	}
	
	@Override
	public boolean canSpecs()
	{
		return specs;
	}

	@Override
	public boolean setInvis(boolean value)
	{
		if(value)
		{
			for(CommonPlayer cp : SET)
			{
				if(cp.getIgnoreInvisAndSpecs())
				{
					continue;
				}
				
				if(!cp.canSpecs())
				{
					cp.getPlayer().hidePlayer(p);
				}
			}
		}
		else
		{
			for(CommonPlayer cp : SET)
			{
				if(cp.getIgnoreInvisAndSpecs())
				{
					continue;
				}
				
				cp.getPlayer().showPlayer(p);
			}
		}
		
		return invis != (invis = value);
	}
	
	@Override
	public boolean isInvis()
	{
		return invis;
	}

	@Override
	public void setIgnoreInvisAndSpecs(boolean value)
	{
		ignoreInvisAndSpecs = value;
	}

	@Override
	public boolean getIgnoreInvisAndSpecs()
	{
		return ignoreInvisAndSpecs;
	}

	@Override
	public boolean isCombat()
	{
		return System.currentTimeMillis() - combat < 9000L;
	}

	@Override
	public boolean isCombat(long ms)
	{
		return System.currentTimeMillis() - combat < ms;
	}

	@Override
	public boolean isPlayerCombat()
	{
		return System.currentTimeMillis() - playerCombat < 9000L;
	}

	@Override
	public boolean isPlayerCombat(long ms)
	{
		return System.currentTimeMillis() - playerCombat < ms;
	}

	@Override
	public void setPlayerCombat(boolean value)
	{
		playerCombat = value ? System.currentTimeMillis() : 0L;
	}

	@Override
	public void setCombat(boolean value)
	{
		combat = value ? System.currentTimeMillis() : 0L;
	}
	
	@Override
	public Player getPlayer()
	{
		return p;
	}
	
	@EventHandler
	public void on(PlayerQuitEvent e)
	{
		if(e.getPlayer() != p)
		{
			return;
		}
		
		setCommonsHotbar(null);
		setCommonsScoreboard(null);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void login(EntityDamageEvent e)
	{
		
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void commonEntityDamage(EntityDamageEvent e)
	{
		if(e.getEntity() != p)
		{
			return;
		}
		
		setCombat(true);
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void on(EntityDamageByEntityEvent e)
	{
		if(e.getEntity() != p)
		{
			return;
		}
				
		Entity entity = e.getEntity();
		Entity damager = e.getDamager();
		
		Player p = null;
		Player t = null;

		if(entity instanceof Player)
		{
			p = (Player) entity;

			if(damager instanceof Player)
			{
				t = (Player) damager;
			}
			else if(damager instanceof Projectile)
			{
				Projectile projectile = (Projectile) damager;

				if(projectile.getShooter() instanceof Player)
				{
					t = (Player) projectile.getShooter();
				}
			}
		}
		
		if(p != null && t != null && p != t)
		{
			if(t.getNoDamageTicks() > t.getMaximumNoDamageTicks())
			{
				t.setNoDamageTicks(0);
			}
			
			lastPlayerDamage = t;
			setPlayerCombat(true);
		}
	}
	
	@Override
	public Tag getTag()
	{
		return tag == null ? Tag.DEFAULT : tag;
	}

	@Override
	public boolean hasPermission(String perm)
	{
		return CommonsUtil.hasPermission(p, perm);
	}
	
	@Override
	public Tag getBestTag()
	{
		for(Tag tag : Tag.values())
		{
			if(hasPermission(tag.getPermission()))
			{
				return tag;
			}
		}
		
		return Tag.DEFAULT;
	}
	
	@Override
	public Player getLastPlayerDamage()
	{
		return lastPlayerDamage;
	}
	
	@Override
	public boolean setTag(Tag tag)
	{
		p.setDisplayName(tag + p.getName());
		p.setPlayerListName(p.getDisplayName().substring(0, Math.min(16, p.getDisplayName().length())));
		return this.tag != (this.tag = tag == null ? Tag.DEFAULT : tag);
	}

	@Override
	public void setGUI(GUI gui)
	{
		if(this.gui != null)
		{
			HandlerList.unregisterAll(this.gui);
		}
		
		if((this.gui = gui) != null)
		{
			Bukkit.getPluginManager().registerEvents(this.gui, Common.getInstance());
		}
	}

	@Override
	public CommonsScoreboard getCommonsScoreboard()
	{
		return commonsScoreboard;
	}
	
	@Override
	public void setCommonsScoreboard(CommonsScoreboard commonsScoreboard)
	{
		if(this.commonsScoreboard != null)
		{
			HandlerList.unregisterAll(this.commonsScoreboard);
		}
		
		if((this.commonsScoreboard = commonsScoreboard) != null)
		{
			Bukkit.getPluginManager().registerEvents(this.commonsScoreboard, Common.getInstance());
			p.setScoreboard(commonsScoreboard.getScoreboard());
		}
		else
		{
			p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
		}
	}
	
	@Override
	public CommonsHotbar getCommonsHotbar()
	{
		return commonsHotbar;
	}
	
	@Override
	public void setCommonsHotbar(CommonsHotbar commonsHotbar)
	{
		if(this.commonsHotbar != null)
		{
			HandlerList.unregisterAll(this.commonsHotbar);
		}
		
		if((this.commonsHotbar = commonsHotbar) != null)
		{
			Bukkit.getPluginManager().registerEvents(this.commonsHotbar, Common.getInstance());
		}
	}
	
	@Override
	public GUI getGUI()
	{
		return gui;
	}
	
	@Override
	public int getPing()
	{
		return ((CraftPlayer) p).getHandle().ping;
	}
	
	@Override
	public String getClan()
	{
		File playerFile = CommonsConfig.getFile(Type.PLAYER, false, p.getUniqueId());
		YamlConfiguration playerConfig = YamlConfiguration.loadConfiguration(playerFile);
		
		return playerConfig.getString("clan");
	}
	
	@Override
	public void reset()
	{
		reset(true);
	}
	
	@Override
	public void reset(boolean clearInventory)
	{
		for(PotionEffect effect : p.getActivePotionEffects())
		{
			p.removePotionEffect(effect.getType());
		}
		
		p.spigot().setCollidesWithEntities(true);
		
		p.setVelocity(new Vector());
		p.setMaximumNoDamageTicks(20);
		p.setFireTicks(0);
		
		if(clearInventory)
		{
			p.getInventory().clear();
			p.getInventory().setArmorContents(null);
		}
		
		Damageable d = (Damageable) p;
		
		d.setMaxHealth(20.0D);
		d.setHealth(d.getMaxHealth());
		p.setFoodLevel(20);
		
		p.setFlySpeed(0.1F);
		p.setWalkSpeed(0.2F);
		p.setExp(0.0F);
		p.setLevel(0);

		setCombat(false);
		setPlayerCombat(false);
	}
	
	@Override
	public void sendMessage(String string, Object... args)
	{
		
	}
	
	@Override
	public void setJackpoting(boolean value)
	{
		this.isJackpoting = value;
	}
	
	@Override
	public boolean isJackpoting()
	{
		return isJackpoting;
	}
	
	@Override
	public Locale getLocale()
	{
		return CommonsUtil.getLocaleFromMinecraft(p.spigot().getLocale());
	}
	
	@Override
	public String getWalletAddress()
	{
		return walletAddress;
	}
	
	@Override
	public void setWalletAddress(String address)
	{
		this.walletAddress = address;
	}
	
	@Override
	public double getBalance()
	{
		return playerData.getBalance();
	}
	
	@Override
	public void setBalance(double balance)
	{
		playerData.setBalance(balance);
	}
	
	@Override
	public double getBTA()
	{
		return playerData.getBTA();
	}
	
	@Override
	public void setBTA(double bta)
	{
		playerData.setBTA(bta);
	}
	
	@Override
	public CommonPlayerData getCommonPlayerData()
	{
		return playerData;
	}
	
	@Override
	public double getDiskBTA()
	{
		return playerData.getDiskBTA();
	}
	
	@Override
	public double getWithdrawableBTA()
	{
		return Math.min(getBTA(), getDiskBTA());
	}
	
	@Override
	public void setCommonPlayerData(CommonPlayerData playerData)
	{
		this.playerData = playerData;
	}
	
	@Override
	public boolean hasInvincibility()
	{
		return playerData.hasInvincibility();
	}
	
	@Override
	public void setInvincibility(boolean value)
	{
		if(playerData.setInvincibility(value))
		{
			Bukkit.getPluginManager().callEvent(new PlayerInvincibilityChangeEvent(this));
		}
	}
	
	@Override
	public boolean hasPVPInvincibility()
	{
		return pvpInvincibility;
	}
	
	@Override
	public void setPVPInvincibility(boolean value)
	{
		pvpInvincibility = value;
	}
	
	@Override
	public void sendActionBarMessage(String msg)
	{
		IChatBaseComponent icbc = ChatSerializer.a("{\"text\": \"" + msg + "\"}");
		PacketPlayOutChat bar = new PacketPlayOutChat(icbc, (byte) 2);
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(bar);
	}
	
	@Override
	public boolean isLogged()
	{
		return logged;
	}
	
	@Override
	public void setLogged(boolean logged)
	{
		if(this.logged != (this.logged = logged) && logged)
		{
			Bukkit.getPluginManager().callEvent(new PlayerSuccessLoginEvent(p));
		}
	}
	
	@Override
	public int getVersion()
	{
		return 47;
//				ProtocolLibrary.getProtocolManager().getProtocolVersion(p);
	}
}
