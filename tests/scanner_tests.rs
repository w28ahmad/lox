use lox::run;

#[cfg(test)]
mod tests {
    use super::run;

     #[test]
    fn single_tokens_test() {
        let input = "(){}";
        let expected_output = "(){}";
        let output = run(input);
        assert_eq!(expected_output, output);
    }

    #[test]
    fn double_tokens_test() {
        let input = "== != <= <!";
        let expected_output = "==!=<=<!";
        let output = run(input);
        assert_eq!(expected_output, output);
    }

    #[test]
    fn comments_test() {
        let input = "//This is a comment";
        let expected_output = "";
        let output = run(input);
        assert_eq!(expected_output, output);
    }

    #[test]
    fn single_double_tokens_and_comments_test() {
        let input = r#"
            // this is a comment
            (( )){} // grouping stuff
            !*+-/=<> <= == // operators
        "#;
            let expected_output = "(()){}!*+-/=<><===";
        let output = run(input);
        assert_eq!(expected_output, output);
    }
    
    #[test]
    fn string_test() {
        let input = "\"This a string\"";
        let expected_output = "\"This a string\"";
        let output = run(input);
        assert_eq!(expected_output, output);
    }

    #[test]
    fn number_test() {
        let input = "3";
        let expected_output = "3";
        let output = run(input);
        assert_eq!(expected_output, output);
    }

    #[test]
    fn keywords_test() {
        let input = "and";
        let expected_output = "and";
        let output = run(input);
        assert_eq!(input, output);
    }
    #[test]
    #[ignore]
    fn string_test_fail() {
        let input = "This a string";
        let expected_output = "This a string";
        let output = run(input);
        assert_eq!(expected_output, output);
    }
}


