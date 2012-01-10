package com.wwsean08.locator;

import java.net.URL;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class Locator extends JavaPlugin{
	Server server;
	private final String key = "get your own";
	private final String lookup = "http://api.ipinfodb.com/v3/ip-country/?key=" + key + "&format=xml&ip=";
	@Override
	public void onDisable() {
	}

	@Override
	public void onEnable() {
		server = Bukkit.getServer();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		boolean result = false;
		if(args.length == 1){
			if(sender instanceof Player){
				Player player = (Player) sender;
				if(player.hasPermission("Geo.geoLocate"))
					result = geoLocate(args[0], sender);
			}else if(sender instanceof ConsoleCommandSender)
				result = geoLocate(args[0], sender);
		}
		return result;
	}
	/**
	 * this does the actual geolocation of the player
	 * @param playerName the player who we are geolocating
	 * @param sender the person who sent the command
	 * @return whether or not the geolocation was successful
	 */
	private boolean geoLocate(String playerName, final CommandSender sender){
		boolean result = false;
		if(server.matchPlayer(playerName) !=null){
			final Player player = server.matchPlayer(playerName).get(0);
			String request = lookup + player.getAddress().getAddress().getHostAddress();
			try {
				SAXParserFactory factory = SAXParserFactory.newInstance();
				SAXParser saxParser = factory.newSAXParser();
				DefaultHandler handler = new DefaultHandler() {
					boolean country = false;
					public void startElement(String uri, String localName,String qName, Attributes attributes) throws SAXException {
						if (qName.equalsIgnoreCase("countryName")) 
							country = true;
					}

					public void endElement(String uri, String localName, String qName) throws SAXException {
					}	//unneeded and unused

					public void characters(char ch[], int start, int length) throws SAXException {
						if (country) {
							String location = new String(ch, start, length);
							sender.sendMessage(ChatColor.GRAY + player.getName() + " is located in " + location.toLowerCase() + " according to their ip address");
							country = false;
						}
					}
				};
				saxParser.parse(new InputSource(new URL(request).openStream()), handler);
				result = true;	//we got thru the parser without an exception, so it worked (or should have)
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else
			sender.sendMessage(ChatColor.GRAY + "There is no player by the name of " + playerName);
		return result;
	}
}