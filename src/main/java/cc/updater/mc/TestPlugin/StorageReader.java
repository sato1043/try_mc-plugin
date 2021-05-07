package cc.updater.mc.TestPlugin;

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

public class StorageReader {

    public enum StorageType {SQLITE, MYSQL}

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


    public static StorageReader createStorage(JavaPlugin plugin)
            throws AssertionError, ClassNotFoundException, IOException, SQLException {
        var type = plugin.getConfig().getString("storage.type");
        assert type != null && !type.isBlank();
        switch (type.trim().toLowerCase()) {
            case "sqlite":
                return createStorageBySQLite(plugin);
            case "mysql":
                return createStorageByMySQL(plugin);
        }
        assert false;
        return null;
    }

    public static StorageReader createStorageBySQLite(JavaPlugin plugin)
            throws AssertionError, ClassNotFoundException, IOException, SQLException {

        Class.forName("org.sqlite.JDBC");

        var storage = new StorageReader();

        storage.setType(StorageType.SQLITE);

        var config = storage.getPoolConfig();

        config.setJdbcUrl(
                "jdbc:sqlite:"
                        + plugin.getDataFolder().getAbsolutePath()
                        + File.separator
                        + PropertiesReader.getStorageSqLiteFilename()
        );

        config.setConnectionTestQuery("SELECT 1;");
        config.setMaximumPoolSize(1);

        storage.open();

        return storage;
    }

    public static StorageReader createStorageByMySQL(JavaPlugin plugin)
            throws AssertionError, ClassNotFoundException, IOException, SQLException {

        Class.forName("com.mysql.jdbc");

        var storage = new StorageReader();

        storage.setType(StorageType.MYSQL);

        var config = storage.getPoolConfig();

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

        config.setJdbcUrl(
                "jdbc:mysql://" + mysqlHost + ":" + mysqlPort + "/" + mysqlDbName +
                        "?allowMultiQueries=true" +
                        "&autoReconnect=true" +
                        "&useSSL=" + (mysqlUseSsl ? "true" : "false")
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
        config.setPoolName(PropertiesReader.getProjectName() + "-Connection-Pool");
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
