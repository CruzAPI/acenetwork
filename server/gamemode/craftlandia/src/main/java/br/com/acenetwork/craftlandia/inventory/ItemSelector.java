package br.com.acenetwork.craftlandia.inventory;

import java.util.Iterator;
import java.util.ResourceBundle;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import br.com.acenetwork.commons.CommonsUtil;
import br.com.acenetwork.commons.inventory.AnvilCommand;
import br.com.acenetwork.commons.inventory.Scroller;
import br.com.acenetwork.commons.player.CommonPlayer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

public class ItemSelector extends Scroller
{
	private ItemStack itemSearch;
	private ItemStack clearSearch;
	private String search;
	
	public ItemSelector(CommonPlayer cp)
	{
		this(cp, null);
	}
	
	private class TranslatedItem extends Item
	{
		public TranslatedItem(ItemStack itemStack, ResourceBundle minecraftBundle)
		{
			super(() ->
			{
				ItemMeta meta = itemStack.getItemMeta();
				meta.setDisplayName(ChatColor.WHITE + CommonsUtil.getTranslation(itemStack, minecraftBundle));
				itemStack.setItemMeta(meta);
				
				return itemStack;
			});
		}
		
	}
	
	@SuppressWarnings("deprecation")
	public ItemSelector(CommonPlayer cp, String search)
	{
		super(cp, "inv.item-selector");
		
		this.search = search;
		
		ResourceBundle minecraftBundle = ResourceBundle.getBundle("minecraft", cp.getLocale());
		ResourceBundle bundle = ResourceBundle.getBundle("message", cp.getLocale());
		
		itemList.add(new TranslatedItem(new ItemStack(1, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(1, 1, (short) 1), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(1, 1, (short) 2), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(1, 1, (short) 3), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(1, 1, (short) 4), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(1, 1, (short) 5), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(1, 1, (short) 6), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(2, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(3, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(3, 1, (short) 1), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(3, 1, (short) 2), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(4, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(5, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(5, 1, (short) 1), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(5, 1, (short) 2), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(5, 1, (short) 3), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(5, 1, (short) 4), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(5, 1, (short) 5), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(12, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(12, 1, (short) 1), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(13, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(14, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(15, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(16, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(17, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(17, 1, (short) 1), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(17, 1, (short) 2), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(17, 1, (short) 3), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(19, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(19, 1, (short) 1), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(20, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(21, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(22, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(24, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(24, 1, (short) 1), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(24, 1, (short) 2), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(35, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(35, 1, (short) 1), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(35, 1, (short) 2), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(35, 1, (short) 3), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(35, 1, (short) 4), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(35, 1, (short) 5), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(35, 1, (short) 6), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(35, 1, (short) 7), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(35, 1, (short) 8), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(35, 1, (short) 9), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(35, 1, (short) 10), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(35, 1, (short) 11), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(35, 1, (short) 12), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(35, 1, (short) 13), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(35, 1, (short) 14), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(35, 1, (short) 15), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(41, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(42, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(44, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(44, 1, (short) 1), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(44, 1, (short) 3), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(44, 1, (short) 4), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(44, 1, (short) 5), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(44, 1, (short) 6), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(44, 1, (short) 7), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(45, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(47, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(48, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(49, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(52, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(53, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(56, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(57, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(67, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(73, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(79, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(80, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(82, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(86, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(87, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(88, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(89, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(91, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(95, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(95, 1, (short) 1), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(95, 1, (short) 2), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(95, 1, (short) 3), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(95, 1, (short) 4), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(95, 1, (short) 5), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(95, 1, (short) 6), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(95, 1, (short) 7), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(95, 1, (short) 8), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(95, 1, (short) 9), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(95, 1, (short) 10), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(95, 1, (short) 11), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(95, 1, (short) 12), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(95, 1, (short) 13), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(95, 1, (short) 14), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(95, 1, (short) 15), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(98, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(98, 1, (short) 1), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(98, 1, (short) 2), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(98, 1, (short) 3), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(103, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(108, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(109, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(110, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(112, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(114, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(121, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(126, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(126, 1, (short) 1), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(126, 1, (short) 2), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(126, 1, (short) 3), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(126, 1, (short) 4), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(126, 1, (short) 5), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(128, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(129, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(133, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(134, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(135, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(136, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(139, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(139, 1, (short) 1), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(153, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(155, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(155, 1, (short) 1), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(155, 1, (short) 2), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(156, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(159, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(159, 1, (short) 1), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(159, 1, (short) 2), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(159, 1, (short) 3), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(159, 1, (short) 4), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(159, 1, (short) 5), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(159, 1, (short) 6), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(159, 1, (short) 7), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(159, 1, (short) 8), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(159, 1, (short) 9), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(159, 1, (short) 10), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(159, 1, (short) 11), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(159, 1, (short) 12), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(159, 1, (short) 13), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(159, 1, (short) 14), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(159, 1, (short) 15), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(162, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(162, 1, (short) 1), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(163, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(164, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(168, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(168, 1, (short) 1), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(168, 1, (short) 2), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(169, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(170, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(172, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(173, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(174, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(179, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(179, 1, (short) 1), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(179, 1, (short) 2), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(180, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(182, 1, (short) 0), minecraftBundle));
		
		itemList.add(new TranslatedItem(new ItemStack(6, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(6, 1, (short) 1), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(6, 1, (short) 2), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(6, 1, (short) 3), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(6, 1, (short) 4), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(6, 1, (short) 5), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(18, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(18, 1, (short) 1), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(18, 1, (short) 2), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(18, 1, (short) 3), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(30, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(31, 1, (short) 1), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(31, 1, (short) 2), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(32, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(37, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(38, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(38, 1, (short) 1), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(38, 1, (short) 2), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(38, 1, (short) 3), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(38, 1, (short) 4), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(38, 1, (short) 5), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(38, 1, (short) 6), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(38, 1, (short) 7), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(38, 1, (short) 8), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(39, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(40, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(50, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(54, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(58, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(61, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(65, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(78, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(81, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(84, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(85, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(97, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(97, 1, (short) 1), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(97, 1, (short) 2), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(97, 1, (short) 3), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(97, 1, (short) 4), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(97, 1, (short) 5), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(101, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(102, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(106, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(111, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(113, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(116, 1, (short) 0), minecraftBundle));
		
		itemList.add(new TranslatedItem(new ItemStack(130, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(145, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(145, 1, (short) 1), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(145, 1, (short) 2), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(146, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(160, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(160, 1, (short) 1), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(160, 1, (short) 2), minecraftBundle));	
		itemList.add(new TranslatedItem(new ItemStack(160, 1, (short) 3), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(160, 1, (short) 4), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(160, 1, (short) 5), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(160, 1, (short) 6), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(160, 1, (short) 7), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(160, 1, (short) 8), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(160, 1, (short) 9), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(160, 1, (short) 10), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(160, 1, (short) 11), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(160, 1, (short) 12), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(160, 1, (short) 13), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(160, 1, (short) 14), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(160, 1, (short) 15), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(161, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(161, 1, (short) 1), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(165, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(171, 1, (short) 0), minecraftBundle));		
		itemList.add(new TranslatedItem(new ItemStack(171, 1, (short) 1), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(171, 1, (short) 2), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(171, 1, (short) 3), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(171, 1, (short) 4), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(171, 1, (short) 5), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(171, 1, (short) 6), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(171, 1, (short) 7), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(171, 1, (short) 8), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(171, 1, (short) 9), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(171, 1, (short) 10), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(171, 1, (short) 11), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(171, 1, (short) 12), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(171, 1, (short) 13), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(171, 1, (short) 14), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(171, 1, (short) 15), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(175, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(175, 1, (short) 1), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(175, 1, (short) 2), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(175, 1, (short) 3), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(175, 1, (short) 4), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(175, 1, (short) 5), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(188, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(189, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(190, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(191, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(192, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(321, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(323, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(355, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(389, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(390, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(397, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(397, 1, (short) 1), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(397, 1, (short) 2), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(397, 1, (short) 3), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(397, 1, (short) 4), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(416, 1, (short) 0), minecraftBundle));
		
		itemList.add(new TranslatedItem(new ItemStack(23, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(25, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(29, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(33, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(46, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(69, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(70, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(72, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(76, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(77, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(96, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(107, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(123, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(131, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(143, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(147, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(148, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(151, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(152, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(154, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(158, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(167, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(183, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(184, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(185, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(186, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(187, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(324, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(330, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(331, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(356, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(404, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(427, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(428, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(429, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(430, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(431, 1, (short) 0), minecraftBundle));
		
		itemList.add(new TranslatedItem(new ItemStack(27, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(28, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(66, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(157, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(328, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(329, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(333, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(342, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(343, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(398, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(407, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(408, 1, (short) 0), minecraftBundle));
		
		itemList.add(new TranslatedItem(new ItemStack(138, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(325, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(326, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(327, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(332, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(335, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(339, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(340, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(341, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(352, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(368, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(381, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(383, 1, (short) 50), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(383, 1, (short) 51), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(383, 1, (short) 52), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(383, 1, (short) 54), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(383, 1, (short) 55), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(383, 1, (short) 56), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(383, 1, (short) 57), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(383, 1, (short) 58), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(383, 1, (short) 59), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(383, 1, (short) 60), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(383, 1, (short) 61), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(383, 1, (short) 62), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(383, 1, (short) 65), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(383, 1, (short) 66), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(383, 1, (short) 67), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(383, 1, (short) 68), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(383, 1, (short) 90), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(383, 1, (short) 91), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(383, 1, (short) 92), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(383, 1, (short) 93), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(383, 1, (short) 94), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(383, 1, (short) 95), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(383, 1, (short) 96), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(383, 1, (short) 98), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(383, 1, (short) 100), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(383, 1, (short) 101), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(383, 1, (short) 120), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(384, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(385, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(386, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(395, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(402, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(417, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(418, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(419, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(2256, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(2257, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(2258, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(2259, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(2260, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(2261, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(2262, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(2263, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(2264, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(2265, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(2266, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(2267, 1, (short) 0), minecraftBundle));
		
		itemList.add(new TranslatedItem(new ItemStack(260, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(282, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(297, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(319, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(320, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(322, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(322, 1, (short) 1), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(349, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(349, 1, (short) 1), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(349, 1, (short) 2), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(349, 1, (short) 3), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(350, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(350, 1, (short) 1), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(354, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(357, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(360, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(363, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(364, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(365, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(366, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(367, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(375, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(391, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(392, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(393, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(394, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(400, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(411, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(412, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(413, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(423, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(424, 1, (short) 0), minecraftBundle));
		
		itemList.add(new TranslatedItem(new ItemStack(256, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(257, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(258, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(259, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(269, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(270, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(271, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(273, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(274, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(275, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(277, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(278, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(279, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(284, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(285, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(286, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(290, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(291, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(292, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(293, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(294, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(345, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(346, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(347, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(359, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(420, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(421, 1, (short) 0), minecraftBundle));

		itemList.add(new TranslatedItem(new ItemStack(261, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(262, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(267, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(268, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(272, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(276, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(283, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(298, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(299, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(300, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(301, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(302, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(303, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(304, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(305, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(306, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(307, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(308, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(309, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(310, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(311, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(312, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(313, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(314, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(315, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(316, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(317, 1, (short) 0), minecraftBundle));
		
		itemList.add(new TranslatedItem(new ItemStack(403, 1, (short) 0), minecraftBundle));
		
		itemList.add(new TranslatedItem(new ItemStack(370, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(373, 1, (short) 8193), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(373, 1, (short) 8225), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(373, 1, (short) 8257), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(373, 1, (short) 16385), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(373, 1, (short) 16417), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(373, 1, (short) 16449), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(373, 1, (short) 8194), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(373, 1, (short) 8226), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(373, 1, (short) 8258), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(373, 1, (short) 16386), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(373, 1, (short) 16418), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(373, 1, (short) 16450), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(373, 1, (short) 8227), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(373, 1, (short) 8259), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(373, 1, (short) 16419), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(373, 1, (short) 16451), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(373, 1, (short) 8196), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(373, 1, (short) 8228), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(373, 1, (short) 8260), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(373, 1, (short) 16388), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(373, 1, (short) 16420), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(373, 1, (short) 16452), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(373, 1, (short) 8261), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(373, 1, (short) 8229), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(373, 1, (short) 16453), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(373, 1, (short) 16421), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(373, 1, (short) 8230), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(373, 1, (short) 8262), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(373, 1, (short) 16422), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(373, 1, (short) 16454), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(373, 1, (short) 8232), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(373, 1, (short) 8264), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(373, 1, (short) 16424), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(373, 1, (short) 16456), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(373, 1, (short) 8201), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(373, 1, (short) 8233), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(373, 1, (short) 8265), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(373, 1, (short) 16393), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(373, 1, (short) 16425), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(373, 1, (short) 16457), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(373, 1, (short) 8234), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(373, 1, (short) 8266), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(373, 1, (short) 16426), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(373, 1, (short) 16458), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(373, 1, (short) 8267), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(373, 1, (short) 8235), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(373, 1, (short) 16459), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(373, 1, (short) 16427), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(373, 1, (short) 8268), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(373, 1, (short) 8236), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(373, 1, (short) 16460), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(373, 1, (short) 16428), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(373, 1, (short) 8237), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(373, 1, (short) 8269), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(373, 1, (short) 16429), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(373, 1, (short) 16461), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(373, 1, (short) 8238), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(373, 1, (short) 8270), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(373, 1, (short) 16430), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(373, 1, (short) 16462), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(374, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(376, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(377, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(378, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(379, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(380, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(382, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(396, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(414, 1, (short) 0), minecraftBundle));
		
		itemList.add(new TranslatedItem(new ItemStack(263, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(263, 1, (short) 1), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(264, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(265, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(266, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(280, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(281, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(287, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(288, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(289, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(295, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(296, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(318, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(334, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(336, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(337, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(338, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(344, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(348, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(351, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(351, 1, (short) 1), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(351, 1, (short) 2), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(351, 1, (short) 3), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(351, 1, (short) 4), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(351, 1, (short) 5), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(351, 1, (short) 6), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(351, 1, (short) 7), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(351, 1, (short) 8), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(351, 1, (short) 9), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(351, 1, (short) 10), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(351, 1, (short) 11), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(351, 1, (short) 12), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(351, 1, (short) 13), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(351, 1, (short) 14), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(351, 1, (short) 15), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(353, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(361, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(362, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(369, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(371, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(372, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(388, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(399, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(405, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(406, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(409, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(410, 1, (short) 0), minecraftBundle));
		itemList.add(new TranslatedItem(new ItemStack(415, 1, (short) 0), minecraftBundle));
		
		Iterator<Item> iterator = itemList.iterator();
		
		ItemMeta meta;
		
		if(search != null)
		{
			while(iterator.hasNext())
			{
				Item item = iterator.next();
				
				if(!item.getItemMeta().getDisplayName().substring(2).toLowerCase().contains(search.toLowerCase()))
				{
					iterator.remove();
				}
			}
			
			clearSearch = new ItemStack(Material.INK_SACK, 1, (short) 1);
			meta = clearSearch.getItemMeta();
			meta.setDisplayName(ChatColor.RED + bundle.getString("inv.item-selector.clear-search"));
			clearSearch.setItemMeta(meta);
			
			inv.setItem(2, clearSearch);
		}
		
		itemSearch = new ItemStack(Material.NAME_TAG);
		meta = itemSearch.getItemMeta();
		meta.setDisplayName(ChatColor.WHITE + bundle.getString("inv.item-selector.search-item"));
		itemSearch.setItemMeta(meta);		
		inv.setItem(3, itemSearch);
		
		refresh();
	}
	
	@EventHandler
	public void asdasdf(InventoryClickEvent e)
	{
		Player p = cp.getPlayer();
		
		if(e.getWhoClicked() != p)
		{
			return;
		}
		
		e.setCancelled(true);
		
		ClickType click = e.getClick();
		ItemStack current = e.getCurrentItem();
		
		if(CommonsUtil.compareDisplayName(current, itemSearch) && click == ClickType.LEFT)
		{
			ResourceBundle bundle = ResourceBundle.getBundle("message", cp.getLocale());
			
			new AnvilCommand(cp, search == null ? bundle.getString("inv.item-selector.search-item") : search, "/shopsearch {0}", search != null);
		}
		else if(CommonsUtil.compareDisplayName(current, clearSearch) && click == ClickType.LEFT)
		{
			Bukkit.dispatchCommand(p, "shop");
		}
		else
		{
			for(ItemStack item : itemList)
			{
				if(item.equals(current))
				{
					p.closeInventory();
					Bukkit.dispatchCommand(p, "shop " + item.getTypeId() + " " + item.getDurability());
					return;
				}
			}
				
		}
	}
}