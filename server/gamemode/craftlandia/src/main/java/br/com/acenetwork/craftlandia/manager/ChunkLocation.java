package br.com.acenetwork.craftlandia.manager;

import java.util.Objects;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;

public class ChunkLocation
{
	private final int x;
	private final int z;
	
	public ChunkLocation(int x, int z)
	{
		this.x = x;
		this.z = z;
	}
	
	public ChunkLocation(Chunk c)
	{
		this.x = c.getX();
		this.z = c.getZ();
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