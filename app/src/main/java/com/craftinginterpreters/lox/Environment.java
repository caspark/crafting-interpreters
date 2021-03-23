package com.craftinginterpreters.lox;

import java.util.HashMap;
import java.util.Map;

class Environment {
    public static final Object UNINITIALIZED_VALUE = new Object();

    final Environment enclosing;
    private final Map<String, Object> values = new HashMap<>();

    Environment(Environment enclosing) {
        this.enclosing = enclosing;
    }

    void define(String name, Object value) {
        values.put(name, value);
    }

    Object get(Token name) {
        if (values.containsKey(name.lexeme)) {
            Object val = values.get(name.lexeme);
            if (val == UNINITIALIZED_VALUE) { // intentional reference equality
                throw new RuntimeError(name, "Uninitialized variable '" + name.lexeme + "'.");
            } else {
                return val;
            }
        }

        if (enclosing != null) return enclosing.get(name);

        throw new RuntimeError(name, "Undefined variable '" + name.lexeme + "'.");
    }

    void assign(Token name, Object value) {
        if (values.containsKey(name.lexeme)) {
            values.put(name.lexeme, value);
            return;
        }

        if (enclosing != null) {
            enclosing.assign(name, value);
            return;
        }

        throw new RuntimeError(name,
                "Undefined variable '" + name.lexeme + "'.");
    }
}