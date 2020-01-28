/*
Kenta Suzue
CSC385 Data Structures & Algorithms 2018 Fall 183CSC38511880
Professor Roger West
Semester Project
*/

package AntColonySim;

public class SimDriver {

	public static AntSimGUI gui;

	public static Simulation sim;
	
	public static void main(String[] args) {
		gui = new AntSimGUI();
		sim = new Simulation(gui);
	}
}
