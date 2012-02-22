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
	private int weatherduration;
	private int thunderduration;
	private int difference;
	private boolean storm;
	private boolean thunder;
	private int accuracy;
	
	public NewsRunner(World world, YamlExecVM vm, FileConfiguration config)
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
		this.players = new ArrayList<Player>();
		this.players.add(player);
	}
	
	private void setupWeather()
	{
		this.vm.parseConfig();
		this.weatherduration = this.world.getWeatherDuration();
		this.thunderduration = this.world.getThunderDuration();
		this.difference = Math.abs(this.weatherduration-this.thunderduration);
		this.storm = this.world.hasStorm();
		this.thunder = this.world.isThundering();
		this.accuracy = this.getAccuarcy();
	}
	
	private String getNow()
	{
		int state = (this.storm?1:0) + (this.thunder?2:0) + (this.weatherduration<=this.thunderduration?4:0);
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
	
	private int getAccuarcy()
	{
		int maximumtime = this.iconfig.getInt("maximumtime");
		double maximum = this.iconfig.getDouble("maximumpercentage");
		double minimum = this.iconfig.getDouble("minimumpercentage");
		double exponent = this.iconfig.getDouble("exponentialmod");
		int time = Math.min(this.thunderduration, this.weatherduration);
		//(maxpercentage-minpercentage) * exp(-2*pi*(t./maxt).^endrise) + minpercentage;
		double s = ((maximum-minimum) * Math.exp(-2*Math.PI*Math.pow(((double)time/(double)maximumtime), exponent)) + minimum)*100.0;
		return (int)s;
	}
	
	private String getNext()
	{	
		int modulation = this.iconfig.getInt("modulation");
		modulation = modulation > 0 ? modulation : 79;
		int modulated = ((this.difference%modulation)*100)/modulation;
		int state = (this.storm?1:0) + (this.thunder?2:0) + (this.weatherduration<=this.thunderduration?4:0) + ((this.difference&1)==0?8:0) + (this.iconfig.getBoolean("unprecise")&&(this.accuracy<modulated)?16:0);
		switch(state)
		{
		default:
		case 0:			//from sun > to rain
		case 8:			//from sun > to rain
		case 7:			//from thunder > to rain
		case 15:		//from thunder > to rain
		case 20:		//from sun > to sun 0	
		case 22:		//from sun > to sun 0
		case 19:		//from thunder > to sun 0
		case 17:		//from rain > to sun 0
		case 18:		//from sun > to thunder 0
		case 21:		//from rain > to thunder 0
			return "RAIN";
		case 1:			//from rain > to sun
		case 9:			//from rain > to sun
		case 3:			//from thunder > to sun
		case 11:		//from thunder > to sun
		case 4:			//from sun > to sun
		case 12:		//from sun > to sun
		case 6:			//from sun > to sun
		case 14:		//from sun > to sun
		case 37:		//from thunder > to sun 1
		case 26:		//from sun > to thunder 1
		case 39:		//from rain > to thunder 1
		case 41:		//from thunder > to rain 1
		case 23:		//from thunder > to rain 0
			return "SUN";
		case 2:			//from sun > to thunder
		case 10:		//from sun > to thunder
		case 5:			//from rain > to thunder
		case 13:		//from rain > to thunder
		case 40:		//from sun > to sun 1
		case 38:		//from sun > to sun 1
		case 25:		//from rain > to sun 1
		case 24:		//from sun > to rain 1
		case 16:		//from sun > to rain 0
			return "THUNDERSTORM";
		}
	}
	
	@Override
	public void run() {
		this.setupWeather();
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
				for(Player p:this.players)
					ChatUtil.send(this.vm.getParsedString("UNAVAILABLE"), p);
			}
			return;
		}
		
		String now = getNow();
		String next = getNext();
		
		MinecraftTime time = new MinecraftTime(world.getFullTime()+6000);//I want midnight
		MinecraftTime wdur = new MinecraftTime(this.weatherduration);
		MinecraftTime tdur = new MinecraftTime(this.thunderduration);
		MinecraftTime mnext = this.weatherduration<this.thunderduration?wdur:tdur;
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
		this.vm.setVariable("ACCURACY", new Integer(this.accuracy).toString());
		this.vm.execute();		
		String data;
		if(now.equals(next))
			data = this.vm.getParsedString("UNCLEAR");
		else
			data = this.vm.getParsedString("CLEAR");
		this.players = this.players!=null?this.players:world.getPlayers();
		for(Player p : this.players)
			ChatUtil.send(data, p);
	}

}