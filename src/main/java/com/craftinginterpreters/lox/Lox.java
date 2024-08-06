package com.craftinginterpreters.lox;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Lox {

    private static boolean hadError = false;
    
    public static void main(final String[] args) throws IOException {
        if (args.length > 1) {
            System.out.println("Usage: lox <filepath>.lox");
            System.exit(64);
        } else if (args.length == 1) {
            runFile(args[0]);
        } else {
            runPrompt();
        }
    }

    private static void runFile(final String filePath) throws IOException {
        final byte[] bytes = Files.readAllBytes(Paths.get(filePath));
        run(new String(bytes, Charset.defaultCharset()));

        if (hadError) {
            System.exit(65);
        }
    }

    private static void runPrompt() throws IOException {
        final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        for (;;) {
            System.out.print(">");
            final String line = reader.readLine();
            if (line == null) {
                return;
            }
            run(line);
            hadError = false;
        }
    }

    private static void run(final String in) {
        Scanner scanner = new Scanner(in);
        List<Token> tokens = scanner.scanTokens();
        
        for(Token token : tokens) {
            System.out.println(token);
        }
    }

    static void error(final int line, final String message) {
        report(line, "", message);
    }

    private static void report(final int line, final String where, final String message) {
        System.out.println("[line " + line + "] Error" + where + ": " + message);
        hadError = true;
    }
}


