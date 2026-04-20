package org.reveng.output;

import org.reveng.config.Config;
import org.reveng.model.*;

public class YumlFormat implements OutputFormat{
    @Override
    public void render(DiagramData data, Config config) {
        StringBuilder sb = new StringBuilder();

        for (ClassInfo c : data.classes()) {
            sb.append(renderBody(c)).append("\n");
        }

        sb.append("\n");

        for (ClassInfo c : data.classes()) {
            renderRelationships(c, sb);
        }

        System.out.println(sb);
    }

    private String renderBody(ClassInfo c) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");

        // header
        if (c.isInterface())
            sb.append("<<").append(c.name()).append(">>");
        else
            sb.append(c.name());

        // campuri
        if (!c.fields().isEmpty()) {
            sb.append("|");
            for (FieldInfo f : c.fields())
                sb.append(modifierSymbol(f.modifier()))
                        .append(f.type()).append(" ").append(f.name()).append(";");
        }

        // metode
        if (!c.methods().isEmpty()) {
            sb.append("|");
            for (MethodInfo m : c.methods())
                sb.append(modifierSymbol(m.modifier()))
                        .append(m.returnType()).append(" ").append(m.name())
                        .append("(").append(String.join(", ", m.parameterTypes())).append(");");
        }

        sb.append("]");
        return sb.toString();
    }

    private void renderRelationships(ClassInfo c, StringBuilder sb) {
        // extends
        if (c.parent() != null)
            sb.append("[").append(c.parent()).append("]")
                    .append("^")
                    .append("[").append(c.name()).append("]\n");

        // implements
        for (String iface : c.interfaces())
            sb.append("[<<").append(iface).append(">>]")
                    .append("^")
                    .append("[").append(c.name()).append("]\n");

        // association
        for (String assoc : c.associations())
            sb.append("[").append(c.name()).append("]")
                    .append("->[").append(assoc).append("]\n");

        // dependency
        for (String dep : c.dependencies())
            sb.append("[").append(c.name()).append("]")
                    .append("-.->[").append(dep).append("]\n");
    }
}