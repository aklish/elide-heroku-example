/*
 * Copyright 2019, Yahoo Inc.
 * Licensed under the Apache License, Version 2.0
 * See LICENSE file in project root for terms.
 */
package example;

import com.yahoo.elide.standalone.ElideStandalone;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import static example.Settings.JDBC_PASSWORD;
import static example.Settings.JDBC_URL;
import static example.Settings.JDBC_USER;

/**
 * Base class for running a set of functional Elide tests.  This class
 * sets up an Elide instance with an in-memory H2 database.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class IntegrationTest {
    private ElideStandalone elide;

    @BeforeAll
    public void init() throws Exception {
        Settings settings = new Settings(true) {
            @Override
            public int getPort() {
                return 8080;
            }
        };

        elide = new ElideStandalone(settings);

        settings.runLiquibaseMigrations();

        elide.start(false);
    }

    protected Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
    }

    @AfterAll
    public void shutdown() throws Exception {
        elide.stop();
    }
}
