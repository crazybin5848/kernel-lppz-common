package com.lppz.dubbox;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.beans.factory.InitializingBean;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.lppz.core.inittb.InitDBMySqlUtil;

public abstract class LppzDynamicDubboRefConfig<T> implements InitializingBean{
	public static final String LPPZDUBBO="lppzdubbo";
	public static final String REFERENCE="com.alibaba.dubbo.config.annotation.Reference";
	private static final String SYSPLATFORM = "sys_platform";

	protected Class<T> clazz;
	protected String group="";
	protected String version="1.0.0";
	protected T localbean;
	protected Map<String,T> dubborefMap=new HashMap<String,T>();
	
	private DataSource dataSource;
	@Resource 
	private ApplicationConfig application;
	@Resource 
	private RegistryConfig registry;
	
	protected void init(Class<T> clazz,String group,String version,T localbean,DataSource dataSource){
		this.clazz=clazz;
		this.group=group;
		this.version=version;
		this.localbean=localbean;
		this.dataSource=dataSource;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void afterPropertiesSet() throws Exception {
		if(clazz==null)
			return;
		List<DubboRefBean> list=getRefFromDB();
		if(list==null){
			dubborefMap=null;
			return;
		}
		for(DubboRefBean dd:list){
				Object o=null;
				ReferenceConfig reference = new ReferenceConfig(); // 此实例很重，封装了与注册中心的连接以及与提供者的连接，请自行缓存，否则可能造成内存和连接泄漏
				reference.setApplication(application);
				reference.setRegistry(registry); // 多个注册中心可以用setRegistries()
				reference.setInterface(clazz);
				reference.setVersion(version);
				reference.setGroup(group);
				reference.setCheck(false);
				reference.setUrl(dd.getUrl()+"/"+clazz.getName());
				reference.setTimeout(100000);
				reference.setConnections(1000);
				o=reference.get();
				dubborefMap.put(dd.getCode(), (T) o);
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
	
//	public static void main(String[] args)  {
//		LppzDynamicDubboRefConfig ld=new LppzDynamicDubboRefConfig();
//		Reference ref=ld.getRef("rest://fuck/com.fuck.qwqe");
//		RestDubboStockComponent rd=(RestDubboStockComponent) ld.refer(ref,RestDubboStockComponent.class);
//		System.out.println(rd);  
//		String url="rest://com.lppz.aaa/xxx/services/";
//		Class<DubboRefBean> clazz=DubboRefBean.class;
//		System.out.println(url+clazz.getName());
//	}
	
	private List<DubboRefBean> getRefFromDB() {
		return dataSource==null||InitDBMySqlUtil.getInstance()
				.isTablePresent(dataSource,SYSPLATFORM)?getMybatisRefFromDB():null;
	}
	
	protected abstract List<DubboRefBean> getMybatisRefFromDB();
	protected T getDubboInterface(String platformCode) {
		return dubborefMap==null?localbean:(dubborefMap.containsKey(platformCode)?
				dubborefMap.get(platformCode):localbean);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void addDubboRefBean(DubboRefBean bean) {
		Object o=null;
		ReferenceConfig reference = new ReferenceConfig(); // 此实例很重，封装了与注册中心的连接以及与提供者的连接，请自行缓存，否则可能造成内存和连接泄漏
		reference.setApplication(application);
		reference.setRegistry(registry); // 多个注册中心可以用setRegistries()
		reference.setInterface(clazz);
		//reference.setVersion(version);
		reference.setGroup(group);
		reference.setCheck(false);
		reference.setUrl(bean.getUrl()+"/"+clazz.getName());
		reference.setTimeout(100000);
		reference.setConnections(200);
		o=reference.get();
		dubborefMap.put(bean.getCode(), (T) o);
	}
}
