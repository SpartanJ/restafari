package com.ensoft.restafari.database;

public class TableColumn
{
	protected String columnName;
	protected DatabaseDataType dataType;
	protected boolean indexed;
	protected String foreignKey;
	
	public TableColumn( String columnName, DatabaseDataType dataType, boolean indexed, String foreignKey )
	{
		this.columnName = columnName;
		this.dataType = dataType;
		this.indexed = indexed;
		this.foreignKey = foreignKey;
	}
	
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
	
	public boolean isForeignKey()
	{
		return null != foreignKey && !foreignKey.isEmpty();
	}
	
	public String getForeignKey()
	{
		return foreignKey;
	}
}
