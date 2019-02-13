package com.ensoft.restafari_app.example;

import com.ensoft.restafari.database.DatabaseModel;
import com.ensoft.restafari.database.annotations.DbCompositeIndex;
import com.ensoft.restafari.database.annotations.DbField;
import com.ensoft.restafari.database.annotations.DbIndex;

@DbCompositeIndex( value = { "language", "os" } )
public class DeviceModel extends DatabaseModel
{
	@DbField
	@DbIndex( isUnique = true )
	public String id;
	
	@DbField
	public String language;
	
	@DbField
	public String os;
	
	@DbField
	public int timezone;
}
