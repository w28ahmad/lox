
pub struct Token { 
    lexeme: String
}

impl Token {

    pub fn new(source: &str) -> Token {

        Token { lexeme: String::from(source) }
    }

    pub fn get_lexeme(&self) -> &String {
        &self.lexeme
    }
}
