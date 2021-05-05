package cc.updater.mc.TestPlugin;

import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.Properties;

public class PropertiesReader {

    private static final String PLUGIN_CONSTANTS_PROPERTIES = "plugin-constants.properties";

    private static final String UPDATE_CHECKER_URL_KEY = "updateChecker.url";

    private static PropertiesReader pluginConstantsPropertiesReader;

    private static PropertiesReader getPluginConstantsPropertiesReader() throws IOException, AssertionError {
        if (pluginConstantsPropertiesReader == null) {
            var reader = new PropertiesReader(PLUGIN_CONSTANTS_PROPERTIES);
            assert reader != null;
            pluginConstantsPropertiesReader = reader;
        }
        return pluginConstantsPropertiesReader;
    }

    private static String getString(String key) throws IOException, AssertionError {
        var s = PropertiesReader.getPluginConstantsPropertiesReader().getProperty(key);
        assert s != null;
        return s;
    }

    public static String getUpdateCheckerUrl() throws IOException, AssertionError {
        return PropertiesReader.getString(UPDATE_CHECKER_URL_KEY);
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
