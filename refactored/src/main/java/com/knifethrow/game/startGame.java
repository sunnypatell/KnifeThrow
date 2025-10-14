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
 * Menu, onboarding, and high-score management panel.
 * <p>All Swing wiring, file I/O, and UI flows are preserved from the 2022
 * release.</p>
 */
class startGame extends JPanel {
		
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
