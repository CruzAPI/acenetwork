package br.com.acenetwork.commons.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldSaveEvent;

import br.com.acenetwork.commons.manager.PlayerData;

public class WorldSave implements Listener
{
	@EventHandler
	public void a(WorldSaveEvent e)
	{
		if(!e.getWorld().getName().equals("world"))
		{
			return;
		}
		
		PlayerData.save();
	}
}