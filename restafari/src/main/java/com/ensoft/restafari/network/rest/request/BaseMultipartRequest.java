package com.ensoft.restafari.network.rest.request;

import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.ensoft.restafari.network.service.RequestService;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public class BaseMultipartRequest extends MultipartRequest<String>
{
	public BaseMultipartRequest( int method, String url, JSONObject parameters, Map<String, String> headers, Response.Listener<String> listener, Response.ErrorListener errorListener )
	{
		super( method, url, parameters, headers, listener, errorListener );
	}

	@Override
	protected Response<String> parseNetworkResponse(NetworkResponse response)
	{
		try
		{
			RequestService.getInstance().getResponseStatusManager().add( (long)getTag(), new com.ensoft.restafari.network.rest.response.NetworkResponse( response ) );
		} catch ( Exception e ) { Log.i( TAG, e.toString() ); }
		
		String parsed;

		try
		{
			parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
		}
		catch (UnsupportedEncodingException e)
		{
			parsed = new String(response.data);
		}

		return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
	}
}
