package org.reveng.config;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ConfigParser {
    public static Config parse(String[] args) {
        String jarPath = args[0];
        Set<String> ignoredPackages = new HashSet<>();
        boolean fqn = true;
        boolean showMethods = true;
        boolean showAttributes = true;
        String outputFormat = "text";

        for(String arg : args){
            if(arg.startsWith("--ignore=")) {
                String[] packages = arg.split("=")[1].split(",");
                ignoredPackages.addAll(Arrays.asList(packages));
            }
            if(arg.startsWith("--fqn=")) {
                fqn = Boolean.parseBoolean(arg.split("=")[1]);
            }
            if(arg.startsWith("--methods=")){
                showMethods = Boolean.parseBoolean(arg.split("=")[1]);
            }
            if(arg.startsWith("--attributes=")) {
                showAttributes = Boolean.parseBoolean(arg.split("=")[1]);
            }
            if(arg.startsWith("--format=")){
                outputFormat = arg.split("=")[1];
            }
        }
        return new Config(jarPath, ignoredPackages, fqn, showMethods, showAttributes, outputFormat);
    }
}
