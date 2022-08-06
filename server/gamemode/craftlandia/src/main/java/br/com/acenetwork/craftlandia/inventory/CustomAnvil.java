package br.com.acenetwork.craftlandia.inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Random;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerLevelChangeEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.Repairable;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.acenetwork.commons.CommonsUtil;
import br.com.acenetwork.commons.inventory.GUI;
import br.com.acenetwork.commons.manager.Message;
import br.com.acenetwork.commons.manager.Pitch;
import br.com.acenetwork.commons.player.CommonPlayer;
import br.com.acenetwork.craftlandia.Main;
import br.com.acenetwork.craftlandia.Rarity;
import br.com.acenetwork.craftlandia.Util;
import br.com.acenetwork.craftlandia.item.ContainmentPickaxe;
import br.com.acenetwork.craftlandia.manager.ItemSpecial;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

public class CustomAnvil extends GUI
{
	private ItemStack blackGlass;
	private ItemStack grayGlass;
	private ItemStack levelCost;
	private ItemStack tooExpensive;
	private ItemStack renameItem;
	private ItemStack clearRename;
	
	private ItemStack renamed;
	
	private final Rarity rarity;
	private final Block b;
	private final ResourceBundle bundle;
	
	private final ContainmentPickaxe containmentPickaxe = ItemSpecial.getInstance(ContainmentPickaxe.class);
	
	private int cost;
	private int materials;
	private String levelCostUUID = CommonsUtil.getRandomItemUUID();
	private Rarity worstRarity = Rarity.COMMON;
	private String rename;
	private int task;
	
	public CustomAnvil(CommonPlayer cp, Rarity rarity, Block b)
	{
		this(cp, rarity, b, null, new ItemStack[2]);
	}
	
	public CustomAnvil(CommonPlayer cp, Rarity rarity, Block b, String rename, ItemStack[] content)
	{
		super(cp, () ->
		{
			ResourceBundle bundle = ResourceBundle.getBundle("message", cp.getLocale());
			String title = rarity.getColor() + bundle.getString("inv.anvil." + rarity.name().toLowerCase() + ".title");
			return Bukkit.createInventory(cp.getPlayer(), 5 * 9, title);
		});
		
		bundle = ResourceBundle.getBundle("message", cp.getLocale());
		
		this.rarity = rarity;
		this.b = b;
		this.rename = rename;
		
		task = new BukkitRunnable()
		{
			@Override
			public void run()
			{
				if(b.getType() != Material.ANVIL 
						|| p.getWorld() != b.getWorld()
						|| p.getLocation().distance(b.getLocation()) > 5.0D)
				{
					this.cancel();
					p.closeInventory();
					return;
				}
			}
		}.runTaskTimer(Main.getInstance(), 0L, 1L).getTaskId();
		
		ItemMeta meta;
		
		blackGlass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);
		meta = blackGlass.getItemMeta();
		meta.setDisplayName(CommonsUtil.getRandomItemUUID() + "");
		blackGlass.setItemMeta(meta);
		
		grayGlass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 8);
		meta = grayGlass.getItemMeta();
		meta.setDisplayName(CommonsUtil.getRandomItemUUID() + "");
		grayGlass.setItemMeta(meta);
		
		levelCost = new ItemStack(Material.GLASS_BOTTLE, 1);
		meta = levelCost.getItemMeta();
		meta.setDisplayName(levelCostUUID + "");
		levelCost.setItemMeta(meta);
		
		renameItem = new ItemStack(Material.NAME_TAG, 1);
		meta = renameItem.getItemMeta();
		meta.setDisplayName(CommonsUtil.getRandomItemUUID() + ChatColor.WHITE + StringUtils.capitalize(bundle.getString("verb.rename")));
		renameItem.setItemMeta(meta);
		
		clearRename = new ItemStack(Material.INK_SACK, 1, (short) 1);
		meta = clearRename.getItemMeta();
		meta.setDisplayName(CommonsUtil.getRandomItemUUID() + ChatColor.RED + bundle.getString("inv.anvil.clear-name"));
		clearRename.setItemMeta(meta);
		
		tooExpensive = new ItemStack(Material.INK_SACK, 1, (short) 1);
		meta = tooExpensive.getItemMeta();
		meta.setDisplayName(CommonsUtil.getRandomItemUUID() + ChatColor.RED + "✕ " + bundle.getString("inv.anvil.too-expensive"));
		tooExpensive.setItemMeta(meta);
		
		for(int i = 0; i < inv.getSize(); i++)
		{
			if(i == 10 || i == 12)
			{
				continue;
			}
			
			inv.setItem(i, blackGlass);
		}
		
		inv.setItem(10, content[0]);
		inv.setItem(12, content[1]);
		
		if(rename != null)
		{
			renamed = inv.getItem(10);
		}
		
		refresh();
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void cancelTask(PlayerTeleportEvent e)
	{
		if(e.getPlayer() != p)
		{
			return;
		}
		
		Bukkit.getScheduler().cancelTask(task);
		task = 0;
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void asd(InventoryDragEvent e)
	{
		Player p = cp.getPlayer();
		
		if(e.getWhoClicked() != p)
		{
			return;
		}
		
		for(int rawSlot : e.getRawSlots())
		{
			if(!(rawSlot == 10 || rawSlot == 12) && rawSlot < inv.getSize())
			{
				e.setCancelled(true);
				return;
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void asda(InventoryDragEvent e)
	{
		if(e.getWhoClicked() != p)
		{
			return;
		}
		
		Bukkit.getScheduler().runTask(Main.getInstance(), () ->
		{
			refresh();
		});
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void asda(InventoryClickEvent e)
	{
		if(e.getWhoClicked() != p)
		{
			return;
		}
		
		if(e.getAction() == InventoryAction.NOTHING)
		{
			return;
		}
		
		Bukkit.getScheduler().runTask(Main.getInstance(), () ->
		{
			refresh();
		});
	}
	
	private Material getRepairingMaterial(Material tool)
	{
		switch(tool)
		{
		case DIAMOND_AXE:
		case DIAMOND_BOOTS:
		case DIAMOND_CHESTPLATE:
		case DIAMOND_HELMET:
		case DIAMOND_LEGGINGS:
		case DIAMOND_SWORD:
		case DIAMOND_HOE:
		case DIAMOND_PICKAXE:
		case DIAMOND_SPADE:
			return Material.DIAMOND;
		case IRON_AXE:
		case IRON_BOOTS:
		case IRON_CHESTPLATE:
		case IRON_HELMET:
		case IRON_LEGGINGS:
		case IRON_SWORD:
		case IRON_HOE:
		case IRON_PICKAXE:
		case IRON_SPADE:
			return Material.IRON_INGOT;
		case GOLD_AXE:
		case GOLD_BOOTS:
		case GOLD_CHESTPLATE:
		case GOLD_HELMET:
		case GOLD_LEGGINGS:
		case GOLD_SWORD:
		case GOLD_HOE:
		case GOLD_PICKAXE:
		case GOLD_SPADE:
			return Material.GOLD_INGOT;
		case STONE_AXE:
		case STONE_SWORD:
		case STONE_HOE:
		case STONE_PICKAXE:
		case STONE_SPADE:
			return Material.COBBLESTONE;
		case WOOD_AXE:
		case WOOD_SWORD:
		case WOOD_HOE:
		case WOOD_PICKAXE:
		case WOOD_SPADE:
			return Material.WOOD;
		case LEATHER_LEGGINGS:
		case LEATHER_CHESTPLATE:
		case LEATHER_HELMET:
		case LEATHER_BOOTS:
			return Material.LEATHER;
		default:
			return Material.AIR;
		}
	}
	
	private void refresh()
	{
		ItemStack left = Optional.ofNullable(inv.getItem(10)).orElse(new ItemStack(Material.AIR));
		ItemStack right = Optional.ofNullable(inv.getItem(12)).orElse(new ItemStack(Material.AIR));
		
		Repairable leftMeta = (Repairable) left.getItemMeta();
		Repairable rightMeta = (Repairable) right.getItemMeta();
		
		ItemMeta meta;
		
		worstRarity = Util.getWorstRarity(Optional.ofNullable(Util.getRarity(left)).orElse(Rarity.COMMON),
				Optional.ofNullable(Util.getRarity(right)).orElse(Rarity.COMMON),
				rarity);
		
		int leftCost = leftMeta == null ? 0 : leftMeta.getRepairCost();
		int rightCost = rightMeta == null ? 0 : rightMeta.getRepairCost();
		
		if(!left.equals(renamed))
		{
			rename = null;
		}
		
		inv.setItem(32, left.getType() == Material.AIR || ItemSpecial.isSpecial(left) ? blackGlass : rename == null ? renameItem : clearRename);
		
		ItemStack result = left.clone();
		
		materials = 0;
		int repairCost = getRepairCost(left, right, result);
		int enchantmentCost = getEnchantmentCost(left, right, result);
		int renameCost = rename == null ? 0 : 1;
		
		if(result.getType() != Material.AIR && (repairCost > 0 || enchantmentCost > 0 || renameCost > 0))
		{
			if(leftCost > 256 || rightCost > 256)
			{
				inv.setItem(33, tooExpensive);
				p.updateInventory();
				return;
			}
			
			if(materials == 0 && right.getType() != Material.AIR)
			{
				invalidRecipe();
				return;
			}
			
			cost = leftCost + rightCost + repairCost + enchantmentCost + renameCost;
			
			if(!ItemSpecial.isSpecial(result))
			{
				Util.setCommodity(result, worstRarity);
			}
			
			
			Repairable resultMeta = (Repairable) result.getItemMeta();
			
			if(rename != null)
			{
				((ItemMeta) resultMeta).setDisplayName(rename);
			}
			
			resultMeta.setRepairCost(Math.max(leftCost, rightCost) * 2 + 1);
			result.setItemMeta((ItemMeta) resultMeta);
			
			levelCost.setAmount(cost);
			levelCost.setType(Material.EXP_BOTTLE);
			meta = levelCost.getItemMeta();
			ChatColor color = p.getLevel() < cost && p.getGameMode() != GameMode.CREATIVE ? ChatColor.RED : ChatColor.GREEN;
			
			TextComponent[] extra = new TextComponent[1];
			extra[0] = new TextComponent("" + cost);
			TextComponent text = Message.getTextComponent(bundle.getString("inv.anvil.enchantment-cost"), extra);
			text.setColor(color);
			
			meta.setDisplayName(levelCostUUID + text.toLegacyText());
			levelCost.setItemMeta(meta);
			
			inv.setItem(33, levelCost);
			inv.setItem(16, result);
			
			p.updateInventory();
			
			return;
		}
		
		invalidRecipe();
	}
	
	private void invalidRecipe()
	{
		ItemMeta meta;
		
		cost = Integer.MAX_VALUE / 2;
		materials = Integer.MAX_VALUE / 2;
		
		inv.setItem(16, grayGlass);

		levelCost.setType(Material.GLASS_BOTTLE);
		levelCost.setAmount(1);
		meta = levelCost.getItemMeta();
		meta.setDisplayName(levelCostUUID + "");
		levelCost.setItemMeta(meta);
		
		inv.setItem(33, levelCost);
		p.updateInventory();
	}
	
	private int getRepairCost(ItemStack target, ItemStack sacrifice, ItemStack result)
	{
		if(target.getDurability() == 0)
		{
			return 0;
		}
		
		if(ItemSpecial.isSpecial(target))
		{
			if(!containmentPickaxe.isInstanceOf(target)
					|| !CommonsUtil.containsUUID(sacrifice, CommonsUtil.getHiddenUUIDs(target).get(0)))
			{
				return 0;
			}
			
			float targetChance = containmentPickaxe.getContainmentPickaxeChance(target);
			float sacrificeChance = containmentPickaxe.getContainmentPickaxeChance(sacrifice);
			
			float f = (1.0F - (1.0F - targetChance / 100.0F) * (1.0F - sacrificeChance / 100.0F)) * 100.0F;
			
			CommonsUtil.setItemCopyOf(result, containmentPickaxe.getItemStack(null, f));
			
			materials = 1;
			return 2;
		}
		
		if(target.getType() == sacrifice.getType())
		{
			result.setDurability((short) Math.max(0, target.getDurability() 
					- (sacrifice.getType().getMaxDurability() - sacrifice.getDurability()) - target.getType().getMaxDurability() * 0.12D));
			materials = 1;
			
			return 2;
		}
		
		Material type = getRepairingMaterial(target.getType());
		
		if(type != Material.AIR && type == sacrifice.getType())
		{
			short limit = target.getType().getMaxDurability();
			double points = Math.ceil(((double) limit) / 4.0D);
			int maxMaterials = (int) Math.ceil(target.getDurability() / points);
			materials = Math.min(sacrifice.getAmount(), maxMaterials);
			
			result.setDurability((short) Math.max(0, result.getDurability() - points * materials));
			
			return materials;
		}
		
		return 0;
	}
	
	private int getEnchantmentCost(ItemStack target, ItemStack sacrifice, ItemStack result)
	{
		Map<Enchantment, Integer> sacrificeEnchants = CommonsUtil.getEnchants(sacrifice);
		
		if(target.getType() == Material.AIR || sacrificeEnchants.isEmpty() 
				|| sacrifice.getType().getMaxDurability() == 0 && sacrifice.getType() != Material.ENCHANTED_BOOK
				|| target.getType() != sacrifice.getType() && sacrifice.getType() != Material.ENCHANTED_BOOK
				|| ItemSpecial.isSpecial(target)
				|| ItemSpecial.isSpecial(sacrifice))
		{
			return 0;
		}
		
		boolean isBook = sacrifice.getType() == Material.ENCHANTED_BOOK;
		
		int cost = 0;
		boolean isPossible = false;
		Map<Enchantment, Integer> targetEnchants = CommonsUtil.getEnchants(target);
		
		for(Entry<Enchantment, Integer> entry : sacrificeEnchants.entrySet())
		{
			Enchantment enchantment = entry.getKey();
			int level = entry.getValue();
			
			if(target.getType() != Material.ENCHANTED_BOOK && !enchantment.canEnchantItem(target))
			{
				continue;
			}
			
			isPossible = true;
			
			if(targetEnchants.keySet().stream().filter(x -> x.conflictsWith(enchantment) && !x.equals(enchantment)).findAny().isPresent())
			{
				continue;
			}
			
			int targetLevel = Optional.ofNullable(targetEnchants.get(enchantment)).orElse(0);
			
			int finalLevel = Math.min(Util.getMaxLevel(enchantment, worstRarity), targetLevel == level ? level + 1 : Math.max(targetLevel, level));
			int multiplier = isBook ? CommonsUtil.getEnchantmentMultiplierFromBook(enchantment) : CommonsUtil.getEnchantmentMultiplierFromItem(enchantment);
			
			CommonsUtil.removeEnchant(result, enchantment);
			CommonsUtil.addEnchant(result, enchantment, finalLevel, true);
			
			cost += finalLevel * multiplier;
		}
		
		if(isPossible)
		{
			materials = 1;
			return Math.max(1, cost);
		}
		
		return 0;
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void asd(InventoryClickEvent e)
	{
		Player p = cp.getPlayer();
		
		if(e.getWhoClicked() != p)
		{
			return;
		}
		
		int rawSlot = e.getRawSlot();
		
		ItemStack left = Optional.ofNullable(inv.getItem(10)).orElse(new ItemStack(Material.AIR));
		ItemStack right = Optional.ofNullable(inv.getItem(12)).orElse(new ItemStack(Material.AIR));
		ItemStack result = Optional.ofNullable(inv.getItem(16)).orElse(grayGlass);
		
		ItemStack cursor = e.getCursor();
		ItemStack current = e.getCurrentItem();
		
		if(inv.equals(e.getClickedInventory()))
		{
			e.setCancelled(true);
			
			if(rawSlot == 16)
			{
				if(grayGlass.isSimilar(result) 
						|| (p.getLevel() < cost && p.getGameMode() != GameMode.CREATIVE)
						|| left.getType() == Material.AIR 
						|| (right.getType() == Material.AIR && rename == null)
						|| right.getAmount() < materials 
						|| cursor.getType() != Material.AIR)
				{
					return;
				}
				
				rename = null;
				
				right.setAmount(right.getAmount() - materials);
				
				inv.setItem(10, null);
				inv.setItem(12, right.getAmount() <= 0 ? null : right);
				
				final int finalRepairCost = ((Repairable) result.getItemMeta()).getRepairCost();
				final int timesRepaired = Integer.toBinaryString(finalRepairCost).length() - 1;
				
				boolean destroy = false;
				
				if(p.getGameMode() != GameMode.CREATIVE)
				{
					Random r = new Random();
					int nextInt = r.nextInt(12);
					
					if(b.getType() == Material.ANVIL && nextInt == 0)
					{
						if(b.getData() < 2 * 4)
						{
							b.setData((byte) (b.getData() + 4));
						}
						else
						{
							b.setType(Material.AIR, true);
							destroy = true;
						}
					}
					
					p.setLevel(Math.max(0, p.getLevel() - cost));
				}
				
				p.getWorld().playSound(b.getLocation(), destroy ? Sound.ANVIL_BREAK : Sound.ANVIL_USE, 1.0F, Pitch.getPitch((8 - timesRepaired) * 2));
				
				e.setCancelled(false);
				return;
			}
			else if(rawSlot == 10 || rawSlot == 12)
			{
				e.setCancelled(false);
			}
			else if(CommonsUtil.compareDisplayName(current, clearRename))
			{
				rename = null;
				refresh();
			}
			else if(CommonsUtil.compareDisplayName(current, renameItem))
			{
				ItemStack[] contents = getContents();
				ItemStack[] clonedContents = new ItemStack[contents.length];
				
				for(int i = 0; i < contents.length; i++)
				{
					clonedContents[i] = contents[i] == null ? null : contents[i].clone();
				}
				
				clearContents();
				
				new RenameItem(cp, rarity, b, clonedContents);
			}
			
			return;
		}
	}
	
	@EventHandler
	public void a(PlayerLevelChangeEvent e)
	{
		if(e.getPlayer() != p)
		{
			return;
		}
		
		refresh();
	}
	
	public ItemStack[] getContents()
	{
		ItemStack[] contents = new ItemStack[2];
		
		contents[0] = inv.getItem(10);
		contents[1] = inv.getItem(12);
		
		return contents;
	}
	
	public void clearContents()
	{
		inv.setItem(10, null);
		inv.setItem(12, null);
	}
	
	@EventHandler
	public void a(InventoryCloseEvent e)
	{
		if(e.getPlayer() != p)
		{
			return;
		}
		
		Bukkit.getScheduler().cancelTask(task);
		task = 0;
		
		List<ItemStack> items = new ArrayList<>();
		
		items.add(inv.getItem(10));
		items.add(inv.getItem(12));
		
		items.forEach(x -> 
		{
			if(x != null)
			{
				p.getWorld().dropItem(p.getLocation().add(0.0D, 1.25D, 0.0D), x).setVelocity(p.getLocation().getDirection().multiply(0.35D));
			}
		});
	}
}