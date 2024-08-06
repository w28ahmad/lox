package com.craftinginterpreters.lox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scanner {

    private static final Map<String, TokenType> keywords;
    static {
        keywords = new HashMap<>();
        keywords.put("and", TokenType.AND);
        keywords.put("class", TokenType.CLASS);
        keywords.put("else", TokenType.ELSE);
        keywords.put("false", TokenType.FALSE);
        keywords.put("for", TokenType.FOR);
        keywords.put("fun", TokenType.FUN);
        keywords.put("if", TokenType.IF);
        keywords.put("nil", TokenType.NIL);
        keywords.put("or", TokenType.OR);
        keywords.put("print", TokenType.PRINT);
        keywords.put("return", TokenType.RETURN);
        keywords.put("super", TokenType.SUPER);
        keywords.put("this", TokenType.THIS);
        keywords.put("true", TokenType.TRUE);
        keywords.put("var", TokenType.VAR);
        keywords.put("while", TokenType.WHILE);
    }

    String source;
    List<Token> tokens = new ArrayList<>();
    private int current = 0;

    private int start = 0;

    private int line = 1;

    public Scanner(final String source) {
        this.source = source;
    }

    public List<Token> scanTokens() {
        while (!isEnd()) {
            start = current;
            scanToken();
        }
        tokens.add(new Token(TokenType.EOF, "", null, line));
        return tokens;
    }

    private void scanToken() {
        final char c = advance();

        switch (c) {
            case '(':
                addToken(TokenType.LEFT_PAREN);
                break;
            case ')':
                addToken(TokenType.RIGHT_PAREN);
                break;
            case '{':
                addToken(TokenType.LEFT_BRACE);
                break;
            case '}':
                addToken(TokenType.RIGHT_BRACE);
                break;
            case ',':
                addToken(TokenType.COMMA);
                break;
            case '.':
                addToken(TokenType.DOT);
                break;
            case '-':
                addToken(TokenType.MINUS);
                break;
            case '+':
                addToken(TokenType.PLUS);
                break;
            case ';':
                addToken(TokenType.SEMICOLON);
                break;
            case '*':
                addToken(TokenType.STAR);
                break;
            case '!':
                addToken(matchAdvance('=') ? TokenType.BANG_EQUAL : TokenType.BANG);
                break;
            case '=':
                addToken(matchAdvance('=') ? TokenType.EQUAL_EQUAL : TokenType.EQUAL);
                break;
            case '<':
                addToken(matchAdvance('=') ? TokenType.LESS_EQUAL : TokenType.LESS);
                break;
            case '>':
                addToken(matchAdvance('=') ? TokenType.GREATER_EQUAL : TokenType.GREATER);
                break;
            case '/':
                if (matchAdvance('/')) {
                    while (peek() != '\n' && !isEnd()) {
                        advance();
                    }
                } else {
                    addToken(TokenType.SLASH);
                }
                break;
            case ' ':
            case '\t':
            case '\r':
                break;
            case '\n':
                line++;
                break;
            case '"':
                string();
                break;
            default:
                if (isDigit(c)) {
                    number();
                } else if (isAlpha(c)) {
                    identifier();
                } else {
                    Lox.error(line, "Unexpected character: " + c);
                }
                break;
        }
    }

    private void identifier() {
        while (isAlphanumeric(peek()))
            advance();

        final String text = source.substring(start, current);
        TokenType type = keywords.get(text);

        if (type == null)
            type = TokenType.IDENTIFIER;

        addToken(type);
    }

    private boolean isAlpha(final char c) {
        return (('a' <= c && c <= 'z') ||
                ('A' <= c && c <= 'Z') ||
                (c == '_'));
    }

    private boolean isAlphanumeric(final char c) {
        return isDigit(c) || isAlpha(c);

    }

    private boolean isDigit(final char c) {
        return '0' <= c && c <= '9';
    }

    private void number() {
        while (isDigit(peek()))
            advance();

        if (peek() == '.' && isDigit(peekNext())) {
            advance();

            while (isDigit(peek()))
                advance();
        } else if (peek() == '.') {
            Lox.error(line, "Unexpected character in number");
        }

        addToken(TokenType.NUMBER, Double.parseDouble(source.substring(start, current)));
    }

    private void string() {
        while (peek() != '"' && !isEnd()) {
            if (peek() == '\n')
                line++;
            advance();
        }

        if (isEnd()) {
            Lox.error(line, "Unterminated string.");
            return;
        }

        advance();

        final String value = source.substring(start + 1, current - 1);
        addToken(TokenType.STRING, value);
    }

    private char peek() {
        if (isEnd()) {
            return '\0';
        }

        return source.charAt(current);

    }

    private char peekNext() {
        if (current + 1 >= source.length())
            return '\0';
        return source.charAt(current + 1);
    }

    private boolean matchAdvance(final char value) {
        if (isEnd()) {
            return false;
        }

        if (source.charAt(current) == value) {
            current++;
            return true;
        }

        return false;
    }

    private char advance() {
        return source.charAt(current++);
    }

    private void addToken(final TokenType type) {
        addToken(type, null);
    }

    private void addToken(final TokenType type, final Object literal) {
        final String text = source.substring(start, current);
        tokens.add(new Token(type, text, literal, line));
    }

    private boolean isEnd() {
        return current >= source.length();
    }

}
