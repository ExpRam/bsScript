package ru.expram.bsscript;

import ru.expram.bsscript.interpreter.Interpreter;
import ru.expram.bsscript.lexer.Lexer;
import ru.expram.bsscript.parser.Parser;
import ru.expram.bsscript.semantic.SemanticAnalyzator;

import java.io.*;

public class Main {

    public static void main(String[] args) throws IOException {
        ClassLoader classLoader = Main.class.getClassLoader();
        File file = new File(classLoader.getResource("script.bsc").getFile());

        InputStream inputStream = new FileInputStream(file);
        String code = readFromInputStream(inputStream);

        Lexer lexer = new Lexer(code);
        lexer.parseTokens();

        Parser parser = new Parser(lexer.getTokens());
        parser.buildAST();

        SemanticAnalyzator semanticAnalyzator = new SemanticAnalyzator();
        semanticAnalyzator.analyze(parser.getAst());

        Interpreter interpreter = new Interpreter();
        interpreter.execute(parser.getAst());
    }

    private static String readFromInputStream(InputStream inputStream)
            throws IOException {
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br
                     = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }
        return resultStringBuilder.toString();
    }
}
