package com.lppz.core.datasource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DualMasterInternalDynamicDataSource extends DynamicDataSource
{
	@Override
	protected String generateRouteKey()
	{
		String key= DynamicDataSourceHolder.getIntenal()==null?"master":DynamicDataSourceHolder.getIntenal();
		if(key.contains("slave"))
			return key;
		int num=java.util.concurrent.ThreadLocalRandom.current().nextInt(loadweights.size());
		return key+loadweights.get(num);
	}
	private List<Integer> loadweights=new ArrayList<Integer>();
	public void setLoadFactormasterMap(Map<String, Integer> loadFactormasterMap) {
		int k=0;
		for(String master:loadFactormasterMap.keySet()){
			Integer weight=loadFactormasterMap.get(master);
			Integer load=k++;
			for(int j=0;j<weight;j++){
				loadweights.add(load);
			}
		}
	}
	
//	private static Map<Integer,List<Integer>> map=new HashMap<Integer,List<Integer>>();
//	private static int getNum(){
//		int num=java.util.concurrent.ThreadLocalRandom.current().nextInt(3);
//		List<Integer> l=map.get(num);
//		if(l==null)
//			l=new ArrayList<Integer>();
//		l.add(num);
//		map.put(num, l);
//		return num;
//	}
//	
//	 public static void main(String[] args) {
//		 for(int i=0;i<1000000;i++)
//		 getNum();
//		 System.out.println(map.get(0).get(0));
//		 System.out.println(map.get(1).get(0));
//		 System.out.println(map.get(2).get(0));
//	    }
}