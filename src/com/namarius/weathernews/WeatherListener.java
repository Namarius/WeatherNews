package com.namarius.weathernews;

import org.bukkit.World;
import org.bukkit.event.weather.ThunderChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

public class WeatherListener extends org.bukkit.event.weather.WeatherListener {
	
	private WeatherNews plugin;
	
	public WeatherListener(WeatherNews plugin)
	{
		this.plugin=plugin;
	}
	
	@Override
	public void onThunderChange(ThunderChangeEvent event) {
		World w = event.getWorld();
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new NewsRunner(w,w.hasStorm(),w.isThundering()), 1);
	}
	
	@Override
	public void onWeatherChange(WeatherChangeEvent event) {
		World w = event.getWorld();
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new NewsRunner(w,w.hasStorm(),w.isThundering()), 1);
	}
}
