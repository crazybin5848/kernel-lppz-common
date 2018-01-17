package com.lppz.hive.spark;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.lppz.hive.bean.HiveSqoopConfigBean;
import com.lppz.hive.util.HiveUtil;

public class SparkSqlUtil {
  public static void main(String[] args) {
	  docalcWithSpark(true);
	  docalcWithSpark(false);
}

private static void docalcWithSpark(boolean mark) {
	HiveSqoopConfigBean bean=new HiveSqoopConfigBean();
	if(mark)
	bean.setHive2jdbcUrl("jdbc:hive2://10.6.30.36:10000/hivetest");
	else
	bean.setHive2jdbcUrl("jdbc:hive2://10.6.30.37:10000/hivetest");
	bean.setHive2jdbcUser("hadoop");
	bean.setHive2jdbcPasswd("111111");
	Statement st=null;
	try {
		st=HiveUtil.buildHiveStatement(bean);
		long time=System.currentTimeMillis();
		st.execute("use hivetest");
		ResultSet rs=st.executeQuery("select count(*) as c from omsstockhive"); 
		while(rs.next()){
			int c=rs.getInt("c");
			System.out.println(c);
		}
		System.out.println((mark?"spark":"hadoop")+" useTime:"+(System.currentTimeMillis()-time)+"ms");
	} catch (SQLException e) {
		e.printStackTrace();
	}
}
}
