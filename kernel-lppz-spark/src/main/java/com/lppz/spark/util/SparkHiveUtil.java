package com.lppz.spark.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.sql.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lppz.spark.bean.HivePartionCol;
import com.lppz.spark.bean.Rdbms2HDfsBean;
import com.lppz.spark.bean.SparkHiveSqlBean;
import com.lppz.spark.bean.SparkMysqlDmlBean;
import com.lppz.spark.bean.SparkSqlConfigBean;
import com.lppz.spark.scala.HiveContextUtil;

public class SparkHiveUtil {
	private static final Logger LOG = LoggerFactory
			.getLogger(SparkHiveUtil.class);

	private static String getHiveSqlFromMysql(SparkHiveSqlBean sparkBean) throws Exception {
		Connection conn = null;
		String sql;
		SparkSqlConfigBean bean = sparkBean.getConfigBean();
		SparkMysqlDmlBean mysqlbean = sparkBean.getMysqlBean();
		Rdbms2HDfsBean hivebean = sparkBean.getSourcebean();
		String url = bean.getRdbmsjdbcUrl();
		try {
			Class.forName(bean.getRdbmsdbDriver());
			conn = DriverManager.getConnection(url, bean.getRdbmsjdbcUser(),
					bean.getRdbmsjdbcPasswd());
			Statement stmt = conn.createStatement();
			String tableName = mysqlbean.getTableName();
			sql = "desc " + tableName + ";";
			ResultSet rs = stmt.executeQuery(sql);
			StringBuilder ddlSql = new StringBuilder("create table "
					+ hivebean.getHivetableName() + "(");
			while (rs.next()) {
				ddlSql.append(rs.getString(1)).append(" ");
				String type = rs.getString(2);
				ddlSql.append(convert(type)).append(",");
			}
			String s = ddlSql.substring(0, ddlSql.length() - 1) + ")";
			if (CollectionUtils.isNotEmpty(hivebean.getHpcList())) {
				s += "\r\n PARTITIONED BY(";
				int k = 0;
				for (HivePartionCol hpc : hivebean.getHpcList()) {
					s += hpc.getCol() + " " + hpc.getType();
					if (k++ < hivebean.getHpcList().size() - 1)
						s += ",";
				}
				s += ")";
			}
			s += "\r\n row format delimited fields terminated by '\\t'";
			return s;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw e;
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
			}
		}
	}

	private static String convert(String type) {
		type = type.toLowerCase();
		if (type.contains("bit") || type.contains("tinyint"))
			return "tinyint";
		if (type.contains("smallint"))
			return "smallint";
		if (type.contains("mediumint") || type.contains("int")
				|| type.contains("integer"))
			return "int";
		if (type.contains("bigint"))
			return "bigint";
		if (type.contains("decimal"))
			return type;
		if (type.contains("double") || type.contains("numeric"))
			return "double";
		if (type.contains("float"))
			return "float";
		if (type.contains("varchar"))
			return type;
		if (type.contains("char"))
			return type;
		if (type.contains("text"))
			return "string";
		if (type.contains("binary"))
			return "binary";
		if (type.contains("datetime") || type.contains("timestamp"))
			return "timestamp";
		if (type.contains("date"))
			return "date";
		return type;
	}

	public static void createHiveTableFromRDBMS(SparkHiveSqlBean sparkBean,
			String mode,SparkContext sc) throws SQLException {
		boolean boo=sc==null;
		if(boo){
		SparkConf conf = new SparkConf();
		conf.setMaster(mode);
		conf.setAppName("createHiveTable:"
				+ sparkBean.getSourcebean().getHiveschema() + ":"
				+ sparkBean.getSourcebean().getHivetableName() + now());
		sc = new SparkContext(conf);
		}
		try {
			String ddl = sparkBean.getSourcebean().getDdl();
			String crSql = StringUtils.isNotBlank(ddl) ? ddl
					: getHiveSqlFromMysql(sparkBean);
			String tableName = sparkBean.getSourcebean().getHivetableName();
//			hcu.exec(sc, "create schema IF NOT EXISTS "
//					+ sparkBean.getSourcebean().getHiveschema());
//			LOG.info("create schema IF NOT EXISTS "
//					+ sparkBean.getSourcebean().getHiveschema());
			HiveContextUtil.exec(sc, "use " + sparkBean.getSourcebean().getHiveschema());
			LOG.info("use " + sparkBean.getSourcebean().getHiveschema());
			HiveContextUtil.exec(sc, "drop table if exists " + tableName);
			LOG.info("drop table if exists " + tableName);
			HiveContextUtil.exec(sc, crSql);
			LOG.info(crSql);
		} catch (Exception e) {
			LOG.error(e.getMessage(),e);
		} finally {
			if(boo)
			sc.stop();
		}
	}

	public static void doLoad2Hive(Rdbms2HDfsBean hivebean, String mode,
			String fileDir,SparkContext sc,boolean overWrite) {
		boolean boo=sc==null;
		if(boo){
		SparkConf conf = new SparkConf();
		conf.setMaster(mode);
		conf.setAppName("mysqlhiveload:" + hivebean.getHiveschema() + ":"
				+ hivebean.getHivetableName() + now());
		sc = new SparkContext(conf);
		}
		try {
			String sql = "load data inpath '" + fileDir
					+ "'"+(overWrite?" overwrite":" ")+" into table " + hivebean.getHivetableName();
			if (CollectionUtils.isNotEmpty(hivebean.getHpcList())) {
				sql += " PARTITION (";
				int k = 0;
				for (HivePartionCol hpc : hivebean.getHpcList()) {
					sql += hpc.getCol() + "=" + hpc.getValue();
					if (k++ < hivebean.getHpcList().size() - 1)
						sql += ",";
				}
				sql += ")";
			}
			LOG.info(sql);
			HiveContextUtil.exec(sc, "use " + hivebean.getHiveschema());
			HiveContextUtil.exec(sc, sql);
		} catch (Exception e) {
			LOG.error(e.getMessage(),e);
		}
		finally{
			if(boo)
			sc.stop();
		}
	}
	
	public static String now() {
		return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
	}

	public static void parse(SparkHiveSqlBean shsb, String[] args) {
		String url = shsb.getConfigBean().getRdbmsjdbcUrl()
				.replaceAll("#schema#", shsb.getConfigBean().getSchema());
		shsb.getConfigBean().setRdbmsjdbcUrl(url);
		if (CollectionUtils.isNotEmpty(shsb.getSourcebean().getHpcList())) {
			String[] ss = args[args.length - 2].split(",;");
			int k = 0;
			for (HivePartionCol hpc : shsb.getSourcebean().getHpcList()) {
				String val = hpc.getValue().replaceAll("#" + ss[k] + "#",
						ss[k + 1]);
				k += 2;
				hpc.setValue(val);
			}
		}
		String sql = shsb.getMysqlBean().getSql();
		if (sql.contains("#")) {
			String[] parmas = args[args.length - 1].split(",;");
			for (int i = 0; i < parmas.length; i += 2) {
				sql = sql.replaceAll("#" + parmas[i] + "#", parmas[i + 1]);
			}
			shsb.getMysqlBean().setSql(sql);
		}
	}

	public static Map<String, Object> buildString(List<Row> list, int oidIdx) {
		Map<String, Object> map = new HashMap<String, Object>(2);
		StringBuilder sb = new StringBuilder("");
		List<String> listOid = new ArrayList<String>();
		for (Row r : list) {
			for (int i = 0; i < r.length(); i++) {
				Object o = r.get(i);
				if (o instanceof String) {
					o = ((String) o).replaceAll("\n", " ");
					o = ((String) o).replaceAll("\t", " ");
				}
				sb.append(o);
				if (i < r.length() - 1)
					sb.append("\t");
				if (i == oidIdx)
					listOid.add((String) o);
			}
			sb.append("\n");
		}
		map.put("sb", sb);
		map.put("listOid", listOid);
		return map;
	}
	
	public static String[] buildStringArray(List<String> oidList,int numPartitions,String relateKey,String... tbName){
		String ssk=null;
		if(tbName==null||tbName.length==0)
			ssk=relateKey;
		else
			ssk="delete from "+tbName[0]+" where `"+relateKey+"`";
		String[] colArray=null;
		int x = oidList.size() / numPartitions;
		if (oidList.size() < numPartitions) {
			StringBuilder sb = new StringBuilder(ssk).append(" in(");
			for(String s:oidList){
				if(tbName==null||tbName.length==0)
				sb.append(s).append(",");
				else
				sb.append("'").append(s.replaceAll("'", "")).append("'").append(",");
			}
			colArray=new String[]{sb.substring(0, sb.length() - 1) + ")"};
			return colArray;
		}
		if(oidList.size()%numPartitions>0)
		numPartitions=oidList.size()/x+1;
		colArray=new String[numPartitions];
		for (int i = 0,k=0; i < oidList.size();) {
			try {
				List<String> ls = oidList.subList(i,
						i + x > oidList.size() ? oidList.size() : i + x);
				i += x;
				StringBuilder sb = new StringBuilder(ssk).append(" in(");
				for (String s : ls){
					if(tbName==null||tbName.length==0)
						sb.append(s).append(",");
					else
						sb.append("'").append(s.replaceAll("'", "")).append("'").append(",");
				}
				colArray[k++]=sb.substring(0, sb.length() - 1) + ")";
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return colArray;
	}
	
	public static String[] buildStringArray(String[] oidArray,int numPartitions,String relateKey,String... tbName){
		List<String> oidList=Arrays.asList(oidArray);
		String[] ss = buildStringArray(oidList, numPartitions, relateKey, tbName);
		return ss;
	}
}