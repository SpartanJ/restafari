package com.ensoft.restafari.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

public class DatabaseProvider extends ContentProvider
{
	public static String AUTHORITY;
	protected TableCollection tables;
	protected final UriMatcher uriMatcher;
	protected DatabaseOpenHelper dbHelper;

	public DatabaseProvider()
	{
		this( DatabaseProvider.class.getCanonicalName() );
	}

	public DatabaseProvider( String authority )
	{
		super();

		AUTHORITY = authority;

		uriMatcher = new UriMatcher( UriMatcher.NO_MATCH );
	}

	@Override
	public boolean onCreate()
	{
		init();
		return true;
	}

	protected void init()
	{
		int matchIndex = 0;

		tables = DatabaseService.getInstance().getTables();

		for ( DatabaseTable table : tables )
		{
			table.setMatchIndex( matchIndex );

			uriMatcher.addURI( AUTHORITY, table.getTableName(), matchIndex++ );
			uriMatcher.addURI( AUTHORITY, table.getTableName() + "/#", matchIndex++ );
		}

		dbHelper = new DatabaseOpenHelper( getContext(), tables );
		dbHelper.getRW();
	}

	public DatabaseOpenHelper getDatabaseOpenHelper()
	{
		return dbHelper;
	}
	
	@Nullable
	@Override
	public Cursor query( @NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder )
	{
		int uriMatchIndex = uriMatcher.match( uri );

		if ( -1 != uriMatchIndex )
		{
			DatabaseTable table = tables.getTableFromUriMatch( uriMatchIndex );

			if ( isRowUri( uriMatchIndex ) )
			{
				selection = appendRowId( table, selection, uri.getLastPathSegment() );
			}

			Cursor cursor = dbHelper.getRW().query( table.getTableName(), projection, selection, selectionArgs, null, null, sortOrder );

			if ( null != getContext() && null != getContext().getContentResolver() )
				cursor.setNotificationUri( getContext().getContentResolver(), uri );

			return cursor;
		}
		else
		{
			Log.e( AUTHORITY, "query: No uri match for uri: " + uri.toString() );
		}

		return null;
	}

	@Nullable
	@Override
	public String getType( @NonNull Uri uri )
	{
		return null;
	}

	@Nullable
	@Override
	public Uri insert( @NonNull Uri uri, ContentValues values )
	{
		int uriMatchIndex = uriMatcher.match( uri );

		if ( -1 != uriMatchIndex )
		{
			if ( isRowUri( uriMatchIndex ) )
			{
				Log.e( AUTHORITY, "Unknown URI " + uri );
			}
			else
			{
				DatabaseTable table = tables.getTableFromUriMatch( uriMatchIndex );

				try
				{
					long id = dbHelper.getRW().insertOrThrow( table.getTableName(), null, values );

					Uri newUri = ContentUris.withAppendedId( table.getContentUri(), id );

					String idName = table.getColumnPK().getColumnName();

					if ( !idName.equals( BaseColumns._ID ) && values.containsKey( idName ) )
					{
						Long realId = values.getAsLong( idName );

						if ( null != realId )
						{
							newUri = ContentUris.withAppendedId( table.getContentUri(), realId );
						}
					}

					if ( null != getContext() && null != getContext().getContentResolver() )
						getContext().getContentResolver().notifyChange( newUri, null );

					return newUri;
				}
				catch ( SQLException sqlException )
				{
					Log.e( AUTHORITY, sqlException.getMessage() );
				}
			}
		}
		else
		{
			Log.e( AUTHORITY, "insert: No uri match for uri: " + uri.toString() );
		}

		return null;
	}

	@Override
	public int delete( @NonNull Uri uri, String selection, String[] selectionArgs )
	{
		int deletedRowsCount = 0;

		int uriMatch = uriMatcher.match( uri );

		if ( -1 != uriMatch )
		{
			DatabaseTable table = tables.getTableFromUriMatch( uriMatch );

			if ( isRowUri( uriMatch ) )
			{
				selection = appendRowId( table, selection, uri.getLastPathSegment() );
			}

			deletedRowsCount = dbHelper.getRW().delete( table.getTableName(), selection, selectionArgs );

			if ( null != getContext() && null != getContext().getContentResolver() )
				getContext().getContentResolver().notifyChange( uri, null );
		}
		else
		{
			Log.e( AUTHORITY, "delete: No uri match for uri: " + uri.toString() );
		}

		return deletedRowsCount;
	}

	@Override
	public int update( @NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs )
	{
		int affected = 0;
		int uriMatchIndex = uriMatcher.match( uri );

		if ( -1 != uriMatchIndex )
		{
			DatabaseTable table = tables.getTableFromUriMatch( uriMatchIndex );

			if ( isRowUri( uriMatchIndex ) )
			{
				selection = appendRowId( table, selection, uri.getLastPathSegment() );
			}

			affected = dbHelper.getRW().update( table.getTableName(), values, selection, selectionArgs );

			if ( null != getContext() && null != getContext().getContentResolver() )
				getContext().getContentResolver().notifyChange( uri, null );
		}
		else
		{
			Log.e( AUTHORITY, "update: No uri match for uri: " + uri.toString() );
		}

		return affected;
	}

	@Override
	public int bulkInsert( @NonNull Uri uri, @NonNull ContentValues[] allValues )
	{
		int numInserted = 0;

		String tableName = tables.getTableFromUriMatch( uriMatcher.match( uri ) ).getTableName();

		SQLiteDatabase db = dbHelper.getRW();

		db.beginTransaction();

		try
		{
			for ( ContentValues cv : allValues )
			{
				long newID = db.insertWithOnConflict( tableName, null, cv, SQLiteDatabase.CONFLICT_REPLACE );

				if ( -1 == newID )
					throw new SQLException( "Error to add: " + uri );
			}

			db.setTransactionSuccessful();

			if ( null != getContext() && null != getContext().getContentResolver() )
				getContext().getContentResolver().notifyChange( uri, null );

			numInserted = allValues.length;
		}
		finally
		{
			db.endTransaction();
		}

		return numInserted;
	}

	private boolean isRowUri(int uriMatch)
	{
		return uriMatch % 2 == 1;
	}

	private String appendRowId( DatabaseTable table, String selection, String id )
	{
		return table.getColumnPK().getColumnName() + " = '" + id + "'" + (!TextUtils.isEmpty( selection ) ? " AND (" + selection + ')' : "");
	}
}
