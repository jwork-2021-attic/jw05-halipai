package com.hasiya.calabashbros;
import java.awt.Color;
import asciiPanel.AsciiPanel;

public class Portal extends Thing {

    public Portal(World world) {
        super(new Color(255, 193, 37), (char) 15, world);
    }

    public Portal(World world, char number){
        super(new Color(205, 145, 158), (char) number, world);
    }
}