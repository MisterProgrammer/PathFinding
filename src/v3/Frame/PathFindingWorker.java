package v3.Frame;

import v3.Pathfinding.Grid;
import v3.Pathfinding.PathFindingAlgorithm;
import v3.Pathfinding.StopWatch;

import javax.swing.*;

public class PathFindingWorker extends SwingWorker<Void, Void> {

	PathFindingAlgorithm alg = MainFrame.alg;
	Grid grid;

	@Override
	protected Void doInBackground() throws Exception {
		// TODO Auto-generated method stub

		StopWatch watch = new StopWatch();
		watch.start();

		MainFrame.running = true;
		MainFrame.editEnabled = false;
		grid = alg.startAlgorithm(MainFrame.gridP.getCharGrid(), MainFrame.gridP);

		if (alg.isNoPathFound()) {
			if (!alg.isTraverseSamePath()) {
				JOptionPane.showMessageDialog(PathfindingMain.frame, "No Path Found! Traversing same path...", "Error",
						JOptionPane.ERROR_MESSAGE);
				alg.setTraverseSamePath(true);
				grid = alg.startAlgorithm(MainFrame.gridP.getCharGrid(), MainFrame.gridP);
				if (alg.isNoPathFound()) {
					JOptionPane.showMessageDialog(PathfindingMain.frame, "No Path Found!", "Error",
							JOptionPane.ERROR_MESSAGE);
				} else if (alg.isInvalidNodes()) {
					JOptionPane.showMessageDialog(PathfindingMain.frame, "Start or End Node not inserted", "Error",
							JOptionPane.ERROR_MESSAGE);
					MainFrame.editEnabled = true;
				} else {
					MainFrame.gridP.setGrid(grid);
				}

			} else {
				JOptionPane.showMessageDialog(PathfindingMain.frame, "No Path Found!", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		} else if (alg.isInvalidNodes()) {
			JOptionPane.showMessageDialog(PathfindingMain.frame, "Start or End Node not inserted", "Error",
					JOptionPane.ERROR_MESSAGE);
			MainFrame.editEnabled = true;
		} else {
			MainFrame.gridP.setGrid(grid);
		}

		watch.stop();
		MainFrame.running = false;
		PathfindingMain.frame.setTitle("PathFinding v3 by Andr� P�scoa       Elapsed Time: " + watch.getTime());
		return null;
	}

}
