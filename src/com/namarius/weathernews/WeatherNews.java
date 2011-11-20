package com.namarius.weathernews;

import java.io.File;
import java.util.HashMap;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.namarius.weathernews.ye.YamlExecVM;

public class WeatherNews extends JavaPlugin {
	
	private PlayerListener pl;
	private WeatherListener wl;
	private YamlExecVM vm;

	@Override
	public void onDisable() {
		pl=null;
		wl=null;
		vm=null;
	}

	@Override
	public void onEnable() {
		Logger log=this.getServer().getLogger();
		log.info("[WeatherNews] Version: "+this.getDescription().getVersion());
		if(this.vm==null)
		{
			HashMap<String,String> vars = new HashMap<String, String>();
			for(ChatColor c : ChatColor.values())
			{
				vars.put(c.name(), c.toString());
			}
			this.vm=new YamlExecVM(this, new File(this.getDataFolder(),"config.yml"), vars);
		}
		if(wl==null)
			this.wl = new WeatherListener(this,vm);
		if(pl==null)
			this.pl = new PlayerListener(this,vm);
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvent(Type.PLAYER_JOIN, pl, Priority.Monitor, this);
		pm.registerEvent(Type.WEATHER_CHANGE, wl, Priority.Monitor, this);
		pm.registerEvent(Type.THUNDER_CHANGE, wl, Priority.Monitor, this);
		log.info("[WeatherNews] Fully loaded and ready to run.");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		Player player = (Player) sender;
		World w = player.getWorld();
		this.getServer().getScheduler().scheduleSyncDelayedTask(this, new NewsRunner(w, player,this.vm), 1);
		return true;
	}

}
