package com.nextlevel.playarduino.arduinofullstack.Main.Schema;

import java.util.List;

/**
 * Created by sukumar on 9/8/17.
 */

public class Schema {

    String schemaName;

    /* takes values like Arduino UNO, Arduino Mini, Raspberry2 etc */

    List<ButtonViewModel> buttonViewModelList;

    /* Like Command or Game */
    String schemaType;

    public class ButtonViewModel{
        int id;
        String buttonType;
        int x = 0;
        int y = 0;
        int width = 0;
        int height = 0;
        String description;
        String command;

        public ButtonViewModel(int id,String buttonType, int x, int y,int width,int height, String description, String command){
            this.id = id;
            this.buttonType = buttonType;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.description = description;
            this.command = command;
        }
    }
}
