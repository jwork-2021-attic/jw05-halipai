package com.hasiya.calabashbros;

import java.awt.Color;

public class Carpet extends Thing {

    public Carpet(World world) {
        super(new Color(137, 104, 205), (char) 36, world);
    }

    public Carpet(World world, int best, Color color){
        //super(new Color(205, 145, 158), (char) 7, world);
        super(color, (char) 173, world);
    }

    Carpet(World world, boolean gold) {
        super(new Color(255, 204, 153), (char) 3, world);
    }

}