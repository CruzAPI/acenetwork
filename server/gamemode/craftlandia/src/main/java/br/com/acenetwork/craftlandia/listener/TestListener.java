package br.com.acenetwork.craftlandia.listener;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import br.com.acenetwork.commons.CommonsUtil;
import br.com.acenetwork.commons.event.PlayerConsumeSoupEvent;
import br.com.acenetwork.commons.executor.VipChest;
import br.com.acenetwork.commons.manager.Message;
import br.com.acenetwork.craftlandia.Rarity;
import br.com.acenetwork.craftlandia.Util;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

public class TestListener implements Listener
{
	@EventHandler
	public void a(PlayerConsumeSoupEvent e)
	{
		Rarity rarity = Optional.ofNullable(Util.getRarity(e.getSoup())).orElse(Rarity.COMMON);
		
		int heal;
		
		switch(rarity)
		{
		case LEGENDARY:
			heal = 10;
			break;
		case RARE:
			heal = 7;
			break;
		default:
			heal = 4;
			break;
		}
		
		e.setHeal(heal);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void vipActivation(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		
		ItemStack item = e.getItem();
		
		List<UUID> list = CommonsUtil.getHiddenUUIDs(item);
		
		VipChest vipChest = VipChest.getInstance();
		
		if(list.size() != 2 || !list.get(0).equals(vipChest.getUUID()))
		{
			return;
		}
		
		e.setCancelled(true);
		
		if(!e.getAction().name().contains("RIGHT"))
		{
			return;
		}
		
		int amount = item.getAmount();
		
		if(amount <= 0)
		{
			return;
		}
		
		boolean isValid = vipChest.getValidVips().remove(list.get(1));
		item.setAmount(--amount);
		
		if(amount <= 0 || !isValid)
		{
			p.setItemInHand(null);
		}
		
		if(!isValid)
		{
			p.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "INVALID VIP ITEM");
			return;
		}
		
		
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
		
		p.setFallDistance(-9999.9F);
		p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 5, 5));
		p.getWorld().createExplosion(p.getLocation(), 6.0F);
		
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
}
