package com.ensoft.restafari.network.toolbox;

import com.android.volley.toolbox.HurlStack;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;

public class ProxiedHurlStack extends HurlStack
{
	protected String proxyHost;
	protected int proxyPort;
	protected boolean allowUntrustedConnections;

	public ProxiedHurlStack( String host, int port )
	{
		this( host, port, false );
	}

	public ProxiedHurlStack( String host, int port, boolean allowUntrustedConnections )
	{
		super();
		proxyHost = host;
		proxyPort = port;
		this.allowUntrustedConnections = allowUntrustedConnections;
	}

	@Override
	protected HttpURLConnection createConnection(URL url) throws IOException
	{
		// Start the connection by specifying a proxy server
		Proxy proxy = new Proxy( Proxy.Type.HTTP, InetSocketAddress.createUnresolved( proxyHost, proxyPort ) );

		HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection(proxy);

		if ( allowUntrustedConnections )
		{
			UntrustedHurlStack.trustAllCertsForConnection( httpURLConnection );
		}

		return httpURLConnection;
	}
}