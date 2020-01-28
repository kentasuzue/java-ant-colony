package AntColonySim;



public class Ants {

	//percentage chance out of 100 that new bala ant appears in turn
	public static final int CHANCE_BALA = 3;
	
	public static final int UNIQUE_ID_QUEEN = 0;

	private LinkedList antLL;

	private int uniqueIDCounter;
	
	private boolean queenAlive;
	
	private static AntType newAntType;
	
	private static boolean isHatchTurn;
	
	private static LinkedList killedAntsLL;

	Ants() {
		antLL = new LinkedList();
		uniqueIDCounter = 0;
		Ant.setAnts(this);
		isHatchTurn = false;
	}
	
	public void setQueenAliveToDead() {
		queenAlive = false;
	}
	
	public void setNewAntType(AntType antTypeParam) {
		newAntType = antTypeParam;
	}
	
	public void addToKilledAntsLL (int uniqueIDParam) {

		// killedAntsLL should include only a single copy of each killed ant
		if (!killedAntsLL.contains(uniqueIDParam)) {
			killedAntsLL.add(uniqueIDParam);
		}
	}
	public void addQueen() {
		
		queenAlive = true;
		
//		System.out.println("set Queenalive = true");

		Ant antLLElement = new Queen(uniqueIDCounter);

//		System.out.println("made new queen Ant");

		
		uniqueIDCounter++;

//		System.out.println("incremented uniqueIDcounter");

		antLL.add(antLLElement);

//		System.out.println("added new queen Ant to LL");
	}

/*
	public boolean isQueenAlive() {
		return queenAlive;
	}
*/
	public void addForager() {
		
		Ant antLLElement = new Forager(uniqueIDCounter);
		
		uniqueIDCounter++;
		
		antLL.add(antLLElement);
	}

	public void addScout() {
		
		Ant antLLElement = new Scout(uniqueIDCounter);
		
		uniqueIDCounter++;
		
		antLL.add(antLLElement);
	}
	
	public void addSoldier() {
		
		Ant antLLElement = new Soldier(uniqueIDCounter);
		
		uniqueIDCounter++;
		
		antLL.add(antLLElement);
	}
	
	public void addBala() {
		
		Ant antLLElement = new Bala(uniqueIDCounter);
		
		uniqueIDCounter++;
		
		antLL.add(antLLElement);
	}

	// return true if queen alive
	// return false if queen dead
	public boolean singleTurnLL() {

		// queen acts = hatch and eat, if no food then end sim
		// all other ants act
		actAntLL();
		if (!queenAlive)
			return queenAlive;

		
		// all ants move except for queen
		moveAntLL();


		// attack phase. if queen dead, then end sim
		attackAntLL();
		if (!queenAlive)
			return queenAlive;
		

		// decrease lifespan of queen, if lifespan 0 then end sim
		// decrease lifespan of every ant, if lifespan 0 then remove ant; remove ant from linklist of ants?

		lifespanAntLL();
		if (!queenAlive)
			return queenAlive;
		
		// add hatched ant at end of turn, so that hatched ant doesn't do anything  
		if (isHatchTurn == true) {
			addHatchedToAntLL();
		}
		
		if (isNewBala()) {
			addBala();
		}
		
		return queenAlive;
	}

	public void attackAntLL() {
		
		// throw away list of killed ants from prior turn
		killedAntsLL = new LinkedList();
		
		Ant ant;

		// iterate through all ants to attack. queen, forager, and scout have empty attack() methods
		for (ListIterator antsIterator = antLL.listIterator(0); antsIterator.hasNext();) {

			ant = (Ant)antsIterator.getCurrent();
			ant.attack();
			
			antsIterator.next();		
		}

		int killedAntUniqueID;
		
		// process killedAntsLL only if not empty
		if (!killedAntsLL.isEmpty()) {
	
			// iterate through all ants to look for queen ant
			
			for (ListIterator killedAntIterator = killedAntsLL.listIterator(0); killedAntIterator.hasNext();) {

				killedAntUniqueID = (int)killedAntIterator.getCurrent();

				if (killedAntUniqueID == UNIQUE_ID_QUEEN) {
					setQueenAliveToDead();

					ant = (Ant)antLL.getFirst();
					ant.removeAntFromNode();
					antLL.removeFirst();
					return;
				}

				killedAntIterator.next();		
			}

			// iterate through all ants to remove killed ants from antLL and from node
			for (ListIterator killedAntIterator = killedAntsLL.listIterator(0); killedAntIterator.hasNext();) {

				killedAntUniqueID = (int)killedAntIterator.getCurrent();

				boolean killedAntFoundFlag = false;

				// iterate through antLL until ant with killedAntUniqeID is found, then remove ant from antLL and node
				for (ListIterator antsIterator = antLL.listIterator(0); antsIterator.hasNext() && !killedAntFoundFlag;) {

					ant = (Ant)antsIterator.getCurrent();

					
					if (ant.getUniqueID() == killedAntUniqueID) {
						//when ant found in LL
						killedAntFoundFlag = true;

						ant.removeAntFromNode();
						antsIterator.remove();
					}			

					// if ant removed, can't issue a call to next(), else iterator will skip over an item 
					// so only issue a call to next() only if killedAntFoundFlag false in which case no ant was removed from  antLL
					if (!killedAntFoundFlag) {
						antsIterator.next();
					}
				}
				killedAntIterator.next();		
			}

		}



/*
		ListIterator antsIterator = antLL.listIterator(0);
		
		Ant ant;
		ant = (Ant)antsIterator.getCurrent();

		// queen acts, since queen is first in AntLL
		ant.act();
		
		if (! queenAlive ) {
			ant.removeAntFromNode();
			antsIterator.remove();

			return;
		}
		
		// if antLL had only a single ant
		if (!antsIterator.hasNext()) {
			return;			
		}
		
		// advance iterator to ant after queen
		antsIterator.next();
		
		// rest of ants after queen act
		while (antsIterator.hasNext()) {

			ant = (Ant)antsIterator.getCurrent();

			antsIterator.next();		
		}
		return;
*/
	}

	public boolean isNewBala() {
		
		return (Colony.getRandom(1, 100) <= CHANCE_BALA);
	}
	
	
	public static void setIsHatchTurn(boolean isHatchTurnParam) {
		isHatchTurn = isHatchTurnParam;
	}
	
	public void addHatchedToAntLL() {
		
		isHatchTurn = false;
		
		switch (newAntType) {
		
		case FORAGER:
			addForager();
			break;

		case SCOUT:
			addScout();
			break;
			
		case SOLDIER:
			addSoldier();
			break;
		}
	}

	public void moveAntLL() {
		
		//Colony.setAntsStart() adds new ants starting with queen. So any queen will be the 1st ant in the antLL
		// new iterator points to queen
		
		// Iterator didn't work with remove until changed to ListIterator
		
		Ant ant;
		
		for (ListIterator antsIterator = antLL.listIterator(0); antsIterator.hasNext();) {

			ant = (Ant)antsIterator.getCurrent();
			ant.move();
			
			antsIterator.next();		

		}

/*		
		ListIterator antsIterator = antLL.listIterator(0);
		
		Ant ant;
		ant = (Ant)antsIterator.getCurrent();

		// queen moves, since queen is first in AntLL
		ant.move();
		
		// if antLL had only a single ant
		if (!antsIterator.hasNext()) {
			return;			
		}
		
		// advance iterator to ant after queen
		antsIterator.next();
		
		// rest of ants after queen act
		while (antsIterator.hasNext()) {

			ant = (Ant)antsIterator.getCurrent();

			ant.move();
			
			antsIterator.next();		
		}

		return;
*/
	}
	

	public void actAntLL() {
		
		//Colony.setAntsStart() adds new ants starting with queen. So any queen will be the 1st ant in the antLL
		// new iterator points to queen
		
		// Iterator didn't work with remove until changed to ListIterator
		
		Ant ant;

// previously, the design iterated through all ants. but at the conclusion of the project, 
//only the Queen did anything during act
// so rather than iterate through all ants, only the queen performs act
	
		ListIterator antsIterator = antLL.listIterator(0); 
		ant = (Ant)antsIterator.getCurrent();
		ant.act();
		
		
/*
		for (ListIterator antsIterator = antLL.listIterator(0); antsIterator.hasNext();) {

			ant = (Ant)antsIterator.getCurrent();
			ant.act();
			
			antsIterator.next();		

		}
*/
		
/*
		ListIterator antsIterator = antLL.listIterator(0);
		
		Ant ant;
		ant = (Ant)antsIterator.getCurrent();

		// queen acts, since queen is first in AntLL
		ant.act();
		
		if (! queenAlive ) {
			ant.removeAntFromNode();
			antsIterator.remove();

			return;
		}
		
		// if antLL had only a single ant
		if (!antsIterator.hasNext()) {
			return;			
		}
		
		// advance iterator to ant after queen
		antsIterator.next();
		
		// rest of ants after queen act
		while (antsIterator.hasNext()) {

			ant = (Ant)antsIterator.getCurrent();

			antsIterator.next();		
		}

		return;
*/

			//and ant act
/*
			System.out.print("Ant with uniqueID " + ant.getUniqueID() + " is a ");

			AntType antType = ant.getAntType();
			
			switch (antType) {
			
			case QUEEN:
				System.out.print(" queen!");
				break;

			case FORAGER:
				System.out.print(" forager");
				break;

			case SCOUT:
				System.out.print(" scout");
				break;
				
			case SOLDIER:
				System.out.print(" soldier");
				break;
	
			case BALA:
				System.out.print(" bala");
				break;
			}

			System.out.println();
*/
			
/*
			System.out.println(" with " + lifespanLeft + " turns left!");
			
			if (lifespanLeft == 0) {
				
				//int antLLindex = .indexOf(antUniqueIDParam);

				ant.removeAntFromNode();
				antsIterator.remove();
			}			

			// if ant removed, can't issue a call to next(), else iterator will skip over an item 
			// so only issue a call to next() only if (lifespanLeft > 0) in which case no item was removed
			if (lifespanLeft > 0) {
				antsIterator.next();
			}
*/
		
	}
	
	// leave queenAlive unchanged and return if queen alive
	// change queenAlive to false and return if queen dead

	public void lifespanAntLL() {

		//Colony.setAntsStart() adds new ants starting with queen. So any queen will be the 1st ant in the antLL
		// new iterator points to queen
		
		// Iterator didn't work with remove, until changed to ListIterator
		
		ListIterator antsIterator = antLL.listIterator(0);
		
		Ant ant;
		int lifespanLeft;
		
		ant = (Ant)antsIterator.getCurrent();
		
		lifespanLeft = ant.decrementLifespan();

/*
		if (ant instanceof Queen) {
			System.out.println("Ant with uniqueID " + ant.getUniqueID() + " is a queen with " + lifespanLeft + " turns left!");
		}
*/	
		if (lifespanLeft == 0) {
			ant.removeAntFromNode();
			antsIterator.remove();
		}
		
		if ((lifespanLeft == 0) && (ant instanceof Queen)) {
			setQueenAliveToDead();
			return;
		}
		
		// antLL had only a single ant
		if (!antsIterator.hasNext()) {
			return;			
		}
		
		antsIterator.next();
			
		while (antsIterator.hasNext()) {

			ant = (Ant)antsIterator.getCurrent();

			lifespanLeft = ant.decrementLifespan();
	/*

			System.out.print("Ant with uniqueID " + ant.getUniqueID() + " is a ");

			AntType antType = ant.getAntType();
			
			switch (antType) {
			
			case QUEEN:
				System.out.print(" queen!");
				break;

			case FORAGER:
				System.out.print(" forager");
				break;

			case SCOUT:
				System.out.print(" scout");
				break;
				
			case SOLDIER:
				System.out.print(" soldier");
				break;
	
			case BALA:
				System.out.print(" bala");
				break;
			}

			System.out.println(" with " + lifespanLeft + " turns left!");
	*/		
			if (lifespanLeft == 0) {
				
				//int antLLindex = .indexOf(antUniqueIDParam);

				ant.removeAntFromNode();
				antsIterator.remove();
			}			

			// if ant removed, can't issue a call to next(), else iterator will skip over an item 
			// so only issue a call to next() only if (lifespanLeft > 0) in which case no item was removed
			if (lifespanLeft > 0) {
				antsIterator.next();
			}
		}
//		LinkedListIterator antsIterator = antLL.listIterator(1);
		
		return;
	}
	
}
