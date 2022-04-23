/*
    Will be spawned from random encounter function
    will be held in an arraylist and cycled through
    will have and select an action after each player action
    will have a loot table and drop loot
    will have x and y coordinates that are instanciated on spawn
    will have an animation;

*/

import java.awt.Graphics2D;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import java.util.Random;
import java.awt.Image;
import java.awt.Font;
import java.awt.Color;


public class NPC {

    private Animation animation;

    private int x;
    private int y;

    private GameWindow window;
    private Player player;

    private String name;


    public NPC (JFrame w, Player p, int X, int Y)
    {
        x = X;
        y = Y;

        this.window = (GameWindow) w;
        this.player= p;

        animation = new Animation(w);

        loadAnimation();

        animation.setLocation(X, Y);
    }
    
    public void draw (Graphics2D g2) {
        animation.draw(g2);

        Font f = new Font ("Calibri", Font.ITALIC, 14);
         g2.setFont (f);
         g2.setColor(Color.WHITE);
         g2.drawString(name, 20 + x,  y - 25 );

    }

    public void act() {
        Random rand = new Random();

        int i = rand.nextInt(3);

        if (i == 1) {
            
        }
        else if (i == 2) {
            
        } else {
            
        }
    }

    public void setName(int i) {
        if (i == 1) {
            name = "Item_Shop";
        } else {
            name = "Quest_Giver_1";

        }
    }









    // Anim related stuff
    public void loadAnimation() {

        Random rand = new Random();

        //int i = rand.nextInt(5);

        int i = 1;// for testing purposes

        if (i == 1) {

            setName(i);

            Image animImage1 = loadImage("images/bird1.png");
            Image animImage2 = loadImage("images/bird2.png");
            Image animImage3 = loadImage("images/bird3.png");
            Image animImage4 = loadImage("images/bird4.png");
            Image animImage5 = loadImage("images/bird5.png");
            Image animImage6 = loadImage("images/bird6.png");
            Image animImage7 = loadImage("images/bird7.png");
            Image animImage8 = loadImage("images/bird8.png");
            Image animImage9 = loadImage("images/bird9.png");

            // create animation object and insert frames

            animation = new Animation(window);

            animation.addFrame(animImage1, 200);
            animation.addFrame(animImage2, 200);
            animation.addFrame(animImage3, 200);
            animation.addFrame(animImage4, 200);
            animation.addFrame(animImage5, 200);
            animation.addFrame(animImage6, 200);
            animation.addFrame(animImage7, 200);		
            animation.addFrame(animImage8, 200);
            animation.addFrame(animImage9, 200);
        }
        else if (i == 2) {
            
        } 
        else if (i == 3) {
            
        } 
        else if (i == 4) {
            
        } 
        else {
            
        }
		
	}

    public Image loadImage (String fileName) {
		return new ImageIcon(fileName).getImage();
	}

    public void update_Anim() {
        animation.update();
    }





    

}
