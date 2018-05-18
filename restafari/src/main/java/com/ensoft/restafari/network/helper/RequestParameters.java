package com.ensoft.restafari.network.helper;

import android.support.v4.util.Pair;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.HashMap;
import java.util.Map;

public class RequestParameters extends JSONObject
{
	public static RequestParameters fromObject( Object object )
	{
		try
		{
			return new RequestParameters( new Gson().toJson( object ) );
		}
		catch ( Exception e )
		{
			return null;
		}
	}
	
	public static RequestParameters fromMap(Map<String, String> parameters)
	{
		RequestParameters b = new RequestParameters();

		try
		{
			if ( null != parameters )
			{
				for ( String parameter : parameters.keySet() )
					b.put( parameter, parameters.get( parameter ) );
			}
		}
		catch ( JSONException e )
		{}

		return b;
	}

	public static Map<String, String> toMap( JSONObject parameters )
	{
		Map<String, String> params = new HashMap<>();

		if ( parameters != null )
		{
			JSONArray names = parameters.names();

			try
			{
				if ( null != names )
				{
					for ( int i = 0; i < names.length(); i++ )
					{
						params.put( names.getString( i ), parameters.get( names.getString( i ) ).toString() );
					}
				}
			}
			catch ( JSONException e )
			{}
		}

		return params;
	}

	public static String getUrlQuery(JSONObject parameters)
	{
		StringBuilder urlParameters = new StringBuilder();

		if ( null != parameters && parameters.length() > 0 )
		{
			Map<String, String> parametersMap = toMap( parameters );

			if (parametersMap.size() > 0) urlParameters.append("?");

			for (String parameterName : parametersMap.keySet())
				urlParameters.append(parameterName).append("=").append(parametersMap.get(parameterName).replace(" ", "%20")).append("&");

			urlParameters.deleteCharAt(urlParameters.length() - 1);

		}

		return urlParameters.toString();
	}

	private int attachmentId = 0;
	
	public RequestParameters()
	{
		super();
	}
	
	public RequestParameters(Map copyFrom)
	{
		super(copyFrom);
	}
	
	public RequestParameters(String json) throws JSONException
	{
		super(json);
	}
	
	public RequestParameters(JSONTokener readFrom) throws JSONException
	{
		super(readFrom);
	}
	
	public RequestParameters(JSONObject copyFrom, String[] names) throws JSONException
	{
		super(copyFrom, names);
	}
	
	public JSONObject putBoolean(String name, boolean value)
	{
		try
		{
			super.put( name, value );
		}
		catch ( JSONException e ) {}
		return this;
	}

	public JSONObject putDouble(String name, double value)
	{
		try
		{
			super.put( name, value );
		}
		catch ( JSONException e ) {}
		return this;
	}

	public JSONObject putInt(String name, int value)
	{
		try
		{
			super.put( name, value );
		}
		catch ( JSONException e ) {}
		return this;
	}

	public JSONObject putLong(String name, long value)
	{
		try
		{
			super.put( name, value );
		}
		catch ( JSONException e ) {}
		return this;
	}

	public JSONObject putString(String name, String value)
	{
		try
		{
			super.put( name, value );
		}
		catch ( JSONException e ) {}
		return this;
	}

	public JSONObject putObject(String name, Object value)
	{
		try
		{
			if ( value instanceof JSONObject || value instanceof JSONArray )
			{
				super.put( name, value );
			}
			else
			{
				super.put( name, fromObject( value ) );
			}
		}
		catch ( JSONException e ) {}
		return this;
	}

	public JSONObject putOptObject(String name, Object value)
	{
		if (name == null || value == null)
			return this;
		return putObject(name, value);
	}

	public JSONObject putObjectArray(String name, Object[] objects)
	{
		JSONArray jsonArray = new JSONArray();

		for ( Object object : objects )
		{
			try
			{
				jsonArray.put( new JSONObject( new Gson().toJson( object ) ) );
			}
			catch ( JSONException e )
			{
			}
		}
		
		putOptObject( name, jsonArray );

		return this;
	}

	public JSONObject addAttachment(String fieldName, String attachmentPath)
	{
		return putOptObject( "restafari-attachment-id-" + String.valueOf( ++attachmentId ), new Pair<>( fieldName, attachmentPath ) );
	}
}
