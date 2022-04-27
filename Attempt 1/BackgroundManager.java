/* BackgroundManager manages many backgrounds (wraparound images 
   used for the game's background). 

   Backgrounds 'further back' move slower than ones nearer the
   foreground of the game, creating a parallax distance effect.

   When a sprite is instructed to move left or right, the sprite
   doesn't actually move, instead the backgrounds move in the 
   opposite direction (right or left).

*/

import java.awt.Graphics2D;
import javax.swing.JFrame;


public class BackgroundManager {

	/* private String bgImages[] = {"images/layer_08.png",
			       	     "images/layer_07.png",
				     "images/layer_06.png",
				     "images/layer_05.png",
				     "images/layer_04.png",
				     "images/layer_03.png",
				     "images/layer_02.png",
			       	     "images/layer_01.png"};

  	private int moveAmount[] = {1, 2, 3, 4, 4, 4, 5, 10};   */
						// pixel amounts to move each background left or right
     						// a move amount of 0 makes a background stationary

	// testing a new big bg
	private String bgImages[] = {"images/BG's/al2.jpg"};
	private String bgImagesC2[] = {"images/BG's/10.jpg"};
	private String bgImagesCombat[] = {"images/BG's/10.jpg"};


	private int moveAmount[] = {10};

  	private Background[] backgrounds;
	private Background[] backgroundsC2;

  	private int numBackgrounds;
	private int numBackgroundsC2;


  	private JFrame window;			// JFrame on which backgrounds are drawn

  	public BackgroundManager(JFrame window, int moveSize) {
						// ignore moveSize
    		this.window = window;

    		numBackgrounds = bgImages.length;
    		backgrounds = new Background[numBackgrounds];

    		for (int i = 0; i < numBackgrounds; i++) {
       			backgrounds[i] = new Background(window, bgImages[i], moveAmount[i]);
    		}


    		numBackgroundsC2 = bgImagesC2.length;
			backgroundsC2 = new Background[numBackgroundsC2];

			for (int i = 0; i < numBackgroundsC2; i++) {
				backgroundsC2[i] = new Background(window, bgImagesC2[i], moveAmount[i]);
		 	}
  	} 


  	public void moveRight() { 
		for (int i=0; i < numBackgrounds; i++)
      			backgrounds[i].moveRight();

		backgroundsC2[0].moveRight();

  	}


  	public void moveLeft() {
		for (int i=0; i < numBackgrounds; i++)
      			backgrounds[i].moveLeft();

		backgroundsC2[0].moveLeft();

  	}

	  public void moveDown() { 
		for (int i=0; i < numBackgrounds; i++)
      			backgrounds[i].moveDown();
		
		
      	backgroundsC2[0].moveDown();
  	}


  	public void moveUp() {
		for (int i=0; i < numBackgrounds; i++)
      			backgrounds[i].moveUp();

      	backgroundsC2[0].moveUp();
  	}


  	// The draw method draws the backgrounds on the screen. The
  	// backgrounds are drawn from the back to the front.

  	public void draw (Graphics2D g2) { 
		for (int i=0; i < numBackgrounds; i++)
      			backgrounds[i].draw(g2);
  	}

	public void drawC2 (Graphics2D g2) { 
		for (int i=0; i < numBackgroundsC2; i++)
      			backgroundsC2[i].draw(g2);
  	}

	  public void drawCM (Graphics2D g2) { 
		for (int i=0; i < numBackgroundsC2; i++)
      			backgroundsC2[i].draw(g2);
  	}

}