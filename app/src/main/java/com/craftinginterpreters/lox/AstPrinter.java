package com.craftinginterpreters.lox;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class AstPrinter implements Expr.Visitor<String>, Stmt.Visitor<String> {
    public static final String INDENT = "    ";
    private int depth = 0;

    String print(Expr expr) {
        return expr.accept(this);
    }

    String print(List<Stmt> statements) {
        return statements.stream()
                .map(s -> s.accept(this))
                .collect(Collectors.joining("\n"));
    }

    @Override
    public String visitAssignExpr(Expr.Assign expr) {
        return parenthesize("assign:" + expr.name.lexeme, expr.value);
    }

    @Override
    public String visitBinaryExpr(Expr.Binary expr) {
        return parenthesize(expr.operator.lexeme,
                expr.left, expr.right);
    }

    @Override
    public String visitTernaryExpr(Expr.Ternary expr) {
        return parenthesize("ternary" + expr.operator1.lexeme + expr.operator2.lexeme, expr.first, expr.second, expr.third);
    }

    @Override
    public String visitGroupingExpr(Expr.Grouping expr) {
        return parenthesize("group", expr.expression);
    }

    @Override
    public String visitLiteralExpr(Expr.Literal expr) {
        if (expr.value == null) return "nil";
        if (expr.value instanceof String) return "\"" + expr.value + "\"";
        return expr.value.toString();
    }

    @Override
    public String visitLogicalExpr(Expr.Logical expr) {
        return parenthesize(expr.operator.lexeme, expr.left, expr.right);
    }

    @Override
    public String visitUnaryExpr(Expr.Unary expr) {
        return parenthesize(expr.operator.lexeme, expr.right);
    }

    @Override
    public String visitVariableExpr(Expr.Variable expr) {
        return "`" + expr.name.lexeme + "`";
    }

    private String parenthesize(String name, Expr... exprs) {
        StringBuilder builder = new StringBuilder();

        builder.append("(").append(name);
        for (Expr expr : exprs) {
            builder.append(" ");
            builder.append(expr.accept(this));
        }
        builder.append(")");

        return builder.toString();
    }

    public static void main(String[] args) {
        Expr expression = new Expr.Binary(
                new Expr.Unary(
                        new Token(TokenType.MINUS, "-", null, 1),
                        new Expr.Literal(123)),
                new Token(TokenType.STAR, "*", null, 1),
                new Expr.Grouping(
                        new Expr.Literal(45.67)));

        System.out.println(new AstPrinter().print(expression));
    }

    @Override
    public String visitBlockStmt(Stmt.Block stmt) {
        StringBuilder sb = new StringBuilder("{");

        sb.append(INDENT.repeat(depth));
        depth += 1;
        for (Stmt statement : stmt.statements) {
            sb.append("\n");
            sb.append(INDENT.repeat(depth));
            sb.append(statement.accept(this));
        }
        depth -= 1;
        sb.append("\n");
        sb.append(INDENT.repeat(depth));
        sb.append("}");

        return sb.toString();
    }

    @Override
    public String visitExpressionStmt(Stmt.Expression stmt) {
        return parenthesize(";", stmt.expression);
    }

    @Override
    public String visitIfStmt(Stmt.If stmt) {
        StringBuilder sb = new StringBuilder("IF(");
        sb.append(stmt.condition.accept(this));
        sb.append(") THEN [\n");
        depth += 1;
        sb.append(INDENT.repeat(depth));
        sb.append(stmt.thenBranch.accept(this));
        if (stmt.elseBranch == null) {
            depth -= 1;
            sb.append(INDENT.repeat(depth));
            sb.append("]");
        } else {
            sb.append("\n");
            sb.append(INDENT.repeat(depth));
            sb.append("] ELSE [\n");
            depth += 1;
            sb.append(stmt.elseBranch.accept(this));
            depth -= 1;
            sb.append(INDENT.repeat(depth));
        }
        return sb.toString();
    }

    @Override
    public String visitPrintStmt(Stmt.Print stmt) {
        return parenthesize("PRINT", stmt.expression);
    }

    @Override
    public String visitVarStmt(Stmt.Var stmt) {
        return "VAR:" + stmt.name.lexeme + "=" +
                (stmt.initializer == null ?
                        "null" :
                        stmt.initializer.accept(this));
    }

    @Override
    public String visitWhileStmt(Stmt.While stmt) {
        StringBuilder sb = new StringBuilder("WHILE(");
        sb.append(stmt.condition.accept(this));
        sb.append(")");
        sb.append(stmt.body.accept(this));
        return sb.toString();
    }
}