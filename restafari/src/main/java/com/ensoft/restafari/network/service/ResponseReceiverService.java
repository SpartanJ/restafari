package com.ensoft.restafari.network.service;

import android.content.Context;
import android.content.Intent;

import com.ensoft.restafari.network.rest.response.RequestResponseProcessor;
import com.ensoft.restafari.network.rest.response.ResponseReceiver;

import java.util.ArrayList;
import java.util.List;

public class ResponseReceiverService implements ResponseReceiver.Receiver
{
	protected ResponseReceiver requestReceiver;
	protected List<Long> requestList;
	protected RequestResponse requestResponse;

	public interface RequestResponse
	{
		void onRequestSuccess( long requestId );

		void onRequestError( long requestId, int resultCode, String resultMsg );

		void onRequestFinished( long requestId );
	}

	public ResponseReceiverService( Context context, RequestResponse reqResp )
	{
		requestReceiver = new ResponseReceiver( context );
		requestList = new ArrayList<Long>();
		requestResponse = reqResp;
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
	public void onRequestSuccess( long requestId )
	{
		if ( existsRequest( requestId ) )
			requestResponse.onRequestSuccess( requestId );
	}

	@Override
	public void onRequestError( long requestId, int resultCode, String resultMsg )
	{
		if ( existsRequest( requestId ) )
			requestResponse.onRequestError( requestId, resultCode, resultMsg );
	}

	@Override
	public void onRequestFinished( long requestId )
	{
		if ( existsRequest( requestId ) )
		{
			requestResponse.onRequestFinished( requestId );

			removeRequest( requestId );

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

			if ( resultCode == RequestResponseProcessor.REQUEST_RESPONSE_SUCCESS )
			{
				requestResponse.onRequestSuccess( resultRequestId );
			}
			else
			{
				requestResponse.onRequestError( resultRequestId, resultCode, msg );
			}

			requestResponse.onRequestFinished( resultRequestId );

			return true;
		}

		return false;
	}
}
