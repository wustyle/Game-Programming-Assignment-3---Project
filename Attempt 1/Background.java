import java.awt.Graphics2D;
import java.awt.Dimension;
import java.awt.Image;
import javax.swing.JFrame;
import javax.swing.ImageIcon;

public class Background {
  	private Image bgImage;
  	private int bgImageWidth;      		// width of the background (>= panel Width)
	private int bgImageHeight;      		// width of the background (>= panel Width)

	private JFrame window;
	private Dimension dimension;

 	private int bgX;
	private int backgroundX;
	private int backgroundX2;
	private int bgDX;			// size of the background move (in pixels)

	private int bgY;
	private int backgroundY;
	private int backgroundY2;
	private int bgDY;			// size of the background move (in pixels)


  public Background(JFrame window, String imageFile, int bgDX) {
    
	this.window = window;
	this.bgImage = loadImage(imageFile);
	bgImageWidth = bgImage.getWidth(null);	// get width of the background
	bgImageHeight = bgImage.getHeight(null);

	System.out.println ("bgImageWidth = " + bgImageWidth);
	System.out.println ("bgImageHeight = " + bgImageHeight);


	dimension = window.getSize();

	if (bgImageWidth < dimension.width)
      	System.out.println("Background width < panel width");

	if (bgImageHeight < dimension.height)
      	System.out.println("Background height < panel height");

    this.bgDX = bgDX;

	bgDY = bgDX; // double check if to have a separate value for this later, probably the height of the player charachter

  }


  public void moveRight() {

	if (bgX == 0) {
		backgroundX = 0;
		backgroundX2 = bgImageWidth;			
	}

	bgX = bgX - bgDX;

	backgroundX = backgroundX - bgDX;
	backgroundX2 = backgroundX2 - bgDX;

	String mess = "Right: bgX=" + bgX + " bgX1=" + backgroundX + " bgX2=" + backgroundX2;
	//System.out.println (mess);

	if ((bgX + bgImageWidth) % bgImageWidth == 0) {
		System.out.println ("Background change: bgX = " + bgX); 
		backgroundX = 0;
		backgroundX2 = bgImageWidth;
	}

  }


  public void moveLeft() {

	if (bgX == 0) {
		backgroundX = bgImageWidth * -1;
		backgroundX2 = 0;			
	}

	bgX = bgX + bgDX;
				
	backgroundX = backgroundX + bgDX;	
	backgroundX2 = backgroundX2 + bgDX;

	String mess = "Left: bgX=" + bgX + " bgX1=" + backgroundX + " bgX2=" + backgroundX2;
	//System.out.println (mess);

	if ((bgX + bgImageWidth) % bgImageWidth == 0) {
		System.out.println ("Background change: bgX = " + bgX); 
		backgroundX = bgImageWidth * -1;
		backgroundX2 = 0;
	}			
   }

   //a copy of moveLeft to be edited
   public void moveUp() {

	if (bgY == 0) {
		backgroundY = bgImageHeight * -1;
		backgroundY2 = 0;			
	}

	bgY = bgY + bgDY;
				
	backgroundY = backgroundY + bgDY;	
	backgroundY2 = backgroundY2 + bgDY;

	String mess = "Left: bgY=" + bgY + " bgY1=" + backgroundY + " bgY2=" + backgroundY2;
	//System.out.println (mess);

	if ((bgY + bgImageHeight) % bgImageHeight == 0) {
		System.out.println ("Background change: bgY = " + bgY); 
		backgroundY = bgImageHeight * -1;
		backgroundY2 = 0;
	}			
   }

	//a copy of moveRight to be edited
   public void moveDown() {

	if (bgY == 0) {
		backgroundY = 0;
		backgroundY2 = bgImageWidth;			
	}

	bgY = bgY - bgDY;

	backgroundY = backgroundY - bgDY;
	backgroundY2 = backgroundY2 - bgDY;

	String mess = "Right: bgY=" + bgY + " bgY1=" + backgroundY + " bgY2=" + backgroundY2;
	//System.out.println (mess);

	if ((bgY + bgImageWidth) % bgImageWidth == 0) {
		System.out.println ("Background change: bgY = " + bgY); 
		backgroundY = 0;
		backgroundY2 = bgImageWidth;
	}

  }
 

  public void draw (Graphics2D g2) {
	g2.drawImage(bgImage, backgroundX, backgroundY, null);
	g2.drawImage(bgImage, backgroundX2, backgroundY2, null);
  }


  public Image loadImage (String fileName) {
	return new ImageIcon(fileName).getImage();
  }

}
