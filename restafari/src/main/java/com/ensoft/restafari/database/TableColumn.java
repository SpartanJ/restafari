package com.ensoft.restafari.database;

import com.ensoft.restafari.database.annotations.DbForeignKey;
import com.ensoft.restafari.database.annotations.DbIndex;

public class TableColumn
{
	protected String columnName;
	protected DatabaseDataType dataType;
	protected DbIndex index;
	protected DbForeignKey foreignKey;
	
	public TableColumn( String columnName, DatabaseDataType dataType, DbIndex index, DbForeignKey foreignKey )
	{
		this.columnName = columnName;
		this.dataType = dataType;
		this.index = index;
		this.foreignKey = foreignKey;
	}
	
	public TableColumn( String columnName, DatabaseDataType dataType, DbIndex index )
	{
		this.columnName = columnName;
		this.dataType = dataType;
		this.index = index;
	}

	public TableColumn( String columnName, DatabaseDataType dataType )
	{
		this( columnName, dataType, null );
	}

	public String getColumnName()
	{
		return columnName;
	}

	public DatabaseDataType getDataType()
	{
		return dataType;
	}

	public DbIndex getIndex()
	{
		return index;
	}
	
	public boolean hasIndex()
	{
		return null != index;
	}
	
	public boolean isForeignKey()
	{
		return null != foreignKey;
	}
	
	public DbForeignKey getForeignKey()
	{
		return foreignKey;
	}
}
