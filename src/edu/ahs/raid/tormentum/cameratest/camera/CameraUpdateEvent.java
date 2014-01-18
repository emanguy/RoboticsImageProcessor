package edu.ahs.raid.tormentum.cameratest.camera;

import org.opencv.core.Mat;
import org.opencv.core.Point;

/**
 * This class is intended to allow a camera updater to post camera frame updates to a 
 * class implementing the CameraUpdateListener interface.
 * 
 * @author Evan Rittenhouse
 *
 */
public class CameraUpdateEvent 
{
	private Mat image;
	private Point[] detectedPoints;
	
	/**
	 * Create a CameraUpdateEvent with a frame but no detected points
	 * 
	 * @param image The frame retrieved from the camera
	 */
	public CameraUpdateEvent(Mat image)
	{
		this.image = image;
		this.detectedPoints = null;
	}
	
	/**
	 * Create a CameraUpdateEvent with a frame and detected points
	 * @param image
	 * @param detectedPoints
	 */
	public CameraUpdateEvent(Mat image, Point[] detectedPoints)
	{
		this.image = image;
		this.detectedPoints = detectedPoints;
	}
	
	/**
	 * Get the image matrix for the processed camera frame
	 * @return A RGB 3-channel matrix containing the image data
	 */
	public Mat getImage()
	{
		return image;
	}
	
	/**
	 * Return the detected points within the camera frame.
	 * 
	 * @return An array of points describing where the corners of the rectangles of the image are.
	 */
	public Point[] getPoints()
	{
		return detectedPoints;
	}
}
