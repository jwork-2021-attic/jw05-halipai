package com.hasiya.calabashbros;

import java.awt.Color;

public class Marbles extends Thing {

    public boolean exist;
    public String direction;

    public Marbles(World world) {
        super(new Color(222, 100, 100), (char) 7, world);
        this.exist = true;
        this.direction = "";
    }
    public Marbles(World world, Color color) {
        super(color, (char) 7, world);
        this.exist = true;
        this.direction = "";
    }
    public void setDirection(String direction){
        this.direction = direction;
    }

    public void launch(){
        switch(this.direction){
            case "attackleft":
                go(-1,0);
                break;
            case "attackup":
                go(0,1);
                break;
            case "attackright":
                go(1,0);
                break;
            case "attackdown":
                go(0,-1);
                break;
        }
    }

    public void go(int dx, int dy){
        int x = this.getX();
        int y = this.getY();
        if(this.world.inTheWorld(x+dx, y-dy)){
            Thing p = this.world.get(x+dx, y-dy);
            if(p instanceof Floor){
                this.moveTo(x+dx, y-dy);
                this.world.put(new Floor(this.world), x, y);
                return;
            }
            else if(p instanceof Goblin){
                Goblin g = (Goblin) p;
                this.world.put(new Floor(this.world), x, y);
                this.world.put(new Floor(this.world), x+dx, y-dy);
                g.getAttacked();
            }
            else if(p instanceof BigGoblin){
                BigGoblin g = (BigGoblin) p;
                this.world.put(new Floor(this.world), x, y);
                g.getAttacked();
            }
            //if(p.getGlyph() == 177 || p.getGlyph() == 19 || p.getGlyph() == 2)
        }
        this.exist = false;
    }

    @Override
    public boolean isExistent(){
        return this.exist;
    }
}