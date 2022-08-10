package com.rampeo.tiw.progetto2022.DAOs;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class AbstractDAO implements AutoCloseable {
    private final Connection connection;

    protected AbstractDAO() throws SQLException {
        connection = DBConnectionPool.getConnection();
    }

    Connection getConnection() {
        return connection;
    }

    @Override
    public void close() throws SQLException {
        connection.close();
    }
}
