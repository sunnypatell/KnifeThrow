package com.knifethrow.game;

/**
Copyright (C) May 2022 {Sunny Patel} <{sunnypatel124555@gmail.com}>

This file is part of the {KnifeThrow} project.

The {KnifeThrow} project can not be copied, distributed, and/or modified without the express
permission of {Sunny Patel} <{sunnypatel124555@gmail.com}>.
/*

/**
 * KnifeThrow.java
 * @author Sunny Patel
 * Final Summative: 2D Knife Throwing Mini-Game // Arcade Style (Java Graphics Jswing)
 * May 25, 2022
 * @version Java (SE 11)
 */


 											/**GAME IDEAS TO IMPLEMENT (Only initial ideas, not final before starting this ICS4U Project)
  - OOP (Object Oriented Programming).
  - A menu screen.
  - Welcome and Game Over Screen.
  - Game sounds for hitting targets, knife throwing, menu sound or jumping sound (colission with wall or floor), game over sound etc.
  - High Score System(cool design effect where the higher your score gets the colour turns red)? (score shakes when target is hit, particles come from score at top and target both... or entire screen shakes, try and experiment).
    High score made using File.I/O to demonstrate in class learning. File I/O and .txt.
  - Creating Own sprites and animations.
  - Colission System. *
  - Eventually gold coloured targets will appear (for shorter time than regular targets) randomly, but hitting these are worth 3 points instead of 1.
  - Different knives have different shapes and perks (some knives throw straight while others may throw at a curve).
  - If time allows, a trajectory can be made (enabled or disabled from game settings).
  - As the score increases, eventually, a super power type knife ability will shoot knifes in all directions etc. (if time is enough, different knives will have different abilities).
  - There will be a knife shop in game, knives can be unlocked by reaching high scores, the final knife to unlock will be hard as you will need to reach higher scores.
  - Track highscores (ask the user for a Player name so it shows beside the highscores after the game ends), Unlocked Knives, and settings with File I/O.
  - When game ends display your score, the top 5 high score(s)... etc. With the player names. The top high score on the leaderboard should be in Green colour.
   ^^^ This is just to make the game seem like a regular game as much as possible.

---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
*/

// Imports for java.awt.* are the imports for the Abstract Window Toolkit.
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

// Imports for Java.io.* used for reading and writing information (The input output stream).
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

// Imports for Java.util.* used for the utilities.
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

// Imports for javax.sound.* used for reading sound clips.
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;

// Imports for ImageIO.
import javax.imageio.ImageIO;

// More imports for sound implementations.
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException; // UnsupportedAudioFileException Import.

// Imports for javax.swing.* used for building some of the features and creating some of the GUI of our game.
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 * Legacy Knife Throw entry point extracted from the 2022 monolithic source file.
 * <p>This class keeps the original logic intact while the 2024 refactor simply
 * relocates the nested types into their own compilation units.</p>
 */
public class knifeThrow {
	
	/**
	 * This is the parent class. It contains the main method and some global variables.
	 */

	static JFrame f = new JFrame(); // This is the JFrame.
	static Dimension size = Toolkit.getDefaultToolkit().getScreenSize(); // These are the dimensions of the screen
	static final double speed = 3; // speed of the horizontal movement
	static final double frictionForce = 0.8; // Friction of the ground (Character Running Speed.)
	static final double jumpS = 5; // jump speed
	static final double jumpheight = 20; // jump height
	static final double kspeed = 5; // speed of the knives
	static final double aF = 8000; // number of milliseconds the targets appear for
	static final double rT = 1000; // number of milliseconds of time between when one target disappears and other appears.
	static final double mV = 10; // max velocity of the movement of the player
	static final int maxTargetsOnScreen = 1; //maximum number of targets present on the screen at once.
	static final double rTFK = 5000; // number of milliseconds in between the spawning of two consecutive knives.
	static final Dimension knifSize = new Dimension(50,20); // size of the knives
	static final double h = 5; // no. of hits the player can take (HP of player)
	static final double eKS = 2; // speed of enemy knives
	static final double eKMS = 4; // maximum speed of the enemy knives (changing this value won't change anything in the game as it's linked to other logic as of now)
	static final int empl = 25; // points to score to fill the emp meter.
	static final double empTimeL = 1500; // time in milliseconds for which the emp lasts
	static final int enemKnifAppearScore = 0; // The score at which the enemy knives start appearing.
	static final int knifScoreIntervals = 50; // The intervals of the price score of the knives in the knife shop.
	
	static ArrayList<String> players = new ArrayList<String>(); // The names of all the players who have played the game.
	static String cplayer; // The name of the current player playing the game.
	static ArrayList<Integer> bestScores = new ArrayList<Integer>(); // The best scores of all the players who have played the game.
	static ArrayList<Boolean> wasds = new ArrayList<Boolean>(); // The controls selected by the players stored in the data.txt file
	static ArrayList<Integer> selectedKnives = new ArrayList<>(); // The knives selected by the players stored in the data.txt file.
	static String contents; // All of the data stored in the data.txt file.
	static ArrayList<JButton> cOptions = new ArrayList<JButton>(); // The option buttons that appear after the control button is pressed.
	static JButton controls; // The button to select controls (This is made static since it is present in the startGame class and the panel class.
	
	static Point offset = new Point(0,0); // The offset of the graphics in the main game window.
	static double shakeIntensity = 20; // The intensity of screen shake.
	
	static int selectedKnifeIdx = 0; // The index of the selected knife sprite of a player.
	static ArrayList<BufferedImage> knifeS = new ArrayList<>(); //The sprites for the knives.
	static ArrayList<String> KnifeNames = new ArrayList<>(); // The names of the knives in the knife shop.
	static ArrayList<Point> knifeOffsets = new ArrayList<>(); // The offsets of the knives present in the knife shop, this is different for each knife since each knife has different dimensions.
	
	static Font font; // The loaded Pixelated Regular font.
	static boolean newP = false; // This boolean contains whether the player is a new player or not.
	
	static boolean wasd = true; // The boolean containing whether the player has selected a, d, space for controls or not.
	
	static Clip gClip; // The background music of the main game.
	static Clip mmClip; // The background music of the menu.
	
	static int fps = 0; // The frame rate of the program.
	static int total = 0; // The total frames displayed since the last time frame rate was checked.
	static long prevChecked = System.currentTimeMillis(); // The last time frame rate was checked.
	
	
	static panel p; // The main game panel.
	
	static startGame sg; // The main menu and start game menu panel.

	/**
	 * The main method.
	 * @param args Arguments.
	 */
	public static void main(String[] args) {
		//If data.txt file does not exist, create one.
		try {
			File f = new File("data.txt");
			if(!f.exists()) f.createNewFile(); // If a data.txt file does not exist, create one.
			font = Font.createFont(Font.TRUETYPE_FONT, new File("pixelated.ttf")); // Use the pixelated.ttf font file.
		}catch(Exception e) {e.printStackTrace();}
		
		// Loading all the knife sprites form the sprites folder.
		try {
			int j = 1; // j starts from one since the images' name start from 1.
			while(true) {
				for(int i = 1; i<3; i++) {
					knifeS.add(ImageIO.read(new File("sprites/testK"+j+""+i+".png")));
				}
				j++;
			}
		}catch(Exception e) {}
		
		// Loading and reading the data.txt file and configuration.txt file.
		try {
			File tr = new File("data.txt"); // Loading the data.txt file
			
			BufferedReader br = new BufferedReader(new FileReader(tr)); // Reader to read the data file.
			String str = br.readLine(); // The string contents in the data.txt file.
			if(str==null)str="";
			contents = str;
			
			String[] parts = str.split(" "); // Parts of the content are seperated.
			for(int i = 3; i<parts.length; i+=4) {
				if(!parts[i].equals("")) {
					String name = parts[i-3]; // The name of the player stored in the file.
					if(name.contains("_")) { // If the name has a space (denoted by the presence of '_' character) Then this character is replaced by a space.
						String[] nameParts = name.split("_");
						name = "";
						for(int j = 0; j<nameParts.length; j++) {
							String toAdd = " ";
							if(j==nameParts.length-1) toAdd = "";
							name+=nameParts[j]+toAdd;
						}
					}
					
					boolean ta = true; // The loaded wasd boolean.
					if(parts[i-1].equals("N"))ta = false;
					
					// Adding the extracted data to their respective array lists.
					players.add(name); // Adding the name of the player.
					bestScores.add(Integer.parseInt(parts[i-2])); // Adding the bestScore of the player.
					wasds.add(ta); // Adding the controls selected by the player.
					selectedKnives.add(Integer.parseInt(parts[i]));	// Adding the knife selected by the player.
				}
			}
			br.close(); // The bufferedReader is closed.
			
			File cnfig = new File("configuration.txt"); // The configuration file.
			Scanner s = new Scanner(cnfig); // This file is read using a scanner.
			
			for(int i = 0; i<knifeS.size()/2; i++) {
				
				//Refining the data extracted from the document.
				String cnt = s.nextLine();
				String[] cntParts = cnt.split(" ");
				String[] nameParts = cntParts[0].split("_");
				String kname = nameParts[0];
				if(nameParts.length>1) kname += " "+nameParts[1];
				
				// Adding the data extracted from the document into their respective array lists.
				KnifeNames.add(kname);
				knifeOffsets.add(new Point(Integer.parseInt(cntParts[1]), Integer.parseInt(cntParts[2])));
			}
			
			s.close();
		}catch(Exception e) {
			System.out.println("err");
		}
		
		// Initializing the JFrame
		f.setSize(size.width, size.height); //Setting the size.
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Setting up so that on clicking on the cross button, the application closes.
		f.setUndecorated(true); // Removes the boundary of the application.
		f.setFocusable(true);
		
		//Adding key listener
		f.requestFocus();
		f.addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyPressed(KeyEvent arg0) {
				if( (p!=null) ) {
					int key = arg0.getKeyCode();
					// Move the character right if the dedicated keybind controls set are pressed.
					if( (key==68 && wasd) || (key==KeyEvent.VK_RIGHT && !wasd) ) {
						p.movingRight = true;
						p.p.dir = -1;
					}
					// Move the character left if the dedicated keybind controls set are pressed.
					if( (key==65 && wasd) || (key==KeyEvent.VK_LEFT && !wasd) ){
						p.movingLeft = true;
						p.p.dir = 1;
					}
					// If the character jumps against a collision platform, jumping is true and play the jumping sfx.
					if( ( (key==32 && wasd) || (key==KeyEvent.VK_UP && !wasd) ) && (p.p.yCollided) ) {
						p.jumping = true;
						play("sounds/jump.wav"); // The jump sound effect is played.
					}else if( (key==32 && wasd) || (key==KeyEvent.VK_UP && !wasd) ) {
						if( (p.p.xCollided) ) { // Checks if the player has collided with a wall to wall jump from.
							play("sounds/jump.wav"); // PLays the jump sound effect.
							p.walljump = true;
						}
					}
					// Increasing the gravity if the dedicated key bind control set for down is pressed.
					if( (key==KeyEvent.VK_S && wasd) || (key==KeyEvent.VK_DOWN && !wasd) ) {
						p.p.gravity *= 5;
					}
					//Activating the emp is the 'F' key is pressed and the emp is available.
					if( ( (key==KeyEvent.VK_F && wasd) ) && (p.empav) ) {
						p.emp = true;
					}
					// If the escape key is pressed mid-game, bring up the pause menu and play the pause sound.
					if(key==KeyEvent.VK_ESCAPE && !p.gameOver) {
						p.t.stop();
						p.pause = true;
						p.repaint();
						gClip.stop();
						play("sounds/pause.wav");
					}
				}
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				if(p!=null) {
					int key = arg0.getKeyCode();
					// If the dedicated key bind control set for moving right is released, moving right is set to false;
					if( (key==68 && wasd) || (key==KeyEvent.VK_RIGHT && !wasd) ) {
						p.movingRight = false;
						p.p.running = false;
						p.p.xa = 0;
					}
					// If the dedicated key bind control set for moving left is released, moving left is set to false;
					if( (key==65 && wasd) || (key==KeyEvent.VK_LEFT && !wasd) ){
						p.movingLeft = false;
						p.p.running = false;
						p.p.xa = 0;
					}
				}
			}
		});
		
		sg = new startGame(true, true); // Initializing the start game menu panel
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				f.setContentPane(sg); // The content pane is set to start game.
				f.getContentPane().setBackground(new Color(245,245,245)); // The background is set.
				f.setVisible(true); // The frame is set visible.
			}
		});
	}
	
	
	/** 
	 * This method initializes the button icons of a given button in a particular style.
	 * 
	 * @param name The text to be displayed on the button.
	 * @param newPlayer The button to set the icons of.
	 * @param invert Boolean that contains whether the color scheme is to be inverted or not.
	 * @param border Boolean that contains whether the icon needs a border.
	 */
	public static void initialize(String name, JButton newPlayer, boolean invert, boolean border) {
		// Dimensions of the button.
		int npw = newPlayer.getWidth();
		int nph = newPlayer.getHeight();
		
		//Creating the icon image.
		
		// The text buffered image.
		BufferedImage ic = new BufferedImage(npw,nph,BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = ic.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		// If border is enabled, then it is drawn to the image
		if(border) {
			g.setColor(new Color(50,50,50));
			// The color is inverted if invert is true.
			if(invert)g.setColor(new Color(205,205,205));
			g.setStroke(new BasicStroke(5));
			g.draw(new Rectangle2D.Double(2.5,2.5,ic.getWidth()-5,ic.getHeight()-5)); // The border is drawn
		}
		
		// Drawing the string.
		g.setColor(new Color(50,50,50));
		// Inverting color if necessary
		if(invert)g.setColor(new Color(225,225,225));
		g.setFont(new Font("Arial Black", Font.BOLD, nph-5));
		drawString(new Rectangle2D.Double(0,0,npw,nph), name, -1, g);
		
		// The button icon image.
		BufferedImage icon = new BufferedImage(npw,nph,BufferedImage.TYPE_INT_ARGB);
		Graphics2D gicon = icon.createGraphics();
		
		// Background is filled of the icon image and the text buffered image is drawn on top of it.
		gicon.setColor(Color.white);
		if(invert) gicon.setColor(new Color(50,50,50));
		gicon.fillRect(0, 0, icon.getWidth(), icon.getHeight());
		gicon.drawImage(ic, 0, 0, null);
		
		// The rolled over icon, or the icon which is seen when the mouse hovers over the button.
		BufferedImage icr = new BufferedImage(npw,nph,BufferedImage.TYPE_INT_ARGB);
		Graphics2D gicr = icr.createGraphics();
		
		// Filling the background and the text buffered image.
		gicr.setColor(new Color(225,225,225));
		if(invert) gicr.setColor(new Color(0,0,0));
		gicr.fillRect(0, 0, icon.getWidth(), icon.getHeight());
		gicr.drawImage(ic, 0, 0, null);
		
		// Setting the icons to the buttons.
		newPlayer.setRolloverEnabled(true);
		newPlayer.setRolloverIcon(new ImageIcon(icr));
		newPlayer.setIcon(new ImageIcon(icon));
		newPlayer.setDisabledIcon(new ImageIcon(icon));
	}
	
	
	/** 
	 * This method draws a glowing rectangle on the screen.
	 * 
	 * @param x The x coordinate of the rectangle
	 * @param y The y coordinate of the rectangle
	 * @param height The height of the rectangle
	 * @param width The width of the rectangle 
	 * @param g The Graphics2D object to draw the graphics on the screen
	 */
	public static void glowRect(double x, double y, double height, double width, Graphics2D g) {
		double glowRadius = 1; // The glow radius.
		double radius = 100; // The radius till which the glow can spread.
		for(int i = (int)0; i<radius; i+=glowRadius) {
			try {
				// The color is set so that the alpha is reduced by some amount each iteration.
				g.setColor(new Color(g.getColor().getRed(), g.getColor().getGreen(), g.getColor().getBlue(), (int)(255/((i*(100/radius))+1))));
			}catch(Exception e) {
				
			}
			// The rectangle to be drawn is enlarged each iteration and is drawn.
			Rectangle2D toBeDrawn = new Rectangle2D.Double(x-i/glowRadius, y-i/glowRadius, width+ (2*i/glowRadius), height+ (2*i/glowRadius));
			g.draw(toBeDrawn);
		}
	}
	
	
	/** 
	 * Plays a sound from the provided file path.
	 * 
	 * @param filePath The provided file path
	 * @throws UnsupportedAudioFileException
	 * @throws IOException
	 * @Throws LineUnavaiableException
	 */
	public static void play(String filePath) {
		// This sound plays on a different background thread than on the single thread that the game runs on, thus, it won't lag when the game lags.
		try {
			// Audio input stream is loaded from the provided file path.
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filePath).getAbsoluteFile());
			Clip c = AudioSystem.getClip(); // The Clip is initialized.
			c.open(audioInputStream); // The audio input stream is loaded in the clip.
			c.start(); // The clip starts.
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {}
	}
	
	
	/** 
	 * This method loads music from a given file path into a clip.
	 * 
	 * @param filePath The provided file path.
	 * @return Clip The clip with the music loaded.
	 * @throws UnsupportedAudioFileException
	 * @throws IOException
	 * @throws LineUnavailableException
	 */
	public static Clip loadMusic(String filePath) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		// The audio input stream is loaded from the file path.	
		AudioInputStream ais = AudioSystem.getAudioInputStream(new File(filePath).getAbsoluteFile());
		Clip c = AudioSystem.getClip(); // The clip is initialized.
		c.open(ais); // The audio input stream is loaded onto the clip.
		c.loop(Clip.LOOP_CONTINUOUSLY); // The clip is said to loop continuously.
		return c;
	}
	
	/**
	 * This method saves the current data of players in the data.txt file.
	 */
	public static void save() {
		try {
			FileWriter bw = new FileWriter(new File("data.txt")); // The file writer which will be used to write the file.
			String tr = ""; // The string to write.
			
			// The data is saved by looping through each of the player and adding all its details to the tr string.
			for(int i = 0; i<players.size(); i++) {
				// The wasd text is initialized and changed according to the wasd.
				String wasd = "Y";
				if(!wasds.get(i)) wasd = "N";
				
				// The name is adjusted if it has a space, '_' replaces the space.
				String name = players.get(i);
				if(name.contains(" ")) {
					String[] nameParts = name.split(" "); //regex
					name = "";
					for(String n : nameParts) {
						if(!n.equals(""))name+=n+"_";
					}
				}
				
				// The information is added to the to write string.
				tr+=name+" "+bestScores.get(i)+" "+wasd+" "+selectedKnives.get(i)+" ";
			}
			contents = tr; // The contents of the file are updates.
			bw.write(tr); // The data file is written.
			bw.flush(); //flushed, cleared
			bw.close(); // The file writer is closed.
		}catch(Exception e) {e.printStackTrace();}
	}
	
	/**
	 * This method draws string fitting inside of a box with new lines.
	 * 
	 * @param s The string to display
	 * @param box The bounding rectangle in which the string is to be drawn in.
	 * @param g The Graphics2D object to draw string with.
	 */
	public static void drawString(String s, Rectangle2D box, Graphics2D g) {
		String[] lines = s.split("\n"); // The lines are separated by splitting the line along the "\n" character.
		
		int x = (int)box.getX(); // The initial x of the words.
		int n = 0; // The number of lines the string will take in to display on the screen.
		for(String str : lines)n+=str.length();
		n+=lines.length;
		
		// Placeholder jlabel to get the preferred size of a single character in the font.
		JLabel prw = new JLabel("a");
		prw.setFont(g.getFont());
		Dimension prefw = prw.getPreferredSize();
		
		n = (n*prefw.width)/(int)box.getWidth(); // n is calculated.
		int y = (int)(size.height/2 - (n*(g.getFont().getSize() + 10))/2); // The initial y is calculated.
		
		for(String str : lines) {
			String[] words = str.split(" "); // Each word is split from a line.
			for(String w : words) {
				// A jlabel is created to get the preferred size of the word.
				JLabel prx = new JLabel(w);
				prx.setFont(g.getFont());
				Dimension pref = prx.getPreferredSize();
				g.drawString(w, x, y); // The string is drawn.
				x+=pref.width+20; // x coordinate of the drawn string is increased so that the next word appears after the current word.
				if(x>box.getX()+box.getWidth()) {
					// If x is greater than the box's width, then x is set to its default value, and y is increased (to create a new line).
					x = (int)box.getX();
					y += pref.getHeight()+10;
				}
			}
			// When a line is fully drawn, x is set to default and y is increased to begin a new line.
			x = (int)box.getX();
			y += g.getFont().getSize()+10;
		}
	}

	/**
	 * This method draws a string in the middle of a bounding rectangle.
	 * @param b The bounding rectangle.
	 * @param a The string to draw.
	 * @param y The y position of the string.
	 * @param g The Graphics2D object to draw the string.
	 */
	public static void drawString(Rectangle2D b, String a, double y, Graphics2D g) {
		JLabel test = new JLabel(a); // A place holder jlabel to get the preferred size.
		test.setFont(g.getFont());
		double ytd = y;
		if(ytd==-1) {
			// If the y is given as -1, then it is also calculated in the middle of the bounding box.
			ytd = b.getY()+b.getHeight()/2 + g.getFont().getSize()/3;
		}
		// Drawing the string with the graphics object.
		g.drawString(a, (int)(b.getX()+b.getWidth()/2-test.getPreferredSize().width/2), (int)(ytd));
	}

	/**
	 * Draws the icons of a given array of components.
	 * @param g Graphics2D object to draw the graphics on the screen
	 * @param cs The array of components whose icon is to be drawn.
	 */
	public static void draw(Graphics2D g, JButton[] cs) {
		// Loops through each of the provided jbutton and draws their icon image on the screen.
		for(JButton b : cs) {
			g.drawImage(((ImageIcon)b.getIcon()).getImage(), b.getX(), b.getY(), null);
		}
		
	}
	
}
