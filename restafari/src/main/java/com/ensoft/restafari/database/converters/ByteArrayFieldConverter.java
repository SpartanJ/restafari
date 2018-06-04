package com.ensoft.restafari.database.converters;

import android.content.ContentValues;

import com.ensoft.restafari.database.DatabaseDataType;

import java.lang.reflect.Type;

public class ByteArrayFieldConverter extends FieldTypeConverter<Byte[],Byte[]>
{
	public static byte[] toPrimitive(final Byte[] array) {
		if (array == null) {
			return null;
		} else if (array.length == 0) {
			return new byte[0];
		}
		final byte[] result = new byte[array.length];
		for (int i = 0; i < array.length; i++) {
			result[i] = array[i];
		}
		return result;
	}
	
	public static Byte[] fromPrimitive(final byte[] array) {
		if ( array == null ) {
			return null;
		} else if ( array.length == 0 ) {
			return new Byte[0];
		}
		final Byte[] result = new Byte[array.length];
		for (int i = 0; i < array.length; i++) {
			result[i] = array[i];
		}
		return result;
	}
	
	@Override
	public DatabaseDataType getDatabaseDataType()
	{
		return DatabaseDataType.BLOB;
	}
	
	@Override
	public boolean isModelType( Type type )
	{
		return super.isModelType( type ) || byte[].class == type;
	}
	
	@Override
	public Type getModelType()
	{
		return Byte[].class;
	}
	
	@Override
	public void toContentValues( ContentValues contentValues, String fieldName, Byte[] bytes )
	{
		contentValues.put( fieldName, toPrimitive( bytes ) );
	}
	
	@Override
	public Byte[] toModelType( Byte[] bytes )
	{
		return bytes;
	}
}
