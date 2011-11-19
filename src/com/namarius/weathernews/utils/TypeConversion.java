package com.namarius.weathernews.utils;

public class TypeConversion {
	public static Integer getInteger(String in)
	{
		Integer out = null;
		if(in!=null)
		{
			try {
				out = new Integer(in);
			} catch (Exception e) {
				return null;
			}
			
		}
		return out;
	}
	
	public static Double getDouble(String in)
	{
		Double out = null;
		if(in!=null)
		{
			try {
				out = new Double(in);
			} catch (Exception e) {
				return null;
			}
			
		}
		return out;
	}

}
