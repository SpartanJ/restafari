package com.ensoft.restafari.database;

import android.content.ContentResolver;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.ensoft.restafari.database.annotations.DbForeignKey;
import com.ensoft.restafari.helper.ForeignKeyHelper;

import java.util.ArrayList;

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
		ArrayList<String> sqlForeignKeys = new ArrayList<>();

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

			boolean isPrimaryKey = column.getColumnName().equals( getColumnPK().getColumnName() );

			if ( column.hasIndex() || ( !column.getColumnName().equals( idName ) && isPrimaryKey ) )
			{
				String uniqueIndex = isPrimaryKey ? " UNIQUE" : ( column.getIndex().isUnique() ? " UNIQUE" : "" );

				sqlIndexes.add( "CREATE" + uniqueIndex + " INDEX " + getTableName() + "_" + column.getColumnName() + "_index ON " + getTableName() + " (" + column.getColumnName() + ");" );
			}
			
			if ( column.isForeignKey() )
			{
				DbForeignKey fk = column.getForeignKey();
				String[] fkPart = fk.value().split( "\\." );

				if ( fkPart.length >= 2 )
				{
					String fkSql = "FOREIGN KEY(" + column.getColumnName() + ") REFERENCES " + fkPart[0] + "(" + fkPart[1] + ")";
					
					fkSql += " ON UPDATE " + ForeignKeyHelper.fromAction( fk.onUpdate() );
					fkSql += " ON DELETE " + ForeignKeyHelper.fromAction( fk.onDelete() );
					
					sqlForeignKeys.add( fkSql );
				}
			}
			
			if ( i < columns.size() - 1 )
			{
				sql += ", ";
			}
			else
			{
				if ( sqlForeignKeys.size() == 0 )
				{
					sql += ");";
				}
				else
				{
					sql += ", ";
				}
			}
			
		}
		
		for ( int i = 0; i < sqlForeignKeys.size(); i++ )
		{
			sql += sqlForeignKeys.get(i);
			
			if ( i < sqlForeignKeys.size() - 1 )
			{
				sql += ", ";
			}
			else
			{
				sql += ");";
			}
		}

		db.execSQL( sql );

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
		return Uri.parse( "content://" + DatabaseProvider.getAuthority() + "/" + getTableName() );
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
