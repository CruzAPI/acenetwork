package br.com.acenetwork.craftlandia.executor;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

public class Playtime implements TabExecutor
{
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args)
	{
		return new ArrayList<>();
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		return false;
	}
}
