package br.com.acenetwork.craftlandia.manager;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;

public class PRICE
{
	public static List<PRICE> LIST = new ArrayList<>();
	
	public int id;
	public short data;
	public double marketCap;
	public double circulatingSupply;
	public double maxPrice;
	
	public double basePrice;
	public double liquidityMultiplier;
	
	public PRICE(Material type, short data, double basePrice, double liquidityMultiplier)
	{
		this(type.getId(), data, basePrice, liquidityMultiplier);
	}
	
	public PRICE(int id, short data, double basePrice, double liquidityMultiplier)
	{
		this.id = id;
		this.data = data;
		this.basePrice = basePrice;
		this.liquidityMultiplier = liquidityMultiplier;
		
		maxPrice = basePrice * 2.0D;
		marketCap = basePrice * liquidityMultiplier;
		circulatingSupply = liquidityMultiplier;
		
		LIST.add(this);
	}
}