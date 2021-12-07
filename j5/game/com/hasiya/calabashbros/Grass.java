package com.hasiya.calabashbros;
import java.awt.Color;
import asciiPanel.AsciiPanel;

public class Grass extends Flint {

    public Grass(World world) {
        super(new Color(143, 188, 143), (char) 208, world);
    }

    public Grass(World world, char number) {
        super(new Color(173, 255, 47), (char) number, world);
    }
}