package com.ensoft.restafari.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.util.Log;

import com.ensoft.restafari.database.annotations.DbField;
import com.ensoft.restafari.database.annotations.DbForeignKey;
import com.ensoft.restafari.database.annotations.DbIndex;
import com.ensoft.restafari.database.annotations.DbPrimaryKey;
import com.ensoft.restafari.database.converters.ByteArrayFieldConverter;
import com.ensoft.restafari.database.converters.FieldTypeConverter;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

public class DatabaseModel
{
	public static final String TAG = DatabaseModel.class.getSimpleName();
	private static HashMap<String, Field[]> dbModelFields = new HashMap<>();
	private static HashMap<String, Field> dbModelPkField = new HashMap<>();
	private static HashMap<String, String> dbModelPkFieldName = new HashMap<>();
	private static HashMap<String, TableColumns> dbModelTableColumns = new HashMap<>();

	public static TableColumns getCachedTableColumns( String className )
	{
		return dbModelTableColumns.get( className );
	}

	@SerializedName( BaseColumns._ID )
	@DbField
	protected int _localId;

	public DatabaseModel()
	{
		getDbFields();
	}

	public int getLocalId()
	{
		return _localId;
	}
	
	public void setLocalId( int id )
	{
		_localId = id;
	}
	
	@SuppressWarnings("unchecked")
	public DatabaseModel fromCursor( Cursor cursor )
	{
		if ( cursor.getPosition() == -1 )
			return this;

		Field[] loadedFields = dbModelFields.get( getClass().getCanonicalName() );

		if ( null != loadedFields && loadedFields.length > 0 )
		{
			for ( Field field : loadedFields )
			{
				field.setAccessible( true );

				int index = cursor.getColumnIndex( field.getAnnotation( SerializedName.class ).value() );

				if ( -1 != index )
				{
					try
					{
						FieldTypeConverter fieldTypeConverter = FieldTypeConverterService.getInstance().get( field.getType() );
						
						if ( DatabaseDataType.INTEGER == fieldTypeConverter.getDatabaseDataType() )
							field.set( this, fieldTypeConverter.toModelType( cursor.getInt( index ) ) );
						else if ( DatabaseDataType.BIGINT == fieldTypeConverter.getDatabaseDataType() )
							field.set( this, fieldTypeConverter.toModelType( cursor.getLong( index ) ) );
						else if ( DatabaseDataType.TEXT == fieldTypeConverter.getDatabaseDataType() )
							field.set( this, fieldTypeConverter.toModelType( cursor.getString( index ) ) );
						else if ( DatabaseDataType.SMALLINT == fieldTypeConverter.getDatabaseDataType() )
							field.set( this, fieldTypeConverter.toModelType( cursor.getShort( index ) ) );
						else if ( DatabaseDataType.REAL == fieldTypeConverter.getDatabaseDataType() || DatabaseDataType.FLOAT == fieldTypeConverter.getDatabaseDataType() )
							field.set( this, fieldTypeConverter.toModelType( cursor.getFloat( index ) ) );
						else if ( DatabaseDataType.DOUBLE == fieldTypeConverter.getDatabaseDataType() )
							field.set( this, fieldTypeConverter.toModelType( cursor.getDouble( index ) ) );
						else if ( DatabaseDataType.BOOLEAN == fieldTypeConverter.getDatabaseDataType() )
							field.set( this, fieldTypeConverter.toModelType( cursor.getInt( index ) ) );
						else if ( DatabaseDataType.BLOB == fieldTypeConverter.getDatabaseDataType() )
							field.set( this, fieldTypeConverter.toModelType( ByteArrayFieldConverter.fromPrimitive( cursor.getBlob( index ) ) ) );
					}
					catch ( Exception e )
					{
						Log.e( TAG, e.getMessage() );
					}
				}
			}
		}
		
		return this;
	}

	private Field[] getClassFields()
	{
		return getClassFields( getClass().getCanonicalName() );
	}

	private Field[] getClassFields( String className )
	{
		return dbModelFields.get( className );
	}

	private void getDbFields()
	{
		String className = getClass().getCanonicalName();
		Field[] loadedFields = dbModelFields.get( className );

		if ( null == loadedFields || loadedFields.length == 0 )
		{
			ArrayList<Field> arrFields = new ArrayList<>();

			Field[] fields = getClass().getDeclaredFields();

			for ( Field field : fields )
			{
				field.setAccessible( true );

				if ( field.isAnnotationPresent( SerializedName.class ) && field.isAnnotationPresent( DbField.class ) )
				{
					arrFields.add( field );
				}
			}

			Class superClass = getClass().getSuperclass();

			while ( superClass != null )
			{
				Field[] superClassFields = superClass.getDeclaredFields();

				if ( superClassFields != null && superClassFields.length > 0 )
				{
					for ( Field field : superClassFields )
					{
						field.setAccessible( true );

						if ( field.isAnnotationPresent( SerializedName.class ) && field.isAnnotationPresent( DbField.class ) )
						{
							arrFields.add( field );
						}
					}
				}

				superClass = superClass.getSuperclass();
			}

			if ( arrFields.size() > 0 )
			{
				Field[] dbFields = new Field[arrFields.size()];

				dbFields = arrFields.toArray( dbFields );

				dbModelFields.put( className, dbFields );
			}
			else
			{
				dbModelFields.put( className, new Field[0] );
			}
		}
	}

	private Field getPrimaryKeyField()
	{
		String className = getClass().getCanonicalName();
		Field loadedField = dbModelPkField.get( className );

		if ( null == loadedField )
		{
			Field[] fields = getClassFields( className );
			Field localIdField = null;

			if ( null != fields && fields.length > 0 )
			{
				for ( Field field : fields )
				{
					field.setAccessible( true );

					if ( field.isAnnotationPresent( DbPrimaryKey.class ) )
					{
						dbModelPkField.put( className, field );
						dbModelPkFieldName.put( className, field.getAnnotation( SerializedName.class ).value() );

						return field;
					}
					
					if ( field.getAnnotation( SerializedName.class ).value().equals( BaseColumns._ID ) )
					{
						localIdField = field;
					}
				}
				
				dbModelPkField.put( className, localIdField );
				dbModelPkFieldName.put( className, BaseColumns._ID );
				
				return localIdField;
			}
		}

		return loadedField;
	}
	
	public Long getPrimaryKeyValue()
	{
		try
		{
			Field pkField = getPrimaryKeyField();

			if ( null != pkField )
			{
				Object value = pkField.get( this );

				if ( value instanceof Long )
				{
					return (Long)value;
				}
				else if ( value instanceof Integer )
				{
					return Long.valueOf( (Integer)value );
				}
				else if ( value instanceof Short )
				{
					return Long.valueOf( (Short)value );
				}
				else
				{
					Log.e( TAG, "Primary key must be an integer value" );
				}
			}
			else
			{
				Log.e( TAG, "No primary key field declared in " + TAG );
			}

			return 0L;
		}
		catch( Exception e )
		{
			Log.e( TAG, e.getMessage() );
			throw new NullPointerException("content values failed to build");
		}
	}

	public String getPrimaryKeyName()
	{
		String pkName = dbModelPkFieldName.get( getClass().getCanonicalName() );
		
		if ( null == pkName )
		{
			Field pkField = getPrimaryKeyField();
			
			if ( null != pkField )
			{
				return pkField.getAnnotation( SerializedName.class ).value();
			}
		}
		
		return pkName;
	}
	
	@SuppressWarnings("unchecked")
	public ContentValues toContentValues()
	{
		ContentValues values = new ContentValues();

		Field[] fields = getClassFields();

		if ( null != fields && fields.length > 0 )
		{
			for ( Field field : fields )
			{
				field.setAccessible( true );

				try
				{
					Object value = field.get( this );

					if ( value != null )
					{
						String fieldName = field.getAnnotation( SerializedName.class ).value();
						
						FieldTypeConverter fieldTypeConverter = FieldTypeConverterService.getInstance().get( field.getType() );
						
						if ( null != fieldTypeConverter )
						{
							if ( !BaseColumns._ID.equals( fieldName ) )
								fieldTypeConverter.toContentValues( values, fieldName, value );
						}
						else
						{
							throw new IllegalArgumentException( "value could not be handled by field: " + value.toString() );
						}
					}
				}
				catch ( IllegalAccessException illegalAccess )
				{
					Log.e( TAG, illegalAccess.getMessage() );
				}
			}
		}

		return values;
	}

	public TableColumns getTableColumns()
	{
		String className = getClass().getCanonicalName();
		TableColumns cachedTableColumns = dbModelTableColumns.get( className );

		if ( null == cachedTableColumns || cachedTableColumns.size() == 0 )
		{
			Field[] fields = getClassFields();

			if ( null != fields && fields.length > 0 )
			{
				TableColumns tableColumns = new TableColumns();

				for ( Field field : fields )
				{
					try
					{
						field.setAccessible( true );

						String fieldName = field.getAnnotation( SerializedName.class ).value();
						DatabaseDataType dataType = DatabaseDataType.TEXT;
						FieldTypeConverter fieldTypeConverter = FieldTypeConverterService.getInstance().get( field.getType() );
						
						if ( null != fieldTypeConverter )
						{
							dataType = fieldTypeConverter.getDatabaseDataType();
						}

						if ( field.isAnnotationPresent( DbPrimaryKey.class ) )
						{
							tableColumns.addPrimaryKey( fieldName, dataType );
						}
						else
						{
							DbIndex index = field.isAnnotationPresent( DbIndex.class ) ? field.getAnnotation( DbIndex.class ) : null;
							
							if ( field.isAnnotationPresent( DbForeignKey.class ) )
							{
								DbForeignKey foreignKey = field.getAnnotation( DbForeignKey.class );
								
								tableColumns.add( fieldName, dataType, index, foreignKey );
							}
							else
							{
								tableColumns.add( fieldName, dataType, index );
							}
						}
					}
					catch ( Exception e )
					{
						Log.e( TAG, "Can't resolve field " + field.getName() );
					}
				}

				dbModelTableColumns.put( className, tableColumns );

				cachedTableColumns = tableColumns;
			}
		}

		return cachedTableColumns;
	}

	protected void insert()
	{
		new DatabaseTableModel<>( this ).insert( this );
	}

	protected int update()
	{
		return new DatabaseTableModel<>( this ).update( this );
	}

	public int delete()
	{
		return new DatabaseTableModel<>( this ).delete( this );
	}

	public void save()
	{
		if ( 0 == update() )
		{
			insert();
		}
	}
	
	public String toJson()
	{
		return new Gson().toJson( this );
	}
}
