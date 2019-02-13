package com.ensoft.restafari.helper;

import android.os.Handler;
import android.os.Looper;

public class ThreadRunner
{
	protected static ThreadMode defaultThreadMode = ThreadMode.MAIN;
	
	public static ThreadMode getDefaultThreadMode()
	{
		return defaultThreadMode;
	}
	
	public static void setDefaultThreadMode( ThreadMode defaultThreadMode )
	{
		ThreadRunner.defaultThreadMode = defaultThreadMode;
	}
	
	public static void run( ThreadMode threadMode, Runnable runnable )
	{
		if ( threadMode == ThreadMode.ASYNC )
		{
			runnable.run();
		}
		else if ( threadMode == ThreadMode.MAIN )
		{
			new Handler( Looper.getMainLooper() ).post( runnable );
		}
	}
}
