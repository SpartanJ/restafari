package com.ensoft.restafari.network.processor;

import android.content.Context;

import com.ensoft.restafari.network.rest.request.RequestConfiguration;

public abstract class ResponseProcessor<T>
{
	public long requestId;

	public long getRequestId()
	{
		return requestId;
	}

	public abstract void handleResponse( Context context, RequestConfiguration request, T response );

	public void handleError( Context context, RequestConfiguration request, int errorCode, String errorMessage ) {}
}
