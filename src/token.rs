use crate::token_type::TokenType;

pub struct Token { 
    token_type: TokenType,
    lexeme: String,
    literal: Option<LiteralValue>,
    line: usize,
}

pub enum LiteralValue {
    Number(f64),
    Text(String),
}

impl Token {

    pub fn new(token_type: TokenType, lexeme: &str, literal: Option<LiteralValue>, line:usize) -> Token {
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

    pub fn get_literal(&self) -> &Option<LiteralValue> {
        &self.literal
    }

    pub fn get_line(&self) -> usize {
        self.line
    }

    pub fn to_string(&self) -> String {
        let literal_str = match &self.literal {
            Some(LiteralValue::Number(num)) => num.to_string(),
            Some(LiteralValue::Text(text)) => text.clone(),
            None => String::from(""),
        };

        format!(
            "{} {} {}",
            self.token_type.to_string(),
            self.lexeme,
            literal_str,
        )
    }
}
