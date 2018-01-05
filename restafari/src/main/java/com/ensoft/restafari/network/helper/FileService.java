package com.ensoft.restafari.network.helper;

import android.webkit.MimeTypeMap;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FileService
{
	public File file;

	public FileService( File file )
	{
		this.file = file;
	}

	public byte[] toByteArray() throws IOException
	{
		int size = (int) file.length();
		byte[] bytes = new byte[size];

		BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
		buf.read(bytes, 0, bytes.length);
		buf.close();

		return bytes;
	}
	
	public String getMimeType()
	{
		String type = "";
		
		final String url = file.toString();
		
		final String extension = MimeTypeMap.getFileExtensionFromUrl(url);
		
		if ( extension != null )
		{
			type = MimeTypeMap.getSingleton().getMimeTypeFromExtension( extension.toLowerCase() );
		}
		
		return type;
	}
}
