package org.reveng.config;

import java.util.Set;

public record Config(
        String jarPath,
        Set<String> ignoredPackages,
        boolean fullyQualifiedNames,
        boolean showMethods,
        boolean showAttributes,
        String outputFormat
) {
}
