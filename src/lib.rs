mod scanner;
mod token;
mod token_type;
mod keywords;
mod ast_printer;
mod expr;
mod stmt;

use crate::scanner::Scanner;

pub fn run(source: &str) -> String {
   let mut scanner = Scanner::new(source);
   let tokens = scanner.scan_tokens();

   let mut output = String::new();
   for token in tokens {
       print!("{}", token.get_lexeme());
       output.push_str(token.get_lexeme());
   }

   output
}

