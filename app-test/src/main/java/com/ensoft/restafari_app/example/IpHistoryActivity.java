package com.ensoft.restafari_app.example;

import android.app.LoaderManager;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.ensoft.restafari.network.rest.request.RequestConfiguration;
import com.ensoft.restafari.network.service.RequestService;
import com.ensoft.restafari.ui.view.RequestResponseActivity;
import com.ensoft.restafari_app.R;

public class IpHistoryActivity extends RequestResponseActivity implements LoaderManager.LoaderCallbacks<Cursor>, SwipeRefreshLayout.OnRefreshListener
{
	public static final int IPS_LOADER = 1;
	private long requestId;
	private SimpleCursorAdapter ipsAdapter;
	private SwipeRefreshLayout swipeRefreshLayout;

	@Override
	protected void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_test );

		ipsAdapter = new SimpleCursorAdapter(	this,
												android.R.layout.simple_list_item_2,
												null,
												new String[] { "ip", "timestampStr" },
												new int [] { android.R.id.text1, android.R.id.text2 },
												SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER
		);
		
		((ListView)findViewById( R.id.ipListView )).setAdapter( ipsAdapter );
		
		swipeRefreshLayout = findViewById( R.id.swipRefresh );
		swipeRefreshLayout.setOnRefreshListener( this );
		
		getLoaderManager().initLoader( IPS_LOADER, null, this );
		
		createRequest();
	}
	
	protected void createRequest()
	{
		// Creates the request and add it to the request queue
		requestId = RequestService.getInstance().addRequest( new RequestConfiguration( IpRequest.class, IpResponseProcessor.class, IpModel.class ) );
		
		getRequestReceiverService().addRequest( requestId );
	}

	@Override
	public void onRequestSuccess( long requestId )
	{
		Toast.makeText( this, "New IP received", Toast.LENGTH_SHORT ).show();
	}

	@Override
	public void onRequestError( long requestId, int resultCode, String resultMsg )
	{
		Toast.makeText( this, "Error: " + resultCode + " Msg: " + resultMsg, Toast.LENGTH_SHORT ).show();
	}
	
	@Override
	public void onRequestFinished( long requestId )
	{
		swipeRefreshLayout.setRefreshing( false );
	}

	@Override
	public Loader<Cursor> onCreateLoader( int id, Bundle args )
	{
		return new IpTable().getIps();
	}

	@Override
	public void onLoadFinished( Loader<Cursor> loader, Cursor data )
	{
		ipsAdapter.changeCursor( data );
	}

	@Override
	public void onLoaderReset( Loader<Cursor> loader ) {}
	
	@Override
	public void onRefresh()
	{
		createRequest();
	}
}
