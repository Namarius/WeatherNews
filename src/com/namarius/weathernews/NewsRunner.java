package com.namarius.weathernews;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;

import com.namarius.weathernews.utils.ChatUtil;
import com.namarius.weathernews.utils.MinecraftTime;
import com.namarius.weathernews.utils.ChatUtil.Symbol;

public class NewsRunner implements Runnable {
	
	private final World world;
	private List<Player> players = null;
	
	public NewsRunner( World world )
	{
		this.world = world;
	}
	
	public NewsRunner(World world, Player player)
	{
		this.world=world;
		players = new ArrayList<Player>();
		players.add(player);		
	}

	@Override
	public void run() {
		if(world.getEnvironment()==Environment.NETHER)
			return;
		players = players!=null?players:world.getPlayers();
		ArrayList<Object> data = new ArrayList<Object>();
		boolean storm = world.hasStorm();
		boolean thunder = world.isThundering();
		int weaterduration = world.getWeatherDuration();
		int thunderduration = world.getThunderDuration();
		int state = (storm?1:0) + (thunder?2:0) + (weaterduration<=thunderduration?4:0);
		String now = null;
		String next = null;
		switch(state)
		{
		case 0:		//from sun > to rain
		case 2:		//from sun > to thunder
		case 4:		//from sun > to sun
		case 6:		//from sun > to sun
			now = "sun";
			break;
		case 3:		//from thunder > to sun
		case 7:		//from thunder > to rain
			now = "thunder";
			break;
		case 1:		//from rain > to sun
		case 5:		//from rain > to thunder
			now = "rain";
			break;
		}
		switch(state)
		{
		case 1:		//from rain > to sun
		case 3:		//from thunder > to sun
		case 4:		//from sun > to sun
		case 6:		//from sun > to sun
			next = "sun";
			break;
		case 0:		//from sun > to rain
		case 7:		//from thunder > to rain
			next = "rain";
			break;
		case 2:		//from sun > to thunder
		case 5:		//from rain > to thunder
			next = "thunder";
			break;
		}
		MinecraftTime time = new MinecraftTime(world.getFullTime()+6000);//I want midnight
		MinecraftTime wdur = new MinecraftTime(weaterduration);
		MinecraftTime tdur = new MinecraftTime(thunderduration);
		
		
		for(Player p : players)
		{
			ChatUtil.send(data.toArray(), p, ChatColor.GOLD);
		}
	}

}
