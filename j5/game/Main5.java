// package game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;

import com.hasiya.calabashbros.World;
import com.hasiya.screen.Screen;
import com.hasiya.screen.WorldScreen;

import asciiPanel.AsciiFont;
import asciiPanel.AsciiPanel;

import java.util.ArrayList;
import java.util.List;

public class Main5 extends JFrame implements KeyListener {

    private AsciiPanel terminal;
    private Screen screen;
    private boolean gameOver = false;
    private boolean gameBegin = true;
    public static int over;

    public Main5() {
        super();
        //terminal = new AsciiPanel(World.OUTWIDTH, World.OUTHEIGHT, AsciiFont.CP437_10x10);
        terminal = new AsciiPanel(21, 23, AsciiFont.CP437_16x16);
        add(terminal);
        pack();
        screen = new WorldScreen();
        addKeyListener(this);
        repaint();
        ExecutorService exec = Executors.newCachedThreadPool();
        exec.execute(new repaintCounter());
        over = 0;
    }

    public class repaintCounter implements Runnable{
        public void run(){
            while (over == 0){
                try {
                    repaint();
                    TimeUnit.MILLISECONDS.sleep(50);
                } catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
            if(over == 1){
                repaintOver();
            }
            if(over == -1){
                repaintFail();
            }
        }
    }

    public void repaintOver(){
        terminal.clear();
        screen.displayOver(terminal);
        super.repaint();
        gameOver = true;
    }

    int k = 1;

    public void repaintBegin(){
        terminal.clear();
        screen.displayBegin(terminal, k);
        super.repaint();
        k++;
    }

    public void repaintFail(){
        terminal.clear();
        screen.displayFail(terminal);
        super.repaint();
        gameOver = true;
    }
    @Override
    public void repaint() {
        terminal.clear();
        this.over = screen.displayOutput(terminal);
        super.repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

        screen = screen.respondToUserInput(e);
        repaint();
        // if(gameBegin){
        //     if(e.getKeyCode() == 0x0A){//enter
        //         gameBegin = false;
        //         screen = new WorldScreen();
        //         repaint();
        //     }
        //     else if(e.getKeyCode() == 0x41){//a
        //         gameBegin = false;
        //         screen = new WorldScreen();
        //         repaint();
        //     }
        // }
        // else if(gameOver){
        //     if(e.getKeyCode() == 0x0A){
        //         repaintBegin();
        //         gameOver = false;
        //         gameBegin = true;
        //         return;
        //     }
        // }
        // else{
        //     screen = screen.respondToUserInput(e);
        //     repaint();
        //     if(screen.over()){
        //         repaintOver();
        //     }
        //     else if(screen.fail()){
        //         repaintFail();
        //     }
        // }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public static void main(String[] args) {
        Main5 app = new Main5();
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        app.setVisible(true);
        //app.repaintBegin();
    }

}
