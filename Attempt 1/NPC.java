/*
    Will be spawned from random encounter function
    will be held in an arraylist and cycled through
    will have and select an action after each player action
    will have a loot table and drop loot
    will have x and y coordinates that are instanciated on spawn
    will have an animation;

*/

import java.awt.Graphics2D;
import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import java.util.Random;
import java.awt.Image;
import java.awt.Font;
import java.awt.Color;


public class NPC {

    private Anim curr_Animation;
    private Anim idle_Animation;
    private Anim left_Animation;
    private Anim right_Animation;
    private Anim up_Animation;
    private Anim down_Animation;


    private int x;
    private int y;

    private GameWindow window;
    private Player player;

    private String name;

    private int anim_stop_counter;
    private int anim_stop_condition;

    public NPC (JFrame w, Player p, int X, int Y)
    {
        x = X;
        y = Y;

        this.window = (GameWindow) w;
        this.player= p;

        down_Animation = loadAnimation("images/NPC/tile", 3, 54);
        left_Animation = loadAnimation("images/NPC/tile", 3, 66);
        right_Animation = loadAnimation("images/NPC/tile", 3, 78);
        up_Animation = loadAnimation("images/NPC/tile", 3, 90);
        //idle_Animation = loadAnimation("images/NPC/tile", 3, 21);

        // loadAnimation();

        
    }
    
    public void draw (Graphics2D g2) {
        //animation.draw(g2);

        Font f = new Font ("Calibri", Font.ITALIC, 14);
         g2.setFont (f);
         g2.setColor(Color.WHITE);
         g2.drawString(name, 20 + x,  y - 25 );

    }

    public void act() {
        Random rand = new Random();

        int i = rand.nextInt(5);

        if (i == 1) {
            moveDown();
        }
        else if (i == 2) {
            moveLeft();
        } else if (i == 3) {
            moveRight();
        } else {
            moveUp();
        }
    }

    public void setName(int i) {
        if (i == 1) {
            name = "Item_Shop";
        } else {
            name = "Quest_Giver_1";

        }
    }






    private void moveLeft () {
        if (!window.isVisible ()) return;
  
        curr_Animation = left_Animation;
        anim_stop_counter = 1;
        anim_stop_condition = 3; 

        if ((x - 10) > 0)
              x = x - 10;
     }

     private void moveRight () {
        Dimension dimension;
  
        if (!window.isVisible ()) return;
        curr_Animation = right_Animation;
        anim_stop_counter = 1;
        anim_stop_condition = 3; 
  
        dimension = window.getSize();
  
        if ((x + 60) < dimension.getWidth())
              x = x + 10;
  
        // check if x is outside the right side of the tile map.
  
     }

     public void moveUp () {
  
        if (!window.isVisible ()) return;
  
        curr_Animation = up_Animation;
        anim_stop_counter = 1;
        anim_stop_condition = 3; 

        if ((y - 10) > 0)
              y = y - 10;
     }

     public void moveDown () {
        Dimension dimension;
  
        if (!window.isVisible ()) return;

        curr_Animation = down_Animation;
        anim_stop_counter = 1;
        anim_stop_condition = 3; 
  
        dimension = window.getSize();
  
        if ((y + 60) < dimension.getHeight())
              y = y + 10;

     }


    // Anim related stuff
    

    public Anim loadAnimation(String dir, int frames, int offset) {
        // load images for wild cat animation
          
          String prefix = dir;
          String suffix = ".png";
  
          Anim animation = new Anim((GameWindow) window);
  
        String fullPath;
  
          for (int i=offset; i<=frames + offset; i++) {
              if(i < 10){
              fullPath = prefix + "00" + i + suffix;
           }
              
           else if(i < 100){
              fullPath = prefix + "0" + i + suffix;
           }
            else {
              fullPath = prefix + i + suffix;
  
            }  
           
           
              
              Image animImage = ImageManager.loadImage(fullPath);
              animation.addFrame(animImage, 100);
          }
  
        return animation;
     }
  
     public void update_Anim() {
  
        anim_stop_counter++;
  
        if (anim_stop_condition <= anim_stop_counter) {
           act();
        }
        else
           curr_Animation.update();
     } 





    

}
