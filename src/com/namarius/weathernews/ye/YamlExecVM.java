package com.namarius.weathernews.ye;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;

public class YamlExecVM {

	private HashMap<String,String> vars,defaultvars;
	private final Plugin plugin;
	
	public YamlExecVM(Plugin plugin)
	{
		this.plugin=plugin;
		this.vars = new HashMap<String, String>();
		for(ChatColor c : ChatColor.values())
			this.vars.put(c.name(), c.toString());
		this.defaultvars = new HashMap<String, String>(this.vars);

	}
	
	public void parseConfig()
	{
		ConfigurationSection cs=this.plugin.getConfig().getConfigurationSection("variables");
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
		String toexecute=this.plugin.getConfig().getString("execute");
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
				log.warning("Cannot parse line: "+line+" skipping it." );
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