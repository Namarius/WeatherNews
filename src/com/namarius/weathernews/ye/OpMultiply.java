package com.namarius.weathernews.ye;

import com.namarius.weathernews.utils.TypeConversion;

public class OpMultiply extends AYamlOperator {

	OpMultiply(String param1, String param2) {
		super(param1, param2);
		// TODO Auto-generated constructor stub
	}

	@Override
	String execute() {
		Double a=TypeConversion.getDouble(param1);
		Double b=TypeConversion.getDouble(param2);
		a=a!=null?a:0;
		b=b!=null?b:0;
		a*=b;
		return a.toString();
	}

}
