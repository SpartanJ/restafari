package com.ensoft.restafari_app.example;

import com.ensoft.restafari.database.DatabaseModel;
import com.ensoft.restafari.database.annotations.DbField;
import com.ensoft.restafari.database.annotations.DbForeignKey;
import com.google.gson.annotations.SerializedName;

public class IpModel extends DatabaseModel
{
	@DbField
	public String ip;
	
	@SerializedName( "device_id" )
	@DbField
	@DbForeignKey( value = "DeviceModel.id", onDelete = DbForeignKey.CASCADE )
	public String deviceId;

	@DbField
	public long timestamp;

	@DbField
	public String timestampStr;
	
	@DbField
	public DeviceModel device;
}
