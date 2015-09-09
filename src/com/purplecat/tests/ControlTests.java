package com.purplecat.tests;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.purplecat.commons.swing.CXableTextField;
import com.purplecat.commons.swing.Toolbox;

public class ControlTests {
	
	public static void main(String[] args) {
		System.out.println("doing control tests");
		ControlTests tests = new ControlTests();
		tests.setup();
		xableFieldTest(tests);
		tests.display();
	}
	
	private static void xableFieldTest(ControlTests tests) {
		CXableTextField field = new CXableTextField(25);
		field.setText("sample");
		tests._panel.add(field, BorderLayout.PAGE_START);
	}
	
	JFrame _frame = new JFrame();
	JPanel _panel = new JPanel();
	Toolbox _toolbox = new Toolbox();
	
	public void setup() {
		_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		_frame.getContentPane().add(_panel, BorderLayout.CENTER);
		
		Dimension size = new Dimension(300, 100);
		Point center = _toolbox.getCenterScreenPoint(size);
		_frame.setLocation(center.x, center.y);
		
		_panel.setLayout(new BorderLayout());
		_panel.setPreferredSize(size);
	}
	
	public void display() {
		_frame.pack();
		_frame.setVisible(true);
	}	

}
