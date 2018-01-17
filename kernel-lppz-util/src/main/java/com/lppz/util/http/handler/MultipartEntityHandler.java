package com.lppz.util.http.handler;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.nio.entity.NByteArrayEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lppz.util.FileReaderUtils;
import com.lppz.util.http.model.EdEPModel;
/**
 * Don't modify below code unless understand sufficiently
 * 1.As a delegation which support file entity ,MultipartEntityBuilder is the best one to overide despiercy method
 * 2.Avoid httpasyncclient  unsupported MultipartEntityBuilder exception but httpclient could 
 * 3.MultipartEntityBuilder covert to NByteArrayEntity allow us to close file after conversion initialized successfully
 * 4.NByteArrayEntity need random unique(Toker use UUID as an optional) as a header which disallow future wrapper repeatable
 */
public class MultipartEntityHandler extends BaseHttpHandler{
	private final Logger logger = LoggerFactory.getLogger(MultipartEntityHandler.class);

	private MultipartEntityHandler() {
	}

	private static MultipartEntityHandler instance = new MultipartEntityHandler();

	public static MultipartEntityHandler getInstance() {
		return instance;
	}

	@Override
	protected void doHttpPost(HttpRequestBase httpBase,
			Map<String, String> body, EdEPModel edEPModel) {
		HttpPost post = (HttpPost) httpBase;
		generateSign(body,edEPModel);
		MultipartEntityBuilder builder = MultipartEntityBuilder.create(); 
		try {
			File file = File.createTempFile(edEPModel.getPushEnum().fileid(), edEPModel.getPushEnum().format())	;
			String boundary = "-------------" + UUID.randomUUID().toString();
			builder.setBoundary(boundary);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();				
			builder.addBinaryBody(edEPModel.getPushEnum().fileid(), FileReaderUtils.buildXmlfile(edEPModel.getEncodeString(), file));
			for(String key:body.keySet())
				builder.addTextBody(key,body.get(key));
			builder.build().writeTo(baos);	
			post.setHeader("Content-Type", "multipart/form-data;boundary=" + boundary);
			post.setEntity(new NByteArrayEntity(baos.toByteArray(), ContentType.MULTIPART_FORM_DATA));
			file.delete();
		} catch (IOException e) {
			logger.debug(e.getMessage(), e);
		}
		
	}

	
}
