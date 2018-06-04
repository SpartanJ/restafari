package com.ensoft.restafari.database.converters;


import android.content.ContentValues;

import com.ensoft.restafari.database.DatabaseDataType;

import java.lang.reflect.Type;

public class LongFieldTypeConverter extends FieldTypeConverter<Long,Long>
{
	@Override
	public DatabaseDataType getDatabaseDataType()
	{
		return DatabaseDataType.BIGINT;
	}
	
	@Override
	public boolean isModelType( Type type )
	{
		return super.isModelType( type ) || long.class == type;
	}
	
	@Override
	public Type getModelType()
	{
		return Long.class;
	}
	
	@Override
	public void toContentValues( ContentValues contentValues, String fieldName, Long val )
	{
		contentValues.put( fieldName, val );
	}
	
	@Override
	public Long toModelType( Long val )
	{
		return val;
	}
}
