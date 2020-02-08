package com.ensoft.restafari.network.rest.request;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.ensoft.restafari.network.helper.NetworkLogHelper;
import com.ensoft.restafari.network.service.RequestService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;

public abstract class BaseJsonArrayRequest extends JsonArrayRequest
{
    protected static final String TAG = BaseJsonArrayRequest.class.getSimpleName();

	protected int method;
	protected Map<String, String> headers;
	@Nullable protected final String requestBody;

    protected BaseJsonArrayRequest( int method, String url, JSONObject parameters, Map<String, String> headers, Listener<JSONArray> listener, ErrorListener errorListener )
    {
        super( method, url, null, listener, errorListener );

		this.method = method;
		this.headers = headers;
		this.requestBody = (parameters == null) ? null : parameters.toString();

        if ( NetworkLogHelper.LOG_DEBUG_INFO )
            Log.i(TAG, RequestLoggingHelper.getRequestText(this));
    }
    
	protected BaseJsonArrayRequest( int method, String url, JSONArray parameters, Map<String, String> headers, Listener<JSONArray> listener, ErrorListener errorListener )
	{
		super( method, url, null, listener, errorListener );
		
		this.method = method;
		this.headers = headers;
		this.requestBody = (parameters == null) ? null : parameters.toString();
		
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
    protected void deliverResponse(JSONArray response)
    {
        if (NetworkLogHelper.LOG_DEBUG_INFO)
            Log.i(TAG, RequestLoggingHelper.getRequestResponseText(this, response));

        super.deliverResponse(response);
    }
    
	@Override
	protected Response<JSONArray> parseNetworkResponse( NetworkResponse response) {
		try
		{
			RequestService.getInstance().getResponseStatusManager().add( (long)getTag(), new com.ensoft.restafari.network.rest.response.NetworkResponse( response ) );
		} catch ( Exception e ) { Log.i( TAG, e.toString() ); }
		
    	return super.parseNetworkResponse( response );
	}
	
	@Override
	public byte[] getBody() {
		try {
			return requestBody == null ? null : requestBody.getBytes(PROTOCOL_CHARSET);
		} catch ( UnsupportedEncodingException uee) {
			VolleyLog.wtf(
				"Unsupported Encoding while trying to get the bytes of %s using %s",
				requestBody, PROTOCOL_CHARSET);
			return null;
		}
	}
}