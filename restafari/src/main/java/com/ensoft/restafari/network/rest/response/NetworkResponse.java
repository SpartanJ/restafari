package com.ensoft.restafari.network.rest.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NetworkResponse implements Serializable
{
	private List<Header> convertHeaders( List<com.android.volley.Header> headers )
	{
		ArrayList<Header> ret = new ArrayList<>();
		
		for ( com.android.volley.Header header : headers )
		{
			ret.add( new Header( header.getName(), header.getValue() ) );
		}
		
		return ret;
	}
	
	public NetworkResponse( com.android.volley.NetworkResponse networkResponse )
	{
		this.statusCode = networkResponse.statusCode;
		this.headers = networkResponse.headers;
		this.allHeaders = convertHeaders( networkResponse.allHeaders );
		this.notModified = networkResponse.notModified;
		this.networkTimeMs = networkResponse.networkTimeMs;
	}
	
	public NetworkResponse( int statusCode, Map<String, String> headers, List<Header> allHeaders, boolean notModified, long networkTimeMs )
	{
		this.statusCode = statusCode;
		this.headers = headers;
		this.allHeaders = allHeaders;
		this.notModified = notModified;
		this.networkTimeMs = networkTimeMs;
	}
	
	/** The HTTP status code. */
	public final int statusCode;
	
	/** Response headers. */
	public final Map<String, String> headers;
	
	/** All response headers. */
	public final List<Header> allHeaders;
	
	/** True if the server returned a 304 (Not Modified). */
	public final boolean notModified;
	
	/** Network roundtrip time in milliseconds. */
	public final long networkTimeMs;
}
