package com.ensoft.restafari.database.converters;

import android.content.ContentValues;

import com.ensoft.restafari.database.DatabaseDataType;

import java.lang.reflect.Type;
import java.util.UUID;

public class UUIDFieldTypeConverter extends FieldTypeConverter<String, UUID>
{
	@Override
	public DatabaseDataType getDatabaseDataType()
	{
		return DatabaseDataType.TEXT;
	}
	
	@Override
	public Type getModelType()
	{
		return UUID.class;
	}
	
	@Override
	public void toContentValues( ContentValues contentValues, String fieldName, UUID uuid )
	{
		contentValues.put( fieldName, uuid.toString() );
	}
	
	@Override
	public UUID toModelType( String s )
	{
		return UUID.fromString( s );
	}
}
