package com.ensoft.restafari.database.converters;

import android.content.ContentValues;

import com.ensoft.restafari.database.DatabaseDataType;
import com.google.gson.Gson;

import java.lang.reflect.Type;

public class ObjectFieldTypeConverter extends FieldTypeConverter<String,Object>
{
	@Override
	public DatabaseDataType getDatabaseDataType()
	{
		return DatabaseDataType.TEXT;
	}
	
	@Override
	public Type getModelType()
	{
		return Object.class;
	}
	
	@Override
	public void toContentValues( ContentValues contentValues, String fieldName, Object o )
	{
		contentValues.put( fieldName, new Gson().toJson( o ) );
	}
	
	@Override
	public Object toModelType( String s )
	{
		return new Gson().fromJson( s, getModelType() );
	}
}
