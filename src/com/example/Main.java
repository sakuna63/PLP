package com.example;

import com.example.Token.Operation;

import javax.naming.OperationNotSupportedException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, OperationNotSupportedException {
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

    private static int interpret(String line) throws OperationNotSupportedException {
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

    private static AST parse(String line) throws OperationNotSupportedException {
        StringTokenIterator iterator = new StringTokenIterator(line);
        AST preAST = null;
        while (!iterator.next().equals(Token.EOL)) {
            preAST = read(preAST, iterator);
        }
        return preAST;
    }

    private static AST read(AST preAST, StringTokenIterator iterator) throws OperationNotSupportedException {
        Token token = iterator.current();
        switch (token.operation) {
            case NUM:
            case PAR_L:
                preAST = readNum(iterator);
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
                throw new IllegalStateException(String.valueOf(token.operation));
        }
        return preAST;
    }

    private static AST readNum(StringTokenIterator iterator) throws OperationNotSupportedException {
        Token token = iterator.current();
        switch (token.operation) {
            case NUM:
                return new AST(token);
            case PAR_L:
                return readParenthesis(iterator);
            default:
                throw new OperationNotSupportedException();
        }
    }

    private static AST readParenthesis(StringTokenIterator iterator) throws OperationNotSupportedException {
        Token token;
        AST preAST = null;
        while (!(token = iterator.next()).equals(Token.EOL)) {
            switch (token.operation) {
                case PAR_R:
                    return preAST;
                default:
                    preAST = read(preAST, iterator);
            }
        }
        throw new IllegalArgumentException("parenthesis is not closed with ')'");
    }

    private static AST readTerm(AST preAST, StringTokenIterator iterator) throws OperationNotSupportedException {
        AST ast = new AST(iterator.current());
        if (preAST.operation == Operation.PLUS || preAST.operation == Operation.MINUS) {
            ast.left = preAST.right;
            preAST.right = ast;
            iterator.next();
            ast.right = readNum(iterator);
            return preAST;
        }
        else {
            ast.left = preAST;
            iterator.next();
            ast.right = readNum(iterator);
            return ast;
        }
    }

    private static AST readExpr(AST preAST, StringTokenIterator iterator) throws OperationNotSupportedException {
        AST ast = new AST(iterator.current());
        ast.left = preAST;
        iterator.next();
        ast.right = readNum(iterator);
        return ast;
    }

}
