package com.ensoft_dev.restafari_app.example;

import android.content.Context;

import com.ensoft_dev.restafari.network.processor.ResponseProcessor;
import com.ensoft_dev.restafari.network.rest.request.RequestConfiguration;

// The response processor, it runs in the background, this should be separated from the UI, this stores the result
public class SimpleResponseProcessor extends ResponseProcessor<SimpleModel>
{

	@Override
	public void handleResponse( Context context, RequestConfiguration request, SimpleModel response )
	{
		TestActivity.responseIp = response.ip;
	}
}
