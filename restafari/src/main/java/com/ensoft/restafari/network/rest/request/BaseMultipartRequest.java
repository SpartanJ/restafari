package com.ensoft.restafari.network.rest.request;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

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
