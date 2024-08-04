import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Lox {

    private static boolean hadError = false;
    
    public static void main(String[] args) throws IOException {
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
        byte[] bytes = Files.readAllBytes(Paths.get(filePath));
        run(new String(bytes, Charset.defaultCharset()));

        if (hadError) {
            System.exit(65);
        }
    }

    private static void runPrompt() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        for (;;) {
            System.out.print(">");
            String line = reader.readLine();
            if (line == null) {
                return;
            }
            run(line);
            hadError = false;
        }
    }

    private static void run(String in) {
        System.out.println(in);
    }

    static void error(final int line, final String message) {
        report(line, "", message);
    }

    private static void report(final int line, final String where, final String message) {
        System.out.println("[line " + line + "] Error" + where + ": " + message);
        hadError = true;
    }
}


