package AntColonySim;

public class NodeCoordList {

	public static final int COLONY_NODE_SIZE_X = 27;
	public static final int COLONY_NODE_SIZE_Y = 27;

	public static final int COLONY_NODE_MIN_INDEX_X = 0;
	public static final int COLONY_NODE_MAX_INDEX_X = COLONY_NODE_SIZE_X - 1;

	public static final int COLONY_NODE_MIN_INDEX_Y = 0;
	public static final int COLONY_NODE_MAX_INDEX_Y = COLONY_NODE_SIZE_Y - 1;

	LinkedList nodeCoordLinkedList;
	
	static LinkedList borderNodeLinkedList;
	
	NodeCoordList(NodeCoords nodeCoordsParam) {
		nodeCoordLinkedList = findAdjNodes(nodeCoordsParam);
	}
	
	public LinkedList findAdjNodes(NodeCoords nodeCoordsParam) {

		nodeCoordLinkedList = new LinkedList();
		
		// i is x coordinate
		for (int i = nodeCoordsParam.getX() - 1; i <= nodeCoordsParam.getX() + 1; i++) {

			// only add adjacentNode to NodeCoordLinkedList
			// if adjacentNode is within bounds of the grid's x-coordinates
			if ((i >= COLONY_NODE_MIN_INDEX_X ) &&  (i <= COLONY_NODE_MAX_INDEX_X)) {
				
				// j is y coordinate
				for (int j = nodeCoordsParam.getY() - 1; j <= nodeCoordsParam.getY() + 1; j++) {

					// only add adjacentNode to NodeCoordLinkedList
					// if adjacentNode is within bounds of the grid's y-coordinates
					if ((j >= COLONY_NODE_MIN_INDEX_Y) &&  (j <= COLONY_NODE_MAX_INDEX_Y)) {

						NodeCoords adjacentNode = new NodeCoords(i,j);

						// only add adjacentNode to NodeCoordLinkedList 
						// if adjacentNode is different from nodeCordsParam
						// since a node is not adjacent to itself
						if (nodeCoordsParam.equals(adjacentNode) == false) {
							nodeCoordLinkedList.add(adjacentNode);
						}					
					} // end if condition of j
				} // end for loop over j
			} // end if condition of i
		} // end for loop over i
		
		return nodeCoordLinkedList;
	}
	
	public LinkedList getAdjLinkedList() {
		return nodeCoordLinkedList;
	}
	
	public String toString() {

		return toStringNodeList(nodeCoordLinkedList);
	}
	
	public static String toStringNodeList(LinkedList nodeListParam) {

		String s = "";
		
		if (nodeListParam.size() == 0) {
			return s;
		}
		
		for (Iterator itr = nodeListParam.iterator(); itr.hasNext(); ) {
			
			s += itr.getCurrent() + " ";
			
			itr.next();
		}

		return s;

	}

	public static String toStringBorderNodeList() {

		return toStringNodeList(borderNodeLinkedList);
	}
	
	public static void setBorderNodeList() {

	//	System.out.println("setBorderNodeList");

		borderNodeLinkedList = new LinkedList();
		int i,j;

		// add left edge nodes
		i = COLONY_NODE_MIN_INDEX_X;

		for (j = COLONY_NODE_MIN_INDEX_Y; j <= COLONY_NODE_MAX_INDEX_Y; j++) {
			NodeCoords borderNode = new NodeCoords(i,j);
			borderNodeLinkedList.add(borderNode);
		}

		// add right edge nodes
		i = COLONY_NODE_MAX_INDEX_X;
		
		for (j = COLONY_NODE_MIN_INDEX_Y; j <= COLONY_NODE_MAX_INDEX_Y; j++) {
			NodeCoords borderNode = new NodeCoords(i,j);
			borderNodeLinkedList.add(borderNode);
		}

		// add top edge nodes (except for top left and top right corners added already)
		j = COLONY_NODE_MIN_INDEX_Y;

		for (i = COLONY_NODE_MIN_INDEX_X + 1; i <= COLONY_NODE_MAX_INDEX_X - 1; i++) {
			NodeCoords borderNode = new NodeCoords(i,j);
			borderNodeLinkedList.add(borderNode);
		}

		// add bottom edge nodes (except for bottom left and bottom right corners added already)
		j = COLONY_NODE_MAX_INDEX_Y;

		for (i = COLONY_NODE_MIN_INDEX_X + 1; i <= COLONY_NODE_MAX_INDEX_X - 1; i++) {
			NodeCoords borderNode = new NodeCoords(i,j);
			borderNodeLinkedList.add(borderNode);
		}

	}

}
