package com.ensoft.restafari_app.example;

import com.ensoft.restafari.database.DatabaseModel;
import com.ensoft.restafari.database.annotations.DbCompositeIndex;
import com.ensoft.restafari.database.annotations.DbField;
import com.ensoft.restafari.database.annotations.DbIndex;
import com.ensoft.restafari.database.annotations.DbPrimaryKey;

@DbCompositeIndex( value = { "language", "os" } )
public class DeviceModel extends DatabaseModel
{
	@DbPrimaryKey
	public String id;
	
	@DbField
	public String language;
	
	@DbField
	public String os;
	
	@DbField
	public int timezone;
}
