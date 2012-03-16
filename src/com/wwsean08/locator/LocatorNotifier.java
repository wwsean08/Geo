package com.wwsean08.locator;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class LocatorNotifier implements Runnable{
	
	String country, player, ip;
	CommandSender sender;
	public LocatorNotifier(String location, String name, String address, CommandSender sender){
		country = location;
		player = name;
		ip = address;
		this.sender = sender;
	}
	
	@Override
	public void run() {
		if(player == null){
			sender.sendMessage(ChatColor.GRAY + "Sorry I couldn't find anyone by that name");
		}else{
			sender.sendMessage(ChatColor.GRAY + player + " is located in " + country.toLowerCase() + " according to their ip address, " + ip);
		}
	}
}