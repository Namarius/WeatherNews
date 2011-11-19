package com.namarius.weathernews.utils;

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
	
	public String nicePrint(YamlExecVM vm)
	{
		String sminute,shour,sday,sand;
		sminute=new Long(minute).toString()+" "+vm.getParsedString(minute>1?"minute_plural":"minute");;
		shour=new Long(hour).toString()+" "+vm.getParsedString(hour>1?"hour_plural":"hour");
		sday=new Long(day).toString()+" "+vm.getParsedString(day>1?"day_plural":"day");
		sand=vm.getParsedString("and");
		switch(minute>0?1:0+hour>0?2:0+day>0?4:0)
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
