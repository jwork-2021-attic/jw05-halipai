package com.hasiya.screen;

import java.util.ArrayList;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.Random;

import com.hasiya.calabashbros.Boat;
import com.hasiya.calabashbros.Cane;
import com.hasiya.calabashbros.Key;
import com.hasiya.calabashbros.Creature;
import com.hasiya.calabashbros.EnergyFood;
import com.hasiya.calabashbros.Goblin;
import com.hasiya.calabashbros.BigGoblin;
import com.hasiya.calabashbros.Calabash;
import com.hasiya.calabashbros.Marbles;
import com.hasiya.calabashbros.Wall;
import com.hasiya.calabashbros.Floor;
import com.hasiya.calabashbros.World;
import com.hasiya.maze_generator.MazeGenerator;

import asciiPanel.AsciiPanel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class WorldScreen implements Screen {

    public World world;
    private Calabash bro;
    String[] sortSteps;
    MazeGenerator mazeGenerator;
    private int score;
    private int steps, sumstep;
    private Color color;
    private boolean bigMan = false;
    private int direction;
    public ArrayList<Creature> creatures;
    public ExecutorService exec;
    public Lock lock = new ReentrantLock();
    public String action = "1", attackaction = "1";
    public static int over;

    public WorldScreen() {
        world = new World();
        mazeGenerator = new MazeGenerator(World.WIDTH, World.HEIGHT, world);
        //mazeGenerator.generateMaze();
        //mazeGenerator.beput(world);

        exec = Executors.newCachedThreadPool();
        creatures = mazeGenerator.CreateCreature(exec);
        for (int i = 0; i < creatures.size(); i++) {
            exec.execute(new creatureThread(creatures.get(i)));
        }
        over = 0;
    }

    public class marblesThread implements Runnable{
        public Marbles marbles;

        public marblesThread(Marbles marbles){
            this.marbles = marbles;
        }

        @Override
        public void run(){
            while (marbles.isExistent()) {
                try {
                    lock.lock();
                    marbles.launch();
                } finally {
                    lock.unlock();
                    try {
                        TimeUnit.MILLISECONDS.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            try {
                lock.lock();
                world.put(new Floor(world), marbles.getX(), marbles.getY());
            } finally {
                lock.unlock();
            }
        }

    }

    public class creatureThread implements Runnable{
        public Creature creature;

        public creatureThread(Creature creature){
            this.creature = creature;
        }

        @Override
        public void run(){
            if(this.creature instanceof Goblin){
                this.creature = (Goblin)this.creature;
                try{
                    while(this.creature.isExistent()){
                        try{
                            lock.lock();
                            boolean direc[] ={false, false, false, false};
                            int x = this.creature.getX(), y = this.creature.getY();
                            if(x-1 >= 0 && (world.tiles[x-1][y].getThing() instanceof Floor|| world.tiles[x-1][y].getThing() instanceof Calabash))
                                direc[0] = true;
                            if(y-1 >= 15 && (world.tiles[x][y-1].getThing() instanceof Floor|| world.tiles[x-1][y].getThing() instanceof Calabash))
                                direc[1] = true;
                            if(world.tiles[x+1][y].getThing() instanceof Floor|| world.tiles[x-1][y].getThing() instanceof Calabash)
                                direc[2] = true;
                            if(y+1 < world.HEIGHT && (world.tiles[x][y+1].getThing() instanceof Floor|| world.tiles[x-1][y].getThing() instanceof Calabash))
                                direc[3] = true;
                            if(direc[0] == false && direc[1] == false && direc[2] == false && direc[3] == false){
                                this.creature.getAttacked();
                            }
                            else{
                                while(true){
                                    Random ran = new Random();
                                    int i = ran.nextInt(4);
                                    if(direc[i]){
                                        if(i == 0)  this.creature.go(-1, 0);
                                        else if(i == 1)  this.creature.go(0, 1);
                                        else if(i == 2)  this.creature.go(1, 0);
                                        else if(i == 3)  this.creature.go(0, -1);
                                        break;
                                    }
                                }
                            }
                        }finally{
                            lock.unlock();
                            try {
                                Random ran = new Random();
                                TimeUnit.MILLISECONDS.sleep(ran.nextInt(500));
                            } catch (InterruptedException e) {
                                System.out.println("get exception!");
                                e.printStackTrace();
                            }
                        }
                    }
                }finally{
                    try {
                        lock.lock();
                        world.put(new Floor(world), creature.getX(), creature.getY());
                    } finally {
                        lock.unlock();
                    }
                }
            }
            else if(this.creature instanceof Calabash){
                while(this.creature.isExistent()){
                    try{
                        lock.lock();
                        if(this.creature.isSuccess()) break;
                        switch(action){
                            case "left":
                                this.creature.go(-1,0);
                                break;
                            case "up":
                                this.creature.go(0,1);
                                break;
                            case "right":
                                this.creature.go(1,0);
                                break;
                            case "down":
                                this.creature.go(0,-1);
                                break;

                        }
                        if(!attackaction.equals("1")){
                            Marbles m = new Marbles(world, this.creature.getColor());
                            m.setDirection(attackaction);
                            int x = creature.getX(), y = creature.getY();
                            switch(attackaction){
                                case "attackleft":
                                    if(world.tiles[x-1][y].getThing() instanceof Floor|| world.tiles[x-1][y].getThing() instanceof Goblin)
                                        world.put(m, x-1, y);
                                    break;
                                case "attackup":
                                    if(world.tiles[x][y-1].getThing() instanceof Floor|| world.tiles[x][y-1].getThing() instanceof Goblin)
                                        world.put(m, x, y-1);
                                    break;
                                case "attackright":
                                    if(world.tiles[x+1][y].getThing() instanceof Floor|| world.tiles[x+1][y].getThing() instanceof Goblin)
                                        world.put(m, x+1, y);
                                    break;
                                case "attackdown":
                                    if(world.tiles[x][y+1].getThing() instanceof Floor|| world.tiles[x][y+1].getThing() instanceof Goblin)
                                        world.put(m, x, y+1);
                                    break;
                                default: break;
                            }
                            exec.execute(new marblesThread(m));
                        }

                        action = "";
                        attackaction = "1";
                    }finally{
                        lock.unlock();
                        try {
                            TimeUnit.MILLISECONDS.sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                if(this.creature.isExistent()){
                    exec.shutdownNow();
                    over = 1;
                }
                else{
                    exec.shutdownNow();
                    over = -1;
                }
            }
            else if(this.creature instanceof BigGoblin){
                try{
                    while(this.creature.isExistent()){
                        try{
                            lock.lock();
                            boolean direc[] ={false, false, false, false};
                            int x = this.creature.getX(), y = this.creature.getY();
                            if(x-1 >= 0 && (world.tiles[x-1][y].getThing() instanceof Floor|| world.tiles[x-1][y].getThing() instanceof Calabash))
                                direc[0] = true;
                            if(y-1 >= 0 && (world.tiles[x][y-1].getThing() instanceof Floor|| world.tiles[x-1][y].getThing() instanceof Calabash))
                                direc[1] = true;
                            if(world.tiles[x+1][y].getThing() instanceof Floor|| world.tiles[x-1][y].getThing() instanceof Calabash)
                                direc[2] = true;
                            if(y+1 < world.HEIGHT && (world.tiles[x][y+1].getThing() instanceof Floor|| world.tiles[x-1][y].getThing() instanceof Calabash))
                                direc[3] = true;
                            if(direc[0] == false && direc[1] == false && direc[2] == false && direc[3] == false){
                                this.creature.getAttacked();
                            }
                            else{
                                while(true){
                                    Random ran = new Random();
                                    int i = ran.nextInt(4);
                                    if(direc[i]){
                                        if(i == 0)  this.creature.go(-1, 0);
                                        else if(i == 1)  this.creature.go(0, 1);
                                        else if(i == 2)  this.creature.go(1, 0);
                                        else if(i == 3)  this.creature.go(0, -1);
                                        break;
                                    }
                                }
                            }
                            Marbles m = new Marbles(world, this.creature.getColor());
                            m.setDirection(attackaction);
                            x = creature.getX();
                            y = creature.getY();
                            int nowx = creatures.get(0).getX(), nowy = creatures.get(0).getY();
                            if(nowy > y-5 && nowy < y+5 && nowx < x && nowx > x-20){
                                m.setDirection("attackleft");
                                if(world.tiles[x-1][y].getThing() instanceof Floor|| world.tiles[x-1][y].getThing() instanceof Goblin)
                                    world.put(m, x-1, y);
                                exec.execute(new marblesThread(m));
                            }
                            else if(nowy < y && nowx > x-10){
                                m.setDirection("attackup");
                                if(world.tiles[x][y-1].getThing() instanceof Floor|| world.tiles[x][y-1].getThing() instanceof Goblin)
                                    world.put(m, x, y-1);
                                exec.execute(new marblesThread(m));
                            }
                            else if(nowy > y-5 && nowy < y+5 && nowx > x){
                                m.setDirection("attackright");
                                if(world.tiles[x+1][y].getThing() instanceof Floor|| world.tiles[x+1][y].getThing() instanceof Goblin)
                                    world.put(m, x+1, y);
                                exec.execute(new marblesThread(m));
                            }
                            else if(nowy < y+5 && nowy > y && nowx > x-10){
                                m.setDirection("attackdown");
                                if(world.tiles[x][y+1].getThing() instanceof Floor|| world.tiles[x][y+1].getThing() instanceof Goblin)
                                    world.put(m, x, y+1);
                                exec.execute(new marblesThread(m));
                            }
                        }finally{
                            lock.unlock();
                            try {
                                TimeUnit.MILLISECONDS.sleep(800);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }finally{
                    try {
                        lock.lock();
                        world.put(new Floor(world), creature.getX(), creature.getY());
                    } finally {
                        lock.unlock();
                    }
                }
            }
        }
    }

    @Override
    public Screen respondToUserInput(KeyEvent key) {
        //System.out.println(key.getKeyCode());
        if(key.getKeyCode() == 0x25){//left
            action = "left";
            //direction = 27;
        }else if(key.getKeyCode() == 0x26){//up
            action = "up";
            //direction = 24;
        }else if(key.getKeyCode() == 0x27){//right
            action = "right";
            //direction = 26;
        }else if(key.getKeyCode() == 0x28){//down
            action = "down";
            //direction = 25;
        }else if(key.getKeyCode() == 0x53){//s
            attackaction = "attackleft";
        }else if(key.getKeyCode() == 0x45){//e
            attackaction = "attackup";
        }else if(key.getKeyCode() == 0x46){//f
            attackaction = "attackright";
        }else if(key.getKeyCode() == 0x44){//d
            attackaction = "attackdown";
        }
        return this;
    }


    // int wrongWay = 0;
    // private void execute(Calabash bro, int dx, int dy) {
    //     steps += 1;
    //     int s = bro.go(dx,dy);
    //     if(s == -2){
    //         score -= 1;
    //         wrongWay = 2;
    //     }else if(s == -1){
    //         score -= 1;
    //         wrongWay = 1;
    //     }
    //     else if(s == 1){
    //         score += 3;
    //         wrongWay = -1;
    //     }
    //     if(score > 10){
    //         bigMan = true;
    //     }
    // }

    @Override
    public int displayOutput(AsciiPanel terminal) {
        // if(bigMan){
        //     terminal.writeCenter("YOU ARE A DIAMOND CALABASH WITH WALL PIERCING MAGIC! ", 4, new Color(255, 204, 153));
        // }
        // terminal.write((char)direction, 5, 2, color);
        // terminal.writeCenter("SCORE : "+ score + "     STEPS : " + steps, 2, new Color(173, 127, 168));
        // if(wrongWay == 1){
        //     terminal.writeCenter("HINT: HIT THE WALL! HIT THE WALL!", 5, new Color(139, 58, 58));
        // }else if(wrongWay == 2){
        //     terminal.writeCenter("HINT: CROSS THE BORDER! CROSS THE BORDER!", 5, new Color(139, 58, 58));
        // }else if(wrongWay == -1){
        //     terminal.writeCenter("GOOD! YOU ATE AN ENERGY FOOD", 5, new Color(205, 102, 29));
        // }
        // for (int x = 0; x < World.WIDTH; x++) {
        //     for (int y = 0; y < World.HEIGHT; y++) {

        //         terminal.write(world.get(x, y).getGlyph(), x+1, y+7, world.get(x, y).getColor());

        //     }
        // }
        for (int x = 0; x < 21; x++) {
            for (int y = 0; y < 21; y++) {
                if(creatures.get(0).getX()+x-10 < 0 ||creatures.get(0).getX()+x-10 >= world.WIDTH){
                    Wall w = new Wall(world);
                    terminal.write(w.getGlyph(), x, y, w.getColor());
                    continue;
                }
                if(creatures.get(0).getY()+y-10 < 0 ||creatures.get(0).getY()+y-10 >= world.HEIGHT){
                    Wall w = new Wall(world);
                    terminal.write(w.getGlyph(), x, y, w.getColor());
                    continue;
                }
                terminal.write(world.get(creatures.get(0).getX()+x-10, creatures.get(0).getY()+y-10).getGlyph(), x, y, world.get(creatures.get(0).getX()+x-10, creatures.get(0).getY()+y-10).getColor());

            }
        }
        Calabash cal = (Calabash)creatures.get(0);
        int lifenum = cal.lifeValue;
        EnergyFood en = new EnergyFood(world);
        Color black = new Color(0, 0, 0);
        for(int a = 0; a < 21; a++)
            terminal.write((char)205, a, 21, black);
        terminal.write((char)12, 2, 22, new Color(218, 112, 214));
        terminal.write(":", 3, 22, cal.getColor());
        int a = 0;
        for(; a < lifenum; a++)
            terminal.write(en.getGlyph(), 4+a, 22, en.getColor());
        if(cal.ownBoat == true){
            Boat bo = new Boat(world);
            terminal.write(bo.getGlyph(), 4+a, 22, bo.getColor());
            a += 1;
        }
        if(cal.ownCane == true){
            Cane cane = new Cane(world);
            terminal.write(cane.getGlyph(), 4+a, 22, cane.getColor());
            a += 1;
        }
        if(cal.ownKey == true){
            Key k = new Key(world);
            terminal.write(k.getGlyph(), 4+a, 22, k.getColor());
            a += 1;
        }
        return over;
        // wrongWay = 0;
    }

    @Override
    public boolean over(){
        if(bro.isOver()){
            //score += 10;
            return true;
        }
        return false;
    }

    int i = 0;



    @Override
    public void displayOver(AsciiPanel terminal) {
        for (int x = 0; x < 21; x++) {
            for (int y = 0; y < 21; y++) {

                terminal.write((char) 255, x, y, Color.gray);

            }
        }

        terminal.writeCenter("CONGRATULATION!", 8, new Color(173, 127, 168));
        terminal.writeCenter("YOU WIN!", 15, new Color(173, 127, 168));

    }
    @Override
    public void displayBegin(AsciiPanel terminal, int k) {
        for (int x = 0; x < World.OUTWIDTH; x++) {
            for (int y = 0; y < World.OUTHEIGHT; y++) {

                terminal.write((char) 255, x, y, Color.gray);

            }
        }

        terminal.writeCenter("ROUND "+ k, 10, new Color(173, 127, 168));
        terminal.writeCenter("WELCOME TO MY WORLD", 15, new Color(173, 127, 168));
        terminal.writeCenter("I AM A MAZE GAME", 17, new Color(173, 127, 168));
        terminal.writeCenter("PRESS ENTER TO BEGIN ME NOW!", 19, new Color(173, 127, 168));
        terminal.writeCenter("----------------------", 24, new Color(173, 127, 168));
        terminal.writeCenter("|want to play easier?|", 25, new Color(173, 127, 168));
        terminal.writeCenter("| try to press <A> to use ASTAR  |", 27, new Color(173, 127, 168));
        terminal.writeCenter("----------------------", 28, new Color(173, 127, 168));
        terminal.write((char) 2, 17, 32, new Color(204, 0, 0));
        terminal.write((char) 2, 21, 32, new Color(255, 165, 0));
        terminal.write((char) 2, 25, 32, new Color(252, 233, 79));
        terminal.write((char) 2, 29, 32, new Color(78, 154, 6));
        terminal.write((char) 2, 33, 32, new Color(50, 175, 255));
        terminal.write((char) 2, 37, 32, new Color(114, 159, 207));
        terminal.write((char) 2, 41, 32, new Color(173, 127, 168));
    }

    @Override
    public boolean fail() {
        if(score < 0){
            return true;
        }
        return false;
    }

    @Override
    public void displayFail(AsciiPanel terminal) {
        for (int x = 0; x < 21; x++) {
            for (int y = 0; y < 21; y++) {

                terminal.write((char) 255, x, y, Color.gray);

            }
        }

        terminal.writeCenter("--GAME OVER--", 8, new Color(173, 127, 168));
        terminal.writeCenter("YOU LOST!", 15, new Color(173, 127, 168));
    }
}