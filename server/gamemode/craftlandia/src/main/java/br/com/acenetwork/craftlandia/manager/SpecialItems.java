package br.com.acenetwork.craftlandia.manager;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import br.com.acenetwork.commons.CommonsUtil;
import br.com.acenetwork.commons.manager.BundleSupplier;
import br.com.acenetwork.craftlandia.manager.Config.Type;
import net.md_5.bungee.api.ChatColor;

public class SpecialItems
{
	private final UUID containmentPickaxeUUID;
	private final BundleSupplier<ItemStack> containmentPickaxeSupplier;
	
	private static SpecialItems instance;
	
	public SpecialItems()
	{
		instance = this;
		
		containmentPickaxeUUID = CommonsUtil.getUUID(Config.getFile(Type.CONTAINMENT_PICKAXE_UUID, true));
		
		containmentPickaxeSupplier = new BundleSupplier<ItemStack>()
		{
			@Override
			public ItemStack get(ResourceBundle bundle, Object... args)
			{
				ItemMeta meta;
				ItemStack item = new ItemStack(Material.GOLD_PICKAXE);
				meta = item.getItemMeta();
				meta.addEnchant(Enchantment.LUCK, 1, true);
				meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
				
				meta.setDisplayName(CommonsUtil.hideUUID(containmentPickaxeUUID) + ChatColor.YELLOW + ChatColor.BOLD + "Containment Pickaxe");
				List<String> lore = new ArrayList<>();
				
				double nextDouble = Double.valueOf(args[0].toString());
				item.setDurability((short) (item.getType().getMaxDurability() - item.getType().getMaxDurability() * nextDouble / 100.0D));
				
				DecimalFormat df = new DecimalFormat("#.##");
				lore.add(ChatColor.GRAY + df.format(nextDouble) + "% chance to get spawners");
				lore.add(ChatColor.GRAY + "(single use)");
				meta.setLore(lore);
				
				item.setItemMeta(meta);
				
				return item;
			}
		};
	}
	
	public BundleSupplier<ItemStack> getContainmentPickaxeSupplier()
	{
		return containmentPickaxeSupplier;
	}
	
	public static SpecialItems getInstance()
	{
		return instance == null ? instance = new SpecialItems() : instance;
	}
	
	public boolean isContainmentPickaxe(ItemStack item)
	{
		return CommonsUtil.containsUUID(item, containmentPickaxeUUID);
	}
	
	public float getContainmentPickaxeChance(ItemStack item)
	{
		if(!isContainmentPickaxe(item))
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
	
	public boolean isSpecial(ItemStack item)
	{
		return CommonsUtil.containsUUID(item, containmentPickaxeUUID);
	}
}