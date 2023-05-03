use crate::token_type::TokenType;

pub struct Token { 
    token_type: TokenType,
    lexeme: String,
    literal: Option<String>,
    line: usize,
}

impl Token {

    pub fn new(token_type: TokenType, lexeme: &str, literal: Option<String>, line:usize) -> Token {
        Token {
            token_type,
            lexeme: lexeme.to_string(),
            literal,
            line,
        }
    }

    pub fn get_token_type(&self) -> &TokenType {
        &self.token_type
    }

    pub fn get_lexeme(&self) -> &String {
        &self.lexeme
    }

    pub fn get_literal(&self) -> &Option<String> {
        &self.literal
    }

    pub fn get_line(&self) -> usize {
        self.line
    }

    pub fn to_string(&self) -> String {
        format!(
            "{} {} {}",
            self.token_type.to_string(),
            self.lexeme,
            self.literal.as_ref().unwrap_or(&String::from("")),
        )
    }
}
