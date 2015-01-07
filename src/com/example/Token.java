package com.example;

public class Token {
    public Operation operation;
    public int value;
    public static final Token EOL = new Token(Integer.MIN_VALUE);

    public Token(Operation operation) {
        this.operation = operation;
        this.value = Integer.MIN_VALUE;
    }

    public Token(int value) {
        this.operation = Operation.NUM;
        this.value = value;
    }

    public enum Operation {
        NUM, PLUS, MINUS, MULTIPLY, DIVIDE, PAR_L, PAR_R // () = parenthesis
    }
}
