package com.hasiya.calabashbros;

import java.awt.Color;

public class Calabash extends Creature{

    public boolean ownKey, ownBoat, ownCane,success;
    public boolean exist;
    public int lifeValue;
    public static boolean rightHouse,rightWater, rightCarpet;

    public Calabash(Color color, int rank, World world) {
        super(color, (char) 12, world);
        this.lifeValue = 1;
        this.exist = true;
        this.ownBoat = false;
        this.ownKey = false;
        this.ownCane = false;
        this.success = false;
        this.rightHouse = true;
        this.rightWater = false;
        this.rightCarpet = false;
    }

    @Override
    public void getAttacked(){
        this.lifeValue -= 1;
        if(this.lifeValue == 0) this.exist = false;
    }

    public void getMoreLife(){
        this.lifeValue += 1;
    }

    @Override
    public boolean isSuccess(){
        return this.success;
    }

    public void getBoat(){
        this.ownBoat = true;
    }

    public void getCane(){
        this.ownCane = true;
    }

    public void getKey(){
        this.ownKey = true;
    }

    @Override
    public boolean isExistent(){
        return this.exist;
    }

    @Override
    public void go(int dx, int dy){
        int x = this.getX();
        int y = this.getY();
        if(this.world.inTheWorld(x+dx, y-dy)){
            Thing p = this.world.get(x+dx, y-dy);
            if(p instanceof Flint)
                return;
            if(p instanceof Water){
                if(this.ownBoat){
                    this.moveTo(x+dx, y-dy);
                    if(rightWater) this.world.put(new Water(this.world), x, y);
                    else this.world.put(new Floor(this.world), x, y);
                    rightWater = true;
                }
                return;
            }
            if(p instanceof Portal){
                if(this.ownKey){
                    this.moveTo(x+dx, y-dy);
                    this.world.put(new Floor(this.world), x, y);
                    this.success = true;
                }
                return;
            }

            if(p instanceof Carpet){
                if(this.ownCane){
                    this.moveTo(x+dx, y-dy);
                    this.world.put(new Floor(this.world), x, y);
                    rightCarpet = true;
                }
                return;
            }
            if(p instanceof Goblin){
                this.getAttacked();
                return;
            }
            if(p instanceof Boat)   this.getBoat();
            if(p instanceof EnergyFood) this.getMoreLife();
            if(p instanceof Mine) this.getAttacked();
            if(p instanceof Key)    this.getKey();
            if(p instanceof Cane)   this.getCane();

            this.moveTo(x+dx, y-dy);

            if(rightHouse){
                this.world.put(new House(this.world), x, y);
                if(!(p instanceof House))   rightHouse = false;
                return;
            }
            if(rightWater){
                this.world.put(new Water(this.world), x, y);
                rightWater = false;
                return;
            }
            if(rightCarpet){
                this.world.put(new Carpet(this.world), x, y);
                if(!(p instanceof Carpet))  rightCarpet = false;
                return;
            }
            if(p instanceof House){
                rightHouse = true;
            }

            this.world.put(new Floor(this.world), x, y);
        }
        return;
    }
}
