package com.ensoft.restafari.ui.view;

import android.os.Bundle;

import com.ensoft.restafari.network.service.ResponseReceiverService;

import androidx.fragment.app.Fragment;

public class RequestResponseSupportFragment extends Fragment implements ResponseReceiverService.RequestResponse
{
	protected ResponseReceiverService responseReceiverService;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate( savedInstanceState );
		createResponseReceiverService();
	}
	
	protected void createResponseReceiverService()
	{
		responseReceiverService = new ResponseReceiverService( getActivity().getApplicationContext(), this );
	}
	
	@Override
	public void onPause()
	{
		responseReceiverService.pause();
		super.onPause();
	}

	@Override
	public void onResume()
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
