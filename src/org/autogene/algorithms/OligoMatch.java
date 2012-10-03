/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.autogene.algorithms;

/**
 *
 * @author Emily
 */
public class OligoMatch {
    
    String id;
    String description;
    int start;
    int end;
    String direction;
    
    public OligoMatch(String tempId, String tempDescription, int tempStart, int tempEnd, String tempDirection)
    {
        id=tempId;
        description=tempDescription;
        start=tempStart;
        end=tempEnd;
        direction=tempDirection;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }
    
    
}
