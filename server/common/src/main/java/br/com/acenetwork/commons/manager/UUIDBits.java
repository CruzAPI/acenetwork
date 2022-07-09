package br.com.acenetwork.commons.manager;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class UUIDBits implements Serializable
{
	private static final long serialVersionUID = -1334316946700757286L;
	private long mostSigBits;
	private long leastSigBits;
	
	public UUIDBits(long mostSigBits, long leastSigBits)
	{
		this.mostSigBits = mostSigBits;
		this.leastSigBits = leastSigBits;
	}
	
	public UUIDBits(UUID uuid)
	{
		this.mostSigBits = uuid.getMostSignificantBits();
		this.leastSigBits = uuid.getLeastSignificantBits();
	}
	
	public long getLeastSigBits()
	{
		return leastSigBits;
	}
	
	public long getMostSigBits()
	{
		return mostSigBits;
	}
	
	public UUID getUUID()
	{
		return new UUID(mostSigBits, leastSigBits);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(leastSigBits, mostSigBits);
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
		UUIDBits other = (UUIDBits) obj;
		return leastSigBits == other.leastSigBits && mostSigBits == other.mostSigBits;
	}
}