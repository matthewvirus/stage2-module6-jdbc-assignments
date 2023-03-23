package jdbc;

import javax.sql.DataSource;

import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;

@Getter
@Setter
public class CustomDataSource implements DataSource {
    private static volatile CustomDataSource instance;
    private final String driver;
    private final String url;
    private final String name;
    private final String password;
    private static final Object lock = new Object();

    private CustomDataSource(String driver, String url, String password, String name) {
        this.driver = driver;
        this.url = url;
        this.password = password;
        this.name = name;
        instance = this;
    }

    public static CustomDataSource getInstance() {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    try {
                        Properties properties = new Properties();
                        properties.load(
                                CustomDataSource.class.getClassLoader().getResourceAsStream("app.properties")
                        );
                        instance = new CustomDataSource(
                                properties.getProperty("postgres.driver"),
                                properties.getProperty("postgres.url"),
                                properties.getProperty("postgres.name"),
                                properties.getProperty("postgres.password")

                        );
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return instance;
    }

    @Override
    public Connection getConnection() {
        return new CustomConnector().getConnection(url, name, password);
    }

    @Override
    public Connection getConnection(String s, String s1) {
        return new CustomConnector().getConnection(url, name, password);
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        throw new SQLException();
    }

    @Override
    public void setLogWriter(PrintWriter printWriter) throws SQLException {
        throw new SQLException();

    }

    @Override
    public void setLoginTimeout(int i) throws SQLException {
        throw new SQLException();

    }

    @Override
    public int getLoginTimeout() throws SQLException {
        throw new SQLException();
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public <T> T unwrap(Class<T> aClass) throws SQLException {
        throw new SQLException();
    }

    @Override
    public boolean isWrapperFor(Class<?> aClass) throws SQLException {
        throw new SQLException();
    }
}