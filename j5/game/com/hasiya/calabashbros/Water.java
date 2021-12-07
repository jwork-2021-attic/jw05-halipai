package com.hasiya.calabashbros;
import java.awt.Color;
import asciiPanel.AsciiPanel;

public class Water extends Thing {

    public Water(World world) {
        super(new Color(0, 0, 255), (char) 240, world);
    }

}