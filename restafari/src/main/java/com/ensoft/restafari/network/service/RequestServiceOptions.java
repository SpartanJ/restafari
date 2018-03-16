package com.ensoft.restafari.network.service;

import com.android.volley.DefaultRetryPolicy;

public class RequestServiceOptions
{
	protected String proxyHost = "";
	protected int proxyPort = 8118;
	protected boolean allowUntrustedConnections = false;
	protected DefaultRetryPolicy defaultRetryPolicy;
	protected boolean unsafeConversion;

	public RequestServiceOptions()
	{
		defaultRetryPolicy = new DefaultRetryPolicy( DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT );
	}

	public RequestServiceOptions( Builder builder )
	{
		proxyHost = builder.proxyHost;
		proxyPort = builder.proxyPort;
		allowUntrustedConnections = builder.allowUntrustedConnections;
		defaultRetryPolicy = builder.defaultRetryPolicy;
		unsafeConversion = builder.unsafeConversions;
	}

	public static class Builder
	{
		private String proxyHost;
		private int proxyPort;
		private boolean allowUntrustedConnections;
		private DefaultRetryPolicy defaultRetryPolicy;
		private boolean unsafeConversions;

		public Builder()
		{
			defaultRetryPolicy = new DefaultRetryPolicy( DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT );
		}

		public Builder setProxyHost( String proxyHost )
		{
			this.proxyHost = proxyHost;
			return this;
		}

		public Builder setProxyPort( int proxyPort )
		{
			this.proxyPort = proxyPort;
			return this;
		}

		public Builder setAllowUntrustedConnections( boolean allowUntrustedConnections )
		{
			this.allowUntrustedConnections = allowUntrustedConnections;
			return this;
		}

		public Builder setDefaultRetryPolicy( DefaultRetryPolicy defaultRetryPolicy )
		{
			this.defaultRetryPolicy = defaultRetryPolicy;
			return this;
		}

		public Builder setUnsafeConversions( boolean unsafeConversions )
		{
			this.unsafeConversions = unsafeConversions;
			return this;
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

	public DefaultRetryPolicy getDefaultRetryPolicy()
	{
		return new DefaultRetryPolicy( defaultRetryPolicy.getCurrentTimeout(), defaultRetryPolicy.getCurrentRetryCount(), defaultRetryPolicy.getBackoffMultiplier() );
	}

	public void setDefaultRetryPolicy( DefaultRetryPolicy defaultRetryPolicy )
	{
		this.defaultRetryPolicy = defaultRetryPolicy;
	}

	public boolean isAllowUntrustedConnections()
	{
		return allowUntrustedConnections;
	}

	public boolean isUnsafeConversion()
	{
		return unsafeConversion;
	}

	public void apply()
	{
		RequestService.getInstance().createRequestQueue( this );
	}
}
