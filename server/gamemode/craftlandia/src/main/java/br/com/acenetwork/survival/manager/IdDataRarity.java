package br.com.acenetwork.survival.manager;

import java.io.Serializable;
import java.util.Objects;

import br.com.acenetwork.commons.manager.IdData;
import br.com.acenetwork.survival.Rarity;

public class IdDataRarity implements Serializable
{
	private static final long serialVersionUID = 7168290120829855398L;
	
	private final int id;
	private final short data;
	private final Rarity rarity;
	
	public IdDataRarity(int id, short data, Rarity rarity)
	{
		this.id = id;
		this.data = data;
		this.rarity = rarity;
	}
	
	public Rarity getRarity()
	{
		return rarity;
	}
	
	public int getId()
	{
		return id;
	}
	
	public short getData()
	{
		return data;
	}
	
	public IdData getIdData()
	{
		return new IdData(id, data);
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(data, id, rarity);
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
		IdDataRarity other = (IdDataRarity) obj;
		return data == other.data && id == other.id && rarity == other.rarity;
	}
}