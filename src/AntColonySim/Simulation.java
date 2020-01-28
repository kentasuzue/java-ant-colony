package AntColonySim;

import javax.swing.Timer;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

 public class Simulation implements SimulationEventListener, ActionListener {

// public class Simulation implements SimulationEventListener {
// public class Simulation {
	
	// add Random method
	
	public final static int COLONY_NODE_SIZE_X = 27;
	public final static int COLONY_NODE_SIZE_Y = 27;

	public final static int ACTION_DELAY = 1000;

//	public final static int ACTION_DELAY = 1;

	public Timer timer;
	
	public static SimTime simTime;

	public AntSimGUI gui;
	
	public ColonyView colonyView;
	
//	public TimerHandler timerHandler;
	
//	public SimHandler simHandler;

	public Colony colony; 
	
	public ColonyNodeView colonyNodeView;

/*
	public Simulation (AntSimGUI antSimGUI) {
		
		gui = antSimGUI;
		
		colonyView = new ColonyView(COLONY_NODE_SIZE_X, COLONY_NODE_SIZE_Y);

		colony = new Colony(colonyView);
		
//		gui.addSimulationEventListener(simHandler);
		
		gui.addSimulationEventListener(this);


		gui.initGUI(colonyView);

		simTime = new SimTime();
		
		gui.setTime(simTime.toString());

	}
	
	public void start() {


//		gui.initGUI(colonyView);

//		simTime = new SimTime();
		
//		gui.setTime(simTime.toString());

		System.out.println("STARTING TIMER");


		timer = new Timer(ACTION_DELAY, timerHandler);

		timer.start();	

	}
*/
	
	public Simulation (AntSimGUI antSimGUI) {
		
		gui = antSimGUI;

		//		start(antSimGUI);
		
		start();
	}
	
//	public void start(AntSimGUI antSimGUI)  {

	public void start()  {

		colonyView = new ColonyView(COLONY_NODE_SIZE_X, COLONY_NODE_SIZE_Y);

		colony = new Colony(colonyView);
		
		gui.addSimulationEventListener(this);


		gui.initGUI(colonyView);

		simTime = new SimTime();
		
		gui.setTime(simTime.toString());

		System.out.println("STARTING TIMER");


//		timer = new Timer(ACTION_DELAY, timerHandler);

	}

	public void stop() {

		timer.stop();	
	
	}
	
	public void actionPerformed(ActionEvent e) {
		singleTurn();
	}
	
	public void singleTurn() {

		// System.out.println("SINGLE TURN");

		simTime.incrementSimTime();
		gui.setTime(simTime.toString());

		// end sim when queen dies
		if (!(colony.singleTurnAnts())) {
			stop();
		}
		// loop through ants
		// true if queen alive
		// false if queen dead

		
		// loop through nodes to decrease pheromones every 10th turn
		if (simTime.pheromoneTurn() == true) {
			colony.updateNodePheromone();
		}
		
		colony.updateNodeAntIcons();

		colony.updateNodeAntCounts();
	
	}
	
	// continuous()

	public void simulationEventOccurred(SimulationEvent simEvent) {

		int QUEEN_START;
		int FORAGER_START;
		int SCOUT_START;
		int SOLDIER_START;
		int BALA_START;

		// types of events
		
		// set up simulation for normal run
		final int NORMAL_SETUP_EVENT = 0;
		
		// set up simulation for testing queen ant
		final int QUEEN_TEST_EVENT = 1;
		
		// set up simulation for testing scout ant
		final int SCOUT_TEST_EVENT = 2;
		
		// set up simulation for testing forager ant
		final int FORAGER_TEST_EVENT = 3;
		
		// set up simulation for testing soldier ant
		final int SOLDIER_TEST_EVENT = 4;
		
		// run simulation continuously
		final int RUN_EVENT = 5;
		
		// run simulation one turn at a time
		final int STEP_EVENT = 6;
		
		switch(simEvent.getEventType()) {
		
		case NORMAL_SETUP_EVENT:

			QUEEN_START = 1;
			FORAGER_START = 50;
			SCOUT_START = 4;
			SOLDIER_START = 10;
			BALA_START = 0;

			System.out.println("NORMAL_SETUP_EVENT!");
			colony.setAntsStart(QUEEN_START, FORAGER_START, SCOUT_START, SOLDIER_START, BALA_START);

//			System.out.println("after set ants start!");

			colony.updateNodeAntIcons();

			colony.updateNodeAntCounts();

//			System.out.println("after update ant icons!");


//			gui.setTime(simTime.toString() + "Normal Setup");
			gui.setTime(simTime.toString());

			
			System.out.println("AFTER NORMAL_SETUP_EVENT!");

			break;

		case QUEEN_TEST_EVENT:

			QUEEN_START = 1;
			FORAGER_START = 0;
			SCOUT_START = 0;
			SOLDIER_START = 0;
			BALA_START = 0;

			System.out.println("QUEEN_TEST_EVENT!");
			colony.setAntsStart(QUEEN_START, FORAGER_START, SCOUT_START, SOLDIER_START, BALA_START);
			colony.updateNodeAntIcons();
			colony.updateNodeAntCounts();

			break;
			
		case SCOUT_TEST_EVENT:

			QUEEN_START = 1;
			FORAGER_START = 0;
			SCOUT_START = 1;
			SOLDIER_START = 0;
			BALA_START = 0;

			System.out.println("SCOUT_TEST_EVENT!");
			colony.setAntsStart(QUEEN_START, FORAGER_START, SCOUT_START, SOLDIER_START, BALA_START);
			colony.updateNodeAntIcons();
			colony.updateNodeAntCounts();

			break;
		
		case FORAGER_TEST_EVENT:

			QUEEN_START = 1;
			FORAGER_START = 1;
//			SCOUT_START = 1;

			SCOUT_START = 0;
			SOLDIER_START = 0;
//			BALA_START = 1;
			BALA_START = 0;

			System.out.println("FORAGER_TEST_EVENT!");
			colony.setAntsStart(QUEEN_START, FORAGER_START, SCOUT_START, SOLDIER_START, BALA_START);
			colony.updateNodeAntIcons();
			colony.updateNodeAntCounts();

			break;

		case SOLDIER_TEST_EVENT:

			QUEEN_START = 1;
			FORAGER_START = 0;
			SCOUT_START = 0;
			SOLDIER_START = 1;
			BALA_START = 1;
			
			System.out.println("SOLDIER_TEST_EVENT!");
			colony.setAntsStart(QUEEN_START, FORAGER_START, SCOUT_START, SOLDIER_START, BALA_START);
			colony.updateNodeAntIcons();
			colony.updateNodeAntCounts();
			
			break;
		
		case RUN_EVENT:

			System.out.println("RUN_EVENT!");

			timer = new Timer(ACTION_DELAY, this);

			timer.start();	


			break;
			
		case STEP_EVENT:
			System.out.println("STEP_EVENT!");
			singleTurn();
			break;
		}
	}
	
	public static SimTime getSimTime() {
		return simTime;
	}

/*
	public class SimHandler implements SimulationEventListener {

		// types of events
		
		// set up simulation for normal run
		public final static int NORMAL_SETUP_EVENT = 0;
		
		// set up simulation for testing queen ant
		public final static int QUEEN_TEST_EVENT = 1;
		
		// set up simulation for testing scout ant
		public final static int SCOUT_TEST_EVENT = 2;
		
		// set up simulation for testing forager ant
		public final static int FORAGER_TEST_EVENT = 3;
		
		// set up simulation for testing soldier ant
		public final static int SOLDIER_TEST_EVENT = 4;
		
		// run simulation continuously
		public final static int RUN_EVENT = 5;
		
		// run simulation one turn at a time
		public final static int STEP_EVENT = 6;

		public void simulationEventOccurred(SimulationEvent simEvent) {

			switch(simEvent.getEventType()) {
			
			case NORMAL_SETUP_EVENT:
				System.out.println("NORMAL_SETUP_EVENT!");
				start();
				break;

			case QUEEN_TEST_EVENT:
				break;
				
			case SCOUT_TEST_EVENT:
				break;
			
			case FORAGER_TEST_EVENT:
				break;

			case SOLDIER_TEST_EVENT:
				break;
			
			case RUN_EVENT:
				break;
				
			case STEP_EVENT:
				singleTurn();
				break;
			}
		}
		
	}
*/

/*
	private class TimerHandler implements ActionListener {

		public void actionPerformed(ActionEvent actionEvent) {

			singleTurn();
		}

	}
*/	
}
