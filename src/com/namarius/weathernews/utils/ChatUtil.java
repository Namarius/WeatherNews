package com.namarius.weathernews.utils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
/*
import com.namarius.complexredstone.ComplexRedstone;
import com.namarius.complexredstone.message.AbstractMessage;
*/
public final class ChatUtil {
	private static final ChatColor errorcolor = ChatColor.RED;
	private static final ChatColor note = ChatColor.GREEN;
	private static final int MAXLINELENGTH = 59;

	public static void rawsendout(CommandSender sender, String message) {
		sender.sendMessage(message);
	}
	
	public static void send(Object[] message, CommandSender sender)
	{
		StringBuilder out = new StringBuilder();
		for(Object o : message)
		{
			out.append(o.toString());
		}
		send(out.toString(),sender);
	}
	
	public static void send(Object[] message, CommandSender sender,ChatColor color)
	{
		StringBuilder out = new StringBuilder();
		out.append(color.toString());
		for(Object o : message)
		{
			out.append(o.toString());
		}
		send(out.toString(),sender);
	}
	
	public static void send(String message, CommandSender sender) {
		StringBuilder out = new StringBuilder();
		try
		{
			while(message.length()>0)
			{
				int i=message.substring(0, MAXLINELENGTH).lastIndexOf(' ');
				if(i<1)
				{
					out.append(message.substring(0,MAXLINELENGTH)+'\n');
					message=message.substring(MAXLINELENGTH);
				}
				else
				{
					out.append(message.substring(0,i)+'\n');
					message=message.substring(i+1);
				}
			}
		}
		catch (ArrayIndexOutOfBoundsException e) {
			// TODO: handle exception
		}
		rawsendout(sender,out.toString());
	}

	public static void error(Object[] message, CommandSender sender) {
		send(message, sender, errorcolor);
	}

	public static void error(CommandSender sender, String message) {
		Object[] tempmessage = { message };
		send(tempmessage, sender, errorcolor);
	}

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
}
