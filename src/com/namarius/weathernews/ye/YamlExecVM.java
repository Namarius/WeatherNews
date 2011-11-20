package com.namarius.weathernews.ye;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class YamlExecVM {

	private HashMap<String,String> vars,defaultvars;
	private YamlConfiguration iconfig;
	private final Plugin plugin;
	
	public YamlExecVM(Plugin plugin,File fconfig,final HashMap<String,String> defaultvars)
	{
		this.plugin=plugin;
		this.iconfig = new YamlConfiguration();
		this.vars = new HashMap<String, String>(defaultvars);
		this.defaultvars = new HashMap<String, String>(defaultvars);
		try {
			iconfig.load(fconfig);
		} catch (FileNotFoundException e) {
			try {
				plugin.getServer().getLogger().warning("[WeatherNews] Config not found. Create default config.");
				iconfig.load(this.getClass().getResourceAsStream("config.yml"));
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			} catch (InvalidConfigurationException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			try {
				iconfig.save(fconfig);
				plugin.getServer().getLogger().info("[WeatherNews] Default config created.");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void parseConfig()
	{
		ConfigurationSection cs=this.iconfig.getConfigurationSection("variables");
		Map<String,Object> values = cs.getValues(false);
		for(Entry<String, Object> pairs : values.entrySet())
		{
			vars.put(pairs.getKey(), pairs.getValue().toString());
		}
	}
	
	public void setDefaultValue(String key,String value)
	{
		defaultvars.put(key, value);
	}
	
	public void setDefault(final Map<String,String> defaultvars)
	{
		this.defaultvars.clear();
		this.defaultvars = new HashMap<String, String>(defaultvars);
	}
	
	public void resetToDefault()
	{
		this.vars.clear();
		this.vars = new HashMap<String, String>(defaultvars);
	}
	
	public void setVariable(String key,String value)
	{
		vars.put(key, value);
	}
	
	public void execute()
	{
		Logger log = plugin.getServer().getLogger();
		String toexecute=iconfig.getString("execute");
		if(toexecute==null)
			toexecute="";
		if(toexecute=="")
			return;
		String[] lines=toexecute.split("\n");
		int line = 0;
		for(String current : lines)
		{
			line++;
			String[] params = current.split(";");
			if(params.length!=4)
			{
				log.warning("[WeatherNews]Cannot parse line: "+line+" skipping it." );
				continue;
			}
			YamlOperator operator = YamlOperator.getByString(params[0]);
			String ret = operator.execute(getParsedString(params[1]),getParsedString(params[2]));
			ret=ret==null?"":ret;
			if(params[3]!=null)
				vars.put(params[3], ret);
		}
	}
	
	private String getRecusiveParsedString(String key,int layersleft)
	{
		if(key==null)
			return "";
		if(layersleft<=0)
			return "";
		String var = vars.get(key);
		if(var==null)
			return "";
		Pattern p = Pattern.compile("\\$\\{(\\w*)\\}");
		Matcher m = p.matcher(var);
		LinkedList<String> foundvars = new LinkedList<String>();
		while(m.find())
		{
			if(m.groupCount()==1)
			{
				foundvars.add(m.group(1));
			}
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