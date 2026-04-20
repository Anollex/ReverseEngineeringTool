package org.reveng.output;

import org.reveng.config.Config;
import org.reveng.model.*;

public class PlantUMLFormat implements OutputFormat{

    @Override
    public void render(DiagramData data, Config config) {
        StringBuilder sb = new StringBuilder();
        sb.append("@startuml\n\n");

        for (ClassInfo c : data.classes()) {
            sb.append(renderBody(c));
        }

        for (ClassInfo c : data.classes()) {
            renderRelationships(c, sb);
        }

        sb.append("\n@enduml");
        System.out.println(sb);
    }

    private String renderBody(ClassInfo c) {
        StringBuilder sb = new StringBuilder();

        if (c.isInterface())
            sb.append("interface ").append(c.name()).append(" {\n");
        else
            sb.append("class ").append(c.name()).append(" {\n");

        for (FieldInfo f : c.fields())
            sb.append("  ").append(modifierSymbol(f.modifier()))
                    .append(" ").append(f.type()).append(" ").append(f.name()).append("\n");

        for (MethodInfo m : c.methods())
            sb.append("  ").append(modifierSymbol(m.modifier()))
                    .append(" ").append(m.returnType()).append(" ").append(m.name())
                    .append("(").append(String.join(", ", m.parameterTypes())).append(")\n");

        sb.append("}\n\n");
        return sb.toString();
    }

    private void renderRelationships(ClassInfo c, StringBuilder sb) {
        if (c.parent() != null)
            sb.append(c.name()).append(" --|> ").append(c.parent()).append("\n");

        for (String iface : c.interfaces())
            sb.append(c.name()).append(" ..|> ").append(iface).append("\n");

        for (String assoc : c.associations())
            sb.append(c.name()).append(" --> ").append(assoc).append("\n");

        for (String dep : c.dependencies())
            sb.append(c.name()).append(" ..> ").append(dep).append("\n");
    }
}
