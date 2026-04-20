package org.reveng.extract;

import org.reveng.config.Config;
import org.reveng.model.*;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;

public class ClassExtractor {

    public static ClassInfo extract(Class<?> clazz, Config config) {
        String name = getName(clazz, config);
        boolean isInterface = clazz.isInterface();
        String parent = getParent(clazz, config);
        List<String> interfaces = getInterfaces(clazz, config);
        List<FieldInfo> fields = config.showAttributes() ? getFields(clazz, config) : List.of();
        List<MethodInfo> methods = config.showMethods() ? getMethods(clazz, config) : List.of();
        List<String> associations = RelationshipExtractor.getAssociations(clazz, config);
        List<String> dependencies = RelationshipExtractor.getDependencies(clazz, config);

        return new ClassInfo(name, isInterface, parent, interfaces, fields, methods, associations, dependencies);
    }

    private static String getName(Class<?> clazz, Config config) {
        return config.fullyQualifiedNames() ? clazz.getName() : clazz.getSimpleName();
    }

    private static String getParent(Class<?> clazz, Config config) {
        Class<?> parent = clazz.getSuperclass();
        if (parent == null || parent.equals(Object.class)) {
            return null;
        }
        if (shouldIgnore(parent, config)) {
            return null;
        }
        return getName(parent, config);
    }

    private static List<String> getInterfaces(Class<?> clazz, Config config) {
        List<String> result = new ArrayList<>();
        for (Class<?> iface : clazz.getInterfaces()) {
            if (!shouldIgnore(iface, config)) {
                result.add(getName(iface, config));
            }
        }
        return result;
    }

    private static List<FieldInfo> getFields(Class<?> clazz, Config config) {
        List<FieldInfo> result = new ArrayList<>();
        for (Field f : clazz.getDeclaredFields()) {
            String modifier = Modifier.toString(f.getModifiers());
            String type = getTypeName(f.getGenericType(), config);
            result.add(new FieldInfo(f.getName(), type, modifier));
        }
        return result;
    }

    private static List<MethodInfo> getMethods(Class<?> clazz, Config config) {
        List<MethodInfo> result = new ArrayList<>();
        for (Method m : clazz.getDeclaredMethods()) {
            if (isSynthetic(m)){
                continue;
            }
            String modifier = Modifier.toString(m.getModifiers());
            String returnType = getTypeName(m.getGenericReturnType(), config);
            List<String> params = new ArrayList<>();
            for (Parameter p : m.getParameters()) {
                params.add(getTypeName(p.getParameterizedType(), config));
            }
            result.add(new MethodInfo(m.getName(), returnType, modifier, params));
        }
        return result;
    }

    private static boolean isSynthetic(Method m) {
        String name = m.getName();
        return name.equals("equals") || name.equals("hashCode") || name.equals("toString") || m.isSynthetic();
    }

    static String getTypeName(Type type, Config config) {
        if (type instanceof ParameterizedType pt) {
            String raw = ((Class<?>) pt.getRawType()).getSimpleName();
            List<String> args = new ArrayList<>();
            for (Type arg : pt.getActualTypeArguments()) {
                args.add(getTypeName(arg, config));
            }
            return raw + "<" + String.join(", ", args) + ">";
        }
        if (type instanceof Class<?> c) {
            if (c.isArray()) {
                String componentName = config.fullyQualifiedNames()
                        ? c.getComponentType().getName()
                        : c.getComponentType().getSimpleName();
                return componentName + "[]";
            }
            return config.fullyQualifiedNames() ? c.getName() : c.getSimpleName();
        }
        return type.getTypeName();
    }

    static boolean shouldIgnore(Class<?> clazz, Config config) {
        String packageName = clazz.getPackageName();
        for (String ignored : config.ignoredPackages()) {
            if (packageName.startsWith(ignored)) {
                return true;
            }
        }
        return false;
    }
}
