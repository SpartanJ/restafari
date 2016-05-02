package com.ensoft.restafari.database;

public class TableColumn
{
	protected String columnName;
	protected DatabaseDataType dataType;
	protected boolean indexed;
	protected boolean autoIncrement;

	public TableColumn( String columnName, DatabaseDataType dataType, boolean indexed, boolean autoIncrement )
	{
		this.columnName = columnName;
		this.dataType = dataType;
		this.indexed = indexed;
		this.autoIncrement = autoIncrement;
	}

	public TableColumn( String columnName, DatabaseDataType dataType, boolean indexed )
	{
		this( columnName, dataType, indexed, false );
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

	public boolean isAutoIncrement()
	{
		return autoIncrement;
	}
}
