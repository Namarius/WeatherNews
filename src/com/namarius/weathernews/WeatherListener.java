package com.namarius.weathernews;

import java.io.File;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.event.weather.ThunderChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

import com.namarius.weathernews.ye.YamlExecVM;

public class WeatherListener extends org.bukkit.event.weather.WeatherListener {
	
	private WeatherNews plugin;
	private YamlExecVM vm;
	
	public WeatherListener(WeatherNews plugin)
	{
		this.plugin=plugin;
		HashMap<String,String> vars = new HashMap<String, String>();
		for(ChatColor c : ChatColor.values())
		{
			vars.put(c.name(), c.toString());
		}
		this.vm = new YamlExecVM(plugin, new File(plugin.getDataFolder(),"config.yml"), vars);
	}
	
	@Override
	public void onThunderChange(ThunderChangeEvent event) {
		World w = event.getWorld();
		if(w.getEnvironment()==Environment.NORMAL)
			plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new NewsRunner(w,vm), 1);
	}
	
	@Override
	public void onWeatherChange(WeatherChangeEvent event) {
		World w = event.getWorld();
		if(w.getEnvironment()==Environment.NORMAL)
			plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new NewsRunner(w,vm), 1);
	}
}
