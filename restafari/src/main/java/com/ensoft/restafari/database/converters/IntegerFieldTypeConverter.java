package com.ensoft.restafari.database.converters;

import android.content.ContentValues;

import com.ensoft.restafari.database.DatabaseDataType;

import java.lang.reflect.Type;

public class IntegerFieldTypeConverter extends FieldTypeConverter<Integer,Integer>
{
	@Override
	public DatabaseDataType getDatabaseDataType()
	{
		return DatabaseDataType.INTEGER;
	}
	
	@Override
	public boolean isModelType( Type type )
	{
		return super.isModelType( type ) || int.class == type;
	}
	
	@Override
	public Type getModelType()
	{
		return Integer.class;
	}
	
	@Override
	public void toContentValues( ContentValues contentValues, String fieldName, Integer integer )
	{
		contentValues.put( fieldName, integer );
	}
	
	@Override
	public Integer toModelType( Integer integer )
	{
		return integer;
	}
}
