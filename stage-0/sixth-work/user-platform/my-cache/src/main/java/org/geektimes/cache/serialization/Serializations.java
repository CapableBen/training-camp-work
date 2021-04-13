package org.geektimes.cache.serialization;

import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

public class Serializations implements Iterable<Serialization> {

    private ClassLoader classLoader;

    private final Map<Class<?>, Serialization> typedSerializations = new HashMap<>();

    private Boolean addedSerialization = false;

    public Serializations() {
        this(Thread.currentThread().getContextClassLoader());
    }

    public Serializations(ClassLoader classLoader) {
        this.classLoader = classLoader;
        addSerializations();
    }

    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public void addSerializations() {
        if (addedSerialization) {
            return;
        }

        addSerializations(ServiceLoader.load(Serialization.class, classLoader));
        addedSerialization = true;
    }

    private void addSerializations(Iterable<Serialization> load) {
        load.forEach(this::addSerialization);
    }

    private void addSerialization(Serialization serialization) {
        Class<?> serializationType = resolveSerializationType(serialization);
        addSerialization(serialization, serializationType);
    }

    private void addSerialization(Serialization serialization, Class<?> serializationType) {
        typedSerializations.computeIfAbsent(serializationType, map -> typedSerializations.put(serializationType, serialization));
    }

    protected Class<?> resolveSerializationType(Serialization<?> serialization) {
        assertSerialization(serialization);
        Class<?> serializationType = null;
        Class<?> serializationClass = serialization.getClass();
        while (serializationClass != null) {
            serializationType = resolveSerializationType(serializationClass);
            if (serializationType != null) {
                break;
            }

            Type superType = serializationClass.getGenericSuperclass();
            if (superType instanceof ParameterizedType) {
                serializationType = resolveSerializationType(superType);
            }

            if (serializationType != null) {
                break;
            }
            // recursively
            serializationClass = serializationClass.getSuperclass();

        }

        return serializationType;
    }

    private void assertSerialization(Serialization<?> serialization) {
        Class<?> serializationClass = serialization.getClass();
        if (serializationClass.isInterface()) {
            throw new IllegalArgumentException("The implementation class of Converter must not be an interface!");
        }
        if (Modifier.isAbstract(serializationClass.getModifiers())) {
            throw new IllegalArgumentException("The implementation class of Converter must not be abstract!");
        }
    }


    private Class<?> resolveSerializationType(Class<?> serializationClass) {
        Class<?> serializationType = null;

        for (Type superInterface : serializationClass.getGenericInterfaces()) {
            serializationType = resolveSerializationType(superInterface);
            if (serializationType != null) {
                break;
            }
        }

        return serializationType;
    }

    private Class<?> resolveSerializationType(Type type) {
        Class<?> serializationType = null;
        if (type instanceof ParameterizedType) {
            ParameterizedType pType = (ParameterizedType) type;
            if (pType.getRawType() instanceof Class) {
                Class<?> rawType = (Class) pType.getRawType();
                if (Serialization.class.isAssignableFrom(rawType)) {
                    Type[] arguments = pType.getActualTypeArguments();
                    if (arguments.length == 1 && arguments[0] instanceof Class) {
                        serializationType = (Class) arguments[0];
                    }
                }
            }
        }
        return serializationType;
    }

    public void addSerializations(Serialization... serialization) {
        addSerializations(Arrays.asList(serialization));
    }

    public Serialization getSerializations(Class<?> serializationType) {
        Serialization serialization = typedSerializations.get(serializationType);
        if (serialization == null) {
            return typedSerializations.get(Object.class);// 兜底使用Object类型
        }
        return serialization;
    }

    @Override
    public Iterator<Serialization> iterator() {
        List<Serialization> allSerializations = new LinkedList<>();
        for (Serialization serialization : typedSerializations.values()) {
            allSerializations.add(serialization);
        }
        return allSerializations.iterator();
    }
}
