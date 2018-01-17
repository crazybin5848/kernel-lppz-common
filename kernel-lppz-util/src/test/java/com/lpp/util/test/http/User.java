package com.lpp.util.test.http;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Set;

import com.lppz.util.MD5;

public class User implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7178131452560949719L;
	private String name;
	private String id;
	public User(){}
	public User(String name,String id){
		this.id=id;
		this.name=name;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public static void main(String[] args) {
		Set<String> l=new HashSet<String>();
		for(int i=1151686261;i<1171686271;i++){
			String code=(""+i%10)+MD5.getMD5(""+i);
			l.add(code);
//			System.out.println(code);
		}
		System.out.println(l.size());
	}
	
//	public static String MD5(String inStr) {
//        MessageDigest md5 = null;
//        try {
//            md5 = MessageDigest.getInstance("MD5");
//        } catch (Exception e) {
//            System.out.println(e.toString());
//            e.printStackTrace();
//            return "";
//        }
//        char[] charArray = inStr.toCharArray();
//        byte[] byteArray = new byte[charArray.length];
// 
//        for (int i = 0; i < charArray.length; i++)
//            byteArray[i] = (byte) charArray[i];
// 
//        byte[] md5Bytes = md5.digest(byteArray);
// 
//        StringBuffer hexValue = new StringBuffer();
// 
//        for (int i = 0; i < md5Bytes.length; i++) {
//            int val = ((int) md5Bytes[i]) & 0xff;
//            if (val < 16)
//                hexValue.append("0");
//            hexValue.append(Integer.toHexString(val));
//        }
// 
//        return hexValue.toString();
//    }
}