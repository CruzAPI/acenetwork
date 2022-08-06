package br.com.acenetwork.survival.item;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import br.com.acenetwork.commons.CommonsUtil;
import br.com.acenetwork.survival.manager.ItemSpecial;
import net.md_5.bungee.api.ChatColor;

public class ContainmentPickaxe extends ItemSpecial
{
	@SuppressWarnings("unused")
	private static ContainmentPickaxe instance;
	
	private ContainmentPickaxe()
	{
		super("containment_pickaxe");
	}
	
	@Override
	public ItemStack getItemStack(ResourceBundle bundle, Object... args)
	{
		ItemMeta meta;
		ItemStack item = new ItemStack(Material.GOLD_PICKAXE);
		meta = item.getItemMeta();
		meta.addEnchant(Enchantment.LUCK, 1, true);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		
		meta.setDisplayName(CommonsUtil.hideUUID(uuid) + ChatColor.YELLOW + ChatColor.BOLD + "Containment Pickaxe");
		List<String> lore = new ArrayList<>();
		
		float chance = (float) args[0];
		item.setDurability((short) (item.getType().getMaxDurability() - item.getType().getMaxDurability() * chance / 100.0D));
		
		DecimalFormat df = new DecimalFormat("#.##");
		lore.add(ChatColor.GRAY + df.format(chance) + "% chance to get spawners");
		lore.add(ChatColor.GRAY + "(single use)");
		meta.setLore(lore);
		
		item.setItemMeta(meta);
		
		return item;
	}
	
	public float getContainmentPickaxeChance(ItemStack item)
	{
		if(!isInstanceOf(item))
		{
			return 0.0F;
		}
		
		ItemMeta meta = item.getItemMeta();
		
		if(meta == null)
		{
			return 0.0F;
		}
		
		List<String> lore = meta.getLore();
		
		if(lore == null || lore.size() == 0)
		{
			return 0.0F;
		}
		
		String line = lore.get(0);
		
		if(line == null)
		{
			return 0.0F;
		}
		
		String chance = "";
		
		for(int i = 2; i < line.length(); i++)
		{
			char c = line.charAt(i);
			
			if(c == '%')
			{
				break;
			}
			
			chance += c;
		}
		
		try
		{
			return Float.valueOf(chance);
		}
		catch(NumberFormatException e)
		{
			return 0.0F;
		}
	}
}