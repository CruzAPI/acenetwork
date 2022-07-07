package br.com.acenetwork.craftlandia.executor;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import com.comphenix.protocol.ProtocolLibrary;

import br.com.acenetwork.commons.player.CommonPlayer;
import br.com.acenetwork.commons.player.craft.CraftCommonPlayer;
import br.com.acenetwork.craftlandia.Main;
import br.com.acenetwork.craftlandia.manager.PRICE;
import br.com.acenetwork.craftlandia.warp.Warp;

public class Temp implements TabExecutor
{
	private static final double HIGHEST = 200.0D;
	private static final double HIGH = 100.0D;
	private static final double NORMAL = 50.0D;
	private static final double LOW = 25.0D;
	private static final double LOWEST = 10.0D;
	private static final double MEME = 5.0D;
	
	public Temp()
	{
		// 200 < 1%  liquidez altíssima
		// 100 < 2% liquidez alta
		// 50 < 4% liquidez média
		// 25 < 7,5% liquidez baixa
		// 10 < 17,5% liquidez baixíssima
		// 5 < 30%  liquidez meme
		
		new PRICE(1, (short) 0, 0.3D, 64, LOW);
		new PRICE(1, (short) 1, 0.3D, 64, LOW);
		new PRICE(1, (short) 2, 0.3D, 64, LOW);
		new PRICE(1, (short) 3, 0.3D, 64, LOW);
		new PRICE(1, (short) 4, 0.3D, 64, LOW);
		new PRICE(1, (short) 5, 0.3D, 64, LOW);
		new PRICE(1, (short) 6, 0.3D, 64, LOW);
		new PRICE(2, (short)0, 0.25D, 640, LOW);
		new PRICE(3, (short)0, 0.01D, 640, NORMAL);
		new PRICE(3, (short)1, 0.01D, 640, NORMAL);
		new PRICE(3, (short)2, 0.01D, 640, NORMAL);
		new PRICE(4, (short)0, 0.03D, 640, NORMAL);
		new PRICE(5, (short)0, 0.1D, 640, HIGHEST);
		new PRICE(5, (short)1, 0.1D, 640, HIGHEST);
		new PRICE(5, (short)2, 0.1D, 640, HIGHEST);
		new PRICE(5, (short)3, 0.1D, 640, HIGHEST);
		new PRICE(5, (short)4, 0.1D, 640, HIGHEST);
		new PRICE(5, (short)5, 0.1D, 640, HIGHEST);
		new PRICE(6, (short)0, 0.1D, 640, LOW);
		new PRICE(6, (short)1, 0.1D, 640, LOW);
		new PRICE(6, (short)2, 0.1D, 640, LOW);
		new PRICE(6, (short)3, 0.1D, 640, LOW);
		new PRICE(6, (short)4, 0.1D, 640, LOW);
		new PRICE(6, (short)5, 0.1D, 640, LOW);
		new PRICE(12, (short)0, 0.04D, 320, NORMAL);
		new PRICE(12, (short)1, 0.04D, 320, NORMAL);
		new PRICE(13, (short)0, 0.04D, 320, NORMAL);
		new PRICE(14, (short)0, 1.5D, 128, LOW);
		new PRICE(15, (short)0, 1.0D, 128, LOW);
		new PRICE(16, (short)0, 1.5D, 128, LOW);
		new PRICE(17, (short)0, 0.4D, 128, NORMAL);
		new PRICE(17, (short)1, 0.4D, 128, NORMAL);
		new PRICE(17, (short)2, 0.4D, 128, NORMAL);
		new PRICE(17, (short)3, 0.4D, 128, NORMAL);
		new PRICE(18, (short)0, 0.03D, 128, NORMAL);
		new PRICE(18, (short)1, 0.03D, 128, NORMAL);
		new PRICE(18, (short)2, 0.03D, 128, NORMAL);
		new PRICE(18, (short)3, 0.03D, 128, NORMAL);
		new PRICE(162, (short)0, 0.4D, 128, NORMAL);
		new PRICE(162, (short)1, 0.4D, 128, NORMAL);
		new PRICE(19, (short)0, 50.0D, 4, LOWEST);
		new PRICE(20, (short)0, 0.15D, 192, NORMAL);
		new PRICE(21, (short)0, 1.2D, 128, LOW);
		new PRICE(22, (short)0, 0.5D, 128, NORMAL);
		new PRICE(24, (short)0, 0.16D, 128, MEME);
		new PRICE(24, (short)0, 0.16D, 128, MEME);
		new PRICE(24, (short)0, 0.16D, 128, MEME);
		new PRICE(35, (short)0, 0.25D, 320, NORMAL);
		new PRICE(35, (short)1, 0.25D, 320, NORMAL);
		new PRICE(35, (short)2, 0.25D, 320, NORMAL);
		new PRICE(35, (short)3, 0.25D, 320, NORMAL);
		new PRICE(35, (short)4, 0.25D, 320, NORMAL);
		new PRICE(35, (short)5, 0.25D, 320, NORMAL);
		new PRICE(35, (short)6, 0.25D, 320, NORMAL);
		new PRICE(35, (short)7, 0.25D, 320, NORMAL);
		new PRICE(35, (short)8, 0.25D, 320, NORMAL);
		new PRICE(35, (short)9, 0.25D, 320, NORMAL);
		new PRICE(35, (short)10, 0.25D, 320, NORMAL);
		new PRICE(35, (short)11, 0.25D, 320, NORMAL);
		new PRICE(35, (short)12, 0.25D, 320, NORMAL);
		new PRICE(35, (short)13, 0.25D, 320, NORMAL);
		new PRICE(35, (short)14, 0.25D, 320, NORMAL);
		new PRICE(35, (short)15, 0.25D, 320, NORMAL);
		new PRICE(45, (short)0, 0.5D, 320, NORMAL);
		new PRICE(47, (short)0, 0.6D, 128, HIGHEST);
		new PRICE(48, (short)0, 2.0D, 256, LOWEST);
		new PRICE(49, (short)0, 1.2D, 256, LOWEST);
		new PRICE(56, (short)0, 3.0D, 256, LOWEST);
		new PRICE(73, (short)0, 0.7D, 256, LOW);
		new PRICE(79, (short)0, 0.2D, 256, NORMAL);
		new PRICE(80, (short)0, 0.15D, 256, NORMAL);
		new PRICE(86, (short)0, 0.05D, 640, HIGH);
		new PRICE(87, (short)0, 0.03D, 640, HIGHEST);
		new PRICE(88, (short)0, 0.05D, 640, HIGHEST);
		new PRICE(348, (short)0, 0.25D, 640, HIGHEST);
		new PRICE(112, (short)0, 0.15D, 640, HIGHEST);
		new PRICE(121, (short)0, 1.0D, 640, NORMAL);
		new PRICE(129, (short)0, 5.0D, 640, NORMAL);
		new PRICE(133, (short)0, 1.2D, 640, NORMAL);
		new PRICE(153, (short)0, 0.2D, 640, NORMAL);
		new PRICE(168, (short)0, 1.0D, 640, NORMAL);
		new PRICE(168, (short)1, 1.0D, 640, NORMAL);
		new PRICE(168, (short)2, 1.0D, 640, NORMAL);
		new PRICE(174, (short)0, 1.0D, 640, LOW);
		new PRICE(37, (short)0, 0.04D, 128, HIGHEST);
		new PRICE(38, (short)0, 0.04D, 128, HIGHEST);
		new PRICE(38, (short)1, 0.04D, 128, HIGHEST);
		new PRICE(38, (short)2, 0.04D, 128, HIGHEST);
		new PRICE(38, (short)3, 0.04D, 128, HIGHEST);
		new PRICE(38, (short)4, 0.04D, 128, HIGHEST);
		new PRICE(38, (short)5, 0.04D, 128, HIGHEST);
		new PRICE(38, (short)6, 0.04D, 128, HIGHEST);
		new PRICE(38, (short)7, 0.04D, 128, HIGHEST);
		new PRICE(38, (short)8, 0.04D, 128, HIGHEST);
		new PRICE(39, (short)0, 0.3D, 128, NORMAL);
		new PRICE(40, (short)0, 0.3D, 128, NORMAL);
		new PRICE(81, (short)0, 0.5D, 640, NORMAL);
		new PRICE(175, (short)0, 0.04D, 640, NORMAL);
		new PRICE(175, (short)1, 0.04D, 640, NORMAL);
		new PRICE(175, (short)2, 0.04D, 640, NORMAL);
		new PRICE(175, (short)3, 0.04D, 640, NORMAL);
		new PRICE(175, (short)4, 0.04D, 640, NORMAL);
		new PRICE(175, (short)5, 0.04D, 640, NORMAL);
		new PRICE(46, (short)0, 0.5D, 128, LOWEST);
		new PRICE(138, (short)0, 0.5D, 640, NORMAL);
		new PRICE(332, (short)0, 0.02D, 320, NORMAL);
		new PRICE(340, (short)0, 0.1D, 640, NORMAL);
		new PRICE(339, (short)0, 0.05D, 640, NORMAL);
		new PRICE(341, (short)0, 0.8D, 640, NORMAL);
		new PRICE(352, (short)0, 0.4D, 640, NORMAL);
		new PRICE(368, (short)0, 0.4D, 640, NORMAL);
		new PRICE(381, (short)0, 1.25D, 640, NORMAL);
		new PRICE(385, (short)0, 2.0D, 128, LOW);
		new PRICE(402, (short)0, 1.2D, 64, MEME);
		new PRICE(260, (short)0, 0.5D, 640, NORMAL);
		new PRICE(282, (short)0, 1.3D, 128, NORMAL);
		new PRICE(319, (short)0, 0.1D, 128, NORMAL);
		new PRICE(322, (short)0, 1.5D, 128, MEME);
		new PRICE(322, (short)1, 5.0D, 128, MEME);
		new PRICE(349, (short)0, 0.2D, 320, NORMAL);
		new PRICE(349, (short)1, 0.2D, 320, NORMAL);
		new PRICE(349, (short)2, 0.2D, 320, NORMAL);
		new PRICE(349, (short)3, 0.2D, 320, NORMAL);
		new PRICE(349, (short)3, 0.2D, 320, NORMAL);
		new PRICE(263, (short)0, 0.7D, 640, HIGH);
		new PRICE(263, (short)1, 0.35D, 640, HIGH);
		new PRICE(264, (short)0, 5.0D, 640, HIGH);
		new PRICE(265, (short)0, 1.3D, 640, HIGH);
		new PRICE(266, (short)0, 2.0D, 640, HIGH);
		new PRICE(287, (short)0, 0.3D, 640, HIGH);
		new PRICE(30, (short)0, 5.0D, 320, NORMAL);
		new PRICE(288, (short)0, 0.2D, 640, HIGH);
		new PRICE(289, (short)0, 0.2D, 640, HIGH);
		new PRICE(295, (short)0, 0.1D, 640, HIGH);
		new PRICE(296, (short)0, 0.5D, 640, HIGHEST);
		new PRICE(318, (short)0, 0.5D, 640, HIGH);
		new PRICE(334, (short)0, 0.4D, 640, HIGH);
		new PRICE(336, (short)0, 0.3D, 640, HIGH);
		new PRICE(337, (short)0, 0.15D, 320, LOW);
		new PRICE(338, (short)0, 0.3D, 640, HIGHEST);
		new PRICE(353, (short)0, 0.3D, 640, LOWEST);
		new PRICE(344, (short)0, 0.15D, 640, NORMAL);
		new PRICE(351, (short)0, 0.15D, 640, NORMAL);
		new PRICE(351, (short)1, 0.15D, 640, NORMAL);
		new PRICE(351, (short)2, 0.15D, 640, NORMAL);
		new PRICE(351, (short)3, 0.15D, 640, NORMAL);
		new PRICE(351, (short)4, 0.4D, 640, HIGH);
		new PRICE(331, (short)0, 0.4D, 640, HIGH);
		new PRICE(351, (short)5, 0.15D, 640, NORMAL);
		new PRICE(351, (short)6, 0.15D, 640, NORMAL);
		new PRICE(351, (short)7, 0.15D, 640, NORMAL);
		new PRICE(351, (short)8, 0.15D, 640, NORMAL);
		new PRICE(351, (short)9, 0.15D, 640, NORMAL);
		new PRICE(351, (short)10, 0.15D, 640, NORMAL);
		new PRICE(351, (short)11, 0.15D, 640, NORMAL);
		new PRICE(351, (short)12, 0.15D, 640, NORMAL);
		new PRICE(351, (short)13, 0.15D, 640, NORMAL);
		new PRICE(351, (short)14, 0.15D, 640, NORMAL);
		new PRICE(351, (short)15, 0.15D, 640, NORMAL);
		new PRICE(351, (short)15, 0.15D, 640, NORMAL);
		new PRICE(361, (short)0, 0.2D, 640, NORMAL);
		new PRICE(362, (short)0, 0.2D, 640, NORMAL);
		new PRICE(369, (short)0, 1.0D, 640, NORMAL);
		new PRICE(372, (short)0, 0.7D, 640, NORMAL);
		new PRICE(388, (short)0, 7.0D, 640, NORMAL);
		new PRICE(399, (short)0, 20.0D, 640, NORMAL);
		new PRICE(405, (short)0, 0.4D, 640, NORMAL);
		new PRICE(406, (short)0, 0.8D, 640, NORMAL);
		new PRICE(409, (short)0, 2.8D, 640, NORMAL);
		new PRICE(410, (short)0, 2.8D, 640, NORMAL);
		new PRICE(415, (short)0, 0.6D, 640, NORMAL);
		new PRICE(391, (short)0, 0.2D, 640, HIGHEST);
		new PRICE(392, (short)0, 0.2D, 640, HIGHEST);
		new PRICE(394, (short)0, 0.2D, 640, NORMAL);
		new PRICE(363, (short)0, 0.25D, 640, HIGHEST);
		new PRICE(365, (short)0, 0.25D, 640, HIGHEST);
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String aliases, String[] args)
	{
		return new ArrayList<>();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String aliases, String[] args)
	{
		if(sender instanceof Player)
		{
			Player p = (Player) sender;
			CommonPlayer cp = CraftCommonPlayer.get(p);
			p.sendMessage("version = " + ProtocolLibrary.getProtocolManager().getProtocolVersion(p));
			sender.sendMessage("" + p.getWorld().getName() + " chunks in memory = " + Warp.MAP.get(p.getWorld().getUID()).blockData.size());
			sender.sendMessage("Unknown command. Type \"/help\" for help.");
			return true;
		}
		
		return true;
	}
}