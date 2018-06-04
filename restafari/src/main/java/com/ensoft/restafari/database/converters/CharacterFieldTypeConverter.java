package com.ensoft.restafari.database.converters;

import android.content.ContentValues;

import com.ensoft.restafari.database.DatabaseDataType;

import java.lang.reflect.Type;

public class CharacterFieldTypeConverter extends FieldTypeConverter<String,Character>
{
	@Override
	public DatabaseDataType getDatabaseDataType()
	{
		return DatabaseDataType.TEXT;
	}
	
	@Override
	public boolean isModelType( Type type )
	{
		return super.isModelType( type ) || char.class == type;
	}
	
	@Override
	public Type getModelType()
	{
		return Character.class;
	}
	
	@Override
	public void toContentValues( ContentValues contentValues, String fieldName, Character character )
	{
		contentValues.put( fieldName, String.valueOf( character ) );
	}
	
	@Override
	public Character toModelType( String character )
	{
		return character.charAt( 0 );
	}
}
