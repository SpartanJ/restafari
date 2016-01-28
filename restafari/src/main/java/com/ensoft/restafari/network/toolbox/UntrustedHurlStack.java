package com.ensoft.restafari.network.toolbox;

import android.util.Log;

import com.android.volley.toolbox.HurlStack;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class UntrustedHurlStack extends HurlStack
{
	public static String TAG = "UntrustedHurlStack";

	public static TrustManager[] getTrustAllCertsManager()
	{
		return new TrustManager[] {
			new X509TrustManager()
			{
				public X509Certificate[] getAcceptedIssuers()
				{
					return null;
				}

				@Override
				public void checkClientTrusted(X509Certificate[] certs, String authType) {}

				@Override
				public void checkServerTrusted(X509Certificate[] certs, String authType) {}
			}
		};
	}

	protected static void setTrustAllCerts( HttpsURLConnection httpsURLConnection )
	{
		try
		{
			SSLContext sc = SSLContext.getInstance( "TLS" );
			sc.init( null, getTrustAllCertsManager(), new SecureRandom() );
			httpsURLConnection.setSSLSocketFactory( sc.getSocketFactory() );
		}
		catch( Exception exception )
		{
			Log.e( TAG, exception.toString() );
		}
	}

	public static void trustAllCertsForConnection( HttpURLConnection httpURLConnection )
	{
		if ( httpURLConnection instanceof HttpsURLConnection )
		{
			HttpsURLConnection httpsURLConnection = (HttpsURLConnection)httpURLConnection;

			setTrustAllCerts( httpsURLConnection );

			httpsURLConnection.setHostnameVerifier( new HostnameVerifier()
			{
				@Override
				public boolean verify( String hostname, SSLSession session )
				{
					return true;
				}
			} );
		}
	}

	@Override
	protected HttpURLConnection createConnection(URL url) throws IOException
	{
		HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

		trustAllCertsForConnection( httpURLConnection );

		return httpURLConnection;
	}
}
