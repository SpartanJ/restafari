package com.ensoft.restafari.network.service;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RetryPolicy;

public class RequestServiceOptions
{
	protected String proxyHost = "";
	protected int proxyPort = 8118;
	protected boolean allowUntrustedConnections = false;
	private RetryPolicy defaultRetryPolicy;

	public RequestServiceOptions()
	{
		defaultRetryPolicy = new DefaultRetryPolicy( 0, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT );
	}

	public RequestServiceOptions( Builder builder )
	{
		proxyHost = builder.proxyHost;
		proxyPort = builder.proxyPort;
		allowUntrustedConnections = builder.allowUntrustedConnections;
		defaultRetryPolicy = builder.defaultRetryPolicy;
	}

	public static class Builder
	{
		private String proxyHost;
		private int proxyPort;
		private boolean allowUntrustedConnections;
		private RetryPolicy defaultRetryPolicy;

		public Builder()
		{
			defaultRetryPolicy = new DefaultRetryPolicy( 0, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT );
		}

		public void setProxyHost( String proxyHost )
		{
			this.proxyHost = proxyHost;
		}

		public void setProxyPort( int proxyPort )
		{
			this.proxyPort = proxyPort;
		}

		public void setAllowUntrustedConnections( boolean allowUntrustedConnections )
		{
			this.allowUntrustedConnections = allowUntrustedConnections;
		}

		public void setDefaultRetryPolicy( RetryPolicy defaultRetryPolicy )
		{
			this.defaultRetryPolicy = defaultRetryPolicy;
		}

		public RequestServiceOptions build()
		{
			return new RequestServiceOptions( this );
		}
	}

	public String getProxyHost()
	{
		return proxyHost;
	}

	public void setProxyHost( String proxyHost )
	{
		this.proxyHost = proxyHost;
	}

	public int getProxyPort()
	{
		return proxyPort;
	}

	public void setProxyPort( int proxyPort )
	{
		this.proxyPort = proxyPort;
	}

	public boolean getAllowUntrustedConnections()
	{
		return allowUntrustedConnections;
	}

	public void setAllowUntrustedConnections( boolean allowUntrustedConnections )
	{
		this.allowUntrustedConnections = allowUntrustedConnections;
	}

	public void setProxy( String host, int port )
	{
		setProxy( host, port, allowUntrustedConnections );
	}

	public void setProxy( String host, int port, boolean allowUntrustedConnections )
	{
		proxyHost = host;
		proxyPort = port;
		this.allowUntrustedConnections = allowUntrustedConnections;
	}

	public RetryPolicy getDefaultRetryPolicy()
	{
		return defaultRetryPolicy;
	}

	public void setDefaultRetryPolicy( RetryPolicy defaultRetryPolicy )
	{
		this.defaultRetryPolicy = defaultRetryPolicy;
	}

	public void apply()
	{
		RequestService.getInstance().createRequestQueue( this );
	}
}