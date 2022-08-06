package br.com.acenetwork.commons.player;

import java.util.Locale;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import br.com.acenetwork.commons.CommonsHotbar;
import br.com.acenetwork.commons.CommonsScoreboard;
import br.com.acenetwork.commons.constants.Tag;
import br.com.acenetwork.commons.inventory.GUI;
import br.com.acenetwork.commons.manager.CommonPlayerData;

public interface CommonPlayer extends Listener
{
	boolean isLogged();
	void setLogged(boolean logged);
	void reset(boolean clearInventory);
	boolean hasInvincibility();
	void setInvincibility(boolean value);
	boolean hasPVPInvincibility();
	void setPVPInvincibility(boolean value);
	double getWithdrawableBTA();
	double getDiskBTA();
	CommonPlayerData getCommonPlayerData();
	double getBalance();
	void setBalance(double balance);
	double getBTA();
	void setBTA(double bta);
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
	void setCommonPlayerData(CommonPlayerData cloneMemoryPD);
	void sendActionBarMessage(String msg);
	int getVersion();
}
