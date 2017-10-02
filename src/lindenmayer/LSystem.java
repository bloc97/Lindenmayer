/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lindenmayer;

import java.awt.geom.Point2D;
import java.util.Iterator;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * IFT2015 Devoir 1, Bowen Peng et Lifeng Wan
 * @author bowen, lifeng
 */
public class LSystem {
    
    private final Map<Character, Symbol> symbolDict = new HashMap<>();
    private final Map<Symbol, List<String>> ruleDict = new HashMap<>();
    private final Map<Symbol, String> actionDict = new HashMap<>();
    
    private String axiom = "";
    
    /**
     * Adds a symbol into this L-System
     * @param sym Character
     * @return Symbol representing the character
     */
    public Symbol addSymbol(char sym) {
        //If dictionary contains character, returns symbol. Otherwise add to dictionary
        if (symbolDict.containsKey(sym)) {
            return symbolDict.get(sym);
        } else {
            Symbol newSymbol = new Symbol(sym);
            symbolDict.put(sym, newSymbol);
            return newSymbol;
        }
    }
    /**
     * Adds a rule into this L-System
     * @param sym Character
     * @param expansion Expansion rule of the character
     */
    public void addRule(Symbol sym, String expansion) {
        //If dictionary contains symbol, returns expansion string. Otherwise add to dictionary.
        if (ruleDict.containsKey(sym)) {
            ruleDict.get(sym).add(expansion);
        } else {
            LinkedList<String> rules = new LinkedList<>();
            rules.add(expansion);
            ruleDict.put(sym, rules);
        }
    }
    /**
     * Adds an action into this L-System
     * @param sym Character
     * @param action Action of that character
     */
    public void setAction(Symbol sym, String action) {
        actionDict.put(sym, action);
    }

    /**
     * Sets the starting axiom of this L-System
     * @param str Starting string
     */
    public void setAxiom(String str){
        axiom = str;
    }
 
    /**
     * Applies the symbols, rules, actions and axiom of JSON file to a L-System and Turtle instance
     * @param filename Path to json file
     * @param system L-System instance
     * @param turtle Turtle instance
     * @throws java.io.IOException
     */
    public static void readJSONFile(String filename, LSystem system, Turtle turtle) throws java.io.IOException {
        JSONObject input = new JSONObject(Utils.readStringFile(filename));
        JSONArray alphabet = input.getJSONArray("alphabet");
        JSONObject rules = input.getJSONObject("rules");
        JSONObject actions = input.getJSONObject("actions");
        //For all the characters in the JSON
        for (Object o : alphabet) {
            if (o instanceof String) {
                if (!((String) o).isEmpty()) {
                    char character = ((String) o).charAt(0);
                    String strChar = "" + character;
                    //Add the character as symbol
                    Symbol s = system.addSymbol(character);
                    
                    //Add the character's rule if exists
                    JSONArray ruleArray = rules.optJSONArray(strChar);
                    if (ruleArray != null) {
                        for (Object or : ruleArray) {
                            if (or instanceof String) {
                                system.addRule(s, (String) or);
                            }
                        }
                    } else if (rules.has(strChar)) {
                        String rule = rules.optString(strChar);
                        system.addRule(s, rule);
                    }
                    
                    //Add the character's action if exists
                    if (actions.has(strChar)) {
                        String action = actions.optString(strChar);
                        system.setAction(s, action);
                    }
                    
                }
            }
        }
        //Set the initial axiom
        String axiom = input.getString("axiom");
        system.setAxiom(axiom);
        
        //Set all the parameters
        JSONObject params = input.getJSONObject("parameters");
        
        int steps = params.optInt("step", 1);
        double angle = params.optDouble("angle", 90);
        turtle.setUnits(steps, angle);
        if (params.has("start")) {
            JSONArray startArray = params.getJSONArray("start");
            if (startArray.length() == 3) {
                turtle.init(new Point2D.Double(startArray.getDouble(0), startArray.getDouble(1)), startArray.getDouble(2));
            } else if (startArray.length() == 1) {
                turtle.init(new Point2D.Double(), startArray.getDouble(0));
            }
        }
    }
 
    /**
     * @return Axiom of the current L-System
     */
    public Iterator<Symbol> getAxiom(){
        LinkedList<Symbol> symbolSequence = new LinkedList<>();
        
        for (char c : axiom.toCharArray()) {
            if (symbolDict.containsKey(c)) {
                symbolSequence.add(symbolDict.get(c));
            }
        }
        
        return symbolSequence.iterator();
        
    }
    /**
     * @return Expansion of the symbol using rules of the current L-System. If there are multiple rules, returns a random expansion using a uniform distribution
     */
    public Iterator<Symbol> rewrite(Symbol sym) {
        List<String> ruleList = ruleDict.get(sym);
        LinkedList<Symbol> newSymbolList = new LinkedList<>();
        
        if (ruleList != null && !ruleList.isEmpty()) {
             //Fetch a random rule expansion
            String rule = ruleList.get((int)Math.floor(Math.random() * ruleList.size()));

            for (char c : rule.toCharArray()) {
                newSymbolList.add(addSymbol(c));
            }
        }
        return newSymbolList.iterator();
    }
    /**
     * Sends the turtle actions based on the symbol
     * @param turtle Turtle instance
     * @param sym Symbol
     */
    public void tell(Turtle turtle, Symbol sym) {
        if (actionDict.containsKey(sym)) {
            switch (actionDict.get(sym)) {
                case "draw":
                    turtle.draw();
                    break;
                case "move":
                    turtle.move();
                    break;
                case "turnR":
                    turtle.turnR();
                    break;
                case "turnL":
                    turtle.turnL();
                    break;
                case "push":
                    turtle.push();
                    break;
                case "pop":
                    turtle.pop();
                    break;
                case "stay":
                    turtle.stay();
                    break;
                default:
                    turtle.stay();
                    break;
            }
        } else {
            switch (sym.getChar()) {
                case '[':
                    turtle.push();
                    break;
                case ']':
                    turtle.pop();
                    break;
                case '+':
                    turtle.turnL();
                    break;
                case '-':
                    turtle.turnR();
                    break;
                default:
                    if (Character.isUpperCase(sym.getChar())) {
                        turtle.draw();
                    } else if (Character.isLowerCase(sym.getChar())) {
                        turtle.move();
                    }
                    break;
            }
        }
    }
 
    
    /**
     * Starts the drawing using a turtle
     * @param turtle Turtle instance
     * @param n Depth of expansion
     */
    public void tell(Turtle turtle, int n) {
        Iterator<Symbol> axiomIterator = getAxiom();
        
        while(axiomIterator.hasNext()) {
            tell(turtle, axiomIterator.next(), n);
        }
        
    }
    
    /**
     * Expands a sequence using the current L-System rules
     * @param seq Input sequence
     * @param n Depth of expansion
     * @return Expansion of sequence of depth n
     */
    public Iterator<Symbol> applyRules(Iterator<Symbol> seq, int n) {
        List<Symbol> symbolList = new LinkedList<>();
        
        //Copes the sequence into the new symbolList
        while (seq.hasNext()) {
            symbolList.add(seq.next());
        }
        
        //For all the symbols in the symbolList
        for (int i=0; i<n; i++) {
            //Create a new symbolList again
            List<Symbol> newSymbolList = new LinkedList<>();
            
            //For all the symbols in symbolList, expand them and add to newSymbolList
            for (Symbol symbol : symbolList) {
                Iterator<Symbol> expansion = rewrite(symbol);
                
                //If there's an expansion, add the expansion. Otherwise add the symbol itself.
                if (expansion.hasNext()) {
                    while (expansion.hasNext()) {
                        newSymbolList.add(expansion.next());
                    }
                } else {
                    newSymbolList.add(symbol);
                }
            }
            //Set symbolList as newSymbolList to continue the iteration
            symbolList = newSymbolList;
            
        }
        
        return symbolList.iterator();
    }
    
    /**
     * Sends the turtle actions recursively based on rules and actions
     * @param turtle Turtle instance
     * @param sym Symbol
     * @param n Depth of action expansions
     */
    public void tell(Turtle turtle, Symbol sym, int n){
        //Base case
        if (n == 0) {
            tell(turtle, sym);
        //If n > 0, recursively call tell() with the expansion
        } else if (n > 0) {
            Iterator<Symbol> expansion = rewrite(sym);
            
            if (expansion.hasNext()) {
                while (expansion.hasNext()) {
                    tell(turtle, expansion.next(), n-1);
                }
            } else {
                tell(turtle, sym);
            }
        }
    }

    /**
     * Gets the bounding box of the final drawing using a turtle and the current L-System
     * @param turtle Turtle instance
     * @param seq Sequence of symbols (Axiom)
     * @param n Depth of expansions
     * @return The bounding box of the drawing
     */
    public Rectangle2D getBoundingBox(Turtle turtle, Iterator<Symbol>seq, int n) {
        turtle = turtle.getEmptyClone();
        
        double minX = turtle.getPosition().getX();
        double maxX = turtle.getPosition().getX();
        
        double minY = turtle.getPosition().getY();
        double maxY = turtle.getPosition().getY();
        
        //Get the final symbol sequence
        Iterator<Symbol> symbolIterator = applyRules(seq, n);
        
        //Get the max and min of x and y
        while (symbolIterator.hasNext()) {
            tell(turtle, symbolIterator.next());
            double thisX = turtle.getPosition().getX();
            double thisY = turtle.getPosition().getY();
            
            minX = Math.min(thisX, minX);
            minY = Math.min(thisY, minY);
            
            maxX = Math.max(thisX, maxX);
            maxY = Math.max(thisY, maxY);
            
        }
        
        return new Rectangle2D.Double(minX, minY, maxX - minX, maxY - minY);
    }
    
}