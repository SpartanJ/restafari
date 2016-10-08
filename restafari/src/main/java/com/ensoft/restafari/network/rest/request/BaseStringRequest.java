package com.ensoft.restafari.network.rest.request;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ensoft.restafari.network.helper.NetworkLogHelper;
import com.ensoft.restafari.network.helper.RequestParameters;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class BaseStringRequest extends StringRequest
{
	protected static final String TAG = BaseJsonRequest.class.getSimpleName();
	protected int method;
	protected Map<String, String> headers;
	protected Map<String, String> params;

	protected BaseStringRequest( int method, String url, JSONObject parameters, Map<String, String> headers, Listener<String> listener, ErrorListener errorListener )
	{
		super( method, url, listener, errorListener );

		this.method = method;
		this.headers = headers;
		this.params = RequestParameters.toMap( parameters );

		if ( NetworkLogHelper.LOG_DEBUG_INFO )
			Log.i( TAG, RequestLoggingHelper.getRequestText( this ) );
	}

	public int getMethod()
	{
		return method;
	}

	@Override
	public Map<String, String> getParams()
	{
		return params;
	}

	@Override
	public Map<String, String> getHeaders() throws AuthFailureError
	{
		return headers != null ? headers : new HashMap<String, String>();
	}

	@Override
	public String getBodyContentType()
	{
		return "text/plain";
	}

	@Override
	public void deliverError(VolleyError error)
	{
		if (NetworkLogHelper.LOG_DEBUG_INFO)
			Log.i(TAG, RequestLoggingHelper.getRequestErrorText(this, error));

		super.deliverError(error);
	}

	@Override
	protected void deliverResponse(String response)
	{
		if (NetworkLogHelper.LOG_DEBUG_INFO)
			Log.i(TAG, RequestLoggingHelper.getRequestResponseText(this, response));

		super.deliverResponse(response);
	}
}
