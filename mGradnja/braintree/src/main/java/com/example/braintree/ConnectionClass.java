package com.example.braintree;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionClass {
    String url = "jdbc:jtds:sqlserver://mgradnja-server.database.windows.net:1433/mGradnja_db";
    String driver = "net.sourceforge.jtds.jdbc.Driver";
    String db = "mG_admin@mgradnja-server";
    String password = "mGradnja2019";

    @SuppressLint("NewApi")
    public Connection CONN() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection conn = null;
        try {

            Class.forName(driver);
            conn = DriverManager.getConnection(url, db, password);

        } catch (SQLException se) {
            Log.e("ERRO", se.getMessage());
        } catch (ClassNotFoundException e) {
            Log.e("ERRO", e.getMessage());
        } catch (Exception e) {
            Log.e("ERRO", e.getMessage());
        }
        return conn;
    }
}
