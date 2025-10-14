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
 * Projectile logic for the player's and enemies' knives.
 * <p>Copied verbatim from the original Knife Throw.java so the timing,
 * collisions, and unlock system remain untouched.</p>
 */
class knife extends tile {
		
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
