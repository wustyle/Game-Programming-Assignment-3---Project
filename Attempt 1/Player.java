import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.Image;
import javax.swing.ImageIcon;
import java.awt.Font;


public class Player {

   private static final int XSIZE = 50;	// width of the player's sprite
   private static final int YSIZE = 50;	// height of the player's sprite			

   private static final int DX = 10;	// amount of X pixels to move in one keystroke
   private static final int DY = 10;	// amount of Y pixels to move in one keystroke

   private JFrame window;		// reference to the JFrame on which player is drawn

   private int x;			// x-position of player's sprite
   private int y;			// y-position of player's sprite

   Graphics2D g2;
   private Dimension dimension;

   private Image playerImage, playerLeftImage, playerRightImage;

   private Anim curr_Animation;
   private Anim idle_animation;
   private Anim walk_left_animation;
   private Anim walk_up_animation;
   private Anim walk_down_animation;
   private Anim walk_right_animation;
   private Anim die_Animation;
   private Anim atk_animation;
   private Anim hurt_animation;

   private int anim_stop_condition;
   private int anim_stop_counter;

   // Combat related stuff
   private int lvl;
   private int hp;
   private int dmg;
   private Equipment weapon;
   private int w2;
   private int a2;

   private String action;



   // Talking related stuff

   private boolean isTalking;
   private String line;
   private Image chatBoxImage;

   // Economy related stuff;

   private int money;

   public Player (JFrame window) {
      this.window = window;

      chatBoxImage = ImageManager.loadImage ("images/dialogue-box-png.png");

      playerLeftImage = loadImage("images/playerLeft.gif");
      playerRightImage = loadImage("images/playerRight.gif");

      playerImage = playerRightImage;

      x = (int) ((window.getWidth() - playerRightImage.getWidth(null)) / 2);
      y = (int) ((window.getHeight() - playerRightImage.getHeight(null)) / 2);

      weapon = new Equipment("sword");

      a2 = 5;
      w2 = 5;
      lvl = 0;
      lvlUp();

      action = "";

      idle_animation = loadAnimation("images/NPC/tile", 3, 9);
      walk_right_animation = loadAnimation("images/NPC/tile", 3, 33);
      walk_down_animation = loadAnimation("images/NPC/tile", 3, 9);
      walk_left_animation = loadAnimation("images/NPC/tile", 3, 21);
      walk_up_animation = loadAnimation("images/NPC/tile", 3, 45);
      //die_Animation = loadAnimation("images/player/sailorMain/tile", 11);
      atk_animation = loadAnimation("images/player/sailorMain/tile", 5, 122);
      //hurt_animation = loadAnimation("images/player/sailorMain/tile", 4);
      curr_Animation = idle_animation;

      anim_stop_counter = 1;
      anim_stop_condition = 12;

      isTalking = false;
   }

   public void draw (Graphics2D g2) {
	   //g2.drawImage (playerImage, x, y, XSIZE, YSIZE, null);
      curr_Animation.draw(g2, x, y);

      Font f = new Font ("Calibri", Font.ITALIC, 14);
      g2.setFont (f);
      g2.setColor(Color.BLACK);
      g2.drawString(action, 20 + x,  y - 35 );

      g2.drawString("Lost Girl", 20 + x,  y - 25 );

      g2.drawString("" + hp, 25 + x,  y - 15 );

      f = new Font ("Calibri", Font.ITALIC, 60);
      g2.setFont (f);
      g2.setColor(Color.YELLOW);

      g2.drawString("" + money , window.getWidth() - 300,  window.getHeight() - 50 );
      g2.drawString( "Gold", window.getWidth() - 125,  window.getHeight() - 50 );

      
      if (isTalking) {
         if (y <300) {
            g2.drawImage(chatBoxImage, x - 5, y+90, 250, 150, null);
            g2.setFont (f);
            g2.setColor(Color.WHITE);
            g2.drawString("Lost Girl", 5 + x,  y + 145 );
   
            g2.drawString(line, 5 + x,  y + 160 );

         }
         else {
            g2.drawImage(chatBoxImage, x - 5, y-150, 250, 150, null);

            g2.setFont (f);
            g2.setColor(Color.WHITE);
            g2.drawString("Lost Girl", 5 + x,  y - 115 );

            g2.drawString(line, 5 + x,  y - 100 );
         }
      }
   }


   public Rectangle2D.Double getBoundingRectangle() {
      return new Rectangle2D.Double (x, y, XSIZE, YSIZE);
   }


   public Image loadImage (String fileName) {
      return new ImageIcon(fileName).getImage();
   }


   public void moveLeft () {
      Dimension dimension;

      if (!window.isVisible ()) return;
      
      curr_Animation = walk_left_animation;
      anim_stop_counter = 1;
      anim_stop_condition = 10;

      dimension = window.getSize();

      if ((x - DX) > 0)
      	  x = x - DX;

      // check if x is outside the left side of the tile map
   }


   public void moveRight () {
      Dimension dimension;

      if (!window.isVisible ()) return;

      //playerImage = playerRightImage;

      curr_Animation = walk_right_animation;
      anim_stop_counter = 1;
      anim_stop_condition = 10;

      dimension = window.getSize();

      if ((x + DX + playerImage.getWidth(null)) < dimension.getWidth())
      	  x = x + DX;

      // check if x is outside the right side of the tile map.

   }

   public void moveUp () {
      Dimension dimension;

      if (!window.isVisible ()) return;
      
      curr_Animation = walk_up_animation;
      anim_stop_counter = 1;
      anim_stop_condition = 12;

      dimension = window.getSize();

      if ((y - DY) > 0)
      	  y = y - DY;

      // check if x is outside the left side of the tile map
   }


   public void moveDown () {
      Dimension dimension;

      if (!window.isVisible ()) return;

      curr_Animation = walk_down_animation;
      anim_stop_counter = 1;
      anim_stop_condition = 10;

      dimension = window.getSize();

      if ((y + DY + playerImage.getHeight(null)) < dimension.getHeight())
      	  y = y + DY;
/*
      int tileMapHeight = tileMap.getHeightPixels(); // double check if not working

      int playerHeight = playerImage.getHeight(null);

      if ((y + DY + playerHeight) <= tileMapHeight) {

	  int yTile = tileMap.pixelsToTiles(y + DY + playerHeight);
	  int xTile = tileMap.pixelsToTiles(x) - 1;

          String mess = "Coordinates in TileMap: (" + xTile + "," + yTile + ")";
	  System.out.println (mess);

	  if (tileMap.getTile(xTile, yTile) == null)
	  	y = y + DY;
      } */

      // check if x is outside the right side of the tile map.

   }

   //old code
   /* public void moveUp () {

      if (!window.isVisible ()) return;

      y = y - DY;
   }


   public void moveDown () {

      if (!window.isVisible ()) return;

      y = y + DY;
   } */


   public int getX() {
      return x;
   }


   public void setX(int x) {
      this.x = x;
   }


   public int getY() {
      return y;
   }


   public void setY(int y) {
      this.y = y;
   }


   public Image getImage() {
      return playerImage;
   }

   public void lvlUp() {
      lvl++;
      hp = lvl * 5 + a2;
      //dmg = (lvl * 2) + weapon.getAtk();
      dmg = (lvl * 2) + w2;
   }

   public void equip(Equipment e) {
      weapon = e;

      w2 += 5;

      //dmg = (lvl * 2) + weapon.getAtk();

      dmg = lvl * 2 + w2;
   }

   public int getDmg() {
       return dmg;
   }

   public void attack(Enemy target) {
      action = "Attack";
      curr_Animation = atk_animation;
      anim_stop_counter = 1;
      anim_stop_condition = 12;
      int enemyHP = target.getHP() - dmg;

      if (enemyHP <= 0) {
         target.die();
      } else {
         target.setHP(enemyHP);
      }
   }

   public void drinkPotion() {
      action = "Drinks potion. You are healed to full health!";
      
      hp = lvl * 5 + a2;
   }

   public int getHp() {
       return hp;
   }

   public void setHp(int hp) {
       this.hp = hp;

       if (this.hp <= 0) {
          action = "Lose";
          GameWindow g = (GameWindow) window;
          g.gameOver();
       }
   }

   public int getLVL() {
      return lvl;
   }

   public void MoneyPlusPlus(int m) {
       money += m;
   }

   public void MoneyMinusMinus(int m){
      money -= m;
   }

   






   //Stuff for animation

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

   public Anim loadAnimation2(String dir, int frames) {
      // load images for wild cat animation
		
		String prefix = dir;
		String suffix = ".png";

		Anim animation = new Anim((GameWindow) window);

      String fullPath;

		for (int i=1; i<=frames; i++) {
			
            fullPath = prefix + i + suffix;

         
         
         
            
			Image animImage = ImageManager.loadImage(fullPath);
			animation.addFrame(animImage, 100);
		}

      return animation;
   }

   public void update_Anim() {

      anim_stop_counter++;

      if (anim_stop_condition <= anim_stop_counter) {
         anim_stop_counter = 1;
         curr_Animation = idle_animation;
         anim_stop_condition = 3;
      }
      else
         curr_Animation.update();
   }









   // stuff for talking

   public void setTalking(boolean b) {
      isTalking = b;
   }

   public void setLine(String s) {
      line = s;
   }






   // Upgrading armour

   public void upgradeWeapon() {
      money -= 500;
      
      w2 += 5;

      dmg = lvl * 2 + w2;
   }

   public void upgradeArmour() {
      money -= 500;
      
      w2 += 5;

      dmg = lvl * 2 + w2;
   }

   public int getMoney() {
       return money;
   }
}