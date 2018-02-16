package com.ensoft.restafari.network.rest.request;

import org.json.JSONArray;
import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.JsonRequest;

import java.util.Map;

/** Helper class to log requests information */
class RequestLoggingHelper
{
	static String methodToString( int method )
	{
		switch ( method )
		{
			case Method.GET: return "GET";
			case Method.POST: return "POST";
			case Method.PUT: return "PUT";
			case Method.DELETE: return "DELETE";
			case Method.HEAD: return "HEAD";
			case Method.OPTIONS: return "OPTIONS";
			case Method.TRACE: return "TRACE";
			case Method.PATCH: return "PATCH";
		}
		
		return "UNKNOWN METHOD";
	}
	
	/**
	 * Gets basic request information.
	 * 
	 * @param request
	 *            Request instance to retrieve information for logging
	 * 
	 * @return Message describing request, including Method type, URL and JSON body
	 * */
	static String getRequestText(Request<?> request)
	{
		StringBuilder msg = new StringBuilder();

		msg.append("New ").append( methodToString( request.getMethod() ) ).append(" request").append("\n");
		msg.append("URL: ").append(request.getUrl()).append("\n");

		try
		{
			if ( null != request.getHeaders() && request.getHeaders().size() > 0 )
			{
				msg.append( "HEADERS:\n" );
				
				for ( Map.Entry<String,String> header : request.getHeaders().entrySet() )
				{
					msg.append( "\t" ).append( header.getKey() ).append( ": " ).append( header.getValue() ).append( "\n" );
				}
			}
			
			if ( null != request.getBody() )
			{
				msg.append( "BODY: " ).append( new String( request.getBody() ) ).append( "\n" );
			}
			else
			{
				msg.append( "BODY: null" );
			}
		}
		catch ( AuthFailureError authFailureError )
		{
			msg.append( "BODY: AUTHORIZATION ERROR\n" );
			msg.append( authFailureError.toString() );
		}

		return msg.toString();
	}

	/**
	 * Gets Request error information.
	 * 
	 * @param request
	 *            Request instance to retrieve information for logging
	 * 
	 * @param volleyError
	 *            Error instance to retrieve information for logging
	 * 
	 * @return Message describing error, including Method type, URL, HTTP status and JSON response
	 * */
	static String getRequestErrorText(Request<?> request, VolleyError volleyError)
	{
		StringBuilder msg = new StringBuilder();

		msg.append( methodToString( request.getMethod() ) ).append(" request failed ").append("\n");
		msg.append("URL: ").append(request.getUrl()).append("\n");

		String statusCode = "unknown";
		String responseData = "unknown";
		if (volleyError.networkResponse != null)
		{
			statusCode = String.valueOf(volleyError.networkResponse.statusCode);
			responseData = new String(volleyError.networkResponse.data);
		}

		msg.append("HTTP status:").append(statusCode).append("\n");
		msg.append("Response:").append(responseData).append("\n");

		return msg.toString();
	}

	/**
	 * Gets Request response information.
	 *
	 * @param request
	 *            Request instance to retrieve information for logging
	 *
	 * @param response
	 *            Response instance to retrieve information for logging
	 *
	 * @return Message describing response, including Method type, URL and JSON response
	 * */
	static String getRequestResponseText(Request<?> request, String response)
	{
		StringBuilder msg = new StringBuilder();

		msg.append( methodToString( request.getMethod() ) ).append(" request successful ").append("\n");
		msg.append("URL: ").append(request.getUrl()).append("\n");
		msg.append("Response: ").append(response).append("\n");

		return msg.toString();
	}

	/**
	 * Gets Request response information.
	 * 
	 * @param request
	 *            Request instance to retrieve information for logging
	 * 
	 * @param response
	 *            Response instance to retrieve information for logging
	 * 
	 * @return Message describing response, including Method type, URL and JSON response
	 * */
	static String getRequestResponseText(JsonRequest<?> request, JSONObject response)
	{
		StringBuilder msg = new StringBuilder();

		msg.append( methodToString( request.getMethod() ) ).append(" request successful ").append("\n");
		msg.append("URL: ").append(request.getUrl()).append("\n");
		msg.append("Response: ").append(response.toString()).append("\n");

		return msg.toString();
	}

	/**
	 * Gets Request response information.
	 *
	 * @param request
	 *            Request instance to retrieve information for logging
	 *
	 * @param response
	 *            Response instance to retrieve information for logging
	 *
	 * @return Message describing response, including Method type, URL and JSON response
	 * */
	static String getRequestResponseText(JsonRequest<?> request, JSONArray response)
	{
		StringBuilder msg = new StringBuilder();

		msg.append( methodToString( request.getMethod() ) ).append(" request successful ").append("\n");
		msg.append("URL: ").append(request.getUrl()).append("\n");
		msg.append("Response: ").append(response.toString()).append("\n");

		return msg.toString();
	}
}
