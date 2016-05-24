package com.ensoft.restafari.database;

import android.content.ContentValues;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

import com.ensoft.restafari.helper.ReflectionHelper;

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

		return getDatabaseResolver().insert( getContentUri(), model.toContentValues() );
	}

	public void insert( T[] models )
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

	public int update( T model )
	{
		if ( null == model )
			return 0;

		return getDatabaseResolver().update( getRowContentUri( model.getPrimaryKeyValue() ), model.toContentValues(), null, null );
	}

	public void update( T[] models )
	{
		insert( models );
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

	public void insertOrUpdate( T model )
	{
		if ( null == model )
			return;

		if ( 0 == update( model ) )
		{
			insert( model );
		}
	}

	public void insertOrUpdate( T[] models )
	{
		if ( null == models || models.length == 0 )
			return;

		for ( T model : models )
		{
			if ( 0 == update( model ) )
			{
				insert( model );
			}
		}
	}

	public Cursor getFromId( long id )
	{
		return getDatabaseResolver().query( getRowContentUri( id ), tableColumns.getAll(), null, null, null );
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
