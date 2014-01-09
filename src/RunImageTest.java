import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

public class RunImageTest {

	//private static final int EDGE_DETECT_SENSITIVITY = 430;
	private static final int LUMINANCE_THRESHOLD = 198;
	
    public static void main(String[] args) 
    {
    	//In order to get OpenCV working we need to import the native library necessary
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        
        //Now call the image generation algorithm
        generateLineDetectImage("E:/Robotics/ImageProcessing/TestImage.png");
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
    	Mat luminanceChart = new Mat(imgMatrix.rows(), imgMatrix.cols(), CvType.CV_8SC1, new Scalar(256));
    	
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
    	//PERFORM EDGE DETECTION ON NEW IMAGE
    	
    	//PERFORM LINE DETECTION ON NEW IMAGE
    	
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
