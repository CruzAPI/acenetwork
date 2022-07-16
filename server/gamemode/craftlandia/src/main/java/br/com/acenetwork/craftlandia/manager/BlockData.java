package br.com.acenetwork.craftlandia.manager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

import br.com.acenetwork.craftlandia.Property;
import br.com.acenetwork.craftlandia.Rarity;
import net.md_5.bungee.api.ChatColor;

public class BlockData implements Serializable
{
	private static final long serialVersionUID = -3647303654452560066L;
	
	private Rarity rarity;
	private Set<Property> properties;
	private UUID player;
	
	public BlockData()
	{
		
	}
	
	public BlockData(Rarity rarity, Set<Property> properties, UUID player)
	{
		this.rarity = rarity;
		this.properties = properties;
		this.player = player;
	}
	
	public BlockData(byte[] b) throws IOException
	{
		try(ByteArrayInputStream streamIn = new ByteArrayInputStream(b);
				DataInputStream in = new DataInputStream(streamIn);)
		{
			byte main = in.readByte();
			
			if((main & 0x80) == 0x80)
			{
				rarity = Rarity.getByData(in.readByte());
			}
			
			if((main & 0x40) == 0x40)
			{
				properties = Property.getPropertySet(in.readNBytes(Property.getByteArrayLength()));
			}
			
			if((main & 0x20) == 0x20)
			{
				player = new UUID(in.readLong(), in.readLong());
			}
		}
		catch(IOException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	public void setRarity(Rarity rarity)
	{
		this.rarity = rarity;
	}
	
	public Rarity getRarity()
	{
		return rarity;
	}
	
	public void setProperties(Set<Property> properties)
	{
		this.properties = properties;
	}
	
	public Set<Property> getProperties()
	{
		return properties;
	}
	
	public void setPlayer(UUID player)
	{
		this.player = player;
	}
	
	public UUID getPlayer()
	{
		return player;
	}
	
	public boolean hasPlayer()
	{
		return player != null;
	}
	
	private byte[] getMainBytes()
	{
		byte[] b = new byte[1];
		
		b[0] |= rarity != null ? 0x80 : 0x00;
		b[0] |= properties != null && !properties.isEmpty() ? 0x40 : 0x00;
		b[0] |= player != null ? 0x20 : 0x00;
		
		return b;
	}
	
	public byte[] toByteArray()
	{
		try(ByteArrayOutputStream streamOut = new ByteArrayOutputStream();
				DataOutputStream out = new DataOutputStream(streamOut))
		{
			byte[] main = getMainBytes();
			
			out.write(main);
			
			if((main[0] & 0x80) == 0x80)
			{
				out.writeByte(rarity.getData());
			}
			
			if((main[0] & 0x40) == 0x40)
			{
				out.write(Property.getByteArray(properties));
			}
			
			if((main[0] & 0x20) == 0x20)
			{
				out.writeLong(player.getMostSignificantBits());
				out.writeLong(player.getLeastSignificantBits());
			}
			
			return streamOut.toByteArray();
		}
		catch(IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	@Override
	public String toString()
	{
		return "BlockData [rarity=" + rarity + ChatColor.RESET 
				+ ", properties=" + properties + ChatColor.RESET 
				+ ", player=" + player + ChatColor.RESET + "]";
	}
}
