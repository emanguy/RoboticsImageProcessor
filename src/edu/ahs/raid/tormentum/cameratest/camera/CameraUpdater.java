package edu.ahs.raid.tormentum.cameratest.camera;

import java.util.ArrayList;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.highgui.VideoCapture;

import edu.ahs.raid.tormentum.cameratest.imageprocessing.ImageProcessor;

/**
 * This class contains code for the thread which reads from the camera and
 * fires an event to the Swing interface whenever it processes and validates the points on a detected rectangle.
 * 
 * @author Evan Rittenhouse
 *
 */
public class CameraUpdater implements Runnable 
{
	private VideoCapture camera;
	private ImageProcessor imgProc;
	private static CameraUpdater instance = new CameraUpdater();
	
	private ArrayList<CameraUpdateListener> listenerList;
	private boolean threadRunning;
	
	private CameraUpdater()
	{
		camera = new VideoCapture(0);
		listenerList = new ArrayList<CameraUpdateListener>();
		threadRunning = false;
	}
	
	@Override
	public void run() 
	{
		//We use threadRunning to stop our thread outside the run() function
		threadRunning = true;
		
		while (threadRunning)
		{
			//Run the synchronized thread process
			doProcess();
			
			//Allow other parts of the program to process before reading the next frame
			try 
			{
				Thread.sleep(100);
			} 
			catch (InterruptedException e1) 
			{
				e1.printStackTrace();
			}
		}
	}
	
	private synchronized void doProcess()
	{
		Mat imgMatrix = new Mat(); //Create a matrix for the camera to grab
			
		//Grab a frame from the camera
		camera.read(imgMatrix);
		//Process the image and create the necessary event based on the outcome
		Point[] detectedCorners = imgProc.generateLineDetectImage(imgMatrix);
		CameraUpdateEvent e = (detectedCorners == null)? new CameraUpdateEvent(imgMatrix) : new CameraUpdateEvent(imgMatrix, detectedCorners);
		
		//Update listeners
		for (CameraUpdateListener l : listenerList)
		{
			l.frameUpdated(e);
		}
	}
	
	/**
	 * Set the image processor of this CameraUpdater
	 * 
	 * @param p The object containing the image processing functionality
	 */
	public synchronized void setImageProcessor(ImageProcessor p)
	{
		this.imgProc = p;
	}
	
	/**
	 * Add a CameraUpdateListener to notify when a new frame has been processed.
	 * 
	 * It is safe to use this function while the thread is running, the synchronized
	 * part makes this possible.
	 * 
	 * @param l The CameraUpdateListener to add
	 */
	public synchronized void addListener(CameraUpdateListener l)
	{
		listenerList.add(l);
	}
	
	/**
	 * Call to remove a CameraUpdateListener from the list.
	 * 
	 * Again, it is safe to use this function while the thread is running.
	 * 
	 * @param l The CameraUpdateListener to remove.
	 */
	public synchronized void removeListener(CameraUpdateListener l)
	{
		listenerList.remove(l);
	}

	/**
	 * Get the CameraUpdater instance.
	 * @return The private CameraUpdater instance.
	 */
	public static CameraUpdater getInstance()
	{
		return instance;
	}
	
	/**
	 * Allow something to stop the thread loop.
	 */
	public synchronized void killThread()
	{
		threadRunning = false;
	}
}
