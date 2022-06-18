package br.com.acenetwork.craftlandia.warp;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.Listener;

import br.com.acenetwork.craftlandia.Main;

public abstract class Warp implements Listener
{
	public static final Set<Warp> SET = new HashSet<>();
	
	protected final World w;
	
	public Warp(World w)
	{
		this.w = w;
		Bukkit.getPluginManager().registerEvents(this, Main.getPlugin());
		SET.add(this);
	}
}