package br.com.acenetwork.craftlandia.warp;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import br.com.acenetwork.craftlandia.player.SurvivalPlayer;
import net.md_5.bungee.api.ChatColor;

public class FactionsNether extends Warp
{
	public FactionsNether(World w)
	{
		super(w);
	}
	
	@Override
	public boolean isSafeZone(Location l)
	{
		return false;
	}
	
	@Override
	public boolean isSpawnProtection(Location l)
	{
		return false;
	}
	
	@Override
	public long getChannelingTicks(SurvivalPlayer sp)
	{
		Player p = sp.getPlayer();
		
		if(p.getWorld() != w)
		{
			return MAP.get(p.getWorld().getUID()).getChannelingTicks(sp);
		}
		
		if(sp.hasInvincibility())
		{
			return 0L;
		}
		
		for(Player all : w.getPlayers())
		{
			if(all == this || !(all instanceof SurvivalPlayer))
			{
				continue;
			}
			
			if(p.getWorld() == all.getWorld() && p.getLocation().distance(all.getLocation()) < 100.0D)
			{
				return 8L * 5L * 20L;
			}
		}
		
		return 8L * 20L;
	}
	
	@Override
	public Result canTeleportAwaySpawn(SurvivalPlayer sp)
	{
		Player p = sp.getPlayer();
		
		if(p.getWorld() != w)
		{
			return MAP.get(p.getWorld().getUID()).canTeleportAwaySpawn(sp);
		}
		
		if(sp.hasInvincibility() || sp.hasPVPInvincibility())
		{
			return Result.INVINCIBILITY;
		}
		
		return Result.ALLOW;
	}
	
	@Override
	public boolean canSetHome()
	{
		return true;
	}
	
	@Override
	public Location getRespawnLocation()
	{
		return Warp.getInstance(Factions.class).getRespawnLocation();
	}
	
	@Override
	public String getColoredName()
	{
		return ChatColor.DARK_PURPLE + "Raid " + ChatColor.RED + "(Nether)";
	}
	
	@Override
	public boolean hasPVP()
	{
		return true;
	}
}