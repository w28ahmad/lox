package com.craftinginterpreters.lox;

import java.util.ArrayList;
import java.util.List;

public class Scanner {

    String source;
    List<Token> tokens = new ArrayList<>();

    private int current = 0;
    private int start = 0;
    private int line = 1;

    public Scanner(String source) {
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
        char c = advance();
        
        switch (c) {
            case '(': addToken(TokenType.LEFT_PAREN); break;
            case ')': addToken(TokenType.RIGHT_PAREN); break;
            case '{': addToken(TokenType.LEFT_BRACE); break;
            case '}': addToken(TokenType.RIGHT_BRACE); break;
            case ',': addToken(TokenType.COMMA); break;
            case '.': addToken(TokenType.DOT); break;
            case '-': addToken(TokenType.MINUS); break;
            case '+': addToken(TokenType.PLUS); break;
            case ';': addToken(TokenType.SEMICOLON); break;
            case '*': addToken(TokenType.STAR); break;
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
            default:
                      Lox.error(line, "Unexpected character.");
                      break;
        }
    }

    private char peek() {
        if (isEnd()) {
            return '\0';
        }

        return source.charAt(current);

    }

    private boolean matchAdvance(char value) {
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

    private void addToken(TokenType type) {
        addToken(type, null);
    }

    private void addToken(TokenType type, Object literal) {
        String text = source.substring(start, current);
        tokens.add(new Token(type, text, literal, line));
    }


    private boolean isEnd() {
        return current >= source.length();
    }

}
