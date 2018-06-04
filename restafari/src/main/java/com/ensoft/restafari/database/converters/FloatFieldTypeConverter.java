package com.ensoft.restafari.database.converters;

import android.content.ContentValues;

import com.ensoft.restafari.database.DatabaseDataType;

import java.lang.reflect.Type;

public class FloatFieldTypeConverter extends FieldTypeConverter<Float,Float>
{
	@Override
	public DatabaseDataType getDatabaseDataType()
	{
		return DatabaseDataType.REAL;
	}
	
	@Override
	public boolean isModelType( Type type )
	{
		return super.isModelType( type ) || float.class == type;
	}
	
	@Override
	public Type getModelType()
	{
		return Float.class;
	}
	
	@Override
	public void toContentValues( ContentValues contentValues, String fieldName, Float val )
	{
		contentValues.put( fieldName, val );
	}
	
	@Override
	public Float toModelType( Float val )
	{
		return val;
	}
}
