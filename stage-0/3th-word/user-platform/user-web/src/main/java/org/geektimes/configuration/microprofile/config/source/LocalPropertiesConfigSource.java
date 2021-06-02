package org.geektimes.configuration.microprofile.config.source;

import org.eclipse.microprofile.config.spi.ConfigSource;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class LocalPropertiesConfigSource implements ConfigSource {

    public static final String NAME = "Local Properties";
    /**
     * Java 系统属性最好通过本地变量保存，使用 Map 保存，尽可能运行期不去调整
     * -Dapplication.name=user-web
     */
    private Map<String, String> properties;

    public LocalPropertiesConfigSource() {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        try (InputStream resourceAsStream = contextClassLoader.getResourceAsStream("META-INF/microprofile-config.properties")) {
            Properties properties = new Properties();
            properties.load(resourceAsStream);
            this.properties = new HashMap<String, String>((Map) properties);
        } catch (IOException e) {
        }
    }

    @Override
    public int getOrdinal() {
        return 800;
    }

    @Override
    public Set<String> getPropertyNames() {
        return properties.keySet();
    }

    @Override
    public String getValue(String propertyName) {
        return properties.get(propertyName);
    }

    @Override
    public String getName() {
        return NAME;
    }
}
