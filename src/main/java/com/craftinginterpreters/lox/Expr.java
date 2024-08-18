package com.craftinginterpreters.lox;

import java.util.List;

abstract class Expr {

	class Binary extends Expr {

		private final Expr left;
		private final Token operator;
		private final Expr right;

		public Binary(Expr left, Token operator, Expr right) {
			this.left = left;
			this.operator = operator;
			this.right = right;
		}
	}

	class Grouping extends Expr {

		private final Expr expression;

		public Grouping(Expr expression) {
			this.expression = expression;
		}
	}

	class Literal extends Expr {

		private final Object value;

		public Literal(Object value) {
			this.value = value;
		}
	}

	class Unary extends Expr {

		private final Token operator;
		private final Expr right;

		public Unary(Token operator, Expr right) {
			this.operator = operator;
			this.right = right;
		}
	}
}
