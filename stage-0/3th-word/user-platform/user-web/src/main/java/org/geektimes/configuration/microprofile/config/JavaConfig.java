package org.geektimes.configuration.microprofile.config;


import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigValue;
import org.eclipse.microprofile.config.spi.ConfigSource;
import org.eclipse.microprofile.config.spi.Converter;
import org.geektimes.context.ComponentContext;

import javax.servlet.ServletContext;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Stream;

public class JavaConfig implements Config {

    /**
     * 内部可变的集合，不要直接暴露在外面
     */
    private List<ConfigSource> configSources = new LinkedList<>();

    private static Comparator<ConfigSource> configSourceComparator = new Comparator<ConfigSource>() {
        @Override
        public int compare(ConfigSource o1, ConfigSource o2) {
            return Integer.compare(o2.getOrdinal(), o1.getOrdinal());
        }
    };

    public JavaConfig() {
        ClassLoader classLoader = getClass().getClassLoader();
        ServiceLoader<ConfigSource> serviceLoader = ServiceLoader.load(ConfigSource.class, classLoader);
        serviceLoader.forEach(configSources::add);
        // 排序
        configSources.sort(configSourceComparator);

        // TODO 通过 builder方式 替换spi配置加载
//        ConfigProviderResolver instance = ConfigProviderResolver.instance();
//        ConfigBuilder builder = instance.getBuilder();
//        // .withConverters(new MyConverter())
//        Config config = builder.withSources(new LocalPropertiesConfigSource()).build();
//        config.getConfigSources().forEach(configSources::add);
    }

    @Override
    public <T> T getValue(String propertyName, Class<T> propertyType) {
        // 获取String类型value
        String propertyValue = getPropertyValue(propertyName);

        Optional<Converter<T>> optional = getConverter(propertyType);

        // String 转换成目标类型
        if (optional.isPresent()) {
            try {
                return propertyType.cast(optional.get().convert(propertyValue));
            } catch (Exception e) {
//                throw new RuntimeException("不支持类型");
            }
        }

        return propertyType.cast(propertyValue);
    }

    @Override
    public ConfigValue getConfigValue(String propertyName) {
        return null;
    }

    protected String getPropertyValue(String propertyName) {
        String propertyValue = null;
        for (ConfigSource configSource : configSources) {
            propertyValue = configSource.getValue(propertyName);
            if (propertyValue != null) {
                break;
            }
        }
        return propertyValue;
    }

    public String getStringValue(String propertyName) {
        return getPropertyValue(propertyName);
    }

    @Override
    public <T> Optional<T> getOptionalValue(String propertyName, Class<T> propertyType) {
        T value = getValue(propertyName, propertyType);
        return Optional.ofNullable(value);
    }

    @Override
    public Iterable<String> getPropertyNames() {
        return null;
    }

    @Override
    public Iterable<ConfigSource> getConfigSources() {
        return Collections.unmodifiableList(configSources);
    }

    @Override
    public <T> Optional<Converter<T>> getConverter(Class<T> forType) {
        // 通过spi方式获取所有 Converter
        ClassLoader classLoader = getClass().getClassLoader();
        ServiceLoader<Converter> serviceLoader = ServiceLoader.load(Converter.class, classLoader);
        List<Converter> list = new LinkedList<>();
        serviceLoader.forEach(list::add);

        // 方式1.获取匹配 Converter
//        Converter converter = list.stream().filter(converterObject -> {
//            try {
//                Method method = converterObject.getClass().getMethod("convert", String.class);
//                return method.getReturnType() == forType;
//            } catch (NoSuchMethodException e) {
//                e.fillInStackTrace();
//            }
//            return false;
//        }).findFirst().orElse(null);

        // 方式2.获取匹配 Converter
        Converter converter = list.stream().filter(converterObject -> {
            Type[] interfaces = converterObject.getClass().getGenericInterfaces();
            return Stream.of(interfaces).anyMatch(interfaceObject -> {
                if (ParameterizedType.class.isAssignableFrom(interfaceObject.getClass())) {
                    ParameterizedType parameterizedType = (ParameterizedType) interfaceObject;
                    return Stream.of(parameterizedType.getActualTypeArguments())
                            .anyMatch(type -> type.getTypeName().equals(forType.getName()));
                }
                return false;
            });
        }).findFirst().orElse(null);

        // 方式3.获取匹配 Converter
//        Converter converter = new Converter() {
//            @Override
//            public Object convert(String value) throws IllegalArgumentException, NullPointerException {
//                String name = forType.getName();
//                Object result = null;
//                switch (name) {
//                    case "java.lang.Integer":
//                        result = Integer.parseInt(value);
//                        break;
//                    case "java.lang.Double":
//                        result = Double.parseDouble(value);
//                        break;
//                    case "java.lang.Long":
//                        result = Long.parseLong(value);
//                        break;
//                    case "java.lang.Boolean":
//                        result = Boolean.parseBoolean(value);
//                        break;
//                    case "java.lang.Float":
//                        result = Float.parseFloat(value);
//                        break;
//                }
//                return result;
//            }
//        };

        return Optional.ofNullable(converter);
    }

    @Override
    public <T> T unwrap(Class<T> type) {
        return null;
    }
}
