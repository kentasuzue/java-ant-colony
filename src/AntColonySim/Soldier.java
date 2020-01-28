package AntColonySim;

public class Soldier extends Ant {
	
	NodeCoords nodeCoordsItr;
	
	Node nodeItr;

	Soldier(int uniqueIDCounterParam) {

		super(uniqueIDCounterParam, COLONY_NODE_CENTER_NODECOORDS);

		addAntToNode(uniqueIDCounterParam, AntType.SOLDIER, COLONY_NODE_CENTER_NODECOORDS);
	}

	
	// override Ant move()
	public void move() {

		Node antNode = getNode(nodeCoords);
		
		// if any bala ants in node, don't move
		if (antNode.isAnyEnemyAnts()) {

			System.out.println("Soldier ID#" + uniqueID + " at: " + nodeCoords + " staying put with bala node");

			return;
		}
		
		// if any bala ants in any adjacent visible node, move to a random adjacent visible node with any bala ants
		LinkedList adjVisibleNodes = findAdjVisibleNodes(nodeCoords);

		int howManyAdjVisibleNodes = adjVisibleNodes.size();


		// determine if soldier adjacent to node with bala
		boolean adjBalaNodeFlag = false;

/*
		// set adjBalaNodeFlag = true, if any adjacent visible node has a bala
		for (Iterator itr = adjVisibleNodes.iterator(); itr.hasNext(); ) {
			
			nodeCoordsItr = (NodeCoords) itr.getCurrent();
					
			nodeItr = getNode(nodeCoordsItr);

			if (nodeItr.isAnyEnemyAnts()) {
				adjBalaNodeFlag = true;
			}
		
			itr.next();
		}
*/	
//		if (adjBalaNodeFlag == true) {

		LinkedList adjBalaNodeList = new LinkedList();
		
//		System.out.println("Ant findAdjvisibleNodes " + nodeCoordsParam);

		LinkedList adjNodeList = ((getNode(nodeCoords)).getAdjNodeList()).getAdjLinkedList();

		NodeCoords nodeCoordsItr;
		
		Node nodeItr;
		
		// iterate through list of adjacent visible nodes, and make list of adjacent visible nodes with a bala
		for (Iterator itr = adjNodeList.iterator(); itr.hasNext(); ) {
			
			nodeCoordsItr = (NodeCoords) itr.getCurrent();
					
			nodeItr = getNode(nodeCoordsItr);

			if (nodeItr.isAnyEnemyAnts()) {
				adjBalaNodeList.add(nodeCoordsItr.copy());
			}
			
			itr.next();
		}

		// if list of adjacent visible nodes with a bala is not empty, move to one of those nodes
		if (!adjBalaNodeList.isEmpty()) {
			
			int howManyAdjBalaNodes = adjBalaNodeList.size();

//			System.out.println("How many adj visible nodes = " + howManyAdjVisibleNodes);

			// need minus 1 else will be out of range
			int indexRandomAdjBalaNode = Colony.getRandom(LOWEST_LL_INDEX, howManyAdjBalaNodes-1);

//			System.out.println("random index = " + indexRandomAdjVisibleNode);
//			System.out.println("Ant moving from " + nodeCoords);
			
			
//			NodeCoords randomAdjVisibleNodeNodeCoords = (NodeCoords)(adjVisibleNodes.get(indexRandomAdjVisibleNode));
			NodeCoords randomAdjBalaNodeNodeCoords = ((NodeCoords)(adjBalaNodeList.get(indexRandomAdjBalaNode))).copy();
//			System.out.println("Ant moving to node " + randomAdjVisibleNodeNodeCoords);
			
			// remove ant from old Node
			removeAntFromNode();
	
			System.out.println("Soldier ID#" + uniqueID + " moving from " + nodeCoords + " to Bala node at: " + randomAdjBalaNodeNodeCoords);
			
			nodeCoords = randomAdjBalaNodeNodeCoords;

			// addAntToNode must come after update of ant's nodeCoords to new node
			addAntToNode();
	
			return;
		}
		
		
		// move randomly, if no bala ants in node, and no bala ants in adjacent node
		super.move();
	}
	
	public void act() {
	}

	// override Ant attack()
	public void attack() {
		
		Node antNode = getNode(nodeCoords);
		
		// if any bala ants in node, then attack
		if (antNode.isAnyEnemyAnts()) {

			System.out.println("Soldier ID#" + uniqueID + " at: " + nodeCoords + " attacking in bala node");

			int killedAnt;
			
			if (Colony.getRandom(CHANCE_MIN, CHANCE_MAX) > CHANCE_ATTACK) {
				killedAnt = antNode.getRandomEnemyAnt();
				ants.addToKilledAntsLL(killedAnt);
				
				System.out.println("Soldier ID#" + uniqueID + " killed enemy ant ID#" + killedAnt);
			}
		}		
	}


}
