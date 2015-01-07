package com.example;

import com.example.Token.Operation;

public class AST {
    public final Operation operation;
    public int value;
    public AST left = null;
    public AST right = null;

    public AST(Token token) {
        this.operation = token.operation;
        this.value = token.value;
    }
}
