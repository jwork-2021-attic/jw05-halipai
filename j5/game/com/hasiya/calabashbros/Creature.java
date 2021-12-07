package com.hasiya.calabashbros;

import java.awt.Color;

public class Creature extends Thing {
    public int x, y;

    Creature(Color color, char glyph, World world) {
        super(color, glyph, world);
    }

    public void go(int x, int y){
        return;
    }

    public boolean isVertical(){
        return true;
    }

    public boolean isSuccess(){
        return false;
    }

    public void getAttacked(){

    }
}
