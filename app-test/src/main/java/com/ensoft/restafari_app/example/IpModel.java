package com.ensoft.restafari_app.example;

import com.ensoft.restafari.database.DatabaseModel;
import com.ensoft.restafari.database.annotations.DbField;
import com.ensoft.restafari.database.annotations.DbForeignKey;
import com.google.gson.annotations.SerializedName;

public class IpModel extends DatabaseModel
{
	@SerializedName( "ip" )
	@DbField
	public String ip;
	
	@SerializedName( "device_id" )
	@DbField
	@DbForeignKey( "DeviceModel.id" )
	public String deviceId;

	@SerializedName( "timestamp" )
	@DbField
	public long timestamp;

	@SerializedName( "timestampStr" )
	@DbField
	public String timestampStr;
}
