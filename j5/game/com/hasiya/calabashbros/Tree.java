package com.hasiya.calabashbros;
import java.awt.Color;
import asciiPanel.AsciiPanel;

public class Tree extends Flint {

    public Tree(World world) {
        super(new Color(0, 238, 0), (char) 30, world);
    }

    public Tree(World world, char number) {
        super(new Color(0, 238, 0), (char) number, world);
    }
}