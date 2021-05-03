package cc.updater.mc.TestPlugin;

import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.Properties;

public class PropertiesReader {

    public static final String PLUGIN_CONSTANTS_PROPERTIES = "plugin-constants.properties";

    private static PropertiesReader pluginConstantsPropertiesReader;

    public static PropertiesReader getPluginConstantsPropertiesReader() throws IOException, AssertionError {
        if (pluginConstantsPropertiesReader == null) {
            var reader = new PropertiesReader(PLUGIN_CONSTANTS_PROPERTIES);
            assert reader != null;
            pluginConstantsPropertiesReader = reader;
        }
        return pluginConstantsPropertiesReader;
    }

    public static final String UPDATE_CHECKER_URL_KEY = "updateChecker.url";

    public static String getUpdateCheckerUrl() throws IOException, AssertionError {
        var reader = PropertiesReader.getPluginConstantsPropertiesReader();
        var urlString = reader.getProperty(UPDATE_CHECKER_URL_KEY);
        assert urlString != null;
        return urlString;
    }

    //

    @Getter
    @Setter
    private Properties properties;

    private PropertiesReader(String propertyFileName) throws IOException, AssertionError {
        var is = getClass().getClassLoader().getResourceAsStream(propertyFileName);
        var properties = new Properties();
        assert is != null;
        assert properties != null;
        properties.load(is);

        setProperties(properties);
    }

    public String getProperty(String propertyName) {
        return getProperties().getProperty(propertyName);
    }

}
