package cc.updater.mc.testplugin;

import com.google.common.base.Strings;
import lombok.Getter;
import lombok.NonNull;
import net.byteflux.libby.BukkitLibraryManager;
import net.byteflux.libby.Library;
import net.byteflux.libby.logging.LogLevel;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

/**
 * Downloads jar files and relocate them.
 */
public class LibraryDownloader {

    @Getter
    private final BukkitLibraryManager libraryManager;

    /**
     * Creates LibraryDownloader.
     *
     * @param plugin JavaPlugin to apply to BukkitLibraryManager
     * @return Instance of LibraryDownloader
     * @throws NullPointerException causes when plugin_ is null.
     */
    public static LibraryDownloader create(@NonNull JavaPlugin plugin) throws NullPointerException {
        return new LibraryDownloader(plugin);
    }

    private LibraryDownloader(@NonNull JavaPlugin plugin) throws NullPointerException {
        libraryManager = new BukkitLibraryManager(plugin);
        libraryManager.setLogLevel(LogLevel.DEBUG);
        libraryManager.addMavenCentral();
        libraryManager.addRepository(D.REPOSITORY_SPIGOT_REPO);
        libraryManager.addRepository(D.REPOSITORY_SONATYPE);

        ClassLoader cl = StorageReader.class.getClassLoader();
        while (cl != null) {
            plugin.getLogger().info(cl.getClass().getName());
            cl = cl.getParent();
        }
    }

    private String getRelocatedPackageName(@NonNull String originalPackageName)
            throws NullPointerException {
        return D.PROJECT_GROUP_ID + ".libs." + originalPackageName;
    }

    /**
     * Downloads jar files and relocate them.
     *
     * @param groupId     to download.
     * @param artifactId  to download.
     * @param version     to download.
     * @param checkSum    to download.
     * @param relocations to download.
     * @return Instance of itself.
     * @throws AssertionError causes when one or more of params is null or empty.
     */
    public LibraryDownloader download(String groupId, String artifactId,
                                      String version, String checkSum,
                                      String... relocations)
            throws AssertionError {

        assert !Strings.isNullOrEmpty(groupId);
        assert !Strings.isNullOrEmpty(artifactId);
        assert !Strings.isNullOrEmpty(version);
        assert !Strings.isNullOrEmpty(checkSum);
        assert relocations != null;
        Arrays.stream(relocations).forEach(originalPackageName -> {
            assert !Strings.isNullOrEmpty(originalPackageName);
        });

        var groupIdPattern = groupId.replaceAll("[.]", "{}");
        var builder = Library.builder()
                .id(artifactId)
                .groupId(groupIdPattern)
                .artifactId(artifactId)
                .version(version)
                .checksum(checkSum);

        for (var originalPackageName : relocations) {
            builder.relocate(originalPackageName.replaceAll("[.]", "{}"),
                    getRelocatedPackageName(originalPackageName).replaceAll("[.]", "{}"));
        }

        libraryManager.loadLibrary(builder.build());
        return this;
    }

    /**
     * Downloads Reflections.
     *
     * @return Instance of itself.
     * @throws ClassNotFoundException causes when downloaded class can not found.
     */
    public LibraryDownloader downloadReflections() throws ClassNotFoundException {
        download("org.reflections", "reflections",
                D.VERSION_REFLECTIONS, D.VERSION_REFLECTIONS_CHECKSUM,
                D.VERSION_REFLECTIONS_PACKAGENAME);
        // TODO
        // Class.forName("");
        return this;
    }

    /**
     * Downloads Hikari Connection Pool.
     *
     * @return Instance of itself.
     * @throws ClassNotFoundException causes when downloaded class can not found.
     */
    public LibraryDownloader downloadHikariCp() throws ClassNotFoundException {
        download("com.zaxxer", "HikariCP",
                D.VERSION_JDBC_HIKARICP, D.VERSION_JDBC_HIKARICP_CHECKSUM,
                D.VERSION_JDBC_HIKARICP_PACKAGENAME,
                "org.slf4j"
        );
        // TODO
        // Class.forName(getRelocatedPackageName(D.VERSION_JDBC_HIKARICP_PACKAGENAME) + ".HikariConfig");
        // Class.forName("com.zaxxer.hikari.HikariConfig");
        return this;
    }

    /**
     * Downloads MySQL connector.
     *
     * @return Instance of itself.
     * @throws ClassNotFoundException causes when downloaded class can not found.
     */
    public LibraryDownloader downloadMySql() throws ClassNotFoundException {
        download("mysql", "mysql-connector-java",
                D.VERSION_JDBC_MYSQL, D.VERSION_JDBC_MYSQL_CHECKSUM,
                D.VERSION_JDBC_MYSQL_PACKAGENAME, "com.google.protobuf");
        // Class.forName(getRelocatedPackageName(D.VERSION_JDBC_MYSQL_PACKAGENAME));
        return this;
    }

    /**
     * Downloads SQLite driver.
     *
     * @return Instance of itself.
     * @throws ClassNotFoundException causes when downloaded class can not found.
     */
    public LibraryDownloader downloadSqlite() throws ClassNotFoundException {
        download("org.xerial", "sqlite-jdbc", D.VERSION_JDBC_SQLITE, D.VERSION_JDBC_SQLITE_CHECKSUM,
                D.VERSION_JDBC_SQLITE_PACKAGENAME);
        // Class.forName(getRelocatedPackageName(D.VERSION_JDBC_SQLITE_PACKAGENAME));
        return this;
    }

}
