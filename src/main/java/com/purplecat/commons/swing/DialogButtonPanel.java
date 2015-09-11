package com.purplecat.commons.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.LayoutStyle;

import com.google.inject.Inject;
import com.purplecat.commons.IResourceService;
import com.purplecat.commons.Resources;

public class DialogButtonPanel extends JPanel implements ActionListener {	
	public interface IDialogCloseListener {
		public void dialogClosed(boolean isOkay);
	}
	
	static int COUNT = 0;
	int mIndex = COUNT++;
	
	protected JButton 				mBtnOK 		= null;	
	protected JButton 				mBtnCancel	= null;	
	protected IDialogCloseListener 	mOkAction 	= null;
	
	protected final IResourceService _resources;
	
	@Inject
	public DialogButtonPanel(IResourceService resources) {
		_resources = resources;
		initialize(true);
		build(true);
	}
	
	public void setCloseListener(IDialogCloseListener listener) {
		mOkAction = listener;		
	}
	
	/*public DialogButtonPanel(IDialogCloseListener listener, boolean bOkButton) {
		mOkAction = listener;
		initialize(bOkButton);
		build(bOkButton);
	}*/
	
	protected void initialize(boolean bOkButton) {
		if ( bOkButton ) {
			mBtnOK = new JButton();
			mBtnOK.setText(_resources.getCommonString(Resources.string.lblOkay));
			mBtnOK.addActionListener(this);
		}
		
		mBtnCancel = new JButton();
		mBtnCancel.setText(_resources.getCommonString(Resources.string.lblCancel));
		mBtnCancel.addActionListener(this);		
	}
	
	public JButton getCancelButton() {
		return(mBtnCancel);
	}
	
	public JButton getOkayButton() {
		return(mBtnOK);
	}
	
	protected JButton getRightButton() {
		return(mBtnCancel);
	}
	
	protected JButton getLeftButton() {
		return(mBtnOK);
	}
	
	protected void build(boolean bOkButton) {
		
		if ( bOkButton ) {
			GroupLayout thisLayout = new GroupLayout(this);
			this.setLayout(thisLayout);
			
			thisLayout.setVerticalGroup(thisLayout.createSequentialGroup()
				.addContainerGap()
				.addGroup(thisLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
				    .addComponent(getRightButton(), GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
				    .addComponent(getLeftButton(), GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addContainerGap());
			thisLayout.setHorizontalGroup(thisLayout.createSequentialGroup()
				.addContainerGap(0, Short.MAX_VALUE)
				.addComponent(getLeftButton(), GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED, 1, GroupLayout.PREFERRED_SIZE)
				.addComponent(getRightButton(), GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
				.addContainerGap());
		}
		else {
			this.add(mBtnCancel);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if ( mOkAction != null ) {
			boolean isOkay = (e.getSource() == mBtnOK);
			System.out.println("DIALOG action - " + mIndex + " - " + isOkay);
			mOkAction.dialogClosed(e.getSource() == mBtnOK);
		}
	}

	public static class DialogNextButtonPanel extends DialogButtonPanel {			
		protected JButton 				mBtnNext;	
		protected JButton 				mBtnPrev;	
		
		@Inject
		public DialogNextButtonPanel(IResourceService resources) {
			super(resources);
		}
		
		public void setNextAction(ActionListener action) {
//			mNextAction = action;
			mBtnNext.addActionListener(action);
		}
		
		public void setPreviousAction(ActionListener action) {
//			mPrevAction = action;
			mBtnPrev.addActionListener(action);		
		}
		
		public void showOkBtn(boolean b) {
			mBtnOK.setVisible(b);
		}
		
		public void showPrevBtn(boolean b) {
			mBtnPrev.setVisible(b);
		}
		
		public void showNextBtn(boolean b) {
			mBtnNext.setVisible(b);
		}
		
		@Override
		protected void initialize(boolean bOkButton) {
			super.initialize(bOkButton);
			
			mBtnNext = new JButton();
			mBtnNext.setText(_resources.getString(Resources.string.lblNext));
			
			mBtnPrev = new JButton();
			mBtnPrev.setText(_resources.getString(Resources.string.lblPrevious));
		}
		
		@Override
		protected void build(boolean bOkButton) {
			GroupLayout thisLayout = new GroupLayout(this);
			this.setLayout(thisLayout);
			
			thisLayout.setVerticalGroup(thisLayout.createSequentialGroup()
				.addContainerGap()
				.addGroup(thisLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
				    .addComponent(mBtnCancel, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
				    .addComponent(mBtnOK, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
				    .addComponent(mBtnNext, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
				    .addComponent(mBtnPrev, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addContainerGap());
			thisLayout.setHorizontalGroup(thisLayout.createSequentialGroup()
				.addContainerGap()
				.addComponent(mBtnCancel, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
				.addGap(0, 10, Short.MAX_VALUE)
				.addComponent(mBtnPrev, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
				.addComponent(mBtnNext, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
				.addComponent(mBtnOK, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
				.addContainerGap());
		}
	}

//	@Override
//	public void windowActivated(WindowEvent arg0) {}
//
//	@Override
//	public void windowClosed(WindowEvent e) {
//		System.out.println("WINDOW closed - " + mIndex);
//	}
//
//	@Override
//	public void windowClosing(WindowEvent e) {
//		System.out.println("WINDOW closing - " + mIndex);
//	}
//
//	@Override
//	public void windowDeactivated(WindowEvent e) {}
//
//	@Override
//	public void windowDeiconified(WindowEvent e) {}
//
//	@Override
//	public void windowIconified(WindowEvent e) {}
//
//	@Override
//	public void windowOpened(WindowEvent e) {}
}

