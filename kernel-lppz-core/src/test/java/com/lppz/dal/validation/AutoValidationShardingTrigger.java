//package com.lppz.dal.validation;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//import javax.annotation.Resource;
//
//import org.junit.runner.RunWith;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
//import com.lppz.core.thread.AsyncBaseHandler;
//import com.lppz.core.thread.trigger.BaseFetchTrigger;
//
//
//public class AutoValidationShardingTrigger extends BaseFetchTrigger<Order,Order>{
//
//	@Resource
//	private JdbcTemplate jdbcTemplate;
//	
//	@Resource(name="autoValidationShardingHandler")
//	public void setAsyncBaseHandler(AsyncBaseHandler<Order,Order> asyncBaseHandler) {
//		super.setAsyncBaseHandler(asyncBaseHandler);
//	}
//	
//	@Override
//	protected List<String> fetchPkIds() {
//		return buildOrderIdList();
//	}
//	
//
//	private String querySql;
//	private void buildQueryOrder()
//	{
//		querySql="select o.orderId from orders o,order_sharding os where o.state='CREATED' and o.liangpintag='0' and o.createdState is null and o.orderid=os.orderid ";
//		if (clusterMode)
//		{
//			querySql+=" and os.id % "+getClusterMap().size()+"="+getNode();
//		}
//			querySql+=" ORDER BY o.creationtime LIMIT " + fetchSize;
//	}
//	
//	private List<String> buildOrderIdList() {
//		if(querySql==null)
//			buildQueryOrder();
//		final List<Map<String, Object>> resultList = jdbcTemplate.queryForList(querySql);
//		final List<String> list = new ArrayList<String>();
//		if (resultList == null || resultList.size() <= 0)
//		{
//			return list;
//		}
//		
//		for (final Map<String, Object> map : resultList)
//		{
//			list.add((String) map.get("orderId"));
//		}
//		return list;
//	}
//	
//	
//	
//}
//
