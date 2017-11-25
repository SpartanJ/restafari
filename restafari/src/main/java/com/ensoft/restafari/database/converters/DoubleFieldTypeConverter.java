package com.ensoft.restafari.database.converters;


import android.content.ContentValues;

import com.ensoft.restafari.database.DatabaseDataType;

import java.lang.reflect.Type;

public class DoubleFieldTypeConverter extends FieldTypeConverter<Double,Double>
{
	@Override
	public DatabaseDataType getDatabaseDataType()
	{
		return DatabaseDataType.DOUBLE;
	}
	
	@Override
	public boolean isModelType( Type type )
	{
		return super.isModelType( type ) || double.class == type;
	}
	
	@Override
	public Type getModelType()
	{
		return Double.class;
	}
	
	@Override
	public void toContentValues( ContentValues contentValues, String fieldName, Double val )
	{
		contentValues.put( fieldName, val );
	}
	
	@Override
	public Double toModelType( Double val )
	{
		return val;
	}
}
