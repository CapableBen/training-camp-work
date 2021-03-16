package org.geektimes.configuration.microprofile.config.source;

import org.eclipse.microprofile.config.spi.ConfigSource;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class OsPropertiesConfigSource implements ConfigSource {

    public static final String NAME = "Os Properties";
    /**
     * Java 系统属性最好通过本地变量保存，使用 Map 保存，尽可能运行期不去调整
     * -Dapplication.name=user-web
     */
    private final Map<String, String> properties;

    public OsPropertiesConfigSource() {
        Map<String, String> getenv = System.getenv();
        this.properties = new HashMap<>(getenv);
    }

    @Override
    public int getOrdinal() {
        return 100;
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
