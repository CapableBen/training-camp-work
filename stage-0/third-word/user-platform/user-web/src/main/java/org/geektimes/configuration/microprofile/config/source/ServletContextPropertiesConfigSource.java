package org.geektimes.configuration.microprofile.config.source;

import org.eclipse.microprofile.config.spi.ConfigSource;
import org.geektimes.context.ComponentContext;

import javax.servlet.ServletContext;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ServletContextPropertiesConfigSource implements ConfigSource {

    private static final String NAME = "ServletContext Properties";
    /**
     * Java 系统属性最好通过本地变量保存，使用 Map 保存，尽可能运行期不去调整
     * -Dapplication.name=user-web
     */
    private final Map<String, String> properties;

    public ServletContextPropertiesConfigSource() {
        ServletContext servletContext = ComponentContext.getServletContext();
        Enumeration<String> initParameterNames = servletContext.getInitParameterNames();
        Map<String, String> map = new HashMap<>();
        while (initParameterNames.hasMoreElements()) {
            String element = initParameterNames.nextElement();
            String value = servletContext.getInitParameter(element);
            map.put(element, value);
        }
        this.properties = map;
    }

    @Override
    public int getOrdinal() {
        return 900;
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
