package org.reveng.model;

import java.util.List;

public record ClassInfo(
        String name,
        boolean isInterface,
        String parent,
        List<String> interfaces,
        List<FieldInfo> fields,
        List<MethodInfo> methods,
        List<String> associations,
        List<String> dependencies
) {
}
