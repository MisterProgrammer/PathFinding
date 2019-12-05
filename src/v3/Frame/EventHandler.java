package v3.Frame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.SwingUtilities;

import v3.Pathfinding.Grid;
import v3.Pathfinding.Grid.Type;
import v3.Pathfinding.Node;

public class EventHandler implements MouseListener, MouseMotionListener, KeyListener, ActionListener {

	PathFindingWorker worker;

	int map(long x, long in_min, long in_max, long out_min, long out_max) {
		return (int) ((x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		if (MainFrame.editEnabled) {
			Grid grid = MainFrame.gridP.getGrid();
			int x = map(e.getX(), 0, 500, 0, MainFrame.GRID_SIZE);
			int y = map(e.getY(), 0, 500, 0, MainFrame.GRID_SIZE);
			Node mouseNode = new Node(x, y);
			if (grid.isValid(mouseNode))
				switch (MainFrame.gridMode) {
				case OBSTACLE:
					if (SwingUtilities.isLeftMouseButton(e))
						grid.get()[y][x].setType(Type.OBSTACLE);
					else {
						grid.get()[y][x].setType(Type.EMPTY);
					}
					break;
				default:
					if (!SwingUtilities.isLeftMouseButton(e))
						grid.get()[y][x].setType(Type.EMPTY);
					break;
				}
			MainFrame.gridP.repaint();
			mouseNode = null;
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub

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
		// TODO Auto-generated method stub
		Grid grid = MainFrame.gridP.getGrid();
		if (MainFrame.editEnabled) {
			int x = map(e.getX(), 0, 500, 0, MainFrame.GRID_SIZE);
			int y = map(e.getY(), 0, 500, 0, MainFrame.GRID_SIZE);
			Node mouseNode = new Node(x, y);
			if (grid.isValid(mouseNode))
				if (!SwingUtilities.isLeftMouseButton(e)) {

					boolean isWaypoint = false;
					int waypointNumber = 0;
					if (grid.get()[y][x].getWaypointNumber() != 0) {
						isWaypoint = true;
						waypointNumber = grid.get()[y][x].getWaypointNumber();
					}

					if (isWaypoint) {
						System.out.println(waypointNumber + "," + MainFrame.waypointCounter);
						if (waypointNumber == (MainFrame.waypointCounter - 1)) {
							grid.get()[y][x].setType(Type.EMPTY);
							MainFrame.waypointCounter--;
						}

					} else {
						grid.get()[y][x].setType(Type.EMPTY);
					}
				} else {
					switch (MainFrame.gridMode) {
					case EMPTY:
						grid.get()[y][x].setType(Type.OBSTACLE);
						MainFrame.gridP.repaint();
						break;
					case START:
						if (!grid.contains(Type.START)) {
							grid.get()[y][x].setType(Type.START);
						}
						MainFrame.gridP.repaint();
						break;
					case FINISH:

						if (!grid.contains(Type.FINISH))
							grid.get()[y][x].setType(Type.FINISH);
						MainFrame.gridP.repaint();
						break;
					case OBSTACLE:
						grid.get()[y][x].setType(Type.OBSTACLE);
						MainFrame.gridP.repaint();
						break;
					case WAYPOINT:

						if (MainFrame.waypointCounter < 10) {
							boolean isWaypoint = false;
							if (grid.get()[y][x].getWaypointNumber() != 0) {
								isWaypoint = true;
							}

							if (!isWaypoint) {
								grid.get()[y][x].setType(Type.WAYPOINT);
								grid.get()[y][x].setWaypointNumber(MainFrame.waypointCounter);
								MainFrame.waypointCounter++;
							}
						}

						break;
					default:
						break;

					}
				}
			MainFrame.gridP.repaint();
			mouseNode = null;
		}

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object origin = e.getSource();
		if (origin.equals(MainFrame.findPathBtn)) {
			if (!MainFrame.running) {
				worker = new PathFindingWorker();
				worker.execute();
			}

		} else if (origin.equals(MainFrame.clearBtn)) {
			if (!MainFrame.running) {
				MainFrame.gridP.clear();
				MainFrame.waypointCounter = 1;
				MainFrame.editEnabled = true;
				MainFrame.gridP.repaint();
			}

		} else if (origin.equals(MainFrame.resetBtn)) {
			if (!MainFrame.running) {
				MainFrame.gridP.reset();
				MainFrame.editEnabled = true;
				MainFrame.gridP.repaint();
			}

		} else if (origin.equals(MainFrame.startBtn)) {
			MainFrame.gridMode = Type.START;
			MainFrame.selectedNode.repaint();

		} else if (origin.equals(MainFrame.waypointBtn)) {
			MainFrame.gridMode = Type.WAYPOINT;
			MainFrame.selectedNode.repaint();

		} else if (origin.equals(MainFrame.obstacleBtn)) {
			MainFrame.gridMode = Type.OBSTACLE;
			MainFrame.selectedNode.repaint();

		} else if (origin.equals(MainFrame.endBtn)) {
			MainFrame.gridMode = Type.FINISH;
			MainFrame.selectedNode.repaint();
		} else if (origin.equals(MainFrame.mazeGenBtn)) {
			if (!MainFrame.running) {
				MainFrame.gridP.generateMaze();
				MainFrame.selectedNode.repaint();
				MainFrame.editEnabled = true;
			}
		}
	}

}
