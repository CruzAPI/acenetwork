package br.com.acenetwork.craftlandia.manager;

import java.io.Serializable;

public class BlockSerializable implements Serializable
{
	private static final long serialVersionUID = 99869833932656500L;
	
	private int id;
	private byte data;
	
	public BlockSerializable(int id, byte data)
	{
		this.id = id;
		this.data = data;
	}
	
	public int getId()
	{
		return id;
	}
	
	public void setId(int id)
	{
		this.id = id;
	}
	
	public byte getData()
	{
		return data;
	}
	
	public void setData(byte data)
	{
		this.data = data;
	}
}