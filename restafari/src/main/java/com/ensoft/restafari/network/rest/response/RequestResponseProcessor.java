package com.ensoft.restafari.network.rest.response;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.ensoft.restafari.helper.ReflectionHelper;
import com.ensoft.restafari.helper.ThreadMode;
import com.ensoft.restafari.helper.ThreadRunner;
import com.ensoft.restafari.network.processor.ResponseListener;
import com.ensoft.restafari.network.processor.ResponseProcessor;
import com.ensoft.restafari.network.rest.request.RequestConfiguration;
import com.ensoft.restafari.network.rest.request.RequestProvider;
import com.ensoft.restafari.network.service.RequestService;
import com.google.gson.Gson;

import org.json.JSONArray;
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
	public static final String RESULT_NETWORK_RESPONSE = "resultNetworkResponse";

	public static final int REQUEST_RESPONSE_FAIL = 0;
	public static final int REQUEST_RESPONSE_SUCCESS = 1;

	protected Context context;

	public RequestResponseProcessor( Context context )
	{
		this.context = context;
	}
	
	public void queueRequest( Request<?> req, RetryPolicy retryPolicy, long requestId )
	{
		if ( null != req )
		{
			req.setRetryPolicy( retryPolicy );
			req.setTag( requestId );
			
			RequestService.getInstance().addToRequestQueue( req );
		}
	}

	public void queueRequest( RequestConfiguration request, JSONObject parameters, Map<String, String> headers, RetryPolicy retryPolicy, long requestId )
	{
		Request<?> req = RequestProvider.createRequest( request, parameters, headers, getResponseListener( request, parameters, requestId ), getErrorListener( request, parameters, requestId ) );
		
		queueRequest( req, retryPolicy, requestId );
	}
	
	public void queueRequest( RequestConfiguration request, JSONArray parameters, Map<String, String> headers, RetryPolicy retryPolicy, long requestId )
	{
		Request<?> req = RequestProvider.createRequest( request, parameters, headers, getResponseListener( request, parameters, requestId ), getErrorListener( request, parameters, requestId ) );
		
		queueRequest( req, retryPolicy, requestId );
	}
	
	public <RequestType> Response.ErrorListener getErrorListener( final RequestConfiguration request, final RequestType parameters, final long requestId )
	{
		return error -> new Thread( () -> {
			String errorMsg = error.getMessage();
			NetworkResponse networkResponse = RequestService.getInstance().getResponseStatusManager().get( requestId, true );
			int statusCode = error.networkResponse == null ? HttpStatus.REQUEST_TIMEOUT_408.getCode() : error.networkResponse.statusCode;

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
				processor.requestId = requestId;
				processor.networkResponse = networkResponse;

				processor.handleError( context, request, statusCode, errorMsg );
			}

			broadcastRequestResponse( REQUEST_RESPONSE_FAIL, parameters, statusCode, errorMsg, requestId, networkResponse );
		} ).start();
	}
	
	@SuppressWarnings( "unchecked" )
	public <RequestType> Response.Listener<T> getResponseListener( final RequestConfiguration request, final RequestType parameters, final long requestId )
	{
		return response -> new Thread( () -> {
			String responseString = response.toString();
			NetworkResponse networkResponse = RequestService.getInstance().getResponseStatusManager().get( requestId, true );

			if ( null != request.getProcessorClass() )
			{
				if ( response instanceof String )
				{
					ResponseProcessor processor = ReflectionHelper.createInstance( request.getProcessorClass() );
					processor.requestId = requestId;
					processor.networkResponse = networkResponse;
					
					processor.handleResponse( context, request, response );
				}
				else if ( request.getResponseClass() != null )
				{
					ResponseProcessor processor = ReflectionHelper.createInstance( request.getProcessorClass() );
					processor.requestId = requestId;
					processor.networkResponse = networkResponse;

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

							broadcastRequestResponse( REQUEST_RESPONSE_FAIL, parameters, HttpStatus.UNKNOWN_ERROR.getCode(), exception.toString(), requestId, networkResponse );

							return;
						}
					}
				}
			}

			broadcastRequestResponse( REQUEST_RESPONSE_SUCCESS, parameters, HttpStatus.OK_200.getCode(), responseString, requestId, networkResponse );
		} ).start();
	}
	
	public <ParamType> Response.ErrorListener getErrorListener( final ResponseListener<T> processor, final ParamType parameters, final long requestId )
	{
		return error -> new Thread( () -> {
			String errorMsg = error.getMessage();
			NetworkResponse networkResponse = RequestService.getInstance().getResponseStatusManager().get( requestId, true );
			int statusCode = error.networkResponse == null ? HttpStatus.REQUEST_TIMEOUT_408.getCode() : error.networkResponse.statusCode;
			
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
			
			if ( null != processor )
			{
				final String message = errorMsg;
				processor.requestId = requestId;
				processor.networkResponse = networkResponse;
				
				ThreadRunner.run( processor.getThreadMode(), () -> processor.onRequestError( context, statusCode, message ) );
			}
			
			broadcastRequestResponse( REQUEST_RESPONSE_FAIL, parameters, statusCode, errorMsg, requestId, networkResponse );
		} ).start();
	}
	
	@SuppressWarnings( "unchecked" )
	public <ParamType> Response.Listener<T> getResponseListener( final ResponseListener<T> processor, final ParamType parameters, final long requestId )
	{
		return response -> new Thread( () -> {
			String responseString = response.toString();
			NetworkResponse networkResponse = RequestService.getInstance().getResponseStatusManager().get( requestId, true );
			
			if ( null != processor )
			{
				if ( response instanceof String )
				{
					processor.requestId = requestId;
					processor.networkResponse = networkResponse;
					
					processor.onRequestSuccess( context, response );
				}
				else
				{
					processor.requestId = requestId;
					processor.networkResponse = networkResponse;
					
					if ( RequestService.getInstance().getRequestServiceOptions().isUnsafeConversion() )
					{
						T resourceResponse = new Gson().fromJson( responseString, ReflectionHelper.getTypeArgument( processor, 0 ) );
						
						ThreadRunner.run( processor.getThreadMode(), () -> processor.onRequestSuccess( context, resourceResponse ) );
					}
					else
					{
						try
						{
							T resourceResponse = new Gson().fromJson( responseString, ReflectionHelper.getTypeArgument( processor, 0 ) );
							
							ThreadRunner.run( processor.getThreadMode(), () -> processor.onRequestSuccess( context, resourceResponse ) );
						}
						catch ( Exception exception )
						{
							ThreadRunner.run( processor.getThreadMode(), () -> processor.onRequestError( context, HttpStatus.UNKNOWN_ERROR.getCode(), exception.toString() ) );
							
							broadcastRequestResponse( REQUEST_RESPONSE_FAIL, parameters, HttpStatus.UNKNOWN_ERROR.getCode(), exception.toString(), requestId, networkResponse );
							
							return;
						}
					}
				}
			}
			
			broadcastRequestResponse( REQUEST_RESPONSE_SUCCESS, parameters, HttpStatus.OK_200.getCode(), responseString, requestId, networkResponse );
		} ).start();
	}
	
	protected void broadcastRequestResponse( int resultCode, Object requestParams, int statusCode, String msg, long requestId, NetworkResponse networkResponse )
	{
		Intent resultBroadcast = new Intent( REQUEST_RESULT );
		resultBroadcast.putExtra( REQUEST_ID, requestId );
		resultBroadcast.putExtra( RESPONSE_CODE, statusCode );
		resultBroadcast.putExtra( REQUEST_PARAMS, requestParams.toString() );
		resultBroadcast.putExtra( RESULT_CODE, resultCode );
		
		if ( null != networkResponse )
			resultBroadcast.putExtra( RESULT_NETWORK_RESPONSE, networkResponse );
		
		if ( ( statusCode < 200 || statusCode >= 300 ) && null != msg && msg.length() <= 92160 )
		{
			resultBroadcast.putExtra( RESULT_MSG, msg );
		}
		
		RequestService.getInstance().getRequestDelayedBroadcast().queueBroadcast( resultBroadcast );
		
		context.sendBroadcast( resultBroadcast );
	}
}
