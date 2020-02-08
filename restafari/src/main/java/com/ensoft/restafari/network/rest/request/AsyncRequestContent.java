package com.ensoft.restafari.network.rest.request;

import org.json.JSONObject;

import androidx.annotation.Nullable;

public abstract class AsyncRequestContent
{
	private AsyncRequestSentListener readyListener;
	
	public AsyncRequestContent()
	{}
	
	public AsyncRequestContent( @Nullable AsyncRequestSentListener readyListener )
	{
		this.readyListener = readyListener;
	}
	
	public abstract JSONObject getRequestParameters();
	
	public abstract RequestConfiguration getRequestConfiguration();
	
	AsyncRequestSentListener getReadyListener()
	{
		return readyListener;
	}
	
	void setReadyListener( @Nullable AsyncRequestSentListener readyListener )
	{
		this.readyListener = readyListener;
	}
}
