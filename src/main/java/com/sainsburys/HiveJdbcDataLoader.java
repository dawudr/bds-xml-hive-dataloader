package com.sainsburys;

import java.sql.*;


public class HiveJdbcDataLoader {

    private Connection conn = null;
    private String tableName = "testdataload";

    public HiveJdbcDataLoader() {

        try {
            Class.forName("org.apache.hive.jdbc.HiveDriver");
            //Note that HDInsight always uses port 443 for SSL secure connections, and the port forwarder listening to 443
            // will direct it to the hiveserver2 from there on port 10001.
            String connectionQuery = "jdbc:hive2://192.168.99.100:10000/default";
            conn = DriverManager.getConnection(connectionQuery, "cloudera", "cloudera");

        } catch (SQLException e) {
            e.getMessage();
            e.printStackTrace();
            System.out.println("Unable to connect to database and execute sql");
        } catch (ClassNotFoundException e) {
            e.getMessage();
            e.printStackTrace();
            System.out.println("Unable to find to locate database driver class");
        }
    }

    public void cleanup() throws SQLException {
        if (conn != null) conn.close();
    }


    public void insertIntoHive(String merchandiseHierarchy, String itemID, String description, String regularSalesUnitPrice, String quantity) throws SQLException {

        Statement stmt = null;
        ResultSet res1 = null;
        try {
            stmt = this.conn.createStatement();

            String sql = null;
            res1 = null;

            //insert data into the new table
            sql = "INSERT INTO TABLE " + tableName + " " +
                    "VALUES ('" + merchandiseHierarchy + "', '" + itemID + "', '" + description + "', '" + regularSalesUnitPrice + "', '" + quantity + "')";
            System.out.println("Running: " + sql);
            stmt.execute(sql);
        } catch (SQLException e) {
            e.getMessage();
            e.printStackTrace();
            System.out.println("Unable to connect to database and execute sql");
        } catch (Exception ex) {
            ex.getMessage();
            ex.printStackTrace();
            System.out.println("Unable to complete database query");
        } finally {
            if (res1 != null) res1.close();
            if (stmt != null) stmt.close();
        }
    }


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
            String connectionQuery = "jdbc:hive2://192.168.99.100:10000/default";

            conn = DriverManager.getConnection(connectionQuery, "cloudera", "cloudera");
            stmt = conn.createStatement();

            String sql = null;
            res1 = null;

            //create an external table
            String tableName = "testdataload";
            sql = "drop table if exists " + tableName;
            stmt.execute(sql);


//            String tableLocation = "wasb://azimhdi32@azimasvwest.blob.core.windows.net/testdataloadcsv";
            sql = "CREATE EXTERNAL TABLE IF NOT EXISTS " + tableName + "(MerchandiseHierarchy string, ItemID string, Description string, RegularSalesUnitPrice string, Quantity string)" +
                    "ROW FORMAT DELIMITED FIELDS TERMINATED BY ' '" +
                    "STORED AS TEXTFILE";
//                    "STORED AS TEXTFILE LOCATION '" + tableLocation + "'";
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
            sql = "INSERT INTO TABLE " + tableName + " " +
                    "VALUES ('AMerchandiseHierarchy', 'AItemID', 'ADescription', 'ARegularSalesUnitPrice', 'AQuantity' )," +
                    "('BMerchandiseHierarchy', 'BItemID', 'BDescription', 'BRegularSalesUnitPrice', 'BQuantity' )";
            System.out.println("Running: " + sql);
            stmt.execute(sql);

            // run a select query
            sql = "SELECT MerchandiseHierarchy, ItemID, Description, RegularSalesUnitPrice, Quantity from " + tableName + " LIMIT 3";
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
            System.out.println("Unable to connect to database and execute sql");
        } catch (Exception ex) {
            ex.getMessage();
            ex.printStackTrace();
            System.out.println("Unable to complete database query");
        } finally {
            if (res1 != null) res1.close();
            if (res2 != null) res2.close();
            if (stmt != null) stmt.close();
            if (stmt2 != null) stmt2.close();
        }

    }
}