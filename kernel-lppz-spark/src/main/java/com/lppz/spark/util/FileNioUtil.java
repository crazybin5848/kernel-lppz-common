package com.lppz.spark.util;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Method;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.UUID;

public class FileNioUtil {
	private static final int DATA_CHUNK = 64 * 1024 * 1024; 
	public static void writeWithMappedByteBuffer(String fileDir,String fileName,StringBuilder sb) throws IOException {
		File dir = new File(fileDir);
		if (!dir.exists()) {
			makeDir(dir);
		}
		File file=new File(fileDir+"/"+(fileName==null?UUID.randomUUID().toString():fileName)+".txt");
		byte[] data = sb.toString().getBytes();
		RandomAccessFile raf = new RandomAccessFile(file, "rw");
		FileChannel fileChannel = raf.getChannel();
		int pos = 0;
		MappedByteBuffer mbb = null;
		long len = data.length;
		int dataChunk = DATA_CHUNK / (1024 * 1024);
		while (len >= DATA_CHUNK) {
			System.out.println("write a data chunk: " + dataChunk + "MB");
			mbb = fileChannel.map(MapMode.READ_WRITE, pos, DATA_CHUNK);
			mbb.put(data,pos,DATA_CHUNK);
			len -= DATA_CHUNK;
			pos += DATA_CHUNK;
		}
		if (len > 0) {
			System.out.println("write rest data chunk: " + len/(1024 * 1024) + "MB");
			mbb = fileChannel.map(MapMode.READ_WRITE, pos, len);
			mbb.put(data,pos,(int)len);
		}
		data = null;
		unmap(mbb);   // release MappedByteBuffer
		fileChannel.close();
		raf.close();
	}
	
	
	/**
	 * 在MappedByteBuffer释放后再对它进行读操作的话就会引发jvm crash，在并发情况下很容易发生
	 * 正在释放时另一个线程正开始读取，于是crash就发生了。所以为了系统稳定性释放前一般需要检
	 * 查是否还有线程在读或写
	 * @param mappedByteBuffer
	 */
	public static void unmap(final MappedByteBuffer mappedByteBuffer) {
		try {
			if (mappedByteBuffer == null) {
				return;
			}
			
			mappedByteBuffer.force();
			AccessController.doPrivileged(new PrivilegedAction<Object>() {
				@Override
				@SuppressWarnings("restriction")
				public Object run() {
					try {
						Method getCleanerMethod = mappedByteBuffer.getClass()
								.getMethod("cleaner", new Class[0]);
						getCleanerMethod.setAccessible(true);
						sun.misc.Cleaner cleaner = 
								(sun.misc.Cleaner) getCleanerMethod
									.invoke(mappedByteBuffer, new Object[0]);
						cleaner.clean();
						
					} catch (Exception e) {
						e.printStackTrace();
					}
					System.out.println("clean MappedByteBuffer completed");
					return null;
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	 public static boolean createFile(File file) throws IOException {  
	        if(! file.exists()) {  
	            makeDir(file.getParentFile());  
	        }  
	        return file.createNewFile();  
	    }  
	      
	    /** 
	     * Enhancement of java.io.File#mkdir() 
	     * Create the given directory . If the parent folders don't exists, we will create them all. 
	     * @see java.io.File#mkdir() 
	     * @param dir the directory to be created 
	     */  
	    public static void makeDir(File dir) {  
	        if(! dir.getParentFile().exists()) {  
	            makeDir(dir.getParentFile());  
	        }  
	        dir.mkdir();  
	    }  
}
