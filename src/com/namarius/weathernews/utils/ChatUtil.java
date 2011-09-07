package com.namarius.weathernews.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.ListIterator;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
/*
import com.namarius.complexredstone.ComplexRedstone;
import com.namarius.complexredstone.message.AbstractMessage;
*/
public final class ChatUtil {
	public enum Symbol {
		Newline, DefaultColor, End;
	}

	private static final ChatColor errorcolor = ChatColor.RED;
	private static final ChatColor note = ChatColor.GREEN;
	private static final int maxlinelength = 59;
	private static final HashMap<Player, String[]> output = new HashMap<Player, String[]>();

	public static String[] getLines(Player player) {
		return output.get(player);
	}

	@Deprecated
	public static void send(CommandSender sender, String color, String message) {
		while (message.length() > 0) {
			message = message.trim();
			int lastspace = message.substring(
					0,
					(maxlinelength > message.length() ? message.length()
							: maxlinelength)).lastIndexOf(' ');
			if (lastspace < 0)
				lastspace = (maxlinelength > message.length() ? message
						.length() : maxlinelength);
			if (message.length() < maxlinelength)
				lastspace = message.length();
			sender.sendMessage(color + message.substring(0, lastspace));
			message = message.substring(lastspace);
		}
	}

	public static void rawsendout(CommandSender sender, String message) {
		if (message.length() > 2 && message.charAt(2) == ' ') {
			message = message.substring(0, 2).concat(message.substring(3)).trim();
		}
		sender.sendMessage(message);
	}

	public static void rawsendout(CommandSender sender, String[] messages) {
		for (String string : messages) {
			if (string.length() > 2 && string.charAt(2) == ' ') {
				string = string.substring(0, 2).concat(string.substring(3)).trim();
			}
			sender.sendMessage(string);
		}
	}
	
	public static void send(Object[] message, CommandSender sender,
			ChatColor defaultcolor) {
		ArrayList<Object> splitted = new ArrayList<Object>();
		ChatColor currentcolor = defaultcolor!=null?defaultcolor:ChatColor.WHITE;
		splitted.add(defaultcolor);
		for(Object o : message)
		{
			if(o instanceof String)
			{
				String s = (String)o;
				for(String split : s.split("\\s"))
				{
					if(split!=null)
					{
						splitted.add(split);
					}
				}
				continue;
			}
			else if(o instanceof ChatColor)
			{
				currentcolor=(ChatColor) o;
			}
			else if(o instanceof Symbol)
			{
				if(o == Symbol.DefaultColor)
				{
					currentcolor=defaultcolor;
					splitted.add(currentcolor);
					continue;
				}
				if(o == Symbol.Newline)
				{
					splitted.add(o);
					splitted.add(currentcolor);
					continue;
				}
			}
			splitted.add(o);
		}
		splitted.add(Symbol.End);
		int linelength=0;
		for(ListIterator<Object> it = splitted.listIterator();it.hasNext();)
		{
			Object o = it.next();
			if( o == Symbol.Newline)
				linelength=0;
			if( o instanceof String)
			{
				String s = (String)o;
				if(linelength!=0)
					s=" "+s;
				if(linelength+s.length()>maxlinelength)
				{
					if((s.length()-1)>maxlinelength)
					{
						s=(String) o;
						it.previous();
						it.add(Symbol.Newline);
						it.add(currentcolor);
						it.next();
						it.set(s.substring(Math.min(maxlinelength, s.length())));
						it.add(Symbol.Newline);
						it.add(currentcolor);
						it.add(s.substring(0,Math.min(maxlinelength, s.length())));						
						it.previous();
						linelength=0;
						continue;
					}
					else
					{
						it.previous();
						it.add(Symbol.Newline);
						it.add(currentcolor);
					}
					linelength=0;
				}
				linelength+=s.length();
			}
		}
		ArrayList<String> strings = new ArrayList<String>();
		String current = "";
		for(Object o : splitted)
		{
			//System.out.println(o);
			if(o == Symbol.Newline || o==Symbol.End)
			{
				strings.add(current);
				current="";
			}
			else
				current+=(current.isEmpty()?"":" ")+o.toString();
		}
		String[] out = new String[strings.size()];
		out = strings.toArray(out);
		ChatUtil.output.put((Player) sender, out);
		rawsendout(sender, out);
	}

	public static void error(Object[] message, CommandSender sender) {
		send(message, sender, errorcolor);
	}

	public static void error(CommandSender sender, String message) {
		Object[] tempmessage = { message };
		send(tempmessage, sender, errorcolor);
	}

/*	public static void error(CommandSender sender, AbstractMessage message) {
		error(sender, message.toString());

	}*/

	public static void tooMany(int number, CommandSender sender) {
		Object[] message = { "Too many parameters:" + number };
		error(message, sender);
	}

	public static void tooFew(int number, CommandSender sender) {
		Object[] message = { "Too few parameters:" + number };
		error(message, sender);
	}

	public static void empty(CommandSender sender) {
		Object[] message = { "Parameter was emtpy" };
		error(message, sender);
	}

	public static void wrongType(CommandSender sender, int pos, String type) {
		error(sender, "The parameter " + pos + " wan't a '" + type + "'");
	}

	public static void note(Object[] message, CommandSender sender) {
		send(message, sender, note);
	}

	public static void note(CommandSender sender, String message) {
		Object[] tempmessage = { message };
		note(tempmessage, sender);
	}

	/*public static void note(CommandSender sender, AbstractMessage message) {
		note(sender, message.toString());
	}*/

}
