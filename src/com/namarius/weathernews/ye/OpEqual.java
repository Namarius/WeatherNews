package com.namarius.weathernews.ye;

import com.namarius.weathernews.utils.TypeConversion;

public class OpEqual extends AYamlOperator {

	OpEqual(String param1, String param2) {
		super(param1, param2);
		// TODO Auto-generated constructor stub
	}

	@Override
	String execute() {
		Double a=TypeConversion.getDouble(param1);
		Double b=TypeConversion.getDouble(param2);
		a=a!=null?a:param1.length();
		b=b!=null?b:param2.length();
		return a==b?"1":"";
	}

}
