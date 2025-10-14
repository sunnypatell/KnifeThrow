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

	@SuppressWarnings("serial")
/**
 * Core game panel responsible for rendering and updating gameplay.
 * <p>This is a direct extraction of the legacy panel logic with no runtime
 * changes so the refactored project feels identical.</p>
 */
class panel extends JPanel {
		
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
