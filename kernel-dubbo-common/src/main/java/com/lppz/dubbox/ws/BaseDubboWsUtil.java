package com.lppz.dubbox.ws;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javassist.CannotCompileException;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ClassFile;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.BooleanMemberValue;
import javassist.bytecode.annotation.NoSuchClassError;
import javassist.bytecode.annotation.StringMemberValue;

import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.rpc.protocol.webservice.DubboWebService;
import com.alibaba.dubbo.rpc.protocol.webservice.WebServiceProtocol;

public class BaseDubboWsUtil {
	public static final String REFERENCE="com.alibaba.dubbo.config.annotation.Reference";
	public static final String DUBBOWEBSERVICE="com.alibaba.dubbo.rpc.protocol.webservice.DubboWebService";

	private Map<String,DubboWsReferBean> dwbeanMap;
	private Map<Class<?>,Object> dwReferMap=new HashMap<Class<?>,Object>();
	private BaseDubboWsUtil(){}
	private static BaseDubboWsUtil instance=new BaseDubboWsUtil();
	public static BaseDubboWsUtil getInstance(){
		return instance;
	}
	
	public void setDwbeanMap(Map<String, DubboWsReferBean> dwbeanMap) {
		this.dwbeanMap = dwbeanMap;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void initMap() throws Exception {
		for(String clazzStr:dwbeanMap.keySet()){
			DubboWsReferBean dwrb=dwbeanMap.get(clazzStr);
			Object o=null;
			ReferenceConfig reference = new ReferenceConfig(); // 此实例很重，封装了与注册中心的连接以及与提供者的连接，请自行缓存，否则可能造成内存和连接泄漏
			reference.setApplication(dwrb.getApplication());
			reference.setRegistry(dwrb.getRegistry()); // 多个注册中心可以用setRegistries()
			WebServiceProtocol.dwsMap.put(dwrb.getClazz(), getAnnotation(dwrb));
			reference.setInterface(dwrb.getClazz());
			reference.setVersion(dwrb.getVersion());
			reference.setGroup(dwrb.getGroup());
			reference.setCheck(false);
			reference.setUrl(dwrb.getUrl());
			reference.setTimeout(dwrb.getTimeout());
			reference.setConnections(dwrb.getConnection());
			o=reference.get();
			dwReferMap.put(dwrb.getClazz(), o);
		}
	}
//	private Reference getRef(String url) throws NotFoundException, ClassNotFoundException, NoSuchClassError {
//		ClassPool pool = ClassPool.getDefault();      
//        CtClass ct = pool.get("com.lppz.dubbox.DubboRefBean");   
//        CtField cf = ct.getField(LPPZDUBBO);  
//        FieldInfo fieldInfo = cf.getFieldInfo();    
//        ConstPool cp = fieldInfo.getConstPool();  
//        Annotation annotation = new Annotation(REFERENCE, cp);  
//        annotation.addMemberValue("url", new StringMemberValue(url, cp));  
//        Reference ref=(Reference)annotation.toAnnotationType(Reference.class.getClassLoader(), pool);
//        return ref;
//	}
	
	public DubboWebService getAnnotation(DubboWsReferBean dwrb) throws NotFoundException, CannotCompileException, IOException, ClassNotFoundException, NoSuchClassError  {
		ClassPool pool = ClassPool.getDefault();
		 pool.appendClassPath(new ClassClassPath(getClass()));
		 CtClass cc = pool.get(dwrb.getClazz().getName());   
		 cc.defrost();
	        ClassFile ccFile = cc.getClassFile();
	        ConstPool constpool = ccFile.getConstPool();
	        AnnotationsAttribute attribute = (AnnotationsAttribute) ccFile
	                  .getAttribute(AnnotationsAttribute.visibleTag);
	        Annotation annot = new Annotation(DUBBOWEBSERVICE, constpool);
	        annot.addMemberValue("parameters", new StringMemberValue(dwrb.getParameters(), constpool));
	        annot.addMemberValue("usrname", new StringMemberValue(dwrb.getUsername(), constpool));
	        annot.addMemberValue("passwd", new StringMemberValue(dwrb.getPasswd(), constpool));
	        annot.addMemberValue("type", new StringMemberValue(dwrb.getType(), constpool));
	        annot.addMemberValue("needLog", new BooleanMemberValue(dwrb.isNeedLog(), constpool));
	        attribute.addAnnotation(annot);
	        ccFile.addAttribute(attribute);
	        return (DubboWebService) cc.getAnnotation(DubboWebService.class);
	       // AnnotationClassLoader cl = new AnnotationClassLoader(getClass().getClassLoader());
//	        Loader cl = new Loader(cc.getClass().getClassLoader(),pool);
	        //Class<?> clazz = cc.toClass(getClass().getClassLoader(),null);
	}
	
	public Object get(Class<?> clazz){
		return dwReferMap.get(clazz);
	}
}

