package com.ensoft.restafari.ui.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ensoft.restafari.network.service.ResponseReceiverService;

public class RequestResponseAppCompatActivity extends AppCompatActivity implements ResponseReceiverService.RequestResponse
{
	protected ResponseReceiverService responseReceiverService;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate( savedInstanceState );
		responseReceiverService = new ResponseReceiverService( this, this );
	}

	@Override
	protected void onPause()
	{
		responseReceiverService.pause();
		super.onPause();
	}

	@Override
	protected void onResume()
	{
		responseReceiverService.resume();
		responseReceiverService.receiveLostResponses();
		super.onResume();
	}

	protected ResponseReceiverService getRequestReceiverService()
	{
		return responseReceiverService;
	}

	@Override
	public void onRequestSuccess( long requestId ) {}

	@Override
	public void onRequestError( long requestId, int resultCode, String resultMsg ) {}

	@Override
	public void onRequestFinished( long requestId ) {}
}
