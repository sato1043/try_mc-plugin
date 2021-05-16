package cc.updater.mc.testplugin;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

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

    @Getter
    @Setter(AccessLevel.PRIVATE)
    private String mysqlHost;
    @Getter
    @Setter(AccessLevel.PRIVATE)
    private String mysqlPort;
    @Getter
    @Setter(AccessLevel.PRIVATE)
    private String mysqlUser;
    @Getter
    @Setter(AccessLevel.PRIVATE)
    private String mysqlPassword;
    @Getter
    @Setter(AccessLevel.PRIVATE)
    private String mysqlDbName;
    @Getter
    @Setter(AccessLevel.PRIVATE)
    private int mysqlMaxConns;
    @Getter
    @Setter(AccessLevel.PRIVATE)
    private boolean mysqlUseSsl;

    @Getter(AccessLevel.PRIVATE)
    @Setter(AccessLevel.PRIVATE)
    private HikariConfig poolConfig = new HikariConfig();

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
    public static StorageReader createStorage(JavaPlugin plugin)
            throws AssertionError, ClassNotFoundException, IOException, SQLException {
        var type = plugin.getConfig().getString("storage.type");
        assert type != null && !type.isBlank();
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
    public static StorageReader createStorageBySqlite(JavaPlugin plugin)
            throws AssertionError, ClassNotFoundException, IOException, SQLException {

        new LibraryDownloader(plugin)
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
    public static StorageReader createStorageByMysql(JavaPlugin plugin)
            throws AssertionError, ClassNotFoundException, IOException, SQLException {

        new LibraryDownloader(plugin)
                .downloadHikariCp()
                .downloadMySql();

        var storage = new StorageReader();

        storage.setType(StorageType.MYSQL);

        var mysqlHost = plugin.getConfig().getString("storage.mysql.host");
        assert mysqlHost != null && !mysqlHost.isBlank();
//        var regexpHostString = PropertiesReader.getRegexpHost();
//        var regexpHostPattern = Pattern.compile(regexpHostString);
//        assert regexpHostPattern.matcher(mysqlHost).matches();

        var mysqlPort = plugin.getConfig().getString("storage.mysql.port");
        assert mysqlPort != null && !mysqlPort.isBlank();

        var mysqlUser = plugin.getConfig().getString("storage.mysql.user");
        assert mysqlUser != null && !mysqlUser.isBlank();

        var mysqlPassword = plugin.getConfig().getString("storage.mysql.password");
        assert mysqlPassword != null && !mysqlPassword.isBlank();

        var mysqlDbName = plugin.getConfig().getString("storage.mysql.db_name");
        assert mysqlDbName != null && !mysqlDbName.isBlank();

        var mysqlMaxConns = plugin.getConfig().getInt("storage.mysql.max_conns", 0);
        assert mysqlMaxConns > 0;

        var mysqlUseSsl = plugin.getConfig().getBoolean("storage.mysql.use_ssl", false);

        storage.setMysqlHost(mysqlHost);
        storage.setMysqlPort(mysqlPort);
        storage.setMysqlUser(mysqlUser);
        storage.setMysqlPassword(mysqlPassword);
        storage.setMysqlDbName(mysqlDbName);
        storage.setMysqlMaxConns(mysqlMaxConns);
        storage.setMysqlUseSsl(mysqlUseSsl);

        var config = storage.getPoolConfig();

        config.setJdbcUrl(
                "jdbc:mysql://" + mysqlHost + ":" + mysqlPort + "/" + mysqlDbName
                        + "?allowMultiQueries=true"
                        + "&autoReconnect=true"
                        + "&useSSL=" + (mysqlUseSsl ? "true" : "false")
        );

        config.setUsername(mysqlUser);
        config.setPassword(mysqlPassword);

        config.setMinimumIdle(1);
        config.setMaximumPoolSize(mysqlMaxConns);

        storage.open();

        return storage;
    }

    private StorageReader() {
    }

    private void open() throws AssertionError, IOException, SQLException {

        var config = getPoolConfig();
        config.setPoolName(D.PROJECT_NAME + "-Connection-Pool");
        config.addDataSourceProperty("useUnicode", "true");
        config.addDataSourceProperty("characterEncoding", "utf-8");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        setDataSource(new HikariDataSource(getPoolConfig()));

        try {
            getConnection().close(); // test connection
        } catch (Exception exception) {
            close();
            throw exception;
        }
    }

    public void close() {
        getDataSource().close();
    }

    public Connection getConnection() throws SQLException {
        return getDataSource().getConnection();
    }

}
