package br.com.acenetwork.survival.manager;

import java.io.Serializable;

public class CryptoInfo implements Serializable
{
	private static final long serialVersionUID = -3837932034737751633L;
	
	private double marketCap;
	private double circulatingSupply;
	
	public CryptoInfo(double marketCap, double circulatingSupply)
	{
		this.marketCap = marketCap;
		this.circulatingSupply = circulatingSupply;
	}
	
	public double getMarketCap()
	{
		return marketCap;
	}
	
	public void setMarketCap(double marketCap)
	{
		this.marketCap = marketCap;
	}
	
	public double getCirculatingSupply()
	{
		return circulatingSupply;
	}
	
	public void setCirculatingSupply(double circulatingSupply)
	{
		this.circulatingSupply = circulatingSupply;
	}
}