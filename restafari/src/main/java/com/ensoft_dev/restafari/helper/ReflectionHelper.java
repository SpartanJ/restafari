package com.ensoft_dev.restafari.helper;

import android.util.Log;
import java.lang.reflect.Constructor;

public class ReflectionHelper
{
	private static final String TAG = ReflectionHelper.class.getSimpleName();

	@SuppressWarnings("unchecked")
	public static <T> T createInstance(Class<?> clazz)
	{
		String msg = null;
		Object newInstance = null;
		try
		{
			for ( Constructor<?> constructor : clazz.getConstructors() )
			{
				if ( constructor.getParameterTypes().length == 0 )
				{
					newInstance = constructor.newInstance();

					break;
				}
			}

			if ( null == newInstance )
			{
				msg = "Class must have a default constructor without parameters";
			}
		}
		catch (IllegalArgumentException e)
		{
			msg = e.getMessage();
		}
		catch (Exception e)
		{
			msg = "ReflectiveOperationException";
		}
		finally
		{
			if (msg != null)
			{
				Log.e( TAG, "error instantiating type " + clazz.getSimpleName() + "\n" + msg );
				newInstance = null;
			}
		}

		return (T) newInstance;
	}
}
