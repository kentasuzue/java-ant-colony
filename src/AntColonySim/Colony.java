package AntColonySim;

import java.util.Random;

public class Colony {

	public final static int COLONY_NODE_SIZE_X = 27;
	public final static int COLONY_NODE_SIZE_Y = 27;

	public static final int COLONY_NODE_CENTER_INDEX_X = COLONY_NODE_SIZE_X / 2;
	public static final int COLONY_NODE_CENTER_INDEX_Y = COLONY_NODE_SIZE_Y / 2;
	
	//percentage chance out of 100 that square has food
	public static final int CHANCE_FOOD = 25;
	//if square has food, minimum amount of food
	public static final int FOOD_MIN = 500;
	//if square has food, maximum amount of food
	public static final int FOOD_MAX = 1000;
	

	private ColonyView colonyView;
//	private static ColonyView colonyView;

	private ColonyNodeView colonyNodeView;

	private Node[][] colonyNode;

	private Ants ants;
	
	private static Random random; 
	

	Colony(ColonyView colonyViewParam) {
		
		random = new Random();
		
		colonyView = colonyViewParam;

		colonyNode = new Node[COLONY_NODE_SIZE_X][COLONY_NODE_SIZE_Y];

		for (int i = 0; i < COLONY_NODE_SIZE_X; i++) {

			for (int j = 0; j < COLONY_NODE_SIZE_Y; j++) {
				
				colonyNodeView = new ColonyNodeView();
				
				NodeCoords nodeCoords = new NodeCoords(i,j);
				
				colonyView.addColonyNodeView(colonyNodeView, i, j);
				
				colonyNode[i][j] = new Node(colonyNodeView, nodeCoords);
				
//				colonyNode[i][j].showNode();
//				colonyNode[i][j].showQueenIcon();
//				colonyNode[i][j].showBalaIcon();
//				colonyNode[i][j].showSoldierIcon();
//				colonyNode[i][j].showScoutIcon();
//				colonyNode[i][j].showForagerIcon();
				colonyNode[i][j].setID(i + ", " + j);
				
				// random # of 1-100, chance of food in node
				if (getRandom(1,100) <= CHANCE_FOOD) {
					// set food at node to random number between FOOD_MIN and FOOD_MAX
					colonyNode[i][j].setNodeFood(getRandom(FOOD_MIN,FOOD_MAX));
				}
				else {
					// if no food then set food to 0
					colonyNode[i][j].setNodeFood(0);
				}
				
				// set initial pheromone level to 0
				colonyNode[i][j].setNodePher(0);
			}
		}

		// make only center 9 nodes visible
		// make center 9 nodes have 0 food
		for (int i = COLONY_NODE_CENTER_INDEX_X - 1; i <= COLONY_NODE_CENTER_INDEX_X + 1; i++) {
			for (int j = COLONY_NODE_CENTER_INDEX_Y - 1; j <= COLONY_NODE_CENTER_INDEX_Y + 1; j++) {
				colonyNode[i][j].showNode();
				colonyNode[i][j].setNodeFood(0);
				
			}
		}

		// make center node have max food
		colonyNode[COLONY_NODE_CENTER_INDEX_X][COLONY_NODE_CENTER_INDEX_Y].setNodeFood(FOOD_MAX);
		
		//
		Ant.setColonyNode(colonyNode);

/*
		for (int i = 0; i < COLONY_NODE_SIZE_X; i++) {

			for (int j = 0; j < COLONY_NODE_SIZE_Y; j++) {

				NodeCoords nodeCoords = new NodeCoords(i,j);
//				System.out.println("Colony: " + nodeCoords);
				Ant.findAdjVisibleNodes(nodeCoords);
			}
		}
*/
		
		
		// initialize border node list
		NodeCoordList.setBorderNodeList();
		// System.out.println("Border nodes: " + NodeCoordList.toStringBorderNodeList());

		
		ants = new Ants();

	}
	
	public static int getRandom (int minInt, int maxInt) {
		return (random.nextInt(maxInt - minInt + 1) + minInt);
	}
	
	public void setAntsStart(int queenParam, int foragerParam, int scoutParam, int soldierParam, int balaParam) {
		
//		System.out.println("In set ants start, queenParam = " + queenParam);
		
		
		
		for (int i = 0; i < queenParam; i++ ) {
			ants.addQueen();	
		}

//		System.out.println("After add queen");

		
		for (int i = 0; i < foragerParam; i++ ) {
			ants.addForager();	
		}

		for (int i = 0; i < scoutParam; i++ ) {
			ants.addScout();	
		}

		for (int i = 0; i < soldierParam; i++ ) {
			ants.addSoldier();	
		}

		for (int i = 0; i < balaParam; i++ ) {
			ants.addBala();	
		}

//		System.out.println("After set ants start");
		

	}
	
	public void updateNodeAntIcons() {
		
		for (int i = 0; i < COLONY_NODE_SIZE_X; i++) {

			for (int j = 0; j < COLONY_NODE_SIZE_Y; j++) {
				
				colonyNode[i][j].updateAntIcons();
			}
		}

	}

	public void updateNodeAntCounts() {
		
		for (int i = 0; i < COLONY_NODE_SIZE_X; i++) {

			for (int j = 0; j < COLONY_NODE_SIZE_Y; j++) {
				
				colonyNode[i][j].updateAntCounts();
			}
		}

	}

	public void updateNodePheromone() {
		
		for (int i = 0; i < COLONY_NODE_SIZE_X; i++) {

			for (int j = 0; j < COLONY_NODE_SIZE_Y; j++) {
				
				colonyNode[i][j].updatePheromone();
			}
		}

	}


	// return true if queen alive
	// return false if queen dead

	public boolean singleTurnAnts() {
	
		return ants.singleTurnLL();
	}

	
}
