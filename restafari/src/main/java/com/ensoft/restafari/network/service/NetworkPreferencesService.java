package com.ensoft.restafari.network.service;

import android.content.Context;
import android.content.SharedPreferences;

public class NetworkPreferencesService
{
	public static final String PROXY_HOST = "proxyHost";
	public static final String PROXY_PORT = "proxyPort";

	protected Context context;

	public SharedPreferences getDefaultSharedPreferences()
	{
		return context.getSharedPreferences( getDefaultSharedPreferencesName( context ), getDefaultSharedPreferencesMode() );
	}

	private static String getDefaultSharedPreferencesName( Context context )
	{
		return context.getPackageName() + "_preferences";
	}

	private static int getDefaultSharedPreferencesMode()
	{
		return Context.MODE_PRIVATE;
	}

	public NetworkPreferencesService( Context context )
	{
		this.context = context;
	}

	public boolean isProxySet()
	{
		return !getProxyHost().isEmpty();
	}

	public String getProxyHost()
	{
		return getDefaultSharedPreferences().getString( PROXY_HOST, "" );
	}

	public int getProxyPort()
	{
		return Integer.valueOf( getDefaultSharedPreferences().getString( PROXY_PORT, "8118" ) );
	}

	public void setProxyHost( String proxyHost )
	{
		getDefaultSharedPreferences().edit().putString( PROXY_HOST, proxyHost ).apply();

		RequestService.getInstance().getRequestServiceOptions().setProxyHost( proxyHost );
		RequestService.getInstance().createRequestQueue();
	}

	public void setProxyPort( int proxyPort )
	{
		getDefaultSharedPreferences().edit().putString( PROXY_PORT, Integer.toString( proxyPort ) ).apply();

		RequestService.getInstance().getRequestServiceOptions().setProxyPort( proxyPort );
		RequestService.getInstance().createRequestQueue();
	}
}
