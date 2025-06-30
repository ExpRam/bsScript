package ru.expram.bsscript.token;

public enum TokenType {

    ASSIGN("(?<!=)(?<!\\!)(?<!<)(?<!>)=(?!=)(?!\\!)"),
    FLOAT("\\d+\\.\\d+"),
    INTEGER("\\d+"),
    PLUS("\\+"),
    MINUS("-"),
    MULTIPLY("\\*"),
    DIVIDE("(?<!\\/)\\/(?!\\/)"),
    AND("&&"),
    NOT("!"),
    OR("\\|\\|"),
    EQUALS("=="),
    NOT_EQUALS("!="),
    GT(">(?!=)"),
    LT("<(?!=)"),
    GTOET(">="),
    LTOET("<="),
    BOOLEAN("\\b(true|false)\\b"),
    STRING("\"[^\"]*\""),
    IF("if"),
    ELSE("else"),
    WHILE("while"),
    BREAK("break"),
    PRINT("print"),
    INPUT("input"),
    LPAR("\\("),
    RPAR("\\)"),
    LBRACE("\\{"),
    RBRACE("\\}"),
    SPACE("[ \\n\\t\\r]+"),
    SEMICOLON(";"),
    IDENTIFIER("[a-zA-Z0-9]+"),
    COMMENT("//");

    private final String regex;

    TokenType(String regex) {
        this.regex = regex;
    }

    public String getRegex() {
        return regex;
    }
}
