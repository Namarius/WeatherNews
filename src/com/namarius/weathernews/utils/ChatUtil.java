package com.namarius.weathernews.utils;

import java.io.IOException;
import java.io.StringReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public final class ChatUtil {
	private static final ChatColor errorcolor = ChatColor.RED;
	private static final ChatColor note = ChatColor.GREEN;
    private static final int[] characterWidths = new int[] {
        1, 9, 9, 8, 8, 8, 8, 7, 9, 8, 9, 9, 8, 9, 9, 9,
        8, 8, 8, 8, 9, 9, 8, 9, 8, 8, 8, 8, 8, 9, 9, 9,
        4, 2, 5, 6, 6, 6, 6, 3, 5, 5, 5, 6, 2, 6, 2, 6,
        6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 2, 2, 5, 6, 5, 6,
        7, 6, 6, 6, 6, 6, 6, 6, 6, 4, 6, 6, 6, 6, 6, 6,
        6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 4, 6, 4, 6, 6,
        3, 6, 6, 6, 6, 6, 5, 6, 6, 2, 6, 5, 3, 6, 6, 6,
        6, 6, 6, 6, 4, 6, 6, 6, 6, 6, 6, 5, 2, 5, 7, 6,
        6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 4, 6, 3, 6, 6,
        6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 4, 6,
        6, 3, 6, 6, 6, 6, 6, 6, 6, 7, 6, 6, 6, 2, 6, 6,
        8, 9, 9, 6, 6, 6, 8, 8, 6, 8, 8, 8, 8, 8, 6, 6,
        9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9,
        9, 9, 9, 9, 9, 9, 9, 9, 9, 6, 9, 9, 9, 5, 9, 9,
        8, 7, 7, 8, 7, 8, 8, 8, 7, 8, 8, 7, 9, 9, 6, 7,
        7, 7, 7, 7, 9, 6, 7, 8, 7, 6, 6, 9, 7, 6, 7, 1
    };
    private static final char COLOR_CHAR = '\u00A7';
    private static final int CHAT_WINDOW_WIDTH = 320;
    private static final int CHAT_STRING_LENGTH = 117;
    private static final String allowedChars = net.minecraft.server.SharedConstants.allowedCharacters;
    
    private static final class Border
    {
    	private final int index;
    	private final boolean sourceend;
    	Border(final int index,final boolean sourceend)
    	{
    		this.index=index;
    		this.sourceend=sourceend;
    	}
    	
    	public int getIndex()
    	{
    		return this.index;
    	}
    	
    	public boolean getEnd()
    	{
    		return this.sourceend;
    	}
    }

	public static void rawsendout(CommandSender sender, String message) {
		String[] out = message.split("\n");
		for(String s : out)
		{
			sender.sendMessage(s);
		}
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
	
	private static String getLastColor(String input)
	{
		String color = "";
		Pattern p = Pattern.compile("(\u00A7[0-9a-fA-F])");
		Matcher m = p.matcher(input);
		while(m.find())
		{
			color=m.group();
		}
		return color;
	}
	
	private static Border getBorderIndex(String input)
	{
		try {
			StringReader sr = new StringReader(input);
			int ch;
			int linelength=0;
			int linewidth=0;
			while((ch=sr.read())!=-1)
			{
				if(ch=='\n')
					return new Border(linelength+1,false);
				if(ch==COLOR_CHAR)
				{
					if(linelength+2>CHAT_STRING_LENGTH)
						return new Border(linelength,false);
					else
					{
						sr.read();
						linelength+=2;
						continue;
					}
				}
				int index = allowedChars.indexOf(ch);
				if(index==-1)
					continue;
				else
					index+=32;
				linewidth+=characterWidths[index];
				if(linewidth>CHAT_WINDOW_WIDTH)
					return new Border(linelength,false);
				linelength++;
				if(linelength>CHAT_STRING_LENGTH-1)
					return new Border(linelength,false);
			}
			return new Border(linelength,true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new Border(-1,false);
	}
	
	private static String prepare(String message)
	{
		StringBuilder out = new StringBuilder();
		String color = "\u00A7f";
		try {
			while(message.length()>0)
			{
				Border border=getBorderIndex(message);
				int index=border.getIndex();
				if(index<0)
					return ChatColor.RED.toString()+"Internal Error";
				String sub = message.substring(0,index);
				if(!color.equalsIgnoreCase("\u00A7f"))
					out.append(color);
				String lastcolor;
				if(sub.indexOf('\n')>-1)
				{
					lastcolor = getLastColor(sub);
					out.append(sub);
					message=message.substring(index);
				}
				else if(!border.getEnd())
				{
					int lastspace=sub.lastIndexOf(' ');
					if(lastspace<0)
						lastspace=index;
					sub=sub.substring(0,lastspace);
					lastcolor = getLastColor(sub);
					out.append(sub+'\n');
					message=message.substring(lastspace+1);
				}
				else
				{
					lastcolor = getLastColor(sub);
					out.append(sub);
					message=message.substring(index);
					
				}
				if(!lastcolor.isEmpty())
					color=lastcolor;
			}
		} catch (StringIndexOutOfBoundsException e) {
			// TODO: handle exception
		}
		return out.toString();
	}
	
	public static void send(String message, CommandSender sender) {
		rawsendout(sender, prepare(message));
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
