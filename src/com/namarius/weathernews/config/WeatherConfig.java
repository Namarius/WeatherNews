package com.namarius.weathernews.config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.plugin.Plugin;
import org.bukkit.util.config.Configuration;
import org.bukkit.util.config.ConfigurationNode;

public class WeatherConfig {

	private HashMap<String,String> vars;
	private HashMap<String,String> defvars;
	
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
	
	public void setVariable(String key,String value)
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
		defvars=(HashMap<String, String>) vars.clone();
	}
	
	@SuppressWarnings("unchecked")
	public void resetToDefault()
	{
		vars=(HashMap<String, String>) defvars.clone();
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
		return "";
	}
	
	public String getParsedString(String key)
	{		
		return getRecusiveParsedString(key, 16);
	}
}
