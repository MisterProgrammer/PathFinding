package v2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Window extends JFrame implements MouseListener, MouseMotionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	MyPanel p;
	private static final int WIDTH = 507;
	private static final int HEIGHT = 600;
	static char[][] grid = new char[10][10];
	static char gridMode = GridUtilities.OBSTACLE;
	private boolean editEnabled = true;
	static int waypointCounter;

	JButton findPathBtn;
	JButton clearBtn;
	JButton resetBtn;
	JButton startBtn;
	JButton endBtn;
	JButton obstacleBtn;
	JButton waypointBtn;
	JCheckBox diagonalCheck;
	JCheckBox timerCheck;

	A_Star_Algorithm alg = null;
	private boolean running;

	public Window() {
		alg = new A_Star_Algorithm();
		GridUtilities.clearGrid(grid);
		running = false;
		waypointCounter = 1;
		diagonalCheck = new JCheckBox("diagonal", true);
		timerCheck = new JCheckBox("Timer", true);
		p = new MyPanel();
		findPathBtn = new JButton("Find Path");
		clearBtn = new JButton("Clear");
		resetBtn = new JButton("Reset");
		startBtn = new JButton("Start");
		endBtn = new JButton("End");
		waypointBtn = new JButton("Waypoint");
		obstacleBtn = new JButton("Obstacle");
		startBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				gridMode = GridUtilities.START;
				repaint();
			}

		});
		resetBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (!running) {
					GridUtilities.resetGrid(grid);
					editEnabled = true;
					repaint();
				}
			}
		});
		endBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				gridMode = GridUtilities.END;
				repaint();
			}

		});
		obstacleBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				gridMode = GridUtilities.OBSTACLE;
				repaint();
			}

		});

		clearBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if (!running) {
					GridUtilities.clearGrid(grid);
					waypointCounter = 1;
					editEnabled = true;
					repaint();
				}
			}

		});

		waypointBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				gridMode = GridUtilities.WAYPOINT;
				repaint();
			}

		});

		diagonalCheck.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				alg.setChoice(e.getStateChange() == 1 ? 1 : 2);
			}
		});

		findPathBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (!running && editEnabled) {
					if (timerCheck.isSelected() == false) {
						running = true;
						alg.startAlgorithm(grid);
						if (alg.getInvalidNodes()) {
							JOptionPane.showMessageDialog(Window.this, "Start or End Node not inserted", "Error",
									JOptionPane.ERROR_MESSAGE);
							editEnabled = true;
							running = false;
							return;
						} else {
							grid = alg.getGrid();
							if (alg.getNoPathFound())
								JOptionPane.showMessageDialog(Window.this, "No Path Found.", "Error",
										JOptionPane.ERROR_MESSAGE);
							repaint();
							editEnabled = false;
							running = false;
						}
					} else {
						alg.startAlgorithmWthTimer(grid);
						if (alg.getInvalidNodes()) {
							JOptionPane.showMessageDialog(Window.this, "Start or End Node not inserted", "Error",
									JOptionPane.ERROR_MESSAGE);
							editEnabled = true;
							running = false;
							return;
						} else {
							Thread t= new Thread(new Runnable() {

								@Override
								public void run() {
									// TODO Auto-generated method stub
									running = true;
									alg.parent = alg.start;
									Node finishTemp = null;
									for (int i = 0; i < alg.getWaypointNodes().size(); i++) {
										alg.setFinished(false);
										finishTemp = alg.getWaypointNodes().get(i);
										while (!alg.getFinished()) {
											alg.findPathWthTimer(finishTemp);
											grid = alg.getGrid();

											try {
												Thread.sleep(50);
											} catch (InterruptedException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}

											repaint();
											editEnabled = false;
										}
										if (alg.getNoPathFound()) {
											JOptionPane.showMessageDialog(Window.this, "No Path Found.", "Error",
													JOptionPane.ERROR_MESSAGE);
											running = false;
										}

										alg.finish(finishTemp);
										grid = alg.getGrid();
										try {
											Thread.sleep(100);
										} catch (InterruptedException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										alg.parent = finishTemp;
										alg.closedNodes = new ArrayList<Node>();
										alg.openNodes = new ArrayList<Node>();
										repaint();
									}
									alg.setFinished(false);
									finishTemp = alg.finish;
									while (!alg.getFinished()) {
										alg.findPathWthTimer(finishTemp);
										grid = alg.getGrid();

										try {
											Thread.sleep(50);
										} catch (InterruptedException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										repaint();
										editEnabled = false;
									}
									if (alg.getNoPathFound()) {
										JOptionPane.showMessageDialog(Window.this, "No Path Found.", "Error",
												JOptionPane.ERROR_MESSAGE);
										running = false;
									}
									alg.finish(finishTemp);
									grid = alg.getGrid();
									try {
										Thread.sleep(100);
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									repaint();
									running = false;
									editEnabled = false;
								}
								
							}) {

							};
							t.start();
						}
					}
				}
			}
		});

		p.addMouseListener(this);
		p.addMouseMotionListener(this);
		p.setFocusable(true);

		timerCheck.setBounds(200, 530, 80, 20);
		diagonalCheck.setBounds(200, 510, 80, 20);
		clearBtn.setBounds(100, 500, 90, 30);
		resetBtn.setBounds(100, 535, 90, 30);
		findPathBtn.setBounds(0, 500, 90, 65);
		startBtn.setBounds(300 - 10, 500, 65, 20);
		endBtn.setBounds(366 - 10, 500, 60, 20);
		obstacleBtn.setBounds(300, 545, 100, 20);
		waypointBtn.setBounds(300, 522, 100, 20);

		add(findPathBtn);
		add(clearBtn);
		add(resetBtn);
		add(diagonalCheck);
		add(timerCheck);
		add(startBtn);
		add(endBtn);
		add(obstacleBtn);
		add(waypointBtn);
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
				if (!SwingUtilities.isLeftMouseButton(e)) {
					boolean isWaypoint = false;
					int waypointNumber = 0;
					for (int i = 0; i < GridUtilities.CHARWAYPOINT.length; i++) {
						if (grid[y][x] == GridUtilities.CHARWAYPOINT[i]) {
							isWaypoint = true;
							waypointNumber = i;
							break;
						}
					}
					if (isWaypoint) {
						System.out.println(waypointNumber + "," + waypointCounter);
						if (waypointNumber == (waypointCounter - 2)) {
							grid[y][x] = GridUtilities.EMPTY;
							waypointCounter--;
						}

					} else {
						grid[y][x] = GridUtilities.EMPTY;
					}
				} else {
					switch (gridMode) {
					case GridUtilities.OBSTACLE:
						grid[y][x] = GridUtilities.OBSTACLE;
						break;
					case GridUtilities.START:
						if (!nodeAlreadyInGrid(x, y, GridUtilities.START)) {
							grid[y][x] = GridUtilities.START;
						}
						break;
					case GridUtilities.END:
						if (!nodeAlreadyInGrid(x, y, GridUtilities.END))
							grid[y][x] = GridUtilities.END;
						break;
					case GridUtilities.WAYPOINT:
						if (waypointCounter < 10) {
							boolean isWaypoint = false;
							for (int i = 0; i < GridUtilities.CHARWAYPOINT.length; i++) {
								if (grid[y][x] == GridUtilities.CHARWAYPOINT[i]) {
									isWaypoint = true;
								}
							}
							if (!isWaypoint) {
								grid[y][x] = (char) (waypointCounter + '0');
								waypointCounter++;
							}
						}
						break;
					}
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
			case GridUtilities.OBSTACLE:
				if (SwingUtilities.isLeftMouseButton(e))
					grid[y][x] = GridUtilities.OBSTACLE;
				else
					grid[y][x] = GridUtilities.EMPTY;
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
				if (Window.grid[j][i] == typeOfNode) {
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
		Font font = new Font("ARIAL", Font.BOLD, 40);

		g2.setFont(font);
		g2.setColor(Color.GRAY); // sets Graphics2D color

		for (int i = 0; i < 10; i++)
			for (int j = 0; j < 10; j++)
				g2.drawRect(i * 50, j * 50, 50, 50);
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				if (Window.grid[j][i] == GridUtilities.OBSTACLE) {
					g2.setColor(Color.BLACK);
					g2.fillRect(i * 50 + 1, j * 50 + 1, 49, 49);
				} else if (Window.grid[j][i] == GridUtilities.END) {
					g2.setColor(Color.RED);
					g2.fillRect(i * 50 + 1, j * 50 + 1, 49, 49);
					g2.setColor(Color.BLACK);
					g2.drawString("E", i * 50 + (50 - g2.getFontMetrics().stringWidth("E")) / 2,
							j * 50 + 1 + g2.getFontMetrics().getAscent() + (50 - g2.getFontMetrics().getHeight()) / 2);
				} else if (Window.grid[j][i] == GridUtilities.START) {
					g2.setColor(Color.GREEN);
					g2.fillRect(i * 50 + 1, j * 50 + 1, 49, 49);
					g2.setColor(Color.BLACK);
					g2.drawString("S", i * 50 + (50 - g2.getFontMetrics().stringWidth("S")) / 2,
							j * 50 + 1 + g2.getFontMetrics().getAscent() + (50 - g2.getFontMetrics().getHeight()) / 2);
				} else if (Window.grid[j][i] == GridUtilities.PATH) {
					g2.setColor(Color.BLUE);
					g2.fillRect(i * 50 + 1, j * 50 + 1, 49, 49);
				} else if (Window.grid[j][i] == GridUtilities.CLOSED) {
					g2.setColor(Color.CYAN);
					g2.fillRect(i * 50 + 1, j * 50 + 1, 49, 49);
				} else if (Window.grid[j][i] == GridUtilities.OPEN) {
					g2.setColor(new Color(0, 255, 127));
					g2.fillRect(i * 50 + 1, j * 50 + 1, 49, 49);
				} else {
					for (int k = 0; k < GridUtilities.CHARWAYPOINT.length; k++) {
						if (Window.grid[j][i] == GridUtilities.CHARWAYPOINT[k]) {
							g2.setColor(Color.ORANGE);
							g2.fillRect(i * 50 + 1, j * 50 + 1, 49, 49);
							g2.setColor(Color.BLACK);
							g2.drawString(String.valueOf(GridUtilities.CHARWAYPOINT[k]),
									i * 50 + (50 - g2.getFontMetrics()
											.stringWidth(String.valueOf(GridUtilities.CHARWAYPOINT[k]))) / 2,
									j * 50 + 1 + g2.getFontMetrics().getAscent()
											+ (50 - g2.getFontMetrics().getHeight()) / 2);
						}
					}
				}
			}
		}

		g2.setColor(Color.GRAY); // sets Graphics2D color
		g2.drawRect(430, 508, 50, 50);
		switch (Window.gridMode) {
		case GridUtilities.OBSTACLE:
			g2.setColor(Color.BLACK);
			g2.fillRect(430 + 1, 508 + 1, 49, 49);
			break;
		case GridUtilities.END:
			g2.setColor(Color.RED);
			g2.fillRect(430 + 1, 508 + 1, 49, 49);
			g2.setColor(Color.BLACK);
			g2.setColor(Color.BLACK);
			g2.drawString("E", 430 + (50 - g2.getFontMetrics().stringWidth("E")) / 2,
					510 + g2.getFontMetrics().getAscent() + (50 - g2.getFontMetrics().getHeight()) / 2);
			break;
		case GridUtilities.START:
			g2.setColor(Color.GREEN);
			g2.fillRect(430 + 1, 508 + 1, 49, 49);
			g2.setColor(Color.BLACK);
			g2.setColor(Color.BLACK);
			g2.drawString("S", 430 + (50 - g2.getFontMetrics().stringWidth("S")) / 2,
					510 + g2.getFontMetrics().getAscent() + (50 - g2.getFontMetrics().getHeight()) / 2);
			break;
		case GridUtilities.WAYPOINT:
			g2.setColor(Color.ORANGE);
			g2.fillRect(430 + 1, 508 + 1, 49, 49);
			g2.setColor(Color.BLACK);
			g2.setColor(Color.BLACK);
			int number = 0;
			if (Window.waypointCounter < 9)
				number = Window.waypointCounter;
			else
				number = 9;
			g2.drawString(String.valueOf(number),
					430 + (50 - g2.getFontMetrics().stringWidth(String.valueOf(GridUtilities.CHARWAYPOINT[1]))) / 2,
					510 + g2.getFontMetrics().getAscent() + (50 - g2.getFontMetrics().getHeight()) / 2);
			break;

		}
	}

}