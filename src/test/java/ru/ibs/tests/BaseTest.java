package ru.ibs.tests;

import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import ru.ibs.steps.DataBaseSteps;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

public class BaseTest {

    static DataSource dataSource;
    protected DataBaseSteps dataBase;


    /**
     * Выполняется перед всеми тестами
     */
    @BeforeAll
    static void beforeAll() {
        Properties dbProperties = new Properties();

        try {
            dbProperties.load(new BufferedReader(
                    new InputStreamReader(BaseTest.class.getResourceAsStream("/db.properties"))));
        } catch (IOException e) {
            throw new IllegalArgumentException("Не удалось загрузить файл db.properties", e);
        }

        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setPassword(dbProperties.getProperty("db.password"));
        hikariDataSource.setUsername(dbProperties.getProperty("db.username"));
        hikariDataSource.setJdbcUrl(dbProperties.getProperty("db.url"));

        dataSource = hikariDataSource;

    }

    /**
     * Выполняется перед каждым тестом
     */
    @BeforeEach
    public void before() {

        dataBase = DataBaseSteps.getInstance(dataSource);
    }

    /**
     * Выполняется после каждого теста
     */
    @AfterEach
    public void after() {
        dataBase = null;
    }


    /**
     * Выполняется после всех тестов
     */
    @AfterAll
    public static void afterAll() {
        dataSource = null;
    }
}
