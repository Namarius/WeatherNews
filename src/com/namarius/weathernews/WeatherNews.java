package com.namarius.weathernews;

import java.util.logging.Logger;

import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.namarius.weathernews.config.WeatherConfig;

public class WeatherNews extends JavaPlugin {
	
	private final PlayerListener pl = new PlayerListener(this);
	private final WeatherListener wl = new WeatherListener(this);
	private final WeatherConfig wc = new WeatherConfig(this);

	@Override
	public void onDisable() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onEnable() {
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvent(Type.PLAYER_JOIN, pl, Priority.Monitor, this);
		pm.registerEvent(Type.WEATHER_CHANGE, wl, Priority.Monitor, this);
		pm.registerEvent(Type.THUNDER_CHANGE, wl, Priority.Monitor, this);
		Logger log=this.getServer().getLogger();
		log.info("WeatherNews Version:"+this.getDescription().getVersion());
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		Player player = (Player) sender;
		World w = player.getWorld();
		this.getServer().getScheduler().scheduleSyncDelayedTask(this, new NewsRunner(w, player), 1);
		return true;
	}
	
	public WeatherConfig getWeatherConfig()
	{
		return wc;
	}

}
