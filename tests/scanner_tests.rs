use lox::run;

#[cfg(test)]
mod tests {
    use super::run;

    #[test]
    fn test_scanner_output() {
        let input = r#"
            // this is a comment
            (( )){} // grouping stuff
            !*+-/=<> <= == // operators
        "#;
            let expected_output = "(()){}!*+-/=<><===";
        let output = run(input);
        assert_eq!(expected_output, output);
    }
}

