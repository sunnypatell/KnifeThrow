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
 * Target entity that players throw knives at to score points.
 * <p>This class mirrors the 2022 implementation to guarantee identical
 * spawn, reward, and despawn behaviour.</p>
 */
class target extends tile {
		
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
