package nQueen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Random;

//get the coordinate for the board 
public class qSolver {	
	class Coords{
		int x; int y;
		public Coords(int x, int y){
			this.x = x;
			this.y = y;
		}
	}
	
	private chessBoard board;
	private List<Coords> queens;
	private Map<boardTile, ArrayList<boardTile>> map;
	private PriorityQueue<Node> boardQ;
	private double trials;
	private double success;
	private long totalTime;
	private long avgTime;
	private boolean debug;
	
	//make hashmap for board tiles
	public qSolver(){
		trials = 0;
		success = 0;
		map = new HashMap<boardTile, ArrayList<boardTile>>();
		queens = new ArrayList<Coords>();
		debug = false;
	}
	
	public qSolver(int n){
		trials = 0;
		success = 0;
		debug = false;
		makeBoard(n);	
	}
	
	public void makeBoard(int n){
		this.board = new chessBoard(n);
		map = new HashMap<boardTile, ArrayList<boardTile>>(n);
		queens = new ArrayList<Coords>(n);
	}
	
	public chessBoard getBoard(){
		return this.board;
	}
	
	public String getRep(){
		return board.toString();
	}
	
	//generate random x and y board
	public void genRandConfig(){
		Random rand = new Random();
		int randx;
		int randy;
		for(int i = 0; i < board.getSize(); i++){
			randx = Math.abs(rand.nextInt()%board.getSize());
			randy = Math.abs(rand.nextInt()%board.getSize());
			if(!checkQ(randx,randy))
				placeQ(randx, randy);
			else
				i--;
		}
	}
	
	public void genSemiRandConfig(){
		Random rand = new Random();
		int randy;
		for(int i = 0; i < board.getSize(); i++){
			randy = Math.abs(rand.nextInt()%board.getSize());
			if(!checkQ(i,randy))
				placeQ(i, randy);
			else
				i--;
		}
	}
	
	public void genSemiRandConfig(int size){
		makeBoard(size);
		Random rand = new Random();
		int randy;
		for(int i = 0; i < board.getSize(); i++){
			randy = Math.abs(rand.nextInt()%board.getSize());
			if(!checkQ(i,randy))
				placeQ(i, randy);
			else
				i--;
		}
	}
	
	//the number of n trails time will be solved here 
	//for Hill Climbing, CSP, Genetic Method
	public void nTrials(int n, int size, int mode, int additional1, 
		double additional2, int debug){
		long startTime;
		long endTime;
		long time;
		totalTime = 0;
		avgTime = 0;
		trials = 0;
		success = 0;
		if(debug == 1)
			this.debug = true;
		else if (debug == 0)
			this.debug = false;
		if(board == null){
			makeBoard(size);
			genSemiRandConfig();
		}
		
		//gather time to do each task when its called		
		for(int i = 0; i < n; i++){
			switch(mode){
			case 1:
				startTime = System.currentTimeMillis();
				steepestHillClimbing();
				endTime = System.currentTimeMillis();
				time = endTime - startTime;
				totalTime += time;
				break;
			case 2:
				startTime = System.currentTimeMillis();
				minConflicts(additional1);
				endTime = System.currentTimeMillis();
				time = endTime - startTime;
				totalTime += time;
				break;
			case 3:
				startTime = System.currentTimeMillis();
				geneticAlgorithm(additional1,size,additional2);
				endTime = System.currentTimeMillis();
				time = endTime - startTime;
				totalTime += time;
				break;
			default:
				continue;
			}
			avgTime = totalTime/n;
			board = new chessBoard(size);
			queens.clear();
			map.clear();
			if(boardQ != null)
				boardQ.clear();
			genSemiRandConfig();
		}
	}
	
	//Calculate the success rate
	public String getReport(){
		String toReturn;
		toReturn = "Number of Trials: "+(int)trials+ "\nNumber of Solved Problems: " + (int)success +"\nSuccess Rate: "+(double)(success/trials)*100+"%";
		return toReturn;
	}
	
	//replace queen on board tile
	private void replaceQ(boardTile variable, boardTile value) {
		removeQ(variable);
		placeQ(value);
	}

	//check Queens on the board
	public boolean checkQ(int x, int y){
		return board.getTile(x, y).hasQueen();
	}

	//places Queens on the board
	public void placeQ(int x, int y){
		if(board.getTile(x, y).hasQueen())
			return;
		else{
			board.getTile(x, y).placeQ();
			queens.add(x, new Coords(x,y));
		}
	}
	
	private void placeQ(boardTile value) {
		placeQ(value.getX(), value.getY());
	}
	
	//get the current placed Queen on the board
	public void placeQ(Node current, int x, int y){
		if(current.board.getTile(x, y).hasQueen())
			return;
		else{
			current.board.getTile(x, y).placeQ();
			current.queens.add(x, new Coords(x,y));
		}
	}
	
	//remove queen from the board
	public void removeQ(int x, int y){
		if(board.getTile(x, y).hasQueen()){
			board.getTile(x, y).removeQ();
			queens.remove(x);
			return;
		}
		else
			return;
	}
	//remove queen from the x tile
	private void removeQ(int x) {
		@SuppressWarnings("unused")
		Coords coords;
		for(int i = 0; i < board.getSize(); i++){
			if(board.getTile(x, i).hasQueen()){
				coords = new Coords(x,i);
				board.getTile(x, i).removeQ();
				queens.remove(x);
				return;
			}
		}
	}
	
	private void removeQ(boardTile variable) {
		removeQ(variable.getX());
	}

	//check to see if Queen can be placed by using the attacker method 
	//clear map for the next board
	public int getAllAttackers(){
		int allAttackers = 0;
		for(int i = 0; i < board.getSize(); i++){
			for(int j = 0; j < board.getSize(); j++){
				if(board.getTile(i,j).hasQueen())
					allAttackers += getAttackers(i,j);
			}
		}
		map.clear();
		return allAttackers;
	}
	
	private int getAttackers(int x, int y){
		int toReturn = 0;
		toReturn += getColAtkers(x, y);
		toReturn += getRowAtkers(x, y);
		toReturn += getDiagAtkers(x, y);
		board.getTile(x,y).setValue(toReturn);
		return toReturn;
	}
	//placing an attacker on the board 
	//checking if Queen can be placed 
	//down right 
	//down left
	//upright
	//upleft
	private int getDiagAtkers(int x, int y) {
		int i = x; int j = y;
		int attackers = 0;
		while(i < board.getSize() && j < board.getSize()){ 
			if(board.getTile(i, j).hasQueen() && (i != x && j != y) && !counted(board.getTile(x, y), board.getTile(i, j)))
				attackers = incAttackers(attackers, board.getTile(x, y), board.getTile(i, j));
			i++;
			j++;
		}
		i = x; j = y;
		while(i < board.getSize() && j >= 0){ 
			if(board.getTile(i, j).hasQueen() && (i != x && j != y) && !counted(board.getTile(x, y), board.getTile(i, j)))
				attackers = incAttackers(attackers, board.getTile(x, y), board.getTile(i, j));
			i++;
			j--;
		}
		i = x; j = y;
		while(i >= 0 && j < board.getSize()){ 
		if(board.getTile(i, j).hasQueen() && (i != x && j != y) && !counted(board.getTile(x, y), board.getTile(i, j)))
			attackers = incAttackers(attackers, board.getTile(x, y), board.getTile(i, j));
			i--;
			j++;
		}
		i = x; j = y;
		while(i >= 0 && j >= 0){ 
			if(board.getTile(i, j).hasQueen() && (i != x && j != y) && !counted(board.getTile(x, y), board.getTile(i, j)))
				attackers = incAttackers(attackers, board.getTile(x, y), board.getTile(i, j));
			i--;
			j--;
		}
		return attackers;
	}
	//finding row attackers
	private int getRowAtkers(int x, int y) {
		int attackers = 0;
		for(int i = 0; i < board.getSize(); i++){
			if(board.getTile(x, i).hasQueen() && i != y && !counted(board.getTile(x, y), board.getTile(x,i)))
				attackers = incAttackers(attackers, board.getTile(x, y), board.getTile(x,i));
		}
		return attackers;
	}
	//finding column attackers
	private int getColAtkers(int x, int y) {
		int attackers = 0;
		for(int i = 0; i < board.getSize(); i++){
			if(board.getTile(i, y).hasQueen() && i != x && !counted(board.getTile(x, y), board.getTile(i,y)))
				attackers = incAttackers(attackers, board.getTile(x, y), board.getTile(i,y));
		}
		return attackers;
	}
	//increased attackers on the board tile
	private int incAttackers(int count, boardTile one, boardTile two){
		count++;
		if(map.get(one) != null && map.get(two) != null){
			if(!map.get(one).contains(two) && !map.get(two).contains(one)){
				map.get(one).add(two);
				map.get(two).add(one);
			}
		}else if(map.get(one) == null && map.get(two) != null){
			map.put(one, new ArrayList<boardTile>());
			map.get(one).add(two);
			map.get(two).add(one);
		}else if(map.get(two) == null && map.get(one) != null){
			map.put(two, new ArrayList<boardTile>());
			map.get(one).add(two);
			map.get(two).add(one);
		}else{
			map.put(one, new ArrayList<boardTile>());
			map.put(two, new ArrayList<boardTile>());
			map.get(one).add(two);
			map.get(two).add(one);
		}
		return count;
	}
	//keep track of count
	private boolean counted(boardTile one, boardTile two){
		if(map.isEmpty())
			return false;
		else if(map.containsKey(one))
			if(map.get(one).contains(two))
				return true;
			else
				return false;
		else
			return false;
	}
	//finding node placement for SHC
	//keep track of number of trails and solved successful problems
	public Node steepestHillClimbing(){
		Node current; // current node
		Node neighbor; // neighbor node
		current = new Node(board, getAllAttackers(), (ArrayList<Coords>) queens);
		boardQ = new PriorityQueue<Node>();
		//get current neighboring node from the board 
		//solving problems and keeping track of trails + success problems
		while(true){
			neighbor = genNeighbor(current);
			if(neighbor.value >= current.value){
				trials++;
				if(current.value == 0){
					success++;
					if(debug)
						System.out.println(current.board.toString());
				}
				return current;
			}
			current = neighbor;
		}
	}
	
	//check for the neighboring node	
	private Node genNeighbor(Node current) {
		@SuppressWarnings("unused")
		Coords coords;
		Node copy = new Node(current);
		for(int i = 0; i < copy.board.getSize(); i++){
			for(int j = 0; j < copy.board.getSize(); j++){
				coords = new Coords(i,j);
				if(!copy.board.getTile(i, j).hasQueen()){
					this.board = copy.board;			//focus on board
					this.queens = copy.queens;			//get list of queens for that board
					removeQ(i);							//remove queen in row i
					placeQ(i,j);						//place queen in coord i,j
					copy.setValue(getAllAttackers());	//get new value
					boardQ.add(copy);					//enqueue copy node
					//System.out.println(copy.toString());
					copy = new Node(current);			//make new copy
				}
			}
		}
		Node best = boardQ.poll();
		return best;
	}
	
	//Minimum conflicts  Method algorithm
	//keep track of number of trails and solved successful problems
	public Node minConflicts(int maxSteps){
		trials++;
		Node current;
		boardTile variable;
		boardTile value;
		boolean conflicted = false;
		Random rand = new Random();
		current = new Node(board, getAllAttackers(), (ArrayList<Coords>) queens);
		for(int i = 0; i < maxSteps; i++){
			if(current.value == 0){
				success++;
				if(debug)
					System.out.println(board.toString());
				return current;
			}
			do{
				variable = board.getTile(queens.get(Math.abs(rand.nextInt()%queens.size())));
				if(getThreats(variable) > 0)
					conflicted = true;
				else
					conflicted = false;
			}while(!conflicted);
			value = minimize(variable);
			replaceQ(variable, value);
			variable = value;
			current.update(board, getAllAttackers(), (ArrayList<Coords>) queens);
			conflicted = false;
		}
		return current;
	}	
	
	//
	private boardTile minimize(boardTile variable) {
		List<boardTile> minimums = new ArrayList<boardTile>();
		int min = getAttackers(variable.getX(),variable.getY());
		Random rand = new Random();
		boardTile focus;
		for(int i = 0; i < board.getSize(); i++){
			focus = board.getTile(variable.getX(), i);
			if(i == variable.getY()){
				continue;
			}else if(getThreats(focus) < min){
				min = getThreats(focus);
				minimums = new ArrayList<boardTile>();
				minimums.add(focus);
			} else if (getThreats(focus) == min){
				minimums.add(focus);
			}
		}
		if(!minimums.isEmpty())
			return minimums.get(Math.abs(rand.nextInt()%minimums.size()));
		else
			return variable;
	}

	private int getThreats(boardTile variable) {
		int x; int y; int toReturn = 0;
		x = variable.getX();
		y = variable.getY();
		toReturn += getColAtkers(x,y);
		toReturn += getDiagAtkers(x,y);
		variable.setValue(toReturn);
		map.clear();
		return toReturn;
	}
	// Genetic Algorithm Method
	//keep track of number of trails and solved successful problems
	public geneticMethod geneticAlgorithm(int popSize, int boardSize, double mutation){
		trials++;
		List<geneticMethod> population;
		population = initPop(popSize, boardSize);
		population.sort(null);
		geneticMethod x; geneticMethod y; geneticMethod child;
		boolean satisfied = false;
		Random rand = new Random();
		double mutafactor;
		while(!satisfied){
			List<geneticMethod> newPopulation = new ArrayList<geneticMethod>(popSize);
			for(int i = 0; i < popSize; i++){
				x = randomSelect(population); //it will select random from x
				y = randomSelect(population); //it will select random from y
				child = reproduce(x,y);
				mutafactor = Math.abs(rand.nextDouble()%1);
				if(mutafactor < mutation)				
				child = mutate(child);
				newPopulation.add(child);
				newPopulation.sort(null);				
			}
			population = newPopulation;
			setFitnessValues((ArrayList<geneticMethod>) population);
			population.sort(null);
			
			if(population.get(0).getFriendlies() == (boardSize*(boardSize-1))/2)
				satisfied = true;
		}
		if(!debug)
			System.out.println(population.get(0).toString());
		if(debug)			
			System.out.println(population.get(0).getBoard().toString());
		return population.get(0);
	}
	
	private geneticMethod mutate(geneticMethod child) {
		Random rand = new Random();
		int chemicalX = Math.abs(rand.nextInt()%child.getSize());
		int placement = Math.abs(rand.nextInt()%child.getSize());
		geneticMethod toReturn;
		StringBuilder concoction = new StringBuilder(child.getStrain());
		concoction.replace(placement, placement+1, Integer.toString(chemicalX));
		toReturn = new geneticMethod(concoction.toString());
		toReturn.setFriendlies(evalFriendlies(toReturn.getBoard(),toReturn.getQueens()));
		return toReturn;
	}

	private geneticMethod reproduce(geneticMethod x, geneticMethod y) {
		int n = x.getStrain().length();
		Random rand = new Random();
		int c = Math.abs(rand.nextInt()%n);
		geneticMethod toReturn;
		StringBuilder splice = new StringBuilder("");
		splice.append(x.getStrain(),0,c);
		splice.append(y.getStrain(),c,n);
		toReturn = new geneticMethod(splice.toString());
		toReturn.setFriendlies(evalFriendlies(toReturn.getBoard(),toReturn.getQueens()));
		return toReturn;
	}

	private geneticMethod randomSelect(List<geneticMethod> population) {
		double sum = 0;
		double[] accFF = new double[population.size()];
		Random rand = new Random();
		double divine;
		int choice = -1;
		setFitnessValues((ArrayList<geneticMethod>) population);
		population.sort(null);
		for(int i = 0; i < population.size(); i++){
			sum += population.get(i).getFitness();
			accFF[i] = sum;			
		}
		divine = rand.nextDouble()%1;
		for(int i = 0; i < accFF.length; i++){
			if(divine < accFF[i]){
				choice = i;
				break;
			}
		}
		return population.get(choice);
	}

	private ArrayList<geneticMethod> initPop(int popSize, int boardSize){
		List<geneticMethod> population = new ArrayList<geneticMethod>(popSize);
		for(int i = 0; i < popSize; i++){
			makeBoard(boardSize);
			genSemiRandConfig();
			geneticMethod add = new geneticMethod(board);
			add.setFriendlies(evalFriendlies(add.getBoard(), add.getQueens()));
			population.add(add);
		}success++;
		setFitnessValues((ArrayList<geneticMethod>) population);
		return (ArrayList<geneticMethod>) population;
	}
	
	
	private void setFitnessValues(ArrayList<geneticMethod> population) {
		int sum = 0;
		for(geneticMethod geno : population){
			sum += geno.getFriendlies();
		}
		for(geneticMethod geno : population){
			geno.setFitness((double) geno.getFriendlies()/sum);
		}
		
	}

	//check to see if the queen can be place at "friendly" spot 
	//away from the hostile/attacker pieces
	private int evalFriendlies(chessBoard givenBoard, ArrayList<boardTile> givenList){
		map.clear();
		int friendlies = 0;
		for(int i = 0; i < givenList.size(); i++){
			for(int j = i+1; j < givenList.size(); j++){
				if(!isHostile(givenList.get(i),givenList.get(j),givenBoard.getSize()))
					friendlies++;
			}
		}
		return friendlies;
	}

	//checking the board for hostile pieces
	private boolean isHostile(boardTile tile, boardTile tile2, int size) {
		int x; int y;
		x = tile.getX();
		y = tile.getY();
		while(x > 0 | y > 0){ //go up left
			x--;
			y--;
		}
		while(x < size+1 | y < size+1){ //iterate down right
			if(tile2.getX() == x && tile2.getY() == y)
				return true;
			x++;
			y++;
		}
		x = tile.getX();
		y = tile.getY();
		while(x < size+1 | y > 0){ //go down left
			x++;
			y--;
		}
		while(x > 0 | y < size+1){//iterate up right
			if(tile2.getX() == x && tile2.getY() == y)
				return true;
			x--;
			y++;
		}
		x = tile.getX();
		y = tile.getY();
		for(int i = 0; i < size; i++){
			if(tile2.getX() == x && tile2.getY() == i)
				return true;
		}
		for(int i = 0; i < size; i++){
			if(tile2.getX() == i && tile2.getY() == y)
				return true;
		}
		return false;
	}

	//get average time
	public long getTime() {
		return this.avgTime;
	}
	
}
