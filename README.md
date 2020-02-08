restafari
=========

**restafari** is an android library mostly oriented to facilitate REST API calls to a server and storing the server API response to a local SQLite database.

It's designed to be a robust library to handle all the cycle of an REST API call. It's not supposed to be a fast solution for small projects, it's intended to be used in large projects with many REST API endpoints. It works on top of Volley and Gson libraries and supports some not so very common features as cookies and proxied requests.


## Getting started

### Dependency

```
dependencies {
    implementation 'com.ensoft-dev.restafari:restafari:0.4.0'
}
```

## Learn by example

### Creating the application DatabaseProvider

The database data is provided by a special [ContentProvider](http://developer.android.com/guide/topics/providers/content-providers.html) supplied by the application called the `DatabaseProvider`. This will be the standard interface to communicate with the SQLite database. And for this we are going to create an extended `DatabaseProvider` from the one provided by the library. The reason for this is that we need to have a unique [authority](http://developer.android.com/guide/topics/manifest/provider-element.html#auth) for our `ContentProvider`. We could simply use the one provided by the library but it could conflict with other app using the same library, so we are going to avoid it by doing the things right from the beginning.

A simple `DatabaseProvider` will look always look something like:

```java
package com.ensoft.restafari_app.example;

public class DatabaseProvider extends com.ensoft.restafari.database.DatabaseProvider
{
	public DatabaseProvider()
	{
		super( DatabaseProvider.class.getCanonicalName() );
	}
}
```

Now we just need to add our provider to the `AndroidManifest.xml`.

```xml
		<provider
			android:name="com.ensoft.restafari_app.example.DatabaseProvider"
			android:authorities="com.ensoft.restafari_app.example.DatabaseProvider"
			android:exported="false"
			android:syncable="true"/>
```

This should be located inside the `application` element. `android:name` corresponds to the class path, and usually `android:authorities` should be the same ( it's not required by this is the normal way to do it ).


### Initializing the library

The `RequestService` class is class that will handle all the HTTP requests, and just needs a contexts to be initialized.

The `DatabaseService` only needs to be initialized in the case that the Database features are intended to be used. It requires a `TableCollection` with all the table objects that the application will use during the execution and must be initialized on the `attachBaseContext` call ( because it must be initialized before the `ContentProvider` ).

```java
package com.ensoft.restafari_app.example;

import android.app.Application;
import android.content.Context;

import com.ensoft.restafari.database.DatabaseService;
import com.ensoft.restafari.database.TableCollection;
import com.ensoft.restafari.network.service.RequestService;

public class App extends Application
{
	@Override
	public void onCreate()
	{
		super.onCreate();

		RequestService.init( this );
	}

	@Override
	protected void attachBaseContext(Context base)
	{
		super.attachBaseContext( base );

		TableCollection tableCollection = new TableCollection( "ipdb", 1 );
		tableCollection.add( new IpTable() );

		DatabaseService.init( this, tableCollection );
	}
}
```


### Creating the table database model and response model


The model must extend from the `DatabaseModel` class, this class will handle the representation of the model as a table in the database.

Every table field is indicated with the annotation `@DbField` and the database field name is specified with the `@SerializedName` annotation. If no name is specified the member name will be used.
The table primary key is specified with `@DbPrimaryKey`, it should be the id of the object provided by the server ( an int, long, string, UUID, etc ). In this case we don't have a primary key so that field is not declared.
`@SerializedName` is also used to convert the JSON object from the REST Api call response to a Java object.


```java
package com.ensoft.restafari_app.example;

import com.ensoft.restafari.database.DatabaseModel;
import com.ensoft.restafari.database.annotations.DbField;
import com.ensoft.restafari.database.annotations.DbPrimaryKey;
import com.google.gson.annotations.SerializedName;

public class IpModel extends DatabaseModel
{
	@SerializedName( "ip" )
	@DbField
	public String ip;

	@SerializedName( "timestamp" )
	@DbField
	public long timestamp;

	@SerializedName( "timestampStr" )
	@DbField
	public String timestampStr;
}
```


### Creating the table object to manage the database model


The table object is just a helper class to connect the model to the database.
Extends from `DatabaseTableModel` indicating the database model that represents ( in this case the `IpModel` ). It will handle the queries, inserts, updates and deletes of the object in the database table.

```java
package com.ensoft.restafari_app.example;

import android.content.CursorLoader;

import com.ensoft.restafari.database.DatabaseTableModel;

public class IpTable extends DatabaseTableModel<IpModel>
{
	public IpTable()
	{
		super( IpModel.class );
	}

	// Get the IPs from the database table!
	public CursorLoader getIps()
	{
		return new CursorLoader( getContext(), getContentUri(), tableColumns.getAll(), null, null, "timestamp DESC" );
	}
}
```


### The request object


Now that we have the model and the table we want to make a request to the server so we can fill our database with **awesome** data.

All the requests will have 4 parameters: the requests parameters, the requests headers, the response success listener and the response error listener.

Since the request is created with reflection the constructor must be the same for all the requests. 

The library provides a variety of base kind of requests depending on the response format expected:

* `BaseJsonRequest` for JSONObject responses
* `BaseJsonArrayRequest` for JSONObject arrays responses.
* `BaseStringRequest` for simple string responses ( not expected to be used for the normal library use )

It also supports Multipart requests for file uploading, the base classes available are:
* `BaseMultipartJsonRequest` with a JSONObject response
* `BaseMultipartJsonArrayRequest` with a JSONObject arrays response
* `BaseMultipartRequest` for simple string responses

The parameters can be easily created with the `ParametersJSONObject` helper class.

The request also provides the endpoint and the request type.

```java
package com.ensoft.restafari_app.example;

import com.android.volley.Response;
import com.ensoft.restafari.network.rest.request.BaseJsonRequest;

import org.json.JSONObject;

import java.util.Map;

// The request structure
public class IpRequest extends BaseJsonRequest
{
	public IpRequest( JSONObject parameters, Map<String, String> headers, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener )
	{
		super( Method.GET, "https://ip.ensoft.dev/?f=json", parameters, headers, listener, errorListener );
	}
}
```


### The response processor

The response processor ( `ResponseProcessor` ) is the class that handles the request response from the server. And also can optionally handle the request response errors.

The `ResponseProcessor` should expect as a response the same model object type that the request made, in this case is `IpModel`. And usually you'll want to add / update / delete that object from your database table as in this case is showed. So we just instantiate our table and insert the response object to it.

```java
package com.ensoft.restafari_app.example;

import android.content.Context;

import com.ensoft.restafari.network.processor.ResponseProcessor;
import com.ensoft.restafari.network.rest.request.RequestConfiguration;

import java.text.SimpleDateFormat;
import java.util.Date;

// The response processor, it runs in the background, this should be separated from the UI, this stores the result
public class IpResponseProcessor extends ResponseProcessor<IpModel>
{
	@Override
	public void handleResponse( Context context, RequestConfiguration request, IpModel response )
	{
		// We can also fill some data from the client side, it's not necessary to get all the object data from the server.
		response.timestamp = System.currentTimeMillis() / 1000L;
		response.timestampStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format( new Date( response.timestamp * 1000L ) );

        // Saves the response object to the internal database
        // This is the same as doing "new IpTable().insertOrUpdate( response );"
		response.save();
	}
	
	@Override
	public void handleError( Context context, RequestConfiguration request, int errorCode, String errorMessage )
	{
		// Do whatever you want with this
	}
}
```


### How do I make the request?


We have everything ready to make a request, process it and save it to our database. Now we just need to create the request.

The `RequestConfiguration` class indicates to the request service who are the classes that will handle the whole process. And will return a request id that will be used later if we need to get notifications 


```java
long requestId = RequestService.getInstance().addRequest( new RequestConfiguration( IpRequest.class, IpResponseProcessor.class, IpModel.class ) );
```


### But... how do I get the data from the database?


You will just use `CursorLoader` s. Any table data change will be notified by the cursor loader.


### But... how? Show me please!


No problem!

I'll show you the full example with a couple of new things too ( see the comments in the code ).

```java
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
	private long requestId;
	private SimpleCursorAdapter ipsAdapter;

	@Override
	protected void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_test );

		// get your list view ( or whatever component you wan't to use ) and set a cursor adapter to feed your list view with data
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

		// If you wan't to get a notification in the activty when the request is done ( succcesfully or not )
		// you can use the `RequestReceiverService` that will handle that notifications for you. Remember to extend from a `RequestResponseActivity` activity ( you also have the Fragment equivalent `RequestResponseFragment` ).
		// Add your request to the listener. 
		// You'll always receive the request response, but you'll receive always when the activity is active, 
		// so, if a response was received when the activity was paused, it'll be received next time the activity is resumed.
		getRequestReceiverService().addRequest( requestId );

		// Init the loader manager that will handle the database queries.
		getLoaderManager().initLoader( IPS_LOADER, null, this );
	}

	@Override
	public void onRequestSuccess( long requestId )
	{
		if ( requestId == this.requestId )
		{
			// Do whatever you wan't with the request response notification
			Toast.makeText( IpHistoryActivity.this, "New IP received", Toast.LENGTH_SHORT ).show();
		}
	}

	@Override
	public void onRequestError( long requestId, int resultCode, String resultMsg )
	{
		if ( requestId == this.requestId )
		{
			// Do whatever you wan't with the request response notification
			Toast.makeText( IpHistoryActivity.this, "Error: " + resultCode + " Msg: " + resultMsg, Toast.LENGTH_SHORT ).show();
		}
	}

	@Override
	public Loader<Cursor> onCreateLoader( int id, Bundle args )
	{
		if ( IPS_LOADER == id )
		{
			// Start loading the IPs in the table database
			return new IpTable().getIps();
		}

		return null;
	}

	@Override
	public void onLoadFinished( Loader<Cursor> loader, Cursor data )
	{
		if ( loader.getId() == IPS_LOADER )
		{
			// Assign the cursor to the adapter
			ipsAdapter.changeCursor( data );
		}
	}

	@Override
	public void onLoaderReset( Loader<Cursor> loader )
	{
	}
}
```


### But... that looks overly complicated!


Well... you're somewhat right. But this is intended to be used in cases when you have a lot of interaction between the server responses and your local database. It will keep your code clean and simple.

The library also provides ways to make just simple requests without this many steps, and many other features not mentioned here. Feel free to look at the code or ask me.





## License


```
The MIT License (MIT)

Copyright (c) 2020 Martín Lucas Golini 

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

