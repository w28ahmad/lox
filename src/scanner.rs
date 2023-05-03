use crate::token::Token;
use crate::token_type::TokenType;

pub struct Scanner { 
    source: String,
    tokens: Vec<Token>,
    start: i32,
    current: i32,
    line: usize,
}

impl Scanner {

    pub fn new(source: &str) -> Scanner {
        
        Scanner {
            source: String::from(source),
            tokens: Vec::new(),
            start: 0,
            current: 0,
            line: 1
        }
        
    }

    pub fn scan_tokens(&mut self) -> &Vec<Token> {

        while !self.is_at_end() {
            self.start = self.current; 
            self.scan_token();
        }

        self.tokens.push(Token::new(
            TokenType::EOF,
            &String::from(""),
            None,
            self.line,
        ));

        &self.tokens
    }


    fn scan_token(&mut self) {

        let c = self.advance();
       
        let token_type = {
            match c {
                
                // Single Letter Tokens
                '(' => TokenType::LeftParen,
                ')' => TokenType::RightParen,
                '{' => TokenType::LeftBrace,
                '}' => TokenType::RightBrace,
                ',' => TokenType::Comma,
                '.' => TokenType::Dot,
                '-' => TokenType::Minus,
                '+' => TokenType::Plus,
                ';' => TokenType::Semicolon,
                '*' => TokenType::Star,
                
                // Double Letter Tokens
                '!' => self.conditional_select('=', TokenType::BangEqual, TokenType::Bang),
                '=' => self.conditional_select('=', TokenType::EqualEqual, TokenType::Equal),
                '<' => self.conditional_select('=', TokenType::LessEqual, TokenType::Less),
                '>' => self.conditional_select('=', TokenType::GreaterEqual, TokenType::Greater),
                
                // Comments
                '/' => {
                    if self.match_next_token('/') {
                        while self.peek() != '\n' && !self.is_at_end() { self.advance(); }
                        TokenType::NOP
                    } else {
                        TokenType::Slash
                    }
                }
                
                // Meaningles characters
                ' ' | '\r' | '\t' => TokenType::NOP,
                '\n' => {
                    self.line += 1;
                    TokenType::NOP
                },

                // Unknown Character
                _ =>  { 
                    eprintln!("[line {}] Error: {}", self.line, "Unexpected Character.");
                    std::process::exit(65)
                }
            }
        };
        
        if token_type != TokenType::NOP {
            self.add_token(token_type);
        }
    }
    
    // Look one character ahead
    fn peek(&self) -> char {
        if self.is_at_end() { return '\0' }
        return self.source.chars().nth(self.current as usize).unwrap();
    }

    // Select a token depending on the next token
    fn conditional_select(&mut self, next_token: char, match_type: TokenType, mismatch_type: TokenType) -> TokenType {
        if self.match_next_token(next_token) {
            return match_type;
        } else {
            return mismatch_type;
        }
    }

    // Advance one character ahread
    // Return the character
    fn advance(&mut self) -> char { 
        let c = self.source.chars().nth(self.current as usize).unwrap();
        self.current += 1;
        c
    }

    // Check if the next token matches the expected
    fn match_next_token(&mut self, expected: char) -> bool {

        if self.is_at_end() {
            return false;
        }
        
        if self.source[self.current as usize..].chars().next().unwrap() != expected {
            return false
        }

        self.current += 1;
        true
    }

    // Add a non-literal token
    fn add_token(&mut self, token_type: TokenType) {
        self.add_token_with_literal(token_type, None);
    }

    // Add a token with a literal
    // Ex.
    // x = 42
    // 42 is a literal
    fn add_token_with_literal(&mut self, token_type: TokenType, literal: Option<String>) {
       let text = self.source[self.start as usize..self.current as usize].to_string();
       let token = Token::new(token_type, &text, literal, self.line);
       self.tokens.push(token);
    }

    // Check if we have reached the end of source
    fn is_at_end(&self) -> bool {
        self.current >= self.source.len() as i32
    }
}


