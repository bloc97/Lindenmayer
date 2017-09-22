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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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
        
    }
    public void tell(Turtle turtle, Symbol sym) {
        
    }
 
    /* opérations avancées */
    public Iterator<Symbol> applyRules(Iterator<Symbol> seq, int n) {
        
    }
    public void tell(Turtle turtle, Symbol sym, int rounds){
        
    }
    public Rectangle2D getBoundingBox(Turtle turtle, Iterator<Symbol>seq, int n) {
        
    }
    
}