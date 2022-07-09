package br.com.acenetwork.craftlandia.player;

import org.bukkit.Location;

import br.com.acenetwork.commons.player.CommonPlayer;
import br.com.acenetwork.craftlandia.manager.ChannelCommand;

public interface SurvivalPlayer extends CommonPlayer
{
	void channel(ChannelCommand channel, long ticks, Location destiny, Object... args);
	boolean cancelChannel(boolean force);
}