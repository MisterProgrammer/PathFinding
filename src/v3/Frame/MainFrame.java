package v3.Frame;

import v3.Pathfinding.PathFindingAlgorithm;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7558187862191538569L;
	static int GRID_SIZE = 100;
	static GridPanel gridP;
	static JPanel content;
	static JPanel interaction;
	static JButton findPathBtn;
	static JButton clearBtn;
	static JButton resetBtn;
	static JButton startBtn;
	static JButton endBtn;
	static JButton obstacleBtn;
	static JButton waypointBtn;
	static JCheckBox diagonalCheck;

	static JCheckBox timerCheck;
	static JPanel selectedNode;
	static JPanel NodeEditing;
	static JPanel MainControls;
	static JPanel southeastPanel;
	static JPanel NodeType;
	static JPanel GridControls;
	static JComboBox<String> comboBox;
	static JSlider slider;
	static JSlider delaySlider;
	static JLabel lblDelay;
	static JSeparator separator;
	static JLabel lblGridSizex;
	static Color color;
	public static boolean editEnabled;
	public static int waypointCounter;
	public static boolean running;
	public static v3.Pathfinding.Grid.Type gridMode;
	EventHandler handler;
	static PathFindingAlgorithm alg;
	static JCheckBox chckbxTraverseSamePath;
	static JPanel panel;
	static JButton mazeGenBtn;

	public MainFrame() {
		alg = new PathFindingAlgorithm();
		running = false;
		waypointCounter = 1;
		editEnabled = true;
		gridMode = v3.Pathfinding.Grid.Type.OBSTACLE;

		handler = new EventHandler();
		color = Color.BLACK;
		
		gridP = new GridPanel();
		content = new JPanel();
		interaction = new JPanel();
		gridP.setPreferredSize(new Dimension(507, 501));
		interaction.setLayout(new BoxLayout(interaction, BoxLayout.Y_AXIS));

		/***********************************************************
		 * MainControls Panel setup *
		 ***********************************************************/

		MainControls = new JPanel();
		MainControls
				.setBorder(new TitledBorder(null, "Main Controls", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		interaction.add(MainControls);
		MainControls.setLayout(new BoxLayout(MainControls, BoxLayout.Y_AXIS));
		findPathBtn = new JButton("Find Path");
		MainControls.add(findPathBtn);
		findPathBtn.addActionListener(handler);
		findPathBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		panel = new JPanel();
		MainControls.add(panel);
		panel.setLayout(new GridLayout(0, 2, 0, 0));
		clearBtn = new JButton("Clear");
		panel.add(clearBtn);
		clearBtn.addActionListener(handler);
		clearBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
		resetBtn = new JButton("Reset");
		panel.add(resetBtn);
		resetBtn.addActionListener(handler);
		resetBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		mazeGenBtn = new JButton("Generate Maze");
		mazeGenBtn.addActionListener(handler);
		MainControls.add(mazeGenBtn);
		mazeGenBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		mazeGenBtn.setAlignmentX(0.5f);
		timerCheck = new JCheckBox("Timer", true);
		timerCheck.setAlignmentX(Component.CENTER_ALIGNMENT);
		MainControls.add(timerCheck);

		delaySlider = new JSlider(25, 500);
		delaySlider.setPaintTicks(true);
		delaySlider.setSnapToTicks(true);
		delaySlider.setPreferredSize(new Dimension(100, 35));
		delaySlider.setMinorTickSpacing(25);
		delaySlider.setMajorTickSpacing(50);
		MainControls.add(delaySlider);

		lblDelay = new JLabel("Delay: 50");
		lblDelay.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblDelay.setAlignmentX(Component.CENTER_ALIGNMENT);
		MainControls.add(lblDelay);

		diagonalCheck = new JCheckBox("Diagonal", true);
		diagonalCheck.setAlignmentX(Component.CENTER_ALIGNMENT);
		MainControls.add(diagonalCheck);

		comboBox = new JComboBox<String>();
		comboBox.setPreferredSize(new Dimension(50, 20));

		// comboBox setup
		comboBox.addItem("A*");
		comboBox.addItem("Djikstra");
		comboBox.addItem("Breadth-First");

		comboBox.getSelectedItem();
		
		chckbxTraverseSamePath = new JCheckBox("Traverse same path", true);
		chckbxTraverseSamePath.setAlignmentX(0.5f);
		MainControls.add(chckbxTraverseSamePath);
		MainControls.add(comboBox);

		separator = new JSeparator();
		separator.setVisible(false);
		separator.setPreferredSize(new Dimension(0, 50));
		MainControls.add(separator);

		/***********************************************************
		 * NodeEditing Panel setup *
		 ***********************************************************/
		NodeEditing = new JPanel();
		interaction.add(NodeEditing);
		NodeEditing.setBorder(
				new TitledBorder(null, "Node Editing", TitledBorder.LEADING, TitledBorder.TOP, null, Color.black));
		NodeEditing.setLayout(new GridLayout(0, 2, 0, 0));
		startBtn = new JButton("Start");
		startBtn.addActionListener(handler);
		startBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
		NodeEditing.add(startBtn);
		waypointBtn = new JButton("Waypoint");
		waypointBtn.addActionListener(handler);
		waypointBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
		NodeEditing.add(waypointBtn);
		endBtn = new JButton("End");
		endBtn.addActionListener(handler);
		endBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
		NodeEditing.add(endBtn);
		obstacleBtn = new JButton("Obstacle");
		obstacleBtn.addActionListener(handler);
		obstacleBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
		NodeEditing.add(obstacleBtn);

		/***********************************************************
		 * SouthEast Panel setup *
		 ***********************************************************/

		southeastPanel = new JPanel();
		interaction.add(southeastPanel);
		southeastPanel.setLayout(new BoxLayout(southeastPanel, BoxLayout.Y_AXIS));

		GridControls = new JPanel();
		GridControls
				.setBorder(new TitledBorder(null, "Grid Controls", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		southeastPanel.add(GridControls);
		GridControls.setLayout(new BoxLayout(GridControls, BoxLayout.Y_AXIS));

		slider = new JSlider(0, 4);
		slider.setMinorTickSpacing(1);
		slider.setMajorTickSpacing(5);
		slider.setSnapToTicks(true);

		slider.setPreferredSize(new Dimension(100, 30));
		GridControls.add(slider);

		lblGridSizex = new JLabel("Grid Size: 10x10");
		lblGridSizex.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblGridSizex.setAlignmentX(0.5f);
		GridControls.add(lblGridSizex);

		NodeType = new JPanel();
		NodeType.setBorder(new TitledBorder(null, "Node Type", TitledBorder.LEADING, TitledBorder.TOP, null, null));

		southeastPanel.add(NodeType);
		selectedNode = new SelectedTypePanel();
		NodeType.add(selectedNode);

		selectedNode.setPreferredSize(new Dimension(50, 50));

		/***********************************************************
		 * content Panel setup *
		 ***********************************************************/
		content.setLayout(new BorderLayout());
		content.add(gridP, BorderLayout.WEST);
		content.add(interaction, BorderLayout.EAST);
		content.addKeyListener(handler);
		gridP.setFocusable(true);
		gridP.addMouseListener(handler);
		gridP.addMouseMotionListener(handler);
		getContentPane().add(content);

		pack();
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("PathFinding v3 by Andre Pascoa");
		setResizable(false);
		setVisible(true);

		SwingWorker<Void, Void> worker = new SettingsWorker();
		worker.execute();

	}

}
