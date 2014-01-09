import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

public class RunImageTest {

	private static final int EDGE_DETECT_SENSITIVITY = 300;
	private static final int LINE_DETECT_SENSITIVITY = 50;
	private static final int LUMINANCE_THRESHOLD = 252;
	
    public static void main(String[] args) 
    {
    	//In order to get OpenCV working we need to import the native library necessary
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        
        //Now call the image generation algorithm
        //TODO: Change this file location for the test image you're using on your system
        generateLineDetectImage(args[0]);
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
    	Mat imgMatrix = Highgui.imread(imageLocation);
    	Imgproc.cvtColor(imgMatrix, imgMatrix, Imgproc.COLOR_RGB2HLS);
    	
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
    	
    	//TODO: Remove this later.
    	Highgui.imwrite(generateFileCopyWithExtension(imageLocation, "Lum"), luminanceChart);
    	
    	//PERFORM EDGE DETECTION ON NEW IMAGE
    	
    	//Create a new matrix for the edge detection
    	Mat edgeMap = new Mat();
    	Imgproc.Canny(luminanceChart, edgeMap, EDGE_DETECT_SENSITIVITY, EDGE_DETECT_SENSITIVITY);
    	Imgproc.blur(edgeMap, edgeMap, new Size(5,5));
    	Highgui.imwrite(generateFileCopyWithExtension(imageLocation, "Edge"), edgeMap);
    	
    	//PERFORM LINE DETECTION ON NEW IMAGE
    	
    	Mat lines = new Mat();
    	Imgproc.HoughLinesP(edgeMap, lines, 1, Math.PI/180, LINE_DETECT_SENSITIVITY);
    	
    	//Iterate through each (x1, y1) (x2, y2) pair and draw the findings onto a new image
    	int currentHue = 255;
    	int hueChange = 127 / lines.cols();
    	
    	for (int line = 0; line < lines.cols(); line++)
    	{
    		//Draw the line
    		Point lineStart = new Point(lines.get(0, line)[0], lines.get(0, line)[1]);
    		Point lineEnd = new Point(lines.get(0, line)[2], lines.get(0, line)[3]);
    		Core.line(edgeMap, lineStart, lineEnd, new Scalar(currentHue));
    		
    		currentHue -= hueChange; //Change the hue slightly for every line
    	}
    	
    	
    	//DRAW LINES ON NEW IMAGE
    	
    	//WRITE NEW IMAGE TO DISK
    }
    
    /**
     * Generates a file output string where the resulting filename is "filename(extension).whatever"
     * 
     * @param location The file location of the image
     * @param extension 
     * @return
     */
    public static String generateFileCopyWithExtension(String location, String extension)
    {
    	//Taking off the last four characters removes the file extension
    	return location.substring(0, location.length() - 4) + extension + location.substring(location.length() - 4);
    }

}
