package com.ensoft.restafari.network.rest.request;

import android.os.AsyncTask;

import com.ensoft.restafari.network.service.RequestService;

public class AsyncRequestSender
{
	private AsyncRequestContent requestContent;
	
	public AsyncRequestSender( AsyncRequestContent requestContent )
	{
		this.requestContent = requestContent;
	}
	
	public AsyncRequestContent getRequestContent()
	{
		return requestContent;
	}
	
	protected static class AsyncRequestTask extends AsyncTask<AsyncRequestContent,Void,Long>
	{
		private AsyncRequestContent asyncRequestContent;
		
		@Override
		protected Long doInBackground( AsyncRequestContent... asyncRequestContents )
		{
			this.asyncRequestContent = asyncRequestContents[0];
			
			return RequestService.getInstance().addRequest(
				asyncRequestContent.getRequestConfiguration(),
				asyncRequestContent.getRequestParameters()
			);
		}
		
		@Override
		protected void onPostExecute( Long requestId )
		{
			if ( null != asyncRequestContent.getReadyListener() )
				asyncRequestContent.getReadyListener().onRequestSent( requestId );
		}
	}
	
	public void start()
	{
		new AsyncRequestTask().execute( requestContent );
	}
	
	public void start( AsyncRequestSentListener readyListener )
	{
		requestContent.setReadyListener( readyListener );
		
		start();
	}
}
