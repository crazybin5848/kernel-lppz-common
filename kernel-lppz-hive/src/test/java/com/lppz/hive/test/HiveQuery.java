package com.lppz.hive.test;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
 
public class HiveQuery {
  private static String driverName = "org.apache.hive.jdbc.HiveDriver";
 
  /**
   * @param args
   * @throws SQLException
   */
  public static void main(String[] args) throws SQLException {
      try {
      Class.forName(driverName);
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
      System.exit(1);
    }
    Connection con = DriverManager.getConnection("jdbc:hive2://10.8.102.204:11000/default", "hadoop", "hadoop111");
    Statement stmt = con.createStatement();
    String sql = "select * from omsextorders limit 10";
    System.out.println("Running: " + sql);
    String useDB = "use omsext";
    stmt.executeUpdate(useDB);
    ResultSet rs = stmt.executeQuery(sql);
    ResultSetMetaData md = rs.getMetaData(); //得到结果集(rs)的结构信息，比如字段数、字段名等 
    int columnCount = md.getColumnCount(); //返回此 ResultSet 对象中的列数
    List<Map<String,Object>> list = new ArrayList<>();
    Map<String,Object> rowData = null;
    while (rs.next()) {   
     rowData = new HashMap<>(columnCount);   
     for (int i = 1; i <= columnCount; i++) {   
          rowData.put(md.getColumnName(i), rs.getObject(i));   
     }   
     list.add(rowData);   
    }   
    System.out.println("list:" + list.toString());
    System.out.println("list size:" + list.size());
  }
}