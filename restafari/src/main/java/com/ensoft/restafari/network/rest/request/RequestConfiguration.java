package com.ensoft.restafari.network.rest.request;

import com.android.volley.Request;
import com.ensoft.restafari.network.processor.ResponseProcessor;

public class RequestConfiguration
{
	private final Class<? extends Request> requestClass;
	private final Class<?> responseClass;
	private final Class<? extends ResponseProcessor> processorClass;

	public RequestConfiguration( Class<? extends Request> requestClass, Class<? extends ResponseProcessor> processorClass, Class<?> responseClass )
	{
		this.requestClass = requestClass;
		this.processorClass = processorClass;
		this.responseClass = responseClass;
	}

	public RequestConfiguration( Class<? extends Request> requestClass, Class<? extends ResponseProcessor> processorClass )
	{
		this( requestClass, processorClass, null );
	}

	public RequestConfiguration( Class<? extends Request> requestClass )
	{
		this( requestClass, null, null );
	}

	public Class<?> getResponseClass()
	{
		return responseClass;
	}

	public Class<? extends Request> getRequestClass()
	{
		return requestClass;
	}

	public Class<? extends ResponseProcessor> getProcessorClass()
	{
		return processorClass;
	}
}
