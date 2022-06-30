package br.com.acenetwork.commons.manager;

public class Translation
{
	private final String key;
	private final Object[] args;
	
	public Translation(String key, Object... args)
	{
		this.key = key;
		this.args = args;
	}
	
	public String getKey()
	{
		return key;
	}
	
	public Object[] getArgs()
	{
		return args;
	}
}