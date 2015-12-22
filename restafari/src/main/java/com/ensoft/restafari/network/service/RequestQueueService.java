package com.ensoft.restafari.network.service;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.Volley;
import com.ensoft.restafari.network.cookie.PersistentCookieStore;
import com.ensoft.restafari.network.proxy.ProxiedHurlStack;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;

public class RequestQueueService
{
	protected RequestQueue requestQueue;
	protected Context context;

	protected String proxyHost = "";
	protected int proxyPort = 8118;
	PersistentCookieStore persistentCookieStore;
	CookieManager cookieManager;

	public RequestQueueService( Context context )
	{
		this.context = context;

		requestQueue = getRequestQueue();

		persistentCookieStore = new PersistentCookieStore( context );

		cookieManager = new CookieManager( persistentCookieStore, CookiePolicy.ACCEPT_ORIGINAL_SERVER );

		CookieHandler.setDefault( cookieManager );
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
		HttpStack httpStack = null;

		if ( null != proxyHost && !proxyHost.isEmpty() )
		{
			httpStack = new ProxiedHurlStack( proxyHost, proxyPort );
		}

		// getApplicationContext() is key, it keeps you from leaking the
		// Activity or BroadcastReceiver if someone passes one in.
		requestQueue = Volley.newRequestQueue( context.getApplicationContext(), httpStack );
	}

	public void setProxy( String host, int port )
	{
		proxyHost = host;
		proxyPort = port;
		createRequestQueue();
	}

	public void setProxyHost( String proxyHost )
	{
		this.proxyHost = proxyHost;
		createRequestQueue();
	}

	public void setProxyPort( int proxyPort )
	{
		this.proxyPort = proxyPort;
		createRequestQueue();
	}

	public String getProxyHost()
	{
		return proxyHost;
	}

	public int getProxyPort()
	{
		return proxyPort;
	}
}
