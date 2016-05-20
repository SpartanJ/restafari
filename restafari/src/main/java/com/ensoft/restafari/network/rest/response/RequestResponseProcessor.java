package com.ensoft.restafari.network.rest.response;

import android.content.Context;
import android.content.Intent;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.ensoft.restafari.helper.ReflectionHelper;
import com.ensoft.restafari.network.processor.ResponseProcessor;
import com.ensoft.restafari.network.rest.request.RequestConfiguration;
import com.ensoft.restafari.network.rest.request.RequestProvider;
import com.ensoft.restafari.network.service.RequestService;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public class RequestResponseProcessor<T>
{
	public static final String TAG = RequestResponseProcessor.class.getCanonicalName();

	public static final String REQUEST_RESULT = "requestResult";
	public static final String RESULT_CODE = "resultCode";
	public static final String REQUEST_ID = "requestId";
	public static final String REQUEST_PARAMS = "requestParams";
	public static final String RESPONSE_CODE = "responseCode";
	public static final String RESULT_MSG = "resultMsg";

	public static final int REQUEST_RESPONSE_FAIL = 0;
	public static final int REQUEST_RESPONSE_SUCCESS = 1;

	protected Context context;

	public RequestResponseProcessor( Context context )
	{
		this.context = context;
	}

	public void queueRequest( RequestConfiguration request, JSONObject parameters, Map<String, String> headers, RetryPolicy retryPolicy, long requestId )
	{
		Request<?> req = RequestProvider.createRequest( request, parameters, headers, getResponseListener( request, parameters, requestId ), getErrorListener( request, parameters, requestId ) );

		req.setRetryPolicy( retryPolicy );

		RequestService.getInstance().addToRequestQueue( req );
	}

	protected Response.ErrorListener getErrorListener( final RequestConfiguration request, final JSONObject parameters, final long requestId )
	{
		return new Response.ErrorListener()
		{
			@Override
			public void onErrorResponse( final VolleyError error )
			{
				new Thread( new Runnable()
				{
					@Override
					public void run()
					{
						String errorMsg = error.getMessage();
						int statusCode = error.networkResponse == null ? HttpStatus.NOT_FOUND_404.getCode() : error.networkResponse.statusCode;

						if ( null == errorMsg && error.networkResponse != null && error.networkResponse.data.length > 0 )
						{
							try
							{
								errorMsg = new String( error.networkResponse.data, "UTF-8" );
							}
							catch ( UnsupportedEncodingException exception )
							{
								errorMsg = new String( error.networkResponse.data );
							}
						}

						if ( null != request.getProcessorClass() )
						{
							ResponseProcessor processor = ReflectionHelper.createInstance( request.getProcessorClass() );

							processor.handleError( context, request, statusCode, errorMsg );
						}

						broadcastRequestResponse( REQUEST_RESPONSE_FAIL, parameters, statusCode, errorMsg, requestId );
					}
				} ).start();
			}
		};
	}

	protected Response.Listener<T> getResponseListener( final RequestConfiguration request, final JSONObject parameters, final long requestId )
	{
		return new Response.Listener<T>()
		{
			@SuppressWarnings("unchecked")
			@Override
			public void onResponse( final T response )
			{
				new Thread( new Runnable()
				{
					@Override
					public void run()
					{
						String responseString = response.toString();

						if ( null != request.getProcessorClass() )
						{
							if ( response instanceof String )
							{
								ResponseProcessor processor = ReflectionHelper.createInstance( request.getProcessorClass() );
								processor.requestId = requestId;
								processor.handleResponse( context, request, response );
							}
							else if ( request.getResponseClass() != null )
							{
								ResponseProcessor processor = ReflectionHelper.createInstance( request.getProcessorClass() );
								processor.requestId = requestId;

								if ( RequestService.getInstance().getRequestServiceOptions().isUnsafeConversion() )
								{
									Object resourceResponse = new Gson().fromJson( responseString, request.getResponseClass() );

									processor.handleResponse( context, request, resourceResponse );
								}
								else
								{
									try
									{
										Object resourceResponse = new Gson().fromJson( responseString, request.getResponseClass() );

										processor.handleResponse( context, request, resourceResponse );
									}
									catch ( Exception exception )
									{
										processor.handleError( context, request, HttpStatus.UNKNOWN_ERROR.getCode(), exception.toString() );

										broadcastRequestResponse( REQUEST_RESPONSE_FAIL, parameters, HttpStatus.UNKNOWN_ERROR.getCode(), exception.toString(), requestId );

										return;
									}
								}
							}
						}

						broadcastRequestResponse( REQUEST_RESPONSE_SUCCESS, parameters, HttpStatus.OK_200.getCode(), responseString, requestId );
					}
				} ).start();
			}
		};
	}

	protected void broadcastRequestResponse( int resultCode, JSONObject requestParams, int statusCode, String msg, long requestId )
	{
		Intent resultBroadcast = new Intent( REQUEST_RESULT );
		resultBroadcast.putExtra( REQUEST_ID, requestId );
		resultBroadcast.putExtra( RESPONSE_CODE, statusCode );
		resultBroadcast.putExtra( REQUEST_PARAMS, requestParams.toString() );
		resultBroadcast.putExtra( RESULT_CODE, resultCode );

		if ( HttpStatus.OK_200.getCode() != statusCode && null != msg && msg.length() <= 92160 )
		{
			resultBroadcast.putExtra( RESULT_MSG, msg );
		}

		RequestService.getInstance().getRequestDelayedBroadcast().queueBroadcast( resultBroadcast );

		context.sendBroadcast( resultBroadcast );
	}
}
