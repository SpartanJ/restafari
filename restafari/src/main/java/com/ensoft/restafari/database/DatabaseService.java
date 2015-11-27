package com.ensoft.restafari.database;

import android.content.ContentResolver;
import android.content.Context;

public class DatabaseService
{
	private static DatabaseService instance;
	protected Context context;
	protected TableCollection tables;

	public static DatabaseService init( Context context, TableCollection tables )
	{
		instance = new DatabaseService( context, tables );
		return instance;
	}

	public static DatabaseService getInstance()
	{
		return instance;
	}

	private DatabaseService( Context context, TableCollection tables )
	{
		this.context = context;
		this.tables = tables;
	}

	public TableCollection getTables()
	{
		return tables;
	}

	public ContentResolver getDatabaseResolver()
	{
		return context.getContentResolver();
	}

	public Context getContext()
	{
		return context;
	}
}
