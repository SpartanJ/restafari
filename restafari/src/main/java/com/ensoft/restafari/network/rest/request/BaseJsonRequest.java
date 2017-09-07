package com.ensoft.restafari.network.rest.request;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ensoft.restafari.network.helper.NetworkLogHelper;

public abstract class BaseJsonRequest extends JsonObjectRequest
{
	protected static final String TAG = BaseJsonRequest.class.getSimpleName();
	
	protected int method;
	protected Map<String, String> headers;

	protected BaseJsonRequest( int method, String url, JSONObject parameters, Map<String, String> headers, Listener<JSONObject> listener, ErrorListener errorListener )
	{
		super( method, url, parameters, listener, errorListener );

		this.method = method;
		this.headers = headers;

		if ( NetworkLogHelper.LOG_DEBUG_INFO )
			Log.i(TAG, RequestLoggingHelper.getRequestText(this));
	}
	
	protected BaseJsonRequest( int method, String url, JSONArray parameters, Map<String, String> headers, Listener<JSONObject> listener, ErrorListener errorListener )
	{
		super( method, url, parameters.toString(), listener, errorListener );
		
		this.method = method;
		this.headers = headers;
		
		if ( NetworkLogHelper.LOG_DEBUG_INFO )
			Log.i(TAG, RequestLoggingHelper.getRequestText(this));
	}

	public int getMethod()
	{
		return method;
	}

	@Override
	public Map<String, String> getHeaders() throws AuthFailureError
	{
		return headers != null ? headers : new HashMap<String, String>();
	}

	@Override
	public String getBodyContentType()
	{
		return "application/json";
	}

	@Override
	public void deliverError(VolleyError error)
	{
		if (NetworkLogHelper.LOG_DEBUG_INFO)
			Log.i(TAG, RequestLoggingHelper.getRequestErrorText(this, error));

		super.deliverError(error);
	}

	@Override
	protected void deliverResponse(JSONObject response)
	{
		if (NetworkLogHelper.LOG_DEBUG_INFO)
			Log.i(TAG, RequestLoggingHelper.getRequestResponseText(this, response));

		super.deliverResponse(response);
	}
}