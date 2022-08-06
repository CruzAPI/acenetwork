package br.com.acenetwork.craftlandia.manager;

import java.io.Serializable;

public class SchematicSerializable implements Serializable
{
	private static final long serialVersionUID = -7150624386439621255L;
	
	private final int relativeX, relativeY, relativeZ;
	private final BlockSerializable[][][] blocks;
	
	public SchematicSerializable(int relativeX, int relativeY, int relativeZ, BlockSerializable[][][] blocks)
	{
		this.relativeX = relativeX;
		this.relativeY = relativeY;
		this.relativeZ = relativeZ;
		this.blocks = blocks;
	}
	
	public int getRelativeX()
	{
		return relativeX;
	}
	
	public int getRelativeY()
	{
		return relativeY;
	}
	
	public int getRelativeZ()
	{
		return relativeZ;
	}
	
	public BlockSerializable[][][] getBlocks()
	{
		return blocks;
	}
}