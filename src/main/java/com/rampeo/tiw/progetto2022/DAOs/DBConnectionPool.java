package com.rampeo.tiw.progetto2022.DAOs;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DBConnectionPool {

    private static volatile DataSource dataSource = null;

    protected static Connection getConnection() throws SQLException {
        if (dataSource == null) {
            synchronized (DBConnectionPool.class) {
                if (dataSource == null) {
                    try {
                        dataSource = (DataSource) new InitialContext().lookup("java:/comp/env/jdbc/DBConnectionPool");
                    } catch (NamingException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return dataSource.getConnection();
    }
}