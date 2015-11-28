package com.ensoft_dev.restafari.network.service;

import android.content.Context;

import com.ensoft_dev.restafari.network.rest.request.RequestConfiguration;
import com.ensoft_dev.restafari.network.rest.request.RequestDelayedBroadcast;
import com.ensoft_dev.restafari.network.rest.response.RequestResponseProcessor;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RequestService
{
	protected static RequestService instance;

	protected RequestQueueService requestQueueService;
	protected RequestDelayedBroadcast requestDelayedBroadcast;
	protected Context context;
	protected RequestResponseProcessor requestResponseProcessor;

	public static synchronized RequestService init( Context context )
	{
		if ( instance == null )
		{
			instance = new RequestService( context );
		}

		return instance;
	}

	public static synchronized RequestService getInstance()
	{
		return instance;
	}

	private RequestService( Context context )
	{
		this.context = context;

		requestQueueService = new RequestQueueService( context );
		requestDelayedBroadcast = new RequestDelayedBroadcast();
		requestResponseProcessor = new RequestResponseProcessor( context );
	}

	public RequestQueueService getRequestQueueService()
	{
		return requestQueueService;
	}

	public RequestDelayedBroadcast getRequestDelayedBroadcast()
	{
		return requestDelayedBroadcast;
	}

	protected long generateRequestID()
	{
		long random = UUID.randomUUID().getLeastSignificantBits();
		if (random < 0) random = random * -1;
		return random;
	}

	public long addRequest( RequestConfiguration requestConfiguration, JSONObject parameters, Map<String, String> headers )
	{
		long requestId = generateRequestID();

		if ( null == parameters )
			parameters = new JSONObject();

		if ( null == headers )
			headers = new HashMap<>();

		requestResponseProcessor.queueRequest( requestConfiguration, parameters, headers, requestId );

		return requestId;
	}

	public long addRequest( RequestConfiguration requestConfiguration, JSONObject parameters )
	{
		return addRequest( requestConfiguration, parameters, new HashMap<String, String>() );
	}

	public long addRequest( RequestConfiguration requestConfiguration )
	{
		return addRequest( requestConfiguration, new JSONObject() );
	}
}
