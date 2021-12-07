package com.hasiya.maze_generator;

import java.util.ArrayList;
import java.util.Stack;
import java.util.Random;
import java.util.Arrays;
import java.awt.Color;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

import com.hasiya.calabashbros.Wall;
import com.hasiya.calabashbros.Carpet;

import com.hasiya.calabashbros.World;
import com.hasiya.calabashbros.Key;
import com.hasiya.calabashbros.Creature;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.hasiya.calabashbros.Calabash;
import com.hasiya.calabashbros.Goblin;
import com.hasiya.calabashbros.BigGoblin;


public class MazeGenerator {

    private Stack<Node> stack = new Stack<>();
    private Random rand = new Random();
    private int[][] maze;
    //private int dimension;
    private int row, column;
    public World world;

    PriorityQueue tableOne;
    int visited[][];
    int finalx, finaly;
    int path[][] = {{-1,0},{1,0},{0,1},{0,-1}};
    ArrayList<State> bbest;

    public MazeGenerator(int row, int column, World world) {
        maze = new int[row][column];
        for(int i = 0; i < row; i++){
            for(int j = 0; j < column; j++){
                maze[i][j] = 1;
            }
        }
        this.row = row;
        this.column = column;
        this.world = world;
    }

    public void generateMaze() {
        // int left = this.row/2-10;
        // int right = this.row/2+10;
        // int up = this.column/2-7;
        // int down = this.column/2+7;
        // for(int i = left+2; i < right-1; i++){
        //     if(i == this.row/2)
        //         continue;
        //     maze[i][up] = 0;
        //     maze[i][down] = 0;
        // }
        // for(int i = up+1; i < down; i++){
        //     if(i == this.column/2)
        //         continue;
        //     maze[left][i] = 0;
        //     maze[right][i] = 0;
        // }

        // for (int i = 0; i < row; i++) {
        //     for (int j = 0; j < column; j++) {
        //         if(maze[i][j] == 0){
        //             Wall wall = new Wall(world);
        //             world.put(wall, i ,j);
        //         }
        //     }
        // }

        // Key key = new Key(world);
        // world.put(key, this.row/2 ,this.column/2);


    }
    public ArrayList<Creature> CreateCreature(ExecutorService exec){
        ArrayList<Creature> creatures = new ArrayList<>();
        Calabash crea = new Calabash(new Color(218, 112, 214), 4, this.world);
        world.put(crea, 1, 11);
        creatures.add(crea);

        BigGoblin bigGoblin1 = new BigGoblin(this.world);
        world.put(bigGoblin1, this.row-12, 3);
        creatures.add(bigGoblin1);
        BigGoblin bigGoblin2 = new BigGoblin(this.world);
        world.put(bigGoblin2, this.row-12, 13);
        creatures.add(bigGoblin2);
        BigGoblin bigGoblin3 = new BigGoblin(this.world);
        world.put(bigGoblin3, this.row-12, 23);
        creatures.add(bigGoblin3);

        Goblin goblin11 = new Goblin(this.world);
        world.put(goblin11, 23, 28);
        creatures.add(goblin11);
        Goblin goblin12 = new Goblin(this.world);
        world.put(goblin12, 7, 25);
        creatures.add(goblin12);
        Goblin goblin13 = new Goblin(this.world);
        world.put(goblin13, 22, 36);
        creatures.add(goblin13);
        Goblin goblin14 = new Goblin(this.world);
        world.put(goblin14, 16, 40);
        creatures.add(goblin14);

        Goblin goblin21 = new Goblin(this.world, new Color(47, 79, 79));
        world.put(goblin21, this.row/2-3, this.column/2);
        creatures.add(goblin21);
        Goblin goblin22 = new Goblin(this.world, new Color(47, 79, 79));
        world.put(goblin22, this.row/2, this.column/2-3);
        creatures.add(goblin22);
        Goblin goblin23 = new Goblin(this.world, new Color(47, 79, 79));
        world.put(goblin23, this.row/2+3, this.column/2);
        creatures.add(goblin23);
        Goblin goblin24 = new Goblin(this.world, new Color(47, 79, 79));
        world.put(goblin24, this.row/2, this.column/2+3);
        creatures.add(goblin24);

        Goblin goblin31 = new Goblin(this.world, new Color(47, 79, 79));
        world.put(goblin31, this.row-20, this.column-3);
        creatures.add(goblin31);
        Goblin goblin32 = new Goblin(this.world, new Color(47, 79, 79));
        world.put(goblin32, this.row-12, this.column-16);
        creatures.add(goblin32);
        Goblin goblin33 = new Goblin(this.world, new Color(47, 79, 79));
        world.put(goblin33, this.row-30, this.column-3);
        creatures.add(goblin33);
        Goblin goblin34 = new Goblin(this.world, new Color(47, 79, 79));
        world.put(goblin34, this.row-32, this.column-16);
        creatures.add(goblin34);

        return creatures;
    }



    public void beput(World world){
        Key key = new Key(world);
        world.put(key, this.row/2 ,this.column/2);
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                if(maze[i][j] == 0){
                    Wall wall = new Wall(world);
                    world.put(wall, i ,j);
                }
            }
        }
    }

    // public void addRandomFood(World world, int n){
    //     int num = 0;
    //     while(num < n){
    //         int i = rand.nextInt(row);
    //         int j = rand.nextInt(column);
    //         if(maze[i][j] == 1){
    //             world.put(new EnergyFood(world), i ,j);
    //             num++;
    //         }
    //     }
    // }
                    // if(this.creature.isVertical()){
                    //     int ymax = Math.min(this.creature.getY()+5, world.HEIGHT-1);
                    //     int ymin = Math.max(this.creature.getY()-5, 0);
                    //     boolean down = true;
                    //     while(this.creature.isExistent()){
                    //         try{
                    //             lock.lock();
                    //             if(down && this.creature.getY() < ymax){
                    //                 this.creature.go(0,-1);
                    //             }
                    //             else if(down){
                    //                 down = false;
                    //             }
                    //             else if(!down && this.creature.getY() > ymin){
                    //                 this.creature.go(0,1);
                    //             }
                    //             else if(!down){
                    //                 down = true;
                    //             }
                    //         }finally{
                    //             lock.unlock();
                    //             try {
                    //                 Random ran = new Random();
                    //                 TimeUnit.MILLISECONDS.sleep(ran.nextInt(500));
                    //             } catch (InterruptedException e) {
                    //                 System.out.println("get exception!");
                    //                 e.printStackTrace();
                    //             }
                    //         }
                    //     }
                    // }
                    // else{
                    //     int xmax = Math.min(this.creature.getX()+5, world.HEIGHT-1);
                    //     int xmin = Math.max(this.creature.getX()-5, 0);
                    //     boolean right = true;
                    //     while(this.creature.isExistent()){
                    //         try{
                    //             lock.lock();
                    //             if(right && this.creature.getX() < xmax){
                    //                 this.creature.go(1,0);
                    //             }
                    //             else if(right){
                    //                 right = false;
                    //             }
                    //             else if(!right && this.creature.getX() > xmin){
                    //                 this.creature.go(-1,0);
                    //             }
                    //             else if(!right){
                    //                 right = true;
                    //             }
                    //         }finally{
                    //             lock.unlock();
                    //             try {
                    //                 Random ran = new Random();
                    //                 TimeUnit.MILLISECONDS.sleep(ran.nextInt(500));
                    //             } catch (InterruptedException e) {
                    //                 System.out.println("get exception!");
                    //                 e.printStackTrace();
                    //             }
                    //         }
                    //     }
                    // }
    public String getRawMaze() {
        StringBuilder sb = new StringBuilder();
        for (int[] r : maze) {
            sb.append(Arrays.toString(r) + "\n");
        }
        return sb.toString();
    }

    public String getSymbolicMaze() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                sb.append(maze[i][j] == 1 ? "*" : " ");
                sb.append("  ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    private boolean validNextNode(Node node) {
        int numNeighboringOnes = 0;
        for (int y = node.y-1; y < node.y+2; y++) {
            for (int x = node.x-1; x < node.x+2; x++) {
                if (pointOnGrid(x, y) && pointNotNode(node, x, y) && maze[row-x-1][column-y-1] == 1) {
                    numNeighboringOnes++;
                }
            }
        }
        return (numNeighboringOnes < 3) && maze[row-node.x-1][column-node.y-1] != 1;
    }

    private void randomlyAddNodesToStack(ArrayList<Node> nodes) {
        int targetIndex;
        while (!nodes.isEmpty()) {
            targetIndex = rand.nextInt(nodes.size());
            stack.push(nodes.remove(targetIndex));
        }
    }

    private ArrayList<Node> findNeighbors(Node node) {
        ArrayList<Node> neighbors = new ArrayList<>();
        for (int y = node.y-1; y < node.y+2; y++) {
            for (int x = node.x-1; x < node.x+2; x++) {
                if (pointOnGrid(x, y) && pointNotCorner(node, x, y)
                    && pointNotNode(node, x, y)) {
                    neighbors.add(new Node(x, y));
                }
            }
        }
        return neighbors;
    }

    private Boolean pointOnGrid(int x, int y) {
        return x >= 0 && y >= 0 && x < row && y < column;
    }

    private Boolean pointNotCorner(Node node, int x, int y) {
        return (x == node.x || y == node.y);
    }

    private Boolean pointNotNode(Node node, int x, int y) {
        return !(x == node.x && y == node.y);
    }

    public class State{
        public int x;
        public int y;
        public int f;
        public int g;
        public int h;
        public ArrayList<State> best;
    };

    public class StateComparator implements Comparator{
        public int compare(Object arg0, Object arg1) {
            State s1 = (State)arg0;
            State s2 = (State)arg1;
            return s1.f - s2.f;
        }
    }

    int calc(State state){
        return (int)Math.sqrt(Math.pow(state.x - finalx, 2) + Math.pow(state.y - finaly,2));
    }

    @SuppressWarnings("unchecked")

    public int aStar(World world, boolean display, Color color){
        visited = new int [row][column];
        tableOne = new PriorityQueue<State>(100000, new StateComparator());
        finalx = row - 1;
        finaly = column - 1;
        for(int i = 0;i < visited.length;i++){
            for(int j = 0;j < visited[0].length;j++){
                visited[i][j] = Integer.MAX_VALUE;
            }
        }
        State newState = new State();
        ArrayList<State> best = new ArrayList<>();
        newState.best = best;
        newState.best.add(newState);
        newState.x = 0;
        newState.y = 0;
        newState.g = 0;
        newState.h = calc(newState);
        newState.f = newState.g + newState.h;
        tableOne.add(newState);
        while(! tableOne.isEmpty()){
            State tmp = (State)tableOne.poll();
            if(tmp.x == finalx && tmp.y == finaly){
                bbest = tmp.best;
                break;
            }
            if(firstVisited(tmp)){
                visited[tmp.x][tmp.y] = tmp.f;
                ArrayList<State> successors = getSuccessors(tmp);
                for(State state : successors){
                    if(firstVisited(state)){
                        tableOne.add(state);
                    }
                }
            }
        }
        return putInWorld(world, display, color);
    }

    private int putInWorld(World world, boolean display, Color color){
        int sumstep = 0;
        for(State s : bbest){
            if(s != null){
                sumstep += 1;
                if(display){
                    Carpet carpet= new Carpet(world);
                    world.put(carpet, s.x ,s.y);
                }
            }
        }
        return sumstep;
    }

    private boolean firstVisited(State state){
        if(state.f >= visited[state.x][state.y]){
            return false;
        }
        return true;
    }

    private ArrayList<State> getSuccessors(State state){
        ArrayList<State> successors = new ArrayList<>();
        for(int i = 0;i < path.length;i++){
            State newState = step(state,i);
            if(newState == null) continue;
            newState.h = calc(newState);
            newState.g = state.g + 1;
            newState.f = newState.h + newState.g;
            ArrayList<State> best = new ArrayList<>();
            for(State s: state.best){
                best.add(s);
            }
            best.add(newState);
            newState.best = best;
            successors.add(newState);
        }
        return successors;
    }

    State step(State state, int dir){
        int dirx = state.x + path[dir][0];
        int diry = state.y + path[dir][1];
        if(dirx >= row || dirx < 0 || diry >= column || diry < 0)
            return null;
        if(maze[dirx][diry] == 0)
            return null;
        State newState = new State();
        newState.x = dirx;
        newState.y = diry;
        return newState;
    }
}
