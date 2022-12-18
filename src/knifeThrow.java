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
   -------------- This project was started and finished solely for the purpose of the RHHS ICS4U Final Project Summative in the provided project timeframe --------------
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
	
	static class tile {

		/**
		 * This class handles all the physical and interactive objects in the game including the player, the knives, targets and ground.
		 */
		
		double x; // x coordinate of the player's position
		double y; // y coordinate of the player's position
		double hitboxWidth; // width of the player's hit box
		double hitboxHeight; // height of the player's hit box
		
		boolean yCollided = false; // Becomes true when the player collides with something along the y axis
		boolean xCollided = false; // Becomes true when the player collides with something along the x axis
		boolean collided = false; // Becomes true when the player collides with something.
		int dir = -1; // The horizontal direction of the player's movement. Right = 1, Left = -1.
		
		double maxXV = mV; // The max limit of the horizontal velocity.
		double maxYV = 20; // The max limit of the vertical velocity.
		
		double gravity = 0.5; // The acceleration due to gravity of the game.
		
		double yv = 0; // The velocity of the object in the vertical direction or the y component of the velocity vector.
		double ya = 0; // The acceleration of the object in the vertical direction.
		
		double xv = 0; // The velocity of the object in the horizontal direction, or the x component of the velocity vector.
		double xa = 0; // The acceleration of the object in the horizontal direction/
		
		boolean down = false; // This turns true when the object collides with something which is above.
		boolean movable; // This determines if the object is movable or not.
		double xafterCol = 0; // This is the position of the object after it has collided with something/
		
		tile colTile; // The tile with which the object has collided with
		
		Color c = new Color(205,205,205); // The color of the object
		
		BufferedImage sprite; // The sprite of the object
		boolean repeat; // This determines whether the sprite tiles or not.
		
		/**
		 * This is the constructor of the tile class
		 * 
		 * @param x The x coordinate
		 * @param y The y coordinate
		 * @param width The width of the hitbox
		 * @param height The height of the hitbox
		 * @param ismovable Boolean that stores whether the object is movable or not.
		 */
		public tile(double x, double y, double width, double height, boolean ismovable) {
			this.x = x;
			this.y = y;
			hitboxWidth = width;
			hitboxHeight = height;
			movable = ismovable;
		}
		
		/**
		 * This method sets the sprite image of the object.
		 * 
		 * @param i The image with which the sprite image is to be set.
		 * @param tileup Boolean containing whether the sprite will tile or not.
		 */
		public void setImage(BufferedImage i, boolean tileup) {
			sprite = i;
			repeat = tileup;
		}
		
		/**
		 * This method updates the object position based on collisions, acceleration, velocity etc.
		 * 
		 * @param platforms The tiles present in the game with which the object can collide with.
		 */
		public void update(ArrayList<tile> platforms) {
			
			if(movable) {
				collided = false;
				
				//handling y collision
				ya = gravity; // First the y acceleration is set to gravity
				if(yv<maxYV) { 
					yv+=ya; // If the yv is less than the max y velocity that can be achieved by the object, y velocity is added by the y acceleration value.
				}
				
				// Checking and handling collision along the vertical axis.
				tile toTest = new tile(x,y+yv,hitboxHeight,hitboxWidth,true); // A placeholder tile looking one step into the future. This tile is used to test for collision.
				toTest.yv = yv;
				tile[] future = toTest.collide(platforms);
				
				yCollided = false;
				if(future[0]!=null) { // This means that the object has collided with something along the vertical axis.
					this.collided = true;
					colTile = future[0];
					if(future[0].down) { //If the object has hit the platform from underneath.
						yv = 0;
						y=future[0].y+future[0].hitboxHeight+0.1; // The y of the object is set below the platform.
					}else {
						if(yv>0) yv = future[0].y-(y+hitboxHeight); // The y velocity is set so if it is added to the y, the object comes at the top of the platform.
						yCollided = true;
					}
				}
				y+=yv; // The y velocity is added to the y of the object.
				
				//handling x collision
				tile[] collided = collide(platforms);
				
				if(collided[1]!=null) { // Means if there is any platform with which the object has collided with in the horizontal direction.
					x = collided[1].xafterCol; // The x of the object is set to where it should be after the collision.
					xCollided = true;
					xv = 0;
				}else { // If there is no collision along the horizontal axis.
					if((xv)!=0) {
						double friction = 0.5/0.8 * frictionForce; // The friction force is set.
						if(yCollided) {
							friction = frictionForce; // If the object is on the ground or, it is collided along the y axis, the friction is increased.
						}
						double tempxv = xv + (-Math.abs(xv)/xv)*friction; // The x velocity of the object after friction force is added.
					
						// Checks if the direction of the stored x velocity is the opposite of the x velocity of the object, in such a situation the object should stop moving.
						if( (Math.abs(tempxv)/tempxv ) == (-Math.abs(xv)/xv) ) { 
							xv = 0;
						}else {
							xv = tempxv;
						}
					
					}else {
						xv=0;
					}
				
					//Checking if the x velocity is not greater than the max x velocity.
					if(xv<maxXV && xv>-maxXV) {
						xv+=xa;
					}
					x+=xv; // Changing the position of the object according to the x velocity.
					
					tile[] f = collide(platforms); // Checks one step into the future to see if the object has collided with anything.
					if(f[1]!=null) {
						x=f[1].xafterCol;
					}
				}
			}
		}
		
		/**
		 * This method checks for collisions along both the axis and helps in handling them as well.
		 * 
		 * @param platforms The platforms with which the object can collide with.
		 * @return Returns a tile array containing two tile objects, with which the object has collided with along with the y axis and the x axis respectively.
		 */
		public tile[] collide(ArrayList<tile> platforms) {
			
			//Initializing the return objects
			tile collidedy = null;
			tile collidedx = null;
			xCollided = false;
			
			for(tile p : platforms) {
				// Checking for collisions along the y axis.
				
				// This statement checks the intersection of the objects along the x axis.
				if( ( (x>=p.x) || (x+hitboxWidth>p.x) ) && ( (x<p.x+p.hitboxWidth) || (x+hitboxWidth<=p.x+p.hitboxWidth) ) ) {
					
					// This statement checks collision below the object by checking if the object is inside the platform and its y velocity was in the downward direction, plus some extra conditions for safety.
					if( (y+hitboxHeight>=p.y-0.1) &&  (Math.abs(yv)/yv>0) && (y<=p.y) ) {
						if(collidedy==null) {
							collidedy = p;
							collidedy.down = false;
						}
						yCollided = true;
					}
					// This statement checks collision above the object by checking if the object is inside the platform and its y velocity in the upward direction, plus some extra statments for safety.
					else if( (y>p.y) && (Math.abs(yv)/yv<0) && (y<=p.y+p.hitboxHeight) ) {
						if(collidedy==null) {
							collided = true;
							collidedy = p;
							collidedy.down = true;
						}
					}
				}
				
				// Checking collision along the x axis.
				
				// This statement checks the intersection of objects along the y axis.
				if( ( (y>p.y) || (y+hitboxHeight>p.y) ) && ( (y<p.y+p.hitboxHeight) || (y+hitboxHeight<=p.y+p.hitboxHeight) ) ) {
					
					// This statement checks the collision of object to its left, by checking if the object intersects another tile with its x velocity towards the left.
					if( (x<=p.x+p.hitboxWidth) && (Math.abs(xv)/xv<0) && ((x-xv)>p.x+p.hitboxWidth) ){
						if(collidedx==null) {
							collidedx = p;
							collidedx.xafterCol = collidedx.x+collidedx.hitboxWidth+0.1; // This is the x coordinate of the object after collision.
						}
					}
					
					// This statement checks the collision of object to its right, by checking if the object intersects another tile with its x velocity towards left.
					if( (x+hitboxWidth>=p.x) && (x+2<p.x+p.hitboxWidth) ) {
						if(collidedx==null) {
							collidedx = p;
							collidedx.xafterCol = collidedx.x-hitboxWidth-0.1; // This is the x coordinate of the object after collision.
						}
					}
					
					// This statement checks what both the above statement check, but with more room for errors, this statement is present to set all booleans or variables that indicate x collision of the object.
					if( ( (x-5<=p.x+p.hitboxWidth+5) && ((x-xv)>p.x+p.hitboxWidth)) || ( (x+5+hitboxWidth>=p.x) && (x+2<p.x+p.hitboxWidth) ) ){
						colTile = collidedx;
						collided = true;
						xCollided = true;
					}
				}
			}
			tile[] ret = {collidedy, collidedx};
			return ret;
		}
		
		/**
		 * This method is used to set the color of the object.
		 * 
		 * @param c The desired color of the object.
		 */
		public void setColor(Color c) {
			this.c = c;
		}
		
		/**
		 * This method is used to display/render the object on the screen.
		 * 
		 * @param g The Graphics2D object which is used to draw the object onto the screen.
		 */
		public void show(Graphics2D g) {
			
			//Checks if a sprite is not set for the object, if so, draws a rectangle of the size of the object's hitbox with the set color.
			if(sprite==null) {
				g.setColor(c);
				g.setStroke(new BasicStroke(3));
				g.draw(new Rectangle2D.Double(x+offset.x,y+offset.y,hitboxWidth,hitboxHeight));
			}
			else {
				// If a sprite is set, then it checks if the sprite is to be tiled or not and renders it accordingly.
				if(!repeat) {
					int v1 = (int)Math.floor(hitboxWidth/hitboxHeight);
					int v2 = (int)Math.floor(sprite.getWidth()/sprite.getHeight());
					if(v1>1)v1=1;
					if(v2>1)v2=1;
					if(v1==v2) {
						g.drawImage(sprite, (int)x+offset.x, (int)y+offset.y, (int)hitboxWidth, (int)hitboxHeight, null);
					}
				}else {
					if(sprite.getWidth()<hitboxWidth) {
						for(int i = 0; i<5; i++) {
							g.drawImage(sprite, (int)(i*hitboxWidth/5) + offset.x, (int)y + offset.y, (int)hitboxWidth/5, (int)(sprite.getHeight()/(sprite.getWidth()/(hitboxWidth/5))), null);
						}
					}
				}
			}
		}
		
		/**
		 * This is another method which can be used to display/render the tile object on the screen.
		 * 
		 * @param g
		 */
		public void fill(Graphics2D g) {
			//Checks if a sprite is set for the object or not and renders accordingly.
			if(sprite==null) {
				//If a sprite is not set, then a rectangle is filled of the size of the object's hitbox with the color of the object.
				g.setColor(c);
				g.fill(new Rectangle2D.Double(x+offset.x,y+offset.y,hitboxWidth,hitboxHeight));
			}else {
				//If a sprite is set, then the sprite is rendered if the asect ratio of both the object's hitbox and the sprite is the same.
				int v1 = (int)Math.floor(hitboxWidth/hitboxHeight);
				int v2 = (int)Math.floor(sprite.getWidth()/sprite.getHeight());
				if(v1>1)v1=1;
				if(v2>1)v2=1;
				if(v1==v2) {
					g.drawImage(sprite, (int)x+offset.x, (int)(y+(hitboxHeight*4/5))+offset.y, (int)hitboxWidth, (int)-hitboxHeight, null);
				}
			}
		}
		
		/**
		 * This method returns a rectangle2D object extacly as the hitbox of the object.
		 * 
		 * @return The rectangle2D object.
		 */
		public Rectangle2D getRect() {
			return new Rectangle2D.Double(x,y,hitboxWidth,hitboxHeight);
		}
		
		/**
		 * This method adds the object into a given arrayList of tiles.
		 * 
		 * @param t The array list in which the object is to be added
		 * @return The arrayList of tiles with the object added.
		 */
		public ArrayList<tile> addMe(ArrayList<tile> t){
			t.add(this);
			return t;
		}
	}
	
	static class knife extends tile{
		
		/**
		 * This sub-class of tile which contains some exclusive methods and variables.
		 */
		
		long deathTime; //The time when a knife collides with something.
		boolean flipped = false; // This stores whether the knife has been affected by emp or not.
		double damage = 1; // The damage caused by the enemy knife
		boolean valid = true; // Whether the knife is valid to hit a target or player.
		boolean goingDown = false; // This determines whether the knife was moving downward or not.
		boolean isEnemy = false; // This boolean keeps track that if the knife is one thrown by the player or an enemy knife.
		
		/**
		 * This is the constructor of this class having all the arguments same as the tile class plus a isEnemy boolean.
		 * 
		 * @param x X position of the knife.
		 * @param y Y position of the knife.
		 * @param width Width of the hitbox of the knife.
		 * @param height Height of the hitbox of the knife.
		 * @param isEnemy Boolean that contains if the knife is an enemy knife or one thrown by the player.
		 */
		public knife(double x, double y, double width, double height, boolean isEnemy) {
			super(x,y,width,height,true);
			this.isEnemy = isEnemy;
		}
		
		/**
		 * This is the update method overridden from the tile class to include some changes exclusive to knives.
		 * 
		 * @param platforms This is the array list of objects with which the knife can collide with.
		 */
		@Override
		public void update(ArrayList<tile> platforms) {
			
			//This statement checks if the direction of movement and the dimensions of the knife hitbox match or not. If not it is corrected.
			if( (dir==2) && (valid) ) { // If the knife is moving upwards but it is facing right or left.
				if(hitboxHeight<hitboxWidth) {
					hitboxHeight = knifSize.width;
					hitboxWidth = knifSize.height;
					x += knifSize.width-knifSize.height;
					y += knifSize.height-knifSize.width;
				}
			}
			
			if( (dir<2) && (valid) ) { // If the knife is moving left or right but it is facing up.
				if(hitboxHeight>hitboxWidth) {
					hitboxHeight = knifSize.height;
					hitboxWidth = knifSize.width;
					y += knifSize.width-knifSize.height;
					x += knifSize.height-knifSize.width;
				}
			}
			
			// The rest is the same as the update method in tile class
			if(movable) {
				
				if(dir==2) {
					ya = gravity;
					tile[] collided = collide(platforms);
					if(collided[0]!=null) {
						this.collided = true;
						colTile = collided[0];
						if(collided[0].down) {
							yv = 0;
							y=collided[0].y+collided[0].hitboxHeight+0.1;
						}else {
							if(yv>0) yv = collided[0].y-(y+hitboxHeight);
							yCollided = true;
						}
					}else {
						if( (yv<maxYV) && (yv>-maxYV) ) {
							yv+=ya;
						}
						y+=yv;
					}
				}
				
				if(dir<2) {
				//handling x collision
				tile[] collided = collide(platforms);
				if(collided[1]!=null) {
					x = collided[1].xafterCol;
					xv = 0;
				}else {
					if((xv)!=0) {
						double friction = 0.5/0.8 * frictionForce;
						if(yCollided) {
							friction = frictionForce;
						}
						double tempxv = xv + (-Math.abs(xv)/xv)*friction;
						if(Math.abs(tempxv)/tempxv == -Math.abs(xv)/xv) {
							xv = 0;
						}else {
							xv = tempxv;
						}
					}else {
						xv=0;
					}
					
					if( (xv<maxXV) && (xv>-maxXV) ) {
						xv+=xa;
					}
					x+=xv;
					tile[] f = collide(platforms);
					if(f[1]!=null) {
						x=f[1].xafterCol;
					}
				}
				}
			}
		}
		
		/**
		 * This method renders the knife sprite onto the screen.
		 * 
		 * @param g This is the graphics2D object used to draw the sprite on the screen.
		 */
		@Override
		public void fill(Graphics2D g) {
			//Index of the enemy knife sprite is 0, so by default the index to be used is set to 0.
			int idxToUse = 0;
			// But if the knife is not an enemy knife, the index to be used is set to the selected knife sprite by the player in the knife shop.
			if(!isEnemy) idxToUse = selectedKnifeIdx; 
			
			BufferedImage td = knifeS.get((idxToUse*2)+1); // The to draw sprite is set to the left image by default.
			if(dir==2) {td = knifeS.get((idxToUse*2)+0);} // If the knife is moving upwards, it is set to the up sprite, loaded from the sprites folder.
			 
			// The coordinates and details of the to draw sprite.
			int tdx = (int)x; // The x coordinate to draw the image at.
			int tdy = (int)y; // The y coordinate to draw the image at.
			int tdw = (int)hitboxWidth; // The width of the drawn image.
			int tdh = (int)hitboxHeight; // The height of the drawn image.
			
			if(dir==1) {
				// If the knife is facing to the right, then the drawn sprite is flipped since the stored image is facing to the left.
				tdx+=tdw;
				tdw=-tdw;
			}
			if(dir==2) {
				if(yv>0 || goingDown) {
					// If the knife is going down then the sprite is flipped along the x axis, since the stored image is facing upwards.
					goingDown = true;
					tdy+=tdh;
					tdh=-tdh;
				}
			}
			
			Point off = knifeOffsets.get(selectedKnifeIdx); // This is the offset for different knives to make them not look wierd by drawing them in the same dimensions since the different knife sprites have different dimensions.
			int offsetx = off.x;
			int offsety = off.y;
			if(isEnemy) { 
				// If the knife is an enemy knife then their knife is the default kunai knife whose offset is 0,0.
				offsety = 0;
				offsetx = 0;
			}
			
			if(dir==2) {
				// If the knife is moving in the vertical direction, then the offset is also rotated, so the y offset becomes x offset and vice-versa.
				int prevoffy = offsety;
				offsety = -offsetx;
				offsetx = -prevoffy;
			}else offsetx = dir*offsetx;
			
			// Renders the sprite on the screen.
			g.drawImage(td,tdx+offset.x+offsetx, tdy+offset.y+offsety, tdw + (int)((double)tdw/Math.abs(tdw) * 2 * Math.abs(offsetx)), tdh + (int)((double)tdh/Math.abs(tdh) * 2 * Math.abs(offsety)),null);
		}
	}
	
	static class target extends tile{
		
		/**
		 * This subclass of tile stores some variables exclusive to targets.
		 */
		
		long startime; // The time when the targets are displayed.
		double health; // The number of hits a target can take.
		int worth = 1; // How much the player will score after hitting them.
		double appearfor = p.appearFor; // The time for which they will appear.
		ArrayList<knife> hit = new ArrayList<knife>(); // The knives that have hit them.
		
		/**
		 * This is the constructor of the target class
		 * 
		 * @param x x coordinate
		 * @param y y coordinate
		 * @param width width of hitbox
		 * @param height height of hitbox
		 * @param st time when the target is spawned
		 */
		public target(double x, double y, double width, double height, long st) {
			super(x,y,width,height,false);
			startime = st;
			health = 1;
		}
	}
	
	static class player extends tile{
		
		/**
		 * This subclass of tile manages the rendering of the main player of the game.
		 */
		
		double runI = 0; // The current frame index of the run animation
		double shooti = 0; // The current frame index of the shoot animation.
		ArrayList<BufferedImage> run = new ArrayList<>(); // The arrayList containing the frames of the run animation.
		BufferedImage ss; // The sprite sheet of the player.
		BufferedImage idlei; // The idle sprite of the player.
		ArrayList<BufferedImage> j = new ArrayList<>(); // The array list containing the frames of the jump animation.
		ArrayList<BufferedImage> s = new ArrayList<>(); // The array list containing the frames of the shoot animation.
		
		boolean running = false; // The boolean storing whether the player is running.
		boolean idle = false; // The boolean storing whether the player is idle.
		boolean shoot = false; // The boolean storing whether the player is shooting.
		boolean jumping = false; // The boolean storing whether the player is jumping.
		
		/**
		 * This constructor of the player class has the same arguments as of the tile class.
		 * @param x x position of the player.
		 * @param y y position of the player.
		 * @param w width of the player's hitbox.
		 * @param h height of the player's hitbox.
		 */
		public player(double x, double y, double w, double h) {
			super(x,y,w,h,true);
			
			// Loading the sprite sheet from the sprites folder.
			try {
				ss = ImageIO.read(new File("sprites/mainP.png"));
			}catch(Exception e) {
				
			}
			double ih = ss.getHeight()/8; // The height of one sprite in the sprite sheet.
			double iw = ss.getWidth()/13; // The width of one sprite in the sprite sheet.
			
			// Loading the run animation.
			for(int i = 0; i<8; i++) {
				BufferedImage ta = new BufferedImage((int)iw,(int)ih,BufferedImage.TYPE_INT_ARGB); // The frame of the animation to be added.
				Graphics2D g = ta.createGraphics();
				g.drawImage(ss, (int)(-i*(iw)), (int)(-(ih)),ss.getWidth(),ss.getHeight(), null); // To get the frame, the sprite sheet is drawn on the frame such that, everything except of the desired frame is cut off.
				run.add(ta);
				//This same method of loading sprites is used below.
			}
			
			// Loading the idle frame.
			idlei = new BufferedImage((int)iw,(int)ih,BufferedImage.TYPE_INT_ARGB);
			Graphics2D gi = idlei.createGraphics();
			gi.drawImage(ss, 0, -1,ss.getWidth(),ss.getHeight(), null);
			
			// Loading the jumping animation frames.
			for(int i = -2; i>=-4; i-=2) {
				BufferedImage u = new BufferedImage((int)iw,(int)ih,BufferedImage.TYPE_INT_ARGB);
				Graphics2D gu = u.createGraphics();
				gu.drawImage(ss,(int)(i*(iw)), (int)(-5*(ih)),ss.getWidth(),ss.getHeight(), null);
				j.add(u);
			}
			
			//Loading the shoot animation frames.
			for(int i = 0; i<5; i++) {
				BufferedImage ta = new BufferedImage((int)iw,(int)ih,BufferedImage.TYPE_INT_ARGB);
				Graphics2D g = ta.createGraphics();
				g.drawImage(ss, (int)(-i*(iw)), (int)(-3*(ih)),ss.getWidth(),ss.getHeight(), null);
				s.add(ta);
			}
		}
		
		/**
		 * This method renders the sprite of the main player according to which animation is to be played and adjusts the sprite according to the hitbox.
		 *
		 *@param g The Graphics2D object used to render the sprite on the screen.
		 */
		@Override
		public void fill(Graphics2D g) {
			
			int o =20; // The offset of the sprite from the actual hitbox.
			int w = -dir*((int)hitboxWidth + o*3 - 5); // The width of the rendered sprite.
			int tdx = (int)x - o; // The x coordinate of where the sprite is to be rendered.
			
			if(dir==1) {
				//If the player is facing to the left, the sprite is flipped.
				tdx += -w - o;
			}
			int tdy = (int)y - o*2 - 5; // The y coordinate of where the sprite is to be rendered.
			
			tdx+=offset.x; // The offset of the screen shake is added to the sprite x coordinate.
			tdy+=offset.y; // The offset of the screen shake is added to the sprite y coordinate.
			
			if(shoot) {
				/* If the player is shooting, then the frames from the shooting animation are to be rendered.
				 * Two frames are switched between to give the illusion that something is thrown, for this the shooti variable is used.
				 * When the shooti variable is above a certain value, one frame is shown and when below, the other.
				 */
				if(shooti>=s.size()/2) {
					g.drawImage(s.get(0),tdx,tdy,w,(int)hitboxHeight + o*3,null);
				}else g.drawImage(s.get(s.size()-1),tdx,tdy,w,(int)hitboxHeight + o*3,null);
				
				shooti+=0.5;
				if(shooti>=s.size()) {
					shoot = false;
					shooti = 0;
				}
			}else if(jumping) {
				
				//Renders image from the jump animation when the player is jumping.
				
				//If the player is going up, then one frame is shown, if it is going down, then another frame is shown.
				if(yv>=0)g.drawImage(j.get(1),tdx,tdy,w,(int)hitboxHeight + o*3,null);
				if(yv<0)g.drawImage(j.get(0),tdx,tdy,w,(int)hitboxHeight + o*3,null);
				
			}else if(running) {
				
				// If the player is running, then the frames from the running animation are shown
				g.drawImage(run.get((int)runI),tdx,tdy,w,(int)hitboxHeight + o*3,null);
				runI+=0.25; // The runI variable is increased by a small amount to control the frame rate of the animation.
				if(runI>=run.size())runI=0;
			}else {
				
				// If the player is doing nothing then the idle image is rendered.
				g.drawImage(idlei,tdx,tdy,w,(int)hitboxHeight + o*3,null);
			}
		}
	}
	
	static class particles{
		/**
		 * This class manages the particle effects used in the game.
		 */
		
		static class particle extends tile{
			/**
			 * This subclass of tile class represents a single particle in the whole particle system.
			 */
			boolean sb = true; // This boolean contains whether the particle has experienced the starting acceleration.
			int timer = 0; // This is the timer till when the particle experiences the starting acceleration.
			
			/**
			 * This is the constructor of the particle class, containing the same arguments as the tile class.
			 * @param x The x position of the particle
			 * @param y The y position of the particle
			 * @param w The width of the particle
			 * @param h The height of the partile
			 */
			public particle(double x, double y, double w, double h) {
				super(x,y,w,h,true);
			}
			
			/**
			 * This method has some additions exclusive to the particles.
			 * @param platforms This is the array list of tile objects with which the particles can collide with.
			 */
			@Override
			public void update(ArrayList<tile> platforms) {
				
				//This method is the same as the tile method with minor changes.
				
				if(movable) {
					collided = false;
					
					//handling y collision 
					ya = gravity;
					if(yv<maxYV) {
						yv+=ya;
					}
					
					tile toTest = new tile(x,y+yv,hitboxHeight,hitboxWidth,true);
					toTest.yv = yv;
					
					tile[] future = toTest.collide(platforms);
					yCollided = false;
					if(future[0]!=null) {
						this.collided = true;
						colTile = future[0];
						if(future[0].down) {
							yv = 0;
							y=future[0].y+future[0].hitboxHeight+0.1;
						}else {
							if(yv>0) {
								y += future[0].y-(y+hitboxHeight);
								yv=-yv*3/4; // The y velocity of the particles after collision is set to opposite direction to simulate bounce.
							}
							yCollided = true;
						}
					}
					y+=yv;
					
					//handling x collision
					tile[] collided = collide(platforms);
					if(collided[1]!=null) {
						x = collided[1].xafterCol;
						xv = -xv*3/4; // The x velocity is set to its opposite direction to simulate bouncing
					}else {
						
						if((xv)!=0) {
							double friction = 0.5/0.8 * frictionForce;
							if(yCollided) {
								friction = frictionForce;
							}
							double tempxv = xv + (-Math.abs(xv)/xv)*friction;
							if( (Math.abs(tempxv)/tempxv) == (-Math.abs(xv)/xv) ) {
								xv = 0;
							}else {
								xv = tempxv;
							}
						}else {
							xv=0;
						}
						
						if(xv<maxXV && xv>-maxXV) {
							xv+=xa;
						}
						
						x+=xv;
						
						tile[] f = collide(platforms);
						if(f[1]!=null) {
							x=f[1].xafterCol;
						}
					}
				}
			}
		}
		
		ArrayList<particle> parr = new ArrayList<particle>(); // The array list of particles containing the particles in the particle system.
		Point2D dir; // The direction of the initial force on the particles.
		double mag; // The magnitude of the initial force on the particles.
		int timert = 0; // The time for which new particles are added.
		double x; // The x coordinate of the point from which the particles emerge.
		double y; // The y coordinate of the point from which the particles emerge.
		
		Line2D l; // The line from which the particles emerge (if so).
		
		boolean running = true; // This boolean contains whether the particle simulation is complete or not.
		boolean line = false; // This boolean contains whether the particles emerge from a line.
		
		Color c = new Color(205,205,205); // This is the color of the particles. (By default set to grey)
		
		/**
		 * This constructor generates particles emerging from a single point.
		 * 
		 * @param x The x coordinate of the point.
		 * @param y The y coordinate of the point.
		 * @param n The total number of particles.
		 * @param dir The direction of the initial force.
		 * @param mag The magnitude of the initial force. (Basically in math and physics terms, magnitude is the size of the object in the relative to the movement).
		 * @param maxs The max size of the particles.
		 */
		public particles(double x, double y, int n, Point2D dir, double mag, double maxs) {
			this.dir = dir;
			this.mag = mag;
			this.x = x;
			this.y = y;
			
			// Generating particles from a point.
			for(int i = 0; i<n; i++) {
				double w = maxs/2 +Math.random()*maxs/2; // A random size is decided for the particles between 1/2maxs to maxs.
				parr.add(new particle(x+w/2,y+w/2,w,w)); // The size is used to initialise a particle and add to the parr arraylist.
			}
		}
		
		/**
		 * This constructor generates particles emerging from a line.
		 * 
		 * @param l The line from which the particles emerge.
		 * @param n The maximum number of particles on the screen.
		 * @param dir The direction of the initial force on the particles.
		 * @param mag The magnitude of the initial force on the particles.
		 * @param maxs The maximum size of the particles.
		 */
		public particles(Line2D l, int n, Point2D dir, double mag, double maxs) {
			this.dir = dir;
			this.mag = mag;
			line = true;
			x = l.getX1();
			y = l.getY1();
			this.l = l;
			
			for(int i = 0; i<n; i++) {
				double w = maxs/2 + Math.random()*maxs/2; // Pick a random size
				double angle = Math.atan2(l.getY2()-l.getY1(), l.getX2()-l.getX1()); // Finding the angle between the line
				double len = Point.distance(l.getX1(),l.getY1(),l.getX2(),l.getY2()); // Finding the length of the line.
				double nlen = len/n; // Calculating the length between each particle
				
				parr.add(new particle(l.getX1()+(i*nlen*Math.cos(angle)),l.getY1()+(i*nlen*Math.sin(angle)),w,w)); // Adding a new particle
				
				//Calculating the random outward force
				particle pp = parr.get(parr.size()-1);
				Point2D mid = new Point2D.Double((l.getX1()+l.getX2())/2, (l.getY1()+l.getY2())/2); // Finding the middle of the line (along which the particles are to be generated)
				Point2D face = new Point2D.Double(pp.x-mid.getX(), pp.y-mid.getY()); // The vector pointing from the center of the line to the particle.
				double ll = Point.distance(0, 0, face.getX(), face.getY());
				face.setLocation(face.getX()/ll, face.getY()/ll); // Normalizing the vector
				pp.xv += face.getX()*(mag+(2.5 - Math.random()*5)); // Adding the starting force using the face vector.
				pp.yv += face.getY()*(mag+(2.5 - Math.random()*5));
			}
		}
		
		/**
		 * Sets color of the particle.
		 * @param c The color with which the particle color is to be set.
		 */
		public void setColor(Color c) {
			this.c = c;
			for(particle pp : parr) {
				pp.setColor(c);
			}
		}
		
		/**
		 * Updating the particles in the particle system.
		 * @param p The array list containing the tile objects with which the particles cacn collide with.
		 * @param g The Graphics2D object to render the particles.
		 */
		public void update(ArrayList<tile> p, Graphics2D g) {
			ArrayList<tile> i = new ArrayList<tile>(); // The array list of tile objects which will be used as input for the update method of the particles.
			for(tile t : p)i.add(t);
			for(particle pp : parr)i.add(pp);
			
			// Updating the particles 
			for(int j = 0; j<parr.size(); j++) {
				particle pp = parr.get(j); // The particle to update
				i.remove(pp); // Remove the current particle from the input array list.
				
				if(pp.sb) { // If the particle has not been added the initial start, it is added
					if(!line) { // If the particles do not emerge from a line, a force is added in the same direction.
						pp.xv += dir.getX()*(mag+(2.5 - Math.random()*5));
						pp.yv += dir.getY()*(mag+(2.5 - Math.random()*5));
						pp.timer++;
					}else { // If the particles are emerge from a line, the particles emerge outwards from the middle of the line, this is done in the same way when they were initialized.
						Point2D mid = new Point2D.Double((l.getX1()+l.getX2())/2, (l.getY1()+l.getY2())/2);
						Point2D face = new Point2D.Double(pp.x-mid.getX(), pp.y-mid.getY());
						double len = Point.distance(0, 0, face.getX(), face.getY());
						face.setLocation(face.getX()/len, face.getY()/len);
						pp.xv += face.getX()*(mag+(2.5 - Math.random()*5));
						pp.yv += face.getY()*(mag+(2.5 - Math.random()*5));
						pp.timer++; // A timer is used to keep track of how many times the particle is added the initial force.
					}
				}
				pp.update(i); // The particle is updated.
				pp.fill(g); // The particle is rendered.
				pp.show(g); // The particle boundary is shown.
				pp.hitboxHeight-=1; // The height of the particle is reduced.
				pp.hitboxWidth-=1; // The width of the particle is reduced.
				
				// If the size of a particle is 0, a new particle is created if the flow time is less than 10.
				if(pp.hitboxWidth<=0 || pp.hitboxHeight<=0) { 
					parr.remove(pp);
					if(timert<=10 && !line) {
						running = false;
						double w = 10+Math.random()*10;
						parr.add(new particle(x+w/2,y+w/2,w,w));
						parr.get(parr.size()-1).setColor(c);
					}
				}
				
				// If the number of times for which the particle has been added initial force is greater then 10, it is stopped.
				if(pp.timer>10) {
					pp.sb = false;
					pp.timer = 0;
				}
				i.add(pp);
			}
			timert++;
			
		}
	}
	
	@SuppressWarnings("serial")
	static class panel extends JPanel{
		
		/**
		 * This class manages the main game panel and handles all the logic.
		 */
		
		double appearFor; // The time for which a target stays on the screen.
		double restTime; // The time between the spawning of two targets.
		double enemKSpeed; // The speed of the enemy knives.
		double enemKMaxS; // The max speed of the enemy knives.
		double restTimeForKnives; // The time between the spawning of two knives.
		double maxV; // The max velocity of the knives.
		double health; // The health of the player.
		
		double empmsga; // The alpha of the "Emp is ready" message.
		boolean empai = false; // Boolean that contains whether the empmsga is increasing or decreasing.
		
		ArrayList<tile> platforms = new ArrayList<tile>(); // Array list of tile objects containing the platforms in the game.
		ArrayList<knife> knives = new ArrayList<knife>(); // Array list of all the knives in the game.
		ArrayList<knife> enemKniv = new ArrayList<knife>(); // Array list of all the enemy knives in the game.
		ArrayList<target> targets = new ArrayList<target>(); // Array list of all the targets on the screen.
		player p; // The main player.
		
		BufferedImage background; // The background image.
		
		boolean movingRight = false; // The boolean containing whether the player is moving to the right.
		boolean movingLeft = false; // The boolean containing whether the player is moving to the left.
		boolean jumping = false; // The boolean containing whether the player is jumping.
		boolean walljump = false; // Boolean containing whether the player is wall jumping.
		boolean enemKnives = false; // Boolean containing whether enemy knives should start coming or not.
		boolean showMsg = false; // Boolean containing whether to show the "Enemy knives are coming message or not".
		boolean gameOver = false; // Boolean containing whether the game is over or not.
		boolean shake = false; // Boolean containing whether the screen is shaking or not.
		boolean sshake = false; // Boolean containing whether the score text is shaking or not.
		
		boolean emp = false; // Boolean containing whether the emp is active or not.
		boolean empav = false; // Boolean containing whether the emp is available for usage or not.
		int empm = 0; // The value of the emp meter.
		int empi = 1; // The timer for the emp.
		
		int timer = 0; // The timer to accelerate the yv for jumping.
		int timer2 = 0; // The timer to accelerate for wall junping.
		
		double sshakeI = 15; // The score text shake intensity.
		Point ssOff = new Point(0,0); // The offset of the score offset for the score text shake.
		
		double ybeforejump = 0; // The y of the player before jumping (used to calculate the height achieved)
		
		int score = 0; // The score of the player.
		double sSize = 50; // The size of the score text.
		
		Point mouse = MouseInfo.getPointerInfo().getLocation(); // The location of the mouse on the screen.
		
		long startRest = 0; // The time when the rest for the targets is started. 
		long startRestForKnives = 0; // The time when the rest for the knives is started.
		long startMsg = 0; // The time when the "Enemy knives are coming" message is started.
		
		ArrayList<particles> effects; // Array list containing the particle effects on screen.
		
		int ci = 0; // The time index for the screen shake.
		int ci2 = 0; // The time index for the score text shake.
		Timer t = new Timer(10, new ActionListener() { // This is the main game loop with most of the logic of the game.
			@Override
			public void actionPerformed(ActionEvent e) {
				if(startedTime!=0 && System.currentTimeMillis()-startedTime>=1000 && !gClip.isRunning()) {
					// If the game has started but the music isnt playing, the music is loaded and is played.
					try {
						gClip = loadMusic("sounds/mmbg2.wav");
					} catch (UnsupportedAudioFileException | IOException
							| LineUnavailableException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					gClip.start();
				}
				
				// If the player collide with a tile along the y axis, gravity is set to normal and jumping is disabled.
				if(p.yCollided) {
					p.jumping = false;
					p.gravity = 0.5;
				}
				
				// If the message is not being displayed and the game is not over, the logic is implemented.
				if( (!showMsg) && (!gameOver)) {
					
					// The x acceleration is set to speed if the player is moving right.
					if(movingRight) {
						p.xa = speed;
						p.running = true;
						p.dir = -1;
					}
					
					// The x acceleration is set to -speed if the player is moving left.
					if(movingLeft) {
						p.xa = -speed;
						p.running = true;
						p.dir = 1;
					}
					
					// The player's y velocity is accelerated if the player is jumping.
					if(jumping) {
						if(timer==0) { // If jumping has just begun, y before jump is initialized.
							ybeforejump = p.y;
						}
						p.yv -= jumpS; // y velocity is set to the jump speed.
						timer++;
						p.jumping = true;
						
						// Checking if the height achieved is greater than the jump height, if so, jumping is set to false.
						if(ybeforejump-(p.y+p.yv)>=jumpheight) {
							timer = 0;
							jumping = false;
						}
					}
					
					// The players y velocity along with the x velocity are adjusted if the player is wall jumping.
					if(walljump) {
						if(timer2==0) { // If the wall jumping has just begun, the yv is set to 0.
							p.yv=0;
						}
						if(timer2<5) { // If the timer is less than the update time.
							p.yv -= 15;
							if(p.xv!=0) { // If the player was moving in a direction
								p.xv = (-Math.abs(p.xv)/p.xv)*12; // Its direction of movement is flipped and the magnitude of the change is set to12.
								p.dir=-p.dir;
							}else {
								p.xv = p.dir*8; // If the x velocity is 0, some is added.
								p.dir=-p.dir;
							}
							timer2++;
						}else if(timer2>=5) {
							timer2 = 5;
						}
					}
					
					// If the player collides along any of the axis, wall jump is set to false.
					if( ( (p.yCollided) || (p.xCollided) ) && (walljump) ) {
						p.xa = 0;
						timer2 = 0;
						walljump = false;
					}
					
					// Updating the targets.
					for(int i = targets.size()-1; i>=0; i--) {
						target t = targets.get(i);
						
						for(knife k : knives) {
							//Checking if the target is hit by a knife, if so, its health is reduced.
							if( (!t.hit.contains(k)) && (k.valid) ) {
								if(k.getRect().intersects(t.getRect())) {
									t.health-=k.damage;
									t.hit.add(k);
								}
							}
						}
						
						// If the time for which the targets are appeared is greater than the time for which the targets are supposed to appear for or the health of the target is 0.
						if( (System.currentTimeMillis()-t.startime>=t.appearfor) || (t.health<=0) ) {
							
							// Reduce the time for which a target is supposed to appear for.
							if(appearFor>1500)
								appearFor-=10;
							
							// Reduce the time between two targets.
							if(restTime>500);
								restTime-=10;
							
							// If the health of the target is 0
							if(t.health<=0) {
								play("sounds/point.wav"); // The point scored sound is played.
								if(t.worth>3)play("sounds/point2.wav");
								
								// Score and the emp meter is updated.
								score+=t.worth;
								empm+=t.worth;
								sshake = true; // The score text shakes since the score is updated.
								sshakeI = 15;
								
								// Randomizing the score text shake 
								double r = Math.random();
								if(r<0.5)ssOff.x = (int)sshakeI;
								if(r>=0.5)ssOff.y = (int)sshakeI;
								
								// Checking if the emp meter is above the limit needed to make the emp available.
								if(empm>=empl) {
									empav = true;
									empm=empl;
								}
								
								// Adding the particle effects.
								Point2D f = new Point2D.Double(-1,0); // Initial force of the particles.
								if(t.x<size.width/2) {
									f = new Point2D.Double(1,0);
								}
								effects.add(new particles(new Line2D.Double(t.x,t.y,t.x+t.hitboxWidth,t.y+t.hitboxHeight), 10,f, 2, 20));
								effects.get(effects.size()-1).setColor(t.c);
							}
							startRest = System.currentTimeMillis(); // Start rest time is set
							targets.remove(i);
						}
					}
					
					// Updating the knives.
					for(int j = 0; j<knives.size(); j++) {
						knife k = knives.get(j);
						
						// If the knives are facing along the horizontal direction, their x velocity is updated.
						if( (k.dir<2) && (!k.collided) ) {
							k.xv+=k.dir*kspeed;
						}
						
						if(k.collided) {
							k.valid = false; // The knife can no longer damage the targets.
							if( !(k.deathTime>0) ) k.deathTime = System.currentTimeMillis();
							k.gravity = 0;
							if(k.dir==2) k.movable = false;
							
							// If the knife has collided for 5 seconds, it is removed.
							if(System.currentTimeMillis()-k.deathTime>=5000) {
								knives.remove(j);
							}
						}
					}
					
					// If there are no targets on the screen, new target is added.
					if( (targets.size()<maxTargetsOnScreen) && (System.currentTimeMillis()-startRest>=restTime) ) {
						
						// A random side is chosen from the top, right and left sides, to add the target to and then is added.
						double r = Math.random()*3;
						if( (r>=0) && (r<1) ) targets.add(new target(size.width-20, Math.random()*(size.height-(platforms.get(0).hitboxHeight)-120), 20, 120, System.currentTimeMillis()));
						else if( (r>=1) && (r<2) )targets.add(new target(Math.random()*(size.width-120), 0, 120, 20, System.currentTimeMillis()));
						else if( (r>=2) && (r<3) )targets.add(new target(0, Math.random()*(size.height-(platforms.get(0).hitboxHeight)-120), 20, 120, System.currentTimeMillis()));
						
						// A random number is chosen between 0 and 10
						r = Math.random()*10;
						target t = targets.get(targets.size()-1);
						t.setColor(new Color(164,116,73).brighter());
						
						// If the randomly chosen number is less than 1, the gold target is created and added.
						if(r<1) {
							t.worth = 4;
							t.setColor(Color.yellow);
							t.appearfor = 1500;
							if(t.hitboxHeight>t.hitboxWidth) t.hitboxHeight = targets.get(targets.size()-1).hitboxHeight * 3/4;
							else t.hitboxWidth = targets.get(targets.size()-1).hitboxWidth * 3/4;
						}
						
						// If the randomly chosen number is between 1 and 3, the target is made bigger.
						if( (r>=1) && (r<3) ) {
							t.worth = 3;
							t.health = 2;
							if(t.hitboxHeight>t.hitboxWidth) t.hitboxHeight = t.hitboxHeight * 3/2;
							else t.hitboxWidth = t.hitboxWidth * 3/2;
						}
					}
					
					// Implementing logic for the enemy knives.
					for(int i = 0; i<enemKniv.size(); i++) {
						knife t = enemKniv.get(i);
						
						// If the knife is facing left or right, its x velocity is accelerated.
						if( (t.dir<2) && (!t.collided) ) {
							if( (t.xv<t.maxXV) && (t.xv>-t.maxXV) )
								t.xv+=t.dir*enemKSpeed;
						}
						
						// If emp is activated, the knife is not already affected by emp and the knife has not collided with anything, it is affected by the emo.
						if( (emp) && (!t.flipped) && (t.valid) ) {
							Point2D[] tc = {new Point2D.Double(t.x,t.y), new Point2D.Double(t.x,t.y+t.hitboxHeight)}; // The vertices of the knife hitbox to be added is added in a array.
							if(t.dir==2)tc = new Point2D[] {new Point2D.Double(t.x,t.y+t.hitboxHeight), new Point2D.Double(t.x+t.hitboxWidth, t.y+t.hitboxHeight)};
							if(t.dir==1)tc = new Point2D[] {new Point2D.Double(t.x+t.hitboxWidth, t.y), new Point2D.Double(t.x+t.hitboxWidth, t.y+t.hitboxHeight)};
							
							// The vertices of the player's hitbox.
							Point2D[] tca = new Point2D[] {
									new Point2D.Double(p.x,p.y),
									new Point2D.Double(p.x+p.hitboxWidth,p.y),
									new Point2D.Double(p.x+p.hitboxWidth,p.y+p.hitboxHeight),
									new Point2D.Double(p.x,p.y+p.hitboxHeight)
							};
							
							// Now the distance between each of the player vertices and the vertices of the knives is used to check if it is less than 50, if it is, the knife is flipped.
							boolean done = false;
							for(Point2D p : tc) {
								for(Point2D p2 : tca) {
									if( (!done) && (Point.distance(p.getX(), p.getY(), p2.getX(), p2.getY())<=50) ) {
										// Flipping the knife.
										t.flipped = true;
										if(t.dir<2) {
											// If the knife is towards the player's head, it flips up.
											if( (t.y<panel.this.p.y) || (-panel.this.p.y+platforms.get(0).y<=100) ) {
												t.dir = 2;
												t.yv = t.xv;
												if(t.xv>0)t.yv=-t.xv;
												t.xv = 0;
												t.gravity = -5*enemKSpeed;
											}else {
												// Else it flips down.
												t.dir = 2;
												t.yv = t.xv;
												if(t.xv<0)t.yv=-t.xv;
												t.xv=0;
												t.gravity = enemKSpeed;
											}
										}else if(t.dir==2) {
											t.gravity = 0;
											// If the knife hitbox is towards the right of the player, it is flipped towards the right.
											if(t.x+t.hitboxWidth/2 > panel.this.p.x+panel.this.p.hitboxWidth) {
												t.dir = 1;
											}else t.dir = -1;
										}
										done = true;
										break;
									}
								}
								if(done)break;
							}
						}
						
						if(t.collided) {
							t.valid = false; // If the knife has collided with a platform, it is set invalid.
							if(!(t.deathTime>0)) t.deathTime = System.currentTimeMillis();
							t.gravity = 0;
							if(t.dir==2) t.movable = false;
							
							// If the knife has been collided for 5 seconds, it is removed.
							if(System.currentTimeMillis()-t.deathTime>=5000) {
								enemKniv.remove(i);
							}
						}
					}
					
					// If enemy knives are to be spawned, the knives are spawned.
					if(enemKnives) {
						if(startRestForKnives==0)startRestForKnives = System.currentTimeMillis();
						
						// If it is time for a knife to be spawned, it is spawned.
						if( (System.currentTimeMillis()-startRestForKnives>=restTimeForKnives) ) {
							
							// First a random number is generated, which is used to determine, along which side the knife is spawned,
							double r = Math.random()*3;
							if(p.y<size.height/5) r = (int)(Math.random()) * 2; // If the player is close to the top, knives are spawned from the left or right,
							if(p.x>size.width*4/5 && (r>=0 && r<1)) r = 2; // If the player is close to the right edge, the knife comes from the left edge.
							if(p.x<size.width*1/5 && (r>=2 && r<3)) r = 0; // If the player is close to the left edge, the knife comes from the right edge.
							
							// The knives are added to the enemKniv array list and adjusted accordingly.
							if( (r>=0) && (r<1) ) {
								enemKniv.add(new knife(size.width-knifSize.width-7, p.y+(Math.random()*(p.hitboxHeight+knifSize.width+1) - knifSize.width+1), 50, 20, true));
								enemKniv.get(enemKniv.size()-1).dir = -1;
								enemKniv.get(enemKniv.size()-1).gravity = 0;
							}else if( (r>=1) && (r<2) ){
								enemKniv.add(new knife(p.x+(Math.random()*(p.hitboxWidth+knifSize.width+2)-knifSize.width-2), 1, 20, 50, true));
								enemKniv.get(enemKniv.size()-1).dir = 2;
								enemKniv.get(enemKniv.size()-1).gravity = enemKSpeed;
							}else if( (r>=2) && (r<3) ) {
								enemKniv.add(new knife(12, p.y+(Math.random()*(p.hitboxHeight+knifSize.width+1) - knifSize.width+1), 50, 20, true));
								enemKniv.get(enemKniv.size()-1).dir = 1;
								enemKniv.get(enemKniv.size()-1).gravity = 0;
							}
							
							// Max speed of the knives is set.
							enemKniv.get(enemKniv.size()-1).maxXV = enemKMaxS;
							enemKniv.get(enemKniv.size()-1).maxYV = enemKMaxS;
							startRestForKnives = System.currentTimeMillis();
							
							if(restTimeForKnives>500) restTimeForKnives -= 100; // The rest time for the knives is reduced.
							if(enemKSpeed<7) { // The speed of the enemy knives is increased.
								enemKSpeed+=0.05;
							}
							enemKMaxS = enemKSpeed * (double)25/7; // The enemy max speed is set according to the enemy knife speed.
						}
					}
				}
				
				// If the score is greater than the score required to start spawning the enemy knives.
				if( (score>=enemKnifAppearScore) && (startMsg==0) ) {
					showMsg = true;
				}
				
				// If emp is active, it is checked if the emp is run for sufficient time, then it is disabled.
				if(emp) {
					if(empi/empTimeL >= 1) {
						emp = false;
						empm = 0;
						empav = false;
						empi = 1;
					}
					empi++;
				}
				
				//Implementing camera shake.
				if(shake) {
					
					// Disabling the score shake
					ssOff = new Point(0,0); 
					sshake = false;
					sshakeI = 15;
					
					if(ci%6==0) {
						if(offset.x!=0) {
							// Offset x is flipped.
							offset.x = -1 * Math.abs(offset.x)/offset.x * ((int)shakeIntensity);
						}if(offset.y!=0) {
							// Offset y is flipped.
							offset.y = -1 * Math.abs(offset.y)/offset.y * ((int)shakeIntensity);	
						}
						ci = 0;
					}
					shakeIntensity -=0.5; // The shake intensity is reduced.
					
					// If the shake intensity is 0, camera shake is set false.
					if(shakeIntensity<=0) {
						offset = new Point(0,0);
						shake = false;
					}
					ci++;
				}
				
				//Implementing score text shake
				if(sshake) {
					if(ci2%6==0) {
						if(ssOff.x!=0) { 
							// Offset x is flipped.
							ssOff.x = -1 * Math.abs(ssOff.x)/ssOff.x * ((int)sshakeI);
						}if(ssOff.y!=0) {
							// Offset y is flipped.
							ssOff.y = -1 * Math.abs(ssOff.y)/ssOff.y * ((int)sshakeI);	
						}
						ci = 0;
					}
					sshakeI -=0.5; // The score shake intensity is reduced.
					
					// If the score shake intensity is 0, score shake is disabled.
					if(sshakeI<=0) {
						ssOff = new Point(0,0);
						shake = false;
					}
					ci2++;
				}
				
				// Calculating the frame rate.
				total++;
				
				// If 1 second has passed since the last frame count check, fps is updated.
				if(System.currentTimeMillis() >= prevChecked+1000) { 
					fps = total;
					total = 0;
					prevChecked = System.currentTimeMillis();
				}
				
				// The screen is refreshed.
				repaint();
			}
		});
		
		String name; // The name of the player.
		int best = 0; // The best score of the player.
		
		boolean pause = false; // Boolean whether the game is pause or not.
		boolean quitConfirm = false; // Boolean whether the game quit confirm dialog box is visible or not.
		boolean control = false; // Boolean whether the control menu is visible
		JButton mainMenu = new JButton(); // JButton to set to main menu.
		JButton resume = new JButton(); // Button to resume the game.
		
		JButton mm; // The button to go to the main menu.
		JButton restart; // The button to restart the game.
		
		BufferedImage ground; // The ground sprite.
		BufferedImage mainMenuSS; // The ss of the main menu.
		
		int ssx = 0; // The main menu screen shot x.
		int ssxv = 10; // The main menu screen shot x velocity.
		
		boolean saved = false; // Boolean whether the game is saved.
		long startedTime = 0; // The time of the start of the game.
		
		/**
		 * This constructor of the panel class initializes all the important variables.
		 * 
		 * @param n The name of the player.
		 * @param smss The start menu screen shot. (used for screen scrolling animation)
		 */
		public panel(String n, BufferedImage smss) {
			
			// The start game object is stopped and disabled.
			if(sg!=null)sg.t.stop();
			sg = null;
			
			if(f.getKeyListeners().length>1)f.removeKeyListener(f.getKeyListeners()[1]); // Any extra key listeners are removed from the frame.
			effects = new ArrayList<particles>(); // The effects array list is initialized.
			mainMenuSS = smss; // The main menu screen shot is set.
			
			try {
				gClip = loadMusic("sounds/mmbg2.wav"); // The game background sound is loaded.
				
				BufferedImage bg = ImageIO.read(new File("sprites/bg0.png")); // The background of the game is loaded.
				background = new BufferedImage(size.width,size.height,BufferedImage.TYPE_INT_ARGB);
				Graphics2D g = background.createGraphics();
				g.drawImage(bg,0,0,size.width,size.height,null); // The background image is scaled.
				
				// The pixelated font is loaded.
				GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
				ge.registerFont(font);
				
				ground = ImageIO.read(new File("sprites/ground.png")); // The ground sprite is loaded.
				
				mmClip = loadMusic("sounds/mmbg.wav"); // The main menu clip is loaded.
			}catch(Exception e) {}
			
			// All the music is stopped.
			mmClip.stop();
			gClip.stop();
			
			if(!gClip.isRunning())startedTime = System.currentTimeMillis();
			
			this.setLayout(null);
			
			// Important variables are initialized.
			appearFor = aF;
			restTime = rT;
			maxV = mV;
			restTimeForKnives = rTFK;
			health = h;
			enemKSpeed = eKS;
			enemKMaxS = eKMS;
			empmsga = 255;
			empai = false;
			
			// The name is set.
			name = n;
			cplayer = n;
			
			// The mouse listener is added.
			this.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent arg0) {
					
					if( (!showMsg) && (!gameOver) && (!pause) && (mainMenuSS==null) ) {
						p.shoot = true; // Setting the shooting animation of the player to true.
						knife ta = new knife(p.x+(-p.dir*p.hitboxWidth/2), p.y+(p.hitboxHeight/2), knifSize.width, knifSize.height, false); // The knife to be added.
						
						// If left mouse button is pressed, the knife spawaned goes upwards, thus the dimensions need adjusting.
						if(arg0.getButton()==MouseEvent.BUTTON3) {
							
							// Adjusting the x location of the knife.
							ta.x = p.x+(p.hitboxWidth-(ta.hitboxWidth/2));
							
							// Rotating the hitbox dimensions of the knife.
							ta.hitboxWidth = ta.hitboxHeight;
							ta.hitboxHeight = knifSize.width;
							
							// Adjusting the y location of the knife.
							ta.y -=ta.hitboxHeight;
							ta.dir = 2;
							ta.gravity = -kspeed;
						}else {
							// If right mouse button is pressed, gravity for the knife is disabled.
							ta.gravity = 0;
							ta.dir = -p.dir;
						}
						ta.maxYV = 50;
						knives.add(ta);
					}
					
					if(pause) { // If the pause menu is being displayed.
						Component[] c = panel.this.getComponents();
						ArrayList<Component> cs = new ArrayList<Component>();
						for(Component co : c)cs.add(co);
						
						// Checking if the control menu is shown, if so, it is closed (because the mouse is clicked outside of the button)
						if(cs.contains(cOptions.get(0))) {
							quitConfirm = false; // The dialog is disabled.
							control = false; // The control menu is disabled.
							removeAll();
							repaint();
						}
					}
				}
			});

			int npw = 400; // Width of buttons on the pause menu.
			int nph = 50;  // Height of the buttons on the pause menu.
			// Adjusting the dimensions based on the screen size.
			if(npw>=size.width/2)npw = size.width/2 - 25;
			if(nph>=size.height/2)npw = size.height/2 - 25;
			
			// JLabel to get the preferred size of the text on screen.
			JLabel test = new JLabel("Paused");
			test.setFont(new Font("Pixelated Regular", Font.BOLD, 80));
			Dimension ps = test.getPreferredSize();
			
			// Initializing the resume button in the pause menu.
			resume.setSize(npw,nph);
			resume.setLocation(size.width/2 - npw, ps.height+10+size.height/2 - nph/2 - (30 + nph));
			resume.setFocusable(false); // Setting that the button cannot take focus (to ensure that the key listener always has focus.)
			resume.setBorderPainted(false); // Disabling the outlines on the buttons.
			resume.addActionListener(new ActionListener() { // Adding the action listener.
				@Override
				public void actionPerformed(ActionEvent e) {
					play("sounds/click.wav"); // Playing the click sound effect.
					gClip.start(); // Resuming the background game music.
					t.start(); // Resuming the game loop.
					pause = false; // Disabling the pause menu.
					removeAll();
				}
			});
			initialize("Resume",resume,false,true); // Initializing the icons of the resume button.
			
			// All the other buttons are initialized in the same way with the only difference in their location and actionListener. 
			
			// Initializing the main menu button in the pause menu.
			mainMenu.setSize(npw,nph);
			mainMenu.setLocation(size.width/2, ps.height+10+size.height/2 - nph/2 - (30 + nph));
			mainMenu.setFocusable(false);
			mainMenu.setBorderPainted(false);
			mainMenu.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					play("sounds/dialog.wav"); // Playing the dialog box open sound effect.
					quitConfirm = true; // Enabling the confirm dialog.
					repaint(); //  Refreshing the page.
				}
			});
			// Initializing the main menu icons.
			initialize("Main Manu",mainMenu,false,true);
			
			// Initializing the controls button in the pause menu.
			controls = new JButton();
			controls.setSize(npw,nph);	
			controls.setLocation(size.width/2 - npw/2, mainMenu.getY()+mainMenu.getHeight());
			controls.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					play("sounds/click.wav"); // Playing the click sound effect.
					control = true; // Enabling the control menu.
					repaint(); //  Refreshing the screen.
				}
			});
			// Removing any unwanted extra mouse listeners in the controls button.
			for(int i = 1; i<controls.getMouseListeners().length; i++) {
				controls.removeMouseListener(controls.getMouseListeners()[i]);
			}
			controls.setBorderPainted(false);
			initialize("Controls",controls,false,true); // Initializing controls button icons.

			// If the player is loaded from the data.txt file, then the best score and controls selected is also loaded from the data file.
			if( (players.contains(name)) ){
				best = bestScores.get(players.indexOf(name));
				wasd = wasds.get(players.indexOf(name));
			}else wasds.add(wasd);
			
			// The ceiling, floor, left and right walls are added.
			platforms.add(new tile(0,size.height-150, size.width, 150, false));
			platforms.add(new tile(-100, 0, 100, size.height, false));
			platforms.add(new tile(size.width, 0, 100, size.height, false));
			platforms.add(new tile(0,-100,size.width, 100, false));
			platforms.get(0).setImage(ground, false);
			
			// Initializing the player.
			p = new player(size.width/2 - 25, size.height/2 - 25, 60,60);
			
			t.start(); // Starting the game loop.
		}

		/**
		 * This method renders all the objects onto the screen.
		 * 
		 * @param g
		 */
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			
			// Initializing the graphics2D object.
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
			// If there is a background in the game, it is rendered.
			if(background!=null)g2.drawImage(background,0,0,size.width,size.height,null);
			p.update(platforms); // The player is updated.
			p.fill(g2); // The player is rendered.
			
			// Updating and rendering all the particle systems.
			for(int i = effects.size()-1; i>=0; i--) {
				particles particles = effects.get(i);
				ArrayList<tile> input = new ArrayList<tile>(); // The tile array list used as an argument for the update method.
				input.add(p); // Adding the player (so the particles interact with the player.)
				input.add(platforms.get(0)); // Adding the floor tile (so the particles collide with the floor)
				particles.update(input, g2); // Updating and rendering the particles.
				if(particles.parr.size()==0)effects.remove(particles); // If the particles simulation of a system is finished, it is removed from the game.
			}
			
			// Updating and rendering all the knives.
			for(tile t : knives) {
				t.update(platforms);
				t.fill(g2);
			}
			
			// Rendering and updating all the enemy knives, also registering damage if they hit the player.
			for(int i = enemKniv.size()-1; i>=0; i--) {
				knife t = enemKniv.get(i);
				t.update(platforms); // The enemy knives are updated.
				
				tile pc = new tile(p.x,p.y,p.hitboxWidth,p.hitboxHeight,true); // A copy of the player tile, to check for collision.
				tile[] tta = pc.collide(t.addMe(new ArrayList<tile>())); // Checking for collision of the player with the enemy knife.
				
				// If the knife is valid and it has hit the player and if the emp is not active, damage is registered.
				if( (t.valid) && ( (tta[0]!=null) || (tta[1]!=null)) && (!emp) ) {
					health--; // The health of the player is reduced.
					shake = true; // Camera shake is enabled.
					shakeIntensity = 20; // Camera shake intensity is set.
					
					// Randomizing the direction of the shake (horizontal or vertical)
					double r = Math.random();
					if(r>0.5) offset = new Point((int)shakeIntensity,0); // If r is greater than 0.5, screen shakes horizontally.
					if(r<=0.5) offset = new Point(0,(int)shakeIntensity); // If r is less than 0.5, screen shakes vertically.
					
					// Adding the player hit particle effect.
					double x = p.x; // x position of the particle effect.
					double y = p.y+t.hitboxHeight/2; // y position of the particle effect.
					Point2D f = new Point2D.Double(-1,0); // The direction of the initial force.
					
					// Setting the direction of the force based on the direction of the motion of the enemy knife.
					if(t.dir==2) {
						x += t.hitboxWidth/2;
						f.setLocation(0,-1); // If the knife came from above, the initial force acts above.
					}else if(t.dir==-1) {
						x+=p.hitboxWidth; 
						f.setLocation(1,0); // If the knife came from the left, the inital force acts right.
					}
					
					effects.add(new particles(x,y,10,f,2,10));
					effects.get(effects.size()-1).setColor(Color.red);
					
					enemKniv.remove(t); // The knife is destroyed.
				}
				t.fill(g2); // Rendering the knife.
			}
			
			// Rendering the targets.
			for(target t : targets) {
				t.fill(g2); // Rendering the targets.
				if(t.c.equals(Color.yellow)) { // Adding glow to the targets if they are of yellow color.
					glowRect(t.x+offset.x,t.y+offset.y,t.hitboxHeight,t.hitboxWidth,g2);
				}else {t.show(g2);}
			}
			
			// If the health of the player is 0, or if the player has died.
			if(health==0) {
				if(score>best)best = score; // The best score is updated (If the current score is greater than the best score)
				if(players.contains(name)) { // If the player is loaded from the data file, the loaded best score is also changed.
					bestScores.set(players.indexOf(name), best);
				}else{
					// If not, the player is added to the loaded list.
					players.add(name);
					bestScores.add(best);
				}
				
				// The game is saved if not already.
				if(!saved) {
					save();
					saved = true;
				}
			}
			if(health<=0)gameOver = true; // Game is set to over/finished.
			
			// Rendering the platforms.
			for(tile t : platforms) {
				t.fill(g2);
				t.show(g2);
			}
			
			// Displaying the score text at the top of the screen.
			g2.setFont(new Font("Pixelated Regular", Font.BOLD, (int)sSize));
			g2.setColor(Color.white);
			drawString(new Rectangle2D.Double(offset.x+ssOff.x,offset.y+ssOff.y,size.width,size.height), score+"", 35 + sSize/3 + offset.y + ssOff.y, g2);
			
			// Displaying the Health text and Best score text on the game screen.
			g2.setFont(new Font("Pixelated Regular",  Font.BOLD, 40));
			int tdisplay = (int)health;
			if(health<0)tdisplay = 0;
			g2.drawString("Health : "+tdisplay, 40 + offset.x, 50 + offset.y); // Rendering the health text.
			g2.drawString("Best : "+best, size.width-200 + offset.x, 50 + offset.y); // Rendering the best score text.
			
			// Rendering the emp meter at the bottom of the screen.
			g2.setColor(Color.black);
			Rectangle2D tdraw = new Rectangle2D.Double(size.width/5 + offset.x, size.height - 75 - 35 + offset.y, size.width*3/5, size.height/20);
			g2.draw(tdraw); // Drawing the container.
			g2.fill(new Rectangle2D.Double(tdraw.getX(), tdraw.getY(), tdraw.getWidth()/empl * empm, tdraw.getHeight())); // Showing the progress.
			
			if(empav) {
				// Displaying message on the EMP bar.
				g2.setColor(new Color(255,255,255,(int)empmsga)); // Setting the alpha of the color of the graphics 2d object to the empmsga (To get the 
				g2.setFont(new Font("Pixelated Regular", Font.BOLD, (int)tdraw.getHeight()));
				String t = "EMP is ready for use."; // If EMP is charged, it will display this flashing message.
				if(emp) t = "EMP is being used."; // If EMP is being used, it will display this flashing message.
				
				// Displaying the string on the screen.
				drawString(tdraw, t, (int)(tdraw.getY() + offset.y + (tdraw.getHeight()/2) + (g2.getFont().getSize())/3), g2);
				
				// Increasing or decreasing the msg text alpha based on teh empai boolean (To create the flashing effect)
				if(empai) empmsga+=5;
				else empmsga -= 5;
				if(empmsga>=255) {
					empai = false;
					empmsga=255;
				}
				if(empmsga<=0) {
					empai = true;
					empmsga = 0;
				}
				
				// If the emp is fully charged, the option to press "F" will appear in the circle beside the charging rectangular box.
				Ellipse2D circle = new Ellipse2D.Double(tdraw.getX() + offset.x+tdraw.getWidth()+25, tdraw.getY()-12.5 + offset.y, 60,60);
				g2.setColor(Color.black);
				g2.draw(circle);
				String td = "F";
				if(emp) {
					td = ""+(int)((empTimeL - empi)/100);
				}
				
				// Displaying the text to be drawn inside the circle.
				drawString(new Rectangle2D.Double(circle.getX(), circle.getY(), circle.getWidth(), circle.getHeight()), td, -1, g2);
			
			}
			
			// Showing the "Enemy knives are approaching message.
			if( (showMsg) && (health>0) ) {
				if(startMsg==0)startMsg = System.currentTimeMillis(); // Set the starting time for when the message appears.
				g2.setColor(new Color(0,0,0,150));
				g2.fill(new Rectangle2D.Double(0,0,size.width, size.height)); // Darkening the background.
				
				// 
				double w = size.width/2;
				double h = size.height/2;
				if(w>350) w = 350;
				if(h>225) h = 225;
				
				// Drawing the overlay warning message displaying that the enemy knives are approaching after starting a new game.
				g2.setColor(Color.white);
				g2.setFont(new Font("Arial", Font.BOLD, 40));
				g2.drawString("Enemy knives are", (int)(size.width/2 - w/2 + 10), (int)(size.height/2 - 20));
				g2.drawString("approaching", (int)(size.width/2 - 120), (int)(size.height/2) + 20);
				
				// If the time for the message to appear for is up, then it is disabled and the enemy knives are enabled.
				if(System.currentTimeMillis()-startMsg>=2500) {
					showMsg = false;
					enemKnives = true;
				}
			}
			
			// Displaying the end game screen if the player has died.
			if(health<=0) {
				g2.setFont(new Font("Pixelated Regular",  Font.BOLD, 40)); // Setting the font.
				
				// Darkening the background.
				g2.setColor(new Color(0,0,0,150));
				g2.fill(new Rectangle2D.Double(0,0,size.width,size.height));
				g2.setColor(Color.black);
				
				// Drawing the leaderboard.
				Rectangle2D r = new Rectangle2D.Double(size.width/2 - 300/2, size.height/2 - 400/2, 300, 400); // The position and dimensions of the leaderboard.
				g2.fill(r);
				
				// Converting the bestScores array list to an array (for sorting)
				int[] bs = new int[bestScores.size()];
				for(int i = 0; i<bestScores.size(); i++) {
					bs[i] = bestScores.get(i);
				}
				Arrays.sort(bs);
				ArrayList<Integer> bsarrlist = new ArrayList<Integer>(); // The sorted array is converted to this array list.
				for(int b : bs)bsarrlist.add(b);
				
				// Displaying the contents of the leaderboard.
				for(int i = (int)r.getHeight()/50 - 2; i>=0; i--) { // Looping through each available spot in the leaderboard.
					
					// Highlighting the current player's ranking.
					if(i == (int)r.getHeight()/50 - 2) g2.setColor(new Color(100,100,100));
					else g2.setColor(new Color(50,50,50));
					
					Rectangle2D td = new Rectangle2D.Double(r.getX()+10, r.getY()+5+i*55, r.getWidth()-20, 50); // Dimensions of the slot in the leaderboard.
					g2.fill(td);
					
					String tdis = "-"; // Test to be displayed on the leaderboard.
					String beststr = ""; // The best score string of the player (whose ranking is to be displayed)
					
					// Setting the text to display.
					
					// If a player with the ranking (to show) exists, the text to display is set to his/her information.
					if(bs.length>i) { 
						tdis = (i+1)+" "+players.get(bestScores.indexOf(bs[bs.length-1-i]));
						String toAdd = ""+bs[bs.length-1-i];
						if(i == (int)r.getHeight()/50 - 2) toAdd = ""+best;
						beststr = toAdd;
					}
					
					// If the current slot to display information is the last one (where the current player's information is to be displayed), then the text is set to the current player's information.
					if(i == (int)r.getHeight()/50 - 2) {
						tdis = ((bsarrlist.size()-bsarrlist.indexOf(best)))+" "+name;
						String toAdd = ""+best;
						beststr = toAdd;
					}
					
					// Drawing the text.
					g2.setColor(Color.white);
					g2.drawString(tdis, (int)td.getX()+5, (int)td.getMaxY()-5); // Drawing the name and rank of the player on the left side.
					
					// Drawing the best score of the player on the right side.
					JLabel temp = new JLabel(beststr);
					temp.setFont(g2.getFont());
					g2.drawString(beststr, (int)(td.getX()+td.getWidth()-temp.getPreferredSize().width), (int)(td.getY()+td.getHeight()/2 + temp.getPreferredSize().height/2));
				}
				
				// Initializing and adding the restart and main menu buttons.
				if(health<=0 && (restart==null || mm == null)){ // Checking if the buttons have not been previously initialized.
					
					// Initializing the restart button.
					restart = new JButton();
					restart.setSize(200,40);
					restart.setLocation(size.width/2 - 250, (int)r.getMaxY()+20);
					initialize("Restart", restart, true,true); // Setting the icons of the restart buttons.
					restart.setBorderPainted(false); // Disabling the showing of border in buttons.
					restart.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							newP = false; // The player is no longer new.
							knifeThrow.p.t.stop(); // The current panel's game loop is stopped.
							gClip.stop(); // The background music is stopped.
							knifeThrow.p = new panel(name, null); // A new game panel is created and set as the content pane of the frame.
							f.setContentPane(knifeThrow.p);
							f.setVisible(true);
						}
					});
					
					// Initializing the main menu button.
					mm = new JButton();
					mm.setSize(250,40);
					mm.setLocation(size.width/2, (int)r.getMaxY()+20);
					initialize("Main Menu", mm, true,true); // Setting the icons of the main menu button.
					mm.setBorderPainted(false); // Disabling the border in the main menu button.
					mm.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							knifeThrow.p.t.stop(); // The game loop of the current panel is stopped.
							knifeThrow.p = null; // The current panel is set to null.
							if(sg!=null)sg.t.stop(); // If a start game panel exists, its loop is stopped.
							knifeThrow.sg = new startGame(false, true); // A new menu panel is created and set as the main content pane of the frame..
							f.setContentPane(knifeThrow.sg); 
							f.setVisible(true);
						}
					});
					
					// Adding the buttons onto the screen.
					this.add(mm);
					this.add(restart); 
				}
			}
			
			// If the game is paused, display the pause menu.
			if(pause) {
				pause(g2);
			}
			
			// If the quitting confirm dialog box is to be displayed, it is displayed.
			if(quitConfirm) {
				confirmDialog(g2);
			}
			
			// If the control menu is to be displayed, it is displayed.
			if(control) {
				controls(g2);
			}

			// If the game has just begun, the sliding animamtion takes place.
			if(mainMenuSS!=null) {
				g2.drawImage(mainMenuSS, ssx,0,null); // Drawing the screen shot of the main menu (taken as argument when the game was started)
				ssx -=ssxv; // The x of the screen shot is changed based on the velocity.
				ssxv+=5; // The x velocity is accelerated.
				if(ssx<=-size.width) { // If the image has moved out of the screen, sliding animation is disabled.
					ssx = 0;
					ssxv = 5;
					mainMenuSS = null;
				}
			}
			
			// Drawing the fps counter in-game.
			g2.setFont(new Font("Arial", Font.BOLD, 20));
			g2.drawString("FPS : "+fps, 10, size.height - 10);
		}
		
		/**
		 * Displaying the pause menu.
		 * @param g The Graphics2D object to display the menu on the screen.
		 */
		public void pause(Graphics2D g) {
			
			// Darkening the background.
			g.setColor(new Color(0,0,0,150));
			g.fill(new Rectangle2D.Double(0,0,size.width,size.height));
			
			// Draws the "Paused" text in the middle of the screen.
			g.setFont(new Font("Pixelated Regular", Font.BOLD, 80));
			g.setColor(Color.white);
			drawString(new Rectangle2D.Double(0,0,size.width,size.height), "Paused", size.height/2 - g.getFont().getSize()/2 + 20, g);
			
			// Setting up the control menu options based on the selected controls.
			int iddis = 0;
			int iden = 1;
			if(!wasd) {iddis = 1;iden = 0;}
			
			cOptions.get(iddis).setEnabled(false);
			cOptions.get(iden).setEnabled(true);
			
			// Adding all the buttons in the panel.
			add(resume);
			add(mainMenu);
			add(controls);
		}
		
		/**
		 * Displays the control menu.
		 * @param g Graphics2D object to draw all the graphics objects on the screen.
		 */
		public void controls(Graphics2D g) {
			removeAll(); // Removing all the objects from the screen.
			draw(g, new JButton[] {resume, mainMenu, controls}); // Drawing button icons already displayed on the screen as background.
			
			//Darkening the background.
			g.setColor(new Color(0,0,0,180));
			g.fillRect(0, 0, size.width, size.height);
			
			for(JButton b : cOptions) panel.this.add(b); // Adding all the control menu options to the panel.
		}
		
		/**
		 * Displays the confirm dialog for quitting the game.
		 * 
		 * @param g Graphics2D object needed to display all the graphics on the screen.
		 */
		public void confirmDialog(Graphics2D g) {
			removeAll(); // Removing all the buttons from the screen.
			
			draw(g, new JButton[] {resume, mainMenu, controls}); // Drawing already present buttons in the background.

			// Darkening the background
			g.setColor(new Color(0,0,0,180));
			g.fillRect(0, 0, size.width, size.height);
			
			// Drawing the dialog box.
			g.setColor(Color.black);
			Rectangle2D dialog = new Rectangle2D.Double(size.width/2 - 125, size.height/2 - 75, 250,150);
			g.fill(dialog);
			
			// Drawing the text on the box.
			g.setColor(Color.white);
			g.setFont(new Font("Pixelated Regular", Font.BOLD, 30));
			String td = "Do you want to";
			String td2 = "quit?";
			drawString(dialog, td, (int)(dialog.getY()+dialog.getHeight()/2 - 35), g);
			drawString(dialog, td2, (int)(dialog.getY()+dialog.getHeight()/2), g);
			
			// Initializing the option buttons.
			
			//Initializing the yes button
			JButton yes = new JButton();
			yes.setSize((int)dialog.getWidth()/2 - 10, 30);
			yes.setLocation((int)(dialog.getX()+5), (int)(dialog.getY()+dialog.getHeight() - 35));
			yes.setFocusable(false);
			yes.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					play("sounds/changeTab.wav"); // Playing the change tab sound effect.
					if(sg!=null)sg.t.stop(); // Stopping the loop of the start game menu (if it already exists)
					sg = new startGame(false, true); // Start game menu is re-initialized.
					f.setContentPane(sg); // It is set as the content pane of the frame.
					f.setVisible(true);
				}
			});
			yes.setBorderPainted(false);
			
			// Drawing and setting the icons of the yes button.
			BufferedImage icon = new BufferedImage(yes.getWidth(), yes.getHeight(), BufferedImage.TYPE_INT_ARGB); // Initializing the icon image.
			Graphics2D g2 = icon.createGraphics();
			
			g2.setColor(new Color(20,20,20)); // Drawing the background
			g2.fill(new Rectangle2D.Double(0,0,icon.getWidth(),icon.getHeight()));
			
			// Drawing the text (which in this case is "Yes")
			g2.setFont(new Font("Pixelated Regular", Font.BOLD, icon.getHeight() ));
			g2.setColor(Color.white);
			drawString(new Rectangle2D.Double(0,0,icon.getWidth(),icon.getHeight()), "Yes", -1, g2);
			yes.setIcon(new ImageIcon(icon));
			
			// Initializing the no button.
			JButton no = new JButton();
			no.setSize((int)dialog.getWidth()/2 - 10, 30);
			no.setLocation((int)(dialog.getX()+5 + dialog.getWidth()/2 - 5), (int)(dialog.getY()+dialog.getHeight() - 35));
			no.setFocusable(false);
			no.setBorderPainted(false);
			no.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					play("sounds/click.wav"); // Playing the click sound effect.
					quitConfirm = false; // Disabling the confirm dialog box.
					pause = true;
					panel.this.removeAll();
					repaint(); // Refreshing the screen.
				}
			});
			
			// Drawing and setting the no button icon, in the same way the yes button icon was initialized, with the only change being the text here is "No".
			BufferedImage icon2 = new BufferedImage(yes.getWidth(), yes.getHeight(), BufferedImage.TYPE_INT_ARGB);
			Graphics2D g22 = icon2.createGraphics();
			g22.setColor(new Color(20,20,20));
			g22.fill(new Rectangle2D.Double(0,0,icon2.getWidth(),icon2.getHeight()));
			g22.setFont(new Font("Pixelated Regular", Font.BOLD, icon2.getHeight() ));
			g22.setColor(Color.white);
			drawString(new Rectangle2D.Double(0,0,icon2.getWidth(),icon2.getHeight()), "No", -1, g22);
			no.setIcon(new ImageIcon(icon2));
			
			// Adding the buttons on the screen.
			this.add(yes);
			this.add(no);
			
		}
		
	}
	
	@SuppressWarnings("serial")
	static class startGame extends JPanel{
		
		/**
		 * This class manages and implements the start game menu and the main menu, the loading a player logic and creating a new player logic.
		 */
		
		boolean sm; // Boolean containing whether the menu is the start menu or the main menu.
		
		// Declaring some JButtons for some options in the menus. 
		JButton newPlayer;
		JButton loadPlayer;
		JButton newGame;
		JButton knifeShop;
		JButton rules;
		JButton information;
		JButton quit;
		
		// Declaring navigation JButtons for knife shop
		JButton right;
		JButton left;
		
		JButton select; // Selects a knife as the current selected knife in the knife shop.
		
		ArrayList<JButton> options = new ArrayList<JButton>(); // array list of options of all the saved players to load a player from.
		
		double selectedK = selectedKnifeIdx; // The selected knife to view in the knife shop.
		
		JTextField tf; // The text field to enter the name of a new player.
		
		boolean add = false; // Boolean storing whether the background is to be darken or not.
		boolean quitConfirm = false; // Boolean storing whether the game quit confirm dialog box is visible or not.
		boolean displayRules = false; // Boolean storing whether the rules are to be displayed or not.
		boolean displayDeveloper = false; // Boolean storing whether information is displayed or not.
		boolean knifeShopSelected = false; // Boolean storing whether the knife shop is selected or not.
		boolean transition = false; // Boolean storing whether the transition is happening in the knife shop or not.
		
		long start = 0; // The time when the "No players found" message starts appearing.
		
		int koff = 0; // The offset of the knives in the knife shop (Used in the transition animation)
		int kov = 10; // The offset velocity of the knives in the knife shop (Used in the transition animation)
		
		ImageIcon background; // The background of the main menu.
		
		// The timer that shows the message that no player has registered yet.
		Timer noone = new Timer(1, new ActionListener() { 
			public void actionPerformed(ActionEvent e) {
				if(start==0) { // If the timer has just started, the start time is set.
					start = System.currentTimeMillis();
					repaint();
				}
				// If the message has stayed up for 3 seconds, the message is disabled and this timer is stopped.
				if(System.currentTimeMillis()-start >= 3000) {
					sg.t.stop();
					sg = new startGame(sm, false); // A new start game menu is created.
					f.setContentPane(sg); // It is set as the frame's content pane.
					f.setVisible(true);
					noone.stop();
				}
			}
		});
		
		// This timer refreshes the screen constantly, also handles some logic like calculating the fps and the transition animation of the knives.
		Timer t = new Timer(1,new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(transition) {
					// If transition is taking place, the k offset is added by the offset velocity.
					if(koff>0)koff+=kov;
					if(koff<0)koff-=kov;
					kov+=8; // The velocity is accelerated.
					
					// If koff is greater than one third the width of the screen, then, transition is set to false.
					if( (koff>0 && koff>=size.width/3) || (koff<0 && koff<=-size.width/3) ) {
						if(koff>0) selectedK-=1;
						if(koff<0) selectedK+=1;
						koff = 0;
						kov = 5;
						transition = false;
					}
				}
				
				// Refreshing and repainting the FPS counter values
				repaint();
				total++;
				if(System.currentTimeMillis()>=prevChecked+1000) {
					fps = total;
					total = 0;
					prevChecked = System.currentTimeMillis();
				}
			}
		});
		
		BufferedImage info = null; // The image to be displayed when the information button is pressed.
		
		/**
		 * This constructor initializes all the buttons of the menu.
		 * 
		 * @param startM Whether the menu is a start menu or main menu.
		 * @param restartM Whether the music is to be restarted or not.
		 */
		public startGame(boolean startM, boolean restartM) {
			// If the menu music is to be restarted, it is.
			if(restartM) {
				try {
					mmClip = loadMusic("sounds/mmbg.wav");
				}catch(Exception e) {}
			}
			mmClip.start();
			
			sm = startM; // Start menu boolean is set.
			
			if(sm) newP = false; // If the start menu is selected, no new player is selected, thus, newP is set to false.
			
			if(p!=null)p.t.stop(); // If the game panel is not null, its game loop is closed and it is set to null.
			p = null;
			
			for(int i = 1; i<f.getKeyListeners().length; i++)f.removeKeyListener(f.getKeyListeners()[i]); // Removing any extra key listeners from the frame.
			f.requestFocus();
			f.addKeyListener(new KeyAdapter() { // Adding key listener for the menu.
				public void keyPressed(KeyEvent e) {
					 // If the esc key is pressed, any menu if selected is escaped.
					if(e.getKeyCode()==KeyEvent.VK_ESCAPE) {
						sg.t.stop();
						sg = new startGame(startM, false);
						f.setContentPane(sg);
						f.setVisible(true);
					}
				}
			});
			
			// Loading resources.
			try {
				background = new ImageIcon("sprites/MainBackground.gif"); // Loading the main menu background gif.
				GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment(); // Loading the font of the game.
				ge.registerFont(font);
				info = ImageIO.read(new File("sprites/Developer.png")); // Loadinig the information image.
			}catch(Exception e) {}

			if(gClip!=null && gClip.isRunning())gClip.stop(); // If the game background music is playing, it is stopped.
			
			// Adding mouse listener which escapes a menu if in a menu mouse is clicked outside of any of the buttons.
			this.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					// Getting an array of components present in the panel.
					Component[] c = sg.getComponents();
					ArrayList<Component> carr = new ArrayList<>();
					for(Component co : c)carr.add(co); 
					
					// Checks if any menu is opened, by checking if the panel contains any buttons from any of the menu.
					if(carr.contains(tf) || (options.size()>0 && carr.contains(options.get(0))) || carr.contains(cOptions.get(0)) ) {
						// If so the menu is exited.
						sg.t.stop();
						sg = new startGame(sm, false);
						f.setContentPane(sg);
						f.setVisible(true);
					}
				}
			});
			
			this.setLayout(null);
			
			// Initializing the control menu options.
			for(int i = 0; i<2; i++) {
				JButton ta = new JButton(); // The button to be added as option.
				
				// Setting the dimensions of the button.
				int width = size.width/2 - 100;
				int height = size.height - 100;
				if(width>300)width = 300;
				if(height>400)height = 400;
				
				int xoff = 30;
				if(i==1)xoff=-xoff;
				
				ta.setSize(width, height);
				ta.setLocation(size.width/2 + (i-1)*ta.getWidth() - xoff, size.height/2 - ta.getHeight()/2);
				ta.setFocusable(false);
				ta.setBorderPainted(false);
				
				// Icons of the JButton.
				BufferedImage icon = new BufferedImage(ta.getWidth(), ta.getHeight(),BufferedImage.TYPE_INT_ARGB);
				BufferedImage ricon = new BufferedImage(ta.getWidth(), ta.getHeight(),BufferedImage.TYPE_INT_ARGB);
				
				// Loading the icons from the sprites folder.
				try {
					
					BufferedImage imc = ImageIO.read(new File("sprites/ads"+(i+1)+".png")); // Loading the image.
					
					Graphics2D g = icon.createGraphics();
					
					// filling the background.
					g.setColor(new Color(255,255,255));
					g.fillRect(0, 0, icon.getWidth(), icon.getHeight());
					
					// Resizing the image
					g.drawImage(imc,0,0,icon.getWidth(),icon.getHeight(),null);
					
					// Drawing the boundary of the icon.
					g.setColor(Color.black);
					g.setStroke(new BasicStroke(5));
					g.draw(new Rectangle2D.Double(2.5,2.5,icon.getWidth()-5,icon.getHeight()-5));
					
					Graphics2D g2 = ricon.createGraphics();
					
					// Filling the background.
					g2.setColor(new Color(205,205,205));
					g2.fillRect(0, 0, icon.getWidth(), icon.getHeight());
					
					// Resizing the image.
					g2.drawImage(imc,0,0,icon.getWidth(),icon.getHeight(),null);
					
					// Drawing the boundary of the icon.
					g2.setColor(Color.black);
					g2.setStroke(new BasicStroke(5));
					g2.draw(new Rectangle2D.Double(2.5,2.5,ricon.getWidth()-5,ricon.getHeight()-5));
					
				}catch(Exception e) {}
				
				// Setting the icon of the buttons.
				ta.setIcon(new ImageIcon(icon));
				ta.setRolloverEnabled(true);
				ta.setRolloverIcon(new ImageIcon(ricon));
				
				// A blank MouseListener is added so that if mouse is pressed inside of the buttons, this listener picks it up instead of the panel's mouse listener.
				ta.addMouseListener(new MouseAdapter() {@Override public void mousePressed(MouseEvent e) {}});
				cOptions.add(ta);
				
			}
			
			// Enabling and disbaling the options based on the selected controls.
			int iddis = 0; // The idx to disable
			int iden = 1; // The idx to enable
			if(!wasd) {iddis = 1;iden = 0;}
			
			cOptions.get(iddis).setEnabled(false);
			cOptions.get(iden).setEnabled(true);
			
			// If action listener is not added, it is added.
			if(cOptions.get(0).getActionListeners().length==0) {
				cOptions.get(0).addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						wasd = true; // Wasd is set as the controls.
						play("sounds/click.wav"); // Playing the click sound.
						
						//Adjusting the buttons accordingly.
						cOptions.get(1).setEnabled(true);
						cOptions.get(0).setEnabled(false);
						if(cplayer!=null)wasds.set(players.indexOf(cplayer), wasd);
						repaint();
					}
				});
			}
			// Same goes for the other option.
			if(cOptions.get(1).getActionListeners().length==0) {
				cOptions.get(1).addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						wasd = false;
						play("sounds/click.wav");
						cOptions.get(0).setEnabled(true);
						cOptions.get(1).setEnabled(false);
						if(cplayer!=null)wasds.set(players.indexOf(cplayer), wasd);
						repaint();
					}
				});
			}
			
			// Initializing the options for the player to select while loading.
			for(int i = 0; i<players.size(); i++) {
				JButton ta = new JButton(); // Button to add.
				
				int by = size.height/2 - (players.size()*40); // Determining the y of the button.
				Dimension bsize = new Dimension(500/60 * (60), 60); // Determining the size of the button.
				if(players.size()*(bsize.height+20)>size.height) { // Adjusting the height of the button according to the size of the screen.
					bsize.height = (size.height - (players.size()*20))/5;
				}
				
				ta.setSize(bsize.width, bsize.height);
				ta.setLocation(size.width/2 - bsize.width/2, by+i*(bsize.height + 20));
				ta.setBorderPainted(false);
				ta.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						play("sounds/changeTab.wav"); // The change tab sound is played.
						newP = false; // New player is set to false since a player is loaded.
						
						cplayer = players.get(options.indexOf(ta)).toLowerCase(); // Current player is set.
						wasd = wasds.get(players.indexOf(cplayer)); // The controls are loaded from the extracted data of the data.txt file
						selectedKnifeIdx = selectedKnives.get(players.indexOf(cplayer)); // The selected knife is loaded from the extracted data of the data.txt file
						
						// The start game object is reinitialized to create the main menu.
						sg.t.stop(); 
						sg = new startGame(false, false);
						f.setContentPane(sg);
						f.setVisible(true);
					}
				});
				ta.addMouseListener(new MouseAdapter() {@Override public void mousePressed(MouseEvent e) {}});
				initialize(players.get(i), ta,true); // The button icons are set by the method initialize().
				addMouse(ta, 2); // The buttons enlarge when mouse is over them, this feature is added to make the buttons feel more interactive.
				options.add(ta); // The button is added to the options.
			}
			
			// Determining the dimensions of the JButton.
			int npw = 350; // The width of the buttons.
			int nph = 40; // The height of the buttons.
			int space = 15; // The space between the buttons.
			int numberOfButtons = 5; // The number of buttons on the screen.
			if(!sm) numberOfButtons = 6;
			
			// Adjusting the dimensions according to the dimensions of the screen.
			if((nph + space)*numberOfButtons>size.height/2 + nph/2) {
				space = 10;
				nph = (size.height/2 + 25 - space*5)/5; 
				npw = 400/50 * nph;
			}
			if(npw>size.width)npw = size.width - 100;

			// Initializing the new player button.
			newPlayer = new JButton();
			newPlayer.setLocation(size.width/2 - npw/2, size.height/2 - nph/2);
			newPlayer.setSize(npw, nph);
			newPlayer.setBorderPainted(false);
			newPlayer.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					play("sounds/click.wav"); // The click sound effect is played.
					add = true; // The add boolean is set to true so that the background is darkened.
					removeAll();
					repaint();
					add(tf); // The text field to enter the name of the new player is added to the game panel.
				}
			});
			initialize("New Player", newPlayer,false); // Initializing the icons of the button.
			addMouse(newPlayer, 1); // Adding the mouse hover effect to the button.
			
			// All the other buttons are initialized in the same way with minor differences.
			
			// Initializing the button to start a new game.
			newGame = new JButton();
			newGame.setLocation(newPlayer.getX(), newPlayer.getY());
			newGame.setSize(npw, nph);
			newGame.setBorderPainted(false);
			newGame.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					play("sounds/gameStart.wav"); // The game start sound effect is played.
					
					// A screenshot of the main menu is made to give as an argument to the recently initialized panel.
					BufferedImage ss = new BufferedImage(size.width,size.height,BufferedImage.TYPE_INT_ARGB);
					Graphics2D g2d = ss.createGraphics();
					g2d.drawImage(background.getImage(),0,0,size.width,size.height,null);
					draw(g2d);
					
					save(); // The game data is saved before starting.
					
					// Initializing the panel and changing the content pane.
					p = new panel(cplayer,ss);
					f.setContentPane(p);
					f.setVisible(true);
				}
			});
			initialize("New Game", newGame,true); // Initializing the icons of the new game button.
			addMouse(newGame, 1); // Adding the mouse hover effect.

			// Initializing the knife shop button.
			knifeShop = new JButton();
			knifeShop.setLocation(size.width/2 - npw/2,  size.height/2 + nph*3/2 + 2*space);
			knifeShop.setSize(npw, nph);
			knifeShop.setBorderPainted(false);
			knifeShop.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					play("sounds/click.wav"); // Playing the click sound effect.
					add = true; // Darkens the background.
					knifeShopSelected = true; // knife shop is enabled.
					removeAll();
					repaint();
					
					// The navigation buttons for the knife shop and the button to select a knife are added to the panel.
					add(left);
					add(right);
					add(select);
				}
			});
			initialize("Knife Shop", knifeShop,true);
			addMouse(knifeShop, 1);
			
			int rulesPlacement = 1; // Number of buttons after which the rules button is places.
			if(!sm) rulesPlacement = 2;
			// Initializing the rules button.
			rules = new JButton();
			rules.setLocation(size.width/2 - npw/2,  size.height/2 + nph/2 + rulesPlacement*(nph+space) + space);
			rules.setSize(npw, nph);
			rules.setBorderPainted(false);
			rules.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					play("sounds/click.wav"); // Playing the click sound effect.
					displayRules = true; // The displaying rules is enabled.
					add = true; // The background is darkened.
					removeAll();
					repaint();
				}
			});
			initialize("Rules", rules,true);
			addMouse(rules, 1);
			
			int informationPlacement = 2; // Number of buttons after which information button is placed.
			if(!sm) informationPlacement = 3;
			// Initializing the information button.
			information = new JButton();
			information.setLocation(size.width/2 - npw/2,  size.height/2 + nph/2 + informationPlacement*(nph+space) + space);
			information.setSize(npw, nph);
			information.setBorderPainted(false);
			information.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					play("sounds/click.wav"); // Playing the click button.
					displayDeveloper = true; // The display information is enabled.
					add = true; // The background is darkened.
					removeAll();
					repaint();
				}
			});
			initialize("Developer", information,true);
			addMouse(information, 1);
			
			// Initializing the quit button.
			quit = new JButton();
			if(sm)quit.setLocation(size.width/2 - npw/2,  size.height/2 + nph/2 + 3*(nph+space) + space);
			else quit.setLocation(size.width/2 - npw/2,  size.height/2 + nph/2 + 4*(nph+space) + space);
			quit.setSize(npw, nph);
			quit.setBorderPainted(false);
			quit.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(sm) {
						// If start menu is selected, then the game is to be exited. 
						quitConfirm = true; // The quit confirm dialog box is enabled.
						add = true; // The background is darkened.
						removeAll();
						repaint();
						play("sounds/dialog.wav"); // The dialog box open sound is played.
					}
					else {
						// If the main menu is selected, the start menu is to be selected.
						sg.t.stop(); // Stopping the game loop
						sg = new startGame(true, false); // Reinitializing the game loop
						f.setContentPane(sg); // It is set as the content pane.
						f.setVisible(true);
						play("sounds/changeTab.wav"); // The change tab sound effect is played.
					}
				}
			});
			String tti = "Quit"; // The text to be displayed on the button.
			if(!sm)tti = "Back";
			initialize(tti, quit,true);
			addMouse(quit, 1);
			
			// The load player button is initialized.
			loadPlayer = new JButton();
			loadPlayer.setLocation(size.width/2 - npw/2, size.height/2 + nph/2 + space);
			loadPlayer.setSize(npw, nph);
			loadPlayer.setBorderPainted(false);
			loadPlayer.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					play("sounds/click.wav"); // The click sound is played.
					add = true; // The background is played.
					removeAll();
					if(options.size()==0) noone.start(); // If no players are in the data file, no players found message is displayed.
					else {
						// Else the options are added.
						repaint();
						for(JButton n : options)startGame.this.add(n);
					}
				}
			});
			initialize("Load Player", loadPlayer,true);
			addMouse(loadPlayer, 1);
			
			// Initializing the new player text field.
			tf = new JTextField("Name"); // The default name is set to name.
			tf.setFont(new Font("Arial", Font.BOLD, 30)); // Setting the font.
			tf.setBackground(Color.white);
			tf.setForeground(Color.gray.darker());
			tf.setColumns(25);
			tf.setSize(500, 50);
			tf.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					play("sounds/changeTab.wav"); // Playing the change tab sound effect.
					newP = true; // Since new player is created, new p is set to true.
					cplayer = tf.getText().toLowerCase(); // Current player is set to the name entered in the text field.
					if(!players.contains(cplayer)) {
						// If the player is not already mentioned in the data file, new default placeholder data is set for it.
						players.add(cplayer);
						bestScores.add(0);
						wasds.add(wasd);
						selectedKnifeIdx = 0;
						selectedKnives.add(selectedKnifeIdx);
					}
					save(); // The new player is saved.
					
					// The menu is switched.
					sg.t.stop();
					sg = new startGame(false, false);
					f.setContentPane(sg);
					f.setVisible(true);
					sg.remove(tf);
				}
			});
			tf.addMouseListener(new MouseAdapter() { @Override public void mousePressed(MouseEvent e) {}});
			
			tf.setLocation(size.width/2 - 250, size.height/2 - 50); // Setting the location of the text field.

			// Initializing the controls button.
			controls = new JButton();
			controls.setLocation(loadPlayer.getX(), loadPlayer.getY());
			controls.setSize(npw, nph);
			controls.setBorderPainted(false);
			// Removing any extra action listener added to the controls button.
			if(controls.getActionListeners().length>0)controls.removeActionListener(controls.getActionListeners()[0]);
			controls.addActionListener(new ActionListener() {
				@Override 
				public void actionPerformed(ActionEvent e) {
					play("sounds/click.wav"); // Playing the click sound effect. 
					add = true; // The background is darkened.
					removeAll();
					for(JButton b : cOptions) startGame.this.add(b); // The options are added to the panel.
					repaint();
				}
			});
			initialize("Controls",controls,true);
			addMouse(controls, 1);
			
			// Initializing the knife shop navigation buttons.
			
			// Initializing the right button.
			right = new JButton();
			right.setSize(80,80);
			right.setLocation(30, size.height/2 - 40);
			right.setBorderPainted(false);
			initializeNavButtons(right, true); // The right button icon is set.
			addMouse(right, 1);
			right.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if(selectedK>0) { 
						transition = true; // Knife transition is set to true.
						koff = 1; // The offset direction is set.
					}
					repaint();
				}
			});
			
			// Initializing the left button.
			left = new JButton();
			left.setSize(80,80);
			left.setLocation(size.width - 130, size.height/2 - 40);
			left.setBorderPainted(false);
			initializeNavButtons(left, false);
			addMouse(left, 1);
			left.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if(selectedK<knifeS.size()/2 - 1) {
						transition = true; // Knife transition is set to true.
						koff = -1; // The offset direction is set.
					}
					repaint();
				}
			});
			
			// Initializing the select knife button.
			select = new JButton();
			select.setSize(200,40);
			select.setLocation(size.width/2 - 100, size.height/2 + (size.height/5 + 75)/2 + 35);
			select.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					select.setSize(200,40); // The size of the button is set to normal.
					select.setLocation(size.width/2 - 100, size.height/2 + (size.height/5 + 75)/2 + 35); // Its location is set to normal.
					select.setIcon(new ImageIcon(resizeImg(((ImageIcon)select.getIcon()).getImage(), select.getWidth(), select.getHeight()))); // Scaling the icon of the button.
					
					// Selecting the currently viewing knife in the knife shop.
					if(selectedK*knifScoreIntervals<=bestScores.get(players.indexOf(cplayer))) {
						selectedKnifeIdx = (int)selectedK;
						selectedKnives.set(players.indexOf(cplayer), selectedKnifeIdx);
					}
				}
			});
			select.setBorderPainted(false);
			initialize("Select", select, true); // The icons for the select button are set.
			addMouse(select, 1); // The mouse hover effect is added.
			select.setDisabledIcon(null); // No disabled icon is set.
			
			// All the buttons are set un-focusable so as to not take focus from the key listener.
			newPlayer.setFocusable(false);
			loadPlayer.setFocusable(false);
			newGame.setFocusable(false);
			controls.setFocusable(false);
			rules.setFocusable(false);
			information.setFocusable(false);
			knifeShop.setFocusable(false);
			right.setFocusable(false);
			left.setFocusable(false);
			select.setFocusable(false);
			
			// If the start menu is there, new player and load player buttons are added.
			if(sm) {
				add(newPlayer);
				add(loadPlayer);
			}
			else{
				// If not main menu buttons are added.
				add(controls);
				if(cplayer!=null)add(newGame);
				add(knifeShop);
			}
			
			// Buttons common to both the menus are added.
			add(rules);
			add(information);
			add(quit);
			
			repaint();
			t.start(); // The loop is started.
		}
		
		/**
		 * This method draws all the graphics on the screen
		 * @param g
		 */
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			
			Graphics2D g2d = (Graphics2D) g;
			g2d.setStroke(new BasicStroke(3));
			g2d.setColor(Color.black);
			
			// Drawing the background image.
			g2d.drawImage(background.getImage(),0,0,size.width,size.height,null);

			if(add) {
				draw(g2d); // Drawing the present buttons' icons on the screen.
				
				// The background is darkened
				g2d.setColor(new Color(0,0,0,200));
				g2d.fill(new Rectangle2D.Double(0,0,size.width,size.height));
				
				if(start!=0) {
					// If the timer noone is running, the "No players found" text is displayed.
					g2d.setColor(Color.white);
					g2d.setFont(new Font("Arial Black", Font.BOLD, 50));
					g2d.drawString("No players found", size.width/2 - 240, size.height/2 - 25);
				}
				
				if(displayRules) {
					// If the display rules is enabled, the text from the rules.txt file is displayed.
					String rules = ""; // The text from the rules file.
					try {
						File rfile = new File("rules.txt"); // Loading the file.
						Scanner s = new Scanner(rfile); // Scanner to read the file.
						// Reading the file.
						while(true) {
							try {
								String str = s.nextLine();
								rules+=str+"\n"; // On each new line an escape character is added to indicate that a new line is to be created.
							}catch(Exception e) {
								break;
							}
						}
						s.close();
						rules.replace("\n",""); // Removing the \n at the end of the text.
					}catch(Exception e) {}
					
					// Drawing the text with the help of the drawString() method.
					g2d.setColor(Color.white);
					g2d.setFont(new Font("Arial Black", Font.PLAIN, 30));
					drawString(rules, new Rectangle2D.Double(size.width/2 - (size.height-100)/2, size.height/2 - (size.height-50)/2, size.height-100, size.height-50), g2d);
				}
				
				if(displayDeveloper) {
					// If the display information is enabled, the information.png image is displayed.
					if(info!=null) {
						// Dimensions of the drawn image.
						int width = info.getWidth();
						int height = info.getHeight();
						
						// Adjusting the dimensions of the image to fit in the window.
						if(width>size.width) {
							width = size.width;
							height = info.getHeight()/info.getWidth() * size.width;
						}
						if(height>size.height) {
							height = size.height;
							width = info.getWidth()/info.getHeight() * size.height;
						}
						
						// The image is drawn.
						g2d.drawImage(info,size.width/2 - width/2, size.height/2 - height/2, width, height, null);
					}
				}
				
				if(quitConfirm) {
					// If the quit confirm dialog is to be displayed, it is displayed.
					confirmDialog(g2d);
				}
				
				if(knifeShopSelected) {
					// If the knife shop is selected, it is shown.
					
					// Boolean whether the currently viewing knife is locked or unlocked.
					boolean currentUnlocked = selectedK*knifScoreIntervals<=bestScores.get(players.indexOf(cplayer));
					
					// Drawing the Best score of the current player at the top of the screen.
					g2d.setColor(Color.white);
					g2d.setFont(new Font("Pixelated Regular", Font.BOLD, 50));
					g2d.drawString("Best : "+bestScores.get(players.indexOf(cplayer)), size.width-200 + offset.x, 50 + offset.y);
					
					// Drawing the knives to the right of the current knife.
					int i = (int)selectedK+1; // The index of the knife after the knife being viewed.
					while(!(i<0 || i>=knifeS.size()/2)) { // the loop runs only if the index is within bounds.
						int x = (int)((i - selectedK) * size.width/3); // The x of the drawn knife from the center of the screen.
						
						BufferedImage td = knifeS.get(i*2); // The knife sprite to display
						if(i*knifScoreIntervals>bestScores.get(players.indexOf(cplayer))) {
							// If the knife is not unlocked, a silhouette of the knife is shown
							td = darkify(td);
						}else td = setOpacity(td, 150); // The opacity of the knife is reduced.
						
						// The dimensions of the drawn image.
						int width = td.getWidth();
						int height = td.getHeight();
						
						// The dimensions are adjusted to implement the sliding animation.
						int toadd = (int)((double)Math.abs(koff)/(size.width/3) * 75);
						if(size.width/2 + x - width/2 + koff > size.width/2 + size.width/3 - width/2) toadd = 0;
						height = (size.height/5) + toadd;
						width = (int)((double)td.getWidth()/td.getHeight() * height);
						
						// If the knife cannot be seen, the loop breaks.
						if(size.width/2 + x - width/2 + koff > size.width) {
							break;
						}
						
						// The sprite is drawn.
						g2d.drawImage(td, size.width/2 + x - width/2 + koff, size.height/2 - height/2, width, height, null);
						i++;
					}
					
					// Getting the array list of all the components present on the panel.
					Component[] c = getComponents();
					ArrayList<Component> carr = new ArrayList<Component>();
					for(Component comp : c) carr.add(comp);
					
					// The main viewing knife sprite.
					BufferedImage main = knifeS.get((int)selectedK*2);
					if(selectedK*knifScoreIntervals>bestScores.get(players.indexOf(cplayer))) {
						// If the main knife is not unlocked, a silhouette of the image is shown.
						main = darkify(main);
					}
					
					int mx = (int)(((int)selectedK - selectedK) * size.width/3); // The x position of the main knife.
					
					// The dimensions of the knife.
					int w = main.getWidth();
					int h = main.getHeight();
					// Adjusting the dimensions to implement the animaition.
					h = size.height/5 + 75 - (int)((double)Math.abs(koff)/(size.width/3) * 75);
					w = (int)((double)main.getWidth()/main.getHeight() * h);
					// Drawing the sprite.
					g2d.drawImage(main, size.width/2 - mx - w/2 + koff, size.height/2 - h/2, w, h, null);
					
					// Drawing the name of the knife.
					g2d.setFont(new Font("Pixelated Regular", Font.BOLD, 40));
					String kname = "-----";
					if(currentUnlocked) kname = KnifeNames.get((int)selectedK);
					drawString(new Rectangle2D.Double(koff,size.height/2 - h/2 - 55, size.width, 45),kname,  size.height/2 - h/2 - 10, g2d);
					
					if(selectedK==selectedKnifeIdx) {
						// If the knife is selected, the select button is colored blue to indicate that the knife is selected.
						
						if(!carr.contains(select)) add(select); // The select button is added.
						select.setEnabled(false);
						
						// Creating the icon.
						BufferedImage di = new BufferedImage(select.getWidth(), select.getHeight(), BufferedImage.TYPE_INT_ARGB);
						Graphics2D gdi = di.createGraphics();
						
						// Filling the blue background.
						gdi.setColor(new Color(135,206,235));
						gdi.fillRect(0,0,di.getWidth(),di.getHeight());
						gdi.setColor(Color.white);
						
						// Drawing the "Select" string on top of the background.
						gdi.setColor(Color.white);
						gdi.setFont(new Font("Arial Black", Font.BOLD, select.getHeight()-5));
						drawString(new Rectangle2D.Double(0,0,select.getWidth(),select.getHeight()), "Select", -1, gdi);
						
						// The image is set as the button's icon.
						select.setDisabledIcon(new ImageIcon(di));
					}
					else if(currentUnlocked) {
						// If the current viewed knife is unlocked, the select button is enabled and is added (if not present).
						if(!carr.contains(select)) add(select);
						select.setEnabled(true);
					}
					else {
						// If the currently viewed knife is not unlocked, the locked text appears and the text indicating how many points are required appears.
						if(carr.contains(select)) remove(select); // The select button is removed if it is present.
						
						// The rectangle bounding the locked text is initialized and filled.
						Rectangle2D r = new Rectangle2D.Double(size.width/2 - 125 + koff,size.height/2 - 40,  250,80);
						g2d.setColor(new Color(255,255,255,100));
						g2d.fill(r);
						
						// The text to be displayed in the box.
						String name = "Locked";
						int nph = (int)r.getHeight();
						
						// Displaying the text.
						g2d.setColor(Color.white);
						g2d.setFont(new Font("Pixelated Regular", Font.BOLD, nph-20));
						drawString(r, name, (int)r.getY()+nph-10, g2d);
						
						// Displaying the amount of points required to unlock the knife.
						g2d.setFont(new Font("Pixelated Regular", Font.BOLD, 35));
						drawString(r, ((int)selectedK*knifScoreIntervals)+" points required", (int)(size.height/2 + h/2 + 10 + g2d.getFont().getSize()/2), g2d);
					
					}
					
					// Drawing the knives to the left of the screen.
					int i2 = (int)selectedK-1; // The idx of the knife preceeding the current knife.
					while(!(i2<0 || i2>=knifeS.size()/2)) { // If the idx is within bounds.
						int x = (int)((i2 - selectedK) * size.width/3); // The x of the to draw knife.
						BufferedImage td = knifeS.get(i2*2); // The knife sprite to draw.
						if(i2*knifScoreIntervals>bestScores.get(players.indexOf(cplayer))) {
							// If the knife is not unlocked, a silhouette of the knife sprite is displayed.
							td = darkify(td);
						}
						td = setOpacity(td, 150); // The opacity of the knife sprite is reduced.
						
						// Dimensions of the to draw image.
						int width = td.getWidth();
						int height = td.getHeight();
						
						// Adjusting the dimensions to fit with the transition animation.
						int toadd = (int)((double)Math.abs(koff)/(size.width/3) * 75);
						if(size.width/2 + x - width/2 + koff < size.width/2 - size.width/3 - width/2) toadd = 0;
						height = (size.height/5) + toadd;
						width = (int)((double)td.getWidth()/td.getHeight() * height);
						
						// The loop breaks if the knife cannot be viewed. (To save cpu power)
						if(size.width/2 + x - width/2 + koff < -width) {
							break;
						}
						
						// The image is rendered.
						g2d.drawImage(td, size.width/2 + x - width/2 + koff, size.height/2 - height/2, width, height, null);					
						i2--;
					}
				}
			}
			
			// Drawing the fps counter on the home screen (Lobby not in game)
			g2d.setFont(new Font("Arial", Font.BOLD, 20));
			g2d.setColor(Color.white);
			g2d.drawString("FPS : "+fps, 10, size.height - 10);
		}
		
		/**
		 * Initializes the icons of the navigation buttons of the knife shop.
		 * @param ti The Button to initialize.
		 * @param isR If the button is to the right or not.
		 */
		public void initializeNavButtons(JButton ti, boolean isR) {
			// Making the button transparent.
			ti.setOpaque(false);
			ti.setContentAreaFilled(false);
			
			// The icon of the button.
			BufferedImage i = new BufferedImage(ti.getWidth(), ti.getHeight(), BufferedImage.TYPE_INT_ARGB);
			Graphics2D gi = i.createGraphics();
			
			// Drawing the background of the icon (which is semi-transparent).
			gi.setColor(new Color(255,255,255,100));
			gi.fillRect(0, 0, i.getWidth(), i.getHeight());
			
			// Drawing the triangle on the icon.
			gi.setColor(Color.white);
			fillTriangle(new Rectangle2D.Double(0 ,0 , ti.getWidth(), ti.getHeight()), gi);
			
			// The icon to set the button to.
			BufferedImage icon = new BufferedImage(ti.getWidth(), ti.getHeight(), BufferedImage.TYPE_INT_ARGB);
			Graphics2D gicon = icon.createGraphics();
			// The icon is flipped if the button is facing the other direction.
			if(!isR) gicon.drawImage(i,icon.getWidth(),0,-icon.getWidth(),icon.getHeight(),null);
			else gicon.drawImage(i,0,0,null);
			
			ti.setIcon(new ImageIcon(icon)); // Set the icon.
		}
		
		/**
		 * Fills a triangle bounded between a rectangle
		 * @param b The bounding box of the rectangle
		 * @param g The Graphics2D object to draw the triangle
		 */
		public void fillTriangle(Rectangle2D b, Graphics2D g) {
			// The points of the triangle.
			Point2D p1 = new Point2D.Double(b.getX()+25, b.getY() + b.getHeight()/2);
			Point2D p2 = new Point2D.Double(b.getX()+b.getWidth() - 15, b.getY() + 15);
			Point2D p3 = new Point2D.Double(b.getX()+b.getWidth() - 15, b.getY()+b.getHeight() - 15);
			
			// The path to draw (which is the triangle)
			Path2D path = new Path2D.Double();
			path.moveTo(p1.getX(), p1.getY());
			path.lineTo(p2.getX(), p2.getY());
			path.lineTo(p3.getX(), p3.getY());
			
			// Filling the path.
			g.fill(path);
		}
		
		/**
		 * This method draws the present buttons' icons on the screen
		 * @param g The Graphics2D object to draw graphics with.
		 */
		public void draw(Graphics2D g) {
			JButton[] cs = new JButton[1];
			if(sm) {
				// Drawing the buttons on the Start Menu.
				cs = new JButton[] {newPlayer, loadPlayer, rules, information, quit};
			}else {
				// Drawing the buttons on the Main Menu (after new player or player selection).
				cs = new JButton[] {newGame, controls, knifeShop, rules, information, quit};
			}
			knifeThrow.draw(g,cs);
		}
		
		/**
		 * This method draws the game quit confirm dialog box.
		 * 
		 * @param g This graphics 2D object is used to render the graphics object.
		 */
		public void confirmDialog(Graphics2D g) {
			// Filling the dialog box.
			g.setColor(Color.black);
			Rectangle2D dialog = new Rectangle2D.Double(size.width/2 - 125, size.height/2 - 75, 250,150);
			g.fill(dialog);
			
			// Printing the second exit button message overlay (Do you want to quit?)
			g.setColor(Color.white);
			g.setFont(new Font("Pixelated Regular", Font.BOLD, 30));
			String td = "Do you want to";
			String td2 = "quit?";
			drawString(dialog, td, (int)(dialog.getY()+dialog.getHeight()/2 - 35), g);
			drawString(dialog, td2, (int)(dialog.getY()+dialog.getHeight()/2), g);
			
			// Initializing the yes button
			JButton yes = new JButton();
			yes.setSize((int)dialog.getWidth()/2 - 10, 30);
			yes.setLocation((int)(dialog.getX()+5), (int)(dialog.getY()+dialog.getHeight() - 35));
			yes.setFocusable(false);
			yes.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					play("sounds/click.wav"); // The click sound effect is played.
					if(cplayer!=null)save(); // The game is saved.
					System.exit(0); // The program is closed.
				}
			});
			yes.setBorderPainted(false);
			
			// Creating the yes icon image.
			BufferedImage icon = new BufferedImage(yes.getWidth(), yes.getHeight(), BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2 = icon.createGraphics();
			
			// Filling the background of the icon image.
			g2.setColor(new Color(20,20,20));
			g2.fill(new Rectangle2D.Double(0,0,icon.getWidth(),icon.getHeight()));
			
			// Drawing the "Yes" text on the icon image.
			g2.setFont(new Font("Pixelated Regular", Font.BOLD, icon.getHeight() ));
			g2.setColor(Color.white);
			drawString(new Rectangle2D.Double(0,0,icon.getWidth(),icon.getHeight()), "Yes", -1, g2);
			yes.setIcon(new ImageIcon(icon)); // Setting the icon image as the button icon.
			
			// Initializing the no button.
			JButton no = new JButton();
			no.setSize((int)dialog.getWidth()/2 - 10, 30);
			no.setLocation((int)(dialog.getX()+5 + dialog.getWidth()/2 - 5), (int)(dialog.getY()+dialog.getHeight() - 35));
			no.setFocusable(false);
			no.setBorderPainted(false);
			no.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					play("sounds/click.wav"); // Plays the click sound effect.
					quitConfirm = false; // The game quit confirm dialog is disabled.
 					
					// A start game object is created and the frame content pane is set to the new object.
					sg.t.stop();
					sg = new startGame(sm, false);
					f.setContentPane(sg);
					f.setVisible(true);
				}
			});
			
			// Creating the no button icon image.
			BufferedImage icon2 = new BufferedImage(yes.getWidth(), yes.getHeight(), BufferedImage.TYPE_INT_ARGB);
			Graphics2D g22 = icon2.createGraphics();
			
			// Filling the background.
			g22.setColor(new Color(20,20,20));
			g22.fill(new Rectangle2D.Double(0,0,icon2.getWidth(),icon2.getHeight()));
			
			// Drawing the "No" text on the no button icon image.
			g22.setFont(new Font("Pixelated Regular", Font.BOLD, icon2.getHeight() ));
			g22.setColor(Color.white);
			drawString(new Rectangle2D.Double(0,0,icon2.getWidth(),icon2.getHeight()), "No", -1, g22);
			no.setIcon(new ImageIcon(icon2)); // Setting the button icon as the icon image.
			
			// Adding the buttons to the panel.
			add(yes);
			add(no);
			
		}
		
		/**
		 * Reduces the opacity of a bufferedImage by a given amount.
		 * @param i The bufferedImage to operate upon.
		 * @param alpha The number to reduce the alpha by.
		 * @return The BufferedImage with reduced opacity.
		 */
		public BufferedImage setOpacity(BufferedImage i, int alpha) {
			//Initializing the return image.
			BufferedImage ret = new BufferedImage(i.getWidth(), i.getHeight(), BufferedImage.TYPE_INT_ARGB);
			
			// The transparent input image is drawn on a green background so as to differentiate between what is transparent and what isnt.
			BufferedImage ref = new BufferedImage(i.getWidth(), i.getHeight(), BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = ref.createGraphics();
			g.setColor(Color.green);
			g.fillRect(0, 0, ref.getWidth(), ref.getHeight());
			g.drawImage(i,0,0,null);
			
			// The ref image is used to check whether a pixel is transparent or not, if it isnt, its color in the return image is set to the color of that pixel in the input image but with lower opacity.
			for(int x = 0; x<i.getWidth(); x++) {
				for(int y = 0; y<i.getHeight(); y++) {
					Color c = new Color(ref.getRGB(x, y));
					if(!(c.getRed()==0 && c.getGreen()==255 && c.getBlue()==0)) {
						Color cc = new Color(i.getRGB(x,y));
						ret.setRGB(x,y,new Color(cc.getRed(), cc.getGreen(), cc.getBlue(), cc.getAlpha()-alpha).getRGB());
					}
				}
			}
			
			// The return image is returned.
			return ret;
		}
		
		/**
		 * This method initializes the icons of the main menu buttons.
		 * 
		 * @param name The text to be displayed in the button.
		 * @param newPlayer The button whose icons are to be initialized.
		 * @param border Boolean whether the button has border or not.
		 */
		public void initialize(String name, JButton newPlayer, boolean border) {
			// The button is made transparent.
			newPlayer.setOpaque(false);
			newPlayer.setContentAreaFilled(false);
			
			// The dimensions of the button/
			int npw = newPlayer.getWidth();
			int nph = newPlayer.getHeight();
			
			// The icon image is initialized.
			BufferedImage ic = new BufferedImage(npw,nph,BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = ic.createGraphics();
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			
			// The background of the icon image is drawn.
			g.setColor(new Color(255,255,255,100));
			g.fill(new Rectangle2D.Double(0,0,ic.getWidth(),ic.getHeight()));
			
			// The text to be displayed is drawn on the icon image.
			g.setColor(Color.white);
			g.setFont(new Font("Arial Black", Font.BOLD, nph-5));
			drawString(new Rectangle2D.Double(0,0,npw,nph), name, -1, g);
			
			// The icon image is set as icon.
			newPlayer.setIcon(new ImageIcon(ic));
			newPlayer.setDisabledIcon(new ImageIcon(ic));
			
		}
		
		/**
		 * This produces a black silhouette of a buffered image.
		 * @param img The bufferedImage to produce the silhouette of.
		 * @return The sillouette.
		 */
		public BufferedImage darkify(BufferedImage img) {
			// The return image.
			BufferedImage ret = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
			
			// The transparent image given as input is drawn over a green background so as to easily differentiate between which pixels are transparent and which are not.
			BufferedImage ref = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
			Graphics2D refg = ref.createGraphics();			
			refg.setColor(Color.green);
			refg.fillRect(0,0,ref.getWidth(),ref.getHeight());
			refg.drawImage(img,0,0,null);
			
			// The color of each pixel of the ref image is checkes, if it is not green (means if it not transparent) the color of that pixel in the return image is set to black.
			for(int x = 0; x<ref.getWidth(); x++) {
				for(int y = 0; y<ref.getHeight(); y++) {
					Color c = new Color(ref.getRGB(x, y));
					if(!(c.getRed()==0 && c.getGreen()==255 && c.getBlue()==0)) {
						ret.setRGB(x, y, new Color(255,255,255,180).getRGB());
					}
				}
			}
			
			return ret;
		}
		
		/**
		 * Adds the mouse hover effect to the buttons.
		 * @param newPlayer The button to add the effect too.
		 * @param maxL The max number of mouseListeners on the button.
		 */
		public void addMouse(JButton newPlayer, int maxL) {
			// Dimensions of the button.
			int npw = newPlayer.getWidth();
			int nph = newPlayer.getHeight();
			
			// If the number of mouse listeners added to the button is less than the maximum mouse listeners taken as argument.
			if(newPlayer.getMouseListeners().length<maxL+1) {
				newPlayer.addMouseListener(new MouseAdapter() {
					public void mouseEntered(MouseEvent e) {
						if(newPlayer.isEnabled()) {
							// If the button is enabled, the size of the new player is increased, and its location is updated accordingly.
							newPlayer.setSize(npw+20, nph+(int)((double)nph/npw * 20));
							newPlayer.setLocation(newPlayer.getX()-10, newPlayer.getY()-(int)((double)nph/npw * 10));
							
							// The icon image of the button is also rescaled.
							Image i = ((ImageIcon)newPlayer.getIcon()).getImage();
							newPlayer.setIcon(new ImageIcon(resizeImg(i, npw+20,nph+(int)((double)nph/npw * 20))));
						}
					}
					
					public void mouseExited(MouseEvent e) {
						if(newPlayer.isEnabled()) {
							// If the button is enabled, the size of the new player is decreased, and its location is set accordingly.
							newPlayer.setSize(npw,nph);
							newPlayer.setLocation(newPlayer.getX()+10, newPlayer.getY()+(int)((double)nph/npw * 10));
							
							// The icon image of the button is also rescaled.
							Image i = ((ImageIcon)newPlayer.getIcon()).getImage();
							newPlayer.setIcon(new ImageIcon(resizeImg(i, npw, nph)));
						}
					}
				});
			}
		}
		
		/**
		 * This method rescales a bufferedImage.
		 * @param i The image to scale
		 * @param w The new width
		 * @param h The new height
		 * @return
		 */
		public BufferedImage resizeImg(Image i, int w, int h) {
			// The return image.
			BufferedImage ret = new BufferedImage(w,h,BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = ret.createGraphics();
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			// The input image is drawn on the return image with the given width and height and the image is returned..
			g.drawImage(i,0,0,w,h,null);
			return ret;
		}
	}
	
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
