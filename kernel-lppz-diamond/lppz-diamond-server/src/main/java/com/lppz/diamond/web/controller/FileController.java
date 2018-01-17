package com.lppz.diamond.web.controller;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.lppz.diamond.Constants;
import com.lppz.diamond.utils.SessionHolder;
import com.lppz.diamond.web.model.User;
import com.lppz.diamond.web.service.ConfigService;
import com.lppz.diamond.web.service.ModuleService;
import com.lppz.diamond.web.service.ProjectService;

@Controller
@RequestMapping("/file")
public class FileController extends BaseController {
	private static final Logger LOGGER = LoggerFactory.getLogger(FileController.class);
	
	@Autowired
	private ConfigService configService;
	@Autowired
	private ModuleService moduleService;
	@Autowired
	private ProjectService projectService;
	
	/*
     * 通过流的方式上传文件
     * @RequestParam("file") 将name=file控件得到的文件封装成CommonsMultipartFile 对象
     */
    @RequestMapping("/fileUpload")
    @ResponseBody
    public String  fileUpload(@RequestParam("uploadfile") CommonsMultipartFile uploadfile
    		,@RequestParam("projectId") long projectId) throws IOException {
    	String result = "success";
    	User user = null;
    	try {
    		user = (User) SessionHolder.getSession().getAttribute("sessionUser");
		} catch (Exception e) {
			LOGGER.error("",e);
		}
        //用来检测程序运行时间
        long  startTime=System.currentTimeMillis();
        LOGGER.debug("fileName：{}",uploadfile.getOriginalFilename());
        InputStream is=null;
        String[] tmpStrArr = null;
        String tmpNextDesc = null;
        long moduleId = 0;
        try {
            //获取输入流 CommonsMultipartFile 中可以直接得到文件的流
            is=uploadfile.getInputStream();
            BufferedReader br=new BufferedReader(new InputStreamReader(is));
            
            while (true) {
            	String line = br.readLine();
				if (StringUtils.isNotBlank(line)) {
					if (line.contains("MODULE_NAME")) {
						tmpStrArr = line.split("=");
						if (tmpStrArr.length==2) {
							moduleId = moduleService.save(projectId, tmpStrArr[1]);
						}
					}else{
						if (moduleId != 0) {							
							tmpNextDesc = buildConfig(line, projectId, moduleId, user==null?null:user.getUserCode(), tmpNextDesc);
						}else{
							tmpNextDesc = null;
						}
					}
				}else{
					break;
				}
			}
         
        } catch (FileNotFoundException e) {
           LOGGER.error("",e);
           result = "fail";
        }finally{
        	if (is != null) {				
        		is.close();
			}
        }
        long  endTime=System.currentTimeMillis();
        LOGGER.debug("方法一的运行时间：{} ms",endTime-startTime);
        
        
        return result; 
    }

	private String buildConfig(String line, long projectId, long moduleId, String user, String desc) {
		String descNext = null;
		String[] tmpStrArr = null;
		if (StringUtils.isNotBlank(line) && line.trim().length()>0) {
			if (!line.contains("version")) {
				tmpStrArr = line.split("=");
				if(tmpStrArr.length > 1){
					String value = null;
					if (tmpStrArr.length > 2) {
						value = append(tmpStrArr);
					}else{
						value = tmpStrArr[1].trim();
					}
					LOGGER.debug("-----{}------{}------",tmpStrArr[0].trim(), value);
					configService.insertConfig(tmpStrArr[0].trim(), value, desc, projectId, moduleId, user);
				}else{
					descNext = line.replace("#", "");
				}
			}
		}
		return descNext;
	}

	private String append(String[] tmpStrArr) {
		StringBuilder sb = new StringBuilder();
		for (int i = 1; i < tmpStrArr.length; i++) {
			sb.append(tmpStrArr[i]);
		}
		return sb.toString();
	}
	
	/**
	 * 根据要下载的文件名称
	 * 上服务器的目录下，下载该文件
	 * @param request 请求
	 * @param response 响应
	 * @throws IOException
	 */
	@RequestMapping("/download/{projectCode}/{moduleId}")
	public void download(@PathVariable("projectCode") String projectCode,@PathVariable("moduleId") String moduleId
			, HttpServletResponse response) {
	    OutputStream os = null;
	    try{
	    	String config = configService.queryConfigsForView(projectCode, "development", "preview", moduleId.equals("all")?null:moduleId);
	        //设置头部信息
	        response.setCharacterEncoding("UTF-8");
	        response.setContentType("multipart/form-data");
	        response.setHeader("Content-Disposition", "attachment;fileName="+ projectCode);

	        //创建读取流
	        os = response.getOutputStream();
	        byte[] configBtyes = config.getBytes();
	        //读取数据
	        byte[] b = new byte[2048];
	        int length;
	        int index = 0;
	        while ((length = read(configBtyes, b, index++)) > Constants.ZERO_SIZE) {
	            os.write(b, 0, length);
	        }
	    }catch(IOException e){
	        LOGGER.error("",e);
	    }finally{
	        // 关闭
	        if(os != null){
	            try {
	                os.close();
	            } catch (IOException e) {
	            	LOGGER.error("",e);
	            }
	        }
	    }
	}

	private int read(byte[] configBtyes, byte[] b, int i) {
		int length = b.length;
		int configLength = configBtyes.length;
		int startIndex = i*length;
		int endIndex = (i+1)*length;
		if (endIndex > configLength) {
			endIndex = configLength;
		}
		int bindex = 0;
		for (int j = startIndex; j < endIndex; j++) {
			b[bindex++]=configBtyes[j];
		}
		
		if (bindex < length) {
			for (int j = bindex; j < length; j++) {
				b[j] = 0;
			}
		}
		return bindex;
	}

}
