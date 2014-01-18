package edu.ahs.raid.tormentum.cameratest.imageprocessing;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

public class ImageProcessor 
{
	private final double CORNER_QUALITY = .25;
	private final int LUMINANCE_THRESHOLD = 252;
	//This helps to ensure corners aren't too close together
	private final double MIN_DISTANCE_BETWEEN_CORNERS = 10;
	
	private static ImageProcessor p = new ImageProcessor();
	
	private ImageProcessor() {}
	
	/**
	 * Get the single instance of the ImageProcessor
	 * @return
	 */
	public static ImageProcessor getInstance()
	{
		return p;
	}
	
	/**
     * Find the corners of the rectangles and show them
     * 
     * @param imageToProcess A matrix from the camera to process
     */
    public Point[] generateLineDetectImage(Mat imageToProcess)
    {
    	/*
    	 * This should isolate the highest luminance HLS values and place
    	 * them into a single channel, then do edge detection and line detection.
    	 */
    	
    	//GENERATE GRAYSCALE IMAGE
    	
    	//Create a pixel matrix for the given image
    	Mat imgMatrix = imageToProcess;
    	//The final parameter here denotes a conversion from an RGB image to Luminance (grayscale)
    	Imgproc.cvtColor(imgMatrix, imgMatrix, Imgproc.COLOR_RGB2GRAY);
    	
    	//Make an black 1-channel matrix for the Luminance channel
    	Mat luminanceChart = new Mat(imgMatrix.rows(), imgMatrix.cols(), CvType.CV_8UC1, new Scalar(0));
    	
    	//Copy luminance values over a certain threshold as a white pixel in the black matrix
    	Imgproc.threshold(imgMatrix, luminanceChart, LUMINANCE_THRESHOLD, 255, Imgproc.THRESH_BINARY);
    	
    	//REMOVE NOISE
    	
    	//To remove noise, we'll find the contours of shapes in the image.
    	ArrayList<MatOfPoint> contours = new ArrayList<MatOfPoint>();
    	Imgproc.findContours(luminanceChart, contours, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
    	
    	//Find two contours with largest area
    	
    	ArrayList<MatOfPoint> largestContours = new ArrayList<MatOfPoint>();
    	for (int area = 0; area < 2; area++)
    	{
    		double largestArea = 0;
    		int largestIndex = 0;
    		int currIndex = 0;
    		
    		for (MatOfPoint contour : contours)
    		{
    			double currArea = Imgproc.contourArea(contour);
    			
    			//If the current contour area is greater than the largest area
    			if (currArea > largestArea)
    			{
    				//Set the current index as the largest one & update largest area
    				largestArea = currArea;
    				largestIndex = currIndex;
    			}
    			
    			currIndex++;
    		}
    		
    		if (contours.size() > 0)
    		{
	    		//Remove the largest contour and add it to the largest contour list
	    		largestContours.add(contours.remove(largestIndex));
    		}
    	}
    	
    	//Only do corner detection if we detected contours
    	if (largestContours.size() > 0)
    	{
	    	//Draw contours onto B/W image, we'll make a new black image for this
	    	Mat largestContourImage = new Mat(luminanceChart.rows(), luminanceChart.cols(), CvType.CV_8UC1, new Scalar(0));
	    	//The third parameter is the contour index to draw (negative means all), the last is a thickness value (Core.FILLED fills the contour area)
	    	Imgproc.drawContours(largestContourImage, largestContours, -1, new Scalar(255), Core.FILLED);
	    	
	    	//We'll blur the result to make it pretty (and make sure edges are relatively smooth)
	    	Imgproc.blur(largestContourImage, largestContourImage, new Size(3,3));
	    	
	    	//DETECT CORNERS
	    	
	    	//Make a new image for the corner detection
	    	MatOfPoint cornerDetection = new MatOfPoint();
	    	//Do corner detection
	    	//8 denotes the function should track the 8 strongest corners
	    	Imgproc.goodFeaturesToTrack(largestContourImage, cornerDetection, 8, CORNER_QUALITY, MIN_DISTANCE_BETWEEN_CORNERS);
	    	Point[] cornersFound = cornerDetection.toArray();
	    	
	    	//We only want to return the corners found if we detect a valid number of corners
	    	if (cornersFound.length == 4 || cornersFound.length == 8)
	    		return cornersFound;
	    	else
	    		return null;
	    	}
    	else
    	{
    		return null;
    	}
    }

	/**
	 * Convert Mat to the swing-compatible BufferedImage
	 * @throws IOException 
	 */
    public BufferedImage matToImage(Mat m) throws IOException
    {
    	//Convert to byte array
    	MatOfByte encodedImage = new MatOfByte();
    	Highgui.imencode(".png", m, encodedImage);
    	
    	//Convert byte array to BufferedImage
    	ByteArrayInputStream s = new ByteArrayInputStream(encodedImage.toArray());
    	BufferedImage b = ImageIO.read(s);
    	
    	return b;
    }
    
    /**
     * Do computations to determine view angle
     * 
     * @param p The points to determine view angle from
     */
    public double getViewAngle(Point[] p)
    {
    	//TODO: Do maths here
    	return 0.0;
    }
}
