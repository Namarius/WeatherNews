package com.namarius.weathernews.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.config.Configuration;
import org.bukkit.util.config.ConfigurationException;
import org.bukkit.util.config.ConfigurationNode;

import com.namarius.weathernews.utils.ChatUtil;
import com.namarius.weathernews.utils.ChatUtil.Symbol;

public class WeatherConfig {

	private HashMap<String,Object> vars;
	private HashMap<String,Object> defvars;
	
	public WeatherConfig(Plugin plugin) throws Exception
	{
		File basedir = plugin.getDataFolder();
		if(!basedir.exists())
			if(!basedir.mkdir())
				throw new Exception("Couldn't create plugins basedata dir.");
		File configfile = new File(basedir, "config.yml");
		if(!configfile.exists())
		{
			InputStream is = WeatherConfig.class.getResourceAsStream("config.yml");
			FileOutputStream fos = new FileOutputStream(configfile);
			byte[] b = new byte[4096];
			int read=0;
			while((read=is.read(b))>0)
			{
				fos.write(b, 0, read);
			}
			is.close();
			fos.close();
		}
		Configuration config = new Configuration(configfile);
		config.load();
		readupVariables(config.getNode(""));
	}
	
	private void setVariable(String key,String value)
	{
		if(key!=null)
			vars.put(key, value);
	}
	
	private void readupVariables(ConfigurationNode node)
	{
		try
		{
			for(Map.Entry<String, Object> entry : node.getNode("variables").getAll().entrySet())
			{
				if(entry.getValue() != null)
				{
					vars.put(entry.getKey(), entry.getValue().toString());
				}
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
	
	@SuppressWarnings("unchecked")
	public void setDefault()
	{
		defvars=(HashMap<String, Object>) vars.clone();
	}
	
	@SuppressWarnings("unchecked")
	public void resetToDefault()
	{
		vars=(HashMap<String, Object>) defvars.clone();
	}
	
	public String getParsedString(String key)
	{
		
		
		
		return "";
	}
}
