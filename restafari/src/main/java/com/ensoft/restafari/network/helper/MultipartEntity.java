package com.ensoft.restafari.network.helper;

import android.text.TextUtils;

import com.android.volley.VolleyLog;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public class MultipartEntity
{
	public static final String TWO_HYPHENS = "--";
	public static final String LINE_END = "\r\n";

	private final String boundary = "apiclient-" + System.currentTimeMillis();
	private Map<String, String> fileParams;
	private Map<String, String> params;

	public MultipartEntity( Map<String, String> fileParams, Map<String, String> params )
	{
		this.fileParams = fileParams;
		this.params = params;
	}

	public String getBoundary()
	{
		return boundary;
	}

	public byte[] build()
	{
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);

		try
		{
			if ( null != fileParams && !fileParams.isEmpty() )
			{
				for ( Map.Entry<String, String> entry : fileParams.entrySet() )
				{
					File file = new File( entry.getValue() );

					if ( file.exists() )
					{
						FileService fileService = new FileService(file);
						
						buildPart( dos, fileService.toByteArray(), entry.getKey(), file.getName(), fileService.getMimeType() );
					}
				}
			}

			if ( null != params )
			{
				for ( Map.Entry<String, String> entry : params.entrySet() )
				{
					if ( entry.getValue() != null )
					{
						buildTextPart( dos, entry.getKey(), entry.getValue() );
					}
				}
			}

			dos.writeBytes( TWO_HYPHENS + boundary + TWO_HYPHENS + LINE_END );
		}
		catch ( IOException e )
		{
			VolleyLog.e( "buildMultipartEntity: " + e.toString() );
		}

		return bos.toByteArray();
	}

	private void buildPart(DataOutputStream dataOutputStream, byte[] fileData, String fieldName, String fileName, String contentType) throws IOException
	{
		dataOutputStream.writeBytes( TWO_HYPHENS + boundary + LINE_END );
		dataOutputStream.writeBytes( "Content-Disposition: form-data; name=\""+ fieldName +"\"; filename=\"" + fileName + "\"" + LINE_END );
		
		if ( !TextUtils.isEmpty( contentType ) )
		{
			dataOutputStream.writeBytes( "Content-Type: " + contentType + LINE_END );
		}
		
		dataOutputStream.writeBytes( "Content-Transfer-Encoding: binary" + LINE_END );
		dataOutputStream.writeBytes( LINE_END );

		ByteArrayInputStream fileInputStream = new ByteArrayInputStream(fileData);
		int bytesAvailable = fileInputStream.available();

		int maxBufferSize = 1024 * 1024;
		int bufferSize = Math.min(bytesAvailable, maxBufferSize);
		byte[] buffer = new byte[bufferSize];

		// read file and write it into form...
		int bytesRead = fileInputStream.read( buffer, 0, bufferSize );

		while (bytesRead > 0)
		{
			dataOutputStream.write(buffer, 0, bufferSize);
			bytesAvailable = fileInputStream.available();
			bufferSize = Math.min(bytesAvailable, maxBufferSize);
			bytesRead = fileInputStream.read(buffer, 0, bufferSize);
		}

		dataOutputStream.writeBytes( LINE_END );
	}

	private void buildTextPart(DataOutputStream dataOutputStream, String parameterName, String parameterValue) throws IOException
	{
		dataOutputStream.writeBytes( TWO_HYPHENS + boundary + LINE_END );
		dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"" + parameterName + "\"" + LINE_END );
		dataOutputStream.writeBytes("Content-Type: text/plain; charset=UTF-8" + LINE_END );
		dataOutputStream.writeBytes( LINE_END );
		dataOutputStream.write(parameterValue.getBytes( "UTF-8" ));
		dataOutputStream.writeBytes( LINE_END );
	}
}
