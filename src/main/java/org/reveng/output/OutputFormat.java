package org.reveng.output;

import org.reveng.model.DiagramData;
import org.reveng.config.Config;

public interface OutputFormat {
    default String modifierSymbol(String modifier) {
        if (modifier.contains("public")) return "+";    //public
        if (modifier.contains("private")) return "-";   //private
        if (modifier.contains("protected")) return "#"; //protected
        return "~";                                     // package-private
    }
    void render(DiagramData data, Config config);
}