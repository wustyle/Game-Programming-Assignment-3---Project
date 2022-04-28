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

import java.util.ArrayList;
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

    // Stuff for talking
    private ArrayList<String> lines;
    private int line;

    private boolean isTalking;
    private Image chatBoxImage;


    public NPC (JFrame w, Player p, int X, int Y, String n)
    {
        name = n;
        x = X;
        y = Y;

        this.window = (GameWindow) w;
        this.player= p;

        lines = new ArrayList<>();
        line = 0;
        isTalking = false;
        chatBoxImage = ImageManager.loadImage ("images/dialogue-box-png.png");


        if(name == "Jon"){
            down_Animation = loadAnimation("images/NPC/tile", 3, 48);
            left_Animation = loadAnimation("images/NPC/tile", 3, 60);
            right_Animation = loadAnimation("images/NPC/tile", 3, 72);
            up_Animation = loadAnimation("images/NPC/tile", 3, 84);
            //idle_Animation = loadAnimation("images/NPC/tile", 3, 21);

            lines.add("Seems like you're new here missy! The name's Jon!");
            lines.add("I'm Lillia. Where am  I?");
            lines.add("You're in Ravenhome! A small village on the outside of the Forbidden Forest.");
            lines.add("Ravenhome? But I'm from St. Petersville.");
            lines.add("Haven't heard of there, honey. And you surely don't look like from here.");
            lines.add("How do I get of Ravenhome?");
            lines.add("The only way out of here, sweetie, is through the Forbidden Forest. But no one ever leaves here, it's too dangerous to pass through.");
            lines.add("I have no choice, I must get back home.");
            lines.add("Well, good luck but you will need some weapons if you want to get through the forest alive. Talk to Layla, she's the village chief. She can help you.");
            lines.add("Thanks, Jon.");
        }
        else if (name == "Layla - Village Chief") {
            down_Animation = loadAnimation("images/NPC/tile", 3, 54);
            left_Animation = loadAnimation("images/NPC/tile", 3, 66);
            right_Animation = loadAnimation("images/NPC/tile", 3, 78);
            up_Animation = loadAnimation("images/NPC/tile", 3, 90);
            //idle_Animation = loadAnimation("images/NPC/tile", 3, 21);

            lines.add("Hello, I'm Lillia, I need to go through the Forbidden Forest to go back home. Can you help me?");
            lines.add("The Forbidden Forest? That's quite a scary place for a tiny person like you.");
            lines.add("Please, I don't belong here. My parents must be worried about me.");
            lines.add("Okay little one, I shall give you weapons and items that can assist you through the forest, but you must be careful, there are dangerous monsters that can pop up out of nowhere.");
            lines.add("Thank you, village chief.");
            lines.add("Press 'SHIFT' to attack.");

        } else if(name == "Zeke - Forest guard"){
            down_Animation = loadAnimation("images/NPC/tile", 3, 0);
            left_Animation = loadAnimation("images/NPC/tile", 3, 12);
            right_Animation = loadAnimation("images/NPC/tile", 3, 24);
            up_Animation = loadAnimation("images/NPC/tile", 3, 36);
            //idle_Animation = loadAnimation("images/NPC/tile", 3, 21);

            lines.add("You're going into the forest? Be careful in there. Stock up on potions by killing enemies.");
            lines.add("Thank you!");

        }

        

        // loadAnimation();

        
    }
    
    public void draw (Graphics2D g2) {
        //animation.draw(g2);

        curr_Animation.draw(g2, x, y);

        Font f = new Font ("Calibri", Font.ITALIC, 14);
         g2.setFont (f);
         g2.setColor(Color.WHITE);
         g2.drawString(name, 20 + x,  y - 25 );

         if (isTalking) {
            if (y <300) {
               g2.drawImage(chatBoxImage, x - 5, y+90, 250, 150, null);
               g2.setFont (f);
               g2.setColor(Color.WHITE);
               g2.drawString(name, 5 + x,  y + 95 );
      
               g2.drawString(lines.get(line), 5 + x,  y + 110 );

            }
            else {
               g2.drawImage(chatBoxImage, x - 5, y-250, 250, 150, null);

               g2.setFont (f);
                g2.setColor(Color.WHITE);
                g2.drawString("Lost Girl", 5 + x,  y - 245 );
    
                g2.drawString(lines.get(line), 5 + x,  y - 230 );
            }
   
   
            
         }

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
  
          for (int i=offset; i<frames + offset; i++) {
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
  
        if (anim_stop_condition * 2 <= anim_stop_counter) {
           act();
        }
        else
           curr_Animation.update();
     } 

     public void talk() {
         isTalking = true;



     }

     public void talkTarget() {

      
        if(!isTalking)
        {
           if (line >= lines.size()) {
              line = 0;
  
              
              
           }
  
           player.setTalking(false);
  
           isTalking = true;
        }
        else {
           player.setLine(lines.get(line + 1));
  
           player.setTalking(true);
  
           isTalking = false;
  
           line++;
           line++;
        }
        
  
  
     }

    

}



// Hi I'm max. I'm sick :(
