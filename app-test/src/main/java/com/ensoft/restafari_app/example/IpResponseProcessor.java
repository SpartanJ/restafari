package com.ensoft.restafari_app.example;

import android.content.Context;

import com.ensoft.restafari.network.processor.ResponseProcessor;
import com.ensoft.restafari.network.rest.request.RequestConfiguration;

import java.text.SimpleDateFormat;
import java.util.Date;

// The response processor, it runs in the background, this should be separated from the UI, this stores the result
public class IpResponseProcessor extends ResponseProcessor<IpModel>
{
	@Override
	public void handleResponse( Context context, RequestConfiguration request, IpModel response )
	{
		IpHistoryActivity.responseIp = response.ip;

		response.timestamp = System.currentTimeMillis() / 1000L;
		response.timestampStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format( new Date( response.timestamp * 1000L ) );

		response.save();
	}
}
