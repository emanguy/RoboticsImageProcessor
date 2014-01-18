package edu.ahs.raid.tormentum.cameratest.camera;
//TODO: Determine package here

/**
 * This class allows something to listen for CameraUpdateEvents.
 * 
 * @author Evan Rittenhouse
 */
public interface CameraUpdateListener 
{
	/**
	 * Will be called by an event dispatch thread upon an updated processing
	 * of a single camera frame.
	 * 
	 * @param e The CameraUpdateEvent to process.
	 */
	public void frameUpdated(CameraUpdateEvent e);
}
