package com.namarius.weathernews.config;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class WeatherConfig {

	private HashMap<String,String> vars;
	private YamlConfiguration iconfig;
	
	public WeatherConfig(Plugin plugin)
	{
		try
		{
		File basedir = plugin.getDataFolder();
		if(!basedir.exists())
			if(!basedir.mkdir())
				throw new Exception("Couldn't create plugins basedata dir.");
		File configfile = new File(basedir, "config.yml");
		if(!configfile.exists())
		{
			iconfig = YamlConfiguration.loadConfiguration(configfile);
			iconfig.setDefaults(YamlConfiguration.loadConfiguration(WeatherConfig.class.getResourceAsStream("config.yml")));
			iconfig.save(configfile);
		}
		iconfig = iconfig==null ? YamlConfiguration.loadConfiguration(configfile) : iconfig;
		readupVariables(iconfig);
		}
		catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public void setVariable(String key,String value)
	{
		if(key!=null)
			vars.put(key, value);
	}
	
	private void readupVariables(Configuration node)
	{
		try
		{
			for(Map.Entry<String, Object> entry : node.getConfigurationSection("variables").getValues(false).entrySet())
			{
				if(entry.getValue() != null)
					vars.put(entry.getKey(), entry.getValue().toString());
			}
			setVariable("rain", node.getString("rain"));
			setVariable("sun", node.getString("sun"));
			setVariable("thunderstrom", node.getString("thunderstorm"));
			setVariable("nextclear", node.getString("nextclear"));
			setVariable("nextunclear", node.getString("nextunclear"));
		}
		catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	private String getRecusiveParsedString(String key,int layersleft)
	{
		if(key==null)
			return "";
		if(layersleft<=0)
			return "";
		String var = vars.get(key);
		Pattern p = Pattern.compile("\\$\\{(\\w*)\\}");
		Matcher m = p.matcher(var);
		LinkedList<String> foundvars = new LinkedList<String>();
		while(m.find())
		{		
			if(m.groupCount()==2)
				foundvars.add(m.group(1));
		}
		for(String s : foundvars)
		{
			var=var.replace("${"+s+"}", getRecusiveParsedString(s, layersleft-1));
		}
		return var;
	}
	
	public String getParsedString(String key)
	{		
		return getRecusiveParsedString(key, 16);
	}
}
