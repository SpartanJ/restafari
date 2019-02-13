package com.ensoft.restafari.network.rest.response;

import java.util.HashMap;

public class ResponseStatusManager
{
	private final HashMap<Long, NetworkResponse> responses = new HashMap<>();
	
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
