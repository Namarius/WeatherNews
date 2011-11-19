package com.namarius.weathernews.ye;

import com.namarius.weathernews.utils.TypeConversion;

public class OpDividate extends AYamlOperator {

	OpDividate(String param1, String param2) {
		super(param1, param2);
		// TODO Auto-generated constructor stub
	}

	@Override
	String execute() {
		Double a=TypeConversion.getDouble(param1);
		Double b=TypeConversion.getDouble(param2);
		a=a!=null?a:0;
		b=b!=null?b:0;
		if(b==0)
		{
			a=a<0?Double.NEGATIVE_INFINITY:Double.POSITIVE_INFINITY;
			return a.toString();
		}
		a/=b;
		return a.toString();
	}

}
