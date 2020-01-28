package AntColonySim;

public class SimTime {

	public final static int TURNS_IN_DAY = 10;

	public final static int DAYS_IN_YEAR = 365;

	// queen hatches on 1st turn of day, represented by (turns % TURNS_IN_DAY) == HATCH_TURN
	public final static int HATCH_TURN = 1;

	// pheromones halve after last turn of each day, represented by (turns % TURNS_IN_DAY) == PHERMONE_TURN
	public final static int PHEROMONE_TURN = 0;

	public int turns;
	
	SimTime() {
		turns = 0;
	}
	
	// queen hatches, every day, on turn number HATCH_TURN out of TURNS_IN_DAY turns 
	public boolean hatchTurn() {
		return ((turns % TURNS_IN_DAY) == HATCH_TURN);
	}

	public boolean pheromoneTurn() {
		return (turns % TURNS_IN_DAY) == PHEROMONE_TURN;
	}
	
	public void incrementSimTime() {
		turns++;
	}

	public String toString() {

		int years = turns / TURNS_IN_DAY / DAYS_IN_YEAR;
		
		int days = (turns / TURNS_IN_DAY) - (years * DAYS_IN_YEAR);

		int turns = this.turns % TURNS_IN_DAY;
		
		// less than 1 year
		if (years == 0) {

			// just 1 day
			if (days == 1) {
				
				// just 1 turn
				if (turns == 1) {
					return days + " Day " + turns + " Turn";
				}

				// other than 1 turn
				else {
					return days + " Day " + turns + " Turns";
				}		
			}
			
			// other than 1 day
			else {

				// just 1 turn
				if (turns == 1) {
					return days + " Days " + turns + " Turn";
				}

				// other than 1 turn
				else {
					return days + " Days " + turns + " Turns";
				}		
			}
		}

		// just 1 year		
		else if (years == 1) {

			// just 1 day
			if (days == 1) {
				
				// just 1 turn
				if (turns == 1) {
					return years + " Year " + days + " Day " + turns + " Turn";
				}

				// other than 1 turn
				else {
					return years + " Year " + days + " Day " + turns + " Turns";
				}		
			}
			
			// other than 1 day
			else {
				
				// just 1 turn
				if (turns == 1) {
					return years + " Year " + days + " Days " + turns + " Turn";
				}

				// other than 1 turn
				else {
					return years + " Year " + days + " Days " + turns + " Turns";
				}		
			}		
		}
		
		// more than 1 year
		else {
			
			// just 1 day
			if (days == 1) {
				
				// just 1 turn
				if (turns == 1) {
					return years + " Years " + days + " Day " + turns + " Turn";
				}

				// other than 1 turn
				else {
					return years + " Years " + days + " Day " + turns + " Turns";
				}		
			}
			
			// other than 1 day
			else {
				
				// just 1 turn
				if (turns == 1) {
					return years + " Years " + days + " Days " + turns + " Turn";
				}

				// other than 1 turn
				else {
					return years + " Years " + days + " Days " + turns + " Turns";
				}		
			}
		}
	}
}
