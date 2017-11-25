package com.ensoft.restafari.database;

import android.provider.BaseColumns;

import java.util.ArrayList;

public class TableColumns extends ArrayList<TableColumn>
{
	protected TableColumn realPrimaryKey = new TableColumn( BaseColumns._ID, DatabaseDataType.INTEGER );
	protected TableColumn primaryKey;

	public TableColumns()
	{
	}
	
	public TableColumns add( String columnName, DatabaseDataType dataType, boolean indexed, String foreignKey )
	{
		add( new TableColumn( columnName, dataType, indexed, foreignKey ) );
		
		return this;
	}
	
	public TableColumns add( String columnName, DatabaseDataType dataType, boolean indexed )
	{
		add( new TableColumn( columnName, dataType, indexed ) );

		return this;
	}

	public TableColumns add( String columnName, DatabaseDataType dataType )
	{
		return add( columnName, dataType, false );
	}

	public TableColumns addPrimaryKey( String columnName, DatabaseDataType dataType )
	{
		primaryKey = new TableColumn( columnName, dataType, false );

		add( primaryKey );

		return this;
	}

	public TableColumn getPrimaryKey()
	{
		return primaryKey;
	}

	public TableColumn getRealPrimaryKey()
	{
		return realPrimaryKey;
	}
	
	public String[] getAll( String tableName )
	{
		String[] res = new String[ size() ];
		int i = 0;
		
		for ( TableColumn column : this )
		{
			res[i] = tableName + "." + column.getColumnName();
			i++;
		}
		
		return res;
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
