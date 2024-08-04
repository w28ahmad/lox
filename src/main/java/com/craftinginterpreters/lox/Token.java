package com.craftinginterpreters.lox;

public class Token {
    TokenType type;
    String lexeme;
    Object literal;
    int line;

    public Token(TokenType type, String lexeme, Object literal, int line) {
        this.type = type;
        this.lexeme = lexeme;
        this.literal = literal;
        this.line = line;
    }

    @Override
    public String toString() {
        return "Token [type=" + type + ", lexeme=" + lexeme + ", literal=" + literal + "]";
    }
}
