package br.com.acenetwork.commons.listener;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import br.com.acenetwork.commons.event.SocketEvent;
import br.com.acenetwork.commons.executor.Stop;

public class SocketListener implements Listener
{
	@EventHandler
	public void a(SocketEvent e)
	{
		String[] args = e.getArgs();
		
		String cmd = args[0];
		
		if(cmd.equals("stop"))
		{
			Stop.stop();
		}
	}
	
//	@EventHandler
//	public synchronized void a(SocketEvent e)
//	{
//		Socket s = e.getSocket();
//		
//		try(DataInputStream in = new DataInputStream(s.getInputStream()))
//		{
//			String cmd = in.readUTF();
//			
//			if(cmd.equals("broadcast"))
//			{
//				Bukkit.broadcastMessage(in.readUTF());
//			}
//		}
//		catch(IOException ex)
//		{
//			ex.printStackTrace();
//		}
//	}
}