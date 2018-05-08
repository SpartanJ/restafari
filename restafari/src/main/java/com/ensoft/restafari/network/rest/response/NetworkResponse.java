package com.ensoft.restafari.network.rest.response;

import java.io.Serializable;
import java.util.Map;

public class NetworkResponse implements Serializable
{
	public NetworkResponse( com.android.volley.NetworkResponse networkResponse )
	{
		this.statusCode = networkResponse.statusCode;
		this.headers = networkResponse.headers;
		this.notModified = networkResponse.notModified;
		this.networkTimeMs = networkResponse.networkTimeMs;
	}
	
	public NetworkResponse( int statusCode, Map<String, String> headers, boolean notModified, long networkTimeMs )
	{
		this.statusCode = statusCode;
		this.headers = headers;
		this.notModified = notModified;
		this.networkTimeMs = networkTimeMs;
	}
	
	/** The HTTP status code. */
	public final int statusCode;
	
	/** Response headers. */
	public final Map<String, String> headers;
	
	/** True if the server returned a 304 (Not Modified). */
	public final boolean notModified;
	
	/** Network roundtrip time in milliseconds. */
	public final long networkTimeMs;
}
