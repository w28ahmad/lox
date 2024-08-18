package com.craftinginterpreters.lox.tools;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;

public class GenerateAst {

    public static void main(final String[] args) throws FileNotFoundException, UnsupportedEncodingException {

        if (args.length != 1) {
            System.out.println("Usage: GenerateAst <file directory path>");
        }

        String outputDir = args[0];
        defineAst(outputDir, "Expr", Arrays.asList(
                    "Binary   : Expr left, Token operator, Expr right",
                    "Grouping : Expr expression",
                    "Literal  : Object value",
                    "Unary    : Token operator, Expr right"
                    ));
    }

    private static void defineAst(String outputDir, String baseName, List<String> types) throws FileNotFoundException, UnsupportedEncodingException {

        String file = outputDir + "/" + baseName + ".java";
        PrintWriter writer = new PrintWriter(file, "UTF-8");

        writer.println("package com.craftinginterpreters.lox;");
        writer.println();
        writer.println("import java.util.List;");
        writer.println();
        writer.println("abstract class " + baseName + " {");

        for (String type : types) {
            String className = type.split(":")[0].trim();
            String fields = type.split(":")[1].trim();
            createType(baseName, className, fields, writer);
        }
        writer.println("}");

        writer.close();
    }

    private static void createType(String baseName, String className, String fields, PrintWriter writer) {
        
        writer.println();
        writer.println("\tclass " + className + " extends " + baseName + " {");
        writer.println();
        // Fields
        for (String field : fields.split(",")) {
            writer.println("\t\tprivate final " + field.trim() + ";");
        }
        writer.println();

        // Constructor
        writer.println("\t\tpublic " + className + "(" + fields + ") {");
        for (String field : fields.split(",")) {
            String fieldName = field.trim().split(" ")[1].trim();
            writer.println("\t\t\tthis." + fieldName + " = " + fieldName + ";");
        }
        writer.println("\t\t}");
        writer.println("\t}");
    }
}
