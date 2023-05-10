use crate::token::Token;
use crate::expr::Expr;

pub struct VariableExpr {
    pub name: Token,
}

pub struct FunctionStmt {
    pub name: Token,
    pub params: Vec<Token>,
    pub body: Vec<Box<Stmt>>,
}

pub enum Stmt {
    Block {
        statements: Vec<Box<Stmt>>,
    },
    Class {
        name: Token,
        superclass: Option<Box<VariableExpr>>, 
        methods: Vec<FunctionStmt>,
    },
    Expression {
        expression: Box<Expr>,
    },
    Function {
        name: Token,
        params: Vec<Token>,
        body: Vec<Box<Stmt>>,
    },
    If {
        condition: Box<Expr>,
        then_branch: Box<Stmt>,
        else_branch: Box<Stmt>,
    },
    Print {
        expression: Box<Expr>,
    },
    Return {
        keyword: Token,
        value: Box<Expr>,
    },
    Var {
        name: Token,
        initializer: Box<Expr>,
    },
    While {
        condition: Box<Expr>,
        body: Box<Stmt>,
    },
}

pub trait Visitor<R> {
    fn visit_block_stmt(&mut self, expr: &Stmt) -> R;
    fn visit_class_stmt(&mut self, expr: &Stmt) -> R;
    fn visit_expression_stmt(&mut self, expr: &Stmt) -> R;
    fn visit_function_stmt(&mut self, expr: &Stmt) -> R;
    fn visit_if_stmt(&mut self, expr: &Stmt) -> R;
    fn visit_print_stmt(&mut self, expr: &Stmt) -> R;
    fn visit_return_stmt(&mut self, expr: &Stmt) -> R;
    fn visit_var_stmt(&mut self, expr: &Stmt) -> R;
    fn visit_while_stmt(&mut self, expr: &Stmt) -> R;
}

impl Stmt {
    pub fn accept<R>(&self, visitor: &mut dyn Visitor<R>) -> R {
        match self {
            Stmt::Block { statements } => visitor.visit_block_stmt(&self),
            Stmt::Class { name, superclass, methods } => visitor.visit_class_stmt(&self),
            Stmt::Expression { expression } => visitor.visit_expression_stmt(&self),
            Stmt::Function { name, params, body } => visitor.visit_function_stmt(&self),
            Stmt::If { condition, then_branch, else_branch } => visitor.visit_if_stmt(&self),
            Stmt::Print { expression } => visitor.visit_print_stmt(&self),
            Stmt::Return { keyword, value } => visitor.visit_return_stmt(&self),
            Stmt::Var { name, initializer } => visitor.visit_var_stmt(&self),
            Stmt::While { condition, body } => visitor.visit_while_stmt(&self),
        }
    }
}
