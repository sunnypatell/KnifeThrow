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
 * Particle system responsible for celebratory score effects.
 * <p>Retains the original nested particle class and rendering rules.</p>
 */
class particles {
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
