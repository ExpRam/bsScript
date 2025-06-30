package ru.expram.bsscript.lexer;

import ru.expram.bsscript.token.Token;
import ru.expram.bsscript.token.TokenType;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static ru.expram.bsscript.utils.ErrorUtil.error;

public class Lexer {

    private final String code;
    private List<Token> tokens;
    private int position;

    public Lexer(String code) {
        this.code = code;
        this.tokens = new ArrayList<>();
        this.position = 0;
    }

    public void parseTokens() {
        while(parseToken()) {}
        tokens = tokens.stream().filter(token -> !token.getTokenType().equals(TokenType.SPACE)).toList();
    }

    private boolean parseToken() {
        if(position >= code.length()) {
            return false;
        }

        TokenType[] types = TokenType.values();
        for(TokenType type : types) {
            String regex = "^" + type.getRegex();

            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(code.substring(position));
            if (matcher.find()) {
                String token = matcher.group();
                int skip = token.length();
                position += skip;

                if(type.equals(TokenType.COMMENT)) {
                    while (code.charAt(position) != '\n') {
                        position++;
                    }
                    return true;
                }

                tokens.add(new Token(type, token));
                return true;
            }
        }

        throw error("Cannot find token at position %d!".formatted(position));
    }

    public List<Token> getTokens() {
        return tokens;
    }
}
