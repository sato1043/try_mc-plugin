package cc.updater.mc.testplugin;

import com.google.common.base.Strings;
import lombok.Getter;
import lombok.NonNull;
import net.byteflux.libby.BukkitLibraryManager;
import net.byteflux.libby.Library;
import net.byteflux.libby.logging.LogLevel;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public class LibraryDownloader {

    @Getter
    private final JavaPlugin plugin;

    @Getter
    private final BukkitLibraryManager libraryManager;

    public LibraryDownloader(@NonNull JavaPlugin plugin_) throws NullPointerException {
        plugin = plugin_;
        libraryManager = new BukkitLibraryManager(plugin);
        libraryManager.setLogLevel(LogLevel.DEBUG);
        libraryManager.addMavenCentral();
        libraryManager.addRepository(D.REPOSITORY_SPIGOT_REPO);
        libraryManager.addRepository(D.REPOSITORY_SONATYPE);
    }

    private String getRelocatedPackageName(@NonNull String originalPackageName) throws NullPointerException {
        return D.PROJECT_GROUP_ID + ".libs." + originalPackageName;
    }

    public LibraryDownloader download(String groupId, String artifactId, String version, String checkSum, String... relocations)
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
            builder.relocate(originalPackageName, getRelocatedPackageName(originalPackageName));
        }

        libraryManager.loadLibrary(builder.build());
        return this;
    }

    public LibraryDownloader downloadHikariCP() throws ClassNotFoundException {
        download("com.zaxxer", "HikariCP", D.VERSION_JDBC_HIKARICP, D.VERSION_JDBC_HIKARICP_CHECKSUM,
                D.VERSION_JDBC_HIKARICP_PACKAGENAME);
// TODO
//        Class.forName(getRelocatedPackageName(D.VERSION_JDBC_HIKARICP_PACKAGENAME) + ".HikariConfig");
//        Class.forName("com.zaxxer.hikari.HikariConfig");
        return this;
    }

    public LibraryDownloader downloadMySQL() throws ClassNotFoundException {
        download("mysql", "mysql-connector-java", D.VERSION_JDBC_MYSQL, D.VERSION_JDBC_MYSQL_CHECKSUM,
                D.VERSION_JDBC_MYSQL_PACKAGENAME, "com.google.protobuf");
//        Class.forName(getRelocatedPackageName(D.VERSION_JDBC_MYSQL_PACKAGENAME));
        return this;
    }

    public LibraryDownloader downloadSqlite() throws ClassNotFoundException {
        download("org.xerial", "sqlite-jdbc", D.VERSION_JDBC_SQLITE, D.VERSION_JDBC_SQLITE_CHECKSUM,
                D.VERSION_JDBC_SQLITE_PACKAGENAME);
//        Class.forName(getRelocatedPackageName(D.VERSION_JDBC_SQLITE_PACKAGENAME));
        return this;
    }

}
