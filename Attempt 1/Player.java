import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.Image;
import javax.swing.ImageIcon;

public class Player {

   private static final int XSIZE = 50;	// width of the player's sprite
   private static final int YSIZE = 50;	// height of the player's sprite			

   private static final int DX = 12;	// amount of X pixels to move in one keystroke
   private static final int DY = 50;	// amount of Y pixels to move in one keystroke

   private JFrame window;		// reference to the JFrame on which player is drawn

   private int x;			// x-position of player's sprite
   private int y;			// y-position of player's sprite

   Graphics2D g2;
   private Dimension dimension;

   private Image playerImage, playerLeftImage, playerRightImage;




   // Combat related stuff
   private int lvl;
   private int hp;
   private int dmg;
   private Equipment weapon;

   public Player (JFrame window) {
      this.window = window;

      playerLeftImage = loadImage("images/playerLeft.gif");
      playerRightImage = loadImage("images/playerRight.gif");

      playerImage = playerRightImage;

      x = (int) ((window.getWidth() - playerRightImage.getWidth(null)) / 2);
      y = (int) ((window.getHeight() - playerRightImage.getHeight(null)) / 2);;

      weapon = new Equipment("sword");
      lvl = 0;
      lvlUp();

   }


   public void draw (Graphics2D g2) {
	   g2.drawImage (playerImage, x, y, XSIZE, YSIZE, null);
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

      playerImage = playerLeftImage;

      dimension = window.getSize();

      if ((x - DX) > 0)
      	  x = x - DX;

      // check if x is outside the left side of the tile map
   }


   public void moveRight () {
      Dimension dimension;

      if (!window.isVisible ()) return;

      playerImage = playerRightImage;

      dimension = window.getSize();

      if ((x + DX + playerImage.getWidth(null)) < dimension.getWidth())
      	  x = x + DX;

      // check if x is outside the right side of the tile map.

   }

   public void moveUp () {
      Dimension dimension;

      if (!window.isVisible ()) return;

      playerImage = playerLeftImage;

      dimension = window.getSize();

      if ((y - DY) > 0)
      	  y = y - DY;

      // check if x is outside the left side of the tile map
   }


   public void moveDown () {
      Dimension dimension;

      if (!window.isVisible ()) return;

      playerImage = playerRightImage; // we need a player down image

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

   private void lvlUp() {
      lvl++;
      hp = lvl * 5;
      dmg = (lvl * 2) + weapon.getAtk();
   }

   private void equip(Equipment e) {
      weapon = e;

      dmg = (lvl * 2) + weapon.getAtk();
   }

   public int getDmg() {
       return dmg;
   }

   public void attack(Enemy target) {
      int enemyHP = target.getHP() - dmg;

      if (enemyHP <= 0) {
         target.die();
      } else {
         target.setHP(enemyHP);
      }
   }

   public int getHp() {
       return hp;
   }

   public void setHp(int hp) {
       this.hp = hp;
   }

}