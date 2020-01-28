package AntColonySim;

public class Forager extends Ant {

/*
 
sortedPathHistory: LinkedList
 
*/

	// foragerMode == 0 wait mode
	// foragerMode == 1 forage randomly mode
	// foragerMode == 2 pheromone following mode, from mode 1 NOT at colony entrance
	// foragerMode == 3 pheromone following mode, from mode 1 AT colony entrance
	// foragerMode == 4 return to nest WITH food mode
	// foragerMode == 5 return to nest WITHOUT food mode
	
	public enum ForagerMode {MODE0, MODE1, MODE2, MODE3, MODE4, MODE5}
	
	private ForagerMode mode;
	
	private LinkedList nodeCoordsHistory;
	
	// history of just last node needed to decrement food of last node
	// nodeCoordsHistory can't be used, since on return to colony entrance, nodeCoordsHistory is being erased
	private NodeCoords lastNodeCoords;

	// true if forager just entered MODE4
	// false if forager has been in MODE4 at least 1 turn
	// helps track how to increment and decrement node food as forager carries food back to colony entrance 
	private boolean newMODE4Flag;
	

//	private boolean foodCarryFlag;
	
	Forager(int uniqueIDCounterParam) {

		super(uniqueIDCounterParam, COLONY_NODE_CENTER_NODECOORDS);

		addAntToNode(uniqueIDCounterParam, AntType.FORAGER, COLONY_NODE_CENTER_NODECOORDS);

		mode = ForagerMode.MODE1;
		
		nodeCoordsHistory = new LinkedList();
		
		// foodCarryFlag = false;
	}

	
	// override Ant move()
	public void move() {

		
		updateMode();
		moveMode();
/*
		forage();
		sortPathNodes();
		returnNest();

		super.move();
*/	
	}
	
	public void moveMode() {

		Node foragerNode = getNode(nodeCoords);// colonyNode[nodeCoords.getX()][nodeCoords.getY()];

		LinkedList adjVisibleNodes, adjVisiblePherNodes;
		
		NodeCoords nodeCoordsItr;
		
		Node nodeItr;
		
		int howManyAdjVisibleNodes, howManyAdjVisiblePherNodes;
		
		int indexRandomAdjVisibleNode, indexRandomAdjVisiblePherNode;
		
		NodeCoords randomAdjVisibleNodeNodeCoords, randomAdjVisiblePherNodeNodeCoords, destAdjVisiblePherNodeNodeCoords;
		
		switch (mode) {
		
		//in wait mode in between forage mode #1/2/3 and return-to-nest carrying mode #4 
		case MODE0:
//			System.out.println("moveMode begin " + mode + " Forager ID#" + uniqueID);

			break;
		
		// in forage randomly mode
		case MODE1:
			
//			System.out.println("moveMode begin " + mode + " Forager ID#" + uniqueID);

			//System.out.println("moveMode MODE1 begin");
			
			adjVisibleNodes = findAdjVisibleNodes(nodeCoords);
			
			// remove most recent node from choices since don't want to follow trail backwards (unless history empty), 
			if (!nodeCoordsHistory.isEmpty()) {
				NodeCoords priorNodeCoords = (NodeCoords) nodeCoordsHistory.getFirst();
				adjVisibleNodes.remove(priorNodeCoords);
			}

			howManyAdjVisibleNodes = adjVisibleNodes.size();

//			System.out.println("How many adj visible nodes = " + howManyAdjVisibleNodes);

			// need minus 1 else will be out of range
			indexRandomAdjVisibleNode = Colony.getRandom(LOWEST_LL_INDEX, howManyAdjVisibleNodes-1);

//			System.out.println("random index = " + indexRandomAdjVisibleNode);
//			System.out.println("Ant moving from " + nodeCoords);
			
			
//			NodeCoords randomAdjVisibleNodeNodeCoords = (NodeCoords)(adjVisibleNodes.get(indexRandomAdjVisibleNode));
			randomAdjVisibleNodeNodeCoords = ((NodeCoords)(adjVisibleNodes.get(indexRandomAdjVisibleNode))).copy();
//			System.out.println("Ant moving to node " + randomAdjVisibleNodeNodeCoords);
			
			// remove ant from old Node
			removeAntFromNode();

//			System.out.println("moveMode before MODE1 addFirst Forager ID#"+ uniqueID + " has nodeCoordsHistory: " + NodeCoordList.toStringNodeList(nodeCoordsHistory));

			// add old nodeCoords to history before updating nodeCords to new value
			nodeCoordsHistory.addFirst(nodeCoords);
//			System.out.println("moveMode after MODE1 addFirst Forager ID#"+ uniqueID + " has nodeCoordsHistory: " + NodeCoordList.toStringNodeList(nodeCoordsHistory));
			
			nodeCoords = randomAdjVisibleNodeNodeCoords;

			// addAntToNode must come after update of ant's nodeCoords to new node
			addAntToNode();

			break;

		// in forage following pheromone mode, 
		// either from mode 1 not at colony entrance
		// or from mode 1 at colony entrance
		// MODE 2 and MODE 3 the same
		case MODE2:
		case MODE3:
			
//			System.out.println("moveMode begin " + mode + " Forager ID#" + uniqueID);
			
			//System.out.println("moveMode MODE2MODE3 begin");
			
			LinkedList highPherNodeList = new LinkedList();
			
			adjVisiblePherNodes = findAdjVisiblePherNodes(nodeCoords);
			
			// remove most recent node from choices since don't want to follow trail backwards (unless history empty), 
			if (!nodeCoordsHistory.isEmpty()) {
				NodeCoords priorNodeCoords = (NodeCoords) nodeCoordsHistory.getFirst();
				adjVisiblePherNodes.remove(priorNodeCoords);
			}

			howManyAdjVisiblePherNodes = adjVisiblePherNodes.size();
			
			// if only 1 node left, set destination to the only 1 node left 
			if (howManyAdjVisiblePherNodes == 1) {
				destAdjVisiblePherNodeNodeCoords = (NodeCoords) adjVisiblePherNodes.get();
			}

//			System.out.println("How many adj visible nodes = " + howManyAdjVisibleNodes);
			else {
				
				// find maximum pheromone value
				
				int maxPheromone = 0;

				// iterate through adjacent visible nodes with nonzero pheromone to find maximum pheromone
				for (Iterator itr = adjVisiblePherNodes.iterator(); itr.hasNext(); ) {
					
					nodeCoordsItr = (NodeCoords) itr.getCurrent();
							
					nodeItr = getNode(nodeCoordsItr);

					if (nodeItr.getNodePher() >  maxPheromone) {
						maxPheromone = nodeItr.getNodePher();
					}
					
					itr.next();
				}
				
				// fill highPherNodeList with nodeCoords that have pheromone equal to maxPheromone

				for (Iterator itr = adjVisiblePherNodes.iterator(); itr.hasNext(); ) {
					
					nodeCoordsItr = (NodeCoords) itr.getCurrent();
							
					nodeItr = getNode(nodeCoordsItr);

					if (nodeItr.getNodePher() == maxPheromone) {
						highPherNodeList.add(nodeCoordsItr.copy());
					}
					
					itr.next();
				}
				
				howManyAdjVisiblePherNodes = highPherNodeList.size();

				if (howManyAdjVisiblePherNodes == 1) {
					destAdjVisiblePherNodeNodeCoords = (NodeCoords) adjVisiblePherNodes.get();					
				}
				
				else {
					// need minus 1 else will be out of range
					indexRandomAdjVisiblePherNode = Colony.getRandom(LOWEST_LL_INDEX, howManyAdjVisiblePherNodes-1);

//					System.out.println("random index = " + indexRandomAdjVisibleNode);
//					System.out.println("Ant moving from " + nodeCoords);
					
					
//					NodeCoords randomAdjVisibleNodeNodeCoords = (NodeCoords)(adjVisibleNodes.get(indexRandomAdjVisibleNode));
					randomAdjVisiblePherNodeNodeCoords = ((NodeCoords)(highPherNodeList.get(indexRandomAdjVisiblePherNode))).copy();
					destAdjVisiblePherNodeNodeCoords = randomAdjVisiblePherNodeNodeCoords;					
				}	
			}

			//System.out.println("Ant moving to node " + randomAdjVisibleNodeNodeCoords);
			// remove ant from old Node
			removeAntFromNode();
			
			// add old nodeCoords to history before updating nodeCords to new value
//			System.out.println("moveMode before MODE2/3 addFirst Forager ID#"+ uniqueID + " has nodeCoordsHistory: " + NodeCoordList.toStringNodeList(nodeCoordsHistory));
			nodeCoordsHistory.addFirst(nodeCoords);
//			System.out.println("moveMode after MODE2/3 addFirst Forager ID#"+ uniqueID + " has nodeCoordsHistory: " + NodeCoordList.toStringNodeList(nodeCoordsHistory));
			
			nodeCoords = destAdjVisiblePherNodeNodeCoords;

			// addAntToNode must come after update of ant's nodeCoords to new node
			addAntToNode();

			break;

		//in return-to-nest carrying food mode
		// MODE4 and MODE5 the same except that MODE4 must set lastNode to last node 
		case MODE4:
		case MODE5:
//			System.out.println("moveMode begin " + mode + " Forager ID#" + uniqueID);

			//System.out.println("moveMode MODE4/MODE5 begin");
//			System.out.println("moveMode before " + mode + " removeFirst Forager ID#"+ uniqueID + " has nodeCoordsHistory: " + NodeCoordList.toStringNodeList(nodeCoordsHistory));

			NodeCoords priorNodeCoords = (NodeCoords) nodeCoordsHistory.getFirst();
			// to continue backtracking to colony entrance, remove prior nodeCoords from nodeCoordsHistory
			nodeCoordsHistory.removeFirst();

//			System.out.println("moveMode after " + mode + " removeFirst Forager ID#"+ uniqueID + " has nodeCoordsHistory: " + NodeCoordList.toStringNodeList(nodeCoordsHistory));

			// remove ant from old Node
			removeAntFromNode();
			// set 
			lastNodeCoords = nodeCoords;
//			System.out.println("moveMode after " + mode + " Forager ID#" + uniqueID + " set lastNodeCoords to: " + lastNodeCoords + " (from old nodeCoords)");

/*
			//decrement food prior to moving forager

			if (mode == ForagerMode.MODE4) {
				System.out.println("moveMode Forager ID#" + uniqueID + " about to decrement food at: " + nodeCoords + " with " +
						foragerNode.getFood() + " food.");

				foragerNode.decrementFood();

				System.out.println("moveMode Forager ID#" + uniqueID + " did decrement food at: " + nodeCoords + " with " +
						foragerNode.getFood() + " food.");

			}
			//move forager back to prior node from history
*/			
			nodeCoords = priorNodeCoords;
//			System.out.println("moveMode after " + mode + " Forager ID#"+ uniqueID + " after set nodeCoords to: " + nodeCoords + " (from removeFirst on NodeCoordList)");

/*
			if (mode == ForagerMode.MODE4) {
				foragerNode = getNode(nodeCoords);
				//increment food after moving forager

				System.out.println("moveMode Forager ID#" + uniqueID + " about to increment food at: " + nodeCoords + " with " +
						foragerNode.getFood() + " food.");

				foragerNode.incrementFood();

				System.out.println("moveMode Forager ID#" + uniqueID + " did increment food at: " + nodeCoords + " with " +
						foragerNode.getFood() + " food.");

			}
*/		
//			foragerNode.incrementFood();
//			oldForagerNode.decrementFood();

			addAntToNode();
			
			foragerNode = getNode(nodeCoords);
			Node oldForagerNode = getNode(lastNodeCoords);
			
			if ((newMODE4Flag == true) && (oldForagerNode.getFood() == 0)){
				mode = ForagerMode.MODE5;
			}

			if (mode == ForagerMode.MODE4) {
//			System.out.println("updateMode Forager ID#"+ uniqueID + " has nodeCoordsHistory: " + NodeCoordList.toStringNodeList(nodeCoordsHistory));
			// since forager carried food between nodes, increment food of new node and decrement food of old node
//			System.out.println("updateMode Forager ID#" + uniqueID + " about to increment food to foragerNode at: " + nodeCoords + " with " +
//					foragerNode.getFood() + " food.");

			//				System.out.println("updateMode Forager ID#" + uniqueID + " just incremented food to foragerNode at: " + nodeCoords + " with " +
//					foragerNode.getFood() + " food.");

//			System.out.println("updateMode Forager ID#" + uniqueID + " about to decrement food from oldForagerNode at: " + lastNodeCoords + " with " +
//					oldForagerNode.getFood() + " food.");


//			System.out.println("updateMode Forager ID#" + uniqueID + " just decremented food from oldForagerNode at: " + lastNodeCoords + " with " +
//					oldForagerNode.getFood() + " food.");
				if (nodeCoords.equals(COLONY_NODE_CENTER_NODECOORDS)) {
			
//			System.out.println("updateMode MODE4 NodeCenter Forager ID#" + uniqueID + 
//					" about to increment foodUnits from foragerNode at: " + nodeCoords + " with " + 
//					foragerNode.getFood() + " foodunits; " +
//					" about to decrement transitfood from oldForagerNode at: " + lastNodeCoords + " with " +
//					oldForagerNode.getTransitFood() + " transitfood.");

					foragerNode.incrementFood();
					oldForagerNode.decrementTransitFood();

//			System.out.println("updateMode MODE4 NodeCenter Forager ID#" + uniqueID + 
//					" did increment foodUnits from foragerNode at: " + nodeCoords + " with " + 
//					foragerNode.getFood() + " foodunits; " +
//					" did decrement transitfood from oldForagerNode at: " + lastNodeCoords + " with " +
//					oldForagerNode.getTransitFood() + " transitfood.");
				}
		
//		else if ((newMODE4Flag == true) && (oldForagerNode.getFood() > 0)) {
				else if (newMODE4Flag == true) {

//			System.out.println("updateMode MODE4 newMODE4Flag==true and getFood>0 Forager ID#" + uniqueID + 
//					" about to increment transitfood from foragerNode at: " + nodeCoords + " with " + 
//					foragerNode.getTransitFood() + " transitfood; " +
//					" about to decrement foodunits from oldForagerNode at: " + lastNodeCoords + " with " +
//					oldForagerNode.getFood() + " foodunits.");

					foragerNode.incrementTransitFood();
					oldForagerNode.decrementFood();				
			
//			System.out.println("updateMode MODE4 newMODE4Flag==true and getFood>0 Forager ID#" + uniqueID + 
//					" did increment transitfood from foragerNode at: " + nodeCoords + " with " + 
//					foragerNode.getTransitFood() + " transitfood; " +
//					" did decrement foodunits from oldForagerNode at: " + lastNodeCoords + " with " +
//					oldForagerNode.getFood() + " foodunits.");
				}
		
		
				else {

//			System.out.println("updateMode MODE4 else (newMODE4Flag==false && not NodeCenter) Forager ID#" + uniqueID + 
//					" about to increment transitfood from foragerNode at: " + nodeCoords + " with " + 
//					foragerNode.getTransitFood() + " foodunits; " +
//					" about to decrement transitfood from oldForagerNode at: " + lastNodeCoords + " with " +
//					oldForagerNode.getTransitFood() + " transitfood.");

					foragerNode.incrementTransitFood();
					oldForagerNode.decrementTransitFood();								

//			System.out.println("updateMode MODE4 else (newMODE4Flag==false && not NodeCenter) Forager ID#" + uniqueID + 
//					" did increment transitfood from foragerNode at: " + nodeCoords + " with " + 
//					foragerNode.getTransitFood() + " foodunits; " +
//					" did decrement transitfood from oldForagerNode at: " + lastNodeCoords + " with " +
//					oldForagerNode.getTransitFood() + " transitfood.");
				}
				
/*
				if (!nodeCoords.equals(COLONY_NODE_CENTER_NODECOORDS)) {
					oldForagerNode.incrementNodePher();
				}
*/			
				oldForagerNode.incrementNodePher();
				
				newMODE4Flag = false;

			}
			
			
			
			break;
			
		default:
			System.out.println("invalid foragerMode");
			break;
	
		}
		
	}
	
	public void updateMode() {
		
		Node foragerNode = getNode(nodeCoords);// colonyNode[nodeCoords.getX()][nodeCoords.getY()];

		LinkedList adjVisibleNodes;
		
		NodeCoords nodeCoordsItr;
		
		Node nodeItr;
		
		switch (mode) {
			
		case MODE0:
//			System.out.println("updateMode begin " + mode + " Forager ID#" + uniqueID);

			mode = ForagerMode.MODE4;
			
			newMODE4Flag = true;
			//foodCarryFlag = false;
			break;
			
		case MODE1:

//			System.out.println("updateMode begin " + mode + " Forager ID#" + uniqueID);

			//System.out.println("updateMode MODE1 begin");

			// if forager at colony entrance erase history
			if (nodeCoords.equals(COLONY_NODE_CENTER_NODECOORDS)) {
				nodeCoordsHistory = new LinkedList();
			}

			//if food at node and forager forager not at colony entrance, enter mode #0
			if ((foragerNode.getFood() > 0) && !(nodeCoords.equals(COLONY_NODE_CENTER_NODECOORDS))) {
				mode = ForagerMode.MODE0;	
				
//				System.out.println("updateMode MODE1 changeToMODE0 Forager ID#" + uniqueID + 
//						" foragerNode at: " + nodeCoords + " with " + 
//						foragerNode.getFood() + " foodunits");


				return;
			}

			adjVisibleNodes = findAdjVisibleNodes(nodeCoords);
			// remove most recent node from choices since don't want to follow pheromone trail backwards (unless history empty), 
			if (!nodeCoordsHistory.isEmpty()) {
				NodeCoords lastNodeCoords = (NodeCoords) nodeCoordsHistory.getFirst();
				adjVisibleNodes.remove(lastNodeCoords);
			}

			int howManyAdjVisibleNodes = adjVisibleNodes.size();

			//if forager has no choices for movement, then change to mode5 to return ant to colony entrance
			if (howManyAdjVisibleNodes == 0) {
				mode = ForagerMode.MODE5;
				return;
			}

			// determine if forager adjacent to pheromone trail
			boolean adjPherTrailFlag = false;
			
			// set adjPherTrailFlag = true, if any adjacent visible node has a positive pheromone value
			for (Iterator itr = adjVisibleNodes.iterator(); itr.hasNext(); ) {
				
				nodeCoordsItr = (NodeCoords) itr.getCurrent();
						
				nodeItr = getNode(nodeCoordsItr);

				if (nodeItr.getNodePher() > 0) {
					adjPherTrailFlag = true;
				}
			
				itr.next();
			}
			
			// if forager adjacent to pheromone trail and not at colony entrance, enter mode #2
			if (adjPherTrailFlag && !(nodeCoords.equals(COLONY_NODE_CENTER_NODECOORDS))) {
				mode = ForagerMode.MODE2;	
			}
			
			// if forager adjacent to pheromone trail and forager at colony entrance, enter mode #3
			if (adjPherTrailFlag && nodeCoords.equals(COLONY_NODE_CENTER_NODECOORDS)) {
				mode = ForagerMode.MODE3;	
			}
						
			break;

		// only difference between MODE2 and MODE3 is that in MODE2 history may be reset 
		case MODE2:
		case MODE3:

//			System.out.println("updateMode begin " + mode + " Forager ID#" + uniqueID);

			//System.out.println("updateMode MODE2MODE3 begin");

			//if food at node and forager not at colony entrance, enter mode #0
			if ((foragerNode.getFood() > 0) && !(nodeCoords.equals(COLONY_NODE_CENTER_NODECOORDS))) {
				mode = ForagerMode.MODE0;

//				System.out.println("updateMode MODE2/3 changeToMODE0 Forager ID#" + uniqueID + 
//						" foragerNode at: " + nodeCoords + " with " + 
//						foragerNode.getFood() + " foodunits");

				return;
			}

			//System.out.println("updateMode MODE2MODE3 a");

			//remove most recent node from choices, since don't want to follow pheromone trail backwards

			adjVisibleNodes = findAdjVisibleNodes(nodeCoords);
			// remove most recent node from choices since don't want to follow pheromone trail backwards (unless history empty), 
			// count leftover nodes with pheromone
			if (!nodeCoordsHistory.isEmpty()) {
				NodeCoords lastNodeCoords = (NodeCoords) nodeCoordsHistory.getFirst();
				adjVisibleNodes.remove(lastNodeCoords);
			}

			//System.out.println("updateMode MODE2MODE3 b");

			Iterator itr;
			int adjPherNodes = 0;
			
			//if adjVisibleNodes isn't empty
			// then iterate through adjVisibleNodes and count adjacent nodes with pheromone 
			if (!adjVisibleNodes.isEmpty()) {

				//System.out.println("updateMode MODE2MODE3 c");

				for (itr = adjVisibleNodes.iterator(); itr.hasNext(); ) {

					//System.out.println("updateMode MODE2MODE3 d");

					nodeCoordsItr = (NodeCoords) itr.getCurrent();
						
					nodeItr = getNode(nodeCoordsItr);

					if (nodeItr.getNodePher() > 0) {
						adjPherNodes++;
						//System.out.println("updateMode MODE2MODE3 e");

					} // end if

					itr.next();

				} // end for
							
			} // end if
			
			//System.out.println("updateMode MODE2MODE3 f");

			//if 0 adjacent nodes with nonzero pheromone,
			//reset history if in mode2 and forager next to colony entrance
			//enter mode #5 since at end of pheromone trail, or trail faded
			if (adjPherNodes == 0) {
				
				//System.out.println("updateMode MODE2MODE3 g");

				if (mode == ForagerMode.MODE2) {
				
					//System.out.println("updateMode MODE2MODE3 h");

					// determine if forager adjacent to colony entrance 
					boolean adjColonyEntryFlag = false;

					adjVisibleNodes = findAdjVisibleNodes(nodeCoords);

					//System.out.println("updateMode MODE2MODE3 i");

					// iterate through adjacent visible nodes, and 
					// set adjColonyEntryFlag = true, if any adjacent visible node is the colony entrance
					for (itr = adjVisibleNodes.iterator(); itr.hasNext(); ) {
					
						//System.out.println("updateMode MODE2MODE3 j");

						nodeCoordsItr = (NodeCoords) itr.getCurrent();
							
						nodeItr = getNode(nodeCoordsItr);

						if (nodeCoordsItr.equals(COLONY_NODE_CENTER_NODECOORDS)) {
							adjColonyEntryFlag = true;
						}
					
						itr.next();
					} // end for

					//System.out.println("updateMode MODE2MODE3 k");

					// if forager next to colony entrance
					// (since don’t want forager to double track through history when ant can just reset)
					if (adjColonyEntryFlag == true) {
						// erase history
						//System.out.println("updateMode MODE2MODE3 l");

						nodeCoordsHistory = new LinkedList();
						// replace history with single entry of colony entrance so in mode5 forager returns to colony entrance
						nodeCoordsHistory.add(COLONY_NODE_CENTER_NODECOORDS);
					} // end if
				} // end if
				//System.out.println("updateMode MODE2MODE3 m");

				// change to mode5 must come after test for mode 2 to reset history
				mode = ForagerMode.MODE5;				
			} // end if
			
/*
			if (!nodeCoordsHistory.isEmpty()) {
				nodeCoordsHistory.removeFirst();
			}
*/					
			break;
			
		case MODE4:
			// System.out.println("updateMode begin " + mode + " foodCarryFlag=" + foodCarryFlag + " Forager ID#" + uniqueID);

			//System.out.println("updateMode MODE4 begin");
			
			// When another forager empties food at a node,
			// remaining ants in the same turn will try to get food on the same turn.
			// instead send such ants back to colony entrance in MODE5
/*	
			if ((foragerNode.getFood() == 0) && (newMODE4Flag == true)) {
				mode = ForagerMode.MODE5;
				return;
			}


//				System.out.println("updateMode MODE4 switchMODE5 Forager ID#" + uniqueID +
//						" foragerNode at: " + nodeCoords + " with " + 
//						foragerNode.getFood() + " foodunits");
				return;
			}

/*			
//				System.out.println("updateMode Forager ID#"+ uniqueID + " has nodeCoordsHistory: " + NodeCoordList.toStringNodeList(nodeCoordsHistory));
				// since forager carried food between nodes, increment food of new node and decrement food of old node
//				System.out.println("updateMode Forager ID#" + uniqueID + " about to increment food to foragerNode at: " + nodeCoords + " with " +
//						foragerNode.getFood() + " food.");

				//				System.out.println("updateMode Forager ID#" + uniqueID + " just incremented food to foragerNode at: " + nodeCoords + " with " +
//						foragerNode.getFood() + " food.");

//				System.out.println("updateMode Forager ID#" + uniqueID + " about to decrement food from oldForagerNode at: " + lastNodeCoords + " with " +
//						oldForagerNode.getFood() + " food.");


//				System.out.println("updateMode Forager ID#" + uniqueID + " just decremented food from oldForagerNode at: " + lastNodeCoords + " with " +
//						oldForagerNode.getFood() + " food.");
			if (nodeCoords.equals(COLONY_NODE_CENTER_NODECOORDS)) {
				
//				System.out.println("updateMode MODE4 NodeCenter Forager ID#" + uniqueID + 
//						" about to increment foodUnits from foragerNode at: " + nodeCoords + " with " + 
//						foragerNode.getFood() + " foodunits; " +
//						" about to decrement transitfood from oldForagerNode at: " + lastNodeCoords + " with " +
//						oldForagerNode.getTransitFood() + " transitfood.");

				foragerNode.incrementFood();
				oldForagerNode.decrementTransitFood();

//				System.out.println("updateMode MODE4 NodeCenter Forager ID#" + uniqueID + 
//						" did increment foodUnits from foragerNode at: " + nodeCoords + " with " + 
//						foragerNode.getFood() + " foodunits; " +
//						" did decrement transitfood from oldForagerNode at: " + lastNodeCoords + " with " +
//						oldForagerNode.getTransitFood() + " transitfood.");
			}
			
//			else if ((newMODE4Flag == true) && (oldForagerNode.getFood() > 0)) {
			else if (newMODE4Flag == true) {

//				System.out.println("updateMode MODE4 newMODE4Flag==true and getFood>0 Forager ID#" + uniqueID + 
//						" about to increment transitfood from foragerNode at: " + nodeCoords + " with " + 
//						foragerNode.getTransitFood() + " transitfood; " +
//						" about to decrement foodunits from oldForagerNode at: " + lastNodeCoords + " with " +
//						oldForagerNode.getFood() + " foodunits.");

				foragerNode.incrementTransitFood();
				oldForagerNode.decrementFood();				

//				System.out.println("updateMode MODE4 newMODE4Flag==true and getFood>0 Forager ID#" + uniqueID + 
//						" did increment transitfood from foragerNode at: " + nodeCoords + " with " + 
//						foragerNode.getTransitFood() + " transitfood; " +
//						" did decrement foodunits from oldForagerNode at: " + lastNodeCoords + " with " +
//						oldForagerNode.getFood() + " foodunits.");
			}
			
			
			else {

//				System.out.println("updateMode MODE4 else (newMODE4Flag==false && not NodeCenter) Forager ID#" + uniqueID + 
//						" about to increment transitfood from foragerNode at: " + nodeCoords + " with " + 
//						foragerNode.getTransitFood() + " foodunits; " +
//						" about to decrement transitfood from oldForagerNode at: " + lastNodeCoords + " with " +
//						oldForagerNode.getTransitFood() + " transitfood.");

				foragerNode.incrementTransitFood();
				oldForagerNode.decrementTransitFood();								

//				System.out.println("updateMode MODE4 else (newMODE4Flag==false && not NodeCenter) Forager ID#" + uniqueID + 
//						" did increment transitfood from foragerNode at: " + nodeCoords + " with " + 
//						foragerNode.getTransitFood() + " foodunits; " +
//						" did decrement transitfood from oldForagerNode at: " + lastNodeCoords + " with " +
//						oldForagerNode.getTransitFood() + " transitfood.");

			}
*/

			// if forager at colony entrance enter mode1
			if (nodeCoords.equals(COLONY_NODE_CENTER_NODECOORDS)) {
				mode = ForagerMode.MODE1;
			}

			// if forager not at colony entrance increment pheromone of old node by 10 units
/*
			else {
				oldForagerNode.incrementNodePher();
			}
*/			
			// the first turn that forager is in MODE4, the forager hasn't moved from a node with food
			// foodCarryFlag conditions logic that occurs if the forager has moved from a node with food
			// Such logic happens the second turn and later that a forage is in MODE4
			// foodCarryFlag = true;
			
			
			break;
			
		case MODE5:

			//System.out.println("updateMode MODE5 begin");

			// if forager at colony entrance enter mode1
			if (nodeCoords.equals(COLONY_NODE_CENTER_NODECOORDS)) {
				mode = ForagerMode.MODE1;
			}
			
			break;
			
		default:
			System.out.println("invalid foragerMode");
			break;
		}
	}
	
	public void act() {
//		pheromone();
	}

	// override Ant attack()
	public void attack() {
		
	}

	public void forage() {
		
	}
	
	public void sortPathNodes() {
		
	}

	public void returnNest() {
		
	}
	
	public void pheromone() {
		
	}
}
