package AntColonySim;

public class Bala extends Ant {
	
	private static NodeCoords newBalaNodeCoords;

	Bala(int uniqueIDCounterParam) {
		
		super(uniqueIDCounterParam, newBalaNodeCoords = getNewBalaNodeCoords());

		addAntToNode(uniqueIDCounterParam, AntType.BALA, newBalaNodeCoords);

		System.out.println("new bala ant with ID#" + uniqueID + " at " + newBalaNodeCoords + " at: " + Simulation.getSimTime());
		

		//change default Node to random border Node

	}
	
	public static NodeCoords getNewBalaNodeCoords() {
		
		int howManyBorderNodes = NodeCoordList.borderNodeLinkedList.size();
		
		// need -1 or will be out of range of linkedlist
		int indexRandomBorderNode = Colony.getRandom(LOWEST_LL_INDEX, howManyBorderNodes-1);
		
		NodeCoords randomBorderNodeCoords = ( (NodeCoords)(NodeCoordList.borderNodeLinkedList.get(indexRandomBorderNode)) ).copy();
		
		newBalaNodeCoords = randomBorderNodeCoords;
		
		return newBalaNodeCoords ;
	}
	
	// override Ant move()
	public void move() {
		
		Node antNode = getNode(nodeCoords);
		
		// if any bala ants in node, don't move
		if (antNode.isAnyFriendAnts()) {

			System.out.println("Bala ID#" + uniqueID + " at: " + nodeCoords + " staying put with friend node");

			return;
		}
		

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

	}
	
	public void act() {
	}

	// override Ant attack()
	public void attack() {
		
		Node antNode = getNode(nodeCoords);
		
		// if any bala ants in node, then attack
		if (antNode.isAnyFriendAnts()) {

			System.out.println("Bala ID#" + uniqueID + " at: " + nodeCoords + " attacking in friend node");

			int killedAnt;
			
			if (Colony.getRandom(CHANCE_MIN, CHANCE_MAX) > CHANCE_ATTACK) {
				killedAnt = antNode.getRandomFriendAnt();
				ants.addToKilledAntsLL(killedAnt);
				System.out.println("Bala ID#" + uniqueID + " killed friend ant ID#" + killedAnt);
			}
		}		
	}
}
