package com.example;

import com.example.Token.Operation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
//        if (args.length < 1) {
//            throw new IllegalArgumentException("Usage: java -jar plp.jar formula-file");
//        }
//
//        String fileName = args[0];
        String fileName = "formula";
        File file = new File(fileName);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(interpret(line));
        }
    }

    private static int interpret(String line) {
        AST ast = parse(line);
        return eval(ast);
    }

    private static int eval(AST ast) {
        if (ast.operation == Operation.NUM) {
            return ast.value;
        }
        else if (ast.operation == Operation.PLUS) {
            return eval(ast.left) + eval(ast.right);
        }
        else if (ast.operation == Operation.MINUS) {
            return eval(ast.left) - eval(ast.right);
        }
        else if (ast.operation == Operation.MULTIPLY) {
            return eval(ast.left) * eval(ast.right);
        }
        else if (ast.operation == Operation.DIVIDE) {
            return eval(ast.left) / eval(ast.right);
        }
        else {
            throw new IllegalArgumentException("");
        }
    }

    private static AST parse(String line) {
        StringTokenIterator iterator = new StringTokenIterator(line);
        Token token;
        AST preAST = null;
        while (!(token = iterator.next()).equals(Token.EOL)) {
            switch (token.operation) {
                case NUM:
                    preAST = new AST(token);
                    break;
                case PLUS:
                    preAST = readExpr(preAST, iterator);
                    break;
                case MINUS:
                    preAST = readExpr(preAST, iterator);
                    break;
                case MULTIPLY:
                    preAST = readTerm(preAST, iterator);
                    break;
                case DIVIDE:
                    preAST = readTerm(preAST, iterator);
                    break;
                default:
                    throw new IllegalStateException("");
            }
        }
        return preAST;
    }

    private static AST readTerm(AST preAST, StringTokenIterator iterator) {
        AST ast = new AST(iterator.current());
        if (preAST.operation == Operation.PLUS || preAST.operation == Operation.MINUS) {
            ast.left = preAST.right;
            preAST.right = ast;
            ast.right = new AST(iterator.next());
            return preAST;
        }
        else {
            ast.left = preAST;
            ast.right = new AST(iterator.next());
            return ast;
        }
    }

    private static AST readExpr(AST preAST, StringTokenIterator iterator) {
        AST ast = new AST(iterator.current());
        ast.left = preAST;
        ast.right = new AST(iterator.next());
        return ast;
    }

}
