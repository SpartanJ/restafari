package com.ensoft.restafari.database;

public enum DatabaseDataType
{
	NULL("NULL"),
	INT("INT"),
	INTEGER("INTEGER"),
	TINYINT("TINYINT"),
	SMALLINT("SMALLINT"),
	MEDIUMINT("MEDIUMINT"),
	BIGINT("BIGINT"),
	UNSIGNED_BIG_INT("UNSIGNED BIG INT"),
	INT2("INT2"),
	INT8("INT8"),
	REAL("REAL"),
	DOUBLE("DOUBLE"),
	DOUBLE_PRECISION("DOUBLE PRECISION"),
	FLOAT("FLOAT"),
	BOOLEAN("BOOLEAN"),
	DATE("DATE"),
	DATETIME("DATETIME"),
	DECIMAL("DECIMAL"),
	TEXT("TEXT"),
	BLOB("BLOB"),
	VARCHAR("VARCHAR");

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
