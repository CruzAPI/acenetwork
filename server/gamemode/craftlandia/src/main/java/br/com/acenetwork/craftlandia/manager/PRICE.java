package br.com.acenetwork.craftlandia.manager;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;

public class PRICE
{
	public static final List<PRICE> LIST = new ArrayList<>();
	
	public int id;
	public short data;
	public double marketCap;
	public double circulatingSupply;
	public double maxPrice;
	public int sellLimit;
	
	public double basePrice;
	public double liquidityMultiplier;
	
	public PRICE(int id, short data, double basePrice, int sellLimit, double liquidityMultiplier)
	{
		this.id = id;
		this.data = data;
		this.basePrice = basePrice;
		this.sellLimit = sellLimit;
		this.liquidityMultiplier = liquidityMultiplier;
		
		maxPrice = basePrice * 2.0D;
		marketCap = basePrice * sellLimit * liquidityMultiplier;
		circulatingSupply = (int) (sellLimit * liquidityMultiplier);
		
		LIST.add(this);
	}
}