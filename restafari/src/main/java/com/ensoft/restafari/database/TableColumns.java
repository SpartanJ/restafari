package com.ensoft.restafari.database;

import java.util.ArrayList;

public class TableColumns extends ArrayList<TableColumn>
{
	protected TableColumn primaryKey;

	public TableColumns add( String columnName, DatabaseDataType dataType, boolean indexed, boolean autoIncrement )
	{
		add( new TableColumn( columnName, dataType, indexed, autoIncrement ) );

		return this;
	}

	public TableColumns add( String columnName, DatabaseDataType dataType, boolean indexed )
	{
		add( columnName, dataType, indexed, false );

		return this;
	}

	public TableColumns add( String columnName, DatabaseDataType dataType )
	{
		return add( columnName, dataType, false );
	}

	public TableColumns addPrimaryKey( String columnName, DatabaseDataType dataType, boolean autoIncrement )
	{
		primaryKey = new TableColumn( columnName, dataType, false, autoIncrement );

		add( primaryKey );

		return this;
	}

	public TableColumn getPrimaryKey()
	{
		return primaryKey;
	}

	public String[] getAll()
	{
		String[] res = new String[ size() ];
		int i = 0;

		for ( TableColumn column : this )
		{
			res[i] = column.getColumnName();
			i++;
		}

		return res;
	}
}
