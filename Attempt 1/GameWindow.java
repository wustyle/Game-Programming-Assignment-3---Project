import javax.swing.*;			// need this for GUI objects
import java.awt.*;			// need this for certain AWT classes
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.awt.event.*;
import java.awt.image.BufferStrategy;	// need this to implement page flipping
import java.util.Random;
import java.awt.geom.Rectangle2D;


public class GameWindow extends JFrame implements
				Runnable,
				KeyListener,
				MouseListener,
				MouseMotionListener,
				ActionListener
{
  	private static final int NUM_BUFFERS = 2;	// used for page flipping

	private int pWidth, pHeight;     		// width and height of screen

	private Thread gameThread = null;            	// the thread that controls the game
	private volatile boolean isRunning = false;    	// used to stop the game thread

	private Animation animation = null;		// animation sprite
	private ImageEffect imageEffect;		// sprite demonstrating an image effect

	private BufferedImage image;			// drawing area for each frame

	private Image quit1Image;			// first image for quit button
	private Image quit2Image;			// second image for quit button

	private boolean finishedOff = false;		// used when the game terminates

	private volatile boolean isOverQuitButton = false;
	private Rectangle quitButtonArea;		// used by the quit button

	private volatile boolean isOverPauseButton = false;
	private Rectangle pauseButtonArea;		// used by the pause 'button'
	private volatile boolean isPaused = false;

	private volatile boolean isOverStopButton = false;
	private Rectangle stopButtonArea;		// used by the stop 'button'
	private volatile boolean isStopped = false;

	private volatile boolean isOverShowAnimButton = false;
	private Rectangle showAnimButtonArea;		// used by the show animation 'button'
	private volatile boolean isAnimShown = false;

	private volatile boolean isOverPauseAnimButton = false;
	private Rectangle pauseAnimButtonArea;		// used by the pause animation 'button'
	private volatile boolean isAnimPaused = false;
   
	private GraphicsDevice device;			// used for full-screen exclusive mode 
	private Graphics gScr;
	private BufferStrategy bufferStrategy;

	private SoundManager soundManager;
	BackgroundManager bgManager;
	TileMapManager tileManager;
	TileMap	tileMap;

	private Inventory inventory;
	private boolean isInventoryVisible;

	private boolean isCombat;
	private boolean isChap2;
	private boolean isGameOver;
	private boolean isWon;

	private Player player;

	private ArrayList<NPC> npcs;
	private ArrayList<Enemy> enemies; 
	private ArrayList<Item> items; 
	private Enemy currTarget;
	private NPC currNPCTarget;

	private CombatMenu CM;

	ArrayList<JPanel> panels;


	public GameWindow() {
 
		super("Tiled Bat and Ball Game: Full Screen Exclusive Mode");

		initFullScreen();

		quit1Image = loadImage("images/Quit1.png");
		quit2Image = loadImage("images/Quit2.png");

		setButtonAreas();

		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);

		loadAnimation();
		soundManager = SoundManager.getInstance();
		image = new BufferedImage (pWidth, pHeight, BufferedImage.TYPE_INT_RGB);

		initInventory();

		initCombatMenu();

		panels = new ArrayList<>();

		//panels.add(initNewPanel());

		isCombat = false;
		isChap2  = true;
		isGameOver = false;
		isWon = false;

		player = new Player(this);

		enemies = new ArrayList<>(); //should work
		
		initNPCs();
		
		startGame();
	}

	private void initNPCs() {
		npcs = new ArrayList<>();

		npcs.add(new NPC(this, player, 300, 500, "Jon"));
		npcs.add(new NPC(this, player, 600, 250, "Layla - Village Chief"));
		npcs.add(new NPC(this, player, 900, 600, "Zeke - Forest guard"));

		currNPCTarget = npcs.get(0);
	}

	private JPanel initNewPanel() {
		JPanel p = new JPanel();
		p.setSize(640, 640);
		
		

		//setLayout(null);

		p.setLocation(0, 0);
		add(p);

		p.setVisible(true);
		
		return p;
	}

	private void initInventory() {
		inventory = new Inventory();

		setLayout(null);

		inventory.setLocation(getBounds().width - 320, getBounds().height - 320);
		add(inventory);

		inventory.setVisible(true);
		inventory.setVisible(false);
		isInventoryVisible = false;
	}

	private void initCombatMenu() {
		//CM = new CombatMenu(player, this);
		CM = new CombatMenu();

		//setLayout(null);

        CM.setLocation(0, 0);
		add(CM);

		CM.setVisible(true);
		//CM.setVisible(false);
		//isInventoryVisible = false;
	}


	// implementation of Runnable interface

	public void run () {
		try {
			isRunning = true;
			while (isRunning) {
				if (isPaused == false) {
					gameUpdate();
				}
				screenUpdate();
				Thread.sleep (50);
			}
		}
		catch(InterruptedException e) {}

		finishOff();
	}


	/* This method performs some tasks before closing the game.
	   The call to System.exit() should not be necessary; however,
	   it prevents hanging when the game terminates.
	*/

	private void finishOff() { 
    		if (!finishedOff) {
			finishedOff = true;
			restoreScreen();
			System.exit(0);
		}
	}


	/* This method switches off full screen mode. The display
	   mode is also reset if it has been changed.
	*/

	private void restoreScreen() { 
		Window w = device.getFullScreenWindow();
		
		if (w != null)
			w.dispose();
		
		device.setFullScreenWindow(null);
	}


	public void gameUpdate () {

		player.update_Anim();

		if (isCombat) {
			for (Enemy enemy : enemies) {
				enemy.update_Anim();
			}
		} else if (isChap2) {
			for (NPC npc : npcs) {
				npc.update_Anim();
			}
		} else if (!isChap2) {
			
		}
		else if (!isPaused && isAnimShown && !isAnimPaused) {}
			//animation.update();
		//imageEffect.update();

		
	}


	private void screenUpdate() { 

		try {
			gScr = bufferStrategy.getDrawGraphics();
			gameRender(gScr);
			gScr.dispose();
			if (!bufferStrategy.contentsLost())
				bufferStrategy.show();
			else
				System.out.println("Contents of buffer lost.");
      
			// Sync the display on some systems.
			// (on Linux, this fixes event queue problems)

			Toolkit.getDefaultToolkit().sync();
		}
		catch (Exception e) { 
			e.printStackTrace();  
			isRunning = false; 
		} 
	}


	public void gameRender (Graphics gScr) {		// draw the game objects

		if (isWon) {
			Victory(gScr);
			//not sure what to do when game over
		} 
		else
		if (isGameOver) {
			GameOver(gScr);
			//not sure what to do when game over
		} 
		else
		if (isCombat) {
			combatMap(gScr);
		} 
		else if (!isChap2) {
			chap1(gScr);
		} else {
			chap2(gScr);
		}

		//gameRenderTest(gScr);
		/* Graphics2D imageContext = (Graphics2D) image.getGraphics();

		bgManager.draw(imageContext);
		tileMap.draw(imageContext);
	
		if (isAnimShown)
			animation.draw(imageContext);		// draw the animation

		imageEffect.draw(imageContext);			// draw the image effect

		//Graphics2D g2 = (Graphics2D) getGraphics();	// get the graphics context for window
		drawButtons(imageContext);			// draw the buttons

		Graphics2D g2 = (Graphics2D) gScr;
		g2.drawImage(image, 0, 0, pWidth, pHeight, null);

		if (isInventoryVisible) {
			inventory.repaint();
		}

		imageContext.dispose();
		g2.dispose(); */


	}

	private void chap1(Graphics gScr) {
		//gameRenderTest(gScr);
		Graphics2D imageContext = (Graphics2D) image.getGraphics();

		bgManager.draw(imageContext);
		//tileMap.draw(imageContext);
		player.draw(imageContext);

		
	
		//if (isAnimShown)
		//	animation.draw(imageContext);		// draw the animation

		//imageEffect.draw(imageContext);			// draw the image effect

		//Graphics2D g2 = (Graphics2D) getGraphics();	// get the graphics context for window
		drawButtons(imageContext);			// draw the buttons

		Graphics2D g2 = (Graphics2D) gScr;
		g2.drawImage(image, 0, 0, pWidth, pHeight, null);

		if (isInventoryVisible) {
			inventory.repaint();
		}
		//panels.get(0).repaint();
		CM.repaint();

		imageContext.dispose();
		g2.dispose();
	}

	private void chap2(Graphics gScr) {
		//gameRenderTest(gScr);
		Graphics2D imageContext = (Graphics2D) image.getGraphics();

		bgManager.drawC2(imageContext);
		player.draw(imageContext);

		currNPCTarget.draw2(imageContext);
		for (NPC npc : npcs) {
			npc.draw(imageContext);
		}
	
		//if (isAnimShown)
		//	animation.draw(imageContext);		// draw the animation

		//imageEffect.draw(imageContext);			// draw the image effect

		//Graphics2D g2 = (Graphics2D) getGraphics();	// get the graphics context for window
		drawButtons(imageContext);			// draw the buttons

		Graphics2D g2 = (Graphics2D) gScr;
		g2.drawImage(image, 0, 0, pWidth, pHeight, null);

		if (isInventoryVisible) {
			inventory.repaint();
		}

		imageContext.dispose();
		g2.dispose();
	}

	private void combatMap(Graphics gScr) {
		//gameRenderTest(gScr);
		Graphics2D imageContext = (Graphics2D) image.getGraphics();

		bgManager.drawCM(imageContext);
		player.draw(imageContext);

		for (Enemy enemy : enemies) {
			enemy.draw(imageContext);
		}

		
		
	
		//if (isAnimShown)
		//	animation.draw(imageContext);		// draw the animation

		//imageEffect.draw(imageContext);			// draw the image effect

		//Graphics2D g2 = (Graphics2D) getGraphics();	// get the graphics context for window
		drawButtons(imageContext);			// draw the buttons

		Graphics2D g2 = (Graphics2D) gScr;
		g2.drawImage(image, 0, 0, pWidth, pHeight, null);

		if (!CM.isVisible()) {
			CM.setVisible(true);
		}
		CM.repaint();

		if (isInventoryVisible) {
			inventory.repaint();
		}

		imageContext.dispose();
		g2.dispose();
	}

	private void GameOver(Graphics gScr) {
		//gameRenderTest(gScr);
		Graphics2D imageContext = (Graphics2D) image.getGraphics();

		
		
		Graphics2D g2 = (Graphics2D) gScr;
		g2.drawImage(image, 0, 0, pWidth, pHeight, null);

		


        g2.setColor(Color.black);
        Rectangle2D.Double gameOver = new Rectangle2D.Double(0, 0, getBounds().width, getBounds().height);
        g2.fill(gameOver);

        Font f1 = new Font("Arial Black", Font.PLAIN, 70);
        g2.setFont(f1);
        g2.setColor(Color.yellow);
        g2.drawString("GAME OVER", getBounds().width /20, getBounds().height/2);

        imageContext.dispose();
		g2.dispose();
	}
	
	private void Victory(Graphics gScr) {
		//gameRenderTest(gScr);
		Graphics2D imageContext = (Graphics2D) image.getGraphics();

		
		
		Graphics2D g2 = (Graphics2D) gScr;
		g2.drawImage(image, 0, 0, pWidth, pHeight, null);

		


        g2.setColor(Color.black);
        Rectangle2D.Double gameOver = new Rectangle2D.Double(0, 0, getBounds().width, getBounds().height);
        g2.fill(gameOver);

        Font f1 = new Font("Arial Black", Font.PLAIN, 70);
        g2.setFont(f1);
        g2.setColor(Color.yellow);
        g2.drawString("Victory", getBounds().width /20, getBounds().height/2);

		f1 = new Font("Arial Black", Font.PLAIN, 36);
        g2.setFont(f1);
        g2.setColor(Color.green);

		g2.drawString("Lilia returns home...", getBounds().width /20, getBounds().height/2 + 100);


        imageContext.dispose();
		g2.dispose();
	}

	private void initFullScreen() {				// standard procedure to get into FSEM

		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		device = ge.getDefaultScreenDevice();

		setUndecorated(true);	// no menu bar, borders, etc.
		setIgnoreRepaint(true);	// turn off all paint events since doing active rendering
		setResizable(false);	// screen cannot be resized
		
		if (!device.isFullScreenSupported()) {
			System.out.println("Full-screen exclusive mode not supported");
			System.exit(0);
		}

		device.setFullScreenWindow(this); // switch on full-screen exclusive mode

		// we can now adjust the display modes, if we wish

		showCurrentMode();

		pWidth = getBounds().width;
		pHeight = getBounds().height;
		
		System.out.println("Width of window is " + pWidth);
		System.out.println("Height of window is " + pHeight);

		try {
			createBufferStrategy(NUM_BUFFERS);
		}
		catch (Exception e) {
			System.out.println("Error while creating buffer strategy " + e); 
			System.exit(0);
		}

		bufferStrategy = getBufferStrategy();
	}


	// This method provides details about the current display mode.

	private void showCurrentMode() {

		DisplayMode dms[] = device.getDisplayModes();

		for (int i=0; i<dms.length; i++) {
			System.out.println("Display Modes Available: (" + 
                           dms[i].getWidth() + "," + dms[i].getHeight() + "," +
                           dms[i].getBitDepth() + "," + dms[i].getRefreshRate() + ")  " );			
		}

/*
		DisplayMode d = new DisplayMode (800, 600, 32, 60);
		device.setDisplayMode(d);
*/

		DisplayMode dm = device.getDisplayMode();

		System.out.println("Current Display Mode: (" + 
                           dm.getWidth() + "," + dm.getHeight() + "," +
                           dm.getBitDepth() + "," + dm.getRefreshRate() + ")  " );
  	}


	// Specify screen areas for the buttons and create bounding rectangles

	private void setButtonAreas() {
		
		//  leftOffset is the distance of a button from the left side of the window.
		//  Buttons are placed at the top of the window.

		int leftOffset = (pWidth - (5 * 150) - (4 * 20)) / 2;
		pauseButtonArea = new Rectangle(leftOffset, getBounds().height -50, 150, 40);

		leftOffset = leftOffset + 170;
		stopButtonArea = new Rectangle(leftOffset, getBounds().height -50, 150, 40);

		leftOffset = leftOffset + 170;
		showAnimButtonArea = new Rectangle(leftOffset, getBounds().height -50, 150, 40);

		leftOffset = leftOffset + 170;
		pauseAnimButtonArea = new Rectangle(leftOffset, getBounds().height -50, 150, 40);

		leftOffset = leftOffset + 170;
		int quitLength = quit1Image.getWidth(null);
		int quitHeight = quit1Image.getHeight(null);
		quitButtonArea = new Rectangle(leftOffset, 55, 180, 50);
	}


	public Image loadImage (String fileName) {
		return new ImageIcon(fileName).getImage();
	}


	public void loadAnimation() {

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

		animation = new Animation(this);

		animation.addFrame(animImage1, 200);
		animation.addFrame(animImage2, 200);
		animation.addFrame(animImage3, 200);
		animation.addFrame(animImage4, 200);
		animation.addFrame(animImage5, 200);
		animation.addFrame(animImage6, 200);
		animation.addFrame(animImage7, 200);		
		animation.addFrame(animImage8, 200);
		animation.addFrame(animImage9, 200);
/*
		Image stripImage = loadImage("images/kaboom.gif");

		int imageWidth = (int) stripImage.getWidth(null) / 6;
		int imageHeight = stripImage.getHeight(null);
		//int transparency = stripImage.getColorModel().getTransparency();

		animation = new Animation(this);

		for (int i=0; i<6; i++) {
			BufferedImage frameImage = GraphicsEnvironment.getLocalGraphicsEnvironment().
				getDefaultScreenDevice().getDefaultConfiguration().
				createCompatibleImage(imageWidth, imageHeight,
				Transparency.TRANSLUCENT);

			Graphics2D g = (Graphics2D) frameImage.getGraphics();
     
			g.drawImage(stripImage, 
					0, 0, imageWidth, imageHeight,
					i*imageWidth, 0, (i*imageWidth)+imageWidth, imageHeight,
					null);

			animation.addFrame(frameImage, 500);
		}
*/	
	}


	private void drawButtons (Graphics g) {
		Font oldFont, newFont;

		oldFont = g.getFont();		// save current font to restore when finished
	
		newFont = new Font ("TimesRoman", Font.ITALIC + Font.BOLD, 18);
		g.setFont(newFont);		// set this as font for text on buttons

    		g.setColor(Color.black);	// set outline colour of button

		// draw the pause 'button'

		g.setColor(Color.BLACK);
		
		g.drawOval(pauseButtonArea.x, pauseButtonArea.y, 
			   pauseButtonArea.width, pauseButtonArea.height);
		g.fillOval(pauseButtonArea.x, pauseButtonArea.y, pauseButtonArea.width, pauseButtonArea.height);

		if (isOverPauseButton && !isStopped)
			g.setColor(Color.WHITE);
		else
			g.setColor(Color.RED);	

		if (isChap2) {
			g.drawString("Weapon++", pauseButtonArea.x+45, pauseButtonArea.y+25);
		} else if (isCombat) {
			g.drawString(" Attack", pauseButtonArea.x+45, pauseButtonArea.y+25);
		} else {
			g.drawString("Force Combat", pauseButtonArea.x+25, pauseButtonArea.y+25);
		}

		// draw the stop 'button'

		g.setColor(Color.BLACK);
		g.drawOval(stopButtonArea.x, stopButtonArea.y, 
			   stopButtonArea.width, stopButtonArea.height);
		g.fillOval(stopButtonArea.x, stopButtonArea.y, stopButtonArea.width, stopButtonArea.height);
		if (isOverStopButton && !isStopped)
			g.setColor(Color.WHITE);
		else
			g.setColor(Color.RED);

		if (isChap2) {
			g.drawString("Armour ++", stopButtonArea.x+40, stopButtonArea.y+25);
		} else if (isCombat) {
			g.drawString("   Heal", stopButtonArea.x+40, stopButtonArea.y+25);
		} else {
			g.drawString("Meet Boss", stopButtonArea.x+40, stopButtonArea.y+25);
		}

		// draw the show animation 'button'

		g.setColor(Color.BLACK);
		g.drawOval(showAnimButtonArea.x, showAnimButtonArea.y, 
			   showAnimButtonArea.width, showAnimButtonArea.height);
		g.fillOval(showAnimButtonArea.x, showAnimButtonArea.y, showAnimButtonArea.width, showAnimButtonArea.height);
		if (isOverShowAnimButton && !isPaused && !isStopped)
			g.setColor(Color.WHITE);
		else
			g.setColor(Color.RED);

			 if (isChap2) {
				g.drawString("Go to Forest", showAnimButtonArea.x+35, showAnimButtonArea.y+25);
			} else if (isCombat) {
				g.drawString("     Flee", showAnimButtonArea.x+35, showAnimButtonArea.y+25);
			} else {
				g.drawString("Go to Village", showAnimButtonArea.x+35, showAnimButtonArea.y+25);
			}
      		
		// draw the pause anim 'button'

		/*g.setColor(Color.BLACK);
		g.drawOval(pauseAnimButtonArea.x, pauseAnimButtonArea.y, 
			   pauseAnimButtonArea.width, pauseAnimButtonArea.height);

		if (isOverPauseAnimButton && isAnimShown && !isPaused && !isStopped)
			g.setColor(Color.WHITE);
		else
			g.setColor(Color.RED);

		 if (isAnimShown && isAnimPaused && !isStopped)
			g.drawString("Anim Paused", pauseAnimButtonArea.x+30, pauseAnimButtonArea.y+25);
		else
			g.drawString("Pause Anim", pauseAnimButtonArea.x+35, pauseAnimButtonArea.y+25); 

		// draw the quit button (an actual image that changes when the mouse moves over it)

		//if (isOverQuitButton)
		   //g.drawImage(quit1Image, quitButtonArea.x, quitButtonArea.y, 180, 50, null);
		    	       //quitButtonArea.width, quitButtonArea.height, null);
				
		//else
		//   g.drawImage(quit2Image, quitButtonArea.x, quitButtonArea.y, 180, 50, null);*/
		    	       //quitButtonArea.width, quitButtonArea.height, null);
/*
		g.setColor(Color.BLACK);
		g.drawOval(quitButtonArea.x, quitButtonArea.y, 
			   quitButtonArea.width, quitButtonArea.height);
		if (isOverQuitButton)
			g.setColor(Color.WHITE);
		else
			g.setColor(Color.RED);

		g.drawString("Quit", quitButtonArea.x+60, quitButtonArea.y+25);
*/
		g.setFont(oldFont);		// reset font

	}


	private void startGame() { 
		if (gameThread == null) {
			soundManager.playSound ("village", true);

			bgManager = new BackgroundManager (this, 12);
	
			//tileManager = new TileMapManager (this);

			/* try {
				tileMap = tileManager.loadMap("maps/map1.txt");
				int w, h;
				w = tileMap.getWidth();
				h = tileMap.getHeight();
				System.out.println ("Width of tilemap " + w);
				System.out.println ("Height of tilemap " + h);
			}
			catch (Exception e) {
				System.out.println(e);
				System.exit(0);
			} */

			imageEffect = new ImageEffect (this);
			gameThread = new Thread(this);
			gameThread.start();			

		}
	}


	// displays a message to the screen when the user stops the game

	private void gameOverMessage(Graphics g) {
		
		Font font = new Font("SansSerif", Font.BOLD, 24);
		FontMetrics metrics = this.getFontMetrics(font);

		String msg = "Game Over. Thanks for playing!";

		int x = (pWidth - metrics.stringWidth(msg)) / 2; 
		int y = (pHeight - metrics.getHeight()) / 2;

		g.setColor(Color.BLUE);
		g.setFont(font);
		g.drawString(msg, x, y);

	}


	// implementation of methods in KeyListener interface

	public void keyPressed (KeyEvent e) {

		if (isPaused)
			return;

		int keyCode = e.getKeyCode();

		if ((keyCode == KeyEvent.VK_ESCAPE) || (keyCode == KeyEvent.VK_Q) ||
             	   (keyCode == KeyEvent.VK_END)) {
           		isRunning = false;		// user can quit anytime by pressing
			return;				//  one of these keys (ESC, Q, END)			
         	}
		else
		if (keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_A) {
			if (isCombat) {
				return;
			}
			bgManager.moveLeft();
			player.moveLeft();

			if (!isChap2 && !isCombat) {
				randomCombat();
			}
		}
		else
		if (keyCode == KeyEvent.VK_RIGHT || keyCode == KeyEvent.VK_D) {
			if (isCombat) {
				return;
			}
			bgManager.moveRight();
			player.moveRight();
			
			if (!isChap2 && !isCombat) {
				randomCombat();
			}
		}
		else
		if (keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_W) {
			if (isCombat) {
				return;
			}
			bgManager.moveUp();
			player.moveUp();
			
			if (!isChap2 && !isCombat) {
				randomCombat();
			}
		}
		else
		if (keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_S) {
			if (isCombat) {
				return;
			}
			bgManager.moveDown();
			player.moveDown();
			
			if (!isChap2 && !isCombat) {
				randomCombat();
			}
		}
		/* else
		if (keyCode == KeyEvent.VK_I) {
			

			if(inventory.isVisible()) {
				inventory.setVisible(false);
				isInventoryVisible = false;
				
			}
			else {
				inventory.setVisible(true);
				isInventoryVisible = true;
			}
		} */
		else 
		if (keyCode == KeyEvent.VK_T && isChap2) {
			currNPCTarget.talk();
		}
		else 
		if (keyCode == KeyEvent.VK_E && isChap2) {
			cycleTargets();
		}
		else 
		if (keyCode == KeyEvent.VK_E && isCombat) {
			cycleEnemies();
		}
	}


	
	public void keyReleased (KeyEvent e) {

	}


	public void keyTyped (KeyEvent e) {

	}


	// implement methods of MouseListener interface

	public void mouseClicked(MouseEvent e) {

	}


	public void mouseEntered(MouseEvent e) {

	}


	public void mouseExited(MouseEvent e) {

	}


	public void mousePressed(MouseEvent e) {
		testMousePress(e.getX(), e.getY());
	}


	public void mouseReleased(MouseEvent e) {

	}


	// implement methods of MouseMotionListener interface

	public void mouseDragged(MouseEvent e) {

	}	


	public void mouseMoved(MouseEvent e) {
		testMouseMove(e.getX(), e.getY()); 
	}


	/* This method handles mouse clicks on one of the buttons
	   (Pause, Stop, Start Anim, Pause Anim, and Quit).
	*/

	private void testMousePress(int x, int y) {

		if (isStopped && !isOverQuitButton) 	// don't do anything if game stopped
			return;

		if (isOverStopButton && isCombat) {			// mouse click on Stop button
			player.drinkPotion();
		}
		else
		if (isOverPauseButton && isCombat) {		// mouse click on Pause button
			attack();    	// toggle pausing
		}
		else 
		if (isOverShowAnimButton && isCombat) {// mouse click on Start Anim button
			flee();
		}
		else
		if (isOverStopButton && isChap2) {		
			if (player.getMoney() >=500) {
				player.upgradeArmour();; 
			}
		}
		else
		if (isOverPauseButton && isChap2) {	
			if (player.getMoney() >=500) {
				player.upgradeWeapon();; 
			}
		}
		else 
		if (isOverShowAnimButton && isChap2) {
			isChap2 = false;
			currNPCTarget.setTalking(false);
			player.setTalking(false);

			soundManager.stopSound("village");;
			soundManager.playSound("forest", true);

		}
		else
		if (isOverStopButton) {		
			enterBossCombat();
		}
		else
		if (isOverPauseButton) {		
			enterCombat();    	
		}
		else 
		if (isOverShowAnimButton) {
			isChap2 = true;				//Go to village
			soundManager.stopSound("forest");
			soundManager.playSound("village", true);
		}
		
		/* else
		if (isOverPauseAnimButton) {		// mouse click on Pause Anim button
			if (isAnimPaused) {
				isAnimPaused = false;
				animation.playSound();
			}
			else {
				isAnimPaused = true;	// toggle pausing
				animation.stopSound();
			}
		}
		else if (isOverQuitButton) {		// mouse click on Quit button
			isRunning = false;		// set running to false to terminate
		} */
  	}


	/* This method checks to see if the mouse is currently moving over one of
	   the buttons (Pause, Stop, Show Anim, Pause Anim, and Quit). It sets a
	   boolean value which will cause the button to be displayed accordingly.
	*/

	

	private void testMouseMove(int x, int y) { 
		if (isRunning) {
			isOverPauseButton = pauseButtonArea.contains(x,y) ? true : false;
			isOverStopButton = stopButtonArea.contains(x,y) ? true : false;
			isOverShowAnimButton = showAnimButtonArea.contains(x,y) ? true : false;
			isOverPauseAnimButton = pauseAnimButtonArea.contains(x,y) ? true : false;
			isOverQuitButton = quitButtonArea.contains(x,y) ? true : false;
		}
	}

	private void debug() {
		System.out.println("hi");
	}

	public void removeEnemy(Enemy enemy) {
		try {
			enemies.remove(enemy);

			if (enemies.size() >= 1) {
				currTarget = enemies.get(0);
			}
			else 
			{
				isCombat = false;
				soundManager.stopSound("combat");
				soundManager.playSound("forest", true);

				player.setAction("");
			}
		} catch (Exception e) {
			//TODO: handle exception
		}
	}

	

	private void spawnEnemies() {
		Random rand = new Random();

        int i = rand.nextInt(3);

		

        if (i == 1) {
            enemies.add(new Enemy(this, player, 600, 200));

			currTarget = enemies.get(0);
        }
        else if (i == 2) {
            enemies.add(new Enemy(this, player, 600, 200));
			enemies.add(new Enemy(this, player, 450, 400));

			currTarget = enemies.get(0);
        } else {
            enemies.add(new Enemy(this, player, 900, 200));
			enemies.add(new Enemy(this, player, 750, 400));
			enemies.add(new Enemy(this, player, 900, 600));

			currTarget = enemies.get(0);
        }

		
	}

	private void spawnBoss() {
		enemies.add(new Enemy(this, player));

		currTarget = enemies.get(0);
	}


	public void attack() {
		player.attack(currTarget);
		soundManager.playSound("attack", false);

		for (Enemy enemy : enemies) {
			enemy.act();
		}
	}

	public void flee() {
		
		enemies = new ArrayList<>();
		
		isCombat = false;

		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		String command = e.getActionCommand();

		/* if (command.equals(CM.getAString())) {
			attack();
		}

		if (command.equals(CM.getItemString())) {
			attack();
		}

		if (command.equals(CM.getFleeString())) {
			attack();
		} */
	}

	public void gameOver() {
		soundManager.stopSound("combat");
		soundManager.stopSound("forest");
		soundManager.playSound("gameOver", false);
		isGameOver = true;
	}

	public void addLoot(Item i){
		items.add(i);
	}

	private void enterCombat(){
		soundManager.stopSound("forest");
		soundManager.playSound("combat", true);
		player.setX(300);
		player.setY(400);

		spawnEnemies();

		CM.setVisible(true);

		isCombat = true;

		items = new ArrayList<>();
	}

	private void enterBossCombat(){
		soundManager.stopSound("forest");
		soundManager.stopSound("village");
		soundManager.playSound("combat", true);
		player.setX(300);
		player.setY(400);

		spawnBoss();

		CM.setVisible(true);

		isCombat = true;

		items = new ArrayList<>();

	}


	private void randomCombat() {
		Random rand = new Random();

		int i = rand.nextInt(3);

		if (i == 1) {
			enterCombat();
		}
	}

	public void setWon(boolean isWon) {
		this.isWon = isWon;
	}

	public void cycleTargets() {
		currNPCTarget.setTalking(false);
		player.setTalking(false);
		
		if (npcs.get(0).equals(currNPCTarget)) {			
			currNPCTarget = npcs.get(1);
		} else if (npcs.get(1).equals(currNPCTarget)) {
			currNPCTarget = npcs.get(2);
		} else {
			currNPCTarget = npcs.get(0);
		}
		
		
	}

	public void cycleEnemies() {
		
		if (enemies.size() > 1) {
			if (enemies.get(0).equals(currNPCTarget)) {
				currNPCTarget = npcs.get(1);
			} else if (npcs.get(1).equals(currNPCTarget)) {
				currNPCTarget = npcs.get(2);
			} else {
				currNPCTarget = npcs.get(0);
			}
		}
		
		
	}

	public void summonEnemies() {
		if (enemies.size() < 2) {
			enemies.add(new Enemy(this, player, 600, 200));

			currTarget = enemies.get(1);
		}
		else if (enemies.size() < 3) {
			enemies.add(new Enemy(this, player, 900, 600));

			currTarget = enemies.get(1);
		}
	}


}