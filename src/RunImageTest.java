import java.util.ArrayList;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

public class RunImageTest {

	private static final int LUMINANCE_THRESHOLD = 252;
	private static final double CORNER_QUALITY = .25;
	//This helps to ensure corners aren't too close together
	private static final double MIN_DISTANCE_BETWEEN_CORNERS = 10;
	
    public static void main(String[] args) 
    {
    	//In order to get OpenCV working we need to import the native library necessary
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        
        //Now call the image generation algorithm
        //TODO: Change this file location for the test image you're using on your system
        if (args.length == 0)
        	generateLineDetectImage(args[0]);
        else
        	System.out.print("No arguments given! Provide a relative or absolute path to the image file.");
    }
    
    /**
     * Generate an image that shows line detection of the vision targets.
     * 
     * @param imageLocation Location of the source image for target detection
     */
    public static void generateLineDetectImage(String imageLocation)
    {
    	/*
    	 * This should isolate the highest luminance HLS values and place
    	 * them into a single channel, then do edge detection and line detection.
    	 */
    	
    	//GENERATE GRAYSCALE IMAGE
    	
    	//Create a pixel matrix for the given image
    	Mat originImg = Highgui.imread(imageLocation);
    	Mat imgMatrix = new Mat();
    	//The final parameter here denotes a conversion from an RGB image to HLS
    	Imgproc.cvtColor(originImg, imgMatrix, Imgproc.COLOR_RGB2HLS);
    	
    	//Make an black 1-channel matrix for the Luminance channel
    	Mat luminanceChart = new Mat(imgMatrix.rows(), imgMatrix.cols(), CvType.CV_8UC1, new Scalar(0));
    	
    	//Copy luminance values over a certain threshold as a white pixel in the black matrix
    	
    	//Make a white pixel for the high luminance locations
    	byte[] whitePixel = { (byte)255 };
    	
    	for (int row = 0; row < imgMatrix.rows(); row++)
    	{
    		for (int col = 0; col < imgMatrix.cols(); col++)
    		{
    			//If the luminance value is over our certain threshold, set the pixel white
    			if (imgMatrix.get(row,  col)[1] > LUMINANCE_THRESHOLD)
    			{
    				luminanceChart.put(row, col, whitePixel);
    			}
    		}
    	}
    	
    	Highgui.imwrite(generateFileCopyWithExtension(imageLocation, "Lum"), luminanceChart);
    	
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
    		
    		//Remove the largest contour and add it to the largest contour list
    		largestContours.add(contours.remove(largestIndex));
    	}
    	
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
    	
    	//For a test, I'll draw circles around the corners I found in the image
    	for (Point p : cornersFound)
    	{
    		Core.circle(largestContourImage, p, 7, new Scalar(127), 3);
    		Core.circle(originImg, p, 7, new Scalar(0, 255, 0), 3);
    	}
    	
    	//Write result to disk
    	Highgui.imwrite(generateFileCopyWithExtension(imageLocation, "Contour"), largestContourImage);
    	Highgui.imwrite(generateFileCopyWithExtension(imageLocation, "Corners"), originImg);
    }
    
    /**
     * Generates a file output string where the resulting filename is "filename(extension).whatever"
     * 
     * @param location The file location of the image
     * @param extension The extension to the file name you want to add
     * @return A new file location string with the new file name
     */
    public static String generateFileCopyWithExtension(String location, String extension)
    {
    	//Taking off the last four characters removes the file extension
    	return location.substring(0, location.length() - 4) + extension + location.substring(location.length() - 4);
    }

}
