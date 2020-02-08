package com.ensoft.restafari.network.rest.request;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ensoft.restafari.network.helper.MultipartEntity;
import com.ensoft.restafari.network.helper.NetworkLogHelper;
import com.ensoft.restafari.network.helper.RequestParameters;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.core.util.Pair;

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

		Map<String, String> params = RequestParameters.toMap( parameters );
		Map<String, String> fileParams = new HashMap<>();

		if ( null != params )
		{
			List<String> removeKeys = new ArrayList<>();

			for ( Map.Entry<String, String> entry : params.entrySet() )
			{
				if ( entry.getKey().startsWith( "restafari-attachment-id-" ) )
				{
					try
					{
						Type type = new TypeToken<Pair<String,String>>() {}.getType();
						
						Pair<String,String> keyVal = new Gson().fromJson( entry.getValue(), type );
						
						fileParams.put( keyVal.first, keyVal.second );
						removeKeys.add( entry.getKey() );
					}
					catch ( Exception e )
					{
						Log.e( TAG, "Attatchment failed to add: " + e.toString() );
					}
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
		return headers != null ? headers : new HashMap<String, String>();
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
