/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.autogene.algorithms;

import java.util.Stack;

/**
 *
 * @author Emily
 */
public class Coordinates {
    
    
    private int start;
    private int end;
    
    public Coordinates(int tempStart, int tempEnd)
    {
        start = tempStart;
        end = tempEnd;
       
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }
    
    
    
    @Override
    public boolean equals(Object otherCoordinates)
    {
        
        
        
        
        Coordinates other = (Coordinates)otherCoordinates;
        
        if(Math.max(start, other.getStart()) - Math.min(start, other.getStart()) <= 3)
            return true;
        if(Math.max(end, other.getEnd()) - Math.min(end, other.getEnd()) <= 3)
            return true;
        
        if(other.getStart() == start || other.getEnd() == end)
            return true;
        if(other.getStart() > start && other.getStart() < end)
            return true;
        if(other.getEnd() > start && other.getEnd() < end)
            return true;
        if(other.getStart() < start && other.getEnd() > end)
            return true;
        if(start < other.getStart() && end > other.getEnd())
            return true;
        
       return false;
    }
    
    
    @Override
    public int hashCode()
    {
        String first = start+"";
        String second = end+"";
        
        return Integer.parseInt(first+second);
            
    }
    
}