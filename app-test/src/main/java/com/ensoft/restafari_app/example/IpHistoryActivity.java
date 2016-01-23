package com.ensoft.restafari_app.example;

import android.app.LoaderManager;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.ensoft.restafari_app.R;

import com.ensoft.restafari.network.rest.request.RequestConfiguration;
import com.ensoft.restafari.network.service.RequestService;
import com.ensoft.restafari.ui.view.RequestResponseActivity;

public class IpHistoryActivity extends RequestResponseActivity implements LoaderManager.LoaderCallbacks<Cursor>
{
	public static final int IPS_LOADER = 1;
	public static String responseIp = "";
	private long requestId;
	private SimpleCursorAdapter ipsAdapter;

	@Override
	protected void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_test );

		ListView ipListView = (ListView)findViewById( R.id.ipListView );

		ipsAdapter = new SimpleCursorAdapter(	this,
												android.R.layout.simple_list_item_2,
												null,
												new String[] { "ip", "timestampStr" },
												new int [] { android.R.id.text1, android.R.id.text2 },
												SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER
		);

		ipListView.setAdapter( ipsAdapter );

		// Creates the request and add it to the request queue
		requestId = RequestService.getInstance().addRequest( new RequestConfiguration( IpRequest.class, IpResponseProcessor.class, IpModel.class ) );

		getRequestReceiverService().addRequest( requestId );

		getLoaderManager().initLoader( IPS_LOADER, null, this );
	}

	@Override
	public void onRequestSuccess( long requestId )
	{
		if ( requestId == this.requestId )
		{
			Toast.makeText( IpHistoryActivity.this, "IP: " + responseIp, Toast.LENGTH_SHORT ).show();
		}
	}

	@Override
	public void onRequestError( long requestId, int resultCode, String resultMsg )
	{
		if ( requestId == this.requestId )
		{
			Toast.makeText( IpHistoryActivity.this, "Error: " + resultCode + " Msg: " + resultMsg, Toast.LENGTH_SHORT ).show();
		}
	}

	@Override
	public Loader<Cursor> onCreateLoader( int id, Bundle args )
	{
		if ( IPS_LOADER == id )
		{
			return new IpTable().getIps();
		}

		return null;
	}

	@Override
	public void onLoadFinished( Loader<Cursor> loader, Cursor data )
	{
		if ( loader.getId() == IPS_LOADER )
		{
			ipsAdapter.changeCursor( data );
		}
	}

	@Override
	public void onLoaderReset( Loader<Cursor> loader )
	{
	}
}
