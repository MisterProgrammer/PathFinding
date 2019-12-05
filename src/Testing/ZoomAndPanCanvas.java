package Testing;

import java.awt.*;
import java.awt.geom.AffineTransform;
import javax.swing.*;

/**
 * Canvas displaying a simple drawing: the coordinate-system axes + some points
 * and their coordinates. It is used to demonstrate the Zoom and Pan
 * functionality.
 *
 * @author Sorin Postelnicu
 * @since July 13, 2009
 */

public class ZoomAndPanCanvas extends Canvas {

	public static void main(String[] args) {
		JFrame frame = new JFrame("Zoom and Pan Canvas");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		ZoomAndPanCanvas chart = new ZoomAndPanCanvas();

		frame.add(chart, BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);
		chart.createBufferStrategy(2);
	}

	private boolean init = true;

	private Point[] points = { new Point(-100, -100), new Point(-100, 100), new Point(100, -100), new Point(100, 100) };

	private ZoomAndPanListener zoomAndPanListener;

	public ZoomAndPanCanvas() {
		this.zoomAndPanListener = new ZoomAndPanListener(this);
		this.addMouseListener(zoomAndPanListener);
		this.addMouseMotionListener(zoomAndPanListener);
		this.addMouseWheelListener(zoomAndPanListener);
	}

	public Dimension getPreferredSize() {
		return new Dimension(600, 500);
	}

	public void paint(Graphics g1) {
		Graphics2D g = (Graphics2D) g1;
		if (init) {
			// Initialize the viewport by moving the origin to the center of the window,
			// and inverting the y-axis to point upwards.
			init = false;
			Dimension d = getSize();
			int xc = d.width / 2;
			int yc = d.height / 2;
			g.translate(0,0);
			g.scale(1, -1);
			// Save the viewport to be updated by the ZoomAndPanListener
			zoomAndPanListener.setCoordTransform(g.getTransform());
		} else {
			// Restore the viewport after it was updated by the ZoomAndPanListener
			g.setTransform(zoomAndPanListener.getCoordTransform());
		}

		int SIZE = 50;
		int rectSize = 50;

		for (int i = 0; i < SIZE; i++)
			for (int j = 0; j < SIZE; j++)
				g.drawRect(i * rectSize, j * rectSize, rectSize, rectSize);

	}

}