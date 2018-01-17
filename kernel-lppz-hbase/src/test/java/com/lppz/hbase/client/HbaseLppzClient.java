//package com.lppz.hbase.client;
//
//import java.io.IOException;
//import java.util.Random;
//
//import org.apache.hadoop.conf.Configuration;
//import org.apache.hadoop.hbase.HBaseConfiguration;
//import org.apache.hadoop.hbase.HColumnDescriptor;
//import org.apache.hadoop.hbase.HTableDescriptor;
//import org.apache.hadoop.hbase.KeyValue;
//import org.apache.hadoop.hbase.client.Delete;
//import org.apache.hadoop.hbase.client.Get;
//import org.apache.hadoop.hbase.client.HBaseAdmin;
//import org.apache.hadoop.hbase.client.HTable;
//import org.apache.hadoop.hbase.client.Put;
//import org.apache.hadoop.hbase.client.Result;
//import org.apache.hadoop.hbase.client.ResultScanner;
//import org.apache.hadoop.hbase.client.Scan;
//import org.apache.hadoop.hbase.client.coprocessor.model.CasCadeListCell;
//import org.apache.hadoop.hbase.filter.CompareFilter;
//import org.apache.hadoop.hbase.filter.Filter;
//import org.apache.hadoop.hbase.filter.RowFilter;
//import org.apache.hadoop.hbase.filter.SubstringComparator;
//import org.apache.hadoop.hbase.util.Bytes;
//
//import com.alibaba.fastjson.JSON;
//
//public class HbaseLppzClient {
//	static Configuration conf = null;
//	static {
//		conf = HBaseConfiguration.create();
//		conf.set("hbase.zookeeper.property.clientPort", "2181");  
////		conf.set("hbase.master", "10.6.25.96:600000");  
//		conf.set("hbase.zookeeper.quorum", "10.6.25.96");
//	}
//
//	
//	/*
//     * 创建表
//     * 
//     * @tableName 表名
//     * 
//     * @family 列族列表
//     */
//    public static void creatTable(String tableName, String[] family,byte[][] splitkey)
//            throws Exception {
//        HBaseAdmin admin = new HBaseAdmin(conf);
////	  Configuration HBASE_CONFIG = new Configuration();      
////      HBASE_CONFIG.set("hbase.zookeeper.quorum", "192.168.37.242,192.168.37.243,192.168.37.245");    
////      HBASE_CONFIG.set("hbase.zookeeper.quorum", "10.6.25.96");    
////      Configuration configuration = HBaseConfiguration.create(HBASE_CONFIG);   
////      HBaseAdmin admin =new HBaseAdmin(configuration);
//        HTableDescriptor desc = new HTableDescriptor(tableName);
//        for (int i = 0; i < family.length; i++) {
//            desc.addFamily(new HColumnDescriptor(family[i]));
//        }
//        if (admin.tableExists(tableName)) {
//        	admin.disableTable(tableName);
//        	admin.deleteTable(tableName);
//            System.out.println("table Exists!");
//            System.exit(0);
//        } else {
//            admin.createTable(desc,splitkey);
//            System.out.println("create table Success!");
//        }
//    }
//
//    /*
//     * 为表添加数据（适合知道有多少列族的固定表）
//     * 
//     * @rowKey rowKey
//     * 
//     * @tableName 表名
//     * 
//     * @column1 第一个列族列表
//     * 
//     * @value1 第一个列的值的列表
//     * 
//     * @column2 第二个列族列表
//     * 
//     * @value2 第二个列的值的列表
//     */
//    public static void addData(String rowKey, String tableName,
//            String[] column1, String[] value1)
//            throws IOException {
//        Put put = new Put(Bytes.toBytes(rowKey));// 设置rowkey
//        HTable table = new HTable(conf, Bytes.toBytes(tableName));// HTabel负责跟记录相关的操作如增删改查等//
//        HColumnDescriptor[] columnFamilies = table.getTableDescriptor() // 获取所有的列族
//                .getColumnFamilies();
//
//        for (int i = 0; i < columnFamilies.length; i++) {
//            String familyName = columnFamilies[i].getNameAsString(); // 获取列族名
//            if (familyName.equals("orderdetail")) { 
//                for (int j = 0; j < column1.length; j++) {
//                    put.add(Bytes.toBytes(familyName),
//                            Bytes.toBytes(column1[j]), Bytes.toBytes(value1[j]));
//                }
//                OrderRow or=build(rowKey);
//                put.setAttribute("rowkeyol",Bytes.toBytes(JSON.toJSONString(or)));
//            }
//            
////            if (familyName.equals("author")) { // author列族put数据
////                for (int j = 0; j < column2.length; j++) {
////                    put.add(Bytes.toBytes(familyName),
////                            Bytes.toBytes(column2[j]), Bytes.toBytes(value2[j]));
////                }
////            }
//        }
//        table.put(put);
//        table.close();
//        System.out.println("add data Success!");
//    }
//
//    private static OrderRow build(String rowKey) {
//    	String[] ss=rowKey.split("_");
//    	OrderRow or=new OrderRow();
//    	or.setOrderId(ss[1]);
//    	or.setUid(ss[0]);
//    	for(int i=0;i<5;i++){
//    	or.addOrderLineId(new OrderLine("ol"+i,"sku"+i,(i+1)*100+""));
//    	}
//		return or;
//	}
//
//	/*
//     * 根据rwokey查询
//     * 
//     * @rowKey rowKey
//     * 
//     * @tableName 表名
//     */
//    public static Result getResult(String tableName, String rowKey)
//            throws IOException {
//        Get get = new Get(Bytes.toBytes(rowKey));
//        HTable table = new HTable(conf, Bytes.toBytes(tableName));// 获取表
//        Result result = table.get(get);
//        for (KeyValue kv : result.list()) {
//            System.out.println("family:" + Bytes.toString(kv.getFamily()));
//            System.out
//                    .println("qualifier:" + Bytes.toString(kv.getQualifier()));
//            System.out.println("value:" + Bytes.toString(kv.getValue()));
//            System.out.println("Timestamp:" + kv.getTimestamp());
//            System.out.println("-------------------------------------------");
//        }
//        return result;
//    }
//
////    @SuppressWarnings("deprecation")
////	public static void QueryAll(String tableName) {  
////		HTablePool pool = new HTablePool(conf, 1000);  
////		PooledHTable table = (PooledHTable) pool.getTable(tableName);  
////        try {  
////            ResultScanner rs = table.getScanner(new Scan());  
////            for (Result r : rs) {  
////                System.out.println("获得到rowkey:" + new String(r.getRow()));  
////                for (KeyValue keyValue : r.raw()) {  
////                    System.out.println("列：" + new String(keyValue.getFamily())  
////                            + "====值:" + new String(keyValue.getValue()));  
////                }  
////            }  
////        } catch (IOException e) {  
////            e.printStackTrace();  
////        }  
////    }  
//    /*
//     * 遍历查询hbase表
//     * 
//     * @tableName 表名
//     */
//    @SuppressWarnings("deprecation")
//	public static void getResultScann(String tableName,String partrowKey) throws IOException {
//        Scan scan = new Scan();
//        scan.addFamily(Bytes.toBytes("orderdetail"));
//		scan.setCaching(10);
//		Filter filter = new RowFilter(CompareFilter.CompareOp.EQUAL,new SubstringComparator(partrowKey));
//		scan.setFilter(filter);
//        ResultScanner rs = null;
//        HTable table = new HTable(conf, Bytes.toBytes(tableName));
//        try {
//            rs = table.getScanner(scan);
//            for (Result r : rs) {
//                for (KeyValue kv : r.list()) {
//                    System.out.println("row:" + Bytes.toString(kv.getRow()));
//                    System.out.println("family:"
//                            + Bytes.toString(kv.getFamily()));
//                    System.out.println("qualifier:"
//                            + Bytes.toString(kv.getQualifier()));
////                    System.out
////                            .println("value:" + Bytes.toString(kv.getValue()));
//                    CasCadeListCell clist=JSON.parseObject(Bytes.toString(kv.getValue()), CasCadeListCell.class);
//                    
//                    System.out.println("timestamp:" + kv.getTimestamp());
//                    System.out
//                            .println("-------------------------------------------");
//                }
//            }
//        }
//        catch (Exception e) {
//			e.printStackTrace();
//		}
//        finally {
//        	table.close();
//            rs.close();
//        }
//    }
//
//    /*
//     * 遍历查询hbase表
//     * 
//     * @tableName 表名
//     */
//    public static void getResultScann(String tableName, String start_rowkey,
//            String stop_rowkey) throws IOException {
//        Scan scan = new Scan();
//        scan.setStartRow(Bytes.toBytes(start_rowkey));
//        scan.setStopRow(Bytes.toBytes(stop_rowkey));
//        ResultScanner rs = null;
//        HTable table = new HTable(conf, Bytes.toBytes(tableName));
//        try {
//            rs = table.getScanner(scan);
//            for (Result r : rs) {
//                for (KeyValue kv : r.list()) {
//                    System.out.println("row:" + Bytes.toString(kv.getRow()));
//                    System.out.println("family:"
//                            + Bytes.toString(kv.getFamily()));
//                    System.out.println("qualifier:"
//                            + Bytes.toString(kv.getQualifier()));
//                    System.out
//                            .println("value:" + Bytes.toString(kv.getValue()));
//                    System.out.println("timestamp:" + kv.getTimestamp());
//                    System.out
//                            .println("-------------------------------------------");
//                }
//            }
//        } finally {
//            rs.close();
//        }
//    }
//
//    /*
//     * 查询表中的某一列
//     * 
//     * @tableName 表名
//     * 
//     * @rowKey rowKey
//     */
//    public static void getResultByColumn(String tableName, String rowKey,
//            String familyName, String columnName) throws IOException {
//        HTable table = new HTable(conf, Bytes.toBytes(tableName));
//        Get get = new Get(Bytes.toBytes(rowKey));
//        get.addColumn(Bytes.toBytes(familyName), Bytes.toBytes(columnName)); // 获取指定列族和列修饰符对应的列
//        Result result = table.get(get);
//        for (KeyValue kv : result.list()) {
//            System.out.println("family:" + Bytes.toString(kv.getFamily()));
//            System.out
//                    .println("qualifier:" + Bytes.toString(kv.getQualifier()));
//            System.out.println("value:" + Bytes.toString(kv.getValue()));
//            System.out.println("Timestamp:" + kv.getTimestamp());
//            System.out.println("-------------------------------------------");
//        }
//    }
//
//    /*
//     * 更新表中的某一列
//     * 
//     * @tableName 表名
//     * 
//     * @rowKey rowKey
//     * 
//     * @familyName 列族名
//     * 
//     * @columnName 列名
//     * 
//     * @value 更新后的值
//     */
//    public static void updateTable(String tableName, String rowKey,
//            String familyName, String columnName, String value)
//            throws IOException {
//        HTable table = new HTable(conf, Bytes.toBytes(tableName));
//        Put put = new Put(Bytes.toBytes(rowKey));
//        put.add(Bytes.toBytes(familyName), Bytes.toBytes(columnName),
//                Bytes.toBytes(value));
//        table.put(put);
//        System.out.println("update table Success!");
//    }
//
//    /*
//     * 查询某列数据的多个版本
//     * 
//     * @tableName 表名
//     * 
//     * @rowKey rowKey
//     * 
//     * @familyName 列族名
//     * 
//     * @columnName 列名
//     */
//    public static void getResultByVersion(String tableName, String rowKey,
//            String familyName, String columnName) throws IOException {
//        HTable table = new HTable(conf, Bytes.toBytes(tableName));
//        Get get = new Get(Bytes.toBytes(rowKey));
//        get.addColumn(Bytes.toBytes(familyName), Bytes.toBytes(columnName));
//        get.setMaxVersions(5);
//        Result result = table.get(get);
//        for (KeyValue kv : result.list()) {
//            System.out.println("family:" + Bytes.toString(kv.getFamily()));
//            System.out
//                    .println("qualifier:" + Bytes.toString(kv.getQualifier()));
//            System.out.println("value:" + Bytes.toString(kv.getValue()));
//            System.out.println("Timestamp:" + kv.getTimestamp());
//            System.out.println("-------------------------------------------");
//        }
//        /*
//         * List<?> results = table.get(get).list(); Iterator<?> it =
//         * results.iterator(); while (it.hasNext()) {
//         * System.out.println(it.next().toString()); }
//         */
//    }
//
//    /*
//     * 删除指定的列
//     * 
//     * @tableName 表名
//     * 
//     * @rowKey rowKey
//     * 
//     * @familyName 列族名
//     * 
//     * @columnName 列名
//     */
//    public static void deleteColumn(String tableName, String rowKey,
//            String falilyName, String columnName) throws IOException {
//        HTable table = new HTable(conf, Bytes.toBytes(tableName));
//        Delete deleteColumn = new Delete(Bytes.toBytes(rowKey));
//        deleteColumn.deleteColumns(Bytes.toBytes(falilyName),
//                Bytes.toBytes(columnName));
//        table.delete(deleteColumn);
//        System.out.println(falilyName + ":" + columnName + "is deleted!");
//    }
//
//    /*
//     * 删除指定的列
//     * 
//     * @tableName 表名
//     * 
//     * @rowKey rowKey
//     */
//    public static void deleteAllColumn(String tableName, String rowKey)
//            throws IOException {
//        HTable table = new HTable(conf, Bytes.toBytes(tableName));
//        Delete deleteAll = new Delete(Bytes.toBytes(rowKey));
//        table.delete(deleteAll);
//        System.out.println("all columns are deleted!");
//    }
//
//    /*
//     * 删除表
//     * 
//     * @tableName 表名
//     */
//    public static void deleteTable(String tableName) throws IOException {
//        HBaseAdmin admin = new HBaseAdmin(conf);
//        admin.disableTable(tableName);
//        admin.deleteTable(tableName);
//        System.out.println(tableName + "is deleted!");
//    }
//    
//	public static void main(String[] args) {
//		try {
////			deleteAllColumn("omsorder","1u1_o01_store1");
////			deleteAllColumn("omsorder","2u1_o01_store1");
////			deleteAllColumn("omsorder","9u1_o01_store1");
////			deleteAllColumn("omsorder","1u1_o01_ol0");
////			deleteAllColumn("omsorder","1u1_o01_ol1");
////			deleteAllColumn("omsorder","1u1_o01_ol2");
////			deleteAllColumn("omsorder","1u1_o01_ol3");
////			deleteAllColumn("omsorder","1u1_o01_ol4");
////			deleteAllColumn("omsorder","2u1_o01_ol0");
////			deleteAllColumn("omsorder","2u1_o01_ol1");
////			deleteAllColumn("omsorder","2u1_o01_ol2");
////			deleteAllColumn("omsorder","2u1_o01_ol3");
////			deleteAllColumn("omsorder","2u1_o01_ol4");
////			String[] column1=new String[]{"uid","oid","totalAmount","storeid"};
////			String[] value1=new String[]{"1","01","100","1"};
////			String rowKey=buildRowKey("u1_o01_store1");
////			String tableName="omsorder";
////			addData(rowKey, tableName, column1, value1);
////			getResultScann("omsorder","o01");
//			byte[][] bb=new byte[9][];
//			for(int i=0;i<9;i++){
//				bb[i]=Bytes.toBytes((i+1)+"");
//			}
//			creatTable("omsorder",new String[]{ "orderdetail", "orderline","orderlinedetail","ordershipment"},bb);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	private static String buildRowKey(String r) {
//		Random random = new Random();
//        int a=random.nextInt(10);
//		return a+r;
//	}
//}
