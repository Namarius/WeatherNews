package com.namarius.weathernews;

import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class WeatherNews extends JavaPlugin {
	
	private WeatherListener wl;

	@Override
	public void onDisable() {
		wl=null;
	}

	@Override
	public void onEnable() {
		this.reloadConfig();
		this.getConfig().options().copyDefaults(true);
		this.saveConfig();
		if(wl==null)
			this.wl = new WeatherListener(this);
		this.getServer().getPluginManager().registerEvents(this.wl, this);
		this.getServer().getLogger().info("Fully loaded and ready to run.");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		Player player = (Player) sender;
		World w = player.getWorld();
		this.wl.runNews(w, player);
		return true;
	}

}
