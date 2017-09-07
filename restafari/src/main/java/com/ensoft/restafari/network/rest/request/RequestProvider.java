package com.ensoft.restafari.network.rest.request;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Constructor;
import java.util.Map;

public class RequestProvider
{
	protected static final String TAG = RequestProvider.class.getSimpleName();

	public static Request<?> createRequest( RequestConfiguration requestConfig, JSONObject parameters, Map<String, String> headers, Response.Listener<?> listener, Response.ErrorListener errorListener )
	{
		String msg = null;
		Request<?> request = null;

		try
		{
			Constructor<?> constructor = requestConfig.getRequestClass().getConstructor( JSONObject.class, Map.class, Response.Listener.class, Response.ErrorListener.class );

			request = (Request<?>) constructor.newInstance( parameters, headers, listener, errorListener );
		}
		catch (IllegalArgumentException e)
		{
			msg = e.getMessage();
		}
		catch (Exception e)
		{
			msg = "ReflectiveOperationException: " + e.toString();
		}
		finally
		{
			if (msg != null)
			{
				Log.e( TAG, "error instantiating request of type " + requestConfig.getRequestClass() + "\n" + msg );
				request = null;
			}
		}

		return request;
	}
	
	public static Request<?> createRequest( RequestConfiguration requestConfig, JSONArray parameters, Map<String, String> headers, Response.Listener<?> listener, Response.ErrorListener errorListener )
	{
		String msg = null;
		Request<?> request = null;
		
		try
		{
			Constructor<?> constructor = requestConfig.getRequestClass().getConstructor( JSONArray.class, Map.class, Response.Listener.class, Response.ErrorListener.class );
			
			request = (Request<?>) constructor.newInstance( parameters, headers, listener, errorListener );
		}
		catch (IllegalArgumentException e)
		{
			msg = e.getMessage();
		}
		catch (Exception e)
		{
			msg = "ReflectiveOperationException: " + e.toString();
		}
		finally
		{
			if (msg != null)
			{
				Log.e( TAG, "error instantiating request of type " + requestConfig.getRequestClass() + "\n" + msg );
				request = null;
			}
		}
		
		return request;
	}
}
