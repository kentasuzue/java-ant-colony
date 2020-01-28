package AntColonySim;

public class NodeCoords {

	private int nodeCoordX;
	private int nodeCoordY;
	
	public NodeCoords(int coordX, int coordY){
		nodeCoordX = coordX;
		nodeCoordY = coordY;
	}

	public void setXY(int coordX, int coordY) {
		nodeCoordX = coordX;
		nodeCoordY = coordY;		
	}

	public void setX(int coordX) {
		nodeCoordX = coordX;
	}

	public void setY(int coordY) {
		nodeCoordX = coordY;
	}

	public int getX() {
		return nodeCoordX;
	}

	public int getY() {
		return nodeCoordY;
	}

	public NodeCoords copy() {
		NodeCoords newNodeCoords = new NodeCoords(this.getX(), this.getY());
		return newNodeCoords;
	}
	
	public boolean equals(NodeCoords nodeCoordsParam) {

		boolean equalsFlag = true;
		
		if (this.getX() != nodeCoordsParam.getX())
			equalsFlag = false;

		if (this.getY() != nodeCoordsParam.getY())
			equalsFlag = false;

		return equalsFlag;
	}
	
	public String toString() {
		String s = "";
		s += "(" + this.getX() + "," + this.getY() + ")";
		return s;
	}
	
}
