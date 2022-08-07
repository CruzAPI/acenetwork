package br.com.acenetwork.craftlandia;

import java.util.HashSet;
import java.util.Set;

public interface ItemTag
{
	Set<ItemTag> SET = new HashSet<>();
	String toString();
	
	public static ItemTag getByTag(String tag)
	{
		return SET.stream().filter(x -> x.toString().equals(tag)).findAny().orElse(null);
	}
}
