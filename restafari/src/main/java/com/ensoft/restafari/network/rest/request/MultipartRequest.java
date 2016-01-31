package com.ensoft.restafari.network.rest.request;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ensoft.restafari.network.helper.MultipartEntity;
import com.ensoft.restafari.network.helper.NetworkLogHelper;
import com.ensoft.restafari.network.helper.ParametersJSONObject;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class MultipartRequest<T> extends Request<T>
{
	protected static final String TAG = BaseMultipartRequest.class.getSimpleName();
	protected static final String PROTOCOL_CHARSET = "utf-8";

	private final Response.Listener<T> listener;

	protected Map<String, String> headers;
	protected String mimeType;
	protected MultipartEntity multipartEntity;
	protected int method;

	public MultipartRequest( int method, String url, JSONObject parameters, Map<String, String> headers, Response.Listener<T> listener, Response.ErrorListener errorListener )
	{
		super(method, url, errorListener);

		this.method = method;
		this.listener = listener;
		this.headers = headers;

		Map<String, String> params = ParametersJSONObject.toMap( parameters );
		Map<String, String> fileParams = new HashMap<>();

		if ( null != params )
		{
			List<String> removeKeys = new ArrayList<>();

			for ( Map.Entry<String, String> entry : params.entrySet() )
			{
				if ( entry.getKey().startsWith( "file" ) )
				{
					fileParams.put( entry.getKey(), entry.getValue() );
					removeKeys.add( entry.getKey() );
				}
			}

			for ( String key : removeKeys )
			{
				params.remove( key );
			}
		}

		multipartEntity = new MultipartEntity( fileParams, params );

		mimeType = "multipart/form-data;boundary=" + multipartEntity.getBoundary();
	}

	public int getMethod()
	{
		return method;
	}

	@Override
	public String getBodyContentType()
	{
		return mimeType;
	}

	@Override
	public Map<String, String> getHeaders() throws AuthFailureError
	{
		Map<String, String> requestHeaders = new HashMap<>();
		requestHeaders.put( "Content-Type", getBodyContentType() );

		if ( headers != null)
		{
			for (Map.Entry<String, String> entry : headers.entrySet())
				requestHeaders.put(entry.getKey(), entry.getValue());
		}

		return requestHeaders;
	}

	@Override
	public byte[] getBody() throws AuthFailureError
	{
		return multipartEntity.build();
	}

	@Override
	public void deliverError(VolleyError error)
	{
		if (NetworkLogHelper.LOG_DEBUG_INFO)
			Log.i( TAG, RequestLoggingHelper.getRequestErrorText( this, error ) );

		super.deliverError(error);
	}

	@Override
	protected void deliverResponse(T response)
	{
		if (NetworkLogHelper.LOG_DEBUG_INFO)
			Log.i(TAG, RequestLoggingHelper.getRequestResponseText(this, response.toString()));

		if ( listener != null)
		{
			listener.onResponse( response );
		}
	}
}
