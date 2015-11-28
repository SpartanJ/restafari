package com.ensoft_dev.restafari.network.processor;

import android.content.Context;

import com.ensoft_dev.restafari.network.rest.request.RequestConfiguration;

public abstract class ResponseProcessor<T>
{
	public abstract void handleResponse( Context context, RequestConfiguration request, T response );

	public void handleError( Context context, RequestConfiguration request, int errorCode, String errorMessage ) {}
}
