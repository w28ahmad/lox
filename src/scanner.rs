use crate::token::Token;

pub struct Scanner { }

impl Scanner {

    pub fn new(source: &str) -> Scanner {
        
        Scanner {  }
    }

    pub fn scan_tokens(&self) -> Vec<Token> {
        // Dummy code
        let mut tokens = Vec::new();
        let token = Token::new("Helo World");
        tokens.push(token);
        tokens
    }

}
