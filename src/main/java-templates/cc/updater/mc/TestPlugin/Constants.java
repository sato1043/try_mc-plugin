package cc.updater.mc.TestPlugin;

import com.google.common.base.Strings;

public class Constants {
    public static final String PROJECT_API_VERSION = "${project.api.version}";
    public static final String PROJECT_AUTHOR = "${project.author}";
    public static final String PROJECT_VERSION = "${project.version}";
    public static final String PROJECT_NAME = "${project.name}";
    public static final String PROJECT_URL = "${project.url}";
    public static final String PROJECT_DESCRIPTION = "${project.description}";

    public static final String UPDATE_CHECKER_URL = "${updateChecker.url}";

    public static final String REGEXP_HOST = "^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$|^(([a-zA-Z0-9]|[a-zA-Z0-9][a-zA-Z0-9\\-]*[a-zA-Z0-9])\\.)+([A-Za-z]|[A-Za-z][A-Za-z0-9\\-]*[A-Za-z0-9])$";

    public static final String STORAGE_SQLITE_FILENAME = "${storage.sqlite.filename}";

    static {
        assert !Strings.isNullOrEmpty(PROJECT_API_VERSION);
        assert !Strings.isNullOrEmpty(PROJECT_AUTHOR);
        assert !Strings.isNullOrEmpty(PROJECT_VERSION);
        assert !Strings.isNullOrEmpty(PROJECT_NAME);
        assert !Strings.isNullOrEmpty(PROJECT_URL);
        assert !Strings.isNullOrEmpty(PROJECT_DESCRIPTION);

        assert !Strings.isNullOrEmpty(UPDATE_CHECKER_URL);

        assert !Strings.isNullOrEmpty(REGEXP_HOST);

        assert !Strings.isNullOrEmpty(STORAGE_SQLITE_FILENAME);
    }
}
