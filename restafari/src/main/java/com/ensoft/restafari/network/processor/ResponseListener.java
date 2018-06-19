package com.ensoft.restafari.network.processor;

import android.content.Context;

import com.ensoft.restafari.network.rest.response.NetworkResponse;

public abstract class ResponseListener<T>
{
	public long requestId;
	public NetworkResponse networkResponse;
	
	public long getRequestId()
	{
		return requestId;
	}
	
	public NetworkResponse getNetworkResponse()
	{
		return networkResponse;
	}
	
	public abstract void onRequestSuccess( Context context, T response );
	
	public void onRequestError( Context context, int errorCode, String errorMessage ) {}
}
