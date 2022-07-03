package br.com.acenetwork.commons.player;

import java.io.IOException;
import java.util.Locale;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;

import br.com.acenetwork.commons.CommonsHotbar;
import br.com.acenetwork.commons.CommonsScoreboard;
import br.com.acenetwork.commons.constants.Tag;
import br.com.acenetwork.commons.executor.VipChest;
import br.com.acenetwork.commons.inventory.VipChestGUI;
import br.com.acenetwork.commons.manager.PlayerData;
import br.com.acenetwork.commons.inventory.GUI;

public interface CommonPlayer extends Listener
{
	double getWithdrawableBTA();
	double getDiskBTA();
	PlayerData getPlayerData();
	double getBalance();
	void setBalance(double balance);
	double getBTA();
	void setBTA(double bta);
	void setVipChest(Inventory inv);
	Inventory getVipChest();
	void readVipChest() throws IOException;
	void writeVipChest() throws IOException;
	void setJackpoting(boolean value);
	boolean isJackpoting();
	CommonsScoreboard getCommonsScoreboard();
	void setCommonsScoreboard(CommonsScoreboard commonsScoreboard);
	CommonsHotbar getCommonsHotbar();
	void setCommonsHotbar(CommonsHotbar commonsHotbar);
	Tag getTag();
	boolean setTag(Tag tag);
	boolean setInvis(boolean value);
	boolean isInvis();
	void setSpecs(boolean value);
	boolean canSpecs();
	boolean delete();
	void setCombat(boolean value);
	boolean isCombat();
	boolean isCombat(long ms);
	void setPlayerCombat(boolean value);
	boolean isPlayerCombat();
	boolean isPlayerCombat(long ms);
	void setIgnoreInvisAndSpecs(boolean value);
	boolean getIgnoreInvisAndSpecs();
	boolean hasPermission(String perm);
	void setGUI(GUI gui);
	GUI getGUI();
	String getClan();
	Tag getBestTag();
	Player getLastPlayerDamage();
	Player getPlayer();
	void reset();
	Locale getLocale();
	void sendMessage(String string, Object... args);
	String getWalletAddress();
	void setWalletAddress(String address);
	boolean isRequesting();
	int requestDatabase();
	int requestDatabase(long timeout);
	int getPing();
	void setPlayerData(PlayerData cloneMemoryPD);
}
