package v3.Frame;

import v3.Pathfinding.PathFindingAlgorithm.Type;
import v3.Pathfinding.StopWatch;

import javax.swing.*;

public class SettingsWorker extends SwingWorker<Void, Void> {

	@Override
	protected Void doInBackground() throws Exception {
		// TODO Auto-generated method stub
		while (true) {
			StopWatch watch = new StopWatch();
			watch.start();
			switch (MainFrame.slider.getValue()) {
			case 0:
				if (MainFrame.GRID_SIZE != 5) {
					MainFrame.GRID_SIZE = 5;
					MainFrame.gridP.clear();
					MainFrame.editEnabled = true;
					MainFrame.running = false;
					MainFrame.waypointCounter = 1;
				}
				MainFrame.lblGridSizex.setText("Grid Size: 5x5");
				MainFrame.gridP.repaint();
				break;
			case 1:
				if (MainFrame.GRID_SIZE != 10) {
					MainFrame.GRID_SIZE = 10;
					MainFrame.gridP.clear();
					MainFrame.editEnabled = true;
					MainFrame.running = false;
					MainFrame.waypointCounter = 1;
				}
				MainFrame.lblGridSizex.setText("Grid Size: 10x10");
				MainFrame.gridP.repaint();

				break;
			case 2:
				if (MainFrame.GRID_SIZE != 20) {
					MainFrame.GRID_SIZE = 20;
					MainFrame.gridP.clear();
					MainFrame.editEnabled = true;
					MainFrame.running = false;
					MainFrame.waypointCounter = 1;
				}
				MainFrame.lblGridSizex.setText("Grid Size: 20x20");
				MainFrame.gridP.repaint();

				break;
			case 3:
				if (MainFrame.GRID_SIZE != 25) {
					MainFrame.GRID_SIZE = 25;
					MainFrame.gridP.clear();
					MainFrame.running = false;
					MainFrame.editEnabled = true;
					MainFrame.waypointCounter = 1;
				}
				MainFrame.lblGridSizex.setText("Grid Size: 25x25");
				MainFrame.gridP.repaint();
				break;
			case 4:
				if (MainFrame.GRID_SIZE != 50) {
					MainFrame.GRID_SIZE = 50;
					MainFrame.gridP.clear();
					MainFrame.editEnabled = true;
					MainFrame.running = false;
					MainFrame.waypointCounter = 1;
				}
				MainFrame.lblGridSizex.setText("Grid Size: 50x50");
				MainFrame.gridP.repaint();

				break;

			}
			String comboValue = (String) MainFrame.comboBox.getSelectedItem();
			switch (comboValue) {
			case "A*":
				MainFrame.alg.setAlgorithmType(Type.AASTERISK);
				break;
			case "Djikstra":
				MainFrame.alg.setAlgorithmType(Type.DJIKSTRA);
				break;
			case "Breadth-First":
				MainFrame.alg.setAlgorithmType(Type.BREADTH);
				break;
			}

			if (MainFrame.timerCheck.isSelected()) {
				MainFrame.delaySlider.setVisible(true);
				MainFrame.lblDelay.setVisible(true);
				MainFrame.separator.setVisible(false);
				PathfindingMain.frame.revalidate();
				MainFrame.alg.setTimerValue(MainFrame.delaySlider.getValue());
			} else {
				MainFrame.delaySlider.setVisible(false);
				MainFrame.lblDelay.setVisible(false);
				MainFrame.separator.setVisible(true);
				PathfindingMain.frame.revalidate();
				MainFrame.alg.setTimerValue(0);
			}

			if (MainFrame.diagonalCheck.isSelected()) {
				if (!MainFrame.running)
					MainFrame.alg.setDiagonal(true);
			} else {
				if (!MainFrame.running)
					MainFrame.alg.setDiagonal(false);
			}
			
			if (MainFrame.chckbxTraverseSamePath.isSelected()) {
				if (!MainFrame.running)
					MainFrame.alg.setTraverseSamePath(true);
			} else {
				if (!MainFrame.running)
					MainFrame.alg.setTraverseSamePath(false);
			}

			MainFrame.lblDelay.setText("Delay: " + MainFrame.delaySlider.getValue() + " ms");

			MainFrame.slider.setEnabled(!MainFrame.running);
			watch.stop();
			Thread.sleep(10);
			MainFrame.selectedNode.repaint();
		}
	}
}
