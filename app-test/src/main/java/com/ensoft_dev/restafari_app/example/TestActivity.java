package com.ensoft_dev.restafari_app.example;

import android.os.Bundle;
import android.widget.Toast;

import com.ensoft_dev.restafari_app.R;

import com.ensoft_dev.restafari.network.rest.request.RequestConfiguration;
import com.ensoft_dev.restafari.network.service.RequestService;
import com.ensoft_dev.restafari.ui.view.RequestResponseActivity;

public class TestActivity extends RequestResponseActivity
{
	public static String responseIp = "";
	private long requestId;

	@Override
	protected void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_test );

		RequestService.init( this );

		// Creates the request and add it to the request queue
		requestId = RequestService.getInstance().addRequest( new RequestConfiguration( SimpleRequest.class, SimpleResponseProcessor.class, SimpleModel.class ) );

		getRequestReceiverService().addRequest( requestId );
	}

	@Override
	public void onRequestSuccess( long requestId )
	{
		if ( requestId == this.requestId )
		{
			Toast.makeText( TestActivity.this, "IP: " + responseIp, Toast.LENGTH_SHORT ).show();
		}
	}

	@Override
	public void onRequestError( long requestId, int resultCode, String resultMsg )
	{
		if ( requestId == this.requestId )
		{
			Toast.makeText( TestActivity.this, "Error: " + resultCode + " Msg: " + resultMsg, Toast.LENGTH_SHORT ).show();
		}
	}
}
