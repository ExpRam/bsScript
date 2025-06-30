package ru.expram.bsscript.parser;

import ru.expram.bsscript.ast.Expression;
import ru.expram.bsscript.ast.Statement;
import ru.expram.bsscript.ast.expressions.*;
import ru.expram.bsscript.ast.statements.*;
import ru.expram.bsscript.token.Token;
import ru.expram.bsscript.token.TokenType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static ru.expram.bsscript.token.TokenType.*;
import static ru.expram.bsscript.utils.ErrorUtil.error;

public class Parser {

    private final List<Token> tokens;

    private int position;
    private RootStatement ast;

    public Parser(List<Token> tokens) {
        this.position = 0;
        this.tokens = tokens;
    }

    public void buildAST() {
        RootStatement ast = new RootStatement();

        while(position < tokens.size()) {
            Statement parsedStatement = parseStatement();
            ast.getStatements().add(parsedStatement);
        }

        this.ast = ast;
    }

    private Statement parseStatement() {
        Token token = peek();

        Statement statement = switch (token.getTokenType()) {
            case IDENTIFIER -> parseIdentifier(token);
            case IF -> parseIfCondition();
            case WHILE -> parseWhile();
            case BREAK -> parseBreak();
            case PRINT -> parsePrint(token);
            case INPUT -> parseInputStatement(token);
            default -> throw error("Unexpected token: %s at position %d"
                    .formatted(token.getTokenType(), position));
        };

        if (!(statement instanceof IfStatement) &&
                !(statement instanceof WhileStatement)) {
            require(SEMICOLON);
        }

        return statement;
    }

    private Statement parseInputStatement(Token token) {
        Expression expression = parseInputExpression(token);

        return new UnarStatement(token, expression);
    }

    private Statement parsePrint(Token token) {
        Expression operand = parseFormula();

        return new UnarStatement(token, operand);
    }

    private Statement parseIdentifier(Token identifier) {
        Token token = peek();

        Statement statement = switch (token.getTokenType()) {
            case ASSIGN -> parseAssign(identifier);
            default -> throw error("Unexpected token: %s at position %d for identifier %s"
                    .formatted(token.getTokenType(), position, identifier));
        };

        return statement;
    }

    private Statement parseAssign(Token identifier) {
        VarExpression left = new VarExpression(identifier);
        Expression right = parseFormula();

        return new AssignStatement(left, right);
    }

    private Expression parseVariableOrDataType() {
        if(match(LPAR) != null) {
            Expression parsedPar = parseFormula();
            require(RPAR);
            return parsedPar;
        }

        Token token = peek();

        return getTypeExpression(token);
    }

    private Expression parseFormula() {
        return parseLogicalOr();
    }

    private Expression parseLogicalOr() {
        TokenType tokenType = OR;

        Expression left = parseLogicalAnd();
        Token operator = match(tokenType);
        while(operator != null) {
            Expression right = parseLogicalAnd();
            left = new BinExpression(operator, left, right);
            operator = match(tokenType);
        }

        return left;
    }

    private Expression parseLogicalAnd() {
        TokenType tokenType = AND;

        Expression left = parseEquals();
        Token operator = match(tokenType);
        while(operator != null) {
            Expression right = parseEquals();
            left = new BinExpression(operator, left, right);
            operator = match(tokenType);
        }

        return left;
    }

    private Expression parseEquals() {
        TokenType tokenType = EQUALS;

        Expression left = parseComparison();
        Token operator = match(tokenType);
        while(operator != null) {
            Expression right = parseComparison();
            left = new BinExpression(operator, left, right);
            operator = match(tokenType);
        }

        return left;
    }

    private Expression parseComparison() {
        TokenType[] tokenTypes = new TokenType[] {
                GT, LT, GTOET, LTOET
        };

        Expression left = parseTerm();
        Token operator = match(tokenTypes);
        while(operator != null) {
            Expression right = parseTerm();
            left = new BinExpression(operator, left, right);
            operator = match(tokenTypes);
        }

        return left;
    }

    private Expression parseTerm() {
        TokenType[] tokenTypes = new TokenType[] {
                PLUS, MINUS
        };

        Expression left = parseFactor();
        Token operator = match(tokenTypes);
        while(operator != null) {
            Expression right = parseFactor();
            left = new BinExpression(operator, left, right);
            operator = match(tokenTypes);
        }

        return left;
    }

    private Expression parseFactor() {
        TokenType[] tokenTypes = new TokenType[] {
                MULTIPLY, DIVIDE
        };

        Expression left = parseUnary();
        Token operator = match(tokenTypes);
        while(operator != null) {
            Expression right = parseUnary();
            left = new BinExpression(operator, left, right);
            operator = match(tokenTypes);
        }

        return left;
    }

    private Expression parseUnary() {
        TokenType[] tokenTypes = new TokenType[] {
                MINUS, NOT
        };
        Token operator = match(tokenTypes);
        Expression left = parseVariableOrDataType();
        while(operator != null) {
            left = new UnarExpression(operator, left);
            operator = match(tokenTypes);
        }

        return left;
    }

    private Statement parseIfCondition() {
        Expression condition = parseFormula();
        List<Statement> thenStatements = parseBlock();
        List<Statement> elseStatements = List.of();

        if(match(ELSE) != null) {
            elseStatements = parseBlock();
        }

        return new IfStatement(condition, thenStatements, elseStatements);
    }

    private Statement parseWhile() {
        Expression condition = parseFormula();
        List<Statement> block = parseBlock();

        return new WhileStatement(condition, block);
    }

    private Statement parseBreak() {
        return new BreakStatement();
    }

    private Expression getTypeExpression(Token token) {
        return switch (token.getTokenType()) {
            case IDENTIFIER -> new VarExpression(token);
            case INTEGER -> new IntExpression(Integer.parseInt(token.getValue()));
            case FLOAT -> new FloatExpression(Float.parseFloat(token.getValue()));
            case STRING -> new StringExpression(token.getValue());
            case BOOLEAN -> new BooleanExpression(Boolean.parseBoolean(token.getValue()));
            case INPUT -> parseInputExpression(token);
            default -> throw error("Unexpected token: %s at token position %d".formatted(token.getTokenType(), position));
        };
    }

    private Expression parseInputExpression(Token token) {
        Expression operand = parseFormula();

        return new UnarExpression(token, operand);
    }

    private Token match(boolean shift, TokenType... excepted) {
        if(position < tokens.size()) {
            Token token = tokens.get(position);
            if(Arrays.stream(excepted).anyMatch(exceptedToken ->
                    exceptedToken.equals(token.getTokenType()))) {
                if(shift) position += 1;
                return token;
            }
        }

        return null;
    }

    private Token match(TokenType... excepted) {
        return match(true, excepted);
    }

    private Token require(TokenType... excepted) {
        Token matchedToken = match(excepted);

        if(matchedToken != null) {
            return matchedToken;
        }

        throw error("Expected %s but got %s at position %d!"
                .formatted(Arrays.toString(excepted),
                        tokens.get(position).getValue(), position));
    }

    private Token peek() {
        Token token = tokens.get(position);
        position += 1;
        return token;
    }

    private List<Statement> parseBlock() {
        List<Statement> blockStatements = new ArrayList<>();
        require(LBRACE);

        while(match(RBRACE) == null) {
            blockStatements.add(parseStatement());
        }

        return blockStatements;
    }

    public RootStatement getAst() {
        return ast;
    }
}
