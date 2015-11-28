package com.ensoft_dev.restafari.database;

public class TableColumn
{
	protected String columnName;
	protected DatabaseDataType dataType;
	protected boolean indexed;

	public TableColumn( String columnName, DatabaseDataType dataType, boolean indexed )
	{
		this.columnName = columnName;
		this.dataType = dataType;
		this.indexed = indexed;
	}

	public TableColumn( String columnName, DatabaseDataType dataType )
	{
		this( columnName, dataType, false );
	}

	public String getColumnName()
	{
		return columnName;
	}

	public DatabaseDataType getDataType()
	{
		return dataType;
	}

	public boolean isIndexed()
	{
		return indexed;
	}
}
