package org.reveng.output;

import org.reveng.config.Config;

public class OutputFormatFactory {

    public static OutputFormat create(Config config) {
        return switch (config.outputFormat()) {
            case "plantuml" -> new PlantUMLFormat();
            case "yuml"     -> new YumlFormat();
            default         -> new TextFormat();
        };
    }
}
