package AntColonySim;

public class Node {

	private ColonyNodeView nodeView;

	// list of nodes adjacent to this node
	private NodeCoordList adjNodeCoordList;
	
	private boolean nodeVisible;
	
	private AntsAtNode antsAtNode;

	// used in forager logic to determine whether to switch from forage mode to return-to-nest mode
	private int foodUnits;
	
	private int pherUnits;

	// used only for food being carried by forager back to colony entrance
	// included in GUI total of food, but otherwise not used
	// separate from foodUnits, so that carried food doesn't cause decisions by other foragers
	private int transitFoodUnits;

	// constructor
	Node(ColonyNodeView nodeViewParam, NodeCoords nodeCoordsParam) {

		nodeView = nodeViewParam;
		
		adjNodeCoordList = new NodeCoordList(nodeCoordsParam);

//		System.out.println("Adjacent nodes of " + nodeCoordsParam + " are: " + adjNodeCoordList);
		
		// default is Node is hidden
		hideNode();		
		
		antsAtNode = new AntsAtNode();
		
		transitFoodUnits = 0;
	}

	public boolean getNodeVisible() {
		return nodeVisible;
	}

	public NodeCoordList getAdjNodeList() {
		return adjNodeCoordList;
	}
	
	public void addAntToNode(int antUniqueIDParam, AntType antTypeParam) {
		
//		System.out.println("In add Ant to Node, antUniqueIDParam = " + antUniqueIDParam + ", antTypeParam = " + antTypeParam);
		antsAtNode.addAntToLL(antUniqueIDParam, antTypeParam);

//		System.out.println("After add Ant to Node");

	}

	public void removeAntFromNode (int antUniqueIDParam, AntType antTypeParam) {
		antsAtNode.removeAntFromLL(antUniqueIDParam, antTypeParam);
	}

	public void updateAntIcons() {
		for (AntType antType : AntType.values()) {
	
			if (antsAtNode.isAnyAntType(antType)) {
				showAntIcon(antType);
			}
			else {
				hideAntIcon(antType);
			}
	
	//		System.out.println("Show icon!");
	//		showAntIcon(antType);
		
		}
		
	}

	private void showAntIcon(AntType antType) {
		switch (antType) {
			
		case QUEEN:
			showQueenIcon();
			break;
		
		case FORAGER:
			showForagerIcon();
			break;
		
		case SCOUT:
			showScoutIcon();
			break;
		
		case SOLDIER:
			showSoldierIcon();
			break;
		
		case BALA:
			showBalaIcon();
			break;			
		}	
	}

	private void hideAntIcon(AntType antType) {
		switch (antType) {
			
		case QUEEN:
			hideQueenIcon();
			break;
		
		case FORAGER:
			hideForagerIcon();
			break;
		
		case SCOUT:
			hideScoutIcon();
			break;
		
		case SOLDIER:
			hideSoldierIcon();
			break;
		
		case BALA:
			hideBalaIcon();
			break;			
		}	
	}

	public void updateAntCounts() {
		for (AntType antType : AntType.values()) {
	
			int antCount = antsAtNode.countAntType(antType);
	
			setAntCount(antType, antCount);
		}
	}

	private void setAntCount(AntType antType, int antCount) {
		switch (antType) {
			
		case QUEEN:
			if (antCount > 0) {
				setQueen(true);
			}
			else {
				setQueen(false);
			}
			break;
		
		case FORAGER:
			setForagerCount(antCount);
			break;
		
		case SCOUT:
			setScoutCount(antCount);
			break;
		
		case SOLDIER:
			setSoldierCount(antCount);
			break;
		
		case BALA:
			setBalaCount(antCount);
			break;			
		}	
	}

	public void setNodeFood(int foodParam) {
		foodUnits = foodParam;
		setFoodAmount(foodUnits);
	}
	
	public int decrementFood() {
		foodUnits--;
		setFoodAmount(foodUnits + transitFoodUnits);
		return foodUnits;
	}

	public void incrementFood() {
		foodUnits++;
		setFoodAmount(foodUnits + transitFoodUnits);
		return;
	}	
	
	public void setNodePher(int pherParam) {
		pherUnits = pherParam;
		setPheromoneLevel(pherUnits);	
	}

	public int getFood() {
		return foodUnits;
	}

	public int getNodePher() {
		return pherUnits;
	}

	public void incrementNodePher() {
		if (pherUnits < 1000) {
			pherUnits += 10;
		}
		setPheromoneLevel(pherUnits);	
	}

	public void updatePheromone() {
		pherUnits /= 2;
		setPheromoneLevel(pherUnits);
	}

	public void incrementTransitFood() {
		transitFoodUnits++;
		setFoodAmount(foodUnits + transitFoodUnits);
	}

	public void decrementTransitFood() {
		transitFoodUnits--;
		setFoodAmount(foodUnits + transitFoodUnits);
	}

	public int getTransitFood() {
		return transitFoodUnits;
	}
	
	
	
	public int getCountEnemyAnts() {
		
		return antsAtNode.getCountEnemyAnts();
	}
	
	public boolean isAnyEnemyAnts() {
		
		return antsAtNode.isAnyEnemyAnts();
	}
	
	public int getCountFriendAnts() {
		
		return antsAtNode.getCountFriendAnts();
	}	
 
	public boolean isAnyFriendAnts() {
		return antsAtNode.isAnyFriendAnts(); 
	}
	
	public int getRandomFriendAnt() {
		
		return antsAtNode.getRandomFriendAnt();
	}

	public int getRandomEnemyAnt() {
		
		return antsAtNode.getRandomEnemyAnt();
	}

/*
spawnBala()
pherDrop()
*/
	
	public void showNode()
	{
		nodeView.showNode();
		nodeVisible = true;
	}
	
	
	public void hideNode()
	{
		nodeView.hideNode();
		nodeVisible = false;
	}
	
	
	public void setID(String id)
	{
		nodeView.setID(id);
	}
		
	public void setQueen(boolean queenPresent)
	{
		nodeView.setQueen(queenPresent);
	}
	
	public void setForagerCount(int numForagers)
	{
		nodeView.setForagerCount(numForagers);
	}
	
	
	public void setScoutCount(int numScouts)
	{
		nodeView.setScoutCount(numScouts);
	}
	
	public void setSoldierCount(int numSoldiers)
	{
		nodeView.setSoldierCount(numSoldiers);
	}
	
	public void setBalaCount(int numBalas)
	{
		nodeView.setBalaCount(numBalas);
	}
	
	
	public void setFoodAmount(int food)
	{
		nodeView.setFoodAmount(food);
	}
	
	public void setPheromoneLevel(int pheromone)
	{
		nodeView.setPheromoneLevel(pheromone);
	}
	
	public void showQueenIcon()
	{
		nodeView.showQueenIcon();
	}
	
	
	public void hideQueenIcon()
	{
		nodeView.hideQueenIcon();
	}

	
	public void showBalaIcon()
	{
		nodeView.showBalaIcon();
	}
	
	
	public void hideBalaIcon()
	{
		nodeView.hideBalaIcon();
	}
	
	public void showSoldierIcon()
	{
		nodeView.showSoldierIcon();
	}
	
	public void hideSoldierIcon()
	{
		nodeView.hideSoldierIcon();
	}
	
	public void showScoutIcon()
	{
		nodeView.showScoutIcon();
	}
	
	public void hideScoutIcon()
	{
		nodeView.hideScoutIcon();
	}
	
	public void showForagerIcon()
	{
		nodeView.showForagerIcon();
	}
	
	public void hideForagerIcon()
	{
		nodeView.hideForagerIcon();
	}

}
