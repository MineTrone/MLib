package com.minetrone.mlib.database;

import java.sql.Connection;

/**
 * Database interface
 */
public interface Database {

    /**
     * Connection of SQL
     * @return Connection
     */
    Connection getConnection();

    /**
     * Initialize the sql
     */
    void initSQL(String query);

    void createTables(String query);
}