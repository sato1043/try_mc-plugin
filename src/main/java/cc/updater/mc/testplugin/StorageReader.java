package cc.updater.mc.testplugin;

import com.google.common.base.Strings;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.regex.Pattern;

/**
 * Manage storage sessions.
 */
public class StorageReader {

    /**
     * Enum for Storage Type.
     */
    public enum StorageType {
        SQLITE, MYSQL
    }

    @Getter
    @Setter(AccessLevel.PRIVATE)
    private StorageType type;

    @Getter(AccessLevel.PRIVATE)
    @Setter(AccessLevel.PRIVATE)
    private HikariConfig poolConfig = null;

    @Getter(AccessLevel.PRIVATE)
    @Setter(AccessLevel.PRIVATE)
    private HikariDataSource dataSource = null;


    /**
     * Creates StorageReader.
     *
     * @param plugin to apply to StorageReader.
     * @return Instance of StorageReader.
     * @throws AssertionError         causes when one of params is null.
     * @throws ClassNotFoundException causes when JDBC can not autoload.
     * @throws IOException
     * @throws SQLException
     */
    public static StorageReader create(TestPlugin plugin)
            throws AssertionError, ClassNotFoundException, IOException, SQLException {
        var type = plugin.getConfig().getString("storage.type");
        assert !Strings.isNullOrEmpty(type);
        switch (type.trim().toLowerCase()) {
            case "sqlite":
                return createStorageBySqlite(plugin);
            case "mysql":
                return createStorageByMysql(plugin);
            default:
                assert false;
        }
        return null;
    }

    /**
     * Creates StorageReader of SQLite.
     *
     * @param plugin to apply to StorageReader.
     * @return Instance of StorageReader.
     * @throws AssertionError         causes when one of params is null.
     * @throws ClassNotFoundException causes when JDBC can not autoload.
     * @throws IOException
     * @throws SQLException
     */
    public static StorageReader createStorageBySqlite(TestPlugin plugin)
            throws AssertionError, ClassNotFoundException, IOException, SQLException {

        plugin.getLibraryDownloader()
                .downloadHikariCp()
                .downloadSqlite();

        var storage = new StorageReader();

        storage.setType(StorageType.SQLITE);

        var config = storage.getPoolConfig();

        config.setJdbcUrl(
                "jdbc:sqlite:"
                        + plugin.getDataFolder().getAbsolutePath()
                        + File.separator
                        + D.STORAGE_SQLITE_FILENAME
        );

        config.setConnectionTestQuery("SELECT 1;");
        config.setMaximumPoolSize(1);

        storage.open();

        return storage;
    }

    /**
     * Creates StorageReader of MySQL.
     *
     * @param plugin to apply to StorageReader.
     * @return Instance of StorageReader.
     * @throws AssertionError         causes when one of params is null.
     * @throws ClassNotFoundException causes when JDBC can not autoload.
     * @throws IOException
     * @throws SQLException
     */
    public static StorageReader createStorageByMysql(TestPlugin plugin)
            throws AssertionError, ClassNotFoundException, IOException, SQLException {

        plugin.getLibraryDownloader()
                .downloadHikariCp()
                .downloadMySql();

        var storage = new StorageReader();

        storage.setType(StorageType.MYSQL);

        var mysqlHost = plugin.getConfig().getString("storage.mysql.host");
        assert !Strings.isNullOrEmpty(mysqlHost);
        var regexpHostPattern = Pattern.compile(D.REGEXP_HOST);
        assert regexpHostPattern.matcher(mysqlHost).matches();

        var mysqlPort = plugin.getConfig().getString("storage.mysql.port");
        assert !Strings.isNullOrEmpty(mysqlPort);
        var port = Integer.parseInt(mysqlPort);
        assert 0 <= port && port <= 65535;

        var mysqlUser = plugin.getConfig().getString("storage.mysql.user");
        assert !Strings.isNullOrEmpty(mysqlUser);

        var mysqlPassword = plugin.getConfig().getString("storage.mysql.password");
        assert !Strings.isNullOrEmpty(mysqlPassword);

        var mysqlDbName = plugin.getConfig().getString("storage.mysql.db_name");
        assert !Strings.isNullOrEmpty(mysqlDbName);

        var mysqlTablePrefix = plugin.getConfig().getString("storage.mysql.table_prefix");
        assert !Strings.isNullOrEmpty(mysqlTablePrefix);

        var mysqlConnTimeoutMs = plugin.getConfig().getInt("storage.mysql.conn_timeout_ms", 5000);
        assert 500 < mysqlConnTimeoutMs && mysqlConnTimeoutMs < 30000;

        var mysqlMaxConns = plugin.getConfig().getInt("storage.mysql.max_conns", 0);
        assert 0 < mysqlMaxConns && mysqlMaxConns < 100;

        var mysqlMaxLifetimeMs = plugin.getConfig().getInt("storage.mysql.max_lifetime_ms", 0);
        assert 0 < mysqlMaxLifetimeMs;

        var config = storage.getPoolConfig();

        config.setPoolName(D.PROJECT_NAME + "-Connection-Pool");

        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setJdbcUrl("jdbc:mysql://" + mysqlHost + ":" + mysqlPort + "/" + mysqlDbName);

        config.addDataSourceProperty("serverTimezone", "UTC"); // Necessary to run tests

        config.addDataSourceProperty("autoReconnect", "true");
        config.addDataSourceProperty("allowMultiQueries", "true"); // Support multiple queries, used to create tables

        config.addDataSourceProperty("useUnicode", "true"); // Forcing the use of unicode
        config.addDataSourceProperty("characterEncoding", "utf-8");

        config.setUsername(mysqlUser);
        config.setPassword(mysqlPassword);

        config.setMinimumIdle(1);
        config.setMaximumPoolSize(mysqlMaxConns);
        config.setMaxLifetime(mysqlMaxLifetimeMs);
        config.setConnectionTimeout(mysqlConnTimeoutMs);

        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.addDataSourceProperty("useServerPrepStmts", "true");
        config.addDataSourceProperty("useLocalSessionState", "true");
        config.addDataSourceProperty("useLocalTransactionState", "true");

        var mysqlUseSsl = plugin.getConfig().getBoolean("storage.mysql.use_ssl", true);
        config.addDataSourceProperty("useSSL", Boolean.toString(mysqlUseSsl));

        // jdbi = Jdbi.create(dataSource);
        // jdbi.installPlugin(new SqlObjectPlugin());
        // jdbi.define("prefix", tablePrefix);

        storage.open();

        return storage;
    }

    private StorageReader() {
        setPoolConfig(new HikariConfig());
    }

    private void open() throws AssertionError, SQLException {

        setDataSource(new HikariDataSource(getPoolConfig()));

        try {
            getConnection().close(); // test connection
        } catch (Exception exception) {
            close();
            throw exception;
        }
    }

    /**
     * Closes connections.
     */
    public void close() {
        if (getDataSource() != null) {
            getDataSource().close();
        }
    }

    /**
     * Gets a connection.
     *
     * @return connection
     * @throws SQLException causes when failed to get connection.
     */
    public Connection getConnection() throws SQLException {
        return getDataSource() == null ? null : getDataSource().getConnection();
    }

}
