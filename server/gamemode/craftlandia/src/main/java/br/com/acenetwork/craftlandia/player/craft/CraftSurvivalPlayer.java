package br.com.acenetwork.craftlandia.player.craft;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import br.com.acenetwork.commons.player.craft.CraftCommonPlayer;
import br.com.acenetwork.craftlandia.player.SurvivalPlayer;

public class CraftSurvivalPlayer extends CraftCommonPlayer implements SurvivalPlayer
{
	public CraftSurvivalPlayer(Player p)
	{
		super(p);
	}
	
	@Override
	public void reset()
	{
		super.reset();
		
		p.setGameMode(GameMode.SURVIVAL);
	}
}