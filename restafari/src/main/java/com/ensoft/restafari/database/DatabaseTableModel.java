package com.ensoft.restafari.database;

import android.content.ContentValues;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.Nullable;

import com.ensoft.restafari.helper.ReflectionHelper;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

public class DatabaseTableModel<T extends DatabaseModel> extends DatabaseTable
{
	protected Class<? extends DatabaseModel> clazz;
	protected TableColumns tableColumns = new TableColumns();

	public DatabaseTableModel( Class<? extends DatabaseModel> model )
	{
		clazz = model;

		TableColumns cachedTableColumns = T.getCachedTableColumns( clazz.getCanonicalName() );

		if ( null != cachedTableColumns )
		{
			tableColumns = cachedTableColumns;
		}
		else
		{
			DatabaseModel instancedModel = ReflectionHelper.createInstance( model );

			tableColumns = instancedModel.getTableColumns();
		}
	}

	public DatabaseTableModel( T model )
	{
		clazz = model.getClass();
		tableColumns = model.getTableColumns();
	}

	@Override
	public TableColumn getColumnPK()
	{
		return tableColumns.getPrimaryKey() == null ? tableColumns.getRealPrimaryKey() : tableColumns.getPrimaryKey();
	}

	@Override
	public TableColumns getColumns()
	{
		return tableColumns;
	}

	public String getTableName()
	{
		return clazz.getSimpleName();
	}

	public Uri insert( T model )
	{
		if ( null == model )
			return null;

		Uri uri = getDatabaseResolver().insert( getContentUri(), model.toContentValues() );
		
		if ( null != uri && BaseColumns._ID.equals( model.getPrimaryKeyName() ) )
		{
			String id = uri.getLastPathSegment();
			
			if ( null != id && !id.isEmpty() )
			{
				try
				{
					model.setLocalId( Integer.valueOf( id ) );
				}
				catch ( Exception e )
				{}
			}
		}
		
		return uri;
	}

	public void insert( T[] models )
	{
		if ( null == models || models.length == 0 )
			return;

		for ( T model : models )
		{
			insert( model );
		}
	}

	public int update( T model )
	{
		if ( null == model )
			return 0;

		return getDatabaseResolver().update( getRowContentUri( model.getPrimaryKeyValue() ), model.toContentValues(), null, null );
	}

	public void update( T[] models )
	{
		if ( null == models || models.length == 0 )
			return;

		for ( T model : models )
		{
			update( model );
		}
	}

	public int delete( T model )
	{
		if ( null == model )
			return 0;

		return getDatabaseResolver().delete( getRowContentUri( model.getPrimaryKeyValue() ), null, null );
	}

	public void delete( T[] models )
	{
		if ( null == models || models.length == 0 )
			return;

		for ( T model : models )
		{
			delete( model );
		}
	}
	
	public void save( T model )
	{
		insertOrUpdate( model );
	}

	public void insertOrUpdate( T model )
	{
		if ( null == model )
			return;

		if ( 0 == update( model ) )
		{
			insert( model );
		}
	}
	
	public void save( T[] models )
	{
		insertOrUpdate( models );
	}

	public void insertOrUpdate( T[] models )
	{
		if ( null == models || models.length == 0 )
			return;

		ContentValues[] contentValues = new ContentValues[ models.length ];

		for ( int i = 0; i < models.length; i++ )
		{
			contentValues[i] = models[i].toContentValues();
		}

		getDatabaseResolver().bulkInsert( getContentUri(), contentValues );
	}
	
	public Cursor getFromId( long id )
	{
		return getDatabaseResolver().query( getRowContentUri( id ), tableColumns.getAll(), null, null, null );
	}
	
	public T getModelFromId( long id )
	{
		T model = null;
		
		Cursor cursor = getFromId( id );
		
		if ( null != cursor )
		{
			model = toModel( cursor );
			
			cursor.close();
		}
		
		return model;
	}
	
	public T toModel( Cursor cursor )
	{
		return toModel( cursor, false );
	}
	
	public T toModel( Cursor cursor, boolean closeCursor )
	{
		T model = null;
		
		if ( null != cursor )
		{
			if ( cursor.getPosition() < 0 || cursor.getPosition() >= cursor.getCount() )
				cursor.moveToFirst();
			
			model = ReflectionHelper.createInstance( clazz );
			model.fromCursor( cursor );
			
			if ( closeCursor )
				cursor.close();
		}
		
		return model;
	}
	
	public T[] toArray( Cursor cursor )
	{
		return toArray( cursor, false );
	}
	
	@SuppressWarnings("unchecked")
	public T[] toArray( Cursor cursor, boolean closeCursor )
	{
		if ( null != cursor && cursor.moveToFirst() )
		{
			T[] models = (T[]) Array.newInstance( clazz, cursor.getCount() );
			int pos = 0;
			
			do
			{
				models[pos] = toModel( cursor );
				
				pos++;
			} while ( cursor.moveToNext() );
			
			if ( closeCursor )
				cursor.close();
			
			return models;
		}
		
		return null;
	}
	
	public List<T> toList( Cursor cursor )
	{
		return Arrays.asList( toArray( cursor ) );
	}
	
	public Cursor getAll( @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder )
	{
		return getDatabaseResolver().query( getContentUri(), tableColumns.getAll(), selection, selectionArgs, sortOrder );
	}
	
	public T[] getAllToModelArray( @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder )
	{
		Cursor cursor = getAll( selection, selectionArgs, sortOrder );
		
		if ( null != cursor && !cursor.isClosed() )
		{
			T[] res = toArray( cursor );
			
			cursor.close();
			
			return res;
		}
		
		return null;
	}
	
	public List<T> getAllToModelList( @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder )
	{
		T[] res = getAllToModelArray( selection, selectionArgs, sortOrder );
		
		if ( null != res )
		{
			return Arrays.asList( res );
		}
		
		return null;
	}
	
	public CursorLoader getAllLoader( @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder )
	{
		return new CursorLoader( getContext(), getContentUri(), tableColumns.getAll(), selection, selectionArgs, sortOrder );
	}
	
	public CursorLoader getLoaderFromId( long id )
	{
		return new CursorLoader( getContext(), getRowContentUri( id ), tableColumns.getAll(), null, null, null );
	}

	public boolean isEmpty()
	{
		Cursor cursor = getDatabaseResolver().query( getContentUri(), new String[] { BaseColumns._ID }, "1 LIMIT 1", null, null );

		boolean isEmpty = true;

		if ( null != cursor )
		{
			isEmpty = !cursor.moveToFirst();

			cursor.close();
		}

		return isEmpty;
	}

	public int getCount()
	{
		int count = 0;

		Cursor cursor = getDatabaseResolver().query( getContentUri(), new String[] { "count(*) as count" }, null, null, null );

		if ( null != cursor )
		{
			if ( cursor.moveToFirst() )
			{
				count = cursor.getInt( 0 );
			}

			cursor.close();
		}

		return count;
	}
}
