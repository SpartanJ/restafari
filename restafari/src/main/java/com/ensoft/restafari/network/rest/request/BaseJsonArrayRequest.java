package com.ensoft.restafari.network.rest.request;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.ensoft.restafari.network.helper.NetworkLogHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseJsonArrayRequest extends JsonArrayRequest
{
    protected static final String TAG = BaseJsonArrayRequest.class.getSimpleName();

	protected int method;
	protected Map<String, String> headers;

    protected BaseJsonArrayRequest(int method, String url, JSONObject parameters, Map<String, String> headers, Listener<JSONArray> listener, ErrorListener errorListener)
    {
        super( method, url, parameters, listener, errorListener );

		this.method = method;
		this.headers = headers;

        if (NetworkLogHelper.LOG_DEBUG_INFO)
            Log.i(TAG, RequestLoggingHelper.getRequestText(this));
    }

	public int getMethod()
	{
		return method;
	}

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError
    {
        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put( "Content-Type", getBodyContentType() );

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
    protected void deliverResponse(JSONArray response)
    {
        if (NetworkLogHelper.LOG_DEBUG_INFO)
            Log.i(TAG, RequestLoggingHelper.getRequestResponseText(this, response));

        super.deliverResponse(response);
    }
}