package com.ensoft.restafari_app.example;

import android.content.CursorLoader;

import com.ensoft.restafari.database.DatabaseTableModel;

public class IpTable extends DatabaseTableModel<IpModel>
{
	public IpTable()
	{
		super( IpModel.class );
	}

	public CursorLoader getIps()
	{
		return new CursorLoader( getContext(), getContentUri(), tableColumns.getAll(), null, null, "timestamp DESC" );
	}
}
