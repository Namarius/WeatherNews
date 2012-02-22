package com.namarius.weathernews.utils;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;

import org.bukkit.configuration.MemoryConfiguration;

import com.namarius.weathernews.ye.YamlExecVM;

public class MinecraftTime {

	private final long time;
	private int faction;
	private long main;
	private long day;
	private int hour;
	private int minute;
	
	public MinecraftTime(long time)
	{
		this.time=time;
		update();
	}
	
	private void update()
	{
		faction = (int) (time%1000);
		main = time/1000;
		day =main/24;
		hour = (int) (main%24);
		minute = (faction*6)/100;
	}
	
	@Override
	public int hashCode() {
		return (int) time;
	}
	
	public String getDay()
	{
		return new Long(day).toString();
	}
	
	public String getHour()
	{
		return new Long(hour).toString();
	}
	
	public String getMinute()
	{
		return new Long(minute).toString();
	}
	
	public void setStapping(MemoryConfiguration config) throws Exception
	{
		if(!config.contains("stepping"))
			return;
		int sday=1,shour=1,sminute=1;
		int found=0;
		final int time=(int) (this.minute+this.hour*60+this.day*24*60);
		try
		{
			@SuppressWarnings("unchecked")
			List<Map<Integer,Map<String,Integer>>> list = (List<Map<Integer, Map<String, Integer>>>) config.get("stepping");
			for(Map<Integer,Map<String,Integer>> step : list )
			{
				Entry<Integer,Map<String,Integer>> entry = step.entrySet().iterator().next();
				if(found<entry.getKey() && time>entry.getKey())
				{
					sday=entry.getValue().get("day")==null?1:entry.getValue().get("day");
					shour=entry.getValue().get("hour")==null?1:entry.getValue().get("hour");
					sminute=entry.getValue().get("minute")==null?1:entry.getValue().get("minute");
					found=entry.getKey();
				}
			}
		}
		catch (ClassCastException e)
		{
			throw new Exception("Unable to parse stepping");
		}
		catch (NoSuchElementException e)
		{
			throw new Exception("Unable to parse stepping");
		}
		if(sday>0)
			this.day=(this.day/sday)*sday;
		else
			this.day=0;
		if(shour>0)
			this.hour=(this.hour/shour)*shour;
		else
			this.hour=0;
		if(sminute>0)
			this.minute=(this.minute/sminute)*sminute;
		else
			this.minute=0;
	}
	
	public void resetStapping()
	{
		update();
	}
	
	public String nicePrint(YamlExecVM vm)
	{
		String sminute,shour,sday,sand;
		sminute=new Long(this.minute).toString()+" "+vm.getParsedString(this.minute>1?"MINUTE_PLURAL":"MINUTE");
		shour=new Long(this.hour).toString()+" "+vm.getParsedString(this.hour>1?"HOUR_PLURAL":"HOUR");
		sday=new Long(this.day).toString()+" "+vm.getParsedString(this.day>1?"DAY_PLURAL":"DAY");
		sand=vm.getParsedString("AND");
		switch((this.minute>0?1:0)+(this.hour>0?2:0)+(this.day>0?4:0))
		{
		case 1:
			return sminute;
		case 2:
			return shour;
		case 3:
			return shour+" "+sand+" "+sminute;
		case 4:
			return sday;
		case 5:
			return sday+" "+sand+" "+sminute;
		case 6:
			return sday+" "+sand+" "+shour;
		case 7:
			return sday+", "+shour+" "+sand+" "+sminute;
		default:
			return "";
		}
	}
		
}