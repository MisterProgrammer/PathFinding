package v3.Frame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.swing.JPanel;

import v3.Pathfinding.Grid;
import v3.Pathfinding.Grid.State;
import v3.Pathfinding.Grid.Type;
import v3.Pathfinding.Maze;
import v3.Pathfinding.Node;

public class GridPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7674802265809747134L;
	private Grid grid = new Grid(MainFrame.GRID_SIZE);
	private Font font;
	private int rectSize;
	private Graphics2D g2;
	private Maze maze;

	private double currentX;
	private double currentY;
	private double zoom = 1;

	public GridPanel() {
		addMouseWheelListener(new MouseWheelListener() {

			public void mouseWheelMoved(MouseWheelEvent e) {

				if (e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL) {

					currentX = e.getX();
					currentY = e.getY();
					int wheelRotation = e.getWheelRotation();
					if (wheelRotation > 0) {
						incrementZoom(.2 * -(double) e.getWheelRotation());
					} else {
						incrementZoom(.2 * -(double) e.getWheelRotation());
					}

				}

			}
		});

		setOpaque(false);

	}

//8,4,3.3
	private void incrementZoom(double amount) {
		zoom += amount * MainFrame.GRID_SIZE / 10;
		zoom = Math.max(0.00001, zoom);
		repaint();

	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		EventHandler handler = new EventHandler();
		this.addMouseListener(handler);
		this.addMouseMotionListener(handler);

		int SIZE = MainFrame.GRID_SIZE;

		g2 = (Graphics2D) g;
		AffineTransform tx = new AffineTransform();

		if (MainFrame.GRID_SIZE > 10) {
			if (zoom >= 1) {
				tx.translate(currentX, currentY);
				tx.scale(zoom, zoom);
				tx.translate(-currentX, -currentY);
				g2.setTransform(tx); // zoom and panning test
			} else {
				zoom = 1;
				repaint();
			}
		} else {
			zoom = 1;
			repaint();
		}
		System.out.println(zoom);
		font = new Font("ARIAL", Font.BOLD, (int) (1.0f / SIZE * 500));

		g2.setFont(font);
		g2.setColor(this.getBackground());
		g2.fillRect(0, 0, getWidth(), getHeight());
		g2.setColor(Color.GRAY); // sets Graphics2D color

		int rectSize = (int) ((double) 500 / SIZE);

		for (int i = 0; i < SIZE; i++)
			for (int j = 0; j < SIZE; j++)
				g2.drawRect(i * rectSize, j * rectSize, rectSize, rectSize);

		if (grid != null) {
			paintGrid();
		}
	}

	private void paintGrid() {
		// TODO Auto-generated method stub
		for (int i = 0; i < grid.getSize(); i++) {
			for (int j = 0; j < grid.getSize(); j++) {
				rectSize = (int) ((double) 500 / MainFrame.GRID_SIZE);
				font = new Font("ARIAL", Font.BOLD, (int) (1.0f / MainFrame.GRID_SIZE * 500));
				g2.setFont(font);
				if (grid.get()[j][i].getType() == Type.OBSTACLE) {
					g2.setColor(Color.BLACK);
					g2.fillRect(i * rectSize + 1, j * rectSize + 1, rectSize - 1, rectSize - 1);
				} else if (grid.get()[j][i].getType() == Type.FINISH) {
					g2.setColor(Color.RED);
					g2.fillRect(i * rectSize + 1, j * rectSize + 1, rectSize - 1, rectSize - 1);
					g2.setColor(Color.BLACK);
					g2.drawString("E", i * rectSize + (rectSize - g2.getFontMetrics().stringWidth("E")) / 2,
							j * rectSize + 1 + g2.getFontMetrics().getAscent()
									+ (rectSize - g2.getFontMetrics().getHeight()) / 2);
				} else if (grid.get()[j][i].getType() == Type.START) {
					g2.setColor(Color.GREEN);
					g2.fillRect(i * rectSize + 1, j * rectSize + 1, rectSize - 1, rectSize - 1);
					g2.setColor(Color.BLACK);
					g2.drawString("S", i * rectSize + (rectSize - g2.getFontMetrics().stringWidth("S")) / 2,
							j * rectSize + 1 + g2.getFontMetrics().getAscent()
									+ (rectSize - g2.getFontMetrics().getHeight()) / 2);
				} else if (grid.get()[j][i].getType() == Type.WAYPOINT) {
					g2.setColor(Color.ORANGE);
					g2.fillRect(i * rectSize + 1, j * rectSize + 1, rectSize - 1, rectSize - 1);
					g2.setColor(Color.BLACK);
					g2.drawString(String.valueOf(grid.get()[j][i].getWaypointNumber()),
							i * rectSize + (rectSize - g2.getFontMetrics()
									.stringWidth(String.valueOf(grid.get()[j][i].getWaypointNumber()))) / 2,
							j * rectSize + 1 + g2.getFontMetrics().getAscent()
									+ (rectSize - g2.getFontMetrics().getHeight()) / 2);
				} else if (grid.get()[j][i].getState() == State.PATH) {
					g2.setColor(Color.BLUE);
					g2.fillRect(i * rectSize + 1, j * rectSize + 1, rectSize - 1, rectSize - 1);
					drawCosts(grid.get()[j][i], i, j);
				} else if (grid.get()[j][i].getState() == State.CLOSED) {
					g2.setColor(Color.CYAN);
					g2.fillRect(i * rectSize + 1, j * rectSize + 1, rectSize - 1, rectSize - 1);
					drawCosts(grid.get()[j][i], i, j);
				} else if (grid.get()[j][i].getState() == State.OPEN) {
					g2.setColor(new Color(0, 255, 127));
					g2.fillRect(i * rectSize + 1, j * rectSize + 1, rectSize - 1, rectSize - 1);
					drawCosts(grid.get()[j][i], i, j);
				}
			}
		}
	}

	private void drawCosts(Node node, int i, int j) {
		// TODO Auto-generated method stub
		if (zoom*(1.0f/MainFrame.GRID_SIZE*100) > 15 || MainFrame.GRID_SIZE <= 10) {
			float g = round(node.getG(), 1);
			float h = round(node.getH(), 1);
			float f = round(node.getF(), 1);

			g2.setColor(Color.black);
			font = new Font("ARIAL", Font.BOLD, (int) (1.0f / MainFrame.GRID_SIZE * 100));
			g2.setFont(font);
			g2.drawString(String.valueOf(h),
					i * rectSize + (rectSize - g2.getFontMetrics().stringWidth(String.valueOf(h))),
					j * rectSize + g2.getFontMetrics().getAscent() / 2 + (g2.getFontMetrics().getHeight()) / 2);

			font = new Font("ARIAL", Font.BOLD, (int) (1.0f / MainFrame.GRID_SIZE * 100));
			g2.setFont(font);
			g2.drawString(String.valueOf(g), i * rectSize,
					j * rectSize + g2.getFontMetrics().getAscent() / 2 + (g2.getFontMetrics().getHeight()) / 2);

			font = new Font("ARIAL", Font.BOLD, (int) (1.0f / MainFrame.GRID_SIZE * 180));
			g2.setFont(font);
			g2.drawString(String.valueOf(f),
					i * rectSize + (rectSize - g2.getFontMetrics().stringWidth(String.valueOf(f))) / 2, j * rectSize + 1
							+ g2.getFontMetrics().getAscent() + (rectSize - g2.getFontMetrics().getHeight()) / 1.5f);
		}

	}

	public char[][] getCharGrid() {

		return grid.getCharGrid();
	}

	public void clear() {
		// TODO Auto-generated method stub
		grid = new Grid(MainFrame.GRID_SIZE);
		repaint();
	}

	public void reset() {
		// TODO Auto-generated method stub
		if (grid != null)
			for (int i = 0; i < grid.getSize(); i++) {
				for (int j = 0; j < grid.getSize(); j++) {
					grid.get()[i][j].setState(null);
				}
			}
		repaint();
	}

	public void setGrid(Grid grid) {
		// TODO Auto-generated method stub
		this.grid = grid;
	}

	public Grid getGrid() {
		// TODO Auto-generated method stub
		return grid;
	}

	public void generateMaze() {
		// TODO Auto-generated method stub
		maze = new Maze(MainFrame.GRID_SIZE);
		maze.generateMaze();
		clear();

		for (int[] y : maze.getMaze()) {
			for (int x : y) {
				System.out.print(x + " ");
			}
			System.out.println();
		}
		for (int i = 0; i < MainFrame.GRID_SIZE; i++) {
			for (int j = 0; j < MainFrame.GRID_SIZE; j++) {
				if (maze.getMaze()[j][i] == 1)
					grid.get()[i][j].setType(Type.EMPTY);
				if (maze.getMaze()[j][i] == 0)
					grid.get()[i][j].setType(Type.OBSTACLE);
			}
		}
		grid.get()[0][0].setType(Type.START);
		grid.get()[MainFrame.GRID_SIZE - 1][MainFrame.GRID_SIZE - 1].setType(Type.FINISH);
		repaint();
	}

	public static float round(float value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

		BigDecimal bd = BigDecimal.valueOf(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return (float) bd.doubleValue();
	}
}
