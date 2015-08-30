package com.purplecat.commons.swing;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

	protected boolean mShowAsButton		= true;
	
	//painting variables.
	private JButton	mButton				= null;
	private Point 	mXPoint 			= new Point();
	private Cursor	mOldCursor 			= null;	
	private boolean	mShowX				= false;
	private boolean mMouseIsHovering	= false;
	
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
	
	private ActionListener mButtonAction = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			deleteText();
		}
	};
	
	public CXableTextField(int columns, boolean displayAsButton) {
		super(columns);
		mShowAsButton = displayAsButton;
		setup();
	}
	
	public CXableTextField(int columns) {
		super(columns);
		setup();
	}
	
	public CXableTextField() {
		setup();
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
	
	public boolean displayAsButton() {
		return(mShowAsButton);
	}
	
	public void setDisplayAsButton(boolean b) {
		mShowAsButton = b;
	}
	
	private void setup() {
		Insets zeroInsets = new Insets(0, 0, 0, 0);
		mButton	= new JButton("x");
		mButton.setMargin(zeroInsets);
		
		UIDefaults def = new UIDefaults();
		def.put("Button.contentMargins", zeroInsets);
		mButton.putClientProperty("Nimbus.Overrides", def);

		mButton.addActionListener(mButtonAction);
		this.addMouseListener(mMouseListener);
		this.addMouseMotionListener(mMouseMotionListener);
	}
	
	@Override
	public void paint(Graphics g) {
		g.setFont(mButton.getFont());
		
		Rectangle2D rect	= g.getFontMetrics().getStringBounds(mButton.getText(), g);
		int stringWidth 	= (int)rect.getWidth();
	
		int topOffset 		= 3;
		int rightOffset 	= 3;
		int bottomOffset 	= 3;
//		int width			= mButton.getPreferredSize().width;
//		int height			= mButton.getPreferredSize().height;
		int width			= (int)Math.ceil(stringWidth * 2.5);
		int height			= getHeight() - (bottomOffset + topOffset);			
		int x_coord 		= getWidth() - (rightOffset + width);
		
		mXPoint.x = x_coord;
		
		if ( mShowX ) {
			if ( mShowAsButton ) {	
				add(mButton);
				mButton.setBounds(x_coord, topOffset, width, height);	
				super.paint(g);
			}
			else {						
				super.paint(g);
			
				g.setColor(mMouseIsHovering ? Color.gray : Color.lightGray);
				g.fillOval(x_coord, topOffset+3, width-1, height-6);				

				g.setColor(Color.black);
				//draw string
				x_coord = getWidth() - (int)Math.ceil(stringWidth * 2) - rightOffset;
				height = (int)(rect.getHeight());
				bottomOffset = height + ( getHeight() - height ) / 2 - 1;
				g.drawString(mButton.getText().toUpperCase(), x_coord, bottomOffset);
			}			
		}
		else {
			remove(mButton);
			super.paint(g);
		}
	}
	
	private void changeCursor(Point e) {
		mMouseIsHovering = ( e.x > mXPoint.x );
		mShowX = ( e.x > mXPoint.x - 35 );
		
		if ( mShowX  && getCursor() != POINTER_CURSOR ) {
			mOldCursor = getCursor();
			setCursor(POINTER_CURSOR);			
		}
		else if ( !mShowX && getCursor() == POINTER_CURSOR ) {
			setCursor(mOldCursor != null ? mOldCursor : Cursor.getDefaultCursor());			
		}
		repaint();
	}
}
