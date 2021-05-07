package cc.updater.mc.TestPlugin;

import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.Properties;

public class PropertiesReader {

    private static final String PLUGIN_CONSTANTS_PROPERTIES = "plugin-constants.properties";

    private static final String PROJECT_API_VERSION = "project.api.version";
    private static final String PROJECT_AUTHOR = "project.author";
    private static final String PROJECT_VERSION = "project.version";
    private static final String PROJECT_NAME = "project.name";
    private static final String PROJECT_URL = "project.url";
    private static final String PROJECT_DESCRIPTION = "project.description";

    private static final String UPDATE_CHECKER_URL_KEY = "updateChecker.url";
    private static final String REGEXP_HOST = "regexp.host";
    private static final String STORAGE_SQLITE_FILENAME = "storage.sqlite.filename";

    private static PropertiesReader pluginConstantsPropertiesReader;

    private static PropertiesReader getPluginConstantsPropertiesReader() throws IOException, AssertionError {
        if (pluginConstantsPropertiesReader == null) {
            pluginConstantsPropertiesReader = new PropertiesReader(PLUGIN_CONSTANTS_PROPERTIES);
            assert pluginConstantsPropertiesReader != null;
        }
        return pluginConstantsPropertiesReader;
    }

    private static String getString(String key) throws IOException, AssertionError {
        var s = PropertiesReader.getPluginConstantsPropertiesReader().getProperty(key);
        assert s != null;
        return s;
    }

    public static String getProjectApiVersion() throws IOException, AssertionError {
        return PropertiesReader.getString(PROJECT_API_VERSION);
    }

    public static String getProjectAuthor() throws IOException, AssertionError {
        return PropertiesReader.getString(PROJECT_AUTHOR);
    }

    public static String getProjectVersion() throws IOException, AssertionError {
        return PropertiesReader.getString(PROJECT_VERSION);
    }

    public static String getProjectName() throws IOException, AssertionError {
        return PropertiesReader.getString(PROJECT_NAME);
    }

    public static String getProjectUrl() throws IOException, AssertionError {
        return PropertiesReader.getString(PROJECT_URL);
    }

    public static String getProjectDescription() throws IOException, AssertionError {
        return PropertiesReader.getString(PROJECT_DESCRIPTION);
    }

    public static String getUpdateCheckerUrl() throws IOException, AssertionError {
        return PropertiesReader.getString(UPDATE_CHECKER_URL_KEY);
    }

    public static String getRegexpHost() throws IOException, AssertionError {
        return PropertiesReader.getString(REGEXP_HOST);
    }

    public static String getStorageSqLiteFilename() throws IOException, AssertionError {
        return PropertiesReader.getString(STORAGE_SQLITE_FILENAME);
    }


    //

    @Getter
    @Setter
    private Properties properties;

    private PropertiesReader(String propertyFileName) throws IOException, AssertionError {
        var is = getClass().getClassLoader().getResourceAsStream(propertyFileName);
        assert is != null;
        var properties = new Properties();
        assert properties != null;
        properties.load(is);

        setProperties(properties);
    }

    public String getProperty(String propertyName) {
        return getProperties().getProperty(propertyName);
    }

}
