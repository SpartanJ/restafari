package com.ensoft.restafari.database.converters;

import android.content.ContentValues;

import com.ensoft.restafari.database.DatabaseDataType;

import java.lang.reflect.Type;

public class StringFieldTypeConverter extends FieldTypeConverter<String,String>
{
	@Override
	public DatabaseDataType getDatabaseDataType()
	{
		return DatabaseDataType.TEXT;
	}
	
	@Override
	public Type getModelType()
	{
		return String.class;
	}
	
	@Override
	public void toContentValues( ContentValues contentValues, String fieldName, String s )
	{
		contentValues.put( fieldName, s );
	}
	
	@Override
	public String toModelType( String s )
	{
		return s;
	}
}
