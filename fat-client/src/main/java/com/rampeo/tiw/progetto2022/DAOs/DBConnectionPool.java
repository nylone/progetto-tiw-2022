package com.rampeo.tiw.progetto2022.DAOs;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import java.sql.Connection;
import java.sql.SQLException;

public class DBConnectionPool {

    private static volatile DataSource dataSource = null;

    protected static Connection getConnection() throws SQLException {
        if (dataSource == null) {
            synchronized (DBConnectionPool.class) {
                if (dataSource == null) {

                    PoolProperties p = new PoolProperties();
                    p.setDriverClassName("com.mysql.cj.jdbc.Driver");
                    p.setUrl("jdbc:mysql://localhost:3306/progetto2022?serverTimezone=UTC&autoReconnect=true");
                    p.setUsername("progetto2022_user");
                    p.setPassword("progetto2022_pass");
                    p.setMaxActive(100);
                    p.setInitialSize(10);

                    dataSource = new DataSource();
                    dataSource.setPoolProperties(p);
                }
            }
        }
        return dataSource.getConnection();
    }
}