package com.example;

import com.example.Token.Operation;

public class StringTokenIterator {
    private String text;
    private int begin;
    private int end;
    // invariant: begin <= pos <= end
    private int pos;
    private Token current;

    /**
     * Constructs an iterator with an initial index of 0.
     *
     * @param text the {@code String} to be iterated over
     */
    public StringTokenIterator(String text)
    {
        this(text, 0);
    }

    /**
     * Constructs an iterator with the specified initial index.
     *
     * @param  text   The String to be iterated over
     * @param  pos    Initial iterator position
     */
    public StringTokenIterator(String text, int pos)
    {
    this(text, 0, text.length(), pos);
    }

    /**
     * Constructs an iterator over the given range of the given string, with the
     * index set at the specified position.
     *
     * @param  text   The String to be iterated over
     * @param  begin  Index of the first character
     * @param  end    Index of the character following the last character
     * @param  pos    Initial iterator position
     */
    public StringTokenIterator(String text, int begin, int end, int pos) {
        if (text == null)
            throw new NullPointerException();
        this.text = text;

        if (begin < 0 || begin > end || end > text.length())
            throw new IllegalArgumentException("Invalid substring range");

        if (pos < begin || pos > end)
            throw new IllegalArgumentException("Invalid position");

        this.begin = begin;
        this.end = end;
        this.pos = pos;
    }

    public Token current() {
        return current;
    }

    public Token next() {
        if (pos >= end) {
            return Token.EOL;
        }

        while (isSpace(text.charAt(pos)) && ++pos < end);

        if (pos >= end) {
            current = Token.EOL;
        }
        else if (isDigit(text.charAt(pos))) {
            current = readNum();
        }
        else {
            switch (text.charAt(pos)) {
                case '+':
                    current = new Token(Operation.PLUS);
                    break;
                case '-':
                    current = new Token(Operation.MINUS);
                    break;
                case '*':
                    current = new Token(Operation.MULTIPLY);
                    break;
                case '/':
                    current = new Token(Operation.DIVIDE);
                    break;
                case '(':
                    current = new Token(Operation.PAR_L);
                    break;
                case ')':
                    current = new Token(Operation.PAR_R);
                    break;
                default:
                    throw new IllegalStateException(String.valueOf(text.charAt(pos)));
            }
        }

        pos++;
        return current;
    }

    private Token readNum() {
        int value = 0;
        char c;
        while (pos < end && isDigit(c = text.charAt(pos))) {
            value = value * 10 + c - '0';
            pos++;
        }
        if (pos < end) {
            pos--;
        }
        return new Token(value);
    }

    private static boolean isDigit(char c) {
        return '0' <= c && c < '9';
    }

    private static boolean isSpace(char c) {
        return c == ' ';
    }
}
