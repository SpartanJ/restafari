package com.ensoft.restafari.database.converters;

import android.content.ContentValues;

import com.ensoft.restafari.database.DatabaseDataType;

import java.lang.reflect.Type;

public abstract class FieldTypeConverter<DbType, ModelType>
{
	public abstract DatabaseDataType getDatabaseDataType();
	
	public boolean isModelType( Type type )
	{
		return getModelType() == type;
	}
	
	public abstract Type getModelType();
	
	public abstract void toContentValues( ContentValues contentValues, String fieldName, ModelType modelType );
	
	public abstract ModelType toModelType( DbType dbType );
}
