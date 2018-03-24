package com.nextlevel.playarduino.arduinofullstack.Main.Schema;

/**
 * Created by sukumar on 12/10/17.
 */

public class ButtonViewModel implements Cloneable{

    int id;
    int buttonType;
    int positionX = 0;
    int positionY = 0;
    int width = 200;
    int height = 200;
    String description;
    String command;
    int color;
    /**
     * This field can describes whether its a user defined or pre-defined button.
     * PRE_DEFINED_BUTTON or USER_DEFINED_BUTTON
     */
    int catogory;

    public ButtonViewModel(int id,int buttonType, int positionX, int positionY,int width, int height, String description, String command, int catogory){
        this.id = id;
        this.buttonType = buttonType;
        this.positionX = positionX;
        this.positionY = positionY;
        this.width = width;
        this.height = height;
        this.description = description;
        this.command = command;
        this.catogory = catogory;
    }

    public void setColor(int color){
        this.color = color;
    }

    public int getColor(){
        return color;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
