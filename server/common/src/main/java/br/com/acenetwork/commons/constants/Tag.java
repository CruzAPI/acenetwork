package br.com.acenetwork.commons.constants;

import java.util.ResourceBundle;

import br.com.acenetwork.commons.manager.BundleSupplier;
import net.md_5.bungee.api.ChatColor;

public enum Tag
{
	OWNER(ChatColor.DARK_RED.toString(), new BundleSupplier<String>()
	{
		@Override
		public String get(ResourceBundle bundle, Object... args)
		{
			return ChatColor.DARK_RED + "(" + bundle.getString("tag.owner") + ") ";
		}
	}),
	
	ADMIN(ChatColor.RED.toString(), new BundleSupplier<String>()
	{
		@Override
		public String get(ResourceBundle bundle, Object... args)
		{
			return ChatColor.RED + "(" + bundle.getString("tag.admin") + ") ";
		}
	}),
	
	MOD(ChatColor.DARK_PURPLE.toString(), new BundleSupplier<String>()
	{
		@Override
		public String get(ResourceBundle bundle, Object... args)
		{
			return ChatColor.DARK_PURPLE + "(" + bundle.getString("tag.mod") + ") ";
		}
	}),
	
	TRIAL_MOD(ChatColor.LIGHT_PURPLE.toString(), new BundleSupplier<String>()
	{
		@Override
		public String get(ResourceBundle bundle, Object... args)
		{
			return ChatColor.LIGHT_PURPLE + "(" + bundle.getString("tag.trial-mod") + ") ";
		}
	}),
	
	VIP(ChatColor.GOLD.toString(), new BundleSupplier<String>()
	{
		@Override
		public String get(ResourceBundle bundle, Object... args)
		{
			return ChatColor.GOLD + "(" + bundle.getString("tag.vip") + ") ";
		}
	}),
	
	DEFAULT(ChatColor.GRAY.toString(), new BundleSupplier<String>()
	{
		@Override
		public String get(ResourceBundle bundle, Object... args)
		{
			return "";
		}
	}),
	
	BETA(ChatColor.DARK_PURPLE.toString(), new BundleSupplier<String>()
	{
		@Override
		public String get(ResourceBundle bundle, Object... args)
		{
			return ChatColor.DARK_PURPLE  + "(" + bundle.getString("tag.beta") + ") ";
		}
	}),
	
	;
	
	private final String color;
	private final BundleSupplier<String> fullTagSupplier;
	
	Tag(String color, BundleSupplier<String> fullTagSupplier)
	{
		this.color = color;
		this.fullTagSupplier = fullTagSupplier;
	}
	
	public BundleSupplier<String> getFullTagSupplier()
	{
		return fullTagSupplier;
	}
	
	@Override
	public String toString()
	{
		return color;
	}
	
	public String getPermission()
	{
		return "tag." + name().toLowerCase().replace("_", "");
	}

	public String getName()
	{
		return this + this.name();
	}
}
