package com.lppz.spark.util;
import java.io.IOException;

import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
 
public class HadoopPutMergeUtil {
	public static void putMerge(FileSystem hdfs,String inputDirStr,String hdfsFileStr) throws IOException {
		Path inputDir = new Path(inputDirStr);
		Path hdfsFile = new Path(hdfsFileStr);
 
		try {
			FileStatus[] inputFiles = hdfs.listStatus(inputDir);
			FSDataOutputStream out = hdfs.create(hdfsFile);
 
			for (int i = 0; i < inputFiles.length; i++) {
//				System.out.println(inputFiles[i].getPath().getName());
				FSDataInputStream in = hdfs.open(inputFiles[i].getPath());
				byte buffer[] = new byte[64 * 1024 * 1024];
				int bytesRead = 0;
				while ((bytesRead = in.read(buffer)) > 0) {
					out.write(buffer, 0, bytesRead);
				}
				in.close();
			}
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
 
	}
	
	public static boolean isRecur = false;
//	 
//	public static void merge(Path inputDir, Path hdfsFile, FileSystem hdfs,FSDataOutputStream out) {
//		try {
//			FileStatus[] inputFiles = hdfs.listStatus(inputDir);
//			for (int i = 0; i < inputFiles.length; i++) {
//				if (!hdfs.isFile(inputFiles[i].getPath())) {
//					if (isRecur){
//						merge(inputFiles[i].getPath(), hdfsFile, hdfs,out);
//						return ;
//					}
//					else {
//						System.out.println(inputFiles[i].getPath().getName()
//								+ "is not file and not allow recursion, skip!");
//						continue;
//					}
//				}
//				System.out.println(inputFiles[i].getPath().getName());
//				FSDataInputStream in = hdfs.open(inputFiles[i].getPath());
//				byte buffer[] = new byte[256];
//				int bytesRead = 0;
//				while ((bytesRead = in.read(buffer)) > 0) {
//					out.write(buffer, 0, bytesRead);
//				}
//				in.close();
//			}
//			out.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
// 
//	public static void errorMessage(String str) {
//		System.out.println("Error Message: " + str);
//		System.exit(1);
//	}
// 
//	public static void main(String[] args) throws IOException {
//		if (args.length == 0)
//			errorMessage("filesmerge [-r|-R] <hdfsTargetDir> <hdfsFileName>");
//		if (args[0].matches("^-[rR]$")) {
//			isRecur = true;
//		}
//		if ((isRecur && args.length != 3) || ( !isRecur && args.length != 2)) {
//			errorMessage("filesmerge [-r|-R] <hdfsTargetDir> <hdfsFileName>");
//		}
// 
//		Configuration conf = new Configuration();
//		FileSystem hdfs = FileSystem.get(conf);
// 
//		Path inputDir;
//		Path hdfsFile;
//		if(isRecur){
//			inputDir = new Path(args[1]);
//			hdfsFile = new Path(args[2]);
//		}
//		else{
//			inputDir = new Path(args[0]);
//			hdfsFile = new Path(args[1]);
//		}
// 
//		if (!hdfs.exists(inputDir)) {
//			errorMessage("hdfsTargetDir not exist!");
//		}
//		if (hdfs.exists(hdfsFile)) {
//			errorMessage("hdfsFileName exist!");
//		}
// 
//		FSDataOutputStream out = hdfs.create(hdfsFile);
//		merge(inputDir, hdfsFile, hdfs,out);
//		System.exit(0);
//	}
}