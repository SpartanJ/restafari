package com.ensoft.restafari_app.example;

import com.ensoft.restafari.database.DatabaseModel;
import com.ensoft.restafari.database.annotations.DbAutoIncrement;
import com.ensoft.restafari.database.annotations.DbField;
import com.ensoft.restafari.database.annotations.DbPrimaryKey;
import com.google.gson.annotations.SerializedName;

public class IpModel extends DatabaseModel
{
	@SerializedName( "_id" )
	@DbField
	@DbPrimaryKey
	@DbAutoIncrement
	public int id = 0;

	@SerializedName( "ip" )
	@DbField
	public String ip;

	@SerializedName( "timestamp" )
	@DbField
	public long timestamp;

	@SerializedName( "timestampStr" )
	@DbField
	public String timestampStr;
}
