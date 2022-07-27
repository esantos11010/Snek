/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snek;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URL;
import java.util.Random;

import javax.sound.sampled.*;
import javax.swing.*;

/**
 *
 * @author Edd
 */
public class GameWindow extends JPanel implements ActionListener{

    private int dot;//length of snake
    private int speed;//time between timer calls
    private int appleseaten;//score
    private int x[]=new int[900];// used to keep x cordinates
    private int y[]=new int[900];// used to keep y cordinates
    private int apple_x;// x apple cordinate
    private int apple_y;
    boolean LEFT=false;// move directions
    boolean RIGHT=false;
    boolean UP=false;
    boolean DOWN=false;
    boolean GAMEON=true;
    private Timer timer;// time to use
    Color RColor1, RColor2, RColor3;// random color
    Clip clip;

    public GameWindow (){
        addKeyListener(new TAdapter());// add key listener for game controls
        setBackground(Color.white);
        setFocusable(true);
        setPreferredSize(new Dimension (300, 300));
        initGame(); 
    }
    
    private void initGame() {
        try {
            // Open an audio input stream.
            URL url = this.getClass().getClassLoader().getResource("gamemusic.wav");
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            // Get a sound clip resource.
            clip = AudioSystem.getClip();
            // Open audio clip and load samples from the audio input stream.
            clip.open(audioIn);
            clip.loop(100);
        } 
        catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {}
        RIGHT=true;//game start with snake moving right
        LEFT=false;
        UP=false;
        DOWN=false;
        dot = 1;// start length 1
        speed=500;
        appleseaten=0;
        x[0] = 50;
        y[0] = 50;
        //timer=new Timer();
        timer = new Timer(speed, this);//initiate timer
        timer.start();
        locateApple();// put apple in random spot
    }

    @Override// does the painting
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);
    }
    private void doDrawing(Graphics g) {
        if (GAMEON) {
            g.setColor(RColor1);//apple color
            g.fillOval(apple_x, apple_y, 10, 10);//apple is a oval filled and drawn as circle

            for (int z = 0; z < dot; z++) {
                if (z == 0) {
                    g.setColor(RColor2);//snake head color
                    g.fillOval(x[z],y[z], 10, 10);
                } 
                else {
                    g.setColor(RColor3);//snake body color
                    g.fillOval(x[z],y[z], 10, 10);
                }
            }
            if(appleseaten%2==1)
                setBackground(Color.BLACK);//alternate background color
            else
                setBackground(Color.WHITE);
            Toolkit.getDefaultToolkit().sync();
        }
        else
        {
            clip.stop();//stop music
            timer.stop();//stop timer when game not on
            gameOver(g);//disply game over screen
        }       
    }

    private void gameOver(Graphics g) {        
        try {
            // Open an audio input stream.
            URL url = this.getClass().getClassLoader().getResource("gameover.wav");
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            // Get a sound clip resource.
            Clip clip = AudioSystem.getClip();
            // Open audio clip and load samples from the audio input stream.
            clip.open(audioIn);
            clip.start();
        } 
        catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {}
        String gameover = "Game Over"; 
        String score="You ate "+ appleseaten+ " apples";
        String restart="HIT ENTER TO TRY AGAIN";
        Font small = new Font("Helvetica", Font.BOLD, 18);//set font size
        FontMetrics metr = getFontMetrics(small);// helps with mesurements

        g.setColor(Color.GREEN);// needed color to show up well against black and white background
        g.setFont(small);
        g.drawString(gameover, (300/2)-metr.stringWidth(gameover)/2, 100);
        g.drawString(score, (300/2)-metr.stringWidth(score)/2, 120);
        g.drawString(restart, (300/2)-metr.stringWidth(restart)/2, 140);
    }

    private void checkApple() {// check if snake eats apple

        if ((x[0] == apple_x) && (y[0] == apple_y)) {

            dot++;//increment snake length
            if(speed>=50)// speed up game
            {
                speed-=25;
            }
            timer.stop();// redo time with new speed
            timer = new Timer(speed, this);
            timer.start();
            appleseaten++;//increment score
            Random rand= new Random();//random number generator
            float a = (rand.nextFloat());
            float b = (rand.nextFloat());
            float c = (rand.nextFloat());
            RColor1=Color.getHSBColor(a, 1, .9f);//new colors
            RColor2=Color.getHSBColor(b, 1, .9f);
            RColor3=Color.getHSBColor(c, 1, .9f);
            try {//play apple eating sound
                // Open an audio input stream.
                URL url = this.getClass().getClassLoader().getResource("apple.wav");
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
                // Get a sound clip resource.
                Clip clip = AudioSystem.getClip();
                // Open audio clip and load samples from the audio input stream.
                clip.open(audioIn);
                clip.start();
            } 
            catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {}
            locateApple();//get new apple location
        }
    }
    
    private void locateApple() {
        Random rand = new Random();
        //some multiple of ten to be used as cordinates
        int  n = ((rand.nextInt(27)+2) *10);
        apple_x = (n);

        n=((rand.nextInt(27)+2) *10);
        apple_y = (n);
    }

    private void move() {//move cordnates of snake based on given direction

        for (int z = dot; z > 0; z--) {//snake body just the position of the piece infront of it
            x[z] = x[(z - 1)];
            y[z] = y[(z - 1)];
        }

        if (LEFT) {
            x[0] -= 10;
        }

        if (RIGHT) {
            x[0] += 10;
        }

        if (UP) {
            y[0] -= 10;
        }

        if (DOWN) {
            y[0] += 10;
        }
    }
    private void checkCollision() {// see if snake head is out of bounds or in the same position as body dots

        for (int z = dot; z > 0; z--) {

            if ((x[0] == x[z]) && (y[0] == y[z])) {
                GAMEON = false;
            }
        }

        if (y[0] >= 300) {
            GAMEON = false;
        }

        if (y[0] < 0) {
            GAMEON = false;
        }

        if (x[0] >= 300) {
            GAMEON = false;
        }

        if (x[0] < 0) {
            GAMEON = false;
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {

        if (GAMEON) {//when game is running check the apple, snake cordinates and move, then repaint

            checkApple();
            checkCollision();
            move();
        }

        repaint();
    }
    private class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {//game controls

            int key = e.getKeyCode();

            if ((key == KeyEvent.VK_LEFT) && (!RIGHT)) {
                LEFT = true;
                UP = false;
                DOWN = false;
            }

            if ((key == KeyEvent.VK_RIGHT) && (!LEFT)) {
                RIGHT = true;
                UP = false;
                DOWN = false;
            }

            if ((key == KeyEvent.VK_UP) && (!DOWN)) {
                UP = true;
                RIGHT = false;
                LEFT = false;
            }

            if ((key == KeyEvent.VK_DOWN) && (!UP)) {
                DOWN = true;
                RIGHT = false;
                LEFT = false;
            }
            
            if ((key==KeyEvent.VK_ENTER) && (!GAMEON)){
               GAMEON=true;
               initGame();
            }
        }
    }
    
}
