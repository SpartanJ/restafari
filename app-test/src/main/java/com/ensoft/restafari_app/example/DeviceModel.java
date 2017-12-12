package com.ensoft.restafari_app.example;

import com.ensoft.restafari.database.DatabaseModel;
import com.ensoft.restafari.database.annotations.DbCompositeIndex;
import com.ensoft.restafari.database.annotations.DbField;
import com.ensoft.restafari.database.annotations.DbIndex;
import com.google.gson.annotations.SerializedName;

@DbCompositeIndex( value = { "language", "os" } )
public class DeviceModel extends DatabaseModel
{
	@SerializedName( "id" )
	@DbField
	@DbIndex( isUnique = true )
	public String id;
	
	@SerializedName( "language" )
	@DbField
	public String language;
	
	@SerializedName( "os" )
	@DbField
	public String os;
	
	@SerializedName( "timezone" )
	@DbField
	public int timezone;
}
