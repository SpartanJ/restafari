package com.ensoft.restafari_app.example;

import android.app.Application;
import android.content.Context;

import com.ensoft.restafari.database.DatabaseService;
import com.ensoft.restafari.database.TableCollection;
import com.ensoft.restafari.database.converters.FieldTypeConverter;
import com.ensoft.restafari.database.converters.JsonFieldTypeConverter;
import com.ensoft.restafari.network.service.RequestService;

import java.util.ArrayList;
import java.util.List;

public class App extends Application
{
	@Override
	public void onCreate()
	{
		super.onCreate();

		RequestService.init( this );
	}

	@Override
	protected void attachBaseContext(Context base)
	{
		super.attachBaseContext( base );
		
		// Register the field type converter to support special types in the database
		// JsonFieldTypeConverter class converts any class into a Json string and saves it as a text field in the database
		List<FieldTypeConverter> fieldTypeConverters = new ArrayList<>(  );
		fieldTypeConverters.add( new JsonFieldTypeConverter<DeviceModel>( DeviceModel.class ) );

		TableCollection tableCollection = new TableCollection( "ipdb", 1, fieldTypeConverters );
		tableCollection.add( new DeviceTable() );
		tableCollection.add( new IpTable() );

		DatabaseService.init( this, tableCollection );
	}
}
