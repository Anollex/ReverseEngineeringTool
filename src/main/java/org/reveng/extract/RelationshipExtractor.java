package org.reveng.extract;

import org.reveng.config.Config;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;

public class RelationshipExtractor {

    public static List<String> getAssociations(Class<?> clazz, Config config) {
        List<String> result = new ArrayList<>();

        for (Field f : clazz.getDeclaredFields()) {
            addIfValid(f.getGenericType(), List.of(), result, config);
        }
        return result;
    }

    public static List<String> getDependencies(Class<?> clazz, Config config) {
        List<String> result = new ArrayList<>();
        List<String> associations = getAssociations(clazz, config);

        for (Method m : clazz.getDeclaredMethods()) {
            addIfValid(m.getGenericReturnType(), associations, result, config);
            for (Parameter p : m.getParameters()) {
                addIfValid(p.getParameterizedType(), associations, result, config);
            }
        }
        return result;
    }

    private static void addIfValid(Type type, List<String> exclude,
                                   List<String> result, Config config) {
        if (type instanceof ParameterizedType pt) {
            // intram in generic ex: List<ClassInfo> -> verificam ClassInfo
            for (Type arg : pt.getActualTypeArguments()) {
                addIfValid(arg, exclude, result, config); // recursiv
            }
        } else {
            Class<?> raw = getRawType(type);
            if (raw == null || raw.isPrimitive() || raw.equals(Void.TYPE)) return;
            if (ClassExtractor.shouldIgnore(raw, config)) return;
            String typeName = ClassExtractor.getTypeName(type, config);
            if (!exclude.contains(typeName) && !result.contains(typeName)) {
                result.add(typeName);
            }
        }
    }

    private static Class<?> getRawType(Type type) {
        if (type instanceof Class<?> c) {
            return c;
        }
        if (type instanceof ParameterizedType pt) {
            return (Class<?>) pt.getRawType();
        }
        return null;
    }
}
