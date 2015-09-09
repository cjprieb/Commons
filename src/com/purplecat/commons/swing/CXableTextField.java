package com.purplecat.commons.swing;

import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.UIDefaults;

public class CXableTextField extends JTextField {
	private static Cursor POINTER_CURSOR = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
	
	//painting variables.
	private JButton	mButton				= null;
	private Point 	mXPoint 			= new Point();
	private Cursor	mOldCursor 			= null;	
	private boolean	mShowX				= false;
	
	private MouseListener mMouseListener = new MouseListener() {
		@Override
		public void mouseClicked(MouseEvent e) {
			if ( e.getPoint().x > mXPoint.x ) {
				deleteText();
			}		
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			changeCursor(e.getPoint());
		}

		@Override
		public void mouseExited(MouseEvent e) {
			Point p = e.getPoint();
			if ( p.y < 0 || p.y >= getHeight() || p.x >= getWidth() ) {
				mShowX = false;
				repaint();
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {}

		@Override
		public void mouseReleased(MouseEvent e) {}
	};

	private MouseMotionListener mMouseMotionListener = new MouseMotionListener() {
		@Override
		public void mouseDragged(MouseEvent e) {}

		@Override
		public void mouseMoved(MouseEvent e) {
			changeCursor(e.getPoint());
		}
	};
	
	private MouseListener mButtonMouseListener = new MouseListener() {
		@Override
		public void mouseClicked(MouseEvent e) {
			deleteText();
		}

		@Override
		public void mouseEntered(MouseEvent e) {}

		@Override
		public void mouseExited(MouseEvent e) {}

		@Override
		public void mousePressed(MouseEvent e) {
			deleteText();
		}

		@Override
		public void mouseReleased(MouseEvent e) {}
	};
	
	public CXableTextField(int columns) {
		setup(columns);
	}
	
	public CXableTextField() {
		setup(-1);
	}
	
	private void deleteText() {
		setText("");
		KeyEvent event = new KeyEvent(
				this, 
				KeyEvent.KEY_RELEASED, 
				Calendar.getInstance().getTimeInMillis(), 
				0, 
				KeyEvent.VK_UNDEFINED, 
				KeyEvent.CHAR_UNDEFINED);
		
		for ( KeyListener l : this.getKeyListeners() ) {
			l.keyReleased(event);
		}
	}
	
	private void setup(int columns) {
		this.setColumns(columns);
		
		Insets zeroInsets = new Insets(0, 0, 0, 0);
		mButton	= new JButton("x");
		mButton.setMargin(zeroInsets);
		
		UIDefaults def = new UIDefaults();
		def.put("Button.contentMargins", zeroInsets);
		mButton.putClientProperty("Nimbus.Overrides", def);

		this.addMouseListener(mMouseListener);
		this.addMouseMotionListener(mMouseMotionListener);
		mButton.addMouseListener(mButtonMouseListener);
	}
	
	@Override
	public void paint(Graphics g) {
		g.setFont(mButton.getFont());
		
		Rectangle2D rect	= g.getFontMetrics().getStringBounds(mButton.getText(), g);
		int stringWidth 	= (int)rect.getWidth();
	
		int topOffset 		= 3;
		int rightOffset 	= 3;
		int bottomOffset 	= 3;
		int width			= (int)Math.ceil(stringWidth * 2.5);
		int height			= getHeight() - (bottomOffset + topOffset);			
		int x_coord 		= getWidth() - (rightOffset + width);
		
		mXPoint.x = x_coord;
		
		if ( mShowX ) {
			System.out.println("showing button - x_coord: " + x_coord);
			add(mButton);
			mButton.setBounds(x_coord, topOffset, width, height);
			super.paint(g);
		}
		else {
			remove(mButton);
			super.paint(g);
		}
	}
	
	private void changeCursor(Point e) {
		mShowX = ( e.x > mXPoint.x - 35 );
		
		if ( mShowX  && getCursor() != POINTER_CURSOR ) {
			mOldCursor = getCursor();
			setCursor(POINTER_CURSOR);
			repaint();
		}
		else if ( !mShowX && getCursor() == POINTER_CURSOR ) {
			setCursor(mOldCursor != null ? mOldCursor : Cursor.getDefaultCursor());		
			repaint();	
		}
	}
}
