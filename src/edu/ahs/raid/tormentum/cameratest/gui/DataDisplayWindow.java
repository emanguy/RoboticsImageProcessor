package edu.ahs.raid.tormentum.cameratest.gui;

import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;

import edu.ahs.raid.tormentum.cameratest.camera.CameraUpdateEvent;
import edu.ahs.raid.tormentum.cameratest.camera.CameraUpdateListener;
import edu.ahs.raid.tormentum.cameratest.camera.CameraUpdater;
import edu.ahs.raid.tormentum.cameratest.imageprocessing.ImageProcessor;

/**
 * GUI Which shows the camera processing results
 * 
 * @author Evan Rittenhouse
 */
public class DataDisplayWindow implements CameraUpdateListener
{
	private JFrame frame;
	private JPanel contentPane = new JPanel();
	private JPanel dataDisplayPane = new JPanel();
	
	private GridLayout mainLayout = new GridLayout(2, 1, 10, 10);
	private GridLayout dataLayout = new GridLayout(1, 2, 10, 10);
	
	private JLabel viewAngle = new JLabel("View angle:");
	private JLabel distance = new JLabel("Distance:");
	private JLabel image;
	
	private ImageProcessor imgProc;
	
	/**
	 * Create a DataDisplayWindow which shows the camera image and
	 * image processing results
	 * 
	 * @param c The the frame update thread to kill when this window is closed
	 * @param i The image processor used to do BufferedImage conversions and the like
	 */
	public DataDisplayWindow(CameraUpdater c, ImageProcessor i)
	{
		//Initialize window and determine close functionality
		frame = new JFrame("Camera test");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.addWindowListener(new ThreadKiller(c));
		
		//Set up components of the window
		contentPane.setLayout(mainLayout);
		
		//Try to set the default image
		try 
		{
			image = new JLabel(new ImageIcon(ImageIO.read(new File("NoCamera.png"))));
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		//Add the image display
		contentPane.add(image);
		
		//Set up the data display
		dataDisplayPane.setLayout(dataLayout);
		
		dataDisplayPane.add(viewAngle);
		dataDisplayPane.add(distance);
		
		contentPane.add(dataDisplayPane);
		
		//Set image processor
		this.imgProc = i;
		
		//Add all visible stuff to the window and show it
		frame.setContentPane(contentPane);
		frame.pack();
		frame.setVisible(true);
	}
	
	@Override
	public void frameUpdated(CameraUpdateEvent e) 
	{
		Mat displayMatrix = e.getImage();
		
		//Draw fancy circles around the corners
		if (e.getPoints() != null)
		{
			for (Point p : e.getPoints())
			{
				Core.circle(displayMatrix, p, 5, new Scalar(127), 3);
			}
		}
		
		//For now I'm just going to show the camera image
		BufferedImage displayImage = null;
		
		//Attempt to show the new image frame
		try 
		{
			displayImage = imgProc.matToImage(displayMatrix);
			
			image.setIcon(new ImageIcon(displayImage));
		} 
		catch (IOException e1) 
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		frame.pack();
		
		//TODO: Do math and show results in window
	}
}
