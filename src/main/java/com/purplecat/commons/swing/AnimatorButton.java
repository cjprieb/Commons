package com.purplecat.commons.swing;

import com.google.inject.Inject;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


/* 
 * A template for animation applications.
 */
public class AnimatorButton extends WindowAdapter implements ActionListener {
	protected int 		mFrameNumber 	= -1;
    protected Timer 	mTimer			= null;
    protected JButton   mButton         = null;
    protected boolean	mStarted		= false;

    protected final IImageRepository _images;

    @Inject
    public AnimatorButton(IImageRepository images, JButton button) {
    	_images = images;
    	
    	mButton = button;
        mButton.setIcon(_images.getImage("process-done.png"));
        

        //Set up a timer that calls this object's action handler.
        int delay = 50;
        mTimer = new Timer(delay, this);
        mTimer.setInitialDelay(0);
        mTimer.setCoalesce(true);
    }
    
    public JButton getComponent() {
    	return mButton;
    }

    public void startAnimation() {
    	mFrameNumber = 0;
        //Start animating!
    	if ( !mStarted ) {
	    	mStarted = true;
	        mTimer.start();
    	}
    }

    public void stopAnimation() {
        //Stop the animating thread.
    	if ( mStarted ) {
    		mStarted = false;
    		mTimer.stop();
            mButton.setIcon(_images.getImage("process-done.png"));
    	}
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        //Advance the animation frame.
    	mButton.setIcon(_images.getTimerIcon("process-working.png", mFrameNumber));
        mFrameNumber++;
    }
    
	@Override
    public void windowIconified(WindowEvent e) {
        stopAnimation();
    }
	
	@Override
    public void windowDeiconified(WindowEvent e) {
        startAnimation();
    }
	
	@Override
    public void windowClosing(WindowEvent e) {}    
}