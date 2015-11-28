package com.ensoft_dev.restafari.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseOpenHelper extends SQLiteOpenHelper
{
	public final String TAG = getClass().getSimpleName();
	private final TableCollection tables;

	public DatabaseOpenHelper( Context context, TableCollection tables )
	{
		super( context, tables.getDbName(), null, tables.getDbVersion() );
		this.tables = tables;
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		Log.i( TAG, "DatabaseOpenHelper - onCreate started" );

		for ( DatabaseTable table : tables )
			table.create( db );

		Log.i(TAG, "DatabaseOpenHelper - onCreate finished");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		Log.i( TAG, "DatabaseOpenHelper - onCreate started" );

		for ( DatabaseTable table : tables )
			table.upgrade( db, oldVersion, newVersion );

		Log.i(TAG, "DatabaseOpenHelper - onCreate finished");
	}

	public SQLiteDatabase getRW()
	{
		return getWritableDatabase();
	}

	public SQLiteDatabase getRO()
	{
		return getReadableDatabase();
	}
}
