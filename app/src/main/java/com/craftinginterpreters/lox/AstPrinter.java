package com.craftinginterpreters.lox;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
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
    public String visitCallExpr(Expr.Call expr) {
        StringBuilder args = new StringBuilder();
        for (Expr arg : expr.arguments) {
            if (args.length() > 0) {
                args.append(", ");
            }
            args.append(arg.accept(this));
        }

        return "CALL[" + expr.callee.accept(this) +
                "](" + args.toString() + ")";
    }

    @Override
    public String visitGetExpr(Expr.Get expr) {
        return "(GET " + expr.object.accept(this) + " DOT " + expr.name.lexeme + ")";
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
    public String visitSetExpr(Expr.Set expr) {
        return "(SET " + expr.object.accept(this) + " DOT " + expr.name.lexeme +
                " TO " + expr.value.accept(this) + ")";
    }

    @Override
    public String visitThisExpr(Expr.This expr) {
        return "THIS";
    }

    @Override
    public String visitUnaryExpr(Expr.Unary expr) {
        return parenthesize(expr.operator.lexeme, expr.right);
    }

    @Override
    public String visitVariableExpr(Expr.Variable expr) {
        return "(VAR " + expr.name.lexeme + ")";
    }

    @Override
    public String visitFunctionExpExpr(Expr.FunctionExp expr) {
        StringBuilder sb = new StringBuilder("FUN_INLINE");
        sb.append("(");
        sb.append(expr.params.stream().map(t -> t.lexeme)
                .collect(Collectors.joining(", ")));
        sb.append(") {\n");
        depth += 1;
        sb.append(expr.body.stream().map(s -> INDENT.repeat(depth) + s.accept(this))
                .collect(Collectors.joining("\n")));
        depth -= 1;
        sb.append("\n}");
        return sb.toString();
    }

    private String parenthesize(String name, Expr... exprs) {
        StringBuilder builder = new StringBuilder();

        builder.append("(").append(name);
        for (Expr expr : Arrays.stream(exprs).filter(Objects::nonNull).collect(Collectors.toList())) {
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
    public String visitClassStmt(Stmt.Class stmt) {
        StringBuilder sb = new StringBuilder("CLASS:");
        sb.append(stmt.name.lexeme);
        sb.append("{\n");
        depth += 1;
        sb.append(stmt.methods.stream().map(func -> INDENT.repeat(depth) + func.accept(this))
                .collect(Collectors.joining("\n")));
        depth -= 1;
        sb.append("\n}");
        return sb.toString();
    }

    @Override
    public String visitExpressionStmt(Stmt.Expression stmt) {
        return parenthesize(";", stmt.expression);
    }

    @Override
    public String visitFunctionStmt(Stmt.Function stmt) {
        StringBuilder sb = new StringBuilder("FUN:");
        sb.append(stmt.name);
        sb.append("(");
        sb.append(stmt.params.stream().map(t -> t.lexeme)
                .collect(Collectors.joining(", ")));
        sb.append(") {\n");
        depth += 1;
        sb.append(stmt.body.stream().map(s -> INDENT.repeat(depth) + s.accept(this))
                .collect(Collectors.joining("\n")));
        depth -= 1;
        sb.append("\n}");
        return sb.toString();
    }

    @Override
    public String visitIfStmt(Stmt.If stmt) {
        StringBuilder sb = new StringBuilder("IF:");
        sb.append(stmt.condition.accept(this));
        sb.append(" THEN [\n");
        depth += 1;
        sb.append(INDENT.repeat(depth));
        sb.append(stmt.thenBranch.accept(this));
        if (stmt.elseBranch == null) {
            depth -= 1;
            sb.append("\n");
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
    public String visitReturnStmt(Stmt.Return stmt) {
        return parenthesize("RETURN", stmt.value);
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

    @Override
    public String visitBreakStmt(Stmt.Break stmt) {
        return "BREAK";
    }
}
