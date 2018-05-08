package com.ensoft.restafari.network.rest.response;

import android.util.LongSparseArray;

public class ResponseStatusManager
{
	private final LongSparseArray<NetworkResponse> responses = new LongSparseArray<>();
	
	public NetworkResponse add( long requestId, NetworkResponse object )
	{
		responses.put( requestId, object );
		
		return object;
	}
	
	public NetworkResponse get( long requestId )
	{
		return get( requestId, true );
	}
	
	public NetworkResponse get( long requestId, boolean autoRemove )
	{
		NetworkResponse response = responses.get( requestId );
		
		if ( autoRemove )
			remove( requestId );
		
		return response;
	}
	
	public void remove( long requestId )
	{
		responses.remove( requestId );
	}
}
