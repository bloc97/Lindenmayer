/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lindenmayer;

/**
 * IFT2015 Devoir 1, Bowen Peng et Lifeng Wan
 * @author bowen, lifeng
 */
public class Symbol {
    private final char c;
    public Symbol(char c) {
        this.c = c;
    }
    
    public char getChar() {
        return c;
    }

    @Override
    public String toString() {
        return "" + c;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Symbol) {
            return ((Symbol) obj).c == c;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return c;
    }

    
}
