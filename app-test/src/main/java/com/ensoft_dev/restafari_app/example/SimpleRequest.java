package com.ensoft_dev.restafari_app.example;

import com.android.volley.Response;
import com.ensoft_dev.restafari.network.rest.request.BaseJsonRequest;

import org.json.JSONObject;

import java.util.Map;

// The request structure
public class SimpleRequest extends BaseJsonRequest
{
	public SimpleRequest( JSONObject parameters, Map<String, String> headers, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener )
	{
		super( Method.POST, "http://ip.ensoft-dev.com/?f=json", parameters, headers, listener, errorListener );
	}
}
