package com.ensoft.restafari.database;

import android.content.ContentResolver;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

public abstract class DatabaseTable
{
	private int matchIndex;

	public abstract TableColumn getColumnPK();

	public abstract String getTableName();

	public abstract TableColumns getColumns();

	public void create( SQLiteDatabase db )
	{
		String sql = "CREATE TABLE IF NOT EXISTS " + getTableName() + " (";
		ArrayList<String> sqlIndexes = new ArrayList<>();

		TableColumns columns = getColumns();
		String idName = columns.getRealPrimaryKey().getColumnName();

		for ( int i = 0; i < columns.size(); i++ )
		{
			TableColumn column = columns.get( i );

			sql += column.getColumnName() + " " + column.getDataType();

			if ( column.getColumnName().equals( idName ) )
			{
				sql += " PRIMARY KEY AUTOINCREMENT";
			}

			if ( i < columns.size() - 1 )
			{
				sql += ", ";
			}
			else
			{
				sql += ");";
			}

			boolean isPrimaryKey = column.getColumnName().equals( getColumnPK().getColumnName() );

			if ( column.isIndexed() || ( !column.getColumnName().equals( idName ) && isPrimaryKey ) )
			{
				String uniqueIndex = isPrimaryKey ? " UNIQUE" : "";

				sqlIndexes.add( "CREATE" + uniqueIndex + " INDEX " + getTableName() + "_" + column.getColumnName() + "_index ON " + getTableName() + " (" + column.getColumnName() + ");" );
			}
		}

		db.execSQL(  sql );

		for ( String dbIndex : sqlIndexes )
		{
			db.execSQL( dbIndex );
		}
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

	public void setMatchIndex( int mMatchIndex )
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
