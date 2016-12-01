package com.sainsburys;

import java.sql.*;


public class HiveJdbcTest {

    public static void main(String[] args) throws SQLException {

        Connection conn = null;
        Statement stmt = null;
        Statement stmt2 = null;
        ResultSet res1 = null;
        ResultSet res2 = null;

        try {
            Class.forName("org.apache.hive.jdbc.HiveDriver");
            //Note that HDInsight always uses port 443 for SSL secure connections, and the port forwarder listening to 443
            // will direct it to the hiveserver2 from there on port 10001.
            String connectionQuery = "jdbc:hive2://YourClusterName.azurehdinsight.net:443/default;ssl=true?hive.server2.transport.mode=http;hive.server2.thrift.http.path=/hive2";

            conn = DriverManager.getConnection(connectionQuery, "ClusterUserId", "YourPassword");
            stmt = conn.createStatement();

            String sql = null;
            res1 = null;

            //create an external table
            String tableName = "hivesampletablederived";
            sql = "drop table if exists " + tableName;
            stmt.execute(sql);


            String tableLocation = "wasb://azimhdi32@azimasvwest.blob.core.windows.net/hivesampletablederived";
            sql = "CREATE EXTERNAL TABLE hivesampletablederived(querytime string, market string, deviceplatform string, devicemodel string, state string, country string)" +
                    "ROW FORMAT DELIMITED FIELDS TERMINATED BY ' '" +
                    "STORED AS TEXTFILE LOCATION '" + tableLocation + "'";
            stmt.execute(sql);

            // describe table
            sql = "describe " + tableName;
            System.out.println("Running: " + sql);
            res1 = stmt.executeQuery(sql);
            while (res1.next()) {
                System.out.println(res1.getString(1) + "\t" + res1.getString(2));
            }
            //stmt.close();

            //insert data into the new table
            sql = "INSERT INTO TABLE hivesampletablederived " +
                    "SELECT querytime, market, deviceplatform, devicemodel, state, country from hivesampletable LIMIT 10";
            System.out.println("Running: " + sql);
            stmt.execute(sql);

            // run a select query
            sql = "SELECT querytime, market, deviceplatform, devicemodel, state, country from " + tableName + " LIMIT 3";
            stmt2 = conn.createStatement();
            System.out.println("Running: " + sql);

            res2 = stmt2.executeQuery(sql);

            while (res2.next()) {
                System.out.println(res2.getString(1) + "\t" + res2.getString(2) + "\t" + res2.getString(3) + "\t" + res2.getString(4) + "\t" + res2.getString(5) + "\t" + res2.getString(6));
            }


            System.out.println("Hive queries completed successfully!");
        } catch (SQLException e) {
            e.getMessage();
            e.printStackTrace();
            System.exit(1);//
        } catch (Exception ex) {
            ex.getMessage();
            ex.printStackTrace();
            System.exit(1);//

        } finally {
            if (res1 != null) res1.close();
            if (res2 != null) res2.close();
            if (stmt != null) stmt.close();
            if (stmt2 != null) stmt2.close();
        }

    }
}