package com.ensoft.restafari.database;

public enum DatabaseDataType
{
	INTEGER("INTEGER"),
	TINYINT("TINYINT"),
	SMALLINT("SMALLINT"),
	BIGINT("BIGINT"),
	REAL("REAL"),
	DOUBLE("DOUBLE"),
	FLOAT("FLOAT"),
	BOOLEAN("BOOLEAN"),
	TEXT("TEXT"),
	BLOB("BLOB"),
	ANY("ANY");

	private final String text;

	DatabaseDataType(final String text)
	{
		this.text = text;
	}

	@Override
	public String toString()
	{
		return text;
	}
}
