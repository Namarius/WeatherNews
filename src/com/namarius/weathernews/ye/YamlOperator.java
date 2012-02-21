package com.namarius.weathernews.ye;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public enum YamlOperator {
	Plus("+",OpPlus.class),
	Minus("-",OpMinus.class),
	Multiply("*",OpMultiply.class),
	Dividate("/",OpDividate.class),
	Equal("=",OpEqual.class),
	UnEqual("!=",OpUnEqual.class),
	Bigger(">",OpBigger.class),
	Lesser("<",OpLesser.class),
	BiggerEqual(">=",OpBiggerEqual.class),
	LesserEqual("<=",OpLesserEqual.class),
	SetString("?",OpSetString.class),
	NoOp("",OpNoOp.class);
	
	private final String localos;
	private Constructor<? extends AYamlOperator> constructor;
	
	static private HashMap<String,YamlOperator> ops;	
	
	static {
		for(YamlOperator o : YamlOperator.values())
			ops.put(o.localos,o);
	}
	
	YamlOperator(String i,Class<? extends AYamlOperator> cl)
	{
		this.localos=i;
		try {
			this.constructor=cl.getConstructor(String.class,String.class);
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String execute(final String param1,final String param2)
	{
		AYamlOperator toexec;
		try {
			toexec = this.constructor.newInstance(new Object[]{param1,param2});
			return toexec.execute();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	static public YamlOperator getByString(final String in)
	{
		YamlOperator out=ops.get(in);
		if(out==null)
			out=NoOp;
		return out;
	}

}
