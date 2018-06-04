package com.ensoft.restafari.helper;

public class StringUtils
{
	public static String join( String[] strings, String glue )
	{
		StringBuilder sb = new StringBuilder();
		
		for ( int i = 0; i < strings.length; i++ )
		{
			sb.append( strings[i] );
			
			if (i != strings.length - 1)
			{
				sb.append(glue);
			}
		}
		
		return sb.toString();
	}
}
