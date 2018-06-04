package com.ensoft.restafari.database.converters;

import android.content.ContentValues;

import com.ensoft.restafari.database.DatabaseDataType;

import java.lang.reflect.Type;

public class BooleanFieldTypeConverter extends FieldTypeConverter<Integer,Boolean>
{
	@Override
	public DatabaseDataType getDatabaseDataType()
	{
		return DatabaseDataType.BOOLEAN;
	}
	
	@Override
	public boolean isModelType( Type type )
	{
		return super.isModelType( type ) || boolean.class == type;
	}
	
	@Override
	public Type getModelType()
	{
		return Boolean.class;
	}
	
	@Override
	public void toContentValues( ContentValues contentValues, String fieldName, Boolean aBoolean )
	{
		String valString = aBoolean.toString();
		contentValues.put( fieldName, null != valString && !valString.isEmpty() ?
			( valString.toLowerCase().equals( "true" ) ? 1 : 0 ) :
			0 );
	}
	
	@Override
	public Boolean toModelType( Integer integer )
	{
		return 0 != integer;
	}
}
