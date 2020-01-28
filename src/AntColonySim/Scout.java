package AntColonySim;

public class Scout extends Ant {

	Scout(int uniqueIDCounterParam) {

		super(uniqueIDCounterParam, COLONY_NODE_CENTER_NODECOORDS);

		addAntToNode(uniqueIDCounterParam, AntType.SCOUT, COLONY_NODE_CENTER_NODECOORDS);
		
	}

	// override Ant move()
	public void move() {
		
		LinkedList adjNodeList = ((getNode(nodeCoords)).getAdjNodeList()).getAdjLinkedList();

		int howManyAdjNodes = adjNodeList.size();

//		System.out.println("How many adj nodes = " + howManyAdjNodes);

		// need minus 1 else will be out of range
		int indexRandomAdjNode = Colony.getRandom(LOWEST_LL_INDEX, howManyAdjNodes-1);

//		System.out.println("random index = " + indexRandomAdjNode);
//		System.out.println("Ant moving from " + nodeCoords);
		
		NodeCoords randomAdjNodeNodeCoords = ((NodeCoords)(adjNodeList.get(indexRandomAdjNode))).copy();
//		System.out.println("Ant moving to node " + randomAdjNodeNodeCoords);

		// remove ant from old Node
		removeAntFromNode();
		
		nodeCoords = randomAdjNodeNodeCoords;

		// addAntToNode must come after update of ant's nodeCoords to new node
		addAntToNode();
		
		Node newNode = getNode(nodeCoords);

		newNode.showNode();
	}
	
	public void act() {
	}

	// override Ant attack()
	public void attack() {
		
	}


}
