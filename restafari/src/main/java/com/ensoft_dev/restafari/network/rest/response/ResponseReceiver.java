package com.ensoft_dev.restafari.network.rest.response;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

public class ResponseReceiver extends BroadcastReceiver
{
	public static final String TAG = ResponseReceiver.class.getCanonicalName();

	private Receiver receiver;
	private Context context;

	public ResponseReceiver( Context context )
	{
		this.context = context;
	}

	public interface Receiver
	{
		void onRequestSuccess( long requestId );

		void onRequestError( long requestId, int resultHttpCode, String resultMsg );

		void onRequestFinished( long requestId );
	}

	public void setReceiver( Receiver receiver )
	{
		IntentFilter filter = new IntentFilter( RequestResponseProcessor.REQUEST_RESULT );

		if (receiver == null)
			context.unregisterReceiver(this);
		else
			context.registerReceiver(this, filter);

		this.receiver = receiver;
	}

	@Override
	public void onReceive( Context context, Intent intent )
	{
		try
		{
			long resultRequestId = intent.getLongExtra( RequestResponseProcessor.REQUEST_ID, 0 );
			int resultCode = intent.getIntExtra( RequestResponseProcessor.RESULT_CODE, 0 );
			int responseCode = intent.getIntExtra( RequestResponseProcessor.RESPONSE_CODE, 0 );

			if ( receiver == null )
			{
				Log.e( TAG, "onReceive: Response lost, receiver was not registered for request id: " + resultRequestId + " with a response code: " + resultCode );
				return;
			}

			String msg = intent.getStringExtra( RequestResponseProcessor.RESULT_MSG );

			if ( resultCode == RequestResponseProcessor.REQUEST_RESPONSE_SUCCESS )
			{
				receiver.onRequestSuccess( resultRequestId );
			}
			else
			{
				receiver.onRequestError( resultRequestId, responseCode, msg );
			}

			receiver.onRequestFinished( resultRequestId );
		}
		catch ( NullPointerException e )
		{
		}
	}
}