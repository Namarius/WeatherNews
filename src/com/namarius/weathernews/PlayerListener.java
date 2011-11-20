package com.namarius.weathernews;

import org.bukkit.event.player.PlayerJoinEvent;

import com.namarius.weathernews.ye.YamlExecVM;

public class PlayerListener extends org.bukkit.event.player.PlayerListener {
	
	private WeatherNews plugin;
	private YamlExecVM vm;
	
	PlayerListener(WeatherNews plugin,YamlExecVM vm)
	{
		this.plugin=plugin;
		this.vm=vm;
	}
	
	@Override
	public void onPlayerJoin(PlayerJoinEvent event) {
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new NewsRunner(event.getPlayer().getWorld(), event.getPlayer(),this.vm), 1);
	}
}
