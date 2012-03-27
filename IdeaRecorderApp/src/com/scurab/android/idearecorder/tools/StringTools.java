package com.scurab.android.idearecorder.tools;

public class StringTools
{
	private StringTools(){}
	
	
	public static boolean isNullOrEmpty(String value)
	{
		return value == null || value.length() == 0;
	}


	public static boolean isNullOrTrimmedEmpty(String value)
	{
		return value == null || value.trim().length() == 0;
	}


	public static String nullIfTrimmedEmpty(String value)
	{
		if(isNullOrTrimmedEmpty(value))
			return null;
		else
			return value;
	}
}
