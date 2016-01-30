package com.ensoft.restafari.network.service;

public class RequestServiceOptions
{
	protected String proxyHost = "";
	protected int proxyPort = 8118;
	protected boolean allowUntrustedConnections = false;

	public RequestServiceOptions()
	{
	}

	public RequestServiceOptions( Builder builder )
	{
		proxyHost = builder.proxyHost;
		proxyPort = builder.proxyPort;
		allowUntrustedConnections = builder.allowUntrustedConnections;
	}

	public static class Builder
	{
		private String proxyHost;
		private int proxyPort;
		private boolean allowUntrustedConnections;

		public Builder()
		{
		}

		public Builder( String proxyHost, int proxyPort )
		{
			this( proxyHost, proxyPort, false );
		}

		public Builder( String proxyHost, int proxyPort, boolean allowUntrustedConnections )
		{
			this.proxyHost = proxyHost;
			this.proxyPort = proxyPort;
			this.allowUntrustedConnections = allowUntrustedConnections;
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

	public void apply()
	{
		RequestService.getInstance().createRequestQueue( this );
	}
}
