package com.craftinginterpreters.lox;

import java.util.*;

class Resolver implements Expr.Visitor<Void>, Stmt.Visitor<Void> {
    private enum FunctionType {
        NONE,
        FUNCTION,
        INITIALIZER,
        METHOD
    }

    private enum VariableState {
        DECLARED,
        DEFINED,
        INITIALIZED,
        USED
    }

    private enum ClassType {
        NONE,
        CLASS,
        SUBCLASS
    }

    private ClassType currentClass = ClassType.NONE;

    private final Interpreter interpreter;

    // var name to (declaration line num, var stat)
    private final Stack<Map<String, Tuple<Integer, VariableState>>> scopes = new Stack<>();
    private FunctionType currentFunction = FunctionType.NONE;

    Resolver(Interpreter interpreter) {
        this.interpreter = interpreter;
    }

    @Override
    public Void visitBlockStmt(Stmt.Block stmt) {
        beginScope();
        resolve(stmt.statements);
        endScope();
        return null;
    }

    @Override
    public Void visitClassStmt(Stmt.Class stmt) {
        ClassType enclosingClass = currentClass;
        currentClass = ClassType.CLASS;

        declare(stmt.name);
        define(stmt.name, true);

        if (stmt.superclass != null && stmt.name.lexeme.equals(stmt.superclass.name.lexeme)) {
            Lox.error(stmt.superclass.name, "A class can't inherit from itself.");
        }

        if (stmt.superclass != null) {
            currentClass = ClassType.SUBCLASS;
            resolve(stmt.superclass);
        }

        if (stmt.superclass != null) {
            beginScope();
            scopes.peek().put("super", new Tuple<>(stmt.name.line, VariableState.USED));
        }

        beginScope();
        // we pretend that `this` is already "used" because we don't consider it an error if `this` is not used.
        scopes.peek().put("this", new Tuple<>(stmt.name.line, VariableState.USED));

        for (Stmt.Function method : stmt.methods) {
            FunctionType declaration = FunctionType.METHOD;
            if (method.name.lexeme.equals("init")) {
                declaration = FunctionType.INITIALIZER;
            }

            resolveFunction(method, declaration);
        }

        endScope();

        if (stmt.superclass != null) {
            endScope();
        }

        currentClass = enclosingClass;
        return null;
    }

    @Override
    public Void visitExtendStmt(Stmt.Extend stmt) {
        if (currentClass != ClassType.NONE) {
            Lox.error(stmt.name, "Cannot extend a class while still defining another class");
        }
        ClassType enclosingClass = currentClass;
        currentClass = ClassType.CLASS;

        beginScope();
        // we pretend that `this` is already "used" because we don't consider it an error if `this` is not used.
        scopes.peek().put("this", new Tuple<>(stmt.name.line, VariableState.USED));

        for (Stmt.Function method : stmt.methods) {
            FunctionType declaration = FunctionType.METHOD;
            if (method.name.lexeme.equals("init")) {
                Lox.error(method.name, "Can't define a constructor while extending a class.");
            }

            resolveFunction(method, declaration);
        }

        endScope();

        currentClass = enclosingClass;
        return null;
    }

    void resolve(List<Stmt> statements) {
        for (Stmt statement : statements) {
            resolve(statement);
        }
    }

    private void resolve(Stmt stmt) {
        stmt.accept(this);
    }

    private void resolve(Expr expr) {
        expr.accept(this);
    }

    private void beginScope() {
        scopes.push(new HashMap<>());
    }

    private void endScope() {
        Map<String, Tuple<Integer, VariableState>> scope = scopes.pop();
        scope.forEach((v, lineAndState) -> {
            Integer lineNum = lineAndState.a;
            VariableState state = lineAndState.b;
            switch (state) {
                case DECLARED -> Lox.error(lineNum, "Variable declared but is never defined: " + v + " (this almost certainly a bug in jlox!)");
                case DEFINED -> Lox.error(lineNum, "Variable defined but not initialized: " + v);
                case INITIALIZED -> Lox.error(lineNum, "Variable initialized but not used: " + v);
                case USED -> {
                    // variable used as expected, so no error to report
                }
            }
        });
    }

    @Override
    public Void visitVarStmt(Stmt.Var stmt) {
        declare(stmt.name);
        if (stmt.initializer != null) {
            resolve(stmt.initializer);
        }
        define(stmt.name, stmt.initializer != null);
        return null;
    }

    private void declare(Token name) {
        if (scopes.isEmpty()) return;

        Map<String, Tuple<Integer, VariableState>> scope = scopes.peek();
        if (scope.containsKey(name.lexeme)) {
            Lox.error(name, "Already variable with this name in this scope.");
        }
        scope.put(name.lexeme, new Tuple<>(name.line, VariableState.DECLARED));
    }

    private void define(Token name, boolean initialized) {
        if (scopes.isEmpty()) return;

        Tuple<Integer, VariableState> tuple = scopes.peek().get(name.lexeme);
        scopes.peek().put(name.lexeme, new Tuple<>(tuple.a, initialized ? VariableState.INITIALIZED : VariableState.DEFINED));
    }


    @Override
    public Void visitVariableExpr(Expr.Variable expr) {
        if (!scopes.isEmpty()) {
            Tuple<Integer, VariableState> tuple = scopes.peek().get(expr.name.lexeme);
            if (tuple != null && tuple.b == VariableState.DECLARED) {
                Lox.error(expr.name, "Can't read local variable in its own initializer.");
            }
        }

        resolveLocal(expr, expr.name, false);
        return null;
    }

    @Override
    public Void visitFunctionExpExpr(Expr.FunctionExp expr) {
        beginScope();
        for (Token param : expr.params) {
            declare(param);
            define(param, true);
        }
        resolve(expr.body);
        endScope();

        return null;
    }

    private void resolveLocal(Expr expr, Token name, boolean isAssigning) {
        for (int i = scopes.size() - 1; i >= 0; i--) {
            Map<String, Tuple<Integer, VariableState>> scope = scopes.get(i);
            Tuple<Integer, VariableState> tuple = scope.get(name.lexeme);
            if (tuple != null) {
                interpreter.resolve(expr, scopes.size() - 1 - i);

                boolean varInitedAlready = tuple.b == VariableState.INITIALIZED || tuple.b == VariableState.USED;
                if (isAssigning && !varInitedAlready) {
                    scope.put(name.lexeme, new Tuple<>(tuple.a, VariableState.INITIALIZED));
                } else {
                    switch (tuple.b) {
                        case DECLARED -> Lox.error(tuple.a, "Variable is declared but not defined before usage: " + name.lexeme + " (this is almost certainly a bug in jlox!)");
                        case DEFINED -> Lox.error(tuple.a, "Variable is defined but not initialized before usage: " + name.lexeme);
                        case INITIALIZED -> scope.put(name.lexeme, new Tuple<>(tuple.a, VariableState.USED)); // record that we've used the variable
                        case USED -> {
                            // nothing to do
                        }
                    }
                }
                return;
            }
        }
    }

    @Override
    public Void visitAssignExpr(Expr.Assign expr) {
        resolve(expr.value);
        resolveLocal(expr, expr.name, true);
        return null;
    }

    @Override
    public Void visitFunctionStmt(Stmt.Function stmt) {
        declare(stmt.name);
        define(stmt.name, true);

        resolveFunction(stmt, FunctionType.FUNCTION);
        return null;
    }

    private void resolveFunction(Stmt.Function function, FunctionType type) {
        FunctionType enclosingFunction = currentFunction;
        currentFunction = type;

        beginScope();
        for (Token param : function.params) {
            declare(param);
            define(param, true);
        }
        resolve(function.body);
        endScope();

        currentFunction = enclosingFunction;
    }

    @Override
    public Void visitExpressionStmt(Stmt.Expression stmt) {
        resolve(stmt.expression);
        return null;
    }

    @Override
    public Void visitIfStmt(Stmt.If stmt) {
        resolve(stmt.condition);
        resolve(stmt.thenBranch);
        if (stmt.elseBranch != null) resolve(stmt.elseBranch);
        return null;
    }

    @Override
    public Void visitPrintStmt(Stmt.Print stmt) {
        resolve(stmt.expression);
        return null;
    }

    @Override
    public Void visitReturnStmt(Stmt.Return stmt) {
        if (currentFunction == FunctionType.NONE) {
            Lox.error(stmt.keyword, "Can't return from top-level code.");
        }

        if (stmt.value != null) {
            if (currentFunction == FunctionType.INITIALIZER) {
                Lox.error(stmt.keyword, "Can't return a value from an initializer.");
            }
            resolve(stmt.value);
        }

        return null;
    }

    @Override
    public Void visitWhileStmt(Stmt.While stmt) {
        resolve(stmt.condition);
        resolve(stmt.body);
        return null;
    }

    @Override
    public Void visitBreakStmt(Stmt.Break stmt) {
        return null;
    }

    @Override
    public Void visitBinaryExpr(Expr.Binary expr) {
        resolve(expr.left);
        resolve(expr.right);
        return null;
    }

    @Override
    public Void visitTernaryExpr(Expr.Ternary expr) {
        resolve(expr.first);
        resolve(expr.second);
        resolve(expr.third);
        return null;
    }

    @Override
    public Void visitCallExpr(Expr.Call expr) {
        resolve(expr.callee);

        for (Expr argument : expr.arguments) {
            resolve(argument);
        }

        return null;
    }

    @Override
    public Void visitGetExpr(Expr.Get expr) {
        resolve(expr.object);
        return null;
    }

    @Override
    public Void visitGroupingExpr(Expr.Grouping expr) {
        resolve(expr.expression);
        return null;
    }

    @Override
    public Void visitLiteralExpr(Expr.Literal expr) {
        return null;
    }

    @Override
    public Void visitLogicalExpr(Expr.Logical expr) {
        resolve(expr.left);
        resolve(expr.right);
        return null;
    }

    @Override
    public Void visitSetExpr(Expr.Set expr) {
        resolve(expr.value);
        resolve(expr.object);
        return null;
    }

    @Override
    public Void visitSuperExpr(Expr.Super expr) {
        if (currentClass == ClassType.NONE) {
            Lox.error(expr.keyword, "Can't use 'super' outside of a class.");
        } else if (currentClass != ClassType.SUBCLASS) {
            Lox.error(expr.keyword, "Can't use 'super' in a class with no superclass.");
        }

        resolveLocal(expr, expr.keyword, false);
        return null;
    }

    @Override
    public Void visitThisExpr(Expr.This expr) {
        if (currentClass == ClassType.NONE) {
            Lox.error(expr.keyword, "Can't use 'this' outside of a class.");
            return null;
        }

        resolveLocal(expr, expr.keyword, true);
        return null;
    }

    @Override
    public Void visitUnaryExpr(Expr.Unary expr) {
        resolve(expr.right);
        return null;
    }
}
