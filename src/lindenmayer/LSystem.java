/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lindenmayer;

import java.awt.geom.Point2D;
import java.util.Iterator;
import java.awt.geom.Rectangle2D;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import lindenmayer.tree.BranchNode;
import lindenmayer.tree.Node;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author bowen
 */
public class LSystem {
    
    private final Map<Character, Symbol> symbolDict = new HashMap<>();
    private final Map<Symbol, List<String>> ruleDict = new HashMap<>();
    private final Map<Symbol, String> actionDict = new HashMap<>();
    
    private String axiom = "";
    
    /**
     * constructeur vide monte un système avec alphabet vide et sans règles
     */
    /* méthodes d'initialisation de système */
    public Symbol addSymbol(char sym) {
        if (symbolDict.containsKey(sym)) {
            return symbolDict.get(sym);
        } else {
            Symbol newSymbol = new Symbol(sym);
            symbolDict.put(sym, newSymbol);
            return newSymbol;
        }
    }
    public void addRule(Symbol sym, String expansion) {
        if (ruleDict.containsKey(sym)) {
            ruleDict.get(sym).add(expansion);
        } else {
            LinkedList<String> rules = new LinkedList<>();
            rules.add(expansion);
            ruleDict.put(sym, rules);
        }
    }
    public void setAction(Symbol sym, String action) {
        actionDict.put(sym, action);
    }
    public void setAxiom(String str){
        axiom = str;
    }
 
    /* initialisation par fichier */
    public static void readJSONFile(String filename, LSystem system, Turtle turtle) throws java.io.IOException {
        JSONObject input = new JSONObject(Utils.readStringFile(filename));
        JSONArray alphabet = input.getJSONArray("alphabet");
        JSONObject rules = input.getJSONObject("rules");
        JSONObject actions = input.getJSONObject("actions");
        for (Object o : alphabet) {
            if (o instanceof String) {
                if (!((String) o).isEmpty()) {
                    char character = ((String) o).charAt(0);
                    String strChar = "" + character;
                    Symbol s = system.addSymbol(character);
                    
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
                    
                    if (actions.has(strChar)) {
                        String action = actions.optString(strChar);
                        system.setAction(s, action);
                    }
                    
                }
            }
        }
        String axiom = input.getString("axiom");
        system.setAxiom(axiom);
        
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
 
    /* accès aux règles et exécution */
    public Iterator<Symbol> getAxiom(){
        LinkedList<Symbol> symbolSequence = new LinkedList<>();
        
        for (char c : axiom.toCharArray()) {
            if (symbolDict.containsKey(c)) {
                symbolSequence.add(symbolDict.get(c));
            }
        }
        
        return symbolSequence.iterator();
        
    }
    public Iterator<Symbol> rewrite(Symbol sym) {
        List<String> ruleList = ruleDict.get(sym);
        LinkedList<Symbol> newSymbolList = new LinkedList<>();
        
        String rule = "";
        
        if (ruleList != null && !ruleList.isEmpty()) {
            
            rule = ruleList.get((int)Math.floor(Math.random() * ruleList.size()));

            for (char c : rule.toCharArray()) {
                if (symbolDict.containsKey(c)) {
                    newSymbolList.add(symbolDict.get(c));
                } else {
                    Symbol newSym = new Symbol(c);
                    symbolDict.put(c, newSym);
                    newSymbolList.add(newSym);
                }
            }
        }
        
        return newSymbolList.iterator();
    }
    public void tell(Turtle turtle, Symbol sym, boolean b) {
        if (actionDict.containsKey(sym)) {
            switch (actionDict.get(sym)) {
                case "draw":
                    if (b) {
                        turtle.draw();
                    } else {
                        turtle.move();
                    }
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
                        if (b) {
                            turtle.draw();
                        } else {
                            turtle.move();
                        }
                    } else if (Character.isLowerCase(sym.getChar())) {
                        turtle.move();
                    }
                    break;
            }
        }
    }
 
    
    public void tell(Turtle turtle, int n) {
        Iterator<Symbol> axiomIterator = getAxiom();
        turtle.setWidth(n);
        while(axiomIterator.hasNext()) {
            tell(turtle, axiomIterator.next(), n);
        }
        
    }
    
    
    /* opérations avancées */
    public Iterator<Symbol> applyRules(Iterator<Symbol> seq, int n) {
        List<Symbol> symbolList = new LinkedList<>();
        
        while (seq.hasNext()) {
            symbolList.add(seq.next());
        }
        
        for (int i=0; i<n-1; i++) {
            List<Symbol> newSymbolList = new LinkedList<>();
            
            for (Symbol symbol : symbolList) {
                Iterator<Symbol> expansion = rewrite(symbol);
                
                if (expansion.hasNext()) {
                    while (expansion.hasNext()) {
                        newSymbolList.add(expansion.next());
                    }
                } else {
                    newSymbolList.add(symbol);
                }
            }
            symbolList = newSymbolList;
        }
        
        return symbolList.iterator();
    }
    public void tell(Turtle turtle, Symbol sym, int n){
        if (n > 1) {
            Iterator<Symbol> expansion = rewrite(sym);
            
            if (expansion.hasNext()) {
                while (expansion.hasNext()) {
                    tell(turtle, expansion.next(), n-1);
                }
            } else {
                tell(turtle, sym, true);
            }
            
        } else if (n == 1) {
            tell(turtle, sym, true);
        }
    }
    public Rectangle2D getBoundingBox(Turtle turtle, Iterator<Symbol> seq, int n) {
        Point2D lastPosition = turtle.getPosition();
        double lastAngle = turtle.getAngle();
        
        double minX = turtle.getPosition().getX();
        double maxX = turtle.getPosition().getX();
        
        double minY = turtle.getPosition().getY();
        double maxY = turtle.getPosition().getY();
        
        Iterator<Symbol> symbolIterator = applyRules(seq, n);
        
        while (symbolIterator.hasNext()) {
            tell(turtle, symbolIterator.next(), false);
            double thisX = turtle.getPosition().getX();
            double thisY = turtle.getPosition().getY();
            
            minX = Math.min(thisX, minX);
            minY = Math.min(thisY, minY);
            
            maxX = Math.max(thisX, maxX);
            maxY = Math.max(thisY, maxY);
            
        }
        
        turtle.init(lastPosition, lastAngle);
        
        return new Rectangle2D.Double(minX, minY, maxX - minX, maxY - minY);
    }
    
    public Node getTree(double turnAngle, double length, double startAngle, Iterator<Symbol> seq, int n) {
        Node rootNode = new BranchNode(0, 0);
        
        LinkedList<Node> nodeStack = new LinkedList<>();
        Node currentNode = rootNode;
        double currentAngle = startAngle;
        
        Iterator<Symbol> symbolIterator = applyRules(seq, n);
        while(symbolIterator.hasNext()) {
            
            Symbol sym = symbolIterator.next();
            
            switch (sym.getChar()) {
                case '[':
                    nodeStack.push(currentNode);
                    break;
                case ']':
                    currentNode = nodeStack.pop();
                    currentAngle = currentNode.getAngle();
                    break;
                case '+':
                    currentAngle += turnAngle;
                    break;
                case '-':
                    currentAngle -= turnAngle;
                    break;
                default:
                    Node newNode = new BranchNode(length, currentAngle);
                    currentNode.addChildrenNode(newNode);
                    currentNode = newNode;
                    break;
            }
            
            
        }
        
        return rootNode;
        
    }
    
}