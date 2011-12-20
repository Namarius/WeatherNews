package com.namarius.weathernews;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import com.namarius.weathernews.utils.ChatUtil;
import com.namarius.weathernews.utils.MinecraftTime;
import com.namarius.weathernews.ye.YamlExecVM;

public class NewsRunner implements Runnable {
	
	private final World world;
	private List<Player> players = null;
	private YamlExecVM vm;
	private FileConfiguration iconfig;
	private boolean joined;
	
	public NewsRunner( World world , YamlExecVM vm, FileConfiguration config)
	{
		this.world = world;
		this.vm = vm;
		this.iconfig = config;
		this.joined = false;
	}
	
	public NewsRunner(World world, Player player,boolean joined, YamlExecVM vm, FileConfiguration config)
	{
		this.vm = vm;
		this.world=world;
		this.iconfig = config;
		this.joined = joined;
		players = new ArrayList<Player>();
		players.add(player);		
	}
	
	private String getNow()
	{
		boolean storm = world.hasStorm();
		boolean thunder = world.isThundering();
		int weatherduration = world.getWeatherDuration();
		int thunderduration = world.getThunderDuration();
		int state = (storm?1:0) + (thunder?2:0) + (weatherduration<=thunderduration?4:0);
		switch(state)
		{
		default:
		case 0:		//from sun > to rain
		case 2:		//from sun > to thunder
		case 4:		//from sun > to sun
		case 6:		//from sun > to sun
			return "SUN";
		case 3:		//from thunder > to sun
		case 7:		//from thunder > to rain
			return "THUNDERSTORM";
		case 1:		//from rain > to sun
		case 5:		//from rain > to thunder
			return "RAIN";
		}
	}
	
	private String getNext()
	{
		boolean storm = world.hasStorm();
		boolean thunder = world.isThundering();
		int weatherduration = world.getWeatherDuration();
		int thunderduration = world.getThunderDuration();
		int state = (storm?1:0) + (thunder?2:0) + (weatherduration<=thunderduration?4:0);
		switch(state)
		{
		default:
		case 1:		//from rain > to sun
		case 3:		//from thunder > to sun
		case 4:		//from sun > to sun
		case 6:		//from sun > to sun
			return "SUN";
		case 0:		//from sun > to rain
		case 7:		//from thunder > to rain
			return "RAIN";
		case 2:		//from sun > to thunder
		case 5:		//from rain > to thunder
			return "THUNDERSTORM";
		}
	}
	
	@Override
	public void run() {
		this.vm.parseConfig();
		if(this.iconfig.getBoolean("worldwhitelist")^this.iconfig.getList("worlds").contains(world.getName()))
		{
			if(this.players!=null && !this.joined)
			{
				this.vm.execute();
				for(Player p:players)
					ChatUtil.send(this.vm.getParsedString("BLACKLISTED"), p);
			}
			return;
		}
		if(this.world.getEnvironment()!=Environment.NORMAL)
		{
			if(this.players!=null && !this.joined)
			{
				this.vm.execute();
				for(Player p:players)
					ChatUtil.send(this.vm.getParsedString("UNAVAILABLE"), p);
			}
			return;
		}
		int weatherduration = world.getWeatherDuration();
		int thunderduration = world.getThunderDuration();
		
		String now = getNow();
		String next = getNext();
		
		MinecraftTime time = new MinecraftTime(world.getFullTime()+6000);//I want midnight
		MinecraftTime wdur = new MinecraftTime(weatherduration);
		MinecraftTime tdur = new MinecraftTime(thunderduration);
		MinecraftTime mnext = weatherduration<thunderduration?wdur:tdur;
		try {
			wdur.setStapping(this.iconfig);
			tdur.setStapping(this.iconfig);
			mnext.setStapping(this.iconfig);
		} catch (Exception e) {
			this.vm.logWarning(e.getMessage());
		}
		this.vm.setVariable("TIMEDAY",time.getDay());
		this.vm.setVariable("TIMEHOUR", time.getHour());
		this.vm.setVariable("TIMEMINUTE", time.getMinute());
		this.vm.setVariable("NEXTDAY", mnext.getDay());
		this.vm.setVariable("NEXTHOUR", time.getHour());
		this.vm.setVariable("NEXTMINUTE", time.getMinute());
		this.vm.setVariable("TIMENICE", time.nicePrint(this.vm));
		this.vm.setVariable("NEXTNICE", mnext.nicePrint(this.vm));
		this.vm.setVariable("NOW", vm.getParsedString(now));
		this.vm.setVariable("NEXT", vm.getParsedString(next));
		this.vm.execute();		
		String data;
		if(now.equals(next))
			data = this.vm.getParsedString("UNCLEAR");
		else
			data = this.vm.getParsedString("CLEAR");
		players = players!=null?players:world.getPlayers();
		for(Player p : players)
			ChatUtil.send(data, p);
	}

}
