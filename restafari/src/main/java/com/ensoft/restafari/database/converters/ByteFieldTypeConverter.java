package com.ensoft.restafari.database.converters;

import android.content.ContentValues;

import com.ensoft.restafari.database.DatabaseDataType;

import java.lang.reflect.Type;

public class ByteFieldTypeConverter extends FieldTypeConverter<Byte,Byte>
{
	@Override
	public DatabaseDataType getDatabaseDataType()
	{
		return DatabaseDataType.TINYINT;
	}
	
	@Override
	public boolean isModelType( Type type )
	{
		return super.isModelType( type ) || byte.class == type;
	}
	
	@Override
	public Type getModelType()
	{
		return Byte.class;
	}
	
	@Override
	public void toContentValues( ContentValues contentValues, String fieldName, Byte aByte )
	{
		contentValues.put( fieldName, aByte );;
	}
	
	@Override
	public Byte toModelType( Byte aByte )
	{
		return aByte;
	}
}
