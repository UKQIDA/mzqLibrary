/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.cranfield.mzqlib.data;

import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author Jun Fan@cranfield
 */
public class MzqDataControl {
    
    private HashMap<Integer,MzqDataControlElement> proteinLevel = new HashMap<Integer, MzqDataControlElement>();
    private HashMap<Integer,MzqDataControlElement> peptideLevel = new HashMap<Integer, MzqDataControlElement>();
    private HashMap<Integer,MzqDataControlElement> featureLevel = new HashMap<Integer, MzqDataControlElement>();
    
    public void addElement(int level, int type, String element){
        getControlElement(level, type).addElement(element);
    }
    
    public boolean isRequired(int level, int type, String quantityName){
        return getControlElement(level, type).isRequired(quantityName);
    }

    public HashSet<String> getElements(int level, int type){
        return getControlElement(level,type).getElements();
    }
            
    private MzqDataControlElement getControlElement(int level, int type){
        HashMap<Integer,MzqDataControlElement> map = null;
        switch(level){
            case MzqData.PROTEIN:
                map = proteinLevel;
                break;
            case MzqData.PEPTIDE:
                map = peptideLevel;
                break;
            case MzqData.FEATURE:
                map = featureLevel;
                break;
        }
        if(map == null) {
            System.out.println("Unrecognized quantitation level, program exits in MzqDataControl.java");
            System.exit(0);
        }
        
        if(!map.containsKey(type)){
             MzqDataControlElement controlElement = new MzqDataControlElement();
             map.put(type, controlElement);
        }
        return map.get(type);
    }
}

class MzqDataControlElement{
    private HashSet<String> elements = new HashSet<String>();
    boolean isRequired(String quantityName){
        if (elements.isEmpty()) return false;
        if (elements.contains(quantityName)) return true;
        return false;
    }
    
    HashSet<String> getElements(){
        return elements;
    }
    
    void addElement(String element){
        elements.add(element);
    }
}
