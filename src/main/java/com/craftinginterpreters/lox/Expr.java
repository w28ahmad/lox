package com.craftinginterpreters.lox;

import java.util.List;

abstract class Expr {

	interface Visitor<R> {
		R visitBinaryExpr(Binary expr);
		R visitGroupingExpr(Grouping expr);
		R visitLiteralExpr(Literal expr);
		R visitUnaryExpr(Unary expr);
	}

	abstract <R> R accept(Visitor<R> visitor);

	static class Binary extends Expr {

		public final Expr left;
		public final Token operator;
		public final Expr right;

		public Binary(Expr left, Token operator, Expr right) {
			this.left = left;
			this.operator = operator;
			this.right = right;
		}

		@Override
		 <R> R accept(Visitor<R> visitor) {
			return visitor.visitBinaryExpr(this);
		}
	}

	static class Grouping extends Expr {

		public final Expr expression;

		public Grouping(Expr expression) {
			this.expression = expression;
		}

		@Override
		 <R> R accept(Visitor<R> visitor) {
			return visitor.visitGroupingExpr(this);
		}
	}

	static class Literal extends Expr {

		public final Object value;

		public Literal(Object value) {
			this.value = value;
		}

		@Override
		 <R> R accept(Visitor<R> visitor) {
			return visitor.visitLiteralExpr(this);
		}
	}

	static class Unary extends Expr {

		public final Token operator;
		public final Expr right;

		public Unary(Token operator, Expr right) {
			this.operator = operator;
			this.right = right;
		}

		@Override
		 <R> R accept(Visitor<R> visitor) {
			return visitor.visitUnaryExpr(this);
		}
	}
}
