package com.lppz.util;

import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ClassFile;
import javassist.bytecode.ConstPool;
import javassist.bytecode.FieldInfo;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.StringMemberValue;

import org.apache.commons.lang.StringUtils;

public class ClassPoolUtils {
	public static Class<?> tableMapping(String entityClassName, String tableName){
	        Class<?> c = null;
	       
	        if(StringUtils.isEmpty(entityClassName) || StringUtils.isEmpty(tableName)){
	            throw new IllegalArgumentException("The mapping parameter is invalid!");
	        }
	       
	        try {
	            ClassPool classPool = ClassPool.getDefault();
	            classPool.appendClassPath(new ClassClassPath(ClassPoolUtils.class));
	            classPool.importPackage("javax.persistence");
	            CtClass clazz = classPool.get(entityClassName);
	            clazz.defrost();
	            ClassFile classFile = clazz.getClassFile();
	          
	            ConstPool constPool = classFile.getConstPool();
	            Annotation tableAnnotation = new Annotation("javax.persistence.Table", constPool);
	            tableAnnotation.addMemberValue("name", new StringMemberValue(tableName, constPool));
	            // 获取运行时注解属性
	            AnnotationsAttribute attribute = (AnnotationsAttribute)classFile.getAttribute(AnnotationsAttribute.visibleTag);
	            attribute.addAnnotation(tableAnnotation);
	            classFile.addAttribute(attribute);
	            classFile.setVersionToJava5();
	            //clazz.writeFile();
	           
	            //TODO 当前ClassLoader中必须尚未加载该实体。（同一个ClassLoader加载同一个类只会加载一次）
	            c = clazz.toClass();
//	            EntityClassLoader loader = new EntityClassLoader(ClassPoolUtils.class.getClassLoader());
//	            c = clazz.toClass(loader , null);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	       
	        return c;
	    } 
	
	public void UpdateTest() throws NotFoundException{  
        ClassPool pool = ClassPool.getDefault();      
        //获取需要修改的类  
        CtClass ct = pool.get("com.tgb.itoo.collection.base.CollectionBase");   
          
        //获取类里的所有方法  
        CtMethod[] cms = ct.getDeclaredMethods();  
        CtMethod cm = cms[0];      
        System.out.println("方法名称====" + cm.getName());  
          
        MethodInfo minInfo = cm.getMethodInfo();  
        //获取类里的em属性  
        CtField cf = ct.getField("em");  
        FieldInfo fieldInfo = cf.getFieldInfo();    
          
        System.out.println("属性名称===" + cf.getName());  
          
        ConstPool cp = fieldInfo.getConstPool();  
        //获取注解信息  
        AnnotationsAttribute attribute2 = new AnnotationsAttribute(cp, AnnotationsAttribute.visibleTag);  
        Annotation annotation = new Annotation("javax.persistence.PersistenceContext", cp);  
        
        //修改名称为unitName的注解  
        annotation.addMemberValue("unitName", new StringMemberValue("basic-entity", cp));  
        attribute2.setAnnotation(annotation);  
        minInfo.addAttribute(attribute2);  
          
        //打印修改后方法  
        Annotation annotation2 = attribute2.getAnnotation("javax.persistence.PersistenceContext");  
        String text = ((StringMemberValue)annotation2.getMemberValue("unitName")).getValue();  
          
        System.out.println("修改后的注解名称===" + text);  
 }  
}

