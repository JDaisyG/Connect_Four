
import javax.swing.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;


public class GridDraw extends JPanel implements ActionListener{
	
	private final BufferedImage gameBoard;
	private final BufferedImage click;
	private final BufferedImage gameOver;
	private final BufferedImage replay;

	private JButton[] buttons;
	private JButton replayButton;
	
	private byte turn = 1;
	private byte winner = 0;
	
	private Timer timer = new Timer(1,
			(e) -> {
				repaint();
			}
	);
	
	private int xPos = 66;
	private int yPos = 100;
	
	private byte[][] grid = new byte[6][7];
	
	public GridDraw() throws IOException{
		
		setPreferredSize(new Dimension(500, 500));
		setBackground(Color.WHITE);
		setLayout(null);
		
		/* Image sources
		 * gameBoard: https://cgi.csc.liv.ac.uk 
		 * click: https://www.flaticon.com/free-icon/click_1536475
		 * gameOver: https://pngimg.com/image/83368
		 * replay: https://www.pngkey.com/detail/u2w7r5a9q8w7q8e6_replay-button-play-again-button-pixel/
		 */
		
		gameBoard = ImageIO.read(getClass().getResourceAsStream("FIAL_Board.png"));
		click = ImageIO.read(getClass().getResourceAsStream("click.png"));
		gameOver = ImageIO.read(getClass().getResourceAsStream("Game-Over.png"));
		replay = ImageIO.read(getClass().getResourceAsStream("Again.png"));
		
		// set replayButton
		
		replayButton = new JButton();
		replayButton.addActionListener(
				(e) -> {
					
					reset();
					
				});
		replayButton.setVisible(false);
		replayButton.setBounds(155, 325, 187, 50);
		replayButton.setOpaque(false);
		replayButton.setContentAreaFilled(false);
		replayButton.setBorderPainted(false);
		add(replayButton);
		
		// add buttons for moves
		
		int j = 0;
		buttons = new JButton[] {new JButton(), new JButton(), new JButton(), new JButton(), new JButton(), new JButton(), new JButton()};
		for (JButton b : buttons) {
			
			b.addActionListener(this);
			b.setBounds(xPos + j + 10, 55, 45, 45);
			b.setOpaque(false);
			b.setContentAreaFilled(false);
			b.setBorderPainted(false);
			add(b);
			
			j += 50;
			
			
			// fill in turns (bottom-up) when column is clicked
			
			b.addActionListener(
					(e) -> {
						// Here goes the action (method) you want to execute when clicked
				    	
				    	int x = b.getX();
				    	
				    	int colIdx = (x - xPos) / (50);
				    	
				    	for (int row = grid.length-1; row >= 0; row--) {
				    		
				    		if (grid[row][colIdx] != 0) {
				    			continue;
				    		}
				    		
				    		grid[row][colIdx] = turn;
				    		
				    		for (byte angle = 0; angle < 8; angle++) {
				    			
				    			byte dx = 0;
					    		byte dy = 0;
				    			
					    		if (angle == 0) {
				    				
				    				dx = 1;
				    				dy = -1;
					    		}
				    				
				    			if (angle == 1) {
				    				
				    				dx = 0;
				    				dy = -1;
				    			}
				    				
				    			if (angle == 2) {
				    				
				    				dx = -1;
				    				dy = -1;
				    			}
				    				
				    			if (angle == 3) {
				    				
				    				dx = -1;
				    				dy = 0;
				    			}
				    				
				    			if (angle == 4) {
				    				
				    				dx = -1;
				    				dy = 1;
				    			}
				    				
				    			if (angle == 5) {
				    				
				    				dx = 0;
				    				dy = 1;
				    			}
				    				
				    			if (angle == 6) {
				    				
				    				dx = 1;
				    				dy = 1;
				    			}
				    				
				    			if (angle == 7) {
				    				
				    				dx = 1;
				    				dy = 0;
				    			}
				    				
				    			// gameOver when 4 in a row (vertically, horizontally, diagonally)
				    			
				    			boolean isGameOver = false;
				    			
				    			for (byte i = 0; i < 4; i++) {
				    				if (row + dy * i < 0 || row + dy * i == grid.length || 
				    					colIdx + dx * i < 0 || colIdx + dx * i == grid[0].length || 
				    					grid[row + dy * i][colIdx + dx * i] != turn) {
				    					break;
				    				}
				    				
				    				if (i == 3) {
				    					isGameOver = true;
				    				}
				    			}
				    			
				    			if (isGameOver) {
				    				winner = turn;
				    				
				    			}
				    			
				    			if (winner != 0) {
				    				for (JButton button : buttons)
				    					button.setEnabled(false);
				    				replayButton.setVisible(true);
				    				
				    				break;
				    			}
				    			
				    		}
				    		
				    		break;
				    		
				    	}
				    	
				    	
				    	
				    	turn = (byte) ((turn == 1) ? 2 : 1);
				    	
				        repaint();
					}
			);
			
		}
		
		for (int r = 0; r < grid.length; r++) {
			for (int c = 0; c < grid[r].length; c++) {
				grid[r][c] = 0;
			}
		}
		
		timer.start();
		
	}
	
	//draw game board & win screen

	@Override
	public void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		
		g.setColor(Color.BLACK);
		
		int x = 100;
		int y = 100;
		
		int unit = 50;
		
		for (int r = 0; r < 6; r++)
			for (int c = 0; c < 7; c++) 
				g.drawRect(c * unit + (x - 34), r * unit + y, unit, unit);
		
		g.setColor(Color.BLUE);
		
		
		for (int r = 0; r < grid.length; r++) {
			for (int c = 0; c < grid[r].length; c++) {
				fill(g, r, c);
			}
		}
		
		g.drawImage(gameBoard, x - 34, y, 361, 311, null);
		
		int a = 0;
		for (int i = 0; i < 7; i++) {
			g.drawImage(click, 66 + a, 57, 35, 35, null);
			a += 50;
		}
		
		
		// draws gameOver & replay messages
			
		if (winner != 0) {
			g.setColor(new Color(1.0f, 1.0f, 1.0f, 0.7f));
			g.fillRect(0, 0, getBounds().width, getBounds().height);
			g.drawImage(gameOver, 115, 150, 263, 164, null);
			g.setColor(Color.WHITE);
			g.fillRect(157, 327, 185, 48);
			g.drawImage(replay, 155, 325, 187, 50, null);
			
		}
		
		// depending on winning player, color of win message changes
		
		// RED == 1
		
		if (winner == 1) {
			
			g.setColor(Color.RED);
			g.setFont(new Font("Dialogue", Font.BOLD, 40));
			g.drawString("RED Wins!", 150, 50);
			
		}
		
		// YELLOW == 2
		
		if (winner == 2) {
			
			g.setColor(Color.YELLOW);
			g.setFont(new Font("Dialogue", Font.BOLD, 40));
			g.drawString("YELLOW Wins!", 100, 50);
			
		}
			
	}
	
	
	public void fill(Graphics g, int rIdx, int cIdx) {
		if (grid[rIdx][cIdx] == 1) {
			g.setColor(Color.RED);
			g.fillRoundRect(xPos + cIdx*50, yPos + rIdx*50, 50, 50, 50, 50);
		}
		
		if (grid[rIdx][cIdx] == 2) {
			g.setColor(Color.YELLOW);
			g.fillRoundRect(xPos + cIdx*50, yPos + rIdx*50, 50, 50, 50, 50);
		}
	
	}
	
	// replay resets board
	
	public void reset() {
		
		for (int i = 0; i < grid.length; i++) {
			
			for (int j = 0; j < grid[i].length; j++) {
				
				grid[i][j] = 0;
				
			}
			
		}
		
		winner = 0;
		
		for (JButton button : buttons)
			button.setEnabled(true);
		
		replayButton.setVisible(false);
		
		
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
}
