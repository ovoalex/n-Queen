package nQueen;

import java.util.Scanner;

public class Main {

	public static void main(String[] args) {		
		qSolver queen = new qSolver();
		Scanner sc = new Scanner(System.in);
		int n; int boardsize; int mode; int debug; 
		boolean satisfied = false;
		int reRun = 0;
		int def = 0;
		
		//Prompt to choose which method user want to use.
		while(!satisfied){
			n = 500;  //number of trails
			boardsize = 8; //board size
			debug = 0;
			promptMethods();
			//user choice method
			mode = Integer.parseInt(sc.nextLine());
			showCompleteBoard();
			//debug in the method
			debug = Integer.parseInt(sc.nextLine());
			userSetting();
			def = Integer.parseInt(sc.nextLine());
			if(def == 1){
				promptTrials();
				n = Integer.parseInt(sc.nextLine());
				promptBoardSize();
				boardsize = Integer.parseInt(sc.nextLine());
			}
						
			switch(mode){
			case 1: // Steepest hill climbing Method
				hillClimbingMethod(queen, n, boardsize, debug);
				break;
			case 2: //CSP Method
				CSPMethod(sc, queen, n, boardsize, debug, def);
				break;
			case 3: //Genetic Method
				if(def == 0)
				n = 5;//number of trails for Genetic method
				geneticMethod(sc, queen, n, boardsize, debug, def);
				break;
			default:
				satisfied = false;
				break;
			}
			rerunProgram();
			reRun = Integer.parseInt(sc.nextLine());
			if(reRun == 0)
				satisfied = true;
			else if (reRun == 1)
				satisfied = false;
		}
	}

	//ask user to choose either default input or specific input
	private static void userSetting() {
		System.out.println("Would you like to input your own number of trails or use a default (500) trials? \n"
				+ "  [0] Default: \n"
				+ "  [1] YES: ");	
	}

	//re-run the program
	private static void rerunProgram() {
		System.out.println("Would you like to re-run the program? \n"
				+ "  [0] NO: \n"
				+ "  [1] YES: ");
	}

	//Genetic algorithm method
	private static void geneticMethod(Scanner sc, qSolver queen, int n, int boardsize, int debug, int def) {
		int popSize = 0;
		double mutafactor = 0;
		if(debug == 0)
			System.out.println("Numbers refer to row positions of queens from left to right.");
		if(boardsize >= 10)
			while(boardsize >= 10){
				System.out.println("Please enter a boardsize: ");
				boardsize = Integer.parseInt(sc.nextLine());
			}
		if(def == 1){
			promptpopSize();
			popSize = Integer.parseInt(sc.nextLine());
			promptMutafactor();
			mutafactor = Double.parseDouble(sc.nextLine());
		}
		if (popSize == 0)
			popSize = 50;
		if(mutafactor == 0.0)
			mutafactor = 0.125;
		queen.nTrials(n, boardsize, 3, popSize, mutafactor, debug);
		System.out.print("-----Solved using Genetic Method-----\n");
		System.out.println(queen.getReport());
		printTime(queen);
	}

	//calculate average time and print it
	private static void printTime(qSolver queen) {
		System.out.println("Average time: "+queen.getTime() + "ms\n");		
	}

	//prompt to get mutation for Genetic method
	private static void promptMutafactor() {
		System.out.print("Percentage of the chance of mutation?: ");
	}
	//prompt to get population size for Genetic Method
	private static void promptpopSize() {
		System.out.print("Size of population? (0 for default): ");
	}

	//Solving CSP Method
	private static void CSPMethod(Scanner sc, qSolver queen, int n, int boardsize, int debug, int def) {
		int maxsteps = 0;
		if(def == 1){
			promptmaxsteps();
			maxsteps = Integer.parseInt(sc.nextLine());
		}
		if(maxsteps == 0)
			maxsteps = 500;
			queen.nTrials(n, boardsize, 2, maxsteps, 0, debug);
			System.out.print("-----Solved using CSP Method-----\n");
			System.out.println(queen.getReport());
			printTime(queen);
	}

	private static void promptmaxsteps() {
		System.out.print("How many max steps?: ");
	}

	private static void hillClimbingMethod(qSolver queen, int n, int boardsize, int debug) {
		queen.nTrials(n, boardsize, 1, 0, 0, debug);
		System.out.print("-----Solved using Steepest-ascent Hill Climbing Method-----\n");
		System.out.println(queen.getReport());
		printTime(queen);
	}
	
	private static void showCompleteBoard() {
		System.out.println("Show the completed boards? \n"
				+ "  [0] NO\n" 
				+ "  [1] YES");
	}
	//prompt to get user input chess board size
	private static void promptBoardSize() {
		System.out.println("Size of the chess board?: ");
	}
	//prompt to get user input for how many Trials 
	private static void promptTrials() {
		System.out.println("How many trails would you like to solve?: ");
	}

	private static void promptMethods() {
		System.out.println("Choose one type of searching method: \n"
				+ "  [1] Steepest Hill Climbing Method\n"
				+ "  [2] Minimum Conflicts (CSP Method)\n"
				+ "  [3] Genetic Algorithm Method");
	}

}
