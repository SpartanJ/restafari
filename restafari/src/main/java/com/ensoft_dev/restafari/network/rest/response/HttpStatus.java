package com.ensoft_dev.restafari.network.rest.response;

public enum HttpStatus
{
	OK_200(200),
	CREATED_201(201),
	ACCEPTED_202(202),
	BAD_REQUEST_400(400),
	UNAUTHORIZED_401(401),
	FORBIDDEN_403(403),
	NOT_FOUND_404(404),
	METHOD_NOT_ALLOWED(405),
	REQUEST_TIMEOUT_408(408),
	LENGTH_REQUIRED_411(411),
	PRECONDITION_FAILED_412(412),
	INTERNAL_SERVER_ERROR_500(500),
	SERVICE_UNAVAILABLE(503),
	UNKNOWN_ERROR(0xFFFFFF);

	private int code;

	HttpStatus(int code)
	{
		this.code = code;
	}

	public int getCode()
	{
		return code;
	}

	public static HttpStatus from(int code)
	{
		for (HttpStatus status : HttpStatus.values())
		{
			if (status.getCode() == code) return status;
		}
		return UNKNOWN_ERROR;
	}
}
