package ru.germanbpopov.chess;

import java.util.Scanner;
import javax.swing.SwingUtilities;

class Main {
	
	
	public static void main(String args[]) {
				
		//SWING CREATION
		View view = new View();
		view.setPresenter(new Presenter(view, new Model()));
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				view.createUI();
			}
		});
		
	}
}