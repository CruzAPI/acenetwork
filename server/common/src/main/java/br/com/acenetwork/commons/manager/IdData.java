package br.com.acenetwork.commons.manager;

import java.io.Serializable;
import java.util.Objects;

public class IdData implements Serializable
{
	private static final long serialVersionUID = 2886313873294489099L;
	
	private final int id;
	private final short data;
	
	public IdData(int id, short data)
	{
		this.id = id;
		this.data = data;
	}
	
	public int getId()
	{
		return id;
	}
	
	public short getData()
	{
		return data;
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(data, id);
	}

	@Override
	public boolean equals(Object obj)
	{
		if(this == obj)
		{
			return true;
		}
		
		if(obj == null)
		{
			return false;
		}
		
		if(getClass() != obj.getClass())
		{
			return false;
		}
		
		IdData other = (IdData) obj;
		return data == other.data && id == other.id;
	}

	@Override
	public String toString()
	{
		return "IdData [id=" + id + ", data=" + data + "]";
	}
}