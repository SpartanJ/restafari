package com.ensoft.restafari_app.example;

import com.ensoft.restafari.database.DatabaseTableModel;

public class DeviceTable extends DatabaseTableModel<DeviceModel>
{
	public DeviceTable()
	{
		super( DeviceModel.class );
	}
	
	public DeviceModel getDeviceId( String deviceId )
	{
		return toModel( getAll( "id = ? LIMIT 1", new String[] { deviceId }, null ), true );
	}
}
