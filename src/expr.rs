use crate::token::Token;

pub struct BinaryExpr {
    pub left: Box<Expr>,
    pub operator: Token,
    pub right: Box<Expr>,
}

pub enum LiteralValue {
    Number(f64),
    Str(String),
    Bool(bool),
    Nil,
}

pub enum Expr {
    Assign {
        name: Token,
        value: Box<Expr>,
    },
    Binary {
        left: Box<Expr>,
        operator: Token,
        right: Box<Expr>,
    },
    Call {
        callee: Box<Expr>,
        paren: Token,
        arguments: Vec<Token>,
    },
    Get {
        object: Box<Expr>,
        name: Token,
    },
    Grouping {
        expression: Box<Expr>,
    },
    Literal {
        value: LiteralValue,
    },
    Logical {
        left: Box<Expr>,
        operator: Token,
        right: Box<Expr>,
    },
    Set {
        object: Box<Expr>,
        name: Token,
        value: Box<Expr>,
    },
    Super {
        keyword: Token,
        method: Token,
    },
    This {
        keyword: Token,
    },
    Unary {
        operator: Token,
        right: Box<Expr>,
    },
    Variable {
        name: Token,
    },
}

pub trait Visitor<R> {
    fn visit_assign_expr(&mut self, expr: &Expr) -> R;
    fn visit_binary_expr(&mut self, expr: &Expr) -> R;
    fn visit_call_expr(&mut self, expr: &Expr) -> R;
    fn visit_get_expr(&mut self, expr: &Expr) -> R;
    fn visit_grouping_expr(&mut self, expr: &Expr) -> R;
    fn visit_literal_expr(&mut self, expr: &Expr) -> R;
    fn visit_logical_expr(&mut self, expr: &Expr) -> R;
    fn visit_set_expr(&mut self, expr: &Expr) -> R;
    fn visit_super_expr(&mut self, expr: &Expr) -> R;
    fn visit_this_expr(&mut self, expr: &Expr) -> R;
    fn visit_unary_expr(&mut self, expr: &Expr) -> R;
    fn visit_variable_expr(&mut self, expr: &Expr) -> R;
}

impl Expr {
    pub fn accept<R>(&self, visitor: &mut dyn Visitor<R>) -> R {
        match self {
            Expr::Assign { name, value } => visitor.visit_assign_expr(&self),
            Expr::Binary { left, operator, right } => visitor.visit_binary_expr(&self),
            Expr::Call { callee, paren, arguments } => visitor.visit_call_expr(&self),
            Expr::Get { object, name } => visitor.visit_get_expr(&self),
            Expr::Grouping { expression } => visitor.visit_grouping_expr(&self),
            Expr::Literal { value } => visitor.visit_literal_expr(&self),
            Expr::Logical { left, operator, right } => visitor.visit_logical_expr(&self),
            Expr::Set { object, name, value } => visitor.visit_set_expr(&self),
            Expr::Super { keyword, method } => visitor.visit_super_expr(&self),
            Expr::This { keyword } => visitor.visit_this_expr(&self),
            Expr::Unary { operator, right } => visitor.visit_unary_expr(&self),
            Expr::Variable { name } => visitor.visit_variable_expr(&self),
        }
    }
}
