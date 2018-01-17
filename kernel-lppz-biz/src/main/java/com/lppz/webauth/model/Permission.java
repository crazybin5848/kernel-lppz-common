package com.lppz.webauth.model;

public class Permission
{
	private String code;
	private String url;


	/**
	 *
	 */
	public Permission(final String code, final String url)
	{
		super();
		this.code = code;
		this.url = url;
	}

	public String getCode()
	{
		return code;
	}

	public void setCode(final String code)
	{
		this.code = code;
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(final String url)
	{
		this.url = url;
	}


}
