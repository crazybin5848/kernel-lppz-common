package com.lppz.hive.test;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import com.lppz.hive.util.HiveUtil;
 
public class HiveCrTable {
  private static String driverName = "org.apache.hive.jdbc.HiveDriver";
 
  /**
   * @param args
   * @throws SQLException
   */
  public static void main(String[] args) throws SQLException {
//      try {
//      Class.forName(driverName);
//    } catch (ClassNotFoundException e) {
//      e.printStackTrace();
//      System.exit(1);
//    }
//      String tableName = "omsstock";
//     String crSql=HiveUtil.getHiveSqlFromMysql(tableName);
//    //replace "hive" here with the name of the user the queries should run as
//    Connection con = DriverManager.getConnection("jdbc:hive2://10.6.30.37:10000/default", "hadoop", "111111");
//    Statement stmt = con.createStatement();
//    stmt.execute("drop table if exists " + tableName);
//    stmt.execute(crSql);
  }
}