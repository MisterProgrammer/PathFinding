package v3.Frame;

import v3.Pathfinding.Grid.Type;

import javax.swing.*;
import java.awt.*;

public class SelectedTypePanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3404498687648369524L;
	Color color;

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D) g;

		g2.setColor(Color.GRAY); // sets Graphics2D color
		g2.drawRect(0, 0, 50, 50);

		Font font = new Font("ARIAL", Font.BOLD, 40);
		g2.setFont(font);

		if (MainFrame.gridMode == Type.OBSTACLE) {
			g2.setColor(Color.black);
			g2.fillRect(1, 1, 49, 49);
		} else if (MainFrame.gridMode == Type.FINISH) {
			g2.setColor(Color.RED);
			g2.fillRect(1, 1, 49, 49);
			g2.setColor(Color.black);
			g2.drawString("E", (50 - g2.getFontMetrics().stringWidth("E")) / 2,
					g2.getFontMetrics().getAscent() + (50 - g2.getFontMetrics().getHeight()) / 2 + 1);
		} else if (MainFrame.gridMode == Type.START) {
			g2.setColor(Color.GREEN);
			g2.fillRect(1, 1, 49, 49);
			g2.setColor(Color.black);
			g2.drawString("S", (50 - g2.getFontMetrics().stringWidth("S")) / 2,
					g2.getFontMetrics().getAscent() + (50 - g2.getFontMetrics().getHeight()) / 2 + 1);
		} else if (MainFrame.gridMode == Type.WAYPOINT) {
			g2.setColor(Color.ORANGE);
			g2.fillRect(1, 1, 49, 49);
			g2.setColor(Color.black);
			int number = 0;
			if (MainFrame.waypointCounter < 9)
				number = MainFrame.waypointCounter;
			else
				number = 9;
			g2.drawString(String.valueOf(number), (50 - g2.getFontMetrics().stringWidth(String.valueOf(number))) / 2,
					g2.getFontMetrics().getAscent() + (50 - g2.getFontMetrics().getHeight()) / 2 + 1);
		}

	}
}
