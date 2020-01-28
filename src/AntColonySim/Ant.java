package AntColonySim;

public abstract class Ant {

	public final static int COLONY_NODE_SIZE_X = 27;
	public final static int COLONY_NODE_SIZE_Y = 27;

	public static final int COLONY_NODE_CENTER_INDEX_X = COLONY_NODE_SIZE_X / 2;
	public static final int COLONY_NODE_CENTER_INDEX_Y = COLONY_NODE_SIZE_Y / 2;

	public static final NodeCoords COLONY_NODE_CENTER_NODECOORDS = new NodeCoords(COLONY_NODE_CENTER_INDEX_X, COLONY_NODE_CENTER_INDEX_Y);
	
	public final static int TURNS_IN_DAY = 10;

	public final static int DAYS_IN_YEAR = 365;

	public static final int ANT_LIFESPAN_YEARS = 1;
	
	public static final int LOWEST_LL_INDEX = 0;
	
	//percentage chance out of 100 that an attack kills
	public static final int CHANCE_ATTACK = 50;
	
	public static final int CHANCE_MIN = 1;
	
	public static final int CHANCE_MAX = 100;

	protected static Node[][] colonyNode;

	protected NodeCoords nodeCoords;

	protected int uniqueID;
	
	protected int lifespanTurnsLeft;
	
	// so Ant instances can access Ants
	protected static Ants ants;

	Ant(int uniqueIDCounterParam, NodeCoords nodeCoordsParam) {

		nodeCoords = nodeCoordsParam.copy();

		uniqueID = uniqueIDCounterParam;
		
		lifespanTurnsLeft = ANT_LIFESPAN_YEARS * DAYS_IN_YEAR * TURNS_IN_DAY;
		
	}

	public static void setAnts (Ants antsparam) {
		ants = antsparam;
	}

	public void move() {
		
		
//		Node oldNode = getNode(nodeCoords);
		
		LinkedList adjVisibleNodes = findAdjVisibleNodes(nodeCoords);

		int howManyAdjVisibleNodes = adjVisibleNodes.size();

//		System.out.println("How many adj visible nodes = " + howManyAdjVisibleNodes);

		// need minus 1 else will be out of range
		int indexRandomAdjVisibleNode = Colony.getRandom(LOWEST_LL_INDEX, howManyAdjVisibleNodes-1);

//		System.out.println("random index = " + indexRandomAdjVisibleNode);
//		System.out.println("Ant moving from " + nodeCoords);
		
		
//		NodeCoords randomAdjVisibleNodeNodeCoords = (NodeCoords)(adjVisibleNodes.get(indexRandomAdjVisibleNode));
		NodeCoords randomAdjVisibleNodeNodeCoords = ((NodeCoords)(adjVisibleNodes.get(indexRandomAdjVisibleNode))).copy();
//		System.out.println("Ant moving to node " + randomAdjVisibleNodeNodeCoords);
		
		// remove ant from old Node
		removeAntFromNode();
		
		nodeCoords = randomAdjVisibleNodeNodeCoords;

		// addAntToNode must come after update of ant's nodeCoords to new node
		addAntToNode();
	}
	
	public abstract void act();
	
	public void attack() {
	
	}

	public static void setColonyNode(Node[][] colonyNodeParam) {
		colonyNode = colonyNodeParam;
	}
	
	protected Node getNode() {

//		System.out.println("Ant getNode " + nodeCoordsParam);

//		System.out.println("Ant getNode X=" + nodeCoordsParam.getX() + ", Y=" + nodeCoordsParam.getY());

		return colonyNode[nodeCoords.getX()][nodeCoords.getY()];
	}

	protected static Node getNode(NodeCoords nodeCoordsParam) {

//		System.out.println("Ant getNode " + nodeCoordsParam);

//		System.out.println("Ant getNode X=" + nodeCoordsParam.getX() + ", Y=" + nodeCoordsParam.getY());

		return colonyNode[nodeCoordsParam.getX()][nodeCoordsParam.getY()];
	}

	public static LinkedList findAdjVisibleNodes(NodeCoords nodeCoordsParam) {

		LinkedList adjVisibleNodeList = new LinkedList();
		
//		System.out.println("Ant findAdjvisibleNodes " + nodeCoordsParam);

		LinkedList adjNodeList = ((getNode(nodeCoordsParam)).getAdjNodeList()).getAdjLinkedList();

		NodeCoords nodeCoordsItr;
		
		Node nodeItr;
		
		for (Iterator itr = adjNodeList.iterator(); itr.hasNext(); ) {
			
			nodeCoordsItr = (NodeCoords) itr.getCurrent();
					
			nodeItr = getNode(nodeCoordsItr);

			if (nodeItr.getNodeVisible()) {
				adjVisibleNodeList.add(nodeCoordsItr.copy());
			}
			
			itr.next();
		}
		
//		System.out.println("Node: " + nodeCoordsParam + " has adjVisibleNodes: " + adjVisibleNodeList);


		return adjVisibleNodeList;
		
	}
	
	public static LinkedList findAdjVisiblePherNodes(NodeCoords nodeCoordsParam) {

		LinkedList adjVisiblePherNodeList = new LinkedList();
		
//		System.out.println("Ant findAdjvisibleNodes " + nodeCoordsParam);

		LinkedList adjNodeList = ((getNode(nodeCoordsParam)).getAdjNodeList()).getAdjLinkedList();

		NodeCoords nodeCoordsItr;
		
		Node nodeItr;
		
		for (Iterator itr = adjNodeList.iterator(); itr.hasNext(); ) {
			
			nodeCoordsItr = (NodeCoords) itr.getCurrent();
					
			nodeItr = getNode(nodeCoordsItr);

			if (nodeItr.getNodeVisible() && (nodeItr.getNodePher() > 0)) {
				adjVisiblePherNodeList.add(nodeCoordsItr.copy());
			}
			
			itr.next();
		}
		
//		System.out.println("Node: " + nodeCoordsParam + " has adjVisibleNodes: " + adjVisibleNodeList);


		return adjVisiblePherNodeList;
		
	}
	
	public int decrementLifespan() {
		
		lifespanTurnsLeft--;
		
		return lifespanTurnsLeft;
	}

	public void addAntToNode() {

		addAntToNode(uniqueID, getAntType(),nodeCoords);
	}


	public void addAntToNode(int antUniqueIDParam, AntType antTypeParam, NodeCoords nodeCoordsParam) {
		int nodeCoordX = nodeCoordsParam.getX();
		int nodeCoordY = nodeCoordsParam.getY();
		colonyNode[nodeCoordX][nodeCoordY].addAntToNode(antUniqueIDParam, antTypeParam);
	}

	public void removeAntFromNode() {

		removeAntFromNode(uniqueID, getAntType(),nodeCoords);
	}

	public void removeAntFromNode(int antUniqueIDParam, AntType antTypeParam, NodeCoords nodeCoordsParam) {
		int nodeCoordX = nodeCoordsParam.getX();
		int nodeCoordY = nodeCoordsParam.getY();
		colonyNode[nodeCoordX][nodeCoordY].removeAntFromNode(antUniqueIDParam, antTypeParam);
	}

	public int getUniqueID() {
		return uniqueID;
	}

	
	public AntType getAntType() {

		if (this instanceof Queen) {
			return AntType.QUEEN;
		}
		else if (this instanceof Forager) {
			return AntType.FORAGER;
		}
		else if (this instanceof Scout) {
			return AntType.SCOUT;
		}
		else if (this instanceof Soldier) {
			return AntType.SOLDIER;
		}
		else {
			return AntType.BALA;
		}

	}
}
