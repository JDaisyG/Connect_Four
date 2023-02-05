import java.io.IOException;
import java.util.Scanner;

import javax.swing.JFrame;

public class Main {
	
  int wins = 0;
  
    public static void main(String[] args) throws IOException {
	
    	// create new grid and call methods in GridDraw
    	
    	GridDraw grid = new GridDraw();
    	JFrame frame = new JFrame("Connect Four");	
        		
       	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(grid);
        frame.pack();
        frame.setLayout(null);
     	frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }
}
