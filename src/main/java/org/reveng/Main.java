package org.reveng;

import org.reveng.config.Config;
import org.reveng.config.ConfigParser;
import org.reveng.extract.ClassExtractor;
import org.reveng.extract.JarLoader;
import org.reveng.model.ClassInfo;
import org.reveng.model.DiagramData;
import org.reveng.output.OutputFormat;
import org.reveng.output.OutputFormatFactory;

import java.util.ArrayList;
import java.util.List;
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws Exception {
        Config config = ConfigParser.parse(args);

        List<Class<?>> classes = JarLoader.load(config);

        List<ClassInfo> classInfos = new ArrayList<>();
        for (Class<?> clazz : classes) {
            classInfos.add(ClassExtractor.extract(clazz, config));
        }

        DiagramData data = new DiagramData(classInfos);

        OutputFormat formatter = OutputFormatFactory.create(config);
        formatter.render(data, config);
    }
}
