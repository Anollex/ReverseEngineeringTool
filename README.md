# reveng — User Commands

## Name
`reveng` - reverse engineering tool for Java class diagrams

## Synopsis
```
java -jar reveng.jar FILE [OPTIONS]
```

## Description
Analyzes a compiled Java `.jar` file using reflection and extracts class diagram
information including classes, interfaces, fields, methods, and relationships
(extends, implements, association, dependency).

## Options

`--format=FORMAT`
Output format. One of: `text` (default), `plantuml`, `yuml`.

`--ignore=PKG1,PKG2,...`
Comma-separated list of packages to exclude. Classes whose package starts with
any of these values are omitted from the diagram. Example: `java.lang,java.util`

`--fqn=true|false`
Write class names as fully qualified names (e.g. `com.example.Foo`). Default: `true`.

`--methods=true|false`
Include method signatures in the output. Default: `true`.

`--attributes=true|false`
Include field declarations in the output. Default: `true`.

## Examples

Basic text output:
```
java -jar reveng.jar myapp.jar
```

PlantUML, no stdlib, short names:
```
java -jar reveng.jar myapp.jar --format=plantuml --ignore=java.lang,java.util --fqn=false
```

yUML, fields only, no methods:
```
java -jar reveng.jar myapp.jar --format=yuml --methods=false
```

Minimal diagram, relationships only:
```
java -jar reveng.jar myapp.jar --methods=false --attributes=false
```

## Relationships

The tool distinguishes four relationship types extracted via reflection:

| Relationship  | Source                                        |
|---------------|-----------------------------------------------|
| `extends`     | superclass hierarchy (`getSuperclass`)        |
| `implements`  | interface implementation (`getInterfaces`)    |
| `association` | field of a non-primitive type (`getDeclaredFields`) |
| `dependency`  | type used in method params or return type only |

