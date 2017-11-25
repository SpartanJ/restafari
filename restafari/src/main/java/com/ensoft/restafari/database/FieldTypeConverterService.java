package com.ensoft.restafari.database;

import com.ensoft.restafari.database.converters.ByteArrayFieldConverter;
import com.ensoft.restafari.database.converters.BooleanFieldTypeConverter;
import com.ensoft.restafari.database.converters.ByteFieldTypeConverter;
import com.ensoft.restafari.database.converters.CharacterFieldTypeConverter;
import com.ensoft.restafari.database.converters.DoubleFieldTypeConverter;
import com.ensoft.restafari.database.converters.FieldTypeConverter;
import com.ensoft.restafari.database.converters.FloatFieldTypeConverter;
import com.ensoft.restafari.database.converters.IntegerFieldTypeConverter;
import com.ensoft.restafari.database.converters.LongFieldTypeConverter;
import com.ensoft.restafari.database.converters.ObjectFieldTypeConverter;
import com.ensoft.restafari.database.converters.ShortFieldTypeConverter;
import com.ensoft.restafari.database.converters.StringFieldTypeConverter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class FieldTypeConverterService
{
	private static FieldTypeConverterService instance;
	
	public static FieldTypeConverterService getInstance()
	{
		if ( null == instance )
			instance = new FieldTypeConverterService();
		
		return instance;
	}
	
	private List<FieldTypeConverter> fieldTypeConverters = new ArrayList<>();
	
	private FieldTypeConverterService()
	{
		add( new IntegerFieldTypeConverter() );
		add( new LongFieldTypeConverter() );
		add( new StringFieldTypeConverter() );
		add( new ShortFieldTypeConverter() );
		add( new FloatFieldTypeConverter() );
		add( new DoubleFieldTypeConverter() );
		add( new BooleanFieldTypeConverter() );
		add( new ByteFieldTypeConverter() );
		add( new CharacterFieldTypeConverter() );
		add( new ObjectFieldTypeConverter() );
		add( new ByteArrayFieldConverter() );
	}
	
	public void add( FieldTypeConverter fieldTypeConverter )
	{
		if ( !exists( fieldTypeConverter.getModelType() ) )
			fieldTypeConverters.add( fieldTypeConverter );
	}
	
	public boolean exists( Type type )
	{
		for ( FieldTypeConverter fieldTypeConverter : fieldTypeConverters )
		{
			if ( fieldTypeConverter.isModelType( type ) )
			{
				return true;
			}
		}
		
		return false;
	}
	
	public FieldTypeConverter get( Type type )
	{
		for ( FieldTypeConverter fieldTypeConverter : fieldTypeConverters )
		{
			if ( fieldTypeConverter.isModelType( type ) )
			{
				return fieldTypeConverter;
			}
		}
		
		return null;
	}
}
