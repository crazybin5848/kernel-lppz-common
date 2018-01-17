package com.lppz.hive.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class HanaTest {
	private static String hanaDriverName = "com.sap.db.jdbc.Driver";

	public static void main(String[] args) throws SQLException {
		try {
			Class.forName(hanaDriverName);
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
			System.exit(1);
		}
		Connection conn = null;

		try {
			conn = DriverManager.getConnection("jdbc:sap://192.168.37.240:30015/?autocommit=true", "HDBMODEL",
					"Modleqasbw_1106");
		} catch (SQLException e) {
			System.err.println("Connection Failed. User/Passwd Error?");
			return;
		}
		System.out.println("Connection to HANA successful!");
		Statement stmt = conn.createStatement();
		ResultSet resultSet = stmt.executeQuery("select USER_ID,USER_NAME,USER_MODE from SYS.USERS");

		processResultSet(resultSet);
		stmt.close();
		conn.close();

	}

	public static void processResultSet(ResultSet rs) throws SQLException {
		while (rs.next()) {
			ResultSetMetaData rsmd = rs.getMetaData();
			int colCount = rsmd.getColumnCount(); // 字段个数
			for (int i = 1; i <= colCount; i++) {
				System.out.println(rsmd.getColumnName(i)+" : " + rs.getString(i) + " , ");				
			}
			System.out.println("\n");
		}
		rs.close();
	}
}
