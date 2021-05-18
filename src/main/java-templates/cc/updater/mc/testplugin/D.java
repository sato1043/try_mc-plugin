package cc.updater.mc.testplugin;

import com.google.common.base.Strings;

/**
 * Defining static constants.
 */
@SuppressWarnings("checkstyle:LineLength")
public class D {
    public static final String PROJECT_GROUP_ID = "${project.groupId}";
    public static final String PROJECT_ARTIFACT_ID = "${project.artifactId}";
    public static final String PROJECT_VERSION = "${project.version}";

    public static final String PROJECT_NAME = "${project.name}";
    public static final String PROJECT_DESCRIPTION = "${project.description}";
    public static final String PROJECT_URL = "${project.url}";

    public static final String PROJECT_AUTHOR = "${project.author}";
    public static final String PROJECT_API_VERSION = "${project.api.version}";

    public static final String UPDATE_CHECKER_URL = "${updateChecker.url}";

    public static final String REGEXP_HOST = "^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$|^(([a-zA-Z0-9]|[a-zA-Z0-9][a-zA-Z0-9\\-]*[a-zA-Z0-9])\\.)+([A-Za-z]|[A-Za-z][A-Za-z0-9\\-]*[A-Za-z0-9])$";

    public static final String STORAGE_SQLITE_FILENAME = "${storage.sqlite.filename}";

    public static final String REPOSITORY_SPIGOT_REPO = "${repository.spigot-repo}";
    public static final String REPOSITORY_SONATYPE = "${repository.sonatype}";

    public static final String VERSION_SPIGOTMC_SPIGOT_API = "${version.spigotmc.spigot-api}";

    public static final String VERSION_REFLECTIONS = "${version.reflections}";
    public static final String VERSION_REFLECTIONS_PACKAGENAME = "${version.reflections.packageName}";
    public static final String VERSION_REFLECTIONS_CHECKSUM = "${version.reflections.checksum}";
    public static final String VERSION_JDBC_HIKARICP = "${version.jdbc.hikaricp}";
    public static final String VERSION_JDBC_HIKARICP_PACKAGENAME = "${version.jdbc.hikaricp.packageName}";
    public static final String VERSION_JDBC_HIKARICP_CHECKSUM = "${version.jdbc.hikaricp.checksum}";
    public static final String VERSION_JDBC_MYSQL = "${version.jdbc.mysql}";
    public static final String VERSION_JDBC_MYSQL_PACKAGENAME = "${version.jdbc.mysql.packageName}";
    public static final String VERSION_JDBC_MYSQL_CHECKSUM = "${version.jdbc.mysql.checksum}";
    public static final String VERSION_JDBC_SQLITE = "${version.jdbc.sqlite}";
    public static final String VERSION_JDBC_SQLITE_PACKAGENAME = "${version.jdbc.sqlite.packageName}";
    public static final String VERSION_JDBC_SQLITE_CHECKSUM = "${version.jdbc.sqlite.checksum}";

    static {
        assert !Strings.isNullOrEmpty(PROJECT_GROUP_ID);
        assert !Strings.isNullOrEmpty(PROJECT_ARTIFACT_ID);
        assert !Strings.isNullOrEmpty(PROJECT_VERSION);

        assert !Strings.isNullOrEmpty(PROJECT_NAME);
        assert !Strings.isNullOrEmpty(PROJECT_DESCRIPTION);
        assert !Strings.isNullOrEmpty(PROJECT_URL);

        assert !Strings.isNullOrEmpty(PROJECT_AUTHOR);
        assert !Strings.isNullOrEmpty(PROJECT_API_VERSION);

        assert !Strings.isNullOrEmpty(UPDATE_CHECKER_URL);

        assert !Strings.isNullOrEmpty(REGEXP_HOST);

        assert !Strings.isNullOrEmpty(STORAGE_SQLITE_FILENAME);

        assert !Strings.isNullOrEmpty(REPOSITORY_SPIGOT_REPO);
        assert !Strings.isNullOrEmpty(REPOSITORY_SONATYPE);

        assert !Strings.isNullOrEmpty(VERSION_SPIGOTMC_SPIGOT_API);

        assert !Strings.isNullOrEmpty(VERSION_REFLECTIONS);
        assert !Strings.isNullOrEmpty(VERSION_REFLECTIONS_PACKAGENAME);
        assert !Strings.isNullOrEmpty(VERSION_REFLECTIONS_CHECKSUM);

        assert !Strings.isNullOrEmpty(VERSION_JDBC_HIKARICP);
        assert !Strings.isNullOrEmpty(VERSION_JDBC_HIKARICP_PACKAGENAME);
        assert !Strings.isNullOrEmpty(VERSION_JDBC_HIKARICP_CHECKSUM);
        assert !Strings.isNullOrEmpty(VERSION_JDBC_MYSQL);
        assert !Strings.isNullOrEmpty(VERSION_JDBC_MYSQL_PACKAGENAME);
        assert !Strings.isNullOrEmpty(VERSION_JDBC_MYSQL_CHECKSUM);
        assert !Strings.isNullOrEmpty(VERSION_JDBC_SQLITE);
        assert !Strings.isNullOrEmpty(VERSION_JDBC_SQLITE_PACKAGENAME);
        assert !Strings.isNullOrEmpty(VERSION_JDBC_SQLITE_CHECKSUM);
    }
}
