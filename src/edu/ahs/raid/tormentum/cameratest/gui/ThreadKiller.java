package edu.ahs.raid.tormentum.cameratest.gui;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import edu.ahs.raid.tormentum.cameratest.camera.CameraUpdater;

/**
 * Kills the thread when the DataDisplayWindow closes.
 * 
 * @author Evan Rittenhouse
 */
public class ThreadKiller implements WindowListener {

	private CameraUpdater c;
	
	public ThreadKiller(CameraUpdater c)
	{
		this.c = c;
	}
	
	//This is the only function I really care about
	@Override
	public void windowClosing(WindowEvent arg0) 
	{
		c.killThread();
	}
	
	@Override
	public void windowActivated(WindowEvent arg0) {}
	@Override
	public void windowClosed(WindowEvent arg0) {}
	@Override
	public void windowDeactivated(WindowEvent arg0) {}
	@Override
	public void windowDeiconified(WindowEvent arg0) {}
	@Override
	public void windowIconified(WindowEvent arg0) {}
	@Override
	public void windowOpened(WindowEvent arg0) {}
}
