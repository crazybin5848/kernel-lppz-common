package com.lppz.util.exception;

public class HttpPersistDBException extends RuntimeException
	{
		private static final long serialVersionUID = 1L;
		public HttpPersistDBException()
		{
		}

		public static long getSerialversionuid() {
			return serialVersionUID;
		}

		public HttpPersistDBException(final String message, final Throwable cause)
		{
			super(message, cause);
		}

		public HttpPersistDBException(final String message)
		{
			super(message);
		}
		
		public HttpPersistDBException(final Throwable cause)
		{
			super(cause);
		}
}
