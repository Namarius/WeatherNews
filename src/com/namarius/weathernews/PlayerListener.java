package com.namarius.weathernews;

import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerListener extends org.bukkit.event.player.PlayerListener {
	
	private WeatherNews plugin;
	
	PlayerListener(WeatherNews plugin)
	{
		this.plugin=plugin;
	}
	
	@Override
	public void onPlayerJoin(PlayerJoinEvent event) {
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new NewsRunner(event.getPlayer().getWorld(), event.getPlayer()), 1);
	}
}
