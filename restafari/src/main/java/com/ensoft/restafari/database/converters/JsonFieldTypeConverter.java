package com.ensoft.restafari.database.converters;

import android.content.ContentValues;
import android.text.TextUtils;

import com.ensoft.restafari.database.DatabaseDataType;
import com.google.gson.Gson;

import java.lang.reflect.Type;

public class JsonFieldTypeConverter<ModelType> extends FieldTypeConverter<String,ModelType>
{
	protected Type classType;
	
	public JsonFieldTypeConverter( Type modelType )
	{
		this.classType = modelType;
	}
	
	@Override
	public DatabaseDataType getDatabaseDataType()
	{
		return DatabaseDataType.TEXT;
	}
	
	@Override
	public Type getModelType()
	{
		return classType;
	}
	
	@Override
	public void toContentValues( ContentValues contentValues, String fieldName, ModelType o )
	{
		contentValues.put( fieldName, new Gson().toJson( o ) );
	}
	
	@Override
	public ModelType toModelType( String s )
	{
		if ( !TextUtils.isEmpty( s ) )
			return new Gson().fromJson( s, getModelType() );
		
		return null;
	}
}
