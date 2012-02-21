package com.namarius.weathernews.ye;

public abstract class AYamlOperator 
{
	protected final String param1,param2;
	
	AYamlOperator(String param1,String param2)
	{
		this.param1=param1!=null?param1:"";
		this.param2=param2!=null?param2:"";
	}
	
	abstract String execute();

}
