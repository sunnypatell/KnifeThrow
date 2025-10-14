package com.knifethrow.game;

import static com.knifethrow.game.knifeThrow.*;

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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import javax.imageio.ImageIO;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 * Legacy physics tile base class extracted from the 2022 Knife Throw monolith.
 * <p>This file was generated during the 2024 archival refactor so the original
 * behaviour could be preserved while giving each class its own source file.</p>
 */
class tile {

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
