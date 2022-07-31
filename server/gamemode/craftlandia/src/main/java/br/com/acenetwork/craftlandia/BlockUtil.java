package br.com.acenetwork.craftlandia;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Tree;

import br.com.acenetwork.craftlandia.event.BreakNaturallyEvent;
import br.com.acenetwork.craftlandia.inventory.SpecialItems;
import br.com.acenetwork.craftlandia.manager.BreakReason;

public class BlockUtil
{
	public static void breakNaturally(Block b, BreakReason reason)
	{
		breakNaturally(b, reason, null);
	}
	
	public static void breakNaturally(Block b, ItemStack tool)
	{
		breakNaturally(b, BreakReason.PLAYER, tool);
	}
	
	private static void breakNaturally(Block b, BreakReason reason, ItemStack tool)
	{
		int silkTouch = tool == null ? 0 : tool.getEnchantmentLevel(Enchantment.SILK_TOUCH);
		int fortune = tool == null ? 0 : tool.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);
		int[] array;
		int amount;
		double major;
		double minor;
		Material drop = null;
		List<String> lore = Util.getLore(b);
		
		List<ItemStack> items = new ArrayList<>();
		
		Random r = new Random();
		
		s0:switch(b.getType())
		{
		case BOOKSHELF:
			if(silkTouch > 0)
			{
				items.add(new ItemStack(Material.BOOKSHELF));
			}
			else
			{
				items.add(new ItemStack(Material.BOOK, 3));
			}
			break;
		case WEB:
			if(tool == null || tool.getType() == Material.SHEARS || tool.getType().name().contains("SWORD"))
			{
				if(silkTouch > 0)
				{
					items.add(new ItemStack(Material.WEB));
				}
				else
				{
					items.add(new ItemStack(Material.STRING));
				}
			}
			break;
		case CLAY:
			if(silkTouch > 0)
			{
				items.add(new ItemStack(b.getType(), 1, b.getData()));
			}
			else
			{
				items.add(new ItemStack(Material.CLAY_BALL, 4));
			}
			break;
		case GLASS:
		case THIN_GLASS:
		case STAINED_GLASS:
		case STAINED_GLASS_PANE:
		case ICE:
		case PACKED_ICE:
			if(silkTouch > 0)
			{
				items.add(new ItemStack(b.getType(), 1, b.getData()));
			}
			break;
		case GLOWSTONE:
			if(silkTouch > 0)
			{
				items.add(new ItemStack(b.getType(), 1, b.getData()));
			}
			else
			{
				array = new int[3 + fortune];
				
				amount = 2;
				
				for(int i = 0; i < array.length; i++)
				{
					array[i] = Math.min(4, amount++);
				}
				
				items.add(new ItemStack(Material.GLOWSTONE_DUST, 2 + r.nextInt(3)));
			}
			break;
		case DIRT:
			if(b.getData() == 1)
			{
				items.add(new ItemStack(b.getType(), 1, b.getData()));
				break;
			}
		case GRASS:
		case MYCEL:
			if(silkTouch > 0)
			{
				items.add(new ItemStack(b.getType(), 1, b.getData()));
			}
			else
			{
				items.add(new ItemStack(Material.DIRT));
			}
			break;
		case GRAVEL:
			if(silkTouch > 0)
			{
				items.add(new ItemStack(b.getType(), 1, b.getData()));
			}
			else
			{
				items.add(new ItemStack(fortune >= 3 || r.nextInt(Math.max(1, 10 - fortune * 3)) == 0 ? Material.FLINT : Material.GRAVEL));
			}
			break;
		case SNOW_BLOCK:
			if(tool == null || tool.getType().name().contains("SPADE"))
			{
				if(silkTouch > 0)
				{
					items.add(new ItemStack(b.getType(), 1, b.getData()));
				}
				else
				{
					items.add(new ItemStack(Material.SNOW_BALL, 4));
				}
			}
			break;
		case SNOW:
			if(tool == null || tool.getType().name().contains("SPADE"))
			{
				items.add(new ItemStack(Material.SNOW_BALL, 2 + b.getData()));
			}
			break;
		case STONE:
			if(tool == null || tool.getType().name().contains("PICKAXE"))
			{
				if(silkTouch > 0)
				{
					items.add(new ItemStack(b.getType(), 1, b.getData()));
				}
				else
				{
					items.add(new ItemStack(Material.COBBLESTONE));
				}
			}
			break;
		case COAL_ORE:
			if(tool == null || getPickaxeLevel(tool.getType()) >= getPickaxeLevel(Material.WOOD_PICKAXE))
			{
				if(silkTouch > 0)
				{
					items.add(new ItemStack(b.getType(), 1, b.getData()));
				}
				else
				{
					items.add(new ItemStack(Material.COAL, 1 * getOreMultiplier(fortune, r)));
				}
			}
			break;
		case IRON_ORE:
			if(tool == null || getPickaxeLevel(tool.getType()) >= getPickaxeLevel(Material.STONE_PICKAXE))
			{
				items.add(new ItemStack(b.getType()));
			}
			break;
		case GOLD_ORE:
			if(tool == null || getPickaxeLevel(tool.getType()) >= getPickaxeLevel(Material.IRON_PICKAXE))
			{
				items.add(new ItemStack(b.getType()));
			}
			break;
		case REDSTONE_ORE:
		case GLOWING_REDSTONE_ORE:
			if(tool == null || getPickaxeLevel(tool.getType()) >= getPickaxeLevel(Material.IRON_PICKAXE))
			{
				if(silkTouch > 0)
				{
					items.add(new ItemStack(Material.REDSTONE_ORE));
				}
				else
				{
					array = new int[2 + fortune];
					
					amount = 4;
					
					for(int i = 0; i < array.length; i++)
					{
						array[i] = amount++;
					}
					
					items.add(new ItemStack(Material.REDSTONE, array[r.nextInt(array.length)]));
				}
			}
			break;
		case LAPIS_ORE:
			if(tool == null || getPickaxeLevel(tool.getType()) >= getPickaxeLevel(Material.IRON_PICKAXE))
			{
				if(silkTouch > 0)
				{
					items.add(new ItemStack(b.getType(), 1, b.getData()));
				}
				else
				{
					items.add(new ItemStack(Material.INK_SACK, getOreMultiplier(fortune, r) * (4 + r.nextInt(4 + 1)), (short) 4));
				}
			}
			break;
		case EMERALD_ORE:
			if(tool == null || getPickaxeLevel(tool.getType()) >= getPickaxeLevel(Material.IRON_PICKAXE))
			{
				if(silkTouch > 0)
				{
					items.add(new ItemStack(b.getType(), 1, b.getData()));
				}
				else
				{
					items.add(new ItemStack(Material.EMERALD, 1 * getOreMultiplier(fortune, r)));
				}
			}
			break;
		case DIAMOND_ORE:
			if(tool == null || getPickaxeLevel(tool.getType()) >= getPickaxeLevel(Material.IRON_PICKAXE))
			{
				if(silkTouch > 0)
				{
					items.add(new ItemStack(b.getType(), 1, b.getData()));
				}
				else
				{
					items.add(new ItemStack(Material.DIAMOND, 1 * getOreMultiplier(fortune, r)));
				}
			}
			break;
		case QUARTZ_ORE:
			if(tool == null || getPickaxeLevel(tool.getType()) >= getPickaxeLevel(Material.WOOD_PICKAXE))
			{
				if(silkTouch > 0)
				{
					items.add(new ItemStack(b.getType(), 1, b.getData()));
				}
				else
				{
					items.add(new ItemStack(Material.QUARTZ, 1 * getOreMultiplier(fortune, r)));
				}
			}
			break;
		case POTATO:
			if(r.nextInt(50) == 0)
			{
				items.add(new ItemStack(Material.POISONOUS_POTATO, 1));
			}
		case CROPS:
			if(b.getData() == 7)
			{
				items.add(new ItemStack(Material.WHEAT));
			}
		case CARROT:
			switch(b.getType())
			{
			case POTATO:
				drop = Material.POTATO_ITEM;
				break;
			case CROPS:
				drop = Material.SEEDS;
				break;
			case CARROT:
				drop = Material.CARROT_ITEM;
				break;
			default:
				drop = Material.AIR;
				break;
			}
			
			if(b.getData() < 7)
			{
				items.add(new ItemStack(drop, 1));
				break;
			}
			
			double next = r.nextDouble();
			double total = 0.0D;
			int n = 4 + fortune;
			major = 0.57D;
			minor = 1.0D - major;
			
			for(int i = 0; i < n; i++)
			{
				if(next <= (total += Math.pow(minor, n - 1 - i) * Math.pow(major, i) * choose(n - 1, (Math.max(n - 1L - i, i)))))
				{
					items.add(new ItemStack(drop, 1 + i));
					break s0;
				}
			}
			break;
		case COCOA:
			items.add(new ItemStack(Material.INK_SACK, b.getData() == 2 ? 3 : 1, (short) 3));
			break;
		case DEAD_BUSH:
			if(tool != null && tool.getType() == Material.SHEARS)
			{
				items.add(new ItemStack(b.getType(), 1, b.getData()));
			}
			break;
		case DOUBLE_PLANT:
			Block base = b.getData() == 10 ? b.getRelative(BlockFace.DOWN) : b;
			lore = Util.getLore(base);
			
			if(base.getType() != b.getType() || base.getData() == 10)
			{
				break;
			}
			
			if(base.getData() != 2 && base.getData() != 3)
			{
				if(base.equals(b))
				{
					items.add(new ItemStack(Material.DOUBLE_PLANT, 1, base.getData()));
				}
				
				break;
			}
			
			Block up = base.getRelative(BlockFace.UP);
			
			short durability = (short) (base.getData() - 1);
			
			if(tool != null && tool.getType() == Material.SHEARS)
			{
				if(up.getType() == Material.DOUBLE_PLANT && up.getData() == 10)
				{
					base.setType(Material.AIR, base.equals(b));
					Util.writeBlock(up, null);
					items.add(new ItemStack(Material.LONG_GRASS, 2, durability));
				}
				else
				{
					items.add(new ItemStack(Material.LONG_GRASS, 1, durability));
				}
				break;
			}
		case LONG_GRASS:
			if(tool != null && tool.getType() == Material.SHEARS)
			{
				items.add(new ItemStack(b.getType(), 1, b.getData()));
			}
			else
			{
				next = r.nextDouble();
				n = 2 + 2 * fortune;
				
				minor = 0.875D - (b.getType() == Material.LONG_GRASS ? 0.0D : 0.125D);
				major = 1.0D - minor;
				total = 0.0D;
				
				for(int i = 0; i < n; i++)
				{
					if(next <= (total += Math.pow(minor, n - 1 - i) * Math.pow(major, i) * choose(n - 1, (Math.max(n - 1L - i, i))))
							|| i + 1 == n)
					{
						if(i != 0)
						{
							items.add(new ItemStack(Material.SEEDS, i));
						}
						
						break s0;
					}
				}
			}
			break;
		case LEAVES_2:
		case LEAVES:
			byte data = (byte) (b.getType() == Material.LEAVES ? ((Tree) b.getState().getData()).getSpecies().getData() : 4 + b.getData() % 2);
			
			if(tool != null && tool.getType() == Material.SHEARS || silkTouch > 0)
			{
				items.add(new ItemStack(b.getType(), 1, (short) (data < 4 ? data : data - 4)));
				break;
			}
			
			n = r.nextInt(Math.max(20, 200 - fortune * 20 - (fortune >= 5 ? 20 : 0)));
			
			int a = 0;
			
			if(n < (a += (data == 3 ? 5 : 10)))
			{
				items.add(new ItemStack(Material.SAPLING, 1, data));
			}
			else if((data == 0 || data == 5) && n < (a += 1))
			{
				items.add(new ItemStack(Material.APPLE));
			}
			else if(data == 3 && n < (a += 1))
			{
				items.add(new ItemStack(Material.INK_SACK, 1, (short) 3));
			}
			break;
		case MELON_BLOCK:
			if(silkTouch > 0)
			{
				items.add(new ItemStack(b.getType(), 1, b.getData()));
				break;
			}
			
			array = new int[5 + fortune];
			
			amount = 3;
			
			for(int i = 0; i < array.length; i++)
			{
				array[i] = Math.min(9, amount++);
			}
			
			items.add(new ItemStack(Material.MELON, array[r.nextInt(array.length)]));
			break;
		case NETHER_WARTS:
			if(b.getData() != 3)
			{
				items.add(new ItemStack(Material.NETHER_STALK, 1));
				break;
			}
			
			array = new int[3 + fortune];
			
			amount = 2;
			
			for(int i = 0; i < array.length; i++)
			{
				array[i] = amount++;
			}
			
			items.add(new ItemStack(Material.NETHER_STALK, array[r.nextInt(array.length)]));
			break;
		case MELON_STEM:
		case PUMPKIN_STEM:
			next = r.nextDouble();
			n = 4;
			
			double cbrt = Math.cbrt(0.813D);
			
			major = cbrt - (1.0D - cbrt) * b.getData();
			minor = 1.0 - major;
			total = 0.0D;
			
			for(int i = 0; i < n; i++)
			{
				if(next <= (total += Math.pow(minor, n - 1 - i) * Math.pow(major, i) * choose(n - 1, (Math.max(n - 1L - i, i)))))
				{
					amount = n - i - 1;
					
					if(amount != 0)
					{
						items.add(new ItemStack(b.getType() == Material.MELON_STEM ? Material.MELON_SEEDS : Material.PUMPKIN_SEEDS, amount));
					}
					
					break s0;
				}
			}
		case ENDER_CHEST:
			if(tool == null || getPickaxeLevel(tool.getType()) >= getPickaxeLevel(Material.WOOD_PICKAXE))
			{
				if(silkTouch > 0)
				{
					items.add(new ItemStack(Material.ENDER_CHEST));
					break;
				}
				
				items.addAll(b.getDrops());
			}
			break;
		case VINE:
			if(tool != null && tool.getType() == Material.SHEARS)
			{
				items.add(new ItemStack(Material.VINE));
			}
		case MOB_SPAWNER:
			if(tool != null)
			{
				if(!SpecialItems.getInstance().isContainmentPickaxe(tool))
				{
					break;
				}
				
				float chance = SpecialItems.getInstance().getContainmentPickaxeChance(tool);
				
				next = r.nextInt(100) + r.nextDouble();
				
				if(next <= chance)
				{
					CreatureSpawner spawner = (CreatureSpawner) b.getState();
					items.add(new ItemStack(Material.MOB_SPAWNER));
					items.add(new ItemStack(Material.MONSTER_EGG, 1, spawner.getSpawnedType().getTypeId()));
				}
			}
			break;
		default:
			if(tool == null)
			{
				items.addAll(b.getDrops());
			}
			else
			{
				items.addAll(b.getDrops(tool));
			}
			break;
		}
		
		Bukkit.getPluginManager().callEvent(new BreakNaturallyEvent(b));
		
		b.setType(Material.AIR);
		
		Util.writeBlock(b, null);
		
		ItemMeta meta;
		
		for(ItemStack item : items)
		{
			meta = item.getItemMeta();
			meta.setLore(lore);
			item.setItemMeta(meta);
			
			Bukkit.getScheduler().runTask(Main.getInstance(), () ->
			{
				b.getWorld().dropItemNaturally(b.getLocation(), item);
			});
		}
	}
	
	public static int getPickaxeLevel(Material type)
	{
		switch(type)
		{
		case WOOD_PICKAXE:
		case GOLD_PICKAXE:
			return 1;
		case STONE_PICKAXE:
			return 2;
		case IRON_PICKAXE:
			return 3;
		case DIAMOND_PICKAXE:
			return 4;
		default:
			return 0;
		}
	}
	
	private static int getOreMultiplier(int fortune, Random r)
	{
		double nextDouble = r.nextDouble();
		int multiplier = 1;
		double noBonus = 2.0D / (2.0D + fortune);
		
		if(nextDouble > noBonus)
		{
			multiplier += 1 + r.nextInt(Math.max(1, fortune));
		}
		
		return multiplier;
	}
	
	public static long choose(long total, long choose)
	{
		if(total < choose)
		{
			return 0;
		}
		
		if(choose == 0 || choose == total)
		{
			return 1;
		}
		
		return choose(total - 1, choose - 1) + choose(total - 1, choose);
	}
}