package br.com.acenetwork.survival.manager;

import net.md_5.bungee.api.chat.TextComponent;

public class InvalidCommandArgumentException extends Exception
{
	private static final long serialVersionUID = -2001475068201673733L;
	private final int argPos;
	private final TextComponent text;
	
	public InvalidCommandArgumentException(int argPos, TextComponent text)
	{
		this.argPos = argPos;
		this.text = text;
	}
	
	public int getArgPos()
	{
		return argPos;
	}
	
	public TextComponent getText()
	{
		return text;
	}
}