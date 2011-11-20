package com.namarius.weathernews;

import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.event.weather.ThunderChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

import com.namarius.weathernews.ye.YamlExecVM;

public class WeatherListener extends org.bukkit.event.weather.WeatherListener {
	
	private WeatherNews plugin;
	private YamlExecVM vm;
	
	public WeatherListener(WeatherNews plugin,YamlExecVM vm)
	{
		this.plugin=plugin;
		this.vm = vm;
	}
	
	@Override
	public void onThunderChange(ThunderChangeEvent event) {
		World w = event.getWorld();
		if(w.getEnvironment()==Environment.NORMAL)
			plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new NewsRunner(w,this.vm), 1);
	}
	
	@Override
	public void onWeatherChange(WeatherChangeEvent event) {
		World w = event.getWorld();
		if(w.getEnvironment()==Environment.NORMAL)
			plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new NewsRunner(w,this.vm), 1);
	}
}
