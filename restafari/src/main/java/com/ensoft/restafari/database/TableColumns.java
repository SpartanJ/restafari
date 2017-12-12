package com.ensoft.restafari.database;

import android.provider.BaseColumns;

import com.ensoft.restafari.database.annotations.DbCompositeIndex;
import com.ensoft.restafari.database.annotations.DbForeignKey;
import com.ensoft.restafari.database.annotations.DbIndex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TableColumns extends ArrayList<TableColumn>
{
	protected TableColumn realPrimaryKey = new TableColumn( BaseColumns._ID, DatabaseDataType.INTEGER );
	protected TableColumn primaryKey;
	protected List<DbCompositeIndex> compositeIndices = new ArrayList<>(  );
	
	public TableColumns()
	{
	}
	
	public void addCompositeIndex( DbCompositeIndex compositeIndex )
	{
		compositeIndices.add( compositeIndex );
	}
	
	public TableColumns add( String columnName, DatabaseDataType dataType, DbIndex index, DbForeignKey foreignKey )
	{
		add( new TableColumn( columnName, dataType, index, foreignKey ) );
		
		return this;
	}
	
	public TableColumns add( String columnName, DatabaseDataType dataType, DbIndex index )
	{
		add( new TableColumn( columnName, dataType, index ) );

		return this;
	}

	public TableColumns add( String columnName, DatabaseDataType dataType )
	{
		return add( columnName, dataType, null );
	}

	public TableColumns addPrimaryKey( String columnName, DatabaseDataType dataType )
	{
		primaryKey = new TableColumn( columnName, dataType, null );

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
	
	public List<DbCompositeIndex> getCompositeIndices()
	{
		return compositeIndices;
	}
}
