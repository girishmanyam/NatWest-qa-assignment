package config;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;

public class ConfigUtil {

    private final static Configuration config;

    static {
        try {
            Configurations configs = new Configurations();
            config = configs.properties(
                    ConfigUtil.class.getClassLoader().getResource("config/config.properties")
            );
            config.getKeys().forEachRemaining(System.out::println);

        } catch (ConfigurationException e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }

    public static String get(String key) {
        return config.getString(key);
    }

}

