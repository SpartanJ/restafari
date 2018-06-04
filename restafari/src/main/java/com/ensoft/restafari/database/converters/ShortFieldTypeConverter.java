package com.ensoft.restafari.database.converters;

import android.content.ContentValues;

import com.ensoft.restafari.database.DatabaseDataType;

import java.lang.reflect.Type;

public class ShortFieldTypeConverter extends FieldTypeConverter<Short,Short>
{
	@Override
	public DatabaseDataType getDatabaseDataType()
	{
		return DatabaseDataType.SMALLINT;
	}
	
	@Override
	public boolean isModelType( Type type )
	{
		return super.isModelType( type ) || short.class == type;
	}
	
	@Override
	public Type getModelType()
	{
		return Short.class;
	}
	
	@Override
	public void toContentValues( ContentValues contentValues, String fieldName, Short val )
	{
		contentValues.put( fieldName, val );
	}
	
	@Override
	public Short toModelType( Short val )
	{
		return val;
	}
}
