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
 * Player avatar implementation containing animation and control logic.
 * <p>Everything from the 2022 build has been preserved so that movement,
 * collisions, and state management feel exactly the same.</p>
 */
class player extends tile {
		
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
