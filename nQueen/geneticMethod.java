package nQueen;

import java.util.ArrayList;
import java.util.List;

//getters and setters for genetic method
public class geneticMethod implements Comparable<geneticMethod> {
	private chessBoard board;
	private String strain;
	private List<boardTile> queens; 
	private int friendlies;
	private double fitness;
	private int size;
	
	
	public geneticMethod(chessBoard board){
		this.board = board;
		this.size = board.getSize();
		this.strain = initStrain();
		this.queens = initQueens();
	}

	public geneticMethod(String strain){
		this.strain = strain;
		this.size = strain.length();
		this.board = initBoard();
		this.queens = initQueens();
	}
	
	public chessBoard getBoard(){
		return this.board;
	}
	
	public String getStrain(){
		return this.strain;
	}
	
	public ArrayList<boardTile> getQueens(){
		return (ArrayList<boardTile>) this.queens;
	}
	
	public int getFriendlies(){
		return this.friendlies;
	}
	
	public double getFitness(){
		return this.fitness;
	}
	
	public void setFriendlies(int friendlies){
		this.friendlies = friendlies;
	}
	
	public void setFitness(double fitness){
		this.fitness = fitness;
	}
	
	private chessBoard initBoard() {
		chessBoard toReturn = new chessBoard(strain.length());
		for(int i = 0; i < size; i++){
			toReturn.getTile(i,Character.getNumericValue(strain.charAt(i))).placeQ();
		}
		return toReturn;
	}

	private String initStrain(){
		StringBuilder toReturn = new StringBuilder("");
		for(int i = 0; i < size; i++){
			for(int j = 0; j < size; j++){
				if(board.getTile(i,j).hasQueen())
					toReturn.append(j);
			}
		}
		return toReturn.toString();
	}
	
	private ArrayList<boardTile> initQueens() {
		List<boardTile> toReturn = new ArrayList<boardTile>(board.getSize());
		int x; int y;
		for(int i = 0; i < size; i++){
			x = i;
			y = Character.getNumericValue(strain.charAt(i));
			toReturn.add(board.getTile(x,y));
		}
		return (ArrayList<boardTile>) toReturn;
	}
	
	public String toString(){
		return getStrain() + "\t" + friendlies + "\t" + fitness;
	}
	
	@Override
	public int compareTo(geneticMethod o) {
		return Double.compare(o.fitness, this.fitness);
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}
}
