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


public class Enemy {

    private Animation animation;

    private Anim curr_Animation;
    private Anim idle_animation;
    private Anim die_Animation;
    private Anim atk_animation;
    private Anim hurt_animation;
    private Anim cast_animation;
    private Anim taunt_animation;

    private int anim_stop_condition;
    private int anim_stop_counter;

    private int x;
    private int y;

    private GameWindow window;
    private Player player;

    private String lootTable[];

    private Image animImage1;


    //Combat related stats
    private String name;
    private int LVL;
    
    private int HP;
    private int dmg;

    private String action;

    public Enemy (JFrame w, Player p, int X, int Y)
    {
        x = X;
        y = Y;

        this.window = (GameWindow) w;
        this.player= p;

        genEnemy();

        //animation.setLocation(X, Y);

        action = "";

    }

    public Enemy (JFrame w, Player p) //To use for loading the boss
    {
        
        this.window = (GameWindow) w;
        this.player= p;

        x = 750;
        y = 400;

        genLootTable(2);
        setName(2);
        
        animation.setLocation(750, 400);

        action = "";

            LVL = player.getLVL() + 2;
            
            HP = LVL * 25;

            dmg = LVL * 4;

    }

    private void genEnemy() {
        Random rand = new Random();

        int i = rand.nextInt(1);
        i = 1;
        genLootTable(i);
        setName(i);

        

        if (i == 1) {
            LVL = player.getLVL() + 1;
            
            HP = LVL * 3;

            dmg = LVL * 3;

            idle_animation = loadAnimation("images/enemies/Flipped/Flipped/Idle/Wraith_03_Idle_", 12, 0);
            die_Animation = loadAnimation("images/enemies/Flipped/Flipped/Dying/Wraith_03_Dying_", 15, 0);
            atk_animation = loadAnimation("images/enemies/Flipped/Flipped/AttackingFlip/Wraith_03_Attack_", 12, 0);
            hurt_animation = loadAnimation("images/enemies/Flipped/Flipped/Hurt/Wraith_03_Hurt_", 12, 0);
            cast_animation = loadAnimation("images/enemies/Flipped/Flipped/Casting/Wraith_03_Casting Spells_", 18, 0);
            taunt_animation = loadAnimation("images/enemies/Flipped/Flipped/Taunt/Wraith_03_Taunt_", 18, 0);
            curr_Animation = idle_animation;

            anim_stop_condition = 12;
            anim_stop_counter = 1;
        }

        if (name == "Charizard!!!") {
            LVL = player.getLVL() + 2;
            
            HP = LVL * 25;

            dmg = LVL * 4;

            animImage1 = loadImage("images/enemies/charizard.jpg");

        }
    }
    
    public void draw (Graphics2D g2) {
        if (name == "wraith") {
            curr_Animation.draw(g2, x, y, true);
        } else{
            g2.drawImage(animImage1, x, y, 150, 150, null);

        }
        

        Font f = new Font ("Calibri", Font.ITALIC, 14);
         g2.setFont (f);
         g2.setColor(Color.WHITE);
         g2.drawString(action, 20 + x,  y - 35 );

         g2.drawString(name, 20 + x,  y - 25 );

         g2.drawString("" + HP, 25 + x,  y - 15 );
    }

    public void act() {
        Random rand = new Random();

        int i = rand.nextInt(3);

        if (name == "wraith") {
            if (i == 1) {
                attack();
    
                curr_Animation = atk_animation;
                anim_stop_counter = 1;
                anim_stop_condition = 12;
            }
            else if (i == 2) {
                action = "k,jbsdjfnaldfkaldmmk - speaks in undeath...";
                
                curr_Animation = taunt_animation;
                anim_stop_counter = 1;
                anim_stop_condition = 18;
            } else {
                action = "Necromancy - Undead energies flow to the wraith!";

                HP = LVL * 3;

                curr_Animation = cast_animation;
                anim_stop_counter = 1;
                anim_stop_condition = 18;
            }
        }
        
    }

    public int getHP() {
        return HP;
    }

    public void setHP(int hP) {
        HP = hP;

        if (HP > 0) {
            curr_Animation = hurt_animation;
            anim_stop_counter = 1;
            anim_stop_condition = 12; 
        }
    }

    public void attack() {
        action = "Attack";
        int enemyHP = player.getHp() - dmg;
  
        if (enemyHP <= 0) {
           //window.gameOver();
           player.setHp(enemyHP);

        } else {
           player.setHp(enemyHP);
        }


     }

    public void die() {
        loot();

        curr_Animation = die_Animation;
        anim_stop_counter = 1;
        anim_stop_condition = 15;

        //window.removeEnemy(this);

        //HP = 0;

        window.removeEnemy(this);
    }
    public void die2() {
        //loot();

        window.removeEnemy(this);
    }
    public void die3() {
        window.setWon(true);

        window.removeEnemy(this);
    }


    private void loot() {
        window.addLoot(new Item(""));

        player.MoneyPlusPlus(LVL * 3);
        
        player.lvlUp();
    }

    private void genLootTable(int i) {
        
    }

    public void setName(int i) {
        if (i == 1) {
            name = "wraith";
        } else {
            name = "Charizard!!!";

        }
    }









    // Anim related stuff


    public Anim loadAnimation(String dir, int frames, int offset) {
      // load images for wild cat animation
		
		String prefix = dir;
		String suffix = ".png.png";

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

    public Image loadImage (String fileName) {
		return new ImageIcon(fileName).getImage();
	}

    public void update_Anim() {
        anim_stop_counter++;

      if (anim_stop_condition <= anim_stop_counter) {
          if (curr_Animation == die_Animation || curr_Animation.equals(die_Animation) || HP <= 0) {
            window.removeEnemy(this);

          }
         anim_stop_counter = 1;
         curr_Animation = idle_animation;
         anim_stop_condition = 12;
      }
      else
         curr_Animation.update();
    }





    

}
