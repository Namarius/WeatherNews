package com.namarius.weathernews.ye;

import com.namarius.weathernews.utils.TypeConversion;


public class OpSetString extends AYamlOperator {

	public OpSetString(String param1, String param2) {
		super(param1, param2);
		// TODO Auto-generated constructor stub
	}

	@Override
	String execute() {
		Double a=TypeConversion.getDouble(param1);
		a=a!=null?a:param1.length();
		return a!=0?param2:null;
	}

}
