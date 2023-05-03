mod scanner;
mod token;
pub mod token_type;

use std::env;
use std::io::{self, BufRead, BufReader, Write};
use std::path::Path;
use std::str;
use std::fs;

use crate::scanner::Scanner;

// static mut HAD_ERROR: bool = false;

// fn report(line: i32, location: &String, message: &String) {
//     eprintln!("[line {}] Error {}: {}", line, location, message);    
// }

// fn error(line: i32, message: &String) {
//     report(line, &String::from(""), message);
// }

fn run(source: &str) {
   let mut scanner = Scanner::new(source);
   let tokens = scanner.scan_tokens();

   for token in tokens {
       println!("{}", token.get_lexeme());
   }
}

fn run_prompt() -> io::Result<()> {
    let stdin = io::stdin();
    let mut reader = BufReader::new(stdin.lock());

    loop {
        print!("> ");
        io::stdout().flush()?;

        let mut line = String::new();
        let bytes_read = reader.read_line(&mut line)?;

        if bytes_read == 0 {
            break;
        }

        run(&line.trim());
    }

    Ok(())
}

fn run_file(path: &str) -> io::Result<()> {
    let bytes = fs::read(Path::new(path))?;
    let content = str::from_utf8(&bytes).map_err(|e| io::Error::new(io::ErrorKind::InvalidData, e))?;
    run(content);
    Ok(())
}

fn main() {
    let args: Vec<String> = env::args().collect();

    if args.len() > 2 {
        println!("Usage: lox [script]");
        std::process::exit(64)
    } else if args.len() == 2 {
        if let Err(e) = run_file(&args[1]) {
            eprintln!("Error: {}", e);
        }
    } else {
        if let Err(e) = run_prompt() {
            eprintln!("Error: {}", e);
        }
    }
}
