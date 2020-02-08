package com.ensoft.restafari.network.rest.response;

import android.text.TextUtils;

import java.io.Serializable;

/** An HTTP header. */
public final class Header implements Serializable
{
	private final String name;
	private final String value;
	
	public Header(String name, String value) {
		this.name = name;
		this.value = value;
	}
	
	public final String getName() {
		return name;
	}
	
	public final String getValue() {
		return value;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		
		com.ensoft.restafari.network.rest.response.Header header = (com.ensoft.restafari.network.rest.response.Header) o;
		
		return TextUtils.equals( name, header.name ) && TextUtils.equals( value, header.value );
	}
	
	@Override
	public int hashCode() {
		int result = name.hashCode();
		result = 31 * result + value.hashCode();
		return result;
	}
	
	@Override
	public String toString() {
		return "Header[name=" + name + ",value=" + value + "]";
	}
}
