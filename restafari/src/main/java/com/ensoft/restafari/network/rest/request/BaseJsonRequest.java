package com.ensoft.restafari.network.rest.request;

import java.util.HashMap;
import java.util.Map;

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

	public int getMethod()
	{
		return method;
	}

	protected BaseJsonRequest( int method, String url, JSONObject parameters, Map<String, String> headers, Listener<JSONObject> listener, ErrorListener errorListener )
	{
		super( method, url, parameters, listener, errorListener );

		this.method = method;
		this.headers = headers;

		if ( NetworkLogHelper.LOG_DEBUG_INFO )
			Log.i(TAG, RequestLoggingHelper.getRequestText(this));
	}

	@Override
	public Map<String, String> getHeaders() throws AuthFailureError
	{
		Map<String, String> requestHeaders = new HashMap<>();
		requestHeaders.put( "Content-Type", "application/json" );

		if ( headers != null)
		{
			for (Map.Entry<String, String> entry : headers.entrySet())
				requestHeaders.put(entry.getKey(), entry.getValue());
		}

		return requestHeaders;
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