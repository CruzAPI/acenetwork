package br.com.acenetwork.commons.manager;

public class ObjectField<T>
{
	public T object;
	
	public ObjectField(T defaultValue)
	{
		object = defaultValue;
	}
}