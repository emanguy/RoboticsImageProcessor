package edu.ahs.raid.tormentum.cameratest;

import org.opencv.core.Core;

import edu.ahs.raid.tormentum.cameratest.camera.CameraUpdater;
import edu.ahs.raid.tormentum.cameratest.gui.DataDisplayWindow;
import edu.ahs.raid.tormentum.cameratest.imageprocessing.ImageProcessor;

public class RunImageTest {

	public static void main(String[] args) 
    {
    	//In order to get OpenCV working we need to import the native library necessary
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        
        //Initialize the threads necessary to run the program
        DataDisplayWindow w = new DataDisplayWindow(CameraUpdater.getInstance(),
        		ImageProcessor.getInstance());
        CameraUpdater.getInstance().addListener(w);
        CameraUpdater.getInstance().setImageProcessor(ImageProcessor.getInstance());
        
        Thread cameraThread = new Thread(CameraUpdater.getInstance());
        cameraThread.start();
    }
}
