package br.com.acenetwork.craftlandia.item;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import br.com.acenetwork.commons.CommonsUtil;
import br.com.acenetwork.commons.constants.Tag;
import br.com.acenetwork.commons.manager.Message;
import br.com.acenetwork.commons.player.CommonPlayer;
import br.com.acenetwork.commons.player.craft.CraftCommonPlayer;
import br.com.acenetwork.craftlandia.Rarity;
import br.com.acenetwork.craftlandia.Util;
import br.com.acenetwork.craftlandia.manager.Activatable;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

public class VipItem extends Activatable
{
	public static final String SKIN_VALUE = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDVjNmRjMmJiZjUxYzM2Y2ZjNzcxNDU4NWE2YTU2ODNlZjJiMTRkNDdkOGZmNzE0NjU0YTg5M2Y1ZGE2MjIifX19";
	
	@SuppressWarnings("unused")
	private static VipItem instance;
	
	private VipItem()
	{
		super("vip_item");
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void a(EntityDamageEvent e)
	{
		if(!(e.getEntity() instanceof Player))
		{
			return;
		}
		
		Player p = (Player) e.getEntity();
		
		if(p.getHealth() - e.getFinalDamage() <= 0.0D)
		{
			for(int i = 0; i < p.getInventory().getSize(); i++)
			{
				if(consume(p, i) == 0)
				{
					e.setCancelled(true);
					p.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 20 * 5, 2));
					p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20 * 5, 2));
					run(p);
					break;
				}
			}
		}
	}
	
	@Override
	public void run(Player p)
	{
		List<CommandSender> senders = new ArrayList<>(Bukkit.getOnlinePlayers());
		senders.add(Bukkit.getConsoleSender());
		
		TextComponent[] extra = new TextComponent[1];
		
		extra[0] = new TextComponent(p.getDisplayName());
		
		for(CommandSender sender : senders)
		{
			ResourceBundle bundle = ResourceBundle.getBundle("message");
			
			TextComponent text = Message.getTextComponent(bundle.getString("broadcast.vip-activated"), extra);
			text.setColor(ChatColor.GOLD);
			
			sender.sendMessage("");
			sender.sendMessage(text.toLegacyText());
			sender.sendMessage("");
		}
		
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "pex group vip user add " + p.getName() +  " +" + (30L * 24L * 60L * 60L));
		
		CommonPlayer cp = CraftCommonPlayer.get(p);
		cp.setTag(Tag.VIP);
		
		double shards = 10000.0D;
		cp.setBalance(cp.getBalance() + shards);
		
		ResourceBundle bundle = ResourceBundle.getBundle("message", cp.getLocale());
		DecimalFormat df = new DecimalFormat("#.##", new DecimalFormatSymbols(bundle.getLocale()));
		
		p.sendMessage(ChatColor.DARK_GREEN + "(+" + df.format(shards) + " SHARDS)");
		
		p.setFallDistance(-256.0F);
		
		PotionEffect resistance = null;
		
		for(PotionEffect effects : p.getActivePotionEffects())
		{
			if(effects.getType() == PotionEffectType.DAMAGE_RESISTANCE)
			{
				resistance = effects;
				break;
			}
		}
		
		p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 1, 5));
		p.getWorld().createExplosion(p.getLocation(), 6.0F);
		p.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
		
		if(resistance != null)
		{
			p.addPotionEffect(resistance);
		}
		
		Firework f = (Firework) p.getWorld().spawnEntity(p.getLocation(), EntityType.FIREWORK);
		FireworkMeta fireworkMeta = f.getFireworkMeta();
		
		fireworkMeta.addEffect(FireworkEffect.builder().with(FireworkEffect.Type.BALL).withColor(Color.GRAY).build());
		fireworkMeta.addEffect(FireworkEffect.builder().with(FireworkEffect.Type.BALL_LARGE).withColor(Color.AQUA).build());
		fireworkMeta.setPower(1);
		
		List<ItemStack> commonItems = new ArrayList<ItemStack>();
		
		ItemStack helmet = new ItemStack(Material.DIAMOND_HELMET);
		ItemStack chestplate = new ItemStack(Material.DIAMOND_CHESTPLATE);
		ItemStack leggings = new ItemStack(Material.DIAMOND_LEGGINGS);
		ItemStack boots = new ItemStack(Material.DIAMOND_BOOTS);
		
		commonItems.add(helmet);
		commonItems.add(chestplate);
		commonItems.add(leggings);
		commonItems.add(boots);
		
		commonItems.forEach(x -> 
		{
			x.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
			x.addEnchantment(Enchantment.DURABILITY, 3);
		});
		
		ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
		
		sword.addEnchantment(Enchantment.DAMAGE_ALL, 2);
		sword.addEnchantment(Enchantment.DURABILITY, 3);
		
		commonItems.add(sword);
		
		ItemStack[] tools = new ItemStack[]
		{
			new ItemStack(Material.DIAMOND_PICKAXE),	
			new ItemStack(Material.DIAMOND_AXE),	
			new ItemStack(Material.DIAMOND_SPADE),	
		};
		
		for(ItemStack tool : tools)
		{
			tool.addEnchantment(Enchantment.DIG_SPEED, 2);
			tool.addEnchantment(Enchantment.DURABILITY, 3);
			
			commonItems.add(tool);
		}
		
		commonItems.add(new ItemStack(Material.GOLDEN_APPLE, 2, (short) 1));
		commonItems.add(new ItemStack(Material.GRILLED_PORK, 32));
		
		Random r = new Random();
		
		List<ItemStack> rareItems = new ArrayList<ItemStack>();
		
		if(r.nextInt(10) < 4)
		{
			rareItems.add(new ItemStack(Material.MOB_SPAWNER));
			
			short[] array = new short[] {50, 51, 52, 54, 55, 56, 57, 58, 59, 60, 61, 62, 65,
					66, 67, 68, 90, 91, 92, 93, 94, 95, 96, 98, 100, 101, 120};
			
			rareItems.add(new ItemStack(Material.MONSTER_EGG, 1, array[r.nextInt(array.length)]));
		}
		
		commonItems.forEach(x -> Util.setCommodity(x, Rarity.COMMON));
		rareItems.forEach(x -> Util.setCommodity(x, Rarity.RARE));
		
		if(p.getInventory().getHelmet() == null)
		{
			p.getInventory().setHelmet(helmet);
			commonItems.remove(helmet);
		}
		
		if(p.getInventory().getChestplate() == null)
		{
			p.getInventory().setChestplate(chestplate);
			commonItems.remove(chestplate);
		}
		
		if(p.getInventory().getLeggings() == null)
		{
			p.getInventory().setLeggings(leggings);
			commonItems.remove(leggings);
		}
		
		if(p.getInventory().getBoots() == null)
		{
			p.getInventory().setBoots(boots);
			commonItems.remove(boots);
		}
		
		List<ItemStack> items = new ArrayList<>();
		
		items.addAll(commonItems);
		items.addAll(rareItems);
		
		for(ItemStack itemStack : commonItems)
		{
			for(ItemStack drops : p.getInventory().addItem(itemStack).values())
			{
				CommonsUtil.dropItem(p, drops);
				continue;
			}
		}
		
		f.setFireworkMeta(fireworkMeta);
	}
	
	@Override
	public ItemStack getItemStack(ResourceBundle bundle, Object... args)
	{
		int version = args.length >= 2 ? (int) args[1] : 47;
		ItemStack item;
		ItemMeta meta;
		
		if(version > 5)
		{
			item = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
			meta = item.getItemMeta();
			CommonsUtil.setCustomSkull((SkullMeta) meta, SKIN_VALUE);
		}
		else
		{
			item = new ItemStack(Material.TRAPPED_CHEST);
			meta = item.getItemMeta();
			meta.addEnchant(Enchantment.DURABILITY, 1, true);
			meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		}
		
		UUID random;
		
		if(args.length >= 1 && args[0] != null)
		{
			random = (UUID) args[0];
		}
		else
		{
			random = UUID.randomUUID();
			validSet.add(random);
		}
		
		meta.setDisplayName(CommonsUtil.hideUUID(uuid) + ChatColor.RESET + CommonsUtil.hideUUID(random)
				+ ChatColor.GOLD + ChatColor.BOLD + "VIP");
		item.setItemMeta(meta);
		
		return item;
	}
}