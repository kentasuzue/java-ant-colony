package AntColonySim;

public class Queen extends Ant {

	//percentage chance out of 100 that hatched ant is forager
	public static final int HATCH_FORAGER = 50;

	//percentage chance out of 100 that hatched ant is scout
	public static final int HATCH_SCOUT = 25;
	
	//percentage chance out of 100 that hatched ant is soldier
	public static final int HATCH_SOLDIER = 25;
	
	public static final int RANDOM_MIN = 1;
	public static final int RANDOM_MAX = 100;

	
	public static final int QUEEN_LIFESPAN_YEARS = 20;
	// override Ant move()
	
//	private boolean queenAlive;

	Queen(int uniqueIDCounterParam) {

		super(uniqueIDCounterParam, COLONY_NODE_CENTER_NODECOORDS);

//		System.out.println("after super() in  queen constructor");
		

		lifespanTurnsLeft = QUEEN_LIFESPAN_YEARS * DAYS_IN_YEAR * TURNS_IN_DAY;

//		System.out.println("after lifeSpanTurnsleft in queen constructor");

		addAntToNode(uniqueIDCounterParam, AntType.QUEEN, COLONY_NODE_CENTER_NODECOORDS);

//		System.out.println("after addAntToNode in queen constructor");
		
		//queenAlive = true;

	}
	
	// override move() so queen doesn't move
	public void move() {
		
	}
	
	public void act() {
		
		if (Simulation.getSimTime().hatchTurn() == true) {
			// System.out.println("Turn# " +  Simulation.getSimTime() + "; remainder: " + Simulation.getSimTime().hatchTurn());
			hatch();
		}
		eat();		
	}

	// override Ant attack()
	public void attack() {
		
	}

	public void hatch() {
		
		// let Ants singleTurnLL() know that present turn is a hatching turn 
		Ants.setIsHatchTurn(true);
		
		int hatchRandom = Colony.getRandom(RANDOM_MIN, RANDOM_MAX);
		if (hatchRandom <= HATCH_FORAGER) {
			ants.setNewAntType(AntType.FORAGER);
		}
		else if (hatchRandom <= HATCH_FORAGER + HATCH_SCOUT) {
			ants.setNewAntType(AntType.SCOUT);
		}
		else {
			ants.setNewAntType(AntType.SOLDIER);	
		}
		
	}

	public void eat() {
		Node nodeOfAnt = this.getNode();
		int foodAtNode = nodeOfAnt.decrementFood();
		if (foodAtNode < 0) {
//			queenAlive = false;
			ants.setQueenAliveToDead();
			// keep negative food amount from showing
			nodeOfAnt.setFoodAmount(0);
		}
	}
/*	
	public boolean getQueenAlive() {
		return queenAlive;
	}
*/
}
