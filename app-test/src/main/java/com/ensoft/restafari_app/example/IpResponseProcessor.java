package com.ensoft.restafari_app.example;

import android.content.Context;
import android.provider.Settings;

import com.ensoft.restafari.network.processor.ResponseProcessor;
import com.ensoft.restafari.network.rest.request.RequestConfiguration;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

// The response processor, it runs in the background, this should be separated from the UI, this stores the result
public class IpResponseProcessor extends ResponseProcessor<IpModel>
{
	@Override
	public void handleResponse( Context context, RequestConfiguration request, IpModel response )
	{
		DeviceTable deviceTable = new DeviceTable();
		DeviceModel deviceModel;
		String deviceId = Settings.Secure.getString( context.getContentResolver(), Settings.Secure.ANDROID_ID );
		
		if ( deviceTable.isEmpty() )
		{
			deviceModel = new DeviceModel();
			deviceModel.id = deviceId;
			deviceModel.language = Locale.getDefault().getLanguage();
			deviceModel.os = "Android";
			deviceModel.timezone = TimeZone.getDefault().getRawOffset() / 1000;
			deviceModel.save();
		}
		else
		{
			deviceModel = deviceTable.getDeviceId( deviceId );
		}
		
		response.deviceId = deviceModel.id;
		response.timestamp = System.currentTimeMillis() / 1000L;
		response.timestampStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format( new Date( response.timestamp * 1000L ) );

		response.save();
	}
}
