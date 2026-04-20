package org.reveng.model;

import java.util.List;

public record MethodInfo(
        String name,
        String returnType,
        String modifier,
        List<String> parameterTypes
) {
}
