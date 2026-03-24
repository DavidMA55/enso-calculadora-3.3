package calculator;

import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {

    private Properties props = new Properties();

    public ConfigLoader() {
        try (InputStream is = getClass().getClassLoader()
                .getResourceAsStream("config.properties")) {

            if (is != null) {
                props.load(is);
            }

        } catch (Exception e) {
            // usar valores por defecto sin romper
        }
    }

    public String get(String key, String defaultValue) {
        return props.getProperty(key, defaultValue);
    }
}