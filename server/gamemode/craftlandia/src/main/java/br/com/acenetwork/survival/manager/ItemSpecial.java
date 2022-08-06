package br.com.acenetwork.craftlandia.manager;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import br.com.acenetwork.commons.CommonsUtil;
import br.com.acenetwork.craftlandia.Main;
import br.com.acenetwork.craftlandia.item.CommonRandomItem;
import br.com.acenetwork.craftlandia.item.ContainmentPickaxe;
import br.com.acenetwork.craftlandia.item.LegendaryRandomItem;
import br.com.acenetwork.craftlandia.item.NormalRandomItem;
import br.com.acenetwork.craftlandia.item.RareRandomItem;
import br.com.acenetwork.craftlandia.item.VipItem;
import br.com.acenetwork.craftlandia.manager.Config.Type;

public abstract class ItemSpecial implements Listener
{
	public static final Set<Class<? extends ItemSpecial>> CHILD_CLASSES = new HashSet<>();
	protected final String fileName;
	
	protected final UUID uuid;
	
	public ItemSpecial(String fileName)
	{
		this.fileName = fileName;
		this.uuid = CommonsUtil.getUUID(Config.getFile(Type.SPECIAL_ITEM_UUID, true, fileName + "_uuid"));
		
		CHILD_CLASSES.add(this.getClass());
		
		Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
	}
	
	public abstract ItemStack getItemStack(ResourceBundle bundle, Object... args);
	
	public boolean isInstanceOf(ItemStack item)
	{
		return CommonsUtil.containsUUID(item, uuid);
	}
	
	public UUID getUUID()
	{
		return uuid;
	}
	
	public static <T extends ItemSpecial> T getInstance(Class<T> type)
	{
		try
		{
			Field field = type.getDeclaredField("instance");
			
			field.setAccessible(true);
			T t = type.cast(field.get(null));
			
			if(t != null)
			{
				return t;
			}
			
			Constructor<T> constructor = type.getDeclaredConstructor();
			constructor.setAccessible(true);
			t = constructor.newInstance();
			field.set(null, t);
			return t;
		}
		catch(Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	public static void load()
	{
		getInstance(CommonRandomItem.class);
		getInstance(ContainmentPickaxe.class);
		getInstance(LegendaryRandomItem.class);
		getInstance(NormalRandomItem.class);
		getInstance(RareRandomItem.class);
		getInstance(VipItem.class);
	}
	
	public static boolean isSpecial(ItemStack item)
	{
		for(Class<? extends ItemSpecial> classes : CHILD_CLASSES)
		{
			if(CommonsUtil.containsUUID(item, getInstance(classes).getUUID()))
			{
				return true;
			}
		}
		
		return false;
	}
}