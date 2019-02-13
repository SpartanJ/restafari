package com.ensoft.restafari.network.service;

import android.content.Context;
import android.content.Intent;

import com.ensoft.restafari.network.rest.response.NetworkResponse;
import com.ensoft.restafari.network.rest.response.RequestResponseProcessor;
import com.ensoft.restafari.network.rest.response.ResponseReceiver;
import com.ensoft.restafari.network.rest.response.ResponseStatusManager;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class ResponseReceiverService implements ResponseReceiver.Receiver
{
	private final ResponseReceiver requestReceiver;
	private final List<Long> requestList;
	private final WeakReference<RequestResponse> requestResponseRef;
	private final ResponseStatusManager responseStatusManager;

	public interface RequestResponse
	{
		void onRequestSuccess( long requestId );

		void onRequestError( long requestId, int resultCode, String resultMsg );

		void onRequestFinished( long requestId );
	}

	public ResponseReceiverService( Context context, RequestResponse reqResp )
	{
		requestReceiver = new ResponseReceiver( context );
		requestList = new ArrayList<>();
		requestResponseRef = new WeakReference<>( reqResp );
		responseStatusManager = new ResponseStatusManager();
	}

	public void pause()
	{
		requestReceiver.setReceiver( null );
	}

	public void resume()
	{
		requestReceiver.setReceiver( this );
	}

	public boolean addRequest( long requestId )
	{
		return requestList.add( requestId );
	}

	public boolean removeRequest( long requestId )
	{
		return requestList.remove( requestId );
	}

	public boolean existsRequest(long requestId)
	{
		return requestList.contains( requestId );
	}

	@Override
	public void onRequestSuccess( long requestId, NetworkResponse networkResponse )
	{
		if ( existsRequest( requestId ) )
		{
			responseStatusManager.add( requestId, networkResponse );
			
			RequestResponse requestResponse = requestResponseRef.get();
			
			if ( null != requestResponse )
				requestResponse.onRequestSuccess( requestId );
		}
	}

	@Override
	public void onRequestError( long requestId, int resultCode, String resultMsg, NetworkResponse networkResponse )
	{
		if ( existsRequest( requestId ) )
		{
			responseStatusManager.add( requestId, networkResponse );
			
			RequestResponse requestResponse = requestResponseRef.get();
			
			if ( null != requestResponse )
				requestResponse.onRequestError( requestId, resultCode, resultMsg );
		}
	}

	@Override
	public void onRequestFinished( long requestId )
	{
		if ( existsRequest( requestId ) )
		{
			RequestResponse requestResponse = requestResponseRef.get();
			
			if ( null != requestResponse )
				requestResponse.onRequestFinished( requestId );

			removeRequest( requestId );
			
			responseStatusManager.remove( requestId );

			/* The broadcast was delivered, so we remove it from the queue. */
			RequestService.getInstance().getRequestDelayedBroadcast().removeBroadcast( requestId );
		}
	}

	public void receiveLostResponses()
	{
		for ( Long resp : requestList )
		{
			receiveLostResponse( resp );
		}
	}

	public boolean receiveLostResponse( long requestId )
	{
		Intent intent = RequestService.getInstance().getRequestDelayedBroadcast().getBroadcast( requestId );

		if ( null != intent )
		{
			long resultRequestId = intent.getLongExtra( RequestResponseProcessor.REQUEST_ID, 0 );
			int resultCode = intent.getIntExtra( RequestResponseProcessor.RESULT_CODE, 0 );
			String msg = intent.getStringExtra( RequestResponseProcessor.RESULT_MSG );
			NetworkResponse networkResponse = (NetworkResponse)intent.getSerializableExtra( RequestResponseProcessor.RESULT_NETWORK_RESPONSE );
			
			responseStatusManager.add( requestId, networkResponse );
			
			RequestResponse requestResponse = requestResponseRef.get();
			
			if ( null != requestResponse )
			{
				if ( resultCode == RequestResponseProcessor.REQUEST_RESPONSE_SUCCESS )
				{
					requestResponse.onRequestSuccess( resultRequestId );
				}
				else
				{
					requestResponse.onRequestError( resultRequestId, resultCode, msg );
				}
				
				requestResponse.onRequestFinished( resultRequestId );
			}
			
			responseStatusManager.remove( requestId );

			return true;
		}

		return false;
	}
	
	public ResponseStatusManager getResponseStatusManager()
	{
		return responseStatusManager;
	}
}
