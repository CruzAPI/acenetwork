package br.com.acenetwork.craftlandia;

import java.util.HashSet;
import java.util.Set;

import net.md_5.bungee.api.ChatColor;

public enum Property implements ItemTag
{
	SOLD("" + ChatColor.RED + ChatColor.BOLD),
	;
	
	private final String tag;
	private final String color;
	
	Property(String color)
	{
		tag = color + name();
		this.color = color;
		SET.add(this);
	}
	
	@Override
	public String toString()
	{
		return tag;
	}
	
	public String getColor()
	{
		return color;
	}
	
	public static byte[] getByteArray(Set<Property> set)
	{
		Property[] values = values();
		int length = values.length;
		byte[] bytes = new byte[length / 8 + (length % 8 == 0 ? 0 : 1)];
		
		for(int i = 0; i < values.length;)
		{
			byte b = Byte.MIN_VALUE;
			
			for(int j = 7; j >= 0; j--, i++)
			{
				b += i < values.length ? set.contains(values[i]) ? 1 << j : 0 : 0;
			}
			
			bytes[(i / 8) - 1] = b;
		}
		
		return bytes;
	}
	
	public static Set<Property> getPropertySet(byte[] bytes)
	{
		Property[] values = values();
		
		Set<Property> set = new HashSet<>();
		
		int n = 0;
		
		for(int i = 0; i < bytes.length; i++)
		{
			int b = bytes[i] + 128;
			
			for(int j = 7; j >= 0; j--)
			{
				if(b % 2 == 1 && n + j < values.length)
				{
					set.add(values[n + j]);
				}
				
				b /= 2;
			}
			
			n += 8;
		}
		
		return set;
	}
}