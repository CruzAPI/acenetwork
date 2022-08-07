package br.com.acenetwork.survival.player;

import org.bukkit.Location;

import br.com.acenetwork.commons.player.CommonPlayer;
import br.com.acenetwork.survival.manager.ChannelCommand;
import br.com.acenetwork.survival.manager.PlayerData;

public interface SurvivalPlayer extends CommonPlayer
{
	void channel(ChannelCommand channel, long ticks, Location destiny, Object... args);
	boolean cancelChannel(boolean force);
	void setPlayerData(PlayerData data);
	PlayerData getPlayerData();
}
