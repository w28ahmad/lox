use std::env;
use std::io::{self, BufRead, BufReader, Write};
use std::path::Path;
use std::str;
use std::fs;

use ast_printer::AstPrinter;
use expr::Expr;
use stmt::Stmt;
use token::{Token, TokenType};
use lox::run;
// static mut HAD_ERROR: bool = false;

// fn report(line: i32, location: &String, message: &String) {
//     eprintln!("[line {}] Error {}: {}", line, location, message);    
// }

// fn error(line: i32, message: &String) {
//     report(line, &String::from(""), message);
// }

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

fn run_tests() {
    let expression = Expr::Binary {
        left: Box::new(Expr::Literal {
            value: 123.0,
        }),
        operator: Token::new(TokenType::Plus, String::from("+"), None, 1),
        right: Box::new(Expr::Grouping {
            expression: Box::new(Expr::Literal {
                value: 45.67,
            }),
        }),
    };

    let printer = AstPrinter::new();
    println!("{}", printer.print(&expression));
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
    } else if args.len() == 1 {
        run_tests();
    } else {
        if let Err(e) = run_prompt() {
            eprintln!("Error: {}", e);
        }
    }
}
