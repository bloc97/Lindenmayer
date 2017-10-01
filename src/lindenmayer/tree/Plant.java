/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lindenmayer.tree;

/**
 *
 * @author bowen
 */
public class Plant {
    private Node rootNode;
    
    //Temperature curve (best in which temperatures)
    //Rainfall curve
    //Air humidity curve
    //Air pressure curve
    //Soil acidity curve
    //Sunlight curve
    //Minerals curve
    //Nitrogen curve
    //Oxygen curve
    //CO2 curve
    
    //Plants have three sets of curves, Blooming, Living, Hibernating
    
    //Plants have triggers for blooming (flowers) and hibernation
    
    //Special plants have special abilities
    //Underground neural network using roots
    //Take 'magic' energy from some other dimension. Able to do special stuff like freezing stuff, change climate, protect etc.
    
    //Genes have recessive and dominant features
    
    //Plants are affected by gravity, too much gravity can bend branches and break them if too much
    
    public void addRootNode(Node rootNode) {
        this.rootNode = rootNode;
        rootNode.setPlant(this);
    }
    
    public double getTotalMass() {
        return rootNode.getTotalMass();
    }
}
