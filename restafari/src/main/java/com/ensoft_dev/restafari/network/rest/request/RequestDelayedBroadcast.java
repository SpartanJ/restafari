package com.ensoft_dev.restafari.network.rest.request;

import android.content.Intent;

import com.ensoft_dev.restafari.network.rest.response.RequestResponseProcessor;

import java.util.HashMap;

public class RequestDelayedBroadcast
{
	protected HashMap<Long, Intent> mIntentsMap = new HashMap<>();

	public void queueBroadcast( Intent intent )
	{
		long requestId = intent.getLongExtra( RequestResponseProcessor.REQUEST_ID, 0 );
		mIntentsMap.put( requestId, intent );
	}

	/** Returns true if was not removed */
	public boolean removeBroadcast( long requestId )
	{
		return null != mIntentsMap.remove( requestId );
	}

	/** Returns the intent of the broadcast and also remove it from the queue */
	public Intent getBroadcast( long requestId )
	{
		Intent intent = null;

		if ( mIntentsMap.containsKey( requestId ) )
		{
			intent = mIntentsMap.get( requestId );
			mIntentsMap.remove( requestId );
		}

		return intent;
	}
}
