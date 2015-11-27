package com.ensoft.restafari.network.rest.response;

import android.content.Context;
import android.content.Intent;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ensoft.restafari.helper.ReflectionHelper;
import com.ensoft.restafari.network.processor.ResponseProcessor;
import com.ensoft.restafari.network.rest.request.RequestConfiguration;
import com.ensoft.restafari.network.rest.request.RequestProvider;
import com.ensoft.restafari.network.service.RequestService;
import com.google.gson.Gson;

import org.json.JSONObject;

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

	public void queueRequest( RequestConfiguration request, JSONObject parameters, Map<String, String> headers, long requestId )
	{
		Request<?> req = RequestProvider.createRequest( request, parameters, headers, getResponseListener( request, parameters, requestId ), getErrorListener( request, parameters, requestId ) );

		req.setRetryPolicy( new DefaultRetryPolicy( 0, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT ) );

		RequestService.getInstance().getRequestQueueService().addToRequestQueue( req );
	}

	protected Response.ErrorListener getErrorListener( final RequestConfiguration request, final JSONObject parameters, final long requestId )
	{
		return new Response.ErrorListener()
		{
			@Override
			public void onErrorResponse( VolleyError error )
			{
				if ( request.getResponseClass() != null )
				{
					ResponseProcessor processor = ReflectionHelper.createInstance( request.getProcessorClass() );

					if ( error.networkResponse == null )
					{
						processor.handleError( context, request, HttpStatus.NOT_FOUND_404.getCode(), error.getMessage() );
					}
					else
					{
						processor.handleError( context, request, error.networkResponse.statusCode, error.getMessage() );
					}
				}

				broadcastRequestResponse( REQUEST_RESPONSE_FAIL, parameters, error.networkResponse == null ? HttpStatus.NOT_FOUND_404.getCode() : error.networkResponse.statusCode, error.getMessage(), requestId );
			}
		};
	}

	protected Response.Listener<T> getResponseListener( final RequestConfiguration request, final JSONObject parameters, final long requestId )
	{
		return new Response.Listener<T>()
		{
			@SuppressWarnings("unchecked")
			@Override
			public void onResponse(T response)
			{
				if ( request.getResponseClass() != null )
				{
					Object resourceResponse = new Gson().fromJson( response.toString(), request.getResponseClass() );
					ResponseProcessor processor = ReflectionHelper.createInstance( request.getProcessorClass() );
					processor.handleResponse( context, request, resourceResponse );
				}

				broadcastRequestResponse( REQUEST_RESPONSE_SUCCESS, parameters, HttpStatus.OK_200.getCode(), response.toString(), requestId );
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
		resultBroadcast.putExtra( RESULT_MSG, msg );

		RequestService.getInstance().getRequestDelayedBroadcast().queueBroadcast( resultBroadcast );

		context.sendBroadcast( resultBroadcast );
	}
}
