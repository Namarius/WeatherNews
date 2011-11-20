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
		sminute=new Long(this.minute).toString()+" "+vm.getParsedString(this.minute>1?"MINUTE_PLURAL":"MINUTE");
		shour=new Long(this.hour).toString()+" "+vm.getParsedString(this.hour>1?"HOUR_PLURAL":"HOUR");
		sday=new Long(this.day).toString()+" "+vm.getParsedString(this.day>1?"DAY_PLURAL":"DAY");
		sand=vm.getParsedString("and");
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
