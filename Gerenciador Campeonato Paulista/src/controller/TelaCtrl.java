package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;

public class TelaCtrl implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		
		JComponent c = (JComponent) e.getSource();
		
		if(c.getName().equals("group generator")) {
			System.out.println("yeh");
		}
	}
	
	//public void popularTabelas () {

}
