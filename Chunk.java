package com.speedreader;

import java.util.ArrayList;
/**
 * 
 * A chunk can consist of 1-4 words
 */
public class Chunk
{
    private String text;
    private String[] words;
    private ArrayList<Integer> space_positions;
    private boolean inRange;
    /**
     * Creates a chunk of words 
     */
    public Chunk(String text)
    {
        this.text = text;
        words = new String[text.split(" ").length];
        words = text.split(" ");
        space_positions = new ArrayList<Integer>();
        inRange = false;
        
        String temp_text = text.trim();
        int add = 0;
        int space = 0;
        while(temp_text.indexOf(" ") > -1){
            space = temp_text.indexOf(" ");
            temp_text = temp_text.substring(space + 1);
            space_positions.add(space + add);
            add += space + 1;
        }
    }
    
    public String getText(){
        return text;
    }
    
    public String[] getWords(){
        return words;
    }
    
    public boolean isEmpty(){
        if(text.length() == 0)
            return true;
        else
            return false;
    }
    
    public int getNumWords(){
        return words.length;
    }
    
    public ArrayList<Integer> getSpacePositions(){
        return space_positions;
    }
    
    public int getLength(){
        return text.length();
    }
    
    public boolean inBounds(int lowerbound, int upperbound){
        if(lowerbound <= text.length() && text.length() <= upperbound){
            return true;
        }
        return false;
    }

    
}
