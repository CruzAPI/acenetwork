package br.com.acenetwork.acegenerator;

import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin
{
	@Override
	public ChunkGenerator getDefaultWorldGenerator(String worldName, String id)
	{
		if(id.equals("void"))
		{
			return new VoidGenerator();
		}
		
		return super.getDefaultWorldGenerator(worldName, id);
	}
}