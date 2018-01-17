package com.lppz.hbase.client;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.coprocessor.Constants;
import org.apache.hadoop.hbase.client.coprocessor.model.ScanResult;
import org.apache.hadoop.hbase.client.coprocessor.model.TableDmlModel;
import org.apache.hadoop.hbase.client.coprocessor.model.idx.FamilyValue;
import org.apache.hadoop.hbase.client.coprocessor.model.scan.CasCadeListCell;
import org.apache.hadoop.hbase.client.coprocessor.model.scan.CascadeCell;
import org.apache.hadoop.hbase.client.coprocessor.model.scan.ScanOrderedKV;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.RegexStringComparator;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Before;
import org.junit.Test;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import com.lppz.hbase.client.impl.HBaseDmlImpl;
import com.lppz.util.jedis.cluster.concurrent.OmsJedisCluster;

@SuppressWarnings("deprecation")
public class HBaseDmlTest extends BaseTest{
	HBaseDmlImpl hbd;
	@Before
	public void init(){
		hbd=new HBaseDmlImpl();
		super.initConf();
	}
	
	@Test
	public void testInsertData() {
//		JedisCluster jedis=buildJedis(); 
		List<List<TableDmlModel>> as=new ArrayList<List<TableDmlModel>>();
		Integer[] ii=new Integer[]{1,1,1};
		for(int j=2;j<3;j++){
			List<TableDmlModel> ll=new ArrayList<TableDmlModel>();
			for(int i=2;i<3;i++){
				Calendar calendar = Calendar.getInstance();    
			    calendar.setTime(new Date());    
			    calendar.add(Calendar.SECOND,i);    
				String oDate=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(calendar.getTime());
				TableDmlModel tdm=new TableDmlModel();
				tdm.setFamiliName("orderdetail");
				tdm.setTableName("omsorder");
				tdm.getQulifierAndValue().put("auid", "u000000"+j);
				tdm.getQulifierAndValue().put("storeid", "s000"+j+i);
				tdm.getQulifierAndValue().put("boid", (""+j)+i);
				tdm.getQulifierAndValue().put("outorderid", "out000000"+j+i);
				tdm.getQulifierAndValue().put("cash",getFixLenthString(6));
				tdm.getQulifierAndValue().put("createTime",oDate);
				buildtdmCellMap(tdm,ii);
				ll.add(tdm);
				}
			as.add(ll);
		}
		try {
			hbd.insertBatchDataWithSamePrefix(as,null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	private JedisCluster buildJedis() {
		JedisPoolConfig jpoolConfig = getJedisPool();
		Set<HostAndPort> nodes = getHostAndPortSet();
		Integer timeout = 50000;
		Integer maxRedirections = 5;
		return new OmsJedisCluster(nodes, timeout,
				maxRedirections, jpoolConfig);
	}
	
	private Set<HostAndPort> getHostAndPortSet() {
		Set<HostAndPort> nodes=new HashSet<HostAndPort>();
		nodes.add(new HostAndPort("192.168.37.243",6380));
		nodes.add(new HostAndPort("192.168.37.243",6381));
		nodes.add(new HostAndPort("192.168.37.243",6382));
		return nodes;
	}
	
	private JedisPoolConfig getJedisPool() {
		JedisPoolConfig jpc=new JedisPoolConfig();
		jpc.setBlockWhenExhausted(false);
		jpc.setNumTestsPerEvictionRun(100);
		jpc.setTestOnBorrow(false);
		jpc.setTestOnReturn(false);
		jpc.setMaxWaitMillis(10000);
		jpc.setMaxTotal(30000);
		jpc.setMaxIdle(5000);
		return jpc;
	}

	private String getFixLenthString(int strLength) {  
	    Random rm = new Random();  
	    double pross = (1 + rm.nextDouble()) * Math.pow(10, strLength);  
	    String fixLenthString = String.valueOf(pross);  
	    return fixLenthString.substring(1, strLength + 1);  
	} 
	
	private void buildtdmCellMap(TableDmlModel tdm,Integer[] ll) {
		CasCadeListCell cc=new CasCadeListCell();
		List<CascadeCell> olLine=new ArrayList<CascadeCell>();
		cc.getListCell().put("orderline", olLine);
		Integer olid1=ll[0]++;
		Integer olid2=ll[0]++;
		CascadeCell c1=new CascadeCell(null,Bytes.toBytes("orderline"));
		c1.getQulifyerValueMap().put("olid", ""+olid1);
		c1.getQulifyerValueMap().put("sku", "123");
		c1.getQulifyerValueMap().put("paymnt", "100");
		CascadeCell c2=new CascadeCell(null,Bytes.toBytes("orderline"));
		c2.getQulifyerValueMap().put("olid", ""+olid2);
		c2.getQulifyerValueMap().put("sku","456");
		c2.getQulifyerValueMap().put("paymnt", "200");
		olLine.add(c1);
		olLine.add(c2);
		
		List<CascadeCell> olorderlinedetail=new ArrayList<CascadeCell>();
		cc.getListCell().put("orderlinedetail", olorderlinedetail);
		CascadeCell c3=new CascadeCell(null,Bytes.toBytes("orderlinedetail"));
		LinkedHashMap<String, FamilyValue> parentRKey=new LinkedHashMap<String, FamilyValue>();
		parentRKey.put("olid", new FamilyValue("orderline",String.valueOf(olid1)));
		c3.setParentRKey(parentRKey);
		c3.getQulifyerValueMap().put("oldid", String.valueOf(ll[1]++));
		c3.getQulifyerValueMap().put("stock", "d123");
		c3.getQulifyerValueMap().put("desc", "hahha");
		CascadeCell c4=new CascadeCell(null,Bytes.toBytes("orderlinedetail"));
		c4.setParentRKey(parentRKey);
		c4.getQulifyerValueMap().put("oldid", String.valueOf(ll[1]++));
		c4.getQulifyerValueMap().put("stock", "d321");
		c4.getQulifyerValueMap().put("desc", "hahhab");
		CascadeCell c5=new CascadeCell(null,Bytes.toBytes("orderlinedetail"));
		LinkedHashMap<String, FamilyValue> parentRKey1=new LinkedHashMap<String, FamilyValue>();
		parentRKey1.put("olid", new FamilyValue("orderline",String.valueOf(olid2)));
		c5.setParentRKey(parentRKey1);
		c5.getQulifyerValueMap().put("oldid", String.valueOf(ll[1]++));
		c5.getQulifyerValueMap().put("stock", "d333");
		c5.getQulifyerValueMap().put("desc", "hahhsab");
		olorderlinedetail.add(c3);
		olorderlinedetail.add(c4);
		olorderlinedetail.add(c5);
		
		List<CascadeCell> olshipment=new ArrayList<CascadeCell>();
		cc.getListCell().put("ordershipment", olshipment);
		Integer spid1=ll[2]++;
		Integer spid2=ll[2]++;
		CascadeCell c6=new CascadeCell(null,Bytes.toBytes("ordershipment"));
		c6.getQulifyerValueMap().put("osid", String.valueOf(spid1));
		c6.getQulifyerValueMap().put("addr", "wuhan");
		c6.getQulifyerValueMap().put("pcode", "430000");
		CascadeCell c7=new CascadeCell(null,Bytes.toBytes("ordershipment"));
		c7.getQulifyerValueMap().put("osid", String.valueOf(spid2));
		c7.getQulifyerValueMap().put("addr", "beijing");
		c7.getQulifyerValueMap().put("pcode", "0000001");
		olshipment.add(c6);
		olshipment.add(c7);
		tdm.setMapCell(cc);
	}
	
	@Test
	public void testInsertBatchData() {
		List<TableDmlModel> tdmList=new ArrayList<TableDmlModel>();
		List<List<TableDmlModel>> ll=new ArrayList<List<TableDmlModel>>();
		for(int i=1;i<10;i++){
		TableDmlModel tdm=new TableDmlModel();
		tdm.setFamiliName("orderdetail");
		tdm.setTableName("omsorder");
		tdm.getQulifierAndValue().put("auid", "u0000001");
		tdm.getQulifierAndValue().put("storeid", "s000"+i);
		tdm.getQulifierAndValue().put("boid", "o000000"+i);
		tdm.getQulifierAndValue().put("cash", i+"0000");
		tdm.getQulifierAndValue().put("outorderid", "out000000"+i);
		Calendar calendar = Calendar.getInstance();    
	    calendar.setTime(new Date());    
	    if(i%2==0)
	    calendar.add(Calendar.SECOND,i);    
		tdm.getQulifierAndValue().put("createTime", new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(calendar.getTime()));
		buildtdmCellMap(tdm,new Integer[]{i,i,i});
		tdmList.add(tdm);
		}
		ll.add(tdmList);
		try {
			hbd.insertBatchDataWithSamePrefix(ll, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testDeleteAllColumn() {
		try {
			String rowkey="96mcq8g_t_zp8xj7qw##auid::u0000002_boid::000000000022_createTime::2017-02-06 04:07:32";
			hbd.deleteAllColumn("omsorder","orderdetail",rowkey);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testUpdateData() {
		CascadeCell cell=new CascadeCell("4o1h9eh_t_auid::u0000002_boid::000000000022".getBytes(),"orderdetail".getBytes());
		cell.getQulifyerValueMap().put("createTime", "2017-02-22 16:47:17");
		cell.getQulifyerValueMap().put("outorderid", "asdasdop21");
		cell.getQulifyerValueMap().put("auid", "asd暗黑大圣");
		cell.getQulifyerValueMap().put("cash", "847777777748");
		try {
			hbd.updateData("omsorder", cell);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

//	@Test
//	public void testInsertJoinData() {
//		List<TableDmlModel> tdmList=new ArrayList<TableDmlModel>();
//		for(int i=1;i<6;i++){
//			String rowa="t_a"+i;
//			TableDmlModel tdm=new TableDmlModel();
//			tdm.setFamiliName("cfa");
//			tdm.setTableName("A");
//			tdm.setRegionNum(2);
//			tdm.setRowkey(rowa);
//			tdm.getQulifierAndValue().put("a11", "a11$"+i);
//			tdm.getQulifierAndValue().put("a12", "a12$"+i);
//			tdm.getQulifierAndValue().put("cc", "abc$"+i);
//			String rowb="t_b"+i;
//			TableDmlModel tdmb=new TableDmlModel();
//			tdmb.setFamiliName("cfb");
//			tdmb.setTableName("B");
//			tdmb.setRegionNum(2);
//			tdmb.setRowkey(rowb);
//			tdmb.getQulifierAndValue().put("b11", "a11$"+i);
//			tdmb.getQulifierAndValue().put("b12", "b12$"+i);
//			tdmb.getQulifierAndValue().put("b13", "b13$"+i);
//			tdmList.add(tdmb);
////			tdmList.add(tdm);
//		}
//		try {
//			hbd.insertBatchData(tdmList);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}

//	@Test
//	public void testGetIdxResultScan() {
//		//List<ScanResult> list=hbd.scanResultByIdxRowKey("omsorder",new ScanOrderedKV[][]{new ScanOrderedKV[]{new ScanOrderedKV("outorderid","k000000016")}},"orderdetail", null, new String[]{"outorderid"}, 10, null);
//		List<ScanResult> list=hbd.scanResultByIdxRowKey("omsorder",new ScanOrderedKV[][]{
//				new ScanOrderedKV[]{new ScanOrderedKV("storeid","70008")
////		,new ScanOrderedKV("oid","000000075")
//				},
//		new ScanOrderedKV[]{new ScanOrderedKV("storeid","000016")
////		,new ScanOrderedKV("oid","000000016")
//		}},"orderdetail", null, new String[]{"storeid"}, 10, "post");
//		for(ScanResult sr:list){
//			String row=Bytes.toString(sr.getScr().getSource().getRow());
//			//hbd.deleteAllColumn("omsorder", row);
//			String outorderId=sr.getScr().getSource().getQulifyerValueMap().get("outorderid");
//			System.out.println("row:"+row+"outorderId:"+outorderId);
//		}
//	}
//	
//	@Test
//	public void testScan() {
//		byte[][] rowll=new byte[][]{"8l9a_t_au00004_o000000044_bs000044".getBytes(),"8e2q_t_au00004_o000000049_bs000049".getBytes()};
//		String rexStr = ".*_t_.*(";
//		int i = 0;
//		for (byte[] b : rowll) {
//			String desctid = Bytes.toString(b).split(Constants.SPLITTER)[3];
//			rexStr += desctid;
//			if (i++ < rowll.length - 1) {
//				rexStr += "|";
//			}
//		}
//		rexStr += ").*";
//		Filter filter1 = new RowFilter(CompareFilter.CompareOp.EQUAL,
//				new RegexStringComparator(rexStr));
//		Scan scan=new Scan();
//		scan.setCaching(100);
//		scan.addFamily("orderdetail".getBytes());
//		scan.setFilter(filter1);
//		try {
//			hbd.getResultScan("omsorder", scan, null);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
}