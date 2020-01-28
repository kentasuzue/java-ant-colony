package AntColonySim;

public class AntsAtNode {

//	public enum AntType {QUEEN, FORAGER, SCOUT, SOLDIER, BALA};

	public static final int ANT_TYPE_TOTAL = AntType.values().length;
	
	private LinkedList[] AntUniqueIDLL;
	
	public AntsAtNode() {
		
		AntUniqueIDLL = new LinkedList[ANT_TYPE_TOTAL];
		
		for (int i = 0; i < ANT_TYPE_TOTAL; i++) {
			AntUniqueIDLL[i] = new LinkedList();
		}
	
	}
	
	public void addAntToLL (int antUniqueIDParam, AntType antTypeParam) {

//		System.out.println("In add ant to LL, antUNiqueIDParam = " + antUniqueIDParam + ", antTypeParam = " + antTypeParam);

/*
		for (int i = 0; i < ANT_TYPE_TOTAL; i++) {
			System.out.println("size of AntUNiqueIDLL[" + i + "] is: " + AntUniqueIDLL[i].size() );
		}
*/	
//		Integer antInteger = new Integer(antUniqueIDParam);
		
		AntUniqueIDLL[antTypeParam.ordinal()].add(antUniqueIDParam);

//		System.out.println("After add ant to LL");

	}

	public void removeAntFromLL (int antUniqueIDParam, AntType antTypeParam) {
		int antLLindex = AntUniqueIDLL[antTypeParam.ordinal()].indexOf(antUniqueIDParam);
		AntUniqueIDLL[antTypeParam.ordinal()].remove(antLLindex);
	}

	public boolean isAnyAntType (AntType antTypeParam) {
		return !(AntUniqueIDLL[antTypeParam.ordinal()].isEmpty());
	}

	public int countAntType (AntType antTypeParam) {
		return AntUniqueIDLL[antTypeParam.ordinal()].size();
	}

	public int getCountEnemyAnts() {
		
		return this.countAntType(AntType.BALA);
		
	}
	
	public boolean isAnyEnemyAnts() {
		return (this.getCountEnemyAnts() > 0);
	}
	
	public int getCountFriendAnts() {
		
		int countFriendAnts = 0;
		
		countFriendAnts += this.countAntType(AntType.QUEEN);
		countFriendAnts += this.countAntType(AntType.FORAGER);
		countFriendAnts += this.countAntType(AntType.SCOUT);
		countFriendAnts += this.countAntType(AntType.SOLDIER);

		return countFriendAnts;
	}	
 
	public boolean isAnyFriendAnts() {
		return (this.getCountFriendAnts() > 0); 
	}
	
	public int getRandomFriendAnt() {
		
		int uniqueIDDeadAnt;
		
		int countQueen = this.countAntType(AntType.QUEEN);
		int countForager = this.countAntType(AntType.FORAGER);
		int countScout = this.countAntType(AntType.SCOUT);
		int countSoldier = this.countAntType(AntType.SOLDIER);
		
		int countFriendAnts = countQueen + countForager + countScout + countSoldier;
		
		int randomAntChoice = Colony.getRandom(1, countFriendAnts);
		
		int randomIndexInAntType;
		
		if (randomAntChoice <= countQueen) {
			randomIndexInAntType = randomAntChoice - 1;
			uniqueIDDeadAnt = (int) AntUniqueIDLL[AntType.QUEEN.ordinal()].get(randomIndexInAntType);
			// ID of queen to be killed
		}
		
		else if (randomAntChoice <= countQueen + countForager) {
			randomIndexInAntType = randomAntChoice - (countQueen) - 1;
			uniqueIDDeadAnt = (int) AntUniqueIDLL[AntType.FORAGER.ordinal()].get(randomIndexInAntType);
			// ID of forager to be killed
		}
		
		else if (randomAntChoice <= countQueen + countForager + countScout) {
			randomIndexInAntType = randomAntChoice - (countQueen + countForager) - 1;
			uniqueIDDeadAnt = (int) AntUniqueIDLL[AntType.SCOUT.ordinal()].get(randomIndexInAntType);
			// ID of scout to be killed
		}
		
		else {
			randomIndexInAntType = randomAntChoice - (countQueen + countForager + countScout) - 1;
			uniqueIDDeadAnt = (int) AntUniqueIDLL[AntType.SOLDIER.ordinal()].get(randomIndexInAntType);
			// ID of soldier to be killed
		}
		
		return uniqueIDDeadAnt;
	}

	public int getRandomEnemyAnt() {
		
		int uniqueIDDeadAnt;
		
		int countBala = this.countAntType(AntType.BALA);
		
		int randomAntChoice = Colony.getRandom(1, countBala);
		
		int randomIndexInAntType;
		
		randomIndexInAntType = randomAntChoice - 1;
		
		uniqueIDDeadAnt = (int) AntUniqueIDLL[AntType.BALA.ordinal()].get(randomIndexInAntType);
			// ID of bala to be killed
		
		return uniqueIDDeadAnt;
	}

}
