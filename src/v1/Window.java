package v1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Window extends JFrame implements MouseListener, MouseMotionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	MyPanel p;
	private static final int WIDTH = 507;
	private static final int HEIGHT = 600;
	static final int EMPTY = 0;
	static final int OBSTACLE = 1;
	static final int START = 2;
	static final int END = 3;
	static final int PATH = 4;
	static final int CLOSED = 5;
	static final int OPEN = 6;
	static int[][] grid = new int[10][10];
	static int gridMode = OBSTACLE;
	private boolean editEnabled = true;

	JButton findPathBtn;
	JButton clearBtn;
	JButton resetBtn;
	JButton startBtn;
	JButton endBtn;
	JButton obstacleBtn;
	JCheckBox diagonalCheck;
	JCheckBox timerCheck;

	A_Star_Algorithm alg = null;

	public Window() {
		alg = new A_Star_Algorithm();

		diagonalCheck = new JCheckBox("diagonal", true);
		timerCheck = new JCheckBox("Timer", true);
		p = new MyPanel();
		findPathBtn = new JButton("Find Path");
		clearBtn = new JButton("Clear");
		resetBtn = new JButton("Reset");
		startBtn = new JButton("Start Node");
		endBtn = new JButton("End Node");
		obstacleBtn = new JButton("Obstacle");
		startBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				gridMode = START;
				repaint();
			}

		});
		resetBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				GridUtilities.resetGrid(grid);
				editEnabled = true;
				repaint();
			}
		});
		endBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				gridMode = END;
				repaint();
			}

		});
		obstacleBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				gridMode = OBSTACLE;
				repaint();
			}

		});
		findPathBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (timerCheck.isSelected() == false) {
					alg.startAlgorithm(GridUtilities.convertWGridToAGrid(grid));
					grid = GridUtilities.convertAGridToWGrid(alg.getGrid());
					System.out.println(GridUtilities.intGridSequence(grid));
					if (alg.getNoPathFound())
						JOptionPane.showMessageDialog(Window.this, "No Path Found.", "Error",
								JOptionPane.ERROR_MESSAGE);
					repaint();
					editEnabled = false;
				} else {
					alg.startAlgorithmWthTimer(GridUtilities.convertWGridToAGrid(grid));
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							while (!alg.getFinished()) {
								alg.findPathWthTimer();
								grid = GridUtilities.convertAGridToWGrid(alg.getGrid());

								System.out.println(GridUtilities.intGridSequence(grid));
								try {
									Thread.sleep(50);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								p.paintImmediately(0, 0, getWidth(), getHeight());
								repaint();
								editEnabled = false;
							}
							if (alg.getNoPathFound())
								JOptionPane.showMessageDialog(Window.this, "No Path Found.", "Error",
										JOptionPane.ERROR_MESSAGE);
							alg.finish();
							grid = GridUtilities.convertAGridToWGrid(alg.getGrid());
							try {
								Thread.sleep(100);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							p.paintImmediately(0, 0, getWidth(), getHeight());
							repaint();
						}
					});

				}
			}

		});
		clearBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				GridUtilities.clearGrid(grid);
				editEnabled = true;
				repaint();
			}

		});
		p.addMouseListener(this);
		p.addMouseMotionListener(this);
		p.setFocusable(true);
		p.getInputMap().put(KeyStroke.getKeyStroke("S"), "doSomething");
		p.getActionMap().put("doSomething", new AbstractAction() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 2904001939465097411L;

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				gridMode = START;
			}

		});
		diagonalCheck.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				alg.setChoice(e.getStateChange() == 1 ? 1 : 2);
			}
		});
		timerCheck.setBounds(200, 530, 80, 20);
		diagonalCheck.setBounds(200, 510, 80, 20);
		clearBtn.setBounds(100, 500, 90, 30);
		resetBtn.setBounds(100, 535, 90, 30);
		findPathBtn.setBounds(0, 500, 90, 65);
		startBtn.setBounds(300, 500, 100, 20);
		endBtn.setBounds(300, 523, 100, 20);
		obstacleBtn.setBounds(300, 545, 100, 20);

		add(findPathBtn);
		add(clearBtn);
		add(resetBtn);
		add(diagonalCheck);
		add(timerCheck);
		add(startBtn);
		add(endBtn);
		add(obstacleBtn);
		add(p);
		setSize(WIDTH, HEIGHT);
		setLocationRelativeTo(null);
		setVisible(true);
		setResizable(false);
		setFocusable(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("PathFinding by Andr� P�scoa");
	}

	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				new Window();

			}

		});
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		try {
			// TODO Auto-generated method stub
			if (editEnabled) {
				int x = (int) (e.getX() / 100.0 * 2);
				int y = (int) (e.getY() / 100.0 * 2);
				switch (gridMode) {
				case OBSTACLE:
					if (SwingUtilities.isLeftMouseButton(e))
						grid[x][y] = OBSTACLE;
					else
						grid[x][y] = EMPTY;
					break;
				case START:
					if (SwingUtilities.isLeftMouseButton(e)) {
						if (!nodeAlreadyInGrid(x, y, START)) {
							grid[x][y] = START;
						}
					} else {
						grid[x][y] = EMPTY;

					}
					break;
				case END:
					if (SwingUtilities.isLeftMouseButton(e)) {
						if (!nodeAlreadyInGrid(x, y, END))
							grid[x][y] = END;
					} else {
						grid[x][y] = EMPTY;
					}
					break;
				}
				repaint();
			}
		} catch (ArrayIndexOutOfBoundsException lel) {

		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		if (editEnabled) {
			int x = (int) (e.getX() / 100.0 * 2);
			int y = (int) (e.getY() / 100.0 * 2);
			switch (gridMode) {
			case OBSTACLE:
				if (SwingUtilities.isLeftMouseButton(e))
					grid[x][y] = OBSTACLE;
				else
					grid[x][y] = EMPTY;
				break;
			case START:
				if (SwingUtilities.isLeftMouseButton(e)) {
					if (!nodeAlreadyInGrid(x, y, START))
						grid[x][y] = START;
				} else {
					grid[x][y] = EMPTY;
				}
				break;
			case END:
				if (SwingUtilities.isLeftMouseButton(e)) {
					if (!nodeAlreadyInGrid(x, y, END))
						grid[x][y] = END;
				} else {
					grid[x][y] = EMPTY;
				}
				break;
			}
			repaint();
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	private boolean nodeAlreadyInGrid(int x, int y, int typeOfNode) {
		boolean found = false;

		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				if (Window.grid[i][j] == typeOfNode) {
					found = true;
					i = 10;
					j = 10;
				}
			}
		}
		return found;
	}
}

class MyPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.GRAY); // sets Graphics2D color

		for (int i = 0; i < 10; i++)
			for (int j = 0; j < 10; j++)
				g2.drawRect(i * 50, j * 50, 500 / 10, 500 / 10);
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				if (Window.grid[i][j] == Window.OBSTACLE) {
					g2.setColor(Color.BLACK);
					g2.fillRect(i * 50 + 1, j * 50 + 1, 49, 49);
				} else if (Window.grid[i][j] == Window.END) {
					g2.setColor(Color.RED);
					g2.fillRect(i * 50 + 1, j * 50 + 1, 49, 49);
				} else if (Window.grid[i][j] == Window.START) {
					g2.setColor(Color.GREEN);
					g2.fillRect(i * 50 + 1, j * 50 + 1, 49, 49);
				} else if (Window.grid[i][j] == Window.PATH) {
					g2.setColor(Color.PINK);
					g2.fillRect(i * 50 + 1, j * 50 + 1, 49, 49);
				} else if (Window.grid[i][j] == Window.CLOSED) {
					g2.setColor(Color.CYAN);
					g2.fillRect(i * 50 + 1, j * 50 + 1, 49, 49);
				} else if (Window.grid[i][j] == Window.OPEN) {
					g2.setColor(new Color(0, 255, 127));
					g2.fillRect(i * 50 + 1, j * 50 + 1, 49, 49);
				}
			}
		}

		g2.setColor(Color.GRAY); // sets Graphics2D color
		g2.drawRect(430, 508, 50, 50);
		switch (Window.gridMode) {
		case Window.OBSTACLE:
			g2.setColor(Color.BLACK);
			g2.fillRect(430 + 1, 508 + 1, 49, 49);
			break;
		case Window.END:
			g2.setColor(Color.RED);
			g2.fillRect(430 + 1, 508 + 1, 49, 49);
			break;
		case Window.START:
			g2.setColor(Color.GREEN);
			g2.fillRect(430 + 1, 508 + 1, 49, 49);
			break;
		}
	}

}