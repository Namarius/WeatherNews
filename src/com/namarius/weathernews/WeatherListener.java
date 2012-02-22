package com.namarius.weathernews;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.weather.ThunderChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

import com.namarius.weathernews.ye.YamlExecVM;

public class WeatherListener implements Listener {
	
	private final WeatherNews plugin;
	private final YamlExecVM vm;
	
	public WeatherListener(WeatherNews plugin)
	{
		this.plugin=plugin;
		this.vm = new YamlExecVM(plugin);
	}
	
	@EventHandler(priority=EventPriority.MONITOR)
	public void onThunderChange(ThunderChangeEvent event) {
		World w = event.getWorld();
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new NewsRunner(w,this.vm,plugin.getConfig()), 2);
	}
	
	@EventHandler(priority=EventPriority.MONITOR)
	public void onWeatherChange(WeatherChangeEvent event) {
		World w = event.getWorld();
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new NewsRunner(w,this.vm,plugin.getConfig()), 2);
	}
	
	@EventHandler(priority=EventPriority.MONITOR)
	public void onPlayerJoin(PlayerJoinEvent event) {
		if(this.plugin.getConfig().getBoolean("showonlogin"))
		{
			plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new NewsRunner(event.getPlayer().getWorld(), event.getPlayer(), true, this.vm,plugin.getConfig()), 2);
		}
	}
	
	public void runNews(World world)
	{
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new NewsRunner(world,this.vm,plugin.getConfig()), 2);
	}
	
	public void runNews(World world, Player player)
	{
		this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new NewsRunner(world, player, false, this.vm,plugin.getConfig()), 2);
	}
}
