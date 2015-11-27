package com.ensoft.restafari.database;

import android.content.res.Resources;

import java.util.ArrayList;

public class TableCollection extends ArrayList<DatabaseTable>
{
	private String dbName;
	private int dbVersion;

	public TableCollection(String dbName, int dbVersion)
	{
		this.dbName = dbName;
		this.dbVersion = dbVersion;
	}

	public DatabaseTable getTableFromUriMatch( int match )
	{
		for ( DatabaseTable table : this )
		{
			if ( table.getMatchIndex() == match || table.getMatchIndex() + 1 == match )
			{
				return table;
			}
		}

		throw new Resources.NotFoundException("No table found with match=" + match);
	}

	public String getDbName()
	{
		return dbName;
	}

	public int getDbVersion()
	{
		return dbVersion;
	}
}
