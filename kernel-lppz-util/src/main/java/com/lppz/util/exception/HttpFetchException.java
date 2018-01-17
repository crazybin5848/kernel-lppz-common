package com.lppz.util.exception;

import org.apache.http.HttpResponse;

public class HttpFetchException extends Exception
	{
		private static final long serialVersionUID = 1L;
		private HttpResponse httpResponse;
		public HttpFetchException()
		{
		}

		public HttpResponse getHttpResponse() {
			return httpResponse;
		}

		public void setHttpResponse(HttpResponse httpResponse) {
			this.httpResponse = httpResponse;
		}

		public static long getSerialversionuid() {
			return serialVersionUID;
		}

		public HttpFetchException(final String message, final Throwable cause)
		{
			super(message, cause);
		}
		public HttpFetchException(final String message, final Throwable cause,HttpResponse httpResponse)
		{
			super(message, cause);
			this.setHttpResponse(httpResponse);
		}

		public HttpFetchException(final String message)
		{
			super(message);
		}
		
		public HttpFetchException(final String message,HttpResponse httpResponse)
		{
			super(message);
			this.setHttpResponse(httpResponse);
		}

		public HttpFetchException(final Throwable cause)
		{
			super(cause);
		}
}
