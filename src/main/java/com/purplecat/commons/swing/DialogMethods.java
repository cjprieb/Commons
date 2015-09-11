package com.purplecat.commons.swing;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Point;
import java.awt.Toolkit;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import javax.swing.JComponent;
import javax.swing.JDialog;

public class DialogMethods {	
	public static void createFrame(JDialog dialog, JComponent panel, DialogButtonPanel btnPanel) {
		if ( panel != null ) {
			dialog.getContentPane().add(panel, BorderLayout.CENTER);
		}
		if ( btnPanel != null ) {
			dialog.getContentPane().add(btnPanel, BorderLayout.PAGE_END);
			dialog.getRootPane().setDefaultButton(btnPanel.getOkayButton());
//			dialog.addWindowListener(btnPanel);
		}
		dialog.pack();
	}	
	
	public static void showFrame(JDialog dialog, Class<?> nodeClass, String nodeKey, boolean enablePrefs) {		
		dialog.setLocation(calculateLocation(dialog, nodeClass, nodeKey, enablePrefs));		
		dialog.setVisible(true);
	}	
	
	public static Point calculateLocation(JDialog dialog, Class<?> nodeClass, String nodeKey, boolean enablePrefs) {		
		Point p = new Point(-1, -1);
		
		if ( nodeKey != null && enablePrefs ) { 
			try {
				Preferences dlgPrefs = Preferences.userNodeForPackage(nodeClass).node(nodeKey);
				if ( dlgPrefs.nodeExists("location") ) {
					p = new Point();
					p.x = dlgPrefs.node("location").getInt("x", -1);
					p.y = dlgPrefs.node("location").getInt("y", -1);				
				}
			} 
			catch (HeadlessException e) {} 
			catch (BackingStoreException e) {}
		}
		
		Dimension dialog_d = dialog.getSize();
		Dimension screen_d = Toolkit.getDefaultToolkit().getScreenSize();
		
		if (( p.x < 0 || p.y < 0 ) && dialog.getParent() != null ) {
			Point parent_p = dialog.getParent().getLocation();
			Dimension parent_d = dialog.getParent().getSize();
			
			p.x = parent_p.x + ( parent_d.width/2 - dialog_d.width/2 );
			p.y = parent_p.y + ( parent_d.height/2 - dialog_d.height/2 );			
		}
		
		if ( p.x < 0 || p.y < 0 ) {	//parent is sized and placed such that dialog lands off-screen; or there is no parent.
			p.x = ( screen_d.width/2 - dialog_d.width/2 );
			p.y = ( screen_d.height/2 - dialog_d.height/2 );			
		}

		return(p);		
	}
	
	public static void closeDialog(JDialog dialog, Class<?> nodeClass, String nodeKey, boolean enablePrefs) {
		if ( nodeKey != null && enablePrefs ) { 
			Preferences dlgPrefs = Preferences.userNodeForPackage(nodeClass).node(nodeKey);
			dlgPrefs.node("location").putInt("x", dialog.getLocation().x);
			dlgPrefs.node("location").putInt("y", dialog.getLocation().y);
		}
		dialog.dispose();		
		
		//or do i want:
		//dialog.setVisible(false);
	}
}
