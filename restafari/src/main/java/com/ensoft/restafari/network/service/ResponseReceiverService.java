package com.ensoft.restafari.network.service;

import android.content.Context;
import android.content.Intent;

import com.ensoft.restafari.network.rest.response.RequestResponseProcessor;
import com.ensoft.restafari.network.rest.response.ResponseReceiver;

import java.util.ArrayList;
import java.util.List;

public class ResponseReceiverService implements ResponseReceiver.Receiver
{
	protected ResponseReceiver mRequestReceiver;
	protected List<Long> mRequestList;
	protected RequestResponse mRequestResponse;

	public interface RequestResponse
	{
		void onRequestSuccess( long requestId );

		void onRequestError( long requestId, int resultCode, String resultMsg );

		void onRequestFinished( long requestId );
	}

	public ResponseReceiverService( Context context, RequestResponse reqResp )
	{
		mRequestReceiver = new ResponseReceiver( context );
		mRequestList = new ArrayList<Long>();
		mRequestResponse = reqResp;
	}

	public void pause()
	{
		mRequestReceiver.setReceiver(null);
	}

	public void resume()
	{
		mRequestReceiver.setReceiver(this);
	}

	public boolean addRequest( long requestId )
	{
		return mRequestList.add( requestId );
	}

	public boolean removeRequest( long requestId )
	{
		return mRequestList.remove( requestId );
	}

	public boolean existsRequest(long requestId)
	{
		return mRequestList.contains( requestId );
	}

	@Override
	public void onRequestSuccess( long requestId )
	{
		if ( existsRequest( requestId ) )
			mRequestResponse.onRequestSuccess( requestId );
	}

	@Override
	public void onRequestError( long requestId, int resultCode, String resultMsg )
	{
		if ( existsRequest( requestId ) )
			mRequestResponse.onRequestError( requestId, resultCode, resultMsg );
	}

	@Override
	public void onRequestFinished( long requestId )
	{
		if ( existsRequest( requestId ) )
		{
			mRequestResponse.onRequestFinished( requestId );

			removeRequest( requestId );

			/* The broadcast was delivered, so we remove it from the queue. */
			RequestService.getInstance().getRequestDelayedBroadcast().removeBroadcast( requestId );
		}
	}

	public void receiveLostResponses()
	{
		for ( Long resp : mRequestList )
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
				mRequestResponse.onRequestSuccess( resultRequestId );
			}
			else
			{
				mRequestResponse.onRequestError( resultRequestId, resultCode, msg );
			}

			mRequestResponse.onRequestFinished( resultRequestId );

			return true;
		}

		return false;
	}
}
