/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 */
package com.lppz.webauth.rest.common;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;



/**
 * 文件下载
 */
@SuppressWarnings("deprecation")
public class DownLoadFileServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	private static final String CONTENT_TYPE = "text/html; charset=UTF-8";

	private static String Path = File.separator + "uploadFolder" + File.separator;
	private static final Logger logger = LoggerFactory.getLogger(DownLoadFileServlet.class);

	// Initialize global variables
	@Override
	public void init() throws ServletException
	{
		final String webPath = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest()
				.getRealPath("");
		logger.info(webPath);
	}

	@Override
	public void doGet(final HttpServletRequest request, final HttpServletResponse response) throws UnsupportedEncodingException
	{
		response.setContentType(CONTENT_TYPE);
		// 得到下载文件的名字,解决中文乱码问题
		final String filename = request.getParameter("filename").replace("\n", "");
		final String folder = request.getParameter("folder");
		final String ExportOrDownLoad = request.getParameter("ExportOrDownLoad");

		// 创建file对象
		File file = null;
		if (ExportOrDownLoad.equals("Export"))
		{
			final String webPath = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest()
					.getRealPath("");
			file = new File(webPath + Path + filename.replace("\n", ""));
		}
		else if (ExportOrDownLoad.equals("DownLoad"))
		{
			final String filePath = request.getSession().getServletContext().getRealPath("");
			final String baseFileName = File.separator + "exportExcel" + File.separator + folder + File.separator;
			final StringBuilder filePathSb = new StringBuilder(filePath);
			filePathSb.append(baseFileName);
			filePathSb.append(filename);
			file = new File(filePathSb.toString());
		}

		// 设置response的编码方式
		response.setContentType("application/x-msdownload");

		// 写明要下载的文件的大小
		response.setContentLength((int) file.length());

		// 设置附加文件名,解决下载完成后不能保存中文文件名
		response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(filename, "UTF-8"));

		// 读出文件到i/o流
		FileInputStream fis = null;
		OutputStream myout = null;
		BufferedInputStream buff = null;
		try
		{
			fis = new FileInputStream(file);

			buff = new BufferedInputStream(fis);

			final byte[] b = new byte[1024];// 相当于我们的缓存

			long k = 0;// 该值用于计算当前实际下载了多少字节

			// 从response对象中得到输出流,准备下载
			myout = response.getOutputStream();

			// 开始循环下载
			while (k < file.length())
			{
				final int j = buff.read(b, 0, 1024);
				k += j;
				// 将b中的数据写到客户端的内存
				myout.write(b, 0, j);
			}
			// 将写入到客户端的内存的数据,刷新到磁盘
			myout.flush();
		}
		catch (final FileNotFoundException e)
		{
			throw new RuntimeException(e);
		}
		catch (final Exception e)
		{
			throw new RuntimeException(e);
		}
		finally
		{
			try
			{
				if (buff != null)
				{
					buff.close();
				}
				if (myout != null)
				{
					myout.close();
				}
				if (fis != null)
				{
					fis.close();
				}
			}
			catch (final IOException e)
			{
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	public void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException
	{
		doGet(request, response);
	}

}
