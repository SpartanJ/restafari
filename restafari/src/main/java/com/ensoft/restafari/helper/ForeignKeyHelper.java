package com.ensoft.restafari.helper;

import com.ensoft.restafari.database.annotations.DbForeignKey;

public class ForeignKeyHelper
{
	public static String fromAction( int action )
	{
		switch ( action )
		{
			case DbForeignKey.NO_ACTION: return "NO ACTION";
			case DbForeignKey.RESTRICT: return "RESTRICT";
			case DbForeignKey.SET_NULL: return "SET NULL";
			case DbForeignKey.SET_DEFAULT: return "SET DEFAULT";
			case DbForeignKey.CASCADE: return "CASCADE";
		}
		
		return null;
	}
}
