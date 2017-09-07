package com.ensoft.restafari.network.service;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.Volley;
import com.ensoft.restafari.network.cookie.PersistentCookieStore;
import com.ensoft.restafari.network.rest.response.RequestResponseProcessor;
import com.ensoft.restafari.network.rest.request.RequestConfiguration;
import com.ensoft.restafari.network.rest.request.RequestDelayedBroadcast;
import com.ensoft.restafari.network.toolbox.ProxiedHurlStack;
import com.ensoft.restafari.network.toolbox.UntrustedHurlStack;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RequestService
{
	protected static RequestService instance;

	protected Context context;

	protected PersistentCookieStore persistentCookieStore;
	protected CookieManager cookieManager;
	protected RequestQueue requestQueue;

	protected RequestServiceOptions requestServiceOptions;
	protected RequestDelayedBroadcast requestDelayedBroadcast;

	protected RequestResponseProcessor requestResponseProcessor;

	public static synchronized RequestService init( Context context )
	{
		return init( context, new RequestServiceOptions() );
	}

	public static synchronized RequestService init( Context context, RequestServiceOptions requestServiceOptions )
	{
		if ( instance == null )
		{
			instance = new RequestService( context, requestServiceOptions );
		}

		return instance;
	}

	public static synchronized RequestService getInstance()
	{
		return instance;
	}

	private RequestService( Context context, RequestServiceOptions requestServiceOptions )
	{
		this.context = context;

		this.requestServiceOptions = requestServiceOptions;

		requestQueue = getRequestQueue();

		persistentCookieStore = new PersistentCookieStore( context );

		cookieManager = new CookieManager( persistentCookieStore, CookiePolicy.ACCEPT_ORIGINAL_SERVER );

		CookieHandler.setDefault( cookieManager );

		requestDelayedBroadcast = new RequestDelayedBroadcast();
		requestResponseProcessor = new RequestResponseProcessor( context );
	}

	public RequestServiceOptions getRequestServiceOptions()
	{
		return requestServiceOptions;
	}

	public PersistentCookieStore getPersistentCookieStore()
	{
		return persistentCookieStore;
	}

	public CookieManager getCookieManager()
	{
		return cookieManager;
	}

	public RequestQueue getRequestQueue()
	{
		if ( requestQueue == null)
		{
			createRequestQueue();
		}

		return requestQueue;
	}

	public <T> void addToRequestQueue(Request<T> req)
	{
		getRequestQueue().add(req);
	}

	public void createRequestQueue()
	{
		createRequestQueue( requestServiceOptions );
	}

	public void createRequestQueue( RequestServiceOptions requestServiceOptions )
	{
		HttpStack httpStack = null;

		if ( null != requestServiceOptions.getProxyHost() && !requestServiceOptions.getProxyHost().isEmpty() )
		{
			httpStack = new ProxiedHurlStack( requestServiceOptions.getProxyHost(), requestServiceOptions.getProxyPort(), requestServiceOptions.getAllowUntrustedConnections());
		}
		else if ( requestServiceOptions.getAllowUntrustedConnections() )
		{
			httpStack = new UntrustedHurlStack();
		}

		// getApplicationContext() is key, it keeps you from leaking the
		// Activity or BroadcastReceiver if someone passes one in.
		requestQueue = Volley.newRequestQueue( context.getApplicationContext(), httpStack );
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

	public long addRequest( RequestConfiguration requestConfiguration, JSONObject parameters, Map<String, String> headers, RetryPolicy retryPolicy )
	{
		long requestId = generateRequestID();

		if ( null == parameters )
			parameters = new JSONObject();

		if ( null == headers )
			headers = new HashMap<>();

		if ( null == retryPolicy )
			retryPolicy = new DefaultRetryPolicy( 0, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT );

		requestResponseProcessor.queueRequest( requestConfiguration, parameters, headers, retryPolicy, requestId );

		return requestId;
	}

	public long addRequest( RequestConfiguration requestConfiguration, JSONObject parameters, Map<String, String> headers )
	{
		return addRequest( requestConfiguration, parameters, headers, getRequestServiceOptions().getDefaultRetryPolicy() );
	}

	public long addRequest( RequestConfiguration requestConfiguration, JSONObject parameters )
	{
		return addRequest( requestConfiguration, parameters, new HashMap<String, String>() );
	}

	public long addRequest( RequestConfiguration requestConfiguration )
	{
		return addRequest( requestConfiguration, new JSONObject() );
	}
	
	public long addRequest( RequestConfiguration requestConfiguration, JSONArray parameters, Map<String, String> headers, RetryPolicy retryPolicy )
	{
		long requestId = generateRequestID();
		
		if ( null == parameters )
			parameters = new JSONArray();
		
		if ( null == headers )
			headers = new HashMap<>();
		
		if ( null == retryPolicy )
			retryPolicy = new DefaultRetryPolicy( 0, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT );
		
		requestResponseProcessor.queueRequest( requestConfiguration, parameters, headers, retryPolicy, requestId );
		
		return requestId;
	}
	
	public long addRequest( RequestConfiguration requestConfiguration, JSONArray parameters, Map<String, String> headers )
	{
		return addRequest( requestConfiguration, parameters, headers, getRequestServiceOptions().getDefaultRetryPolicy() );
	}
	
	public long addRequest( RequestConfiguration requestConfiguration, JSONArray parameters )
	{
		return addRequest( requestConfiguration, parameters, new HashMap<String, String>() );
	}
}
