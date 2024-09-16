package com.craftinginterpreters.lox;

import java.util.List;

/*
*  expression     → equality ;
*  equality       → comparison ( ( "!=" | "==" ) comparison )* ;
*  comparison     → term ( ( ">" | ">=" | "<" | "<=" ) term )* ;
*  term           → factor ( ( "-" | "+" ) factor )* ;
*  factor         → unary ( ( "/" | "*" ) unary )* ;
*  unary          → ( "!" | "-" ) unary
*                 | primary ;
*  primary        → NUMBER | STRING | "true" | "false" | "nil"
*                     | "(" expression ")" ;
*
*
*/

class Parser {
    private int current = 0;
    private final List<Token> tokens;
    private static class ParseError extends RuntimeException {}

    Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    Expr parse() {
        try {
            return expression();
        } catch (ParseError e) {
            return null;
        }
    }

    private Expr expression() throws ParseError {
        return equality();
    }

	private Expr equality() throws ParseError {
        Expr expr = comparison();

        while (match(TokenType.BANG_EQUAL, TokenType.EQUAL_EQUAL)) {
            Token operator = previous();
            Expr right = comparison();
            expr = new Expr.Binary(expr, operator, right);
        }

        return expr;
    }

    private boolean match(TokenType... tokens) {
        for (TokenType token : tokens) {
            if (check(token)) {
                advance();
                return true;
            }
        }
        return false;
    }

    private boolean check(TokenType type) {
        return peek().type == type;
    }

    private Token previous() {
        return tokens.get(current-1);
    }

    private Expr comparison() throws ParseError {
        Expr expr = term();
        while (match(TokenType.GREATER, TokenType.GREATER_EQUAL, TokenType.LESS, TokenType.LESS_EQUAL)) {
            Token operator = previous();
            Expr right = term();
            expr = new Expr.Binary(expr, operator, right);
        }
        return expr;
    }

    private Expr term() throws ParseError {
        Expr expr = factor();
        
        while (match(TokenType.MINUS, TokenType.PLUS)) {
            Token operator = previous();
            Expr right = factor();
            expr = new Expr.Binary(expr, operator, right);
        }

        return expr;
    }

    private Expr factor() throws ParseError {
        Expr expr = unary();
        
        while (match(TokenType.STAR, TokenType.SLASH)) {
            Token operator = previous();
            Expr right = unary();
            expr = new Expr.Binary(expr, operator, right);
        }

        return expr;
    }

    private Expr unary() throws ParseError {
        if (match(TokenType.BANG, TokenType.MINUS)) {
            Token operator = previous();
            Expr right = unary();
            return new Expr.Unary(operator, right);
        }

        return primary();
    }

    private Expr primary() throws ParseError {
        if (match(TokenType.FALSE)) return new Expr.Literal(false);
        if (match(TokenType.TRUE)) return new Expr.Literal(true);
        if (match(TokenType.NIL)) return new Expr.Literal(null);

        if (match(TokenType.NUMBER, TokenType.STRING)) {
            return new Expr.Literal(previous().literal);
        }

        if (match(TokenType.LEFT_PAREN)) {
            Expr expr = expression();
            consume(TokenType.RIGHT_PAREN, "Expect ')' after expression.");
            return new Expr.Grouping(expr);
        }

        throw (ParseError) error(peek(), "Expect expression.");
    }

    private Token consume(TokenType token, String message) throws ParseError {
        if (check(token)) return advance();
        throw (ParseError) error(peek(), message);
    }

    private Exception error(Token token, String message) {
        Lox.error(token, message);
        return new ParseError();
    }

    private Token advance() {
        if (!isAtEnd()) current++;
        return previous();
    }

    private boolean isAtEnd() {
        return peek().type == TokenType.EOF;
    }

    private Token peek() {
        return tokens.get(current);
    }


    // TODO: Synchronize on statement boundaries
    private void synchronize() {
        advance();

        while (!isAtEnd()) {
            if (previous().type == TokenType.SEMICOLON) return;

            switch (peek().type) {
                case CLASS:
                case FUN:
                case VAR:
                case FOR:
                case IF:
                case WHILE:
                case PRINT:
                case RETURN:
                    return;
                default:
                    break;
            }

            advance();
        }
    }

}
