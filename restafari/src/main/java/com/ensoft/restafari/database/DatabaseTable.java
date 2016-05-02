package com.ensoft.restafari.database;

import android.content.ContentResolver;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import java.util.List;

public abstract class DatabaseTable
{
	private int matchIndex;

	public abstract TableColumn getColumnPK();

	public abstract String getTableName();

	protected abstract TableColumns getColumns();

	public void create( SQLiteDatabase db )
	{
		String sql = "CREATE TABLE IF NOT EXISTS " + getTableName() + " (";
		String sqlIndexes = "";

		List<TableColumn> columns = getColumns();
		String idName = getColumnPK().getColumnName();

		for ( int i = 0; i < columns.size(); i++ )
		{
			TableColumn column = columns.get( i );

			sql += column.getColumnName() + " " + ( column.isAutoIncrement() ? "INTEGER" : column.getDataType() );

			if ( column.getColumnName().equals( idName ) )
			{
				sql += " PRIMARY KEY";

				if ( column.isAutoIncrement() )
				{
					sql += " AUTOINCREMENT";
				}
			}

			if ( i < columns.size() - 1 )
			{
				sql += ", ";
			}
			else
			{
				sql += ");";
			}

			if ( column.isIndexed() )
			{
				sqlIndexes +=  "CREATE INDEX " + column.getColumnName() + "_index ON " + getTableName() + " (" + column.getColumnName() + ");";
			}
		}

		db.execSQL(  sql + sqlIndexes );
	}

	public void upgrade( SQLiteDatabase db, int oldVersion, int newVersion )
	{
	}

	public void addColumn( SQLiteDatabase db, TableColumn tableColumn )
	{
		db.execSQL( "ALTER TABLE " + getTableName() + " ADD COLUMN " + tableColumn.getColumnName() + " " + tableColumn.getDataType() );
	}

	public Uri getContentUri()
	{
		return Uri.parse( "content://" + DatabaseProvider.AUTHORITY + "/" + getTableName() );
	}

	public Uri getRowContentUri( String id )
	{
		return Uri.parse( getContentUri() + "/" + id );
	}

	public Uri getRowContentUri( long id )
	{
		return getRowContentUri( String.valueOf( id ) );
	}

	public int getMatchIndex()
	{
		return matchIndex;
	}

	public void setMatchIndex(int mMatchIndex)
	{
		this.matchIndex = mMatchIndex;
	}

	public ContentResolver getDatabaseResolver()
	{
		return DatabaseService.getInstance().getDatabaseResolver();
	}

	public Context getContext()
	{
		return DatabaseService.getInstance().getContext();
	}

	public void truncate()
	{
		getDatabaseResolver().delete( getContentUri(), null, null );
	}
}
