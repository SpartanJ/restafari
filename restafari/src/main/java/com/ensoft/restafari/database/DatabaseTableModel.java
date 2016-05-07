package com.ensoft.restafari.database;

import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;

import com.ensoft.restafari.helper.ReflectionHelper;

public class DatabaseTableModel<T extends DatabaseModel> extends DatabaseTable
{
	protected Class<? extends DatabaseModel> clazz;
	protected TableColumns tableColumns = new TableColumns();

	public DatabaseTableModel( Class<? extends DatabaseModel> model )
	{
		clazz = model;

		DatabaseModel instancedModel = ReflectionHelper.createInstance( model );

		tableColumns = instancedModel.getTableColumns();
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
		return getDatabaseResolver().insert( getContentUri(), model.toContentValues() );
	}

	public void insert( T[] models )
	{
		for ( T model : models )
		{
			insert( model );
		}
	}

	public int update( T model )
	{
		return getDatabaseResolver().update( getRowContentUri( model.getPrimaryKeyValue() ), model.toContentValues(), null, null );
	}

	public void update( T[] models )
	{
		for ( T model : models )
		{
			update( model );
		}
	}

	public int delete( T model )
	{
		return getDatabaseResolver().delete( getRowContentUri( model.getPrimaryKeyValue() ), null, null );
	}

	public void delete( T[] models )
	{
		for ( T model : models )
		{
			delete( model );
		}
	}

	public void insertOrUpdate( T model )
	{
		if ( 0 == update( model ) )
		{
			insert( model );
		}
	}

	public void insertOrUpdate( T[] models )
	{
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
}
