package org.reveng.output;

import org.reveng.config.Config;
import org.reveng.model.*;

public class TextFormat implements OutputFormat {

    @Override
    public void render(DiagramData data, Config config) {
        for (ClassInfo c : data.classes()) {
            System.out.println("[" + c.name() + "]");

            if (c.parent() != null)
                System.out.println("  extends: " + c.parent());

            for (String iface : c.interfaces())
                System.out.println("  implements: " + iface);

            for (FieldInfo f : c.fields())
                System.out.println("  field: " + f.modifier() + " " + f.type() + " " + f.name());

            for (MethodInfo m : c.methods())
                System.out.println("  method: " + m.modifier() + " " + m.returnType()
                        + " " + m.name() + "(" + String.join(", ", m.parameterTypes()) + ")");

            for (String assoc : c.associations())
                System.out.println("  association -> " + assoc);

            for (String dep : c.dependencies())
                System.out.println("  dependency ..> " + dep);

            System.out.println();
        }
    }
}
