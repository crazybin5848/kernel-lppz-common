package com.lppz.mycat.monitor.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
/**
 * 远程调用shell工具
 * 
 * @author licheng
 *
 */
public class ShellUtil {
	private static Logger logger = LoggerFactory.getLogger(ShellUtil.class);

	/**
	 * 执行远程命令
	 * @param host
	 * @param user
	 * @param psw
	 * @param port
	 * @param command
	 * @return
	 */
	public static String exec(String host,String user,String psw,int port,String command){
		ChannelExec openChannel =null;
		String buf = null;
		StringBuilder resultSb = new StringBuilder();
		JSchUtil jSchUtil = new JSchUtil();
		int result = 0;
		try {
			openChannel = (ChannelExec) jSchUtil.getChannel(user,psw,host,port,"exec", 60000);
			openChannel.setCommand(command);
			result = openChannel.getExitStatus();
			openChannel.connect();
            InputStream in = openChannel.getInputStream();  
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));  
            while ((buf = reader.readLine()) != null) {
            	resultSb.append(new String(buf.getBytes("gbk"),"UTF-8")).append("    <br>\r\n");  
            	logger.info("exec shell result==>"+buf);
            }
            reader.close();
            in.close();
		} catch (JSchException | IOException e) {
			logger.error("jsch 执行异常", e);
		}finally{
	        try {
	        	jSchUtil.closeChannel();
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("关闭channel异常 ", e);
			}
		}
		return resultSb.toString();
	}
	
	/**
	 * 上传文件到指定目录
	 * @param host
	 * @param user
	 * @param psw
	 * @param port
	 * @param src
	 * @param dst
	 */
	public static void ftpFile(String host,String user,String psw,int port, String src, String dst){
		JSchUtil jSchUtil = new JSchUtil();
		try {
			// 设置主机ip，端口，用户名，密码
			ChannelSftp chSftp = (ChannelSftp) jSchUtil.getChannel(user,psw,host,port,"sftp", 60000);
			chSftp.connect();
			/**
			 * 代码段1
        OutputStream out = chSftp.put(dst, ChannelSftp.OVERWRITE); // 使用OVERWRITE模式
        byte[] buff = new byte[1024 * 256]; // 设定每次传输的数据块大小为256KB
        int read;
        if (out != null) {
            System.out.println("Start to read input stream");
            InputStream is = new FileInputStream(src);
            do {
                read = is.read(buff, 0, buff.length);
                if (read > 0) {
                    out.write(buff, 0, read);
                }
                out.flush();
            } while (read >= 0);
            System.out.println("input stream read done.");
        }
			 **/
        
			chSftp.put(src, dst, ChannelSftp.OVERWRITE);// 代码段2
			// chSftp.put(new FileInputStream(src), dst, ChannelSftp.OVERWRITE); // 代码段3
			chSftp.quit();
		} catch (SftpException | JSchException e) {
			logger.error("Sftp异常 ",e);
			throw new RuntimeException(e);
		} finally{
			try {
				jSchUtil.closeChannel();
			} catch (Exception e) {
				logger.error("关闭channel异常 ",e);
				throw new RuntimeException(e);
			}
		}
	}
	
	  public static void upLoadFile(String host,String user,String psw,int port, String sPath, String dPath) {
		  JSchUtil jSchUtil = new JSchUtil();
	        try {
	        	ChannelSftp sftp = (ChannelSftp) jSchUtil.getChannel(user,psw,host,port,"sftp", 10000000);
	        	sftp.connect();
	            try {
	                sftp.cd(dPath);
	            } catch (SftpException e) {
	                sftp.mkdir(dPath);
	                sftp.cd(dPath);

	            }
	            File file = new File(sPath);
	            copyFile(sftp, file, sftp.pwd(), 0);
	        } catch (Exception e) {
	           logger.error("上传文件异常 {}",sPath,e);
	        } finally {
	        	try {
					jSchUtil.closeChannel();
				} catch (Exception e) {
					logger.error("关闭channel异常 ",e);
					throw new RuntimeException(e);
				}
	        }
	    }

	    public static void copyFile(ChannelSftp sftp, File file, String pwd, int depth) {

	        if (file.isDirectory()) {
	            File[] list = file.listFiles();
	            if (depth > 0) {
	            	try {
	            		try {
	            			String fileName = file.getName();
	            			sftp.cd(pwd);
	            			logger.info("正在创建目录:{}" , sftp.pwd() + "/" + fileName);
	            			sftp.mkdir(fileName);
	            			logger.info("目录创建成功:{}" , sftp.pwd() + "/" + fileName);
	            		} catch (Exception e) {
	            		}
	            		pwd = pwd + "/" + file.getName();
	            		try {
	            			
	            			sftp.cd(file.getName());
	            		} catch (SftpException e) {
	            			logger.error("切换目录异常 {}",file.getName(),e);
	            		}
	            	} catch (Exception e) {
	            		logger.error(e.toString(),e);
	            	}
				}
	            for (int i = 0; i < list.length; i++) {
	                copyFile(sftp, list[i], pwd, ++depth);
	            }
	        } else {

	            try {
	                sftp.cd(pwd);
	            } catch (SftpException e1) {
	            	logger.error(e1.toString(),e1);
	            }
	            System.out.println("正在复制文件:" + file.getAbsolutePath());
	            InputStream instream = null;
	            OutputStream outstream = null;
	            try {
	                outstream = sftp.put(file.getName());
	                instream = new FileInputStream(file);

	                byte b[] = new byte[1024];
	                int n;
	                try {
	                    while ((n = instream.read(b)) != -1) {
	                        outstream.write(b, 0, n);
	                    }
	                } catch (IOException e) {
	                	logger.error(e.toString(),e);
	                }

	            } catch (SftpException e) {
	            	logger.error(e.toString(),e);
	            } catch (IOException e) {
	            	logger.error(e.toString(),e);
	            } finally {
	                try {
	                    outstream.flush();
	                    outstream.close();
	                    instream.close();

	                } catch (Exception e2) {
	                	logger.error(e2.toString(),e2);
	                }
	            }
	        }
	    }
}
