package br.com.acenetwork.craftlandia.manager;

public class CryptoInfo
{
	private double marketCap;
	private double circulatingSupply;
	private final long pos;
	
	public CryptoInfo(double marketCap, double circulatingSupply, long pos)
	{
		this.marketCap = marketCap;
		this.circulatingSupply = circulatingSupply;
		this.pos = pos;
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
	
	public long getPos()
	{
		return pos;
	}
}