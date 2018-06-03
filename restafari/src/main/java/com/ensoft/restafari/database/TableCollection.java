package com.ensoft.restafari.database;

import android.content.res.Resources;

import com.ensoft.restafari.database.converters.FieldTypeConverter;

import java.util.ArrayList;
import java.util.List;

public class TableCollection extends ArrayList<DatabaseTable>
{
	private String dbName;
	private int dbVersion;
	
	public TableCollection( String dbName, int dbVersion, List<FieldTypeConverter> fieldTypeConverters )
	{
		this.dbName = dbName;
		this.dbVersion = dbVersion;
		
		if ( null != fieldTypeConverters && fieldTypeConverters.size() > 0 )
		{
			for ( FieldTypeConverter fieldTypeConverter : fieldTypeConverters )
				FieldTypeConverterService.getInstance().add( fieldTypeConverter );
		}
	}

	public TableCollection(String dbName, int dbVersion)
	{
		this( dbName, dbVersion, null );
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

		throw new Resources.NotFoundException("No table found with match = " + match);
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
