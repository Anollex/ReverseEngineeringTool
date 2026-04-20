package org.reveng.extract;

import org.reveng.config.Config;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JarLoader {
    public static List<Class<?>> load(Config config) throws Exception {
        File file = new File(config.jarPath());
        URL jarURL = file.toURI().toURL();
        URLClassLoader loader = new URLClassLoader(new URL[]{jarURL});

        List<Class<?>> classes = new ArrayList<>();

        JarFile jar = new JarFile(file);
        Enumeration<JarEntry> entries = jar.entries();

        for(JarEntry entry : Collections.list(entries)) {
            if(entry.getName().endsWith(".class")){
                String className = entry.getName().replace("/", ".").replace(".class", "");

                if(!shouldIgnore(className, config)){
                    try{
                        classes.add(loader.loadClass(className));
                    }
                    catch (ClassNotFoundException | NoClassDefFoundError e) {
                        continue;
                    }
                }
            }
        }

        jar.close();
        loader.close();

        return classes;
    }

    private static boolean shouldIgnore(String className, Config config) {
        for (String ignored : config.ignoredPackages()) {
            if (className.startsWith(ignored)) {
                return true;
            }
        }
        return false;
    }
}
