package com.ensoft.restafari.network.rest.request;

import android.content.Intent;

import com.ensoft.restafari.network.rest.response.RequestResponseProcessor;

import java.util.HashMap;

public class RequestDelayedBroadcast
{
	protected HashMap<Long, Intent> intentsMap = new HashMap<>();

	public void queueBroadcast( Intent intent )
	{
		long requestId = intent.getLongExtra( RequestResponseProcessor.REQUEST_ID, 0 );
		intentsMap.put( requestId, intent );
	}

	/** Returns true if was not removed */
	public boolean removeBroadcast( long requestId )
	{
		return null != intentsMap.remove( requestId );
	}

	/** Returns the intent of the broadcast and also remove it from the queue */
	public Intent getBroadcast( long requestId )
	{
		Intent intent = null;

		if ( intentsMap.containsKey( requestId ) )
		{
			intent = intentsMap.get( requestId );
			intentsMap.remove( requestId );
		}

		return intent;
	}
}
