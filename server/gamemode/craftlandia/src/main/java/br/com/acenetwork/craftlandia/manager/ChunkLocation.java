package br.com.acenetwork.craftlandia.manager;

import java.util.Objects;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;

public class ChunkLocation
{
	private final String w;
	private final int x;
	private final int z;
	
	public ChunkLocation(String w, int x, int z)
	{
		this.w = w;
		this.x = x;
		this.z = z;
	}
	
	public ChunkLocation(Chunk c)
	{
		this.w = c.getWorld().getName();
		this.x = c.getX();
		this.z = c.getZ();
	}
	
	public Chunk getChunk()
	{
		return Bukkit.getWorld(w).getChunkAt(x, z);
	}
	
	public String getW()
	{
		return w;
	}
	
	public int getX()
	{
		return x;
	}
	
	public int getZ()
	{
		return z;
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(x, z);
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ChunkLocation other = (ChunkLocation) obj;
		return x == other.x && z == other.z;
	}
}